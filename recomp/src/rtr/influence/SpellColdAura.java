package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class SpellColdAura extends SpellBase {
   ArrayList<OrderedPair> coordinatesInRange;

   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.COLD_AURA;
      this.name = Text.getText("spell.coldAura.name");
      this.description = Text.getText("spell.coldAura.description");
      this.cooldown = 600;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.COLD_AURA_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.COLD_AURA_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 400;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.COLD_AURA_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.COLD_AURA_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 5100;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.COLD_AURA_SPELL_DURATION) > 0.0) {
         this.duration = (int)(this.duration + 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.COLD_AURA_SPELL_DURATION));
      }

      this.radius = 8;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.COLD_AURA_SPELL_RADIUS) > 0.0) {
         this.radius = (int)(this.radius + this.perk.getPerkTypeTotal(PerkModule.PerkType.COLD_AURA_SPELL_RADIUS));
      }
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)this.radius, (byte)16, this.duration);
            this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, this.radius * 16, 4.0F, ParticleModule.ParticleSet.MAGIC_BLUE);
            this.sound.playSound(SoundModule.SoundType.MAGIC_HEAL_SPELL, this.targetX, this.targetY);
            this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
            this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius, true, 2);
            this.coordinatesInRange = Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, this.radius);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.spellStep++;
            break;
         case 1:
            if (this.durationTick > 80) {
               this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, (this.radius - 1) * 16, 0.5F, ParticleModule.ParticleSet.FIRE_BLUE);
               this.projectile.newConstrictingEnergyRing(this.targetX, this.targetY, (this.radius - 1) * 16, 0.25F, ParticleModule.ParticleSet.FIRE_BLUE);
               this.spellStep++;
            }
            break;
         case 2:
            for (int x = 0; x < 30; x++) {
               OrderedPair o = this.coordinatesInRange.get(Utilities.randomInt(this.coordinatesInRange.size()));
               int randomX = o.getX() * 16 + Utilities.randomInt(-16, 16);
               int randomY = o.getY() * 16 + Utilities.randomInt(-16, 16);
               this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.SPARKLE_SPECK, ParticleModule.ParticleSet.MAGIC_BLUE);
            }

            for (int x = 0; x < 10; x++) {
               OrderedPair o = this.coordinatesInRange.get(Utilities.randomInt(this.coordinatesInRange.size()));
               int randomX = o.getX() * 16 + Utilities.randomInt(-16, 16);
               int randomY = o.getY() * 16 + Utilities.randomInt(-16, 16);
               this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.MAGIC_SMOKE_SPECK, ParticleModule.ParticleSet.FIRE_BLUE);
            }

            if (this.durationTick % 400 == 0) {
               this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, (this.radius - 1) * 16, 0.5F, ParticleModule.ParticleSet.FIRE_BLUE);
            }

            if (this.durationTick % 500 == 0) {
               this.projectile.newConstrictingEnergyRing(this.targetX, this.targetY, (this.radius - 1) * 16, 0.25F, ParticleModule.ParticleSet.FIRE_BLUE);
            }

            if (this.durationTick % 5 == 0) {
               this.projectile.newEnergyRing(this.targetX, this.targetY, 16, ParticleModule.ParticleSet.MAGIC_BLUE);
            }

            if (this.durationTick % 200 == 0) {
               this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius + 4, true, 4);
            }

            if (this.durationTick % 500 == 0) {
               this.weather.removeDisasterTemp((Utilities.randomInt(5) + 10.0) / 200.0);
            }

            if (this.durationTick % 60 == 0) {
               ArrayList<MobBase> mobsInRange = this.mob.getMobsNearby(this.tileX, this.tileY, this.radius);
               if (mobsInRange.size() > 0) {
                  this.sound.playSound(SoundModule.SoundType.MAGIC_HEAL, this.targetX, this.targetY);

                  for (MobBase m : mobsInRange) {
                     if (this.durationTick % 50 == 0 && m.getMobFlags().getAllowThermal()) {
                        m.decreaseBodyTemp(1);
                        if (m.getMobFlags().getAllowFaith()) {
                           if (m.getBodyTemp() <= m.getMobFlags().getThermalFreezeCritical()) {
                              m.decreaseFaith(Utilities.randomInt(2, 5), true);
                           } else if (m.getBodyTemp() <= m.getMobFlags().getThermalFreeze()) {
                              m.decreaseFaith(Utilities.randomInt(1, 5), true);
                           } else if (m.getBodyTemp() >= m.getMobFlags().getThermalOverheat() || Utilities.randomInt(10) == 0) {
                              m.increaseFaith(Utilities.randomInt(1, 2), true);
                           }
                        }

                        if (m.getBodyTemp() <= m.getMobFlags().getThermalFreezeCritical() && Utilities.randomInt(10) == 0) {
                           m.increaseFrozenAmount(300);
                        }
                     }

                     if (this.durationTick % 2 == 0) {
                        this.particle
                           .newParticleGroup(
                              m.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                              m.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                              ParticleModule.ParticleGroup.MAGIC_FIRE,
                              ParticleModule.ParticleSet.FIRE_BLUE
                           );
                     }

                     if (this.durationTick % 20 == 0) {
                        this.particle
                           .newParticleGroup(
                              m.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                              m.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                              ParticleModule.ParticleGroup.SMOKE_BELLOW,
                              ParticleModule.ParticleSet.MAGIC_BLUE
                           );
                     }
                  }
               }
            }

            if (this.durationTick > this.duration) {
               this.influence.removeSpell(this);
            }
      }
   }
}
