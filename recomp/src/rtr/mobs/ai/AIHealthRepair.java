package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIHealthRepair extends AIBase {
   private static final int ATTEMPT_HEAL_SELF = 0;
   private static final int FIND_POST = 1;
   private static final int PATHFIND_TO_BUILDING = 2;
   private static final int POSTING = 3;
   private static final int PATHFIND_TO_NEW_POST_POSITION = 4;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIHealthRepair(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_HEALTH_REPAIR;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.isDying() ? false : this.thisMobFlags.getAllowHealthRepair();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun()
         && (
            Utilities.randomInt(this.thisMobFlags.getHealthRepairChance()) == 0
                  && this.thisMob.getHitPoints() <= this.thisMob.getHitPointsMax() * this.thisMobFlags.getHealthRepairPercentage()
               || Utilities.randomInt(this.thisMobFlags.getHealthRepairCriticalChance()) == 0
                  && (
                     this.thisMob.getHitPoints() <= this.thisMob.getHitPointsMax() * this.thisMobFlags.getHealthRepairCriticalPercentage()
                        || this.thisMob.isCompromised()
                  )
         );
   }

   private boolean isDying() {
      return this.thisMobFlags.getAllowHunger() && this.thisMob.getHunger() <= 0 ? true : this.thisMobFlags.getAllowThirst() && this.thisMob.getThirst() <= 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         failBranch = this.thisMob.getAIGoHome();
         if (!this.isDying() && (this.thisMob.getHitPoints() < this.thisMob.getHitPointsMax() || this.thisMob.isCompromised())) {
            switch (this.aiStep) {
               case 0:
                  this.attemptHealSelf();
                  break;
               case 1:
                  this.findPost();
                  break;
               case 2:
                  this.pathfindToBuilding();
                  break;
               case 3:
                  this.posting();
                  break;
               case 4:
                  this.pathfindToNewPostPosition();
            }
         } else {
            this.switchNode(resetBranch);
         }
      }
   }

   private void attemptHealSelf() throws SlickException {
      if (this.thisMob.hasRecoveryResource() && !this.thisMob.hasCarriedResources()) {
         this.repairHealthInInventory(true);
      } else {
         if (this.thisMob.getHitPoints() <= this.thisMob.getHitPointsMax() * this.thisMobFlags.getHealthRepairCriticalPercentage()) {
            if (this.thisMob.hasCarriedResources()) {
               if (this.thisMob.getInventoryRecoverySlot() == null) {
                  for (ResourceBase r : this.thisMob.getCarriedResourcesList()) {
                     if (r.getType().getTemplate().getHealthPercentPerUse() > 0
                        && r.getType().getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.RECOVERY) {
                        this.thisMob.equipResource(r, r.getType().getTemplate().getResourceSlot());
                        return;
                     }
                  }
               }

               this.runDropResource();
               return;
            }

            if (this.thisMobFlags.getAllowHome() && this.thisMob.getAssignedHome() >= 0) {
               this.aiStep = 1;
               return;
            }
         }

         this.aiStep = 1;
      }
   }

   private void findPost() throws SlickException {
      ArrayList<ObjectBase> validClinics = new ArrayList<>();

      for (MobBase m : this.mob.getMobsByAssignment(Assignment.AssignmentType.MEDIC_WORK, false)) {
         ObjectBase o = m.getAssignment().getPrimaryObject();
         if (o != null && this.thisMob.canReach(o, true) && !validClinics.contains(o)) {
            validClinics.add(o);
         }
      }

      if (validClinics.size() <= 0) {
         this.switchNode(failBranch);
      } else {
         OrderedPair closestPost = this.pathFinder
            .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), validClinics, false, true, this.thisMobFlags.getBlockMapGroup());
         if (closestPost == null) {
            this.switchNode(failBranch);
         } else {
            OrderedPair validCoordinates = this.object.getBuildingAtLocation(closestPost).getRandomCoordinates(this.thisMob, true, true);
            if (validCoordinates != null) {
               Assignment assignment = this.thisMob.getAssignment();
               assignment.reset();
               assignment.setPrimaryCoordinates(closestPost);
               assignment.setPrimaryObject(this.object.getBuildingAtLocation(closestPost.getX(), closestPost.getY()));
               assignment.setAssignmentType(Assignment.AssignmentType.SEEKING_MEDIC);
               assignment.finalizeAssignment();
               this.requestPath(validCoordinates);
               this.aiStep = 2;
            }
         }
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.healthRepair.pathfindToBuilding"));
      this.runPath();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject == null || currentObject.isDestroyed()) {
         this.aiStep = 1;
      } else if (this.pathFailed) {
         this.aiStep = 1;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 3;
         }
      }
   }

   private void posting() throws SlickException {
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject != null && !currentObject.isDestroyed()) {
         ArrayList<MobBase> medicsPosted = this.mob.getMobsByAssignment(Assignment.AssignmentType.MEDIC_WORK, false);
         boolean foundMedicHere = false;

         for (MobBase m : medicsPosted) {
            ObjectBase o = m.getAssignment().getPrimaryObject();
            MobBase target = m.getAssignment().getPrimaryMob();
            if (this.thisMob.getAssignment().getPrimaryObject() == o) {
               foundMedicHere = true;
            }

            if (target == this.thisMob) {
               Text.setVariableText("mobName", m.getFormattedName(false));
               this.thisMob.setCurrentTask(Text.getText("aiTask.healthRepair.postingWaitingForMedic"));
               return;
            }
         }

         Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
         this.thisMob.setCurrentTask(Text.getText("aiTask.healthRepair.postingWaiting"));
         if (Utilities.randomInt(400) == 0) {
            OrderedPair newRandomCoordinate = this.thisMob.getAssignment().getPrimaryObject().getRandomCoordinates(this.thisMob, true, true);
            if (newRandomCoordinate != null) {
               this.requestPath(newRandomCoordinate);
               this.aiStep = 4;
               return;
            }
         }

         if (Utilities.randomInt(1000) == 0 && !foundMedicHere) {
            this.switchNode(failBranch);
         } else if (this.isDying() || this.thisMob.getHitPoints() >= this.thisMob.getHitPointsMax() && !this.thisMob.isCompromised()) {
            this.switchNode(resetBranch);
         }
      } else {
         this.aiStep = 1;
      }
   }

   private void pathfindToNewPostPosition() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.healthRepair.postingWaiting"));
      this.runPath();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject == null || currentObject.isDestroyed()) {
         this.aiStep = 1;
      } else if (this.pathFailed) {
         this.aiStep = 1;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 3;
         }
      }
   }
}
