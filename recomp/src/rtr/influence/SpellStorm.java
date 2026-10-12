package rtr.influence;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class SpellStorm extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.STORM;
      this.name = Text.getText("spell.storm.name");
      this.description = Text.getText("spell.storm.description");
      this.cooldown = 1800;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.STORM_SPELL_COOLDOWN) > 0.0) {
         this.cooldown = (int)(this.cooldown - 60.0 * this.perk.getPerkTypeTotal(PerkModule.PerkType.STORM_SPELL_COOLDOWN));
      }

      this.castCost = freeCast ? 0 : 1500;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.STORM_SPELL_COST) > 0.0) {
         this.castCost = (int)(this.castCost - this.castCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.STORM_SPELL_COST));
      }

      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      this.duration = 1800;
      this.radius = 32;
   }

   @Override
   public void update() throws SlickException {
      switch (this.spellStep) {
         case 0:
            this.lighting.addShortTermLight(this.tileX, this.tileY, (byte)8, (byte)6, this.duration);
            this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.CASTING_SPELLS);
            this.influence.decreaseInfluenceAmount(this.castCost);
            this.spellStep++;
            break;
         case 1:
            this.durationTick++;
            if (this.durationTick % 10 == 0) {
               this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius - 8, true, 2);
            }

            for (int i = 0; i < 10; i++) {
               this.projectile.newEnergyRing(this.targetX, this.targetY, 410 - this.durationTick, ParticleModule.ParticleSet.MAGIC_BLUE);
            }

            this.particle
               .newParticle(
                  this.targetX + Utilities.randomInt(-2, 2),
                  this.targetY + Utilities.randomInt(-2, 2),
                  ParticleModule.ParticleType.FIRE_SPECK,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
            this.particle.newParticle(this.targetX, this.targetY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.MAGIC_BLUE);
            if (this.durationTick > 400) {
               this.durationTick = 0;
               this.spellStep++;
            }
            break;
         case 2:
            this.durationTick++;
            if (this.durationTick % 10 == 0) {
               this.mob.tryConfuseInArea(this.tileX, this.tileY, this.radius - 8, true, 2);
            }

            this.particle
               .newParticle(
                  this.targetX + Utilities.randomInt(-2, 2),
                  this.targetY + Utilities.randomInt(-2, 2),
                  ParticleModule.ParticleType.FIRE_SPECK,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
            this.particle.newParticle(this.targetX, this.targetY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.MAGIC_BLUE);
            this.projectile.newExpandingEnergyTrail(this.targetX, this.targetY, ParticleModule.ParticleSet.MAGIC_BLUE);
            if (Utilities.randomInt(100) == 0) {
               this.weather.newLightning(Utilities.randomInt(256), Utilities.randomInt(256), this.radius * 16, true, ParticleModule.ParticleSet.MAGIC_BLUE);
            }

            if (Utilities.randomInt(30) == 0) {
               this.map.initSreenShake(Utilities.randomInt(3));
            }

            if (this.durationTick > 400) {
               this.durationTick = 0;

               for (int i = 0; i < 4; i++) {
                  this.projectile.newExpandingFireRing(this.targetX, this.targetY, this.radius * 16, 2 + i * 2, ParticleModule.ParticleSet.FIRE_BLUE);
               }

               this.weather.removeDisasterTemp(Utilities.randomInt(5) + 10.0);
               this.sound.playSound(SoundModule.SoundType.HEAVY_WIND, this.targetX, this.targetY);
               this.spellStep++;
            }
            break;
         case 3:
            this.durationTick++;
            if (this.durationTick < 500) {
               for (int i = 0; i < this.durationTick / 16; i++) {
                  this.projectile.newEnergyRing(this.targetX, this.targetY, this.durationTick, ParticleModule.ParticleSet.MAGIC_BLUE);
               }
            }

            if (Utilities.randomInt(80) == 0) {
               this.weather
                  .newLightning(
                     this.tileX + Utilities.randomInt(-16, 16),
                     this.tileY + Utilities.randomInt(-16, 16),
                     this.radius * 16,
                     true,
                     ParticleModule.ParticleSet.MAGIC_BLUE
                  );
            }

            if (Utilities.randomInt(20) == 0) {
               this.weather
                  .newLightning(
                     this.tileX + Utilities.randomInt(-8, 8),
                     this.tileY + Utilities.randomInt(-8, 8),
                     this.radius * 16,
                     true,
                     ParticleModule.ParticleSet.MAGIC_BLUE
                  );
            }

            if (Utilities.randomInt(200) == 0) {
               this.weather.newLightning(Utilities.randomInt(256), Utilities.randomInt(256), this.radius * 16, true, ParticleModule.ParticleSet.MAGIC_BLUE);
            }

            if (Utilities.randomInt(20) == 0) {
               this.map.initSreenShake(Utilities.randomInt(5));
            }

            if (this.durationTick >= 300 || Utilities.randomInt(350 - this.durationTick) == 0) {
               this.particle
                  .newParticle(
                     this.targetX + Utilities.randomInt(-2, 2),
                     this.targetY + Utilities.randomInt(-2, 2),
                     ParticleModule.ParticleType.FIRE_SPECK,
                     ParticleModule.ParticleSet.FIRE_BLUE
                  );
               this.particle.newParticle(this.targetX, this.targetY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.MAGIC_BLUE);
            }

            int randomX = (int)(Utilities.randomInt(4096) - this.map.getMapX());
            int randomY = (int)(Utilities.randomInt(4096) - this.map.getMapY());
            int startX = this.map.getMapRenderTopLeftX() - 2;
            int startY = this.map.getMapRenderTopLeftY() - 2;
            int endX = this.map.getMapRenderBottomRightX() + 2;
            int endY = this.map.getMapRenderBottomRightY() + 2;
            if (randomX >= startX && randomX <= endX && randomY >= startY && randomY <= endY) {
               this.projectile.newExpandingEnergyTrail(randomX, randomY, ParticleModule.ParticleSet.MAGIC_BLUE);
            }

            if (Utilities.randomInt(this.durationTick / 2 + 50 - this.durationTick / 2) == 0) {
               this.projectile.newMagicTrail(this.targetX, this.targetY, ParticleModule.ParticleSet.MAGIC_BLUE);
               this.projectile.newExpandingEnergyTrail(this.targetX, this.targetY, ParticleModule.ParticleSet.MAGIC_BLUE);
            }

            if (this.durationTick == this.duration / 2) {
               this.weather.startWeather();
            }

            if (this.durationTick > this.duration) {
               this.mob.influenceFaithInArea(this.tileX, this.tileY, this.radius - 4, 2, 5, true);
               this.influence.removeSpell(this);
            }
      }
   }
}
