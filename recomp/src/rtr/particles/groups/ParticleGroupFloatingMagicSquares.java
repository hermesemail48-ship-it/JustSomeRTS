package rtr.particles.groups;

import rtr.particles.ParticleModule;

public class ParticleGroupFloatingMagicSquares extends ParticleGroupBase {
   public ParticleGroupFloatingMagicSquares() {
      this.load();
   }

   @Override
   public void load() {
      this.createArrays(7);
      this.addParticle(0, ParticleModule.ParticleType.MAGIC_SMOKE_SPECK, 0, 1, 100);
      this.addParticle(1, ParticleModule.ParticleType.MAGIC_FLOAT_SPECK, 0, 1, 100);
      this.addParticle(2, ParticleModule.ParticleType.MAGIC_FLOAT_SPECKS_2PX, 0, 1, 60);
      this.addParticle(3, ParticleModule.ParticleType.MAGIC_FLOAT_EXPANDING_SQUARE_RING_4PX_DISSIPATING, 0, 1, 30);
      this.addParticle(4, ParticleModule.ParticleType.MAGIC_FLOAT_EXPANDING_SQUARE_RING_8PX_DISSIPATING, 0, 1, 1);
      this.addParticle(5, ParticleModule.ParticleType.MAGIC_FLOAT_EXPANDING_SQUARE_RING_12PX_DISSIPATING, 0, 1, 1);
      this.addParticle(6, ParticleModule.ParticleType.MAGIC_FLOAT_EXPANDING_SQUARE_RING_16PX_DISSIPATING, 0, 1, 1);
      this.loadWeighter();
   }
}
