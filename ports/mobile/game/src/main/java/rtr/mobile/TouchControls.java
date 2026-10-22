package rtr.mobile;

import com.badlogic.gdx.Gdx;
import java.util.ArrayDeque;
import rtr.compat.GdxInputBridge;

/**
 * Maps touch gestures onto the mouse the game expects:
 * <ul>
 *   <li>tap: left click (the press is delayed a frame so hover-driven GUI code sees the pointer first)</li>
 *   <li>one-finger drag on the map: pans (the ground follows the finger)</li>
 *   <li>one-finger drag on the game's panels, or with a painting tool (walls, roads, harvest...): left drag</li>
 *   <li>long press: right click (cancel / deselect)</li>
 *   <li>two fingers: pinch zooms (mouse-wheel notches) and the pair pans</li>
 * </ul>
 * While a building is being placed: dragging the ghost moves it, tapping the ghost builds it, tapping elsewhere moves
 * it there, and dragging elsewhere pans. The overlay's X button ({@link MobileHud}) cancels.
 */
public class TouchControls implements GdxInputBridge.TouchTranslator, MobileHud.ToolListener {
   private static final long LONG_PRESS_MS = 450L;
   private static final float WHEEL_STEP_FRACTION = 0.08F;

   private enum Mode {
      IDLE,
      PENDING,
      LEFT_DRAG,
      PAN,
      AIM,
      HUD,
      TWO_FINGER,
      WAIT_RELEASE
   }

   private Mode mode = Mode.IDLE;
   private final MobileHud hud;
   private final int[] px = new int[2];
   private final int[] py = new int[2];
   private final boolean[] down = new boolean[2];
   private int startX;
   private int startY;
   private long startTime;
   /** What a drag from the current PENDING touch becomes (PAN or LEFT_DRAG). */
   private Mode dragMode;
   /** The current PENDING touch started on the building ghost (a tap builds instead of moving it). */
   private boolean onGhost;
   private int lastX;
   private int lastY;
   private float pinchStart;
   private int wheelSteps;
   private int frame;
   /** Where the building ghost was last aimed (window units). */
   private int aimX = -1;
   private int aimY = -1;
   /** Ghost position when the current aiming touch started (drags move it relative to the finger). */
   private int aimBaseX;
   private int aimBaseY;
   private final ArrayDeque<int[]> scheduled = new ArrayDeque<int[]>();

   public TouchControls(MobileHud hud) {
      this.hud = hud;
      if (hud != null) {
         hud.setListener(this);
      }
   }

   private int slop() {
      return Math.max(8, Gdx.graphics.getHeight() / 60);
   }

   private boolean moved(int x, int y) {
      return Math.abs(x - this.startX) > this.slop() || Math.abs(y - this.startY) > this.slop();
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

   private void click(int sx, int sy) {
      this.schedule(1, 0, true, sx, sy);
      this.schedule(2, 0, false, sx, sy);
   }

   private static int displayY(int y, GdxInputBridge bridge) {
      return rtr.compat.lwjgl.opengl.Display.getHeight() - 1 - bridge.toLwjglY(y);
   }

   private static boolean guiAt(int x, int y, GdxInputBridge bridge) {
      return GameBridge.isGuiAt(bridge.toGameX(x), displayY(y, bridge));
   }

   private void pan(int x, int y, GdxInputBridge bridge) {
      GameBridge.pan(bridge.toGameX(x) - bridge.toGameX(this.lastX), displayY(y, bridge) - displayY(this.lastY, bridge));
      this.lastX = x;
      this.lastY = y;
   }

   private void aimAt(int x, int y, GdxInputBridge bridge) {
      this.aimX = Math.max(0, Math.min(Gdx.graphics.getWidth() - 1, x));
      this.aimY = Math.max(0, Math.min(Gdx.graphics.getHeight() - 1, y));
      bridge.mouseMove(bridge.toGameX(this.aimX), bridge.toLwjglY(this.aimY));
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
         if (this.hud != null && this.hud.touchDown(x, y)) {
            this.mode = Mode.HUD;
            return true;
         }

         this.startX = x;
         this.startY = y;
         this.lastX = x;
         this.lastY = y;
         this.startTime = System.currentTimeMillis();
         this.onGhost = false;
         boolean gui = guiAt(x, y, bridge);
         if (!gui && GameBridge.isPlacingBuilding() && this.aimX >= 0 && this.hud.isOnGhost(x, y)) {
            // Grabbed the ghost: a drag carries it (keeping the finger's offset, so the finger never hides it), a tap
            // builds it.
            this.mode = Mode.AIM;
            this.onGhost = true;
            this.aimBaseX = this.aimX;
            this.aimBaseY = this.aimY;
         } else {
            this.mode = Mode.PENDING;
            this.dragMode = gui || GameBridge.isUsingTool() && !GameBridge.isPlacingBuilding() ? Mode.LEFT_DRAG : Mode.PAN;
            if (!GameBridge.isPlacingBuilding()) {
               bridge.mouseMove(bridge.toGameX(x), bridge.toLwjglY(y));
            }
         }
      } else if (pointer == 1 && this.down[0]) {
         if (this.mode == Mode.LEFT_DRAG) {
            bridge.mouseButton(0, false, bridge.toGameX(this.px[0]), bridge.toLwjglY(this.py[0]));
         }

         if (this.mode == Mode.PENDING || this.mode == Mode.LEFT_DRAG || this.mode == Mode.PAN || this.mode == Mode.AIM) {
            this.mode = Mode.TWO_FINGER;
            this.lastX = (this.px[0] + this.px[1]) / 2;
            this.lastY = (this.py[0] + this.py[1]) / 2;
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
         case HUD:
            this.hud.touchDragged(x, y);
            break;
         case AIM:
            if (this.onGhost && this.moved(x, y)) {
               this.onGhost = false;
            }

            if (!this.onGhost) {
               this.aimAt(this.aimBaseX + x - this.startX, this.aimBaseY + y - this.startY, bridge);
            }
            break;
         case PENDING:
            if (this.moved(x, y)) {
               this.mode = this.dragMode;
               if (this.mode == Mode.LEFT_DRAG) {
                  bridge.mouseButton(0, true, bridge.toGameX(this.startX), bridge.toLwjglY(this.startY));
                  bridge.mouseMove(bridge.toGameX(x), bridge.toLwjglY(y));
               } else {
                  this.pan(x, y, bridge);
               }
            }
            break;
         case LEFT_DRAG:
            bridge.mouseMove(bridge.toGameX(x), bridge.toLwjglY(y));
            break;
         case PAN:
            this.pan(x, y, bridge);
            break;
         case TWO_FINGER:
            this.pan((this.px[0] + this.px[1]) / 2, (this.py[0] + this.py[1]) / 2, bridge);
            float step = Gdx.graphics.getHeight() * WHEEL_STEP_FRACTION;
            int steps = (int)((dist(this.px, this.py) - this.pinchStart) / step);
            if (steps != this.wheelSteps && !GameBridge.isPlacingBuilding()) {
               // Zoom about the fingers (the game zooms around the pointer).
               bridge.mouseMove(bridge.toGameX(this.lastX), bridge.toLwjglY(this.lastY));
            }

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
         case HUD:
            this.mode = Mode.IDLE;
            this.hud.touchUp(x, y);
            break;
         case AIM:
            if (this.onGhost) {
               // Tapped the ghost: build it where it stands.
               this.confirmPlacement();
            }

            this.mode = Mode.IDLE;
            break;
         case PENDING:
            if (GameBridge.isPlacingBuilding() && !guiAt(x, y, bridge)) {
               // A tap on open ground moves the ghost there.
               this.aimAt(x, y, bridge);
            } else {
               this.click(x, y);
            }

            this.mode = Mode.IDLE;
            break;
         case LEFT_DRAG:
            bridge.mouseButton(0, false, bridge.toGameX(x), bridge.toLwjglY(y));
            this.mode = Mode.IDLE;
            break;
         case PAN:
            this.mode = Mode.IDLE;
            break;
         case TWO_FINGER:
            this.mode = this.down[0] || this.down[1] ? Mode.WAIT_RELEASE : Mode.IDLE;
            if (this.aimX >= 0 && GameBridge.isPlacingBuilding()) {
               // Zooming may have moved the pointer; put it back on the ghost.
               bridge.mouseMove(bridge.toGameX(this.aimX), bridge.toLwjglY(this.aimY));
            }
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
   public void confirmPlacement() {
      if (this.aimX >= 0 && GameBridge.canPlaceHere()) {
         this.click(this.aimX, this.aimY);
         Gdx.input.vibrate(15);
      } else {
         this.hud.flashBlocked();
         Gdx.input.vibrate(60);
      }
   }

   @Override
   public void cancelTool() {
      GameBridge.cancelTool();
      this.aimX = -1;
      this.aimY = -1;
   }

   @Override
   public void update(GdxInputBridge bridge) {
      this.frame++;
      if (this.mode == Mode.PENDING && System.currentTimeMillis() - this.startTime > LONG_PRESS_MS) {
         this.mode = Mode.WAIT_RELEASE;
         Gdx.input.vibrate(25);
         bridge.mouseMove(bridge.toGameX(this.startX), bridge.toLwjglY(this.startY));
         this.schedule(0, 1, true, this.startX, this.startY);
         this.schedule(1, 1, false, this.startX, this.startY);
      }

      if (!GameBridge.isPlacingBuilding()) {
         this.aimX = -1;
         this.aimY = -1;
      } else if (this.aimX < 0 && this.mode == Mode.IDLE && this.scheduled.isEmpty()) {
         // Just picked a building: show its ghost mid-screen, ready to drag or tap.
         this.aimAt(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 2 / 5, bridge);
      }

      while (!this.scheduled.isEmpty() && this.scheduled.peek()[0] <= this.frame) {
         int[] e = this.scheduled.poll();
         bridge.mouseMove(bridge.toGameX(e[3]), bridge.toLwjglY(e[4]));
         bridge.mouseButton(e[1], e[2] == 1, bridge.toGameX(e[3]), bridge.toLwjglY(e[4]));
      }
   }
}
