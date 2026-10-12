package rtr.map;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.states.StateBase;
import rtr.utilities.ArrayFillUtil;
import rtr.utilities.OrderedPair;

public class MapData {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   public static final int BASE_MOVEMENT_COST = 1000;
   private MapTilesLoader mtl = this.map.getMapTileLoader();
   private byte[][][] blockMap;
   private int[][][] movementCostMap;
   private ArrayList<OrderedPair> edgeTiles = new ArrayList<>();

   public MapData() throws SlickException {
      this.blockMap = new byte[MapData.BlockMapGroup.values().length][256][256];
      this.movementCostMap = new int[MapData.BlockMapGroup.values().length][256][256];
   }

   public void buildInitialMap() throws SlickException {
      for (int x = 0; x < 256; x++) {
         for (int y = 0; y < 256; y++) {
            if (this.map.getTileId(x, y, 0) != 0) {
               label38:
               for (int x2 = -1; x2 < 2; x2++) {
                  for (int y2 = -1; y2 < 2; y2++) {
                     if (this.map.getTileId(x + x2, y + y2, 0) == 0) {
                        this.edgeTiles.add(OrderedPair.getOrderedPair(x, y));
                        break label38;
                     }
                  }
               }
            }

            this.updateMapData(x, y);
         }
      }
   }

   public void updateMapData(int x, int y) throws SlickException {
      int[][][] mapArray = this.map.getMap();

      MapData.BlockMapGroup[] var7;
      for (MapData.BlockMapGroup g : MapData.BlockMapGroup.values()) {
         this.blockMap[g.ordinal()][x][y] = 0;
         this.movementCostMap[g.ordinal()][x][y] = 0;
         int mCost = 1000;
         if (mapArray[x][y][0] == 0) {
            this.blockMap[g.ordinal()][x][y] = 1;
            mCost *= 3000;
         } else {
            for (int l = 0; l < 12; l++) {
               if (mapArray[x][y][l] != 0) {
                  this.mtl.checkAndloadTileset(mapArray[x][y][l]);
               }

               if (g.getBlockLarge() && this.mtl.isTileBlockLarge(mapArray[x][y][l])) {
                  this.blockMap[g.ordinal()][x][y] = 1;
                  mCost *= 850;
               } else if (g.getBlockSmall() && this.mtl.isTileBlockSmall(mapArray[x][y][l])) {
                  this.blockMap[g.ordinal()][x][y] = 1;
                  mCost *= 600;
               } else if (g.getBlockRoad() && this.mtl.isTileBlockRoad(mapArray[x][y][l])) {
                  this.blockMap[g.ordinal()][x][y] = 1;
               } else if (g.getBlockGate() && this.mtl.isTileBlockGate(mapArray[x][y][l])) {
                  this.blockMap[g.ordinal()][x][y] = 1;
                  mCost *= 250;
               } else if (g.getBlockLight() && this.mtl.isTileBlockLight(mapArray[x][y][l])) {
                  this.blockMap[g.ordinal()][x][y] = 1;
               } else if (g.getBlockWater() && this.mtl.isTileBlockWater(mapArray[x][y][l])) {
                  this.blockMap[g.ordinal()][x][y] = 1;
                  mCost *= 750;
               } else if (g.getBlockCorruption() && this.mtl.isTileBlockCorruption(mapArray[x][y][l])) {
                  this.blockMap[g.ordinal()][x][y] = 1;
                  mCost *= 150;
               } else if (g.getBlockGhost() && this.mtl.isTileBlockGhost(mapArray[x][y][l])) {
                  this.blockMap[g.ordinal()][x][y] = 1;
                  mCost *= 850;
               }
            }
         }

         for (int layer = 0; layer < 12; layer++) {
            if (mapArray[x][y][layer] != 0) {
               mCost += this.mtl.getMovementCost(mapArray[x][y][layer]);
            }
         }

         this.movementCostMap[g.ordinal()][x][y] += mCost;
      }
   }

   public byte[][] getBlockMap(MapData.BlockMapGroup g) {
      return this.blockMap[g.ordinal()];
   }

   public int getMovementCost(MapData.BlockMapGroup g, int x, int y) {
      return this.movementCostMap[g.ordinal()][x][y];
   }

   public ArrayList<OrderedPair> getEdgeTiles() {
      return this.edgeTiles;
   }

   public void reset() {
      ArrayFillUtil.fill(this.blockMap, (byte)0);
      ArrayFillUtil.fill(this.movementCostMap, 0);
   }

   public static enum BlockMapGroup {
      STANDARD(true, true, false, false, false, false, false, false, false),
      STANDARD_NO_CORRUPTION(true, true, false, false, false, false, true, false, false),
      MONSTERS(true, true, false, true, false, false, false, false, false),
      GHOSTS(false, false, false, false, false, false, false, true, true),
      FIRE_ELEMENTALS(true, true, false, true, false, true, false, false, false),
      LIGHTING(false, false, false, false, true, false, false, false, true),
      ROAD(true, true, true, false, false, true, false, false, true);

      private final boolean blockLarge;
      private final boolean blockSmall;
      private final boolean blockRoad;
      private final boolean blockGate;
      private final boolean blockLight;
      private final boolean blockWater;
      private final boolean blockCorruption;
      private final boolean blockGhost;
      private final boolean ignoreMovementCost;

      private BlockMapGroup(
         boolean blockLarge,
         boolean blockSmall,
         boolean blockRoad,
         boolean blockGate,
         boolean blockLight,
         boolean blockWater,
         boolean blockCorruption,
         boolean blockGhost,
         boolean ignoreMovementCost
      ) {
         this.blockLarge = blockLarge;
         this.blockSmall = blockSmall;
         this.blockRoad = blockRoad;
         this.blockGate = blockGate;
         this.blockLight = blockLight;
         this.blockWater = blockWater;
         this.blockCorruption = blockCorruption;
         this.blockGhost = blockGhost;
         this.ignoreMovementCost = ignoreMovementCost;
      }

      public boolean getBlockLarge() {
         return this.blockLarge;
      }

      public boolean getBlockSmall() {
         return this.blockSmall;
      }

      public boolean getBlockRoad() {
         return this.blockRoad;
      }

      public boolean getBlockGate() {
         return this.blockGate;
      }

      public boolean getBlockLight() {
         return this.blockLight;
      }

      public boolean getBlockWater() {
         return this.blockWater;
      }

      public boolean getBlockCorruption() {
         return this.blockCorruption;
      }

      public boolean getBlockGhost() {
         return this.blockGhost;
      }

      public boolean getIgnoreMovementCost() {
         return this.ignoreMovementCost;
      }
   }
}
