package rtr.system.gamemodetemplates;

import rtr.TimeModule;

public class GameModeTemplateSurvival extends GameModeTemplateBase {
   public GameModeTemplateSurvival() {
      this.baseSpawnRate = 25;
      this.boostedMonsterChanceMultiplier = 1.0;
      this.corruptionStartDay = 1;
      this.corruptionDesiredAmountStart = 1024;
      this.corruptionDesiredAmountPerDay = 1024;
      this.corruptionDesiredAmountPerBuilding = 128;
      this.corruptionBuildingRate = 10000;
      this.corruptionBuildingDensity = 10;
      this.corruptionBuildingDensityRange = 24;
      this.maximumLootBoxes = 3;
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
      this.meteorShowerChance = 800000;
      this.earthquakeChance = 800000;
      this.cometChance = 1600000;
      this.electricalStormChance = 800000;
      this.hailStormChance = 500000;
      this.blightChance = 500000;
      this.averageTemperatureSpring = 60;
      this.averageTemperatureSummer = 100;
      this.averageTemperatureAutumn = 45;
      this.averageTemperatureWinter = 5;
      this.temperatureVariance = 20;
      this.seasonStart = TimeModule.Season.SPRING;
      this.seasonLengthSpring = 7;
      this.seasonLengthSummer = 7;
      this.seasonLengthAutumn = 7;
      this.seasonLengthWinter = 7;
      this.fullMoonInterval = 9;
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
