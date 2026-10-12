package rtr.weather;

import java.io.File;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.map.MapModule;
import rtr.states.StateBase;
import rtr.utilities.Utilities;

public class CloudCluster {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   private CloudModule cloud = (CloudModule)StateBase.getModule(ModuleBase.ModuleType.CLOUD);
   private static ArrayList<byte[][]> cloudShapeData;
   private ArrayList<Cloud> allCloudsBottom = new ArrayList<>();
   private ArrayList<Cloud> allCloudsTop = new ArrayList<>();
   private ArrayList<Cloud> removeClouds = new ArrayList<>();
   private float clusterX;
   private float clusterY;
   private float velXMod;
   private float velYMod;
   private int cloudDecay;
   private int cloudDecayTick;
   private ArrayList<Color> clusterColors;

   public CloudCluster(int x, int y, ArrayList<Color> c) throws SlickException {
      this.clusterX = x;
      this.clusterY = y;
      this.clusterColors = c;
      this.velXMod = this.weather.getWindVelocity() + (Utilities.randomFloat() + Utilities.randomFloat() * -1.0F) / 100.0F;
      this.velYMod = (Utilities.randomFloat() + Utilities.randomFloat() * -1.0F) / 100.0F;
      this.cloudDecay = Utilities.randomInt(25000) + 5000;
      if (cloudShapeData == null) {
         this.generateCloudData();
      }

      this.generateCloud();
   }

   private void generateCloud() throws SlickException {
      byte[][] thisCluster = cloudShapeData.get(Utilities.randomInt(cloudShapeData.size()));
      int width = thisCluster.length;
      int height = thisCluster[0].length;

      for (int w = 0; w < width; w++) {
         for (int h = 0; h < height; h++) {
            if (thisCluster[w][h] >= 1) {
               this.allCloudsBottom.add(new Cloud(this.clusterX + w * 16, this.clusterY + h * 16, this));
               this.allCloudsBottom.add(new Cloud(this.clusterX + w * 16, this.clusterY + h * 16, this));
            }

            if (thisCluster[w][h] >= 2) {
               this.allCloudsTop.add(new Cloud(this.clusterX + w * 16, this.clusterY + h * 16, this));
               this.allCloudsTop.add(new Cloud(this.clusterX + w * 16, this.clusterY + h * 16, this));
            }
         }
      }
   }

   private void generateCloudData() throws SlickException {
      ArrayList<Image> clusterImages = new ArrayList<>();
      int clusterNumber = 0;

      for (File file = new File("res/clouds/cluster" + clusterNumber + ".png"); file.exists(); file = new File("res/clouds/cluster" + ++clusterNumber + ".png")) {
         Image thisCloud = new Image("res/clouds/cluster" + clusterNumber + ".png");
         clusterImages.add(thisCloud);
      }

      int fixedStart = clusterNumber;
      clusterNumber = 0;

      for (File var16 = new File("res/clouds/clusterFixed" + clusterNumber + ".png");
         var16.exists();
         var16 = new File("res/clouds/clusterFixed" + ++clusterNumber + ".png")
      ) {
         Image thisCloud = new Image("res/clouds/cluster" + clusterNumber + ".png");
         clusterImages.add(thisCloud);
      }

      cloudShapeData = new ArrayList<>();

      for (int i = 0; i < clusterImages.size(); i++) {
         Image cloudSprite = clusterImages.get(i);
         int imageWidth = cloudSprite.getWidth();
         int imageHeight = cloudSprite.getHeight();
         byte[][] dataOut = new byte[imageWidth][imageHeight];

         for (int w = 0; w < imageWidth; w++) {
            for (int h = 0; h < imageHeight; h++) {
               Color colorCheck = cloudSprite.getColor(w, h);
               if (colorCheck.getAlpha() != 0) {
                  if (colorCheck.getRed() == 0) {
                     dataOut[w][h] = 2;
                  } else if (colorCheck.getRed() == 255) {
                     dataOut[w][h] = 1;
                  }
               }
            }
         }

         cloudSprite.flushPixelData();
         cloudShapeData.add(dataOut);
         byte[][] flipH = new byte[imageWidth][imageHeight];
         byte[][] flipV = new byte[imageWidth][imageHeight];
         byte[][] flipHV = new byte[imageWidth][imageHeight];

         for (int w = 0; w < imageWidth; w++) {
            for (int hx = 0; hx < imageHeight; hx++) {
               flipH[imageWidth - w - 1][hx] = dataOut[w][hx];
               if (i <= fixedStart) {
                  flipV[w][imageHeight - hx - 1] = dataOut[w][hx];
                  flipHV[imageWidth - w - 1][imageHeight - hx - 1] = dataOut[w][hx];
               }
            }
         }

         cloudShapeData.add(flipH);
         if (i <= fixedStart) {
            cloudShapeData.add(flipV);
            cloudShapeData.add(flipHV);
         }
      }

      clusterImages.clear();
   }

   public void renderShadow(Graphics g) throws SlickException {
      for (Cloud c : this.allCloudsBottom) {
         c.render(g, true);
      }

      for (Cloud c : this.allCloudsTop) {
         c.render(g, true);
      }
   }

   public void renderBottom(Graphics g) throws SlickException {
      for (Cloud c : this.allCloudsBottom) {
         c.render(g, false);
      }
   }

   public void renderTop(Graphics g) throws SlickException {
      for (Cloud c : this.allCloudsTop) {
         c.render(g, false);
      }
   }

   public void update() {
      if (this.removeClouds.size() > 0) {
         this.allCloudsBottom.removeAll(this.removeClouds);
         this.allCloudsTop.removeAll(this.removeClouds);
         this.removeClouds.clear();
      }

      this.cloudDecayTick++;
      if (this.cloudDecayTick == this.cloudDecay) {
         for (Cloud c : this.allCloudsBottom) {
            c.triggerDecay();
         }

         for (Cloud c : this.allCloudsTop) {
            c.triggerDecay();
         }
      }

      this.clusterX = this.clusterX + this.velXMod;
      this.clusterY = this.clusterY + this.velYMod;

      for (Cloud c : this.allCloudsBottom) {
         c.updateNew();
      }

      for (Cloud c : this.allCloudsTop) {
         c.updateNew();
      }

      if (Utilities.randomInt(1000) == 0) {
         this.velXMod = this.weather.getWindVelocity() + (Utilities.randomFloat() + Utilities.randomFloat() * -1.0F) / 100.0F;
         this.velYMod = (Utilities.randomFloat() + Utilities.randomFloat() * -1.0F) / 100.0F;
      }

      if (this.allCloudsTop.size() == 0 && this.allCloudsBottom.size() == 0) {
         this.cloud.removeCluster(this);
      }
   }

   public void removeCloud(Cloud c) {
      this.removeClouds.add(c);
   }

   public float getClusterX() {
      return this.clusterX;
   }

   public float getClusterY() {
      return this.clusterY;
   }

   public float getVelXMod() {
      return this.velXMod;
   }

   public float getVelYMod() {
      return this.velYMod;
   }

   public ArrayList<Color> getClusterColor() {
      return this.clusterColors;
   }

   public Color getRandomClusterColor() {
      return this.clusterColors.get(Utilities.randomInt(this.clusterColors.size()));
   }
}
