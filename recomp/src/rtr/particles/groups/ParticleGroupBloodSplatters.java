package rtr.particles.groups;

import rtr.particles.ParticleModule;

public class ParticleGroupBloodSplatters extends ParticleGroupBase {
   public ParticleGroupBloodSplatters() {
      this.load();
   }

   @Override
   public void load() {
      this.createArrays(3);
      this.addParticle(0, ParticleModule.ParticleType.BLOOD_SPECK, 0, 1, 1);
      this.addParticle(1, ParticleModule.ParticleType.BLOOD_SMALL, 0, 1, 1);
      this.addParticle(2, ParticleModule.ParticleType.BLOOD_LARGE, 0, 1, 1);
      this.loadWeighter();
   }
}
