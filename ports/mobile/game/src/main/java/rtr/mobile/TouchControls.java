package rtr.mobile;

import com.badlogic.gdx.Gdx;
import java.util.ArrayDeque;
import rtr.compat.GdxInputBridge;

/**
 * Maps touch gestures onto the mouse the game expects, without changing game code:
 * <ul>
 *   <li>tap: left click (the press is delayed a frame so hover-driven GUI code sees the pointer first)</li>
 *   <li>one-finger drag: left drag (zoning, brushes, placement)</li>
 *   <li>long press: right click (cancel / deselect)</li>
 *   <li>two-finger drag: right-button drag, which pans the map</li>
 *   <li>pinch: mouse-wheel notches, which zoom</li>
 * </ul>
 */
public class TouchControls implements GdxInputBridge.TouchTranslator {
   private static final long LONG_PRESS_MS = 450L;
   private static final float WHEEL_STEP_FRACTION = 0.08F;

   private enum Mode {
      IDLE,
      PENDING,
      LEFT_DRAG,
      TWO_FINGER,
      WAIT_RELEASE
   }

   private Mode mode = Mode.IDLE;
   private final int[] px = new int[2];
   private final int[] py = new int[2];
   private final boolean[] down = new boolean[2];
   private int startX;
   private int startY;
   private long startTime;
   private float pinchStart;
   private int wheelSteps;
   private int frame;
   private final ArrayDeque<int[]> scheduled = new ArrayDeque<int[]>();

   private int slop() {
      return Math.max(8, Gdx.graphics.getHeight() / 60);
   }

   private static float dist(int[] x, int[] y) {
      float dx = x[0] - x[1];
      float dy = y[0] - y[1];
      return (float)Math.sqrt(dx * dx + dy * dy);
   }

   /** Queue: {frame, button, down(1/0), screenX, screenY}. */
   private void schedule(int delay, int button, boolean pressed, int sx, int sy) {
      this.scheduled.add(new int[]{this.frame + delay, button, pressed ? 1 : 0, sx, sy});
   }

   @Override
   public boolean touchDown(int x, int y, int pointer, GdxInputBridge bridge) {
      if (pointer > 1) {
         return true;
      }

      this.px[pointer] = x;
      this.py[pointer] = y;
      this.down[pointer] = true;
      if (pointer == 0 && this.mode == Mode.IDLE) {
         this.mode = Mode.PENDING;
         this.startX = x;
         this.startY = y;
         this.startTime = System.currentTimeMillis();
         bridge.mouseMove(bridge.toGameX(x), bridge.toLwjglY(y));
      } else if (pointer == 1 && this.down[0]) {
         if (this.mode == Mode.LEFT_DRAG) {
            bridge.mouseButton(0, false, bridge.toGameX(this.px[0]), bridge.toLwjglY(this.py[0]));
         }

         if (this.mode == Mode.PENDING || this.mode == Mode.LEFT_DRAG) {
            this.mode = Mode.TWO_FINGER;
            int mx = (this.px[0] + this.px[1]) / 2;
            int my = (this.py[0] + this.py[1]) / 2;
            bridge.mouseMove(bridge.toGameX(mx), bridge.toLwjglY(my));
            bridge.mouseButton(1, true, bridge.toGameX(mx), bridge.toLwjglY(my));
            this.pinchStart = dist(this.px, this.py);
            this.wheelSteps = 0;
         }
      }

      return true;
   }

   @Override
   public boolean touchDragged(int x, int y, int pointer, GdxInputBridge bridge) {
      if (pointer > 1) {
         return true;
      }

      this.px[pointer] = x;
      this.py[pointer] = y;
      switch (this.mode) {
         case PENDING:
            if (Math.abs(x - this.startX) > this.slop() || Math.abs(y - this.startY) > this.slop()) {
               this.mode = Mode.LEFT_DRAG;
               bridge.mouseButton(0, true, bridge.toGameX(this.startX), bridge.toLwjglY(this.startY));
               bridge.mouseMove(bridge.toGameX(x), bridge.toLwjglY(y));
            }
            break;
         case LEFT_DRAG:
            bridge.mouseMove(bridge.toGameX(x), bridge.toLwjglY(y));
            break;
         case TWO_FINGER:
            int mx = (this.px[0] + this.px[1]) / 2;
            int my = (this.py[0] + this.py[1]) / 2;
            bridge.mouseMove(bridge.toGameX(mx), bridge.toLwjglY(my));
            float step = Gdx.graphics.getHeight() * WHEEL_STEP_FRACTION;
            int steps = (int)((dist(this.px, this.py) - this.pinchStart) / step);

            while (this.wheelSteps < steps) {
               this.wheelSteps++;
               bridge.mouseWheel(120);
            }

            while (this.wheelSteps > steps) {
               this.wheelSteps--;
               bridge.mouseWheel(-120);
            }
         default:
            break;
      }

      return true;
   }

   @Override
   public boolean touchUp(int x, int y, int pointer, GdxInputBridge bridge) {
      if (pointer > 1) {
         return true;
      }

      this.px[pointer] = x;
      this.py[pointer] = y;
      this.down[pointer] = false;
      switch (this.mode) {
         case PENDING:
            this.schedule(1, 0, true, x, y);
            this.schedule(2, 0, false, x, y);
            this.mode = Mode.IDLE;
            break;
         case LEFT_DRAG:
            bridge.mouseButton(0, false, bridge.toGameX(x), bridge.toLwjglY(y));
            this.mode = Mode.IDLE;
            break;
         case TWO_FINGER:
            int mx = (this.px[0] + this.px[1]) / 2;
            int my = (this.py[0] + this.py[1]) / 2;
            bridge.mouseButton(1, false, bridge.toGameX(mx), bridge.toLwjglY(my));
            this.mode = this.down[0] || this.down[1] ? Mode.WAIT_RELEASE : Mode.IDLE;
            break;
         case WAIT_RELEASE:
            if (!this.down[0] && !this.down[1]) {
               this.mode = Mode.IDLE;
            }
         default:
            break;
      }

      return true;
   }

   @Override
   public void update(GdxInputBridge bridge) {
      this.frame++;
      if (this.mode == Mode.PENDING && System.currentTimeMillis() - this.startTime > LONG_PRESS_MS) {
         this.mode = Mode.WAIT_RELEASE;
         Gdx.input.vibrate(25);
         this.schedule(0, 1, true, this.startX, this.startY);
         this.schedule(1, 1, false, this.startX, this.startY);
      }

      while (!this.scheduled.isEmpty() && this.scheduled.peek()[0] <= this.frame) {
         int[] e = this.scheduled.poll();
         bridge.mouseMove(bridge.toGameX(e[3]), bridge.toLwjglY(e[4]));
         bridge.mouseButton(e[1], e[2] == 1, bridge.toGameX(e[3]), bridge.toLwjglY(e[4]));
      }
   }
}
