package rtr.system.gamemodetemplates;

import rtr.TimeModule;

public class GameModeTemplateNightmare extends GameModeTemplateBase {
   public GameModeTemplateNightmare() {
      this.baseSpawnRate = 60;
      this.boostedMonsterChanceMultiplier = 0.25;
      this.corruptionStartDay = 1;
      this.corruptionDesiredAmountStart = 2048;
      this.corruptionDesiredAmountPerDay = 1536;
      this.corruptionDesiredAmountPerBuilding = 128;
      this.corruptionBuildingRate = 5000;
      this.corruptionBuildingDensity = 14;
      this.corruptionBuildingDensityRange = 24;
      this.maximumLootBoxes = 2;
      this.lootBoxUncoverChance = 200;
      this.lootBoxKeyChance = 3;
      this.baseNomadSpawnRate = 100000;
      this.nomadSpawnAmountMultiplier = 1.0;
      this.nomadSpawnAmountMinimum = 2;
      this.nomadSpawnAmountMaximum = 10;
      this.weatherChanceSpring = 60000;
      this.weatherChanceSummer = 150000;
      this.weatherChanceAutumn = 90000;
      this.weatherChanceWinter = 80000;
      this.meteorShowerChance = 500000;
      this.earthquakeChance = 500000;
      this.cometChance = 1000000;
      this.electricalStormChance = 500000;
      this.hailStormChance = 300000;
      this.blightChance = 300000;
      this.averageTemperatureSpring = 60;
      this.averageTemperatureSummer = 100;
      this.averageTemperatureAutumn = 45;
      this.averageTemperatureWinter = 5;
      this.temperatureVariance = 20;
      this.seasonStart = TimeModule.Season.SPRING;
      this.seasonLengthSpring = 7;
      this.seasonLengthSummer = 10;
      this.seasonLengthAutumn = 7;
      this.seasonLengthWinter = 10;
      this.fullMoonInterval = 12;
      this.eclipseChance = 20;
      this.eclipseDelay = 6;
      this.bloodMoonChance = 2;
      this.bloodMoonDelay = 5;
      this.allowBloodMoonSpring = true;
      this.allowBloodMoonSummer = true;
      this.allowBloodMoonAutumn = true;
      this.allowBloodMoonWinter = true;
      this.animalSpawnRate = 20000;
   }
}
