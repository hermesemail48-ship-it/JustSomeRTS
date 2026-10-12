package rtr.weather.disasters;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.save.YMLDataMap;
import rtr.utilities.Utilities;

public class DisasterMeteorShower extends DisasterBase {
   public DisasterMeteorShower(int tileX, int tileY, boolean gT) {
      this.disasterType = DisasterBase.DisasterType.METEOR_SHOWER;
      this.godTriggered = gT;
      this.duration = 5000 + Utilities.randomInt(1000);
      this.centerX = tileX;
      this.centerY = tileY;
      this.enviromentColor = new Color(0.5F, 0.1F, 0.0F);
      this.enviromentLightAdjust = 20;
   }

   public DisasterMeteorShower(boolean gT) throws SlickException {
      this.disasterType = DisasterBase.DisasterType.METEOR_SHOWER;
      this.godTriggered = gT;
      this.duration = 5000 + Utilities.randomInt(1000);
      Console.newBanner(
         Text.getText("console.banner.weather.disaster.meteorShower.header"), Text.getText("console.banner.weather.disaster.meteorShower.subtitle")
      );
      this.sound.playSound(SoundModule.SoundType.DISASTER_START);

      for (int x = 0; x < 20; x++) {
         this.centerX = Utilities.randomInt(256);
         this.centerY = Utilities.randomInt(256);
         if (this.object.isInRange(this.centerX, this.centerY)) {
            break;
         }
      }

      this.enviromentColor = new Color(0.5F, 0.1F, 0.0F);
      this.enviromentLightAdjust = 20;
   }

   public DisasterMeteorShower(YMLDataMap dataMap) {
      this.loadDisaster(dataMap);
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      if (Utilities.randomInt(150) == 0) {
         int randomX = this.centerX + Utilities.randomInt(-16, 16);
         int randomY = this.centerY + Utilities.randomInt(-16, 16);
         if (!Utilities.isOutOfBoundsOfUsableMap(randomX, randomY)) {
            int targetX = randomX * 16 + 8;
            int targetY = randomY * 16 + 8;
            this.weather.newMeteor(targetX, targetY, this.godTriggered);
         }
      }

      if (this.durationTick > this.duration) {
         this.weather.removeDisaster(this);
      }
   }
}
