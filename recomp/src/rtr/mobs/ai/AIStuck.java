package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.system.threadpool.RTRThreadPool;

public class AIStuck extends AIBase {
   private static final int REQUEST_UNSTUCK_PATH = 0;
   private static final int PATHFIND_OFF_BLOCKED_TERRAIN = 1;
   private static AIBase resetBranch;
   private static AIBase resetBranchWithResource;

   public AIStuck(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_STUCK;
      this.interruptDeath = true;
   }

   @Override
   public boolean allowRun() {
      byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
      return blockMap[this.thisMob.getTileX()][this.thisMob.getTileY()] > 0;
   }

   @Override
   public boolean tryRun() {
      return this.allowRun();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         resetBranchWithResource = this.thisMob.getAICaptureAnimalsInHand();
         switch (this.aiStep) {
            case 0:
               this.stuck = true;
               this.waitingForPath = true;
               RTRThreadPool.addNewUnstickPathRequest(this.thisMob, this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getBlockMapGroup());
               this.aiStep = 1;
               break;
            case 1:
               this.pathfindOffBlockedTerrain();
         }
      }
   }

   private void pathfindOffBlockedTerrain() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.stuck.pathfindOffBlockedTerrain"));
      this.runStuckPath();
      if (this.pathCompleted) {
         if (this.thisMob.hasCarriedResources()) {
            this.switchNode(resetBranchWithResource);
         } else {
            this.switchNode(resetBranch);
         }
      }
   }
}
