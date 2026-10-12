package rtr.compat.lwjgl.input;

import java.nio.IntBuffer;

/** LWJGL 2 Cursor stand-in: keeps the ARGB image (rows bottom-up, as LWJGL expects) for the platform to use. */
public class Cursor {
   public final int width;
   public final int height;
   public final int xHotspot;
   public final int yHotspot;
   public final int[] argb;
   private Object platformCursor;

   public Cursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws rtr.compat.lwjgl.LWJGLException {
      this.width = width;
      this.height = height;
      this.xHotspot = xHotspot;
      this.yHotspot = yHotspot;
      this.argb = new int[width * height];
      images.get(this.argb, 0, Math.min(this.argb.length, images.remaining()));
   }

   public static int getCapabilities() {
      return 0;
   }

   public static int getMinCursorSize() {
      return 1;
   }

   public static int getMaxCursorSize() {
      return 64;
   }

   public Object getPlatformCursor() {
      return this.platformCursor;
   }

   public void setPlatformCursor(Object c) {
      this.platformCursor = c;
   }

   public void destroy() {
   }
}
