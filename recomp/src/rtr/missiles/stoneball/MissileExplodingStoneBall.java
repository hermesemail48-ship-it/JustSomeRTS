package rtr.missiles.stoneball;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissileExplodingStoneBall extends MissileStoneBallBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.EXPLODING_STONE_BALL;
      this.decayRate = 5000;
      this.damageTypesMin.put(MobBase.DamageType.PIERCING, 10);
      this.damageTypesMax.put(MobBase.DamageType.PIERCING, 15);
      this.damageTypesMin.put(MobBase.DamageType.CRUSHING, 10);
      this.damageTypesMax.put(MobBase.DamageType.CRUSHING, 15);
      this.damageTypesMin.put(MobBase.DamageType.FIRE, 20);
      this.damageTypesMax.put(MobBase.DamageType.FIRE, 30);
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

                     for (MobBase m2 : this.mob.getMobsNearby(this.targetMob.getTileX(), this.targetMob.getTileY(), 2, this.targetMob.getMobGroup())) {
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
                  this.velMod -= 0.05F;
                  this.angle = this.angle + (Utilities.randomFloat() / 4.0F - Utilities.randomFloat() / 4.0F);
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
         this.velMod -= 0.05F;
         this.angle = this.angle + (Utilities.randomFloat() / 4.0F - Utilities.randomFloat() / 4.0F);
         this.runHitEffects();

         for (ObjectBase o2 : this.object.getObjectsNearby(this.tileX, this.tileY, 2, this.mobGroup)) {
            if (this.object.objectExists(o2)) {
               o2.takeDamage(
                  Utilities.randomInt(this.damageTypesMin.get(t), this.damageTypesMax.get(t)), this.firingMob, this.firingObject, t, this.damageCause
               );
               this.runHitEffects();
            }
         }

         this.missile.remove(this);
      }
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
                     ParticleModule.ParticleSet.FIRE_RED
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
            this.projectile.newSmallBlast((int)this.missileX, (int)this.missileY, ParticleModule.ParticleSet.FIRE_RED);
         }
      }
   }
}
