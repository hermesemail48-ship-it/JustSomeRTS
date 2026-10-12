package rtr.system.gamemodetemplates;

import rtr.TimeModule;

public class GameModeTemplateTraditional extends GameModeTemplateBase {
   public GameModeTemplateTraditional() {
      this.baseSpawnRate = 10;
      this.boostedMonsterChanceMultiplier = 1.5;
      this.corruptionStartDay = 2;
      this.corruptionDesiredAmountStart = 1024;
      this.corruptionDesiredAmountPerDay = 768;
      this.corruptionDesiredAmountPerBuilding = 64;
      this.corruptionBuildingRate = 15000;
      this.corruptionBuildingDensity = 8;
      this.corruptionBuildingDensityRange = 24;
      this.maximumLootBoxes = 5;
      this.lootBoxUncoverChance = 200;
      this.lootBoxKeyChance = 3;
      this.baseNomadSpawnRate = 80000;
      this.nomadSpawnAmountMultiplier = 1.0;
      this.nomadSpawnAmountMinimum = 2;
      this.nomadSpawnAmountMaximum = 10;
      this.weatherChanceSpring = 60000;
      this.weatherChanceSummer = 150000;
      this.weatherChanceAutumn = 90000;
      this.weatherChanceWinter = 80000;
      this.meteorShowerChance = 1000000;
      this.earthquakeChance = 1000000;
      this.cometChance = 2000000;
      this.electricalStormChance = 1000000;
      this.hailStormChance = 600000;
      this.blightChance = 600000;
      this.averageTemperatureSpring = 60;
      this.averageTemperatureSummer = 100;
      this.averageTemperatureAutumn = 45;
      this.averageTemperatureWinter = 5;
      this.temperatureVariance = 20;
      this.seasonStart = TimeModule.Season.SPRING;
      this.seasonLengthSpring = 8;
      this.seasonLengthSummer = 6;
      this.seasonLengthAutumn = 8;
      this.seasonLengthWinter = 6;
      this.fullMoonInterval = 7;
      this.eclipseChance = 20;
      this.eclipseDelay = 6;
      this.bloodMoonChance = 4;
      this.bloodMoonDelay = 6;
      this.allowBloodMoonSpring = true;
      this.allowBloodMoonSummer = false;
      this.allowBloodMoonAutumn = true;
      this.allowBloodMoonWinter = false;
      this.animalSpawnRate = 20000;
   }
}
