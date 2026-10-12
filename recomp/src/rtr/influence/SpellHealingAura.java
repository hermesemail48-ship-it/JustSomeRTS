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

public class SpellHealingAura extends SpellBase {
   ArrayList<OrderedPair> coordinatesInRange;

   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.HEALING_AURA;
      this.name = Text.getText("spell.healingAura.name");
      this.description = Text.getText("spell.healingAura.description");
      this.cooldown = 300;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.HEALING_AURA_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.HEALING_AURA_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 400;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.HEALING_AURA_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.HEALING_AURA_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 1020;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.HEALING_AURA_SPELL_DURATION) > 0.0) {
         this.duration = (int)(this.duration + 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.HEALING_AURA_SPELL_DURATION));
      }

      this.radius = 8;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.HEALING_AURA_SPELL_RADIUS) > 0.0) {
         this.radius = (int)(this.radius + this.perk.getPerkTypeTotal(PerkModule.PerkType.HEALING_AURA_SPELL_RADIUS));
      }
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)this.radius, (byte)16, this.duration);

            for (int x = 0; x < 5; x++) {
               this.projectile.newEnergyBeam(this.targetX, this.targetY, this.targetX + Utilities.randomInt(-10, 10), this.targetY - 500);
            }

            this.projectile
               .newExpandingEnergyRing(
                  this.targetX, this.targetY, this.radius * 16, this.radius / 2, ParticleModule.ParticleSet.MAGIC_GREEN, ParticleModule.ParticleSet.FIRE_BLUE
               );
            this.sound.playSound(SoundModule.SoundType.MAGIC_HEAL_SPELL, this.targetX, this.targetY);
            this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
            this.coordinatesInRange = Utilities.getAllCoordinatesWithin(this.tileX, this.tileY, this.radius);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.spellStep++;
            break;
         case 1:
            if (this.durationTick > 80) {
               this.spellStep++;
            }
            break;
         case 2:
            for (int x = 0; x < 10; x++) {
               OrderedPair o = this.coordinatesInRange.get(Utilities.randomInt(this.coordinatesInRange.size()));
               int randomX = o.getX() * 16 + Utilities.randomInt(-16, 16);
               int randomY = o.getY() * 16 + Utilities.randomInt(-16, 16);
               this.particle.newParticleGroup(randomX, randomY, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.FIRE_GREEN);
            }

            for (int x = 0; x < 5; x++) {
               OrderedPair o = this.coordinatesInRange.get(Utilities.randomInt(this.coordinatesInRange.size()));
               int randomX = o.getX() * 16 + Utilities.randomInt(-16, 16);
               int randomY = o.getY() * 16 + Utilities.randomInt(-16, 16);
               this.particle.newParticleGroup(randomX, randomY, ParticleModule.ParticleGroup.MAGIC_SMOKE, ParticleModule.ParticleSet.FIRE_GREEN);
            }

            if (this.durationTick % 3 == 0) {
               this.projectile.newEnergyRing(this.targetX, this.targetY, (this.radius - 1) * 16, ParticleModule.ParticleSet.MAGIC_GREEN);
               this.projectile.newEnergyRing(this.targetX, this.targetY, (this.radius - 2) * 16, ParticleModule.ParticleSet.MAGIC_BLUE);
            }

            if (this.durationTick % 5 == 0) {
               this.projectile.newEnergyRing(this.targetX, this.targetY, 8, ParticleModule.ParticleSet.MAGIC_GREEN);
            }

            if (this.durationTick % 120 == 0) {
               this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius, true, 1);
               this.mob.influenceFaithInArea(this.tileX, this.tileY, this.radius, 1, true);
            }

            ArrayList<MobBase> mobsInRange = this.mob.getMobsNearby(this.tileX, this.tileY, this.radius, MobBase.MobGroup.VILLAGER);
            mobsInRange.addAll(this.mob.getMobsNearby(this.tileX, this.tileY, this.radius, MobBase.MobGroup.ANIMAL));
            mobsInRange.addAll(this.mob.getMobsNearby(this.tileX, this.tileY, this.radius, MobBase.MobGroup.DOMESTICATED_ANIMAL));
            if (mobsInRange.size() > 0) {
               if (this.durationTick % 60 == 0) {
                  this.sound.playSound(SoundModule.SoundType.MAGIC_HEAL, this.targetX, this.targetY);
               }

               for (MobBase m : mobsInRange) {
                  if (this.durationTick % 20 == 0) {
                     int faithChance = m.getHitPoints() < m.getHitPointsMax() ? 30 : 150;
                     if (Utilities.randomInt(faithChance) == 0 && m.getMobFlags().getAllowFaith()) {
                        m.increaseFaith(1, true);
                     }

                     m.increaseHitPoints(1 + (int)(m.getHitPointsMax() * 0.01F));
                     if (Utilities.randomInt(10) == 0) {
                        m.resetBlightAmount();
                        m.resetPoisonedAmount();
                     }
                  }

                  this.particle
                     .newParticleGroup(
                        m.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                        m.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                        ParticleModule.ParticleGroup.MAGIC_FIRE,
                        ParticleModule.ParticleSet.FIRE_GREEN
                     );
                  if (this.durationTick % 5 == 0) {
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

            mobsInRange = this.mob.getMobsNearby(this.tileX, this.tileY, this.radius, MobBase.MobGroup.MONSTER);
            if (mobsInRange.size() > 0) {
               if (this.durationTick % 60 == 0) {
                  this.sound.playSound(SoundModule.SoundType.MAGIC_HEAL, this.targetX, this.targetY);
               }

               for (MobBase m : mobsInRange) {
                  if (m.getMobType() == MobBase.MobType.SKELETON
                     || m.getMobType() == MobBase.MobType.ZOMBIE
                     || m.getMobType() == MobBase.MobType.ZOMBIE_CATJEET
                     || m.getMobType() == MobBase.MobType.ZOMBIE_CATJEET_CHILD
                     || m.getMobType() == MobBase.MobType.ZOMBIE_CHILD
                     || m.getMobType() == MobBase.MobType.ZOMBIE_DOGGO
                     || m.getMobType() == MobBase.MobType.ZOMBIE_DOOFY_DOGGO
                     || m.getMobType() == MobBase.MobType.HEADLESS) {
                     if (this.durationTick % 200 == 0) {
                        m.takeDamage(
                           (int)(m.getHitPointsMax() * 0.05F),
                           m.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                           m.getBottomPixelY(true) + Utilities.randomInt(-8, 8),
                           MobBase.DamageType.MAGIC_REGULAR,
                           MobBase.DamageCause.GOD
                        );
                     }

                     this.particle
                        .newParticleGroup(
                           m.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                           m.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                           ParticleModule.ParticleGroup.MAGIC_FIRE,
                           ParticleModule.ParticleSet.FIRE_GREEN
                        );
                     if (this.durationTick % 5 == 0) {
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
