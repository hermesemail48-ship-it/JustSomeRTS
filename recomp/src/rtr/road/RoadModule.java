package rtr.road;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.console.Console;
import rtr.goal.GoalModule;
import rtr.gui.states.BrushLoader;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.map.WorkSelectionModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class RoadModule extends ModuleBase {
   private static final int ROAD_TRAFFIC_TO_CREATE_PATH = 16000;
   public static final int ROAD_BASE_DURABILITY = 2000;
   private RoadFinder roadFinder;
   private RoadModule.RoadType[][] roadMap;
   private int[][] trafficAmount;
   private int[][] roadDurability;
   private ArrayList<OrderedPair> roadRepairList = new ArrayList<>();
   private ArrayList<OrderedPair> roadClearTopographyList = new ArrayList<>();
   private ArrayList<OrderedPair> roadClearResourceList = new ArrayList<>();
   private int decayTick = 0;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      int mapHeight = 256;
      int mapWidth = 256;
      if (this.roadFinder == null) {
         this.roadFinder = new RoadFinder();
      }

      if (this.roadMap == null) {
         this.roadMap = new RoadModule.RoadType[mapHeight][mapWidth];

         for (int x = 0; x < this.roadMap.length; x++) {
            for (int y = 0; y < this.roadMap[x].length; y++) {
               this.roadMap[x][y] = RoadModule.RoadType.NONE;
            }
         }
      }

      if (this.trafficAmount == null) {
         this.trafficAmount = new int[mapHeight][mapWidth];
      }

      if (this.roadDurability == null) {
         this.roadDurability = new int[mapHeight][mapWidth];
      }

      this.requiresSync = true;
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();

      for (int x = 0; x < this.roadMap.length; x++) {
         for (int y = 0; y < this.roadMap[x].length; y++) {
            this.roadMap[x][y] = RoadModule.RoadType.NONE;
            this.trafficAmount[x][y] = 0;
            this.roadDurability[x][y] = 0;
         }
      }

      this.roadRepairList.clear();
      this.roadClearTopographyList.clear();
      this.roadClearResourceList.clear();
      this.decayTick = 0;
   }

   @Override
   protected void newMainMenu() throws SlickException {
      this.newPlay();
   }

   @Override
   protected void newPlay() throws SlickException {
      for (int x = 0; x < this.roadMap.length; x++) {
         for (int y = 0; y < this.roadMap[x].length; y++) {
            if (this.map.getTileId(x, y, 5) != 0) {
               this.roadDurability[x][y] = 2000;
               MapTilesLoader.TileSet roadTileSet = this.getRoadTileType(x, y);
               if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.ROAD)[x][y] == 0) {
                  RoadModule.RoadType[] var7;
                  for (RoadModule.RoadType t : RoadModule.RoadType.values()) {
                     if (t.getTemplate().isDebris()) {
                        this.roadRepairList.add(OrderedPair.getOrderedPair(x, y));
                     }
                  }
               }

               RoadModule.RoadType[] var11;
               for (RoadModule.RoadType tx : RoadModule.RoadType.values()) {
                  if (tx.getTemplate().getRoadTileSet() == roadTileSet) {
                     this.roadMap[x][y] = tx;
                  }
               }
            }
         }
      }
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
   protected void loadRegionalSaveData() {
      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(this.moduleType);
      if (dataMap.containsMap("trafficAmount")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 1", "Converting trafficAmount.");
         YMLDataMap trafficAmountDataMap = dataMap.getSubMap("trafficAmount");

         for (int trafficAmountCount = 0; trafficAmountDataMap.containsKey(Integer.toString(trafficAmountCount)); trafficAmountCount++) {
            YMLDataMap thisMap = trafficAmountDataMap.getSubMap(Integer.toString(trafficAmountCount));
            OrderedPair o = trafficAmountDataMap.unpackCoordinates(Integer.toString(trafficAmountCount));
            int x = o.getX();
            int y = o.getY();
            int value = Integer.parseInt(thisMap.getOrDefault("value", 0));
            this.trafficAmount[x][y] = value;
         }
      } else if (dataMap.containsKey("trafficAmount")) {
         dataMap.unpackCoordinatesListWithValueToArray("trafficAmount", this.trafficAmount);
      }

      if (dataMap.containsMap("roadDurability")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 1", "Converting roadDurability.");
         YMLDataMap roadDurabilityDataMap = dataMap.getSubMap("roadDurability");

         for (int roadDurabilityCount = 0; roadDurabilityDataMap.containsKey(Integer.toString(roadDurabilityCount)); roadDurabilityCount++) {
            YMLDataMap thisMap = roadDurabilityDataMap.getSubMap(Integer.toString(roadDurabilityCount));
            OrderedPair o = roadDurabilityDataMap.unpackCoordinates(Integer.toString(roadDurabilityCount));
            int x = o.getX();
            int y = o.getY();
            int value = Integer.parseInt(thisMap.getOrDefault("value", 0));
            this.roadDurability[x][y] = value;
         }
      } else if (dataMap.containsKey("roadDurability")) {
         dataMap.unpackCoordinatesListWithValueToArray("roadDurability", this.roadDurability);
      }
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public void initLoadSync() throws SlickException {
      this.synced = true;

      for (int x = 0; x < this.roadMap.length; x++) {
         for (int y = 0; y < this.roadMap[x].length; y++) {
            if (this.map.getTileId(x, y, 5) != 0) {
               int thisTileGID = this.map.getMapTileLoader().getTileSetGID(this.map.getTileId(x, y, 5));

               RoadModule.RoadType[] var7;
               for (RoadModule.RoadType t : RoadModule.RoadType.values()) {
                  if (t.getTemplate().getRoadTileSet() != null) {
                     int typeTileGID = t.getTemplate().getRoadTileSet().getTileSetTemplate().getTileGID();
                     if (thisTileGID == typeTileGID) {
                        this.roadMap[x][y] = t;
                        break;
                     }
                  }
               }

               if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.ROAD)[x][y] == 0) {
                  RoadModule.RoadType roadType = this.roadMap[x][y];
                  if (roadType != RoadModule.RoadType.NONE && roadType != RoadModule.RoadType.PATH) {
                     if (this.roadDurability[x][y] <= 1000 || roadType.getTemplate().isDebris()) {
                        this.roadRepairList.add(OrderedPair.getOrderedPair(x, y));
                     }

                     if (this.map.canClearAtCoord(x, y)
                        && (
                           this.object.isInRange(x + 1, y)
                              || this.object.isInRange(x - 1, y)
                              || this.object.isInRange(x, y + 1)
                              || this.object.isInRange(x, y - 1)
                        )) {
                        this.roadClearTopographyList.add(OrderedPair.getOrderedPair(x, y));
                     }
                  }

                  if (roadType != RoadModule.RoadType.NONE && this.object.isInRange(x, y)) {
                     ResourceModule.ResourceType resourceOnRoad = this.resource.getResourceTypeOnGround(x, y);
                     if (resourceOnRoad != null && (roadType.getTemplate().getNextRoadType() == null || this.getNextRequiredResource(x, y) != resourceOnRoad)) {
                        this.roadClearResourceList.add(OrderedPair.getOrderedPair(x, y));
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      ArrayList<OrderedPair> trafficCoordinatesList = new ArrayList<>();
      ArrayList<Integer> trafficValuesList = new ArrayList<>();
      ArrayList<OrderedPair> roadDurabilityCoordinatesList = new ArrayList<>();
      ArrayList<Integer> roadDurabilityValuesList = new ArrayList<>();

      for (int x = 0; x < this.trafficAmount.length; x++) {
         for (int y = 0; y < this.trafficAmount[x].length; y++) {
            if (this.trafficAmount[x][y] != 0) {
               trafficCoordinatesList.add(OrderedPair.getOrderedPair(x, y));
               trafficValuesList.add(this.trafficAmount[x][y]);
            }

            if (this.roadDurability[x][y] != 0) {
               roadDurabilityCoordinatesList.add(OrderedPair.getOrderedPair(x, y));
               roadDurabilityValuesList.add(this.roadDurability[x][y]);
            }
         }
      }

      if (trafficCoordinatesList.size() > 0) {
         String trafficAmountString = dataMap.packCoordinatesListWithValue(trafficCoordinatesList, trafficValuesList);
         dataMap.put("trafficAmount", trafficAmountString);
      }

      if (roadDurabilityCoordinatesList.size() > 0) {
         String roadDurabilityString = dataMap.packCoordinatesListWithValue(roadDurabilityCoordinatesList, roadDurabilityValuesList);
         dataMap.put("roadDurability", roadDurabilityString);
      }

      return dataMap;
   }

   public void update() throws SlickException {
      this.decayTick++;
      if (this.decayTick > 2000) {
         this.roadRepairList.clear();
         this.roadClearTopographyList.clear();
         this.roadClearResourceList.clear();

         for (int x = 0; x < this.trafficAmount.length; x++) {
            for (int y = 0; y < this.trafficAmount[x].length; y++) {
               if (this.trafficAmount[x][y] > 0) {
                  if (this.weather.isRaining()) {
                     this.trafficAmount[x][y] = this.trafficAmount[x][y] - 150;
                  } else {
                     this.trafficAmount[x][y] = this.trafficAmount[x][y] - 50;
                  }
               }

               if (this.trafficAmount[x][y] <= 0) {
                  this.trafficAmount[x][y] = 0;
               }

               RoadModule.RoadType roadType = this.roadMap[x][y];
               if (roadType != RoadModule.RoadType.NONE) {
                  if (roadType == RoadModule.RoadType.PATH) {
                     if (this.trafficAmount[x][y] == 0) {
                        if (this.weather.isRaining()) {
                           this.decreaseRoadDurability(x, y, Utilities.randomInt(51) + 25);
                        } else {
                           this.decreaseRoadDurability(x, y, Utilities.randomInt(21) + 10);
                        }

                        if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.ROAD)[x][y] > 0) {
                           this.decreaseRoadDurability(x, y, Utilities.randomInt(101) + 100);
                        }
                     }
                  } else {
                     if (this.weather.isRaining()) {
                        this.decreaseRoadDurability(x, y, Utilities.randomInt(11) + 10);
                     }

                     if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.ROAD)[x][y] > 0) {
                        this.decreaseRoadDurability(x, y, Utilities.randomInt(101) + 100);
                     }

                     if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.ROAD)[x][y] == 0
                        && (this.roadDurability[x][y] <= 1000 || roadType.getTemplate().isDebris())) {
                        this.roadRepairList.add(OrderedPair.getOrderedPair(x, y));
                     }

                     if (roadType != RoadModule.RoadType.NONE
                        && this.map.canClearAtCoord(x, y)
                        && (
                           this.object.isInRange(x + 1, y)
                              || this.object.isInRange(x - 1, y)
                              || this.object.isInRange(x, y + 1)
                              || this.object.isInRange(x, y - 1)
                        )) {
                        this.roadClearTopographyList.add(OrderedPair.getOrderedPair(x, y));
                     }
                  }

                  if (roadType != RoadModule.RoadType.NONE
                     && this.object.isInRange(x, y)
                     && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.ROAD)[x][y] <= 0) {
                     ResourceModule.ResourceType resourceOnRoad = this.resource.getResourceTypeOnGround(x, y);
                     if (resourceOnRoad != null && (roadType.getTemplate().getNextRoadType() == null || this.getNextRequiredResource(x, y) != resourceOnRoad)) {
                        this.roadClearResourceList.add(OrderedPair.getOrderedPair(x, y));
                     }
                  }
               }
            }
         }

         this.decayTick = 0;
      }
   }

   public void walkOnRoads(int tileX, int tileY) throws SlickException {
      if (this.trafficAmount[tileX][tileY] < 16000) {
         if (this.weather.isRaining()) {
            this.trafficAmount[tileX][tileY] = this.trafficAmount[tileX][tileY] + 2;
         } else {
            this.trafficAmount[tileX][tileY]++;
         }
      }

      if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.ROAD)[tileX][tileY] == 0) {
         if (this.trafficAmount[tileX][tileY] >= 16000 && this.roadMap[tileX][tileY] == RoadModule.RoadType.NONE) {
            this.upgradeRoad(tileX, tileY);
         }

         RoadModule.RoadType roadType = this.roadMap[tileX][tileY];
         if (roadType != RoadModule.RoadType.NONE && Utilities.randomInt(20) == 0) {
            if (roadType == RoadModule.RoadType.PATH) {
               if (this.weather.isRaining()) {
                  this.increaseRoadDurability(tileX, tileY, 2);
               } else {
                  this.increaseRoadDurability(tileX, tileY, 1);
               }
            } else if (this.weather.isRaining()) {
               this.decreaseRoadDurability(tileX, tileY, 2);
            } else {
               this.decreaseRoadDurability(tileX, tileY, 1);
            }
         }
      }
   }

   public void upgradeRoad(int x, int y) throws SlickException {
      this.roadDurability[x][y] = 2000;
      RoadModule.RoadType nextRoad = this.roadMap[x][y].getTemplate().getNextRoadType();
      this.map.drawRoad(x, y, nextRoad.getTemplate().getRoadTileSet());
      this.roadMap[x][y] = nextRoad;
      if (this.workSelection.getRoadSelectionMapArray().containsKey(OrderedPair.getOrderedPair(x, y))
         && (
            nextRoad.ordinal() >= this.workSelection.getRoadSelectionMapArray().get(OrderedPair.getOrderedPair(x, y)).ordinal()
               || nextRoad.getTemplate().isDebris()
                  && nextRoad.ordinal() + 1 >= this.workSelection.getRoadSelectionMapArray().get(OrderedPair.getOrderedPair(x, y)).ordinal()
         )) {
         this.workSelection.deselectRoadConstruction(x, y, false);
      }

      if (nextRoad == RoadModule.RoadType.CUT_STONE_AND_BOARD_ROAD && this.object.isInRange(x, y)) {
         this.goal.incrementGoal(GoalModule.GoalType.ALL_ROADS_LEAD_TO_HOME);
      }
   }

   private void degradeRoad(int x, int y) throws SlickException {
      RoadModule.RoadType roadType = this.roadMap[x][y];
      if (roadType == RoadModule.RoadType.PATH) {
         this.map.erase(x, y, MapModule.EraseMode.ROADS, 1, BrushLoader.BrushType.ROUND);
         this.roadDurability[x][y] = 0;
         this.trafficAmount[x][y] = 0;
         this.roadMap[x][y] = RoadModule.RoadType.NONE;
      } else if (roadType != RoadModule.RoadType.NONE) {
         this.roadDurability[x][y] = 2000;
         if (roadType.getTemplate().isDebris()) {
            this.map.drawRoad(x, y, MapTilesLoader.TileSet.PATH);
            this.roadMap[x][y] = RoadModule.RoadType.PATH;
         } else if (roadType.getTemplate().getDebrisType() != null) {
            RoadModule.RoadType nextRoad = roadType.getTemplate().getDebrisType();
            if (nextRoad == RoadModule.RoadType.NONE) {
               this.map.erase(x, y, MapModule.EraseMode.ROADS, 1, BrushLoader.BrushType.ROUND);
            } else {
               this.map.drawRoad(x, y, nextRoad.getTemplate().getRoadTileSet());
            }

            this.roadMap[x][y] = nextRoad;
         }
      }
   }

   public void setRoad(int x, int y, RoadModule.RoadType roadType) throws SlickException {
      if (!Utilities.isOutOfBoundsOfUsableMap(x, y)) {
         this.roadDurability[x][y] = 2000;
         this.roadMap[x][y] = roadType;
         this.map.drawRoad(x, y, roadType.getTemplate().getRoadTileSet());
      }
   }

   public int[][] getTrafficAmount() {
      return this.trafficAmount;
   }

   public RoadModule.RoadType getRoadType(int tileX, int tileY) {
      return this.roadMap[tileX][tileY];
   }

   public String getNextRoadName(int tileX, int tileY) throws SlickException {
      return this.roadMap[tileX][tileY].getTemplate().getNextRoadType().getTemplate().getRoadTileSet().getTileSetTemplate().getName();
   }

   public ResourceModule.ResourceType getNextRequiredResource(int tileX, int tileY) {
      RoadModule.RoadType upgradeRoadType = this.roadMap[tileX][tileY].getTemplate().getNextRoadType();
      ArrayList<ResourceModule.ResourceType> resourceTypes = this.getTotalRequiredResource(tileX, tileY, upgradeRoadType);
      return resourceTypes.size() > 0 ? resourceTypes.get(resourceTypes.size() - 1) : null;
   }

   public ArrayList<ResourceModule.ResourceType> getTotalRequiredResource(int tileX, int tileY, RoadModule.RoadType upgradeToRoadType) {
      ArrayList<ResourceModule.ResourceType> resourceTypes = new ArrayList<>();
      resourceTypes.addAll(upgradeToRoadType.getTemplate().getResourceRequirements());
      if (this.roadMap[tileX][tileY].getTemplate().isDebris()) {
         resourceTypes.removeAll(this.roadMap[tileX][tileY].getTemplate().getNextRoadType().getTemplate().getResourceRequirements());
      } else {
         resourceTypes.removeAll(this.roadMap[tileX][tileY].getTemplate().getResourceRequirements());
      }

      return resourceTypes;
   }

   public ArrayList<OrderedPair> getRoadRepairList() {
      return this.roadRepairList;
   }

   public ArrayList<OrderedPair> getRoadClearTopographyList() {
      return this.roadClearTopographyList;
   }

   public ArrayList<OrderedPair> getRoadClearResourceList() {
      return this.roadClearResourceList;
   }

   public void repairRoad(int x, int y) throws SlickException {
      RoadModule.RoadType roadType = this.roadMap[x][y];
      if (roadType.getTemplate().isDebris()) {
         this.upgradeRoad(x, y);
      } else {
         this.roadDurability[x][y] = 2000;
      }

      this.roadRepairList.remove(OrderedPair.getOrderedPair(x, y));
   }

   public void updateClearedTopographyStatus(int x, int y) {
      if (!this.map.canClearAtCoord(x, y)) {
         this.roadClearTopographyList.remove(OrderedPair.getOrderedPair(x, y));
      }

      if (!this.object.isInRange(x + 1, y) && !this.object.isInRange(x - 1, y) && !this.object.isInRange(x, y + 1) && !this.object.isInRange(x, y - 1)) {
         this.roadClearTopographyList.remove(OrderedPair.getOrderedPair(x, y));
      }
   }

   public void updateClearedResourceStatus(int x, int y) {
      RoadModule.RoadType roadType = this.roadMap[x][y];
      if (roadType.getTemplate().getNextRoadType() != null) {
         ResourceModule.ResourceType resourceOnRoad = this.resource.getResourceTypeOnGround(x, y);
         if (resourceOnRoad != null
            && this.getNextRequiredResource(x, y) != resourceOnRoad
            && (this.object.isInRange(x + 1, y) || this.object.isInRange(x - 1, y) || this.object.isInRange(x, y + 1) || this.object.isInRange(x, y - 1))) {
            return;
         }
      }

      this.roadClearResourceList.remove(OrderedPair.getOrderedPair(x, y));
   }

   public void increaseRoadDurability(int x, int y, int i) throws SlickException {
      this.roadDurability[x][y] = this.roadDurability[x][y] + i;
      if (this.roadDurability[x][y] > 2000) {
         this.roadDurability[x][y] = 2000;
      }
   }

   public void decreaseRoadDurability(int x, int y, int i) throws SlickException {
      this.roadDurability[x][y] = this.roadDurability[x][y] - i;
      if (this.roadDurability[x][y] < 0) {
         this.roadDurability[x][y] = 0;
      }

      if (this.roadDurability[x][y] == 0) {
         this.degradeRoad(x, y);
      }
   }

   public int[][] getRoadDurability() {
      return this.roadDurability;
   }

   public boolean isResourceNeededForRoad(ResourceBase r) {
      return (
            !this.workSelection.isInitialized()
               || this.workSelection.getSelectionMap()[r.getTileX()][r.getTileY()] == WorkSelectionModule.SelectionType.BUILD_ROAD.ordinal()
         )
         && this.getNextRequiredResource(r.getTileX(), r.getTileY()) == r.getType();
   }

   public ArrayList<OrderedPair> getConnectedRoadCoordinates(int x, int y) throws SlickException {
      return this.roadFinder.collectRoadCoordinates(x, y);
   }

   private MapTilesLoader.TileSet getRoadTileType(int tileX, int tileY) {
      return this.map.getMapTileLoader().getTileSetByTileID(this.map.getTileId(tileX, tileY, 5));
   }

   public static enum RoadType {
      NONE(new None()),
      PATH(new Path()),
      LOG_PATH_DEBRIS(new LogPathDebris()),
      LOG_PATH(new LogPath()),
      COBBLE_AND_LOG_PATH_DEBRIS(new CobbleAndLogPathDebris()),
      COBBLE_AND_LOG_PATH(new CobbleAndLogPath()),
      COBBLE_AND_BOARD_ROAD_DEBRIS(new CobbleAndBoardRoadDebris()),
      COBBLE_AND_BOARD_ROAD(new CobbleAndBoardRoad()),
      CUT_STONE_AND_BOARD_ROAD_DEBRIS(new CutStoneAndBoardRoadDebris()),
      CUT_STONE_AND_BOARD_ROAD(new CutStoneAndBoardRoad());

      RoadBase roadTemplate;

      private RoadType(RoadBase roadTemplate) {
         this.roadTemplate = roadTemplate;
      }

      public RoadBase getTemplate() {
         if (!this.roadTemplate.loaded()) {
            this.roadTemplate.load();
         }

         return this.roadTemplate;
      }
   }
}
