package rtr.particles.projectiles;

import org.newdawn.slick.SlickException;
import rtr.map.CollisionModule;
import rtr.particles.ParticleModule;
import rtr.utilities.FastMath;
import rtr.utilities.Utilities;

public class ProjectileMagicTrail extends ProjectileBase {
   private float angle;
   private boolean stopped;
   private ParticleModule.ParticleSet particleSet;

   public ProjectileMagicTrail(int x, int y, ParticleModule.ParticleSet s) {
      super(x, y);
      this.decay = Utilities.randomInt(500) + 5;
      this.velX = Utilities.randomFloat() / 50.0F + 0.5F;
      this.velY = Utilities.randomFloat() / 50.0F + 0.5F;
      this.angle = (float)Math.toRadians(Utilities.randomInt(3600) / 10.0);
      this.particleSet = s;
   }

   @Override
   public void update() throws SlickException {
      this.decayTick++;
      if (this.decayTick > this.decay) {
         this.projectile.remove(this);
      }

      if (!this.stopped) {
         this.projectileX = this.projectileX + FastMath.cos(this.angle) * this.velX;
         this.projectileY = this.projectileY + FastMath.sin(this.angle) * this.velY;
         if (this.collision.checkParticleCollision((int)this.projectileX, (int)this.projectileY, CollisionModule.CollisionHeight.MEDIUM)
            != this.collision.getCollisionNone()) {
            this.stopped = true;
         }
      }

      this.particles
         .newParticle(
            this.projectileX + Utilities.randomInt(2) - 1.0F,
            this.projectileY + Utilities.randomInt(2) - 1.0F,
            ParticleModule.ParticleType.FIRE_SPECK,
            this.particleSet
         );
      if (Utilities.randomInt(10) == 0) {
         this.particles
            .newParticle(
               this.projectileX + Utilities.randomInt(2) - 1.0F,
               this.projectileY + Utilities.randomInt(2) - 1.0F,
               ParticleModule.ParticleType.SMOKE_SPECK,
               this.particleSet
            );
         this.particles
            .newParticle(
               this.projectileX + Utilities.randomInt(2) - 1.0F,
               this.projectileY + Utilities.randomInt(2) - 1.0F,
               ParticleModule.ParticleType.SPARKLE_SPECK,
               this.particleSet
            );
      }
   }
}
