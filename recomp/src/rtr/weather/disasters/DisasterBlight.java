package rtr.weather.disasters;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.FarmlandBase;
import rtr.save.YMLDataMap;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class DisasterBlight extends DisasterBase {
   private ArrayList<OrderedPair> coordinatesWithin = new ArrayList<>();

   public DisasterBlight(int tileX, int tileY, boolean gT) {
      this.disasterType = DisasterBase.DisasterType.BLIGHT;
      this.godTriggered = gT;
      this.duration = 15000 + Utilities.randomInt(5000);
      this.centerX = tileX;
      this.centerY = tileY;
      this.coordinatesWithin = Utilities.getAllCoordinatesWithin(this.centerX, this.centerY, 32);
   }

   public DisasterBlight(boolean gT) throws SlickException {
      this.disasterType = DisasterBase.DisasterType.BLIGHT;
      this.godTriggered = gT;
      this.duration = 15000 + Utilities.randomInt(5000);
      Console.newBanner(Text.getText("console.banner.weather.disaster.blight.header"), Text.getText("console.banner.weather.disaster.blight.subtitle"));
      this.sound.playSound(SoundModule.SoundType.DISASTER_START);

      for (int x = 0; x < 20; x++) {
         this.centerX = Utilities.randomInt(256);
         this.centerY = Utilities.randomInt(256);
         if (this.object.isInRange(this.centerX, this.centerY)) {
            break;
         }
      }

      this.coordinatesWithin = Utilities.getAllCoordinatesWithin(this.centerX, this.centerY, 32);
   }

   public DisasterBlight(YMLDataMap dataMap) {
      this.loadDisaster(dataMap);
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      OrderedPair o = this.coordinatesWithin.get(Utilities.randomInt(this.coordinatesWithin.size()));
      int randomX = o.getX();
      int randomY = o.getY();
      if (Utilities.randomInt(20) == 0) {
         if (this.resource.getResourceValues()[randomX][randomY] > 0) {
            this.map.killResourceTerrain(randomX, randomY);
         }

         ObjectBase possibleFarm = this.object.getBuildingAtLocation(randomX, randomY);
         if (possibleFarm != null && possibleFarm.getLocalFarmland().size() > 0) {
            for (FarmlandBase f : possibleFarm.getLocalFarmland()) {
               f.damageCrops();
            }
         }

         MobBase m = this.mob.getMobAt(randomX, randomY);
         if (m != null) {
            m.takeDamage(Utilities.randomInt(10) + 1, randomX, randomY, MobBase.DamageType.BLIGHT, MobBase.DamageCause.FROM_BLIGHT);
         }
      }

      int particleX = randomX * 16;
      int particleY = randomY * 16;
      if (Utilities.randomInt(150) == 0) {
         this.sound.playSound(SoundModule.SoundType.DISSOLVE, particleX, particleY);
      }

      if (this.map.isInView(particleX, particleY, 64)) {
         for (int x = 0; x < 5; x++) {
            this.particle
               .newParticle(
                  particleX + Utilities.randomInt(16),
                  particleY + Utilities.randomInt(16),
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.BLACK
               );
         }

         this.particle
            .newParticle(
               particleX + Utilities.randomInt(16),
               particleY + Utilities.randomInt(16),
               ParticleModule.ParticleType.MAGIC_FLOAT_SPECK,
               ParticleModule.ParticleSet.BLACK
            );
      }

      if (this.durationTick > this.duration) {
         this.weather.removeDisaster(this);
      }
   }

   @Override
   protected void loadDisaster(YMLDataMap dataMap) {
      super.loadDisaster(dataMap);
      if (dataMap.containsKey("coordinatesWithin")) {
         this.coordinatesWithin = dataMap.unpackCoordinatesList("coordinatesWithin");
      }
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = super.getRegionalSaveData();
      if (this.coordinatesWithin.size() > 0) {
         dataMap.put("coordinatesWithin", dataMap.packCoordinatesList(this.coordinatesWithin));
      }

      return dataMap;
   }
}
