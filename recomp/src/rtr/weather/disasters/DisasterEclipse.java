package rtr.weather.disasters;

import org.newdawn.slick.SlickException;
import rtr.TimeModule;
import rtr.mobs.MobBase;
import rtr.save.YMLDataMap;
import rtr.utilities.Utilities;

public class DisasterEclipse extends DisasterBase {
   public DisasterEclipse(boolean gT) {
      this.disasterType = DisasterBase.DisasterType.ECLIPSE;
      this.godTriggered = gT;
      this.enviromentLightAdjust = -250;
      this.enviromentTempAdjust = -3;
   }

   public DisasterEclipse(YMLDataMap dataMap) {
      this.loadDisaster(dataMap);
   }

   @Override
   public void update() throws SlickException {
      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
         if (Utilities.randomInt(5000) == 0 && m.isAvailable()) {
            this.mob.tryConfuseInArea(m.getTileX(), m.getTileY(), 2, this.godTriggered, 1);
         }
      }

      if (this.time.getTimeOfDay() != TimeModule.TimeOfDay.MIDDAY) {
         this.weather.removeDisaster(this);
      }
   }
}
