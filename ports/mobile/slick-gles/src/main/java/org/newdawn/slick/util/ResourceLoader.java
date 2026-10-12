package org.newdawn.slick.util;

import com.badlogic.gdx.Gdx;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import rtr.compat.RtrPaths;

/** Finds resources in the game data folder ({@link RtrPaths}), then the classpath, then libGDX internal files. */
public class ResourceLoader {
   public static void addResourceLocation(ResourceLocation location) {
   }

   public static void removeResourceLocation(ResourceLocation location) {
   }

   public static void removeAllResourceLocations() {
   }

   private static File file(String ref) {
      File f = new File(RtrPaths.resolve(ref.replace('\\', '/')));
      return f.isFile() ? f : null;
   }

   public static InputStream getResourceAsStream(String ref) {
      try {
         File f = file(ref);
         if (f != null) {
            return new BufferedInputStream(new FileInputStream(f));
         }

         InputStream in = ResourceLoader.class.getClassLoader().getResourceAsStream(ref);
         if (in != null) {
            return new BufferedInputStream(in);
         }

         if (Gdx.files != null && Gdx.files.internal(ref).exists()) {
            return new BufferedInputStream(Gdx.files.internal(ref).read());
         }
      } catch (IOException var3) {
         throw new RuntimeException("Resource not readable: " + ref, var3);
      }

      throw new RuntimeException("Resource not found: " + ref);
   }

   public static boolean resourceExists(String ref) {
      return file(ref) != null
         || ResourceLoader.class.getClassLoader().getResource(ref) != null
         || Gdx.files != null && Gdx.files.internal(ref).exists();
   }

   public static URL getResource(String ref) {
      try {
         File f = file(ref);
         if (f != null) {
            return f.toURI().toURL();
         }
      } catch (MalformedURLException var2) {
         throw new RuntimeException(var2);
      }

      URL url = ResourceLoader.class.getClassLoader().getResource(ref);
      if (url == null) {
         throw new RuntimeException("Resource not found: " + ref);
      }

      return url;
   }
}
