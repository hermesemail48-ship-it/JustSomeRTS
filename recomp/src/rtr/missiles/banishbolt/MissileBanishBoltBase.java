package rtr.missiles.banishbolt;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.map.MapData;
import rtr.missiles.MissileBase;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class MissileBanishBoltBase extends MissileBase {
   @Override
   public void update() throws SlickException {
      super.update();
      this.velMod += 5.0E-4F;
      if (this.targetMob != null && this.targetMob.isAvailable()) {
         this.targetX = this.targetMob.getCenterPixelX(true);
         this.targetY = this.targetMob.getCenterPixelY(true);
      }

      if (this.targetMob != null && this.targetMob.isAvailable()) {
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

         if (this.velX > 1.0F) {
            this.velX = 1.0F;
         }

         if (this.velX < -1.0F) {
            this.velX = -1.0F;
         }

         if (this.velY > 1.0F) {
            this.velY = 1.0F;
         }

         if (this.velY < -1.0F) {
            this.velY = -1.0F;
         }
      }

      this.missileX = this.missileX + this.velX;
      this.missileY = this.missileY + this.velY;
      this.updateMissileAngle();
      this.runFlightEffects();
      ArrayList<MobBase> mobsHit = this.mob.getMobsNearby(this.tileX, this.tileY, 2, this.mobGroup);
      if (mobsHit.size() > 0) {
         for (MobBase m : mobsHit) {
            if (this.canMissileHitTarget(m)) {
               this.moveMob(m);
               this.runHitEffects();
               this.missile.remove(this);
               return;
            }
         }
      }
   }

   private void moveMob(MobBase selectedTarget) throws SlickException {
      OrderedPair randomCoordinates = this.randomCoordinates();
      this.projectile
         .newEnergyBeam(
            selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), randomCoordinates.getX() * 16 + 8, randomCoordinates.getY() * 16 + 8
         );
      this.lighting.addShortTermLight(randomCoordinates.getX(), randomCoordinates.getY(), (byte)5, (byte)5, 50);
      if (this.map.isInView(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), 10)) {
         for (int x = 0; x < 20; x++) {
            this.projectile.newEnergyRing(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), 16, ParticleModule.ParticleSet.FIRE_RED);
         }

         for (int x = 0; x < 10; x++) {
            this.particles
               .newParticle(
                  selectedTarget.getCenterPixelX(true) + Utilities.randomInt(-1, 1),
                  selectedTarget.getTopPixelY(true) + Utilities.randomInt(selectedTarget.getSprite().getSpriteWidth()),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
         }
      }

      selectedTarget.move(randomCoordinates);
      if (this.map.isInView(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), 10)) {
         for (int x = 0; x < 20; x++) {
            this.projectile.newEnergyRing(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), 16, ParticleModule.ParticleSet.FIRE_RED);
         }

         for (int x = 0; x < 10; x++) {
            this.particles
               .newParticle(
                  selectedTarget.getCenterPixelX(true) + Utilities.randomInt(-1, 1),
                  selectedTarget.getTopPixelY(true) + Utilities.randomInt(selectedTarget.getSprite().getSpriteWidth()),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
         }
      }

      selectedTarget.interruptNode(selectedTarget.getAIConfused());
   }

   protected OrderedPair randomCoordinates() {
      int randomX = Utilities.randomInt(255);

      int randomY;
      for (randomY = Utilities.randomInt(255);
         this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[randomX][randomY] > 0
            || this.map.getMap()[randomX][randomY][6] > 0
            || this.map.getMap()[randomX][randomY][9] > 0
            || this.object.isInRange(randomX, randomY);
         randomY = Utilities.randomInt(255)
      ) {
         randomX = Utilities.randomInt(255);
      }

      return OrderedPair.getOrderedPair(randomX, randomY);
   }

   protected void runFlightEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         this.particles.newParticle(this.missileX, this.missileY, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, ParticleModule.ParticleSet.FIRE_PURPLE);

         for (int i = 0; i < 3; i++) {
            this.particles.newParticleGroup(this.missileX, this.missileY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_PURPLE);
            this.particles.newParticle(this.missileX, this.missileY, ParticleModule.ParticleType.SPARKLE_SPECK, ParticleModule.ParticleSet.FIRE_PURPLE);
            this.particles
               .newParticleGroup(
                  this.missileX + Utilities.randomInt(-1, 1),
                  this.missileY + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleGroup.FIRE,
                  ParticleModule.ParticleSet.FIRE_PURPLE
               );
         }
      }
   }

   protected void runHitEffects() throws SlickException {
      this.sound.playSound(SoundModule.SoundType.MAGIC_BANISH, this.missileX, this.missileY);
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 5; i++) {
            this.projectile.newSmallBlast((int)this.missileX, (int)this.missileY, ParticleModule.ParticleSet.FIRE_PURPLE);
         }
      }
   }
}
