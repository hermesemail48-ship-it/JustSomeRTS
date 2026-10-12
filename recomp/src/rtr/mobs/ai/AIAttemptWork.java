package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.utilities.Utilities;

public class AIAttemptWork extends AIBase {
   public AIAttemptWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_ATTEMPT_WORK;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowWork();
   }

   @Override
   public boolean tryRun() {
      return !this.allowRun() ? false : this.rollWorkerChance();
   }

   protected boolean rollWorkerChance() {
      if (this.thisMobFlags.getAllowThermal()) {
         if (this.thisMobFlags.getCanThermalOverheatCritical() && this.thisMob.getBodyTemp() >= this.thisMobFlags.getThermalOverheatCritical()) {
            return false;
         }

         if (this.thisMobFlags.getCanThermalFreezeCritical() && this.thisMob.getBodyTemp() <= this.thisMobFlags.getThermalFreezeCritical()) {
            return false;
         }
      }

      if (this.thisMobFlags.getAllowSleep() && this.thisMob.getEnergy() <= this.thisMobFlags.getWorkEnergyCutoff()) {
         return false;
      } else {
         int workChanceModified = this.thisMobFlags.getWorkChance();
         if (this.thisMobFlags.getAllowSocial()) {
            if (this.thisMob.getSocial() < this.thisMobFlags.getSocialSadnessLevel()) {
               workChanceModified *= 5;
            } else if (this.thisMob.getSocial() < this.thisMobFlags.getSocialLonelyLevel()) {
               workChanceModified *= 2;
            }
         }

         if (this.thisMobFlags.getAllowThirst()) {
            if (this.thisMob.getThirst() < this.thisMobFlags.getThirstDehydrationWaterSearchLevel()) {
               workChanceModified *= 8;
            } else if (this.thisMob.getThirst() < this.thisMobFlags.getThirstThirstyWaterSearchLevel()) {
               workChanceModified *= 2;
            }
         }

         if (this.thisMobFlags.getAllowHunger()) {
            if (this.thisMob.getHunger() < this.thisMobFlags.getHungerStarvationFoodSearchLevel()) {
               workChanceModified *= 8;
            } else if (this.thisMob.getHunger() < this.thisMobFlags.getHungerHungryFoodSearchLevel()) {
               workChanceModified *= 2;
            }
         }

         if (!this.thisMobFlags.isInvulnerable() && this.thisMob.getHitPointsMax() > 0) {
            if (this.thisMob.getHitPoints() <= this.thisMob.getHitPointsMax() * 0.25) {
               workChanceModified *= 8;
            } else if (this.thisMob.getHitPoints() <= this.thisMob.getHitPointsMax() * 0.5) {
               workChanceModified *= 5;
            } else if (this.thisMob.getHitPoints() <= this.thisMob.getHitPointsMax() * 0.75) {
               workChanceModified *= 2;
            }
         }

         if (this.thisMob.isCompromised()) {
            workChanceModified *= 5;
         }

         return Utilities.randomInt(workChanceModified) == 0;
      }
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         this.switchNode(this.thisMob.getAIPatrolVillage());
      }
   }
}
