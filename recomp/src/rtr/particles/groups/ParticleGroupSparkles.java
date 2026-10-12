package rtr.particles.groups;

import rtr.particles.ParticleModule;

public class ParticleGroupSparkles extends ParticleGroupBase {
   public ParticleGroupSparkles() {
      this.load();
   }

   @Override
   public void load() {
      this.createArrays(4);
      this.addParticle(0, ParticleModule.ParticleType.SPARKLE_SPECK, 0, 1, 20);
      this.addParticle(1, ParticleModule.ParticleType.SPARKLE_SMALL, 0, 1, 1);
      this.addParticle(2, ParticleModule.ParticleType.SPARKLE_MEDIUM, 0, 1, 1);
      this.addParticle(3, ParticleModule.ParticleType.SPARKLE_LARGE, 0, 1, 1);
      this.loadWeighter();
   }
}
