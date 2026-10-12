package rtr.platform;

import java.io.File;
import java.io.IOException;
import org.newdawn.slick.TrueTypeFont;

/**
 * Seam between the game and the host OS. The desktop build uses {@code rtr.platform.desktop.DesktopPlatform}
 * (AWT); ports install their own implementation with {@link #set(Platform)} before the game starts.
 */
public abstract class Platform {
   private static Platform instance;

   public static Platform get() {
      if (instance == null) {
         try {
            instance = (Platform)Class.forName("rtr.platform.desktop.DesktopPlatform").newInstance();
         } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("No Platform installed", e);
         }
      }

      return instance;
   }

   public static void set(Platform platform) {
      instance = platform;
   }

   public abstract int getScreenWidth();

   public abstract int getScreenHeight();

   /** Opens a web page or local file in the OS shell. Returns false if unsupported. */
   public abstract boolean browse(String uri);

   public abstract boolean open(File file);

   /** Moves the OS mouse pointer by the given amount; no-op where there is no pointer. */
   public abstract void moveMouseBy(int dx, int dy);

   public abstract PlatformFont loadFont(File ttf, float size) throws IOException;

   public abstract TrueTypeFont createTrueTypeFont(PlatformFont font, boolean antiAlias, char[] additionalChars);

   public abstract PlatformImage readImage(File file) throws IOException;

   public interface PlatformFont {
      double getStringWidth(String text);

      double getStringHeight(String text);
   }

   public interface PlatformImage {
      int getWidth();

      int getHeight();

      /** ARGB, like BufferedImage.getRGB. */
      int getRGB(int x, int y);
   }
}
