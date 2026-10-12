package rtr.spawning;

import java.util.ArrayList;
import java.util.PriorityQueue;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.mobs.pathfinder.PathNode;
import rtr.states.StateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class CorruptionRoadPathFinder {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private PriorityQueue<PathNode> openPathNodes = new PriorityQueue<>();
   private ArrayList<PathNode> resettablePathNodes = new ArrayList<>();
   private PathNode basePathNode;
   private int mapWidth = 256;
   private int mapHeight = 256;
   private PathNode[][] pathNodes = new PathNode[this.mapWidth][this.mapHeight];

   public CorruptionRoadPathFinder() throws SlickException {
      for (int x = 0; x < this.pathNodes.length; x++) {
         for (int y = 0; y < this.pathNodes[x].length; y++) {
            this.pathNodes[x][y] = new PathNode(x, y);
         }
      }
   }

   public ArrayList<OrderedPair> findPath(int startX, int startY, int goalX, int goalY) {
      if (goalX < 0 || goalX > 255 || goalY < 0 || goalY > 255) {
         return null;
      } else if (startX == goalX && startY == goalY) {
         ArrayList<OrderedPair> returnedPath = new ArrayList<>();
         returnedPath.add(OrderedPair.getOrderedPair(goalX, goalY));
         return returnedPath;
      } else {
         MapData.BlockMapGroup g = MapData.BlockMapGroup.ROAD;
         int pathVariance = 1;
         byte[][] blockMap = this.map.getMapData().getBlockMap(g);
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
               for (int y = -1; y < 2; y++) {
                  if ((x != 0 || y != 0) && (x != -1 || y != -1) && (x != 1 || y != 1) && (x != -1 || y != 1) && (x != 1 || y != -1)) {
                     int checkX = thisX + x;
                     int checkY = thisY + y;
                     if (!Utilities.isOutOfBoundsOfUsableMap(checkX, checkY) && this.map.getMap()[checkX][checkY][0] != 0) {
                        if (this.pathNodes[checkX][checkY].isResettable()) {
                           this.pathNodes[checkX][checkY].resetNode(goalX, goalY, pathVariance, g);
                        }

                        if (!this.pathNodes[checkX][checkY].isClosed()) {
                           this.resettablePathNodes.add(this.pathNodes[checkX][checkY]);
                           if (blockMap[checkX][checkY] <= 0
                              && (
                                 checkX <= thisX
                                    || checkY <= thisY
                                    || (Utilities.isOutOfBoundsOfUsableMap(checkX - 1, checkY) || blockMap[checkX - 1][checkY] <= 0)
                                       && (Utilities.isOutOfBoundsOfUsableMap(checkX, checkY - 1) || blockMap[checkX][checkY - 1] <= 0)
                              )
                              && (
                                 checkX >= thisX
                                    || checkY >= thisY
                                    || (Utilities.isOutOfBoundsOfUsableMap(checkX + 1, checkY) || blockMap[checkX + 1][checkY] <= 0)
                                       && (Utilities.isOutOfBoundsOfUsableMap(checkX, checkY + 1) || blockMap[checkX][checkY + 1] <= 0)
                              )
                              && (
                                 checkX <= thisX
                                    || checkY >= thisY
                                    || (Utilities.isOutOfBoundsOfUsableMap(checkX - 1, checkY) || blockMap[checkX - 1][checkY] <= 0)
                                       && (Utilities.isOutOfBoundsOfUsableMap(checkX, checkY + 1) || blockMap[checkX][checkY + 1] <= 0)
                              )
                              && (
                                 checkX >= thisX
                                    || checkY <= thisY
                                    || (Utilities.isOutOfBoundsOfUsableMap(checkX + 1, checkY) || blockMap[checkX + 1][checkY] <= 0)
                                       && (Utilities.isOutOfBoundsOfUsableMap(checkX, checkY - 1) || blockMap[checkX][checkY - 1] <= 0)
                              )) {
                              PathNode checkNode = this.pathNodes[checkX][checkY];
                              int movementCost = 0;
                              int nodeMValue = checkNode.getM();
                              if (checkNode.isOpen()) {
                                 int gValueCheck = this.basePathNode.getG() + nodeMValue;
                                 if (gValueCheck < checkNode.getG()) {
                                    checkNode.setParent(this.basePathNode);
                                 }
                              } else {
                                 checkNode.openNode();
                                 checkNode.setParent(this.basePathNode);
                                 checkNode.setG(this.basePathNode.getG() + nodeMValue);
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

   private void resetOldPathNodes() {
      for (PathNode p : this.resettablePathNodes) {
         p.flagResettable();
      }

      this.resettablePathNodes.clear();
   }
}
