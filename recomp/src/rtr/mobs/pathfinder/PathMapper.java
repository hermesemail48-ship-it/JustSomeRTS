package rtr.mobs.pathfinder;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.states.StateBase;
import rtr.system.threadpool.RTRThreadPool;
import rtr.utilities.ArrayFillUtil;
import rtr.utilities.OrderedPair;

public class PathMapper {
   private MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   private byte[][] pathMapsCurrent;
   private byte[][] pathMapNew;
   private int updateWaitPeriod = 100;
   private int updateWaitPeriodTick;
   private int mapHeight = 256;
   private int mapWidth = 256;
   protected ArrayList<MobBase> mobListInUse;

   public PathMapper() throws SlickException {
      this.pathMapsCurrent = new byte[this.mapWidth][this.mapHeight];
      this.pathMapNew = new byte[this.mapWidth][this.mapHeight];
      this.updateFullPathMap();
   }

   public void update() {
      this.updateWaitPeriodTick++;
      this.updateWaitPeriod = 100;
      if (this.updateWaitPeriodTick >= this.updateWaitPeriod) {
         while (!RTRThreadPool.isPathMapperRequestsComplete()) {
            try {
               Thread.sleep(5L);
            } catch (InterruptedException var2) {
               var2.printStackTrace();
            }
         }

         this.copyNewPathMapToCurrent();
         ArrayFillUtil.fill(this.pathMapNew, (byte)0);
         this.mobListInUse = new ArrayList<>();
         this.mobListInUse.addAll(this.mob.getMobArray());
         RTRThreadPool.generatePathMapperMap(this.mobListInUse);
         this.updateWaitPeriodTick = 0;
      }
   }

   private void updateFullPathMap() throws SlickException {
      ArrayFillUtil.fill(this.pathMapNew, (byte)0);

      for (MobBase m : this.mob.getMobArray()) {
         this.createSinglePath(m);
      }

      this.copyNewPathMapToCurrent();
   }

   public void createSinglePath(MobBase m) {
      ArrayList<OrderedPair> pathCoordinates = m.getActiveNode().getPathCoordinatesList();
      if (pathCoordinates != null && pathCoordinates.size() > 0) {
         for (int p = m.getActiveNode().getPathStep() - 1; p >= 0; p--) {
            OrderedPair o = pathCoordinates.get(p);
            if (this.pathMapNew[o.getX()][o.getY()] < 100) {
               this.pathMapNew[o.getX()][o.getY()]++;
            }
         }
      }
   }

   private void copyNewPathMapToCurrent() {
      for (int x = 0; x < this.pathMapsCurrent.length; x++) {
         for (int y = 0; y < this.pathMapsCurrent[x].length; y++) {
            this.pathMapsCurrent[x][y] = this.pathMapNew[x][y];
         }
      }
   }

   public byte[][] getPathMap() {
      return this.pathMapsCurrent;
   }
}
