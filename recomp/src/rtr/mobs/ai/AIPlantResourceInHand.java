package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.resources.FarmlandBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIPlantResourceInHand extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_BUILDING = 1;
   private static final int PLANT_CROP = 2;
   private static final int DROP_OFF_LIFT = 3;
   private static final int PATHFIND_TO_EXTRA_RESOURCE = 4;
   private static final int PICKUP_EXTRA_RESOURCE = 5;
   private static final int LIFT_EXTRA_RESOURCE = 6;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIPlantResourceInHand(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_PLANT_RESOURCE_IN_HAND;
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
         resetBranch = this.thisMob.getAIStart();
         failBranch = this.thisMob.getAIDeliverConstructionResourceInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToBuilding();
               break;
            case 2:
               this.plantCrop();
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
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         OrderedPair plantingCoordinates = assignment.getSecondaryCoordinates();
         if (plantingCoordinates == null) {
            MobJobBase job = jobType.getJob();
            ResourceModule.ResourceType resourceTypeCarried = this.thisMob.getCarriedResourcesListType();
            if (!job.canWorkerPlant(resourceTypeCarried)) {
               this.switchNode(failBranch);
               return;
            }

            if (this.weather.getCurrentTemp() <= this.weather.getFreezePoint()) {
               this.switchNode(failBranch);
               return;
            }

            ArrayList<FarmlandBase> farmland = this.resource.getFarmlandArray();
            if (farmland.size() == 0) {
               this.switchNode(failBranch);
               return;
            }

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
               return;
            }

            ArrayList<OrderedPair> farmlandCoordinatesList = new ArrayList<>();

            for (FarmlandBase fx : validFarmland) {
               farmlandCoordinatesList.add(OrderedPair.getOrderedPair(fx.getTileX(), fx.getTileY()));
            }

            plantingCoordinates = this.pathFinder
               .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), farmlandCoordinatesList, false, true, this.thisMobFlags.getBlockMapGroup());
         }

         assignment.reset();
         assignment.setSecondaryCoordinates(plantingCoordinates);
         assignment.setAssignmentType(Assignment.AssignmentType.PLANTING);
         assignment.finalizeAssignment();
         this.requestPath(plantingCoordinates);
         this.aiStep = 1;
      }
   }

   private void pathfindToBuilding() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.plantResourceInHand.pathfindToBuilding"));
      this.runPath();
      if (this.pathFailed) {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      } else if (this.pathCompleted) {
         this.aiStep = 2;
      } else if (this.searchForMore()) {
         this.aiStep = 4;
      }
   }

   private void plantCrop() throws SlickException {
      OrderedPair coordinates = this.thisMob.getAssignment().getSecondaryCoordinates();
      int workX = coordinates.getX();
      int workY = coordinates.getY();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      if (workX == tileX && workY == tileY && this.resource.isCrop(this.thisMob.getAssignment())) {
         this.thisMob.setPositionDrop();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.awardXP(Utilities.randomInt(10) + 5);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.01, GodModule.GodXPType.PLANTING_CROPS);
            }

            this.resource.plantCrop(this.thisMob.getAssignment(), this.thisMob.removeFirstCarriedResource());
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 3;
         }
      } else {
         if (this.thisMob.hasCarriedResources()) {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.thisMob.getAssignment().reset();
            this.aiStep = 0;
         } else {
            this.switchNode(failBranch);
         }
      }
   }

   private void dropOffLift() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         if (this.thisMob.hasCarriedResources()) {
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
      this.thisMob.setCurrentTask(Text.getText("aiTask.plantResourceInHand.pathfindToExtraResource"));
      this.runPath();
      OrderedPair o = this.thisMob.getAssignment().getPrimaryCoordinates();
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
         OrderedPair farmTile = this.thisMob.getAssignment().getSecondaryCoordinates();
         if (farmTile != null && this.thisMob.canReach(farmTile, true)) {
            this.requestPath(farmTile);
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
      this.thisMob.setPositionDrop();
      if (this.thisMob.getSprite().isStopped()) {
         Assignment assignment = this.thisMob.getAssignment();
         ResourceBase resourceOnGround = this.resource.getResourceOnGround(tileX, tileY);
         OrderedPair plantingCoordinates = this.thisMob.getAssignment().getSecondaryCoordinates();
         if (resourceOnGround != null
            && assignment.containsPickupResources(resourceOnGround)
            && plantingCoordinates != null
            && this.thisMob.canReach(plantingCoordinates, true)) {
            this.thisMob.addCarriedResource(resourceOnGround);
            this.thisMob.getAssignment().removePickupResources(resourceOnGround);
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.aiStep = 6;
         } else {
            this.thisMob.getAssignment().reset();
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.aiStep = 0;
         }
      }
   }

   private void liftExtraResource() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.requestPath(this.thisMob.getAssignment().getSecondaryCoordinates());
         this.aiStep = 1;
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
                        assignment.setPrimaryCoordinates(r.getTileX(), r.getTileY());
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
