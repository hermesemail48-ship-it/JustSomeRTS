package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.road.RoadModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIDeliverRoadConstructionResourceInHand extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_ROAD = 1;
   private static final int DROP_OFF_RESOURCE = 2;
   private static final int DROP_OFF_LIFT = 3;
   private static final int PATHFIND_TO_EXTRA_RESOURCE = 4;
   private static final int PICKUP_EXTRA_RESOURCE = 5;
   private static final int LIFT_EXTRA_RESOURCE = 6;
   private static AIBase failWithResourceBranch;
   private static AIBase nextBranch;

   public AIDeliverRoadConstructionResourceInHand(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_DELIVER_ROAD_RESOURCE_IN_HAND;
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
         failWithResourceBranch = this.thisMob.getAIDeliverCourierResourceInHand();
         nextBranch = this.thisMob.getAIRoadWork();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToRoad();
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
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failWithResourceBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         OrderedPair deliveryOut = assignment.getSecondaryCoordinates();
         if (deliveryOut == null) {
            MobJobBase job = jobType.getJob();
            ResourceModule.ResourceType resourceTypeCarried = this.thisMob.getCarriedResourcesListType();
            if (!job.canWorkerDeliverRoadConstruction(resourceTypeCarried)) {
               this.switchNode(failWithResourceBranch);
               return;
            }

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
               this.switchNode(failWithResourceBranch);
               return;
            }

            ArrayList<OrderedPair> validRoads = new ArrayList<>();

            for (OrderedPair o : roadSelectionListFiltered) {
               if ((!this.thisMob.getMobFlags().canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isTileThreatened(o))
                  && this.road.getNextRequiredResource(o.getX(), o.getY()) == resourceTypeCarried
                  && this.thisMob.canReach(o, true)) {
                  ResourceBase resourceOnGround = this.resource.getResourceOnGround(o.getX(), o.getY());
                  if (resourceOnGround == null) {
                     validRoads.add(o);
                  }
               }
            }

            if (validRoads.size() == 0) {
               this.switchNode(failWithResourceBranch);
               return;
            }

            deliveryOut = this.pathFinder
               .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), validRoads, false, true, this.thisMobFlags.getBlockMapGroup());
         }

         assignment.reset();
         assignment.setSecondaryCoordinates(deliveryOut);
         assignment.setAssignmentType(Assignment.AssignmentType.DELIVERY);
         assignment.finalizeAssignment();
         this.requestPath(deliveryOut);
         this.aiStep = 1;
      }
   }

   private void pathfindToRoad() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.deliverRoadConstructionResourceInHand.pathfindToRoad"));
      this.runPath();
      if (this.thisMob.getMobFlags().canIgnoreThreatenedTiles()
         && this.map.getMapThreatMap().isTileThreatened(this.thisMob.getAssignment().getSecondaryCoordinates())) {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      } else if (this.pathCompleted) {
         this.aiStep = 2;
      } else if (this.pathFailed) {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      } else if (this.searchForMore()) {
         this.aiStep = 4;
      }
   }

   private void dropOffResource() throws SlickException {
      OrderedPair coordinates = this.thisMob.getAssignment().getSecondaryCoordinates();
      int workX = coordinates.getX();
      int workY = coordinates.getY();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      if (workX == tileX && workY == tileY && this.workSelection.getRoadSelectionMapArray().containsKey(coordinates)) {
         this.thisMob.setPositionDrop();
         if (!this.thisMob.getSprite().isStopped()) {
            return;
         }

         this.thisMob.awardXP(Utilities.randomInt(5) + 5);
         if (this.thisMobFlags.getAllowGodExperience()) {
            this.god.increaseGodXP(0.01, GodModule.GodXPType.DELIVERING_RESOURCES_TO_ROAD);
         }

         ResourceBase resourceOnGroundCheck = this.resource.getResourceOnGround(tileX, tileY);
         if (resourceOnGroundCheck != null) {
            OrderedPair o = this.resource.findOpenSpace(tileX, tileY, this.thisMobFlags.getBlockMapGroup());
            this.resource.moveResourcesOnGround(tileX, tileY, o.getX(), o.getY(), resourceOnGroundCheck);
         }

         this.resource.setResourcesOnGround(tileX, tileY, this.thisMob.removeFirstCarriedResource());
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionLift();
         this.aiStep = 3;
      } else {
         this.switchNode(failWithResourceBranch);
      }
   }

   private void dropOffLift() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         if (this.thisMob.hasCarriedResources()) {
            this.thisMob.getAssignment().reset();
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 0;
         } else {
            this.switchNode(nextBranch);
         }
      }
   }

   private void pathfindToExtraResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.deliverRoadConstructionResourceInHand.pathfindToExtraResource"));
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
         if (this.thisMob.canReach(this.thisMob.getAssignment().getSecondaryCoordinates(), true)) {
            this.requestPath(this.thisMob.getAssignment().getSecondaryCoordinates());
            this.aiStep = 1;
         } else {
            this.thisMob.getAssignment().reset();
            this.aiStep = 0;
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
         if (resourceOnGround != null && assignment.containsPickupResources(resourceOnGround)) {
            this.thisMob.addCarriedResource(resourceOnGround);
            this.thisMob.getAssignment().removePickupResources(resourceOnGround);
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 6;
         } else {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.requestPath(this.thisMob.getAssignment().getSecondaryCoordinates());
            this.aiStep = 1;
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
            if (AIUtilities.getTotalNeededRoadResourcesCount(this.thisMob) <= 0) {
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
}
