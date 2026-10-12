package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIDeliverCourierResourceInHand extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_COURIER = 1;
   private static final int DELIVER_RESOURCE = 2;
   private static final int PATHFIND_TO_EXTRA_RESOURCE = 3;
   private static final int PICKUP_EXTRA_RESOURCE = 4;
   private static final int LIFT_EXTRA_RESOURCE = 5;
   private static AIBase failBranch;

   public AIDeliverCourierResourceInHand(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_DELIVER_COURIER_RESOURCE_IN_HAND;
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
         failBranch = this.thisMob.getAICaptureSoulInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToCourier();
               break;
            case 2:
               this.deliverResources();
               break;
            case 3:
               this.pathfindToExtraResource();
               break;
            case 4:
               this.pickupExtraResource();
               break;
            case 5:
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
         MobBase courierOut = assignment.getPrimaryMob();
         String mapName = assignment.getRegionName();
         String mapFileName = assignment.getRegionFileName();
         if (courierOut == null) {
            MobJobBase job = jobType.getJob();
            ResourceModule.ResourceType resourceTypeCarried = this.thisMob.getCarriedResourcesListType();
            if (!job.canWorkerTrade(resourceTypeCarried)) {
               this.switchNode(failBranch);
               return;
            }

            ArrayList<MobBase> allCouriers = this.mob.getMobsByAssignment(Assignment.AssignmentType.CATJEET_SELLING_WARES, false);
            ArrayList<MobBase> allCouriersEnRoute = this.mob.getMobsByAssignment(Assignment.AssignmentType.SELL_RESOURCE, false);
            ArrayList<MobBase> validCourier = new ArrayList<>();

            label50:
            for (MobBase m : allCouriers) {
               if (this.thisMob.canReach(m, true)) {
                  for (MobBase m2 : allCouriersEnRoute) {
                     if (m2.getAssignment().getPrimaryMob() == m) {
                        continue label50;
                     }
                  }

                  ObjectBase courierStation = this.object.getBuildingAtLocation(m.getTileX(), m.getTileY());
                  if (courierStation != null && courierStation.getObjectFlags().getBaseType() == MapTilesLoader.TileSet.MARKETPLACE) {
                     validCourier.add(m);
                  }
               }
            }

            if (validCourier.size() == 0) {
               this.switchNode(failBranch);
               return;
            }

            if (this.trade.getAcceptSellCount(resourceTypeCarried, this.thisMob.getID()) < this.thisMob.getCarriedResourcesList().size()) {
               this.switchNode(failBranch);
               return;
            }

            OrderedPair nearestCourierCoordinates = this.pathFinder
               .searchForMob(this.thisMob.getTileX(), this.thisMob.getTileY(), validCourier, false, true, this.thisMobFlags.getBlockMapGroup());
            courierOut = this.pathFinder.mobFetch(validCourier, nearestCourierCoordinates.getX(), nearestCourierCoordinates.getY());
         }

         assignment.reset();
         assignment.setPrimaryMob(courierOut);
         assignment.setPrimaryCoordinates(courierOut.getTileX(), courierOut.getTileY());
         assignment.setPickupResourceType(this.thisMob.getCarriedResourcesListType());
         assignment.setRegionName(mapName);
         assignment.setRegionFileName(mapFileName);
         assignment.setAssignmentType(Assignment.AssignmentType.COURIER_SUPPLIER_DELIVERING);
         assignment.finalizeAssignment();
         this.requestPath(courierOut.getTileX(), courierOut.getTileY());
         this.aiStep = 1;
      }
   }

   private void pathfindToCourier() throws SlickException {
      MobBase deliveryCourier = this.thisMob.getAssignment().getPrimaryMob();
      Text.setVariableText("regionName", this.thisMob.getAssignment().getRegionName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.deliverCourierResourceInHand.pathfindToCourier"));
      this.followMob(deliveryCourier);
      if (this.pathCompleted) {
         this.aiStep = 2;
      } else if (this.pathFailed
         || deliveryCourier == null
         || deliveryCourier.getAssignment().getAssignmentType() != Assignment.AssignmentType.COURIER_PARKED
         || !this.thisMob.canReach(deliveryCourier, true)
         || this.migration.getDailySendAmountRemaining(this.thisMob.getAssignment().getRegionFileName(), this.thisMob.getAssignment().getPickupResourceType())
            < 0) {
         this.switchNode(failBranch);
      } else if (this.searchForMore()) {
         this.aiStep = 3;
      }
   }

   private void deliverResources() throws SlickException {
      MobBase courier = this.thisMob.getAssignment().getPrimaryMob();
      String mapName = this.thisMob.getAssignment().getRegionName();
      String mapFileName = this.thisMob.getAssignment().getRegionFileName();
      Assignment.AssignmentType courierAssignment = courier.getAssignment().getAssignmentType();
      if (courierAssignment == Assignment.AssignmentType.COURIER_PARKED
         && this.migration.getDailySendAmountRemaining(this.thisMob.getAssignment().getRegionFileName(), this.thisMob.getAssignment().getPickupResourceType())
            >= 0) {
         for (;
            this.thisMob.hasCarriedResources() && courier.canCarryMore();
            this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.thisMob.getCenterPixelX(true), this.thisMob.getCenterPixelY(true))
         ) {
            ResourceBase resourceDelivery = this.thisMob.removeFirstCarriedResource();
            courier.addCarriedResource(resourceDelivery);
            this.thisMob.awardXP(Utilities.randomInt(5) + 5);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.01, GodModule.GodXPType.DELIVERING_RESOURCES_TO_COURIER);
            }
         }

         courier.getAssignment().setRegionName(mapName);
         courier.getAssignment().setRegionFileName(mapFileName);
         this.switchNode(failBranch);
      } else {
         this.switchNode(failBranch);
      }
   }

   private void pathfindToExtraResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.deliverCourierResourceInHand.pathfindToExtraResource"));
      this.runPath();
      OrderedPair o = this.thisMob.getAssignment().getSecondaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (!this.pathFailed
         && r != null
         && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
         && !this.road.isResourceNeededForRoad(r)
         && r.getType() == this.thisMob.getCarriedResourcesListType()) {
         if (this.pathCompleted) {
            this.aiStep = 4;
         }
      } else {
         OrderedPair resourceTile = this.thisMob.getAssignment().getSecondaryCoordinates();
         if (resourceTile != null && this.thisMob.canReach(resourceTile, true)) {
            this.requestPath(resourceTile);
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
      MobBase courier = this.thisMob.getAssignment().getPrimaryMob();
      if (courier != null && this.thisMob.canReach(courier, true) && assignment.containsPickupResources(resourceOnGround)) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.addCarriedResource(resourceOnGround);
            this.thisMob.getAssignment().removePickupResources(resourceOnGround);
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 5;
         }
      } else {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      }
   }

   private void liftExtraResource() throws SlickException {
      MobBase courier = this.thisMob.getAssignment().getPrimaryMob();
      if (courier != null && this.thisMob.canReach(courier, true)) {
         this.thisMob.setPositionLift();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.requestPath(courier.getTileX(), courier.getTileY());
            this.aiStep = 1;
         }
      } else {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
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
            if (!this.migration.canCourierSendMore(assignment.getPrimaryMob())) {
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
