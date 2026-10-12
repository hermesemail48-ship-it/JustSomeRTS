package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;

public class AIStoreResource extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_RESOURCE = 1;
   private static final int PICK_UP_RESOURCE = 2;
   private static final int LIFT_RESOURCE = 3;
   private static AIBase failBranch;
   private static AIBase nextBranch;

   public AIStoreResource(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_STORE_RESOURCE;
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
         failBranch = this.thisMob.getAIStoreTrashInTrashCan();
         nextBranch = this.thisMob.getAIStoreResourceInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToResource();
               break;
            case 2:
               this.pickUpResource();
               break;
            case 3:
               this.liftResource();
         }
      }
   }

   private void findWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         MobJobBase job = jobType.getJob();
         if (!job.canWorkerStore()) {
            this.switchNode(failBranch);
         } else {
            int mobID = this.thisMob.getID();
            ArrayList<ResourceModule.ResourceType> canStoreResources = new ArrayList<>();
            ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();

            for (ResourceModule.ResourceType type : job.getCanWorkerStore()) {
               for (ObjectBase objectCheck : objectList) {
                  if (!objectCheck.isPaused()
                     && objectCheck.getObjectFlags().canBuildingStore(type)
                     && (!job.canWorkerStoreWorksiteOnly(type) || objectCheck.getObjectFlags().getWorkerJobType() == this.thisMob.getMobJobType())
                     && this.thisMob.canReach(objectCheck, true)
                     && objectCheck.getAcceptStorageResourceCount(type, mobID) > 0) {
                     canStoreResources.add(type);
                     break;
                  }
               }
            }

            if (canStoreResources.size() == 0) {
               this.switchNode(failBranch);
            } else {
               HashMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> validResourcesOnGroundByType = AIUtilities.createAvailabeResourceList(
                  this.thisMob, canStoreResources, true, this.thisMobFlags.getWorkNearbyResourcesOnGroundDistance()
               );
               Collections.shuffle(canStoreResources);

               for (ResourceModule.ResourceType storageType : canStoreResources) {
                  ArrayList<ResourceBase> resourcesOnGroundByTypeArray = validResourcesOnGroundByType.get(storageType);
                  if (resourcesOnGroundByTypeArray.size() != 0) {
                     OrderedPair nearestResourceCoordinates = this.pathFinder
                        .searchForResource(tileX, tileY, resourcesOnGroundByTypeArray, false, true, this.thisMobFlags.getBlockMapGroup());
                     if (nearestResourceCoordinates != null) {
                        ArrayList<ObjectBase> validObjects = new ArrayList<>();

                        for (ObjectBase objectCheckx : objectList) {
                           if (!objectCheckx.isPaused()
                              && objectCheckx.getObjectFlags().canBuildingStore(storageType)
                              && (
                                 !job.canWorkerStoreWorksiteOnly(storageType)
                                    || objectCheckx.getObjectFlags().getWorkerJobType() == this.thisMob.getMobJobType()
                              )
                              && this.thisMob.canReach(objectCheckx, true)
                              && objectCheckx.getAcceptStorageResourceCount(storageType, mobID) > 0) {
                              validObjects.add(objectCheckx);
                           }
                        }

                        if (validObjects.size() != 0) {
                           EnumMap<ObjectBase.ResourceStorePriority, ArrayList<ObjectBase>> validSorted = new EnumMap<>(ObjectBase.ResourceStorePriority.class);

                           ObjectBase.ResourceStorePriority[] var18;
                           for (ObjectBase.ResourceStorePriority p : ObjectBase.ResourceStorePriority.values()) {
                              validSorted.put(p, new ArrayList<>());
                           }

                           for (ObjectBase objectCheckxx : validObjects) {
                              ObjectBase.ResourceStorePriority p = objectCheckxx.getObjectFlags().getBuildingStorePriority().get(storageType);
                              if (p != null) {
                                 validSorted.get(p).add(objectCheckxx);
                              }
                           }

                           ObjectBase deliveryOut = null;
                           if (job.canWorkerSortEvenly(storageType)) {
                              ObjectBase.ResourceStorePriority[] var19;
                              for (ObjectBase.ResourceStorePriority p : ObjectBase.ResourceStorePriority.values()) {
                                 ArrayList<ObjectBase> list = validSorted.get(p);
                                 if (list.size() > 0) {
                                    int currentLowest = 100000;
                                    ObjectBase lowestBuilding = null;

                                    for (ObjectBase objectCheckxxx : list) {
                                       if (objectCheckxxx.getResourceCount(storageType, true) < currentLowest) {
                                          lowestBuilding = objectCheckxxx;
                                          currentLowest = objectCheckxxx.getResourceCount(storageType, true);
                                       }
                                    }

                                    if (lowestBuilding != null) {
                                       deliveryOut = lowestBuilding;
                                       break;
                                    }
                                 }
                              }
                           } else if (job.canWorkerSortClosest(storageType)) {
                              ObjectBase.ResourceStorePriority[] var45;
                              for (ObjectBase.ResourceStorePriority px : ObjectBase.ResourceStorePriority.values()) {
                                 ArrayList<ObjectBase> list = validSorted.get(px);
                                 if (list.size() > 0) {
                                    OrderedPair deliveryCoordinatesOut = this.pathFinder
                                       .searchForObject(
                                          nearestResourceCoordinates.getX(),
                                          nearestResourceCoordinates.getY(),
                                          list,
                                          false,
                                          true,
                                          this.thisMobFlags.getBlockMapGroup()
                                       );
                                    if (deliveryCoordinatesOut != null) {
                                       deliveryOut = this.object.getBuildingAtLocation(deliveryCoordinatesOut.getX(), deliveryCoordinatesOut.getY());
                                       break;
                                    }
                                 }
                              }
                           } else if (job.canWorkerSortPriority(storageType)) {
                              ObjectBase.ResourceStorePriority[] var46;
                              for (ObjectBase.ResourceStorePriority pxx : ObjectBase.ResourceStorePriority.values()) {
                                 ArrayList<ObjectBase> list = validSorted.get(pxx);
                                 if (list.size() > 0) {
                                    deliveryOut = list.get(0);
                                    break;
                                 }
                              }
                           }

                           Assignment assignment = this.thisMob.getAssignment();
                           assignment.reset();
                           assignment.setDeliveryObject(deliveryOut);
                           assignment.setPrimaryCoordinates(nearestResourceCoordinates);
                           assignment.addPickupResources(
                              this.resource.getResourceOnGround(nearestResourceCoordinates.getX(), nearestResourceCoordinates.getY())
                           );
                           assignment.setAssignmentType(Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND);
                           assignment.finalizeAssignment();
                           this.requestPath(nearestResourceCoordinates);
                           this.aiStep = 1;
                           return;
                        }
                     }
                  }
               }

               this.switchNode(failBranch);
            }
         }
      }
   }

   private void pathfindToResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.storeResource.pathfindToResource"));
      this.runPath();
      OrderedPair o = this.thisMob.getAssignment().getPrimaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (!this.pathFailed
         && r != null
         && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
         && !this.road.isResourceNeededForRoad(r)
         && this.thisMob.getAssignment().containsPickupResources(r)) {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void pickUpResource() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
      if (deliveryObject != null && this.thisMob.canReach(deliveryObject, true) && this.resource.getResourceTypeOnGround(tileX, tileY) != null) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.addCarriedResource(this.resource.getResourceOnGround(tileX, tileY));
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 3;
         }
      } else {
         this.aiStep = 0;
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
      }
   }

   private void liftResource() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
         if (deliveryObject != null && this.thisMob.canReach(deliveryObject, true)) {
            this.switchNode(nextBranch);
            Assignment assignment = this.thisMob.getAssignment();
            assignment.setDeliveryObject(deliveryObject);
            assignment.setAssignmentType(Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND);
            assignment.finalizeAssignment();
         } else {
            this.thisMob.getSprite().restart();
            this.aiStep = 0;
         }
      }
   }
}
