package org.newdawn.slick.opengl.renderer;

/** Same API as Slick's Renderer, but always backed by the libGDX fixed-function emulator. */
public class Renderer {
   public static final int IMMEDIATE_RENDERER = 1;
   public static final int VERTEX_ARRAY_RENDERER = 2;
   public static final int DEFAULT_LINE_STRIP_RENDERER = 3;
   public static final int QUAD_BASED_LINE_STRIP_RENDERER = 4;
   private static SGL renderer = new GdxGL();
   private static LineStripRenderer lineStripRenderer = new DefaultLineStripRenderer();

   public static void setRenderer(int type) {
   }

   public static void setLineStripRenderer(int type) {
      switch (type) {
         case DEFAULT_LINE_STRIP_RENDERER:
            setLineStripRenderer(new DefaultLineStripRenderer());
            break;
         case QUAD_BASED_LINE_STRIP_RENDERER:
            setLineStripRenderer(new QuadBasedLineStripRenderer());
            break;
         default:
            throw new RuntimeException("Unknown line strip renderer type: " + type);
      }
   }

   public static void setLineStripRenderer(LineStripRenderer r) {
      lineStripRenderer = r;
   }

   public static void setRenderer(SGL r) {
      renderer = r;
   }

   public static SGL get() {
      return renderer;
   }

   public static GdxGL gdx() {
      return (GdxGL)renderer;
   }

   public static LineStripRenderer getLineStripRenderer() {
      return lineStripRenderer;
   }
}
