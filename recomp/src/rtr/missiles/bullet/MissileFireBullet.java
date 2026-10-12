package rtr.missiles.bullet;

import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissileFireBullet extends MissileBulletBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.FIRE_BULLET;
      this.decayRate = 5000;
      this.damageTypesMin.put(MobBase.DamageType.CRUSHING, 12);
      this.damageTypesMax.put(MobBase.DamageType.CRUSHING, 15);
      this.damageTypesMin.put(MobBase.DamageType.FIRE, 4);
      this.damageTypesMax.put(MobBase.DamageType.FIRE, 6);
      this.velocity = 4.0F + Utilities.randomFloat() * 2.0F;
      this.friction = 0.01F;
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
         for (int i = 0; i < 3; i++) {
            this.particles
               .newParticleGroup(
                  this.missileX + Utilities.randomInt(-1, 1),
                  this.missileY + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleGroup.FIRE,
                  ParticleModule.ParticleSet.FIRE_RED
               );
            this.particles
               .newParticleGroup(
                  this.missileX + Utilities.randomInt(-1, 1),
                  this.missileY + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.GRAY
               );
         }
      }
   }

   @Override
   protected void runHitEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 3; i++) {
            this.projectile
               .newSmallBlast(
                  (int)this.missileX + Utilities.randomInt(-1, 1), (int)this.missileY + Utilities.randomInt(-1, 1), ParticleModule.ParticleSet.FIRE_RED
               );
         }
      }
   }
}
