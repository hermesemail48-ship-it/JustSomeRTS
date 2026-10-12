package rtr.gui.states.worldmap;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.gui.buttons.GUIButtonWorldMapSelection;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.gui.states.WorldMapGUIPanelBase;
import rtr.mobs.MobBase;
import rtr.save.RegionalSavedGame;
import rtr.save.SavedGamesHandler;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.Utilities;

public class WorldMap extends WorldMapGUIPanelBase {
   private Image worldMap;
   ArrayList<String> mapNames = new ArrayList<>();
   private ArrayList<MobDot> worldMapMobDotsList = new ArrayList<>();
   private ArrayList<Building> worldMapBuildingsList = new ArrayList<>();
   private byte[][] buildingPositions;
   private byte[][] worldMapBlockMap;
   private ArrayList<GUIButtonWorldMapSelection> worldMapButtons = new ArrayList<>();
   private GUIButtonWorldMapSelection selectedWorldMapButton;
   private float mapX;
   private float mapY;
   private float mapMoveSpeedX;
   private float mapMoveSpeedY;
   private boolean worldMapFlashCoolDown;
   private boolean worldMapFlash;
   private float worldMapFlashAlpha = 0.0F;

   public WorldMap(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.worldMap = ImageLoader.getImage("res/GUI/worldMap/worldMap.png");
      this.mapX = this.worldMap.getWidth() / 2 * -1 + ScaleControl.getWorldHeight() / 2;
      this.mapY = this.worldMap.getHeight() / 2 * -1 + ScaleControl.getWorldWidth() / 2;
      this.generateBlockMap();
      WorldMapGUIData.refreshData();
      if (SettingsParser.getLastGameMode() == null) {
         controller.toggleGameModeSelectOverlayPanel();
      } else if (SettingsParser.getLastGameMode() == GameModeTemplateBase.GameMode.CUSTOM && !WorldMapGUIData.hasCustomConfig()) {
         controller.toggleGameModeConfigPanel();
      } else {
         this.buildWorldMap();
         this.initFlash();
      }
   }

   public void render(boolean debug) throws SlickException {
      this.worldMap.draw(this.mapX, this.mapY, new Color(1.0F, 1.0F, 1.0F, this.worldMapFlashAlpha));
      this.g.glBeginQuads();

      for (MobDot m : this.worldMapMobDotsList) {
         m.render(this.g, this.mapX, this.mapY);
      }

      this.g.glEnd();

      for (Building b : this.worldMapBuildingsList) {
         b.render(this.mapX, this.mapY);
      }

      if (!this.controller.isGameModeSelectOverlayPanelEnabled() && !this.controller.isGameModeConfigPanelEnabled()) {
         for (int i = 0; i < this.worldMapButtons.size(); i++) {
            this.worldMapButtons.get(i).render(this.g, this.mouse, this.mapX, this.mapY, debug);
         }
      }

      for (int i = 0; i < this.worldMapButtons.size(); i++) {
         this.worldMapButtons.get(i).renderDescriptionToolTip(this.mouse, this.g, this.mapX, this.mapY);
      }
   }

   @Override
   public void update() {
      int worldWidth = ScaleControl.getWorldWidth();
      int worldHeight = ScaleControl.getWorldHeight();
      if (this.selectedWorldMapButton != null) {
         this.mapMoveSpeedX = 0.0F;
         this.mapMoveSpeedY = 0.0F;
         RegionalSavedGame mapSave = this.controller.getMapSelectPanel().getMapSave();
         float x;
         float xSpeed;
         float y;
         float ySpeed;
         if (mapSave != null
            && mapSave.getGameMode() != GameModeTemplateBase.GameMode.MAP_EDITOR
            && Boolean.parseBoolean(mapSave.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapStarted", false))
            && !Boolean.parseBoolean(mapSave.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapLost", false))) {
            x = worldWidth / 2 + 124.0F / ScaleControl.getScalingDifference();
            xSpeed = Math.abs(this.mapX + this.selectedWorldMapButton.getX() - (worldWidth / 2 + 124.0F / ScaleControl.getScalingDifference())) / 10.0F;
            y = 295.0F / ScaleControl.getScalingDifference();
            ySpeed = Math.abs(this.mapY + this.selectedWorldMapButton.getY() - 295.0F / ScaleControl.getScalingDifference()) / 10.0F;
         } else {
            x = worldWidth / 2 - 14.0F / ScaleControl.getScalingDifference();
            xSpeed = Math.abs(this.mapX + this.selectedWorldMapButton.getX() - (worldWidth / 2 - 14.0F / ScaleControl.getScalingDifference())) / 10.0F;
            y = 295.0F / ScaleControl.getScalingDifference();
            ySpeed = Math.abs(this.mapY + this.selectedWorldMapButton.getY() - 295.0F / ScaleControl.getScalingDifference()) / 10.0F;
         }

         if (this.mapX + this.selectedWorldMapButton.getX() > x) {
            this.mapX -= xSpeed;
         }

         if (this.mapX + this.selectedWorldMapButton.getX() < x) {
            this.mapX += xSpeed;
         }

         if (this.mapY + this.selectedWorldMapButton.getY() > y) {
            this.mapY -= ySpeed;
         }

         if (this.mapY + this.selectedWorldMapButton.getY() < y) {
            this.mapY += ySpeed;
         }

         if (xSpeed < 0.3F && ySpeed < 0.3F) {
            this.controller.getMapSelectPanel().fadeIn();
         }
      } else {
         if (!(this.mapX >= 128.0F) && !(this.mapX * -1.0F >= this.worldMap.getWidth() - worldWidth + 128)) {
            this.mapX = this.mapX + this.mapMoveSpeedX;
         }

         if (!(this.mapY >= 128.0F) && !(this.mapY * -1.0F >= this.worldMap.getHeight() - worldHeight + 128)) {
            this.mapY = this.mapY + this.mapMoveSpeedY;
         }

         if (this.mapMoveSpeedX < 0.4F && this.mapMoveSpeedX > -0.4F) {
            this.mapMoveSpeedX = 0.0F;
         } else {
            if (this.mapMoveSpeedX > 0.0F) {
               this.mapMoveSpeedX -= 0.4F;
            }

            if (this.mapMoveSpeedX < 0.0F) {
               this.mapMoveSpeedX += 0.4F;
            }
         }

         if (this.mapMoveSpeedY < 0.4F && this.mapMoveSpeedY > -0.4F) {
            this.mapMoveSpeedY = 0.0F;
         } else {
            if (this.mapMoveSpeedY > 0.0F) {
               this.mapMoveSpeedY -= 0.4F;
            }

            if (this.mapMoveSpeedY < 0.0F) {
               this.mapMoveSpeedY += 0.4F;
            }
         }
      }

      if (this.worldMapFlash) {
         if (!this.worldMapFlashCoolDown) {
            this.worldMapFlashAlpha -= 0.05F;
            if (this.worldMapFlashAlpha <= -0.5F) {
               this.worldMapFlashCoolDown = true;
            }
         } else {
            this.worldMapFlashAlpha += 0.01F;
            if (this.worldMapFlashAlpha >= 1.0F) {
               this.worldMapFlash = false;
               this.worldMapFlashCoolDown = false;
            }
         }
      }

      for (MobDot m : this.worldMapMobDotsList) {
         m.update();
      }
   }

   public boolean intersectsWorldMap(String buttonName) {
      for (int i = 0; i < this.worldMapButtons.size(); i++) {
         if (this.worldMapButtons.get(i).intersects(this.mouse) && this.worldMapButtons.get(i).getButtonName().equals(buttonName)) {
            return true;
         }
      }

      return false;
   }

   public void selectWorldMap(String buttonName) {
      for (int i = 0; i < this.worldMapButtons.size(); i++) {
         if (this.worldMapButtons.get(i).getButtonName().equals(buttonName)) {
            this.worldMapButtons.get(i).select();
            this.selectedWorldMapButton = this.worldMapButtons.get(i);
         } else {
            this.worldMapButtons.get(i).deselect();
         }
      }
   }

   public void deselectAllWorldMap() {
      for (int i = 0; i < this.worldMapButtons.size(); i++) {
         this.worldMapButtons.get(i).deselect();
      }

      this.selectedWorldMapButton = null;
   }

   public void buildWorldMap() throws SlickException {
      this.worldMapMobDotsList.clear();
      this.worldMapButtons.clear();
      this.mapNames.clear();
      this.worldMapBuildingsList.clear();
      this.buildingPositions = new byte[this.worldMap.getWidth()][this.worldMap.getHeight()];
      this.selectedWorldMapButton = null;
      String mapPackFolderName = "WorldMaps";
      File[] listOfMapFolders = new File("maps/" + mapPackFolderName).listFiles();

      for (int i = 0; i < listOfMapFolders.length; i++) {
         if (listOfMapFolders[i].isFile() && listOfMapFolders[i].getName().endsWith(".zip")) {
            String mapFileName = listOfMapFolders[i].getName().replace(".zip", "");
            RegionalSavedGame savedGame = null;
            if (SavedGamesHandler.regionalSaveExist(
               mapPackFolderName, listOfMapFolders[i].getName().replace(".zip", ""), StateBase.GameType.WORLD_MAP, WorldMapGUIData.getSelectedGameMode()
            )) {
               savedGame = SavedGamesHandler.getRegionalSave(
                  mapPackFolderName, mapFileName, StateBase.GameType.WORLD_MAP, WorldMapGUIData.getSelectedGameMode()
               );
            } else {
               savedGame = SavedGamesHandler.getRegionalSave(
                  mapPackFolderName, mapFileName, StateBase.GameType.WORLD_MAP, GameModeTemplateBase.GameMode.MAP_EDITOR
               );
            }

            String mapName = savedGame.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapName", "null");
            GUIButtonWorldMapSelection button = new GUIButtonWorldMapSelection(savedGame);
            this.worldMapButtons.add(button);
            this.addMobDots(button.getX(), button.getY(), "WorldMaps", mapFileName);
            this.mapNames.add(mapName);
         }
      }

      int monsterCount = 200;

      for (int ix = 0; ix < monsterCount; ix++) {
         int dotX = Utilities.randomInt(this.worldMap.getWidth());

         int dotY;
         for (dotY = Utilities.randomInt(this.worldMap.getHeight());
            this.worldMapBlockMap[dotX][dotY] > 0;
            dotY = Utilities.randomInt(this.worldMap.getHeight())
         ) {
            dotX = Utilities.randomInt(this.worldMap.getWidth());
         }

         this.worldMapMobDotsList.add(new MobDot(dotX, dotY, this.worldMapBlockMap, Color.red));
      }
   }

   private void addMobDots(int centerX, int centerY, String mapPack, String mapName) throws SlickException {
      if (SavedGamesHandler.regionalSaveExist(mapPack, mapName, StateBase.GameType.WORLD_MAP, WorldMapGUIData.getSelectedGameMode())) {
         RegionalSavedGame s = SavedGamesHandler.getRegionalSave(mapPack, mapName, StateBase.GameType.WORLD_MAP, WorldMapGUIData.getSelectedGameMode());
         ArrayList<MobBase> villagers = WorldMapGUIData.getVillagerList(s);
         int villagerCount = villagers.size() / 3;

         for (int i = 0; i < villagerCount; i++) {
            this.worldMapMobDotsList.add(new MobDot(centerX, centerY, this.worldMapBlockMap, Color.cyan));
         }

         ArrayList<MobBase> monsters = WorldMapGUIData.getMonsterList(s);
         int monsterCount = monsters.size() / 3;

         for (int i = 0; i < monsterCount; i++) {
            this.worldMapMobDotsList.add(new MobDot(centerX, centerY, this.worldMapBlockMap, Color.red));
         }

         int buildingCount = WorldMapGUIData.getObjectCount(s) / 10 + 1;
         if (buildingCount > 30) {
            buildingCount = 30;
         }

         Random random = new Random(centerX + centerY + this.profile.getProfileName().length());
         int range = 64;

         label137:
         for (int i = 0; i < buildingCount; i++) {
            int attempts = 0;
            Building b;
            if (i == 0 && WorldMapGUIData.getCampType(s) != -1) {
               b = new Building(random, true, WorldMapGUIData.getCampType(s));
            } else {
               b = new Building(random, false, -1);
            }

            byte[][] collisionMap = b.getCollisionMap();
            int buildingX = centerX + (random.nextInt(range) - random.nextInt(range));
            int buildingY = centerY + (random.nextInt(range) - random.nextInt(range));

            while (this.worldMapBlockMap[buildingX][buildingY] > 1 || this.buildingPositions[buildingX][buildingY] == 1) {
               attempts++;
               buildingX = centerX + (random.nextInt(range) - random.nextInt(range));
               buildingY = centerY + (random.nextInt(range) - random.nextInt(range));
               if (attempts > 5000) {
                  range += 4;
                  continue label137;
               }
            }

            if (attempts < 5000) {
               for (int x1 = 0; x1 < collisionMap.length; x1++) {
                  for (int y1 = 0; y1 < collisionMap[x1].length; y1++) {
                     if ((
                              this.worldMapBlockMap[buildingX + x1][buildingY + y1] > 1
                                 || this.worldMapBlockMap[buildingX + x1 + 1][buildingY + y1] > 1
                                 || this.worldMapBlockMap[buildingX + x1 - 1][buildingY + y1] > 1
                                 || this.worldMapBlockMap[buildingX + x1][buildingY + y1 + 1] > 1
                                 || this.worldMapBlockMap[buildingX + x1][buildingY + y1 - 1] > 1
                           )
                           && collisionMap[x1][y1] > 0
                        || this.buildingPositions[buildingX + x1][buildingY + y1] == 1) {
                        buildingX = centerX + (random.nextInt(range) - random.nextInt(range));
                        buildingY = centerY + (random.nextInt(range) - random.nextInt(range));

                        while (
                           (
                                    this.worldMapBlockMap[buildingX + x1][buildingY + y1] > 1
                                       || this.worldMapBlockMap[buildingX + x1 + 1][buildingY + y1] > 1
                                       || this.worldMapBlockMap[buildingX + x1 - 1][buildingY + y1] > 1
                                       || this.worldMapBlockMap[buildingX + x1][buildingY + y1 + 1] > 1
                                       || this.worldMapBlockMap[buildingX + x1][buildingY + y1 - 1] > 1
                                 )
                                 && collisionMap[x1][y1] > 0
                              || this.buildingPositions[buildingX + x1][buildingY + y1] == 1
                        ) {
                           attempts++;
                           buildingX = centerX + (random.nextInt(range) - random.nextInt(range));
                           buildingY = centerY + (random.nextInt(range) - random.nextInt(range));
                           if (attempts > 5000) {
                              range += 4;
                              continue label137;
                           }
                        }

                        x1 = 0;
                        y1 = 0;
                     }
                  }
               }
            }

            if (attempts < 5000) {
               b.setLocation(buildingX, buildingY);

               for (int x1 = 0; x1 < collisionMap.length; x1++) {
                  for (int y1x = 0; y1x < collisionMap[x1].length; y1x++) {
                     if (collisionMap[x1][y1x] == 1) {
                        this.buildingPositions[buildingX + x1][buildingY + y1x] = collisionMap[x1][y1x];
                     }
                  }
               }

               this.worldMapBuildingsList.add(b);
            }
         }
      }
   }

   private void generateBlockMap() throws SlickException {
      Image blockedMap = ImageLoader.getImage("res/GUI/worldMap/worldMapBlocked.png");
      this.worldMapBlockMap = new byte[blockedMap.getWidth()][blockedMap.getHeight()];

      for (int x = 0; x < blockedMap.getWidth(); x++) {
         for (int y = 0; y < blockedMap.getHeight(); y++) {
            if (blockedMap.getColor(x, y).equals(Color.black)) {
               this.worldMapBlockMap[x][y] = 2;
            } else if (blockedMap.getColor(x, y).equals(new Color(100, 100, 100))) {
               this.worldMapBlockMap[x][y] = 1;
            }
         }
      }

      blockedMap.flushPixelData();
   }

   public void initFlash() {
      this.worldMapFlash = true;
      this.worldMapFlashCoolDown = false;
   }

   public void moveMapMouse(int mouseXPressed, int mouseYPressed, int mouseX, int mouseY) {
      if (this.selectedWorldMapButton == null) {
         if (!SettingsParser.getMapScrollInverted()) {
            this.mapMoveSpeedX = (mouseXPressed - mouseX) * 0.08F;
            this.mapMoveSpeedY = (mouseYPressed - mouseY) * 0.08F;
         } else {
            this.mapMoveSpeedX = (mouseXPressed - mouseX) * -1 * 0.08F;
            this.mapMoveSpeedY = (mouseYPressed - mouseY) * -1 * 0.08F;
         }

         this.moveMap(this.mapMoveSpeedX, this.mapMoveSpeedY);
      }
   }

   public void moveMapKeyboard(float moveX, float moveY) {
      if (this.selectedWorldMapButton == null) {
         this.mapMoveSpeedX = moveX * 0.06F;
         this.mapMoveSpeedY = moveY * 0.06F;
         this.moveMap(this.mapMoveSpeedX, this.mapMoveSpeedY);
      }
   }

   private void moveMap(float moveX, float moveY) {
      int worldWidth = ScaleControl.getWorldWidth();
      int worldHeight = ScaleControl.getWorldHeight();
      if ((!(this.mapX >= 128.0F) || !(moveX > 0.0F)) && (!(this.mapX * -1.0F >= this.worldMap.getWidth() - worldWidth + 128) || !(moveX < 0.0F))) {
         this.mapX = this.mapX + this.mapMoveSpeedX;
      }

      if ((!(this.mapY >= 128.0F) || !(moveY > 0.0F)) && (!(this.mapY * -1.0F >= this.worldMap.getHeight() - worldHeight + 128) || !(moveY < 0.0F))) {
         this.mapY = this.mapY + this.mapMoveSpeedY;
      }
   }

   public void realignMapPosition() {
      this.setMapPosition(this.mapX, this.mapY);
   }

   public void setMapPosition(float x, float y) {
      if (x > 256.0F) {
         this.mapX = 256.0F;
      } else if (x < this.worldMap.getWidth() * -1 + ScaleControl.getWorldWidth() - 128) {
         this.mapX = this.worldMap.getWidth() * -1 + ScaleControl.getWorldWidth() - 128;
      } else {
         this.mapX = x;
      }

      if (y > 256.0F) {
         this.mapY = 256.0F;
      } else if (y < this.worldMap.getHeight() * -1 + ScaleControl.getWorldHeight() - 128) {
         this.mapY = this.worldMap.getHeight() * -1 + ScaleControl.getWorldHeight() - 128;
      } else {
         this.mapY = y;
      }
   }

   public float getMapX() {
      return this.mapX;
   }

   public float getMapY() {
      return this.mapY;
   }

   public ArrayList<String> getWorldMapList() {
      return this.mapNames;
   }
}
