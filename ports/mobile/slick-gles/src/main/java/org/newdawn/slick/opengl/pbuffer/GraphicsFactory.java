package org.newdawn.slick.opengl.pbuffer;

import java.util.HashMap;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/** Hands out one off-screen Graphics per Image, always FBO-backed on GLES. */
public class GraphicsFactory {
   private static final HashMap<Image, Graphics> graphics = new HashMap<Image, Graphics>();

   public static void setUseFBO(boolean useFBO) {
   }

   public static boolean usingFBO() {
      return true;
   }

   public static boolean usingPBuffer() {
      return false;
   }

   public static Graphics getGraphicsForImage(Image image) throws SlickException {
      Graphics g = graphics.get(image);
      if (g == null) {
         g = new FBOGraphics(image);
         graphics.put(image, g);
      }

      return g;
   }

   public static void releaseGraphicsForImage(Image image) throws SlickException {
      Graphics g = graphics.remove(image);
      if (g != null) {
         g.destroy();
      }
   }
}
