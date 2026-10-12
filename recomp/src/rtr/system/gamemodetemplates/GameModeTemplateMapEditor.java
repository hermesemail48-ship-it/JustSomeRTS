package rtr.system.gamemodetemplates;

import rtr.TimeModule;

public class GameModeTemplateMapEditor extends GameModeTemplateBase {
   public GameModeTemplateMapEditor() {
      this.baseSpawnRate = 0;
      this.boostedMonsterChanceMultiplier = 1.0;
      this.corruptionStartDay = 0;
      this.corruptionDesiredAmountStart = 0;
      this.corruptionDesiredAmountPerDay = 0;
      this.corruptionDesiredAmountPerBuilding = 0;
      this.corruptionBuildingRate = 0;
      this.corruptionBuildingDensity = 0;
      this.corruptionBuildingDensityRange = 0;
      this.maximumLootBoxes = 0;
      this.lootBoxUncoverChance = 0;
      this.lootBoxKeyChance = 0;
      this.baseNomadSpawnRate = 0;
      this.nomadSpawnAmountMultiplier = 0.0;
      this.nomadSpawnAmountMinimum = 0;
      this.nomadSpawnAmountMaximum = 0;
      this.weatherChanceSpring = 0;
      this.weatherChanceSummer = 0;
      this.weatherChanceAutumn = 0;
      this.weatherChanceWinter = 0;
      this.meteorShowerChance = 0;
      this.earthquakeChance = 0;
      this.cometChance = 0;
      this.electricalStormChance = 0;
      this.hailStormChance = 0;
      this.blightChance = 0;
      this.averageTemperatureSpring = 60;
      this.averageTemperatureSummer = 60;
      this.averageTemperatureAutumn = 60;
      this.averageTemperatureWinter = 60;
      this.temperatureVariance = 0;
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
      this.animalSpawnRate = 0;
   }
}
