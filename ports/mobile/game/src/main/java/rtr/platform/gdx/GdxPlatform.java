package rtr.platform.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import java.io.File;
import java.io.IOException;
import org.newdawn.slick.TrueTypeFont;
import rtr.compat.font.GdxFont;
import rtr.compat.lwjgl.opengl.Display;
import rtr.platform.Platform;

/** Platform services for the libGDX build (Android, iOS, and the libGDX desktop test host). */
public class GdxPlatform extends Platform {
   @Override
   public int getScreenWidth() {
      return Display.getWidth();
   }

   @Override
   public int getScreenHeight() {
      return Display.getHeight();
   }

   @Override
   public boolean browse(String uri) {
      return Gdx.net.openURI(uri);
   }

   @Override
   public boolean open(File file) {
      return false;
   }

   @Override
   public void moveMouseBy(int dx, int dy) {
   }

   @Override
   public Platform.PlatformFont loadFont(File ttf, float size) throws IOException {
      if (!ttf.isFile()) {
         throw new IOException("Font not found: " + ttf);
      }

      return new GdxPlatform.Font(new GdxFont(rtr.compat.GdxFiles.absolute(ttf.getAbsolutePath()), size));
   }

   @Override
   public TrueTypeFont createTrueTypeFont(Platform.PlatformFont font, boolean antiAlias, char[] additionalChars) {
      return new TrueTypeFont(((GdxPlatform.Font)font).font, antiAlias, additionalChars);
   }

   @Override
   public Platform.PlatformImage readImage(File file) throws IOException {
      final Pixmap p;
      try {
         p = new Pixmap(rtr.compat.GdxFiles.absolute(file.getAbsolutePath()));
      } catch (RuntimeException var4) {
         throw new IOException("Unable to read image " + file, var4);
      }

      final int w = p.getWidth();
      final int h = p.getHeight();
      final int[] argb = new int[w * h];

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            int rgba = p.getPixel(x, y);
            argb[y * w + x] = rgba >>> 8 | rgba << 24;
         }
      }

      p.dispose();
      return new Platform.PlatformImage() {
         @Override
         public int getWidth() {
            return w;
         }

         @Override
         public int getHeight() {
            return h;
         }

         @Override
         public int getRGB(int x, int y) {
            return argb[y * w + x];
         }
      };
   }

   static class Font implements Platform.PlatformFont {
      final GdxFont font;

      Font(GdxFont font) {
         this.font = font;
      }

      @Override
      public double getStringWidth(String text) {
         return this.font.stringWidth(text);
      }

      @Override
      public double getStringHeight(String text) {
         return this.font.getLineHeight();
      }
   }
}
