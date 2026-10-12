package rtr.missiles.elementalbolt;

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

public class MissileElementalBoltBase extends MissileBase {
   @Override
   public void update() throws SlickException {
      super.update();
      this.velMod += 0.005F;
      if (this.targetMob != null && this.targetMob.isAvailable()) {
         this.targetX = this.targetMob.getCenterPixelX(true);
         this.targetY = this.targetMob.getCenterPixelY(true);
      }

      if (this.targetMob != null && this.targetMob.isAvailable() || this.object.objectExists(this.targetObject)) {
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

         if (this.velX > 3.0F) {
            this.velX = 3.0F;
         }

         if (this.velX < -3.0F) {
            this.velX = -3.0F;
         }

         if (this.velY > 3.0F) {
            this.velY = 3.0F;
         }

         if (this.velY < -3.0F) {
            this.velY = -3.0F;
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
         if (this.resource.getResourceValues()[this.tileX][this.tileY] > 0) {
            this.resource.damageResource(this.tileX, this.tileY);
         }

         this.runHitEffects();
         this.missile.remove(this);
      } else {
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

                     for (MobBase m2 : this.mob.getMobsNearby(m.getTileX(), m.getTileY(), 2, m.getMobGroup())) {
                        if (m2.isAvailable()) {
                           m2.takeDamage(
                              Utilities.randomInt(this.damageTypesMin.get(t), this.damageTypesMax.get(t)) / 4,
                              this.missileX - 4.0F,
                              this.missileY - 4.0F,
                              this.object.getBuildingAtLocation((int)(this.originX / 16.0F), (int)(this.originY / 16.0F)),
                              t,
                              this.damageCause
                           );
                        }
                     }
                  }

                  this.runHitEffects();
                  this.missile.remove(this);
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

         for (ObjectBase o2 : this.object.getObjectsNearby(this.tileX, this.tileY, 2, this.mobGroup)) {
            if (this.object.objectExists(o2)) {
               o2.takeDamage(
                  Utilities.randomInt(this.damageTypesMin.get(t), this.damageTypesMax.get(t)), this.firingMob, this.firingObject, t, this.damageCause
               );
            }
         }

         this.runHitEffects();
         this.missile.remove(this);
      }
   }

   protected void runFlightEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 3; i++) {
            this.particles.newParticleGroup(this.missileX, this.missileY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.MAGIC_BLUE);
            this.particles
               .newParticleGroup(
                  this.missileX + Utilities.randomInt(-1, 1),
                  this.missileY + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleGroup.FIRE,
                  ParticleModule.ParticleSet.MAGIC_BLUE
               );
         }
      }
   }

   protected void runHitEffects() throws SlickException {
      this.sound.playSound(SoundModule.SoundType.MAGIC_HIT, this.missileX, this.missileY);
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 5; i++) {
            this.projectile.newSmallBlast((int)this.missileX, (int)this.missileY, ParticleModule.ParticleSet.MAGIC_BLUE);
         }
      }
   }
}
