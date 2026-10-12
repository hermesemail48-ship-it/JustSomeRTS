package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class SpellMend extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.MEND;
      this.name = Text.getText("spell.mend.name");
      this.description = Text.getText("spell.mend.description");
      this.cooldown = 30;
      this.castCost = freeCast ? 0 : 800;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MEND_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.MEND_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 1020;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MEND_SPELL_DURATION) > 0.0) {
         this.duration = (int)(this.duration + 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.MEND_SPELL_DURATION));
      }

      this.radius = 6;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MEND_SPELL_RADIUS) > 0.0) {
         this.radius = (int)(this.radius + this.perk.getPerkTypeTotal(PerkModule.PerkType.MEND_SPELL_RADIUS));
      }
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)this.radius, (byte)16, this.duration);

            for (int x = 0; x < 50; x++) {
               this.projectile.newGroundStatic(this.targetX, this.targetY, ParticleModule.ParticleSet.FIRE_GREEN);
            }

            this.projectile.newExpandingFireRing(this.targetX, this.targetY, this.radius * 16, 4.0F, ParticleModule.ParticleSet.MAGIC_YELLOW);
            this.sound.playSound(SoundModule.SoundType.MAGIC_HEAL_SPELL, this.targetX, this.targetY);
            this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.targetX, this.targetY);
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
               int randomX = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
               int randomY = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
               this.particle
                  .newParticleGroup(
                     this.targetX + randomX, this.targetY + randomY, ParticleModule.ParticleGroup.SPARKLES, ParticleModule.ParticleSet.FIRE_GREEN
                  );
            }

            if (this.durationTick % 10 == 0) {
               int randomX = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
               int randomY = Utilities.randomInt(this.radius * 16 * -1, this.radius * 16);
               this.particle
                  .newParticleGroup(
                     this.targetX + randomX, this.targetY + randomY, ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.FIRE_GREEN
                  );
            }

            if (this.durationTick % 60 == 0) {
               this.projectile.newExpandingFireRing(this.targetX, this.targetY, this.radius * 16, 0.5F, ParticleModule.ParticleSet.FIRE_YELLOW);
            }

            if (this.durationTick % 5 == 0) {
               this.projectile.newEnergyRing(this.targetX, this.targetY, 8, ParticleModule.ParticleSet.MAGIC_GREEN);
            }

            ArrayList<MobBase> mobsInRange = this.mob.getMobsNearby(this.tileX, this.tileY, this.radius, MobBase.MobGroup.GOLEM);
            if (mobsInRange.size() > 0) {
               if (this.durationTick % 60 == 0) {
                  this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_ROCKS, this.targetX, this.targetY);
               }

               if (this.durationTick % 20 == 0 && Utilities.randomInt(5) == 0) {
                  this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.targetX, this.targetY);
               }

               for (MobBase m : mobsInRange) {
                  if (this.durationTick % 20 == 0) {
                     this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius, true, 3);
                     m.increaseHitPoints(1 + (int)(m.getHitPointsMax() * 0.01F));
                     if (Utilities.randomInt(10) == 0) {
                        m.resetBlightAmount();
                        m.resetPoisonedAmount();
                     }
                  }

                  if (this.durationTick % 2 == 0) {
                     this.particle
                        .newParticleGroup(
                           m.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                           m.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                           ParticleModule.ParticleGroup.MAGIC_FIRE,
                           ParticleModule.ParticleSet.FIRE_YELLOW
                        );
                  }

                  if (this.durationTick % 20 == 0) {
                     this.particle
                        .newParticleGroup(
                           m.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                           m.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                           ParticleModule.ParticleGroup.SMOKE_BELLOW,
                           ParticleModule.ParticleSet.MAGIC_YELLOW
                        );
                  }
               }
            }

            ArrayList<ObjectBase> objectsInRange = this.object.getObjectsNearby(this.tileX, this.tileY, this.radius);
            if (objectsInRange.size() > 0) {
               if (this.durationTick % 60 == 0) {
                  this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_ROCKS, this.targetX, this.targetY);
               }

               if (this.durationTick % 20 == 0 && Utilities.randomInt(5) == 0) {
                  this.sound.playSound(SoundModule.SoundType.STORE_ITEM, this.targetX, this.targetY);
               }

               for (ObjectBase o : objectsInRange) {
                  if (this.durationTick % 20 == 0) {
                     o.increaseHitPoints(1 + (int)(o.getHitPointsMax() * 0.01F));
                     o.putOutFire();
                  }

                  if (this.durationTick % 60 == 0) {
                     this.mob.tryConfuseInArea(o.getCenterCoordinateX(), o.getCenterCoordinateY(), this.radius, true, 3);
                     if (o.getObjectFlags().getMobGroup() == MobBase.MobGroup.VILLAGER) {
                        this.mob.influenceFaithInArea(o.getCenterCoordinateX(), o.getCenterCoordinateY(), this.radius, 1, true);
                     }
                  }

                  int effects = o.getObjectFlags().getWidth() * o.getObjectFlags().getHeight() / 2;
                  if (effects <= 0) {
                     effects = 1;
                  }

                  for (int s = 0; s < effects; s++) {
                     OrderedPair randomCoords = o.getRandomCoordinates(true);
                     int randomX = randomCoords.getX() * 16 + 8 + Utilities.randomInt(-8, 8);
                     int randomY = randomCoords.getY() * 16 + 8 + Utilities.randomInt(-8, 8);
                     int amount = this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[randomCoords.getX()][randomCoords.getY()] == 1 ? 5 : 1;

                     for (int x = 0; x < amount; x++) {
                        if (this.durationTick % 10 == 0) {
                           this.particle
                              .newParticleGroup(
                                 randomX + Utilities.randomInt(-1, 1),
                                 randomY + Utilities.randomInt(-1, 1),
                                 ParticleModule.ParticleGroup.MAGIC_FIRE,
                                 ParticleModule.ParticleSet.FIRE_YELLOW
                              );
                        }

                        if (this.durationTick % 100 == 0) {
                           this.particle
                              .newParticleGroup(
                                 randomX + Utilities.randomInt(-1, 1),
                                 randomY + Utilities.randomInt(-1, 1),
                                 ParticleModule.ParticleGroup.SMOKE_BELLOW,
                                 ParticleModule.ParticleSet.MAGIC_YELLOW
                              );
                        }
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
