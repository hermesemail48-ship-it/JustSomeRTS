package rtr.gui.buttons;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.font.Text;
import rtr.gui.states.WorldMapGUIData;
import rtr.gui.states.worldmap.ActiveVillageIcon;
import rtr.particles.ParticleModule;
import rtr.save.RegionalSavedGame;
import rtr.save.SavedGamesHandler;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.Utilities;

public class GUIButtonWorldMapSelection extends GUIButton {
   private int positionX;
   private int positionY;
   private Image worldMapHighlightBox;
   private String[] regionConnectionNames;
   private ArrayList<Integer> connectedRegionsX = new ArrayList<>();
   private ArrayList<Integer> connectedRegionsY = new ArrayList<>();
   private ArrayList<Boolean> connectedRegionExists = new ArrayList<>();
   private ArrayList<Boolean> connectedRegionLost = new ArrayList<>();
   private ActiveVillageIcon mapIcon;
   private RegionalSavedGame savedGame;
   private boolean regionLost;
   private boolean regionStarted;
   private boolean regionReadyToMigrate;
   private Image FBO = new Image(512, 512);
   private float regionConnectAlpha;

   public GUIButtonWorldMapSelection(RegionalSavedGame s) throws SlickException {
      this.worldMapHighlightBox = ImageLoader.getImage("res/GUI/gameState/worldMapHighlightBox.png");
      this.intersectBox = new Rectangle(0.0F, 0.0F, 24.0F, 24.0F);
      YMLDataMap mapModuleDataMap = s.getAndUnloadModuleSaveData(ModuleBase.ModuleType.MAP);
      this.buttonName = "worldMap" + mapModuleDataMap.getOrDefault("mapFileName", "null");
      this.toolTipDescription = mapModuleDataMap.getOrDefault("mapName", "null");
      this.positionX = Integer.parseInt(mapModuleDataMap.getOrDefault("worldMapX", 512));
      this.positionY = Integer.parseInt(mapModuleDataMap.getOrDefault("worldMapY", 512));
      this.savedGame = s;
      this.regionLost = WorldMapGUIData.isMapLost(s);
      this.regionStarted = WorldMapGUIData.isMapStarted(s);
      this.regionReadyToMigrate = !this.regionStarted && this.migration.getEnroutePopulation(mapModuleDataMap.getOrDefault("mapFileName", "null")) >= 10;
      YMLDataMap regionConnectionDataMap = mapModuleDataMap.getSubMap("connectedRegion");
      this.regionConnectionNames = new String[8];

      for (int i = 0; i < 8; i++) {
         this.regionConnectionNames[i] = regionConnectionDataMap.getOrDefault(Integer.toString(i), "null");
      }

      if (this.regionLost) {
         this.mapIcon = new ActiveVillageIcon(this.positionX, this.positionY, 2);
      } else if (this.regionReadyToMigrate) {
         this.mapIcon = new ActiveVillageIcon(this.positionX, this.positionY, 1);
      } else if (s.getGameMode() != GameModeTemplateBase.GameMode.MAP_EDITOR) {
         this.mapIcon = new ActiveVillageIcon(this.positionX, this.positionY, 0);
      }

      this.buildRegionConnectionData();
   }

   private void buildRegionConnectionData() {
      String mapPackFolderName = "WorldMaps";

      for (int i = 0; i < 8; i++) {
         String connectedRegionName = this.regionConnectionNames[i];
         if (!connectedRegionName.equals("null")) {
            RegionalSavedGame connectedRegionSave = null;
            if (SavedGamesHandler.regionalSaveExist(mapPackFolderName, connectedRegionName, StateBase.GameType.WORLD_MAP, WorldMapGUIData.getSelectedGameMode())
               )
             {
               connectedRegionSave = SavedGamesHandler.getRegionalSave(
                  mapPackFolderName, connectedRegionName, StateBase.GameType.WORLD_MAP, WorldMapGUIData.getSelectedGameMode()
               );
               this.connectedRegionExists.add(true);
            } else {
               connectedRegionSave = SavedGamesHandler.getRegionalSave(
                  mapPackFolderName, connectedRegionName, StateBase.GameType.WORLD_MAP, GameModeTemplateBase.GameMode.MAP_EDITOR
               );
               this.connectedRegionExists.add(false);
            }

            YMLDataMap connectedRegionMap = connectedRegionSave.getAndUnloadModuleSaveData(ModuleBase.ModuleType.MAP);
            this.connectedRegionsX.add(Integer.parseInt(connectedRegionMap.getOrDefault("worldMapX", 0)));
            this.connectedRegionsY.add(Integer.parseInt(connectedRegionMap.getOrDefault("worldMapY", 0)));
            if (connectedRegionMap.containsKey("mapLost") && Boolean.parseBoolean(connectedRegionMap.getOrDefault("mapLost", false))) {
               this.connectedRegionLost.add(true);
            } else {
               this.connectedRegionLost.add(false);
            }
         }
      }
   }

   public void render(Graphics g, Rectangle mouse, float mapX, float mapY, boolean debug) throws SlickException {
      this.intersectBox
         .setBounds(
            (mapX + this.positionX - 58.0F) * ScaleControl.getScalingDifference(),
            (mapY + this.positionY - 58.0F) * ScaleControl.getScalingDifference(),
            128.0F * ScaleControl.getScalingDifference(),
            128.0F * ScaleControl.getScalingDifference()
         );
      if (this.isSelected || mouse.intersects(this.intersectBox)) {
         int boxX = 0;
         int boxY = 0;

         for (int x = 0; x < 128; x++) {
            int randomX = Utilities.randomInt(-1, 1);
            int randomY = Utilities.randomInt(-1, 1);
            ParticleModule.ParticleSet colorOutOne = this.regionLost ? ParticleModule.ParticleSet.BLACK : ParticleModule.ParticleSet.FIRE_PURPLE;
            ParticleModule.ParticleSet colorOutTwo = this.regionLost ? ParticleModule.ParticleSet.FIRE_RED : ParticleModule.ParticleSet.FIRE_BLUE;
            if (Utilities.randomInt(100) == 0) {
               this.particle
                  .newParticleGroup(this.positionX + randomX + boxX - 57, this.positionY + randomY - 57, ParticleModule.ParticleGroup.MAGIC_FIRE, colorOutOne);
               this.particle
                  .newParticleGroup(
                     this.positionX + randomX + boxX - 57, this.positionY + randomY + 128 - 57, ParticleModule.ParticleGroup.MAGIC_FIRE, colorOutOne
                  );
               this.particle
                  .newParticleGroup(this.positionX + randomX - 57, this.positionY + randomY + boxY - 57, ParticleModule.ParticleGroup.MAGIC_FIRE, colorOutOne);
               this.particle
                  .newParticleGroup(
                     this.positionX + randomX + 128 - 57, this.positionY + randomY + boxY - 57, ParticleModule.ParticleGroup.MAGIC_FIRE, colorOutOne
                  );
            }

            if (Utilities.randomInt(200) == 0) {
               this.particle
                  .newParticleGroup(this.positionX + randomX + boxX - 57, this.positionY + randomY - 57, ParticleModule.ParticleGroup.MAGIC_FIRE, colorOutTwo);
               this.particle
                  .newParticleGroup(
                     this.positionX + randomX + boxX - 57, this.positionY + randomY + 128 - 57, ParticleModule.ParticleGroup.MAGIC_FIRE, colorOutTwo
                  );
               this.particle
                  .newParticleGroup(this.positionX + randomX - 57, this.positionY + randomY + boxY - 57, ParticleModule.ParticleGroup.MAGIC_FIRE, colorOutTwo);
               this.particle
                  .newParticleGroup(
                     this.positionX + randomX + 128 - 57, this.positionY + randomY + boxY - 57, ParticleModule.ParticleGroup.MAGIC_FIRE, colorOutTwo
                  );
            }

            boxX++;
            boxY++;
         }

         if (this.regionConnectAlpha < 0.3F) {
            this.regionConnectAlpha += 0.02F;
         }
      } else if (this.regionConnectAlpha > 0.0F) {
         this.regionConnectAlpha -= 0.02F;
      }

      if (!this.regionLost) {
         Graphics FBOG = this.FBO.getGraphics();
         FBOG.clear();
         String connectedRegionName = this.regionConnectionNames[0];
         if (!connectedRegionName.equals("null") && !this.isSelected) {
            for (int x = 0; x < this.connectedRegionsX.size(); x++) {
               FBOG.setColor(new Color(0, 0, 0));
               FBOG.setLineWidth(4.0F);
               FBOG.drawLine(256.0F, 256.0F, this.connectedRegionsX.get(x) - this.positionX + 256, this.connectedRegionsY.get(x) - this.positionY + 256);
               FBOG.fillOval(
                  this.connectedRegionsX.get(x) - this.positionX + 256 - 9, this.connectedRegionsY.get(x) - this.positionY + 256 - 9, 18.0F, 18.0F, 6
               );
               FBOG.fillOval(247.0F, 247.0F, 18.0F, 18.0F, 6);
            }

            for (int x = 0; x < this.connectedRegionsX.size(); x++) {
               if (this.connectedRegionLost.get(x)) {
                  FBOG.setColor(new Color(255, 0, 0));
                  FBOG.setLineWidth(1.0F);
                  FBOG.drawLine(256.0F, 256.0F, this.connectedRegionsX.get(x) - this.positionX + 256, this.connectedRegionsY.get(x) - this.positionY + 256);
                  FBOG.setColor(new Color(255, 155, 0));
                  FBOG.fillOval(
                     this.connectedRegionsX.get(x) - this.positionX + 256 - 8, this.connectedRegionsY.get(x) - this.positionY + 256 - 8, 16.0F, 16.0F, 6
                  );
                  FBOG.fillOval(248.0F, 248.0F, 16.0F, 16.0F, 6);
               } else if (this.connectedRegionExists.get(x)) {
                  FBOG.setColor(new Color(0, 255, 0));
                  FBOG.setLineWidth(2.0F);
                  FBOG.drawLine(256.0F, 256.0F, this.connectedRegionsX.get(x) - this.positionX + 256, this.connectedRegionsY.get(x) - this.positionY + 256);
                  FBOG.setColor(new Color(100, 255, 255));
                  FBOG.fillOval(
                     this.connectedRegionsX.get(x) - this.positionX + 256 - 8, this.connectedRegionsY.get(x) - this.positionY + 256 - 8, 16.0F, 16.0F, 6
                  );
                  FBOG.fillOval(248.0F, 248.0F, 16.0F, 16.0F, 6);
               } else {
                  FBOG.setColor(new Color(255, 255, 0));
                  FBOG.setLineWidth(1.0F);
                  FBOG.drawLine(256.0F, 256.0F, this.connectedRegionsX.get(x) - this.positionX + 256, this.connectedRegionsY.get(x) - this.positionY + 256);
                  FBOG.setColor(new Color(255, 155, 0));
                  FBOG.fillOval(
                     this.connectedRegionsX.get(x) - this.positionX + 256 - 8, this.connectedRegionsY.get(x) - this.positionY + 256 - 8, 16.0F, 16.0F, 6
                  );
                  FBOG.fillOval(248.0F, 248.0F, 16.0F, 16.0F, 6);
               }
            }
         }

         FBOG.setLineWidth(1.0F);
         FBOG.flush();
         this.FBO.setAlpha(this.regionConnectAlpha);
         this.FBO.draw(mapX + this.positionX - 256.0F + 6.0F, mapY + this.positionY - 256.0F + 6.0F);
      }

      int mouseDistanceX = (int)Math.abs(mouse.getX() / ScaleControl.getScalingDifference() - (mapX + this.positionX));
      int mouseDistanceY = (int)Math.abs(mouse.getY() / ScaleControl.getScalingDifference() - (mapY + this.positionY));
      int finalDistance = mouseDistanceX + mouseDistanceY;
      if (finalDistance > 255) {
         finalDistance = 255;
      }

      finalDistance = 255 - finalDistance;
      float finalDistancePercect = (float)Utilities.getPercentOf(127.0, finalDistance / 2);
      GL11.glBlendFunc(774, 771);
      if (this.regionLost) {
         g.setColor(new Color(1.0F, 1.0F - finalDistancePercect / 200.0F, 1.0F - finalDistancePercect / 200.0F, 1.0F - finalDistancePercect / 300.0F));
      } else {
         g.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F - finalDistancePercect / 300.0F));
      }

      g.fillRect(mapX + this.positionX - 58.0F, mapY + this.positionY - 58.0F, 128.0F, 128.0F);
      g.setDrawMode(Graphics.MODE_NORMAL);
      this.worldMapHighlightBox.setAlpha(finalDistancePercect / 100.0F);
      this.worldMapHighlightBox.draw(mapX + this.positionX - 62.0F, mapY + this.positionY - 62.0F);
      if (this.mapIcon != null) {
         this.mapIcon.render(mapX, mapY);
      }

      if (debug) {
         String connectedRegionName = this.regionConnectionNames[0];
         if (!connectedRegionName.equals("null") && debug) {
            this.worldMapHighlightBox.setAlpha(1.0F);
            this.worldMapHighlightBox.draw(mapX + this.positionX - 59.0F, mapY + this.positionY - 59.0F);
            g.resetTransform();
            g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
            g.setColor(new Color(0, 255, 255, 150));
            g.draw(this.intersectBox);
            g.resetTransform();
            g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
            this.FBO.setAlpha(1.0F);
            this.FBO.draw(mapX + this.positionX - 256.0F + 6.0F, mapY + this.positionY - 256.0F + 6.0F);
         } else {
            g.resetTransform();
            g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
            g.setColor(Color.red);
            g.draw(this.intersectBox);
            g.resetTransform();
            g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
         }
      }
   }

   public void renderDescriptionToolTip(Rectangle mouse, Graphics g, float mapX, float mapY) throws SlickException {
      if (mouse.intersects(this.intersectBox)) {
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         int scale = (int)ScaleControl.getInterfaceScale();
         if (scale > 6) {
            scale = 6;
         }

         int width = Text.getWidth(this.toolTipDescription, Text.FontType.HEADER, scale);
         int height = Text.getHeight(this.toolTipDescription, Text.FontType.HEADER, scale);
         g.setColor(new Color(0, 0, 0, 125));
         int titleBarX = (int)ScaleControl.getTransformedPosition(mapX + this.positionX - 45.0F);
         int titleBarY = (int)ScaleControl.getTransformedPosition(mapY + this.positionY - 50.0F);
         g.fillRoundRect(titleBarX - 10, titleBarY, width + 20, height + 10, 4);
         g.fillRoundRect(titleBarX - 5, titleBarY + 5, width + 10, height, 4);
         String colorCode = "$CYA1";
         if (this.regionLost) {
            colorCode = "$RED2";
         } else if (this.savedGame.getGameMode() != GameModeTemplateBase.GameMode.MAP_EDITOR) {
            colorCode = "$YEL1";
         }

         this.font.drawString(titleBarX, titleBarY + 5, colorCode + this.toolTipDescription, Text.FontType.HEADER, scale, false);
         int infoBarX = (int)ScaleControl.getTransformedPosition(mapX + this.positionX - 45.0F);
         int infoBarY = (int)ScaleControl.getTransformedPosition(mapY + this.positionY - 10.0F);
         if (!this.regionLost && !this.regionReadyToMigrate) {
            if (this.savedGame.getGameMode() != GameModeTemplateBase.GameMode.MAP_EDITOR) {
               g.setColor(new Color(0, 0, 0, 200));
               int daysSurvived = WorldMapGUIData.getCurrentDay(this.savedGame);
               Text.setVariableText("days", daysSurvived);
               height = Text.getHeight(Text.getText("worldMapSelectionButton.survivedDay"), Text.FontType.BODY, 0);
               width = Text.getWidth(Text.getText("worldMapSelectionButton.survivedDay"), Text.FontType.BODY, 0);
               g.fillRoundRect(infoBarX - 5, infoBarY - 2, width + 15, height + 5, 4);
               Text.setVariableText("days", daysSurvived);
               if (daysSurvived == 1) {
                  this.font.drawString(infoBarX, infoBarY, Text.getText("worldMapSelectionButton.survivedDay"), Text.FontType.BODY, 0);
               } else {
                  this.font.drawString(infoBarX, infoBarY, Text.getText("worldMapSelectionButton.survivedDays"), Text.FontType.BODY, 0);
               }

               Text.setVariableText("amount", WorldMapGUIData.getVillagerList(this.savedGame).size());
               height = Text.getHeight(Text.getText("worldMapSelectionButton.population"), Text.FontType.BODY, 0);
               width = Text.getWidth(Text.getText("worldMapSelectionButton.population"), Text.FontType.BODY, 0);
               Text.setVariableText("amount", WorldMapGUIData.getVillagerList(this.savedGame).size());
               g.fillRoundRect(infoBarX - 5, infoBarY - 2 + 20, width + 15, height + 5, 4);
               this.font.drawString(infoBarX, infoBarY + 20, Text.getText("worldMapSelectionButton.population"), Text.FontType.BODY, 0);
               Text.setVariableText("amount", this.migration.getEnroutePopulation(this.savedGame.getMapFileName()));
               height = Text.getHeight(Text.getText("worldMapSelectionButton.migrantsEnRoute"), Text.FontType.BODY, 0);
               width = Text.getWidth(Text.getText("worldMapSelectionButton.migrantsEnRoute"), Text.FontType.BODY, 0);
               g.fillRoundRect(infoBarX - 5, infoBarY - 2 + 40, width + 15, height + 5, 4);
               Text.setVariableText("amount", this.migration.getEnroutePopulation(this.savedGame.getMapFileName()));
               this.font.drawString(infoBarX, infoBarY + 40, Text.getText("worldMapSelectionButton.migrantsEnRoute"), Text.FontType.BODY, 0);
            }
         } else {
            g.setColor(new Color(0, 0, 0, 125));
            int daysSurvived = WorldMapGUIData.getCurrentDay(this.savedGame);
            Text.setVariableText("days", daysSurvived);
            height = Text.getHeight(Text.getText("worldMapSelectionButton.lostToCorruption"), Text.FontType.BODY, 1) * 2;
            int var14 = 92;
            g.fillRoundRect(infoBarX - 10, infoBarY, var14 + 20, height + 10, 4);
            g.fillRoundRect(infoBarX - 5, infoBarY + 5, var14 + 10, height, 4);
            if (this.regionLost) {
               this.font.drawString(infoBarX + 46, infoBarY + 5, Text.getText("worldMapSelectionButton.lostToCorruption"), Text.FontType.BODY, 1, true);
            } else {
               this.font.drawString(infoBarX + 46, infoBarY + 5, Text.getText("worldMapSelectionButton.establishVillage"), Text.FontType.BODY, 1, true);
            }
         }

         g.resetTransform();
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }
   }

   public int getX() {
      return this.positionX;
   }

   public int getY() {
      return this.positionY;
   }
}
