package rtr.mobs.pathfinder;

import java.util.Comparator;
import rtr.ModuleBase;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.states.StateBase;

public class SearchNode implements Comparable<SearchNode>, Comparator<SearchNode> {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private int gValue;
   private int mValue;
   private int nodeX;
   private int nodeY;
   private boolean open;
   private boolean closed;
   private boolean resettable;
   private SearchNode parent;

   public SearchNode(int x, int y) {
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

   public void resetNode(MapData.BlockMapGroup g) {
      this.mValue = this.map.getMapData().getMovementCost(g, this.nodeX, this.nodeY);
      this.gValue = 0;
      this.open = false;
      this.closed = false;
      this.parent = null;
      this.resettable = false;
   }

   public int getG() {
      return this.gValue;
   }

   public void setG(int g) {
      this.gValue = g;
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

   public SearchNode getParent() {
      return this.parent;
   }

   public void setParent(SearchNode node) {
      this.parent = node;
   }

   public int compareTo(SearchNode n) {
      return this.gValue - n.getG();
   }

   public int compare(SearchNode n1, SearchNode n2) {
      return n1.getG() == n2.getG() ? n1.getG() : n1.getG() - n2.getG();
   }
}
