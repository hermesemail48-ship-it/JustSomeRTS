package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIProcessTrash extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_WORK = 1;
   private static final int PROCESS_TRASH = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIProcessTrash(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_PROCESS_TRASH;
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
         failBranch = this.thisMob.getAIMedicWork();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToWork();
               break;
            case 2:
               this.processResource();
         }
      }
   }

   private void findWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canProcessTrash()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<ResourceModule.ResourceType> canProcessResources = new ArrayList<>();
            ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();

            for (ObjectBase objectCheck : objectList) {
               if (!objectCheck.isPaused() && objectCheck.getObjectFlags().isProcessor()) {
                  MobJobBase objectJob = objectCheck.getObjectFlags().getWorkerJobType().getJob();
                  if (this.thisMob.canReach(objectCheck, true)) {
                     ResourceModule.ResourceType[] coordinatesOut;
                     for (ResourceModule.ResourceType t : coordinatesOut = ResourceModule.ResourceType.values()) {
                        if (!canProcessResources.contains(t)
                           && objectJob.canProcessTrash()
                           && t.getTemplate().getTrashProcessResource() != null
                           && objectCheck.getObjectFlags().canBuildingStore(t)
                           && objectCheck.isResourceAvailable(t)
                           && (
                              !objectCheck.getObjectFlags().canBuildingBeBuilt(t)
                                 || objectCheck.getResourceCount(t)
                                    > objectCheck.getObjectFlags().getResourceValueBaseCount(t) - objectCheck.getResourceValueCount(t)
                           )) {
                           canProcessResources.add(t);
                        }
                     }
                  }
               }
            }

            if (canProcessResources.size() == 0) {
               this.switchNode(failBranch);
            } else {
               ArrayList<ObjectBase> validRefineObjects = new ArrayList<>();

               for (ObjectBase objectCheckx : objectList) {
                  if (!objectCheckx.isPaused() && objectCheckx.getObjectFlags().isProcessor()) {
                     MobJobBase objectJob = objectCheckx.getObjectFlags().getWorkerJobType().getJob();
                     if (this.thisMob.canReach(objectCheckx, true)) {
                        for (ResourceModule.ResourceType tx : canProcessResources) {
                           if (objectJob.canProcessTrash()
                              && objectCheckx.getObjectFlags().canBuildingStore(tx)
                              && !validRefineObjects.contains(objectCheckx)
                              && objectCheckx.isResourceAvailable(tx)
                              && (
                                 !objectCheckx.getObjectFlags().canBuildingBeBuilt(tx)
                                    || objectCheckx.getResourceCount(tx)
                                       > objectCheckx.getObjectFlags().getResourceValueBaseCount(tx) - objectCheckx.getResourceValueCount(tx)
                              )) {
                              validRefineObjects.add(objectCheckx);
                           }
                        }
                     }
                  }
               }

               if (validRefineObjects.size() == 0) {
                  this.switchNode(failBranch);
               } else {
                  ObjectBase processOut = null;
                  OrderedPair refineCoordinates = null;
                  ResourceModule.ResourceType resourceTypeInBuilding = null;
                  ResourceModule.ResourceType resourceTypeToCreate = null;
                  ArrayList<ResourceModule.ResourceType> canRefineResourcesSorted = new ArrayList<>();
                  canRefineResourcesSorted.addAll(canProcessResources);
                  Collections.shuffle(canRefineResourcesSorted);

                  for (ResourceModule.ResourceType txx : canRefineResourcesSorted) {
                     processOut = null;
                     refineCoordinates = null;
                     ArrayList<ObjectBase> validRefineObjectsFiltered = new ArrayList<>();

                     for (ObjectBase o : validRefineObjects) {
                        if (!o.isPaused() && o.getObjectFlags().canBuildingStore(txx) && o.isResourceAvailable(txx)) {
                           validRefineObjectsFiltered.add(o);
                        }
                     }

                     if (validRefineObjectsFiltered.size() != 0) {
                        int tileX = this.thisMob.getTileX();
                        int tileY = this.thisMob.getTileY();
                        refineCoordinates = this.pathFinder
                           .searchForObject(tileX, tileY, validRefineObjectsFiltered, false, true, this.thisMobFlags.getBlockMapGroup());
                        if (refineCoordinates != null) {
                           resourceTypeInBuilding = txx;
                           resourceTypeToCreate = txx.getTemplate().getTrashProcessResource();
                           processOut = this.object.getBuildingAtLocation(refineCoordinates.getX(), refineCoordinates.getY());
                           break;
                        }
                     }
                  }

                  if (processOut == null) {
                     this.switchNode(failBranch);
                  } else {
                     OrderedPair coordinatesOut = processOut.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
                     Assignment assignment = this.thisMob.getAssignment();
                     assignment.reset();
                     assignment.setDeliveryObject(processOut);
                     assignment.setPrimaryCoordinates(coordinatesOut);
                     assignment.addPickupResources(processOut.getAvailableResource(resourceTypeInBuilding));
                     assignment.setPickupResourceType(resourceTypeToCreate);
                     assignment.setAssignmentType(Assignment.AssignmentType.PROCESS_RESOURCE);
                     assignment.finalizeAssignment();
                     this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
                     this.aiStep = 1;
                  }
               }
            }
         }
      }
   }

   private void pathfindToWork() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getDeliveryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.processTrash.pathfindToWork"));
      this.runPath();
      if (this.pathFailed) {
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void processResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.processTrash.processTrash"));
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(resetBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         float mobX = this.thisMob.getMobX();
         float mobY = this.thisMob.getMobY();
         if (assignment.getPrimaryX() == tileX && assignment.getPrimaryY() == tileY) {
            boolean usingTools = this.hasPreferredTools();
            this.thisMob.setPositionInteract(usingTools, true);
            this.thisMob.runWorkDelayTick(1);
            this.thisMob.doWork(1, 1, 1, 1, 0);
            if (this.thisMob.getWorkDelayTick() % 35 == 0) {
               for (int l = 0; l < 12; l++) {
                  ResourceModule.ResourceType resourceType = this.thisMob.getAssignment().getFirstPickupResources().getType();
                  if (resourceType != null) {
                     if (resourceType == ResourceModule.ResourceType.WOOD) {
                        this.sound.playSound(SoundModule.SoundType.WOOD_CHOP, mobX, mobY);
                     } else if (resourceType == ResourceModule.ResourceType.ROCK) {
                        this.sound.playSound(SoundModule.SoundType.MINING, mobX, mobY);
                     }
                     break;
                  }
               }

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

               ObjectBase processBuilding = this.thisMob.getAssignment().getDeliveryObject();
               ResourceBase baseResource = this.thisMob.getAssignment().removeAndGetFirstPickupResources();
               this.thisMob.awardXP(Utilities.randomInt(5) + 5);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.PROCESSING_RESOURCES);
               }

               processBuilding.consumeStores(baseResource);
               if (Utilities.randomInt(3) == 0) {
                  ResourceBase processedResource = this.resource
                     .createRefinedResource(baseResource, this.thisMob.getAssignment().getPickupResourceType(), true, 40);
                  this.thisMob.addCarriedResource(processedResource, true);
               }

               if (Utilities.randomInt(2) == 0) {
                  if (processBuilding.canAcceptRefineOrProcessResource(ResourceModule.ResourceType.TRASHY_TRASH, -1)) {
                     ResourceBase trashyTrash = this.resource
                        .createResourceInBuilding(ResourceModule.ResourceType.TRASHY_TRASH, baseResource.getColor(), processBuilding);
                     processBuilding.increaseStores(trashyTrash);
                  } else {
                     OrderedPair o = this.resource.findOpenSpace(this.thisMob.getTileX(), this.thisMob.getTileY(), MapData.BlockMapGroup.STANDARD);
                     if (o != null) {
                        this.resource.createResourceOnGround(ResourceModule.ResourceType.TRASHY_TRASH, baseResource.getColor(), o.getX(), o.getY());
                     }
                  }
               }

               this.switchNode(resetBranch);
            }
         } else {
            this.switchNode(resetBranch);
         }
      }
   }
}
