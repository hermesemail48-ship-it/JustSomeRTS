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

public class AIDeliverConstructionResource extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_RESOURCE_ON_GROUND = 1;
   private static final int PATHFIND_TO_BUILDING = 2;
   private static final int PICK_UP_RESOURCE_ON_GROUND = 3;
   private static final int PICK_UP_RESOURCE_IN_BUILDING = 4;
   private static final int LIFT_RESOURCE = 5;
   private static AIBase failBranch;
   private static AIBase exitBranch;

   public AIDeliverConstructionResource(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_DELIVER_CONSTRUCTION_RESOURCE;
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
         failBranch = this.thisMob.getAIDeliverRoadConstructionResource();
         exitBranch = this.thisMob.getAIDeliverConstructionResourceInHand();
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
         if (!job.canWorkerDeliverConstruction()) {
            this.switchNode(failBranch);
         } else {
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            int mobID = this.thisMob.getID();
            ArrayList<ResourceModule.ResourceType> canDeliverResources = new ArrayList<>();
            ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();

            for (ResourceModule.ResourceType type : job.getCanWorkerDeliverConstruction()) {
               for (ObjectBase o : objectList) {
                  if (!o.isPaused()
                     && o.getObjectFlags().canBuildingBeBuilt(type)
                     && this.thisMob.canReach(o, true)
                     && o.getAcceptBuildResourceCount(type, mobID) > 0) {
                     canDeliverResources.add(type);
                     break;
                  }
               }
            }

            if (canDeliverResources.size() == 0) {
               this.switchNode(failBranch);
            } else {
               HashMap<ResourceModule.ResourceType, ArrayList<ObjectBase>> validConstruction = new HashMap<>();
               boolean foundConstruction = false;

               for (ResourceModule.ResourceType t : canDeliverResources) {
                  validConstruction.put(t, new ArrayList<>());

                  for (ObjectBase ox : objectList) {
                     if (this.thisMob.canReach(ox, true)
                        && !ox.isPaused()
                        && ox.getObjectFlags().canBuildingBeBuilt(t)
                        && ox.getAcceptBuildResourceCount(t, mobID) > 0
                        && !validConstruction.get(t).contains(ox)) {
                        validConstruction.get(t).add(ox);
                        foundConstruction = true;
                     }
                  }
               }

               if (!foundConstruction) {
                  this.switchNode(failBranch);
               } else {
                  HashMap<ResourceModule.ResourceType, ArrayList<ObjectBase>> validStoresByTypeLowestPriority = AIUtilities.createLowestPriorityStorageListHashMap(
                     this.thisMob, canDeliverResources, objectList
                  );
                  HashMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> validResourcesOnGroundByType = AIUtilities.createAvailabeResourceList(
                     this.thisMob, canDeliverResources, true, this.thisMobFlags.getWorkNearbyResourcesOnGroundDistance()
                  );
                  Collections.shuffle(canDeliverResources);

                  for (ResourceModule.ResourceType deliveryType : canDeliverResources) {
                     for (ObjectBase deliveryBuilding : validConstruction.get(deliveryType)) {
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
                              assignment.setDeliveryObject(deliveryBuilding);
                              assignment.setPrimaryCoordinates(closestResourceAnywhere);
                              assignment.addPickupResources(pickupResource);
                              assignment.setAssignmentType(Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND);
                              assignment.finalizeAssignment();
                              this.requestPath(closestResourceAnywhere);
                              this.aiStep = 1;
                              return;
                           }

                           ObjectBase pickupBuilding = this.object.getBuildingAtLocation(closestResourceAnywhere);
                           if (pickupBuilding != null && validStoresByTypeLowestPriorityArray.contains(pickupBuilding)) {
                              OrderedPair pickupOutCoordinates = pickupBuilding.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
                              OrderedPair deliverOutCoordinates = deliveryBuilding.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
                              int carryMax = this.thisMob.getCarryMax();
                              int availableResources = pickupBuilding.getAvailableResourceCount(deliveryType, false);
                              int deliveryMax = deliveryBuilding.getAcceptBuildResourceCount(deliveryType, mobID);
                              int pickupAmount = availableResources > carryMax ? carryMax : availableResources;
                              if (pickupAmount > deliveryMax) {
                                 pickupAmount = deliveryMax;
                              }

                              assignment.setDeliveryObject(deliveryBuilding);
                              assignment.setSecondaryObject(pickupBuilding);
                              assignment.setPrimaryCoordinates(deliverOutCoordinates);
                              assignment.setSecondaryCoordinates(pickupOutCoordinates);

                              for (int i = 0; i < pickupAmount; i++) {
                                 assignment.addPickupResources(pickupBuilding.getAvailableResource(deliveryType));
                              }

                              assignment.setAssignmentType(Assignment.AssignmentType.DELIVERY);
                              assignment.finalizeAssignment();
                              this.requestPath(pickupOutCoordinates);
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

   private void pathfindToResourceOnGround() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getDeliveryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.deliverConstructionResource.pathfindToResourceOnGround"));
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
      ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
      if (deliveryObject != null && this.thisMob.canReach(deliveryObject, true) && this.resource.getResourceTypeOnGround(tileX, tileY) != null) {
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
      Text.setVariableText("buildingNameSource", this.thisMob.getAssignment().getSecondaryObjectName());
      Text.setVariableText("buildingNameDestination", this.thisMob.getAssignment().getDeliveryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.deliverConstructionResource.pathfindToBuilding"));
      this.runPath();
      if (this.pathFailed) {
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 4;
         }
      }
   }

   private void pickUpResourcesInBuilding() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase building = assignment.getSecondaryObject();
      if (building != null
         && building == this.object.getBuildingAtLocation(tileX, tileY)
         && building.getResourceCount(assignment.getFirstPickupResources().getType()) > 0) {
         ObjectBase deliveryObject = assignment.getDeliveryObject();
         if (deliveryObject != null && this.thisMob.canReach(deliveryObject, true)) {
            this.thisMob.setPositionPickup();
            if (this.thisMob.getSprite().isStopped()) {
               ResourceModule.ResourceType pickupType = assignment.getFirstPickupResources().getType();

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
                     && deliveryObject.getAcceptBuildResourceCount(pickupType, this.thisMob.getID()) > this.thisMob.getCarriedResourcesList().size()
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
      } else {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.aiStep = 0;
      }
   }

   private void liftResource() throws SlickException {
      ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
      if (deliveryObject != null && this.thisMob.canReach(deliveryObject, true)) {
         this.thisMob.setPositionLift();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.switchNode(exitBranch);
            Assignment assignment = this.thisMob.getAssignment();
            assignment.setDeliveryObject(deliveryObject);
            assignment.setAssignmentType(Assignment.AssignmentType.DELIVERY);
            assignment.finalizeAssignment();
         }
      } else {
         this.switchNode(exitBranch);
      }
   }
}
