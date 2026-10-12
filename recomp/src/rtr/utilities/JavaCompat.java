package rtr.utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * Replacements for the few Java 8 / java.nio.file APIs the game used, so it also runs on runtimes with an older
 * class library (MobiVM on iOS). Behaviour matches the original call sites.
 */
public final class JavaCompat {
   private JavaCompat() {
   }

   public static <K, V> V getOrDefault(Map<K, V> map, Object key, V defaultValue) {
      V v = map.get(key);
      return v != null || map.containsKey(key) ? v : defaultValue;
   }

   /** A file's timestamp formatted like BasicFileAttributes.creationTime().toString() (UTC, ISO-8601). */
   public static String fileTimeIso(File file) {
      SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      f.setTimeZone(TimeZone.getTimeZone("UTC"));
      return f.format(new Date(file.lastModified()));
   }
}
