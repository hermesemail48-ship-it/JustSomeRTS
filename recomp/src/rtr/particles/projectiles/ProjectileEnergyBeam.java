package rtr.particles.projectiles;

import org.newdawn.slick.SlickException;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class ProjectileEnergyBeam extends ProjectileBase {
   protected float targetX;
   protected float targetY;
   protected float angle;

   public ProjectileEnergyBeam(int x, int y, int x2, int y2) {
      super(x, y);
      this.decay = 2000;
      this.targetX = x2;
      this.targetY = y2;
      this.velX = 1.0F;
      this.velY = 1.0F;
      this.angle = Utilities.calculateAngle(x, y, this.targetX, this.targetY);
   }

   @Override
   public void update() throws SlickException {
      this.decayTick++;
      if (this.decayTick > this.decay) {
         this.projectile.remove(this);
      }

      boolean arrived = false;

      while (!arrived) {
         this.projectileX = (float)(this.projectileX + Math.cos(this.angle) * this.velX);
         this.projectileY = (float)(this.projectileY + Math.sin(this.angle) * this.velY);
         this.particles
            .newParticleGroup(
               this.projectileX + Utilities.randomInt(2) - 1.0F,
               this.projectileY + Utilities.randomInt(2) - 1.0F,
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.FIRE_BLUE,
               ParticleModule.ParticleSet.FIRE_PURPLE
            );
         this.particles
            .newParticleGroup(
               this.projectileX + Utilities.randomInt(2) - 1.0F,
               this.projectileY + Utilities.randomInt(2) - 1.0F,
               ParticleModule.ParticleGroup.SPARKLES,
               ParticleModule.ParticleSet.MAGIC_BLUE
            );
         if ((int)this.projectileX >= this.targetX - 1.0F
            && (int)this.projectileX <= this.targetX + 1.0F
            && (int)this.projectileY >= this.targetY - 1.0F
            && (int)this.projectileY <= this.targetY + 1.0F) {
            arrived = true;
         }
      }

      this.projectile.remove(this);
   }
}
