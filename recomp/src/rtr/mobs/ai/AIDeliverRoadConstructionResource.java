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
import rtr.road.RoadModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;

public class AIDeliverRoadConstructionResource extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_RESOURCE_ON_GROUND = 1;
   private static final int PATHFIND_TO_BUILDING = 2;
   private static final int PICK_UP_RESOURCE_ON_GROUND = 3;
   private static final int PICK_UP_RESOURCE_IN_BUILDING = 4;
   private static final int LIFT_RESOURCE = 5;
   private static AIBase failBranch;
   private static AIBase exitBranch;

   public AIDeliverRoadConstructionResource(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_DELIVER_ROAD_CONSTRUCTION_RESOURCE;
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
         failBranch = this.thisMob.getAIDeliverCourierResource();
         exitBranch = this.thisMob.getAIDeliverRoadConstructionResourceInHand();
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
         if (!job.canWorkerDeliverRoadConstruction()) {
            this.switchNode(failBranch);
         } else {
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            HashMap<OrderedPair, RoadModule.RoadType> roadSelectionList = this.workSelection.getRoadSelectionMapArray();
            ArrayList<OrderedPair> roadSelectionListFiltered = new ArrayList<>();
            roadSelectionListFiltered.addAll(roadSelectionList.keySet());

            for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
               if (m.getAssignment() != null
                  && m.getMobJobType().getJob().canWorkerDeliverRoadConstruction()
                  && m.getAssignment().getSecondaryCoordinates() != null) {
                  roadSelectionListFiltered.remove(m.getAssignment().getSecondaryCoordinates());
               }
            }

            if (roadSelectionListFiltered.size() <= 0) {
               this.switchNode(failBranch);
            } else {
               ArrayList<OrderedPair> validRoads = new ArrayList<>();

               for (OrderedPair o : roadSelectionListFiltered) {
                  if ((!this.thisMob.getMobFlags().canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isTileThreatened(o))
                     && this.thisMob.canReach(o, true)) {
                     for (ResourceModule.ResourceType t : job.getCanWorkerDeliverRoadConstruction()) {
                        if (this.road.getNextRequiredResource(o.getX(), o.getY()) == t) {
                           ResourceBase resourceOnGround = this.resource.getResourceOnGround(o.getX(), o.getY());
                           if (resourceOnGround == null) {
                              validRoads.add(o);
                           }
                           break;
                        }
                     }
                  }
               }

               if (validRoads.size() == 0) {
                  this.switchNode(failBranch);
               } else {
                  EnumMap<ResourceModule.ResourceType, ArrayList<OrderedPair>> sortedValidRoads = new EnumMap<>(ResourceModule.ResourceType.class);

                  for (OrderedPair ox : validRoads) {
                     ResourceModule.ResourceType tx = this.road.getNextRequiredResource(ox.getX(), ox.getY());
                     if (sortedValidRoads.get(tx) == null) {
                        sortedValidRoads.put(tx, new ArrayList<>());
                     }

                     sortedValidRoads.get(tx).add(ox);
                  }

                  ArrayList<ResourceModule.ResourceType> canDeliverResources = new ArrayList<>();

                  for (ResourceModule.ResourceType tx : sortedValidRoads.keySet()) {
                     if (sortedValidRoads.size() > 0) {
                        canDeliverResources.add(tx);
                     }
                  }

                  ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
                  HashMap<ResourceModule.ResourceType, ArrayList<ObjectBase>> validStoresByTypeLowestPriority = AIUtilities.createLowestPriorityStorageListHashMap(
                     this.thisMob, canDeliverResources, objectList
                  );
                  HashMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> validResourcesOnGroundByType = AIUtilities.createAvailabeResourceList(
                     this.thisMob, canDeliverResources, true, this.thisMobFlags.getWorkNearbyResourcesOnGroundDistance()
                  );
                  Collections.shuffle(canDeliverResources);

                  for (ResourceModule.ResourceType deliveryType : canDeliverResources) {
                     ArrayList<OrderedPair> validRoadsArray = sortedValidRoads.get(deliveryType);
                     OrderedPair deliveryOut = this.pathFinder
                        .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), validRoadsArray, false, true, this.thisMobFlags.getBlockMapGroup());
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
                           assignment.setPrimaryCoordinates(closestResourceAnywhere);
                           assignment.setSecondaryCoordinates(deliveryOut);
                           assignment.addPickupResources(this.resource.getResourceOnGround(closestResourceAnywhere.getX(), closestResourceAnywhere.getY()));
                           assignment.setAssignmentType(Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND);
                           assignment.finalizeAssignment();
                           this.requestPath(closestResourceAnywhere);
                           this.aiStep = 1;
                           return;
                        }

                        ObjectBase pickupBuilding = this.object.getBuildingAtLocation(closestResourceAnywhere);
                        if (pickupBuilding != null && validStoresByTypeLowestPriorityArray.contains(pickupBuilding)) {
                           OrderedPair pickupOutCoordinates = pickupBuilding.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
                           assignment.setSecondaryObject(pickupBuilding);
                           assignment.setPrimaryCoordinates(pickupOutCoordinates);
                           assignment.setSecondaryCoordinates(deliveryOut);
                           assignment.addPickupResources(pickupBuilding.getAvailableResource(deliveryType));
                           assignment.setPickupResourceType(assignment.getFirstPickupResources().getType());
                           assignment.setAssignmentType(Assignment.AssignmentType.DELIVERY);
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

   private void pathfindToResourceOnGround() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.deliverRoadConstructionResource.pathfindToResourceOnGround"));
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
      OrderedPair deliveryCoordinates = this.thisMob.getAssignment().getSecondaryCoordinates();
      if (this.thisMob.canReach(deliveryCoordinates, true) && this.resource.getResourceTypeOnGround(tileX, tileY) != null) {
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
         this.thisMob.setPositionFace();
         this.aiStep = 0;
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getSecondaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.deliverRoadConstructionResource.pathfindToBuilding"));
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
      ObjectBase building = assignment.getSecondaryObject();
      OrderedPair deliveryCoordinates = assignment.getSecondaryCoordinates();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      if (building != null
         && deliveryCoordinates != null
         && this.thisMob.canReach(deliveryCoordinates, true)
         && building == this.object.getBuildingAtLocation(tileX, tileY)
         && building.getResourceCount(assignment.getPickupResourceType()) > 0) {
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

            for (int totalNeededCount = AIUtilities.getTotalNeededRoadResourcesCount(this.thisMob);
               this.thisMob.canCarryMore() && building.getAvailableResourceCount(pickupType, false) > 0 && totalNeededCount > 0;
               totalNeededCount--
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
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.aiStep = 0;
      }
   }

   private void liftResource() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      OrderedPair deliveryCoordinates = assignment.getSecondaryCoordinates();
      if (deliveryCoordinates != null && this.thisMob.canReach(deliveryCoordinates, true)) {
         this.thisMob.setPositionLift();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.switchNode(exitBranch);
            assignment.setSecondaryCoordinates(deliveryCoordinates);
            assignment.setAssignmentType(Assignment.AssignmentType.DELIVERY);
            assignment.finalizeAssignment();
         }
      } else {
         this.switchNode(exitBranch);
      }
   }
}
