package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIFindBetterHome extends AIBase {
   private static final int SEARCH_FOR_HOME = 0;
   private static final int VISIT_NEW_HOME = 1;
   private static AIBase resetBranch;
   private static AIBase exitBranch;

   public AIFindBetterHome(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_FIND_BETTER_HOME;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      if (!this.thisMobFlags.getAllowHome()) {
         return false;
      } else {
         MobBase partner = null;
         if (this.thisMob.getMate() != null) {
            partner = this.thisMob.getMate();
         } else if (this.thisMob.getMother() != null && this.mob.getMobHashMap().get(this.thisMob.getMother().getID()) != null) {
            partner = this.thisMob.getMother();
         } else if (this.thisMob.getFather() != null && this.mob.getMobHashMap().get(this.thisMob.getFather().getID()) != null) {
            partner = this.thisMob.getFather();
         } else if (this.thisMob.getOwner() != null && this.thisMobFlags.canSocialHaveOwner()) {
            partner = this.thisMob.getOwner();
         }

         if (partner != null && partner.getAssignedHome() != -1) {
            ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
            ObjectBase partnerHome = this.object.getObjects().get(partner.getAssignedHome());
            if (home != null) {
               if (home.getObjectFlags().getOccupantsJob() == this.thisMob.getMobJobType()
                  && partnerHome.getObjectFlags().getOccupantsJob() != this.thisMob.getMobJobType()) {
                  return false;
               }

               if (home.getObjectFlags().getOccupantsTypes().contains(this.thisMob.getMobType())
                  && !partnerHome.getObjectFlags().getOccupantsTypes().contains(this.thisMob.getMobType())) {
                  return false;
               }
            }

            return this.thisMob.getID() > partner.getID()
               && home != partnerHome
               && (partnerHome.getObjectFlags().getOccupantsJob() == null || partnerHome.getObjectFlags().getOccupantsJob() == this.thisMob.getMobJobType())
               && (
                  partnerHome.getObjectFlags().getOccupantsTypes().size() == 0
                     || partnerHome.getObjectFlags().getOccupantsTypes().contains(this.thisMob.getMobType())
               )
               && this.thisMob.canReach(partnerHome)
               && partnerHome.getOccupants().size() < partnerHome.getObjectFlags().getOccupantsMax();
         } else {
            return false;
         }
      }
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getHomeFindChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         exitBranch = this.thisMob.getAIGoHome();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.searchForHome();
                  break;
               case 1:
                  this.visitNewHome();
            }
         }
      }
   }

   private void searchForHome() throws SlickException {
      MobBase partner = null;
      ObjectBase attemptHome = null;
      if (this.thisMob.getMate() != null) {
         partner = this.thisMob.getMate();
      } else if (this.thisMob.getMother() != null && this.mob.getMobHashMap().get(this.thisMob.getMother().getID()) != null) {
         partner = this.thisMob.getMother();
      } else if (this.thisMob.getFather() != null && this.mob.getMobHashMap().get(this.thisMob.getFather().getID()) != null) {
         partner = this.thisMob.getFather();
      } else if (this.thisMob.getOwner() != null && this.thisMobFlags.canSocialHaveOwner()) {
         partner = this.thisMob.getOwner();
      }

      if (partner != null) {
         attemptHome = this.object.getObjects().get(partner.getAssignedHome());
      }

      if (attemptHome != null && attemptHome.getOccupants().size() < attemptHome.getObjectFlags().getOccupantsMax()) {
         byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
         int randomX = attemptHome.getObjectX() + Utilities.randomInt(attemptHome.getObjectFlags().getWidth());

         int randomY;
         for (randomY = attemptHome.getObjectY() + Utilities.randomInt(attemptHome.getObjectFlags().getHeight());
            blockMap[randomX][randomY] > 0 || !this.thisMob.canReach(randomX, randomY);
            randomY = attemptHome.getObjectY() + Utilities.randomInt(attemptHome.getObjectFlags().getHeight())
         ) {
            randomX = attemptHome.getObjectX() + Utilities.randomInt(attemptHome.getObjectFlags().getWidth());
         }

         Assignment assignment = this.thisMob.getAssignment();
         assignment.reset();
         assignment.setPrimaryObject(attemptHome);
         assignment.setPrimaryCoordinates(OrderedPair.getOrderedPair(randomX, randomY));
         assignment.setAssignmentType(Assignment.AssignmentType.FINDING_HOME);
         assignment.finalizeAssignment();
         this.requestPath(assignment.getPrimaryCoordinates());
         this.aiStep = 1;
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void visitNewHome() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.findBetterHome.visitNewHome"));
      ObjectBase attemptHome = this.thisMob.getAssignment().getPrimaryObject();
      if (attemptHome != null && !attemptHome.isDestroyed()) {
         if (attemptHome.getOccupants().size() >= attemptHome.getObjectFlags().getOccupantsMax()) {
            this.aiStep = 0;
         } else {
            this.runPath();
            if (this.pathFailed) {
               this.switchNode(resetBranch);
            } else if (this.pathCompleted) {
               Text.setVariableText("buildingName", attemptHome.getFormattedName());
               this.thisMob.setCurrentTask(Text.getText("aiTask.findBetterHome.visitNewHomeClaim"));
               if (attemptHome.getOccupants().size() < attemptHome.getObjectFlags().getOccupantsMax()) {
                  if (this.object.getObjects().get(this.thisMob.getAssignedHome()) != null) {
                     this.object.getObjects().get(this.thisMob.getAssignedHome()).getOccupants().remove(this.thisMob);
                  }

                  this.thisMob.awardXP(Utilities.randomInt(20) + 20);
                  if (this.thisMobFlags.getAllowGodExperience()) {
                     this.god.increaseGodXP(0.03, GodModule.GodXPType.FINDING_NEW_HOME);
                  }

                  this.thisMob.setAssignedHome(attemptHome.getID());
                  this.object.getObjects().get(attemptHome.getID()).getOccupants().add(this.thisMob);
                  this.switchNode(exitBranch);
               } else {
                  this.switchNode(resetBranch);
               }
            }
         }
      } else {
         this.aiStep = 0;
      }
   }
}
