package rtr.compat;

import java.io.File;

/**
 * Where the game's files live. The mobile build rewrites the game's relative path literals:
 * {@code "res/..."} and {@code "localization/..."} become {@code RtrPaths.ASSETS + "..."} (read-only, may be inside
 * the app bundle), while {@code "profiles/"}, {@code "maps/"} and {@code "logs/"} become {@code RtrPaths.ROOT + "..."}
 * (writable). Set both before the game starts; on desktop they are the same folder.
 */
public final class RtrPaths {
   public static String ROOT = "";
   public static String ASSETS = "";

   private static String dir(File dir) {
      String p = dir.getAbsolutePath().replace('\\', '/');
      return p.endsWith("/") ? p : p + "/";
   }

   /** Writable data root. Also sets the asset root if none was given. */
   public static void setRoot(File dir) {
      ROOT = dir(dir);
      if (ASSETS.isEmpty()) {
         ASSETS = ROOT;
      }
   }

   public static void setAssetsRoot(File dir) {
      ASSETS = dir(dir);
   }

   private static boolean isAsset(String path) {
      return path.startsWith("res/") || path.startsWith("localization/");
   }

   /**
    * Like {@link #resolve} for audio: an .ogg reference is redirected to a sibling with {@code preferredExt} when
    * one exists (the mobile data pack converts Ogg, which iOS cannot play).
    */
   public static String resolveAudio(String path, String preferredExt) {
      String p = resolve(path);
      if (p != null && p.toLowerCase().endsWith(".ogg")) {
         String alt = p.substring(0, p.length() - 4) + preferredExt;
         if (new File(alt).isFile()) {
            return alt;
         }
      }

      return p;
   }

   /** Resolves a path the way the desktop game would, relative to its install folder. */
   public static String resolve(String path) {
      if (path == null || ROOT.isEmpty() || path.startsWith(ROOT) || path.startsWith(ASSETS) || new File(path).isAbsolute()) {
         return path;
      }

      return (isAsset(path) ? ASSETS : ROOT) + path;
   }
}
