package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIRefineWaterBottle extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_WORK = 1;
   private static final int REFINE_RESOURCE = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIRefineWaterBottle(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_REFINE_WATER;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowWork() && this.thisMob.getMobJobType() != MobJobBase.MobJobType.NONE;
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
         failBranch = this.thisMob.getAIDeliverConstructionResource();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToWork();
               break;
            case 2:
               this.refineResource();
         }
      }
   }

   private void findWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canWorkerRefine(ResourceModule.ResourceType.WATER_BOTTLE)) {
            this.switchNode(failBranch);
         } else {
            ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
            ArrayList<ObjectBase> validRefineObjects = new ArrayList<>();
            ResourceModule.ResourceType refineType = ResourceModule.ResourceType.WATER_BOTTLE;
            if (this.resource.getResourceMaintainAmount(refineType) <= 0 && this.resource.getResourceMakeAmount(refineType) <= 0) {
               this.switchNode(failBranch);
            } else if (this.enoughWorkersRefining(refineType)) {
               this.switchNode(failBranch);
            } else {
               for (ObjectBase objectCheck : objectList) {
                  if (!objectCheck.isPaused()) {
                     MobJobBase objectJob = objectCheck.getObjectFlags().getWorkerJobType().getJob();
                     if (objectJob.canWorkerRefine(refineType)
                        && (
                           this.resource.getResourceMakeAmount(refineType) == ResourceModule.RESOURCE_INFINITE_AMOUNT
                              || this.resource.getResourceMaintainAmount(refineType) == ResourceModule.RESOURCE_INFINITE_AMOUNT
                              || objectCheck.getAcceptStorageResourceCount(refineType, this.thisMob.getID()) > 0
                        )
                        && this.thisMob.canReach(objectCheck, true)
                        && objectCheck.getTotalWater() >= refineType.getTemplate().getUsesMax()) {
                        int workerCount = this.getWorkerCount(objectCheck) * refineType.getTemplate().getUsesMax();
                        if (objectCheck.getTotalWater() - workerCount >= refineType.getTemplate().getUsesMax()) {
                           validRefineObjects.add(objectCheck);
                        }
                     }
                  }
               }

               if (validRefineObjects.size() == 0) {
                  this.switchNode(failBranch);
               } else {
                  OrderedPair o = this.pathFinder
                     .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), validRefineObjects, false, true, this.thisMobFlags.getBlockMapGroup());
                  ObjectBase refineOut = this.object.getBuildingAtLocation(o);
                  OrderedPair coordinatesOut = refineOut.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
                  Assignment assignment = this.thisMob.getAssignment();
                  assignment.reset();
                  assignment.setDeliveryObject(refineOut);
                  assignment.setPrimaryCoordinates(coordinatesOut);
                  assignment.setPickupResourceType(refineType);
                  assignment.setAssignmentType(Assignment.AssignmentType.REFINE_WATER);
                  assignment.finalizeAssignment();
                  this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
                  this.aiStep++;
               }
            }
         }
      }
   }

   private void pathfindToWork() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getDeliveryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.refineWaterBottle.pathfindToWork"));
      this.runPath();
      if (this.pathFailed) {
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            this.aiStep++;
         }
      }
   }

   private void refineResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.refineWaterBottle.refineResource"));
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(resetBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         if (assignment.getPrimaryX() == tileX && assignment.getPrimaryY() == tileY) {
            ObjectBase refineBuilding = this.thisMob.getAssignment().getDeliveryObject();
            if (!this.object.objectExists(refineBuilding)) {
               this.switchNode(failBranch);
            } else if (this.enoughWorkersRefining(this.thisMob.getAssignment().getPickupResourceType())) {
               this.switchNode(failBranch);
            } else {
               boolean usingTools = this.hasPreferredTools();
               this.thisMob.setPositionInteract(usingTools, false);
               this.thisMob.runWorkDelayTick(1);
               this.thisMob.doWork(1, 1, 1, 1, 0);
               if (this.thisMob.getWorkDelayTick() % 35 == 0) {
                  int attackX = assignment.getPrimaryX();
                  int attackY = assignment.getPrimaryY();
                  double xDiff = this.thisMob.getCenterPixelX(true) - attackX * 16 - 8;
                  double yDiff = this.thisMob.getCenterPixelY(true) - attackY * 16 - 8;
                  float angle = (float)Math.toDegrees(Math.atan2(yDiff, xDiff));
                  int randomParticles = Utilities.randomInt(2) + 1;

                  for (int x = 0; x < randomParticles; x++) {
                     this.particle
                        .newParticleGroup(
                           attackX * 16 + Utilities.randomInt(16),
                           attackY * 16 + Utilities.randomInt(16),
                           angle,
                           ParticleModule.ParticleGroup.DUST,
                           ParticleModule.ParticleSet.BLACK
                        );
                     this.particle
                        .newParticleGroup(
                           attackX * 16 + Utilities.randomInt(8) + 2,
                           (attackY - 1) * 16 + Utilities.randomInt(8) + 2,
                           ParticleModule.ParticleGroup.SMOKE_BELLOW,
                           ParticleModule.ParticleSet.GRAY
                        );
                  }
               }

               if (this.thisMob.getWorkDelayTick() > this.mobJob.getCalculatedWorkTime(this.thisMob)) {
                  if (usingTools) {
                     this.thisMob.getInventoryToolSlot().decreaseUses(1);
                  }

                  this.thisMob.awardXP(Utilities.randomInt(5) + 5);
                  ResourceBase refinedResource = this.resource.createRefinedResource(this.thisMob.getAssignment().getPickupResourceType(), true, 40);
                  if (this.thisMobFlags.getAllowGodExperience()) {
                     this.god.increaseGodXP(0.01, GodModule.GodXPType.REDISTRIBUTING_RESOURCES);
                  }

                  if (this.resource.getResourceMakeAmount(refinedResource.getType()) != ResourceModule.RESOURCE_INFINITE_AMOUNT) {
                     this.resource.decreaseResourceMakeAmount(refinedResource.getType(), 1);
                  }

                  refineBuilding.decreaseWater(this.thisMob.getAssignment().getPickupResourceType().getTemplate().getUsesMax());
                  if (refineBuilding.getObjectFlags().canBuildingStore(refinedResource.getType())
                     && refineBuilding.canAcceptRefineOrProcessResource(refinedResource.getType(), this.thisMob.getID())) {
                     refinedResource.placeInBuilding(refineBuilding);
                     refineBuilding.increaseStores(refinedResource);
                  } else {
                     this.thisMob.addCarriedResource(refinedResource, true);
                  }

                  this.switchNode(resetBranch);
               }
            }
         } else {
            this.switchNode(resetBranch);
         }
      }
   }

   private boolean enoughWorkersRefining(ResourceModule.ResourceType resourceType) {
      if (this.resource.getResourceMakeAmount(resourceType) != ResourceModule.RESOURCE_INFINITE_AMOUNT
         && this.resource.getResourceMaintainAmount(resourceType) != ResourceModule.RESOURCE_INFINITE_AMOUNT) {
         int refineCount = 0;

         for (MobBase m : this.mob.getMobsByAssignment(Assignment.AssignmentType.REFINE_RESOURCE, false)) {
            if (m.getAssignment() != null && m != this.thisMob) {
               Assignment assignment = m.getAssignment();
               ResourceModule.ResourceType assignmentResourceType = assignment.getPickupResourceType();
               if (assignmentResourceType == resourceType) {
                  refineCount++;
               }
            }
         }

         if (this.resource.getResourceMakeAmount(resourceType) == ResourceModule.RESOURCE_STORAGE_AMOUNT
            || this.resource.getResourceMaintainAmount(resourceType) == ResourceModule.RESOURCE_STORAGE_AMOUNT) {
            int storageSpace = this.resource.getResourceStoresSpace(resourceType);
            int resourcesOnGround = this.resource.getResourceGround(resourceType, true);
            int resourcesCarried = this.resource.getResourceCarried(resourceType);
            return storageSpace - resourcesOnGround - refineCount - resourcesCarried <= 0;
         } else {
            return this.resource.getResourceMakeAmount(resourceType) - refineCount <= 0
               && this.resource.getResourceMaintainAmount(resourceType) - this.resource.getResourceTotal(resourceType, true) - refineCount <= 0;
         }
      } else {
         return false;
      }
   }

   private int getWorkerCount(ObjectBase o) {
      int delivererCount = 0;

      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
         if (m.getAssignment() != null && m.getID() != this.thisMob.getID()) {
            Assignment assignment = m.getAssignment();
            if (assignment.getAssignmentType() == Assignment.AssignmentType.REFINE_WATER && assignment.getDeliveryObject() == o) {
               delivererCount++;
            }
         }
      }

      return delivererCount;
   }
}
