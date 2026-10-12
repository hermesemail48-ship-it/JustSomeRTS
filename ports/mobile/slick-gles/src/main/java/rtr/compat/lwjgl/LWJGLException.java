package rtr.compat.lwjgl;

public class LWJGLException extends Exception {
   public LWJGLException() {
   }

   public LWJGLException(String msg) {
      super(msg);
   }

   public LWJGLException(String msg, Throwable cause) {
      super(msg, cause);
   }

   public LWJGLException(Throwable cause) {
      super(cause);
   }
}
