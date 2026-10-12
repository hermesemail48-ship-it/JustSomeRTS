package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.utilities.Utilities;

public class AIWildDoggoDomesticate extends AIBase {
   public AIWildDoggoDomesticate(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_WILD_DOGGO_DOMESTICATE;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.canSocialHaveOwner() && this.thisMob.getOwner() != null && this.thisMobFlags.getDomesticateType() != null;
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
            float mobX = this.thisMob.getMobX();
            float mobY = this.thisMob.getMobY();
            this.font.newFloater("iconOwner", mobX + 4.0F, mobY);
            this.thisMob.awardXP(Utilities.randomInt(100) + 100);
            if (this.thisMob.getOwner().getMobFlags().getAllowGodExperience()) {
               this.god.increaseGodXP(1.0, GodModule.GodXPType.DOMESTICATION);
            }

            this.sound.playSound(SoundModule.SoundType.VILLAGER_BIRTH);
            this.stats.increaseAnimalDomesticatedThisPeriod(this.thisMob.getMobFlags().getDomesticateType());
            Text.setVariableText("mobType", this.thisMobFlags.getDomesticateType().getName());
            this.mob.spawnMob(this.thisMob, this.thisMobFlags.getDomesticateType(), Text.getText("console.banner.mob.wildDoggoDomesticate.subtitle"), true);
         }
      }
   }
}
