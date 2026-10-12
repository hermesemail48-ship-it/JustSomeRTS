package rtr.influence;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.TimeModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class SpellIlluminate extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.ILLUMINATE;
      this.name = Text.getText("spell.illuminate.name");
      this.description = Text.getText("spell.illuminate.description");
      this.cooldown = 30;
      this.castCost = freeCast ? 0 : 200;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.ILLUMINATE_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.ILLUMINATE_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 15000;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.ILLUMINATE_SPELL_DURATION) > 0.0) {
         this.duration = (int)(this.duration + 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.ILLUMINATE_SPELL_DURATION));
      }

      this.radius = 32;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.ILLUMINATE_SPELL_RADIUS) > 0.0) {
         this.radius = (int)(this.radius + this.perk.getPerkTypeTotal(PerkModule.PerkType.ILLUMINATE_SPELL_RADIUS));
      }
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)this.radius, (byte)18, this.duration + 50);
            this.goal.incrementGoal(GoalModule.GoalType.LIGHT_THE_WAY);
            this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius / 2, true, 4);
            this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, (int)(this.radius * 0.75 * 16.0), 3.0F, ParticleModule.ParticleSet.FIRE_BLUE);
            this.projectile.newConstrictingEnergyRing(this.targetX, this.targetY, (int)(this.radius * 0.75 * 16.0), 3.0F, ParticleModule.ParticleSet.FIRE_BLUE);
            this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, (int)(this.radius * 0.75 * 16.0), 2.0F, ParticleModule.ParticleSet.FIRE_BLUE);
            this.projectile.newConstrictingEnergyRing(this.targetX, this.targetY, (int)(this.radius * 0.75 * 16.0), 2.0F, ParticleModule.ParticleSet.FIRE_BLUE);
            this.spellStep++;
            break;
         case 1:
            if (this.durationTick % 400 == 0) {
               this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, (int)(this.radius * 0.75 * 16.0), 2.0F, ParticleModule.ParticleSet.FIRE_BLUE);
               if ((this.time.getTimeOfDay() == TimeModule.TimeOfDay.NIGHT || this.time.getTimeOfDay() == TimeModule.TimeOfDay.DUSK)
                  && Utilities.randomInt(5) == 0) {
                  this.mob.influenceFaithInArea(this.tileX, this.tileY, this.radius / 2, 1, true);
               } else if (this.time.isBloodMoon() || this.time.isEclipse()) {
                  this.mob.influenceFaithInArea(this.tileX, this.tileY, this.radius / 2, 1, true);
               }
            }

            if (this.durationTick > this.duration) {
               this.influence.removeSpell(this);
            }
      }
   }
}
