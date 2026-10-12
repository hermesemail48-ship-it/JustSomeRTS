package rtr.particles.projectiles;

import org.newdawn.slick.SlickException;
import rtr.map.CollisionModule;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class ProjectileExpandingFireRing extends ProjectileBase {
   private float angle;
   private int startX;
   private int startY;
   private float radius;
   private float currentRadius;
   private float speed;
   private ParticleModule.ParticleSet particleSet;
   private boolean guiProjectile;

   public ProjectileExpandingFireRing(int x, int y, int r, float s, ParticleModule.ParticleSet pS, boolean gP) {
      super(x, y);
      this.startX = x;
      this.startY = y;
      this.angle = (float)Math.toRadians(Utilities.randomInt(3600) / 10.0);
      this.particleSet = pS;
      this.guiProjectile = gP;
      this.radius = r;
      this.speed = s;
   }

   @Override
   public void update() throws SlickException {
      int particleAmount = 360;
      if (this.currentRadius < 360.0F) {
         particleAmount = (int)this.currentRadius;
      } else {
         particleAmount = (int)(particleAmount + this.currentRadius / 4.0F);
      }

      particleAmount /= 2;

      for (int x = 0; x < particleAmount; x++) {
         this.angle += x;
         this.projectileX = (float)(Math.cos(this.angle) * this.currentRadius + this.startX);
         this.projectileY = (float)(Math.sin(this.angle) * this.currentRadius + this.startY);
         if (this.guiProjectile
            || this.collision.checkParticleCollision((int)this.projectileX, (int)this.projectileY, CollisionModule.CollisionHeight.MEDIUM)
               == this.collision.getCollisionNone()) {
            this.particles
               .newParticleGroup(
                  this.projectileX + Utilities.randomInt(-1, 1),
                  this.projectileY + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleGroup.FIRE,
                  this.particleSet,
                  this.guiProjectile
               );
            if (Utilities.randomInt(400) == 0) {
               this.particles
                  .newParticleGroup(
                     this.projectileX + Utilities.randomInt(-1, 1),
                     this.projectileY + Utilities.randomInt(-1, 1),
                     ParticleModule.ParticleGroup.SMOKE_BELLOW,
                     this.particleSet,
                     this.guiProjectile
                  );
            }
         }
      }

      this.currentRadius = this.currentRadius + this.speed;
      if (this.currentRadius >= this.radius) {
         this.projectile.remove(this);
      }
   }
}
