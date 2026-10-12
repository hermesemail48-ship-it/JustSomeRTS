package rtr.particles.projectiles;

import org.newdawn.slick.SlickException;
import rtr.particles.ParticleModule;
import rtr.utilities.FastMath;
import rtr.utilities.Utilities;

public class ProjectileEnergyNova extends ProjectileBase {
   float angle;
   ParticleModule.ParticleSet particleSet;

   public ProjectileEnergyNova(int x, int y, ParticleModule.ParticleSet s) {
      super(x, y);
      this.decay = Utilities.randomInt(200) + 200;
      this.velX = Utilities.randomFloat() / 5.0F + 0.4F;
      this.velY = Utilities.randomFloat() / 5.0F + 0.4F;
      this.angle = (float)Math.toRadians(Utilities.randomInt(3600) / 10.0);
      this.particleSet = s;
   }

   @Override
   public void update() throws SlickException {
      this.decayTick++;
      if (this.decayTick > this.decay) {
         this.projectile.remove(this);
      }

      this.angle = (float)(this.angle + 0.01);
      this.projectileX = this.projectileX + FastMath.cos(this.angle) * this.velX;
      this.projectileY = this.projectileY + FastMath.sin(this.angle) * this.velY;
      this.particles.newParticle(this.projectileX, this.projectileY, ParticleModule.ParticleType.FIRE_SPECK, ParticleModule.ParticleSet.FIRE_BLUE);
      this.particles
         .newParticle(
            this.projectileX + Utilities.randomInt(-1, 1),
            this.projectileY + Utilities.randomInt(-1, 1),
            ParticleModule.ParticleType.FIRE_SPECK,
            this.particleSet
         );
      if (Utilities.randomInt(40) == 0) {
         this.particles
            .newParticle(
               this.projectileX + Utilities.randomInt(-1, 1),
               this.projectileY + Utilities.randomInt(-1, 1),
               ParticleModule.ParticleType.SPARKLE_SPECK,
               this.particleSet
            );
      }
   }
}
