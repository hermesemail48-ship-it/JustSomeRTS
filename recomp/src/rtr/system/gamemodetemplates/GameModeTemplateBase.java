package rtr.system.gamemodetemplates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import rtr.TimeModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.system.Game;

public class GameModeTemplateBase {
   protected int baseSpawnRate;
   protected double boostedMonsterChanceMultiplier;
   protected int corruptionStartDay;
   protected int corruptionDesiredAmountStart;
   protected int corruptionDesiredAmountPerDay;
   protected int corruptionDesiredAmountPerBuilding;
   protected int corruptionBuildingRate;
   protected int corruptionBuildingDensity;
   protected int corruptionBuildingDensityRange;
   protected int maximumLootBoxes;
   protected int lootBoxUncoverChance;
   protected int lootBoxKeyChance;
   protected int baseNomadSpawnRate;
   protected double nomadSpawnAmountMultiplier;
   protected int nomadSpawnAmountMinimum;
   protected int nomadSpawnAmountMaximum;
   protected int weatherChanceSpring;
   protected int weatherChanceSummer;
   protected int weatherChanceAutumn;
   protected int weatherChanceWinter;
   protected int meteorShowerChance;
   protected int earthquakeChance;
   protected int cometChance;
   protected int electricalStormChance;
   protected int hailStormChance;
   protected int blightChance;
   protected int averageTemperatureSpring;
   protected int averageTemperatureSummer;
   protected int averageTemperatureAutumn;
   protected int averageTemperatureWinter;
   protected int temperatureVariance;
   protected TimeModule.Season seasonStart;
   protected int seasonLengthSpring;
   protected int seasonLengthSummer;
   protected int seasonLengthAutumn;
   protected int seasonLengthWinter;
   protected int fullMoonInterval;
   protected int eclipseChance;
   protected int eclipseDelay;
   protected int bloodMoonChance;
   protected int bloodMoonDelay;
   protected boolean allowBloodMoonSpring;
   protected boolean allowBloodMoonSummer;
   protected boolean allowBloodMoonAutumn;
   protected boolean allowBloodMoonWinter;
   protected int animalSpawnRate;

   public void loadCustomProfileConfig() {
      Properties config = new Properties();

      try {
         config.load(new FileInputStream("profiles/profile" + Game.getActiveProfile() + "/customConfig.gameMode"));
      } catch (IOException var3) {
         System.out.println("Failed to load custom configuation file. Leaving default values.");
      }

      this.loadTemplate(config);
   }

   public void loadTemplate(String templateName) {
      Properties config = new Properties();

      try {
         config.load(new FileInputStream("profiles/gameModeConfigs/" + templateName + ".gameMode"));
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      this.loadTemplate(config);
   }

   public void loadTemplate(Properties config) {
      this.baseSpawnRate = Integer.parseInt(config.getProperty("baseSpawnRate", "30"));
      this.boostedMonsterChanceMultiplier = Double.parseDouble(config.getProperty("boostedMonsterChanceMultiplier", "1.0"));
      this.corruptionStartDay = Integer.parseInt(config.getProperty("corruptionStartDay", "1"));
      this.corruptionDesiredAmountStart = Integer.parseInt(config.getProperty("corruptionDesiredAmountStart", "1024"));
      this.corruptionDesiredAmountPerDay = Integer.parseInt(config.getProperty("corruptionDesiredAmountPerDay", "1024"));
      this.corruptionDesiredAmountPerBuilding = Integer.parseInt(config.getProperty("corruptionDesiredAmountPerBuilding", "128"));
      this.corruptionBuildingRate = Integer.parseInt(config.getProperty("corruptionBuildingRate", "20000"));
      this.corruptionBuildingDensity = Integer.parseInt(config.getProperty("corruptionBuildingDensity", "4"));
      this.corruptionBuildingDensityRange = Integer.parseInt(config.getProperty("corruptionBuildingDensityRange", "16"));
      this.maximumLootBoxes = Integer.parseInt(config.getProperty("maximumLootBoxes", "20"));
      this.lootBoxUncoverChance = Integer.parseInt(config.getProperty("lootBoxUncoverChance", "200"));
      this.lootBoxKeyChance = Integer.parseInt(config.getProperty("lootBoxKeyChance", "3"));
      this.baseNomadSpawnRate = Integer.parseInt(config.getProperty("baseNomadSpawnRate", "3"));
      this.nomadSpawnAmountMultiplier = Double.parseDouble(config.getProperty("nomadSpawnAmountMultiplier", "3"));
      this.nomadSpawnAmountMinimum = Integer.parseInt(config.getProperty("nomadSpawnAmountMinimum", "3"));
      this.nomadSpawnAmountMaximum = Integer.parseInt(config.getProperty("nomadSpawnAmountMaximum", "3"));
      this.weatherChanceSpring = Integer.parseInt(config.getProperty("weatherChanceSpring", "60000"));
      this.weatherChanceSummer = Integer.parseInt(config.getProperty("weatherChanceSummer", "150000"));
      this.weatherChanceAutumn = Integer.parseInt(config.getProperty("weatherChanceAutumn", "90000"));
      this.weatherChanceWinter = Integer.parseInt(config.getProperty("weatherChanceWinter", "80000"));
      this.meteorShowerChance = Integer.parseInt(config.getProperty("meteorShowerChance", "1000000"));
      this.earthquakeChance = Integer.parseInt(config.getProperty("earthquakeChance", "1000000"));
      this.cometChance = Integer.parseInt(config.getProperty("cometChance", "2000000"));
      this.electricalStormChance = Integer.parseInt(config.getProperty("electricalStormChance", "1000000"));
      this.hailStormChance = Integer.parseInt(config.getProperty("hailStormChance", "100000"));
      this.blightChance = Integer.parseInt(config.getProperty("blightChance", "100000"));
      this.averageTemperatureSpring = Integer.parseInt(config.getProperty("averageTemperatureSpring", "60"));
      this.averageTemperatureSummer = Integer.parseInt(config.getProperty("averageTemperatureSummer", "100"));
      this.averageTemperatureAutumn = Integer.parseInt(config.getProperty("averageTemperatureAutumn", "45"));
      this.averageTemperatureWinter = Integer.parseInt(config.getProperty("averageTemperatureWinter", "5"));
      this.temperatureVariance = Integer.parseInt(config.getProperty("temperatureVariance", "20"));
      String seasonStartString = config.getProperty("seasonStart", "SPRING");

      TimeModule.Season[] var6;
      for (TimeModule.Season s : TimeModule.Season.values()) {
         if (s.toString().equals(seasonStartString)) {
            this.seasonStart = s;
         }
      }

      this.seasonLengthSpring = Integer.parseInt(config.getProperty("seasonLengthSpring", "5"));
      this.seasonLengthSummer = Integer.parseInt(config.getProperty("seasonLengthSummer", "5"));
      this.seasonLengthAutumn = Integer.parseInt(config.getProperty("seasonLengthAutumn", "5"));
      this.seasonLengthWinter = Integer.parseInt(config.getProperty("seasonLengthWinter", "5"));
      this.fullMoonInterval = Integer.parseInt(config.getProperty("fullMoonInterval", "7"));
      this.eclipseChance = Integer.parseInt(config.getProperty("eclipseChance", "10"));
      this.eclipseDelay = Integer.parseInt(config.getProperty("eclipseDelay", "6"));
      this.bloodMoonChance = Integer.parseInt(config.getProperty("bloodMoonChance", "4"));
      this.bloodMoonDelay = Integer.parseInt(config.getProperty("bloodMoonDelay", "6"));
      this.allowBloodMoonSpring = Boolean.parseBoolean(config.getProperty("allowBloodMoonSpring", "true"));
      this.allowBloodMoonSummer = Boolean.parseBoolean(config.getProperty("allowBloodMoonSummer", "false"));
      this.allowBloodMoonAutumn = Boolean.parseBoolean(config.getProperty("allowBloodMoonAutumn", "true"));
      this.allowBloodMoonWinter = Boolean.parseBoolean(config.getProperty("allowBloodMoonWinter", "false"));
      this.animalSpawnRate = Integer.parseInt(config.getProperty("animalSpawnRate", "20000"));
   }

   public void copyTemplate(GameModeTemplateBase.GameMode gM) {
      GameModeTemplateBase template = gM.getTemplate();
      this.baseSpawnRate = template.getBaseSpawnRate();
      this.boostedMonsterChanceMultiplier = template.getBoostedMonsterChanceMultiplier();
      this.corruptionStartDay = template.getCorruptionStartDay();
      this.corruptionDesiredAmountStart = template.getCorruptionDesiredAmountStart();
      this.corruptionDesiredAmountPerDay = template.getCorruptionDesiredAmountPerDay();
      this.corruptionDesiredAmountPerBuilding = template.getCorruptionDesiredAmountPerBuilding();
      this.corruptionBuildingRate = template.getCorruptionBuildingRate();
      this.corruptionBuildingDensity = template.getCorruptionBuildingDensity();
      this.corruptionBuildingDensityRange = template.getCorruptionBuildingDensityRange();
      this.maximumLootBoxes = template.getMaximumLootBoxes();
      this.lootBoxUncoverChance = template.getLootBoxUncoverChance();
      this.lootBoxKeyChance = template.getLootBoxKeyChance();
      this.baseNomadSpawnRate = template.getBaseNomadSpawnRate();
      this.nomadSpawnAmountMultiplier = template.getNomadSpawnAmountMultiplier();
      this.nomadSpawnAmountMinimum = template.getNomadSpawnAmountMinimum();
      this.nomadSpawnAmountMaximum = template.getNomadSpawnAmountMaximum();
      this.weatherChanceSpring = template.getWeatherChanceSpring();
      this.weatherChanceSummer = template.getWeatherChanceSummer();
      this.weatherChanceAutumn = template.getWeatherChanceAutumn();
      this.weatherChanceWinter = template.getWeatherChanceWinter();
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
      this.seasonStart = template.getSeasonStart();
      this.seasonLengthSpring = template.getSeasonLengthSpring();
      this.seasonLengthSummer = template.getSeasonLengthSummer();
      this.seasonLengthAutumn = template.getSeasonLengthAutumn();
      this.seasonLengthWinter = template.getSeasonLengthWinter();
      this.fullMoonInterval = template.getFullMoonInterval();
      this.eclipseChance = template.getEclipseChance();
      this.eclipseDelay = template.getEclipseDelay();
      this.bloodMoonChance = template.getBloodMoonChance();
      this.bloodMoonDelay = template.getBloodMoonDelay();
      this.allowBloodMoonSpring = template.allowBloodMoonSpring();
      this.allowBloodMoonSummer = template.allowBloodMoonSummer();
      this.allowBloodMoonAutumn = template.allowBloodMoonAutumn();
      this.allowBloodMoonWinter = template.allowBloodMoonWinter();
      this.animalSpawnRate = template.getAnimalSpawnRate();
   }

   public void saveTemplate(String templateName) {
      Properties config = this.buildPropertiesFile();

      try {
         File folderOut = new File("profiles/gameModeConfigs/");
         folderOut.mkdirs();
         FileOutputStream configOut = new FileOutputStream("profiles/gameModeConfigs/" + templateName + ".gameMode");
         config.store(configOut, "Game Mode Template Data");
         configOut.flush();
         configOut.close();
      } catch (IOException var5) {
         Console.out("Failed to save game mode template!", true);
      }
   }

   public void saveSetCustomTemplate() {
      Properties config = this.buildPropertiesFile();

      try {
         File folderOut = new File("profiles/profile" + Game.getActiveProfile() + "/");
         folderOut.mkdirs();
         FileOutputStream configOut = new FileOutputStream("profiles/profile" + Game.getActiveProfile() + "/customConfig.gameMode");
         config.store(configOut, "Game Mode Template Data");
         configOut.flush();
         configOut.close();
      } catch (IOException var4) {
         Console.out("Failed to custom game mode template!", true);
      }
   }

   private Properties buildPropertiesFile() {
      Properties config = new Properties();
      config.setProperty("baseSpawnRate", Integer.toString(this.baseSpawnRate));
      config.setProperty("boostedMonsterChanceMultiplier", Double.toString(this.boostedMonsterChanceMultiplier));
      config.setProperty("corruptionStartDay", Integer.toString(this.corruptionStartDay));
      config.setProperty("corruptionDesiredAmountStart", Integer.toString(this.corruptionDesiredAmountStart));
      config.setProperty("corruptionDesiredAmountPerDay", Integer.toString(this.corruptionDesiredAmountPerDay));
      config.setProperty("corruptionDesiredAmountPerBuilding", Integer.toString(this.corruptionDesiredAmountPerBuilding));
      config.setProperty("corruptionBuildingRate", Integer.toString(this.corruptionBuildingRate));
      config.setProperty("corruptionBuildingDensity", Integer.toString(this.corruptionBuildingDensity));
      config.setProperty("corruptionBuildingDensityRange", Integer.toString(this.corruptionBuildingDensityRange));
      config.setProperty("maximumLootBoxes", Integer.toString(this.maximumLootBoxes));
      config.setProperty("lootBoxUncoverChance", Integer.toString(this.lootBoxUncoverChance));
      config.setProperty("lootBoxKeyChance", Integer.toString(this.lootBoxKeyChance));
      config.setProperty("baseNomadSpawnRate", Integer.toString(this.baseNomadSpawnRate));
      config.setProperty("nomadSpawnAmountMultiplier", Double.toString(this.nomadSpawnAmountMultiplier));
      config.setProperty("nomadSpawnAmountMinimum", Integer.toString(this.nomadSpawnAmountMinimum));
      config.setProperty("nomadSpawnAmountMaximum", Integer.toString(this.nomadSpawnAmountMaximum));
      config.setProperty("weatherChanceSpring", Integer.toString(this.weatherChanceSpring));
      config.setProperty("weatherChanceSummer", Integer.toString(this.weatherChanceSummer));
      config.setProperty("weatherChanceAutumn", Integer.toString(this.weatherChanceAutumn));
      config.setProperty("weatherChanceWinter", Integer.toString(this.weatherChanceWinter));
      config.setProperty("meteorShowerChance", Integer.toString(this.meteorShowerChance));
      config.setProperty("earthquakeChance", Integer.toString(this.earthquakeChance));
      config.setProperty("cometChance", Integer.toString(this.cometChance));
      config.setProperty("electricalStormChance", Integer.toString(this.electricalStormChance));
      config.setProperty("hailStormChance", Integer.toString(this.hailStormChance));
      config.setProperty("blightChance", Integer.toString(this.blightChance));
      config.setProperty("averageTemperatureSpring", Integer.toString(this.averageTemperatureSpring));
      config.setProperty("averageTemperatureSummer", Integer.toString(this.averageTemperatureSummer));
      config.setProperty("averageTemperatureAutumn", Integer.toString(this.averageTemperatureAutumn));
      config.setProperty("averageTemperatureWinter", Integer.toString(this.averageTemperatureWinter));
      config.setProperty("temperatureVariance", Integer.toString(this.temperatureVariance));
      config.setProperty("seasonStart", this.seasonStart.name());
      config.setProperty("seasonLengthSpring", Integer.toString(this.seasonLengthSpring));
      config.setProperty("seasonLengthSummer", Integer.toString(this.seasonLengthSummer));
      config.setProperty("seasonLengthAutumn", Integer.toString(this.seasonLengthAutumn));
      config.setProperty("seasonLengthWinter", Integer.toString(this.seasonLengthWinter));
      config.setProperty("fullMoonInterval", Integer.toString(this.fullMoonInterval));
      config.setProperty("eclipseChance", Integer.toString(this.eclipseChance));
      config.setProperty("eclipseDelay", Integer.toString(this.eclipseDelay));
      config.setProperty("bloodMoonChance", Integer.toString(this.bloodMoonChance));
      config.setProperty("bloodMoonDelay", Integer.toString(this.bloodMoonDelay));
      config.setProperty("allowBloodMoonSpring", Boolean.toString(this.allowBloodMoonSpring));
      config.setProperty("allowBloodMoonSummer", Boolean.toString(this.allowBloodMoonSummer));
      config.setProperty("allowBloodMoonAutumn", Boolean.toString(this.allowBloodMoonAutumn));
      config.setProperty("allowBloodMoonWinter", Boolean.toString(this.allowBloodMoonWinter));
      config.setProperty("animalSpawnRate", Integer.toString(this.animalSpawnRate));
      return config;
   }

   public void deleteTemplate(String templateName) {
      File f = new File("profiles/gameModeConfigs/" + templateName + ".gameMode");

      try {
         FileUtils.forceDelete(f);
      } catch (IOException var4) {
         var4.printStackTrace();
      }
   }

   public int getBaseSpawnRate() {
      return this.baseSpawnRate;
   }

   public double getBoostedMonsterChanceMultiplier() {
      return this.boostedMonsterChanceMultiplier;
   }

   public int getCorruptionStartDay() {
      return this.corruptionStartDay;
   }

   public int getCorruptionDesiredAmountStart() {
      return this.corruptionDesiredAmountStart;
   }

   public int getCorruptionDesiredAmountPerDay() {
      return this.corruptionDesiredAmountPerDay;
   }

   public int getCorruptionDesiredAmountPerBuilding() {
      return this.corruptionDesiredAmountPerBuilding;
   }

   public int getCorruptionBuildingRate() {
      return this.corruptionBuildingRate;
   }

   public int getCorruptionBuildingDensity() {
      return this.corruptionBuildingDensity;
   }

   public int getCorruptionBuildingDensityRange() {
      return this.corruptionBuildingDensityRange;
   }

   public int getMaximumLootBoxes() {
      return this.maximumLootBoxes;
   }

   public int getLootBoxUncoverChance() {
      return this.lootBoxUncoverChance;
   }

   public int getLootBoxKeyChance() {
      return this.lootBoxKeyChance;
   }

   public int getBaseNomadSpawnRate() {
      return this.baseNomadSpawnRate;
   }

   public double getNomadSpawnAmountMultiplier() {
      return this.nomadSpawnAmountMultiplier;
   }

   public int getNomadSpawnAmountMinimum() {
      return this.nomadSpawnAmountMinimum;
   }

   public int getNomadSpawnAmountMaximum() {
      return this.nomadSpawnAmountMaximum;
   }

   public int getWeatherChanceSpring() {
      return this.weatherChanceSpring;
   }

   public int getWeatherChanceSummer() {
      return this.weatherChanceSummer;
   }

   public int getWeatherChanceAutumn() {
      return this.weatherChanceAutumn;
   }

   public int getWeatherChanceWinter() {
      return this.weatherChanceWinter;
   }

   public int getMeteorShowerChance() {
      return this.meteorShowerChance;
   }

   public int getEarthquakeChance() {
      return this.earthquakeChance;
   }

   public int getCometChance() {
      return this.cometChance;
   }

   public int getElectricalStormChance() {
      return this.electricalStormChance;
   }

   public int getHailStormChance() {
      return this.hailStormChance;
   }

   public int getBlightChance() {
      return this.blightChance;
   }

   public int getAverageTemperatureSpring() {
      return this.averageTemperatureSpring;
   }

   public int getAverageTemperatureSummer() {
      return this.averageTemperatureSummer;
   }

   public int getAverageTemperatureAutumn() {
      return this.averageTemperatureAutumn;
   }

   public int getAverageTemperatureWinter() {
      return this.averageTemperatureWinter;
   }

   public int getTemperatureVariance() {
      return this.temperatureVariance;
   }

   public TimeModule.Season getSeasonStart() {
      return this.seasonStart;
   }

   public int getSeasonLengthSpring() {
      return this.seasonLengthSpring;
   }

   public int getSeasonLengthSummer() {
      return this.seasonLengthSummer;
   }

   public int getSeasonLengthAutumn() {
      return this.seasonLengthAutumn;
   }

   public int getSeasonLengthWinter() {
      return this.seasonLengthWinter;
   }

   public int getFullMoonInterval() {
      return this.fullMoonInterval;
   }

   public int getEclipseChance() {
      return this.eclipseChance;
   }

   public int getEclipseDelay() {
      return this.eclipseDelay;
   }

   public int getBloodMoonChance() {
      return this.bloodMoonChance;
   }

   public int getBloodMoonDelay() {
      return this.bloodMoonDelay;
   }

   public boolean allowBloodMoonSpring() {
      return this.allowBloodMoonSpring;
   }

   public boolean allowBloodMoonSummer() {
      return this.allowBloodMoonSummer;
   }

   public boolean allowBloodMoonAutumn() {
      return this.allowBloodMoonAutumn;
   }

   public boolean allowBloodMoonWinter() {
      return this.allowBloodMoonWinter;
   }

   public int getAnimalSpawnRate() {
      return this.animalSpawnRate;
   }

   public void setBaseSpawnRate(int i) {
      this.baseSpawnRate -= i;
   }

   public void increaseBaseSpawnRate(int i) {
      if (this.baseSpawnRate < 1000) {
         this.baseSpawnRate += i;
      }

      if (this.baseSpawnRate > 1000) {
         this.baseSpawnRate = 1000;
      }
   }

   public void decreaseBaseSpawnRate(int i) {
      if (this.baseSpawnRate > 0) {
         this.baseSpawnRate -= i;
      }

      if (this.baseSpawnRate < 0) {
         this.baseSpawnRate = 0;
      }
   }

   public void setBoostedMonsterChanceMultiplier(double i) {
      this.boostedMonsterChanceMultiplier -= i;
   }

   public void increaseBoostedMonsterChanceMultiplier(double i) {
      if (this.boostedMonsterChanceMultiplier < 10.0) {
         this.boostedMonsterChanceMultiplier += i;
      }

      if (this.boostedMonsterChanceMultiplier > 10.0) {
         this.boostedMonsterChanceMultiplier = 10.0;
      }
   }

   public void decreaseBoostedMonsterChanceMultiplier(double i) {
      if (this.boostedMonsterChanceMultiplier > 0.25) {
         this.boostedMonsterChanceMultiplier -= i;
      }

      if (this.boostedMonsterChanceMultiplier < 0.25) {
         this.boostedMonsterChanceMultiplier = 0.25;
      }
   }

   public void setCorruptionStartDay(int i) {
      this.corruptionStartDay -= i;
   }

   public void increaseCorruptionStartDay(int i) {
      if (this.corruptionStartDay < 200) {
         this.corruptionStartDay += i;
      }

      if (this.corruptionStartDay > 200) {
         this.corruptionStartDay = 200;
      }
   }

   public void decreaseCorruptionStartDay(int i) {
      if (this.corruptionStartDay > 0) {
         this.corruptionStartDay -= i;
      }

      if (this.corruptionStartDay < 0) {
         this.corruptionStartDay = 0;
      }
   }

   public void setCorruptionDesiredAmountStart(int i) {
      this.corruptionDesiredAmountStart -= i;
   }

   public void increaseCorruptionDesiredAmountStart(int i) {
      if (this.corruptionDesiredAmountStart < 8192) {
         this.corruptionDesiredAmountStart += i;
      }

      if (this.corruptionDesiredAmountStart > 8192) {
         this.corruptionDesiredAmountStart = 8192;
      }
   }

   public void decreaseCorruptionDesiredAmountStart(int i) {
      if (this.corruptionDesiredAmountStart > 64) {
         this.corruptionDesiredAmountStart -= i;
      }

      if (this.corruptionDesiredAmountStart < 64) {
         this.corruptionDesiredAmountStart = 64;
      }
   }

   public void setCorruptionDesiredAmountPerDay(int i) {
      this.corruptionDesiredAmountPerDay -= i;
   }

   public void increaseCorruptionDesiredAmountPerDay(int i) {
      if (this.corruptionDesiredAmountPerDay < 8192) {
         this.corruptionDesiredAmountPerDay += i;
      }

      if (this.corruptionDesiredAmountPerDay > 8192) {
         this.corruptionDesiredAmountPerDay = 8192;
      }
   }

   public void decreaseCorruptionDesiredAmountPerDay(int i) {
      if (this.corruptionDesiredAmountPerDay > 64) {
         this.corruptionDesiredAmountPerDay -= i;
      }

      if (this.corruptionDesiredAmountPerDay < 64) {
         this.corruptionDesiredAmountPerDay = 64;
      }
   }

   public void setCorruptionDesiredAmountPerBuilding(int i) {
      this.corruptionDesiredAmountPerBuilding -= i;
   }

   public void increaseCorruptionDesiredAmountPerBuilding(int i) {
      if (this.corruptionDesiredAmountPerBuilding < 512) {
         this.corruptionDesiredAmountPerBuilding += i;
      }

      if (this.corruptionDesiredAmountPerBuilding > 512) {
         this.corruptionDesiredAmountPerBuilding = 512;
      }
   }

   public void decreaseCorruptionDesiredAmountPerBuilding(int i) {
      if (this.corruptionDesiredAmountPerBuilding > 16) {
         this.corruptionDesiredAmountPerBuilding -= i;
      }

      if (this.corruptionDesiredAmountPerBuilding < 16) {
         this.corruptionDesiredAmountPerBuilding = 16;
      }
   }

   public void setCorruptionBuildingRate(int i) {
      this.corruptionBuildingRate -= i;
   }

   public void increaseCorruptionBuildingRate(int i) {
      if (this.corruptionBuildingRate < 500000) {
         this.corruptionBuildingRate += i;
      }

      if (this.corruptionBuildingRate > 500000) {
         this.corruptionBuildingRate = 500000;
      }
   }

   public void decreaseCorruptionBuildingRate(int i) {
      if (this.corruptionBuildingRate > 5000) {
         this.corruptionBuildingRate -= i;
      }

      if (this.corruptionBuildingRate < 5000) {
         this.corruptionBuildingRate = 5000;
      }
   }

   public void setCorruptionBuildingDensity(int i) {
      this.corruptionBuildingDensity -= i;
   }

   public void increaseCorruptionBuildingDensity(int i) {
      if (this.corruptionBuildingDensity < 50) {
         this.corruptionBuildingDensity += i;
      }

      if (this.corruptionBuildingDensity > 50) {
         this.corruptionBuildingDensity = 50;
      }
   }

   public void decreaseCorruptionBuildingDensity(int i) {
      if (this.corruptionBuildingDensity > 1) {
         this.corruptionBuildingDensity -= i;
      }

      if (this.corruptionBuildingDensity < 1) {
         this.corruptionBuildingDensity = 1;
      }
   }

   public void setCorruptionBuildingDensityRange(int i) {
      this.corruptionBuildingDensityRange -= i;
   }

   public void increaseCorruptionBuildingDensityRange(int i) {
      if (this.corruptionBuildingDensityRange < 128) {
         this.corruptionBuildingDensityRange += i;
      }

      if (this.corruptionBuildingDensityRange > 128) {
         this.corruptionBuildingDensityRange = 128;
      }
   }

   public void decreaseCorruptionBuildingDensityRange(int i) {
      if (this.corruptionBuildingDensityRange > 8) {
         this.corruptionBuildingDensityRange -= i;
      }

      if (this.corruptionBuildingDensityRange < 8) {
         this.corruptionBuildingDensityRange = 8;
      }
   }

   public void setMaximumLootBoxes(int i) {
      this.maximumLootBoxes -= i;
   }

   public void increaseMaximumLootBoxes(int i) {
      if (this.maximumLootBoxes < 250) {
         this.maximumLootBoxes += i;
      }

      if (this.maximumLootBoxes > 250) {
         this.maximumLootBoxes = 250;
      }
   }

   public void decreaseMaximumLootBoxes(int i) {
      if (this.maximumLootBoxes > 0) {
         this.maximumLootBoxes -= i;
      }

      if (this.maximumLootBoxes < 0) {
         this.maximumLootBoxes = 0;
      }
   }

   public void setLootBoxUncoverChance(int i) {
      this.lootBoxUncoverChance -= i;
   }

   public void increaseLootBoxUncoverChance(int i) {
      if (this.lootBoxUncoverChance < 5000) {
         this.lootBoxUncoverChance += i;
      }

      if (this.lootBoxUncoverChance > 5000) {
         this.lootBoxUncoverChance = 5000;
      }
   }

   public void decreaseLootBoxUncoverChance(int i) {
      if (this.lootBoxUncoverChance > 0) {
         this.lootBoxUncoverChance -= i;
      }

      if (this.lootBoxUncoverChance < 0) {
         this.lootBoxUncoverChance = 0;
      }
   }

   public void setLootBoxKeyChance(int i) {
      this.lootBoxKeyChance -= i;
   }

   public void increaseLootBoxKeyChance(int i) {
      if (this.lootBoxKeyChance < 50) {
         this.lootBoxKeyChance += i;
      }

      if (this.lootBoxKeyChance > 50) {
         this.lootBoxKeyChance = 50;
      }
   }

   public void decreaseLootBoxKeyChance(int i) {
      if (this.lootBoxKeyChance > 0) {
         this.lootBoxKeyChance -= i;
      }

      if (this.lootBoxKeyChance < 0) {
         this.lootBoxKeyChance = 0;
      }
   }

   public void setBaseNomadSpawnRate(int i) {
      this.baseNomadSpawnRate -= i;
   }

   public void increaseBaseNomadSpawnRate(int i) {
      if (this.baseNomadSpawnRate < 320000) {
         this.baseNomadSpawnRate += i;
      }

      if (this.baseNomadSpawnRate > 320000) {
         this.baseNomadSpawnRate = 320000;
      }
   }

   public void decreaseBaseNomadSpawnRate(int i) {
      if (this.baseNomadSpawnRate > 5000) {
         this.baseNomadSpawnRate -= i;
      }

      if (this.baseNomadSpawnRate < 5000) {
         this.baseNomadSpawnRate = 5000;
      }
   }

   public void setNomadSpawnAmountMultiplier(double i) {
      this.nomadSpawnAmountMultiplier -= i;
   }

   public void increaseNomadSpawnAmountMultiplier(double i) {
      if (this.nomadSpawnAmountMultiplier < 10.0) {
         this.nomadSpawnAmountMultiplier += i;
      }

      if (this.nomadSpawnAmountMultiplier > 10.0) {
         this.nomadSpawnAmountMultiplier = 10.0;
      }
   }

   public void decreaseNomadSpawnAmountMultiplier(double i) {
      if (this.nomadSpawnAmountMultiplier > 0.25) {
         this.nomadSpawnAmountMultiplier -= i;
      }

      if (this.nomadSpawnAmountMultiplier < 0.25) {
         this.nomadSpawnAmountMultiplier = 0.25;
      }
   }

   public void setNomadSpawnAmountMinimum(int i) {
      this.nomadSpawnAmountMinimum -= i;
   }

   public void increaseNomadSpawnAmountMinimum(int i) {
      if (this.nomadSpawnAmountMinimum < 50) {
         this.nomadSpawnAmountMinimum += i;
      }

      if (this.nomadSpawnAmountMinimum > 50) {
         this.nomadSpawnAmountMinimum = 50;
      }
   }

   public void decreaseNomadSpawnAmountMinimum(int i) {
      if (this.nomadSpawnAmountMinimum > 1) {
         this.nomadSpawnAmountMinimum -= i;
      }

      if (this.nomadSpawnAmountMinimum < 1) {
         this.nomadSpawnAmountMinimum = 1;
      }
   }

   public void setNomadSpawnAmountMaximum(int i) {
      this.nomadSpawnAmountMaximum -= i;
   }

   public void increaseNomadSpawnAmountMaximum(int i) {
      if (this.nomadSpawnAmountMaximum < 50) {
         this.nomadSpawnAmountMaximum += i;
      }

      if (this.nomadSpawnAmountMaximum > 50) {
         this.nomadSpawnAmountMaximum = 50;
      }
   }

   public void decreaseNomadSpawnAmountMaximum(int i) {
      if (this.nomadSpawnAmountMaximum > 1) {
         this.nomadSpawnAmountMaximum -= i;
      }

      if (this.nomadSpawnAmountMaximum < 1) {
         this.nomadSpawnAmountMaximum = 1;
      }
   }

   public void setWeatherChanceSpring(int i) {
      this.weatherChanceSpring -= i;
   }

   public void increaseWeatherChanceSpring(int i) {
      if (this.weatherChanceSpring < 300000) {
         this.weatherChanceSpring += i;
      }

      if (this.weatherChanceSpring > 300000) {
         this.weatherChanceSpring = 300000;
      }
   }

   public void decreaseWeatherChanceSpring(int i) {
      if (this.weatherChanceSpring > 0) {
         this.weatherChanceSpring -= i;
      }

      if (this.weatherChanceSpring < 0) {
         this.weatherChanceSpring = 0;
      }
   }

   public void setWeatherChanceSummer(int i) {
      this.weatherChanceSummer -= i;
   }

   public void increaseWeatherChanceSummer(int i) {
      if (this.weatherChanceSummer < 300000) {
         this.weatherChanceSummer += i;
      }

      if (this.weatherChanceSummer > 300000) {
         this.weatherChanceSummer = 300000;
      }
   }

   public void decreaseWeatherChanceSummer(int i) {
      if (this.weatherChanceSummer > 0) {
         this.weatherChanceSummer -= i;
      }

      if (this.weatherChanceSummer < 0) {
         this.weatherChanceSummer = 0;
      }
   }

   public void setWeatherChanceAutumn(int i) {
      this.weatherChanceAutumn -= i;
   }

   public void increaseWeatherChanceAutumn(int i) {
      if (this.weatherChanceAutumn < 300000) {
         this.weatherChanceAutumn += i;
      }

      if (this.weatherChanceAutumn > 300000) {
         this.weatherChanceAutumn = 300000;
      }
   }

   public void decreaseWeatherChanceAutumn(int i) {
      if (this.weatherChanceAutumn > 0) {
         this.weatherChanceAutumn -= i;
      }

      if (this.weatherChanceAutumn < 0) {
         this.weatherChanceAutumn = 0;
      }
   }

   public void setWeatherChanceWinter(int i) {
      this.weatherChanceWinter -= i;
   }

   public void increaseWeatherChanceWinter(int i) {
      if (this.weatherChanceWinter < 300000) {
         this.weatherChanceWinter += i;
      }

      if (this.weatherChanceWinter > 300000) {
         this.weatherChanceWinter = 300000;
      }
   }

   public void decreaseWeatherChanceWinter(int i) {
      if (this.weatherChanceWinter > 0) {
         this.weatherChanceWinter -= i;
      }

      if (this.weatherChanceWinter < 0) {
         this.weatherChanceWinter = 0;
      }
   }

   public void setMeteorShowerChance(int i) {
      this.meteorShowerChance -= i;
   }

   public void increaseMeteorShowerChance(int i) {
      if (this.meteorShowerChance < 3000000) {
         this.meteorShowerChance += i;
      }

      if (this.meteorShowerChance > 3000000) {
         this.meteorShowerChance = 3000000;
      }
   }

   public void decreaseMeteorShowerChance(int i) {
      if (this.meteorShowerChance > 0) {
         this.meteorShowerChance -= i;
      }

      if (this.meteorShowerChance < 0) {
         this.meteorShowerChance = 0;
      }
   }

   public void setEarthquakeChance(int i) {
      this.earthquakeChance -= i;
   }

   public void increaseEarthquakeChance(int i) {
      if (this.earthquakeChance < 3000000) {
         this.earthquakeChance += i;
      }

      if (this.earthquakeChance > 3000000) {
         this.earthquakeChance = 3000000;
      }
   }

   public void decreaseEarthquakeChance(int i) {
      if (this.earthquakeChance > 0) {
         this.earthquakeChance -= i;
      }

      if (this.earthquakeChance < 0) {
         this.earthquakeChance = 0;
      }
   }

   public void setCometChance(int i) {
      this.cometChance -= i;
   }

   public void increaseCometChance(int i) {
      if (this.cometChance < 3000000) {
         this.cometChance += i;
      }

      if (this.cometChance > 3000000) {
         this.cometChance = 3000000;
      }
   }

   public void decreaseCometChance(int i) {
      if (this.cometChance > 0) {
         this.cometChance -= i;
      }

      if (this.cometChance < 0) {
         this.cometChance = 0;
      }
   }

   public void setElectricalStormChance(int i) {
      this.electricalStormChance -= i;
   }

   public void increaseElectricalStormChance(int i) {
      if (this.electricalStormChance < 3000000) {
         this.electricalStormChance += i;
      }

      if (this.electricalStormChance > 3000000) {
         this.electricalStormChance = 3000000;
      }
   }

   public void decreaseElectricalStormChance(int i) {
      if (this.electricalStormChance > 0) {
         this.electricalStormChance -= i;
      }

      if (this.electricalStormChance < 0) {
         this.electricalStormChance = 0;
      }
   }

   public void setHailStormChance(int i) {
      this.hailStormChance -= i;
   }

   public void increaseHailStormChance(int i) {
      if (this.hailStormChance < 3000000) {
         this.hailStormChance += i;
      }

      if (this.hailStormChance > 3000000) {
         this.hailStormChance = 3000000;
      }
   }

   public void decreaseHailStormChance(int i) {
      if (this.hailStormChance > 0) {
         this.hailStormChance -= i;
      }

      if (this.hailStormChance < 0) {
         this.hailStormChance = 0;
      }
   }

   public void setBlightChance(int i) {
      this.blightChance -= i;
   }

   public void increaseBlightChance(int i) {
      if (this.blightChance < 3000000) {
         this.blightChance += i;
      }

      if (this.blightChance > 3000000) {
         this.blightChance = 3000000;
      }
   }

   public void decreaseBlightChance(int i) {
      if (this.blightChance > 0) {
         this.blightChance -= i;
      }

      if (this.blightChance < 0) {
         this.blightChance = 0;
      }
   }

   public void setAverageTemperatureSpring(int i) {
      this.averageTemperatureSpring -= i;
   }

   public void increaseAverageTemperatureSpring(int i) {
      if (this.averageTemperatureSpring < 100) {
         this.averageTemperatureSpring += i;
      }

      if (this.averageTemperatureSpring > 100) {
         this.averageTemperatureSpring = 100;
      }
   }

   public void decreaseAverageTemperatureSpring(int i) {
      if (this.averageTemperatureSpring > 0) {
         this.averageTemperatureSpring -= i;
      }

      if (this.averageTemperatureSpring < 0) {
         this.averageTemperatureSpring = 0;
      }
   }

   public void setAverageTemperatureSummer(int i) {
      this.averageTemperatureSummer -= i;
   }

   public void increaseAverageTemperatureSummer(int i) {
      if (this.averageTemperatureSummer < 100) {
         this.averageTemperatureSummer += i;
      }

      if (this.averageTemperatureSummer > 100) {
         this.averageTemperatureSummer = 100;
      }
   }

   public void decreaseAverageTemperatureSummer(int i) {
      if (this.averageTemperatureSummer > 0) {
         this.averageTemperatureSummer -= i;
      }

      if (this.averageTemperatureSummer < 0) {
         this.averageTemperatureSummer = 0;
      }
   }

   public void setAverageTemperatureAutumn(int i) {
      this.averageTemperatureAutumn -= i;
   }

   public void increaseAverageTemperatureAutumn(int i) {
      if (this.averageTemperatureAutumn < 100) {
         this.averageTemperatureAutumn += i;
      }

      if (this.averageTemperatureAutumn > 100) {
         this.averageTemperatureAutumn = 100;
      }
   }

   public void decreaseAverageTemperatureAutumn(int i) {
      if (this.averageTemperatureAutumn > 0) {
         this.averageTemperatureAutumn -= i;
      }

      if (this.averageTemperatureAutumn < 0) {
         this.averageTemperatureAutumn = 0;
      }
   }

   public void setAverageTemperatureWinter(int i) {
      this.averageTemperatureWinter -= i;
   }

   public void increaseAverageTemperatureWinter(int i) {
      if (this.averageTemperatureWinter < 100) {
         this.averageTemperatureWinter += i;
      }

      if (this.averageTemperatureWinter > 100) {
         this.averageTemperatureWinter = 100;
      }
   }

   public void decreaseAverageTemperatureWinter(int i) {
      if (this.averageTemperatureWinter > 0) {
         this.averageTemperatureWinter -= i;
      }

      if (this.averageTemperatureWinter < 0) {
         this.averageTemperatureWinter = 0;
      }
   }

   public void setTemperatureVariance(int i) {
      this.temperatureVariance -= i;
   }

   public void increaseTemperatureVariance(int i) {
      if (this.temperatureVariance < 20) {
         this.temperatureVariance += i;
      }

      if (this.temperatureVariance > 20) {
         this.temperatureVariance = 20;
      }
   }

   public void decreaseTemperatureVariance(int i) {
      if (this.temperatureVariance > 0) {
         this.temperatureVariance -= i;
      }

      if (this.temperatureVariance < 0) {
         this.temperatureVariance = 0;
      }
   }

   public void getSeasonStart(TimeModule.Season s) {
      this.seasonStart = s;
   }

   public void increaseSeasonStart(int i) {
      if (this.seasonStart.ordinal() < TimeModule.Season.values().length - 1) {
         this.seasonStart = TimeModule.Season.values()[this.seasonStart.ordinal() + 1];
      } else {
         this.seasonStart = TimeModule.Season.values()[0];
      }
   }

   public void decreaseSeasonStart(int i) {
      if (this.seasonStart.ordinal() == 0) {
         this.seasonStart = TimeModule.Season.values()[TimeModule.Season.values().length - 1];
      } else {
         this.seasonStart = TimeModule.Season.values()[this.seasonStart.ordinal() - 1];
      }
   }

   public void setSeasonLengthSpring(int i) {
      this.seasonLengthSpring -= i;
   }

   public void increaseSeasonLengthSpring(int i) {
      if (this.seasonLengthSpring < 30) {
         this.seasonLengthSpring += i;
      }

      if (this.seasonLengthSpring > 30) {
         this.seasonLengthSpring = 30;
      }
   }

   public void decreaseSeasonLengthSpring(int i) {
      if (this.seasonLengthSpring > 1) {
         this.seasonLengthSpring -= i;
      }

      if (this.seasonLengthSpring < 1) {
         this.seasonLengthSpring = 1;
      }
   }

   public void setSeasonLengthSummer(int i) {
      this.seasonLengthSummer -= i;
   }

   public void increaseSeasonLengthSummer(int i) {
      if (this.seasonLengthSummer < 30) {
         this.seasonLengthSummer += i;
      }

      if (this.seasonLengthSummer > 30) {
         this.seasonLengthSummer = 30;
      }
   }

   public void decreaseSeasonLengthSummer(int i) {
      if (this.seasonLengthSummer > 1) {
         this.seasonLengthSummer -= i;
      }

      if (this.seasonLengthSummer < 1) {
         this.seasonLengthSummer = 1;
      }
   }

   public void setSeasonLengthAutumn(int i) {
      this.seasonLengthAutumn -= i;
   }

   public void increaseSeasonLengthAutumn(int i) {
      if (this.seasonLengthAutumn < 30) {
         this.seasonLengthAutumn += i;
      }

      if (this.seasonLengthAutumn > 30) {
         this.seasonLengthAutumn = 30;
      }
   }

   public void decreaseSeasonLengthAutumn(int i) {
      if (this.seasonLengthAutumn > 1) {
         this.seasonLengthAutumn -= i;
      }

      if (this.seasonLengthAutumn < 1) {
         this.seasonLengthAutumn = 1;
      }
   }

   public void setSeasonLengthWinter(int i) {
      this.seasonLengthWinter -= i;
   }

   public void increaseSeasonLengthWinter(int i) {
      if (this.seasonLengthWinter < 30) {
         this.seasonLengthWinter += i;
      }

      if (this.seasonLengthWinter > 30) {
         this.seasonLengthWinter = 30;
      }
   }

   public void decreaseSeasonLengthWinter(int i) {
      if (this.seasonLengthWinter > 1) {
         this.seasonLengthWinter -= i;
      }

      if (this.seasonLengthWinter < 1) {
         this.seasonLengthWinter = 1;
      }
   }

   public void setFullMoonInterval(int i) {
      this.fullMoonInterval -= i;
   }

   public void increaseFullMoonInterval(int i) {
      if (this.fullMoonInterval < 30) {
         this.fullMoonInterval += i;
      }

      if (this.fullMoonInterval == 0) {
         this.fullMoonInterval++;
      }

      if (this.fullMoonInterval > 30) {
         this.fullMoonInterval = 30;
      }
   }

   public void decreaseFullMoonInterval(int i) {
      if (this.fullMoonInterval > 0) {
         this.fullMoonInterval -= i;
      }

      if (this.fullMoonInterval == 0) {
         this.fullMoonInterval--;
      }

      if (this.fullMoonInterval < 0) {
         this.fullMoonInterval = 0;
      }
   }

   public void setEclipseChance(int i) {
      this.eclipseChance -= i;
   }

   public void increaseEclipseChance(int i) {
      if (this.eclipseChance < 30) {
         this.eclipseChance += i;
      }

      if (this.eclipseChance == 1) {
         this.eclipseChance++;
      }

      if (this.eclipseChance > 30) {
         this.eclipseChance = 30;
      }
   }

   public void decreaseEclipseChance(int i) {
      if (this.eclipseChance > 1) {
         this.eclipseChance -= i;
      }

      if (this.eclipseChance == 1) {
         this.eclipseChance--;
      }

      if (this.eclipseChance < 1) {
         this.eclipseChance = 1;
      }
   }

   public void setEclipseDelay(int i) {
      this.eclipseDelay -= i;
   }

   public void increaseEclipseDelay(int i) {
      if (this.eclipseDelay < 50) {
         this.eclipseDelay += i;
      }

      if (this.eclipseDelay == 0) {
         this.eclipseDelay++;
      }

      if (this.eclipseDelay > 50) {
         this.eclipseDelay = 50;
      }
   }

   public void decreaseEclipseDelay(int i) {
      if (this.eclipseDelay > 0) {
         this.eclipseDelay -= i;
      }

      if (this.eclipseDelay == 0) {
         this.eclipseDelay--;
      }

      if (this.eclipseDelay < 0) {
         this.eclipseDelay = 0;
      }
   }

   public void setBloodMoonChance(int i) {
      this.bloodMoonChance -= i;
   }

   public void increaseBloodMoonChance(int i) {
      if (this.bloodMoonChance < 30) {
         this.bloodMoonChance += i;
      }

      if (this.bloodMoonChance == 1) {
         this.bloodMoonChance++;
      }

      if (this.bloodMoonChance > 30) {
         this.bloodMoonChance = 30;
      }
   }

   public void decreaseBloodMoonChance(int i) {
      if (this.bloodMoonChance > 1) {
         this.bloodMoonChance -= i;
      }

      if (this.bloodMoonChance == 1) {
         this.bloodMoonChance--;
      }

      if (this.bloodMoonChance < 1) {
         this.bloodMoonChance = 1;
      }
   }

   public void setBloodMoonDelay(int i) {
      this.bloodMoonDelay -= i;
   }

   public void increaseBloodMoonDelay(int i) {
      if (this.bloodMoonDelay < 50) {
         this.bloodMoonDelay += i;
      }

      if (this.bloodMoonDelay == 0) {
         this.bloodMoonDelay++;
      }

      if (this.bloodMoonDelay > 50) {
         this.bloodMoonDelay = 50;
      }
   }

   public void decreaseBloodMoonDelay(int i) {
      if (this.bloodMoonDelay > 0) {
         this.bloodMoonDelay -= i;
      }

      if (this.bloodMoonDelay == 0) {
         this.bloodMoonDelay--;
      }

      if (this.bloodMoonDelay < 0) {
         this.bloodMoonDelay = 0;
      }
   }

   public void toggleAllowBloodMoonSpring() {
      this.allowBloodMoonSpring = !this.allowBloodMoonSpring;
   }

   public void toggleAllowBloodMoonSummer() {
      this.allowBloodMoonSummer = !this.allowBloodMoonSummer;
   }

   public void toggleAllowBloodMoonAutumn() {
      this.allowBloodMoonAutumn = !this.allowBloodMoonAutumn;
   }

   public void toggleAllowBloodMoonWinter() {
      this.allowBloodMoonWinter = !this.allowBloodMoonWinter;
   }

   public void setAnimalSpawnRate(int i) {
      this.animalSpawnRate -= i;
   }

   public void increaseAnimalSpawnRate(int i) {
      if (this.animalSpawnRate < 20000) {
         this.animalSpawnRate += i;
      }

      if (this.animalSpawnRate > 20000) {
         this.animalSpawnRate = 20000;
      }
   }

   public void decreaseAnimalSpawnRate(int i) {
      if (this.animalSpawnRate > 0) {
         this.animalSpawnRate -= i;
      }

      if (this.animalSpawnRate < 0) {
         this.animalSpawnRate = 0;
      }
   }

   public static enum GameMode {
      NIGHTMARE("nightmare", Text.getText("gameMode.nightmare"), new GameModeTemplateNightmare()),
      SURVIVAL("survival", Text.getText("gameMode.survival"), new GameModeTemplateSurvival()),
      TRADITIONAL("traditional", Text.getText("gameMode.traditional"), new GameModeTemplateTraditional()),
      PEACEFUL("peaceful", Text.getText("gameMode.peaceful"), new GameModeTemplatePeaceful()),
      CUSTOM("custom", Text.getText("gameMode.custom"), new GameModeTemplateCustom()),
      SANDBOX("sandbox", Text.getText("gameMode.sandbox"), new GameModeTemplateSandbox()),
      MAIN_MENU("mainMenu", Text.getText("gameMode.mainMenu"), new GameModeTemplateMainMenu()),
      MAP_EDITOR("mapEditor", Text.getText("gameMode.mapEditor"), new GameModeTemplateMapEditor()),
      WORLD_MAP("worldMap", Text.getText("gameMode.worldMap"), new GameModeTemplateWorldMap());

      private final String folderName;
      private final String name;
      private final GameModeTemplateBase template;

      private GameMode(String fN, String n, GameModeTemplateBase t) {
         this.folderName = fN;
         this.name = n;
         this.template = t;
      }

      public String getFolderName() {
         return this.folderName;
      }

      public String getName() {
         return this.name;
      }

      public GameModeTemplateBase getTemplate() {
         return this.template;
      }
   }
}
