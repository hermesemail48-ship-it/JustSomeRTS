package rtr.particles.groups;

import rtr.particles.ParticleModule;

public class ParticleGroupFloatingDust extends ParticleGroupBase {
   public ParticleGroupFloatingDust() {
      this.load();
   }

   @Override
   public void load() {
      this.createArrays(3);
      this.addParticle(0, ParticleModule.ParticleType.FLOATING_DUST_SPECK, 0, 1, 100);
      this.addParticle(1, ParticleModule.ParticleType.FLOATING_DUST_CIRCLE_2PX, 0, 1, 100);
      this.addParticle(2, ParticleModule.ParticleType.FLOATING_DUST_CIRCLE_4PX, 0, 1, 60);
      this.loadWeighter();
   }
}
