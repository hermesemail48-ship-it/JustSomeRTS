package rtr.map;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.console.Console;
import rtr.gui.states.BrushLoader;
import rtr.mobs.MobBase;
import rtr.resources.ResourceModule;
import rtr.road.RoadModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.ArrayFillUtil;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class WorkSelectionModule extends ModuleBase {
   private SpriteSheet selectionDots;
   private Image[][] selectionDotsMapped;
   private byte[][] selectionMap;
   private ArrayList<OrderedPair> harvestSelectionArray = new ArrayList<>();
   private EnumMap<ResourceModule.ResourceType, ArrayList<OrderedPair>> harvestSelectionTypeArray = new EnumMap<>(ResourceModule.ResourceType.class);
   private ArrayList<OrderedPair> regrowSelectionArray = new ArrayList<>();
   private ArrayList<OrderedPair> clearSelectionArray = new ArrayList<>();
   private ArrayList<OrderedPair> clearRoadSelectionArray = new ArrayList<>();
   private HashMap<OrderedPair, RoadModule.RoadType> roadSelectionArray = new HashMap<>();
   private ArrayList<OrderedPair> digSelectionArray = new ArrayList<>();

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      if (this.selectionDots == null) {
         this.selectionDots = new SpriteSheet(ImageLoader.getImage("res/GUI/selectionDots.png"), 16, 16);
         this.selectionDotsMapped = new Image[this.selectionDots.getHorizontalCount()][this.selectionDots.getVerticalCount()];

         for (int x = 0; x < this.selectionDots.getHorizontalCount(); x++) {
            for (int y = 0; y < this.selectionDots.getVerticalCount(); y++) {
               this.selectionDotsMapped[x][y] = this.selectionDots.getSprite(x, y);
            }
         }
      }

      if (this.selectionMap == null) {
         this.selectionMap = new byte[256][256];
      }

      ResourceModule.ResourceType[] var6;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         this.harvestSelectionTypeArray.put(t, new ArrayList<>());
      }

      this.updateHarvestSelectionTypeArray();
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      ArrayFillUtil.fill(this.selectionMap, (byte)WorkSelectionModule.SelectionType.NONE.ordinal());
      this.harvestSelectionArray.clear();
      this.harvestSelectionTypeArray.clear();
      this.regrowSelectionArray.clear();
      this.clearSelectionArray.clear();
      this.clearRoadSelectionArray.clear();
      this.roadSelectionArray.clear();
      this.digSelectionArray.clear();
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
      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(this.moduleType);
      String[][] roadTileSetMapStrings = new String[256][256];
      if (dataMap.containsMap("selectionMap")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 1", "Converting selectionMap");
         YMLDataMap trafficAmountDataMap = dataMap.getSubMap("selectionMap");

         for (int count = 0; trafficAmountDataMap.containsKey(Integer.toString(count)); count++) {
            YMLDataMap thisMap = trafficAmountDataMap.getSubMap(Integer.toString(count));
            OrderedPair o = trafficAmountDataMap.unpackCoordinates(Integer.toString(count));
            int x = o.getX();
            int y = o.getY();
            byte value = Byte.parseByte(thisMap.getOrDefault("value", 0));
            if (thisMap.containsKey("tileSet")) {
               roadTileSetMapStrings[x][y] = thisMap.getOrDefault("tileSet", "null");
            }

            this.selectionMap[x][y] = value;
         }
      } else if (dataMap.containsKey("selectionMap")) {
         dataMap.unpackCoordinatesListWithValueToArray("selectionMap", this.selectionMap);
         if (dataMap.containsKey("selectionMapRoadType")) {
            int[][] rawRoadMap = new int[256][256];

            for (int x = 0; x < rawRoadMap.length; x++) {
               for (int y = 0; y < rawRoadMap[x].length; y++) {
                  rawRoadMap[x][y] = -1;
               }
            }

            dataMap.unpackCoordinatesListWithValueToArray("selectionMapRoadType", rawRoadMap);

            for (int x = 0; x < rawRoadMap.length; x++) {
               for (int y = 0; y < rawRoadMap[x].length; y++) {
                  if (rawRoadMap[x][y] != -1) {
                     roadTileSetMapStrings[x][y] = RoadModule.RoadType.values()[rawRoadMap[x][y]].toString();
                  }
               }
            }
         }
      }

      for (int x = 0; x < this.selectionMap.length; x++) {
         for (int yx = 0; yx < this.selectionMap[x].length; yx++) {
            if (this.selectionMap[x][yx] == WorkSelectionModule.SelectionType.HARVEST_TERRAIN.ordinal()) {
               this.harvestSelectionArray.add(OrderedPair.getOrderedPair(x, yx));
            }

            if (this.selectionMap[x][yx] == WorkSelectionModule.SelectionType.REGROW.ordinal()) {
               this.regrowSelectionArray.add(OrderedPair.getOrderedPair(x, yx));
            }

            if (this.selectionMap[x][yx] == WorkSelectionModule.SelectionType.CLEAR_TERRAIN.ordinal()) {
               this.clearSelectionArray.add(OrderedPair.getOrderedPair(x, yx));
            }

            if (this.selectionMap[x][yx] != WorkSelectionModule.SelectionType.BUILD_ROAD.ordinal()
               || roadTileSetMapStrings[x][yx] == null && roadTileSetMapStrings[x][yx].equals("null")) {
               if (this.selectionMap[x][yx] == WorkSelectionModule.SelectionType.BUILD_ROAD.ordinal()
                  && (roadTileSetMapStrings[x][yx] == null || roadTileSetMapStrings[x][yx].equals("null"))) {
                  this.selectionMap[x][yx] = (byte)WorkSelectionModule.SelectionType.NONE.ordinal();
               }
            } else {
               RoadModule.RoadType[] var20;
               for (RoadModule.RoadType t : RoadModule.RoadType.values()) {
                  if (t.toString().equals(roadTileSetMapStrings[x][yx])) {
                     this.roadSelectionArray.put(OrderedPair.getOrderedPair(x, yx), t);
                     break;
                  }
               }
            }

            if (this.selectionMap[x][yx] == WorkSelectionModule.SelectionType.CLEAR_ROAD.ordinal()) {
               this.clearRoadSelectionArray.add(OrderedPair.getOrderedPair(x, yx));
            }

            if (this.selectionMap[x][yx] == WorkSelectionModule.SelectionType.DIG.ordinal()) {
               this.digSelectionArray.add(OrderedPair.getOrderedPair(x, yx));
            }
         }
      }

      this.updateHarvestSelectionTypeArray();
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      ArrayList<OrderedPair> selectMapCoordinatesList = new ArrayList<>();
      ArrayList<Integer> valuesList = new ArrayList<>();
      ArrayList<OrderedPair> roadTypeCoordinatesList = new ArrayList<>();
      ArrayList<Integer> roadTypeList = new ArrayList<>();

      for (int x = 0; x < this.selectionMap.length; x++) {
         for (int y = 0; y < this.selectionMap[x].length; y++) {
            if (this.selectionMap[x][y] != 0) {
               selectMapCoordinatesList.add(OrderedPair.getOrderedPair(x, y));
               valuesList.add(Integer.valueOf(this.selectionMap[x][y]));
               if (this.roadSelectionArray.get(OrderedPair.getOrderedPair(x, y)) != null) {
                  roadTypeCoordinatesList.add(OrderedPair.getOrderedPair(x, y));
                  roadTypeList.add(this.roadSelectionArray.get(OrderedPair.getOrderedPair(x, y)).ordinal());
               }
            }
         }
      }

      if (selectMapCoordinatesList.size() > 0) {
         String selectionMapString = dataMap.packCoordinatesListWithValue(selectMapCoordinatesList, valuesList);
         dataMap.put("selectionMap", selectionMapString);
      }

      if (roadTypeCoordinatesList.size() > 0) {
         String selectionMapRoadTypeString = dataMap.packCoordinatesListWithValue(roadTypeCoordinatesList, roadTypeList);
         dataMap.put("selectionMapRoadType", selectionMapRoadTypeString);
      }

      return dataMap;
   }

   public void update() throws SlickException {
      ArrayList<OrderedPair> removalList = new ArrayList<>();
      ArrayList<OrderedPair> addRegrow = new ArrayList<>();
      ArrayList<OrderedPair> addHarvest = new ArrayList<>();
      if (this.harvestSelectionArray.size() > 0) {
         for (OrderedPair o : this.harvestSelectionArray) {
            if (this.resource.getResourceType()[o.getX()][o.getY()] == null
               || this.resource.getResourceType()[o.getX()][o.getY()] != null && this.resource.getResourceValues()[o.getX()][o.getY()] == 0
               || this.map.isTileFrozen(o.getX(), o.getY())) {
               removalList.add(o);
               if (this.mapAI.getRegrowMap()[o.getX()][o.getY()] != null
                  || this.resource.getResourceValues()[o.getX()][o.getY()] == 0
                  || this.map.isTileFrozen(o.getX(), o.getY())) {
                  addRegrow.add(o);
               }
            }
         }
      }

      if (this.regrowSelectionArray.size() > 0) {
         for (OrderedPair ox : this.regrowSelectionArray) {
            if (this.mapAI.getRegrowMap()[ox.getX()][ox.getY()] == null && this.resource.getResourceType()[ox.getX()][ox.getY()] == null) {
               removalList.add(ox);
            }

            if (this.resource.getResourceType()[ox.getX()][ox.getY()] != null
               && this.resource.getResourceValues()[ox.getX()][ox.getY()] > 0
               && !this.map.isTileFrozen(ox.getX(), ox.getY())) {
               addHarvest.add(ox);
            }
         }
      }

      if (this.clearSelectionArray.size() > 0) {
         for (OrderedPair ox : this.clearSelectionArray) {
            if (!this.map.canClearAtCoord(ox.getX(), ox.getY())) {
               removalList.add(ox);
            }
         }
      }

      if (this.clearRoadSelectionArray.size() > 0) {
         for (OrderedPair oxx : this.clearRoadSelectionArray) {
            if (this.map.getTileId(oxx.getX(), oxx.getY(), 5) == 0) {
               removalList.add(oxx);
            }
         }
      }

      if (this.roadSelectionArray.size() > 0) {
         for (OrderedPair oxxx : this.roadSelectionArray.keySet()) {
            if (!this.object.isInRange(oxxx.getX(), oxxx.getY()) || this.map.getMapData().getBlockMap(MapData.BlockMapGroup.ROAD)[oxxx.getX()][oxxx.getY()] > 0
               )
             {
               removalList.add(oxxx);
            }

            RoadModule.RoadType t = this.road.getRoadType(oxxx.getX(), oxxx.getY());
            if (t.ordinal() >= this.roadSelectionArray.get(oxxx).ordinal()
               || t.getTemplate().isDebris() && t.ordinal() + 1 >= this.roadSelectionArray.get(oxxx).ordinal()) {
               removalList.add(oxxx);
            }
         }
      }

      if (this.digSelectionArray.size() > 0) {
         for (OrderedPair oxxx : this.digSelectionArray) {
            if (!this.map.canDigAtCoord(oxxx.getX(), oxxx.getY())) {
               removalList.add(oxxx);
            }
         }
      }

      for (OrderedPair oxxxx : removalList) {
         this.deselectAll(oxxxx.getX(), oxxxx.getY());
      }

      for (OrderedPair oxxxx : addRegrow) {
         this.selectRegrow(oxxxx.getX(), oxxxx.getY());
      }

      for (OrderedPair oxxxx : addHarvest) {
         this.selectHarvestResources(oxxxx.getX(), oxxxx.getY());
      }

      this.updateHarvestSelectionTypeArray();
   }

   private void updateHarvestSelectionTypeArray() {
      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         this.harvestSelectionTypeArray.get(t).clear();
      }

      for (OrderedPair o : this.harvestSelectionArray) {
         ResourceModule.ResourceType type = this.resource.getResourceType()[o.getX()][o.getY()];
         if (type != null) {
            this.harvestSelectionTypeArray.get(type).add(o);
         }
      }
   }

   public void render(Graphics g) throws SlickException {
      int startX = this.map.getMapRenderTileTopLeftX() - 4;
      int startY = this.map.getMapRenderTileTopLeftY() - 4;
      int endX = this.map.getMapRenderTileBottomRightX() + 4;
      int endY = this.map.getMapRenderTileBottomRightY() + 4;
      byte[][] blockMap = this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD);
      ArrayList<MobBase> mobArray = this.mob.getMobArray();
      ArrayList<OrderedPair> assignedList = new ArrayList<>();

      for (MobBase m : mobArray) {
         if (m.getAssignment() != null) {
            assignedList.add(m.getAssignment().getPrimaryCoordinates());
            assignedList.add(m.getAssignment().getSecondaryCoordinates());
         }
      }

      this.selectionDots.startUse();

      for (int x = startX; x < endX; x++) {
         for (int y = startY; y < endY; y++) {
            if (x >= 0 && x <= 255 && y >= 0 && y <= 255) {
               if (this.selectionMap[x][y] == WorkSelectionModule.SelectionType.HARVEST_TERRAIN.ordinal()) {
                  if (blockMap[x + 1][y] > 0 && blockMap[x - 1][y] > 0 && blockMap[x][y + 1] > 0 && blockMap[x][y - 1] > 0) {
                     this.selectionDotsMapped[2][0].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  } else if (this.map.getMapThreatMap().isTileThreatened(x, y)) {
                     this.selectionDotsMapped[3][0].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  } else if (assignedList.contains(OrderedPair.getOrderedPair(x, y))) {
                     this.selectionDotsMapped[0][0].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  } else {
                     this.selectionDotsMapped[1][0].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  }
               }

               if (this.selectionMap[x][y] == WorkSelectionModule.SelectionType.REGROW.ordinal()) {
                  if (this.map.getMapThreatMap().isTileThreatened(x, y)) {
                     this.selectionDotsMapped[3][1].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  } else {
                     this.selectionDotsMapped[1][1].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  }
               }

               if (this.selectionMap[x][y] == WorkSelectionModule.SelectionType.CLEAR_TERRAIN.ordinal()
                  || this.selectionMap[x][y] == WorkSelectionModule.SelectionType.CLEAR_ROAD.ordinal()) {
                  if (blockMap[x + 1][y] > 0 && blockMap[x - 1][y] > 0 && blockMap[x][y + 1] > 0 && blockMap[x][y - 1] > 0) {
                     this.selectionDotsMapped[2][2].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  } else if (this.map.getMapThreatMap().isTileThreatened(x, y)) {
                     this.selectionDotsMapped[3][2].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  } else if (assignedList.contains(OrderedPair.getOrderedPair(x, y))) {
                     this.selectionDotsMapped[0][2].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  } else {
                     this.selectionDotsMapped[1][2].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  }
               }

               if (this.selectionMap[x][y] == WorkSelectionModule.SelectionType.BUILD_ROAD.ordinal()) {
                  ResourceModule.ResourceType resourceRequired = this.road.getNextRequiredResource(x, y);
                  RoadModule.RoadType thisUpgrade = this.getRoadSelectionMapArray().get(OrderedPair.getOrderedPair(x, y));
                  int roadUpgradeLevel = thisUpgrade.getTemplate().getRoadLevel();
                  if (resourceRequired != null && this.resource.getResourceTotal(resourceRequired, true) <= 0) {
                     this.selectionDotsMapped[2][3 + roadUpgradeLevel].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  } else if (assignedList.contains(OrderedPair.getOrderedPair(x, y))) {
                     this.selectionDotsMapped[0][3 + roadUpgradeLevel].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  } else if (this.map.getMapThreatMap().isTileThreatened(x, y)) {
                     this.selectionDotsMapped[3][3 + roadUpgradeLevel].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  } else {
                     this.selectionDotsMapped[1][3 + roadUpgradeLevel].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  }
               }

               if (this.selectionMap[x][y] == WorkSelectionModule.SelectionType.DIG.ordinal()) {
                  if (blockMap[x + 1][y] > 0 && blockMap[x - 1][y] > 0 && blockMap[x][y + 1] > 0 && blockMap[x][y - 1] > 0) {
                     this.selectionDotsMapped[2][8].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  } else if (assignedList.contains(OrderedPair.getOrderedPair(x, y))) {
                     this.selectionDotsMapped[0][8].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  } else if (this.map.getMapThreatMap().isTileThreatened(x, y)) {
                     this.selectionDotsMapped[3][8].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  } else {
                     this.selectionDotsMapped[1][8].drawEmbedded(this.map.getMapX() + x * 16, this.map.getMapY() + y * 16);
                  }
               }
            }
         }
      }

      this.selectionDots.endUse();
   }

   public void selectHarvestResources(int tileX, int tileY, ResourceModule.ResourceType type, int brushSize, BrushLoader.BrushType brushType) {
      byte[][] currentBrush = Game.getCS().getBrushLoader().getBrush(brushSize, brushType);

      for (int x = 0; x < currentBrush.length; x++) {
         for (int y = 0; y < currentBrush[x].length; y++) {
            if (currentBrush[x][y] >= 1) {
               int brushX = x + tileX - currentBrush.length / 2;
               int brushY = y + tileY - currentBrush[x].length / 2;
               if (Utilities.isOutOfBoundsOfUsableMap(brushX, brushY)) {
                  return;
               }

               if (this.selectionMap[brushX][brushY] == WorkSelectionModule.SelectionType.NONE.ordinal()
                  && (
                     this.resource.getResourceType()[brushX][brushY] == type
                        || this.mapAI.getRegrowMap()[brushX][brushY] != null
                           && this.map
                                 .getMapTileLoader()
                                 .getTileResourceType(this.map.getMapTileLoader().getTileSetGID(this.mapAI.getRegrowMap()[brushX][brushY].getTileType()))
                              == type
                  )) {
                  this.deselectAll(brushX, brushY);
                  this.selectionMap[brushX][brushY] = (byte)WorkSelectionModule.SelectionType.HARVEST_TERRAIN.ordinal();
                  this.harvestSelectionArray.add(OrderedPair.getOrderedPair(brushX, brushY));
               }
            }
         }
      }
   }

   private void selectHarvestResources(int tileX, int tileY) {
      this.deselectAll(tileX, tileY);
      this.selectionMap[tileX][tileY] = (byte)WorkSelectionModule.SelectionType.HARVEST_TERRAIN.ordinal();
      this.harvestSelectionArray.add(OrderedPair.getOrderedPair(tileX, tileY));
   }

   private void selectRegrow(int tileX, int tileY) {
      this.deselectAll(tileX, tileY);
      this.selectionMap[tileX][tileY] = (byte)WorkSelectionModule.SelectionType.REGROW.ordinal();
      this.regrowSelectionArray.add(OrderedPair.getOrderedPair(tileX, tileY));
   }

   public void selectClearResources(int tileX, int tileY, int brushSize, BrushLoader.BrushType brushType) {
      byte[][] currentBrush = Game.getCS().getBrushLoader().getBrush(brushSize, brushType);

      for (int x = 0; x < currentBrush.length; x++) {
         for (int y = 0; y < currentBrush[x].length; y++) {
            if (currentBrush[x][y] >= 1) {
               int brushX = x + tileX - currentBrush.length / 2;
               int brushY = y + tileY - currentBrush[x].length / 2;
               if (Utilities.isOutOfBoundsOfUsableMap(brushX, brushY)) {
                  return;
               }

               if (this.map.canClearAtCoord(brushX, brushY)) {
                  this.deselectAll(brushX, brushY);
                  this.selectionMap[brushX][brushY] = (byte)WorkSelectionModule.SelectionType.CLEAR_TERRAIN.ordinal();
                  this.clearSelectionArray.add(OrderedPair.getOrderedPair(brushX, brushY));
               }
            }
         }
      }
   }

   public void selectClearRoad(int tileX, int tileY, boolean selectConnected) throws SlickException {
      if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
         if (selectConnected) {
            for (OrderedPair o : this.road.getConnectedRoadCoordinates(tileX, tileY)) {
               int thisTileX = o.getX();
               int thisTileY = o.getY();
               if (this.object.isInRange(thisTileX, thisTileY)
                  && (
                     this.selectionMap[thisTileX][thisTileY] == WorkSelectionModule.SelectionType.NONE.ordinal()
                        || this.selectionMap[thisTileX][thisTileY] == WorkSelectionModule.SelectionType.BUILD_ROAD.ordinal()
                  )
                  && this.map.getTileId(thisTileX, thisTileY, 5) != 0) {
                  this.deselectAll(thisTileX, thisTileY);
                  this.selectionMap[thisTileX][thisTileY] = (byte)WorkSelectionModule.SelectionType.CLEAR_ROAD.ordinal();
                  this.clearRoadSelectionArray.add(OrderedPair.getOrderedPair(thisTileX, thisTileY));
               }
            }
         } else if (this.object.isInRange(tileX, tileY)
            && (
               this.selectionMap[tileX][tileY] == WorkSelectionModule.SelectionType.NONE.ordinal()
                  || this.selectionMap[tileX][tileY] == WorkSelectionModule.SelectionType.BUILD_ROAD.ordinal()
            )
            && this.map.getTileId(tileX, tileY, 5) != 0) {
            this.deselectAll(tileX, tileY);
            this.selectionMap[tileX][tileY] = (byte)WorkSelectionModule.SelectionType.CLEAR_ROAD.ordinal();
            this.clearRoadSelectionArray.add(OrderedPair.getOrderedPair(tileX, tileY));
         }
      }
   }

   public void selectRoadConstruction(int tileX, int tileY, RoadModule.RoadType roadType, boolean selectConnected) throws SlickException {
      if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
         if (selectConnected) {
            for (OrderedPair o : this.road.getConnectedRoadCoordinates(tileX, tileY)) {
               int thisTileX = o.getX();
               int thisTileY = o.getY();
               if (this.object.isInRange(thisTileX, thisTileY)
                  && (
                     this.selectionMap[thisTileX][thisTileY] == WorkSelectionModule.SelectionType.NONE.ordinal()
                        || this.selectionMap[tileX][tileY] == WorkSelectionModule.SelectionType.HARVEST_TERRAIN.ordinal()
                        || this.selectionMap[tileX][tileY] == WorkSelectionModule.SelectionType.REGROW.ordinal()
                        || this.selectionMap[thisTileX][thisTileY] == WorkSelectionModule.SelectionType.BUILD_ROAD.ordinal()
                        || this.selectionMap[thisTileX][thisTileY] == WorkSelectionModule.SelectionType.CLEAR_ROAD.ordinal()
                  )
                  && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.ROAD)[thisTileX][thisTileY] == 0
                  && this.road.getRoadType(thisTileX, thisTileY).getTemplate().getNextRoadType() != null
                  && this.road.getRoadType(thisTileX, thisTileY).getTemplate().getRoadLevel() < roadType.getTemplate().getRoadLevel()) {
                  this.deselectAll(thisTileX, thisTileY);
                  this.selectionMap[thisTileX][thisTileY] = (byte)WorkSelectionModule.SelectionType.BUILD_ROAD.ordinal();
                  this.roadSelectionArray.put(OrderedPair.getOrderedPair(thisTileX, thisTileY), roadType);
               }
            }
         } else if (this.object.isInRange(tileX, tileY)
            && (
               this.selectionMap[tileX][tileY] == WorkSelectionModule.SelectionType.NONE.ordinal()
                  || this.selectionMap[tileX][tileY] == WorkSelectionModule.SelectionType.HARVEST_TERRAIN.ordinal()
                  || this.selectionMap[tileX][tileY] == WorkSelectionModule.SelectionType.REGROW.ordinal()
                  || this.selectionMap[tileX][tileY] == WorkSelectionModule.SelectionType.BUILD_ROAD.ordinal()
                  || this.selectionMap[tileX][tileY] == WorkSelectionModule.SelectionType.CLEAR_ROAD.ordinal()
            )
            && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.ROAD)[tileX][tileY] == 0
            && this.road.getRoadType(tileX, tileY).getTemplate().getNextRoadType() != null
            && this.road.getRoadType(tileX, tileY).getTemplate().getRoadLevel() < roadType.getTemplate().getRoadLevel()) {
            this.deselectAll(tileX, tileY);
            this.selectionMap[tileX][tileY] = (byte)WorkSelectionModule.SelectionType.BUILD_ROAD.ordinal();
            this.roadSelectionArray.put(OrderedPair.getOrderedPair(tileX, tileY), roadType);
         }
      }
   }

   public void selectDig(int tileX, int tileY, int brushSize, BrushLoader.BrushType brushType) {
      byte[][] currentBrush = Game.getCS().getBrushLoader().getBrush(brushSize, brushType);

      for (int x = 0; x < currentBrush.length; x++) {
         for (int y = 0; y < currentBrush[x].length; y++) {
            if (currentBrush[x][y] >= 1) {
               int brushX = x + tileX - currentBrush.length / 2;
               int brushY = y + tileY - currentBrush[x].length / 2;
               if (Utilities.isOutOfBoundsOfUsableMap(brushX, brushY)) {
                  return;
               }

               if (this.map.canDigAtCoord(brushX, brushY)) {
                  this.deselectAll(brushX, brushY);
                  this.selectionMap[brushX][brushY] = (byte)WorkSelectionModule.SelectionType.DIG.ordinal();
                  this.digSelectionArray.add(OrderedPair.getOrderedPair(brushX, brushY));
               }
            }
         }
      }
   }

   public void deselectHarvestResources(int tileX, int tileY, ResourceModule.ResourceType type, int brushSize, BrushLoader.BrushType brushType) {
      byte[][] currentBrush = Game.getCS().getBrushLoader().getBrush(brushSize, brushType);

      for (int x = 0; x < currentBrush.length; x++) {
         for (int y = 0; y < currentBrush[x].length; y++) {
            if (currentBrush[x][y] >= 1) {
               int brushX = x + tileX - currentBrush.length / 2;
               int brushY = y + tileY - currentBrush[x].length / 2;
               if (Utilities.isOutOfBoundsOfUsableMap(brushX, brushY)) {
                  return;
               }

               if (this.selectionMap[brushX][brushY] > WorkSelectionModule.SelectionType.NONE.ordinal()
                  && (
                     this.resource.getResourceValues()[brushX][brushY] > 0 && this.resource.getResourceType()[brushX][brushY] == type
                        || this.mapAI.getRegrowMap()[brushX][brushY] != null
                           && this.map
                                 .getMapTileLoader()
                                 .getTileResourceType(this.map.getMapTileLoader().getTileSetGID(this.mapAI.getRegrowMap()[brushX][brushY].getTileType()))
                              == type
                  )) {
                  for (int i = 0; i < this.harvestSelectionArray.size(); i++) {
                     OrderedPair check = this.harvestSelectionArray.get(i);
                     if (check.getX() == brushX && check.getY() == brushY) {
                        this.deselectAll(brushX, brushY);
                        check = null;
                        break;
                     }
                  }

                  for (int ix = 0; ix < this.regrowSelectionArray.size(); ix++) {
                     OrderedPair check = this.regrowSelectionArray.get(ix);
                     if (check.getX() == brushX && check.getY() == brushY) {
                        this.deselectAll(brushX, brushY);
                        check = null;
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   public void deselectRegrow(int tileX, int tileY) {
      if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
         this.selectionMap[tileX][tileY] = (byte)WorkSelectionModule.SelectionType.NONE.ordinal();
         this.regrowSelectionArray.remove(OrderedPair.getOrderedPair(tileX, tileY));
      }
   }

   public void deselectClearResources(int tileX, int tileY, int brushSize, BrushLoader.BrushType brushType) {
      if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
         byte[][] currentBrush = Game.getCS().getBrushLoader().getBrush(brushSize, brushType);

         for (int x = 0; x < currentBrush.length; x++) {
            for (int y = 0; y < currentBrush[x].length; y++) {
               if (currentBrush[x][y] >= 1) {
                  int brushX = x + tileX - currentBrush.length / 2;
                  int brushY = y + tileY - currentBrush[x].length / 2;
                  if (Utilities.isOutOfBoundsOfUsableMap(brushX, brushY)) {
                     return;
                  }

                  if (this.selectionMap[brushX][brushY] > WorkSelectionModule.SelectionType.NONE.ordinal()) {
                     for (int i = 0; i < this.clearSelectionArray.size(); i++) {
                        OrderedPair check = this.clearSelectionArray.get(i);
                        if (check.getX() == brushX && check.getY() == brushY) {
                           this.deselectAll(brushX, brushY);
                           check = null;
                           break;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void deselectClearRoad(int tileX, int tileY, boolean selectConnected) throws SlickException {
      if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
         if (selectConnected) {
            for (OrderedPair o : this.road.getConnectedRoadCoordinates(tileX, tileY)) {
               int thisTileX = o.getX();
               int thisTileY = o.getY();
               if (this.selectionMap[thisTileX][thisTileY] == WorkSelectionModule.SelectionType.CLEAR_ROAD.ordinal()) {
                  this.selectionMap[thisTileX][thisTileY] = (byte)WorkSelectionModule.SelectionType.NONE.ordinal();
                  this.clearRoadSelectionArray.remove(OrderedPair.getOrderedPair(thisTileX, thisTileY));
               }
            }
         } else if (this.selectionMap[tileX][tileY] == WorkSelectionModule.SelectionType.CLEAR_ROAD.ordinal()) {
            this.selectionMap[tileX][tileY] = (byte)WorkSelectionModule.SelectionType.NONE.ordinal();
            this.clearRoadSelectionArray.remove(OrderedPair.getOrderedPair(tileX, tileY));
         }
      }
   }

   public void deselectRoadConstruction(int tileX, int tileY, boolean selectConnected) throws SlickException {
      if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
         if (selectConnected) {
            for (OrderedPair o : this.road.getConnectedRoadCoordinates(tileX, tileY)) {
               int thisTileX = o.getX();
               int thisTileY = o.getY();
               this.selectionMap[thisTileX][thisTileY] = (byte)WorkSelectionModule.SelectionType.NONE.ordinal();
               this.roadSelectionArray.remove(OrderedPair.getOrderedPair(thisTileX, thisTileY));
            }
         } else {
            this.selectionMap[tileX][tileY] = (byte)WorkSelectionModule.SelectionType.NONE.ordinal();
            this.roadSelectionArray.remove(OrderedPair.getOrderedPair(tileX, tileY));
         }
      }
   }

   public void deselectDig(int tileX, int tileY, int brushSize, BrushLoader.BrushType brushType) {
      byte[][] currentBrush = Game.getCS().getBrushLoader().getBrush(brushSize, brushType);

      for (int x = 0; x < currentBrush.length; x++) {
         for (int y = 0; y < currentBrush[x].length; y++) {
            if (currentBrush[x][y] >= 1) {
               int brushX = x + tileX - currentBrush.length / 2;
               int brushY = y + tileY - currentBrush[x].length / 2;
               if (Utilities.isOutOfBoundsOfUsableMap(brushX, brushY)) {
                  return;
               }

               if (this.selectionMap[brushX][brushY] > WorkSelectionModule.SelectionType.NONE.ordinal()) {
                  for (int i = 0; i < this.digSelectionArray.size(); i++) {
                     OrderedPair check = this.digSelectionArray.get(i);
                     if (check.getX() == brushX && check.getY() == brushY) {
                        this.deselectAll(brushX, brushY);
                        check = null;
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   public void deselectAll(int tileX, int tileY) {
      if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
         this.selectionMap[tileX][tileY] = (byte)WorkSelectionModule.SelectionType.NONE.ordinal();
         this.harvestSelectionArray.remove(OrderedPair.getOrderedPair(tileX, tileY));
         this.regrowSelectionArray.remove(OrderedPair.getOrderedPair(tileX, tileY));
         this.clearSelectionArray.remove(OrderedPair.getOrderedPair(tileX, tileY));
         this.clearRoadSelectionArray.remove(OrderedPair.getOrderedPair(tileX, tileY));
         this.roadSelectionArray.remove(OrderedPair.getOrderedPair(tileX, tileY));
         this.digSelectionArray.remove(OrderedPair.getOrderedPair(tileX, tileY));
      }
   }

   public ArrayList<OrderedPair> getHarvestSelectionMapArray(ResourceModule.ResourceType t) {
      return this.harvestSelectionTypeArray.get(t);
   }

   public ArrayList<OrderedPair> getHarvestSelectionMapArray() {
      return this.harvestSelectionArray;
   }

   public ArrayList<OrderedPair> getClearSelectionMapArray() {
      return this.clearSelectionArray;
   }

   public ArrayList<OrderedPair> getClearRoadSelectionMapArray() {
      return this.clearRoadSelectionArray;
   }

   public HashMap<OrderedPair, RoadModule.RoadType> getRoadSelectionMapArray() {
      return this.roadSelectionArray;
   }

   public ArrayList<OrderedPair> getDigSelectionMapArray() {
      return this.digSelectionArray;
   }

   public byte[][] getSelectionMap() {
      return this.selectionMap;
   }

   public static enum SelectionType {
      NONE,
      HARVEST_TERRAIN,
      REGROW,
      CLEAR_TERRAIN,
      BUILD_ROAD,
      CLEAR_ROAD,
      DIG;
   }
}
