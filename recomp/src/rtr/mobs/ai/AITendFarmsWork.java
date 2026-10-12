package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.FarmlandBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AITendFarmsWork extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_FARMLAND = 1;
   private static final int TEND_FARMLAND = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AITendFarmsWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_TEND_FARMS_WORK;
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
         failBranch = this.thisMob.getAIPlantResource();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToFarmland();
               break;
            case 2:
               this.tendFarmland();
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
            ArrayList<FarmlandBase> farmland = this.resource.getFarmlandArray();
            ArrayList<FarmlandBase> untendedFarmsCritical = new ArrayList<>();
            ArrayList<FarmlandBase> untendedFarms = new ArrayList<>();
            ArrayList<FarmlandBase> untendedFarmsLowPriority = new ArrayList<>();

            for (FarmlandBase f : farmland) {
               if (f.isPlanted()) {
                  ObjectBase farm = this.object.getBuildingAtLocation(f.getTileX(), f.getTileY());
                  if (!farm.isPaused()
                     && this.thisMob.canReach(farm, true)
                     && !this.mob.isWorkAssigned(f.getX(), f.getY(), Assignment.AssignmentType.TENDING_FARM)) {
                     if (f.getTendedAmount() > 5
                        && (f.getWaterAmount() > 5 || farm.getTotalWater() + farm.getTotalDirtyWater() <= 0 || !this.isWaterAvailable(farm))) {
                        if (f.getTendedAmount() > 20
                           && (f.getWaterAmount() > 20 || farm.getTotalWater() + farm.getTotalDirtyWater() <= 0 || !this.isWaterAvailable(farm))) {
                           if (f.getTendedAmount() <= 50
                              || f.getWaterAmount() <= 50 && farm.getTotalWater() + farm.getTotalDirtyWater() > 0 && this.isWaterAvailable(farm)) {
                              untendedFarmsLowPriority.add(f);
                           }
                        } else {
                           untendedFarms.add(f);
                        }
                     } else {
                        untendedFarmsCritical.add(f);
                     }
                  }
               }
            }

            if (untendedFarmsCritical.size() <= 0 && untendedFarms.size() <= 0 && untendedFarmsLowPriority.size() <= 0) {
               this.switchNode(failBranch);
            } else {
               ArrayList<FarmlandBase> farmlandOut = null;
               if (untendedFarmsCritical.size() > 0) {
                  farmlandOut = untendedFarmsCritical;
               } else if (untendedFarms.size() > 0) {
                  farmlandOut = untendedFarms;
               } else if (untendedFarmsLowPriority.size() > 0) {
                  farmlandOut = untendedFarmsLowPriority;
               }

               ArrayList<OrderedPair> coordinates = new ArrayList<>();

               for (FarmlandBase fx : farmlandOut) {
                  coordinates.add(OrderedPair.getOrderedPair(fx.getTileX(), fx.getTileY()));
               }

               OrderedPair o = this.pathFinder
                  .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), coordinates, false, true, this.thisMobFlags.getBlockMapGroup());
               ObjectBase farm = this.object.getBuildingAtLocation(o.getX(), o.getY());
               if (!farm.isPaused()) {
                  int checkX = o.getX();
                  int checkY = o.getY();
                  Assignment assignment = this.thisMob.getAssignment();
                  assignment.reset();
                  assignment.setPrimaryCoordinates(OrderedPair.getOrderedPair(checkX, checkY));
                  assignment.setSecondaryCoordinates(OrderedPair.getOrderedPair(checkX, checkY));
                  assignment.setPickupResourceType(ResourceModule.ResourceType.RAW_VEGETABLE);
                  assignment.setPrimaryObject(farm);
                  assignment.setAssignmentType(Assignment.AssignmentType.TENDING_FARM);
                  assignment.finalizeAssignment();
                  this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
                  this.aiStep = 1;
               } else {
                  this.switchNode(failBranch);
               }
            }
         }
      }
   }

   private void pathfindToFarmland() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.tendFarmsWork.pathfindToFarmland"));
      this.runPath();
      if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void tendFarmland() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.tendFarmsWork.tendFarmland"));
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(resetBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         float mobX = this.thisMob.getMobX();
         float mobY = this.thisMob.getMobY();
         if (assignment.getPrimaryX() == tileX && assignment.getPrimaryY() == tileY && this.resource.getFarmland(assignment) != null) {
            boolean usingTools = this.hasPreferredTools();
            this.thisMob.setPositionInteract(usingTools, true);
            if (assignment.getSecondaryX() == tileX + 1 && assignment.getSecondaryY() == tileY) {
               this.thisMob.setPosition(MobBase.MobPosition.EAST);
            } else if (assignment.getSecondaryX() == tileX - 1 && assignment.getSecondaryY() == tileY) {
               this.thisMob.setPosition(MobBase.MobPosition.WEST);
            } else if (assignment.getSecondaryX() == tileX && assignment.getSecondaryY() == tileY + 1) {
               this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
            } else if (assignment.getSecondaryX() == tileX && assignment.getSecondaryY() == tileY - 1) {
               this.thisMob.setPosition(MobBase.MobPosition.NORTH);
            }

            this.thisMob.runWorkDelayTick(1);
            this.thisMob.doWork(1, 1, 1, 1, 0);
            if (this.thisMob.getWorkDelayTick() % 35 == 0) {
               for (int l = 0; l < 12; l++) {
                  int tileID = this.map.getTileId(assignment.getSecondaryX(), assignment.getSecondaryY(), l);
                  ResourceModule.ResourceType resourceType = this.map.getMapTileLoader().getTileResourceType(tileID);
                  if (resourceType != null) {
                     if (resourceType == ResourceModule.ResourceType.WOOD) {
                        this.sound.playSound(SoundModule.SoundType.WOOD_CHOP, mobX, mobY);
                     } else if (resourceType == ResourceModule.ResourceType.ROCK) {
                        this.sound.playSound(SoundModule.SoundType.MINING, mobX, mobY);
                     }
                     break;
                  }
               }

               int attackX = assignment.getSecondaryX();
               int attackY = assignment.getSecondaryY();
               double xDiff = this.thisMob.getCenterPixelX(true) - attackX * 16 - 8;
               double yDiff = this.thisMob.getCenterPixelY(true) - attackY * 16 - 8;
               float angle = (float)Math.toDegrees(Math.atan2(yDiff, xDiff));
               int randomParticles = Utilities.randomInt(2) + 1;

               for (int x = 0; x < randomParticles; x++) {
                  this.particle
                     .newParticleGroup(
                        attackX * 16 + Utilities.randomInt(16),
                        attackY * 16 + Utilities.randomInt(16),
                        angle,
                        ParticleModule.ParticleGroup.DUST,
                        ParticleModule.ParticleSet.BLACK
                     );
                  this.particle
                     .newParticleGroup(
                        attackX * 16 + Utilities.randomInt(8) + 2,
                        (attackY - 1) * 16 + Utilities.randomInt(8) + 2,
                        ParticleModule.ParticleGroup.SMOKE_BELLOW,
                        ParticleModule.ParticleSet.GRAY
                     );
               }
            }

            if (this.thisMob.getWorkDelayTick() > this.mobJob.getCalculatedWorkTime(this.thisMob)) {
               if (usingTools) {
                  this.thisMob.getInventoryToolSlot().decreaseUses(1);
               }

               this.thisMob.awardXP(Utilities.randomInt(5) + 5);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.TENDING_FARMS);
               }

               FarmlandBase f = this.resource.getFarmland(this.thisMob.getAssignment());
               f.tendCrop();
               ObjectBase farm = assignment.getPrimaryObject();
               if (farm.getTotalDirtyWater() > 0) {
                  int waterAmount = 20;
                  if (f.getWaterAmountMax() - f.getWaterAmount() < waterAmount) {
                     waterAmount = f.getWaterAmountMax() - f.getWaterAmount();
                  }

                  if (waterAmount > farm.getTotalDirtyWater()) {
                     waterAmount = farm.getTotalDirtyWater();
                  }

                  f.waterCrop(waterAmount);
                  farm.decreaseDirtyWater(waterAmount);
               } else if (farm.getTotalWater() > 0) {
                  int waterAmountx = 20;
                  if (f.getWaterAmountMax() - f.getWaterAmount() < waterAmountx) {
                     waterAmountx = f.getWaterAmountMax() - f.getWaterAmount();
                  }

                  if (waterAmountx > farm.getTotalWater()) {
                     waterAmountx = farm.getTotalWater();
                  }

                  f.waterCrop(waterAmountx);
                  farm.decreaseWater(waterAmountx);
               }

               this.thisMob.resetWorkDelayTick();
               if (f.getTendedAmount() >= f.getTendedAmountMax() * 0.9 && (f.getWaterAmount() >= f.getWaterAmountMax() * 0.9 || !this.isWaterAvailable(farm))) {
                  this.switchNode(resetBranch);
                  return;
               }
            }
         } else {
            this.switchNode(resetBranch);
         }
      }
   }

   public boolean isWaterAvailable(ObjectBase objectCheck) {
      ArrayList<MobBase> tendingVillagers = this.mob.getMobsByAssignment(Assignment.AssignmentType.TENDING_FARM, false);
      int enroute = 0;

      for (MobBase m : tendingVillagers) {
         if (m != this.thisMob && m.getAssignment().getPrimaryObject() == objectCheck) {
            enroute++;
         }
      }

      return enroute * 20 < objectCheck.getTotalWater() + objectCheck.getTotalDirtyWater();
   }
}
