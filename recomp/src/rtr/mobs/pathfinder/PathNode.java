package rtr.mobs.pathfinder;

import java.util.Comparator;
import rtr.ModuleBase;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.states.StateBase;

public class PathNode implements Comparable<PathNode>, Comparator<PathNode> {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private int hValue;
   private int gValue;
   private int fValue;
   private int mValue;
   private int nodeX;
   private int nodeY;
   private boolean open;
   private boolean closed;
   private boolean resettable;
   private PathNode parent;

   public PathNode(int x, int y) {
      this.nodeX = x;
      this.nodeY = y;
      this.flagResettable();
   }

   public void flagResettable() {
      this.resettable = true;
   }

   public boolean isResettable() {
      return this.resettable;
   }

   public void resetNode(int gX, int gY, int variance, MapData.BlockMapGroup g) {
      this.gValue = 0;
      this.fValue = 0;
      this.mValue = this.map.getMapData().getMovementCost(g, this.nodeX, this.nodeY);
      int dx = gX - this.nodeX;
      int dy = gY - this.nodeY;
      this.hValue = (int)Math.sqrt(dx * dx + dy * dy) * variance;
      this.open = false;
      this.closed = false;
      this.parent = null;
      this.resettable = false;
   }

   public int getH() {
      return this.hValue;
   }

   public void setH(int h) {
      this.hValue = h;
   }

   public int getG() {
      return this.gValue;
   }

   public void setG(int g) {
      this.gValue = g;
   }

   public int getF() {
      return this.fValue;
   }

   public void setF(int f) {
      this.fValue = f;
   }

   public void calcF() {
      this.fValue = this.gValue + this.hValue;
   }

   public int getM() {
      return this.mValue;
   }

   public void setM(int m) {
      this.mValue = m;
   }

   public int getX() {
      return this.nodeX;
   }

   public int getY() {
      return this.nodeY;
   }

   public void openNode() {
      this.open = true;
      this.closed = false;
   }

   public void closeNode() {
      this.open = false;
      this.closed = true;
   }

   public boolean isOpen() {
      return this.open;
   }

   public boolean isClosed() {
      return this.closed;
   }

   public PathNode getParent() {
      return this.parent;
   }

   public void setParent(PathNode node) {
      this.parent = node;
   }

   public int compareTo(PathNode n) {
      return this.fValue - n.getF();
   }

   public int compare(PathNode n1, PathNode n2) {
      return n1.getF() == n2.getF() ? n1.getF() : n1.getF() - n2.getF();
   }
}
