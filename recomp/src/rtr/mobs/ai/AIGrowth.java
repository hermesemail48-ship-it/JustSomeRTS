package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.utilities.Utilities;

public class AIGrowth extends AIBase {
   public AIGrowth(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_GROWTH;
      this.interruptAggroMobs = true;
      this.interruptAggroBuildings = true;
      this.interruptStuck = true;
      this.interruptDeath = true;
      this.interruptOnFire = true;
      this.interruptFrozen = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowAging()
         && (
            this.thisMobFlags.getAgingGrowUpType() != null && this.thisMob.getAge() >= this.thisMobFlags.getAgingGrowUpAge()
               || this.thisMobFlags.getAgingElderType() != null && this.thisMob.getAge() >= this.thisMobFlags.getAgingElderAge()
         );
   }

   @Override
   public boolean tryRun() {
      return this.allowRun();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         if (this.runDropResource()) {
            this.thisMob.awardXP(Utilities.randomInt(200) + 200);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(1.0, GodModule.GodXPType.GROWING_UP);
            }

            this.sound.playSound(SoundModule.SoundType.VILLAGER_BIRTH);
            if (this.thisMobFlags.getAgingGrowUpType() != null) {
               this.mob.spawnMob(this.thisMob, this.thisMobFlags.getAgingGrowUpType(), Text.getText("console.banner.mob.grownUp.subtitle"), true);
               if (this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                  this.stats.increaseVillagerGrownUpThisPeriod(this.thisMob.getMobType());
                  this.goal.incrementGoal(GoalModule.GoalType.ANGST);
               }
            } else if (this.thisMobFlags.getAgingElderType() != null) {
               this.mob.spawnMob(this.thisMob, this.thisMobFlags.getAgingElderType(), Text.getText("console.banner.mob.elder.subtitle"), true);
            }
         }
      }
   }
}
