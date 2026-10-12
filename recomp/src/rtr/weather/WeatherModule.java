package rtr.weather;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.goal.GoalModule;
import rtr.map.MapTilesLoader;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.FastMath;
import rtr.utilities.Utilities;
import rtr.weather.disasters.DisasterBase;
import rtr.weather.disasters.DisasterBlight;
import rtr.weather.disasters.DisasterBloodRain;
import rtr.weather.disasters.DisasterComet;
import rtr.weather.disasters.DisasterEarthquake;
import rtr.weather.disasters.DisasterEclipse;
import rtr.weather.disasters.DisasterElectricalStorm;
import rtr.weather.disasters.DisasterHailStorm;
import rtr.weather.disasters.DisasterMeteorShower;
import rtr.weather.disasters.DisasterMusic;
import rtr.weather.disasters.DisasterRain;
import rtr.weather.disasters.DisasterSnow;

public class WeatherModule extends ModuleBase {
   private static final int MAXIMUM_DISASTERS_PER_DAY = 2;
   private static final int COLD_POINT = 20;
   private static final int FREEZE_POINT = 10;
   private static final int HOT_POINT = 80;
   private static final int FRY_POINT = 90;
   public static final double LOW_WIND = 0.8;
   public static final double HIGH_WIND = 1.0;
   private int weatherChance;
   private int severeStormChance;
   private int meteorShowerChance;
   private int earthquakeChance;
   private int cometChance;
   private int electricalStormChance;
   private int hailStormChance;
   private int blightChance;
   private int averageTemperatureSpring;
   private int averageTemperatureSummer;
   private int averageTemperatureAutumn;
   private int averageTemperatureWinter;
   private int temperatureVariance;
   private ArrayList<Lightning> lightningList = new ArrayList<>();
   private ArrayList<Lightning> lightningListRemove = new ArrayList<>();
   private ArrayList<Meteor> meteorList = new ArrayList<>();
   private ArrayList<Meteor> meteorListRemove = new ArrayList<>();
   private ArrayList<Comet> cometList = new ArrayList<>();
   private ArrayList<Comet> cometListRemove = new ArrayList<>();
   private double currentTemp = 50.0;
   private double newTemp = 50.0;
   private double newTempDisasterVarianceAdjust = 0.0;
   private static final int DISASTER_TEMP_ADJUST = 1000;
   private int newTempDisasterVarianceAdjustTick = 0;
   private static final int TEMP_ADJUST = 200;
   private int tempAdjustTick = 200;
   private static final int NEW_TEMP_ADJUST = 2000;
   private int newTempAdjustTick = 2000;
   private double currentWind;
   private double newWind;
   private static final int WIND_ADJUST = 150;
   private int windAdjustTick = 150;
   private static final int NEW_WIND_ADJUST = 25000;
   private int newWindAdjustTick = 25000;
   private float windAngle;
   private float windVelocity;
   private WeatherModule.WaterFreezeState waterFreezeState = WeatherModule.WaterFreezeState.LIQUID;
   private static final int WATER_FREEZE_TIME = 10000;
   private int waterFreezeTimeTick = 0;
   private static final int DISASTER_DELAY_DAYS = 3;
   private int disasterCountPerDay = 0;
   private int disasterCountPerDayCurrentDay = 0;
   private ArrayList<DisasterBase> disastersList = new ArrayList<>();
   private ArrayList<DisasterBase> disastersListRemove = new ArrayList<>();

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.weatherChance = 0;
      this.severeStormChance = 0;
      this.meteorShowerChance = 0;
      this.earthquakeChance = 0;
      this.cometChance = 0;
      this.electricalStormChance = 0;
      this.hailStormChance = 0;
      this.blightChance = 0;
      this.lightningList.clear();
      this.lightningListRemove.clear();
      this.meteorList.clear();
      this.meteorListRemove.clear();
      this.cometList.clear();
      this.cometListRemove.clear();
      this.currentTemp = 0.0;
      this.newTemp = 0.0;
      this.newTempDisasterVarianceAdjust = 0.0;
      this.newTempDisasterVarianceAdjustTick = 0;
      this.tempAdjustTick = 200;
      this.newTempAdjustTick = 2000;
      this.currentWind = 0.0;
      this.newWind = 0.0;
      this.windAdjustTick = 150;
      this.newWindAdjustTick = 25000;
      this.windAngle = 0.0F;
      this.windVelocity = 0.0F;
      this.waterFreezeState = WeatherModule.WaterFreezeState.LIQUID;
      this.waterFreezeTimeTick = 0;
      this.disasterCountPerDay = 0;
      this.disasterCountPerDayCurrentDay = 0;
      this.disastersList.clear();
      this.disastersListRemove.clear();
   }

   @Override
   protected void newMainMenu() throws SlickException {
      this.newPlay();
   }

   @Override
   protected void newPlay() throws SlickException {
      this.currentTemp = 50.0;
      this.newTemp = 50.0;
      this.waterFreezeState = WeatherModule.WaterFreezeState.LIQUID;
      GameModeTemplateBase template = this.gameMode.getTemplate();
      this.weatherChance = template.getWeatherChanceSpring();
      this.meteorShowerChance = template.getMeteorShowerChance();
      this.earthquakeChance = template.getEarthquakeChance();
      this.cometChance = template.getCometChance();
      this.electricalStormChance = template.getElectricalStormChance();
      this.hailStormChance = template.getHailStormChance();
      this.blightChance = template.getBlightChance();
      this.averageTemperatureSpring = template.getAverageTemperatureSpring();
      this.averageTemperatureSummer = template.getAverageTemperatureSummer();
      this.averageTemperatureAutumn = template.getAverageTemperatureAutumn();
      this.averageTemperatureWinter = template.getAverageTemperatureWinter();
      this.temperatureVariance = template.getTemperatureVariance();
   }

   @Override
   protected void loadPlay() throws SlickException {
      GameModeTemplateBase template = this.gameMode.getTemplate();
      this.weatherChance = template.getWeatherChanceSpring();
      this.meteorShowerChance = template.getMeteorShowerChance();
      this.earthquakeChance = template.getEarthquakeChance();
      this.cometChance = template.getCometChance();
      this.electricalStormChance = template.getElectricalStormChance();
      this.hailStormChance = template.getHailStormChance();
      this.blightChance = template.getBlightChance();
      this.averageTemperatureSpring = template.getAverageTemperatureSpring();
      this.averageTemperatureSummer = template.getAverageTemperatureSummer();
      this.averageTemperatureAutumn = template.getAverageTemperatureAutumn();
      this.averageTemperatureWinter = template.getAverageTemperatureWinter();
      this.temperatureVariance = template.getTemperatureVariance();
      this.loadRegionalSaveData();
   }

   @Override
   protected void newMapEditor() throws SlickException {
      this.newPlay();
   }

   @Override
   protected void loadMapEditor() throws SlickException {
      this.newPlay();
   }

   @Override
   protected void newWorldMap() throws SlickException {
   }

   @Override
   protected void loadWorldMap() throws SlickException {
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(this.moduleType);
      this.severeStormChance = Integer.parseInt(dataMap.getOrDefault("severeStormChance", 30));
      this.currentTemp = Double.parseDouble(dataMap.getOrDefault("currentTemp", 50));
      this.newTemp = Double.parseDouble(dataMap.getOrDefault("newTemp", 50));
      this.newTempDisasterVarianceAdjust = Double.parseDouble(dataMap.getOrDefault("newTempDisasterVarianceAdjust", 0));
      this.newTempDisasterVarianceAdjustTick = Integer.parseInt(dataMap.getOrDefault("newTempDisasterVarianceAdjustTick", 0));
      this.tempAdjustTick = Integer.parseInt(dataMap.getOrDefault("tempAdjustTick", 200));
      this.newTempAdjustTick = Integer.parseInt(dataMap.getOrDefault("newTempAdjustTick", 2000));
      this.currentWind = Double.parseDouble(dataMap.getOrDefault("currentWind", 0));
      this.newWind = Double.parseDouble(dataMap.getOrDefault("newWind", 0));
      this.windAdjustTick = Integer.parseInt(dataMap.getOrDefault("windAdjustTick", 150));
      this.newWindAdjustTick = Integer.parseInt(dataMap.getOrDefault("newWindAdjustTick", 25000));
      this.windAngle = Float.parseFloat(dataMap.getOrDefault("windAngle", 0));
      this.windVelocity = Float.parseFloat(dataMap.getOrDefault("windVelocity", 0));
      String waterFreezeStateString = dataMap.getOrDefault("waterFreezeState", WeatherModule.WaterFreezeState.LIQUID.toString());

      WeatherModule.WaterFreezeState[] typeString;
      for (WeatherModule.WaterFreezeState w : typeString = WeatherModule.WaterFreezeState.values()) {
         if (w.toString().equals(waterFreezeStateString)) {
            this.waterFreezeState = w;
         }
      }

      this.waterFreezeTimeTick = Integer.parseInt(dataMap.getOrDefault("waterFreezeTimeTick", 0));
      this.disasterCountPerDay = Integer.parseInt(dataMap.getOrDefault("disasterCountPerDay", 0));
      this.disasterCountPerDayCurrentDay = Integer.parseInt(dataMap.getOrDefault("disasterCountPerDayCurrentDay", 0));

      for (int count = 0; dataMap.containsKey("disaster" + count); count++) {
         YMLDataMap thisMap = dataMap.getSubMap("disaster" + count);
         DisasterBase.DisasterType type = null;
         String typeStringx = thisMap.getOrDefault("disasterType", "null");

         DisasterBase.DisasterType[] var10;
         for (DisasterBase.DisasterType t : DisasterBase.DisasterType.values()) {
            if (t.toString().equals(typeStringx)) {
               type = t;
               break;
            }
         }

         if (type != null) {
            this.loadDisaster(type, thisMap);
         }
      }
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      if (this.severeStormChance != 0) {
         dataMap.put("severeStormChance", this.severeStormChance);
      }

      if (this.currentTemp != 0.0) {
         dataMap.put("currentTemp", this.currentTemp);
      }

      if (this.newTemp != 0.0) {
         dataMap.put("newTemp", this.newTemp);
      }

      if (this.newTempDisasterVarianceAdjust != 0.0) {
         dataMap.put("newTempDisasterVarianceAdjust", this.newTempDisasterVarianceAdjust);
      }

      if (this.newTempDisasterVarianceAdjustTick != 0) {
         dataMap.put("newTempDisasterVarianceAdjustTick", this.newTempDisasterVarianceAdjustTick);
      }

      if (this.tempAdjustTick != 0) {
         dataMap.put("tempAdjustTick", this.tempAdjustTick);
      }

      if (this.newTempAdjustTick != 0) {
         dataMap.put("newTempAdjustTick", this.newTempAdjustTick);
      }

      if (this.currentWind != 0.0) {
         dataMap.put("currentWind", this.currentWind);
      }

      if (this.newWind != 0.0) {
         dataMap.put("newWind", this.newWind);
      }

      if (this.windAdjustTick != 0) {
         dataMap.put("windAdjustTick", this.windAdjustTick);
      }

      if (this.newWindAdjustTick != 0) {
         dataMap.put("newWindAdjustTick", this.newWindAdjustTick);
      }

      if (this.windAngle != 0.0) {
         dataMap.put("windAngle", this.windAngle);
      }

      if (this.windVelocity != 0.0) {
         dataMap.put("windVelocity", this.windVelocity);
      }

      if (this.waterFreezeState != WeatherModule.WaterFreezeState.LIQUID) {
         dataMap.put("waterFreezeState", this.waterFreezeState.toString());
      }

      if (this.waterFreezeTimeTick != 0) {
         dataMap.put("waterFreezeTimeTick", this.waterFreezeTimeTick);
      }

      if (this.disasterCountPerDay != 0) {
         dataMap.put("disasterCountPerDay", this.disasterCountPerDay);
      }

      if (this.disasterCountPerDayCurrentDay != 0) {
         dataMap.put("disasterCountPerDayCurrentDay", this.disasterCountPerDayCurrentDay);
      }

      int count = 0;

      for (DisasterBase d : this.disastersList) {
         dataMap.put("disaster" + count, d.getRegionalSaveData());
         count++;
      }

      return dataMap;
   }

   public void update() throws SlickException {
      if (this.save.getActiveRegionalSave().getGameMode() != GameModeTemplateBase.GameMode.MAIN_MENU) {
         this.updateDisasterChance();
         this.adjustSeason();
         this.updateWind();
         this.updateTemp();
      }

      this.disastersList.removeAll(this.disastersListRemove);
      this.disastersListRemove.clear();
      this.lightningList.removeAll(this.lightningListRemove);
      this.lightningListRemove.clear();
      this.meteorList.removeAll(this.meteorListRemove);
      this.meteorListRemove.clear();
      this.cometList.removeAll(this.cometListRemove);
      this.cometListRemove.clear();

      for (Lightning l : this.lightningList) {
         l.update();
      }

      for (Meteor m : this.meteorList) {
         m.update();
      }

      for (Comet c : this.cometList) {
         c.update();
      }

      for (DisasterBase d : this.disastersList) {
         d.update();
      }
   }

   private void updateWind() {
      this.windAdjustTick++;
      if (this.windAdjustTick >= 150) {
         if (this.currentWind > this.newWind) {
            this.currentWind -= 0.02;
         } else if (this.currentWind < this.newWind) {
            this.currentWind += 0.02;
         }

         this.currentWind *= 100.0;
         this.currentWind = Math.round(this.currentWind);
         this.currentWind /= 100.0;
         this.windAdjustTick = 0;
         this.windAngle = (float)Math.toRadians(260.0);
         this.windVelocity = (float)(FastMath.cos(this.windAngle) * this.currentWind);
      }

      this.newWindAdjustTick++;
      if (this.newWindAdjustTick >= 25000) {
         double randomAdjust = 0.0;

         for (DisasterBase d : this.disastersList) {
            if (this.newWind < 0.0) {
               randomAdjust -= d.getEnviromentWindAdjust();
            } else {
               randomAdjust += d.getEnviromentWindAdjust();
            }
         }

         if (randomAdjust == 0.0) {
            if (this.newWind > 0.4) {
               randomAdjust -= 0.2;
            } else if (this.newWind < -0.4) {
               randomAdjust += 0.2;
            }
         }

         randomAdjust += (Utilities.randomInt(-5, 5) + Utilities.randomInt(-5, 5)) / 10.0;
         this.newWind += randomAdjust;
         if (this.newWind > 1.0) {
            this.newWind = 1.0;
         } else if (this.newWind < -1.0) {
            this.newWind = -1.0;
         }

         this.newWind *= 100.0;
         this.newWind = Math.round(this.newWind);
         this.newWind /= 100.0;
         this.newWindAdjustTick = Utilities.randomInt(1000);
      }

      if (this.isLowWind()) {
         int amount = (int)(Math.abs(this.currentWind) * 10.0);
         if (ScaleControl.getWorldScale() == 1.0F) {
            amount = 0;
         }

         if (ScaleControl.getWorldScale() == 3.0F) {
            amount *= 2;
         }

         if (ScaleControl.getWorldScale() >= 4.0F) {
            amount *= 3;
         }

         if (this.isHighWind()) {
            amount *= 2;
         }

         for (int x = 0; x < amount; x++) {
            int randomX = Utilities.randomInt(4096);
            int randomY = Utilities.randomInt(4096);
            if (this.map.isInView(randomX, randomY, 64)) {
               if (this.currentWind >= 0.0) {
                  for (int x2 = 0; x2 < 3; x2++) {
                     this.particle.newParticle(randomX, randomY, 180.0F, ParticleModule.ParticleType.WIND_DUST, ParticleModule.ParticleSet.LIGHT_GRAY);
                  }

                  if (Utilities.randomInt(10) == 0) {
                     for (int x2 = 0; x2 < 10; x2++) {
                        this.particle.newParticle(randomX, randomY, 180.0F, ParticleModule.ParticleType.WIND_DUST, ParticleModule.ParticleSet.LIGHT_GRAY);
                     }
                  }
               } else if (this.currentWind <= 0.0) {
                  for (int x2 = 0; x2 < 3; x2++) {
                     this.particle.newParticle(randomX, randomY, 0.0F, ParticleModule.ParticleType.WIND_DUST, ParticleModule.ParticleSet.LIGHT_GRAY);
                  }

                  if (Utilities.randomInt(10) == 0) {
                     for (int x2 = 0; x2 < 10; x2++) {
                        this.particle.newParticle(randomX, randomY, 0.0F, ParticleModule.ParticleType.WIND_DUST, ParticleModule.ParticleSet.LIGHT_GRAY);
                     }
                  }
               }
            }
         }
      }
   }

   private void updateTemp() throws SlickException {
      this.tempAdjustTick++;
      if (this.tempAdjustTick >= 200) {
         if (this.currentTemp > this.newTemp) {
            this.currentTemp--;
         } else if (this.currentTemp < this.newTemp) {
            this.currentTemp++;
         }

         this.tempAdjustTick = 0;
      }

      if (this.newTempDisasterVarianceAdjust != 0.0) {
         this.newTempDisasterVarianceAdjustTick++;
         if (this.newTempDisasterVarianceAdjustTick >= 1000) {
            if (this.newTempDisasterVarianceAdjust > 0.0) {
               if (this.newTempDisasterVarianceAdjust > 10.0) {
                  this.newTempDisasterVarianceAdjust = this.newTempDisasterVarianceAdjust - (int)(this.newTempDisasterVarianceAdjust * 0.1);
               } else {
                  this.newTempDisasterVarianceAdjust--;
               }
            } else if (this.newTempDisasterVarianceAdjust < 0.0) {
               if (this.newTempDisasterVarianceAdjust < -10.0) {
                  this.newTempDisasterVarianceAdjust = this.newTempDisasterVarianceAdjust + (int)(Math.abs(this.newTempDisasterVarianceAdjust) * 0.1);
               } else {
                  this.newTempDisasterVarianceAdjust++;
               }
            }

            if (this.newTempDisasterVarianceAdjust < 1.0 && this.newTempDisasterVarianceAdjust > -1.0) {
               this.newTempDisasterVarianceAdjust = 0.0;
            }

            this.newTempDisasterVarianceAdjustTick = 0;
         }
      }

      if (this.currentTemp >= 80.0) {
         for (int i = 0; i < 10; i++) {
            int randomX = (int)(Utilities.randomInt(4096) - this.map.getMapX());
            int randomY = (int)(Utilities.randomInt(4096) - this.map.getMapY());
            int startX = this.map.getMapRenderTopLeftX() - 2;
            int startY = this.map.getMapRenderTopLeftY() - 2;
            int endX = this.map.getMapRenderBottomRightX() + 2;
            int endY = this.map.getMapRenderBottomRightY() + 2;
            if (randomX >= startX && randomX <= endX && randomY >= startY && randomY <= endY) {
               if (Utilities.randomInt(20) == 0) {
                  this.particle
                     .newParticleGroup(
                        randomX,
                        randomY,
                        ParticleModule.ParticleGroup.SMOKE_BELLOW,
                        ParticleModule.ParticleSet.FIRE_RED,
                        ParticleModule.ParticleSet.FIRE_YELLOW
                     );
                  this.particle
                     .newParticleGroup(
                        randomX,
                        randomY,
                        ParticleModule.ParticleGroup.SMOKE_BELLOW,
                        ParticleModule.ParticleSet.FIRE_YELLOW,
                        ParticleModule.ParticleSet.FIRE_RED
                     );
               }

               this.particle
                  .newParticleGroup(
                     randomX, randomY, ParticleModule.ParticleGroup.FLOATING_DUST, ParticleModule.ParticleSet.FIRE_RED, ParticleModule.ParticleSet.FIRE_YELLOW
                  );
               this.particle
                  .newParticleGroup(
                     randomX, randomY, ParticleModule.ParticleGroup.FLOATING_DUST, ParticleModule.ParticleSet.FIRE_YELLOW, ParticleModule.ParticleSet.FIRE_RED
                  );
            }
         }
      } else if (this.currentTemp <= 20.0) {
         for (int ix = 0; ix < 10; ix++) {
            int randomX = (int)(Utilities.randomInt(4096) - this.map.getMapX());
            int randomY = (int)(Utilities.randomInt(4096) - this.map.getMapY());
            int startX = this.map.getMapRenderTopLeftX() - 2;
            int startY = this.map.getMapRenderTopLeftY() - 2;
            int endX = this.map.getMapRenderBottomRightX() + 2;
            int endY = this.map.getMapRenderBottomRightY() + 2;
            if (randomX >= startX && randomX <= endX && randomY >= startY && randomY <= endY) {
               if (Utilities.randomInt(20) == 0) {
                  this.particle
                     .newParticleGroup(
                        randomX,
                        randomY,
                        ParticleModule.ParticleGroup.SMOKE_BELLOW,
                        ParticleModule.ParticleSet.FIRE_BLUE,
                        ParticleModule.ParticleSet.MAGIC_BLUE
                     );
                  this.particle
                     .newParticleGroup(
                        randomX,
                        randomY,
                        ParticleModule.ParticleGroup.SMOKE_BELLOW,
                        ParticleModule.ParticleSet.MAGIC_BLUE,
                        ParticleModule.ParticleSet.FIRE_BLUE
                     );
               }

               this.particle
                  .newParticleGroup(
                     randomX, randomY, ParticleModule.ParticleGroup.FLOATING_DUST, ParticleModule.ParticleSet.FIRE_BLUE, ParticleModule.ParticleSet.MAGIC_BLUE
                  );
               this.particle
                  .newParticleGroup(
                     randomX, randomY, ParticleModule.ParticleGroup.FLOATING_DUST, ParticleModule.ParticleSet.MAGIC_BLUE, ParticleModule.ParticleSet.FIRE_BLUE
                  );
            }
         }
      }

      if (this.currentTemp <= 10.0) {
         this.waterFreezeTimeTick++;
         if (this.waterFreezeTimeTick >= 10000) {
            this.waterFreezeTimeTick = 0;
            if (this.waterFreezeState == WeatherModule.WaterFreezeState.LIQUID) {
               this.waterFreezeState = WeatherModule.WaterFreezeState.PARTIALLY_FROZEN;
               this.map.fullTileSetSwap(MapTilesLoader.TileSet.WATER, MapTilesLoader.TileSet.WATER_PARTIALLY_FROZEN);
               this.map.fullTileSetSwap(MapTilesLoader.TileSet.WATER_PARTIALLY_DRY, MapTilesLoader.TileSet.WATER_PARTIALLY_DRY_FROZEN);
            } else if (this.waterFreezeState == WeatherModule.WaterFreezeState.PARTIALLY_FROZEN) {
               this.waterFreezeState = WeatherModule.WaterFreezeState.FROZEN;
               this.map.fullTileSetSwap(MapTilesLoader.TileSet.WATER, MapTilesLoader.TileSet.WATER_FROZEN);
               this.map.fullTileSetSwap(MapTilesLoader.TileSet.WATER_PARTIALLY_FROZEN, MapTilesLoader.TileSet.WATER_FROZEN);
               this.map.fullTileSetSwap(MapTilesLoader.TileSet.WATER_PARTIALLY_DRY, MapTilesLoader.TileSet.WATER_PARTIALLY_DRY_FROZEN);
            }
         }
      } else {
         if (this.currentTemp <= 20.0) {
            this.waterFreezeTimeTick--;
         } else {
            this.waterFreezeTimeTick -= 2;
         }

         if (this.waterFreezeTimeTick <= 0) {
            this.waterFreezeTimeTick = 10000;
            if (this.waterFreezeState == WeatherModule.WaterFreezeState.FROZEN) {
               this.waterFreezeState = WeatherModule.WaterFreezeState.PARTIALLY_FROZEN;
               this.map.fullTileSetSwap(MapTilesLoader.TileSet.WATER_FROZEN, MapTilesLoader.TileSet.WATER_PARTIALLY_FROZEN);
            } else if (this.waterFreezeState == WeatherModule.WaterFreezeState.PARTIALLY_FROZEN) {
               this.waterFreezeState = WeatherModule.WaterFreezeState.LIQUID;
               this.map.fullTileSetSwap(MapTilesLoader.TileSet.WATER_PARTIALLY_FROZEN, MapTilesLoader.TileSet.WATER);
               this.map.fullTileSetSwap(MapTilesLoader.TileSet.WATER_PARTIALLY_DRY_FROZEN, MapTilesLoader.TileSet.WATER_PARTIALLY_DRY);
            }
         }
      }

      this.newTempAdjustTick++;
      if (this.newTempAdjustTick >= 2000) {
         int randomAdjust = 0;

         for (DisasterBase d : this.disastersList) {
            randomAdjust += d.getEnviromentTempAdjust();
         }

         randomAdjust += Utilities.randomInt(-5, 5);
         switch (this.time.getTimeOfDay()) {
            case NIGHT:
               randomAdjust -= 2;
               break;
            case DAWN:
               randomAdjust++;
               break;
            case MORNING:
               randomAdjust += 2;
               break;
            case MIDDAY:
               randomAdjust += 2;
               break;
            case EVENING:
               randomAdjust--;
               break;
            case DUSK:
               randomAdjust -= 2;
         }

         if (this.isLowWind()) {
            randomAdjust--;
         } else if (this.isHighWind()) {
            randomAdjust -= 2;
         }

         this.newTemp += randomAdjust;
         if (this.newTemp > 100.0) {
            this.newTemp = 100.0;
         } else if (this.newTemp < 0.0) {
            this.newTemp = 0.0;
         }

         this.newTempAdjustTick = 0;
      }
   }

   private void updateDisasterChance() throws SlickException {
      if (this.time.getDay() != this.disasterCountPerDayCurrentDay) {
         this.disasterCountPerDayCurrentDay = this.time.getDay();
         this.disasterCountPerDay = 0;
      }

      if (this.weatherChance != 0 && Utilities.randomInt(this.weatherChance) == 0) {
         this.startWeather();
      } else if (this.disasterCountPerDay < 2 && this.hailStormChance != 0 && this.currentTemp <= 20.0 && Utilities.randomInt(this.hailStormChance) == 0) {
         this.newDisaster(DisasterBase.DisasterType.HAIL_STORM, false);
         this.goal.incrementGoal(GoalModule.GoalType.SERIOUSLY);
      } else if (this.disasterCountPerDay < 2 && this.blightChance != 0 && this.currentTemp >= 80.0 && Utilities.randomInt(this.blightChance) == 0) {
         this.newDisaster(DisasterBase.DisasterType.BLIGHT, false);
         this.goal.incrementGoal(GoalModule.GoalType.SERIOUSLY);
      }

      if (this.disasterCountPerDay < 2 && this.time.getDay() > 3) {
         if (this.electricalStormChance != 0 && Utilities.randomInt(this.electricalStormChance) == 0) {
            this.newDisaster(DisasterBase.DisasterType.ELECTRICAL_STORM, false);
            this.goal.incrementGoal(GoalModule.GoalType.SERIOUSLY);
            this.disasterCountPerDay++;
         } else if (this.meteorShowerChance != 0 && Utilities.randomInt(this.meteorShowerChance) == 0) {
            this.newDisaster(DisasterBase.DisasterType.METEOR_SHOWER, false);
            this.goal.incrementGoal(GoalModule.GoalType.SERIOUSLY);
            this.disasterCountPerDay++;
         } else if (this.earthquakeChance != 0 && Utilities.randomInt(this.earthquakeChance) == 0) {
            this.newDisaster(DisasterBase.DisasterType.EARTHQUAKE, false);
            this.goal.incrementGoal(GoalModule.GoalType.SERIOUSLY);
            this.disasterCountPerDay++;
         } else if (this.cometChance != 0 && Utilities.randomInt(this.cometChance) == 0) {
            this.newDisaster(DisasterBase.DisasterType.COMET, false);
            this.goal.incrementGoal(GoalModule.GoalType.SERIOUSLY);
            this.disasterCountPerDay++;
         }
      }
   }

   public void startWeather() throws SlickException {
      if (!this.isWeather()) {
         if (this.getCurrentTemp() <= this.getFreezePoint()) {
            this.newDisaster(DisasterBase.DisasterType.SNOW, false);
         } else {
            this.newDisaster(DisasterBase.DisasterType.RAIN, false);
         }
      }
   }

   public void stopWeather() {
      this.removeAllDisasters(DisasterBase.DisasterGroup.WEATHER);
   }

   public void newDisaster(DisasterBase.DisasterType t, int tileX, int tileY, boolean godTriggered) {
      if (!this.isDisasterGroupActive(t.getGroup())) {
         if (t == DisasterBase.DisasterType.METEOR_SHOWER) {
            this.disastersList.add(new DisasterMeteorShower(tileX, tileY, godTriggered));
         } else if (t == DisasterBase.DisasterType.EARTHQUAKE) {
            this.disastersList.add(new DisasterEarthquake(tileX, tileY, godTriggered));
            if (this.time.isBloodMoon()) {
               this.goal.incrementGoal(GoalModule.GoalType.RAGNAROK);
            }
         } else if (t == DisasterBase.DisasterType.ELECTRICAL_STORM) {
            this.disastersList.add(new DisasterElectricalStorm(tileX, tileY, godTriggered));
         } else if (t == DisasterBase.DisasterType.BLIGHT) {
            this.disastersList.add(new DisasterBlight(tileX, tileY, godTriggered));
         } else if (t == DisasterBase.DisasterType.COMET) {
            this.disastersList.add(new DisasterComet(tileX, tileY, godTriggered));
         }
      }
   }

   public void newDisaster(DisasterBase.DisasterType t, boolean godTriggered) throws SlickException {
      if (!this.isDisasterGroupActive(t.getGroup())) {
         if (t == DisasterBase.DisasterType.RAIN) {
            this.disastersList.add(new DisasterRain(godTriggered));
         } else if (t == DisasterBase.DisasterType.SNOW) {
            this.disastersList.add(new DisasterSnow(godTriggered));
         } else if (t == DisasterBase.DisasterType.HAIL_STORM) {
            this.disastersList.add(new DisasterHailStorm(godTriggered));
         } else if (t == DisasterBase.DisasterType.BLOOD_RAIN) {
            this.disastersList.add(new DisasterBloodRain(godTriggered));
         } else if (t == DisasterBase.DisasterType.METEOR_SHOWER) {
            this.disastersList.add(new DisasterMeteorShower(godTriggered));
         } else if (t == DisasterBase.DisasterType.EARTHQUAKE) {
            this.disastersList.add(new DisasterEarthquake(godTriggered));
            if (this.time.isBloodMoon()) {
               this.goal.incrementGoal(GoalModule.GoalType.RAGNAROK);
            }
         } else if (t == DisasterBase.DisasterType.ELECTRICAL_STORM) {
            this.disastersList.add(new DisasterElectricalStorm(godTriggered));
         } else if (t == DisasterBase.DisasterType.BLIGHT) {
            this.disastersList.add(new DisasterBlight(godTriggered));
         } else if (t == DisasterBase.DisasterType.ECLIPSE) {
            this.disastersList.add(new DisasterEclipse(godTriggered));
         } else if (t == DisasterBase.DisasterType.MUSIC) {
            this.disastersList.add(new DisasterMusic(godTriggered));
         } else if (t == DisasterBase.DisasterType.COMET) {
            this.disastersList.add(new DisasterComet(godTriggered));
         }
      }
   }

   public void forceNewDisaster(DisasterBase.DisasterType t, boolean godTriggered) throws SlickException {
      ArrayList<DisasterBase> disastersListForceRemove = new ArrayList<>();

      for (DisasterBase d : this.disastersList) {
         if (d.getDisasterType().getGroup() == t.getGroup()) {
            disastersListForceRemove.add(d);
         }
      }

      this.disastersList.removeAll(disastersListForceRemove);
      this.newDisaster(t, godTriggered);
   }

   public void loadDisaster(DisasterBase.DisasterType t, YMLDataMap dataMap) {
      if (t == DisasterBase.DisasterType.METEOR_SHOWER) {
         this.disastersList.add(new DisasterMeteorShower(dataMap));
      } else if (t == DisasterBase.DisasterType.EARTHQUAKE) {
         this.disastersList.add(new DisasterEarthquake(dataMap));
      } else if (t == DisasterBase.DisasterType.ELECTRICAL_STORM) {
         this.disastersList.add(new DisasterElectricalStorm(dataMap));
      } else if (t == DisasterBase.DisasterType.RAIN) {
         this.disastersList.add(new DisasterRain(dataMap));
      } else if (t == DisasterBase.DisasterType.SNOW) {
         this.disastersList.add(new DisasterSnow(dataMap));
      } else if (t == DisasterBase.DisasterType.HAIL_STORM) {
         this.disastersList.add(new DisasterHailStorm(dataMap));
      } else if (t == DisasterBase.DisasterType.BLOOD_RAIN) {
         this.disastersList.add(new DisasterBloodRain(dataMap));
      } else if (t == DisasterBase.DisasterType.BLIGHT) {
         this.disastersList.add(new DisasterBlight(dataMap));
      } else if (t == DisasterBase.DisasterType.ECLIPSE) {
         this.disastersList.add(new DisasterEclipse(dataMap));
      } else if (t == DisasterBase.DisasterType.MUSIC) {
         this.disastersList.add(new DisasterMusic(dataMap));
      } else if (t == DisasterBase.DisasterType.COMET) {
         this.disastersList.add(new DisasterComet(dataMap));
      }
   }

   private void removeAllDisasters(DisasterBase.DisasterGroup type) {
      for (DisasterBase d : this.disastersList) {
         if (d.getDisasterType().getGroup() == type) {
            this.disastersListRemove.add(d);
         }
      }
   }

   public void removeDisaster(DisasterBase d) {
      this.disastersListRemove.add(d);
   }

   public ArrayList<DisasterBase> getDisasters() {
      return this.disastersList;
   }

   public float getMovementSpeedReductionTotal() {
      float velMultiplier = 1.0F;

      for (DisasterBase d : this.disastersList) {
         velMultiplier += d.getVelocityMultiplier();
      }

      if (this.isHighWind()) {
         velMultiplier -= 0.2F;
      } else if (this.isLowWind()) {
         velMultiplier -= 0.1F;
      }

      if (velMultiplier < 0.1F) {
         velMultiplier = 0.1F;
      }

      return velMultiplier;
   }

   private void adjustSeason() {
      GameModeTemplateBase template = this.gameMode.getTemplate();
      double tempHighMax = this.newTempDisasterVarianceAdjust;
      double tempLowMax = this.newTempDisasterVarianceAdjust;
      tempHighMax += this.temperatureVariance;
      tempLowMax -= this.temperatureVariance;
      switch (this.time.getSeason()) {
         case WINTER:
            tempHighMax += this.averageTemperatureWinter;
            tempLowMax += this.averageTemperatureWinter;
            this.weatherChance = template.getWeatherChanceWinter();
            this.severeStormChance = 30;
            break;
         case SPRING:
            tempHighMax += this.averageTemperatureSpring;
            tempLowMax += this.averageTemperatureSpring;
            this.weatherChance = template.getWeatherChanceSpring();
            this.severeStormChance = 20;
            break;
         case SUMMER:
            tempHighMax += this.averageTemperatureSummer;
            tempLowMax += this.averageTemperatureSummer;
            this.weatherChance = template.getWeatherChanceSummer();
            this.severeStormChance = 40;
            break;
         case AUTUMN:
            tempHighMax += this.averageTemperatureAutumn;
            tempLowMax += this.averageTemperatureAutumn;
            this.weatherChance = template.getWeatherChanceAutumn();
            this.severeStormChance = 30;
      }

      if (this.time.isBloodMoon()) {
         tempHighMax += 50.0;
         tempLowMax += 50.0;
      } else if (this.time.isBloodMoonDay()) {
         tempHighMax += 30.0;
         tempLowMax += 30.0;
      } else if (this.time.isFullMoon()) {
         tempHighMax -= 30.0;
         tempLowMax -= 30.0;
      } else if (this.time.isFullMoonDay()) {
         tempHighMax -= 20.0;
         tempLowMax -= 20.0;
      } else if (this.time.isEclipse()) {
         tempHighMax -= 40.0;
         tempLowMax -= 40.0;
      } else if (this.time.isEclipseDay()) {
         tempHighMax -= 30.0;
         tempLowMax -= 30.0;
      }

      if (tempHighMax > 100.0) {
         tempHighMax = 100.0;
      } else if (tempHighMax < 0.0) {
         tempHighMax = 0.0;
      }

      if (tempLowMax > 100.0) {
         tempLowMax = 100.0;
      } else if (tempLowMax < 0.0) {
         tempLowMax = 0.0;
      }

      if (this.newTemp > tempHighMax) {
         this.newTemp = tempHighMax;
      } else if (this.newTemp < tempLowMax) {
         this.newTemp = tempLowMax;
      }
   }

   public int getSevereStormChance() {
      return this.severeStormChance;
   }

   public boolean isWeather() {
      return this.isDisasterGroupActive(DisasterBase.DisasterGroup.WEATHER);
   }

   public boolean isRaining() {
      return this.isDisasterTypeActive(DisasterBase.DisasterType.RAIN) || this.isDisasterTypeActive(DisasterBase.DisasterType.BLOOD_RAIN);
   }

   public boolean isSnowing() {
      return this.isDisasterTypeActive(DisasterBase.DisasterType.SNOW) || this.isDisasterTypeActive(DisasterBase.DisasterType.HAIL_STORM);
   }

   public boolean isDisasterTypeActive(DisasterBase.DisasterType type) {
      for (DisasterBase d : this.disastersList) {
         if (d.getDisasterType() == type) {
            return true;
         }
      }

      return false;
   }

   public boolean isDisasterGroupActive(DisasterBase.DisasterGroup group) {
      for (DisasterBase d : this.disastersList) {
         if (d.getDisasterType().getGroup() == group) {
            return true;
         }
      }

      return false;
   }

   public int getColdPoint() {
      return 20;
   }

   public int getFreezePoint() {
      return 10;
   }

   public int getHotPoint() {
      return 80;
   }

   public int getFryPoint() {
      return 90;
   }

   public void removeLightning(Lightning l) {
      this.lightningListRemove.add(l);
   }

   public void newLightning(int tileX, int tileY, int length, boolean godTriggered, ParticleModule.ParticleSet particleSet) throws SlickException {
      ArrayList<ObjectBase> objectsList = this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.LIGHTNING_RODS);
      ArrayList<ObjectBase> lightningRods = new ArrayList<>();

      for (ObjectBase o : objectsList) {
         if (!o.isPaused()
            && o.getEnergy() < o.getObjectFlags().getEnergyMax()
            && Utilities.coordinatesWithin(tileX, tileY, o.getLightningRodXOnMap(), o.getLightningRodYOnMap(), o.getObjectFlags().getLightningRodRange())) {
            lightningRods.add(o);
         }
      }

      if (lightningRods.size() <= 0) {
         tileX *= 16;
         tileY *= 16;
         tileX += 8;
         tileY += 8;
         this.lightningList.add(new Lightning(tileX, tileY, length, godTriggered, false, particleSet));
      } else {
         ObjectBase lightningRod = lightningRods.get(Utilities.randomInt(lightningRods.size()));
         this.lightningList
            .add(
               new Lightning(
                  lightningRod.getLightningRodXOnMap() * 16 + 8, lightningRod.getLightningRodYOnMap() * 16 + 8, length, godTriggered, true, particleSet
               )
            );
         this.goal.incrementGoal(GoalModule.GoalType.OBVIOUS_IN_HINDSIGHT);
      }
   }

   public void removeMeteor(Meteor l) {
      this.meteorListRemove.add(l);
   }

   public void newMeteor(int targetX, int targetY, boolean godTriggered) throws SlickException {
      this.meteorList.add(new Meteor(targetX, targetY, godTriggered));
   }

   public void removeComet(Comet l) {
      this.cometListRemove.add(l);
   }

   public void newComet(int targetX, int targetY, boolean godTriggered) throws SlickException {
      this.cometList.add(new Comet(targetX, targetY, godTriggered));
   }

   public double getCurrentTemp() {
      return this.currentTemp;
   }

   public double getNewTemp() {
      return this.newTemp;
   }

   public void addDisasterTemp(double addTemp) {
      this.newTempDisasterVarianceAdjust += addTemp;
      this.adjustSeason();
   }

   public void removeDisasterTemp(double removeTemp) {
      this.newTempDisasterVarianceAdjust -= removeTemp;
      this.adjustSeason();
   }

   public boolean isLowWind() {
      return this.currentWind >= 0.8 || this.currentWind <= -0.8;
   }

   public boolean isHighWind() {
      return this.currentWind >= 1.0 || this.currentWind <= -1.0;
   }

   public float getWindVelocity() {
      return this.windVelocity;
   }

   public double getNewWind() {
      return this.newWind;
   }

   public double getCurrentWind() {
      return this.currentWind;
   }

   public static enum WaterFreezeState {
      LIQUID,
      PARTIALLY_FROZEN,
      FROZEN;
   }
}
