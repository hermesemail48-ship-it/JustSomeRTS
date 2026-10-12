package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;

public class AIRangerSummoned extends AIBase {
   private static final int BE_SUMMONED = 0;
   private static final int PATHFIND_TO_SUMMONS = 1;
   private static AIBase resetBranch;

   public AIRangerSummoned(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_RANGER_SUMMONED;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMob.getMobJobType().getJob().isGuard();
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
         if (this.runDropResource()) {
            if (this.thisMob.getAssignment().getPrimaryMob() == null) {
               this.switchNode(resetBranch);
            } else {
               switch (this.aiStep) {
                  case 0:
                     this.beSummoned();
                     break;
                  case 1:
                     this.pathfindToSummons();
               }
            }
         }
      }
   }

   private void beSummoned() throws SlickException {
      this.requestPath(this.thisMob.getAssignment().getPrimaryMob().getTileX(), this.thisMob.getAssignment().getPrimaryMob().getTileY());
      this.font.newFloater("iconConfused", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
      this.font.newFloater("iconExcite", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
      this.aiStep = 1;
   }

   private void pathfindToSummons() throws SlickException {
      Text.setVariableText("mobName", this.thisMob.getAssignment().getPrimaryMob().getFormattedName(true));
      this.thisMob.setCurrentTask(Text.getText("aiTask.rangerSummoned.pathfindToSummons"));
      this.followMob(this.thisMob.getAssignment().getPrimaryMob());
      if (this.pathFailed || this.pathCompleted) {
         this.switchNode(resetBranch);
      }
   }
}
