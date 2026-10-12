package rtr.weather.disasters;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.TimeModule;
import rtr.map.CollisionModule;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.save.YMLDataMap;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class DisasterBloodRain extends DisasterBase {
   private int weatherIntensity;
   private int lightningChance;
   private boolean isLightning;
   private ArrayList<OrderedPair> waterPuddles = new ArrayList<>();

   public DisasterBloodRain(boolean gT) {
      this.disasterType = DisasterBase.DisasterType.BLOOD_RAIN;
      this.godTriggered = gT;
      this.enviromentColor = new Color(0.75F, -1.0F, -1.0F);
      this.enviromentLightAdjust = -70;
      this.enviromentTempAdjust = -2;
      this.enviromentWindAdjust = 0.2;
      this.velocityMultiplier = -0.2F;
      this.sound.playRain();
      this.weatherIntensity = Utilities.randomInt(200) + 101;
      this.lightningChance = Utilities.randomInt(50) + 250;
      if (Game.getCS().getGameMode() == GameModeTemplateBase.GameMode.MAP_EDITOR) {
         this.isLightning = false;
      } else {
         this.isLightning = true;
      }

      this.makePuddles();
   }

   public DisasterBloodRain(YMLDataMap dataMap) {
      this.loadDisaster(dataMap);
   }

   @Override
   public void update() throws SlickException {
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
               this.weather.newLightning(lightningX, lightningY, length, this.godTriggered, ParticleModule.ParticleSet.FIRE_RED);
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
            this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.RAIN, ParticleModule.ParticleSet.BLOOD_RED);
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
            this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.WATER_POOL_FAST, ParticleModule.ParticleSet.BLOOD_RED);
         }
      }

      for (int xxx = 0; xxx < 5; xxx++) {
         int randomX = Utilities.randomInt(4096);
         int randomY = Utilities.randomInt(4096);
         if (this.map.isInView(randomX, randomY, 64)) {
            this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.SMOKE_SPECK, ParticleModule.ParticleSet.FIRE_RED);
            this.projectile.newGroundStatic(randomX, randomY, ParticleModule.ParticleSet.FIRE_RED);
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
                  ParticleModule.ParticleSet.BLOOD_RED
               );
         }
      }

      if (Utilities.randomInt(500) == 0) {
         this.makePuddles();
      }

      if (Utilities.randomInt(150) == 0) {
         int randomX = Utilities.randomInt(256);
         int randomY = Utilities.randomInt(256);
         if (this.object.isInRange(randomX, randomY)) {
            this.weather.newMeteor(randomX * 16, randomY * 16, this.godTriggered);
         }
      }

      if (Utilities.randomInt(60) == 0) {
         int randomX = Utilities.randomInt(256);
         int randomY = Utilities.randomInt(256);
         if (this.object.isInRange(randomX, randomY)) {
            if (Utilities.randomInt(2) == 0) {
               this.mob.newMob(randomX, randomY, MobBase.MobType.BLOOD_SLIME, true);
            } else {
               this.mob.newMob(randomX, randomY, MobBase.MobType.SMALL_BLOOD_SLIME, true);
            }
         }
      }

      for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false)) {
         if (Utilities.randomInt(1000) == 0 && m.isAvailable()) {
            this.mob.tryPanicInArea(m.getTileX(), m.getTileY(), 2, this.godTriggered, 1);
         }
      }

      if (this.time.getTimeOfDay() != TimeModule.TimeOfDay.NIGHT) {
         this.sound.stopRain();
         this.weather.removeDisaster(this);
      }
   }

   private void makePuddles() {
      this.waterPuddles.clear();
      int randomPuddles = Utilities.randomInt(25) + 5;

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
