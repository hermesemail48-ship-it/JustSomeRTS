package rtr.missiles.recombobulatorbolt;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.missiles.MissileBase;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissileRecombobulatorBoltBase extends MissileBase {
   @Override
   public void update() throws SlickException {
      super.update();
      this.velMod += 0.005F;
      if (this.targetMob != null && this.targetMob.isAvailable()) {
         this.targetX = this.targetMob.getCenterPixelX(true);
         this.targetY = this.targetMob.getCenterPixelY(true);
      }

      if (this.targetMob != null && this.targetMob.isAvailable() && this.targetMob.getHitPoints() < this.targetMob.getHitPointsMax()) {
         if (Math.abs(this.missileX - this.targetX) > 2.0F) {
            if (this.missileX > this.targetX) {
               this.velX = this.velX - this.velMod;
            }

            if (this.missileX < this.targetX) {
               this.velX = this.velX + this.velMod;
            }
         }

         if (Math.abs(this.missileY - this.targetY) > 2.0F) {
            if (this.missileY > this.targetY) {
               this.velY = this.velY - this.velMod;
            }

            if (this.missileY < this.targetY) {
               this.velY = this.velY + this.velMod;
            }
         }

         if (this.velX > 2.0F) {
            this.velX = 2.0F;
         }

         if (this.velX < -2.0F) {
            this.velX = -2.0F;
         }

         if (this.velY > 2.0F) {
            this.velY = 2.0F;
         }

         if (this.velY < -2.0F) {
            this.velY = -2.0F;
         }
      }

      this.missileX = this.missileX + this.velX;
      this.missileY = this.missileY + this.velY;
      this.updateMissileAngle();
      this.runFlightEffects();
      if (this.decayRateTick > 20
         && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[this.tileX][this.tileY] > 0
         && this.collision.checkParticleCollision((int)this.missileX, (int)this.missileY, CollisionModule.CollisionHeight.HIGH)
            != this.collision.getCollisionNone()) {
         this.runHitEffects();
         this.missile.remove(this);
      } else {
         ArrayList<MobBase> mobsHit = this.mob.getMobsNearby(this.tileX, this.tileY, 2, this.mobGroup);
         if (mobsHit.size() > 0) {
            for (MobBase m : mobsHit) {
               if (this.canMissileHitTarget(m) && m.getHitPoints() < m.getHitPointsMax()) {
                  m.increaseHitPoints((int)(m.getHitPointsMax() * 0.25F));
                  this.runHitEffects(m);
                  this.missile.remove(this);
                  return;
               }
            }
         }
      }
   }

   protected void runFlightEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 3; i++) {
            this.particles.newParticleGroup(this.missileX, this.missileY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_GREEN);
         }

         this.particles.newParticleGroup(this.missileX, this.missileY, ParticleModule.ParticleGroup.MAGIC_FALL, ParticleModule.ParticleSet.MAGIC_GREEN);
      }
   }

   protected void runHitEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 25; i++) {
            this.particles
               .newParticle(
                  this.missileX + Utilities.randomInt(-2, 2),
                  this.missileY + Utilities.randomInt(-2, 2),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.MAGIC_GREEN
               );
         }
      }
   }

   protected void runHitEffects(MobBase m) throws SlickException {
      this.sound.playSound(SoundModule.SoundType.MAGIC_HEAL, this.missileX, this.missileY);
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 25; i++) {
            this.particles
               .newParticle(
                  m.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                  m.getCenterPixelY(true) + Utilities.randomInt(-4, 4),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.MAGIC_GREEN
               );
         }
      }
   }
}
