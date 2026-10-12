package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIGoHome extends AIBase {
   private static final int FIND_HOME = 0;
   private static final int PATHFIND_TO_HOME = 1;
   private static final int ENTER_HOME = 2;
   private static AIBase resetBranch;
   private static AIBase exitBranch;

   public AIGoHome(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_GO_HOME;
      this.interruptAggroMobs = true;
      this.interruptAggroBuildings = true;
      this.interruptStuck = true;
      this.interruptDeath = true;
      this.interruptOnFire = true;
      this.interruptFrozen = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowHome() && this.thisMobFlags.getCanEnterHome() && this.thisMob.getAssignedHome() >= 0;
   }

   @Override
   public boolean tryRun() {
      return this.allowRun()
         && (
            Utilities.randomInt(this.thisMobFlags.getHomeVisitChance()) == 0
               || this.thisMobFlags.getAllowSleep() && this.thisMob.getEnergy() <= this.thisMobFlags.getSleepHomeReturnLevel()
               || this.thisMobFlags.getAllowThermal()
                  && this.thisMobFlags.getCanThermalFreeze()
                  && this.thisMob.getBodyTemp() <= this.thisMobFlags.getThermalFreeze()
               || this.thisMobFlags.getAllowThermal()
                  && this.thisMobFlags.getCanThermalOverheat()
                  && this.thisMob.getBodyTemp() >= this.thisMobFlags.getThermalOverheat()
               || this.thisMobFlags.getAllowHunger()
                  && this.thisMob.getHunger() <= this.thisMobFlags.getHungerHungryFoodSearchLevel()
                  && this.isFoodAvailable()
               || !this.isDying() && this.thisMob.getHitPoints() <= this.thisMob.getHitPointsMax() * this.thisMobFlags.getHealthRepairPercentage()
               || this.thisMobFlags.getAllowThirst()
                  && this.thisMob.getThirst() <= this.thisMobFlags.getThirstThirstyWaterSearchLevel()
                  && this.isWaterAvailable()
         );
   }

   private boolean isDying() {
      return this.thisMobFlags.getAllowHunger() && this.thisMob.getHunger() <= 0 ? true : this.thisMobFlags.getAllowThirst() && this.thisMob.getThirst() <= 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         exitBranch = this.thisMob.getAIInHome();
         if (this.runDropResource()) {
            ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
            if (home != null && this.thisMob.canReach(home)) {
               switch (this.aiStep) {
                  case 0:
                     this.findHome();
                     break;
                  case 1:
                     this.pathfindToHome();
                     break;
                  case 2:
                     this.enterHome();
               }
            } else {
               this.switchNode(resetBranch);
            }
         }
      }
   }

   private void findHome() throws SlickException {
      ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
      Text.setVariableText("buildingName", home.getFormattedName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.goHome.findHome"));
      OrderedPair homeCoordinates = home.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
      if (this.thisMob.canReach(homeCoordinates)) {
         this.requestPath(homeCoordinates);
         this.aiStep = 1;
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void pathfindToHome() throws SlickException {
      ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
      Text.setVariableText("buildingName", home.getFormattedName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.goHome.pathfindToHome"));
      this.runPath();
      if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            if (!this.thisMobFlags.getCanEnterHome()) {
               this.switchNode(resetBranch);
               return;
            }

            this.aiStep = 2;
         }
      }
   }

   private void enterHome() throws SlickException {
      ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
      Text.setVariableText("buildingName", home.getFormattedName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.goHome.enterHome"));
      if (this.object.getBuildingAtLocation(this.thisMob.getTileX(), this.thisMob.getTileY()) == home) {
         this.thisMob.enterBuilding();
         this.switchNode(exitBranch);
      } else {
         this.switchNode(resetBranch);
      }
   }

   private boolean isWaterAvailable() {
      if (this.object.getObjects().get(this.thisMob.getAssignedHome()).getTotalWater() <= 0) {
         return false;
      } else {
         ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
         ArrayList<MobBase> occupants = home.getOccupants();
         int totalThirst = 0;

         for (MobBase m : occupants) {
            if (m != this.thisMob
               && (m.getActiveNode() == m.getAIGoHome() || m.isInsideBuilding() && this.object.getBuildingAtLocation(m.getTileX(), m.getTileY()) == home)) {
               totalThirst += m.getThirstMax() - m.getThirst();
            }
         }

         if (totalThirst > home.getTotalWater()) {
            return false;
         } else {
            ArrayList<MobBase> entireVillage = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
            entireVillage.removeAll(occupants);

            for (MobBase mx : entireVillage) {
               if (mx != this.thisMob && (mx.getActiveNode() == mx.getAIFindWaterFountain() || mx.getAssignment().getPrimaryObject() == home)) {
                  totalThirst += mx.getThirstMax() - mx.getThirst();
               }
            }

            return totalThirst <= home.getTotalWater();
         }
      }
   }

   private boolean isFoodAvailable() {
      ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
      boolean foundFood = false;

      ResourceModule.ResourceType[] t;
      for (ResourceModule.ResourceType tx : t = ResourceModule.ResourceType.values()) {
         if (tx.getTemplate().getFoodPerUse() != 0 && home.getResourceCount(tx) > 0) {
            foundFood = true;
            break;
         }
      }

      if (!foundFood) {
         return false;
      } else {
         ArrayList<MobBase> occupants = home.getOccupants();
         int totalHunger = 0;

         for (MobBase m : occupants) {
            if (m != this.thisMob
               && (m.getActiveNode() == m.getAIGoHome() || m.isInsideBuilding() && this.object.getBuildingAtLocation(m.getTileX(), m.getTileY()) == home)) {
               totalHunger += m.getHungerMax() - m.getHunger();
            }
         }

         int availableFood = 0;

         ResourceModule.ResourceType[] var9;
         for (ResourceModule.ResourceType txx : ResourceModule.ResourceType.values()) {
            if (txx.getTemplate().getFoodPerUse() != 0) {
               for (ResourceBase r : home.getStorageResources(txx)) {
                  availableFood += r.getUses();
               }
            }
         }

         return totalHunger <= availableFood;
      }
   }
}
