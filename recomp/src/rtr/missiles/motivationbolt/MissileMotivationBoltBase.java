package rtr.missiles.motivationbolt;

import org.newdawn.slick.SlickException;
import rtr.map.RegrowTile;
import rtr.missiles.MissileBase;
import rtr.particles.ParticleModule;

public class MissileMotivationBoltBase extends MissileBase {
   @Override
   public void update() throws SlickException {
      super.update();
      this.velMod += 0.005F;
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

      if (this.velX > 2.0F) {
         this.velX = 2.0F;
      }

      if (this.velX < -2.0F) {
         this.velX = -2.0F;
      }

      if (this.velY > 2.0F) {
         this.velY = 2.0F;
      }

      if (this.velY < -2.0F) {
         this.velY = -2.0F;
      }

      this.missileX = this.missileX + this.velX;
      this.missileY = this.missileY + this.velY;
      this.updateMissileAngle();
      this.runFlightEffects();
      if (this.canMissileHitTarget((int)this.targetX, (int)this.targetY)) {
         RegrowTile regrowTarget = this.mapAI.getRegrowMap()[this.targetCoordinatesX][this.targetCoordinatesY];
         if (regrowTarget != null) {
            regrowTarget.motivate(500);
         }

         this.runHitEffects();
         this.missile.remove(this);
      }
   }

   protected void runFlightEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 3; i++) {
            this.particles.newParticleGroup(this.missileX, this.missileY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_BLUE);
         }
      }
   }

   protected void runHitEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         this.projectile.newExpandingFireRing((int)this.missileX, (int)this.missileY, 24, 1.0F, ParticleModule.ParticleSet.MAGIC_BLUE);
      }
   }
}
