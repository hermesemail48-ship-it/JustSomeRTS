package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AISlaughterAnimalInHand extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_BUILDING = 1;
   private static final int DROP_OFF_ANIMAL = 2;
   private static final int SLAUGHTER_ANIMAL = 3;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AISlaughterAnimalInHand(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_SLAUGHTER_ANIMAL_IN_HAND;
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
         failBranch = this.thisMob.getAIPlantResourceInHand();
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
               this.slaughterAnimal();
         }
      }
   }

   private void findWork() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      ObjectBase deliveryOut = assignment.getDeliveryObject();
      ObjectBase originalPen = assignment.getPrimaryObject();
      ResourceModule.ResourceType targetAnimalResourceType = assignment.getPickupResourceType();
      MobBase capturedMob = this.thisMob.getCarriedResourcesList().get(0).getCaptured();
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else if (this.thisMob.getMobJobType().getJob().canCaptureAnimals()) {
         this.switchNode(failBranch);
      } else if (!this.thisMob.getCarriedResourcesList().get(0).hasCaptured()) {
         this.switchNode(failBranch);
      } else if (!capturedMob.getMobFlags().canDropsAllowSlaughter()) {
         this.switchNode(failBranch);
      } else {
         if (deliveryOut == null) {
            ArrayList<ObjectBase> objects = this.object.getObjectMasterBuildingPriorityList();
            MobJobBase mobJob = this.thisMob.getMobJobType().getJob();
            ArrayList<ResourceModule.ResourceType> slaughterTypes = mobJob.getCanWorkerSlaughter();
            ArrayList<ResourceModule.ResourceType> validSlaughterTypes = new ArrayList<>();

            for (ResourceModule.ResourceType t : slaughterTypes) {
               if (!this.enoughWorkersSlaughtering(t) && capturedMob.getMobFlags().getDropsList().keySet().contains(t)) {
                  validSlaughterTypes.add(t);
               }
            }

            if (validSlaughterTypes.size() == 0) {
               this.switchNode(failBranch);
               return;
            }

            originalPen = this.object.getObjects().get(capturedMob.getAssignedHome());
            if (originalPen == null || originalPen.isDestroyed()) {
               this.switchNode(failBranch);
               return;
            }

            ObjectFlags objectFlags = originalPen.getObjectFlags();
            if (objectFlags.getOccupantsMax() == 0) {
               this.switchNode(failBranch);
               return;
            }

            ArrayList<MobBase> occupants = originalPen.getOccupants();
            if (occupants.size() <= objectFlags.getOccupantsMax() * 0.75) {
               this.switchNode(failBranch);
               return;
            }

            ArrayList<MobBase> assignedAnimals = new ArrayList<>();
            ArrayList<MobBase> allButchers = this.mob.getMobsByAssignment(Assignment.AssignmentType.SLAUGHTERING_ANIMAL, false);
            int enroute = 0;

            for (MobBase m : allButchers) {
               if (m != this.thisMob) {
                  if (m.getAssignment().getPrimaryObject() == originalPen) {
                     enroute++;
                  }

                  assignedAnimals.add(m.getAssignment().getPrimaryMob());
               }
            }

            if (occupants.size() - enroute <= objectFlags.getOccupantsMax() * 0.75) {
               this.switchNode(failBranch);
               return;
            }

            ArrayList<ObjectBase> validWorkBuildings = new ArrayList<>();

            for (ObjectBase o : objects) {
               if (this.thisMob.canReach(o, true)) {
                  ObjectFlags workObjectFlags = o.getObjectFlags();
                  if (workObjectFlags.getWorkerJobType() == this.thisMob.getMobJobType()) {
                     validWorkBuildings.add(o);
                  }
               }
            }

            if (validWorkBuildings.size() == 0) {
               this.switchNode(failBranch);
               return;
            }

            Collections.shuffle(validSlaughterTypes);

            for (ResourceModule.ResourceType tx : validSlaughterTypes) {
               if (capturedMob.getMobFlags().getDropsList().containsKey(tx)) {
                  targetAnimalResourceType = tx;
                  break;
               }
            }

            OrderedPair deliveryObjectCoordinates = this.pathFinder
               .searchForObject(
                  this.thisMob.getTileX(), this.thisMob.getTileY(), validWorkBuildings, false, true, this.thisMob.getMobFlags().getBlockMapGroup()
               );
            deliveryOut = this.object.getBuildingAtLocation(deliveryObjectCoordinates);
         }

         OrderedPair randomOut = deliveryOut.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
         assignment = this.thisMob.getAssignment();
         assignment.reset();
         assignment.setPrimaryMob(capturedMob);
         assignment.setPickupResourceType(targetAnimalResourceType);
         assignment.setPrimaryObject(originalPen);
         assignment.setDeliveryObject(deliveryOut);
         assignment.setPrimaryCoordinates(randomOut);
         assignment.setAssignmentType(Assignment.AssignmentType.SLAUGHTERING_ANIMAL);
         assignment.finalizeAssignment();
         this.requestPath(randomOut);
         this.aiStep = 1;
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("mobName", this.thisMob.getAssignment().getPrimaryMobName());
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getDeliveryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.slaughterAnimalInHand.pathfindToBuilding"));
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
      if (this.thisMob.hasCarriedResources()) {
         ResourceBase capturedAnimalResource = this.thisMob.removeFirstCarriedResource();
         MobBase capturedAnimal = this.thisMob.getAssignment().getPrimaryMob();
         this.resource.deleteResource(capturedAnimalResource);
         capturedAnimal.interruptNode(capturedAnimal.getAIBeingSlaughtered());
         this.aiStep = 3;
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void slaughterAnimal() throws SlickException {
      MobBase capturedAnimal = this.thisMob.getAssignment().getPrimaryMob();
      ObjectBase slaughterBuilding = this.thisMob.getAssignment().getDeliveryObject();
      MobFlagsBase capturedAnimalFlags = capturedAnimal.getMobFlags();
      if (capturedAnimal.getActiveNode() != capturedAnimal.getAIBeingSlaughtered()) {
         this.switchNode(resetBranch);
      } else {
         Text.setVariableText("mobName", capturedAnimal.getFormattedName(false));
         this.thisMob.setCurrentTask(Text.getText("aiTask.slaughterAnimalInHand.slaughterAnimal"));
         if (Utilities.randomInt(50) == 0) {
            capturedAnimal.takeDamage(
               (int)(capturedAnimal.getHitPointsMax() * 0.1),
               this.thisMob.getMobX(),
               this.thisMob.getMobY(),
               this.thisMob,
               MobBase.DamageType.REGULAR,
               MobBase.DamageCause.BEING_SLAUGHTERED,
               true
            );
         }

         this.thisMob.setPositionAttack();
         int capturedAnimalX = capturedAnimal.getTileX();
         int capturedAnimalY = capturedAnimal.getTileY();
         if (capturedAnimal.getHitPoints() < capturedAnimal.getHitPointsMax() * 0.2) {
            capturedAnimal.setDropsBlocked(true);
            capturedAnimal.setHitPoints(0);
            int totalDrops = capturedAnimalFlags.getDropsAttempts();
            ResourceModule.ResourceType desiredResourceType = this.thisMob.getAssignment().getPickupResourceType();

            for (int x = 0; x < totalDrops; x++) {
               if (x == totalDrops - 1) {
                  HashMap<ResourceModule.ResourceType, Integer> dropMap = capturedAnimalFlags.getDropsList();
                  ArrayList<ResourceModule.ResourceType> dropList = new ArrayList<>();

                  for (ResourceModule.ResourceType t : dropMap.keySet()) {
                     dropList.add(t);
                  }

                  Collections.shuffle(dropList);
                  desiredResourceType = dropList.get(0);
               }

               if (this.resource.getResourceMakeAmount(desiredResourceType) != ResourceModule.RESOURCE_INFINITE_AMOUNT) {
                  this.resource.decreaseResourceMakeAmount(desiredResourceType, 1);
               }

               if (this.object.objectExists(slaughterBuilding) && slaughterBuilding.getAcceptStorageResourceCount(desiredResourceType, -1) > 0) {
                  ResourceBase r = this.resource.createResourceUnassigned(desiredResourceType);
                  r.placeInBuilding(slaughterBuilding);
                  slaughterBuilding.increaseStores(r);
               } else if (!this.resource.isResourcesOnGround(capturedAnimalX, capturedAnimalY)) {
                  this.resource.createResourceOnGround(desiredResourceType, ResourceModule.ResourceColorSet.DEFAULT, capturedAnimalX, capturedAnimalY);
               } else {
                  OrderedPair resourceCoordinates = this.resource.findOpenSpace(capturedAnimalX, capturedAnimalY, MapData.BlockMapGroup.STANDARD);
                  this.resource
                     .createResourceOnGround(
                        desiredResourceType, ResourceModule.ResourceColorSet.DEFAULT, resourceCoordinates.getX(), resourceCoordinates.getY()
                     );
               }
            }

            this.thisMob.awardXP(Utilities.randomInt(10) + 10);
            this.goal.incrementGoal(GoalModule.GoalType.FACTORY_FARMING);
            if (this.thisMob.getMobFlags().getAllowGodExperience()) {
               this.god.increaseGodXP(0.3, GodModule.GodXPType.SLAUGHTERING_ANIMALS);
            }

            this.switchNode(resetBranch);
         }
      }
   }

   private boolean enoughWorkersSlaughtering(ResourceModule.ResourceType resourceType) {
      if (this.resource.getResourceMakeAmount(resourceType) != ResourceModule.RESOURCE_INFINITE_AMOUNT
         && this.resource.getResourceMaintainAmount(resourceType) != ResourceModule.RESOURCE_INFINITE_AMOUNT) {
         int slaughterCount = 0;

         for (MobBase m : this.mob.getMobsByAssignment(Assignment.AssignmentType.SLAUGHTERING_ANIMAL, false)) {
            if (m.getAssignment() != null && m != this.thisMob) {
               Assignment assignment = m.getAssignment();
               ResourceModule.ResourceType assignmentResourceType = assignment.getPickupResourceType();
               if (assignmentResourceType == resourceType) {
                  slaughterCount += assignment.getPrimaryMob().getMobFlags().getDropsAttempts() - 1;
               }
            }
         }

         if (this.resource.getResourceMakeAmount(resourceType) == ResourceModule.RESOURCE_STORAGE_AMOUNT
            || this.resource.getResourceMaintainAmount(resourceType) == ResourceModule.RESOURCE_STORAGE_AMOUNT) {
            int storageSpace = this.resource.getResourceStoresSpace(resourceType);
            int resourcesOnGround = this.resource.getResourceGround(resourceType, true);
            int resourcesCarried = this.resource.getResourceCarried(resourceType);
            return storageSpace - resourcesOnGround - slaughterCount - resourcesCarried <= 0;
         } else {
            return this.resource.getResourceMakeAmount(resourceType) - slaughterCount <= 0
               && this.resource.getResourceMaintainAmount(resourceType) - this.resource.getResourceTotal(resourceType, true) - slaughterCount <= 0;
         }
      } else {
         return false;
      }
   }
}
