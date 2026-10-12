package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.Utilities;

public class AIInHome extends AIBase {
   private static final int SETUP_IN_HOME = 0;
   private static final int IN_HOME = 1;
   private static AIBase exitBranch;

   public AIInHome(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_IN_HOME;
      this.interruptDeath = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowHome() && this.thisMobFlags.getCanSleepHome() && this.thisMob.getAssignedHome() >= 0 && this.thisMob.isInsideBuilding();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         exitBranch = this.thisMob.getAIWander();
         switch (this.aiStep) {
            case 0:
               this.setupInHome();
               break;
            case 1:
               this.inHome();
         }
      }
   }

   private void setupInHome() throws SlickException {
      ObjectBase currentBuilding = this.object.getBuildingAtLocation(this.thisMob.getTileX(), this.thisMob.getTileY());
      if (currentBuilding == null) {
         this.thisMob.leaveBuilding();
         this.switchNode(exitBranch);
      } else {
         if (this.thisMob.getMobFlags().getAllowFaith() && currentBuilding.getObjectFlags().getOccupantsFaithBonus() > 0) {
            this.thisMob.increaseFaith(currentBuilding.getObjectFlags().getOccupantsFaithBonus(), true);
         }

         Assignment assignment = this.thisMob.getAssignment();
         assignment.setPrimaryObject(currentBuilding);
         assignment.setAssignmentType(Assignment.AssignmentType.IN_HOME);
         assignment.finalizeAssignment();
         this.aiStep = 1;
      }
   }

   private void inHome() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.inHome.inHome"));
      ObjectBase currentBuilding = this.object.getBuildingAtLocation(this.thisMob.getTileX(), this.thisMob.getTileY());
      if (currentBuilding == null) {
         this.thisMob.leaveBuilding();
         this.switchNode(exitBranch);
      } else {
         if (Utilities.randomInt(this.thisMobFlags.getHomeLeaveWeight()) == 0) {
            if (this.thisMobFlags.getAllowThermal()
               && (
                  !this.thisMobFlags.getCanThermalFreeze()
                     || this.thisMobFlags.getCanThermalFreeze() && this.thisMob.getBodyTemp() >= this.thisMobFlags.getThermalBaseline() - 20
               )
               && (
                  !this.thisMobFlags.getCanThermalOverheat()
                     || this.thisMobFlags.getCanThermalOverheat() && this.thisMob.getBodyTemp() <= this.thisMobFlags.getThermalBaseline() + 20
               )) {
               this.thisMob.leaveBuilding();
               this.switchNode(exitBranch);
               return;
            }

            if (this.thisMobFlags.getAllowHealthRegen()
               && this.thisMob.getHitPoints() <= this.thisMob.getHitPointsMax() * this.thisMobFlags.getHealthRepairPercentage()) {
               this.thisMob.leaveBuilding();
               this.switchNode(exitBranch);
               return;
            }
         }

         if (this.thisMobFlags.getAllowSleep() && this.thisMob.getEnergy() <= this.thisMobFlags.getSleepHomeEnergyLevel()) {
            this.sleeping = true;
         }

         if (this.sleeping) {
            int sleepDelayModified = (int)(
               this.thisMobFlags.getSleepDelay()
                  - Utilities.getPercentOf(this.thisMobFlags.getSleepDelay(), currentBuilding.getObjectFlags().getOccupantsSleepBonusPercentOff())
            );
            this.thisMob.setCurrentTask(Text.getText("aiTask.inHome.sleeping"));
            this.thisMob.runSleepDelayTick(1);
            if (this.thisMob.getSleepDelayTick() > sleepDelayModified) {
               this.thisMob.increaseEnergy(1);
               this.thisMob.resetSleepDelayTick();
            }

            if (this.thisMob.getEnergy() >= this.thisMobFlags.getSleepHomeBreakawayLevel()
               && Utilities.randomInt(this.thisMobFlags.getSleepHomeBreakawayChance()) == 0) {
               this.thisMob.resetSleepDelayTick();
               this.sleeping = false;
            }
         } else {
            ArrayList<ResourceModule.ResourceType> availableFoodTypes = new ArrayList<>();

            ResourceModule.ResourceType[] var6;
            for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
               if (t.getTemplate().getFoodPerUse() != 0 && currentBuilding.isResourceAvailable(t)) {
                  availableFoodTypes.add(t);
               }
            }

            Collections.shuffle(availableFoodTypes);
            if (this.thisMobFlags.getAllowHunger()
               && !this.eating
               && this.thisMob.getHunger() <= this.thisMobFlags.getHungerInHomeEatLevel()
               && (this.thisMob.hasFoodResource() || availableFoodTypes.size() > 0)) {
               if (!this.thisMob.hasFoodResource()) {
                  ResourceBase r = null;

                  for (ResourceModule.ResourceType tx : availableFoodTypes) {
                     if (currentBuilding.isResourceAvailable(tx)) {
                        r = currentBuilding.takeStores(tx);
                        break;
                     }
                  }

                  this.thisMob.equipResource(r, r.getType().getTemplate().getResourceSlot());
               }

               this.eating = true;
            }

            if (this.eating) {
               if (this.thisMob.getHunger() >= this.thisMob.getHungerMax()
                  || this.thisMob.getHunger() > this.thisMobFlags.getHungerEatBreakawayLevel()
                     && Utilities.randomInt(this.thisMobFlags.getHungerEatBreakawayChance()) == 0) {
                  this.eating = false;
               } else if (this.thisMob.hasFoodResource()) {
                  this.eatFoodInInventory();
               } else {
                  ResourceBase r = null;

                  for (ResourceModule.ResourceType txx : availableFoodTypes) {
                     if (currentBuilding.isResourceAvailable(txx)) {
                        r = currentBuilding.takeStores(txx);
                        break;
                     }
                  }

                  if (r != null) {
                     this.thisMob.equipResource(r, r.getType().getTemplate().getResourceSlot());
                  } else {
                     this.eating = false;
                  }
               }
            } else {
               if (this.thisMobFlags.getAllowThirst()
                  && !this.drinking
                  && this.thisMob.getThirst() <= this.thisMobFlags.getThirstInHomeDrinkLevel()
                  && (
                     this.thisMob.hasWaterResource()
                        || currentBuilding.getTotalWater() > 0
                        || currentBuilding.isResourceAvailable(ResourceModule.ResourceType.WATER_BOTTLE)
                  )) {
                  if (!this.thisMob.hasWaterResource() && currentBuilding.isResourceAvailable(ResourceModule.ResourceType.WATER_BOTTLE)) {
                     ResourceBase r = currentBuilding.takeStores(ResourceModule.ResourceType.WATER_BOTTLE);
                     this.thisMob.equipResource(r, r.getType().getTemplate().getResourceSlot());
                  }

                  this.drinking = true;
               }

               if (this.drinking) {
                  if (this.thisMob.getThirst() >= this.thisMob.getThirstMax()
                     || this.thisMob.getThirst() > this.thisMobFlags.getThirstDrinkBreakawayLevel()
                        && Utilities.randomInt(this.thisMobFlags.getThirstDrinkBreakawayChance()) == 0) {
                     this.eating = false;
                  } else if (currentBuilding.getTotalWater() > 0) {
                     this.thisMob.runThirstDrinkTimeTick(1);
                     if (this.thisMob.getThirstDrinkTimeTick() >= this.thisMobFlags.getThirstDrinkTime()) {
                        currentBuilding.decreaseWater(1);
                        this.thisMob.drinkWater(1);
                        this.thisMob.resetThirstDrinkTimeTick();
                     }
                  } else if (this.thisMob.hasWaterResource()) {
                     this.drinkWaterInInventory();
                  } else {
                     ResourceBase r = currentBuilding.takeStores(ResourceModule.ResourceType.WATER_BOTTLE);
                     if (r != null) {
                        this.thisMob.equipResource(r, r.getType().getTemplate().getResourceSlot());
                     } else {
                        this.drinking = false;
                     }
                  }
               }
            }
         }
      }
   }
}
