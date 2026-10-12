package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class AIBeingSlaughtered extends AIBase {
   private static final int SETUP_SLAUGHTER = 0;
   private static final int BE_SLAUGHTERED = 1;
   private static final int ESCAPE_SLAUGHTER = 2;
   private static AIBase exitBranch;
   private static AIBase exitBranchPanic;

   public AIBeingSlaughtered(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_BEING_SLAUGHTERED;
      this.interruptDeath = true;
   }

   @Override
   public boolean allowRun() {
      return true;
   }

   @Override
   public boolean tryRun() {
      return this.allowRun();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         exitBranch = this.thisMob.getAIStart();
         exitBranchPanic = this.thisMob.getAIPanic();
         switch (this.aiStep) {
            case 0:
               this.setupSlaughter();
               break;
            case 1:
               this.beSlaughtered();
               break;
            case 2:
               this.escapeSlaughter();
         }
      }
   }

   private void setupSlaughter() throws SlickException {
      this.thisMob.setPosition(Utilities.randomInt(2) == 0 ? MobBase.MobPosition.WEST : MobBase.MobPosition.EAST);
      this.aiStep = 1;
   }

   private void beSlaughtered() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.beingSlaughtered.beSlaughtered"));
      this.thisMob.setPositionFlee();
      if (Utilities.randomInt(100) == 0) {
         SoundModule.SoundType hit = this.thisMobFlags.getMobSoundSet(this.thisMob.getGender()).getSoundHitSweetener2();
         if (hit != null) {
            this.sound.playSound(hit, this.thisMob.getMobX(), this.thisMob.getMobY());
         } else {
            hit = this.thisMobFlags.getMobSoundSet(this.thisMob.getGender()).getSoundHitSweetener();
            if (hit != null) {
               this.sound.playSound(hit, this.thisMob.getMobX(), this.thisMob.getMobY());
            }
         }
      }

      if (Utilities.randomInt(2000) == 0) {
         this.aiStep = 2;
      }
   }

   private void escapeSlaughter() throws SlickException {
      int randomX = 0;
      int randomY = 0;
      this.thisMob.setMobY(this.thisMob.getMobY() + 2.0F);

      for (int x = 0; x < 2; x++) {
         randomX = (int)(this.thisMob.getMobX() + Utilities.randomInt(16));
         randomY = (int)(this.thisMob.getMobY() + Utilities.randomInt(16) + 8.0F);
         this.particle.newParticleGroup(randomX, randomY, ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.GRAY);
      }

      if (this.thisMobFlags.getAllowPanic()) {
         this.switchNode(exitBranchPanic);
      } else {
         this.switchNode(exitBranch);
      }
   }
}
