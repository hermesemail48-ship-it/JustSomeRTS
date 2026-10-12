package rtr.compat;

import com.badlogic.gdx.Input.Keys;
import rtr.compat.lwjgl.input.Keyboard;

/** libGDX key codes to LWJGL 2 key codes. */
public final class GdxKeyMap {
   private static final int[] map = new int[Keys.MAX_KEYCODE + 1];

   static {
      int[][] pairs = new int[][]{
         {Keys.A, Keyboard.KEY_A}, {Keys.B, Keyboard.KEY_B}, {Keys.C, Keyboard.KEY_C}, {Keys.D, Keyboard.KEY_D},
         {Keys.E, Keyboard.KEY_E}, {Keys.F, Keyboard.KEY_F}, {Keys.G, Keyboard.KEY_G}, {Keys.H, Keyboard.KEY_H},
         {Keys.I, Keyboard.KEY_I}, {Keys.J, Keyboard.KEY_J}, {Keys.K, Keyboard.KEY_K}, {Keys.L, Keyboard.KEY_L},
         {Keys.M, Keyboard.KEY_M}, {Keys.N, Keyboard.KEY_N}, {Keys.O, Keyboard.KEY_O}, {Keys.P, Keyboard.KEY_P},
         {Keys.Q, Keyboard.KEY_Q}, {Keys.R, Keyboard.KEY_R}, {Keys.S, Keyboard.KEY_S}, {Keys.T, Keyboard.KEY_T},
         {Keys.U, Keyboard.KEY_U}, {Keys.V, Keyboard.KEY_V}, {Keys.W, Keyboard.KEY_W}, {Keys.X, Keyboard.KEY_X},
         {Keys.Y, Keyboard.KEY_Y}, {Keys.Z, Keyboard.KEY_Z},
         {Keys.NUM_0, Keyboard.KEY_0}, {Keys.NUM_1, Keyboard.KEY_1}, {Keys.NUM_2, Keyboard.KEY_2}, {Keys.NUM_3, Keyboard.KEY_3},
         {Keys.NUM_4, Keyboard.KEY_4}, {Keys.NUM_5, Keyboard.KEY_5}, {Keys.NUM_6, Keyboard.KEY_6}, {Keys.NUM_7, Keyboard.KEY_7},
         {Keys.NUM_8, Keyboard.KEY_8}, {Keys.NUM_9, Keyboard.KEY_9},
         {Keys.F1, Keyboard.KEY_F1}, {Keys.F2, Keyboard.KEY_F2}, {Keys.F3, Keyboard.KEY_F3}, {Keys.F4, Keyboard.KEY_F4},
         {Keys.F5, Keyboard.KEY_F5}, {Keys.F6, Keyboard.KEY_F6}, {Keys.F7, Keyboard.KEY_F7}, {Keys.F8, Keyboard.KEY_F8},
         {Keys.F9, Keyboard.KEY_F9}, {Keys.F10, Keyboard.KEY_F10}, {Keys.F11, Keyboard.KEY_F11}, {Keys.F12, Keyboard.KEY_F12},
         {Keys.ESCAPE, Keyboard.KEY_ESCAPE}, {Keys.BACK, Keyboard.KEY_ESCAPE}, {Keys.ENTER, Keyboard.KEY_RETURN},
         {Keys.SPACE, Keyboard.KEY_SPACE}, {Keys.BACKSPACE, Keyboard.KEY_BACK}, {Keys.TAB, Keyboard.KEY_TAB},
         {Keys.SHIFT_LEFT, Keyboard.KEY_LSHIFT}, {Keys.SHIFT_RIGHT, Keyboard.KEY_RSHIFT},
         {Keys.CONTROL_LEFT, Keyboard.KEY_LCONTROL}, {Keys.CONTROL_RIGHT, Keyboard.KEY_RCONTROL},
         {Keys.ALT_LEFT, Keyboard.KEY_LMENU}, {Keys.ALT_RIGHT, Keyboard.KEY_RMENU},
         {Keys.UP, Keyboard.KEY_UP}, {Keys.DOWN, Keyboard.KEY_DOWN}, {Keys.LEFT, Keyboard.KEY_LEFT}, {Keys.RIGHT, Keyboard.KEY_RIGHT},
         {Keys.HOME, Keyboard.KEY_HOME}, {Keys.END, Keyboard.KEY_END}, {Keys.PAGE_UP, Keyboard.KEY_PRIOR},
         {Keys.PAGE_DOWN, Keyboard.KEY_NEXT}, {Keys.INSERT, Keyboard.KEY_INSERT}, {Keys.FORWARD_DEL, Keyboard.KEY_DELETE},
         {Keys.MINUS, Keyboard.KEY_MINUS}, {Keys.EQUALS, Keyboard.KEY_EQUALS}, {Keys.PLUS, Keyboard.KEY_EQUALS},
         {Keys.LEFT_BRACKET, Keyboard.KEY_LBRACKET}, {Keys.RIGHT_BRACKET, Keyboard.KEY_RBRACKET},
         {Keys.SEMICOLON, Keyboard.KEY_SEMICOLON}, {Keys.APOSTROPHE, Keyboard.KEY_APOSTROPHE}, {Keys.GRAVE, Keyboard.KEY_GRAVE},
         {Keys.BACKSLASH, Keyboard.KEY_BACKSLASH}, {Keys.COMMA, Keyboard.KEY_COMMA}, {Keys.PERIOD, Keyboard.KEY_PERIOD},
         {Keys.SLASH, Keyboard.KEY_SLASH}, {Keys.CAPS_LOCK, Keyboard.KEY_CAPITAL}, {Keys.PAUSE, Keyboard.KEY_PAUSE},
         {Keys.NUMPAD_0, Keyboard.KEY_NUMPAD0}, {Keys.NUMPAD_1, Keyboard.KEY_NUMPAD1}, {Keys.NUMPAD_2, Keyboard.KEY_NUMPAD2},
         {Keys.NUMPAD_3, Keyboard.KEY_NUMPAD3}, {Keys.NUMPAD_4, Keyboard.KEY_NUMPAD4}, {Keys.NUMPAD_5, Keyboard.KEY_NUMPAD5},
         {Keys.NUMPAD_6, Keyboard.KEY_NUMPAD6}, {Keys.NUMPAD_7, Keyboard.KEY_NUMPAD7}, {Keys.NUMPAD_8, Keyboard.KEY_NUMPAD8},
         {Keys.NUMPAD_9, Keyboard.KEY_NUMPAD9}, {Keys.NUMPAD_ADD, Keyboard.KEY_ADD}, {Keys.NUMPAD_SUBTRACT, Keyboard.KEY_SUBTRACT},
         {Keys.NUMPAD_MULTIPLY, Keyboard.KEY_MULTIPLY}, {Keys.NUMPAD_DIVIDE, Keyboard.KEY_DIVIDE},
         {Keys.NUMPAD_DOT, Keyboard.KEY_DECIMAL}, {Keys.NUMPAD_ENTER, Keyboard.KEY_NUMPADENTER}
      };

      for (int[] p : pairs) {
         map[p[0]] = p[1];
      }
   }

   private GdxKeyMap() {
   }

   public static int toLwjgl(int gdxKey) {
      return gdxKey >= 0 && gdxKey < map.length ? map[gdxKey] : Keyboard.KEY_NONE;
   }
}
