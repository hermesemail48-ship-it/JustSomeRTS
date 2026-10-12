package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIMateGoCoitus extends AIBase {
   private static final int FIND_HOME = 0;
   private static final int PATHFIND_TO_HOME = 1;
   private static final int ENTER_HOME = 2;
   private static AIBase resetBranch;
   private static AIBase exitBranch;

   public AIMateGoCoitus(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_MATE_GO_COITUS;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowSocial() && this.thisMob.getAssignedHome() != -1;
   }

   @Override
   public boolean tryRun() {
      return this.allowRun()
         && Utilities.randomInt(this.thisMobFlags.getSocialCoitusChance()) == 0
         && this.thisMob.getSocialCoitusDelayTick() >= this.thisMobFlags.getSocialCoitusDelay();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         exitBranch = this.thisMob.getAIMateCoitus();
         if (this.runDropResource()) {
            ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
            MobBase mate = this.thisMob.getMate();
            if (home != null && mate != null) {
               switch (this.aiStep) {
                  case 0:
                     this.findHome();
                     break;
                  case 1:
                     this.pathfindToHome();
                     break;
                  case 2:
                     this.enterHome();
               }
            } else {
               this.switchNode(resetBranch);
            }
         }
      }
   }

   private void findHome() throws SlickException {
      ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
      Text.setVariableText("buildingName", home.getFormattedName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.mateGoCoitus.findHome"));
      if (!this.thisMob.canReach(home)) {
         this.switchNode(resetBranch);
      } else {
         OrderedPair homeCoordinates = home.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
         this.requestPath(homeCoordinates);
         this.aiStep = 1;
      }
   }

   private void pathfindToHome() throws SlickException {
      Text.setVariableText("mobName", this.thisMob.getMate().getFormattedName(true));
      this.thisMob.setCurrentTask(Text.getText("aiTask.mateGoCoitus.pathfindToHome"));
      this.runPath();
      if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void enterHome() throws SlickException {
      ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
      MobBase mate = this.thisMob.getMate();
      Text.setVariableText("mobName", mate.getFormattedName(true));
      this.thisMob.setCurrentTask(Text.getText("aiTask.mateGoCoitus.enterHome"));
      if (this.object.getBuildingAtLocation(this.thisMob.getTileX(), this.thisMob.getTileY()) == home && this.thisMobFlags.getCanEnterHome()) {
         this.thisMob.enterBuilding();
         this.switchNode(exitBranch);
      } else {
         this.switchNode(resetBranch);
      }
   }
}
