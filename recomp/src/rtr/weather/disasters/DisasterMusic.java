package rtr.weather.disasters;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.save.YMLDataMap;
import rtr.utilities.Utilities;

public class DisasterMusic extends DisasterBase {
   public DisasterMusic(boolean gT) {
      this.disasterType = DisasterBase.DisasterType.MUSIC;
      this.godTriggered = gT;
      this.duration = 3500;
      this.enviromentColor = new Color(-2.0F, 0.25F, 1.0F);
      this.enviromentLightAdjust = -300;
      this.sound.playLostSoul();
   }

   public DisasterMusic(YMLDataMap dataMap) {
      this.loadDisaster(dataMap);
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;

      for (int x = 0; x < 200; x++) {
         String color = "White";
         switch (Utilities.randomInt(5)) {
            case 0:
               color = "Blue";
               break;
            case 1:
               color = "Purple";
               break;
            case 2:
               color = "Green";
               break;
            case 3:
               color = "Yellow";
         }

         this.font.newFloater("iconMusicNote" + color, Utilities.randomInt(4096), Utilities.randomInt(4096), Utilities.randomInt(2) == 0);
      }

      for (int x = 0; x < 10; x++) {
         this.lighting.addShortTermLight(Utilities.randomInt(256), Utilities.randomInt(256), (byte)8, (byte)8, 50);
      }

      if (this.durationTick >= this.duration) {
         this.weather.removeDisaster(this);
      }
   }
}
