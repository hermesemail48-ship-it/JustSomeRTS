package rtr.compat.lwjgl.input;

/**
 * LWJGL 2 Mouse emulation (bottom-left origin, like LWJGL). Events are pushed by {@link rtr.compat.GdxInputBridge}.
 */
public class Mouse {
   private static final int QUEUE_SIZE = 512;
   private static final int[] qButton = new int[QUEUE_SIZE];
   private static final boolean[] qState = new boolean[QUEUE_SIZE];
   private static final int[] qX = new int[QUEUE_SIZE];
   private static final int[] qY = new int[QUEUE_SIZE];
   private static final int[] qDX = new int[QUEUE_SIZE];
   private static final int[] qDY = new int[QUEUE_SIZE];
   private static final int[] qWheel = new int[QUEUE_SIZE];
   private static int head;
   private static int tail;
   private static final boolean[] buttons = new boolean[16];
   private static int x;
   private static int y;
   private static int eButton = -1;
   private static boolean eState;
   private static int eX;
   private static int eY;
   private static int eDX;
   private static int eDY;
   private static int eWheel;
   private static boolean grabbed;
   private static Cursor nativeCursor;
   private static CursorHandler cursorHandler;

   public static boolean isCreated() {
      return true;
   }

   public static synchronized int getX() {
      return x;
   }

   public static synchronized int getY() {
      return y;
   }

   public static synchronized boolean isButtonDown(int button) {
      return button >= 0 && button < buttons.length && buttons[button];
   }

   public static int getButtonCount() {
      return 3;
   }

   public static boolean isGrabbed() {
      return grabbed;
   }

   public static void setGrabbed(boolean grab) {
      grabbed = grab;
   }

   public static boolean isInsideWindow() {
      return true;
   }

   public static synchronized void setCursorPosition(int newX, int newY) {
      x = newX;
      y = newY;
   }

   public static Cursor setNativeCursor(Cursor cursor) throws rtr.compat.lwjgl.LWJGLException {
      Cursor old = nativeCursor;
      nativeCursor = cursor;
      if (cursorHandler != null) {
         cursorHandler.setCursor(cursor);
      }

      return old;
   }

   public static Cursor getNativeCursor() {
      return nativeCursor;
   }

   public static void setCursorHandler(CursorHandler handler) {
      cursorHandler = handler;
   }

   /** Buttons pressed by an event consumed since {@link #beginFrame()}: their release waits for the next frame. */
   private static final boolean[] pressedThisFrame = new boolean[16];

   /** Called once per frame before the events are read (Slick's Input.poll). */
   public static synchronized void beginFrame() {
      java.util.Arrays.fill(pressedThisFrame, false);
   }

   /**
    * Next queued event. Button state changes as events are consumed, and a release whose press was consumed in the
    * same frame is held back until the next frame, so a quick click (press and release between two frames, common at
    * low frame rates) is still seen as held for one frame by code that polls isButtonDown.
    */
   public static synchronized boolean next() {
      if (head == tail) {
         return false;
      }

      int b = qButton[head];
      if (b >= 0 && b < buttons.length) {
         if (!qState[head] && pressedThisFrame[b]) {
            return false;
         }

         buttons[b] = qState[head];
         if (qState[head]) {
            pressedThisFrame[b] = true;
         }
      }

      eButton = qButton[head];
      eState = qState[head];
      eX = qX[head];
      eY = qY[head];
      eDX = qDX[head];
      eDY = qDY[head];
      eWheel = qWheel[head];
      head = (head + 1) % QUEUE_SIZE;
      return true;
   }

   public static int getEventButton() {
      return eButton;
   }

   public static boolean getEventButtonState() {
      return eState;
   }

   public static int getEventX() {
      return eX;
   }

   public static int getEventY() {
      return eY;
   }

   public static int getEventDX() {
      return eDX;
   }

   public static int getEventDY() {
      return eDY;
   }

   public static int getEventDWheel() {
      return eWheel;
   }

   /** Queues an event in LWJGL coordinates (y up). button = -1 for move/wheel events. */
   public static synchronized void push(int button, boolean state, int newX, int newY, int wheel) {
      int dx = newX - x;
      int dy = newY - y;
      x = newX;
      y = newY;
      int next = (tail + 1) % QUEUE_SIZE;
      if (next == head) {
         return;
      }

      qButton[tail] = button;
      qState[tail] = state;
      qX[tail] = newX;
      qY[tail] = newY;
      qDX[tail] = dx;
      qDY[tail] = dy;
      qWheel[tail] = wheel;
      tail = next;
   }

   public interface CursorHandler {
      void setCursor(Cursor cursor);
   }
}
