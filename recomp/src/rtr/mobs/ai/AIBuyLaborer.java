package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIBuyLaborer extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_LABORER = 1;
   private static final int HIRE_LABORER = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIBuyLaborer(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_BUY_LABORER;
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
         failBranch = this.thisMob.getAIBuyFromProvisioner();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToLaborer();
               break;
            case 2:
               this.hireLaborer();
               return;
         }
      }
   }

   private void findWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (job.canWorkerTrade() && this.trade.getLaborTradeAmount() > 0 && this.resource.getTotalAvailableGoldCoins() >= this.trade.getLaborBuyPrice()) {
            ArrayList<MobBase> allCatjeet = this.mob.getMobsByAssignment(Assignment.AssignmentType.CATJEET_SELLING_LABOR, false);
            ArrayList<MobBase> allProvisionersBuying = this.mob.getMobsByAssignment(Assignment.AssignmentType.BUY_RESOURCE, false);
            allProvisionersBuying.addAll(this.mob.getMobsByAssignment(Assignment.AssignmentType.BUY_LABOR, false));
            ArrayList<MobBase> validCatjeet = new ArrayList<>();

            label93:
            for (MobBase m : allCatjeet) {
               if (this.thisMob.canReach(m, true)) {
                  for (MobBase m2 : allProvisionersBuying) {
                     if (m2.getAssignment().getPrimaryMob() == m) {
                        continue label93;
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
                  ArrayList<MobBase> validCatjeetFiltered = new ArrayList<>();

                  for (MobBase mxx : validCatjeet) {
                     ObjectBase marketplace = this.object.getBuildingAtLocation(mxx.getTileX(), mxx.getTileY());
                     int totalMarketplaceCoins = marketplace.getTotalGoldCoins();
                     if (totalMarketplaceCoins >= this.trade.getLaborBuyPrice()) {
                        for (MobBase m2x : allProvisionersBuying) {
                           if (m2x.getAssignment().getPrimaryObject() == marketplace) {
                              if (mxx.getAssignment().getAssignmentType() == Assignment.AssignmentType.BUY_LABOR) {
                                 totalMarketplaceCoins -= this.trade.getLaborBuyPrice();
                              } else {
                                 totalMarketplaceCoins -= this.trade.getBuyPrice(mxx.getAssignment().getPickupResourceType());
                              }
                           }
                        }

                        if (totalMarketplaceCoins >= this.trade.getLaborBuyPrice()) {
                           validCatjeetFiltered.add(mxx);
                        }
                     }
                  }

                  if (validCatjeetFiltered.size() > 0) {
                     OrderedPair nearestCatjeetCoordinates = this.pathFinder
                        .searchForMob(this.thisMob.getTileX(), this.thisMob.getTileY(), validCatjeetFiltered, false, true, this.thisMobFlags.getBlockMapGroup());
                     Assignment assignment = this.thisMob.getAssignment();
                     assignment.reset();
                     assignment.setPrimaryMob(
                        this.pathFinder.mobFetch(validCatjeetFiltered, nearestCatjeetCoordinates.getX(), nearestCatjeetCoordinates.getY())
                     );
                     assignment.setPrimaryCoordinates(nearestCatjeetCoordinates);
                     assignment.setAssignmentType(Assignment.AssignmentType.BUY_LABOR);
                     assignment.finalizeAssignment();
                     this.requestPath(nearestCatjeetCoordinates);
                     this.aiStep = 1;
                  } else {
                     this.switchNode(failBranch);
                  }
               }
            }
         } else {
            this.switchNode(failBranch);
         }
      }
   }

   private void pathfindToLaborer() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.buyLaborer.pathfindToLaborer"));
      this.followMob(this.thisMob.getAssignment().getPrimaryMob());
      int value = this.trade.getLaborBuyPrice();
      MobBase catjeet = this.thisMob.getAssignment().getPrimaryMob();
      ObjectBase marketplace = this.object.getBuildingAtLocation(catjeet.getTileX(), catjeet.getTileY());
      if (this.pathCompleted) {
         this.aiStep = 2;
      } else if (this.pathFailed
         || catjeet == null
         || catjeet.getAssignment().getAssignmentType() != Assignment.AssignmentType.CATJEET_SELLING_LABOR
         || marketplace == null
         || marketplace.getTotalGoldCoins() < value
         || !this.thisMob.canReach(catjeet, true)
         || this.trade.getLaborTradeAmount() <= 0) {
         this.switchNode(failBranch);
      }
   }

   private void hireLaborer() throws SlickException {
      int value = this.trade.getLaborBuyPrice();
      MobBase catjeet = this.thisMob.getAssignment().getPrimaryMob();
      ObjectBase marketplace = this.object.getBuildingAtLocation(catjeet.getTileX(), catjeet.getTileY());
      if (catjeet != null
         && catjeet.getAssignment().getAssignmentType() == Assignment.AssignmentType.CATJEET_SELLING_LABOR
         && marketplace != null
         && marketplace.getTotalGoldCoins() >= value
         && this.trade.getLaborTradeAmount() > 0) {
         marketplace.removeGoldCoins(value);
         this.trade.decreaseLaborTradeAmount(1);
         if (this.trade.getLaborTradeAmount() == 0) {
            this.trade.resetLaborTradeMode();
         }

         this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.thisMob.getCenterPixelX(true), this.thisMob.getCenterPixelY(true));
         this.thisMob.awardXP(25 + Utilities.randomInt(25));
         if (this.thisMobFlags.getAllowGodExperience()) {
            this.god.increaseGodXP(0.03, GodModule.GodXPType.HIRING_LABORERS);
         }

         this.mob.spawnMob(catjeet, MobBase.MobType.CATJEET_VILLAGER, Text.getText("console.banner.mob.hired.subtitle"), true);
         this.goal.incrementGoal(GoalModule.GoalType.MIGRANT_WORKERS);
         this.stats.increaseCatjeetHiredThisPeriod(MobBase.MobType.CATJEET_LABORER);
         this.switchNode(resetBranch);
      } else {
         this.switchNode(failBranch);
      }
   }
}
