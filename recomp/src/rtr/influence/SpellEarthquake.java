package rtr.influence;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;
import rtr.weather.disasters.DisasterBase;

public class SpellEarthquake extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.EARTHQUAKE;
      this.name = Text.getText("spell.earthquake.name");
      this.description = Text.getText("spell.earthquake.description");
      this.cooldown = 3600;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.EARTHQUAKE_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.EARTHQUAKE_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 2000;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.EARTHQUAKE_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.EARTHQUAKE_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 1500;
      this.radius = 32;
   }

   @Override
   public void update() throws SlickException {
      switch (this.spellStep) {
         case 0:
            this.weather.newDisaster(DisasterBase.DisasterType.EARTHQUAKE, this.tileX, this.tileY, true);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.spellStep++;
            break;
         case 1:
            this.durationTick++;
            this.particle
               .newParticle(
                  this.targetX + Utilities.randomInt(-2, 2),
                  this.targetY + Utilities.randomInt(-2, 2),
                  ParticleModule.ParticleType.FIRE_SPECK,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
            this.particle.newParticle(this.targetX, this.targetY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.MAGIC_GREEN);
            if (this.durationTick % 100 == 0 || Utilities.randomInt(100) == 0) {
               this.projectile.newEnergyBeam(this.targetX, this.targetY, this.targetX + Utilities.randomInt(-10, 10), this.targetY - 500);
               this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);

               for (int i = 0; i < 16; i++) {
                  this.projectile.newEnergyRing(this.targetX, this.targetY, 32, ParticleModule.ParticleSet.FIRE_BLUE);
               }
            }

            if (this.durationTick % 300 == 0) {
               this.mob.influenceFaithInArea(this.tileX, this.tileY, this.radius, 1, false);
            }

            if (this.durationTick > this.duration) {
               this.influence.removeSpell(this);
            }
      }
   }
}
