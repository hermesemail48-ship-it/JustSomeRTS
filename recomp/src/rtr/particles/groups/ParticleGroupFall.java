package rtr.particles.groups;

import rtr.particles.ParticleModule;

public class ParticleGroupFall extends ParticleGroupBase {
   public ParticleGroupFall() {
      this.load();
   }

   @Override
   public void load() {
      this.createArrays(16);
      this.addParticle(0, ParticleModule.ParticleType.FALL_SPECK, 0, 1, 50);
      this.addParticle(1, ParticleModule.ParticleType.FALL_SPECKS_2PX, 0, 1, 40);
      this.addParticle(2, ParticleModule.ParticleType.FALL_SPECKS_3PX, 0, 1, 30);
      this.addParticle(3, ParticleModule.ParticleType.FALL_SPECKS_4PX, 0, 1, 20);
      this.addParticle(4, ParticleModule.ParticleType.FALL_SPLATTER_3PX, 0, 1, 30);
      this.addParticle(5, ParticleModule.ParticleType.FALL_SPLATTER_5PX, 0, 1, 20);
      this.addParticle(6, ParticleModule.ParticleType.FALL_FAST_SPECK, 0, 1, 5);
      this.addParticle(7, ParticleModule.ParticleType.FALL_FAST_SPECKS_2PX, 0, 1, 4);
      this.addParticle(8, ParticleModule.ParticleType.FALL_FAST_SPECKS_3PX, 0, 1, 3);
      this.addParticle(9, ParticleModule.ParticleType.FALL_FAST_SPECKS_4PX, 0, 1, 2);
      this.addParticle(10, ParticleModule.ParticleType.FALL_FAST_SPLATTER_3PX, 0, 1, 3);
      this.addParticle(11, ParticleModule.ParticleType.FALL_FAST_SPLATTER_5PX, 0, 1, 2);
      this.loadWeighter();
   }
}
