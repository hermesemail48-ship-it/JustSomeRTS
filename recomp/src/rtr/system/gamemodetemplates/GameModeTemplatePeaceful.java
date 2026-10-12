package rtr.system.gamemodetemplates;

import rtr.TimeModule;

public class GameModeTemplatePeaceful extends GameModeTemplateBase {
   public GameModeTemplatePeaceful() {
      this.baseSpawnRate = 0;
      this.boostedMonsterChanceMultiplier = 0.25;
      this.corruptionStartDay = 0;
      this.corruptionDesiredAmountStart = 0;
      this.corruptionDesiredAmountPerDay = 0;
      this.corruptionDesiredAmountPerBuilding = 0;
      this.corruptionBuildingRate = 20000;
      this.corruptionBuildingDensity = 1;
      this.corruptionBuildingDensityRange = 8;
      this.maximumLootBoxes = 6;
      this.lootBoxUncoverChance = 100;
      this.lootBoxKeyChance = 2;
      this.baseNomadSpawnRate = 100000;
      this.nomadSpawnAmountMultiplier = 1.0;
      this.nomadSpawnAmountMinimum = 2;
      this.nomadSpawnAmountMaximum = 10;
      this.weatherChanceSpring = 60000;
      this.weatherChanceSummer = 150000;
      this.weatherChanceAutumn = 90000;
      this.weatherChanceWinter = 80000;
      this.meteorShowerChance = 0;
      this.earthquakeChance = 1500000;
      this.cometChance = 3000000;
      this.electricalStormChance = 1500000;
      this.hailStormChance = 1000000;
      this.blightChance = 1000000;
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
      this.allowBloodMoonSpring = false;
      this.allowBloodMoonSummer = false;
      this.allowBloodMoonAutumn = false;
      this.allowBloodMoonWinter = false;
      this.animalSpawnRate = 20000;
   }
}
