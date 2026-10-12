package rtr;

import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageLoader {
   private static HashMap<String, Image> imagesList = new HashMap<>();

   public static Image getImage(String i) throws SlickException {
      if (imagesList.containsKey(i)) {
         return imagesList.get(i);
      } else {
         Image newImage = new Image(i);
         newImage.setFilter(9728);
         imagesList.put(i, newImage);
         return imagesList.get(i);
      }
   }

   public static void unloadImage(String i) throws SlickException {
      imagesList.get(i).destroy();
      imagesList.remove(i);
   }

   public static int getTotalImages() throws SlickException {
      return imagesList.size();
   }

   public static void reload(String i) throws SlickException {
      unloadImage(i);
      getImage(i);
   }
}
