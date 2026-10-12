package rtr.platform.desktop;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import javax.imageio.ImageIO;
import org.newdawn.slick.TrueTypeFont;
import rtr.platform.Platform;

/** Original desktop behaviour (AWT), used by the Slick/LWJGL 2 build. */
public class DesktopPlatform extends Platform {
   private static final FontRenderContext FRC = new FontRenderContext(new AffineTransform(), true, true);

   @Override
   public int getScreenWidth() {
      return Toolkit.getDefaultToolkit().getScreenSize().width;
   }

   @Override
   public int getScreenHeight() {
      return Toolkit.getDefaultToolkit().getScreenSize().height;
   }

   @Override
   public boolean browse(String uri) {
      try {
         Desktop.getDesktop().browse(new URI(uri));
         return true;
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }
   }

   @Override
   public boolean open(File file) {
      if (!Desktop.isDesktopSupported()) {
         return false;
      }

      try {
         Desktop.getDesktop().open(file);
         return true;
      } catch (IOException e) {
         e.printStackTrace();
         return false;
      }
   }

   @Override
   public void moveMouseBy(int dx, int dy) {
      try {
         Point p = MouseInfo.getPointerInfo().getLocation();
         new Robot().mouseMove(p.x + dx, p.y + dy);
      } catch (AWTException e) {
         e.printStackTrace();
      }
   }

   @Override
   public Platform.PlatformFont loadFont(File ttf, float size) throws IOException {
      try {
         return new AwtFont(Font.createFont(0, ttf).deriveFont(size));
      } catch (FontFormatException e) {
         throw new IOException(e);
      }
   }

   @Override
   public TrueTypeFont createTrueTypeFont(Platform.PlatformFont font, boolean antiAlias, char[] additionalChars) {
      return new TrueTypeFont(((AwtFont)font).font, antiAlias, additionalChars);
   }

   @Override
   public Platform.PlatformImage readImage(File file) throws IOException {
      final BufferedImage img = ImageIO.read(file);
      return new Platform.PlatformImage() {
         @Override
         public int getWidth() {
            return img.getWidth();
         }

         @Override
         public int getHeight() {
            return img.getHeight();
         }

         @Override
         public int getRGB(int x, int y) {
            return img.getRGB(x, y);
         }
      };
   }

   static class AwtFont implements Platform.PlatformFont {
      final Font font;

      AwtFont(Font font) {
         this.font = font;
      }

      @Override
      public double getStringWidth(String text) {
         return this.font.getStringBounds(text, FRC).getWidth();
      }

      @Override
      public double getStringHeight(String text) {
         return this.font.getStringBounds(text, FRC).getHeight();
      }
   }
}
