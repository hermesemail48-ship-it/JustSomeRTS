package rtr.particles.projectiles;

import org.newdawn.slick.SlickException;
import rtr.map.CollisionModule;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class ProjectileGroundStatic extends ProjectileBase {
   private float particleX;
   private float particleY;
   private float directionX;
   private float directionY;
   private ParticleModule.ParticleSet particleSet;

   public ProjectileGroundStatic(int x, int y, ParticleModule.ParticleSet s) {
      super(x, y);
      this.particleX = x;
      this.particleY = y;
      this.decay = 100;
      this.directionX = Utilities.randomInt(3);
      this.directionY = Utilities.randomInt(3);
      this.particleSet = s;
   }

   @Override
   public void update() throws SlickException {
      this.decayTick++;
      if (this.decayTick > this.decay
         || this.collision.checkParticleCollision((int)this.particleX, (int)this.particleY, CollisionModule.CollisionHeight.MEDIUM)
            != this.collision.getCollisionNone()) {
         this.projectile.remove(this);
      }

      if (Utilities.randomInt(5) == 0) {
         this.directionX = Utilities.randomInt(3);
         this.directionY = Utilities.randomInt(3);
      }

      this.particles.newParticleGroup(this.particleX, this.particleY, ParticleModule.ParticleGroup.SPARKLES, this.particleSet);
      if (this.directionX == 0.0F) {
         this.particleX += 2.0F;
      } else if (this.directionX == 1.0F) {
         this.particleX -= 2.0F;
      }

      if (this.directionY == 0.0F) {
         this.particleY += 2.0F;
      } else if (this.directionY == 1.0F) {
         this.particleY -= 2.0F;
      }
   }
}
