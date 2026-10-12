package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.utilities.Utilities;

public class AIVisitMate extends AIBase {
   private static final int FIND_MATE = 0;
   private static final int PATHFIND_TO_MATE = 1;
   private static final int VISIT_WITH_MATE = 2;
   private static AIBase resetBranch;
   private static AIBase exitBranch;

   public AIVisitMate(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_VISIT_MATE;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowWander()
         && this.thisMobFlags.getAllowSocial()
         && this.thisMob.getMate() != null
         && this.thisMob.canReach(this.thisMob.getMate())
         && !this.thisMob.getMate().isInsideBuilding();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getWanderMateWeight()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         exitBranch = this.thisMob.getAIChat();
         if (this.runDropResource()) {
            if (this.thisMob.getMate() == null) {
               this.switchNode(resetBranch);
            } else {
               switch (this.aiStep) {
                  case 0:
                     this.findMate();
                     break;
                  case 1:
                     this.pathfindToMate();
                     break;
                  case 2:
                     this.visitWithMate();
                     return;
               }
            }
         }
      }
   }

   private void findMate() throws SlickException {
      this.requestPath(this.thisMob.getMate().getTileX(), this.thisMob.getMate().getTileY());
      this.aiStep = 1;
   }

   private void pathfindToMate() throws SlickException {
      Text.setVariableText("mobName", this.thisMob.getMate().getFormattedName(true));
      this.thisMob.setCurrentTask(Text.getText("aiTask.visitMate.pathfindToMate"));
      this.followMob(this.thisMob.getMate());
      if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void visitWithMate() throws SlickException {
      this.font.newFloater("iconHitPoints", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
      this.font.newFloater("iconHitPoints", this.thisMob.getMate().getMobX() + 4.0F, this.thisMob.getMate().getMobY() - 16.0F);
      this.thisMob.getMate().interruptNode(this.thisMob.getMate().getAIChat());
      this.thisMob.getMate().getAssignment().setPrimaryMob(this.thisMob);
      if (this.thisMobFlags.getAllowGodExperience()) {
         this.god.increaseGodXP(0.01, GodModule.GodXPType.VISITING_MATE);
      }

      this.switchNode(exitBranch);
      this.thisMob.getAssignment().setPrimaryMob(this.thisMob.getMate());
   }
}
