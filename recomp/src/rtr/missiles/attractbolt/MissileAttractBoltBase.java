package rtr.missiles.attractbolt;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.map.MapData;
import rtr.missiles.MissileBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class MissileAttractBoltBase extends MissileBase {
   @Override
   public void update() throws SlickException {
      super.update();
      this.velMod += 0.005F;
      if (this.velocity + this.velMod <= 0.0F) {
         this.isActive = false;
         this.isTop = false;
      } else {
         this.missileX = this.missileX + (float)Math.cos(this.angle) * (this.velocity + this.velMod);
         this.missileY = this.missileY + (float)Math.sin(this.angle) * (this.velocity + this.velMod);
         this.runFlightEffects();
         if (this.canMissileHitTarget((int)this.targetX, (int)this.targetY)) {
            ResourceBase r = this.resource.getResourceOnGround(this.tileX, this.tileY);
            if (r != null) {
               this.moveResource(r);
            }

            this.runHitEffects();
            this.missile.remove(this);
         }
      }
   }

   private void moveResource(ResourceBase selectedTarget) throws SlickException {
      OrderedPair randomCoordinates = this.resource.findOpenSpace((int)this.originX / 16, (int)this.originY / 16, MapData.BlockMapGroup.STANDARD);
      if (this.resource.isResourcesOnGround(randomCoordinates.getX(), randomCoordinates.getY())) {
         randomCoordinates = this.resource.findOpenSpace(randomCoordinates.getX(), randomCoordinates.getY(), MapData.BlockMapGroup.STANDARD);
      }

      this.projectile
         .newEnergyBeam(
            selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), randomCoordinates.getX() * 16 + 8, randomCoordinates.getY() * 16 + 8
         );
      this.lighting.addShortTermLight(randomCoordinates.getX(), randomCoordinates.getY(), (byte)5, (byte)5, 50);
      if (this.map.isInView(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), 10)) {
         for (int x = 0; x < 20; x++) {
            this.projectile.newEnergyRing(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), 16, ParticleModule.ParticleSet.FIRE_BLUE);
         }

         for (int x = 0; x < 10; x++) {
            this.particles
               .newParticle(
                  selectedTarget.getCenterPixelX(true) + Utilities.randomInt(-1, 1),
                  selectedTarget.getTopPixelY(true) + Utilities.randomInt(selectedTarget.getSprite().getWidth()),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
         }
      }

      selectedTarget.moveResource(randomCoordinates);
      if (this.map.isInView(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), 10)) {
         for (int x = 0; x < 20; x++) {
            this.projectile
               .newEnergyRing(selectedTarget.getCenterPixelX(true), selectedTarget.getCenterPixelY(true), 16, ParticleModule.ParticleSet.FIRE_GREEN);
         }

         for (int x = 0; x < 10; x++) {
            this.particles
               .newParticle(
                  selectedTarget.getCenterPixelX(true) + Utilities.randomInt(-1, 1),
                  selectedTarget.getTopPixelY(true) + Utilities.randomInt(selectedTarget.getSprite().getWidth()),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
         }
      }
   }

   protected void runFlightEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 2; i++) {
            this.particles.newParticleGroup(this.missileX, this.missileY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_BLUE);
         }
      }
   }

   protected void runHitEffects() throws SlickException {
      this.sound.playSound(SoundModule.SoundType.MAGIC_RECALL, this.missileX, this.missileY);
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         this.projectile.newExpandingFireRing((int)this.missileX, (int)this.missileY, 24, 1.0F, ParticleModule.ParticleSet.MAGIC_BLUE);
      }
   }
}
