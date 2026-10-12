package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIRefineResource extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_WORK = 1;
   private static final int REFINE_RESOURCE = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIRefineResource(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_REFINE_RESOURCE;
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
         failBranch = this.thisMob.getAIProcessTrash();
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
         if (!job.canWorkerRefine()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<ResourceModule.ResourceType> canRefineResources = new ArrayList<>();
            ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();

            for (ObjectBase objectCheck : objectList) {
               if (!objectCheck.isPaused()) {
                  MobJobBase objectJob = objectCheck.getObjectFlags().getWorkerJobType().getJob();
                  if (this.thisMob.canReach(objectCheck, true)) {
                     label258:
                     for (ResourceModule.ResourceType t : job.getCanWorkerRefine()) {
                        if (!canRefineResources.contains(t)
                           && objectJob.canWorkerRefine(t)
                           && objectCheck.getObjectFlags().canBuildingStore(t)
                           && (this.resource.getResourceMaintainAmount(t) > 0 || this.resource.getResourceMakeAmount(t) > 0)
                           && !this.enoughWorkersRefining(t)
                           && t.getTemplate().getBaseResources().size() != 0) {
                           for (ResourceModule.ResourceType t2 : t.getTemplate().getBaseResources()) {
                              if (!objectCheck.isResourceAvailable(t2)
                                 || objectCheck.getObjectFlags().canBuildingBeBuilt(t2)
                                    && objectCheck.getResourceCount(t2)
                                       <= objectCheck.getObjectFlags().getResourceValueBaseCount(t2) - objectCheck.getResourceValueCount(t2)) {
                                 continue label258;
                              }
                           }

                           if (this.resource.getResourceMakeAmount(t) == ResourceModule.RESOURCE_INFINITE_AMOUNT
                              || this.resource.getResourceMaintainAmount(t) == ResourceModule.RESOURCE_INFINITE_AMOUNT
                              || objectCheck.canAcceptRefineOrProcessResource(t, this.thisMob.getID())) {
                              canRefineResources.add(t);
                           }
                        }
                     }
                  }
               }
            }

            if (canRefineResources.size() == 0) {
               this.switchNode(failBranch);
            } else {
               ArrayList<ObjectBase> validRefineObjects = new ArrayList<>();

               for (ObjectBase objectCheckx : objectList) {
                  if (!objectCheckx.isPaused()) {
                     MobJobBase objectJob = objectCheckx.getObjectFlags().getWorkerJobType().getJob();
                     if (this.thisMob.canReach(objectCheckx, true)) {
                        label199:
                        for (ResourceModule.ResourceType tx : canRefineResources) {
                           if (objectJob.canWorkerRefine(tx)
                              && objectCheckx.getObjectFlags().canBuildingStore(tx)
                              && (this.resource.getResourceMaintainAmount(tx) > 0 || this.resource.getResourceMakeAmount(tx) > 0)
                              && (
                                 this.resource.getResourceMakeAmount(tx) == ResourceModule.RESOURCE_INFINITE_AMOUNT
                                    || this.resource.getResourceMaintainAmount(tx) == ResourceModule.RESOURCE_INFINITE_AMOUNT
                                    || objectCheckx.canAcceptRefineOrProcessResource(tx, this.thisMob.getID())
                              )
                              && !validRefineObjects.contains(objectCheckx)) {
                              for (ResourceModule.ResourceType t2x : tx.getTemplate().getBaseResources()) {
                                 if (!objectCheckx.isResourceAvailable(t2x)
                                    || objectCheckx.getObjectFlags().canBuildingBeBuilt(t2x)
                                       && objectCheckx.getResourceCount(t2x)
                                          <= objectCheckx.getObjectFlags().getResourceValueBaseCount(t2x) - objectCheckx.getResourceValueCount(t2x)) {
                                    continue label199;
                                 }
                              }

                              validRefineObjects.add(objectCheckx);
                           }
                        }
                     }
                  }
               }

               if (validRefineObjects.size() == 0) {
                  this.switchNode(failBranch);
               } else {
                  ObjectBase refineOut = null;
                  OrderedPair refineCoordinates = null;
                  ArrayList<ResourceModule.ResourceType> resourceTypeInBuilding = null;
                  ResourceModule.ResourceType resourceTypeToCreate = null;
                  ArrayList<ResourceModule.ResourceType> canRefineResourcesSorted = new ArrayList<>();
                  canRefineResourcesSorted.addAll(canRefineResources);
                  Collections.shuffle(canRefineResourcesSorted);

                  for (ResourceModule.ResourceType txx : canRefineResourcesSorted) {
                     refineOut = null;
                     refineCoordinates = null;
                     ArrayList<ObjectBase> validRefineObjectsFiltered = new ArrayList<>();

                     label144:
                     for (ObjectBase o : validRefineObjects) {
                        if ((
                              this.resource.getResourceMakeAmount(txx) == ResourceModule.RESOURCE_INFINITE_AMOUNT
                                 || this.resource.getResourceMaintainAmount(txx) == ResourceModule.RESOURCE_INFINITE_AMOUNT
                                 || o.canAcceptRefineOrProcessResource(txx, this.thisMob.getID())
                           )
                           && o.getObjectFlags().canBuildingStore(txx)) {
                           for (ResourceModule.ResourceType t2xx : txx.getTemplate().getBaseResources()) {
                              if (!o.isResourceAvailable(t2xx)) {
                                 continue label144;
                              }
                           }

                           validRefineObjectsFiltered.add(o);
                        }
                     }

                     if (validRefineObjectsFiltered.size() != 0) {
                        int tileX = this.thisMob.getTileX();
                        int tileY = this.thisMob.getTileY();
                        refineCoordinates = this.pathFinder
                           .searchForObject(tileX, tileY, validRefineObjectsFiltered, false, true, this.thisMobFlags.getBlockMapGroup());
                        if (refineCoordinates != null) {
                           resourceTypeInBuilding = txx.getTemplate().getBaseResources();
                           resourceTypeToCreate = txx;
                           refineOut = this.object.getBuildingAtLocation(refineCoordinates.getX(), refineCoordinates.getY());
                           break;
                        }
                     }
                  }

                  if (refineOut == null) {
                     this.switchNode(failBranch);
                  } else {
                     OrderedPair coordinatesOut = refineOut.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
                     Assignment assignment = this.thisMob.getAssignment();
                     assignment.reset();
                     assignment.setDeliveryObject(refineOut);
                     assignment.setPrimaryCoordinates(coordinatesOut);

                     for (ResourceModule.ResourceType txx : resourceTypeInBuilding) {
                        assignment.addPickupResources(refineOut.getAvailableResource(txx));
                     }

                     assignment.setPickupResourceType(resourceTypeToCreate);
                     assignment.setAssignmentType(Assignment.AssignmentType.REFINE_RESOURCE);
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
      this.thisMob.setCurrentTask(Text.getText("aiTask.refineResource.pathfindToWork"));
      this.runPath();
      if (this.pathFailed) {
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void refineResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.refineResource.refineResource"));
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
            ObjectBase refineBuilding = this.thisMob.getAssignment().getDeliveryObject();
            if (!this.object.objectExists(refineBuilding)) {
               this.switchNode(failBranch);
            } else if (this.enoughWorkersRefining(this.thisMob.getAssignment().getPickupResourceType())) {
               this.switchNode(resetBranch);
            } else {
               boolean usingTools = this.hasPreferredTools();
               this.thisMob.setPositionInteract(usingTools, false);
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

                  ResourceBase firstBaseResource = this.thisMob.getAssignment().getFirstPickupResources();
                  ResourceBase refinedResource = this.resource
                     .createRefinedResource(firstBaseResource, this.thisMob.getAssignment().getPickupResourceType(), true, 40);
                  this.thisMob.awardXP(Utilities.randomInt(5) + 5);
                  if (this.thisMobFlags.getAllowGodExperience()) {
                     this.god.increaseGodXP(0.01, GodModule.GodXPType.REFINING_RESOURCES);
                  }

                  for (ResourceBase t : this.thisMob.getAssignment().getPickupResources()) {
                     refineBuilding.consumeStores(t);
                  }

                  if (this.resource.getResourceMakeAmount(refinedResource.getType()) != ResourceModule.RESOURCE_INFINITE_AMOUNT) {
                     this.resource.decreaseResourceMakeAmount(refinedResource.getType(), 1);
                  }

                  if (refinedResource.getType() == ResourceModule.ResourceType.GOLD_COIN_SACK) {
                     this.goal.incrementGoal(GoalModule.GoalType.RISE_TO_RICHES, refinedResource.getUses());
                  }

                  ResourceModule.ResourceSlot slot = refinedResource.getType().getTemplate().getResourceSlot();
                  if (slot == ResourceModule.ResourceSlot.TOOL) {
                     this.goal.incrementGoal(GoalModule.GoalType.TOOLS_FOR_ALL);
                  }

                  if (slot == ResourceModule.ResourceSlot.CHEST || slot == ResourceModule.ResourceSlot.SHIELD || slot == ResourceModule.ResourceSlot.HEAD) {
                     this.goal.incrementGoal(GoalModule.GoalType.WELL_PROTECTED);
                  }

                  if (refinedResource.getType() == ResourceModule.ResourceType.COOKED_MEAT) {
                     this.goal.incrementGoal(GoalModule.GoalType.CLUCK_NUGGETS);
                  }

                  if (refinedResource.getType() == ResourceModule.ResourceType.BOARD || refinedResource.getType() == ResourceModule.ResourceType.CUT_STONE) {
                     this.goal.incrementGoal(GoalModule.GoalType.INDUSTRY);
                  }

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
         int refineAnyCount = 0;

         for (MobBase m : this.mob.getMobsByAssignment(Assignment.AssignmentType.REFINE_RESOURCE, false)) {
            if (m.getAssignment() != null && m != this.thisMob) {
               Assignment assignment = m.getAssignment();
               ResourceModule.ResourceType assignmentResourceType = assignment.getPickupResourceType();
               if (assignmentResourceType == resourceType) {
                  refineCount++;
               }

               refineAnyCount++;
            }
         }

         MobJobBase mobJob = this.thisMob.getMobJobType().getJob();
         if (mobJob.canHalfRefineWork()
            && refineAnyCount != 0
            && this.mob.getMobsByJob(this.thisMob.getMobJobType(), false).size() > 1
            && this.mob.getMobsByJob(this.thisMob.getMobJobType(), false).size() / 2 <= refineCount) {
            return true;
         } else if (this.resource.getResourceMakeAmount(resourceType) == ResourceModule.RESOURCE_STORAGE_AMOUNT
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
}
