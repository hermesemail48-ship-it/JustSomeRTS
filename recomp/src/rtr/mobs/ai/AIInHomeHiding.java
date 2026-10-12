package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;

public class AIInHomeHiding extends AIBase {
   private static AIBase exitBranchPanic;
   private static AIBase exitBranchInHome;

   public AIInHomeHiding(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_IN_HOME_HIDING;
      this.interruptDeath = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowHome() && this.thisMob.getAssignedHome() >= 0 && this.thisMob.isInsideBuilding();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         exitBranchPanic = this.thisMob.getAIPanic();
         exitBranchInHome = this.thisMob.getAIInHome();
         this.thisMob.runPanicHideInHomeDurationTick(1);
         ObjectBase currentBuilding = this.object.getBuildingAtLocation(this.thisMob.getTileX(), this.thisMob.getTileY());
         if (currentBuilding == null) {
            this.thisMob.leaveBuilding();
            this.thisMob.resetPanicHideInHomeDurationTick();
            this.switchNode(exitBranchPanic);
         } else if (this.thisMob.getPanicHideInHomeDurationTick() > this.thisMobFlags.getPanicHideInHomeDuration()) {
            this.thisMob.resetPanicHideInHomeDurationTick();
            this.switchNode(exitBranchInHome);
         } else {
            this.thisMob.setCurrentTask(Text.getText("aiTask.inHomeHiding.hiding"));
         }
      }
   }
}
