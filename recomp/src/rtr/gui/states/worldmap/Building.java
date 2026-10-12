package rtr.gui.states.worldmap;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import rtr.ImageLoader;

public class Building {
   private static ArrayList<Image> buildingImages = new ArrayList<>();
   private static ArrayList<Image> campImages = new ArrayList<>();
   private static ArrayList<byte[][]> buildingImageCollisionMap = new ArrayList<>();
   private static ArrayList<byte[][]> campImageCollisionMap = new ArrayList<>();
   private Image building;
   private byte[][] buildingCollisionMap;
   private int x;
   private int y;

   public Building(Random r, boolean isCamp, int campNumber) throws SlickException {
      if (isCamp) {
         if (campImages.size() == 0) {
            String path = "res/GUI/worldMap/buildings/c1.png";
            int count = 1;

            for (File imageCheck = new File(path); imageCheck.exists(); imageCheck = new File(path)) {
               campImages.add(ImageLoader.getImage(path));
               path = "res/GUI/worldMap/buildings/c" + ++count + ".png";
            }

            for (int i = 0; i < campImages.size(); i++) {
               Image image = campImages.get(i);
               int width = image.getWidth();
               int height = image.getHeight();
               byte[][] mapOut = new byte[width][height];

               for (int x = 0; x < width; x++) {
                  for (int y = 0; y < height; y++) {
                     if (image.getColor(x, y).getAlpha() >= 255) {
                        mapOut[x][y] = 1;
                     }
                  }
               }

               campImageCollisionMap.add(mapOut);
            }
         }

         this.building = campImages.get(campNumber - 1);
         this.buildingCollisionMap = campImageCollisionMap.get(campNumber - 1);
      } else {
         if (buildingImages.size() == 0) {
            String path = "res/GUI/worldMap/buildings/1.png";
            int count = 1;

            for (File imageCheck = new File(path); imageCheck.exists(); imageCheck = new File(path)) {
               buildingImages.add(ImageLoader.getImage(path));
               path = "res/GUI/worldMap/buildings/" + ++count + ".png";
            }

            for (int i = 0; i < buildingImages.size(); i++) {
               Image image = buildingImages.get(i);
               int width = image.getWidth();
               int height = image.getHeight();
               byte[][] mapOut = new byte[width][height];

               for (int x = 0; x < width; x++) {
                  for (int yx = 0; yx < height; yx++) {
                     if (image.getColor(x, yx).getAlpha() >= 255) {
                        mapOut[x][yx] = 1;
                     }
                  }
               }

               buildingImageCollisionMap.add(mapOut);
               image.flushPixelData();
            }
         }

         int randomBuilding = r.nextInt(buildingImages.size());
         this.building = buildingImages.get(randomBuilding);
         this.buildingCollisionMap = buildingImageCollisionMap.get(randomBuilding);
      }
   }

   public void setLocation(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public byte[][] getCollisionMap() {
      return this.buildingCollisionMap;
   }

   public void render(float mapX, float mapY) {
      this.building.draw(mapX + this.x, mapY + this.y);
   }
}
