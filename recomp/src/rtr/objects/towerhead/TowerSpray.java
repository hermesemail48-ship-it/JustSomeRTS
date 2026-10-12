package rtr.objects.towerhead;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.save.YMLDataMap;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class TowerSpray extends TowerBase {
   public TowerSpray(int x, int y, ObjectBase t, TowerBase.TowerUpgradeStage stage) throws SlickException {
      super(x, y, t);
      this.towerFlags = this.object.getTowerHeadFlagFactory().getTowerFlags(TowerBase.TowerType.SPRAY, stage);
      this.sprite = this.object.createTowerSpriteSheet(this.towerFlags.getTowerType());
   }

   public TowerSpray(YMLDataMap dataMap, ObjectBase t) throws SlickException {
      super(dataMap, t);
   }

   @Override
   public void update() throws SlickException {
      super.update();
      if (this.tower.isPaused()) {
         this.setPositionFace();
      } else {
         if (this.verifyTarget()) {
            if (this.targetMob != null) {
               this.updateTargetDirection(this.targetMob.getCenterPixelX(true), this.targetMob.getCenterPixelY(true));
            } else if (this.targetObject != null) {
               this.updateTargetDirection(this.targetObject.getCenterPixelX(), this.targetObject.getCenterPixelY());
            }
         }

         if (this.firing && this.sprite.isStopped()) {
            this.firing = false;
         }

         if (!this.reloading && !this.firing) {
            this.setPositionFace();
         }

         if (this.tower.getAmmo() <= 0 && this.tower.isResourceAvailable(this.towerFlags.getAmmoType())) {
            ResourceBase towerAmmo = this.tower.getAvailableResource(this.towerFlags.getAmmoType());
            this.tower.consumeStores(towerAmmo);
            this.tower.increaseAmmo(this.tower.getObjectFlags().getAmmoMax());
            if (Utilities.randomInt(5) == 0 && towerAmmo.getType().getTemplate().getTrashResource() != null) {
               OrderedPair o = this.resource.findOpenSpace(this.towerX, this.towerY, MapData.BlockMapGroup.STANDARD);
               this.resource.createResourceOnGround(towerAmmo.getType().getTemplate().getTrashResource(), towerAmmo.getColor(), o.getX(), o.getY());
            }
         }

         boolean canFireMain = this.canFireMainAmmo();
         boolean canFireAlt = this.canFireAltAmmo();
         if (!canFireMain && !canFireAlt) {
            this.setPositionFace();
         } else {
            if (this.reloaded || !canFireMain && !canFireAlt) {
               this.reloadRateTick = 0;
            } else {
               this.reloadRateTick++;
            }

            if (this.reloadRateTick > this.towerFlags.getReloadRate()) {
               if (!this.reloading) {
                  this.reloading = true;
               }

               if (this.reloadRateTick > this.towerFlags.getReloadRate()) {
                  this.reloaded = true;
                  this.reloading = false;
                  this.reloadRateTick = 0;
                  this.setPositionFace();
               }
            }

            if (this.reloaded) {
               this.fireRateTick++;
               if (!this.verifyTarget() && Utilities.randomInt(this.towerFlags.getFindTargetRate()) == 0
                  || Utilities.randomInt(this.towerFlags.getChangeTargetRate()) == 0) {
                  this.findTarget();
               }

               if (this.verifyTarget()) {
                  if (this.fireRateTick > this.towerFlags.getFireRate()) {
                     if (!canFireMain && !canFireAlt) {
                        this.resetTarget();
                        return;
                     }

                     if (canFireMain) {
                        if (this.targetMob != null) {
                           this.missile.newMissile(this.towerCenterX, this.towerCenterY, this.tower, this.targetMob, this.towerFlags.getMissileType());
                        } else if (this.targetObject != null) {
                           OrderedPair o = this.targetObject.getCenterCoordinates();
                           this.missile
                              .newMissile(
                                 this.towerCenterX, this.towerCenterY, this.tower, this.targetObject, o.getX(), o.getY(), this.towerFlags.getMissileType()
                              );
                        }

                        this.tower.decreaseEnergy(this.towerFlags.getFireEnergyCost());
                     } else if (canFireAlt) {
                        if (this.targetMob != null) {
                           this.missile.newMissile(this.towerCenterX, this.towerCenterY, this.tower, this.targetMob, this.towerFlags.getAltMissileType());
                        } else if (this.targetObject != null) {
                           OrderedPair o = this.targetObject.getCenterCoordinates();
                           this.missile
                              .newMissile(
                                 this.towerCenterX, this.towerCenterY, this.tower, this.targetObject, o.getX(), o.getY(), this.towerFlags.getAltMissileType()
                              );
                        }
                     }

                     this.sound.playSound(SoundModule.SoundType.SPRAY_BULLET_FIRE, this.towerCenterX, this.towerCenterY);
                     this.tower.decreaseAmmo(1);
                     this.setPositionFire();
                     this.firing = true;
                     this.reloadRateTick = 0;
                     this.fireRateTick = 0;
                     this.reloaded = false;
                  }
               } else {
                  this.resetTarget();
                  this.runRandomRotation();
               }
            }
         }
      }
   }

   @Override
   protected void findTargetMob() {
      ArrayList<MobBase> mobList = this.mob.getMobsNearby(this.towerX, this.towerY, this.towerFlags.getDistance(), this.towerFlags.getTargetMobGroups());
      if (mobList.size() > 0) {
         for (int x = mobList.size() - 1; x >= 0; x--) {
            MobBase mob = mobList.get(x);
            if (this.collision
               .checkParticleCollisionBetween(
                  this.towerCenterX, this.towerCenterY, mob.getCenterPixelX(true), mob.getCenterPixelY(true), this.towerFlags.getCollisionHeight()
               )) {
               mobList.remove(mob);
            }
         }
      }

      if (mobList.size() > 0) {
         ArrayList<MobBase> highPriorityTargets = new ArrayList<>();
         ArrayList<MobBase> regularTargets = new ArrayList<>();
         regularTargets.addAll(mobList);
         ArrayList<MobBase> lowPriorityTargets = new ArrayList<>();

         for (MobBase m : mobList) {
            for (MobBase.DamageType d : m.getMobFlags().getDamageVulnerabilities().keySet()) {
               if (this.missile.getMissileFactory().getMissileInstance(this.towerFlags.getMissileType()).getDamageTypesMin().keySet().contains(d)) {
                  highPriorityTargets.add(m);
                  regularTargets.remove(m);
               }
            }

            for (MobBase.DamageType dx : m.getMobFlags().getDamageResistances().keySet()) {
               if (this.missile.getMissileFactory().getMissileInstance(this.towerFlags.getMissileType()).getDamageTypesMin().keySet().contains(dx)) {
                  lowPriorityTargets.add(m);
                  regularTargets.remove(m);
               }
            }
         }

         if (highPriorityTargets.size() > 0) {
            mobList = this.mob.sortMobsByDistance(this.towerX, this.towerY, highPriorityTargets);
            this.targetMob = highPriorityTargets.get(0);
         } else if (regularTargets.size() > 0) {
            mobList = this.mob.sortMobsByDistance(this.towerX, this.towerY, regularTargets);
            this.targetMob = regularTargets.get(0);
         } else {
            mobList = this.mob.sortMobsByDistance(this.towerX, this.towerY, lowPriorityTargets);
            this.targetMob = lowPriorityTargets.get(0);
         }

         this.updateTargetDirection(this.targetMob.getCenterPixelX(true), this.targetMob.getCenterPixelY(true));
      }
   }

   @Override
   protected void findTargetObject() {
      ArrayList<ObjectBase> objectList = this.object
         .getObjectsNearby(this.towerX, this.towerY, this.towerFlags.getDistance(), this.towerFlags.getTargetMobGroups());
      if (objectList.size() > 0) {
         for (int x = objectList.size() - 1; x >= 0; x--) {
            ObjectBase objectCheck = objectList.get(x);
            if (objectCheck.canNotBeAttacked()
               || this.collision
                  .checkParticleCollisionBetween(
                     this.towerCenterX, this.towerCenterY, objectCheck.getCenterPixelX(), objectCheck.getCenterPixelY(), this.towerFlags.getCollisionHeight()
                  )) {
               objectList.remove(objectCheck);
            }
         }
      }

      if (objectList.size() > 0) {
         this.targetObject = objectList.get(Utilities.randomInt(objectList.size()));
         this.updateTargetDirection(this.targetObject.getCenterPixelX(), this.targetObject.getCenterPixelY());
      }
   }

   @Override
   protected boolean verifyTargetMob() throws SlickException {
      if (!this.targetMob.isAvailable()) {
         return false;
      } else if (this.collision
         .checkParticleCollisionBetween(
            this.towerCenterX,
            this.towerCenterY,
            this.targetMob.getCenterPixelX(true),
            this.targetMob.getCenterPixelY(true),
            this.towerFlags.getCollisionHeight()
         )) {
         return false;
      } else {
         ArrayList<MobBase> mobList = this.mob.getMobsNearby(this.towerX, this.towerY, this.towerFlags.getDistance(), this.towerFlags.getTargetMobGroups());
         return mobList.contains(this.targetMob);
      }
   }

   @Override
   protected boolean verifyTargetObject() throws SlickException {
      return !this.object.objectExists(this.targetObject)
         ? false
         : !this.targetObject.canNotBeAttacked()
            && !this.collision
               .checkParticleCollisionBetween(
                  this.towerCenterX,
                  this.towerCenterY,
                  this.targetObject.getCenterPixelX(),
                  this.targetObject.getCenterPixelY(),
                  this.towerFlags.getCollisionHeight()
               );
   }
}
