package rtr.map.ai;

import rtr.utilities.JavaCompat;
import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.console.Console;
import rtr.goal.GoalModule;
import rtr.gui.states.BrushLoader;
import rtr.map.MapTilesLoader;
import rtr.map.RegrowTile;
import rtr.map.SpreadTile;
import rtr.mobs.essence.EssenceBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class MapAIModule extends ModuleBase {
   private final int ESSENCE_BUILDUP_RATE = 50;
   private final int ESSENCE_BUILDUP_CHANCE = 30;
   private final int ESSENCE_DECAY_RATE = 1000;
   private final int ESSENCE_RATE_VARIANCE = 10;
   private final int ESSENCE_FLOWER_SPAWN_CHANCE = 5000;
   private final int ESSENCE_RANDOM_FLOWER_CHANCE = 100;
   private final int ESSENCE_BUILDUP_MINIMUM_FLOWERS = 100;
   private final int ESSENCE_BUILDUP_MAXIMUM = 125;
   private RegrowTile[][] regrowListMap;
   private SpreadTile[][] spreadListMap;
   private ArrayList<RegrowTile> regrowListArray = new ArrayList<>();
   private ArrayList<SpreadTile> spreadListArray = new ArrayList<>();
   private ArrayList<RegrowTile> regrowListAdd = new ArrayList<>();
   private ArrayList<SpreadTile> spreadListAdd = new ArrayList<>();
   private ArrayList<RegrowTile> regrowListRemove = new ArrayList<>();
   private ArrayList<SpreadTile> spreadListRemove = new ArrayList<>();
   private ArrayList<MapTilesLoader.TileSet> validFlowers;
   private int essenceBuildupTerrainTick = 0;
   private int essenceBuildupObjectsTick = 0;
   private int essenceBuildupFlowersTick = 0;
   private int essenceDecayTick = 0;
   private int[][] essenceBuildupMap;
   private int mapWidth;
   private int mapHeight;
   private int mapLayers;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.mapWidth = 256;
      this.mapHeight = 256;
      this.mapLayers = 12;
      if (this.regrowListMap == null) {
         this.regrowListMap = new RegrowTile[this.mapWidth][this.mapHeight];
      }

      if (this.spreadListMap == null) {
         this.spreadListMap = new SpreadTile[this.mapWidth][this.mapHeight];
      }

      if (this.essenceBuildupMap == null) {
         this.essenceBuildupMap = new int[this.mapWidth][this.mapHeight];
      }

      if (this.validFlowers == null) {
         this.validFlowers = new ArrayList<>();
      }

      this.buildInitialMap();
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();

      for (int x = 0; x < this.mapWidth; x++) {
         for (int y = 0; y < this.mapHeight; y++) {
            this.regrowListMap[x][y] = null;
            this.spreadListMap[x][y] = null;
            this.essenceBuildupMap[x][y] = 0;
         }
      }

      this.regrowListArray.clear();
      this.spreadListArray.clear();
      this.regrowListAdd.clear();
      this.spreadListAdd.clear();
      this.regrowListRemove.clear();
      this.spreadListRemove.clear();
      this.validFlowers.clear();
      this.essenceBuildupTerrainTick = 0;
      this.essenceBuildupObjectsTick = 0;
      this.essenceBuildupFlowersTick = 0;
      this.essenceDecayTick = 0;
      this.mapWidth = 0;
      this.mapHeight = 0;
      this.mapLayers = 0;
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
   }

   @Override
   protected void loadPlay() throws SlickException {
      this.loadRegionalSaveData();
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
      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(ModuleBase.ModuleType.MAP_AI);
      if (dataMap == null) {
         Console.saveConverterOut(this, "Update 1c", "Update 2 Unstable 2", "Map AI Save Data does not exist in this save version. Resetting to defaults.");
      } else {
         this.essenceBuildupTerrainTick = Integer.parseInt(dataMap.getOrDefault("essenceBuildupTerrainTick", 0));
         this.essenceBuildupObjectsTick = Integer.parseInt(dataMap.getOrDefault("essenceBuildupObjectsTick", 0));
         this.essenceBuildupFlowersTick = Integer.parseInt(dataMap.getOrDefault("essenceBuildupFlowersTick", 0));
         this.essenceDecayTick = Integer.parseInt(dataMap.getOrDefault("essenceDecayTick", 0));
         if (dataMap.containsKey("essenceBuildupMap")) {
            dataMap.unpackCoordinatesListWithValueToArray("essenceBuildupMap", this.essenceBuildupMap);
         }
      }
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      if (this.essenceBuildupTerrainTick != 0) {
         dataMap.put("essenceBuildupTerrainTick", this.essenceBuildupTerrainTick);
      }

      if (this.essenceBuildupObjectsTick != 0) {
         dataMap.put("essenceBuildupObjectsTick", this.essenceBuildupObjectsTick);
      }

      if (this.essenceBuildupFlowersTick != 0) {
         dataMap.put("essenceBuildupFlowersTick", this.essenceBuildupFlowersTick);
      }

      if (this.essenceDecayTick != 0) {
         dataMap.put("essenceDecayTick", this.essenceDecayTick);
      }

      ArrayList<OrderedPair> essenceBuildupMapCoordinatesList = new ArrayList<>();
      ArrayList<Integer> essenceBuildupMapValuesList = new ArrayList<>();

      for (int x = 0; x < this.essenceBuildupMap.length; x++) {
         for (int y = 0; y < this.essenceBuildupMap[x].length; y++) {
            if (this.essenceBuildupMap[x][y] != 0) {
               essenceBuildupMapCoordinatesList.add(OrderedPair.getOrderedPair(x, y));
               essenceBuildupMapValuesList.add(this.essenceBuildupMap[x][y]);
            }
         }
      }

      if (essenceBuildupMapCoordinatesList.size() > 0) {
         String essenceBuildupMapAmountString = dataMap.packCoordinatesListWithValue(essenceBuildupMapCoordinatesList, essenceBuildupMapValuesList);
         dataMap.put("essenceBuildupMap", essenceBuildupMapAmountString);
      }

      return dataMap;
   }

   private void buildInitialMap() throws SlickException {
      MapTilesLoader.TileSet[] tileID;
      for (MapTilesLoader.TileSet s : tileID = MapTilesLoader.TileSet.values()) {
         if (s.toString().startsWith("FLOWERS_")) {
            this.map.getMapTileLoader().checkAndloadTileset(s.getTileSetTemplate().getTileGID());
            this.validFlowers.add(s);
         }
      }

      this.essenceBuildupTerrainTick = 0;
      this.essenceBuildupObjectsTick = 0;
      this.essenceBuildupFlowersTick = 0;
      this.essenceDecayTick = 0;

      for (int x = 0; x < this.mapWidth; x++) {
         for (int y = 0; y < this.mapHeight; y++) {
            for (int l = 0; l < this.mapLayers; l++) {
               int tileIDx = this.map.getTileId(x, y, l);
               if (tileIDx != 0) {
                  if (this.map.getMapTileLoader().canTileRegrow(tileIDx) != null) {
                     RegrowTile regrowTile = new RegrowTile(
                        OrderedPair.getOrderedPair(x, y),
                        this.map.getMapTileLoader().canTileRegrow(tileIDx),
                        this.map.getMapTileLoader().getRegrowRate(tileIDx),
                        this.map.getMapTileLoader().getRegrowOnlyMotivated(tileIDx),
                        this.map.getMapTileLoader().getRegrowIgnoreWeather(tileIDx)
                     );
                     this.regrowListArray.add(regrowTile);
                     this.regrowListMap[x][y] = regrowTile;
                  }

                  if (this.map.getMapTileLoader().canTileSpread(tileIDx) != null) {
                     SpreadTile spreadTile = new SpreadTile(
                        OrderedPair.getOrderedPair(x, y),
                        this.map.getMapTileLoader().canTileSpread(tileIDx),
                        l,
                        this.map.getMapTileLoader().getSpreadRate(tileIDx),
                        this.map.getMapTileLoader().getSpreadOnlyMotivated(tileIDx),
                        this.map.getMapTileLoader().getSpreadIgnoreWeather(tileIDx)
                     );
                     this.spreadListArray.add(spreadTile);
                     this.spreadListMap[x][y] = spreadTile;
                  }
               }
            }
         }
      }
   }

   private static final int TILE_UPDATE_STRIDE = 16;
   private int tileUpdatePhase;

   public void update() throws SlickException {
      for (SpreadTile s : this.spreadListAdd) {
         this.spreadListArray.add(s);
         this.spreadListMap[s.getSpreadX()][s.getSpreadY()] = s;
      }

      for (RegrowTile r : this.regrowListAdd) {
         this.regrowListArray.add(r);
         this.regrowListMap[r.getRegrowX()][r.getRegrowY()] = r;
      }

      JavaCompat.removeEach(this.spreadListArray, this.spreadListRemove);

      for (SpreadTile s : this.spreadListRemove) {
         this.spreadListMap[s.getSpreadX()][s.getSpreadY()] = null;
      }

      JavaCompat.removeEach(this.regrowListArray, this.regrowListRemove);

      for (RegrowTile r : this.regrowListRemove) {
         this.regrowListMap[r.getRegrowX()][r.getRegrowY()] = null;
      }

      this.spreadListAdd.clear();
      this.regrowListAdd.clear();
      this.spreadListRemove.clear();
      this.regrowListRemove.clear();

      // Thousands of tiles mostly just count down to their next growth roll; update a sixteenth of them each tick,
      // sixteen ticks at a time (same growth rates, a sixteenth of the work).
      this.tileUpdatePhase = (this.tileUpdatePhase + 1) % TILE_UPDATE_STRIDE;

      for (int i = this.tileUpdatePhase; i < this.spreadListArray.size(); i += TILE_UPDATE_STRIDE) {
         this.spreadListArray.get(i).update(TILE_UPDATE_STRIDE);
      }

      for (int i = this.tileUpdatePhase; i < this.regrowListArray.size(); i += TILE_UPDATE_STRIDE) {
         this.regrowListArray.get(i).update(TILE_UPDATE_STRIDE);
      }

      this.updateEssenceBuildup();
   }

   private void updateEssenceBuildup() throws SlickException {
      this.essenceBuildupTerrainTick++;
      if (this.essenceBuildupTerrainTick >= 50) {
         for (EssenceBase e : this.essence.getEssenceArray()) {
            int tileX = (int)(e.getEssenceX() / 16.0F);
            int tileY = (int)(e.getEssenceY() / 16.0F);
            if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)
               && this.essenceBuildupMap[tileX][tileY] < 125
               && (!e.isIdle() || Utilities.randomInt(30) == 0)) {
               this.essenceBuildupMap[tileX][tileY]++;
            }
         }

         this.essenceBuildupTerrainTick = Utilities.randomInt(10);
      }

      this.essenceBuildupObjectsTick++;
      if (this.essenceBuildupObjectsTick >= 50) {
         for (ObjectBase o : this.object.getObjectsArrayList()) {
            if (o.getEnergy() > 0) {
               ObjectFlags flags = o.getObjectFlags();
               OrderedPair center = o.getCenterCoordinates();
               int width = flags.getWidth();
               int height = flags.getHeight();
               int distance = (width + height) / 2 + o.getEnergy() / 100;
               ArrayList<OrderedPair> nearbyCoordinates = Utilities.getAllCoordinatesWithin(center.getX(), center.getY(), distance);
               ArrayList<OrderedPair> culledCoordinates = new ArrayList<>();

               for (OrderedPair tile : nearbyCoordinates) {
                  int dx = center.getX() - tile.getX();
                  int dy = center.getY() - tile.getY();
                  int hValue = (int)Math.sqrt(dx * dx + dy * dy);
                  if (hValue < distance) {
                     culledCoordinates.add(tile);
                  }
               }

               for (OrderedPair n : culledCoordinates) {
                  if (!Utilities.isOutOfBoundsOfUsableMap(n.getX(), n.getY())
                     && this.essenceBuildupMap[n.getX()][n.getY()] < 125
                     && Utilities.randomInt(30) == 0) {
                     this.essenceBuildupMap[n.getX()][n.getY()]++;
                  }
               }
            }
         }

         this.essenceBuildupObjectsTick = Utilities.randomInt(10);
      }

      this.essenceBuildupFlowersTick++;
      if (this.essenceBuildupFlowersTick >= 50) {
         for (int x = 0; x < this.mapWidth; x++) {
            for (int y = 0; y < this.mapHeight; y++) {
               if (this.essenceBuildupMap[x][y] >= 100 && Utilities.randomInt(5000) == 0) {
                  int x2 = x + Utilities.randomInt(-1, 1);
                  int y2 = y + Utilities.randomInt(-1, 1);
                  if (!Utilities.isOutOfBounds(256, 256, x2, y2)) {
                     int tileID = this.map.getTileId(x2, y2, 6);
                     if (tileID == 0) {
                        tileID = this.map.getTileId(x2, y2, 9);
                        if (tileID == 0) {
                           MapTilesLoader.TileSet thisFlower = this.pickFlowerType(x2, y2);
                           this.map.drawToMap(x2, y2, thisFlower, 1, BrushLoader.BrushType.SQUARE, this.allSidesThisFlower(x2, y2, thisFlower));
                           this.goal.incrementGoal(GoalModule.GoalType.ROSEBUD);
                        }
                     }
                  }
               }
            }
         }

         this.essenceBuildupFlowersTick = Utilities.randomInt(10);
      }

      this.essenceDecayTick++;
      if (this.essenceDecayTick == 1000) {
         for (int x = 0; x < this.mapWidth; x++) {
            for (int yx = 0; yx < this.mapHeight; yx++) {
               if (this.essenceBuildupMap[x][yx] > 0) {
                  this.essenceBuildupMap[x][yx]--;
               }
            }
         }

         this.essenceDecayTick = Utilities.randomInt(10);
      }
   }

   private boolean allSidesThisFlower(int x2, int y2, MapTilesLoader.TileSet thisFlower) {
      for (int x = -1; x < 2; x++) {
         for (int y = -1; y < 2; y++) {
            if (x != 0 || y != 0) {
               int tileX = x + x2;
               int tileY = y + y2;
               if (!Utilities.isOutOfBounds(256, 256, tileX, tileY)) {
                  int tileID = this.map.getTileId(tileX, tileY, 6);
                  int tileGID = this.map.getMapTileLoader().getTileSetGID(tileID);
                  int flowerGID = this.map.getMapTileLoader().getTileSetGID(thisFlower);
                  if (tileGID != flowerGID) {
                     return false;
                  }
               }
            }
         }
      }

      return true;
   }

   private MapTilesLoader.TileSet pickFlowerType(int tileX, int tileY) {
      if (Utilities.randomInt(100) == 0) {
         return this.validFlowers.get(Utilities.randomInt(this.validFlowers.size()));
      } else {
         int distance = 16;
         ArrayList<OrderedPair> nearbyCoordinates = Utilities.getAllCoordinatesWithin(tileX, tileY, distance * 2);
         ArrayList<OrderedPair> culledCoordinates = new ArrayList<>();
         MapTilesLoader.TileSet flowerTileSet = null;

         for (OrderedPair tile : nearbyCoordinates) {
            int dx = tileX - tile.getX();
            int dy = tileY - tile.getY();
            int hValue = (int)Math.sqrt(dx * dx + dy * dy);
            if (hValue < distance) {
               culledCoordinates.add(tile);
            }
         }

         Collections.shuffle(culledCoordinates);

         for (OrderedPair tilex : culledCoordinates) {
            int tileID = this.map.getTileId(tilex.getX(), tilex.getY(), 6);
            if (tileID == 0) {
               tileID = this.map.getTileId(tilex.getX(), tilex.getY(), 9);
            }

            if (tileID != 0) {
               int tileGID = this.map.getMapTileLoader().getTileSetGID(tileID);
               MapTilesLoader.TileSet flowersNearby = this.map.getMapTileLoader().getTileSetByTileID(tileGID);
               if (this.validFlowers.contains(flowersNearby)) {
                  flowerTileSet = flowersNearby;
                  break;
               }
            }
         }

         if (flowerTileSet == null) {
            flowerTileSet = this.validFlowers.get(Utilities.randomInt(this.validFlowers.size()));
         }

         return flowerTileSet;
      }
   }

   public void updateMapAI(int checkX, int checkY) {
      boolean spread = false;
      boolean regrow = false;

      for (int l = 0; l < this.mapLayers; l++) {
         int tileID = this.map.getTileId(checkX, checkY, l);
         if (tileID != 0) {
            if (this.map.getMapTileLoader().canTileRegrow(tileID) != null) {
               if (this.regrowListMap[checkX][checkY] == null) {
                  RegrowTile regrowTile = new RegrowTile(
                     OrderedPair.getOrderedPair(checkX, checkY),
                     this.map.getMapTileLoader().canTileRegrow(tileID),
                     this.map.getMapTileLoader().getRegrowRate(tileID),
                     this.map.getMapTileLoader().getRegrowOnlyMotivated(tileID),
                     this.map.getMapTileLoader().getRegrowIgnoreWeather(tileID)
                  );
                  this.regrowListAdd.add(regrowTile);
                  this.regrowListMap[checkX][checkY] = regrowTile;
               }

               regrow = true;
            }

            if (this.map.getMapTileLoader().canTileSpread(tileID) != null) {
               if (this.spreadListMap[checkX][checkY] == null) {
                  SpreadTile spreadTile = new SpreadTile(
                     OrderedPair.getOrderedPair(checkX, checkY),
                     this.map.getMapTileLoader().canTileSpread(tileID),
                     l,
                     this.map.getMapTileLoader().getSpreadRate(tileID),
                     this.map.getMapTileLoader().getSpreadOnlyMotivated(tileID),
                     this.map.getMapTileLoader().getSpreadIgnoreWeather(tileID)
                  );
                  this.spreadListAdd.add(spreadTile);
                  this.spreadListMap[checkX][checkY] = spreadTile;
               }

               spread = true;
            }
         }
      }

      if (!regrow && this.regrowListMap[checkX][checkY] != null) {
         this.regrowListRemove.add(this.regrowListMap[checkX][checkY]);
      }

      if (!spread && this.spreadListMap[checkX][checkY] != null) {
         this.spreadListRemove.add(this.spreadListMap[checkX][checkY]);
      }
   }

   public void removeRegrowTile(RegrowTile regrowTile) {
      this.regrowListRemove.add(regrowTile);
   }

   public ArrayList<RegrowTile> getRegrowArray() {
      return this.regrowListArray;
   }

   public ArrayList<SpreadTile> getSpreadArray() {
      return this.spreadListArray;
   }

   public RegrowTile[][] getRegrowMap() {
      return this.regrowListMap;
   }

   public SpreadTile[][] getSpreadMap() {
      return this.spreadListMap;
   }

   public int[][] getEssenceBuildupMap() {
      return this.essenceBuildupMap;
   }
}
