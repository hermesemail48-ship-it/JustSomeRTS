package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
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

public class AIDeliverCourierResource extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_RESOURCE_ON_GROUND = 1;
   private static final int PATHFIND_TO_BUILDING = 2;
   private static final int PICK_UP_RESOURCE_ON_GROUND = 3;
   private static final int PICK_UP_RESOURCE_IN_BUILDING = 4;
   private static final int LIFT_RESOURCE = 5;
   private static AIBase failBranch;
   private static AIBase exitBranch;

   public AIDeliverCourierResource(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_DELIVER_COURIER_RESOURCE;
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
         failBranch = this.thisMob.getAIRedistributeResources();
         exitBranch = this.thisMob.getAIDeliverCourierResourceInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToResourceOnGround();
               break;
            case 2:
               this.pathfindToBuilding();
               break;
            case 3:
               this.pickUpResourceOnGround();
               break;
            case 4:
               this.pickUpResourcesInBuilding();
               break;
            case 5:
               this.liftResource();
         }
      }
   }

   private void findWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canWorkerDeliverCourier()) {
            this.switchNode(failBranch);
         } else {
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            ArrayList<MobBase> allCouriers = this.mob.getMobsByAssignment(Assignment.AssignmentType.COURIER_PARKED, false);
            ArrayList<MobBase> allCourierSuppliersEnroute = this.mob.getMobsByAssignment(Assignment.AssignmentType.COURIER_SUPPLIER_DELIVERING, false);
            ArrayList<MobBase> allValidCouriers = new ArrayList<>();

            label227:
            for (MobBase m : allCouriers) {
               if (this.thisMob.canReach(m, true)) {
                  for (MobBase m2 : allCourierSuppliersEnroute) {
                     if (m2.getAssignment().getPrimaryMob() == m) {
                        continue label227;
                     }
                  }

                  if (this.migration.canCourierSendMore(m)) {
                     allValidCouriers.add(m);
                  }
               }
            }

            if (allValidCouriers.size() == 0) {
               this.switchNode(failBranch);
            } else {
               ArrayList<ResourceModule.ResourceType> canCourierAcceptResources = new ArrayList<>();

               for (MobBase mx : allValidCouriers) {
                  if (mx.getCarriedResourcesList().size() == 0) {
                     canCourierAcceptResources.clear();
                     canCourierAcceptResources.addAll(job.getCanWorkerDeliverCourier());
                     break;
                  }

                  for (ResourceModule.ResourceType type : job.getCanWorkerDeliverCourier()) {
                     if (mx.getCarriedResourcesListType() == type && !canCourierAcceptResources.contains(type)) {
                        canCourierAcceptResources.add(type);
                     }
                  }
               }

               if (canCourierAcceptResources.size() == 0) {
                  this.switchNode(failBranch);
               } else {
                  ArrayList<ResourceModule.ResourceType> canDeliverResources = new ArrayList<>();

                  for (ResourceModule.ResourceType typex : canCourierAcceptResources) {
                     if (this.migration.getTotalCourierResourceSendAmount(typex) != 0 || this.migration.getTotalCourierResourceDailyAmount(typex) != 0) {
                        String[] mapList = this.map.getConnectedRegionList();

                        for (String mapName : mapList) {
                           if (!mapName.equals("null") && this.migration.getDailySendAmountRemaining(mapName, typex) > 0) {
                              canDeliverResources.add(typex);
                           }
                        }
                     }
                  }

                  if (canDeliverResources.size() == 0) {
                     this.switchNode(failBranch);
                  } else {
                     ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
                     HashMap<ResourceModule.ResourceType, ArrayList<ObjectBase>> validStoresByTypeLowestPriority = AIUtilities.createLowestPriorityStorageListHashMap(
                        this.thisMob, canDeliverResources, objectList
                     );
                     HashMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> validResourcesOnGroundByType = AIUtilities.createAvailabeResourceList(
                        this.thisMob, canDeliverResources, true, this.thisMobFlags.getWorkNearbyResourcesOnGroundDistance()
                     );
                     String[] mapList = this.map.getConnectedRegionList();
                     ArrayList<String> mapArray = new ArrayList<>();

                     for (String mapNamex : mapList) {
                        if (!mapNamex.equals("null")) {
                           mapArray.add(mapNamex);
                        }
                     }

                     Collections.shuffle(mapArray);
                     Collections.shuffle(canDeliverResources);

                     for (ResourceModule.ResourceType deliveryType : canDeliverResources) {
                        ArrayList<MobBase> mapSpecificValidCouriers = new ArrayList<>();
                        String selectedMap = null;

                        for (String mapNamexx : mapArray) {
                           int requestedAmount = this.migration.getDailySendAmountRemaining(mapNamexx, deliveryType);
                           if (requestedAmount > 0) {
                              for (MobBase mx : allValidCouriers) {
                                 if ((mx.getAssignment().getRegionFileName().equals("") || mx.getAssignment().getRegionFileName().equals(mapNamexx))
                                    && (!mx.hasCarriedResources() || mx.getCarriedResourcesListType() == deliveryType)) {
                                    mapSpecificValidCouriers.add(mx);
                                 }
                              }

                              if (mapSpecificValidCouriers.size() > 0) {
                                 selectedMap = mapNamexx;
                                 break;
                              }
                           }
                        }

                        if (selectedMap != null) {
                           ArrayList<ObjectBase> validStoresByTypeLowestPriorityArray = validStoresByTypeLowestPriority.get(deliveryType);
                           ArrayList<ResourceBase> resourcesOnGroundByTypeArray = validResourcesOnGroundByType.get(deliveryType);
                           if (validStoresByTypeLowestPriorityArray.size() != 0 || resourcesOnGroundByTypeArray.size() != 0) {
                              ArrayList<MobBase> mapSpecificValidCouriersWithResources = new ArrayList<>();
                              ArrayList<MobBase> mapSpecificValidCouriersWithoutResources = new ArrayList<>();

                              for (MobBase mxx : mapSpecificValidCouriers) {
                                 if (mxx.hasCarriedResources()) {
                                    mapSpecificValidCouriersWithResources.add(mxx);
                                 } else {
                                    mapSpecificValidCouriersWithoutResources.add(mxx);
                                 }
                              }

                              ArrayList<MobBase> mapSpecificValidCouriersFinalOut = null;
                              if (mapSpecificValidCouriersWithResources.size() > 0) {
                                 mapSpecificValidCouriersFinalOut = mapSpecificValidCouriersWithResources;
                              } else {
                                 mapSpecificValidCouriersFinalOut = mapSpecificValidCouriersWithoutResources;
                              }

                              OrderedPair nearestCourierCoordinates = this.pathFinder
                                 .searchForMob(
                                    this.thisMob.getTileX(),
                                    this.thisMob.getTileY(),
                                    mapSpecificValidCouriersFinalOut,
                                    false,
                                    true,
                                    this.thisMobFlags.getBlockMapGroup()
                                 );
                              ArrayList<OrderedPair> coordinatesList = AIUtilities.createAvailabeResourceCoordinatesList(
                                 this.thisMob, validStoresByTypeLowestPriorityArray, resourcesOnGroundByTypeArray
                              );
                              OrderedPair closestResourceAnywhere = this.pathFinder
                                 .searchFor(
                                    this.thisMob.getTileX(), this.thisMob.getTileY(), coordinatesList, false, true, this.thisMobFlags.getBlockMapGroup()
                                 );
                              ResourceBase pickupResource = this.resource.getResourceOnGround(closestResourceAnywhere);
                              if (pickupResource != null && pickupResource.getType() == deliveryType && resourcesOnGroundByTypeArray.contains(pickupResource)) {
                                 assignment.setPrimaryMob(
                                    this.pathFinder
                                       .mobFetch(mapSpecificValidCouriersFinalOut, nearestCourierCoordinates.getX(), nearestCourierCoordinates.getY())
                                 );
                                 assignment.setPrimaryCoordinates(closestResourceAnywhere);
                                 assignment.addPickupResources(pickupResource);
                                 assignment.setPickupResourceType(deliveryType);
                                 assignment.setRegionName(this.map.getConnectedRegionListWorldName(selectedMap));
                                 assignment.setRegionFileName(selectedMap);
                                 assignment.setAssignmentType(Assignment.AssignmentType.COURIER_SUPPLIER_DELIVERING);
                                 assignment.finalizeAssignment();
                                 this.requestPath(closestResourceAnywhere);
                                 this.aiStep = 1;
                                 return;
                              }

                              ObjectBase pickupBuilding = this.object.getBuildingAtLocation(closestResourceAnywhere);
                              if (pickupBuilding != null && validStoresByTypeLowestPriorityArray.contains(pickupBuilding)) {
                                 OrderedPair pickupOutCoordinates = pickupBuilding.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
                                 MobBase courierOut = this.pathFinder
                                    .mobFetch(mapSpecificValidCouriersFinalOut, nearestCourierCoordinates.getX(), nearestCourierCoordinates.getY());
                                 int carryMax = this.thisMob.getCarryMax();
                                 int courierCarryRemaining = courierOut.getCarryMax() - courierOut.getCarriedResourcesList().size();
                                 int availableResources = pickupBuilding.getAvailableResourceCount(deliveryType, false);
                                 int deliveryMax = this.migration.getDailySendAmountRemaining(selectedMap, deliveryType);
                                 int pickupAmount = availableResources > carryMax ? carryMax : availableResources;
                                 if (pickupAmount > deliveryMax) {
                                    pickupAmount = deliveryMax;
                                 }

                                 if (pickupAmount > courierCarryRemaining) {
                                    pickupAmount = courierCarryRemaining;
                                 }

                                 assignment.setPrimaryMob(courierOut);
                                 assignment.setPrimaryObject(pickupBuilding);
                                 assignment.setPrimaryCoordinates(pickupOutCoordinates);

                                 for (int i = 0; i < pickupAmount; i++) {
                                    assignment.addPickupResources(pickupBuilding.getAvailableResource(deliveryType));
                                 }

                                 assignment.setPickupResourceType(deliveryType);
                                 assignment.setRegionName(this.map.getConnectedRegionListWorldName(selectedMap));
                                 assignment.setRegionFileName(selectedMap);
                                 assignment.setAssignmentType(Assignment.AssignmentType.COURIER_SUPPLIER_DELIVERING);
                                 assignment.finalizeAssignment();
                                 this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
                                 this.aiStep = 2;
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
      }
   }

   private void pathfindToResourceOnGround() throws SlickException {
      Text.setVariableText("regionName", this.thisMob.getAssignment().getRegionName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.deliverCourierResource.pathfindToResourceOnGround"));
      this.runPath();
      OrderedPair o = this.thisMob.getAssignment().getPrimaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (!this.pathFailed
         && r != null
         && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
         && !this.road.isResourceNeededForRoad(r)
         && this.thisMob.getAssignment().containsPickupResources(r)
         && this.migration.getDailySendAmountRemaining(this.thisMob.getAssignment().getRegionFileName(), this.thisMob.getAssignment().getPickupResourceType())
            >= 0) {
         if (this.pathCompleted) {
            this.aiStep = 3;
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void pickUpResourceOnGround() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      MobBase deliveryCourier = this.thisMob.getAssignment().getPrimaryMob();
      if (deliveryCourier != null
         && this.thisMob.canReach(deliveryCourier, true)
         && this.resource.getResourceTypeOnGround(tileX, tileY) != null
         && deliveryCourier.isAvailable()) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.addCarriedResource(this.resource.getResourceOnGround(tileX, tileY));
            this.thisMob.getAssignment().removeAndGetFirstPickupResources();
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 5;
         }
      } else {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionLift();
         this.aiStep = 0;
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObject().getFormattedName());
      Text.setVariableText("regionName", this.thisMob.getAssignment().getRegionName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.deliverCourierResource.pathfindToBuilding"));
      this.runPath();
      MobBase deliveryCourier = this.thisMob.getAssignment().getPrimaryMob();
      if (!this.pathFailed
         && deliveryCourier != null
         && deliveryCourier.getAssignment().getAssignmentType() == Assignment.AssignmentType.COURIER_PARKED
         && this.thisMob.canReach(deliveryCourier, true)
         && this.migration.getDailySendAmountRemaining(this.thisMob.getAssignment().getRegionFileName(), this.thisMob.getAssignment().getPickupResourceType())
            >= 0) {
         if (this.pathCompleted) {
            this.aiStep = 4;
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void pickUpResourcesInBuilding() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase building = assignment.getPrimaryObject();
      MobBase deliveryCourier = assignment.getPrimaryMob();
      if (building != null
         && deliveryCourier != null
         && this.thisMob.canReach(deliveryCourier, true)
         && building == this.object.getBuildingAtLocation(tileX, tileY)
         && building.getResourceCount(this.thisMob.getAssignment().getFirstPickupResources().getType()) > 0) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            ResourceModule.ResourceType pickupType = assignment.getPickupResources().get(0).getType();

            while (assignment.getPickupResources().size() > 0) {
               ResourceBase deliveryResource = assignment.removeAndGetFirstPickupResources();
               ResourceBase r = building.takeStores(deliveryResource);
               if (r == null) {
                  r = building.takeStores(pickupType);
               }

               if (r != null) {
                  this.thisMob.addCarriedResource(r, true);
               }
            }

            while (
               this.thisMob.canCarryMore()
                  && building.getAvailableResourceCount(pickupType, false) > 0
                  && this.trade.getAcceptSellCount(pickupType, this.thisMob.getID()) > this.thisMob.getCarriedResourcesList().size()
            ) {
               this.thisMob.addCarriedResource(building.takeStores(pickupType));
            }

            this.thisMob.getSprite().restart();
            if (this.thisMob.hasCarriedResources()) {
               this.thisMob.setPositionLift();
               this.aiStep = 5;
            } else {
               this.thisMob.setPositionFace();
               this.aiStep = 0;
            }
         }
      } else {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.aiStep = 0;
      }
   }

   private void liftResource() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      MobBase deliveryCourier = assignment.getPrimaryMob();
      if (deliveryCourier != null && this.thisMob.canReach(deliveryCourier, true) && deliveryCourier.isAvailable()) {
         this.thisMob.setPositionLift();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            String mapName = assignment.getRegionName();
            String mapFileName = assignment.getRegionFileName();
            this.switchNode(exitBranch);
            assignment.setPrimaryMob(deliveryCourier);
            assignment.setRegionName(mapName);
            assignment.setRegionFileName(mapFileName);
            assignment.setAssignmentType(Assignment.AssignmentType.COURIER_SUPPLIER_DELIVERING);
            assignment.finalizeAssignment();
         }
      } else {
         this.switchNode(exitBranch);
      }
   }
}
