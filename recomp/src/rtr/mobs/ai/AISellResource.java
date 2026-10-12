package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;

public class AISellResource extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_RESOURCE_ON_GROUND = 1;
   private static final int PATHFIND_TO_BUILDING = 2;
   private static final int PICK_UP_RESOURCE_ON_GROUND = 3;
   private static final int PICK_UP_RESOURCE_IN_BUILDING = 4;
   private static final int LIFT_RESOURCE = 5;
   private static AIBase failBranch;
   private static AIBase exitBranch;

   public AISellResource(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_SELL_RESOURCE;
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
         failBranch = this.thisMob.getAIStoreResource();
         exitBranch = this.thisMob.getAISellResourceInHand();
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
         if (!job.canWorkerTrade()) {
            this.switchNode(failBranch);
         } else {
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            ArrayList<MobBase> allCatjeet = this.mob.getMobsByAssignment(Assignment.AssignmentType.CATJEET_SELLING_WARES, false);
            ArrayList<MobBase> allProvisionersSelling = this.mob.getMobsByAssignment(Assignment.AssignmentType.SELL_RESOURCE, false);
            ArrayList<MobBase> validCatjeet = new ArrayList<>();

            label108:
            for (MobBase m : allCatjeet) {
               if (this.thisMob.canReach(m, true)) {
                  for (MobBase m2 : allProvisionersSelling) {
                     if (m2.getAssignment().getPrimaryMob() == m) {
                        continue label108;
                     }
                  }

                  ObjectBase marketplace = this.object.getBuildingAtLocation(m.getTileX(), m.getTileY());
                  if (marketplace != null && marketplace.getObjectFlags().getBaseType() == MapTilesLoader.TileSet.MARKETPLACE) {
                     validCatjeet.add(m);
                  }
               }
            }

            if (validCatjeet.size() == 0) {
               this.switchNode(failBranch);
            } else {
               ArrayList<ResourceModule.ResourceType> canSellResources = new ArrayList<>();

               for (ResourceModule.ResourceType type : job.getCanWorkerTrade()) {
                  if (this.trade.getAcceptSellCount(type, this.thisMob.getID()) > 0) {
                     canSellResources.add(type);
                  }
               }

               if (canSellResources.size() == 0) {
                  this.switchNode(failBranch);
               } else {
                  ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
                  HashMap<ResourceModule.ResourceType, ArrayList<ObjectBase>> validStoresByTypeLowestPriority = AIUtilities.createLowestPriorityStorageListHashMap(
                     this.thisMob, canSellResources, objectList
                  );
                  HashMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> validResourcesOnGroundByType = AIUtilities.createAvailabeResourceList(
                     this.thisMob, canSellResources, true, this.thisMobFlags.getWorkNearbyResourcesOnGroundDistance()
                  );
                  OrderedPair deliveryOut = this.pathFinder
                     .searchForMob(this.thisMob.getTileX(), this.thisMob.getTileY(), validCatjeet, false, true, this.thisMobFlags.getBlockMapGroup());
                  if (deliveryOut == null) {
                     this.switchNode(failBranch);
                  } else {
                     Collections.shuffle(canSellResources);

                     for (ResourceModule.ResourceType deliveryType : canSellResources) {
                        ArrayList<ObjectBase> validStoresByTypeLowestPriorityArray = validStoresByTypeLowestPriority.get(deliveryType);
                        ArrayList<ResourceBase> resourcesOnGroundByTypeArray = validResourcesOnGroundByType.get(deliveryType);
                        if (validStoresByTypeLowestPriorityArray.size() != 0 || resourcesOnGroundByTypeArray.size() != 0) {
                           ArrayList<OrderedPair> coordinatesList = AIUtilities.createAvailabeResourceCoordinatesList(
                              this.thisMob, validStoresByTypeLowestPriorityArray, resourcesOnGroundByTypeArray
                           );
                           OrderedPair closestResourceAnywhere = this.pathFinder
                              .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), coordinatesList, false, true, this.thisMobFlags.getBlockMapGroup());
                           ResourceBase pickupResource = this.resource.getResourceOnGround(closestResourceAnywhere);
                           if (pickupResource != null && pickupResource.getType() == deliveryType && resourcesOnGroundByTypeArray.contains(pickupResource)) {
                              assignment.setPrimaryMob(this.pathFinder.mobFetch(validCatjeet, deliveryOut.getX(), deliveryOut.getY()));
                              assignment.setPrimaryCoordinates(closestResourceAnywhere);
                              assignment.addPickupResources(pickupResource);
                              assignment.setPickupResourceType(deliveryType);
                              assignment.setAssignmentType(Assignment.AssignmentType.SELL_RESOURCE);
                              assignment.finalizeAssignment();
                              this.requestPath(closestResourceAnywhere);
                              this.aiStep = 1;
                              return;
                           }

                           ObjectBase pickupBuilding = this.object.getBuildingAtLocation(closestResourceAnywhere);
                           if (pickupBuilding != null && validStoresByTypeLowestPriorityArray.contains(pickupBuilding)) {
                              OrderedPair pickupOutCoordinates = pickupBuilding.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
                              int carryMax = this.thisMob.getCarryMax();
                              int availableResources = pickupBuilding.getAvailableResourceCount(deliveryType, false);
                              int deliveryMax = this.trade.getAcceptSellCount(deliveryType, this.thisMob.getID());
                              int pickupAmount = availableResources > carryMax ? carryMax : availableResources;
                              if (pickupAmount > deliveryMax) {
                                 pickupAmount = deliveryMax;
                              }

                              assignment.setPrimaryMob(this.pathFinder.mobFetch(validCatjeet, deliveryOut.getX(), deliveryOut.getY()));
                              assignment.setPrimaryObject(pickupBuilding);
                              assignment.setPrimaryCoordinates(pickupOutCoordinates);

                              for (int i = 0; i < pickupAmount; i++) {
                                 assignment.addPickupResources(pickupBuilding.getAvailableResource(deliveryType));
                              }

                              assignment.setPickupResourceType(deliveryType);
                              assignment.setAssignmentType(Assignment.AssignmentType.SELL_RESOURCE);
                              assignment.finalizeAssignment();
                              this.requestPath(pickupOutCoordinates);
                              this.aiStep = 2;
                              return;
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
      this.thisMob.setCurrentTask(Text.getText("aiTask.sellResource.pathfindToResourceOnGround"));
      this.runPath();
      OrderedPair o = this.thisMob.getAssignment().getPrimaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (!this.pathFailed
         && r != null
         && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
         && !this.road.isResourceNeededForRoad(r)
         && this.thisMob.getAssignment().containsPickupResources(r)) {
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
      MobBase deliveryCatjeet = this.thisMob.getAssignment().getPrimaryMob();
      if (deliveryCatjeet != null && this.thisMob.canReach(deliveryCatjeet, true) && this.resource.getResourceTypeOnGround(tileX, tileY) != null) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.addCarriedResource(this.resource.getResourceOnGround(tileX, tileY));
            this.thisMob.getAssignment().removeAndGetFirstPickupResources();
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 5;
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObject().getFormattedName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.sellResource.pathfindToBuilding"));
      this.runPath();
      MobBase deliveryCatjeet = this.thisMob.getAssignment().getPrimaryMob();
      ResourceModule.ResourceType resourceTypeSelling = this.thisMob.getAssignment().getPickupResourceType();
      if (!this.pathFailed
         && deliveryCatjeet != null
         && deliveryCatjeet.getAssignment().getAssignmentType() == Assignment.AssignmentType.CATJEET_SELLING_WARES
         && this.thisMob.canReach(deliveryCatjeet, true)
         && this.trade.isModeSell(resourceTypeSelling)
         && this.trade.getTradeAmount(resourceTypeSelling) > 0) {
         if (this.pathCompleted) {
            this.aiStep = 4;
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void pickUpResourcesInBuilding() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase building = this.thisMob.getAssignment().getPrimaryObject();
      MobBase deliveryCatjeet = this.thisMob.getAssignment().getPrimaryMob();
      if (building != null
         && deliveryCatjeet != null
         && this.thisMob.canReach(deliveryCatjeet, true)
         && building == this.object.getBuildingAtLocation(tileX, tileY)
         && building.getResourceCount(this.thisMob.getAssignment().getFirstPickupResources().getType()) > 0) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            Assignment assignment = this.thisMob.getAssignment();
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
               this.thisMob.addCarriedResource(building.takeStores(pickupType), true);
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
         this.aiStep = 0;
      }
   }

   private void liftResource() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      MobBase deliveryCatjeet = this.thisMob.getAssignment().getPrimaryMob();
      if (deliveryCatjeet != null && this.thisMob.canReach(deliveryCatjeet, true)) {
         this.thisMob.setPositionLift();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.switchNode(exitBranch);
            assignment.setPrimaryMob(deliveryCatjeet);
            assignment.setAssignmentType(Assignment.AssignmentType.SELL_RESOURCE);
            assignment.finalizeAssignment();
         }
      } else {
         this.switchNode(exitBranch);
      }
   }
}
