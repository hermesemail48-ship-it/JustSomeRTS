package rtr.weather.disasters;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.map.CollisionModule;
import rtr.particles.ParticleModule;
import rtr.save.YMLDataMap;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class DisasterRain extends DisasterBase {
   private int weatherIntensity;
   private int lightningChance;
   private boolean isLightning;
   private ArrayList<OrderedPair> waterPuddles = new ArrayList<>();

   public DisasterRain(boolean gT) {
      this.disasterType = DisasterBase.DisasterType.RAIN;
      this.godTriggered = gT;
      this.duration = Utilities.randomInt(20000) + 5000;
      this.enviromentColor = new Color(-0.5F, -0.5F, 0.5F);
      this.enviromentLightAdjust = -50;
      this.enviromentTempAdjust = -2;
      this.enviromentWindAdjust = 0.1;
      this.velocityMultiplier = -0.1F;
      this.sound.playRain();
      if (Utilities.randomInt(this.weather.getSevereStormChance()) == 0) {
         this.weatherIntensity = Utilities.randomInt(400) + 201;
         this.lightningChance = Utilities.randomInt(250) + 150;
      } else {
         this.weatherIntensity = Utilities.randomInt(200) + 101;
         this.lightningChance = Utilities.randomInt(2400) + 100;
      }

      if (this.lightningChance <= 1000 && Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.MAP_EDITOR) {
         this.isLightning = true;
      } else {
         this.isLightning = false;
      }

      this.makePuddles();
   }

   public DisasterRain(YMLDataMap dataMap) {
      this.loadDisaster(dataMap);
   }

   @Override
   public void update() throws SlickException {
      this.durationTick++;
      if (this.isLightning) {
         if (Utilities.randomInt(20000 - this.weatherIntensity * 3) == 0) {
            this.sound.playSound(SoundModule.SoundType.THUNDER);
         }

         if (Utilities.randomInt(this.lightningChance) == 0) {
            int randomBolts = Utilities.randomInt(3) + 1;

            for (int x = 0; x < randomBolts; x++) {
               int length = Utilities.randomInt(250) + 200;
               int lightningX = Utilities.randomInt(256);
               int lightningY = Utilities.randomInt(256);
               this.weather.newLightning(lightningX, lightningY, length, this.godTriggered, ParticleModule.ParticleSet.MAGIC_BLUE);
            }
         }
      }

      int rainIntensityMod = this.weatherIntensity;
      if (ScaleControl.getWorldScale() == 1.0F) {
         rainIntensityMod /= 2;
      }

      if (ScaleControl.getWorldScale() == 3.0F) {
         rainIntensityMod *= 2;
      }

      if (ScaleControl.getWorldScale() >= 4.0F) {
         rainIntensityMod *= 3;
      }

      for (int x = 0; x < rainIntensityMod; x++) {
         int randomX = Utilities.randomInt(4096);
         int randomY = Utilities.randomInt(4096);
         if (this.map.isInView(randomX, randomY, 64)) {
            this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.RAIN, ParticleModule.ParticleSet.WATER);
         }
      }

      for (int xx = 0; xx < rainIntensityMod * 20; xx++) {
         int randomX = Utilities.randomInt(4096);
         int randomY = Utilities.randomInt(4096);
         if (this.map.isInView(randomX, randomY, 64)
            && (
               Utilities.randomInt(20) == 0
                  || this.collision.checkParticleCollision(randomX, randomY, CollisionModule.CollisionHeight.LOW) != this.collision.getCollisionNone()
            )) {
            this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.WATER_POOL_FAST, ParticleModule.ParticleSet.WATER);
         }
      }

      this.sound.playRain();
      int puddleRate = 1;

      for (OrderedPair o : this.waterPuddles) {
         if (Utilities.randomInt(puddleRate) == 0) {
            puddleRate++;
            this.particle
               .newParticle(
                  o.getX() * 16 + Utilities.randomInt(3) + 2,
                  o.getY() * 16 + Utilities.randomInt(3) + 2,
                  ParticleModule.ParticleType.WATER_POOL,
                  ParticleModule.ParticleSet.WATER
               );
         }
      }

      if (Utilities.randomInt(500) == 0) {
         this.makePuddles();
      }

      if (this.durationTick >= this.duration) {
         this.sound.stopRain();
         this.weather.removeDisaster(this);
      }
   }

   private void makePuddles() {
      this.waterPuddles.clear();
      int randomPuddles = Utilities.randomInt(5) + 5;

      for (int x = 0; x < randomPuddles; x++) {
         int randomX = Utilities.randomInt(254) + 1;

         int randomY;
         for (randomY = Utilities.randomInt(254) + 1;
            !this.object.isObject(randomX, randomY) && Utilities.randomInt(100) < 99;
            randomY = Utilities.randomInt(254) + 1
         ) {
            randomX = Utilities.randomInt(254) + 1;
         }

         if (this.map.getMap()[randomX][randomY][0] != 0) {
            this.waterPuddles.add(OrderedPair.getOrderedPair(randomX, randomY));
         }
      }
   }

   @Override
   protected void loadDisaster(YMLDataMap dataMap) {
      super.loadDisaster(dataMap);
      this.weatherIntensity = Integer.parseInt(dataMap.getOrDefault("weatherIntensity", "100"));
      this.lightningChance = Integer.parseInt(dataMap.getOrDefault("lightningChance", "100"));
      this.isLightning = Boolean.parseBoolean(dataMap.getOrDefault("isLightning", "false"));
      if (dataMap.containsKey("waterPuddles")) {
         this.waterPuddles = dataMap.unpackCoordinatesList("waterPuddles");
      }
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = super.getRegionalSaveData();
      if (this.weatherIntensity != 0) {
         dataMap.put("weatherIntensity", this.weatherIntensity);
      }

      if (this.weatherIntensity != 0) {
         dataMap.put("lightningChance", this.lightningChance);
      }

      if (this.isLightning) {
         dataMap.put("isLightning", this.isLightning);
      }

      if (this.waterPuddles.size() > 0) {
         dataMap.put("waterPuddles", dataMap.packCoordinatesList(this.waterPuddles));
      }

      return dataMap;
   }
}
