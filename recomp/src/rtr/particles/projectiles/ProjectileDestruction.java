package rtr.particles.projectiles;

import org.newdawn.slick.SlickException;
import rtr.map.CollisionModule;
import rtr.particles.ParticleModule;
import rtr.utilities.FastMath;
import rtr.utilities.Utilities;

public class ProjectileDestruction extends ProjectileBase {
   float angle;
   boolean direction;
   boolean onFire;

   public ProjectileDestruction(int x, int y) {
      super(x, y);
      this.decay = Utilities.randomInt(500) + 500;
      this.velX = Utilities.randomFloat() * 6.0F;
      this.velY = this.velX;
      this.angle = (float)Math.toRadians(Utilities.randomInt(3600) / 10.0);
      if (Utilities.randomInt(2) == 0) {
         this.direction = true;
      }

      if (Utilities.randomInt(3) == 0) {
         this.onFire = true;
      }
   }

   @Override
   public void update() throws SlickException {
      this.decayTick++;
      if (this.decayTick > this.decay) {
         this.projectile.remove(this);
      }

      if (this.velX > 0.0F) {
         if (this.direction) {
            this.angle = this.angle + Utilities.randomFloat() / 50.0F;
         } else {
            this.angle = this.angle - Utilities.randomFloat() / 50.0F;
         }

         this.projectileX = this.projectileX + FastMath.cos(this.angle) * this.velX;
         this.projectileY = this.projectileY + FastMath.sin(this.angle) * this.velY;
         if (this.collision.checkParticleCollision((int)this.projectileX, (int)this.projectileY, CollisionModule.CollisionHeight.MEDIUM)
            != this.collision.getCollisionNone()) {
            this.velX -= 2.0F;
            this.velY -= 2.0F;
         } else {
            this.velX -= 0.2F;
            this.velY -= 0.2F;
         }

         if (Utilities.randomInt(2) == 0) {
            this.particles
               .newParticleGroup(
                  this.projectileX + Utilities.randomInt(-2, 2),
                  this.projectileY + Utilities.randomInt(-2, 2),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.GRAY
               );
            this.particles
               .newParticleGroup(
                  this.projectileX + Utilities.randomInt(-2, 2),
                  this.projectileY + Utilities.randomInt(-2, 2),
                  Utilities.randomInt(360),
                  ParticleModule.ParticleGroup.DUST,
                  ParticleModule.ParticleSet.BLACK
               );
         }
      }

      if (this.velX <= 0.0F) {
         this.velX = 0.0F;
      }

      if (this.onFire && this.map.isInView(this.projectileX, this.projectileY, 10)) {
         this.particles.newParticleGroup(this.projectileX, this.projectileY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED);
         if (Utilities.randomInt(2) == 0) {
            this.particles
               .newParticleGroup(
                  this.projectileX + Utilities.randomInt(-2, 2),
                  this.projectileY + Utilities.randomInt(-2, 2),
                  ParticleModule.ParticleGroup.FIRE,
                  ParticleModule.ParticleSet.FIRE_RED
               );
         }
      }
   }
}
