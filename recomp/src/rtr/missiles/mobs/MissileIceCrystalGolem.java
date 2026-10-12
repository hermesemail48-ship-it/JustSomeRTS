package rtr.missiles.mobs;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.lighting.Light;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.missiles.MissileBase;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissileIceCrystalGolem extends MissileBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.ICE_CRYSTAL_GOLEM;
      this.decayRate = 100;
      this.damageTypesMin.put(MobBase.DamageType.MAGIC_REGULAR, 1);
      this.damageTypesMax.put(MobBase.DamageType.MAGIC_REGULAR, 1);
      this.damageTypesMin.put(MobBase.DamageType.MAGIC_ICE, 1);
      this.damageTypesMax.put(MobBase.DamageType.MAGIC_ICE, 1);
      this.lightIntensity = 2;
      this.lightRadius = 16;
      this.light = new Light(this.tileX, this.tileY, this.lightIntensity, this.lightRadius, false);
      this.velocity = 3.0F + Utilities.randomFloat();
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
   public void update() throws SlickException {
      super.update();
      this.velMod = this.velMod - this.friction;
      if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[this.tileX][this.tileY] > 0
         && this.collision.checkParticleCollision((int)this.missileX, (int)this.missileY, CollisionModule.CollisionHeight.HIGH)
            != this.collision.getCollisionNone()) {
         this.velMod -= 2.0F;
      } else {
         this.missileX = this.missileX + (float)Math.cos(this.angle) * (this.velocity + this.velMod);
         this.missileY = this.missileY + (float)Math.sin(this.angle) * (this.velocity + this.velMod);
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
         this.particles.newParticleGroup(this.missileX, this.missileY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_BLUE);
         this.particles
            .newParticleGroup(
               this.missileX + Utilities.randomInt(-1, 1),
               this.missileY + Utilities.randomInt(-1, 1),
               ParticleModule.ParticleGroup.FIRE,
               ParticleModule.ParticleSet.MAGIC_BLUE
            );
         if (Utilities.randomInt(3) == 0) {
            this.particles.newParticle(this.missileX, this.missileY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.FIRE_BLUE);
         }
      }
   }

   protected void runHitEffects() throws SlickException {
      this.sound.playSound(SoundModule.SoundType.PHANTNOM_DART_HIT, this.missileX, this.missileY);
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 2; i++) {
            this.projectile.newSmallBlast((int)this.missileX, (int)this.missileY, ParticleModule.ParticleSet.MAGIC_BLUE);
         }
      }
   }
}
