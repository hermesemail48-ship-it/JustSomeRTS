package rtr.utilities.devtools;

import java.io.File;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageScrubber {
   public static void scrubMapSheets() {
      scrub(new File("res/maps/base").listFiles());
      scrub(new File("res/maps/collision").listFiles());
      scrub(new File("res/maps/data").listFiles());
      scrub(new File("res/maps/depth").listFiles());
      scrub(new File("res/maps/particle").listFiles());
      scrub(new File("res/maps/shadow").listFiles());
      System.out.println("Scrub complete");
   }

   private static void scrub(File[] base) {
      for (File f : base) {
         if (f.isFile()) {
            if (f.getName().contains("terrainMaster")) {
               System.out.println("Found illegal terrain image: " + f.getName());
               f.delete();
               f.deleteOnExit();
            } else if (f.getName().contains("objectMaster")) {
               System.out.println("Found illegal object image: " + f.getName());
               f.delete();
               f.deleteOnExit();
            } else if (isBlankImage(f)) {
               System.out.println("Found blank image: " + f.getName());
               f.delete();
               f.deleteOnExit();
            }
         }
      }
   }

   private static boolean isBlankImage(File f) {
      try {
         Image i = new Image(f.getAbsolutePath());
         int width = i.getWidth();
         int height = i.getHeight();

         for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
               if (i.getColor(w, h).getAlpha() > 0) {
                  return false;
               }
            }
         }

         return true;
      } catch (SlickException var6) {
         return true;
      }
   }
}
