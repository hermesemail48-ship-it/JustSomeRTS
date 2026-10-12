package rtr.particles.projectiles;

import org.newdawn.slick.SlickException;
import rtr.particles.ParticleModule;
import rtr.utilities.FastMath;
import rtr.utilities.Utilities;

public class ProjectileExpandingEnergyTrail extends ProjectileBase {
   private float angle;
   private int startX;
   private int startY;
   private float radius;
   private ParticleModule.ParticleSet particleSet;
   private boolean guiProjectile;

   public ProjectileExpandingEnergyTrail(int x, int y, ParticleModule.ParticleSet s, boolean gP) {
      super(x, y);
      this.startX = x;
      this.startY = y;
      this.decay = Utilities.randomInt(200) + 50;
      this.angle = (float)Math.toRadians(Utilities.randomInt(3600) / 10.0);
      this.particleSet = s;
      this.guiProjectile = gP;
      this.radius = 0.1F;
   }

   @Override
   public void update() throws SlickException {
      this.decayTick++;
      if (this.decayTick > this.decay) {
         this.projectile.remove(this);
      }

      this.angle = (float)(this.angle + 0.02);
      this.radius += 0.2F;
      this.projectileX = FastMath.cos(this.angle) * this.radius + this.startX;
      this.projectileY = FastMath.sin(this.angle) * this.radius + this.startY;
      if (this.decay % 2 == 0) {
         this.particles
            .newParticleGroup(
               this.projectileX, this.projectileY, ParticleModule.ParticleGroup.MAGIC_FIRE, this.particleSet, this.particleSet, this.guiProjectile
            );
      }

      if (Utilities.randomInt(10) == 0) {
         this.particles
            .newParticleGroup(
               this.projectileX + Utilities.randomInt(-1, 1),
               this.projectileY + Utilities.randomInt(-1, 1),
               ParticleModule.ParticleGroup.SPARKLES,
               this.particleSet,
               this.particleSet,
               this.guiProjectile
            );
      }
   }
}
