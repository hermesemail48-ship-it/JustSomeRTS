package rtr.objects.towerhead;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.save.YMLDataMap;
import rtr.utilities.Utilities;

public class TowerStatic extends TowerBase {
   public TowerStatic(int x, int y, ObjectBase t, TowerBase.TowerUpgradeStage stage) throws SlickException {
      super(x, y, t);
      this.towerFlags = this.object.getTowerHeadFlagFactory().getTowerFlags(TowerBase.TowerType.STATIC, stage);
   }

   public TowerStatic(YMLDataMap dataMap, ObjectBase t) throws SlickException {
      super(dataMap, t);
   }

   @Override
   public void update() throws SlickException {
      super.update();
      if (this.tower.isPaused()) {
         this.setPositionFace();
      } else {
         if (this.tower.getEnergy() > 0) {
            if (this.map.isInView(this.towerCenterX, this.towerCenterY, 128) && Utilities.randomInt(10) == 0) {
               this.projectile.newEnergyRing(this.towerCenterX, this.towerCenterY, 8, ParticleModule.ParticleSet.MAGIC_BLUE);
            }

            this.fireRateTick++;
         }

         if (this.fireRateTick > this.towerFlags.getFireRate()) {
            ArrayList<MobBase> mobList = this.mob.getMobsNearby(this.towerX, this.towerY, this.towerFlags.getDistance(), this.towerFlags.getTargetMobGroups());
            if (mobList.size() > 0) {
               boolean foundTarget = false;

               for (MobBase m : mobList) {
                  if (!this.collision
                     .checkParticleCollisionBetween(
                        this.towerCenterX, this.towerCenterY, m.getCenterPixelX(true), m.getCenterPixelY(true), this.towerFlags.getCollisionHeight()
                     )) {
                     m.takeDamage(
                        Utilities.randomInt(10, 50),
                        (float)this.towerCenterX,
                        (float)this.towerCenterY,
                        this.tower,
                        MobBase.DamageType.MAGIC_ELECTRIC,
                        MobBase.DamageCause.TOWER
                     );
                     if (this.map.isInView(m.getCenterPixelX(true), m.getCenterPixelY(true), 10)) {
                        this.projectile.newExpandingFireRing(m.getCenterPixelX(true), m.getCenterPixelY(true), 16, 1.0F, ParticleModule.ParticleSet.FIRE_BLUE);

                        for (int i = 0; i < 4; i++) {
                           this.projectile.newSmallBlast(m.getCenterPixelX(true), m.getCenterPixelY(true), ParticleModule.ParticleSet.FIRE_BLUE);
                        }
                     }

                     foundTarget = true;
                  }
               }

               if (foundTarget) {
                  if (this.map.isInView(this.towerCenterX, this.towerCenterY, this.towerFlags.getDistance() * 16)) {
                     for (int i = 0; i < 2; i++) {
                        this.projectile
                           .newExpandingFireRing(
                              this.towerCenterX, this.towerCenterY, this.towerFlags.getDistance() * 16, 6.0F, ParticleModule.ParticleSet.MAGIC_BLUE
                           );
                     }

                     for (int i = 0; i < 20; i++) {
                        this.projectile
                           .newEnergyRing(this.towerCenterX, this.towerCenterY, this.towerFlags.getDistance() * 16, ParticleModule.ParticleSet.FIRE_BLUE);
                     }
                  }

                  this.sound.playSound(SoundModule.SoundType.MAGIC_ATTRACT_FIRE, this.towerCenterX, this.towerCenterY);
                  this.fireRateTick = 0;
                  this.tower.decreaseEnergy(this.towerFlags.getFireEnergyCost());
               }
            }
         }
      }
   }
}
