package rtr.particles.projectiles;

import org.newdawn.slick.SlickException;
import rtr.particles.ParticleModule;
import rtr.utilities.FastMath;
import rtr.utilities.Utilities;

public class ProjectileEnergyRing extends ProjectileBase {
   float angle;
   int startX;
   int startY;
   int radius;
   ParticleModule.ParticleSet colorOne;
   ParticleModule.ParticleSet colorTwo;

   public ProjectileEnergyRing(int x, int y, int r, ParticleModule.ParticleSet c1) {
      this(x, y, r, c1, c1);
   }

   public ProjectileEnergyRing(int x, int y, int r, ParticleModule.ParticleSet c1, ParticleModule.ParticleSet c2) {
      super(x, y);
      this.startX = x;
      this.startY = y;
      this.decay = Utilities.randomInt(25) + 25;
      this.angle = (float)Math.toRadians(Utilities.randomInt(3600) / 10.0);
      this.colorOne = c1;
      this.colorTwo = c2;
      this.radius = r;
   }

   @Override
   public void update() throws SlickException {
      this.decayTick++;
      if (this.decayTick > this.decay) {
         this.projectile.remove(this);
      }

      this.angle = (float)(this.angle + 0.03);
      this.projectileX = FastMath.cos(this.angle) * this.radius + this.startX;
      this.projectileY = FastMath.sin(this.angle) * this.radius + this.startY;
      this.particles
         .newParticleGroup(
            this.projectileX + Utilities.randomInt(2) - 1.0F,
            this.projectileY + Utilities.randomInt(2) - 1.0F,
            ParticleModule.ParticleGroup.MAGIC_FIRE,
            this.colorOne,
            this.colorTwo
         );
      if (Utilities.randomInt(10) == 0) {
         this.particles
            .newParticleGroup(
               this.projectileX + Utilities.randomInt(2) - 1.0F,
               this.projectileY + Utilities.randomInt(2) - 1.0F,
               ParticleModule.ParticleGroup.SPARKLES,
               this.colorOne,
               this.colorTwo
            );
      }
   }
}
