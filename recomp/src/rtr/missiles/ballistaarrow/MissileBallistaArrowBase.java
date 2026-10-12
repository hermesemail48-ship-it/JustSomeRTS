package rtr.missiles.ballistaarrow;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.map.CollisionModule;
import rtr.missiles.MissileBase;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissileBallistaArrowBase extends MissileBase {
   @Override
   public void update() throws SlickException {
      super.update();
      this.velMod = this.velMod - this.friction;
      if (this.decayRateTick > 150) {
         this.velMod--;
      }

      if (this.velocity + this.velMod <= 0.0F) {
         this.isActive = false;
         this.isTop = false;
      } else {
         this.missileX = this.missileX + (float)Math.cos(this.angle) * (this.velocity + this.velMod);
         this.missileY = this.missileY + (float)Math.sin(this.angle) * (this.velocity + this.velMod);
         this.runFlightEffects();
         ArrayList<MobBase> mobsHit = this.mob.getMobsNearby(this.tileX, this.tileY, 2, this.mobGroup);
         if (mobsHit.size() > 0) {
            for (MobBase m : mobsHit) {
               if (this.canMissileHitTarget(m)) {
                  if (Utilities.randomInt(5) != 0 && m != this.targetMob) {
                     for (MobBase.DamageType t : this.damageTypesMin.keySet()) {
                        m.takeDamage(
                           Utilities.randomInt(this.damageTypesMin.get(t) / 10, this.damageTypesMax.get(t) / 10),
                           this.missileX,
                           this.missileY,
                           this.firingMob,
                           this.firingObject,
                           t,
                           this.damageCause
                        );
                     }
                  } else {
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

                     this.missile.remove(this);
                  }

                  this.runHitEffects();
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
         this.runHitEffects();
         this.missile.remove(this);
      }
   }

   protected void runFlightEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 3; i++) {
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

   protected void runHitEffects() throws SlickException {
      this.sound.playSound(SoundModule.SoundType.ARROW_HIT, this.missileX, this.missileY);
   }
}
