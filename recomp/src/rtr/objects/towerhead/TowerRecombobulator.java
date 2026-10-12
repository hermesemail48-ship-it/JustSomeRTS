package rtr.objects.towerhead;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.missiles.MissileBase;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.save.YMLDataMap;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class TowerRecombobulator extends TowerBase {
   public TowerRecombobulator(int x, int y, ObjectBase t, TowerBase.TowerUpgradeStage stage) throws SlickException {
      super(x, y, t);
      this.towerFlags = this.object.getTowerHeadFlagFactory().getTowerFlags(TowerBase.TowerType.RECOMBOBULATOR, stage);
   }

   public TowerRecombobulator(YMLDataMap dataMap, ObjectBase t) throws SlickException {
      super(dataMap, t);
   }

   @Override
   public void update() throws SlickException {
      super.update();
      if (this.tower.isPaused()) {
         this.setPositionFace();
      } else {
         boolean canFireMain = this.canFireMainAmmo();
         boolean canFireAlt = this.canFireAltAmmo();
         if (canFireMain || canFireAlt) {
            if (this.reloaded || !canFireMain && !canFireAlt) {
               this.reloadRateTick = 0;
            } else {
               this.reloadRateTick++;
            }

            if (this.reloadRateTick > this.towerFlags.getReloadRate()) {
               this.reloaded = true;
               this.reloading = false;
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

                        this.tower.decreaseEnergy(this.towerFlags.getFireEnergyCost());
                     }

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
            if (mob.getHitPoints() >= mob.getHitPointsMax()) {
               mobList.remove(mob);
            } else {
               for (MissileBase m : missileList) {
                  if (m.isActive() && m.getTargetMob() == mob && m.getType() == this.towerFlags.getMissileType()) {
                     mobList.remove(mob);
                     break;
                  }
               }
            }
         }
      }

      if (mobList.size() > 0) {
         ArrayList<MobBase> highPriority = new ArrayList<>();
         ArrayList<MobBase> lowPriority = new ArrayList<>();

         for (MobBase mx : mobList) {
            if (mx.getHitPoints() <= mx.getHitPointsMax() * 0.75) {
               highPriority.add(mx);
            } else {
               lowPriority.add(mx);
            }
         }

         if (highPriority.size() > 0) {
            this.targetMob = highPriority.get(Utilities.randomInt(highPriority.size()));
         } else {
            this.targetMob = lowPriority.get(Utilities.randomInt(lowPriority.size()));
         }

         this.updateTargetDirection(this.targetMob.getCenterPixelX(true), this.targetMob.getCenterPixelY(true));
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
         } else if (this.targetMob.getHitPoints() >= this.targetMob.getHitPointsMax()) {
            return false;
         } else {
            for (MissileBase m : this.missile.getMissileArray()) {
               if (m.isActive() && m.getTargetMob() == this.targetMob && m.getType() == this.towerFlags.getMissileType()) {
                  return false;
               }
            }

            return true;
         }
      }
   }
}
