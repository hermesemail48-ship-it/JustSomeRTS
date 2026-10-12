package rtr.missiles.arrow;

import org.newdawn.slick.SlickException;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissileLightningArrow extends MissileArrowBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.LIGHTNING_ARROW;
      this.decayRate = 5000;
      this.damageTypesMin.put(MobBase.DamageType.PIERCING, 30);
      this.damageTypesMax.put(MobBase.DamageType.PIERCING, 40);
      this.damageTypesMin.put(MobBase.DamageType.ELECTRIC, 10);
      this.damageTypesMax.put(MobBase.DamageType.ELECTRIC, 15);
      this.velocity = 6.0F + Utilities.randomFloat();
      this.friction = 0.02F;
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
                  ParticleModule.ParticleSet.FIRE_YELLOW
               );
            this.particles
               .newParticleGroup(
                  this.missileX + Utilities.randomInt(-1, 1),
                  this.missileY + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.FIRE_YELLOW
               );
         }
      }
   }

   @Override
   protected void runHitEffects() throws SlickException {
      super.runHitEffects();
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 3; i++) {
            this.projectile
               .newSmallBlast(
                  (int)this.missileX + Utilities.randomInt(-1, 1), (int)this.missileY + Utilities.randomInt(-1, 1), ParticleModule.ParticleSet.FIRE_YELLOW
               );
         }
      }
   }
}
