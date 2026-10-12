package rtr.compat.lwjgl.input;

import rtr.compat.lwjgl.LWJGLException;

public class Controllers {
   public static void create() throws LWJGLException {
   }

   public static int getControllerCount() {
      return 0;
   }

   public static Controller getController(int index) {
      throw new IndexOutOfBoundsException("No controllers");
   }
}
