package rtr.objects.rangebuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.states.StateBase;
import rtr.system.threadpool.RTRThreadPool;
import rtr.utilities.ArrayFillUtil;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class RangeBuilder {
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected int[][][] rangeMapsCurrent;
   protected int[][][] rangeMapsNew;
   protected int updateWaitPeriod = 100;
   protected int updateWaitPeriodTick;
   protected ArrayList<ObjectBase> objectListInUse;
   protected int mapHeight;
   protected int mapWidth;
   protected PriorityQueue<RangeNode> openRangeNodes = new PriorityQueue<>();
   protected ArrayList<RangeNode> resettableRangeNodes = new ArrayList<>();
   protected RangeNode baseRangeNode;
   protected RangeNode[][] rangeNodes;

   public RangeBuilder() throws SlickException {
      this.mapHeight = 256;
      this.mapWidth = 256;
      this.rangeMapsCurrent = new int[RangeBuilder.RangeMapLayer.values().length][this.mapWidth][this.mapHeight];
      this.rangeMapsNew = new int[RangeBuilder.RangeMapLayer.values().length][this.mapWidth][this.mapHeight];
      this.rangeNodes = new RangeNode[this.mapWidth][this.mapHeight];

      for (int x = 0; x < this.rangeNodes.length; x++) {
         for (int y = 0; y < this.rangeNodes[x].length; y++) {
            this.rangeNodes[x][y] = new RangeNode(x, y);
         }
      }

      this.updateFullRangeMap();
   }

   public void update() throws SlickException {
      this.updateWaitPeriodTick++;
      this.updateWaitPeriod = 100;

      for (ObjectBase o : this.object.getObjectsArrayList()) {
         this.checkObjectRange(o);
      }

      if (this.updateWaitPeriodTick >= this.updateWaitPeriod) {
         while (!RTRThreadPool.isRangeComplete()) {
            try {
               Thread.sleep(5L);
            } catch (InterruptedException var4) {
               var4.printStackTrace();
            }
         }

         this.copyNewRangeMapToCurrent();
         ArrayFillUtil.fill(this.rangeMapsNew, 0);
         this.objectListInUse = new ArrayList<>();
         this.objectListInUse.addAll(this.object.getObjectsArrayList());
         RTRThreadPool.generateNewRangeMap(this.objectListInUse);
         this.updateWaitPeriodTick = 0;
      }
   }

   public void updateFullRangeMap() throws SlickException {
      ArrayFillUtil.fill(this.rangeMapsNew, 0);

      for (ObjectBase o : this.object.getObjectsArrayList()) {
         if (o.getObjectFlags().getBaseRange() > 0) {
            this.createSingleRange(o);
            this.createSingleCorruptionResistance(o);
         }
      }

      this.trimRangeMap();
      this.createThreatMap();
      this.copyNewRangeMapToCurrent();
   }

   private void copyNewRangeMapToCurrent() {
      for (int layer = 0; layer < this.rangeMapsCurrent.length; layer++) {
         for (int x = 0; x < this.rangeMapsCurrent[layer].length; x++) {
            for (int y = 0; y < this.rangeMapsCurrent[layer][x].length; y++) {
               this.rangeMapsCurrent[layer][x][y] = this.rangeMapsNew[layer][x][y];
            }
         }
      }
   }

   public void checkObjectRange(ObjectBase object) {
      int width = object.getObjectFlags().getWidth();
      int height = object.getObjectFlags().getHeight();
      int objectX = object.getObjectX();
      int objectY = object.getObjectY();

      for (int x = -1; x < width + 1; x++) {
         for (int y = -1; y < height + 1; y++) {
            if (this.rangeMapsCurrent[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][objectX + x][objectY + y] >= 1) {
               object.setInRange(true);
               return;
            }
         }
      }

      object.setInRange(false);
   }

   public void createSingleRange(ObjectBase o) throws SlickException {
      int range = o.getObjectFlags().getBaseRange();
      int desirability = o.getObjectFlags().getBaseDesirability();
      ArrayList<OrderedPair> corners = new ArrayList<>();
      int width = o.getObjectFlags().getWidth();
      int height = o.getObjectFlags().getHeight();
      if (width > 1 && height > 1) {
         corners.add(o.getTopLeftCoordinate());
         corners.add(o.getTopRightCoordinate());
         corners.add(o.getBottomLeftCoordinate());
         corners.add(o.getBottomRightCoordinate());
      } else {
         corners.add(OrderedPair.getOrderedPair(o.getObjectX(), o.getObjectY()));
      }

      this.resetOldRangeNodes();
      this.openRangeNodes.clear();

      for (OrderedPair c : corners) {
         int rangeX = c.getX();
         int rangeY = c.getY();
         this.baseRangeNode = this.rangeNodes[rangeX][rangeY];
         this.openRangeNodes.add(this.rangeNodes[rangeX][rangeY]);
         this.rangeNodes[rangeX][rangeY].resetNode(MapData.BlockMapGroup.STANDARD_NO_CORRUPTION);
         this.rangeNodes[rangeX][rangeY].openNode();
         this.resettableRangeNodes.add(this.rangeNodes[rangeX][rangeY]);
         this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][rangeX][rangeY] += o.getObjectFlags().getBaseDesirability() + 1;
         if (this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][rangeX][rangeY] < 1) {
            this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][rangeX][rangeY] = 1;
         }
      }

      int movementCostMax = range * 1000;
      boolean generateReachMap = false;
      if (o.getObjectFlags().getBaseType() == MapTilesLoader.TileSet.CASTLE_1) {
         generateReachMap = true;
      }

      byte[][] blockMap = this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD_NO_CORRUPTION);

      while (this.openRangeNodes.size() > 0) {
         this.baseRangeNode = this.openRangeNodes.poll();
         int thisX = this.baseRangeNode.getX();
         int thisY = this.baseRangeNode.getY();

         for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
               if (x != 0 || y != 0) {
                  int checkX = thisX + x;
                  int checkY = thisY + y;
                  if (!Utilities.isOutOfBoundsOfUsableMap(checkX, checkY)) {
                     RangeNode checkNode = this.rangeNodes[checkX][checkY];
                     if (checkNode.isResettable()) {
                        checkNode.resetNode(MapData.BlockMapGroup.STANDARD_NO_CORRUPTION);
                     }

                     if (!checkNode.isOpen() && !checkNode.isClosed() && blockMap[checkX][checkY] <= 0) {
                        this.resettableRangeNodes.add(this.rangeNodes[checkX][checkY]);
                        if ((checkX <= thisX || checkY <= thisY || blockMap[checkX - 1][checkY] <= 0 && blockMap[checkX][checkY - 1] <= 0)
                           && (checkX >= thisX || checkY >= thisY || blockMap[checkX + 1][checkY] <= 0 && blockMap[checkX][checkY + 1] <= 0)
                           && (checkX <= thisX || checkY >= thisY || blockMap[checkX - 1][checkY] <= 0 && blockMap[checkX][checkY + 1] <= 0)
                           && (checkX >= thisX || checkY <= thisY || blockMap[checkX + 1][checkY] <= 0 && blockMap[checkX][checkY - 1] <= 0)) {
                           int movementCost = 0;
                           int nodeMValue = checkNode.getM();
                           if ((checkX <= thisX || checkY <= thisY)
                              && (checkX >= thisX || checkY >= thisY)
                              && (checkX <= thisX || checkY >= thisY)
                              && (checkX >= thisX || checkY <= thisY)) {
                              movementCost = nodeMValue;
                           } else {
                              movementCost = (int)(nodeMValue + nodeMValue * 0.4);
                           }

                           checkNode.setG(this.baseRangeNode.getG() + movementCost);
                           boolean addedNode = false;
                           if (checkNode.getG() < movementCostMax) {
                              checkNode.openNode();
                              this.openRangeNodes.add(checkNode);
                              addedNode = true;
                              this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][checkX][checkY] += desirability;
                              if (this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][checkX][checkY] < 1) {
                                 this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][checkX][checkY] = 1;
                              }

                              if (this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][checkX][checkY] > 100) {
                                 this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][checkX][checkY] = 100;
                              }
                           }

                           if (generateReachMap) {
                              checkNode.openNode();
                              if (!addedNode) {
                                 this.openRangeNodes.add(checkNode);
                              }

                              if (this.rangeMapsNew[RangeBuilder.RangeMapLayer.REACH_MAP.ordinal()][checkX][checkY] <= 0) {
                                 this.rangeMapsNew[RangeBuilder.RangeMapLayer.REACH_MAP.ordinal()][checkX][checkY] = 1;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         this.baseRangeNode.closeNode();
      }
   }

   public void createSingleCorruptionResistance(ObjectBase o) throws SlickException {
      int range = o.getObjectFlags().getBaseRange();
      ArrayList<OrderedPair> corners = new ArrayList<>();
      int width = o.getObjectFlags().getWidth();
      int height = o.getObjectFlags().getHeight();
      if (width > 1 && height > 1) {
         corners.add(o.getTopLeftCoordinate());
         corners.add(o.getTopRightCoordinate());
         corners.add(o.getBottomLeftCoordinate());
         corners.add(o.getBottomRightCoordinate());
      } else {
         corners.add(OrderedPair.getOrderedPair(o.getObjectX(), o.getObjectY()));
      }

      this.resetOldRangeNodes();
      this.openRangeNodes.clear();

      for (OrderedPair c : corners) {
         int rangeX = c.getX();
         int rangeY = c.getY();
         this.baseRangeNode = this.rangeNodes[rangeX][rangeY];
         this.openRangeNodes.add(this.rangeNodes[rangeX][rangeY]);
         this.rangeNodes[rangeX][rangeY].resetNode(MapData.BlockMapGroup.STANDARD_NO_CORRUPTION);
         this.rangeNodes[rangeX][rangeY].openNode();
         this.resettableRangeNodes.add(this.rangeNodes[rangeX][rangeY]);
         this.rangeMapsNew[RangeBuilder.RangeMapLayer.CORRUPTION_RESISTANCE.ordinal()][rangeX][rangeY] += o.getObjectFlags().getBaseDesirability() + 1;
         if (this.rangeMapsNew[RangeBuilder.RangeMapLayer.CORRUPTION_RESISTANCE.ordinal()][rangeX][rangeY] < 1) {
            this.rangeMapsNew[RangeBuilder.RangeMapLayer.CORRUPTION_RESISTANCE.ordinal()][rangeX][rangeY] = 1;
         }
      }

      int movementCostMax = range * 1000;

      while (this.openRangeNodes.size() > 0) {
         this.baseRangeNode = this.openRangeNodes.poll();
         int thisX = this.baseRangeNode.getX();
         int thisY = this.baseRangeNode.getY();

         for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
               if (x != 0 || y != 0) {
                  int checkX = thisX + x;
                  int checkY = thisY + y;
                  if (!Utilities.isOutOfBoundsOfUsableMap(checkX, checkY)) {
                     RangeNode checkNode = this.rangeNodes[checkX][checkY];
                     if (checkNode.isResettable()) {
                        checkNode.resetNode(MapData.BlockMapGroup.STANDARD_NO_CORRUPTION);
                     }

                     if (!checkNode.isOpen() && !checkNode.isClosed()) {
                        this.resettableRangeNodes.add(this.rangeNodes[checkX][checkY]);
                        int movementCost = 0;
                        int nodeMValue = 1000;
                        if ((checkX <= thisX || checkY <= thisY)
                           && (checkX >= thisX || checkY >= thisY)
                           && (checkX <= thisX || checkY >= thisY)
                           && (checkX >= thisX || checkY <= thisY)) {
                           movementCost = nodeMValue;
                        } else {
                           movementCost = (int)(nodeMValue + nodeMValue * 0.4);
                        }

                        checkNode.setG(this.baseRangeNode.getG() + movementCost);
                        if (checkNode.getG() < movementCostMax) {
                           checkNode.openNode();
                           this.openRangeNodes.add(checkNode);
                           this.rangeMapsNew[RangeBuilder.RangeMapLayer.CORRUPTION_RESISTANCE.ordinal()][checkX][checkY] = 1;
                        }
                     }
                  }
               }
            }
         }

         this.baseRangeNode.closeNode();
      }
   }

   public void trimRangeMap() {
      this.resetOldRangeNodes();
      byte[][] blockMapStandard = this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD);
      byte[][] blockMap = new byte[256][256];
      byte[][] rawReachMap = new byte[256][256];

      for (int w = 0; w < 256; w++) {
         for (int h = 0; h < 256; h++) {
            if (this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][w][h] <= 0 || blockMapStandard[w][h] > 0) {
               blockMap[w][h] = 1;
            }
         }
      }

      this.openRangeNodes.clear();
      ObjectBase originBuilding = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1, true);
      if (originBuilding == null) {
         ArrayList<ObjectBase> allBuildings = this.object.getObjectsByGroup(MobBase.MobGroup.VILLAGER);
         Collections.sort(allBuildings, new Comparator<ObjectBase>() {
            public int compare(ObjectBase o1, ObjectBase o2) {
               return o1 == o2 ? 0 : o1.getID() - o2.getID();
            }
         });

         for (ObjectBase o : allBuildings) {
            if (o.isInRange() && o.getObjectFlags().getBaseDesirability() > 0 && o.getObjectFlags().getBaseRange() > 0) {
               originBuilding = o;
               break;
            }
         }
      }

      if (originBuilding != null) {
         this.openRangeNodes.add(this.rangeNodes[originBuilding.getCenterCoordinateX()][originBuilding.getCenterCoordinateY()]);
      }

      if (this.openRangeNodes.size() != 0) {
         for (RangeNode r : this.openRangeNodes) {
            r.resetNode(MapData.BlockMapGroup.STANDARD);
            r.openNode();
            rawReachMap[r.getX()][r.getY()] = 1;
            this.resettableRangeNodes.add(r);
         }

         for (ArrayList<RangeNode> newOpenRangeNodes = new ArrayList<>(); this.openRangeNodes.size() > 0; this.baseRangeNode.closeNode()) {
            this.baseRangeNode = this.openRangeNodes.poll();
            int thisX = this.baseRangeNode.getX();
            int thisY = this.baseRangeNode.getY();

            for (int x = -1; x < 2; x++) {
               for (int y = -1; y < 2; y++) {
                  if (x != 0 || y != 0) {
                     int checkX = thisX + x;
                     int checkY = thisY + y;
                     if (!Utilities.isOutOfBoundsOfUsableMap(checkX, checkY)) {
                        RangeNode checkNode = this.rangeNodes[checkX][checkY];
                        if (checkNode.isResettable()) {
                           checkNode.resetNode(MapData.BlockMapGroup.STANDARD);
                        }

                        if (!checkNode.isOpen() && !checkNode.isClosed() && blockMap[checkX][checkY] <= 0 && rawReachMap[checkX][checkY] <= 0) {
                           this.resettableRangeNodes.add(this.rangeNodes[checkX][checkY]);
                           if ((checkX <= thisX || checkY <= thisY || blockMap[checkX - 1][checkY] <= 0 && blockMap[checkX][checkY - 1] <= 0)
                              && (checkX >= thisX || checkY >= thisY || blockMap[checkX + 1][checkY] <= 0 && blockMap[checkX][checkY + 1] <= 0)
                              && (checkX <= thisX || checkY >= thisY || blockMap[checkX - 1][checkY] <= 0 && blockMap[checkX][checkY + 1] <= 0)
                              && (checkX >= thisX || checkY <= thisY || blockMap[checkX + 1][checkY] <= 0 && blockMap[checkX][checkY - 1] <= 0)) {
                              int movementCost = 0;
                              int nodeMValue = checkNode.getM();
                              if ((checkX <= thisX || checkY <= thisY)
                                 && (checkX >= thisX || checkY >= thisY)
                                 && (checkX <= thisX || checkY >= thisY)
                                 && (checkX >= thisX || checkY <= thisY)) {
                                 movementCost = nodeMValue;
                              } else {
                                 movementCost = (int)(nodeMValue + nodeMValue * 0.4);
                              }

                              checkNode.setG(this.baseRangeNode.getG() + movementCost);
                              checkNode.openNode();
                              newOpenRangeNodes.add(checkNode);
                              rawReachMap[checkX][checkY] = 1;
                           }
                        }
                     }
                  }
               }
            }

            if (this.openRangeNodes.size() == 0) {
               this.openRangeNodes.addAll(newOpenRangeNodes);
               newOpenRangeNodes.clear();
            }
         }

         for (int w = 0; w < 256; w++) {
            for (int hx = 0; hx < 256; hx++) {
               if (rawReachMap[w][hx] <= 0 && this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][w][hx] >= 1) {
                  this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][w][hx] = -1;
               }
            }
         }
      }
   }

   public void createThreatMap() {
      this.resetOldRangeNodes();
      byte[][] blockMap = this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD);
      byte[][] blockMapCorruption = this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD_NO_CORRUPTION);
      this.openRangeNodes.clear();

      for (int w = 0; w < 256; w++) {
         for (int h = 0; h < 256; h++) {
            if (this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][w][h] > 0) {
               for (int x = -1; x < 2; x++) {
                  for (int y = -1; y < 2; y++) {
                     if ((x != 0 || y != 0) && (x != -1 || y != -1) && (x != 1 || y != 1) && (x != 1 || y != -1) && (x != -1 || y != 1)) {
                        int rangeX = w + x;
                        int rangeY = h + y;
                        if (blockMap[rangeX][rangeY] <= 0
                           && this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][rangeX][rangeY] < 1
                           && !this.openRangeNodes.contains(this.rangeNodes[rangeX][rangeY])) {
                           this.openRangeNodes.add(this.rangeNodes[rangeX][rangeY]);
                        }
                     }
                  }
               }
            }
         }
      }

      if (this.openRangeNodes.size() != 0) {
         int DISTANCE_MULTIPLE = 500;

         for (RangeNode r : this.openRangeNodes) {
            r.resetNode(MapData.BlockMapGroup.STANDARD);
            r.openNode();
            this.rangeMapsNew[RangeBuilder.RangeMapLayer.VILLAGE_THREAT_MAP.ordinal()][r.getX()][r.getY()] = DISTANCE_MULTIPLE;
            this.resettableRangeNodes.add(r);
         }

         int var16 = DISTANCE_MULTIPLE + DISTANCE_MULTIPLE;
         this.baseRangeNode = this.openRangeNodes.poll();

         for (ArrayList<RangeNode> newOpenRangeNodes = new ArrayList<>(); this.openRangeNodes.size() > 0; this.baseRangeNode.closeNode()) {
            this.baseRangeNode = this.openRangeNodes.poll();
            int thisX = this.baseRangeNode.getX();
            int thisY = this.baseRangeNode.getY();

            for (int x = -1; x < 2; x++) {
               for (int yx = -1; yx < 2; yx++) {
                  if (x != 0 || yx != 0) {
                     int checkX = thisX + x;
                     int checkY = thisY + yx;
                     if (!Utilities.isOutOfBoundsOfUsableMap(checkX, checkY)) {
                        RangeNode checkNode = this.rangeNodes[checkX][checkY];
                        if (checkNode.isResettable()) {
                           checkNode.resetNode(MapData.BlockMapGroup.STANDARD);
                        }

                        if (!checkNode.isOpen()
                           && !checkNode.isClosed()
                           && blockMap[checkX][checkY] <= 0
                           && this.rangeMapsNew[RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY.ordinal()][checkX][checkY] <= 0) {
                           this.resettableRangeNodes.add(this.rangeNodes[checkX][checkY]);
                           if ((checkX <= thisX || checkY <= thisY || blockMap[checkX - 1][checkY] <= 0 && blockMap[checkX][checkY - 1] <= 0)
                              && (checkX >= thisX || checkY >= thisY || blockMap[checkX + 1][checkY] <= 0 && blockMap[checkX][checkY + 1] <= 0)
                              && (checkX <= thisX || checkY >= thisY || blockMap[checkX - 1][checkY] <= 0 && blockMap[checkX][checkY + 1] <= 0)
                              && (checkX >= thisX || checkY <= thisY || blockMap[checkX + 1][checkY] <= 0 && blockMap[checkX][checkY - 1] <= 0)) {
                              int movementCost = 0;
                              int nodeMValue = checkNode.getM();
                              if ((checkX <= thisX || checkY <= thisY)
                                 && (checkX >= thisX || checkY >= thisY)
                                 && (checkX <= thisX || checkY >= thisY)
                                 && (checkX >= thisX || checkY <= thisY)) {
                                 movementCost = nodeMValue;
                              } else {
                                 movementCost = (int)(nodeMValue + nodeMValue * 0.4);
                              }

                              checkNode.setG(this.baseRangeNode.getG() + movementCost);
                              checkNode.openNode();
                              newOpenRangeNodes.add(checkNode);
                              if (blockMapCorruption[checkX][checkY] > 0) {
                                 this.rangeMapsNew[RangeBuilder.RangeMapLayer.VILLAGE_THREAT_MAP.ordinal()][checkX][checkY] = var16 * 4;
                              } else {
                                 this.rangeMapsNew[RangeBuilder.RangeMapLayer.VILLAGE_THREAT_MAP.ordinal()][checkX][checkY] = var16;
                              }
                           }
                        }
                     }
                  }
               }
            }

            if (this.openRangeNodes.size() == 0) {
               this.openRangeNodes.addAll(newOpenRangeNodes);
               newOpenRangeNodes.clear();
               var16 += DISTANCE_MULTIPLE;
            }
         }
      }
   }

   private void resetOldRangeNodes() {
      for (RangeNode r : this.resettableRangeNodes) {
         r.flagResettable();
      }

      this.resettableRangeNodes.clear();
   }

   public int[][] getRangeMap(RangeBuilder.RangeMapLayer layer) {
      return this.rangeMapsCurrent[layer.ordinal()];
   }

   public static enum RangeMapLayer {
      RANGE_AND_DESIRABILITY,
      VILLAGE_THREAT_MAP,
      CORRUPTION_RESISTANCE,
      REACH_MAP;
   }
}
