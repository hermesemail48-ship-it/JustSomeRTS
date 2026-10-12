package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AICaptureAnimalsInHand extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_BUILDING = 1;
   private static final int DROP_OFF_ANIMAL = 2;
   private static final int DROP_OFF_LIFT = 3;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AICaptureAnimalsInHand(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CAPTURE_ANIMALS_IN_HAND;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMob.hasCarriedResources();
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
         failBranch = this.thisMob.getAISlaughterAnimalInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToBuilding();
               break;
            case 2:
               this.dropOffAnimal();
               break;
            case 3:
               this.dropOffLift();
         }
      }
   }

   private void findWork() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      ObjectBase deliveryOut = assignment.getDeliveryObject();
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else if (!this.thisMob.getMobJobType().getJob().canCaptureAnimals()) {
         this.switchNode(failBranch);
      } else if (!this.thisMob.getCarriedResourcesList().get(0).hasCaptured()) {
         this.switchNode(failBranch);
      } else {
         if (deliveryOut == null) {
            ArrayList<ObjectBase> objects = this.object.getObjectMasterBuildingPriorityList();
            ResourceBase capturedMobResource = this.thisMob.getCarriedResourcesList().get(0);
            MobBase.MobType capturedMobDomesticatedType = capturedMobResource.getCaptured().getMobFlags().getDomesticateType();

            for (ObjectBase objectCheck : objects) {
               ArrayList<MobBase> allHunters = this.mob.getMobsByAssignment(Assignment.AssignmentType.CAPTURING_ANIMAL, false);
               if (this.canObjectStoreAnyAnimals(objectCheck, allHunters) && this.thisMob.canReach(objectCheck)) {
                  ObjectFlags objectFlags = objectCheck.getObjectFlags();
                  if (objectFlags.getOccupantsTypes().contains(capturedMobDomesticatedType)) {
                     deliveryOut = objectCheck;
                     break;
                  }
               }
            }

            if (deliveryOut == null) {
               this.switchNode(failBranch);
               return;
            }
         }

         OrderedPair randomOut = deliveryOut.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
         assignment.reset();
         assignment.setDeliveryObject(deliveryOut);
         assignment.setPrimaryCoordinates(randomOut);
         assignment.setAssignmentType(Assignment.AssignmentType.CAPTURING_ANIMAL);
         assignment.finalizeAssignment();
         this.requestPath(randomOut);
         this.aiStep = 1;
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("mobName", this.thisMob.getCarriedResourcesList().get(0).getCaptured().getFormattedName(false));
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getDeliveryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.captureAnimalsInHand.pathfindToBuilding"));
      this.runPath();
      ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
      if (deliveryObject == null || !this.object.objectExists(deliveryObject)) {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      } else if (this.pathCompleted) {
         this.aiStep = 2;
      } else if (this.pathFailed || !this.thisMob.canReach(this.goalCoordinates, true)) {
         if (deliveryObject != null && this.thisMob.canReach(deliveryObject, true)) {
            OrderedPair randomCoordinates = deliveryObject.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
            this.thisMob.getAssignment().setPrimaryCoordinates(randomCoordinates);
            this.requestPath(randomCoordinates);
         } else {
            this.thisMob.getAssignment().reset();
            this.aiStep = 0;
         }
      }
   }

   private void dropOffAnimal() throws SlickException {
      if (!this.thisMob.hasCarriedResources()) {
         this.switchNode(resetBranch);
      } else {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            ResourceBase capturedAnimalResource = this.thisMob.removeFirstCarriedResource();
            MobBase capturedAnimal = capturedAnimalResource.getCaptured();
            this.resource.deleteResource(capturedAnimalResource);
            float mobX = capturedAnimal.getMobX();
            float mobY = capturedAnimal.getMobY();
            this.font.newFloater("iconOwner", mobX + 4.0F, mobY);
            this.thisMob.awardXP(Utilities.randomInt(100) + 100);
            MobBase.MobType domesticateType = capturedAnimal.getMobFlags().getDomesticateType();
            if (this.thisMob.getMobFlags().getAllowGodExperience()) {
               this.god.increaseGodXP(0.3, GodModule.GodXPType.DOMESTICATION);
            }

            ObjectBase deliveryBuilding = this.thisMob.getAssignment().getDeliveryObject();
            if (deliveryBuilding.getOccupants().size() < deliveryBuilding.getObjectFlags().getOccupantsMax()) {
               if (this.object.getObjects().get(capturedAnimal.getAssignedHome()) != null) {
                  this.object.getObjects().get(capturedAnimal.getAssignedHome()).getOccupants().remove(capturedAnimal);
               }

               capturedAnimal.setAssignedHome(deliveryBuilding.getID());
               this.object.getObjects().get(deliveryBuilding.getID()).getOccupants().add(capturedAnimal);
            }

            this.mob.spawnMob(capturedAnimal, domesticateType, true);
            this.goal.incrementGoal(GoalModule.GoalType.ITS_HUNTIN_SEASON);
            this.stats.increaseAnimalDomesticatedThisPeriod(domesticateType);
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 3;
         }
      }
   }

   private void dropOffLift() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         this.switchNode(resetBranch);
      }
   }

   private boolean canObjectStoreAnyAnimals(ObjectBase o, ArrayList<MobBase> allHunters) {
      ObjectFlags objectFlags = o.getObjectFlags();
      if (objectFlags.getOccupantsMax() == 0) {
         return false;
      } else if (!this.thisMob.canReach(o, true)) {
         return false;
      } else {
         int availableSpace = objectFlags.getOccupantsMax() - o.getOccupants().size();
         if (availableSpace <= 0) {
            return false;
         } else {
            int totalEnroute = 0;

            for (MobBase m : allHunters) {
               if (m != this.thisMob && m.getAssignment().getDeliveryObject() == o) {
                  totalEnroute++;
               }
            }

            availableSpace -= totalEnroute;
            return availableSpace > 0;
         }
      }
   }
}
