package rtr.particles.groups;

import rtr.particles.ParticleModule;

public class ParticleGroupFire extends ParticleGroupBase {
   public ParticleGroupFire() {
      this.load();
   }

   @Override
   public void load() {
      this.createArrays(4);
      this.addParticle(0, ParticleModule.ParticleType.FIRE_SPECK, 0, ParticleModule.ParticleSet.BLACK, 100);
      this.addParticle(1, ParticleModule.ParticleType.FIRE_SMALL, 0, ParticleModule.ParticleSet.BLACK, 10);
      this.addParticle(2, ParticleModule.ParticleType.FIRE_LARGE, 0, ParticleModule.ParticleSet.BLACK, 10);
      this.addParticle(3, ParticleModule.ParticleType.SMOKE_SPECK, ParticleModule.ParticleSet.BLACK, ParticleModule.ParticleSet.GRAY, 1);
      this.loadWeighter();
   }
}
