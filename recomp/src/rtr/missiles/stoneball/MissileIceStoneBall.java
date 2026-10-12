package rtr.missiles.stoneball;

import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissileIceStoneBall extends MissileStoneBallBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.ICE_STONE_BALL;
      this.decayRate = 5000;
      this.damageTypesMin.put(MobBase.DamageType.CRUSHING, 8);
      this.damageTypesMax.put(MobBase.DamageType.CRUSHING, 10);
      this.damageTypesMin.put(MobBase.DamageType.ICE, 4);
      this.damageTypesMax.put(MobBase.DamageType.ICE, 6);
      this.velocity = 1.5F + Utilities.randomFloat();
      this.friction = 0.01F;
      this.isTop = false;
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
   protected void runFlightEffects(float oldX, float oldY) {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         float angle = (float)Math.toDegrees(Utilities.calculateAngle(this.missileX, this.missileY, oldX, oldY));

         for (int x = 0; x < (int)this.velocity + this.velMod; x++) {
            this.particles
               .newParticleGroup(
                  this.missileX + Utilities.randomInt(-2, 2),
                  this.missileY + Utilities.randomInt(-2, 2),
                  angle,
                  ParticleModule.ParticleGroup.DUST,
                  ParticleModule.ParticleSet.BLACK
               );

            for (int y = 0; y < 5; y++) {
               this.particles
                  .newParticle(
                     this.missileX + Utilities.randomInt(-3, 3),
                     this.missileY + Utilities.randomInt(-3, 3),
                     ParticleModule.ParticleType.FIRE_SPECK,
                     ParticleModule.ParticleSet.FIRE_BLUE
                  );
            }

            this.particles
               .newParticle(
                  this.missileX + Utilities.randomInt(-2, 2),
                  this.missileY + Utilities.randomInt(-2, 2),
                  ParticleModule.ParticleType.SPECK,
                  ParticleModule.ParticleSet.BLACK
               );
            this.particles
               .newParticle(
                  this.missileX + Utilities.randomInt(-1, 1),
                  this.missileY + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleType.SPECK,
                  ParticleModule.ParticleSet.BLACK
               );
         }
      }
   }

   @Override
   public void runHitEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         this.projectile.newSmallBlast((int)this.missileX, (int)this.missileY, ParticleModule.ParticleSet.BLACK);
         this.particles
            .newParticle(
               this.missileX + Utilities.randomInt(-3, 3),
               this.missileY + Utilities.randomInt(-3, 3),
               ParticleModule.ParticleType.SMOKE_SPECK,
               ParticleModule.ParticleSet.GRAY
            );

         for (int i = 0; i < 20; i++) {
            this.projectile.newSmallBlast((int)this.missileX, (int)this.missileY, ParticleModule.ParticleSet.FIRE_BLUE);
         }
      }
   }
}
