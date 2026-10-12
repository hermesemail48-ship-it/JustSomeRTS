package rtr.particles.groups;

import rtr.particles.ParticleModule;

public class ParticleGroupDust extends ParticleGroupBase {
   public ParticleGroupDust() {
      this.load();
   }

   @Override
   public void load() {
      this.createArrays(6);
      this.addParticle(0, ParticleModule.ParticleType.DUST_SPECK, 0, 1, 20);
      this.addParticle(1, ParticleModule.ParticleType.DUST_SPECKS_2PX, 0, 1, 20);
      this.addParticle(2, ParticleModule.ParticleType.DUST_SPECKS_3PX, 0, 1, 5);
      this.addParticle(3, ParticleModule.ParticleType.DUST_SPECKS_4PX, 0, 1, 5);
      this.addParticle(4, ParticleModule.ParticleType.DUST_SPLATTER_3PX, 0, 1, 5);
      this.addParticle(5, ParticleModule.ParticleType.DUST_SPLATTER_5PX, 0, 1, 5);
      this.loadWeighter();
   }
}
