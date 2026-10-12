package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.resources.FarmlandBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;

public class AIPlantResource extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_RESOURCE_ON_GROUND = 1;
   private static final int PATHFIND_TO_BUILDING = 2;
   private static final int PICK_UP_RESOURCE_ON_GROUND = 3;
   private static final int PICK_UP_RESOURCE_IN_BUILDING = 4;
   private static final int LIFT_RESOURCE = 5;
   private static AIBase failBranch;
   private static AIBase exitBranch;

   public AIPlantResource(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_PLANT_RESOURCE;
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
         failBranch = this.thisMob.getAIRefineWaterBottle();
         exitBranch = this.thisMob.getAIPlantResourceInHand();
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
         if (!job.canWorkerPlant()) {
            this.switchNode(failBranch);
         } else {
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
               this.switchNode(failBranch);
            } else {
               ArrayList<FarmlandBase> farmland = this.resource.getFarmlandArray();
               if (farmland.size() == 0) {
                  this.switchNode(failBranch);
               } else {
                  ArrayList<FarmlandBase> validFarmland = new ArrayList<>();

                  for (FarmlandBase f : farmland) {
                     ObjectBase farm = this.object.getBuildingAtLocation(f.getTileX(), f.getTileY());
                     if (!f.isPlanted()
                        && !farm.isPaused()
                        && this.thisMob.canReach(f.getTileX(), f.getTileY(), true)
                        && !this.mob.isWorkAssigned(f.getTileX(), f.getTileY(), Assignment.AssignmentType.PLANTING)) {
                        validFarmland.add(f);
                     }
                  }

                  if (validFarmland.size() == 0) {
                     this.switchNode(failBranch);
                  } else {
                     ArrayList<ResourceModule.ResourceType> canPlantResources = new ArrayList<>();
                     canPlantResources.add(ResourceModule.ResourceType.RAW_VEGETABLE);
                     ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
                     HashMap<ResourceModule.ResourceType, ArrayList<ObjectBase>> validStoresByTypeLowestPriority = AIUtilities.createLowestPriorityStorageListHashMap(
                        this.thisMob, canPlantResources, objectList
                     );
                     HashMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> validResourcesOnGroundByType = AIUtilities.createAvailabeResourceList(
                        this.thisMob, canPlantResources, true, this.thisMobFlags.getWorkNearbyResourcesOnGroundDistance()
                     );
                     ArrayList<ObjectBase> validStoresByTypeLowestPriorityArray = validStoresByTypeLowestPriority.get(ResourceModule.ResourceType.RAW_VEGETABLE);
                     ArrayList<ResourceBase> resourcesOnGroundByTypeArray = validResourcesOnGroundByType.get(ResourceModule.ResourceType.RAW_VEGETABLE);
                     if (validStoresByTypeLowestPriorityArray.size() == 0 && resourcesOnGroundByTypeArray.size() == 0) {
                        this.switchNode(failBranch);
                     } else {
                        ArrayList<OrderedPair> coordinatesList = AIUtilities.createAvailabeResourceCoordinatesList(
                           this.thisMob, validStoresByTypeLowestPriorityArray, resourcesOnGroundByTypeArray
                        );
                        OrderedPair closestResourceAnywhere = this.pathFinder
                           .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), coordinatesList, false, true, this.thisMobFlags.getBlockMapGroup());
                        ArrayList<OrderedPair> farmlandCoordinatesList = new ArrayList<>();

                        for (FarmlandBase fx : validFarmland) {
                           farmlandCoordinatesList.add(OrderedPair.getOrderedPair(fx.getTileX(), fx.getTileY()));
                        }

                        OrderedPair plantingCoordinates = this.pathFinder
                           .searchFor(
                              closestResourceAnywhere.getX(),
                              closestResourceAnywhere.getY(),
                              farmlandCoordinatesList,
                              false,
                              true,
                              this.thisMobFlags.getBlockMapGroup()
                           );
                        ResourceBase pickupResource = this.resource.getResourceOnGround(closestResourceAnywhere);
                        if (pickupResource != null
                           && pickupResource.getType() == ResourceModule.ResourceType.RAW_VEGETABLE
                           && resourcesOnGroundByTypeArray.contains(pickupResource)) {
                           assignment.setPrimaryCoordinates(closestResourceAnywhere.getX(), closestResourceAnywhere.getY());
                           assignment.setSecondaryCoordinates(plantingCoordinates);
                           assignment.addPickupResources(pickupResource);
                           assignment.setAssignmentType(Assignment.AssignmentType.PLANTING);
                           assignment.finalizeAssignment();
                           this.requestPath(closestResourceAnywhere);
                           this.aiStep = 1;
                        } else {
                           ObjectBase pickupBuilding = this.object.getBuildingAtLocation(closestResourceAnywhere);
                           if (pickupBuilding != null && validStoresByTypeLowestPriorityArray.contains(pickupBuilding)) {
                              OrderedPair pickupOutCoordinates = pickupBuilding.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
                              assignment.setSecondaryObject(pickupBuilding);
                              assignment.setPrimaryCoordinates(pickupOutCoordinates);
                              assignment.setSecondaryCoordinates(plantingCoordinates);
                              assignment.addPickupResources(pickupBuilding.getAvailableResource(ResourceModule.ResourceType.RAW_VEGETABLE));
                              assignment.setAssignmentType(Assignment.AssignmentType.PLANTING);
                              assignment.finalizeAssignment();
                              this.requestPath(pickupOutCoordinates);
                              this.aiStep = 2;
                           } else {
                              this.switchNode(failBranch);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void pathfindToResourceOnGround() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.plantResource.pathfindToResourceOnGround"));
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
      this.thisMob.setCurrentTask(Text.getText("aiTask.plantResource.pathfindToBuilding"));
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
      OrderedPair plantingCoordinates = assignment.getSecondaryCoordinates();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      if (building != null
         && plantingCoordinates != null
         && this.thisMob.canReach(plantingCoordinates, true)
         && building == this.object.getBuildingAtLocation(tileX, tileY)
         && building.getResourceCount(this.thisMob.getAssignment().getFirstPickupResources().getType()) > 0) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            ResourceBase deliveryResource = this.thisMob.getAssignment().removeAndGetFirstPickupResources();
            ResourceBase r = building.takeStores(deliveryResource);
            if (r == null) {
               r = building.takeStores(deliveryResource.getType());
            }

            if (r == null) {
               this.aiStep = 0;
            } else {
               this.thisMob.addCarriedResource(r, true);

               while (this.thisMob.canCarryMore() && building.getAvailableResourceCount(r.getType(), false) > 0) {
                  r = building.takeStores(deliveryResource.getType());
                  this.thisMob.addCarriedResource(r, true);
               }

               this.thisMob.getSprite().restart();
               this.thisMob.setPositionLift();
               this.aiStep = 5;
            }
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void liftResource() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      OrderedPair plantingCoordinates = assignment.getSecondaryCoordinates();
      if (plantingCoordinates != null && this.thisMob.canReach(plantingCoordinates, true)) {
         this.thisMob.setPositionLift();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.switchNode(exitBranch);
            assignment.setSecondaryCoordinates(plantingCoordinates);
            assignment.setAssignmentType(Assignment.AssignmentType.PLANTING);
            assignment.finalizeAssignment();
         }
      } else {
         this.switchNode(exitBranch);
      }
   }
}
