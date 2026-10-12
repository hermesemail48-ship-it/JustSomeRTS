package rtr.resources;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.font.FontModule;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobModule;
import rtr.objects.ObjectModule;
import rtr.states.StateBase;
import rtr.utilities.OrderedPair;
import rtr.weather.WeatherModule;

public class WaterBalancer {
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   protected FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   protected boolean flagWaterRebalance;
   protected ArrayList<OrderedPair> waterRebalanceMap = new ArrayList<>();
   protected byte[][] checkMap;
   protected byte[][] waterMap;
   protected byte[][] waterRebalanceMapChecker;
   protected ArrayList<OrderedPair> sweepList = new ArrayList<>();
   protected ArrayList<OrderedPair> setList = new ArrayList<>();
   protected ArrayList<OrderedPair> balanceList = new ArrayList<>();

   public WaterBalancer() {
      int mapWidth = 256;
      int mapHeight = 256;
      this.checkMap = new byte[mapWidth][mapHeight];
      this.waterMap = new byte[mapWidth][mapHeight];
      this.waterRebalanceMapChecker = new byte[mapWidth][mapHeight];
   }

   public void update(byte[][] mapOut) throws SlickException {
      if (this.flagWaterRebalance) {
         this.updateWaterMap();

         for (OrderedPair o : this.waterRebalanceMap) {
            if (this.checkMap[o.getX()][o.getY()] != 1) {
               this.balanceWater(mapOut, o.getX(), o.getY());
               this.waterRebalanceMapChecker[o.getX()][o.getY()] = 0;
            }
         }

         this.waterRebalanceMap.clear();
         this.updateWaterLevels();
         this.flagWaterRebalance = false;
      }
   }

   public void rebalanceAllWater(byte[][] mapOut) throws SlickException {
      int mapWidth = 256;
      int mapHeight = 256;
      this.updateWaterMap();

      for (int x = 0; x < mapWidth; x++) {
         for (int y = 0; y < mapHeight; y++) {
            if (this.waterMap[x][y] == 1 && this.checkMap[x][y] == 0) {
               this.balanceWater(mapOut, x, y);
            }
         }
      }

      this.updateWaterLevels();
   }

   private void updateWaterMap() throws SlickException {
      int mapWidth = 256;
      int mapHeight = 256;

      for (int x = 0; x < mapWidth; x++) {
         for (int y = 0; y < mapHeight; y++) {
            this.waterMap[x][y] = 0;
            this.checkMap[x][y] = 0;
            int tileID = this.map.getTileId(x, y, 6);
            if (tileID != 0 && this.map.getMapTileLoader().isTileWater(tileID)) {
               this.waterMap[x][y] = 1;
            } else {
               int tileID2 = this.map.getTileId(x, y, 7);
               if (tileID2 != 0 && this.map.getMapTileLoader().isTileWater(tileID2)) {
                  this.waterMap[x][y] = 1;
               }
            }
         }
      }
   }

   public void updateWaterLevels() throws SlickException {
      this.updateWaterMap();
      int mapWidth = 256;
      int mapHeight = 256;

      for (int x = 0; x < mapWidth; x++) {
         for (int y = 0; y < mapHeight; y++) {
            if (this.waterMap[x][y] != 0 && this.checkMap[x][y] != 1) {
               int waterOut = this.resource.getResourceValues()[x][y];
               int tileID = this.map.getTileId(x, y, 6);
               MapTilesLoader.TileSet t = this.map.getMapTileLoader().getTileSetByTileID(tileID);
               if (waterOut <= 1 && t != MapTilesLoader.TileSet.WATER_DRY) {
                  this.map.partialTileSetSwap(t, MapTilesLoader.TileSet.WATER_DRY, this.collectPoolCoordinates(x, y), true);
               }

               if (waterOut >= 3 && waterOut <= 4 && t != MapTilesLoader.TileSet.WATER_PARTIALLY_DRY && t != MapTilesLoader.TileSet.WATER_PARTIALLY_DRY_FROZEN) {
                  if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
                     this.map.partialTileSetSwap(t, MapTilesLoader.TileSet.WATER_PARTIALLY_DRY_FROZEN, this.collectPoolCoordinates(x, y), true);
                  } else {
                     this.map.partialTileSetSwap(t, MapTilesLoader.TileSet.WATER_PARTIALLY_DRY, this.collectPoolCoordinates(x, y), true);
                  }
               }

               if (waterOut >= 6
                  && t != MapTilesLoader.TileSet.WATER
                  && t != MapTilesLoader.TileSet.WATER_PARTIALLY_FROZEN
                  && t != MapTilesLoader.TileSet.WATER_FROZEN) {
                  if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint() && t == MapTilesLoader.TileSet.WATER_PARTIALLY_DRY_FROZEN) {
                     this.map.partialTileSetSwap(t, MapTilesLoader.TileSet.WATER_FROZEN, this.collectPoolCoordinates(x, y), true);
                  } else {
                     this.map.partialTileSetSwap(t, MapTilesLoader.TileSet.WATER, this.collectPoolCoordinates(x, y), true);
                  }
               }
            }
         }
      }
   }

   public void rebalanceWaterAt(byte[][] mapOut, int tileX, int tileY) throws SlickException {
      this.updateWaterMap();
      this.balanceWater(mapOut, tileX, tileY);
      this.updateWaterLevels();
   }

   private void balanceWater(byte[][] mapOut, int x, int y) throws SlickException {
      this.sweepList.add(OrderedPair.getOrderedPair(x, y));
      this.checkMap[x][y] = 1;
      this.balanceList.add(OrderedPair.getOrderedPair(x, y));
      int thisX = 0;
      int thisY = 0;
      boolean completedSearch = false;

      while (!completedSearch) {
         for (OrderedPair o : this.sweepList) {
            thisX = o.getX();
            thisY = o.getY();
            if (thisX < 255 && thisX > 0 && thisY < 255 && thisY > 0) {
               if (this.waterMap[thisX][thisY + 1] == 1 && this.checkMap[thisX][thisY + 1] == 0) {
                  this.setList.add(OrderedPair.getOrderedPair(thisX, thisY + 1));
                  this.balanceList.add(OrderedPair.getOrderedPair(thisX, thisY + 1));
                  this.checkMap[thisX][thisY + 1] = 1;
               }

               if (this.waterMap[thisX][thisY - 1] == 1 && this.checkMap[thisX][thisY - 1] == 0) {
                  this.setList.add(OrderedPair.getOrderedPair(thisX, thisY - 1));
                  this.balanceList.add(OrderedPair.getOrderedPair(thisX, thisY - 1));
                  this.checkMap[thisX][thisY - 1] = 1;
               }

               if (this.waterMap[thisX - 1][thisY] == 1 && this.checkMap[thisX - 1][thisY] == 0) {
                  this.setList.add(OrderedPair.getOrderedPair(thisX - 1, thisY));
                  this.balanceList.add(OrderedPair.getOrderedPair(thisX - 1, thisY));
                  this.checkMap[thisX - 1][thisY] = 1;
               }

               if (this.waterMap[thisX + 1][thisY] == 1 && this.checkMap[thisX + 1][thisY] == 0) {
                  this.setList.add(OrderedPair.getOrderedPair(thisX + 1, thisY));
                  this.balanceList.add(OrderedPair.getOrderedPair(thisX + 1, thisY));
                  this.checkMap[thisX + 1][thisY] = 1;
               }
            }
         }

         this.sweepList.clear();
         if (this.setList.size() == 0) {
            completedSearch = true;
         }

         this.sweepList.addAll(this.setList);
         this.setList.clear();
      }

      if (this.balanceList.size() > 0) {
         int waterOut = 0;
         int remainder = 0;
         int divisor = this.balanceList.size();

         for (OrderedPair ox : this.balanceList) {
            waterOut += mapOut[ox.getX()][ox.getY()];
         }

         if (waterOut < this.balanceList.size()) {
            remainder = waterOut;
            waterOut = 0;
         } else {
            remainder = waterOut % divisor;
            waterOut /= divisor;
         }

         for (OrderedPair ox : this.balanceList) {
            if (remainder > 0) {
               mapOut[ox.getX()][ox.getY()] = (byte)(waterOut + 1);
               remainder--;
            } else {
               mapOut[ox.getX()][ox.getY()] = (byte)waterOut;
            }
         }
      }

      this.sweepList.clear();
      this.setList.clear();
      this.balanceList.clear();
   }

   public void increaseAllWater(byte[][] mapOut, int i) throws SlickException {
      int mapWidth = 256;
      int mapHeight = 256;

      for (int x = 0; x < mapWidth; x++) {
         for (int y = 0; y < mapHeight; y++) {
            int tileID = this.map.getTileId(x, y, 6);
            int tileID2 = this.map.getTileId(x, y, 7);
            if (this.map.getMapTileLoader().isTileWater(tileID)) {
               byte max = this.map.getMapTileLoader().getTileResourceAmountMax(tileID);
               if (mapOut[x][y] < max) {
                  mapOut[x][y] = (byte)(mapOut[x][y] + i);
                  if (mapOut[x][y] > max) {
                     mapOut[x][y] = max;
                  }
               }
            } else if (this.map.getMapTileLoader().isTileWater(tileID2)) {
               byte max2 = this.map.getMapTileLoader().getTileResourceAmountMax(tileID2);
               if (mapOut[x][y] < max2) {
                  mapOut[x][y] = (byte)(mapOut[x][y] + i);
                  if (mapOut[x][y] > max2) {
                     mapOut[x][y] = max2;
                  }
               }
            }
         }
      }

      this.rebalanceAllWater(mapOut);
   }

   private ArrayList<OrderedPair> collectPoolCoordinates(int x, int y) throws SlickException {
      this.sweepList.add(OrderedPair.getOrderedPair(x, y));
      this.checkMap[x][y] = 1;
      ArrayList<OrderedPair> out = new ArrayList<>();
      out.add(OrderedPair.getOrderedPair(x, y));
      int thisX = 0;
      int thisY = 0;
      boolean completedSearch = false;

      while (!completedSearch) {
         for (OrderedPair o : this.sweepList) {
            thisX = o.getX();
            thisY = o.getY();
            if (thisX < 255 && thisX > 0 && thisY < 255 && thisY > 0) {
               if (this.waterMap[thisX][thisY + 1] == 1 && this.checkMap[thisX][thisY + 1] == 0) {
                  this.setList.add(OrderedPair.getOrderedPair(thisX, thisY + 1));
                  out.add(OrderedPair.getOrderedPair(thisX, thisY + 1));
                  this.checkMap[thisX][thisY + 1] = 1;
               }

               if (this.waterMap[thisX][thisY - 1] == 1 && this.checkMap[thisX][thisY - 1] == 0) {
                  this.setList.add(OrderedPair.getOrderedPair(thisX, thisY - 1));
                  out.add(OrderedPair.getOrderedPair(thisX, thisY - 1));
                  this.checkMap[thisX][thisY - 1] = 1;
               }

               if (this.waterMap[thisX - 1][thisY] == 1 && this.checkMap[thisX - 1][thisY] == 0) {
                  this.setList.add(OrderedPair.getOrderedPair(thisX - 1, thisY));
                  out.add(OrderedPair.getOrderedPair(thisX - 1, thisY));
                  this.checkMap[thisX - 1][thisY] = 1;
               }

               if (this.waterMap[thisX + 1][thisY] == 1 && this.checkMap[thisX + 1][thisY] == 0) {
                  this.setList.add(OrderedPair.getOrderedPair(thisX + 1, thisY));
                  out.add(OrderedPair.getOrderedPair(thisX + 1, thisY));
                  this.checkMap[thisX + 1][thisY] = 1;
               }
            }
         }

         this.sweepList.clear();
         if (this.setList.size() == 0) {
            completedSearch = true;
         }

         this.sweepList.addAll(this.setList);
         this.setList.clear();
      }

      this.sweepList.clear();
      this.setList.clear();
      return out;
   }

   public void flagWaterRebalance(int x, int y) {
      if (this.waterRebalanceMapChecker[x][y] != 1) {
         this.flagWaterRebalance = true;
         OrderedPair o = OrderedPair.getOrderedPair(x, y);
         this.waterRebalanceMap.add(o);
         this.waterRebalanceMapChecker[x][y] = 1;
      }
   }
}
