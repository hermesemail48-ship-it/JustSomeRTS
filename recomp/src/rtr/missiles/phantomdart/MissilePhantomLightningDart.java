package rtr.missiles.phantomdart;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.lighting.Light;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissilePhantomLightningDart extends MissilePhantomDartBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.PHANTOM_LIGHTNING_DART;
      this.decayRate = 300;
      this.damageTypesMin.put(MobBase.DamageType.MAGIC_REGULAR, 8);
      this.damageTypesMax.put(MobBase.DamageType.MAGIC_REGULAR, 10);
      this.damageTypesMin.put(MobBase.DamageType.MAGIC_ELECTRIC, 4);
      this.damageTypesMax.put(MobBase.DamageType.MAGIC_ELECTRIC, 6);
      this.lightIntensity = 2;
      this.lightRadius = 16;
      this.light = new Light(this.tileX, this.tileY, this.lightIntensity, this.lightRadius, false);
      this.velocity = 3.0F + Utilities.randomFloat();
      this.isTop = true;
      if (this.targetMob != null) {
         this.angle = Utilities.calculateProjectileAngle(
            x, y, this.targetX, this.targetY, this.velocity, this.friction, this.targetMob.getAngle(), this.targetMob.getCurrentVelocity()
         );
      } else {
         this.angle = Utilities.calculateProjectileAngle(x, y, this.targetX, this.targetY, this.velocity, this.friction, 0.0F, 0.0F);
      }

      this.missileImage = this.missile.getMissileImage(this.missileType);
      this.updateMissileAngle();
   }

   @Override
   protected void runFlightEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         this.particles.newParticleGroup(this.missileX, this.missileY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_YELLOW);
         this.particles
            .newParticleGroup(
               this.missileX + Utilities.randomInt(-1, 1),
               this.missileY + Utilities.randomInt(-1, 1),
               ParticleModule.ParticleGroup.FIRE,
               ParticleModule.ParticleSet.MAGIC_YELLOW
            );
         if (Utilities.randomInt(3) == 0) {
            this.particles.newParticle(this.missileX, this.missileY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.FIRE_YELLOW);
         }
      }
   }

   @Override
   protected void runHitEffects() throws SlickException {
      this.sound.playSound(SoundModule.SoundType.PHANTNOM_DART_HIT, this.missileX, this.missileY);
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 2; i++) {
            this.projectile.newSmallBlast((int)this.missileX, (int)this.missileY, ParticleModule.ParticleSet.MAGIC_YELLOW);
         }
      }
   }
}
