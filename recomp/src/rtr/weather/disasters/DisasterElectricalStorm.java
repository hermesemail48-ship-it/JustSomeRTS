package rtr.weather.disasters;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.particles.ParticleModule;
import rtr.save.YMLDataMap;
import rtr.utilities.Utilities;

public class DisasterElectricalStorm extends DisasterBase {
   public DisasterElectricalStorm(int tileX, int tileY, boolean gT) {
      this.disasterType = DisasterBase.DisasterType.ELECTRICAL_STORM;
      this.godTriggered = gT;
      this.duration = 3000;
      this.centerX = tileX;
      this.centerY = tileY;
      this.enviromentColor = new Color(0.0F, 0.0F, 0.5F);
      this.enviromentLightAdjust = -10;
   }

   public DisasterElectricalStorm(boolean gT) throws SlickException {
      this.disasterType = DisasterBase.DisasterType.ELECTRICAL_STORM;
      this.godTriggered = gT;
      this.duration = 3000;
      Console.newBanner(
         Text.getText("console.banner.weather.disaster.electricalStorm.header"), Text.getText("console.banner.weather.disaster.electricalStorm.subtitle")
      );
      this.sound.playSound(SoundModule.SoundType.DISASTER_START);

      for (int x = 0; x < 20; x++) {
         this.centerX = Utilities.randomInt(256);
         this.centerY = Utilities.randomInt(256);
         if (this.object.isInRange(this.centerX, this.centerY)) {
            break;
         }
      }

      this.enviromentColor = new Color(0.0F, 0.0F, 0.5F);
      this.enviromentLightAdjust = -10;
   }

   public DisasterElectricalStorm(YMLDataMap dataMap) {
      this.loadDisaster(dataMap);
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      if (Utilities.randomInt(100) == 0) {
         int randomBolts = Utilities.randomInt(3) + 1;

         for (int x = 0; x < randomBolts; x++) {
            int length = Utilities.randomInt(250) + 200;
            int randomX = this.centerX + Utilities.randomInt(-42, 42);
            int randomY = this.centerY + Utilities.randomInt(-42, 42);
            if (!Utilities.isOutOfBoundsOfUsableMap(randomX, randomY)) {
               this.weather.newLightning(randomX, randomY, length, this.godTriggered, ParticleModule.ParticleSet.MAGIC_BLUE);
            }
         }
      }

      for (int xx = 0; xx < 5; xx++) {
         int randomX = Utilities.randomInt(4096);
         int randomY = Utilities.randomInt(4096);
         if (this.map.isInView(randomX, randomY, 64)) {
            this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.RAIN, ParticleModule.ParticleSet.WATER);
            this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.SMOKE_SPECK, ParticleModule.ParticleSet.MAGIC_BLUE);
            this.projectile.newGroundStatic(randomX, randomY, ParticleModule.ParticleSet.MAGIC_BLUE);
         }
      }

      for (int xxx = 0; xxx < 25; xxx++) {
         int randomX = Utilities.randomInt(4096);
         int randomY = Utilities.randomInt(4096);
         if (this.map.isInView(randomX, randomY, 64)) {
            this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.SMOKE_SPECK, ParticleModule.ParticleSet.MAGIC_BLUE);
         }
      }

      if (this.durationTick > this.duration) {
         this.weather.removeDisaster(this);
      }
   }
}
