package rtr.particles.projectiles;

import org.newdawn.slick.SlickException;
import rtr.map.CollisionModule;
import rtr.particles.ParticleModule;
import rtr.utilities.FastMath;
import rtr.utilities.Utilities;

public class ProjectileSmallBlast extends ProjectileBase {
   float angle;
   boolean stopped;
   ParticleModule.ParticleSet particleSet;

   public ProjectileSmallBlast(int x, int y, ParticleModule.ParticleSet s) {
      super(x, y);
      this.decay = Utilities.randomInt(10) + 5;
      this.velX = Utilities.randomFloat() / 50.0F + 2.0F;
      this.velY = Utilities.randomFloat() / 50.0F + 2.0F;
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

      for (int i = 0; i < 2; i++) {
         this.particles
            .newParticleGroup(
               this.projectileX + Utilities.randomInt(2) - 1.0F,
               this.projectileY + Utilities.randomInt(2) - 1.0F,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               this.particleSet
            );
      }

      if (Utilities.randomInt(20) == 0) {
         this.projectile
            .newExpandingFireRing(
               (int)this.projectileX + Utilities.randomInt(2) - 1, (int)this.projectileY + Utilities.randomInt(2) - 1, 16, 2.0F, this.particleSet
            );
      }

      if (Utilities.randomInt(8) == 0) {
         this.particles
            .newParticleGroup(
               this.projectileX + Utilities.randomInt(2) - 1.0F,
               this.projectileY + Utilities.randomInt(2) - 1.0F,
               ParticleModule.ParticleGroup.DUST,
               ParticleModule.ParticleSet.BLACK
            );
         this.particles
            .newParticleGroup(
               this.projectileX + Utilities.randomInt(2) - 1.0F,
               this.projectileY + Utilities.randomInt(2) - 1.0F,
               ParticleModule.ParticleGroup.DUST,
               this.particleSet
            );
      }
   }
}
