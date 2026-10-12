package rtr.particles.groups;

import rtr.particles.ParticleModule;

public class ParticleGroupElectricLines extends ParticleGroupBase {
   public ParticleGroupElectricLines() {
      this.load();
   }

   @Override
   public void load() {
      this.createArrays(5);
      this.addParticle(0, ParticleModule.ParticleType.FLASH_SPECK, 0, 1, 100);
      this.addParticle(1, ParticleModule.ParticleType.FLASH_LINE_2PX, 0, 1, 50);
      this.addParticle(2, ParticleModule.ParticleType.FLASH_LINE_3PX, 0, 1, 15);
      this.addParticle(3, ParticleModule.ParticleType.FLASH_LINE_4PX, 0, 1, 10);
      this.addParticle(4, ParticleModule.ParticleType.FLASH_LINE_5PX, 0, 1, 5);
      this.loadWeighter();
   }
}
