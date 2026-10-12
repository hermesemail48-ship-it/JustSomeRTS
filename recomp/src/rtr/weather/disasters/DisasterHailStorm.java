package rtr.weather.disasters;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.save.YMLDataMap;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.Utilities;

public class DisasterHailStorm extends DisasterBase {
   private int weatherIntensity;
   private int lightningChance;
   private boolean isLightning;

   public DisasterHailStorm(boolean gT) throws SlickException {
      this.disasterType = DisasterBase.DisasterType.HAIL_STORM;
      this.godTriggered = gT;
      this.velocityMultiplier = -0.1F;
      this.duration = Utilities.randomInt(5000) + 5000;
      Console.newBanner(Text.getText("console.banner.weather.disaster.hailStorm.header"), Text.getText("console.banner.weather.disaster.hailStorm.subtitle"));
      this.sound.playSound(SoundModule.SoundType.DISASTER_START);
      this.enviromentColor = new Color(-0.5F, -0.5F, 0.5F);
      this.enviromentLightAdjust = -50;
      this.enviromentTempAdjust = -2;
      this.enviromentWindAdjust = 0.1;
      this.sound.playRain();
      if (Utilities.randomInt(this.weather.getSevereStormChance()) == 0) {
         this.weatherIntensity = Utilities.randomInt(400) + 401;
         this.lightningChance = Utilities.randomInt(250) + 150;
      } else {
         this.weatherIntensity = Utilities.randomInt(200) + 201;
         this.lightningChance = Utilities.randomInt(2400) + 100;
      }

      this.lightningChance *= 5;
      if (this.lightningChance <= 1000 && Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.MAP_EDITOR) {
         this.isLightning = true;
      } else {
         this.isLightning = false;
      }
   }

   public DisasterHailStorm(YMLDataMap dataMap) {
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
            this.particle.newParticle(randomX, randomY, ParticleModule.ParticleType.HAIL, ParticleModule.ParticleSet.WHITE);
         }
      }

      for (int xx = 0; xx < rainIntensityMod * 20; xx++) {
         int randomX = Utilities.randomInt(4096);
         int randomY = Utilities.randomInt(4096);
         if (this.map.isInView(randomX, randomY, 64) && Utilities.randomInt(15) == 0) {
            this.particle.newParticle(randomX, randomY, Utilities.randomInt(360), ParticleModule.ParticleType.HAIL_LANDED, ParticleModule.ParticleSet.WHITE);
         }
      }

      this.sound.playRain();

      for (ObjectBase o : this.object.getObjectsArrayList()) {
         if (Utilities.randomInt(500) == 0 && !o.canNotBeAttacked()) {
            o.takeDamage(Utilities.randomInt(5) + 1, MobBase.DamageType.ICE, MobBase.DamageCause.HAIL);
            if (this.map.isInView(o.getCenterPixelX(), o.getCenterPixelY(), 64)) {
               this.sound.playSound(SoundModule.SoundType.EARTHQUAKE_ROCKS, o.getCenterPixelX(), o.getCenterPixelY());
               if (Utilities.randomInt(30) == 0) {
                  this.sound.playSound(SoundModule.SoundType.HIT_TOOL_GENERIC, o.getCenterPixelX(), o.getCenterPixelY());
               }

               if (Utilities.randomInt(30) == 0) {
                  this.sound.playSound(SoundModule.SoundType.HIT_WEAPON_GENERIC, o.getCenterPixelX(), o.getCenterPixelY());
               }

               if (Utilities.randomInt(5) == 0) {
                  this.sound.playSound(SoundModule.SoundType.STORE_ITEM, o.getCenterPixelX(), o.getCenterPixelY());
               }
            }
         }
      }

      for (MobBase m : this.mob.getMobArray()) {
         if (Utilities.randomInt(500) == 0 && m.isAvailable()) {
            m.takeDamage(Utilities.randomInt(10) + 1, Utilities.randomInt(4096), Utilities.randomInt(4096), MobBase.DamageType.ICE, MobBase.DamageCause.HAIL);
            if (m.isAvailable()) {
               this.mob.tryPanicInArea(m.getTileX(), m.getTileY(), 2, this.godTriggered, 1);
            }
         }
      }

      if (this.durationTick >= this.duration) {
         this.sound.stopRain();
         this.weather.removeDisaster(this);
      }
   }

   @Override
   protected void loadDisaster(YMLDataMap dataMap) {
      super.loadDisaster(dataMap);
      this.weatherIntensity = Integer.parseInt(dataMap.getOrDefault("weatherIntensity", "100"));
      this.lightningChance = Integer.parseInt(dataMap.getOrDefault("lightningChance", "100"));
      this.isLightning = Boolean.parseBoolean(dataMap.getOrDefault("isLightning", "false"));
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

      return dataMap;
   }
}
