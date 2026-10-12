package rtr.missiles.sprayBullet;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissileLightningSprayBullet extends MissileSprayBulletBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.LIGHTNING_SPRAY_BULLET;
      this.decayRate = 5000;
      this.damageTypesMin.put(MobBase.DamageType.CRUSHING, 3);
      this.damageTypesMax.put(MobBase.DamageType.CRUSHING, 4);
      this.damageTypesMin.put(MobBase.DamageType.ELECTRIC, 1);
      this.damageTypesMax.put(MobBase.DamageType.ELECTRIC, 2);
      this.velocity = 4.0F + (Utilities.randomFloat() - Utilities.randomFloat());
      this.friction = 0.02F + (Utilities.randomFloat() - Utilities.randomFloat()) / 100.0F;
      this.isTop = true;
      if (this.targetMob != null) {
         this.angle = Utilities.calculateProjectileAngle(
            x, y, this.targetX, this.targetY, this.velocity, this.friction, this.targetMob.getAngle(), this.targetMob.getCurrentVelocity()
         );
      } else {
         this.angle = Utilities.calculateProjectileAngle(x, y, this.targetX, this.targetY, this.velocity, this.friction, 0.0F, 0.0F);
      }

      this.angle = this.angle + (Utilities.randomFloat() - Utilities.randomFloat()) / 2.0F;
      this.missileImage = this.missile.getMissileImage(this.missileType);
      this.updateMissileAngle();
   }

   @Override
   protected void runFlightEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         this.particles
            .newParticleGroup(
               this.missileX + Utilities.randomInt(-1, 1),
               this.missileY + Utilities.randomInt(-1, 1),
               ParticleModule.ParticleGroup.FIRE,
               ParticleModule.ParticleSet.MAGIC_YELLOW
            );
         if (Utilities.randomInt(4) == 0) {
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
      this.sound.playSound(SoundModule.SoundType.SPRAY_BULLET_HIT, this.missileX, this.missileY);
      if (this.map.isInView(this.missileX, this.missileY, 10) && Utilities.randomInt(4) == 0) {
         this.projectile
            .newSmallBlast(
               (int)this.missileX + Utilities.randomInt(-1, 1), (int)this.missileY + Utilities.randomInt(-1, 1), ParticleModule.ParticleSet.MAGIC_YELLOW
            );
      }
   }
}
