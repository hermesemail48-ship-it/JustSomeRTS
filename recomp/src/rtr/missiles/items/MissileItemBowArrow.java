package rtr.missiles.items;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.map.CollisionModule;
import rtr.map.MapData;
import rtr.missiles.MissileBase;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissileItemBowArrow extends MissileBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.ITEM_BOW_ARROW;
      this.decayRate = 5000;
      this.damageTypesMin.put(MobBase.DamageType.PIERCING, 1);
      this.damageTypesMax.put(MobBase.DamageType.PIERCING, 1);
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
                  this.hitTargetObject(this.targetObject, true);
                  return;
               }
            }

            if (this.collision.checkParticleCollision((int)this.missileX, (int)this.missileY, CollisionModule.CollisionHeight.HIGH)
               != this.collision.getCollisionNone()) {
               ObjectBase possibleTarget = this.object.getBuildingAtLocation((int)(this.missileX / 16.0F), (int)(this.missileY / 16.0F));
               if (possibleTarget != null && possibleTarget.getObjectFlags().getMobGroup() == this.mobGroup) {
                  this.hitTargetObject(possibleTarget, true);
                  return;
               }
            }
         }

         if (this.object.objectExists(this.targetObject) && this.canMissileHitTarget((int)this.targetX, (int)this.targetY)) {
            ObjectBase possibleTarget = this.object.getBuildingAtLocation((int)(this.missileX / 16.0F), (int)(this.missileY / 16.0F));
            if (this.targetObject == possibleTarget) {
               this.hitTargetObject(this.targetObject, true);
               return;
            }
         }
      }
   }

   private void hitTargetObject(ObjectBase o, boolean destroyMissile) throws SlickException {
      for (MobBase.DamageType t : this.damageTypesMin.keySet()) {
         o.takeDamage(Utilities.randomInt(this.damageTypesMin.get(t), this.damageTypesMax.get(t)), this.firingMob, this.firingObject, t, this.damageCause);
         this.runHitEffects();
         if (destroyMissile) {
            this.missile.remove(this);
         }
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
