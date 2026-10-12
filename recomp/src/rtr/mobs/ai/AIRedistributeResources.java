package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIRedistributeResources extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_BUILDING = 1;
   private static final int PICK_UP_RESOURCE = 2;
   private static final int LIFT_RESOURCE = 3;
   private static final int SET_PATH_TO_DELIVERY = 4;
   private static final int PATHFIND_TO_DELIVERY = 5;
   private static final int DROP_OFF_RESOURCE = 6;
   private static final int DROP_OFF_LIFT = 7;
   private static final int PATHFIND_TO_EXTRA_RESOURCE = 8;
   private static final int PICKUP_EXTRA_RESOURCE = 9;
   private static final int LIFT_EXTRA_RESOURCE = 10;
   private static AIBase resetBranch;
   private static AIBase failBranch;
   private static AIBase failBranchWithResource;

   public AIRedistributeResources(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_REDISTRIBUTE_RESOURCES;
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
         failBranch = this.thisMob.getAISlaughterAnimal();
         failBranchWithResource = this.thisMob.getAICaptureAnimalsInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToBuilding();
               break;
            case 2:
               this.pickupResource();
               break;
            case 3:
               this.liftResource();
               break;
            case 4:
               this.setPathToDelivery();
               break;
            case 5:
               this.pathfindToDelivery();
               break;
            case 6:
               this.dropOffResource();
               break;
            case 7:
               this.dropOffLift();
               break;
            case 8:
               this.pathfindToExtraResource();
               break;
            case 9:
               this.pickupExtraResource();
               break;
            case 10:
               this.liftExtraResource();
         }
      }
   }

   private void findWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canWorkerRedistribute()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
            EnumMap<ObjectBase.ResourceStorePriority, ArrayList<ObjectBase>> validSpace = new EnumMap<>(ObjectBase.ResourceStorePriority.class);
            EnumMap<ObjectBase.ResourceStorePriority, ArrayList<ObjectBase>> validStores = new EnumMap<>(ObjectBase.ResourceStorePriority.class);

            ObjectBase.ResourceStorePriority[] foundOne;
            for (ObjectBase.ResourceStorePriority p : foundOne = ObjectBase.ResourceStorePriority.values()) {
               validSpace.put(p, new ArrayList<>());
               validStores.put(p, new ArrayList<>());
            }

            ArrayList<ResourceModule.ResourceType> shuffledRedistributeList = new ArrayList<>();
            shuffledRedistributeList.addAll(job.getCanWorkerRedistribute());
            Collections.shuffle(shuffledRedistributeList);

            for (ResourceModule.ResourceType t : shuffledRedistributeList) {
               ObjectBase.ResourceStorePriority[] p;
               for (ObjectBase.ResourceStorePriority px : p = ObjectBase.ResourceStorePriority.values()) {
                  validSpace.get(px).clear();
                  validStores.get(px).clear();
               }

               boolean foundOnex = false;

               for (ObjectBase objectCheck : objectList) {
                  if (!objectCheck.isPaused()) {
                     ObjectBase.ResourceStorePriority px = objectCheck.getObjectFlags().getBuildingStorePriority().get(t);
                     if (px != null && this.thisMob.canReach(objectCheck, true) && objectCheck.getAcceptStorageResourceCount(t, -1) > 0) {
                        validSpace.get(px).add(objectCheck);
                        foundOnex = true;
                     }
                  }
               }

               if (foundOnex) {
                  boolean var32 = false;

                  for (ObjectBase objectCheckx : objectList) {
                     if (!objectCheckx.isPaused()) {
                        ObjectBase.ResourceStorePriority px = objectCheckx.getObjectFlags().getBuildingStorePriority().get(t);
                        if (px != null
                           && this.thisMob.canReach(objectCheckx, true)
                           && objectCheckx.isResourceAvailable(t)
                           && (!job.getCanWorkerRedistributeOnlyFull().contains(t) || objectCheckx.isFullResourceAvailable(t))
                           && (
                              !objectCheckx.getObjectFlags().canBuildingBeBuilt(t)
                                 || objectCheckx.getResourceCount(t)
                                    > objectCheckx.getObjectFlags().getResourceValueBaseCount(t) - objectCheckx.getResourceValueCount(t)
                           )) {
                           validStores.get(px).add(objectCheckx);
                           var32 = true;
                        }
                     }
                  }

                  if (var32) {
                     ObjectBase.ResourceStorePriority[] var13;
                     for (ObjectBase.ResourceStorePriority px : ObjectBase.ResourceStorePriority.values()) {
                        ArrayList<ObjectBase> listSpace = validSpace.get(px);
                        if (listSpace.size() > 0) {
                           ArrayList<ObjectBase> mergedStores = new ArrayList<>();

                           ObjectBase.ResourceStorePriority[] pickupOutCoordinates;
                           for (ObjectBase.ResourceStorePriority p2 : pickupOutCoordinates = ObjectBase.ResourceStorePriority.values()) {
                              if (px.ordinal() < p2.ordinal()) {
                                 ArrayList<ObjectBase> listStores = validStores.get(p2);
                                 if (listStores.size() > 0) {
                                    mergedStores.addAll(listStores);
                                 }
                              }
                           }

                           if (mergedStores.size() > 0) {
                              ObjectBase pickupOut = null;
                              ObjectBase deliveryOut = null;
                              OrderedPair pickupCoordinatesOut = this.pathFinder
                                 .searchForObject(
                                    this.thisMob.getTileX(), this.thisMob.getTileY(), mergedStores, false, true, this.thisMobFlags.getBlockMapGroup()
                                 );
                              if (pickupCoordinatesOut != null) {
                                 pickupOut = this.object.getBuildingAtLocation(pickupCoordinatesOut.getX(), pickupCoordinatesOut.getY());
                              }

                              if (job.canWorkerSortEvenly(t)) {
                                 int currentLowest = 100000;
                                 ObjectBase lowestBuilding = null;

                                 for (ObjectBase objectCheckxx : listSpace) {
                                    if (objectCheckxx.getResourceCount(t, true) < currentLowest) {
                                       lowestBuilding = objectCheckxx;
                                       currentLowest = objectCheckxx.getResourceCount(t, true);
                                    }
                                 }

                                 if (lowestBuilding != null) {
                                    deliveryOut = lowestBuilding;
                                 }
                              } else if (job.canWorkerSortClosest(t)) {
                                 OrderedPair deliveryCoordinatesOut = this.pathFinder
                                    .searchForObject(
                                       pickupCoordinatesOut.getX(), pickupCoordinatesOut.getY(), listSpace, false, true, this.thisMobFlags.getBlockMapGroup()
                                    );
                                 if (deliveryCoordinatesOut != null) {
                                    deliveryOut = this.object.getBuildingAtLocation(deliveryCoordinatesOut.getX(), deliveryCoordinatesOut.getY());
                                 }
                              } else if (job.canWorkerSortPriority(t)) {
                                 deliveryOut = listSpace.get(0);
                              }

                              OrderedPair pickupOutCoordinatesx = pickupOut.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
                              OrderedPair deliverOutCoordinates = deliveryOut.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
                              int carryMax = this.thisMob.getCarryMax();
                              int availableResources = pickupOut.getAvailableResourceCount(t, job.getCanWorkerRedistributeOnlyFull().contains(t));
                              int deliveryMax = deliveryOut.getAcceptStorageResourceCount(t, -1);
                              int pickupAmount = availableResources > carryMax ? carryMax : availableResources;
                              if (pickupAmount > deliveryMax) {
                                 pickupAmount = deliveryMax;
                              }

                              Assignment assignment = this.thisMob.getAssignment();
                              assignment.reset();
                              assignment.setDeliveryObject(deliveryOut);
                              assignment.setSecondaryObject(pickupOut);
                              assignment.setPrimaryCoordinates(deliverOutCoordinates);
                              assignment.setSecondaryCoordinates(pickupOutCoordinatesx);

                              for (int i = 0; i < pickupAmount; i++) {
                                 assignment.addPickupResources(pickupOut.getAvailableResource(t));
                              }

                              assignment.setPickupResourceType(t);
                              assignment.setAssignmentType(Assignment.AssignmentType.DELIVERY);
                              assignment.finalizeAssignment();
                              this.requestPath(this.thisMob.getAssignment().getSecondaryCoordinates());
                              this.aiStep = 1;
                              return;
                           }
                        }
                     }
                  }
               }
            }

            this.switchNode(failBranch);
         }
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingNameSource", this.thisMob.getAssignment().getSecondaryObjectName());
      Text.setVariableText("buildingNameDestination", this.thisMob.getAssignment().getDeliveryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.redistributeResources.pathfindToBuilding"));
      this.runPath();
      if (this.pathFailed) {
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void pickupResource() throws SlickException {
      ObjectBase building = this.thisMob.getAssignment().getSecondaryObject();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      Assignment assignment = this.thisMob.getAssignment();
      ResourceModule.ResourceType pickupType = assignment.getPickupResourceType();
      ObjectBase deliveryObject = assignment.getDeliveryObject();
      if (building != null
         && building == this.object.getBuildingAtLocation(tileX, tileY)
         && building.getResourceCount(pickupType) > 0
         && deliveryObject != null
         && this.thisMob.canReach(deliveryObject, true)) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            MobJobBase job = this.thisMob.getMobJobType().getJob();

            while (assignment.getPickupResources().size() > 0) {
               ResourceBase deliveryResource = assignment.removeAndGetFirstPickupResources();
               ResourceBase r = building.takeStores(deliveryResource);
               if (r == null) {
                  if (job.getCanWorkerRedistributeOnlyFull().contains(pickupType)) {
                     if (!building.isFullResourceAvailable(pickupType)) {
                        assignment.resetPickupResources();
                        break;
                     }
                  } else {
                     r = building.takeStores(pickupType);
                  }
               }

               if (r != null) {
                  this.thisMob.addCarriedResource(r, true);
               }
            }

            while (
               this.thisMob.canCarryMore()
                  && building.getAvailableResourceCount(pickupType, job.getCanWorkerRedistributeOnlyFull().contains(pickupType)) > 0
                  && deliveryObject.getAcceptStorageResourceCount(pickupType, this.thisMob.getID()) > this.thisMob.getCarriedResourcesList().size()
            ) {
               this.thisMob.addCarriedResource(building.takeStores(pickupType), true);
            }

            this.thisMob.getSprite().restart();
            if (this.thisMob.hasCarriedResources()) {
               this.thisMob.setPositionLift();
               this.aiStep = 3;
            } else {
               this.thisMob.setPositionFace();
               this.aiStep = 0;
            }
         }
      } else {
         assignment.resetPickupResources();
         this.aiStep = 0;
      }
   }

   private void liftResource() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         Assignment assignment = this.thisMob.getAssignment();
         OrderedPair deliveryCoordinates = assignment.getSecondaryCoordinates();
         if (deliveryCoordinates != null && this.thisMob.canReach(deliveryCoordinates, true)) {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.aiStep = 4;
         } else {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.aiStep = 0;
         }
      }
   }

   private void setPathToDelivery() throws SlickException {
      ObjectBase objectCheck = this.thisMob.getAssignment().getDeliveryObject();
      if (objectCheck != null && this.thisMob.canReach(objectCheck, true)) {
         OrderedPair coordinates = objectCheck.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
         this.thisMob.getAssignment().setPrimaryCoordinates(coordinates);
         this.requestPath(coordinates);
         this.aiStep = 5;
      } else {
         this.switchNode(failBranchWithResource);
      }
   }

   private void pathfindToDelivery() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getDeliveryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.redistributeResources.pathfindToDelivery"));
      this.runPath();
      ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
      if (!this.object.objectExists(deliveryObject)) {
         this.switchNode(failBranchWithResource);
      } else if (this.pathCompleted) {
         this.aiStep = 6;
      } else if (!this.pathFailed && this.thisMob.canReach(this.goalCoordinates, true)) {
         if (this.searchForMore()) {
            this.aiStep = 8;
         }
      } else {
         if (deliveryObject != null && this.thisMob.canReach(deliveryObject, true)) {
            OrderedPair newCoordinates = deliveryObject.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
            this.thisMob.getAssignment().setPrimaryCoordinates(newCoordinates);
            this.requestPath(newCoordinates);
         } else {
            this.aiStep = 4;
         }
      }
   }

   private void dropOffResource() throws SlickException {
      OrderedPair coordinates = this.thisMob.getAssignment().getPrimaryCoordinates();
      int workX = coordinates.getX();
      int workY = coordinates.getY();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase deliveryBuilding = this.thisMob.getAssignment().getDeliveryObject();
      ResourceModule.ResourceType resourceType = this.thisMob.getCarriedResourcesListType();
      if (workX == tileX
         && workY == tileY
         && this.object.objectExists(deliveryBuilding)
         && deliveryBuilding.getResourceCount(resourceType) < deliveryBuilding.getObjectFlags().getResourceStorageMax().get(resourceType)) {
         this.thisMob.setPositionDrop();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.awardXP(Utilities.randomInt(5) + 5);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.01, GodModule.GodXPType.REDISTRIBUTING_RESOURCES);
            }

            ResourceBase r = this.thisMob.removeFirstCarriedResource();
            deliveryBuilding.increaseStores(r);
            this.resource.setResourceInBuilding(deliveryBuilding, r);
            if (r.getType() == ResourceModule.ResourceType.SUSPICICIOUS_KEY
               && this.thisMob.getMobGroup() == MobBase.MobGroup.DOMESTICATED_ANIMAL
               && deliveryBuilding.getObjectFlags().getBaseType() == MapTilesLoader.TileSet.LOOT_BOX) {
               this.goal.incrementGoal(GoalModule.GoalType.ATTABOY);
            }

            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 7;
         }
      } else {
         this.switchNode(failBranchWithResource);
      }
   }

   private void dropOffLift() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         ObjectBase deliveryBuilding = this.thisMob.getAssignment().getDeliveryObject();
         ResourceModule.ResourceType resourceType = this.thisMob.getCarriedResourcesListType();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         if (this.thisMob.hasCarriedResources() && deliveryBuilding.getAcceptStorageResourceCount(resourceType, this.thisMob.getID()) > 0) {
            if (this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup())[tileX][tileY] > 0) {
               return;
            }

            OrderedPair randomOut = deliveryBuilding.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
            if (randomOut == null) {
               this.thisMob.getSprite().restart();
               this.thisMob.setPositionFace();
               this.thisMob.getAssignment().reset();
               this.aiStep = 0;
               return;
            }

            this.thisMob.getAssignment().setPrimaryCoordinates(randomOut);
            this.requestPath(randomOut);
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.aiStep = 5;
         } else {
            this.switchNode(resetBranch);
         }
      }
   }

   private void pathfindToExtraResource() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getDeliveryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.redistributeResources.pathfindToExtraResource"));
      this.runPath();
      OrderedPair o = this.thisMob.getAssignment().getSecondaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (!this.pathFailed
         && r != null
         && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
         && !this.road.isResourceNeededForRoad(r)
         && r.getType() == this.thisMob.getCarriedResourcesListType()) {
         if (this.pathCompleted) {
            this.aiStep = 9;
         }
      } else {
         ObjectBase currentObject = this.thisMob.getAssignment().getDeliveryObject();
         if (currentObject != null && this.thisMob.canReach(currentObject, true)) {
            OrderedPair randomRecalc = currentObject.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
            this.thisMob.getAssignment().setPrimaryCoordinates(randomRecalc);
            this.requestPath(randomRecalc);
            this.aiStep = 5;
         } else {
            this.switchNode(failBranchWithResource);
         }
      }
   }

   private void pickupExtraResource() throws SlickException {
      this.thisMob.setPositionPickup();
      if (this.thisMob.getSprite().isStopped()) {
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         Assignment assignment = this.thisMob.getAssignment();
         ResourceBase resourceOnGround = this.resource.getResourceOnGround(tileX, tileY);
         ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
         if (deliveryObject != null
            && this.thisMob.canReach(deliveryObject, true)
            && resourceOnGround != null
            && assignment.containsPickupResources(resourceOnGround)) {
            this.thisMob.addCarriedResource(resourceOnGround);
            this.thisMob.getAssignment().removePickupResources(resourceOnGround);
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 10;
         } else {
            this.switchNode(failBranchWithResource);
         }
      }
   }

   private void liftExtraResource() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
         this.aiStep = 5;
      }
   }

   private boolean searchForMore() throws SlickException {
      if (!this.thisMob.canCarryMore()) {
         return false;
      } else {
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         if (this.thisMob.getWorkFindExtraResourcesLastX() == tileX && this.thisMob.getWorkFindExtraResourcesLastY() == tileY) {
            return false;
         } else {
            this.thisMob.setWorkFindExtraResourcesLastX(tileX);
            this.thisMob.setWorkFindExtraResourcesLastY(tileY);
            Assignment assignment = this.thisMob.getAssignment();
            if (assignment.getDeliveryObject().getAcceptStorageResourceCount(this.thisMob.getCarriedResourcesListType(), -1) <= 0) {
               return false;
            } else {
               ArrayList<ResourceBase> resourcesInRange = this.resource.getResourcesNearby(tileX, tileY, this.thisMobFlags.getWorkFindExtraResourcesDistance());
               if (resourcesInRange.size() > 0) {
                  resourcesInRange = this.resource
                     .cullResourcesByDistance(
                        tileX, tileY, this.thisMobFlags.getWorkFindExtraResourcesDistance(), resourcesInRange, this.thisMobFlags.getBlockMapGroup()
                     );
                  if (resourcesInRange.size() > 0) {
                     for (int i = resourcesInRange.size() - 1; i >= 0; i--) {
                        ResourceBase r = resourcesInRange.get(i);
                        if (r.getType() != this.thisMob.getCarriedResourcesListType()
                           || r.isAssigned()
                           || this.thisMobFlags.canIgnoreThreatenedTiles() && this.map.getMapThreatMap().isResourceThreatened(r)
                           || this.road.isResourceNeededForRoad(r)) {
                           resourcesInRange.remove(i);
                        }
                     }

                     if (resourcesInRange.size() > 0) {
                        OrderedPair pickupCoordinatesOut = this.pathFinder
                           .searchForResource(
                              this.thisMob.getTileX(), this.thisMob.getTileY(), resourcesInRange, false, true, this.thisMobFlags.getBlockMapGroup()
                           );
                        if (pickupCoordinatesOut != null) {
                           ResourceBase r = this.resource.getResourceOnGround(pickupCoordinatesOut.getX(), pickupCoordinatesOut.getY());
                           assignment.setSecondaryCoordinates(r.getTileX(), r.getTileY());
                           assignment.addPickupResources(r);
                           this.requestPath(r.getTileX(), r.getTileY());
                           return true;
                        }
                     }
                  }
               }

               return false;
            }
         }
      }
   }
}
