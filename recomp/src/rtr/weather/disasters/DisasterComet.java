package rtr.weather.disasters;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.objects.rangebuilder.RangeBuilder;
import rtr.save.YMLDataMap;
import rtr.utilities.Utilities;

public class DisasterComet extends DisasterBase {
   public DisasterComet(int tileX, int tileY, boolean gT) {
      this.disasterType = DisasterBase.DisasterType.COMET;
      this.godTriggered = gT;
      this.duration = 2000;
      this.centerX = tileX;
      this.centerY = tileY;
      this.enviromentColor = new Color(0.0F, 0.0F, 0.2F);
      this.enviromentLightAdjust = 30;
   }

   public DisasterComet(boolean gT) throws SlickException {
      this.disasterType = DisasterBase.DisasterType.COMET;
      this.godTriggered = gT;
      this.duration = 2000;
      Console.newBanner(Text.getText("console.banner.weather.disaster.comet.header"), Text.getText("console.banner.weather.disaster.comet.subtitle"));
      this.sound.playSound(SoundModule.SoundType.DISASTER_START);

      for (int x = 0; x < 4; x++) {
         this.centerX = Utilities.randomInt(256);
         this.centerY = Utilities.randomInt(256);
         if (this.map.getTileId(this.centerX, this.centerY, 0) == 0) {
            x--;
         } else {
            int desirability = this.object.getRangeMap(RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY)[this.centerX][this.centerY];
            int corruption = this.object.getRangeMap(RangeBuilder.RangeMapLayer.CORRUPTION_RESISTANCE)[this.centerX][this.centerY];
            if (corruption > 0) {
               desirability += 10;
            }

            if (desirability > 0) {
               if (Utilities.randomInt(desirability) == 0) {
                  break;
               }

               x--;
            }
         }
      }

      this.enviromentColor = new Color(0.0F, 0.0F, 0.2F);
      this.enviromentLightAdjust = 30;
   }

   public DisasterComet(YMLDataMap dataMap) {
      this.loadDisaster(dataMap);
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      if (this.durationTick == 100) {
         this.weather.newComet(this.centerX * 16, this.centerY * 16, this.godTriggered);
      }

      if (this.durationTick > this.duration) {
         this.weather.removeDisaster(this);
      }
   }
}
