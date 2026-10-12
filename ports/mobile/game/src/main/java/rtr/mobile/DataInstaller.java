package rtr.mobile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** First-launch setup for platforms that ship the game data read-only inside the app (iOS). */
public final class DataInstaller {
   private DataInstaller() {
   }

   /** True when {@code assets} contains the folders the game cannot start without. */
   public static boolean hasAssets(File assets) {
      return new File(assets, "res").isDirectory() && new File(assets, "localization").isDirectory();
   }

   /**
    * Copies the bundled writable folders (maps/, and profiles/ if the bundle has them) into the data root, once.
    * Existing files are never overwritten, so saves and edited maps survive app updates.
    */
   public static void seed(File bundle, File dataRoot) throws IOException {
      for (String folder : new String[]{"maps", "profiles"}) {
         File src = new File(bundle, folder);
         if (src.isDirectory()) {
            copyMissing(src, new File(dataRoot, folder));
         }
      }
   }

   private static void copyMissing(File src, File dst) throws IOException {
      if (src.isDirectory()) {
         if (!dst.isDirectory() && !dst.mkdirs()) {
            throw new IOException("Cannot create " + dst);
         }

         String[] names = src.list();
         if (names != null) {
            for (String n : names) {
               copyMissing(new File(src, n), new File(dst, n));
            }
         }
      } else if (!dst.exists()) {
         InputStream in = new FileInputStream(src);

         try {
            OutputStream out = new FileOutputStream(dst);

            try {
               byte[] buf = new byte[65536];

               int n;
               while ((n = in.read(buf)) > 0) {
                  out.write(buf, 0, n);
               }
            } finally {
               out.close();
            }
         } finally {
            in.close();
         }
      }
   }
}
