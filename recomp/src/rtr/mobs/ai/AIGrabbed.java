package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.system.Game;
import rtr.utilities.Utilities;

public class AIGrabbed extends AIBase {
   private static final int GET_GRABBED = 0;
   private static final int BEING_GRABBED = 1;
   private static final int BEING_DROPPED = 2;
   private static AIBase resetBranch;
   private static AIBase exitBranchConfused;
   private static AIBase exitBranchPanic;
   private static AIBase exitBranchIncapacitated;

   public AIGrabbed(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_GRABBED;
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
         resetBranch = this.thisMob.getAIStart();
         exitBranchConfused = this.thisMob.getAIConfused();
         exitBranchPanic = this.thisMob.getAIPanic();
         exitBranchIncapacitated = this.thisMob.getAIIncapacitated();
         this.thisMob.setPositionFlee();
         this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
         this.particle
            .newParticle(
               this.thisMob.getMobX() + 8.0F + Utilities.randomInt(-2, 2),
               this.thisMob.getMobY() + Utilities.randomInt(-2, 2) + 35.0F - this.thisMob.getGrabbedDropTick() * 2,
               ParticleModule.ParticleType.FIRE_SPECK,
               ParticleModule.ParticleSet.BLACK
            );
         if (Utilities.randomInt(80) == 0) {
            this.font.newFloater("iconConfused", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY() - 16.0F);
         }

         if (Utilities.randomInt(80) == 0) {
            this.font.newFloater("iconExcite", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY() - 16.0F);
         }

         this.thisMob.setCurrentTask(Text.getText("aiTask.grabbed.grabbed"));
         switch (this.aiStep) {
            case 0:
               this.getGrabbed();
               break;
            case 1:
               this.beingGrabbed();
               break;
            case 2:
               this.beingDropped();
         }
      }
   }

   private void getGrabbed() throws SlickException {
      this.thisMob.setGrabbed(true);
      SoundModule.SoundType hit = this.thisMobFlags.getMobSoundSet(this.thisMob.getGender()).getSoundHitSweetener2();
      if (hit != null) {
         this.sound.playSound(hit, this.thisMob.getMobX(), this.thisMob.getMobY());
      } else {
         hit = this.thisMobFlags.getMobSoundSet(this.thisMob.getGender()).getSoundHitSweetener();
         if (hit != null) {
            this.sound.playSound(hit, this.thisMob.getMobX(), this.thisMob.getMobY());
         }
      }

      this.aiStep = 1;
   }

   private void beingGrabbed() throws SlickException {
      this.thisMob.setGrabbed(true);
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

      if (this.influence.getMobGrabbed() == null) {
         this.thisMob.setGrabbed(false);
      }

      if (!this.thisMob.isGrabbed()) {
         this.aiStep = 2;
      } else {
         this.thisMob.setMobX(Game.getCS().getMouseOnMapX() - 8);
         this.thisMob.setMobY(Game.getCS().getMouseOnMapY());
      }
   }

   private void beingDropped() throws SlickException {
      int randomX = 0;
      int randomY = 0;
      this.thisMob.setMobY(this.thisMob.getMobY() + 2.0F);

      for (int x = 0; x < 2; x++) {
         randomX = (int)(this.thisMob.getMobX() + Utilities.randomInt(16));
         randomY = (int)(this.thisMob.getMobY() + Utilities.randomInt(16) + 8.0F);
         this.particle.newParticleGroup(randomX, randomY, ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.GRAY);
      }

      this.thisMob.runGrabbedDropTick(1);
      if (this.thisMob.getGrabbedDropTick() > 10) {
         this.thisMob.tileUpdateForced();
         ObjectBase objectCheck = this.object.getBuildingAtLocation(this.thisMob.getTileX(), this.thisMob.getTileY());
         if (objectCheck != null
            && !this.thisMobFlags.isInvulnerable()
            && objectCheck.getObjectFlags().isCullisGate()
            && Math.abs(this.thisMob.getTileX() - objectCheck.getFunctionalXOnMap()) <= 1
            && Math.abs(this.thisMob.getTileY() - objectCheck.getFunctionalYOnMap()) <= 1) {
            objectCheck.sacrificeToCullisGate(this.thisMob);
            return;
         }

         for (int x = 0; x < 200; x++) {
            randomX = (int)(this.thisMob.getMobX() + Utilities.randomInt(16));
            randomY = (int)(this.thisMob.getMobY() + this.thisMob.getSprite().getSpriteHeight());
            this.particle.newParticleGroup(randomX, randomY, Utilities.randomInt(360), ParticleModule.ParticleGroup.DUST, ParticleModule.ParticleSet.BLACK);
         }

         this.thisMob.takeDamage(Utilities.randomInt(10) + 1, randomX, randomY, MobBase.DamageType.REGULAR, MobBase.DamageCause.DROPPED);
         this.thisMob.resetGrabbedDropTick();
         if (this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER
            && this.map.getMapThreatMap().isTileThreatened(this.thisMob.getTileX(), this.thisMob.getTileY())) {
            this.goal.incrementGoal(GoalModule.GoalType.DUDE_WTH_MAN);
         }

         if (this.thisMobFlags.getAllowIncapacitated() && Utilities.randomInt(10) == 0) {
            this.switchNode(exitBranchIncapacitated);
         } else if (this.thisMobFlags.getAllowConfused() && Utilities.randomInt(3) == 0) {
            this.switchNode(exitBranchConfused);
         } else if (this.thisMobFlags.getAllowPanic()) {
            this.switchNode(exitBranchPanic);
         } else {
            this.switchNode(resetBranch);
         }

         for (MobBase m : this.mob.getMobsAt(this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMob)) {
            if (m.getMobFlags().getAllowIncapacitated()) {
               m.interruptNode(m.getAIIncapacitated());
            }
         }
      }
   }
}
