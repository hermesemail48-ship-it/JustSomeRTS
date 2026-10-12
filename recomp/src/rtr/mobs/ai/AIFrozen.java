package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;

public class AIFrozen extends AIBase {
   private static AIBase resetBranch;

   public AIFrozen(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_FROZEN;
      this.interruptDeath = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowFrozen() && this.thisMob.isFrozen();
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
         this.thisMob.setCurrentTask(Text.getText("aiTask.frozen.frozen"));
         if (this.thisMob.hasCarriedResources()) {
            this.thisMob.forceDropAllCarried();
         }

         if (!this.thisMob.isFrozen()) {
            this.switchNode(resetBranch);
         }
      }
   }
}
