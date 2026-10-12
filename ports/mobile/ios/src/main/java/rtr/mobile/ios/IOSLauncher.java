package rtr.mobile.ios;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import java.io.File;
import java.io.IOException;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.glkit.GLKViewDrawableColorFormat;
import org.robovm.apple.glkit.GLKViewDrawableDepthFormat;
import org.robovm.apple.uikit.UIApplication;
import rtr.mobile.DataInstaller;
import rtr.mobile.MessageScreen;
import rtr.mobile.RtrGdxGame;

/**
 * iOS entry point. Game assets are bundled read-only in the app (rtr/res, rtr/localization); saves and maps live
 * in Documents/rtr, which the Files app can see.
 */
public class IOSLauncher extends IOSApplication.Delegate {
   @Override
   protected IOSApplication createApplication() {
      IOSApplicationConfiguration config = new IOSApplicationConfiguration();
      config.orientationLandscape = true;
      config.orientationPortrait = false;
      config.colorFormat = GLKViewDrawableColorFormat.RGBA8888;
      config.depthFormat = GLKViewDrawableDepthFormat.None;
      config.useAccelerometer = false;
      config.useCompass = false;
      config.hideHomeIndicator = true;
      config.preventScreenDimming = true;
      return new IOSApplication(createGame(), config);
   }

   private static ApplicationListener createGame() {
      File bundle = new File(NSBundle.getMainBundle().getBundlePath(), "rtr");
      File data = new File(System.getenv("HOME"), "Documents/rtr");
      if (!DataInstaller.hasAssets(bundle)) {
         return new MessageScreen(
            "Rise to Ruins game data is missing from this build.\n\n"
               + "Build the data pack on your PC (python -I scripts/build_mobile_data.py) before building the IPA, "
               + "so mobile-data/ gets bundled into the app."
         );
      }

      try {
         DataInstaller.seed(bundle, data);
      } catch (IOException var3) {
         return new MessageScreen("Could not set up the save folder:\n\n" + var3);
      }

      return new RtrGdxGame(data, bundle, true, true);
   }

   public static void main(String[] argv) {
      NSAutoreleasePool pool = new NSAutoreleasePool();
      UIApplication.main(argv, null, IOSLauncher.class);
      pool.close();
   }
}
