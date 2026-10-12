package rtr.missiles.ballistaarrow;

import org.newdawn.slick.SlickException;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissileIceBallistaArrow extends MissileBallistaArrowBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.ICE_BALLISTA_ARROW;
      this.decayRate = 5000;
      this.damageTypesMin.put(MobBase.DamageType.PIERCING, 125);
      this.damageTypesMax.put(MobBase.DamageType.PIERCING, 200);
      this.damageTypesMin.put(MobBase.DamageType.ICE, 50);
      this.damageTypesMax.put(MobBase.DamageType.ICE, 75);
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
                  ParticleModule.ParticleSet.FIRE_BLUE
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
   protected void runHitEffects() throws SlickException {
      super.runHitEffects();
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 3; i++) {
            this.projectile
               .newSmallBlast(
                  (int)this.missileX + Utilities.randomInt(-1, 1), (int)this.missileY + Utilities.randomInt(-1, 1), ParticleModule.ParticleSet.FIRE_BLUE
               );
         }
      }
   }
}
