package rtr.missiles.sprayBullet;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.missiles.MissileBase;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissileSprayBulletBase extends MissileBase {
   @Override
   public void update() throws SlickException {
      super.update();
      this.velMod = this.velMod - this.friction;
      if (this.decayRateTick > 50) {
         this.velMod -= 0.5F;
      }

      if (this.velocity + this.velMod <= 0.0F) {
         this.isActive = false;
         this.isTop = false;
      } else if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[this.tileX][this.tileY] > 0
         && this.collision.checkParticleCollision((int)this.missileX, (int)this.missileY, CollisionModule.CollisionHeight.HIGH)
            != this.collision.getCollisionNone()) {
         this.velMod -= 2.0F;
      } else {
         this.missileX = this.missileX + (float)Math.cos(this.angle) * (this.velocity + this.velMod);
         this.missileY = this.missileY + (float)Math.sin(this.angle) * (this.velocity + this.velMod);
         this.runFlightEffects();
         ArrayList<MobBase> mobsHit = this.mob.getMobsNearby(this.tileX, this.tileY, 2, this.mobGroup);
         if (mobsHit.size() > 0) {
            for (MobBase m : mobsHit) {
               if (this.canMissileHitTarget(m)) {
                  for (MobBase.DamageType t : this.damageTypesMin.keySet()) {
                     m.takeDamage(
                        Utilities.randomInt(this.damageTypesMin.get(t), this.damageTypesMax.get(t)),
                        this.missileX,
                        this.missileY,
                        this.firingMob,
                        this.firingObject,
                        t,
                        this.damageCause
                     );
                  }

                  if (Utilities.randomInt(3) == 0) {
                     this.runHitEffects();
                     this.missile.remove(this);
                  } else {
                     this.velMod -= 0.2F;
                  }

                  return;
               }
            }
         }

         if (this.collision.checkParticleCollision((int)this.missileX, (int)this.missileY, CollisionModule.CollisionHeight.ANY)
            != this.collision.getCollisionNone()) {
            if (this.object.objectExists(this.targetObject)) {
               ObjectBase possibleTarget = this.object.getBuildingAtLocation((int)(this.missileX / 16.0F), (int)(this.missileY / 16.0F));
               if (this.targetObject == possibleTarget) {
                  this.hitTargetObject(this.targetObject);
                  return;
               }
            }

            if (this.collision.checkParticleCollision((int)this.missileX, (int)this.missileY, CollisionModule.CollisionHeight.HIGH)
               != this.collision.getCollisionNone()) {
               ObjectBase possibleTarget = this.object.getBuildingAtLocation((int)(this.missileX / 16.0F), (int)(this.missileY / 16.0F));
               if (possibleTarget != null && possibleTarget.getObjectFlags().getMobGroup() == this.mobGroup) {
                  this.hitTargetObject(possibleTarget);
                  return;
               }
            }
         }

         if (this.object.objectExists(this.targetObject) && this.canMissileHitTarget((int)this.targetX, (int)this.targetY)) {
            ObjectBase possibleTarget = this.object.getBuildingAtLocation((int)(this.missileX / 16.0F), (int)(this.missileY / 16.0F));
            if (this.targetObject == possibleTarget) {
               this.hitTargetObject(this.targetObject);
               return;
            }
         }
      }
   }

   private void hitTargetObject(ObjectBase o) throws SlickException {
      for (MobBase.DamageType t : this.damageTypesMin.keySet()) {
         o.takeDamage(Utilities.randomInt(this.damageTypesMin.get(t), this.damageTypesMax.get(t)), this.firingMob, this.firingObject, t, this.damageCause);
         this.velMod -= 0.2F;
         this.runHitEffects();
      }
   }

   protected void runLaunchEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 5; i++) {
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

   protected void runFlightEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10) && Utilities.randomInt(4) == 0) {
         this.particles
            .newParticleGroup(
               this.missileX + Utilities.randomInt(-1, 1),
               this.missileY + Utilities.randomInt(-1, 1),
               ParticleModule.ParticleGroup.SMOKE_BELLOW,
               ParticleModule.ParticleSet.GRAY
            );
      }
   }

   protected void runHitEffects() throws SlickException {
      this.sound.playSound(SoundModule.SoundType.SPRAY_BULLET_HIT, this.missileX, this.missileY);
   }
}
