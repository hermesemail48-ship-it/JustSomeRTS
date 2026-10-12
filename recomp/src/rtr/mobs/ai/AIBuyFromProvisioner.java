package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIBuyFromProvisioner extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_PROVISIONER = 1;
   private static final int BUY_RESOURCE = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;
   private static AIBase exitWithResourceBranch;

   public AIBuyFromProvisioner(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_BUY_FROM_PROVISIONER;
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
         resetBranch = this.thisMob.getAIStart();
         failBranch = this.thisMob.getAISellResource();
         exitWithResourceBranch = this.thisMob.getAICaptureAnimalsInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToProvisioner();
               break;
            case 2:
               this.buyResource();
         }
      }
   }

   private void findWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(resetBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canWorkerTrade()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<MobBase> allCatjeet = this.mob.getMobsByAssignment(Assignment.AssignmentType.CATJEET_SELLING_WARES, false);
            ArrayList<MobBase> allProvisionersBuying = this.mob.getMobsByAssignment(Assignment.AssignmentType.BUY_RESOURCE, false);
            allProvisionersBuying.addAll(this.mob.getMobsByAssignment(Assignment.AssignmentType.BUY_LABOR, false));
            ArrayList<MobBase> validCatjeet = new ArrayList<>();

            label152:
            for (MobBase m : allCatjeet) {
               if (this.thisMob.canReach(m, true)) {
                  for (MobBase m2 : allProvisionersBuying) {
                     if (m2.getAssignment().getPrimaryMob() == m) {
                        continue label152;
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
               int totalGoldAvailable = this.resource.getTotalAvailableGoldCoins();

               for (MobBase mx : allProvisionersBuying) {
                  if (mx.getAssignment().getAssignmentType() == Assignment.AssignmentType.BUY_LABOR) {
                     totalGoldAvailable -= this.trade.getLaborBuyPrice();
                  } else {
                     totalGoldAvailable -= this.trade.getBuyPrice(mx.getAssignment().getPickupResourceType());
                  }
               }

               if (totalGoldAvailable <= 0) {
                  this.switchNode(failBranch);
               } else {
                  ArrayList<ResourceModule.ResourceType> canBuyResources = new ArrayList<>();

                  for (ResourceModule.ResourceType type : job.getCanWorkerTrade()) {
                     if (this.trade.isModeBuy(type) && this.trade.getTradeAmount(type) > 0 && totalGoldAvailable >= this.trade.getBuyPrice(type)) {
                        int enroute = 0;

                        for (MobBase m2x : allProvisionersBuying) {
                           if (m2x.getAssignment().getPickupResourceType() == type) {
                              enroute += m2x.getAssignment().getPickupResourceAmount();
                           }
                        }

                        if (enroute < this.resource.getProvisionerCount(type)) {
                           canBuyResources.add(type);
                        }
                     }
                  }

                  if (canBuyResources.size() == 0) {
                     this.switchNode(failBranch);
                  } else {
                     Collections.shuffle(canBuyResources);

                     for (ResourceModule.ResourceType r : canBuyResources) {
                        ArrayList<MobBase> validCatjeetFiltered = new ArrayList<>();
                        int totalAvailableMarketplaceCoins = 0;

                        for (MobBase mxx : validCatjeet) {
                           ObjectBase marketplace = this.object.getBuildingAtLocation(mxx.getTileX(), mxx.getTileY());
                           totalAvailableMarketplaceCoins = marketplace.getTotalGoldCoins();
                           if (totalAvailableMarketplaceCoins >= this.trade.getBuyPrice(r)) {
                              for (MobBase m2xx : allProvisionersBuying) {
                                 if (m2xx.getAssignment().getPrimaryObject() == marketplace) {
                                    if (mxx.getAssignment().getAssignmentType() == Assignment.AssignmentType.BUY_LABOR) {
                                       totalAvailableMarketplaceCoins -= this.trade.getLaborBuyPrice();
                                    } else {
                                       totalAvailableMarketplaceCoins -= this.trade.getBuyPrice(mxx.getAssignment().getPickupResourceType())
                                          * mxx.getAssignment().getPickupResourceAmount();
                                    }
                                 }
                              }

                              if (totalAvailableMarketplaceCoins >= this.trade.getBuyPrice(r)) {
                                 validCatjeetFiltered.add(mxx);
                              }
                           }
                        }

                        if (validCatjeetFiltered.size() > 0) {
                           int maxCarry = this.thisMob.getCarryMax();
                           int buyAmount = totalAvailableMarketplaceCoins / this.trade.getBuyPrice(r);
                           if (buyAmount > maxCarry) {
                              buyAmount = maxCarry;
                           }

                           if (buyAmount > this.trade.getTradeAmount(r)) {
                              buyAmount = this.trade.getTradeAmount(r);
                           }

                           OrderedPair nearestCatjeetCoordinates = this.pathFinder
                              .searchForMob(
                                 this.thisMob.getTileX(), this.thisMob.getTileY(), validCatjeetFiltered, false, true, this.thisMobFlags.getBlockMapGroup()
                              );
                           Assignment assignment = this.thisMob.getAssignment();
                           assignment.reset();
                           assignment.setPrimaryMob(
                              this.pathFinder.mobFetch(validCatjeetFiltered, nearestCatjeetCoordinates.getX(), nearestCatjeetCoordinates.getY())
                           );
                           assignment.setPrimaryCoordinates(nearestCatjeetCoordinates);
                           assignment.setPickupResourceType(r);
                           assignment.setPickupResourceAmount(buyAmount);
                           assignment.setAssignmentType(Assignment.AssignmentType.BUY_RESOURCE);
                           assignment.finalizeAssignment();
                           this.requestPath(nearestCatjeetCoordinates);
                           this.aiStep = 1;
                           return;
                        }
                     }

                     this.switchNode(failBranch);
                  }
               }
            }
         }
      }
   }

   private void pathfindToProvisioner() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.buyFromProvisioner.pathfindToProvisioner"));
      this.followMob(this.thisMob.getAssignment().getPrimaryMob());
      ResourceModule.ResourceType resourceType = this.thisMob.getAssignment().getPickupResourceType();
      int value = this.trade.getBuyPrice(resourceType);
      MobBase catjeet = this.thisMob.getAssignment().getPrimaryMob();
      ObjectBase marketplace = this.object.getBuildingAtLocation(catjeet.getTileX(), catjeet.getTileY());
      if (this.pathCompleted) {
         this.aiStep = 2;
      } else if (this.pathFailed
         || catjeet == null
         || catjeet.getAssignment().getAssignmentType() != Assignment.AssignmentType.CATJEET_SELLING_WARES
         || marketplace == null
         || marketplace.getTotalGoldCoins() < value
         || !this.thisMob.canReach(catjeet, true)
         || !this.trade.isModeBuy(resourceType)
         || this.trade.getTradeAmount(resourceType) <= 0) {
         this.switchNode(failBranch);
         return;
      }
   }

   private void buyResource() throws SlickException {
      ResourceModule.ResourceType resourceType = this.thisMob.getAssignment().getPickupResourceType();
      int value = this.trade.getBuyPrice(resourceType);
      MobBase catjeet = this.thisMob.getAssignment().getPrimaryMob();
      ObjectBase marketplace = this.object.getBuildingAtLocation(catjeet.getTileX(), catjeet.getTileY());

      for (int totalPurchase = this.thisMob.getAssignment().getPickupResourceAmount();
         totalPurchase != 0
            && catjeet != null
            && catjeet.getAssignment().getAssignmentType() == Assignment.AssignmentType.CATJEET_SELLING_WARES
            && marketplace != null
            && marketplace.getTotalGoldCoins() >= value
            && this.trade.isModeBuy(resourceType)
            && this.trade.getTradeAmount(resourceType) > 0
            && this.resource.getProvisionerCount(resourceType) > 0;
         totalPurchase--
      ) {
         marketplace.removeGoldCoins(value);
         this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.thisMob.getCenterPixelX(true), this.thisMob.getCenterPixelY(true));
         this.resource.tranferResourceFromProvisionerToMob(this.thisMob, resourceType);
         this.trade.decreaseTradeAmount(resourceType, 1);
         this.thisMob.awardXP(5 + Utilities.randomInt(5));
         if (this.thisMobFlags.getAllowGodExperience()) {
            this.god.increaseGodXP(0.01, GodModule.GodXPType.BUYING_RESOURCES);
         }

         if (resourceType == ResourceModule.ResourceType.SUSPICICIOUS_KEY) {
            this.goal.incrementGoal(GoalModule.GoalType.WHALE);
         }

         this.goal.incrementGoal(GoalModule.GoalType.CATJEET_HAS_WARES);
         if (this.trade.getTradeAmount(resourceType) == 0) {
            this.trade.resetTradeMode(resourceType);
            break;
         }
      }

      if (this.thisMob.hasCarriedResources()) {
         this.switchNode(exitWithResourceBranch);
      } else {
         this.switchNode(failBranch);
      }
   }
}
