package rtr.system;

import java.io.File;
import rtr.platform.Platform;

public class ShutdownHook extends Thread {
   private static boolean safeShutdown = false;

   @Override
   public void run() {
      if (!safeShutdown) {
         if (!Platform.get().open(new File(Launcher.getLogPath()))) {
            System.out.println("Failed to open default text editor.");
         }
      }
   }

   public static void setSafeShutdown(boolean b) {
      safeShutdown = b;
   }
}
