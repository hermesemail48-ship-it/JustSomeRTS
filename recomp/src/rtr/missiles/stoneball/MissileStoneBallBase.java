package rtr.missiles.stoneball;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.missiles.MissileBase;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissileStoneBallBase extends MissileBase {
   @Override
   public void update() throws SlickException {
      super.update();
      this.velMod = this.velMod - this.friction;
      if (this.velocity + this.velMod <= 0.0F) {
         this.isActive = false;
      } else if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[this.tileX][this.tileY] > 0
         && this.collision.checkParticleCollision((int)this.missileX, (int)this.missileY, CollisionModule.CollisionHeight.MEDIUM)
            != this.collision.getCollisionNone()) {
         this.velMod--;
         if (this.resource.getResourceValues()[this.tileX][this.tileY] > 0) {
            this.resource.damageResource(this.tileX, this.tileY);
         }
      } else {
         float oldX = this.missileX;
         float oldY = this.missileY;
         this.missileX = this.missileX + (float)Math.cos(this.angle) * (this.velocity + this.velMod);
         this.missileY = this.missileY + (float)Math.sin(this.angle) * (this.velocity + this.velMod);
         this.runFlightEffects(oldX, oldY);
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

                  this.velMod -= 0.05F;
                  this.angle = this.angle + (Utilities.randomFloat() / 4.0F - Utilities.randomFloat() / 4.0F);
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
         this.velMod -= 0.05F;
         this.angle = this.angle + (Utilities.randomFloat() / 4.0F - Utilities.randomFloat() / 4.0F);
         this.runHitEffects();
      }
   }

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
                     ParticleModule.ParticleSet.DARK_GRAY
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

   protected void runHitEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         this.projectile.newSmallBlast((int)this.missileX, (int)this.missileY, ParticleModule.ParticleSet.BLACK);
         this.particles
            .newParticle(
               this.missileX + Utilities.randomInt(-3, 3),
               this.missileY + Utilities.randomInt(-3, 3),
               ParticleModule.ParticleType.SMOKE_SPECK,
               ParticleModule.ParticleSet.GRAY
            );
      }
   }
}
