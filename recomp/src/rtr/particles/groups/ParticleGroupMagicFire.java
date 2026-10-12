package rtr.particles.groups;

import rtr.particles.ParticleModule;

public class ParticleGroupMagicFire extends ParticleGroupBase {
   public ParticleGroupMagicFire() {
      this.load();
   }

   @Override
   public void load() {
      this.createArrays(3);
      this.addParticle(0, ParticleModule.ParticleType.FIRE_SPECK, 0, 1, 100);
      this.addParticle(1, ParticleModule.ParticleType.FIRE_SMALL, 0, 1, 10);
      this.addParticle(2, ParticleModule.ParticleType.SMOKE_SPECK, ParticleModule.ParticleSet.LIGHT_GRAY, ParticleModule.ParticleSet.GRAY, 1);
      this.loadWeighter();
   }
}
