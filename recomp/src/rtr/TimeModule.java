package rtr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.newdawn.slick.SlickException;
import rtr.console.Console;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.particles.ParticleModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.Utilities;
import rtr.weather.disasters.DisasterBase;

public class TimeModule extends ModuleBase {
   private int day = 1;
   private int currentTimeTick;
   private TimeModule.TimeOfDay currentTime;
   private TimeModule.Season currentSeason;
   private int lastUpdateDay;
   private int fullMoonInterval;
   private boolean fullMoon;
   private boolean willBeFullMoon;
   private int eclipseChance;
   private int eclipseDelay;
   private boolean eclipse;
   private boolean willBeEclipse;
   private int bloodMoonChance;
   private int bloodMoonDelay;
   private boolean allowBloodMoonSpring;
   private boolean allowBloodMoonSummer;
   private boolean allowBloodMoonAutumn;
   private boolean allowBloodMoonWinter;
   private boolean bloodMoon;
   private boolean willBeBloodMoon;
   private int seasonLengthSpring;
   private int seasonLengthSummer;
   private int seasonLengthAutumn;
   private int seasonLengthWinter;
   private int seasonStart;
   private boolean freezeTime;
   private int worldMapDay = 1;
   public static boolean HALLOWEEN_MODE;

   static {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date date = new Date();
      HALLOWEEN_MODE = dateFormat.format(date).equals("2017-10-31");
   }

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.day = 1;
      this.currentTimeTick = 0;
      this.currentTime = null;
      this.currentSeason = null;
      this.lastUpdateDay = 0;
      this.fullMoonInterval = 7;
      this.fullMoon = false;
      this.willBeFullMoon = false;
      this.eclipseChance = 0;
      this.eclipseDelay = 0;
      this.eclipse = false;
      this.willBeEclipse = false;
      this.bloodMoonChance = 0;
      this.bloodMoonDelay = 0;
      this.allowBloodMoonSpring = false;
      this.allowBloodMoonSummer = false;
      this.allowBloodMoonAutumn = false;
      this.allowBloodMoonWinter = false;
      this.bloodMoon = false;
      this.willBeBloodMoon = false;
      this.seasonLengthSpring = 5;
      this.seasonLengthSummer = 5;
      this.seasonLengthAutumn = 5;
      this.seasonLengthWinter = 5;
      this.seasonStart = 0;
      this.freezeTime = false;
      this.worldMapDay = 1;
   }

   @Override
   protected void newMainMenu() throws SlickException {
      this.day = 1;
      this.currentTimeTick = 0;
      this.currentSeason = TimeModule.Season.SPRING;
      if (isHalloween()) {
         this.currentTime = TimeModule.TimeOfDay.NIGHT;
      } else {
         this.currentTime = TimeModule.TimeOfDay.MIDDAY;
         Calendar cal = Calendar.getInstance();
         this.setTimeOfDay(cal.get(11));
      }

      this.freezeTime = true;
   }

   @Override
   protected void newPlay() throws SlickException {
      GameModeTemplateBase template = this.gameMode.getTemplate();
      this.day = 1;
      this.currentTimeTick = 0;
      this.currentTime = TimeModule.TimeOfDay.DAWN;
      this.currentSeason = template.getSeasonStart();
      this.fullMoonInterval = template.getFullMoonInterval();
      this.eclipseChance = template.getEclipseChance();
      this.eclipseDelay = template.getEclipseDelay();
      this.bloodMoonChance = template.getBloodMoonChance();
      this.bloodMoonDelay = template.getBloodMoonDelay();
      this.allowBloodMoonSpring = template.allowBloodMoonSpring();
      this.allowBloodMoonSummer = template.allowBloodMoonSummer();
      this.allowBloodMoonAutumn = template.allowBloodMoonAutumn();
      this.allowBloodMoonWinter = template.allowBloodMoonWinter();
      this.seasonLengthSpring = template.getSeasonLengthSpring();
      this.seasonLengthSummer = template.getSeasonLengthSummer();
      this.seasonLengthAutumn = template.getSeasonLengthAutumn();
      this.seasonLengthWinter = template.getSeasonLengthWinter();
      this.seasonStart = 1;
      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP && !this.save.getActiveWorldSave().isNewGame()) {
         this.loadWorldSaveData();
      }
   }

   @Override
   protected void loadPlay() throws SlickException {
      GameModeTemplateBase template = this.gameMode.getTemplate();
      this.fullMoonInterval = template.getFullMoonInterval();
      this.eclipseChance = template.getEclipseChance();
      this.eclipseDelay = template.getEclipseDelay();
      this.bloodMoonChance = template.getBloodMoonChance();
      this.bloodMoonDelay = template.getBloodMoonDelay();
      this.allowBloodMoonSpring = template.allowBloodMoonSpring();
      this.allowBloodMoonSummer = template.allowBloodMoonSummer();
      this.allowBloodMoonAutumn = template.allowBloodMoonAutumn();
      this.allowBloodMoonWinter = template.allowBloodMoonWinter();
      this.seasonLengthSpring = template.getSeasonLengthSpring();
      this.seasonLengthSummer = template.getSeasonLengthSummer();
      this.seasonLengthAutumn = template.getSeasonLengthAutumn();
      this.seasonLengthWinter = template.getSeasonLengthWinter();
      this.loadRegionalSaveData();
      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP && !this.save.getActiveWorldSave().isNewGame()) {
         this.loadWorldSaveData();
      }
   }

   @Override
   protected void newMapEditor() throws SlickException {
      this.newPlay();
      this.currentTime = TimeModule.TimeOfDay.MIDDAY;
      this.currentSeason = TimeModule.Season.SPRING;
      this.freezeTime = true;
   }

   @Override
   protected void loadMapEditor() throws SlickException {
      this.newMapEditor();
   }

   @Override
   protected void newWorldMap() throws SlickException {
      if (SettingsParser.getLastGameMode() != null) {
         GameModeTemplateBase template = SettingsParser.getLastGameMode().getTemplate();
         this.seasonLengthSpring = template.getSeasonLengthSpring();
         this.seasonLengthSummer = template.getSeasonLengthSummer();
         this.seasonLengthAutumn = template.getSeasonLengthAutumn();
         this.seasonLengthWinter = template.getSeasonLengthWinter();
      }
   }

   @Override
   protected void loadWorldMap() throws SlickException {
      this.newWorldMap();
      this.loadWorldSaveData();
   }

   @Override
   protected void loadRegionalSaveData() {
      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(this.moduleType);
      this.day = Integer.parseInt(dataMap.getOrDefault("day", 1));
      this.currentTimeTick = Integer.parseInt(dataMap.getOrDefault("currentTimeTick", 0));
      String currentTimeString = dataMap.getOrDefault("currentTime", TimeModule.TimeOfDay.DAWN.toString());

      TimeModule.TimeOfDay[] var6;
      for (TimeModule.TimeOfDay t : TimeModule.TimeOfDay.values()) {
         if (t.toString().equals(currentTimeString)) {
            this.currentTime = t;
            break;
         }
      }

      String currentSeasonString = dataMap.getOrDefault("currentSeason", TimeModule.Season.SPRING.toString());

      TimeModule.Season[] var7;
      for (TimeModule.Season s : TimeModule.Season.values()) {
         if (s.toString().equals(currentSeasonString)) {
            this.currentSeason = s;
            break;
         }
      }

      this.lastUpdateDay = Integer.parseInt(dataMap.getOrDefault("lastUpdateDay", 0));
      this.fullMoon = Boolean.parseBoolean(dataMap.getOrDefault("fullMoon", false));
      this.willBeFullMoon = Boolean.parseBoolean(dataMap.getOrDefault("willBeFullMoon", false));
      this.eclipseDelay = Integer.parseInt(dataMap.getOrDefault("eclipseDelay", 0));
      this.eclipse = Boolean.parseBoolean(dataMap.getOrDefault("eclipse", false));
      this.willBeEclipse = Boolean.parseBoolean(dataMap.getOrDefault("willBeEclipse", false));
      this.bloodMoonDelay = Integer.parseInt(dataMap.getOrDefault("bloodMoonDelay", 0));
      this.bloodMoon = Boolean.parseBoolean(dataMap.getOrDefault("bloodMoon", false));
      this.willBeBloodMoon = Boolean.parseBoolean(dataMap.getOrDefault("willBeBloodMoon", false));
      this.seasonStart = Integer.parseInt(dataMap.getOrDefault("seasonStart", 0));
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
      if (this.save.getActiveWorldSave() != null) {
         YMLDataMap dataMap = this.save.getActiveWorldSave().getModuleSaveData(this.moduleType);
         this.worldMapDay = Integer.parseInt(dataMap.getOrDefault("worldMapDay", 1));
      }
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      if (this.day != 1) {
         dataMap.put("day", this.day);
      }

      if (this.currentTimeTick != 0) {
         dataMap.put("currentTimeTick", this.currentTimeTick);
      }

      dataMap.put("currentTime", this.currentTime.toString());
      dataMap.put("currentSeason", this.currentSeason.toString());
      if (this.lastUpdateDay != 0) {
         dataMap.put("lastUpdateDay", this.lastUpdateDay);
      }

      if (this.fullMoon) {
         dataMap.put("fullMoon", this.fullMoon);
      }

      if (this.willBeFullMoon) {
         dataMap.put("willBeFullMoon", this.willBeFullMoon);
      }

      if (this.eclipseDelay > 0) {
         dataMap.put("eclipseDelay", this.eclipseDelay);
      }

      if (this.eclipse) {
         dataMap.put("eclipse", this.eclipse);
      }

      if (this.willBeEclipse) {
         dataMap.put("willBeEclipse", this.willBeEclipse);
      }

      if (this.bloodMoonDelay > 0) {
         dataMap.put("bloodMoonDelay", this.bloodMoonDelay);
      }

      if (this.bloodMoon) {
         dataMap.put("bloodMoon", this.bloodMoon);
      }

      if (this.willBeBloodMoon) {
         dataMap.put("willBeBloodMoon", this.willBeBloodMoon);
      }

      if (this.seasonStart != 0) {
         dataMap.put("seasonStart", this.seasonStart);
      }

      return dataMap;
   }

   @Override
   public YMLDataMap getWorldMapSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      dataMap.put("worldMapDay", this.worldMapDay);
      return dataMap;
   }

   @Override
   public void resetWorldSaveData() throws SlickException {
      this.resetModule();
   }

   public void update() throws SlickException {
      if (!this.freezeTime && this.map.isMapStarted()) {
         this.currentTimeTick++;
         if (this.currentTimeTick >= this.getDayDuration()) {
            this.setTimeOfDayForward(true);
         }
      }

      if (this.lastUpdateDay != this.day) {
         this.updateMoonPhases();
         this.lastUpdateDay = this.day;
      }

      if (this.fullMoon) {
         for (int i = 0; i < 30; i++) {
            int randomX = Utilities.randomInt(4096);
            int randomY = Utilities.randomInt(4096);
            if (Utilities.randomInt(500) == 0) {
               this.essence.newEssence(randomX, randomY, 1, 1);
            }

            if (Utilities.randomInt(1500) == 0) {
               this.sound.playSound(SoundModule.SoundType.FULL_MOON_MAGIC, randomX, randomY);
            }

            randomX = (int)(Utilities.randomInt(4096) - this.map.getMapX());
            randomY = (int)(Utilities.randomInt(4096) - this.map.getMapY());
            int startX = this.map.getMapRenderTopLeftX() - 2;
            int startY = this.map.getMapRenderTopLeftY() - 2;
            int endX = this.map.getMapRenderBottomRightX() + 2;
            int endY = this.map.getMapRenderBottomRightY() + 2;
            if (randomX >= startX && randomX <= endX && randomY >= startY && randomY <= endY) {
               this.particle.newParticleGroup(randomX, randomY, ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS, ParticleModule.ParticleSet.MAGIC_BLUE);
               this.particle.newParticleGroup(randomX, randomY, ParticleModule.ParticleGroup.FLOATING_MAGIC_SQUARES, ParticleModule.ParticleSet.MAGIC_BLUE);
               if (Utilities.randomInt(5) == 0) {
                  for (int i2 = 0; i2 < 10; i2++) {
                     this.particle.newParticleGroup(randomX, randomY, 260.0F, ParticleModule.ParticleGroup.MAGIC_FALL, ParticleModule.ParticleSet.MAGIC_BLUE);
                  }
               }
            }
         }
      }

      if (this.bloodMoon) {
         for (int i = 0; i < 30; i++) {
            int randomXx = Utilities.randomInt(4096);
            int randomYx = Utilities.randomInt(4096);
            if (Utilities.randomInt(1500) == 0) {
               this.essence.newEssence(randomXx, randomYx, 1, 1);
               this.sound.playSound(SoundModule.SoundType.FULL_MOON_MAGIC, randomXx, randomYx);
            }

            randomXx = (int)(Utilities.randomInt(4096) - this.map.getMapX());
            randomYx = (int)(Utilities.randomInt(4096) - this.map.getMapY());
         }
      }
   }

   private void updateMoonPhases() throws SlickException {
      this.willBeFullMoon = this.fullMoonInterval != 0 && this.day % this.fullMoonInterval == 0;
      if (this.willBeFullMoon
         || this.bloodMoonDelay > 0
         || Utilities.randomInt(this.bloodMoonChance) != 0
         || (this.currentSeason != TimeModule.Season.SPRING || !this.allowBloodMoonSpring)
            && (this.currentSeason != TimeModule.Season.SUMMER || !this.allowBloodMoonSummer)
            && (this.currentSeason != TimeModule.Season.AUTUMN || !this.allowBloodMoonAutumn)
            && (this.currentSeason != TimeModule.Season.WINTER || !this.allowBloodMoonWinter)) {
         this.willBeBloodMoon = false;
      } else {
         this.willBeBloodMoon = true;
         this.bloodMoonDelay = this.gameMode.getTemplate().getBloodMoonDelay();
      }

      if (!this.willBeFullMoon && !this.willBeBloodMoon && this.eclipseDelay <= 0 && Utilities.randomInt(this.eclipseChance) == 0) {
         this.willBeEclipse = true;
         this.eclipseDelay = this.gameMode.getTemplate().getEclipseDelay();
      } else {
         this.willBeEclipse = false;
      }
   }

   public void setTimeOfDayForward(boolean changeDay) throws SlickException {
      if (this.currentTime.ordinal() != TimeModule.TimeOfDay.values().length - 1) {
         this.currentTime = TimeModule.TimeOfDay.values()[this.currentTime.ordinal() + 1];
      } else {
         this.currentTime = TimeModule.TimeOfDay.values()[0];
      }

      this.runTimeChange(changeDay);
   }

   public void setTimeOfDayBack(boolean changeDay) throws SlickException {
      if (this.currentTime.ordinal() != 0) {
         this.currentTime = TimeModule.TimeOfDay.values()[this.currentTime.ordinal() - 1];
      } else {
         this.currentTime = TimeModule.TimeOfDay.values()[TimeModule.TimeOfDay.values().length - 1];
      }

      this.runTimeChange(changeDay);
   }

   private void runTimeChange(boolean changeDay) throws SlickException {
      if (this.currentTime == TimeModule.TimeOfDay.NIGHT) {
         this.fullMoon = this.willBeFullMoon;
         this.bloodMoon = this.willBeBloodMoon;
         if (this.bloodMoon) {
            this.weather.forceNewDisaster(DisasterBase.DisasterType.BLOOD_RAIN, false);
         }
      } else {
         this.fullMoon = false;
         this.bloodMoon = false;
      }

      if (this.currentTime == TimeModule.TimeOfDay.MIDDAY) {
         this.eclipse = this.willBeEclipse;
         if (this.eclipse) {
            this.weather.forceNewDisaster(DisasterBase.DisasterType.ECLIPSE, false);
         }
      } else {
         this.eclipse = false;
      }

      if (this.currentTime == TimeModule.TimeOfDay.DAWN) {
         if (this.gameMode != GameModeTemplateBase.GameMode.MAP_EDITOR) {
            if (this.gameMode != GameModeTemplateBase.GameMode.SANDBOX) {
               this.profile.addDaySurvived();
            }

            if (changeDay) {
               this.setDayForward();
               int seasonLength = 0;
               switch (this.currentSeason) {
                  case WINTER:
                     seasonLength = this.seasonLengthWinter;
                     break;
                  case SPRING:
                     seasonLength = this.seasonLengthSpring;
                     break;
                  case SUMMER:
                     seasonLength = this.seasonLengthSummer;
                     break;
                  case AUTUMN:
                     seasonLength = this.seasonLengthAutumn;
               }

               if (this.day >= this.seasonStart + seasonLength) {
                  this.seasonForward();
               }

               String thisSeason = "";
               String nextSeason = "";
               String monsters = "";
               if (this.spawn.getSpawnDelay() - this.day == 1) {
                  monsters = " " + Text.getText("console.banner.time.newDayBanner.monstersArriveTomorrow");
               } else if (this.spawn.getSpawnDelay() - this.day > 1) {
                  Text.setVariableText("days", this.spawn.getSpawnDelay() - this.day);
                  monsters = " " + Text.getText("console.banner.time.newDayBanner.daysUntilMonsters");
               }

               if (this.currentSeason == TimeModule.Season.WINTER) {
                  thisSeason = "$BLU1" + TimeModule.Season.WINTER.getText() + "$WHI0";
                  nextSeason = "$GRE1" + TimeModule.Season.SPRING.getText() + "$WHI0";
               } else if (this.currentSeason == TimeModule.Season.SPRING) {
                  thisSeason = "$GRE1" + TimeModule.Season.SPRING.getText() + "$WHI0";
                  nextSeason = "$YEL1" + TimeModule.Season.SUMMER.getText() + "$WHI0";
               } else if (this.currentSeason == TimeModule.Season.SUMMER) {
                  thisSeason = "$YEL1" + TimeModule.Season.SUMMER.getText() + "$WHI0";
                  nextSeason = "$ORA1" + TimeModule.Season.AUTUMN.getText() + "$WHI0";
               } else if (this.currentSeason == TimeModule.Season.AUTUMN) {
                  thisSeason = "$ORA1" + TimeModule.Season.AUTUMN.getText() + "$WHI0";
                  nextSeason = "$BLU1" + TimeModule.Season.WINTER.getText() + "$WHI0";
               }

               seasonLength = 0;
               switch (this.currentSeason) {
                  case WINTER:
                     seasonLength = this.seasonLengthWinter;
                     break;
                  case SPRING:
                     seasonLength = this.seasonLengthSpring;
                     break;
                  case SUMMER:
                     seasonLength = this.seasonLengthSummer;
                     break;
                  case AUTUMN:
                     seasonLength = this.seasonLengthAutumn;
               }

               int daysUntilInt = this.seasonStart - this.day + seasonLength;
               String daysUntil = "";
               if (daysUntilInt == 1) {
                  Text.setVariableText("season", nextSeason);
                  daysUntil = Text.getText("console.banner.time.newDayBanner.nextSeasonArrivesTomorrow");
               } else {
                  Text.setVariableText("season", nextSeason);
                  Text.setVariableText("days", daysUntilInt);
                  daysUntil = Text.getText("console.banner.time.newDayBanner.daysUntilNextSeason");
               }

               Text.setVariableText("season", thisSeason);
               Text.setVariableText("currentDay", this.time.getDay());
               Console.newBanner(Text.getText("console.banner.time.newDayBanner.newDayBannerHeader"), daysUntil + monsters);
               this.sound.playSound(SoundModule.SoundType.DAWN_ARRIVED);
            }
         }
      } else if (this.currentTime == TimeModule.TimeOfDay.MORNING) {
         if (this.willBeEclipse) {
            Console.newBanner(Text.getText("console.banner.time.feelsOminous.header.yellow"), Text.getText("console.banner.time.feelsOminous.subtitle"));
            this.sound.playSound(SoundModule.SoundType.BLOOD_MOON);
         }
      } else if (this.currentTime == TimeModule.TimeOfDay.MIDDAY) {
         if (this.eclipse) {
            Console.newBanner(Text.getText("console.banner.time.anEclipse.header"), Text.getText("console.banner.time.anEclipse.subtitle"));
            this.sound.playSound(SoundModule.SoundType.BLOOD_MOON);
            this.goal.incrementGoal(GoalModule.GoalType.WHO_TURNED_OFF_THE_SUN);
         }
      } else if (this.currentTime == TimeModule.TimeOfDay.DUSK) {
         if (this.day < this.spawn.getSpawnDelay() || this.spawn.getSpawnRate() == 0) {
            Console.newBanner(Text.getText("console.banner.time.duskArrived.header"), Text.getText("console.banner.time.duskArrived.subtitle.alwaysPeaceful"));
            this.sound.playSound(SoundModule.SoundType.NIGHT_ARRIVED);
         } else if (this.willBeFullMoon) {
            Console.newBanner(Text.getText("console.banner.time.duskArrived.header"), Text.getText("console.banner.time.duskArrived.subtitle.peaceful"));
            this.sound.playSound(SoundModule.SoundType.FULL_MOON);
         } else if (this.willBeBloodMoon) {
            Console.newBanner(Text.getText("console.banner.time.feelsOminous.header.red"), Text.getText("console.banner.time.feelsOminous.subtitle"));
            this.sound.playSound(SoundModule.SoundType.BLOOD_MOON);
         } else {
            Console.newBanner(Text.getText("console.banner.time.duskArrived.header"), Text.getText("console.banner.time.duskArrived.subtitle.restless"));
            this.sound.playSound(SoundModule.SoundType.NIGHT_ARRIVED);
         }
      } else if (this.currentTime == TimeModule.TimeOfDay.NIGHT) {
         if (this.day < this.spawn.getSpawnDelay() || this.spawn.getSpawnRate() == 0) {
            Console.newBanner(Text.getText("console.banner.time.nightArrived.header.blue"), Text.getText("console.banner.time.nightArrived.subtitle.calm"));
            this.sound.playSound(SoundModule.SoundType.NIGHT_ARRIVED);
         } else if (this.fullMoon) {
            Console.newBanner(Text.getText("console.banner.time.nightArrived.header.green"), Text.getText("console.banner.time.nightArrived.subtitle.fullMoon"));
            this.sound.playSound(SoundModule.SoundType.FULL_MOON);
            this.goal.incrementGoal(GoalModule.GoalType.CHILL);
         } else if (this.bloodMoon) {
            Console.newBanner(Text.getText("console.banner.time.bloodMoon.header"), Text.getText("console.banner.time.bloodMoon.subtitle"));
            this.sound.playSound(SoundModule.SoundType.BLOOD_MOON);
            this.goal.incrementGoal(GoalModule.GoalType.THE_RED_SUN_RISES);
         } else {
            Console.newBanner(Text.getText("console.banner.time.nightArrived.header.blue"), Text.getText("console.banner.time.nightArrived.subtitle.enraged"));
            this.sound.playSound(SoundModule.SoundType.NIGHT_ARRIVED);
         }
      }

      this.currentTimeTick = 0;
   }

   public TimeModule.TimeOfDay getTimeOfDay() {
      return this.currentTime;
   }

   public int getTimeOfDayTick() {
      return this.currentTimeTick;
   }

   private void setTimeOfDay(int tODH) {
      if (tODH >= 20 || tODH <= 4) {
         this.currentTime = TimeModule.TimeOfDay.NIGHT;
      } else if (tODH >= 19) {
         this.currentTime = TimeModule.TimeOfDay.DUSK;
      } else if (tODH >= 16) {
         this.currentTime = TimeModule.TimeOfDay.EVENING;
      } else if (tODH >= 10) {
         this.currentTime = TimeModule.TimeOfDay.MIDDAY;
      } else if (tODH >= 7) {
         this.currentTime = TimeModule.TimeOfDay.MORNING;
      } else if (tODH >= 5) {
         this.currentTime = TimeModule.TimeOfDay.DAWN;
      }

      this.currentTimeTick = 0;
   }

   public void setTimeOfDay(TimeModule.TimeOfDay tOD) {
      this.currentTime = tOD;
      this.currentTimeTick = 0;
   }

   private void seasonForward() {
      String thisSeason = "";
      if (this.currentSeason == TimeModule.Season.WINTER) {
         thisSeason = "$GRE1" + TimeModule.Season.SPRING.getText() + "$WHI0";
         this.currentSeason = TimeModule.Season.SPRING;
      } else if (this.currentSeason == TimeModule.Season.SPRING) {
         thisSeason = "$YEL1" + TimeModule.Season.SUMMER.getText() + "$WHI0";
         this.currentSeason = TimeModule.Season.SUMMER;
      } else if (this.currentSeason == TimeModule.Season.SUMMER) {
         thisSeason = "$ORA1" + TimeModule.Season.AUTUMN.getText() + "$WHI0";
         this.currentSeason = TimeModule.Season.AUTUMN;
      } else if (this.currentSeason == TimeModule.Season.AUTUMN) {
         thisSeason = "$BLU1" + TimeModule.Season.WINTER.getText() + "$WHI0";
         this.currentSeason = TimeModule.Season.WINTER;
      }

      this.seasonStart = this.day;
      Text.setVariableText("season", thisSeason);
      Console.newBanner(Text.getText("console.banner.time.newSeason.header"), Text.getText("console.banner.time.newSeason.subtitle"));
      this.currentTimeTick = 0;
   }

   private void seasonBack() {
      String thisSeason = "";
      if (this.currentSeason == TimeModule.Season.WINTER) {
         thisSeason = "$ORA1" + TimeModule.Season.AUTUMN.getText() + "$WHI0";
         this.currentSeason = TimeModule.Season.AUTUMN;
      } else if (this.currentSeason == TimeModule.Season.AUTUMN) {
         thisSeason = "$YEL1" + TimeModule.Season.SUMMER.getText() + "$WHI0";
         this.currentSeason = TimeModule.Season.SUMMER;
      } else if (this.currentSeason == TimeModule.Season.SUMMER) {
         thisSeason = "$GRE1" + TimeModule.Season.SPRING.getText() + "$WHI0";
         this.currentSeason = TimeModule.Season.SPRING;
      } else if (this.currentSeason == TimeModule.Season.SPRING) {
         thisSeason = "$BLU1" + TimeModule.Season.WINTER.getText() + "$WHI0";
         this.currentSeason = TimeModule.Season.WINTER;
      }

      this.seasonStart = this.day;
      Text.setVariableText("season", thisSeason);
      Console.newBanner(Text.getText("console.banner.time.newSeason.header"), Text.getText("console.banner.time.newSeason.subtitle"));
      this.currentTimeTick = 0;
   }

   public TimeModule.Season getSeason() {
      return this.currentSeason;
   }

   public void setSeason(TimeModule.Season s) {
      this.currentSeason = s;
   }

   public void setSeasonForward() {
      this.seasonForward();
   }

   public void setSeasonBack() {
      this.seasonBack();
   }

   public int getDay() {
      return this.day;
   }

   public void setDayForward() {
      this.day++;
      this.bloodMoonDelay--;
      this.eclipseDelay--;
      if (this.save.getActiveRegionalSave().getGameType() != StateBase.GameType.SKIRMISH) {
         this.worldMapDay++;
      }

      this.goal.incrementGoal(GoalModule.GoalType.SURVIVE);
   }

   public void setDayBack() {
      if (this.day > 1) {
         this.day--;
         this.bloodMoonDelay++;
         this.eclipseDelay++;
         if (this.save.getActiveRegionalSave().getGameType() != StateBase.GameType.SKIRMISH) {
            this.worldMapDay--;
         }
      }
   }

   public int getYearByDay(int i) {
      return (i - 1) / (this.seasonLengthSpring + this.seasonLengthSummer + this.seasonLengthAutumn + this.seasonLengthWinter) + 1;
   }

   public int getYear() {
      return (this.day - 1) / (this.seasonLengthSpring + this.seasonLengthSummer + this.seasonLengthAutumn + this.seasonLengthWinter) + 1;
   }

   public int getWorldMapDay() {
      return this.worldMapDay;
   }

   public void toggleFreezeTime() {
      this.freezeTime = !this.freezeTime;
   }

   public boolean getFreezeTime() {
      return this.freezeTime;
   }

   public boolean isFullMoon() {
      return this.fullMoon;
   }

   public boolean isEclipse() {
      return this.eclipse;
   }

   public boolean isBloodMoon() {
      return this.bloodMoon;
   }

   public boolean isFullMoonDay() {
      return this.fullMoon || this.willBeFullMoon;
   }

   public boolean isEclipseDay() {
      return this.eclipse || this.willBeEclipse;
   }

   public boolean isBloodMoonDay() {
      return this.bloodMoon || this.willBeBloodMoon;
   }

   public int getDayDuration() {
      return this.currentTime.getDuration(this.currentSeason);
   }

   public int getFullDayLength() {
      return TimeModule.TimeOfDay.DAWN.getDuration(TimeModule.Season.SPRING)
         + TimeModule.TimeOfDay.MORNING.getDuration(TimeModule.Season.SPRING)
         + TimeModule.TimeOfDay.MIDDAY.getDuration(TimeModule.Season.SPRING)
         + TimeModule.TimeOfDay.EVENING.getDuration(TimeModule.Season.SPRING)
         + TimeModule.TimeOfDay.DUSK.getDuration(TimeModule.Season.SPRING)
         + TimeModule.TimeOfDay.NIGHT.getDuration(TimeModule.Season.SPRING);
   }

   public static boolean isHalloween() {
      return HALLOWEEN_MODE;
   }

   public static enum Season {
      WINTER(Text.getText("season.winter")),
      SPRING(Text.getText("season.spring")),
      SUMMER(Text.getText("season.summer")),
      AUTUMN(Text.getText("season.autumn"));

      private final String seasonText;

      private Season(String seasonText) {
         this.seasonText = seasonText;
      }

      public String getText() {
         return this.seasonText;
      }
   }

   public static enum TimeOfDay {
      NIGHT(Text.getText("timeOfDay.night"), 36000, 30000, 36000, 40000),
      DAWN(Text.getText("timeOfDay.dawn"), 10000, 10000, 10000, 10000),
      MORNING(Text.getText("timeOfDay.morning"), 20000, 20000, 20000, 20000),
      MIDDAY(Text.getText("timeOfDay.midday"), 36000, 42000, 36000, 32000),
      EVENING(Text.getText("timeOfDay.evening"), 20000, 20000, 20000, 20000),
      DUSK(Text.getText("timeOfDay.dusk"), 10000, 10000, 10000, 10000);

      private final String timeOfDayText;
      private final int durationSpring;
      private final int durationSummer;
      private final int durationAutumn;
      private final int durationWinter;

      private TimeOfDay(String timeOfDayText, int durationSpring, int durationSummer, int durationAutumn, int durationWinter) {
         this.timeOfDayText = timeOfDayText;
         this.durationSpring = durationSpring;
         this.durationSummer = durationSummer;
         this.durationAutumn = durationAutumn;
         this.durationWinter = durationWinter;
      }

      public int getDuration(TimeModule.Season s) {
         switch (s) {
            case WINTER:
               return this.durationWinter;
            case SPRING:
               return this.durationSpring;
            case SUMMER:
               return this.durationSummer;
            case AUTUMN:
               return this.durationAutumn;
            default:
               return this.durationSpring;
         }
      }

      public String getText() {
         return this.timeOfDayText;
      }
   }
}
