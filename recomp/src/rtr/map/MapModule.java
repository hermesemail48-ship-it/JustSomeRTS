package rtr.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import r2d.image.SpriteSheet;
import rtr.DataModule;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.goal.GoalModule;
import rtr.gui.states.BrushLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.objects.rangebuilder.RangeBuilder;
import rtr.save.RegionalSavedGame;
import rtr.save.SavedGamesHandler;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class MapModule extends ModuleBase {
   public static final int LAYER_BASE_TERRAIN_BOTTOM = 0;
   public static final int LAYER_BASE_TERRAIN_TOP = 1;
   public static final int LAYER_TERRAIN_BOTTOM = 2;
   public static final int LAYER_TERRAIN_TOP = 3;
   public static final int LAYER_CORRUPTION = 4;
   public static final int LAYER_ROADS = 5;
   public static final int LAYER_LOWER_TOPOGRAPHY_BOTTOM = 6;
   public static final int LAYER_LOWER_TOPOGRAPHY_TOP = 7;
   public static final int LAYER_BRIDGES = 8;
   public static final int LAYER_UPPER_TOPOGRAPHY_BOTTOM = 9;
   public static final int LAYER_UPPER_TOPOGRAPHY_TOP = 10;
   public static final int LAYER_OBJECTS = 11;
   public static final int MAP_HEIGHT = 256;
   public static final int MAP_WIDTH = 256;
   public static final int MAP_LAYERS = 12;
   public static final int MAP_TILE_SIZE = 16;
   public static final int MAP_FULL_WIDTH = 4096;
   public static final int MAP_FULL_HEIGHT = 4096;
   public static final int MAP_FBO_TILE_SIZE = 256;
   public static final int MAP_MOVEMENT_BUFFER = 1025;
   private MapTilesLoader mtl;
   private MapData mapData;
   private MapThreatMap mapThreatMap;
   private int[][][] mapArray;
   private String mapName;
   private String mapAuthor;
   private String mapFileName;
   private MapTilesLoader.TileSet baseTerrain;
   public static final int MAXIMUM_REGION_CONNECTIONS = 8;
   private String[] connectedRegionList;
   private HashMap<String, OrderedPair> connectedRegionListWorldCoordinates;
   private HashMap<String, String> connectedRegionListWorldName;
   private HashMap<String, Image> connectedRegionListWorldMiniMaps;
   private HashMap<String, Boolean> connectedRegionListWorldLost;
   private int worldMapX = 512;
   private int worldMapY = 512;
   private int worldHeight;
   private int worldWidth = 0;
   private float mapX;
   private float mapY = 0.0F;
   private float mapMoveSpeedX;
   private float mapMoveSpeedY = 0.0F;
   private int layer = 1;
   private boolean accentModeLock = false;
   private boolean accentMode = false;
   private Rectangle eraseRectangle = new Rectangle(0.0F, 0.0F, 4.0F, 4.0F);
   private boolean screenShaking;
   private boolean screenShakeFlip;
   private int screenShakeDirection;
   private float screenShakeAmount;
   private boolean mapLost;
   private boolean mapStarted;
   private Image mapFBO;
   private SpriteSheet mapSheet;
   private Image[][] mapTiles;
   private boolean mapFBOLoaded;
   private int reloadSheet = 0;
   private byte[][] tileMap = new byte[3][3];

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      if (this.mtl == null) {
         this.mtl = new MapTilesLoader();
      }

      if (this.mapData == null) {
         this.mapData = new MapData();
      }

      if (this.mapThreatMap == null) {
         this.mapThreatMap = new MapThreatMap();
      }

      this.connectedRegionList = new String[8];
      this.connectedRegionListWorldCoordinates = new HashMap<>();
      this.connectedRegionListWorldName = new HashMap<>();
      this.connectedRegionListWorldMiniMaps = new HashMap<>();
      this.connectedRegionListWorldLost = new HashMap<>();
      this.requiresSync = true;
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.mapArray = null;
      this.mapData.reset();
      this.mapThreatMap.reset();
      this.mapName = "";
      this.mapAuthor = "";
      this.mapFileName = "";
      this.baseTerrain = null;
      this.connectedRegionList = new String[8];
      this.connectedRegionListWorldCoordinates.clear();
      this.connectedRegionListWorldName.clear();
      this.connectedRegionListWorldMiniMaps.clear();
      this.connectedRegionListWorldLost.clear();
      this.worldMapX = 512;
      this.worldMapY = 512;
      this.worldHeight = 0;
      this.worldWidth = 0;
      this.mapX = 0.0F;
      this.mapY = 0.0F;
      this.mapMoveSpeedX = 0.0F;
      this.mapMoveSpeedY = 0.0F;
      this.layer = 1;
      this.accentModeLock = false;
      this.accentMode = false;
      this.screenShaking = false;
      this.screenShakeFlip = false;
      this.screenShakeDirection = 0;
      this.screenShakeAmount = 0.0F;
      this.mapLost = false;
      this.mapStarted = false;
      if (this.mapFBO != null) {
         Graphics g = this.mapFBO.getGraphics();
         g.clearArea(0, 0, this.mapFBO.getWidth(), this.mapFBO.getHeight());
         this.mapFBOLoaded = false;
      }

      this.reloadSheet = 0;
   }

   @Override
   protected void newMainMenu() throws SlickException {
      this.loadRegionalSaveData();
   }

   @Override
   protected void newPlay() throws SlickException {
      this.loadRegionalSaveData();
   }

   @Override
   protected void loadPlay() throws SlickException {
      this.loadRegionalSaveData();
   }

   @Override
   protected void newMapEditor() throws SlickException {
      this.loadRegionalSaveData();
   }

   @Override
   protected void loadMapEditor() throws SlickException {
      this.loadRegionalSaveData();
   }

   @Override
   protected void newWorldMap() throws SlickException {
   }

   @Override
   protected void loadWorldMap() throws SlickException {
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(this.moduleType);
      this.mapName = dataMap.getOrDefault("mapName", "null");
      this.mapAuthor = dataMap.getOrDefault("mapAuthor", "null");
      this.mapFileName = dataMap.getOrDefault("mapFileName", "null");
      String baseTerrainString = dataMap.getOrDefault("baseTerrain", MapTilesLoader.TileSet.GRASS_GREEN.toString());

      MapTilesLoader.TileSet[] var6;
      for (MapTilesLoader.TileSet bT : MapTilesLoader.TileSet.values()) {
         if (bT.toString().equals(baseTerrainString)) {
            this.baseTerrain = bT;
            break;
         }
      }

      if (dataMap.containsMap("threatAmount")) {
         this.mapThreatMap.loadRegionalSaveData(dataMap.getSubMap("threatAmount"));
      }

      int[][][] getMap = this.save.getActiveRegionalSave().getMapData();
      if (getMap == null) {
         this.generateBlankMap();
      } else {
         this.mapArray = getMap;
      }

      YMLDataMap connectedRegionsDataMap = dataMap.getSubMap("connectedRegion");

      for (int x = 0; x < this.connectedRegionList.length; x++) {
         this.connectedRegionList[x] = connectedRegionsDataMap.getOrDefault(Integer.toString(x), "null");
      }

      this.createRegionalPositionData();
      this.worldMapX = Integer.parseInt(dataMap.getOrDefault("worldMapX", -2048));
      this.worldMapY = Integer.parseInt(dataMap.getOrDefault("worldMapY", -2048));
      if (this.save.getActiveRegionalSave().getGameMode() != GameModeTemplateBase.GameMode.MAP_EDITOR) {
         this.mapLost = Boolean.parseBoolean(dataMap.getOrDefault("mapLost", false));
         this.mapStarted = Boolean.parseBoolean(dataMap.getOrDefault("mapStarted", false));
      }

      if (!this.mapStarted && this.save.getActiveRegionalSave().getGameMode() != GameModeTemplateBase.GameMode.MAP_EDITOR) {
         this.mapX = -2048 + Utilities.randomInt(-256, 256);
         this.mapY = -2048 + Utilities.randomInt(-256, 256);
      } else {
         this.mapX = Float.parseFloat(dataMap.getOrDefault("mapX", 0));
         this.mapY = Float.parseFloat(dataMap.getOrDefault("mapY", 0));
      }

      this.mapData.buildInitialMap();
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   private void createRegionalPositionData() throws SlickException {
      this.connectedRegionListWorldCoordinates = new HashMap<>();

      for (int i = 0; i < 8; i++) {
         String connectedRegionName = this.connectedRegionList[i];
         if (!connectedRegionName.equals("null")) {
            String mapPackFolderName = "WorldMaps";
            RegionalSavedGame thisRegion = this.save.getActiveRegionalSave();
            RegionalSavedGame thisConnectedRegion = null;
            if (SavedGamesHandler.regionalSaveExist(mapPackFolderName, connectedRegionName, thisRegion.getGameType(), thisRegion.getGameMode())) {
               thisConnectedRegion = SavedGamesHandler.getRegionalSave(
                  mapPackFolderName, connectedRegionName, thisRegion.getGameType(), thisRegion.getGameMode()
               );
            } else {
               thisConnectedRegion = SavedGamesHandler.getRegionalSave(
                  mapPackFolderName, connectedRegionName, thisRegion.getGameType(), GameModeTemplateBase.GameMode.MAP_EDITOR
               );
            }

            YMLDataMap mapDataMap = thisConnectedRegion.getAndUnloadModuleSaveData(ModuleBase.ModuleType.MAP);
            int x = Integer.parseInt(mapDataMap.getOrDefault("worldMapX", 0));
            int y = Integer.parseInt(mapDataMap.getOrDefault("worldMapY", 0));
            this.connectedRegionListWorldCoordinates.put(connectedRegionName, new OrderedPair(x, y));
            this.connectedRegionListWorldName.put(connectedRegionName, mapDataMap.getOrDefault("mapName", "null"));
            this.connectedRegionListWorldLost.put(connectedRegionName, Boolean.parseBoolean(mapDataMap.getOrDefault("mapLost", false)));
            this.connectedRegionListWorldMiniMaps.put(connectedRegionName, thisConnectedRegion.getMiniMapImagePreview());
         }
      }
   }

   @Override
   public void initLoadSync() {
      this.synced = true;
      if (this.corruption.isInitialized()) {
         this.mapThreatMap.regenerateCorruptionTileThreat();
      }
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = this.getEditorSaveData();
      dataMap.put("threatAmount", this.mapThreatMap.getRegionalSaveData());
      if (this.mapLost) {
         dataMap.put("mapLost", this.mapLost);
      }

      if (this.mapStarted) {
         dataMap.put("mapStarted", this.mapStarted);
      }

      return dataMap;
   }

   @Override
   public YMLDataMap getEditorSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      dataMap.put("mapName", this.mapName);
      dataMap.put("mapFileName", this.mapFileName);
      dataMap.put("mapAuthor", this.mapAuthor);
      dataMap.put("baseTerrain", this.baseTerrain != null ? this.baseTerrain.toString() : "null");
      dataMap.put("mapX", this.mapX);
      dataMap.put("mapY", this.mapY);
      Map<String, Object> connectedRegionsDataMap = new HashMap<>();

      for (int x = 0; x < this.connectedRegionList.length; x++) {
         connectedRegionsDataMap.put(Integer.toString(x), this.connectedRegionList[x]);
      }

      dataMap.put("connectedRegion", connectedRegionsDataMap);
      dataMap.put("worldMapX", this.worldMapX);
      dataMap.put("worldMapY", this.worldMapY);
      return dataMap;
   }

   public void update() throws SlickException {
      this.worldWidth = ScaleControl.getWorldWidth();
      this.worldHeight = ScaleControl.getWorldHeight();
      if (this.corruption.isInitialized()) {
         this.mapThreatMap.update();
      }

      this.updateLoseConditions();
   }

   private void updateLoseConditions() throws SlickException {
      if (!this.mapLost
         && Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.MAP_EDITOR
         && Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.MAIN_MENU
         && this.mapStarted
         && this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false).size() == 0
         && this.mob.getMobsByGroup(MobBase.MobGroup.NOMAD, false).size() == 0
         && this.mob.getMobsByGroup(MobBase.MobGroup.MIGRANT, false).size() == 0
         && this.time.getDay() > 1) {
         this.mapLost = true;
         this.goal.incrementGoal(GoalModule.GoalType.GOD_DAMMIT);
         this.save.saveFullGame();

         try {
            Thread.sleep(10L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }

         System.gc();

         try {
            Thread.sleep(10L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }
   }

   public void updateNoSpeed() throws SlickException {
      if (this.debug) {
         this.reloadSheet++;
         if (this.reloadSheet >= 300) {
            Console.out("Reloading tilesheet", true);
            this.refreshFullMap();
            this.reloadSheet = 0;
            System.gc();
         }
      }

      if (this.screenShaking) {
         this.screenShake();
      }

      this.slowDownMap();
      this.miniMap.update();
   }

   public void render() throws SlickException {
      int startX = this.getMapRenderSheetTopLeftX() - 1;
      int startY = this.getMapRenderSheetTopLeftY() - 1;
      int endX = this.getMapRenderSheetBottomRightX() + 1;
      int endY = this.getMapRenderSheetBottomRightY() + 1;
      this.mapFBO.startUse();

      for (int x = startX; x < endX; x++) {
         for (int y = startY; y < endY; y++) {
            if (x >= 0 && x <= this.mapTiles.length - 1 && y >= 0 && y <= this.mapTiles[x].length - 1) {
               this.mapTiles[x][y].drawEmbedded(this.mapX + x * 256, this.mapY + y * 256);
            }
         }
      }

      this.mapFBO.endUse();
   }

   public void refreshFullMap() throws SlickException {
      if (this.mapFBO == null) {
         this.mapFBO = new Image(4096, 4096);
         this.mapSheet = new SpriteSheet(this.mapFBO, 256, 256);
         this.mapTiles = new Image[this.mapSheet.getHorizontalCount()][this.mapSheet.getVerticalCount()];

         for (int x = 0; x < this.mapTiles.length; x++) {
            for (int y = 0; y < this.mapTiles[x].length; y++) {
               this.mapTiles[x][y] = this.mapSheet.getSprite(x, y);
            }
         }
      }

      Graphics g = this.mapFBO.getGraphics();
      g.clear();

      for (int x = 0; x < 256; x++) {
         for (int y = 0; y < 256; y++) {
            for (int l = 0; l < 12; l++) {
               boolean transparent = false;
               if ((
                     this.data.getDisplayMode() == DataModule.DataDisplayMode.HIDE_TOPOGRAPHY
                        || this.data.getLockedDisplayMode() == DataModule.DataDisplayMode.HIDE_TOPOGRAPHY
                  )
                  && l >= 6) {
                  transparent = true;
               }

               if (l >= 1) {
                  g.setDrawMode(Graphics.MODE_NORMAL_IGNORE_ALPHA);
               } else {
                  g.setDrawMode(Graphics.MODE_NORMAL);
               }

               Image i = this.mtl.getTiles().get(this.mapArray[x][y][l]);
               if (i == null && this.mapArray[x][y][l] != 0) {
                  System.out.println("Failed to find tile " + this.mapArray[x][y][l] + "! May not be loaded, or exist. Deleting Tile.");
                  this.mapArray[x][y][l] = 0;
               } else if (this.mapArray[x][y][l] != 0) {
                  if (transparent) {
                     g.drawImage(i, x * 16, y * 16, new Color(255, 255, 255, 75));
                  } else {
                     g.drawImage(i, x * 16, y * 16);
                  }
               }
            }
         }
      }

      g.flush();
      this.mapFBO.flushPixelData();
      this.mapFBOLoaded = true;
   }

   public void refreshPartMap(int tileX, int tileY, int refreshArea) throws SlickException {
      if (this.mapFBO == null) {
         this.mapFBO = new Image(4096, 4096);
         this.mapSheet = new SpriteSheet(this.mapFBO, 256, 256);
         this.mapTiles = new Image[this.mapSheet.getHorizontalCount()][this.mapSheet.getVerticalCount()];

         for (int x = 0; x < this.mapTiles.length; x++) {
            for (int y = 0; y < this.mapTiles[x].length; y++) {
               this.mapTiles[x][y] = this.mapSheet.getSprite(x, y);
            }
         }
      }

      refreshArea /= 2;
      refreshArea += 3;
      int refreshStartX = tileX - refreshArea;
      int refreshStartY = tileY - refreshArea;
      int refreshEndX = tileX + refreshArea;
      int refreshEndY = tileY + refreshArea;
      Graphics g = this.mapFBO.getGraphics();
      g.clearArea(refreshStartX * 16, refreshStartY * 16, (1 + refreshArea * 2) * 16, (1 + refreshArea * 2) * 16);

      for (int x = refreshStartX; x <= refreshEndX; x++) {
         for (int y = refreshStartY; y <= refreshEndY; y++) {
            if (!Utilities.isOutOfBoundsOfUsableMap(x, y)) {
               for (int l = 0; l < 12; l++) {
                  boolean transparent = false;
                  if ((
                        this.data.getDisplayMode() == DataModule.DataDisplayMode.HIDE_TOPOGRAPHY
                           || this.data.getLockedDisplayMode() == DataModule.DataDisplayMode.HIDE_TOPOGRAPHY
                     )
                     && l >= 6) {
                     transparent = true;
                  }

                  if (l >= 1) {
                     g.setDrawMode(Graphics.MODE_NORMAL_IGNORE_ALPHA);
                  } else {
                     g.setDrawMode(Graphics.MODE_NORMAL);
                  }

                  if (this.mapArray[x][y][l] != 0) {
                     if (transparent) {
                        g.drawImage(this.mtl.getTiles().get(this.mapArray[x][y][l]), x * 16, y * 16, new Color(255, 255, 255, 75));
                     } else {
                        g.drawImage(this.mtl.getTiles().get(this.mapArray[x][y][l]), x * 16, y * 16);
                     }
                  }
               }
            }
         }
      }

      g.flush();
   }

   public boolean isMapFBOLoaded() {
      return this.mapFBOLoaded;
   }

   public Image getMapImage() throws SlickException {
      return this.mapFBO;
   }

   public void screenShake() {
      float amount = this.screenShakeAmount + (Utilities.randomFloat() - Utilities.randomFloat()) * 4.0F;
      if (this.screenShakeFlip) {
         this.screenShakeDirection = Utilities.randomInt(8);
         if (this.screenShakeDirection == 0) {
            this.mapX -= amount;
            this.mapY -= amount;
         } else if (this.screenShakeDirection == 1) {
            this.mapX += amount;
            this.mapY += amount;
         } else if (this.screenShakeDirection == 2) {
            this.mapX -= amount;
            this.mapY += amount;
         } else if (this.screenShakeDirection == 3) {
            this.mapX += amount;
            this.mapY -= amount;
         } else if (this.screenShakeDirection == 4) {
            this.mapX -= amount;
         } else if (this.screenShakeDirection == 5) {
            this.mapY -= amount;
         } else if (this.screenShakeDirection == 6) {
            this.mapX += amount;
         } else if (this.screenShakeDirection == 7) {
            this.mapY += amount;
         }
      } else {
         if (this.screenShakeDirection == 0) {
            this.mapX += amount;
            this.mapY += amount;
         } else if (this.screenShakeDirection == 1) {
            this.mapX -= amount;
            this.mapY -= amount;
         } else if (this.screenShakeDirection == 2) {
            this.mapX += amount;
            this.mapY -= amount;
         } else if (this.screenShakeDirection == 3) {
            this.mapX -= amount;
            this.mapY += amount;
         } else if (this.screenShakeDirection == 4) {
            this.mapX += amount;
         } else if (this.screenShakeDirection == 5) {
            this.mapY += amount;
         } else if (this.screenShakeDirection == 6) {
            this.mapX -= amount;
         } else if (this.screenShakeDirection == 7) {
            this.mapY -= amount;
         }

         this.screenShakeAmount -= 0.5F;
      }

      this.screenShakeFlip = !this.screenShakeFlip;
      if (this.screenShakeAmount <= 0.0F) {
         this.screenShakeAmount = 0.0F;
         this.screenShaking = false;
      }
   }

   public void setMap(String mN, String mA) throws SlickException {
      this.mapName = mN;
      this.mapAuthor = mA;
      this.mapFileName = this.mapName.replaceAll("[^a-zA-Z0-9]", "");
   }

   public void moveMapMouse(int mouseXPressed, int mouseYPressed, int mouseX, int mouseY) {
      float speed = (float)(0.1 - ScaleControl.getWorldScale() / 100.0F);
      if (!SettingsParser.getMapScrollInverted()) {
         this.mapMoveSpeedX = (mouseXPressed - mouseX) * speed;
         this.mapMoveSpeedY = (mouseYPressed - mouseY) * speed;
      } else {
         this.mapMoveSpeedX = (mouseXPressed - mouseX) * -1 * speed;
         this.mapMoveSpeedY = (mouseYPressed - mouseY) * -1 * speed;
      }

      this.moveMap(this.mapMoveSpeedX, this.mapMoveSpeedY);
   }

   public void moveMapKeyboard(float moveX, float moveY) {
      float speed = (float)(0.1 - ScaleControl.getWorldScale() / 100.0F);
      this.mapMoveSpeedX = moveX * speed;
      this.mapMoveSpeedY = moveY * speed;
      this.moveMap(this.mapMoveSpeedX, this.mapMoveSpeedY);
   }

   private void moveMap(float moveX, float moveY) {
      if ((!(this.mapX >= 1025.0F) || !(moveX > 0.0F)) && (!(this.mapX * -1.0F >= 4096 - this.worldWidth + 1025) || !(moveX < 0.0F))) {
         this.mapX = this.mapX + this.mapMoveSpeedX;
      }

      if ((!(this.mapY >= 1025.0F) || !(moveY > 0.0F)) && (!(this.mapY * -1.0F >= 4096 - this.worldHeight + 1025) || !(moveY < 0.0F))) {
         this.mapY = this.mapY + this.mapMoveSpeedY;
      }
   }

   public boolean isMoving() {
      return this.mapMoveSpeedX != 0.0F || this.mapMoveSpeedY != 0.0F;
   }

   private void slowDownMap() {
      if (!(this.mapX >= 1025.0F)) {
         if (!(this.mapX * -1.0F >= 4096 - this.worldWidth + 1025)) {
            this.mapX = this.mapX + this.mapMoveSpeedX;
         } else {
            this.mapMoveSpeedX = 0.0F;
         }
      }

      if (!(this.mapY >= 1025.0F)) {
         if (!(this.mapY * -1.0F >= 4096 - this.worldHeight + 1025)) {
            this.mapY = this.mapY + this.mapMoveSpeedY;
         } else {
            this.mapMoveSpeedY = 0.0F;
         }
      }

      if (this.mapMoveSpeedX < 0.6F && this.mapMoveSpeedX > -0.6F) {
         this.mapMoveSpeedX = 0.0F;
      } else {
         if (this.mapMoveSpeedX > 0.0F) {
            this.mapMoveSpeedX -= 0.6F;
         }

         if (this.mapMoveSpeedX < 0.0F) {
            this.mapMoveSpeedX += 0.6F;
         }
      }

      if (this.mapMoveSpeedY < 0.6F && this.mapMoveSpeedY > -0.6F) {
         this.mapMoveSpeedY = 0.0F;
      } else {
         if (this.mapMoveSpeedY > 0.0F) {
            this.mapMoveSpeedY -= 0.6F;
         }

         if (this.mapMoveSpeedY < 0.0F) {
            this.mapMoveSpeedY += 0.6F;
         }
      }
   }

   public void drawToMap(int tileX, int tileY, MapTilesLoader.TileSet tileSet, int brushSize, BrushLoader.BrushType brushType) throws SlickException {
      if ((!this.accentModeLock || this.accentMode) && (this.accentModeLock || !this.accentMode)) {
         this.drawTerrain(tileX, tileY, tileSet, brushSize, brushType);
      } else {
         this.drawTerrainAccent(tileX, tileY, tileSet, brushSize, brushType);
      }
   }

   public void drawToMap(int tileX, int tileY, MapTilesLoader.TileSet tileSet, int brushSize, BrushLoader.BrushType brushType, boolean forceAccent) throws SlickException {
      if (forceAccent) {
         this.drawTerrainAccent(tileX, tileY, tileSet, brushSize, brushType);
      } else {
         this.drawTerrain(tileX, tileY, tileSet, brushSize, brushType);
      }
   }

   public void drawRoad(int tileX, int tileY, MapTilesLoader.TileSet tileSet) throws SlickException {
      int tileRange = this.mtl.getTileSetGID(tileSet);
      this.mtl.checkAndloadTileset(this.mtl.getTileSetGID(tileSet));
      this.layer = this.mtl.getTileLayer(tileRange);
      if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
         if (this.mapArray[tileX][tileY][0] != 0 && this.mtl.getTileSetGID(tileSet) != this.mtl.getTileSetGID(this.mapArray[tileX][tileY][this.layer])) {
            this.mapArray[tileX][tileY][this.layer] = Utilities.randomInt(5) + tileRange;
         }

         this.updateMap(tileX, tileY, 1, false);
      }
   }

   public ObjectBase placeObject(int tileX, int tileY, MapTilesLoader.TileSet baseType, ObjectBase.ObjectSubType subType, boolean ignoreRangeMap) throws SlickException {
      return this.placeObject(tileX, tileY, baseType, subType, ignoreRangeMap, false);
   }

   public ObjectBase placeObject(
      int tileX, int tileY, MapTilesLoader.TileSet baseType, ObjectBase.ObjectSubType subType, boolean ignoreRangeMap, boolean silent
   ) throws SlickException {
      ObjectFlags objectFlags = this.object.getObjectFlagFactory().getObjectFlags(baseType, subType);
      int objectWidth = objectFlags.getWidth();
      int objectHeight = objectFlags.getHeight();
      int offsetX = objectWidth / 2;
      int offsetY = objectHeight / 2;
      if (this.canPlaceObject(tileX, tileY, baseType, ignoreRangeMap)) {
         if (!silent && this.object.getObjectFlagFactory().getObjectFlags(baseType, subType).getMobGroup() == MobBase.MobGroup.VILLAGER) {
            this.sound.playSound(SoundModule.SoundType.PLACE_OBJECT);
         }

         return this.object.addObject(tileX - offsetX, tileY - offsetY, baseType, subType);
      } else {
         return null;
      }
   }

   public void placeWall(int tileX, int tileY, MapTilesLoader.TileSet baseType, ObjectBase.ObjectSubType subType, boolean ignoreRangeMap) throws SlickException {
      int tileRange = this.mtl.getTileSetGID(baseType);
      this.mtl.checkAndloadTileset(this.mtl.getTileSetGID(baseType));
      this.layer = this.mtl.getTileLayer(tileRange);
      if (this.canPlaceObject(tileX, tileY, baseType, ignoreRangeMap)) {
         if (this.mapArray[tileX][tileY][0] != 0) {
            this.mapArray[tileX][tileY][this.layer] = Utilities.randomInt(25) + tileRange;
         }

         this.object.addObject(tileX, tileY, baseType, subType);
         this.sound.playSound(SoundModule.SoundType.PLACE_OBJECT);
         this.updateMap(tileX, tileY, 1, false);
      }
   }

   public boolean canPlaceObject(int tileX, int tileY, MapTilesLoader.TileSet baseType, boolean ignoreRangeMap) throws SlickException {
      this.mtl.checkAndloadTileset(this.mtl.getTileSetGID(baseType));
      int objectWidth = this.mtl.getTileWidth(this.mtl.getTileSetGID(baseType));
      int objectHeight = this.mtl.getTileHeight(this.mtl.getTileSetGID(baseType));
      int offsetX = objectWidth / 2;
      int offsetY = objectHeight / 2;
      int[][] rangeMap = this.object.getRangeMap(RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY);
      byte[][] blockMap = this.mapData.getBlockMap(MapData.BlockMapGroup.STANDARD);

      for (int w = 0; w < objectWidth; w++) {
         for (int h = 0; h < objectHeight; h++) {
            if (Utilities.isOutOfBoundsOfUsableMap(w + tileX - offsetX, h + tileY - offsetY)) {
               return false;
            }

            if (blockMap[w + tileX - offsetX][h + tileY - offsetY] == 1
               || this.object.isObject(w + tileX - offsetX, h + tileY - offsetY)
               || !this.map.canClearAtCoord(w + tileX - offsetX, h + tileY - offsetY) && this.mapArray[w + tileX - offsetX][h + tileY - offsetY][6] > 0
               || this.mapArray[w + tileX - offsetX][h + tileY - offsetY][11] > 0) {
               return false;
            }

            if (!ignoreRangeMap && rangeMap[w + tileX - offsetX][h + tileY - offsetY] <= 0) {
               return false;
            }
         }
      }

      return true;
   }

   public void updateObject(int tileX, int tileY, MapTilesLoader.TileSet objectBasePhase, MapTilesLoader.TileSetObjectPhase objectCurrentPhase) throws SlickException {
      int tileRange = 0;
      MapTilesLoader.TileSetType tileSetType = objectBasePhase.getTileSetTemplate().getType();
      if (tileSetType == MapTilesLoader.TileSetType.WALL) {
         switch (objectCurrentPhase) {
            case BASE:
               tileRange = this.mtl.getTileSetGID(objectBasePhase);
               break;
            case CONSTRUCTION_OUTLINE:
               tileRange = this.mtl.getTileSetGID(objectBasePhase.getWallOutline());
               break;
            case CONSTRUCTION_PHASE_5:
            case CONSTRUCTION_PHASE_4:
            case CONSTRUCTION_PHASE_3:
            case CONSTRUCTION_PHASE_2:
            case CONSTRUCTION_PHASE_1:
               tileRange = this.mtl.getTileSetGID(objectBasePhase.getWallConstruction());
         }
      } else if (objectCurrentPhase == null) {
         tileRange = this.mtl.getTileSetGID(objectBasePhase);
      } else {
         tileRange = this.mtl.getTileSetGID(objectBasePhase, objectCurrentPhase);
      }

      this.mtl.checkAndloadTileset(tileRange);
      this.layer = this.mtl.getTileLayer(tileRange);
      int objectWidth = 1;
      int objectHeight = 1;
      boolean clearTopography = false;
      if (tileSetType == MapTilesLoader.TileSetType.WALL) {
         this.mapArray[tileX][tileY][this.layer] = Utilities.randomInt(25) + tileRange;
         if (objectCurrentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_OUTLINE
            && objectCurrentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_PHASE_1
            && (this.mapArray[tileX][tileY][6] > 0 || this.mapArray[tileX][tileY][7] > 0)) {
            clearTopography = true;
         }
      } else {
         objectWidth = this.mtl.getTileWidth(this.mtl.getTileSetGID(tileRange));
         objectHeight = this.mtl.getTileHeight(this.mtl.getTileSetGID(tileRange));

         for (int w = 0; w < objectWidth; w++) {
            for (int h = 0; h < objectHeight; h++) {
               this.mapArray[w + tileX][h + tileY][this.layer] = tileRange + h * objectWidth + w;
               if (objectCurrentPhase != MapTilesLoader.TileSetObjectPhase.CONSTRUCTION_OUTLINE
                  && (this.mapArray[w + tileX][h + tileY][6] > 0 || this.mapArray[w + tileX][h + tileY][7] > 0)) {
                  clearTopography = true;
               }
            }
         }
      }

      if (clearTopography) {
         if (tileSetType == MapTilesLoader.TileSetType.WALL) {
            this.erase(tileX, tileY, MapModule.EraseMode.TOPOGRAPHY, 1, BrushLoader.BrushType.SQUARE);
         } else {
            this.eraseArea(tileX, tileY, tileX + objectWidth, tileY + objectHeight, MapModule.EraseMode.TOPOGRAPHY);
         }
      }

      this.updateMap(tileX, tileY, objectWidth + objectHeight, false);
   }

   public void removeObjectFromMap(ObjectBase o) throws SlickException {
      int objectWidth = o.getObjectFlags().getWidth();
      int objectHeight = o.getObjectFlags().getHeight();
      int tileX = o.getObjectX();
      int tileY = o.getObjectY();

      for (int w = 0; w < objectWidth; w++) {
         for (int h = 0; h < objectHeight; h++) {
            this.mapArray[w + tileX][h + tileY][11] = 0;
         }
      }

      this.updateMap(tileX, tileY, objectWidth + objectHeight, false);
   }

   public void fullTileSetSwap(MapTilesLoader.TileSet from, MapTilesLoader.TileSet to) throws SlickException {
      int fromGID = this.mtl.getTileSetGID(from);
      int toGID = this.mtl.getTileSetGID(to);
      this.mtl.checkAndloadTileset(fromGID);
      this.mtl.checkAndloadTileset(toGID);
      int layer = this.mtl.getTileLayer(fromGID);
      int shift = toGID - fromGID;
      int fromGID2 = this.mtl.getTileSetGID(from.getAccent());
      int toGID2 = this.mtl.getTileSetGID(to.getAccent());
      this.mtl.checkAndloadTileset(fromGID2);
      this.mtl.checkAndloadTileset(toGID2);
      int shift2 = toGID2 - fromGID2;

      for (int x = 0; x < 256; x++) {
         for (int y = 0; y < 256; y++) {
            boolean foundTile = false;
            if (this.mtl.getTileSetGID(this.mapArray[x][y][layer]) == fromGID) {
               this.mapArray[x][y][layer] = this.mapArray[x][y][layer] + shift;
               foundTile = true;
            }

            if (this.mtl.getTileSetGID(this.mapArray[x][y][layer + 1]) == fromGID2) {
               this.mapArray[x][y][layer + 1] = this.mapArray[x][y][layer + 1] + shift2;
               foundTile = true;
            }

            if (foundTile) {
               this.updateDataMaps(x, y, false);
            }
         }
      }

      this.refreshFullMap();
   }

   public void partialTileSetSwap(MapTilesLoader.TileSet from, MapTilesLoader.TileSet to, ArrayList<OrderedPair> swapArray, boolean updateMap) throws SlickException {
      int fromGID = this.mtl.getTileSetGID(from);
      int toGID = this.mtl.getTileSetGID(to);
      int layer = this.mtl.getTileLayer(fromGID);
      this.mtl.checkAndloadTileset(toGID);
      int shift = toGID - fromGID;
      int fromGID2 = this.mtl.getTileSetGID(from.getAccent());
      int toGID2 = this.mtl.getTileSetGID(to.getAccent());
      this.mtl.checkAndloadTileset(toGID2);
      int shift2 = toGID2 - fromGID2;

      for (OrderedPair o : swapArray) {
         int x = o.getX();
         int y = o.getY();
         if (this.mtl.getTileSetGID(this.mapArray[x][y][layer]) == fromGID) {
            this.mapArray[x][y][layer] = this.mapArray[x][y][layer] + shift;
         }

         if (this.mtl.getTileSetGID(this.mapArray[x][y][layer + 1]) == fromGID2) {
            this.mapArray[x][y][layer + 1] = this.mapArray[x][y][layer + 1] + shift2;
         }
      }

      for (OrderedPair o : swapArray) {
         int xx = o.getX();
         int yx = o.getY();

         for (int l = 0; l < 12; l++) {
            int tileID = this.mtl.getTileSetGID(this.mapArray[xx][yx][l]);
            if (tileID != 0) {
               if (l == 1 || l == 3 || l == 7 || l == 10) {
                  this.updateTerrainAccentEdgeCheck(xx, yx, l);
               }

               if (l != 5) {
                  this.updateTerrain(xx, yx, l);
               }
            }
         }

         this.updateDataMaps(xx, yx, false);
      }

      this.refreshFullMap();
   }

   public void drawTerrainCollected(int tileX, int tileY, MapTilesLoader.TileSet tileSet) throws SlickException {
      if (tileSet.getCollected() != null) {
         if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
            int tileRange = this.mtl.getTileSetGID(tileSet);
            this.mtl.checkAndloadTileset(this.mtl.getTileSetGID(tileSet));
            this.layer = this.mtl.getTileLayer(tileRange);
            this.mapArray[tileX][tileY][this.layer] = 0;
            tileRange = this.mtl.getTileSetGID(tileSet.getCollected());
            this.mtl.checkAndloadTileset(this.mtl.getTileSetGID(tileSet.getCollected()));
            this.layer = this.mtl.getTileLayer(tileRange);
            this.mapArray[tileX][tileY][this.layer] = Utilities.randomInt(25) + tileRange;
            this.updateMap(tileX, tileY, 1, false);
         }
      }
   }

   private void drawTerrain(int tileX, int tileY, MapTilesLoader.TileSet tileSet, int brushSize, BrushLoader.BrushType brushType) throws SlickException {
      int tileRange = this.mtl.getTileSetGID(tileSet);
      this.mtl.checkAndloadTileset(this.mtl.getTileSetGID(tileSet));
      if (tileSet == this.baseTerrain) {
         this.layer = this.mtl.getTileLayer(tileRange);
         this.erase(tileX, tileY, MapModule.EraseMode.TERRAIN_TOP_ONLY, brushSize, brushType);
         this.layer = 0;
      } else {
         this.layer = this.mtl.getTileLayer(tileRange);
         if (this.layer == 2 || this.layer == 3) {
            this.erase(tileX, tileY, MapModule.EraseMode.TERRAIN_BOTTOM_ONLY, brushSize, brushType);
         }
      }

      boolean drawSuccess = false;
      byte[][] currentBrush = Game.getCS().getBrushLoader().getBrush(brushSize, brushType);

      for (int x = 0; x < currentBrush.length; x++) {
         for (int y = 0; y < currentBrush[x].length; y++) {
            if (currentBrush[x][y] >= 1) {
               int brushX = x + tileX - currentBrush.length / 2;
               int brushY = y + tileY - currentBrush[x].length / 2;
               if (!Utilities.isOutOfBoundsOfUsableMap(brushX, brushY) && this.mapArray[brushX][brushY][0] != 0) {
                  if (this.mapArray[brushX][brushY][this.layer + 1] != 0) {
                     this.mapArray[brushX][brushY][this.layer + 1] = 0;
                     drawSuccess = true;
                  }

                  if (this.mtl.getTileSetGID(tileSet) != this.mtl.getTileSetGID(this.mapArray[brushX][brushY][this.layer])
                     && (!this.object.isObject(brushX, brushY) || this.layer != 6 && this.layer != 7 && this.layer != 9 && this.layer != 10)) {
                     if (this.layer == 9 || this.layer == 10) {
                        this.mapArray[brushX][brushY][6] = 0;
                        this.mapArray[brushX][brushY][7] = 0;
                     }

                     if (this.layer == 6 || this.layer == 7) {
                        this.mapArray[brushX][brushY][9] = 0;
                        this.mapArray[brushX][brushY][10] = 0;
                     }

                     this.mapArray[brushX][brushY][this.layer] = Utilities.randomInt(25) + tileRange;
                     drawSuccess = true;
                  }
               }
            }
         }
      }

      if (drawSuccess) {
         this.updateMap(tileX, tileY, brushSize, false);
      }
   }

   private void drawTerrainAccent(int tileX, int tileY, MapTilesLoader.TileSet tileSet, int brushSize, BrushLoader.BrushType brushType) throws SlickException {
      if (tileSet.getAccent() == null) {
         this.drawTerrain(tileX, tileY, tileSet, brushSize, brushType);
      } else {
         int tileRange = this.mtl.getTileSetGID(tileSet);
         int tileRangeAccent = this.mtl.getTileSetGID(tileSet.getAccent());
         this.mtl.checkAndloadTileset(this.mtl.getTileSetGID(tileSet));
         if (tileSet == this.baseTerrain) {
            this.layer = this.mtl.getTileLayer(tileRange);
            this.erase(tileX, tileY, MapModule.EraseMode.TERRAIN_TOP_ONLY, brushSize + 2, brushType);
            this.layer = 0;
         } else {
            this.layer = this.mtl.getTileLayer(tileRange);
            if (this.layer == 2 || this.layer == 3) {
               this.erase(tileX, tileY, MapModule.EraseMode.TERRAIN_BOTTOM_ONLY, brushSize + 2, brushType);
            }
         }

         boolean drawSuccess = false;
         byte[][] currentBrush = Game.getCS().getBrushLoader().getBrush(brushSize + 2, brushType);

         for (int x = 0; x < currentBrush.length; x++) {
            for (int y = 0; y < currentBrush[x].length; y++) {
               if (currentBrush[x][y] >= 1) {
                  int brushX = x + tileX - currentBrush.length / 2;
                  int brushY = y + tileY - currentBrush[x].length / 2;
                  if (!Utilities.isOutOfBoundsOfUsableMap(brushX, brushY)
                     && this.mapArray[brushX][brushY][0] != 0
                     && this.mtl.getTileSetGID(tileSet) != this.mtl.getTileSetGID(this.mapArray[brushX][brushY][this.layer])
                     && (!this.object.isObject(brushX, brushY) || this.layer != 6 && this.layer != 7 && this.layer != 9 && this.layer != 10)) {
                     if (this.layer == 9 || this.layer == 10) {
                        this.mapArray[brushX][brushY][6] = 0;
                        this.mapArray[brushX][brushY][7] = 0;
                     }

                     if (this.layer == 6 || this.layer == 7) {
                        this.mapArray[brushX][brushY][9] = 0;
                        this.mapArray[brushX][brushY][10] = 0;
                     }

                     this.mapArray[brushX][brushY][this.layer] = Utilities.randomInt(25) + tileRange;
                     drawSuccess = true;
                  }
               }
            }
         }

         byte[][] currentBrushAccent = Game.getCS().getBrushLoader().getBrush(brushSize, brushType);

         for (int x = 0; x < currentBrushAccent.length; x++) {
            label123:
            for (int yx = 0; yx < currentBrushAccent[x].length; yx++) {
               if (currentBrushAccent[x][yx] >= 1) {
                  int brushX = x + tileX - currentBrushAccent.length / 2;
                  int brushY = yx + tileY - currentBrushAccent[x].length / 2;
                  if (!Utilities.isOutOfBoundsOfUsableMap(brushX, brushY)
                     && this.mapArray[brushX][brushY][0] != 0
                     && this.mtl.getTileSetGID(tileSet.getAccent()) != this.mtl.getTileSetGID(this.mapArray[brushX][brushY][this.layer + 1])
                     && (!this.object.isObject(brushX, brushY) || this.layer != 6 && this.layer != 7 && this.layer != 9 && this.layer != 10)) {
                     if (this.layer == 9 || this.layer == 10) {
                        this.mapArray[brushX][brushY][6] = 0;
                        this.mapArray[brushX][brushY][7] = 0;
                     }

                     if (this.layer == 6 || this.layer == 7) {
                        this.mapArray[brushX][brushY][9] = 0;
                        this.mapArray[brushX][brushY][10] = 0;
                     }

                     for (int x1 = -1; x1 <= 1; x1++) {
                        for (int y1 = -1; y1 <= 1; y1++) {
                           if (this.mapArray[brushX + x1][brushY + y1][this.layer] == 0) {
                              continue label123;
                           }
                        }
                     }

                     this.mapArray[brushX][brushY][this.layer + 1] = Utilities.randomInt(25) + tileRangeAccent;
                     drawSuccess = true;
                  }
               }
            }
         }

         if (drawSuccess) {
            this.updateMap(tileX, tileY, brushSize, false);
         }
      }
   }

   public void erase(int tileX, int tileY, MapModule.EraseMode eraseMode, int brushSize, BrushLoader.BrushType brushType) throws SlickException {
      byte[][] currentBrush = Game.getCS().getBrushLoader().getBrush(brushSize, brushType);

      for (int x = 0; x < currentBrush.length; x++) {
         for (int y = 0; y < currentBrush[x].length; y++) {
            if (currentBrush[x][y] >= 1) {
               int brushX = x + tileX - currentBrush.length / 2;
               int brushY = y + tileY - currentBrush[x].length / 2;
               if (!Utilities.isOutOfBoundsOfUsableMap(brushX, brushY)) {
                  for (int l = 0; l < 12; l++) {
                     if (eraseMode == MapModule.EraseMode.ALL) {
                        if (l == 9) {
                           this.eraseRectangle.setX((brushX * 16 + this.mapX + 2.0F) * ScaleControl.getScalingDifference());
                           this.eraseRectangle.setY((brushY * 16 + this.mapY + 2.0F) * ScaleControl.getScalingDifference());
                           if (this.object.isObject(brushX, brushY)) {
                              ObjectBase objectErase = this.object.getObjectAt(this.eraseRectangle);
                              if (objectErase != null) {
                                 this.object.flagObjectForDestructionAndRemoval(objectErase);
                              }
                              continue;
                           }
                        }

                        if (l != 0) {
                           this.mapArray[brushX][brushY][l] = 0;
                        }
                     }

                     if (eraseMode == MapModule.EraseMode.TERRAIN && (l == 1 || l == 2 || l == 3)) {
                        this.mapArray[brushX][brushY][l] = 0;
                     }

                     if (eraseMode == MapModule.EraseMode.TERRAIN_BOTTOM_ONLY && l == 1) {
                        this.mapArray[brushX][brushY][l] = 0;
                     }

                     if (eraseMode == MapModule.EraseMode.TERRAIN_TOP_ONLY && (l == 2 || l == 3)) {
                        this.mapArray[brushX][brushY][l] = 0;
                     }

                     if (eraseMode == MapModule.EraseMode.CORRUPTION && l == 4) {
                        this.mapArray[brushX][brushY][l] = 0;
                     }

                     if (eraseMode == MapModule.EraseMode.TOPOGRAPHY && (l == 9 || l == 10 || l == 6 || l == 7)) {
                        this.mapArray[brushX][brushY][l] = 0;
                     }

                     if (eraseMode == MapModule.EraseMode.ROADS && l == 5) {
                        this.mapArray[brushX][brushY][l] = 0;
                     }
                  }

                  if (eraseMode == MapModule.EraseMode.OBJECTS) {
                     this.eraseRectangle.setX((brushX * 16 + this.mapX + 2.0F) * ScaleControl.getScalingDifference());
                     this.eraseRectangle.setY((brushY * 16 + this.mapY + 2.0F) * ScaleControl.getScalingDifference());
                     if (this.object.isObject(brushX, brushY)) {
                        ObjectBase objectErase = this.object.getObjectAt(this.eraseRectangle);
                        if (objectErase != null) {
                           this.object.flagObjectForDestructionAndRemoval(objectErase);
                        }
                     }
                  }
               }
            }
         }
      }

      this.updateMap(tileX, tileY, brushSize, false);
   }

   public void eraseArea(int startX, int startY, int endX, int endY, MapModule.EraseMode eraseMode) throws SlickException {
      for (int x = startX; x < endX; x++) {
         for (int y = startY; y < endY; y++) {
            if (!Utilities.isOutOfBoundsOfUsableMap(x, y)) {
               for (int l = 0; l < 12; l++) {
                  if (eraseMode == MapModule.EraseMode.ALL) {
                     if (l == 9) {
                        this.eraseRectangle.setX((x * 16 + this.mapX + 2.0F) * ScaleControl.getScalingDifference());
                        this.eraseRectangle.setY((y * 16 + this.mapY + 2.0F) * ScaleControl.getScalingDifference());
                        if (this.object.isObject(x, y)) {
                           ObjectBase objectErase = this.object.getObjectAt(this.eraseRectangle);
                           if (objectErase != null) {
                              this.object.flagObjectForDestructionAndRemoval(objectErase);
                           }
                           continue;
                        }
                     }

                     if (l != 0) {
                        this.mapArray[x][y][l] = 0;
                     }
                  }

                  if (eraseMode == MapModule.EraseMode.TERRAIN && (l == 1 || l == 2 || l == 3)) {
                     this.mapArray[x][y][l] = 0;
                  }

                  if (eraseMode == MapModule.EraseMode.CORRUPTION && l == 4) {
                     this.mapArray[x][y][l] = 0;
                  }

                  if (eraseMode == MapModule.EraseMode.TOPOGRAPHY && (l == 9 || l == 10 || l == 6 || l == 7)) {
                     this.mapArray[x][y][l] = 0;
                  }

                  if (eraseMode == MapModule.EraseMode.ROADS && l == 5) {
                     this.mapArray[x][y][l] = 0;
                  }
               }

               if (eraseMode == MapModule.EraseMode.OBJECTS) {
                  this.eraseRectangle.setX((x * 16 + this.mapX + 2.0F) * ScaleControl.getScalingDifference());
                  this.eraseRectangle.setY((y * 16 + this.mapY + 2.0F) * ScaleControl.getScalingDifference());
                  if (this.object.isObject(x, y)) {
                     ObjectBase objectErase = this.object.getObjectAt(this.eraseRectangle);
                     if (objectErase != null) {
                        this.object.flagObjectForDestructionAndRemoval(objectErase);
                     }
                  }
               }
            }
         }
      }

      this.updateMap(startX + (endX - startX) / 2, startY + (endY - startY) / 2, (endX - startX) / 2 + (endY - startY) / 2, false);
   }

   public void holeTerrain(int tileX, int tileY, int brushSize, BrushLoader.BrushType brushType) throws SlickException {
      byte[][] currentBrush = Game.getCS().getBrushLoader().getBrush(brushSize, brushType);

      for (int x = 0; x < currentBrush.length; x++) {
         for (int y = 0; y < currentBrush[x].length; y++) {
            if (currentBrush[x][y] >= 1) {
               int brushX = x + tileX - currentBrush.length / 2;
               int brushY = y + tileY - currentBrush[x].length / 2;
               if (!Utilities.isOutOfBoundsOfUsableMap(brushX, brushY) && !this.object.isObject(brushX, brushY)) {
                  for (int l = 0; l < 12; l++) {
                     this.mapArray[brushX][brushY][l] = 0;
                  }
               }
            }
         }
      }

      this.updateMap(tileX, tileY, brushSize, false);
   }

   public void patchTerrain(int tileX, int tileY, int brushSize, BrushLoader.BrushType brushType) throws SlickException {
      int tileRange = this.mtl.getTileSetGID(this.baseTerrain);
      byte[][] currentBrush = Game.getCS().getBrushLoader().getBrush(brushSize, brushType);

      for (int x = 0; x < currentBrush.length; x++) {
         for (int y = 0; y < currentBrush[x].length; y++) {
            if (currentBrush[x][y] >= 1) {
               int brushX = x + tileX - currentBrush.length / 2;
               int brushY = y + tileY - currentBrush[x].length / 2;
               if (!Utilities.isOutOfBoundsOfUsableMap(brushX, brushY) && !this.object.isObject(brushX, brushY)) {
                  this.mapArray[brushX][brushY][0] = Utilities.randomInt(25) + tileRange;
               }
            }
         }
      }

      this.updateMap(tileX, tileY, brushSize, false);
   }

   public void consumeResourceTerrain(int tileX, int tileY) throws SlickException {
      for (int l = 0; l < 12; l++) {
         if (this.mtl.getTileResourceType(this.mapArray[tileX][tileY][l]) != null) {
            MapTilesLoader.TileSet s = this.mtl.getTileSetByTileID(this.mapArray[tileX][tileY][l]);
            if (s.getCollected() == null) {
               System.out.println("fix this");
            }

            if (s.getCollected() != null) {
               this.drawTerrainCollected(tileX, tileY, s);
            }
         }
      }
   }

   public void killResourceTerrain(int tileX, int tileY) throws SlickException {
      if (this.mapAI.getRegrowMap()[tileX][tileY] != null || this.mapAI.getSpreadMap()[tileX][tileY] != null) {
         for (int l = 0; l < 12; l++) {
            if (this.mtl.getTileResourceType(this.mapArray[tileX][tileY][l]) != null) {
               MapTilesLoader.TileSet s = this.mtl.getTileSetByTileID(this.mapArray[tileX][tileY][l]);
               MapTilesLoader.TileSet dead = null;
               if (s.getDead() != null) {
                  dead = s.getDead();
               } else if (s.getCollected() != null) {
                  dead = s.getCollected();
               }

               if (dead != null) {
                  this.drawTerrain(tileX, tileY, dead, 1, BrushLoader.BrushType.ROUND);
               }
            }
         }
      }
   }

   public void spreadResourceTerrain(int tileX, int tileY, MapTilesLoader.TileSet tileSet, boolean accent) throws SlickException {
      if (accent) {
         this.drawTerrainAccent(tileX, tileY, tileSet, 1, BrushLoader.BrushType.ROUND);
      } else {
         this.drawTerrain(tileX, tileY, tileSet, 1, BrushLoader.BrushType.ROUND);
      }
   }

   public void regrowResourceTerrain(int tileX, int tileY, MapTilesLoader.TileSet tileSet) throws SlickException {
      this.drawTerrain(tileX, tileY, tileSet, 1, BrushLoader.BrushType.ROUND);
   }

   private void updateMap(int tileX, int tileY, int brushSize, boolean basicsOnly) throws SlickException {
      for (int loop = 0; loop < 2; loop++) {
         for (int x = 0; x < brushSize + 8; x++) {
            for (int y = 0; y < brushSize + 8; y++) {
               int checkX = tileX - x + (brushSize + 8) / 2;
               int checkY = tileY - y + (brushSize + 8) / 2;
               if (!Utilities.isOutOfBoundsOfUsableMap(checkX, checkY)) {
                  for (int l = 0; l < 12; l++) {
                     if (this.mapArray[checkX][checkY][l] > 0 && this.mtl.getTileType(this.mapArray[checkX][checkY][l]) != MapTilesLoader.TileSetType.OBJECT) {
                        if (l == 1 || l == 3 || l == 7 || l == 10) {
                           this.updateTerrainAccentEdgeCheck(checkX, checkY, l);
                        }

                        this.updateTerrain(checkX, checkY, l);
                     }
                  }
               }
            }
         }
      }

      for (int x = 0; x < brushSize + 8; x++) {
         for (int yx = 0; yx < brushSize + 8; yx++) {
            int checkX = tileX - x + (brushSize + 8) / 2;
            int checkY = tileY - yx + (brushSize + 8) / 2;
            if (!Utilities.isOutOfBoundsOfUsableMap(checkX, checkY)) {
               this.updateDataMaps(checkX, checkY, basicsOnly);
            }
         }
      }

      if (!basicsOnly) {
         this.refreshPartMap(tileX, tileY, brushSize);
         this.shadow.refreshPartMap(tileX, tileY, brushSize);
      }

      if (this.mob.isInitialized()) {
         this.mob.buildNewReachMaps();
      }
   }

   private void updateDataMaps(int tileX, int tileY, boolean basicsOnly) throws SlickException {
      this.mapData.updateMapData(tileX, tileY);
      if (this.collision.isInitialized()) {
         this.collision.updateCollisionMap(tileX, tileY);
      }

      if (this.depth.isInitialized()) {
         this.depth.updateDepthMap(tileX, tileY);
      }

      if (!basicsOnly) {
         this.miniMap.flagForUpdate(tileX, tileY);
         if (this.particle.isInitialized()) {
            this.particle.flagEmitterMapForUpdate(tileX, tileY);
         }

         if (this.resource.isInitialized()) {
            this.resource.updateResourceMap(tileX, tileY);
         }

         if (this.mapAI.isInitialized()) {
            this.mapAI.updateMapAI(tileX, tileY);
         }
      }
   }

   private void updateTerrainAccentEdgeCheck(int tileX, int tileY, int layer) {
      if (this.getTileId(tileX, tileY, layer) != 0) {
         int tileRange = this.mtl.getTileSetGID(this.getTileId(tileX, tileY, layer - 1));

         for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
               if (Math.abs(this.mapArray[tileX + x][tileY + y][layer - 1] - tileRange) > MapTilesLoader.TERRAIN_TILE_SUB) {
                  this.mapArray[tileX][tileY][layer] = 0;
                  return;
               }
            }
         }
      }
   }

   public void forceUpdateWholeMapTerrain(int[][][] mapData) {
      this.mapArray = mapData;

      for (int i = 0; i < 3; i++) {
         for (int x = 0; x < 256; x++) {
            for (int y = 0; y < 256; y++) {
               for (int l = 0; l < 12; l++) {
                  this.updateTerrain(x, y, l);
               }
            }
         }
      }
   }

   private void updateTerrain(int tileX, int tileY, int layer) {
      if (this.getTileId(tileX, tileY, layer) != 0) {
         int tileRange = this.mtl.getTileSetGID(this.getTileId(tileX, tileY, layer));
         this.tileMap[0][0] = (byte)(this.mtl.getTileSetGID(this.mapArray[tileX - 1][tileY - 1][layer]) == tileRange ? 1 : 0);
         this.tileMap[1][0] = (byte)(this.mtl.getTileSetGID(this.mapArray[tileX][tileY - 1][layer]) == tileRange ? 1 : 0);
         this.tileMap[2][0] = (byte)(this.mtl.getTileSetGID(this.mapArray[tileX + 1][tileY - 1][layer]) == tileRange ? 1 : 0);
         this.tileMap[0][1] = (byte)(this.mtl.getTileSetGID(this.mapArray[tileX - 1][tileY][layer]) == tileRange ? 1 : 0);
         this.tileMap[2][1] = (byte)(this.mtl.getTileSetGID(this.mapArray[tileX + 1][tileY][layer]) == tileRange ? 1 : 0);
         this.tileMap[0][2] = (byte)(this.mtl.getTileSetGID(this.mapArray[tileX - 1][tileY + 1][layer]) == tileRange ? 1 : 0);
         this.tileMap[1][2] = (byte)(this.mtl.getTileSetGID(this.mapArray[tileX][tileY + 1][layer]) == tileRange ? 1 : 0);
         this.tileMap[2][2] = (byte)(this.mtl.getTileSetGID(this.mapArray[tileX + 1][tileY + 1][layer]) == tileRange ? 1 : 0);
         if (this.tileMatch(1, 1, 1, 1, 1, 1, 1, 1)) {
            if (this.mapArray[tileX][tileY][layer] < tileRange || this.mapArray[tileX][tileY][layer] > tileRange + 24) {
               this.mapArray[tileX][tileY][layer] = Utilities.randomInt(25) + tileRange;
            }
         } else {
            int tilePosition = tileRange + 25;
            if (this.tileMatch(2, 0, 2, 1, 1, 1, 1, 1)) {
               if (this.mapArray[tileX][tileY][layer] < tilePosition || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                  this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
               }
            } else {
               tilePosition += 5;
               if (this.tileMatch(1, 1, 1, 1, 1, 2, 0, 2)) {
                  if (this.mapArray[tileX][tileY][layer] < tilePosition || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                     this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                  }
               } else {
                  tilePosition += 5;
                  if (this.tileMatch(2, 1, 1, 0, 1, 2, 1, 1)) {
                     if (this.mapArray[tileX][tileY][layer] < tilePosition || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                        this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                     }
                  } else {
                     tilePosition += 5;
                     if (this.tileMatch(1, 1, 2, 1, 0, 1, 1, 2)) {
                        if (this.mapArray[tileX][tileY][layer] < tilePosition || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                           this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                        }
                     } else {
                        tilePosition += 5;
                        if (this.tileMatch(2, 0, 2, 0, 1, 2, 1, 1)) {
                           if (this.mapArray[tileX][tileY][layer] < tilePosition || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                              this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                           }
                        } else {
                           tilePosition += 5;
                           if (this.tileMatch(2, 1, 1, 0, 1, 2, 0, 2)) {
                              if (this.mapArray[tileX][tileY][layer] < tilePosition || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                 this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                              }
                           } else {
                              tilePosition += 5;
                              if (this.tileMatch(2, 0, 2, 1, 0, 1, 1, 2)) {
                                 if (this.mapArray[tileX][tileY][layer] < tilePosition || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                    this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                 }
                              } else {
                                 tilePosition += 5;
                                 if (this.tileMatch(1, 1, 2, 1, 0, 2, 0, 2)) {
                                    if (this.mapArray[tileX][tileY][layer] < tilePosition || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                       this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                    }
                                 } else {
                                    tilePosition += 5;
                                    if (this.tileMatch(1, 1, 1, 1, 1, 1, 1, 0)) {
                                       if (this.mapArray[tileX][tileY][layer] < tilePosition || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                          this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                       }
                                    } else {
                                       tilePosition += 5;
                                       if (this.tileMatch(1, 1, 0, 1, 1, 1, 1, 1)) {
                                          if (this.mapArray[tileX][tileY][layer] < tilePosition || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                             this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                          }
                                       } else {
                                          tilePosition += 5;
                                          if (this.tileMatch(1, 1, 1, 1, 1, 0, 1, 1)) {
                                             if (this.mapArray[tileX][tileY][layer] < tilePosition || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                             }
                                          } else {
                                             tilePosition += 5;
                                             if (this.tileMatch(0, 1, 1, 1, 1, 1, 1, 1)) {
                                                if (this.mapArray[tileX][tileY][layer] < tilePosition || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                   this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                                }
                                             } else {
                                                tilePosition += 5;
                                                if (this.tileMatch(0, 1, 1, 1, 1, 1, 1, 0)) {
                                                   if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                      || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                      this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                                   }
                                                } else {
                                                   tilePosition += 5;
                                                   if (this.tileMatch(1, 1, 0, 1, 1, 0, 1, 1)) {
                                                      if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                         || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                         this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                                      }
                                                   } else {
                                                      tilePosition += 5;
                                                      if (this.tileMatch(0, 1, 0, 1, 1, 1, 1, 1)) {
                                                         if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                            || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                            this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                                         }
                                                      } else {
                                                         tilePosition += 5;
                                                         if (this.tileMatch(1, 1, 1, 1, 1, 0, 1, 0)) {
                                                            if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                               || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                               this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                                            }
                                                         } else {
                                                            tilePosition += 5;
                                                            if (this.tileMatch(0, 1, 1, 1, 1, 0, 1, 1)) {
                                                               if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                  || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                                  this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                                               }
                                                            } else {
                                                               tilePosition += 5;
                                                               if (this.tileMatch(1, 1, 0, 1, 1, 1, 1, 0)) {
                                                                  if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                     || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                                     this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                                                  }
                                                               } else {
                                                                  tilePosition += 5;
                                                                  if (this.tileMatch(0, 1, 2, 1, 0, 1, 1, 2)) {
                                                                     if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                        || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                                        this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                                                     }
                                                                  } else {
                                                                     tilePosition += 5;
                                                                     if (this.tileMatch(1, 1, 2, 1, 0, 0, 1, 2)) {
                                                                        if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                           || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                                           this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                                                        }
                                                                     } else {
                                                                        tilePosition += 5;
                                                                        if (this.tileMatch(2, 1, 0, 0, 1, 2, 1, 1)) {
                                                                           if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                              || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                                              this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                                                           }
                                                                        } else {
                                                                           tilePosition += 5;
                                                                           if (this.tileMatch(2, 1, 1, 0, 1, 2, 1, 0)) {
                                                                              if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                                 || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                                                 this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                                                              }
                                                                           } else {
                                                                              tilePosition += 5;
                                                                              if (this.tileMatch(2, 0, 2, 1, 1, 1, 1, 0)) {
                                                                                 if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                                    || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                                                    this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5) + tilePosition;
                                                                                 }
                                                                              } else {
                                                                                 tilePosition += 5;
                                                                                 if (this.tileMatch(1, 1, 0, 1, 1, 2, 0, 2)) {
                                                                                    if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                                       || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                                                       this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5)
                                                                                          + tilePosition;
                                                                                    }
                                                                                 } else {
                                                                                    tilePosition += 5;
                                                                                    if (this.tileMatch(2, 0, 2, 1, 1, 0, 1, 1)) {
                                                                                       if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                                          || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                                                          this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5)
                                                                                             + tilePosition;
                                                                                       }
                                                                                    } else {
                                                                                       tilePosition += 5;
                                                                                       if (this.tileMatch(0, 1, 1, 1, 1, 2, 0, 2)) {
                                                                                          if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                                             || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                                                             this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5)
                                                                                                + tilePosition;
                                                                                          }
                                                                                       } else {
                                                                                          tilePosition += 5;
                                                                                          if (this.tileMatch(0, 1, 0, 1, 1, 1, 1, 0)) {
                                                                                             if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                                                || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                                                                this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5)
                                                                                                   + tilePosition;
                                                                                             }
                                                                                          } else {
                                                                                             tilePosition += 5;
                                                                                             if (this.tileMatch(1, 1, 0, 1, 1, 0, 1, 0)) {
                                                                                                if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                                                   || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                                                                   this.mapArray[tileX][tileY][layer] = Utilities.randomInt(5)
                                                                                                      + tilePosition;
                                                                                                }
                                                                                             } else {
                                                                                                tilePosition += 5;
                                                                                                if (this.tileMatch(0, 1, 0, 1, 1, 0, 1, 1)) {
                                                                                                   if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                                                      || this.mapArray[tileX][tileY][layer] > tilePosition + 4) {
                                                                                                      this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                            5
                                                                                                         )
                                                                                                         + tilePosition;
                                                                                                   }
                                                                                                } else {
                                                                                                   tilePosition += 5;
                                                                                                   if (this.tileMatch(0, 1, 1, 1, 1, 0, 1, 0)) {
                                                                                                      if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                                                         || this.mapArray[tileX][tileY][layer]
                                                                                                            > tilePosition + 4) {
                                                                                                         this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                               5
                                                                                                            )
                                                                                                            + tilePosition;
                                                                                                      }
                                                                                                   } else {
                                                                                                      tilePosition += 5;
                                                                                                      if (this.tileMatch(2, 1, 2, 0, 0, 2, 1, 2)) {
                                                                                                         if (this.mapArray[tileX][tileY][layer] < tilePosition
                                                                                                            || this.mapArray[tileX][tileY][layer]
                                                                                                               > tilePosition + 4) {
                                                                                                            this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                  5
                                                                                                               )
                                                                                                               + tilePosition;
                                                                                                         }
                                                                                                      } else {
                                                                                                         tilePosition += 5;
                                                                                                         if (this.tileMatch(2, 0, 2, 1, 1, 2, 0, 2)) {
                                                                                                            if (this.mapArray[tileX][tileY][layer]
                                                                                                                  < tilePosition
                                                                                                               || this.mapArray[tileX][tileY][layer]
                                                                                                                  > tilePosition + 4) {
                                                                                                               this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                     5
                                                                                                                  )
                                                                                                                  + tilePosition;
                                                                                                            }
                                                                                                         } else {
                                                                                                            tilePosition += 5;
                                                                                                            if (this.tileMatch(2, 0, 2, 0, 1, 2, 1, 0)) {
                                                                                                               if (this.mapArray[tileX][tileY][layer]
                                                                                                                     < tilePosition
                                                                                                                  || this.mapArray[tileX][tileY][layer]
                                                                                                                     > tilePosition + 4) {
                                                                                                                  this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                        5
                                                                                                                     )
                                                                                                                     + tilePosition;
                                                                                                               }
                                                                                                            } else {
                                                                                                               tilePosition += 5;
                                                                                                               if (this.tileMatch(2, 1, 0, 0, 1, 2, 0, 2)) {
                                                                                                                  if (this.mapArray[tileX][tileY][layer]
                                                                                                                        < tilePosition
                                                                                                                     || this.mapArray[tileX][tileY][layer]
                                                                                                                        > tilePosition + 4) {
                                                                                                                     this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                           5
                                                                                                                        )
                                                                                                                        + tilePosition;
                                                                                                                  }
                                                                                                               } else {
                                                                                                                  tilePosition += 5;
                                                                                                                  if (this.tileMatch(2, 0, 2, 1, 0, 0, 1, 2)) {
                                                                                                                     if (this.mapArray[tileX][tileY][layer]
                                                                                                                           < tilePosition
                                                                                                                        || this.mapArray[tileX][tileY][layer]
                                                                                                                           > tilePosition + 4) {
                                                                                                                        this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                              5
                                                                                                                           )
                                                                                                                           + tilePosition;
                                                                                                                     }
                                                                                                                  } else {
                                                                                                                     tilePosition += 5;
                                                                                                                     if (this.tileMatch(0, 1, 2, 1, 0, 2, 0, 2)
                                                                                                                        )
                                                                                                                      {
                                                                                                                        if (this.mapArray[tileX][tileY][layer]
                                                                                                                              < tilePosition
                                                                                                                           || this.mapArray[tileX][tileY][layer]
                                                                                                                              > tilePosition + 4) {
                                                                                                                           this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                                 5
                                                                                                                              )
                                                                                                                              + tilePosition;
                                                                                                                        }
                                                                                                                     } else {
                                                                                                                        tilePosition += 5;
                                                                                                                        if (this.tileMatch(
                                                                                                                           2, 1, 0, 0, 1, 2, 1, 0
                                                                                                                        )) {
                                                                                                                           if (this.mapArray[tileX][tileY][layer]
                                                                                                                                 < tilePosition
                                                                                                                              || this.mapArray[tileX][tileY][layer]
                                                                                                                                 > tilePosition + 4) {
                                                                                                                              this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                                    5
                                                                                                                                 )
                                                                                                                                 + tilePosition;
                                                                                                                           }
                                                                                                                        } else {
                                                                                                                           tilePosition += 5;
                                                                                                                           if (this.tileMatch(
                                                                                                                              0, 1, 2, 1, 0, 0, 1, 2
                                                                                                                           )) {
                                                                                                                              if (this.mapArray[tileX][tileY][layer]
                                                                                                                                    < tilePosition
                                                                                                                                 || this.mapArray[tileX][tileY][layer]
                                                                                                                                    > tilePosition + 4) {
                                                                                                                                 this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                                       5
                                                                                                                                    )
                                                                                                                                    + tilePosition;
                                                                                                                              }
                                                                                                                           } else {
                                                                                                                              tilePosition += 5;
                                                                                                                              if (this.tileMatch(
                                                                                                                                 2, 0, 2, 1, 1, 0, 1, 0
                                                                                                                              )) {
                                                                                                                                 if (this.mapArray[tileX][tileY][layer]
                                                                                                                                       < tilePosition
                                                                                                                                    || this.mapArray[tileX][tileY][layer]
                                                                                                                                       > tilePosition + 4) {
                                                                                                                                    this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                                          5
                                                                                                                                       )
                                                                                                                                       + tilePosition;
                                                                                                                                 }
                                                                                                                              } else {
                                                                                                                                 tilePosition += 5;
                                                                                                                                 if (this.tileMatch(
                                                                                                                                    0, 1, 0, 1, 1, 2, 0, 2
                                                                                                                                 )) {
                                                                                                                                    if (this.mapArray[tileX][tileY][layer]
                                                                                                                                          < tilePosition
                                                                                                                                       || this.mapArray[tileX][tileY][layer]
                                                                                                                                          > tilePosition + 4) {
                                                                                                                                       this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                                             5
                                                                                                                                          )
                                                                                                                                          + tilePosition;
                                                                                                                                    }
                                                                                                                                 } else {
                                                                                                                                    tilePosition += 5;
                                                                                                                                    if (this.tileMatch(
                                                                                                                                       0, 1, 0, 1, 1, 0, 1, 0
                                                                                                                                    )) {
                                                                                                                                       if (this.mapArray[tileX][tileY][layer]
                                                                                                                                             < tilePosition
                                                                                                                                          || this.mapArray[tileX][tileY][layer]
                                                                                                                                             > tilePosition + 4
                                                                                                                                          )
                                                                                                                                        {
                                                                                                                                          this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                                                5
                                                                                                                                             )
                                                                                                                                             + tilePosition;
                                                                                                                                       }
                                                                                                                                    } else {
                                                                                                                                       tilePosition += 5;
                                                                                                                                       if (this.tileMatch(
                                                                                                                                          2,
                                                                                                                                          0,
                                                                                                                                          2,
                                                                                                                                          0,
                                                                                                                                          0,
                                                                                                                                          2,
                                                                                                                                          1,
                                                                                                                                          2
                                                                                                                                       )) {
                                                                                                                                          if (this.mapArray[tileX][tileY][layer]
                                                                                                                                                < tilePosition
                                                                                                                                             || this.mapArray[tileX][tileY][layer]
                                                                                                                                                > tilePosition
                                                                                                                                                   + 4) {
                                                                                                                                             this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                                                   5
                                                                                                                                                )
                                                                                                                                                + tilePosition;
                                                                                                                                          }
                                                                                                                                       } else {
                                                                                                                                          tilePosition += 5;
                                                                                                                                          if (this.tileMatch(
                                                                                                                                             2,
                                                                                                                                             1,
                                                                                                                                             2,
                                                                                                                                             0,
                                                                                                                                             0,
                                                                                                                                             2,
                                                                                                                                             0,
                                                                                                                                             2
                                                                                                                                          )) {
                                                                                                                                             if (this.mapArray[tileX][tileY][layer]
                                                                                                                                                   < tilePosition
                                                                                                                                                || this.mapArray[tileX][tileY][layer]
                                                                                                                                                   > tilePosition
                                                                                                                                                      + 4) {
                                                                                                                                                this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                                                      5
                                                                                                                                                   )
                                                                                                                                                   + tilePosition;
                                                                                                                                             }
                                                                                                                                          } else {
                                                                                                                                             tilePosition += 5;
                                                                                                                                             if (this.tileMatch(
                                                                                                                                                2,
                                                                                                                                                0,
                                                                                                                                                2,
                                                                                                                                                0,
                                                                                                                                                1,
                                                                                                                                                2,
                                                                                                                                                0,
                                                                                                                                                2
                                                                                                                                             )) {
                                                                                                                                                if (this.mapArray[tileX][tileY][layer]
                                                                                                                                                      < tilePosition
                                                                                                                                                   || this.mapArray[tileX][tileY][layer]
                                                                                                                                                      > tilePosition
                                                                                                                                                         + 4) {
                                                                                                                                                   this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                                                         5
                                                                                                                                                      )
                                                                                                                                                      + tilePosition;
                                                                                                                                                }
                                                                                                                                             } else {
                                                                                                                                                tilePosition += 5;
                                                                                                                                                if (this.tileMatch(
                                                                                                                                                   2,
                                                                                                                                                   0,
                                                                                                                                                   2,
                                                                                                                                                   1,
                                                                                                                                                   0,
                                                                                                                                                   2,
                                                                                                                                                   0,
                                                                                                                                                   2
                                                                                                                                                )) {
                                                                                                                                                   if (this.mapArray[tileX][tileY][layer]
                                                                                                                                                         < tilePosition
                                                                                                                                                      || this.mapArray[tileX][tileY][layer]
                                                                                                                                                         > tilePosition
                                                                                                                                                            + 4
                                                                                                                                                      )
                                                                                                                                                    {
                                                                                                                                                      this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                                                            5
                                                                                                                                                         )
                                                                                                                                                         + tilePosition;
                                                                                                                                                   }
                                                                                                                                                } else {
                                                                                                                                                   tilePosition += 5;
                                                                                                                                                   if (!this.tileMatch(
                                                                                                                                                      2,
                                                                                                                                                      0,
                                                                                                                                                      2,
                                                                                                                                                      0,
                                                                                                                                                      0,
                                                                                                                                                      2,
                                                                                                                                                      0,
                                                                                                                                                      2
                                                                                                                                                   )) {
                                                                                                                                                      tilePosition += 5;
                                                                                                                                                   } else {
                                                                                                                                                      if (this.mapArray[tileX][tileY][layer]
                                                                                                                                                            < tilePosition
                                                                                                                                                         || this.mapArray[tileX][tileY][layer]
                                                                                                                                                            > tilePosition
                                                                                                                                                               + 4
                                                                                                                                                         )
                                                                                                                                                       {
                                                                                                                                                         this.mapArray[tileX][tileY][layer] = Utilities.randomInt(
                                                                                                                                                               5
                                                                                                                                                            )
                                                                                                                                                            + tilePosition;
                                                                                                                                                      }
                                                                                                                                                   }
                                                                                                                                                }
                                                                                                                                             }
                                                                                                                                          }
                                                                                                                                       }
                                                                                                                                    }
                                                                                                                                 }
                                                                                                                              }
                                                                                                                           }
                                                                                                                        }
                                                                                                                     }
                                                                                                                  }
                                                                                                               }
                                                                                                            }
                                                                                                         }
                                                                                                      }
                                                                                                   }
                                                                                                }
                                                                                             }
                                                                                          }
                                                                                       }
                                                                                    }
                                                                                 }
                                                                              }
                                                                           }
                                                                        }
                                                                     }
                                                                  }
                                                               }
                                                            }
                                                         }
                                                      }
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean tileMatch(int tl, int tm, int tr, int ml, int mr, int bl, int bm, int br) {
      return (tl == 2 || this.tileMap[0][0] == tl)
         && (tm == 2 || this.tileMap[1][0] == tm)
         && (tr == 2 || this.tileMap[2][0] == tr)
         && (ml == 2 || this.tileMap[0][1] == ml)
         && (mr == 2 || this.tileMap[2][1] == mr)
         && (bl == 2 || this.tileMap[0][2] == bl)
         && (bm == 2 || this.tileMap[1][2] == bm)
         && (br == 2 || this.tileMap[2][2] == br);
   }

   public int[][][] getMap() {
      return this.mapArray;
   }

   public String getMapName() {
      return this.mapName;
   }

   public String getMapAuthor() {
      return this.mapAuthor;
   }

   public MapTilesLoader.TileSet getBaseTerrain() {
      return this.baseTerrain;
   }

   public String getFileName() {
      return this.mapFileName;
   }

   public float getMapX() {
      return this.mapX;
   }

   public float getMapY() {
      return this.mapY;
   }

   public int getMapRenderTopLeftX() {
      return (int)(this.mapX * -1.0F);
   }

   public int getMapRenderTopLeftY() {
      return (int)(this.mapY * -1.0F);
   }

   public int getMapRenderTopRightX() {
      return (int)(this.mapX * -1.0F + this.worldWidth);
   }

   public int getMapRenderTopRightY() {
      return (int)(this.mapY * -1.0F);
   }

   public int getMapRenderBottomLeftX() {
      return (int)(this.mapX * -1.0F);
   }

   public int getMapRenderBottomLeftY() {
      return (int)(this.mapY * -1.0F + this.worldHeight);
   }

   public int getMapRenderBottomRightX() {
      return (int)(this.mapX * -1.0F + this.worldWidth);
   }

   public int getMapRenderBottomRightY() {
      return (int)(this.mapY * -1.0F + this.worldHeight);
   }

   public int getMapRenderTileTopLeftX() {
      return (int)(this.mapX * -1.0F) / 16;
   }

   public int getMapRenderTileTopLeftY() {
      return (int)(this.mapY * -1.0F) / 16;
   }

   public int getMapRenderTileTopRightX() {
      return (int)(this.mapX * -1.0F + this.worldWidth) / 16;
   }

   public int getMapRenderTileTopRightY() {
      return (int)(this.mapY * -1.0F) / 16;
   }

   public int getMapRenderTileBottomLeftX() {
      return (int)(this.mapX * -1.0F) / 16;
   }

   public int getMapRenderTileBottomLeftY() {
      return (int)(this.mapY * -1.0F + this.worldHeight) / 16;
   }

   public int getMapRenderTileBottomRightX() {
      return (int)(this.mapX * -1.0F + this.worldWidth) / 16;
   }

   public int getMapRenderTileBottomRightY() {
      return (int)(this.mapY * -1.0F + this.worldHeight) / 16;
   }

   public int getMapRenderSheetTopLeftX() {
      return (int)(this.mapX * -1.0F) / 256;
   }

   public int getMapRenderSheetTopLeftY() {
      return (int)(this.mapY * -1.0F) / 256;
   }

   public int getMapRenderSheetTopRightX() {
      return (int)(this.mapX * -1.0F + this.worldWidth) / 256;
   }

   public int getMapRenderSheetTopRightY() {
      return (int)(this.mapY * -1.0F) / 256;
   }

   public int getMapRenderSheetBottomLeftX() {
      return (int)(this.mapX * -1.0F) / 256;
   }

   public int getMapRenderSheetBottomLeftY() {
      return (int)(this.mapY * -1.0F + this.worldHeight) / 256;
   }

   public int getMapRenderSheetBottomRightX() {
      return (int)(this.mapX * -1.0F + this.worldWidth) / 256;
   }

   public int getMapRenderSheetBottomRightY() {
      return (int)(this.mapY * -1.0F + this.worldHeight) / 256;
   }

   public int getTileId(int tileX, int tileY, int layer) {
      return this.mapArray[tileX][tileY][layer];
   }

   public void lockAccentMode() {
      this.accentModeLock = !this.accentModeLock;
   }

   public void accentModeOn() {
      this.accentMode = true;
   }

   public void accentModeOff() {
      this.accentMode = false;
   }

   public boolean getAccentMode() {
      return this.accentModeLock && !this.accentMode || !this.accentModeLock && this.accentMode;
   }

   public void realignMapPosition() {
      this.setMapPosition(this.mapX, this.mapY);
   }

   public void setMapPosition(float x, float y) {
      if (x > 1025.0F) {
         this.mapX = 1025.0F;
      } else if (x < -4096 + ScaleControl.getWorldWidth() - 1025) {
         this.mapX = -4096 + ScaleControl.getWorldWidth() - 1025;
      } else {
         this.mapX = x;
      }

      if (y > 1025.0F) {
         this.mapY = 1025.0F;
      } else if (y < -4096 + ScaleControl.getWorldHeight() - 1025) {
         this.mapY = -4096 + ScaleControl.getWorldHeight() - 1025;
      } else {
         this.mapY = y;
      }
   }

   public MapTilesLoader getMapTileLoader() {
      return this.mtl;
   }

   public MapData getMapData() {
      return this.mapData;
   }

   public MapThreatMap getMapThreatMap() {
      return this.mapThreatMap;
   }

   public boolean isTileFrozen(int checkX, int checkY) {
      for (int l = 11; l > 0; l--) {
         int tileID = this.getTileId(checkX, checkY, l);
         if (tileID != 0) {
            if (this.mtl.isTileFrozen(tileID)) {
               return true;
            }

            return false;
         }
      }

      return true;
   }

   public boolean canClearAtCoord(int checkX, int checkY) {
      for (int l = 0; l < 12; l++) {
         int tileID = this.getTileId(checkX, checkY, l);
         boolean checkTile = this.mtl.canClear(tileID);
         if (checkTile) {
            return true;
         }
      }

      return false;
   }

   public boolean canDigAtCoord(int checkX, int checkY) {
      if (this.getTileId(checkX, checkY, 0) == 0) {
         return false;
      } else {
         for (int l = 0; l < 12; l++) {
            int tileID = this.getTileId(checkX, checkY, l);
            if (tileID != 0 && l >= 5 || this.mtl.isTileInvulnerable(tileID)) {
               return false;
            }
         }

         return true;
      }
   }

   public String[] getConnectedRegionList() {
      return this.connectedRegionList;
   }

   public void setConnectedRegionList(int slot, String mapName) {
      this.connectedRegionList[slot] = mapName;
   }

   public int getConnectedRegionListWorldMapX(int slot) {
      return this.connectedRegionListWorldCoordinates.get(this.connectedRegionList[slot]).getX();
   }

   public int getConnectedRegionListWorldMapX(String mapName) {
      return this.connectedRegionListWorldCoordinates.get(mapName).getX();
   }

   public int getConnectedRegionListWorldMapY(int slot) {
      return this.connectedRegionListWorldCoordinates.get(this.connectedRegionList[slot]).getY();
   }

   public int getConnectedRegionListWorldMapY(String mapName) {
      return this.connectedRegionListWorldCoordinates.get(mapName).getY();
   }

   public String getConnectedRegionListWorldName(int slot) {
      return this.connectedRegionListWorldName.get(this.connectedRegionList[slot]);
   }

   public String getConnectedRegionListWorldName(String mapName) {
      return this.connectedRegionListWorldName.get(mapName);
   }

   public Image getConnectedRegionListWorldMiniMaps(int slot) {
      return this.connectedRegionListWorldMiniMaps.get(this.connectedRegionList[slot]);
   }

   public Image getConnectedRegionListWorldMiniMaps(String mapName) {
      return this.connectedRegionListWorldMiniMaps.get(mapName);
   }

   public boolean getConnectedRegionListWorldLost(int slot) {
      return this.connectedRegionListWorldLost.get(this.connectedRegionList[slot]);
   }

   public boolean getConnectedRegionListWorldLost(String mapName) {
      return this.connectedRegionListWorldLost.get(mapName);
   }

   public int getWorldMapX() {
      return this.worldMapX;
   }

   public void setWorldMapX(int i) {
      this.worldMapX = i;
   }

   public int getWorldMapY() {
      return this.worldMapY;
   }

   public void setWorldMapY(int i) {
      this.worldMapY = i;
   }

   private void generateBlankMap() throws SlickException {
      byte[][] generationArray = new byte[256][256];
      int minX = 0;
      int maxX = 255;
      int minY = 0;
      int maxY = 255;

      for (int x = 0; x < 256; x++) {
         for (int y = 0; y < 256; y++) {
            if (x <= minX || x >= maxX || y <= minY || y >= maxY) {
               generationArray[x][y] = 1;
            }

            if (Utilities.randomInt(64) == 0 && (x < minX + 6 || x >= maxX - 6 || y < minY + 6 || y >= maxY - 6)) {
               generationArray[x][y] = 1;
            }
         }
      }

      for (int loop = 0; loop < 15; loop++) {
         for (int x = 0; x < generationArray.length; x++) {
            for (int y = 0; y < generationArray[x].length; y++) {
               if (generationArray[x][y] == 1) {
                  if (Utilities.randomInt(10) == 0 && x - 1 >= 0 && y - 1 >= 0 && generationArray[x - 1][y - 1] == 0) {
                     generationArray[x - 1][y - 1] = 2;
                  }

                  if (Utilities.randomInt(10) == 0 && y - 1 >= 0 && generationArray[x][y - 1] == 0) {
                     generationArray[x][y - 1] = 2;
                  }

                  if (Utilities.randomInt(10) == 0 && x + 1 <= 255 && y - 1 >= 0 && generationArray[x + 1][y - 1] == 0) {
                     generationArray[x + 1][y - 1] = 2;
                  }

                  if (Utilities.randomInt(10) == 0 && x - 1 >= 0 && generationArray[x - 1][y] == 0) {
                     generationArray[x - 1][y] = 2;
                  }

                  if (Utilities.randomInt(10) == 0 && x + 1 <= 255 && generationArray[x + 1][y] == 0) {
                     generationArray[x + 1][y] = 2;
                  }

                  if (Utilities.randomInt(10) == 0 && x - 1 >= 0 && y + 1 <= 255 && generationArray[x - 1][y + 1] == 0) {
                     generationArray[x - 1][y + 1] = 2;
                  }

                  if (Utilities.randomInt(10) == 0 && y + 1 <= 255 && generationArray[x][y + 1] == 0) {
                     generationArray[x][y + 1] = 2;
                  }

                  if (Utilities.randomInt(10) == 0 && x + 1 <= 255 && y + 1 <= 255 && generationArray[x + 1][y + 1] == 0) {
                     generationArray[x + 1][y + 1] = 2;
                  }
               }
            }
         }

         if (loop < 10) {
            for (int x = 0; x < generationArray.length; x++) {
               for (int yx = 0; yx < generationArray[x].length; yx++) {
                  if (generationArray[x][yx] == 2) {
                     generationArray[x][yx] = 1;
                  }
               }
            }
         }
      }

      this.mapArray = new int[256][256][12];

      for (int x = 0; x < generationArray.length; x++) {
         for (int yxx = 0; yxx < generationArray[x].length; yxx++) {
            if (generationArray[x][yxx] >= 1) {
               this.mapArray[x][yxx][0] = 0;
            } else {
               this.mapArray[x][yxx][0] = Utilities.randomInt(25) + this.mtl.getTileSetGID(this.baseTerrain);
            }
         }
      }

      for (int x = 0; x < this.mapArray.length; x++) {
         for (int yxxx = 0; yxxx < this.mapArray[x].length; yxxx++) {
            if (this.mapArray[x][yxxx][0] > 0) {
               int count = 0;
               if (this.mapArray[x + 1][yxxx][0] == 0) {
                  count++;
               }

               if (this.mapArray[x - 1][yxxx][0] == 0) {
                  count++;
               }

               if (this.mapArray[x][yxxx + 1][0] == 0) {
                  count++;
               }

               if (this.mapArray[x][yxxx - 1][0] == 0) {
                  count++;
               }

               if (count >= 3) {
                  this.mapArray[x][yxxx][0] = 0;
               }
            }
         }
      }

      for (int x = 0; x < generationArray.length; x++) {
         for (int yxxxx = 0; yxxxx < generationArray[x].length; yxxxx++) {
            if (generationArray[x][yxxxx] == 2) {
               this.updateMap(x, yxxxx, 1, true);
            }
         }
      }

      generationArray = null;
   }

   public boolean isInView(float x, float y, int buffer) {
      return this.isInView((int)x, (int)y, buffer);
   }

   public boolean isInView(int x, int y, int buffer) {
      int displayWidth = ScaleControl.getWorldWidth();
      int displayHeight = ScaleControl.getWorldHeight();
      return x + this.mapX > -buffer && x + this.mapX < displayWidth + buffer && y + this.mapY > -buffer && y + this.mapY < displayHeight + buffer;
   }

   public void initSreenShake(float i) {
      if (SettingsParser.getScreenshakeEnabled()) {
         this.screenShaking = true;
         if (this.screenShakeAmount < 5.0F) {
            this.screenShakeAmount += i;
         }

         if (this.screenShakeAmount > 5.0F) {
            this.screenShakeAmount = 5.0F;
         }
      }
   }

   public static int getCenterPixelOfCoordinateX(int x) {
      return x * 16 + 8;
   }

   public static int getCenterPixelOfCoordinateY(int y) {
      return y * 16 + 8;
   }

   public boolean isMapLost() {
      return this.mapLost;
   }

   public boolean isMapStarted() {
      return this.mapStarted;
   }

   public void setMapStarted(boolean b) {
      this.mapStarted = b;
   }

   public int getTileCount(MapTilesLoader.TileSet t) {
      return this.getTileCount(t, false);
   }

   public int getTileCount(MapTilesLoader.TileSet t, boolean inRangeOnly) {
      int layer = 0;
      if (this.baseTerrain != t) {
         layer = t.getTileSetTemplate().getLayer();
      }

      int GID = this.mtl.getTileSetGID(t);
      int count = 0;

      for (int x = 0; x < 256; x++) {
         for (int y = 0; y < 256; y++) {
            if (this.mtl.getTileSetGID(this.mapArray[x][y][layer]) == GID && (!inRangeOnly || this.object.isInRange(x, y))) {
               count++;
            }
         }
      }

      if (t.getAccent() != null) {
         if (this.baseTerrain != t) {
            layer = t.getAccent().getTileSetTemplate().getLayer() + 1;
         } else {
            layer = 1;
         }

         GID = this.mtl.getTileSetGID(t.getAccent());

         for (int x = 0; x < 256; x++) {
            for (int yx = 0; yx < 256; yx++) {
               if (this.mtl.getTileSetGID(this.mapArray[x][yx][layer]) == GID && (!inRangeOnly || this.object.isInRange(x, yx))) {
                  count++;
               }
            }
         }
      }

      return count;
   }

   public static enum EraseMode {
      ALL,
      TERRAIN,
      TERRAIN_BOTTOM_ONLY,
      TERRAIN_TOP_ONLY,
      TOPOGRAPHY,
      OBJECTS,
      ROADS,
      CORRUPTION;
   }
}
