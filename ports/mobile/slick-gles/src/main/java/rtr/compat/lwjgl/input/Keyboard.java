package rtr.compat.lwjgl.input;

import java.util.HashMap;
import java.util.Map;

/**
 * LWJGL 2 Keyboard emulation. Codes and names are generated from the real LWJGL 2 class
 * (scripts: work/gen/GenKeyboard.java) so stored key bindings stay compatible.
 * Events are pushed by {@link rtr.compat.GdxInputBridge}.
 */
public class Keyboard {
   public static final int CHAR_NONE = 0;
   public static final int KEYBOARD_SIZE = 256;
   public static final int KEY_NONE = 0;
   public static final int KEY_ESCAPE = 1;
   public static final int KEY_1 = 2;
   public static final int KEY_2 = 3;
   public static final int KEY_3 = 4;
   public static final int KEY_4 = 5;
   public static final int KEY_5 = 6;
   public static final int KEY_6 = 7;
   public static final int KEY_7 = 8;
   public static final int KEY_8 = 9;
   public static final int KEY_9 = 10;
   public static final int KEY_0 = 11;
   public static final int KEY_MINUS = 12;
   public static final int KEY_EQUALS = 13;
   public static final int KEY_BACK = 14;
   public static final int KEY_TAB = 15;
   public static final int KEY_Q = 16;
   public static final int KEY_W = 17;
   public static final int KEY_E = 18;
   public static final int KEY_R = 19;
   public static final int KEY_T = 20;
   public static final int KEY_Y = 21;
   public static final int KEY_U = 22;
   public static final int KEY_I = 23;
   public static final int KEY_O = 24;
   public static final int KEY_P = 25;
   public static final int KEY_LBRACKET = 26;
   public static final int KEY_RBRACKET = 27;
   public static final int KEY_RETURN = 28;
   public static final int KEY_LCONTROL = 29;
   public static final int KEY_A = 30;
   public static final int KEY_S = 31;
   public static final int KEY_D = 32;
   public static final int KEY_F = 33;
   public static final int KEY_G = 34;
   public static final int KEY_H = 35;
   public static final int KEY_J = 36;
   public static final int KEY_K = 37;
   public static final int KEY_L = 38;
   public static final int KEY_SEMICOLON = 39;
   public static final int KEY_APOSTROPHE = 40;
   public static final int KEY_GRAVE = 41;
   public static final int KEY_LSHIFT = 42;
   public static final int KEY_BACKSLASH = 43;
   public static final int KEY_Z = 44;
   public static final int KEY_X = 45;
   public static final int KEY_C = 46;
   public static final int KEY_V = 47;
   public static final int KEY_B = 48;
   public static final int KEY_N = 49;
   public static final int KEY_M = 50;
   public static final int KEY_COMMA = 51;
   public static final int KEY_PERIOD = 52;
   public static final int KEY_SLASH = 53;
   public static final int KEY_RSHIFT = 54;
   public static final int KEY_MULTIPLY = 55;
   public static final int KEY_LMENU = 56;
   public static final int KEY_SPACE = 57;
   public static final int KEY_CAPITAL = 58;
   public static final int KEY_F1 = 59;
   public static final int KEY_F2 = 60;
   public static final int KEY_F3 = 61;
   public static final int KEY_F4 = 62;
   public static final int KEY_F5 = 63;
   public static final int KEY_F6 = 64;
   public static final int KEY_F7 = 65;
   public static final int KEY_F8 = 66;
   public static final int KEY_F9 = 67;
   public static final int KEY_F10 = 68;
   public static final int KEY_NUMLOCK = 69;
   public static final int KEY_SCROLL = 70;
   public static final int KEY_NUMPAD7 = 71;
   public static final int KEY_NUMPAD8 = 72;
   public static final int KEY_NUMPAD9 = 73;
   public static final int KEY_SUBTRACT = 74;
   public static final int KEY_NUMPAD4 = 75;
   public static final int KEY_NUMPAD5 = 76;
   public static final int KEY_NUMPAD6 = 77;
   public static final int KEY_ADD = 78;
   public static final int KEY_NUMPAD1 = 79;
   public static final int KEY_NUMPAD2 = 80;
   public static final int KEY_NUMPAD3 = 81;
   public static final int KEY_NUMPAD0 = 82;
   public static final int KEY_DECIMAL = 83;
   public static final int KEY_F11 = 87;
   public static final int KEY_F12 = 88;
   public static final int KEY_F13 = 100;
   public static final int KEY_F14 = 101;
   public static final int KEY_F15 = 102;
   public static final int KEY_F16 = 103;
   public static final int KEY_F17 = 104;
   public static final int KEY_F18 = 105;
   public static final int KEY_KANA = 112;
   public static final int KEY_F19 = 113;
   public static final int KEY_CONVERT = 121;
   public static final int KEY_NOCONVERT = 123;
   public static final int KEY_YEN = 125;
   public static final int KEY_NUMPADEQUALS = 141;
   public static final int KEY_CIRCUMFLEX = 144;
   public static final int KEY_AT = 145;
   public static final int KEY_COLON = 146;
   public static final int KEY_UNDERLINE = 147;
   public static final int KEY_KANJI = 148;
   public static final int KEY_STOP = 149;
   public static final int KEY_AX = 150;
   public static final int KEY_UNLABELED = 151;
   public static final int KEY_NUMPADENTER = 156;
   public static final int KEY_RCONTROL = 157;
   public static final int KEY_SECTION = 167;
   public static final int KEY_NUMPADCOMMA = 179;
   public static final int KEY_DIVIDE = 181;
   public static final int KEY_SYSRQ = 183;
   public static final int KEY_RMENU = 184;
   public static final int KEY_FUNCTION = 196;
   public static final int KEY_PAUSE = 197;
   public static final int KEY_HOME = 199;
   public static final int KEY_UP = 200;
   public static final int KEY_PRIOR = 201;
   public static final int KEY_LEFT = 203;
   public static final int KEY_RIGHT = 205;
   public static final int KEY_END = 207;
   public static final int KEY_DOWN = 208;
   public static final int KEY_NEXT = 209;
   public static final int KEY_INSERT = 210;
   public static final int KEY_DELETE = 211;
   public static final int KEY_CLEAR = 218;
   public static final int KEY_LMETA = 219;
   public static final int KEY_LWIN = 219;
   public static final int KEY_RMETA = 220;
   public static final int KEY_RWIN = 220;
   public static final int KEY_APPS = 221;
   public static final int KEY_POWER = 222;
   public static final int KEY_SLEEP = 223;

   private static final String[] keyName = new String[KEYBOARD_SIZE];
   private static final Map<String, Integer> keyMap = new HashMap<String, Integer>(253);
   private static final boolean[] down = new boolean[KEYBOARD_SIZE];
   private static final int QUEUE_SIZE = 256;
   private static final int[] queueKey = new int[QUEUE_SIZE];
   private static final char[] queueChar = new char[QUEUE_SIZE];
   private static final boolean[] queueState = new boolean[QUEUE_SIZE];
   private static final boolean[] queueRepeat = new boolean[QUEUE_SIZE];
   private static int head;
   private static int tail;
   private static int eventKey;
   private static char eventChar;
   private static boolean eventState;
   private static boolean eventRepeat;
   private static boolean repeatEnabled;

   static {
      register("NONE", 0);
      register("ESCAPE", 1);
      register("1", 2);
      register("2", 3);
      register("3", 4);
      register("4", 5);
      register("5", 6);
      register("6", 7);
      register("7", 8);
      register("8", 9);
      register("9", 10);
      register("0", 11);
      register("MINUS", 12);
      register("EQUALS", 13);
      register("BACK", 14);
      register("TAB", 15);
      register("Q", 16);
      register("W", 17);
      register("E", 18);
      register("R", 19);
      register("T", 20);
      register("Y", 21);
      register("U", 22);
      register("I", 23);
      register("O", 24);
      register("P", 25);
      register("LBRACKET", 26);
      register("RBRACKET", 27);
      register("RETURN", 28);
      register("LCONTROL", 29);
      register("A", 30);
      register("S", 31);
      register("D", 32);
      register("F", 33);
      register("G", 34);
      register("H", 35);
      register("J", 36);
      register("K", 37);
      register("L", 38);
      register("SEMICOLON", 39);
      register("APOSTROPHE", 40);
      register("GRAVE", 41);
      register("LSHIFT", 42);
      register("BACKSLASH", 43);
      register("Z", 44);
      register("X", 45);
      register("C", 46);
      register("V", 47);
      register("B", 48);
      register("N", 49);
      register("M", 50);
      register("COMMA", 51);
      register("PERIOD", 52);
      register("SLASH", 53);
      register("RSHIFT", 54);
      register("MULTIPLY", 55);
      register("LMENU", 56);
      register("SPACE", 57);
      register("CAPITAL", 58);
      register("F1", 59);
      register("F2", 60);
      register("F3", 61);
      register("F4", 62);
      register("F5", 63);
      register("F6", 64);
      register("F7", 65);
      register("F8", 66);
      register("F9", 67);
      register("F10", 68);
      register("NUMLOCK", 69);
      register("SCROLL", 70);
      register("NUMPAD7", 71);
      register("NUMPAD8", 72);
      register("NUMPAD9", 73);
      register("SUBTRACT", 74);
      register("NUMPAD4", 75);
      register("NUMPAD5", 76);
      register("NUMPAD6", 77);
      register("ADD", 78);
      register("NUMPAD1", 79);
      register("NUMPAD2", 80);
      register("NUMPAD3", 81);
      register("NUMPAD0", 82);
      register("DECIMAL", 83);
      register("F11", 87);
      register("F12", 88);
      register("F13", 100);
      register("F14", 101);
      register("F15", 102);
      register("F16", 103);
      register("F17", 104);
      register("F18", 105);
      register("KANA", 112);
      register("F19", 113);
      register("CONVERT", 121);
      register("NOCONVERT", 123);
      register("YEN", 125);
      register("NUMPADEQUALS", 141);
      register("CIRCUMFLEX", 144);
      register("AT", 145);
      register("COLON", 146);
      register("UNDERLINE", 147);
      register("KANJI", 148);
      register("STOP", 149);
      register("AX", 150);
      register("UNLABELED", 151);
      register("NUMPADENTER", 156);
      register("RCONTROL", 157);
      register("SECTION", 167);
      register("NUMPADCOMMA", 179);
      register("DIVIDE", 181);
      register("SYSRQ", 183);
      register("RMENU", 184);
      register("FUNCTION", 196);
      register("PAUSE", 197);
      register("HOME", 199);
      register("UP", 200);
      register("PRIOR", 201);
      register("LEFT", 203);
      register("RIGHT", 205);
      register("END", 207);
      register("DOWN", 208);
      register("NEXT", 209);
      register("INSERT", 210);
      register("DELETE", 211);
      register("CLEAR", 218);
      register("LMETA", 219);
      register("LWIN", 219);
      register("RMETA", 220);
      register("RWIN", 220);
      register("APPS", 221);
      register("POWER", 222);
      register("SLEEP", 223);
   }

   private static void register(String name, int code) {
      keyName[code] = name;
      keyMap.put(name, code);
   }

   public static String getKeyName(int key) {
      return key >= 0 && key < KEYBOARD_SIZE ? keyName[key] : null;
   }

   public static synchronized int getKeyIndex(String name) {
      Integer code = keyMap.get(name);
      return code == null ? KEY_NONE : code;
   }

   public static synchronized boolean isKeyDown(int key) {
      return key >= 0 && key < KEYBOARD_SIZE && down[key];
   }

   public static boolean isCreated() {
      return true;
   }

   public static void enableRepeatEvents(boolean enable) {
      repeatEnabled = enable;
   }

   public static boolean areRepeatEventsEnabled() {
      return repeatEnabled;
   }

   public static synchronized boolean next() {
      if (head == tail) {
         return false;
      }

      eventKey = queueKey[head];
      eventChar = queueChar[head];
      eventState = queueState[head];
      eventRepeat = queueRepeat[head];
      head = (head + 1) % QUEUE_SIZE;
      return true;
   }

   public static int getEventKey() {
      return eventKey;
   }

   public static char getEventCharacter() {
      return eventChar;
   }

   public static boolean getEventKeyState() {
      return eventState;
   }

   public static boolean isRepeatEvent() {
      return eventRepeat;
   }

   public static int getNumKeyboardEvents() {
      return (tail - head + QUEUE_SIZE) % QUEUE_SIZE;
   }

   /** Queues a key event (called by the input bridge on the render thread). */
   public static synchronized void push(int key, char character, boolean pressed, boolean repeat) {
      if (key < 0 || key >= KEYBOARD_SIZE) {
         return;
      }

      if (!repeat) {
         down[key] = pressed;
      }

      int next = (tail + 1) % QUEUE_SIZE;
      if (next == head) {
         return;
      }

      queueKey[tail] = key;
      queueChar[tail] = character;
      queueState[tail] = pressed;
      queueRepeat[tail] = repeat;
      tail = next;
   }

   /** Attaches a typed character to the most recent queued press of {@code key}, as LWJGL reports both together. */
   public static synchronized boolean attachCharacter(char character) {
      if (head == tail) {
         return false;
      }

      int last = (tail - 1 + QUEUE_SIZE) % QUEUE_SIZE;
      if (queueState[last] && queueChar[last] == CHAR_NONE) {
         queueChar[last] = character;
         return true;
      }

      return false;
   }

   public static synchronized void releaseAll() {
      for (int i = 0; i < KEYBOARD_SIZE; i++) {
         if (down[i]) {
            push(i, (char)CHAR_NONE, false, false);
         }
      }
   }
}
