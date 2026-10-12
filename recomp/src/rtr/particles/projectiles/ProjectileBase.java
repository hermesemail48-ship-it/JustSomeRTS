package rtr.particles.projectiles;

import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.map.CollisionModule;
import rtr.map.MapModule;
import rtr.mobs.MobModule;
import rtr.particles.ParticleModule;
import rtr.states.StateBase;

public class ProjectileBase {
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected ParticleModule particles = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   protected ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   protected CollisionModule collision = (CollisionModule)StateBase.getModule(ModuleBase.ModuleType.COLLISION);
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected float projectileX;
   protected float projectileY;
   protected float velX;
   protected float velY;
   protected float velMod;
   protected int decay;
   protected int decayTick;

   public ProjectileBase(int x, int y) {
      this.projectileX = x;
      this.projectileY = y;
   }

   public void update() throws SlickException {
   }
}
