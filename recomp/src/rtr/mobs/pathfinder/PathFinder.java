package rtr.mobs.pathfinder;

import java.util.ArrayList;
import java.util.PriorityQueue;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.map.WorkSelectionModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.objects.rangebuilder.RangeBuilder;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.states.StateBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class PathFinder {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   private WorkSelectionModule workSelection = (WorkSelectionModule)StateBase.getModule(ModuleBase.ModuleType.WORK_SELECTION);
   private ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   private MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   private PriorityQueue<PathNode> openPathNodes = new PriorityQueue<>();
   private ArrayList<PathNode> resettablePathNodes = new ArrayList<>();
   private PriorityQueue<SearchNode> openSearchNodes = new PriorityQueue<>();
   private ArrayList<SearchNode> resettableSearchNodes = new ArrayList<>();
   private PriorityQueue<SearchNode> openMultithreadedSearchNodes = new PriorityQueue<>();
   private ArrayList<SearchNode> resettableMultithreadedSearchNodes = new ArrayList<>();
   private PathNode basePathNode;
   private SearchNode baseSearchNode;
   private SearchNode baseMultithreadedSearchNode;
   private int mapWidth = 256;
   private int mapHeight = 256;
   private PathNode[][] pathNodes;
   private SearchNode[][] searchNodes;
   private SearchNode[][] multithreadedSearchNodes;
   private byte[][] searchMap = new byte[this.mapWidth][this.mapHeight];
   private byte[][] multithreadedSearchMap = new byte[this.mapWidth][this.mapHeight];

   public PathFinder() throws SlickException {
      this.pathNodes = new PathNode[this.mapWidth][this.mapHeight];
      this.searchNodes = new SearchNode[this.mapWidth][this.mapHeight];
      this.multithreadedSearchNodes = new SearchNode[this.mapWidth][this.mapHeight];

      for (int x = 0; x < this.pathNodes.length; x++) {
         for (int y = 0; y < this.pathNodes[x].length; y++) {
            this.pathNodes[x][y] = new PathNode(x, y);
            this.searchNodes[x][y] = new SearchNode(x, y);
            this.multithreadedSearchNodes[x][y] = new SearchNode(x, y);
         }
      }
   }

   public ArrayList<OrderedPair> findPath(int startX, int startY, int goalX, int goalY, boolean useForce, boolean perferVillage, MapData.BlockMapGroup g) {
      if (goalX < 0 || goalX > 255 || goalY < 0 || goalY > 255) {
         return null;
      } else if (startX == goalX && startY == goalY) {
         ArrayList<OrderedPair> returnedPath = new ArrayList<>();
         returnedPath.add(OrderedPair.getOrderedPair(goalX, goalY));
         return returnedPath;
      } else {
         int pathVariance = Utilities.randomInt(100) + 1;
         byte[][] blockMap = this.map.getMapData().getBlockMap(g);
         int[][] threatMap = null;
         if (perferVillage) {
            threatMap = this.object.getRangeMap(RangeBuilder.RangeMapLayer.VILLAGE_THREAT_MAP);
         }

         this.resetOldPathNodes();
         this.openPathNodes.clear();
         this.basePathNode = this.pathNodes[startX][startY];
         this.openPathNodes.add(this.pathNodes[startX][startY]);
         this.pathNodes[startX][startY].resetNode(goalX, goalY, pathVariance, g);
         this.pathNodes[startX][startY].openNode();
         this.resettablePathNodes.add(this.pathNodes[startX][startY]);
         boolean foundPath = false;

         while (!foundPath) {
            this.basePathNode = this.openPathNodes.poll();
            int thisX = this.basePathNode.getX();
            int thisY = this.basePathNode.getY();

            for (int x = -1; x < 2; x++) {
               label170:
               for (int y = -1; y < 2; y++) {
                  if (x != 0 || y != 0) {
                     int checkX = thisX + x;
                     int checkY = thisY + y;
                     if (!Utilities.isOutOfBoundsOfUsableMap(checkX, checkY) && this.map.getMap()[checkX][checkY][0] != 0) {
                        if (this.pathNodes[checkX][checkY].isResettable()) {
                           this.pathNodes[checkX][checkY].resetNode(goalX, goalY, pathVariance, g);
                        }

                        if (!this.pathNodes[checkX][checkY].isClosed()) {
                           this.resettablePathNodes.add(this.pathNodes[checkX][checkY]);
                           if (blockMap[checkX][checkY] > 0) {
                              if (!useForce) {
                                 continue;
                              }

                              for (int l = 0; l < 12; l++) {
                                 int tileID = this.map.getTileId(checkX, checkY, l);
                                 ObjectBase building = this.object.getBuildingAtLocation(checkX, checkY);
                                 if (this.map.getMapTileLoader().isTileInvulnerable(tileID)
                                    || building != null && building.getObjectFlags().getMobGroup() == MobBase.MobGroup.MONSTER) {
                                    continue label170;
                                 }
                              }
                           }

                           if ((checkX <= thisX || checkY <= thisY || blockMap[checkX - 1][checkY] <= 0 && blockMap[checkX][checkY - 1] <= 0)
                              && (checkX >= thisX || checkY >= thisY || blockMap[checkX + 1][checkY] <= 0 && blockMap[checkX][checkY + 1] <= 0)
                              && (checkX <= thisX || checkY >= thisY || blockMap[checkX - 1][checkY] <= 0 && blockMap[checkX][checkY + 1] <= 0)
                              && (checkX >= thisX || checkY <= thisY || blockMap[checkX + 1][checkY] <= 0 && blockMap[checkX][checkY - 1] <= 0)) {
                              PathNode checkNode = this.pathNodes[checkX][checkY];
                              int movementCost = 0;
                              int nodeMValue = checkNode.getM();
                              nodeMValue += this.mob.getPathMapper().getPathMap()[checkX][checkY] * 30;
                              if (perferVillage) {
                                 nodeMValue += threatMap[checkX][checkY];
                              }

                              if ((checkX <= thisX || checkY <= thisY)
                                 && (checkX >= thisX || checkY >= thisY)
                                 && (checkX <= thisX || checkY >= thisY)
                                 && (checkX >= thisX || checkY <= thisY)) {
                                 movementCost = nodeMValue;
                              } else {
                                 movementCost = (int)(nodeMValue + nodeMValue * 0.4);
                              }

                              if (checkNode.isOpen()) {
                                 int gValueCheck = this.basePathNode.getG() + movementCost;
                                 if (gValueCheck < checkNode.getG()) {
                                    checkNode.setParent(this.basePathNode);
                                 }
                              } else {
                                 checkNode.openNode();
                                 checkNode.setParent(this.basePathNode);
                                 checkNode.setG(this.basePathNode.getG() + movementCost);
                                 checkNode.calcF();
                                 this.openPathNodes.add(checkNode);
                                 if (checkX == goalX && checkY == goalY) {
                                    return this.buildPath(checkNode);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            this.basePathNode.closeNode();
            if (this.openPathNodes.size() <= 0) {
               break;
            }
         }

         return null;
      }
   }

   public ArrayList<OrderedPair> findUnstickPath(int startX, int startY, MapData.BlockMapGroup g) {
      int pathVariance = Utilities.randomInt(100) + 1;
      byte[][] blockMap = this.map.getMapData().getBlockMap(g);
      this.resetOldPathNodes();
      this.openPathNodes.clear();
      this.basePathNode = this.pathNodes[startX][startY];
      this.openPathNodes.add(this.pathNodes[startX][startY]);
      this.pathNodes[startX][startY].resetNode(0, 0, pathVariance, g);
      this.pathNodes[startX][startY].openNode();
      this.resettablePathNodes.add(this.pathNodes[startX][startY]);
      boolean foundPath = false;

      while (!foundPath) {
         this.basePathNode = this.openPathNodes.poll();
         int thisX = this.basePathNode.getX();
         int thisY = this.basePathNode.getY();

         for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
               if (x != 0 || y != 0) {
                  int checkX = thisX + x;
                  int checkY = thisY + y;
                  if (!Utilities.isOutOfBoundsOfUsableMap(checkX, checkY)) {
                     if (this.pathNodes[checkX][checkY].isResettable()) {
                        this.pathNodes[checkX][checkY].resetNode(0, 0, pathVariance, g);
                     }

                     if (!this.pathNodes[checkX][checkY].isClosed()) {
                        this.resettablePathNodes.add(this.pathNodes[checkX][checkY]);
                        PathNode checkNode = this.pathNodes[checkX][checkY];
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

                        if (checkNode.isOpen()) {
                           int gValueCheck = this.basePathNode.getG() + movementCost;
                           if (gValueCheck < checkNode.getG()) {
                              checkNode.setParent(this.basePathNode);
                           }
                        } else {
                           checkNode.openNode();
                           checkNode.setParent(this.basePathNode);
                           checkNode.setG(this.basePathNode.getG() + movementCost);
                           checkNode.calcF();
                           this.openPathNodes.add(checkNode);
                           if (blockMap[checkX][checkY] == 0 && this.getCoordinatesInRangeMultithreaded(checkX, checkY, 16, false, g).size() > 16) {
                              return this.buildPath(checkNode);
                           }
                        }
                     }
                  }
               }
            }
         }

         this.basePathNode.closeNode();
         if (this.openPathNodes.size() <= 0) {
            break;
         }
      }

      return null;
   }

   public ArrayList<OrderedPair> getCoordinatesInRange(int startX, int startY, int range, boolean ignoreMovementCost, MapData.BlockMapGroup g) {
      this.resetOldSearchNodes();
      int movementCostMax = range * 1000;
      ArrayList<OrderedPair> list = new ArrayList<>();
      this.openSearchNodes.clear();
      this.baseSearchNode = this.searchNodes[startX][startY];
      this.openSearchNodes.add(this.searchNodes[startX][startY]);
      this.searchNodes[startX][startY].resetNode(g);
      this.searchNodes[startX][startY].openNode();
      this.resettableSearchNodes.add(this.searchNodes[startX][startY]);
      boolean foundPath = false;

      while (!foundPath) {
         this.baseSearchNode = this.openSearchNodes.poll();
         int thisX = this.baseSearchNode.getX();
         int thisY = this.baseSearchNode.getY();

         for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
               if (x != 0 || y != 0) {
                  int checkX = thisX + x;
                  int checkY = thisY + y;
                  if (!Utilities.isOutOfBoundsOfUsableMap(checkX, checkY)) {
                     SearchNode checkNode = this.searchNodes[checkX][checkY];
                     if (checkNode.isResettable()) {
                        checkNode.resetNode(g);
                     }

                     if (!checkNode.isClosed()) {
                        this.resettableSearchNodes.add(checkNode);
                        int movementCost = 0;
                        int nodeMValue = 0;
                        if (ignoreMovementCost) {
                           nodeMValue = 1000;
                        } else {
                           nodeMValue = checkNode.getM();
                        }

                        if ((checkX <= thisX || checkY <= thisY)
                           && (checkX >= thisX || checkY >= thisY)
                           && (checkX <= thisX || checkY >= thisY)
                           && (checkX >= thisX || checkY <= thisY)) {
                           movementCost = nodeMValue;
                        } else {
                           movementCost = (int)(nodeMValue + nodeMValue * 0.4);
                        }

                        if (!checkNode.isOpen()) {
                           checkNode.openNode();
                           if (this.baseSearchNode.getG() + movementCost <= movementCostMax) {
                              checkNode.setG(this.baseSearchNode.getG() + movementCost);
                              this.openSearchNodes.add(checkNode);
                              list.add(OrderedPair.getOrderedPair(checkX, checkY));
                           }
                        }
                     }
                  }
               }
            }
         }

         this.baseSearchNode.closeNode();
         if (this.openSearchNodes.size() <= 0) {
            break;
         }
      }

      return list;
   }

   public ArrayList<OrderedPair> getCoordinatesInRangeMultithreaded(int startX, int startY, int range, boolean ignoreMovementCost, MapData.BlockMapGroup g) {
      this.resetOldMultithreadedSearchNodes();
      int movementCostMax = range * 1000;
      ArrayList<OrderedPair> list = new ArrayList<>();
      this.openMultithreadedSearchNodes.clear();
      this.baseMultithreadedSearchNode = this.multithreadedSearchNodes[startX][startY];
      this.openMultithreadedSearchNodes.add(this.multithreadedSearchNodes[startX][startY]);
      this.multithreadedSearchNodes[startX][startY].resetNode(g);
      this.multithreadedSearchNodes[startX][startY].openNode();
      this.resettableMultithreadedSearchNodes.add(this.multithreadedSearchNodes[startX][startY]);
      boolean foundPath = false;

      while (!foundPath) {
         this.baseMultithreadedSearchNode = this.openMultithreadedSearchNodes.poll();
         int thisX = this.baseMultithreadedSearchNode.getX();
         int thisY = this.baseMultithreadedSearchNode.getY();

         for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
               if (x != 0 || y != 0) {
                  int checkX = thisX + x;
                  int checkY = thisY + y;
                  if (!Utilities.isOutOfBoundsOfUsableMap(checkX, checkY)) {
                     SearchNode checkNode = this.multithreadedSearchNodes[checkX][checkY];
                     if (checkNode.isResettable()) {
                        checkNode.resetNode(g);
                     }

                     if (!checkNode.isClosed()) {
                        this.resettableMultithreadedSearchNodes.add(checkNode);
                        int movementCost = 0;
                        int nodeMValue = 0;
                        if (ignoreMovementCost) {
                           nodeMValue = 1000;
                        } else {
                           nodeMValue = checkNode.getM();
                        }

                        if ((checkX <= thisX || checkY <= thisY)
                           && (checkX >= thisX || checkY >= thisY)
                           && (checkX <= thisX || checkY >= thisY)
                           && (checkX >= thisX || checkY <= thisY)) {
                           movementCost = nodeMValue;
                        } else {
                           movementCost = (int)(nodeMValue + nodeMValue * 0.4);
                        }

                        if (!checkNode.isOpen()) {
                           checkNode.openNode();
                           if (this.baseMultithreadedSearchNode.getG() + movementCost <= movementCostMax) {
                              checkNode.setG(this.baseMultithreadedSearchNode.getG() + movementCost);
                              this.openMultithreadedSearchNodes.add(checkNode);
                              list.add(OrderedPair.getOrderedPair(checkX, checkY));
                           }
                        }
                     }
                  }
               }
            }
         }

         this.baseMultithreadedSearchNode.closeNode();
         if (this.openMultithreadedSearchNodes.size() <= 0) {
            break;
         }
      }

      return list;
   }

   public ArrayList<OrderedPair> buildPath(PathNode node) {
      ArrayList<OrderedPair> returnedPath = new ArrayList<>();
      PathNode nextNode = node;
      OrderedPair nextPair = OrderedPair.getOrderedPair(node.getX(), node.getY());
      returnedPath.add(nextPair);

      while (nextNode.getParent() != null) {
         nextNode = nextNode.getParent();
         nextPair = OrderedPair.getOrderedPair(nextNode.getX(), nextNode.getY());
         returnedPath.add(nextPair);
      }

      return returnedPath;
   }

   public ArrayList<OrderedPair> buildPath(SearchNode node) {
      ArrayList<OrderedPair> returnedPath = new ArrayList<>();
      SearchNode nextNode = node;
      OrderedPair nextPair = OrderedPair.getOrderedPair(node.getX(), node.getY());
      returnedPath.add(nextPair);

      while (nextNode.getParent() != null) {
         nextNode = nextNode.getParent();
         nextPair = OrderedPair.getOrderedPair(nextNode.getX(), nextNode.getY());
         returnedPath.add(nextPair);
      }

      return returnedPath;
   }

   public OrderedPair searchFor(
      int startX, int startY, int[][] searching, int value, boolean useForce, boolean useMovementCost, MapData.BlockMapGroup blockMapGroup
   ) throws SlickException {
      return this.searchFor(startX, startY, null, null, searching, value, null, null, null, null, false, useForce, useMovementCost, false, blockMapGroup);
   }

   public OrderedPair searchFor(
      int startX, int startY, byte[][] searching, int value, boolean useForce, boolean useMovementCost, MapData.BlockMapGroup blockMapGroup
   ) throws SlickException {
      return this.searchFor(startX, startY, null, searching, null, value, null, null, null, null, false, useForce, useMovementCost, false, blockMapGroup);
   }

   public OrderedPair searchFor(
      int startX, int startY, ArrayList<OrderedPair> searching, boolean useForce, boolean useMovementCost, MapData.BlockMapGroup blockMapGroup
   ) throws SlickException {
      return this.searchFor(startX, startY, searching, null, null, -1, null, null, null, null, false, useForce, useMovementCost, false, blockMapGroup);
   }

   public OrderedPair searchForResourceTile(
      int startX,
      int startY,
      ResourceModule.ResourceType resourceType,
      boolean selectedOnly,
      boolean useForce,
      boolean useMovementCost,
      MapData.BlockMapGroup blockMapGroup
   ) throws SlickException {
      return this.searchFor(startX, startY, null, null, null, -1, null, resourceType, null, null, selectedOnly, useForce, useMovementCost, false, blockMapGroup);
   }

   public OrderedPair searchForResource(
      int startX, int startY, ArrayList<ResourceBase> resourceList, boolean useForce, boolean useMovementCost, MapData.BlockMapGroup blockMapGroup
   ) throws SlickException {
      return this.searchFor(startX, startY, null, null, null, -1, resourceList, null, null, null, false, useForce, useMovementCost, false, blockMapGroup);
   }

   public OrderedPair searchForMob(
      int startX, int startY, ArrayList<MobBase> mobList, boolean useForce, boolean useMovementCost, MapData.BlockMapGroup blockMapGroup
   ) throws SlickException {
      return this.searchFor(startX, startY, null, null, null, -1, null, null, mobList, null, false, useForce, useMovementCost, false, blockMapGroup);
   }

   public OrderedPair searchForObject(
      int startX, int startY, ArrayList<ObjectBase> objectList, boolean useForce, boolean useMovementCost, MapData.BlockMapGroup blockMapGroup
   ) throws SlickException {
      return this.searchFor(startX, startY, null, null, null, -1, null, null, null, objectList, false, useForce, useMovementCost, true, blockMapGroup);
   }

   private OrderedPair searchFor(
      int startX,
      int startY,
      ArrayList<OrderedPair> orderedPairArray,
      byte[][] byteArray,
      int[][] intArray,
      int value,
      ArrayList<ResourceBase> resourceList,
      ResourceModule.ResourceType resourceTile,
      ArrayList<MobBase> mobList,
      ArrayList<ObjectBase> objectList,
      boolean selectedOnly,
      boolean useForce,
      boolean useMovementCost,
      boolean allowNearbyBlocked,
      MapData.BlockMapGroup blockMapGroup
   ) throws SlickException {
      this.resetOldSearchNodes();
      if (mobList != null) {
         for (MobBase m : mobList) {
            this.searchMap[m.getTileX()][m.getTileY()] = 1;
            this.resettableSearchNodes.add(this.searchNodes[m.getTileX()][m.getTileY()]);
         }
      } else if (orderedPairArray != null) {
         for (OrderedPair o : orderedPairArray) {
            this.searchMap[o.getX()][o.getY()] = 1;
            this.resettableSearchNodes.add(this.searchNodes[o.getX()][o.getY()]);
         }
      } else if (resourceList != null) {
         for (ResourceBase r : resourceList) {
            this.searchMap[r.getTileX()][r.getTileY()] = 1;
            this.resettableSearchNodes.add(this.searchNodes[r.getTileX()][r.getTileY()]);
         }
      }

      this.openSearchNodes.clear();
      this.baseSearchNode = this.searchNodes[startX][startY];
      this.openSearchNodes.add(this.searchNodes[startX][startY]);
      this.searchNodes[startX][startY].openNode();
      byte[][] blockMap = this.map.getMapData().getBlockMap(blockMapGroup);

      while (this.openSearchNodes.size() > 0) {
         this.baseSearchNode = this.openSearchNodes.poll();
         int thisX = this.baseSearchNode.getX();
         int thisY = this.baseSearchNode.getY();

         for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
               int checkX = thisX + x;
               int checkY = thisY + y;
               if ((checkX == startX || checkY == startY || x != 0 || y != 0)
                  && !Utilities.isOutOfBoundsOfUsableMap(checkX, checkY)
                  && this.map.getMap()[checkX][checkY][0] != 0) {
                  if (this.searchNodes[checkX][checkY].isResettable()) {
                     this.searchNodes[checkX][checkY].resetNode(blockMapGroup);
                  }

                  if (!this.searchNodes[checkX][checkY].isClosed()) {
                     this.resettableSearchNodes.add(this.searchNodes[checkX][checkY]);
                     if ((allowNearbyBlocked || useForce || blockMap[checkX][checkY] <= 0)
                        && (checkX <= thisX || checkY <= thisY || blockMap[checkX - 1][checkY] <= 0 && blockMap[checkX][checkY - 1] <= 0)
                        && (checkX >= thisX || checkY >= thisY || blockMap[checkX + 1][checkY] <= 0 && blockMap[checkX][checkY + 1] <= 0)
                        && (checkX <= thisX || checkY >= thisY || blockMap[checkX - 1][checkY] <= 0 && blockMap[checkX][checkY + 1] <= 0)
                        && (checkX >= thisX || checkY <= thisY || blockMap[checkX + 1][checkY] <= 0 && blockMap[checkX][checkY - 1] <= 0)) {
                        if (allowNearbyBlocked && blockMap[checkX][checkY] > 0) {
                           OrderedPair searchOut = this.searchForCheck(
                              checkX, checkY, orderedPairArray, byteArray, intArray, value, resourceList, resourceTile, mobList, objectList, selectedOnly
                           );
                           if (searchOut != null) {
                              return searchOut;
                           }

                           if (!useForce) {
                              continue;
                           }
                        }

                        SearchNode checkNode = this.searchNodes[checkX][checkY];
                        int movementCost = 0;
                        int nodeMValue = 1000;
                        if (useMovementCost) {
                           nodeMValue = checkNode.getM();
                        }

                        if ((checkX <= thisX || checkY <= thisY)
                           && (checkX >= thisX || checkY >= thisY)
                           && (checkX <= thisX || checkY >= thisY)
                           && (checkX >= thisX || checkY <= thisY)) {
                           movementCost = nodeMValue;
                        } else {
                           movementCost = (int)(nodeMValue + nodeMValue * 0.4);
                        }

                        if (!checkNode.isOpen()) {
                           checkNode.setG(this.baseSearchNode.getG() + movementCost);
                           OrderedPair searchOutx = this.searchForCheck(
                              checkX, checkY, orderedPairArray, byteArray, intArray, value, resourceList, resourceTile, mobList, objectList, selectedOnly
                           );
                           if (searchOutx != null) {
                              return searchOutx;
                           }

                           checkNode.openNode();
                           this.openSearchNodes.add(checkNode);
                        }
                     }
                  }
               }
            }
         }

         this.baseSearchNode.closeNode();
      }

      return null;
   }

   private OrderedPair searchForCheck(
      int checkX,
      int checkY,
      ArrayList<OrderedPair> orderedPairArray,
      byte[][] byteArray,
      int[][] intArray,
      int value,
      ArrayList<ResourceBase> resourceList,
      ResourceModule.ResourceType resourceTile,
      ArrayList<MobBase> mobList,
      ArrayList<ObjectBase> objectList,
      boolean selectedOnly
   ) throws SlickException {
      byte[][] selectionMap = null;
      if (selectedOnly) {
         selectionMap = this.workSelection.getSelectionMap();
      }

      if (orderedPairArray != null && this.searchMap[checkX][checkY] == 1) {
         return OrderedPair.getOrderedPair(checkX, checkY);
      } else if (byteArray != null && byteArray[checkX][checkY] == value) {
         return OrderedPair.getOrderedPair(checkX, checkY);
      } else if (intArray != null && intArray[checkX][checkY] == value) {
         return OrderedPair.getOrderedPair(checkX, checkY);
      } else if (resourceList != null && this.searchMap[checkX][checkY] == 1 && !this.resource.getResourceOnGround(checkX, checkY).isAssigned()) {
         return OrderedPair.getOrderedPair(checkX, checkY);
      } else if (resourceTile != null
         && this.resource.getResourceType()[checkX][checkY] == resourceTile
         && (!selectedOnly || selectionMap[checkX][checkY] != WorkSelectionModule.SelectionType.NONE.ordinal())
         && !this.mob.isWorkAssigned(checkX, checkY, Assignment.AssignmentType.HARVESTING)
         && !this.mob.isWorkAssigned(checkX, checkY, Assignment.AssignmentType.CLEARING)) {
         return OrderedPair.getOrderedPair(checkX, checkY);
      } else if (mobList != null && this.searchMap[checkX][checkY] == 1) {
         return OrderedPair.getOrderedPair(checkX, checkY);
      } else {
         return objectList != null && this.object.isObject(checkX, checkY) && this.objectLoop(objectList, checkX, checkY)
            ? OrderedPair.getOrderedPair(checkX, checkY)
            : null;
      }
   }

   private void resetOldSearchNodes() {
      for (SearchNode s : this.resettableSearchNodes) {
         this.searchMap[s.getX()][s.getY()] = 0;
         s.flagResettable();
      }

      this.resettableSearchNodes.clear();
   }

   private void resetOldMultithreadedSearchNodes() {
      for (SearchNode s : this.resettableMultithreadedSearchNodes) {
         this.multithreadedSearchMap[s.getX()][s.getY()] = 0;
         s.flagResettable();
      }

      this.resettableMultithreadedSearchNodes.clear();
   }

   private void resetOldPathNodes() {
      for (PathNode p : this.resettablePathNodes) {
         p.flagResettable();
      }

      this.resettablePathNodes.clear();
   }

   public MobBase mobFetch(ArrayList<MobBase> mobList, int x, int y) throws SlickException {
      for (int i = 0; i < mobList.size(); i++) {
         MobBase mob = mobList.get(i);
         if (mob.getTileX() == x && mob.getTileY() == y) {
            return mob;
         }
      }

      return null;
   }

   private boolean objectLoop(ArrayList<ObjectBase> objectList, int x, int y) throws SlickException {
      for (ObjectBase o : objectList) {
         if (o.isBuildingAtLocation(x, y)) {
            return true;
         }
      }

      return false;
   }
}
