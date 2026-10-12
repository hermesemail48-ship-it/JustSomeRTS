package rtr.particles.groups;

import rtr.particles.ParticleModule;

public class ParticleGroupMagicSmoke extends ParticleGroupBase {
   public ParticleGroupMagicSmoke() {
      this.load();
   }

   @Override
   public void load() {
      this.createArrays(3);
      this.addParticle(0, ParticleModule.ParticleType.MAGIC_SMOKE_SPECK, 0, 1, 30);
      this.addParticle(1, ParticleModule.ParticleType.MAGIC_SMOKE_SMALL, 0, 1, 10);
      this.addParticle(2, ParticleModule.ParticleType.MAGIC_SMOKE_PUFF, 0, 1, 1);
      this.loadWeighter();
   }
}
