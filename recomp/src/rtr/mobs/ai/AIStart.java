package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;

public class AIStart extends AIBase {
   public AIStart(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_START;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return true;
   }

   @Override
   public boolean tryRun() {
      return true;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         this.thisMob.setCurrentTask(Text.getText("aiTask.start.idle"));
         this.thisMob.setIdle(true);
         this.thisMob.setPositionFace();
         if (this.runDropResource()) {
            if (this.thisMob.isDead()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIDeath());
            } else if (this.thisMob.getAIStuck().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIStuck());
            } else if (this.thisMob.getAISleepOutside().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAISleepOutside());
            } else if (this.thisMob.getAIBirth().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIBirth());
            } else if (this.thisMob.getAIGrowth().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIGrowth());
            } else if (this.thisMob.getAIFindFoodCheck().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIFindFoodCheck());
            } else if (this.thisMob.getAIFindWaterCheck().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIFindWaterCheck());
            } else if (this.thisMob.getAIFindHome().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIFindHome());
            } else if (this.thisMob.getAIFindBetterHome().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIFindBetterHome());
            } else if (this.thisMob.getAIGoHome().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIGoHome());
            } else if (this.thisMob.getAIWander().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIWander());
            } else if (this.thisMob.getAIConfused().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIConfused());
            } else if (this.thisMob.getAIGolemHealBehavior().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIGolemHealBehavior());
            } else if (this.thisMob.getAIChat().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIChat());
            } else if (this.thisMob.getAITrade().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAITrade());
            } else if (this.thisMob.getAIVisitMate().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIVisitMate());
            } else if (this.thisMob.getAIAttemptBreed().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIAttemptBreed());
            } else if (this.thisMob.getAIEquipResource().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIEquipResource());
            } else if (this.thisMob.getAIHealthRepair().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIHealthRepair());
            } else if (this.thisMob.getAICourierParkBehavior().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAICourierParkBehavior());
            } else if (this.thisMob.getAIMigrationProperationBehavior().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIMigrationProperationBehavior());
            } else if (this.thisMob.getAIMigrantLeave().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIMigrantLeave());
            } else if (this.thisMob.getAIAttemptCorruptionWork().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIAttemptCorruptionWork());
            } else if (this.thisMob.getAICorruptionBuildingPlacement().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAICorruptionBuildingPlacement());
            } else if (this.thisMob.getAIFindJob().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIFindJob());
            } else if (this.thisMob.getAIQuitJob().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIQuitJob());
            } else if (this.thisMob.getAIAttemptWork().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIAttemptWork());
            } else if (this.thisMob.getAIHostileBehaviorAttack().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIHostileBehaviorAttack());
            } else if (this.thisMob.getAIHostileBehaviorLeave().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIHostileBehaviorLeave());
            } else if (this.thisMob.getAINomadBehavior().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAINomadBehavior());
            } else if (this.thisMob.getAIProvisionerPost().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIProvisionerPost());
            } else if (this.thisMob.getAILaborerPost().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAILaborerPost());
            } else if (this.thisMob.getAIAnimalWanderBehavior().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIAnimalWanderBehavior());
            } else if (this.thisMob.getAIWildDoggoWanderIn().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIWildDoggoWanderIn());
            } else if (this.thisMob.getAISpawnRepair().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAISpawnRepair());
            } else if (this.thisMob.getAIFindSpawn().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIFindSpawn());
            } else if (this.thisMob.getAIGhostBehavior().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAIGhostBehavior());
            } else if (this.thisMob.getAICombatBuildings().tryRun()) {
               this.thisMob.setIdle(false);
               this.switchNode(this.thisMob.getAICombatBuildings());
            }
         }
      }
   }
}
