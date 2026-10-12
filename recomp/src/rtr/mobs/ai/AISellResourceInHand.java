package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.map.MapData;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AISellResourceInHand extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_PROVISIONER = 1;
   private static final int SELL_RESOURCES = 2;
   private static final int DROP_OFF_LIFT = 3;
   private static final int PATHFIND_TO_EXTRA_RESOURCE = 4;
   private static final int PICKUP_EXTRA_RESOURCE = 5;
   private static final int LIFT_EXTRA_RESOURCE = 6;
   private static AIBase failBranch;

   public AISellResourceInHand(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_SELL_RESOURCE_IN_HAND;
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
         failBranch = this.thisMob.getAIStoreResourceInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToProvisioner();
               break;
            case 2:
               this.sellResources();
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
         MobBase catjeetOut = assignment.getPrimaryMob();
         if (catjeetOut == null) {
            MobJobBase job = jobType.getJob();
            ResourceModule.ResourceType resourceTypeCarried = this.thisMob.getCarriedResourcesListType();
            if (!job.canWorkerTrade(resourceTypeCarried)) {
               this.switchNode(failBranch);
               return;
            }

            ArrayList<MobBase> allCatjeet = this.mob.getMobsByAssignment(Assignment.AssignmentType.CATJEET_SELLING_WARES, false);
            ArrayList<MobBase> allProvisionersSelling = this.mob.getMobsByAssignment(Assignment.AssignmentType.SELL_RESOURCE, false);
            ArrayList<MobBase> validCatjeet = new ArrayList<>();

            label50:
            for (MobBase m : allCatjeet) {
               if (this.thisMob.canReach(m, true)) {
                  for (MobBase m2 : allProvisionersSelling) {
                     if (m2.getAssignment().getPrimaryMob() == m) {
                        continue label50;
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
               return;
            }

            if (this.trade.getAcceptSellCount(resourceTypeCarried, this.thisMob.getID()) < this.thisMob.getCarriedResourcesList().size()) {
               this.switchNode(failBranch);
               return;
            }

            OrderedPair nearestCatjeetCoordinates = this.pathFinder
               .searchForMob(this.thisMob.getTileX(), this.thisMob.getTileY(), validCatjeet, false, true, this.thisMobFlags.getBlockMapGroup());
            catjeetOut = this.pathFinder.mobFetch(validCatjeet, nearestCatjeetCoordinates.getX(), nearestCatjeetCoordinates.getY());
         }

         assignment.reset();
         assignment.setPrimaryMob(catjeetOut);
         assignment.setPrimaryCoordinates(catjeetOut.getTileX(), catjeetOut.getTileY());
         assignment.setPickupResourceType(this.thisMob.getCarriedResourcesListType());
         assignment.setAssignmentType(Assignment.AssignmentType.SELL_RESOURCE);
         assignment.finalizeAssignment();
         this.requestPath(catjeetOut.getTileX(), catjeetOut.getTileY());
         this.aiStep = 1;
      }
   }

   private void pathfindToProvisioner() throws SlickException {
      MobBase deliveryCatjeet = this.thisMob.getAssignment().getPrimaryMob();
      this.thisMob.setCurrentTask(Text.getText("aiTask.sellResourceInHand.pathfindToProvisioner"));
      this.followMob(deliveryCatjeet);
      ResourceModule.ResourceType resourceTypeSelling = this.thisMob.getCarriedResourcesListType();
      if (this.pathCompleted) {
         this.aiStep = 2;
      } else if (this.pathFailed
         || deliveryCatjeet == null
         || deliveryCatjeet.getAssignment().getAssignmentType() != Assignment.AssignmentType.CATJEET_SELLING_WARES
         || !this.thisMob.canReach(deliveryCatjeet, true)
         || !this.trade.isModeSell(resourceTypeSelling)
         || this.trade.getTradeAmount(resourceTypeSelling) <= 0) {
         this.switchNode(failBranch);
      } else if (this.searchForMore()) {
         this.aiStep = 4;
      }
   }

   private void sellResources() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ResourceModule.ResourceType resourceType = this.thisMob.getCarriedResourcesListType();
      MobBase catjeet = this.thisMob.getAssignment().getPrimaryMob();
      ObjectBase marketplace = this.object.getBuildingAtLocation(catjeet.getTileX(), catjeet.getTileY());
      Assignment.AssignmentType catjeetAssignment = catjeet.getAssignment().getAssignmentType();
      if (catjeetAssignment == Assignment.AssignmentType.CATJEET_SELLING_WARES
         && marketplace != null
         && this.trade.isModeSell(resourceType)
         && this.trade.getTradeAmount(resourceType) > 0) {
         this.thisMob.setPositionDrop();
         if (this.thisMob.getSprite().isStopped()) {
            int value = 0;

            while (
               this.trade.getTradeAmount(resourceType) > 0 && this.thisMob.hasCarriedResources() && this.thisMob.getCarriedResourcesListType() == resourceType
            ) {
               ResourceBase resourceSold = this.thisMob.removeFirstCarriedResource();
               this.resource.transferResourceToProvisioner(resourceSold);
               this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.thisMob.getCenterPixelX(true), this.thisMob.getCenterPixelY(true));
               value += this.trade.getSellPrice(resourceType);
               this.trade.decreaseTradeAmount(resourceType, 1);
               if (this.trade.getTradeAmount(resourceType) == 0) {
                  this.trade.resetTradeMode(resourceType);
               }

               this.thisMob.awardXP(Utilities.randomInt(2) + 2);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.SELLING_RESOURCES);
               }

               if (resourceType == ResourceModule.ResourceType.GOD_DUST) {
                  this.goal.incrementGoal(GoalModule.GoalType.THE_DUST_MUST_FLOW);
               }

               this.goal.incrementGoal(GoalModule.GoalType.CATJEET_HAS_WARES);
               if (resourceType == ResourceModule.ResourceType.EERIE_VESSEL) {
                  this.randomEerieVesselMessage(resourceSold.getBoundGhost());
                  this.goal.incrementGoal(GoalModule.GoalType.THE_SOUL_TRADE);
               }
            }

            ArrayList<ResourceBase> goldSacks = new ArrayList<>();

            while (value > 0) {
               ResourceBase gold = this.resource.createResourceUnassigned(ResourceModule.ResourceType.GOLD_COIN_SACK);
               if (value >= gold.getUsesMax()) {
                  gold.setUses(gold.getUsesMax());
                  value -= gold.getUsesMax();
               } else {
                  gold.setUses(value);
                  value = 0;
               }

               goldSacks.add(gold);
            }

            if (this.thisMob.hasCarriedResources()) {
               for (ResourceBase r : goldSacks) {
                  OrderedPair openSpace = this.resource.findOpenSpace(tileX, tileY, MapData.BlockMapGroup.STANDARD);
                  this.resource.setResourcesOnGround(openSpace.getX(), openSpace.getY(), r);
               }
            } else {
               for (ResourceBase r : goldSacks) {
                  if (this.thisMob.canCarryMore()) {
                     r.forcePositionMob(catjeet);
                     this.thisMob.addCarriedResource(r);
                  } else {
                     OrderedPair openSpace = this.resource.findOpenSpace(tileX, tileY, MapData.BlockMapGroup.STANDARD);
                     this.resource.setResourcesOnGround(openSpace.getX(), openSpace.getY(), r);
                  }
               }
            }

            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 3;
         }
      } else {
         this.switchNode(failBranch);
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
            this.switchNode(failBranch);
         }
      }
   }

   private void pathfindToExtraResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.sellResourceInHand.pathfindToExtraResource"));
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
      this.thisMob.setPositionPickup();
      if (this.thisMob.getSprite().isStopped()) {
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         Assignment assignment = this.thisMob.getAssignment();
         ResourceBase resourceOnGround = this.resource.getResourceOnGround(tileX, tileY);
         MobBase catjeetOut = this.thisMob.getAssignment().getPrimaryMob();
         if (resourceOnGround != null && assignment.containsPickupResources(resourceOnGround) && catjeetOut != null && this.thisMob.canReach(catjeetOut, true)) {
            this.thisMob.addCarriedResource(resourceOnGround);
            this.thisMob.getAssignment().removePickupResources(resourceOnGround);
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 6;
         } else {
            this.switchNode(failBranch);
         }
      }
   }

   private void liftExtraResource() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         MobBase catjeetOut = this.thisMob.getAssignment().getPrimaryMob();
         this.requestPath(catjeetOut.getTileX(), catjeetOut.getTileY());
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
            if (this.trade.getAcceptSellCount(this.thisMob.getCarriedResourcesListType(), -1) <= 0) {
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

   private void randomEerieVesselMessage(MobBase soldMob) throws SlickException {
      String randomSubtitle = this.mob.getRandomEerieVesselMessage(this.thisMob.getGender());
      String title = "";
      Text.setVariableText("mobType", soldMob.getMobType().getName());
      title = Text.getText("console.banner.mobAI.sellEerieVessel.title");
      Text.setVariableText("mobName", soldMob.getFormattedName(true));
      Text.setVariableText("mobTitle", title);
      String deathReason = Text.getText("console.banner.mobAI.sellEerieVessel.header");
      Console.newBanner(deathReason, randomSubtitle);
      this.sound.playSound(SoundModule.SoundType.REGISTER);
   }
}
