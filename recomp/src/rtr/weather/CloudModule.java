package rtr.weather;

import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class CloudModule extends ModuleBase {
   public static final int FBO_OFFSET = 10;
   public static final int MAXIMUM_CLUSTERS = 30;
   private ArrayList<CloudCluster> cloudClusters;
   private ArrayList<CloudCluster> cloudClustersRemove = new ArrayList<>();
   private Image cloudFBOShadow;
   private Image cloudFBOBottom;
   private Image cloudFBOTop;
   private boolean cloudsLoaded;
   private ArrayList<Color> regularCloudColors;
   private ArrayList<Color> rainCloudColors;
   private ArrayList<Color> bloodMoonCloudColors;
   private ArrayList<Color> fullMoonCloudColors;
   private ArrayList<Color> eclipseCloudColors;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.cloudClusters = new ArrayList<>();
      if (!this.cloudsLoaded) {
         this.loadCloudColors();
         this.cloudsLoaded = true;
      }

      this.reinitFBO();
   }

   private void loadCloudColors() {
      this.regularCloudColors = new ArrayList<>();

      for (int i = 0; i < 4; i++) {
         this.regularCloudColors.add(new Color(255 - i * 10, 255 - i * 10, 255 - i * 5, 140));
      }

      this.rainCloudColors = new ArrayList<>();

      for (int i = 0; i < 4; i++) {
         this.rainCloudColors.add(new Color(125 - i * 3, 125 - i * 3, 125 - i * 3, 240 - i * 5));
      }

      this.bloodMoonCloudColors = new ArrayList<>();

      for (int i = 0; i < 4; i++) {
         this.bloodMoonCloudColors.add(new Color(200 - i * 5, 50 - i * 5, 50 - i * 5, 170));
      }

      this.fullMoonCloudColors = new ArrayList<>();

      for (int i = 0; i < 4; i++) {
         this.fullMoonCloudColors.add(new Color(100 - i * 5, 200 - i * 5, 200 - i * 5, 140));
      }

      this.eclipseCloudColors = new ArrayList<>();

      for (int i = 0; i < 4; i++) {
         this.eclipseCloudColors.add(new Color(20 - i * 5, 20 - i * 5, 60 - i * 5, 180));
      }
   }

   public void reinitFBO() throws SlickException {
      System.out.println("Reinitializing Cloud FBOs");
      int newWidth = Display.getWidth() + 20;
      int newHeight = Display.getHeight() + 20;
      if (this.cloudFBOTop == null || newWidth > this.cloudFBOTop.getWidth() || newHeight > this.cloudFBOTop.getHeight()) {
         this.cloudFBOShadow = new Image(newWidth, newHeight);
         this.cloudFBOBottom = new Image(newWidth, newHeight);
         this.cloudFBOTop = new Image(newWidth, newHeight);
      }
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
   }

   @Override
   protected void loadPlay() throws SlickException {
   }

   @Override
   protected void newMapEditor() throws SlickException {
   }

   @Override
   protected void loadMapEditor() throws SlickException {
   }

   @Override
   protected void newWorldMap() throws SlickException {
   }

   @Override
   protected void loadWorldMap() throws SlickException {
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   public void update() throws SlickException {
      if (!SettingsParser.getClouds()) {
         this.cloudClusters.clear();
      } else {
         if (this.cloudClustersRemove.size() > 0) {
            this.cloudClusters.removeAll(this.cloudClustersRemove);
            this.cloudClustersRemove.clear();
         }

         for (CloudCluster c : this.cloudClusters) {
            c.update();
         }

         int maximumClustersFinal = 30;
         if (this.weather.isRaining() || this.weather.isSnowing()) {
            maximumClustersFinal += 15;
         } else if (this.time.isBloodMoon() || this.time.isFullMoon()) {
            maximumClustersFinal = 90;
         }

         if (this.cloudClusters.size() < maximumClustersFinal) {
            this.addNewCluster(Utilities.randomInt(4096), Utilities.randomInt(4096));
         }
      }
   }

   public void render(Graphics g) throws SlickException {
      if (SettingsParser.getShadows()) {
         if (this.cloudClusters.size() != 0) {
            g.resetTransform();
            g.scale(1.0F, 1.0F);
            Graphics gS = this.cloudFBOShadow.getGraphics();
            gS.clear();
            gS.resetTransform();
            gS.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
            Cloud.startUse();

            for (CloudCluster c : this.cloudClusters) {
               c.renderShadow(gS);
            }

            Cloud.endUse();
            gS.flush();
            Graphics gB = this.cloudFBOBottom.getGraphics();
            gB.clear();
            gB.resetTransform();
            gB.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
            Cloud.startUse();

            for (CloudCluster c : this.cloudClusters) {
               c.renderBottom(gB);
            }

            Cloud.endUse();
            gB.flush();
            Graphics gT = this.cloudFBOTop.getGraphics();
            gT.clear();
            gT.resetTransform();
            gT.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
            Cloud.startUse();

            for (CloudCluster c : this.cloudClusters) {
               c.renderTop(gT);
            }

            Cloud.endUse();
            gT.flush();
            this.cloudFBOShadow.draw(-10.0F, -10.0F);
            this.cloudFBOBottom.draw(-10.0F, -10.0F);
            this.cloudFBOTop.draw(-10.0F, -10.0F);
            g.resetTransform();
            g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
         }
      }
   }

   public void removeCluster(CloudCluster c) {
      this.cloudClustersRemove.add(c);
   }

   public void addNewCluster(int x, int y) throws SlickException {
      ArrayList<Color> colorsOut = this.regularCloudColors;
      if (this.time.isBloodMoonDay()) {
         colorsOut = this.bloodMoonCloudColors;
      } else if (this.time.isFullMoonDay()) {
         colorsOut = this.fullMoonCloudColors;
      } else if (this.time.isEclipseDay()) {
         colorsOut = this.eclipseCloudColors;
      } else if (this.weather.isRaining()) {
         colorsOut = this.rainCloudColors;
      }

      this.cloudClusters.add(new CloudCluster(x, y, colorsOut));
   }
}
