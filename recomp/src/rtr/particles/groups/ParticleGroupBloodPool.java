package rtr.particles.groups;

import rtr.particles.ParticleModule;

public class ParticleGroupBloodPool extends ParticleGroupBase {
   public ParticleGroupBloodPool() {
      this.load();
   }

   @Override
   public void load() {
      this.createArrays(3);
      this.addParticle(0, ParticleModule.ParticleType.BLOOD_POOL_SPECK, 0, 1, 1);
      this.addParticle(1, ParticleModule.ParticleType.BLOOD_POOL_SMALL, 0, 1, 1);
      this.addParticle(2, ParticleModule.ParticleType.BLOOD_POOL_LARGE, 0, 1, 1);
      this.loadWeighter();
   }
}
