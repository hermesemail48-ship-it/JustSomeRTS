package rtr.mobs.ai;

import java.util.ArrayList;
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

public class AIStoreResourceInHand extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_BUILDING = 1;
   private static final int DROP_OFF_RESOURCE = 2;
   private static final int DROP_OFF_LIFT = 3;
   private static final int PATHFIND_TO_EXTRA_RESOURCE = 4;
   private static final int PICKUP_EXTRA_RESOURCE = 5;
   private static final int LIFT_EXTRA_RESOURCE = 6;
   private static AIBase failBranch;
   private static AIBase resetBranch;

   public AIStoreResourceInHand(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_STORE_RESOURCE_IN_HAND;
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
         failBranch = this.thisMob.getAIStoreTrashInTrashCanInHand();
         resetBranch = this.thisMob.getAIStart();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToBuilding();
               break;
            case 2:
               this.dropOffResource();
               break;
            case 3:
               this.dropOffLift();
               break;
            case 4:
               this.pathfindToExtraResource();
               break;
            case 5:
               this.pickupExtraResource();
               break;
            case 6:
               this.liftExtraResource();
         }
      }
   }

   private void findWork() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      ObjectBase deliveryOut = assignment.getDeliveryObject();
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         if (deliveryOut != null && (!this.object.objectExists(deliveryOut) || !this.thisMob.canReach(deliveryOut, true))) {
            assignment.reset();
            deliveryOut = null;
         }

         if (deliveryOut == null) {
            MobJobBase job = jobType.getJob();
            ResourceModule.ResourceType resourceTypeCarried = this.thisMob.getCarriedResourcesListType();
            if (!job.canWorkerStore(resourceTypeCarried)) {
               this.switchNode(failBranch);
               return;
            }

            ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
            boolean canStore = false;

            for (ObjectBase objectCheck : objectList) {
               if (!objectCheck.isPaused()
                  && objectCheck.getObjectFlags().canBuildingStore(resourceTypeCarried)
                  && (!job.canWorkerStoreWorksiteOnly(resourceTypeCarried) || objectCheck.getObjectFlags().getWorkerJobType() == this.thisMob.getMobJobType())
                  && this.thisMob.canReach(objectCheck, true)
                  && objectCheck.getAcceptStorageResourceCount(resourceTypeCarried, -1) > 0) {
                  canStore = true;
                  break;
               }
            }

            if (!canStore) {
               this.switchNode(failBranch);
               return;
            }

            ArrayList<ObjectBase> validObjects = new ArrayList<>();

            for (ObjectBase objectCheckx : objectList) {
               if (!objectCheckx.isPaused()
                  && objectCheckx.getObjectFlags().canBuildingStore(resourceTypeCarried)
                  && (!job.canWorkerStoreWorksiteOnly(resourceTypeCarried) || objectCheckx.getObjectFlags().getWorkerJobType() == this.thisMob.getMobJobType())
                  && this.thisMob.canReach(objectCheckx, true)
                  && objectCheckx.getAcceptStorageResourceCount(resourceTypeCarried, -1) > 0) {
                  validObjects.add(objectCheckx);
               }
            }

            if (validObjects.size() == 0) {
               this.switchNode(failBranch);
               return;
            }

            EnumMap<ObjectBase.ResourceStorePriority, ArrayList<ObjectBase>> validSorted = new EnumMap<>(ObjectBase.ResourceStorePriority.class);

            ObjectBase.ResourceStorePriority[] var13;
            for (ObjectBase.ResourceStorePriority p : ObjectBase.ResourceStorePriority.values()) {
               validSorted.put(p, new ArrayList<>());
            }

            for (ObjectBase objectCheckxx : validObjects) {
               ObjectBase.ResourceStorePriority p = objectCheckxx.getObjectFlags().getBuildingStorePriority().get(resourceTypeCarried);
               if (p != null) {
                  validSorted.get(p).add(objectCheckxx);
               }
            }

            if (job.canWorkerSortEvenly(resourceTypeCarried)) {
               for (ObjectBase.ResourceStorePriority p : ObjectBase.ResourceStorePriority.values()) {
                  ArrayList<ObjectBase> list = validSorted.get(p);
                  if (list.size() > 0) {
                     int currentLowest = 100000;
                     ObjectBase lowestBuilding = null;

                     for (ObjectBase objectCheckxxx : list) {
                        if (objectCheckxxx.getResourceCount(resourceTypeCarried, true) < currentLowest) {
                           lowestBuilding = objectCheckxxx;
                           currentLowest = objectCheckxxx.getResourceCount(resourceTypeCarried, true);
                        }
                     }

                     if (lowestBuilding != null) {
                        deliveryOut = lowestBuilding;
                        break;
                     }
                  }
               }
            } else if (job.canWorkerSortClosest(resourceTypeCarried)) {
               for (ObjectBase.ResourceStorePriority px : ObjectBase.ResourceStorePriority.values()) {
                  ArrayList<ObjectBase> list = validSorted.get(px);
                  if (list.size() > 0) {
                     OrderedPair deliveryCoordinatesOut = this.pathFinder
                        .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), list, false, true, this.thisMobFlags.getBlockMapGroup());
                     if (deliveryCoordinatesOut != null) {
                        deliveryOut = this.object.getBuildingAtLocation(deliveryCoordinatesOut.getX(), deliveryCoordinatesOut.getY());
                        break;
                     }
                  }
               }
            } else if (job.canWorkerSortPriority(resourceTypeCarried)) {
               for (ObjectBase.ResourceStorePriority pxx : ObjectBase.ResourceStorePriority.values()) {
                  ArrayList<ObjectBase> list = validSorted.get(pxx);
                  if (list.size() > 0) {
                     deliveryOut = list.get(0);
                     break;
                  }
               }
            }
         }

         OrderedPair randomOut = deliveryOut.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
         assignment.reset();
         assignment.setDeliveryObject(deliveryOut);
         assignment.setPrimaryCoordinates(randomOut);
         assignment.setAssignmentType(Assignment.AssignmentType.DELIVERY);
         assignment.finalizeAssignment();
         this.requestPath(randomOut);
         this.aiStep = 1;
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getDeliveryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.storeResourceInHand.pathfindToBuilding"));
      this.runPath();
      ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
      if (deliveryObject == null || !this.object.objectExists(deliveryObject)) {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      } else if (this.pathCompleted) {
         this.aiStep = 2;
      } else if (!this.pathFailed && this.thisMob.canReach(this.goalCoordinates, true)) {
         if (this.searchForMore()) {
            this.aiStep = 4;
         }
      } else if (deliveryObject != null && this.thisMob.canReach(deliveryObject, true)) {
         OrderedPair randomCoordinates = deliveryObject.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
         this.thisMob.getAssignment().setPrimaryCoordinates(randomCoordinates);
         this.requestPath(randomCoordinates);
      } else {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
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
      if (workX == tileX && workY == tileY) {
         this.thisMob.setPositionDrop();
         if (!this.object.objectExists(deliveryBuilding)
            || deliveryBuilding.getResourceCount(resourceType) >= deliveryBuilding.getObjectFlags().getResourceStorageMax().get(resourceType)) {
            this.thisMob.getAssignment().reset();
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.aiStep = 0;
         } else if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.awardXP(Utilities.randomInt(5) + 5);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.01, GodModule.GodXPType.STORING_RESOURCES);
            }

            if (this.thisMob.getMobGroup() == MobBase.MobGroup.DOMESTICATED_ANIMAL) {
               this.goal.incrementGoal(GoalModule.GoalType.HELPFUL_DOGGOS);
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
            this.aiStep = 3;
         }
      } else {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      }
   }

   private void dropOffLift() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase deliveryBuilding = this.thisMob.getAssignment().getDeliveryObject();
      ResourceModule.ResourceType resourceType = this.thisMob.getCarriedResourcesListType();
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         if (this.thisMob.hasCarriedResources() && deliveryBuilding.getAcceptStorageResourceCount(resourceType, this.thisMob.getID()) > 0) {
            if (this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup())[tileX][tileY] <= 0) {
               OrderedPair randomOut = deliveryBuilding.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
               if (randomOut == null) {
                  this.thisMob.getAssignment().reset();
                  this.thisMob.getSprite().restart();
                  this.thisMob.setPositionFace();
                  this.aiStep = 0;
               } else {
                  this.thisMob.getAssignment().setPrimaryCoordinates(randomOut);
                  this.requestPath(randomOut);
                  this.thisMob.getSprite().restart();
                  this.aiStep = 1;
               }
            }
         } else if (this.thisMob.hasCarriedResources()) {
            this.thisMob.getAssignment().reset();
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.aiStep = 0;
         } else {
            this.switchNode(resetBranch);
         }
      }
   }

   private void pathfindToExtraResource() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getDeliveryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.storeResourceInHand.pathfindToExtraResource"));
      this.runPath();
      OrderedPair o = this.thisMob.getAssignment().getSecondaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (!this.pathFailed
         && r != null
         && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
         && !this.road.isResourceNeededForRoad(r)
         && r.getType() == this.thisMob.getCarriedResourcesListType()) {
         if (this.pathCompleted) {
            this.aiStep = 5;
         }
      } else {
         ObjectBase currentObject = this.thisMob.getAssignment().getDeliveryObject();
         if (currentObject != null && this.thisMob.canReach(currentObject, true)) {
            OrderedPair randomRecalc = currentObject.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
            this.thisMob.getAssignment().setPrimaryCoordinates(randomRecalc);
            this.requestPath(randomRecalc);
            this.aiStep = 1;
         } else {
            this.thisMob.getAssignment().reset();
            this.aiStep = 0;
         }
      }
   }

   private void pickupExtraResource() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      Assignment assignment = this.thisMob.getAssignment();
      ResourceBase resourceOnGround = this.resource.getResourceOnGround(tileX, tileY);
      ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
      if (deliveryObject != null && resourceOnGround != null && this.thisMob.canReach(deliveryObject, true)) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped() && assignment.containsPickupResources(resourceOnGround)) {
            this.thisMob.addCarriedResource(resourceOnGround);
            this.thisMob.getAssignment().removePickupResources(resourceOnGround);
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 6;
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void liftExtraResource() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
         if (deliveryObject != null && this.thisMob.canReach(deliveryObject, true)) {
            OrderedPair randomRecalc = deliveryObject.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.thisMob.getAssignment().setPrimaryCoordinates(randomRecalc);
            this.requestPath(randomRecalc);
            this.aiStep = 1;
         } else {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.thisMob.getAssignment().reset();
            this.aiStep = 0;
         }
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
