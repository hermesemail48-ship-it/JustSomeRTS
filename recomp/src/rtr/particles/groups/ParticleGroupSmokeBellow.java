package rtr.particles.groups;

import rtr.particles.ParticleModule;

public class ParticleGroupSmokeBellow extends ParticleGroupBase {
   public ParticleGroupSmokeBellow() {
      this.load();
   }

   @Override
   public void load() {
      this.createArrays(5);
      this.addParticle(0, ParticleModule.ParticleType.SMOKE_SPECK, 0, 1, 40);
      this.addParticle(1, ParticleModule.ParticleType.SMOKE_LARGE_SPECK, 0, 1, 40);
      this.addParticle(2, ParticleModule.ParticleType.SMOKE_SMALL_PUFF, 0, 1, 10);
      this.addParticle(3, ParticleModule.ParticleType.SMOKE_MEDIUM_PUFF, 0, 1, 10);
      this.addParticle(4, ParticleModule.ParticleType.SMOKE_LARGE_PUFF, 0, 1, 2);
      this.loadWeighter();
   }
}
