package rtr.influence;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class SpellRegenerate extends SpellBase {
   protected MobBase target;

   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.REGENERATE;
      this.name = Text.getText("spell.regenerate.name");
      this.description = Text.getText("spell.regenerate.description");
      this.cooldown = 30;
      this.castCost = freeCast ? 0 : 250;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.REGENERATION_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.REGENERATION_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 2100;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.REGENERATION_SPELL_DURATION) > 0.0) {
         this.duration = (int)(this.duration + 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.REGENERATION_SPELL_DURATION));
      }
   }

   @Override
   public void update() throws SlickException {
      switch (this.spellStep) {
         case 0:
            ArrayList<MobBase> mobListRaw = this.mob.getMobsNearby(this.tileX, this.tileY, 2);
            ArrayList<MobBase> mobList = new ArrayList<>();

            for (MobBase m : mobListRaw) {
               if (m.getMobGroup() != MobBase.MobGroup.GOLEM && m.getMobGroup() != MobBase.MobGroup.GHOST && m.isAvailable()) {
                  mobList.add(m);
               }
            }

            if (mobList.size() <= 0) {
               this.influence.removeSpell(this);
            } else {
               Collections.shuffle(mobList);

               for (MobBase mx : mobList) {
                  if (mx.getMobGroup() != MobBase.MobGroup.MONSTER && mx.getHitPoints() < mx.getHitPointsMax()) {
                     this.target = mx;
                     break;
                  }
               }

               if (this.target == null) {
                  for (MobBase mxx : mobList) {
                     if (mxx.getMobGroup() != MobBase.MobGroup.MONSTER) {
                        this.target = mxx;
                        break;
                     }
                  }

                  if (this.target == null) {
                     for (MobBase mxxx : mobList) {
                        if (mxxx.getHitPoints() < mxxx.getHitPointsMax()) {
                           this.target = mxxx;
                           break;
                        }
                     }

                     if (this.target == null) {
                        this.target = mobList.get(0);
                     }
                  }
               }

               this.projectile.newExpandingEnergyRing(this.targetX, this.targetY, 32, 2.0F, ParticleModule.ParticleSet.FIRE_GREEN);
               this.projectile.newConstrictingEnergyRing(this.targetX, this.targetY, 32, 2.0F, ParticleModule.ParticleSet.FIRE_GREEN);
               this.projectile.newEnergyBeam(this.target.getCenterPixelX(true), this.target.getCenterPixelY(true), this.targetX, this.targetY);
               this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
               this.mob.tryConfuseInArea(this.tileX, this.tileY, 4, true, 1);
               this.mob.influenceFaithInArea(this.tileX, this.tileY, 4, 1, 2, this.target.getMobGroup() != MobBase.MobGroup.MONSTER);
               this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
               this.influence.decreaseInfluenceAmount(this.castCost);
               this.spellStep++;
            }
            break;
         case 1:
            this.durationTick++;
            if (this.durationTick % 3 == 0) {
               this.particle
                  .newParticle(
                     this.target.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                     this.target.getBottomPixelY(true) + Utilities.randomInt(-8, 8),
                     ParticleModule.ParticleType.MAGIC_SMOKE_SPECK,
                     ParticleModule.ParticleSet.MAGIC_GREEN
                  );
            }

            if (this.durationTick % 10 == 0) {
               this.lighting.addShortTermLight(this.target.getTileX(), this.target.getTileY(), (byte)2, (byte)16, 10);
            }

            if (this.durationTick % 100 == 0) {
               for (int i = 0; i < 100; i++) {
                  this.particle
                     .newParticle(
                        this.target.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                        this.target.getBottomPixelY(true) + Utilities.randomInt(-8, 8),
                        ParticleModule.ParticleType.MAGIC_SMOKE_SPECK,
                        ParticleModule.ParticleSet.MAGIC_GREEN
                     );
               }

               if (this.target.getMobType() != MobBase.MobType.SKELETON
                  && this.target.getMobType() != MobBase.MobType.ZOMBIE
                  && this.target.getMobType() != MobBase.MobType.ZOMBIE_CATJEET
                  && this.target.getMobType() != MobBase.MobType.ZOMBIE_CATJEET_CHILD
                  && this.target.getMobType() != MobBase.MobType.ZOMBIE_CHILD
                  && this.target.getMobType() != MobBase.MobType.ZOMBIE_DOGGO
                  && this.target.getMobType() != MobBase.MobType.ZOMBIE_DOOFY_DOGGO
                  && this.target.getMobType() != MobBase.MobType.HEADLESS) {
                  this.target.increaseHitPoints((int)(this.target.getHitPointsMax() * 0.05));
                  if (Utilities.randomInt(10) == 0) {
                     this.target.resetBlightAmount();
                     this.target.resetPoisonedAmount();
                  }
               } else {
                  this.target
                     .takeDamage(
                        (int)(this.target.getHitPointsMax() * 0.05),
                        this.target.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                        this.target.getBottomPixelY(true) + Utilities.randomInt(-8, 8),
                        MobBase.DamageType.MAGIC_REGULAR,
                        MobBase.DamageCause.GOD
                     );
               }
            }

            if ((this.durationTick + 20) % 100 == 0) {
               this.projectile
                  .newConstrictingEnergyRing(
                     this.target.getCenterPixelX(true), this.target.getBottomPixelY(true), 32, 1.0F, ParticleModule.ParticleSet.MAGIC_GREEN
                  );
            }

            if (this.durationTick > this.duration || this.target.isDead()) {
               this.influence.removeSpell(this);
            }
      }
   }
}
