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

   /**
    * Same result as calling {@code list.remove(o)} for each element of {@code toRemove} in turn (each call drops the
    * first equal element), but in one pass instead of one linear search per element.
    */
   public static <T> void removeEach(java.util.ArrayList<T> list, java.util.List<T> toRemove) {
      if (toRemove.isEmpty()) {
         return;
      }

      if (toRemove.size() == 1) {
         list.remove(toRemove.get(0));
         return;
      }

      java.util.HashMap<T, Integer> pending = new java.util.HashMap<T, Integer>(toRemove.size() * 2);

      for (T t : toRemove) {
         Integer n = pending.get(t);
         pending.put(t, n == null ? 1 : n + 1);
      }

      int w = 0;

      for (int r = 0; r < list.size(); r++) {
         T e = list.get(r);
         Integer n = pending.isEmpty() ? null : pending.get(e);
         if (n != null) {
            if (n == 1) {
               pending.remove(e);
            } else {
               pending.put(e, n - 1);
            }
         } else {
            list.set(w++, e);
         }
      }

      while (list.size() > w) {
         list.remove(list.size() - 1);
      }
   }

   /** A file's timestamp formatted like BasicFileAttributes.creationTime().toString() (UTC, ISO-8601). */
   public static String fileTimeIso(File file) {
      SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      f.setTimeZone(TimeZone.getTimeZone("UTC"));
      return f.format(new Date(file.lastModified()));
   }
}
