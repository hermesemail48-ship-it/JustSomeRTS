package rtr.lighting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import rtr.ModuleBase;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.states.StateBase;
import rtr.utilities.Utilities;

public class Light {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private LightingModule lighting = (LightingModule)StateBase.getModule(ModuleBase.ModuleType.LIGHTING);
   private byte[][] lightMap;
   private byte lightRadius;
   private byte lightIntensity;
   private int lightX;
   private int lightY;
   private int lightDuration;
   private boolean useThreaded;
   private PriorityQueue<LightNode> openLightNodes = new PriorityQueue<>();
   private LightNode baseLightNode;
   private LightNode[][] lightNodes;

   public Light(int x, int y, byte r, boolean t) {
      this(x, y, r, (byte)1, t);
   }

   public Light(int x, int y, byte r, byte i, boolean t) {
      this(x, y, r, i, -1, t);
   }

   public Light(int x, int y, byte r, byte i, int d, boolean t) {
      this.useThreaded = t;
      if (this.useThreaded) {
         this.lightNodes = this.lighting.getLightNodesThreaded();
      } else {
         this.lightNodes = this.lighting.getLightNodes();
      }

      this.lightMap = new byte[r * 2 + 1][r * 2 + 1];
      this.lightRadius = r;
      this.lightIntensity = i;
      this.lightX = x;
      this.lightY = y;
      this.lightDuration = d;
      this.generateLight();
   }

   public void resetLight() {
      for (int i = 0; i < this.lightMap.length; i++) {
         Arrays.fill(this.lightMap[i], (byte)0);
      }

      this.generateLight();
   }

   private void generateLight() {
      if (!Utilities.isOutOfBoundsOfUsableMap(this.lightX, this.lightY)) {
         if (this.useThreaded) {
            this.lighting.resetOldLightNodesThreaded();
         } else {
            this.lighting.resetOldLightNodes();
         }

         this.openLightNodes.clear();
         this.baseLightNode = this.lightNodes[this.lightX][this.lightY];
         this.openLightNodes.add(this.lightNodes[this.lightX][this.lightY]);
         this.lightNodes[this.lightX][this.lightY].resetNode();
         this.lightNodes[this.lightX][this.lightY].openNode();
         ArrayList<LightNode> resettableNodes;
         if (this.useThreaded) {
            resettableNodes = this.lighting.getResettableLightNodesThreaded();
         } else {
            resettableNodes = this.lighting.getResettableLightNodes();
         }

         resettableNodes.add(this.lightNodes[this.lightX][this.lightY]);
         int movementCostMax = this.lightRadius * 10;

         while (this.openLightNodes.size() > 0) {
            this.baseLightNode = this.openLightNodes.poll();
            int thisX = this.baseLightNode.getX();
            int thisY = this.baseLightNode.getY();

            for (int x = -1; x < 2; x++) {
               for (int y = -1; y < 2; y++) {
                  if (x != 0 || y != 0) {
                     int checkX = thisX + x;
                     int checkY = thisY + y;
                     if (!Utilities.isOutOfBoundsOfUsableMap(checkX, checkY)) {
                        if (this.lightNodes[checkX][checkY].isResettable()) {
                           this.lightNodes[checkX][checkY].resetNode();
                        }

                        if (!this.lightNodes[checkX][checkY].isClosed()) {
                           resettableNodes.add(this.lightNodes[checkX][checkY]);
                           byte[][] blockMap = this.map.getMapData().getBlockMap(MapData.BlockMapGroup.LIGHTING);
                           if ((checkX <= thisX || checkY <= thisY || blockMap[checkX - 1][checkY] <= 0 && blockMap[checkX][checkY - 1] <= 0)
                              && (checkX >= thisX || checkY >= thisY || blockMap[checkX + 1][checkY] <= 0 && blockMap[checkX][checkY + 1] <= 0)
                              && (checkX <= thisX || checkY >= thisY || blockMap[checkX - 1][checkY] <= 0 && blockMap[checkX][checkY + 1] <= 0)
                              && (checkX >= thisX || checkY <= thisY || blockMap[checkX + 1][checkY] <= 0 && blockMap[checkX][checkY - 1] <= 0)) {
                              LightNode checkNode = this.lightNodes[checkX][checkY];
                              int movementCost = 0;
                              if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.LIGHTING)[checkX][checkY] > 0) {
                                 movementCost += 30;
                              }

                              if ((checkX <= thisX || checkY <= thisY)
                                 && (checkX >= thisX || checkY >= thisY)
                                 && (checkX <= thisX || checkY >= thisY)
                                 && (checkX >= thisX || checkY <= thisY)) {
                                 movementCost += 10;
                              } else {
                                 movementCost += 14;
                              }

                              if (!checkNode.isOpen()) {
                                 checkNode.setG(this.baseLightNode.getG() + movementCost);
                                 if (checkNode.getG() < movementCostMax) {
                                    checkNode.openNode();
                                    this.openLightNodes.add(checkNode);
                                    this.lightMap[checkX - this.lightX + this.lightRadius][checkY - this.lightY + this.lightRadius] = (byte)(
                                       this.lightIntensity + this.lightRadius - checkNode.getG() / 10
                                    );
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            this.baseLightNode.closeNode();
         }
      }
   }

   public void update() {
      this.lightDuration--;
   }

   public byte[][] getLightMap() {
      return this.lightMap;
   }

   public int getLightX() {
      return this.lightX;
   }

   public void setLightX(int x) {
      if (this.lightX != x) {
         this.lightX = x;
         this.resetLight();
         this.generateLight();
      }
   }

   public int getLightY() {
      return this.lightY;
   }

   public void setLightY(int y) {
      if (this.lightY != y) {
         this.lightY = y;
         this.resetLight();
         this.generateLight();
      }
   }

   public int getLightAmount() {
      return this.lightRadius;
   }

   public int getLightDuration() {
      return this.lightDuration;
   }
}
