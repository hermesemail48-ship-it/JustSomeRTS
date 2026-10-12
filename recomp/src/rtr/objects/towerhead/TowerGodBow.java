package rtr.objects.towerhead;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.missiles.MissileBase;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.towerhead.spritesheet.templates.TowerSpriteTemplateBase;
import rtr.save.YMLDataMap;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class TowerGodBow extends TowerBase {
   public TowerGodBow(int x, int y, ObjectBase t) throws SlickException {
      super(x, y, t);
      this.towerFlags = this.object.getTowerHeadFlagFactory().getTowerFlags(TowerBase.TowerType.GOD_BOW, TowerBase.TowerUpgradeStage.BASE);
      this.sprite = this.object.createTowerSpriteSheet(this.towerFlags.getTowerType());
   }

   public TowerGodBow(YMLDataMap dataMap, ObjectBase t) throws SlickException {
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

         boolean canFireMain = this.canFireMainAmmo();
         if (canFireMain) {
            if (!this.reloaded && canFireMain) {
               this.reloadRateTick++;
            } else {
               this.reloadRateTick = 0;
            }

            int reloadAnimationTime = this.sprite.getSpriteTemplate().getAnimationDurations().get(TowerSpriteTemplateBase.TowerAnimationConfig.RELOAD)
               * this.sprite.getFrameCount();
            if (this.reloadRateTick > this.towerFlags.getReloadRate() - reloadAnimationTime) {
               if (!this.reloading) {
                  this.setPositionReload();
                  this.sprite.restart();
                  this.reloading = true;
               }

               if (this.reloadRateTick > this.towerFlags.getReloadRate()) {
                  this.reloaded = true;
                  this.reloading = false;
                  this.reloadRateTick = 0;
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
                     if (!canFireMain) {
                        this.resetTarget();
                        return;
                     }

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
                     this.sound.playSound(SoundModule.SoundType.BOW_FIRE, this.towerCenterX, this.towerCenterY);
                     this.setPositionFire();
                     this.sprite.restart();
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
         ArrayList<MissileBase> missileList = this.missile.getMissileArray();

         for (int xx = mobList.size() - 1; xx >= 0; xx--) {
            MobBase mob = mobList.get(xx);
            int possibleDamage = 0;

            for (MissileBase m : missileList) {
               if (m.isActive()) {
                  if (m.getTargetMob() == mob) {
                     possibleDamage += m.getAverageDamage();
                  }

                  if (possibleDamage >= mob.getHitPoints()) {
                     mobList.remove(mob);
                  }
               }
            }
         }
      }

      if (mobList.size() > 0) {
         ArrayList<MobBase> highPriorityTargets = new ArrayList<>();
         ArrayList<MobBase> regularTargets = new ArrayList<>();
         regularTargets.addAll(mobList);
         ArrayList<MobBase> lowPriorityTargets = new ArrayList<>();

         for (MobBase mx : mobList) {
            for (MobBase.DamageType d : mx.getMobFlags().getDamageVulnerabilities().keySet()) {
               if (this.missile.getMissileFactory().getMissileInstance(this.towerFlags.getMissileType()).getDamageTypesMin().keySet().contains(d)) {
                  highPriorityTargets.add(mx);
                  regularTargets.remove(mx);
               }
            }

            for (MobBase.DamageType dx : mx.getMobFlags().getDamageResistances().keySet()) {
               if (this.missile.getMissileFactory().getMissileInstance(this.towerFlags.getMissileType()).getDamageTypesMin().keySet().contains(dx)) {
                  lowPriorityTargets.add(mx);
                  regularTargets.remove(mx);
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
         ArrayList<MissileBase> missileList = this.missile.getMissileArray();

         for (int xx = objectList.size() - 1; xx >= 0; xx--) {
            ObjectBase objectCheck = objectList.get(xx);
            int possibleDamage = 0;

            for (MissileBase m : missileList) {
               if (m.isActive()) {
                  if (m.getTargetObject() == objectCheck) {
                     possibleDamage += m.getAverageDamage();
                  }

                  if (possibleDamage >= objectCheck.getHitPoints()) {
                     objectList.remove(objectCheck);
                  }
               }
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
         if (!mobList.contains(this.targetMob)) {
            return false;
         } else {
            ArrayList<MissileBase> missileList = this.missile.getMissileArray();
            int possibleDamage = 0;

            for (MissileBase m : missileList) {
               if (m.isActive()) {
                  if (m.getTargetMob() == this.targetMob) {
                     possibleDamage += m.getAverageDamage();
                  }

                  if (possibleDamage >= this.targetMob.getHitPoints()) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   @Override
   protected boolean verifyTargetObject() throws SlickException {
      if (!this.object.objectExists(this.targetObject)) {
         return false;
      } else if (!this.targetObject.canNotBeAttacked()
         && !this.collision
            .checkParticleCollisionBetween(
               this.towerCenterX,
               this.towerCenterY,
               this.targetObject.getCenterPixelX(),
               this.targetObject.getCenterPixelY(),
               this.towerFlags.getCollisionHeight()
            )) {
         ArrayList<MissileBase> missileList = this.missile.getMissileArray();
         int possibleDamage = 0;

         for (MissileBase m : missileList) {
            if (m.isActive()) {
               if (m.getTargetObject() == this.targetObject) {
                  possibleDamage += m.getAverageDamage();
               }

               if (possibleDamage >= this.targetObject.getHitPoints()) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
