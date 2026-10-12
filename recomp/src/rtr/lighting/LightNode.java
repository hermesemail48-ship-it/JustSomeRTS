package rtr.lighting;

import java.util.Comparator;
import rtr.ModuleBase;
import rtr.map.MapModule;
import rtr.states.StateBase;

public class LightNode implements Comparable<LightNode>, Comparator<LightNode> {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private int gValue;
   private int nodeX;
   private int nodeY;
   private boolean open;
   private boolean closed;
   private boolean resettable;

   public LightNode(int x, int y) {
      this.nodeX = x;
      this.nodeY = y;
   }

   public void flagResettable() {
      this.resettable = true;
   }

   public boolean isResettable() {
      return this.resettable;
   }

   public void resetNode() {
      this.gValue = 0;
      this.open = false;
      this.closed = false;
      this.resettable = false;
   }

   public int getG() {
      return this.gValue;
   }

   public void setG(int g) {
      this.gValue = g;
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

   public int compareTo(LightNode n) {
      return this.gValue - n.getG();
   }

   public int compare(LightNode n1, LightNode n2) {
      return n1.getG() == n2.getG() ? n1.getG() : n1.getG() - n2.getG();
   }
}
