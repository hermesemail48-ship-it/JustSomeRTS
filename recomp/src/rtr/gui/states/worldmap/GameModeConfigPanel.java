package rtr.gui.states.worldmap;

import java.io.File;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.TimeModule;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonInputText;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.buttons.GUIPanelSubButton;
import rtr.gui.buttons.GUIRoundButton;
import rtr.gui.buttons.GUISquarePanelButton;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.gui.states.WorldMapGUIPanelBase;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class GameModeConfigPanel extends WorldMapGUIPanelBase {
   private Image panel;
   private Rectangle mask;
   private GUIPanelSubButton baseSpawnRateLeft;
   private GUIPanelSubButton baseSpawnRateRight;
   private GUIClickableText baseSpawnRate;
   private GUIPanelSubButton boostedMonsterChanceMultiplierLeft;
   private GUIPanelSubButton boostedMonsterChanceMultiplierRight;
   private GUIClickableText boostedMonsterChanceMultiplier;
   private GUIPanelSubButton corruptionStartDayLeft;
   private GUIPanelSubButton corruptionStartDayRight;
   private GUIClickableText corruptionStartDay;
   private GUIPanelSubButton corruptionDesiredAmountStartLeft;
   private GUIPanelSubButton corruptionDesiredAmountStartRight;
   private GUIClickableText corruptionDesiredAmountStart;
   private GUIPanelSubButton corruptionDesiredAmountPerDayLeft;
   private GUIPanelSubButton corruptionDesiredAmountPerDayRight;
   private GUIClickableText corruptionDesiredAmountPerDay;
   private GUIPanelSubButton corruptionDesiredAmountPerBuildingLeft;
   private GUIPanelSubButton corruptionDesiredAmountPerBuildingRight;
   private GUIClickableText corruptionDesiredAmountPerBuilding;
   private GUIPanelSubButton corruptionBuildingRateLeft;
   private GUIPanelSubButton corruptionBuildingRateRight;
   private GUIClickableText corruptionBuildingRate;
   private GUIPanelSubButton corruptionBuildingDensityLeft;
   private GUIPanelSubButton corruptionBuildingDensityRight;
   private GUIClickableText corruptionBuildingDensity;
   private GUIPanelSubButton corruptionBuildingDensityRangeLeft;
   private GUIPanelSubButton corruptionBuildingDensityRangeRight;
   private GUIClickableText corruptionBuildingDensityRange;
   private GUIPanelSubButton maximumLootBoxesLeft;
   private GUIPanelSubButton maximumLootBoxesRight;
   private GUIClickableText maximumLootBoxes;
   private GUIPanelSubButton lootBoxUncoverChanceLeft;
   private GUIPanelSubButton lootBoxUncoverChanceRight;
   private GUIClickableText lootBoxUncoverChance;
   private GUIPanelSubButton lootBoxKeyChanceLeft;
   private GUIPanelSubButton lootBoxKeyChanceRight;
   private GUIClickableText lootBoxKeyChance;
   private GUIPanelSubButton baseNomadSpawnRateRight;
   private GUIPanelSubButton baseNomadSpawnRateLeft;
   private GUIClickableText baseNomadSpawnRate;
   private GUIPanelSubButton nomadSpawnAmountMultiplierRight;
   private GUIPanelSubButton nomadSpawnAmountMultiplierLeft;
   private GUIClickableText nomadSpawnAmountMultiplier;
   private GUIPanelSubButton nomadSpawnAmountMinimumRight;
   private GUIPanelSubButton nomadSpawnAmountMinimumLeft;
   private GUIClickableText nomadSpawnAmountMinimum;
   private GUIPanelSubButton nomadSpawnAmountMaximumRight;
   private GUIPanelSubButton nomadSpawnAmountMaximumLeft;
   private GUIClickableText nomadSpawnAmountMaximum;
   private GUIPanelSubButton temperatureVarianceLeft;
   private GUIPanelSubButton temperatureVarianceRight;
   private GUIClickableText temperatureVariance;
   private GUIPanelSubButton startingSeasonLeft;
   private GUIPanelSubButton startingSeasonRight;
   private GUIClickableText startingSeason;
   private GUIPanelSubButton weatherChanceSpringLeft;
   private GUIPanelSubButton weatherChanceSpringRight;
   private GUIClickableText weatherChanceSpring;
   private GUIPanelSubButton averageTemperatureSpringLeft;
   private GUIPanelSubButton averageTemperatureSpringRight;
   private GUIClickableText averageTemperatureSpring;
   private GUIPanelSubButton seasonLengthSpringLeft;
   private GUIPanelSubButton seasonLengthSpringRight;
   private GUIClickableText seasonLengthSpring;
   private GUIPanelSubButton weatherChanceSummerLeft;
   private GUIPanelSubButton weatherChanceSummerRight;
   private GUIClickableText weatherChanceSummer;
   private GUIPanelSubButton averageTemperatureSummerLeft;
   private GUIPanelSubButton averageTemperatureSummerRight;
   private GUIClickableText averageTemperatureSummer;
   private GUIPanelSubButton seasonLengthSummerLeft;
   private GUIPanelSubButton seasonLengthSummerRight;
   private GUIClickableText seasonLengthSummer;
   private GUIPanelSubButton weatherChanceAutumnLeft;
   private GUIPanelSubButton weatherChanceAutumnRight;
   private GUIClickableText weatherChanceAutumn;
   private GUIPanelSubButton averageTemperatureAutumnLeft;
   private GUIPanelSubButton averageTemperatureAutumnRight;
   private GUIClickableText averageTemperatureAutumn;
   private GUIPanelSubButton seasonLengthAutumnLeft;
   private GUIPanelSubButton seasonLengthAutumnRight;
   private GUIClickableText seasonLengthAutumn;
   private GUIPanelSubButton weatherChanceWinterLeft;
   private GUIPanelSubButton weatherChanceWinterRight;
   private GUIClickableText weatherChanceWinter;
   private GUIPanelSubButton averageTemperatureWinterLeft;
   private GUIPanelSubButton averageTemperatureWinterRight;
   private GUIClickableText averageTemperatureWinter;
   private GUIPanelSubButton seasonLengthWinterLeft;
   private GUIPanelSubButton seasonLengthWinterRight;
   private GUIClickableText seasonLengthWinter;
   private GUIPanelSubButton fullMoonIntervalLeft;
   private GUIPanelSubButton fullMoonIntervalRight;
   private GUIClickableText fullMoonInterval;
   private GUIPanelSubButton bloodMoonChanceLeft;
   private GUIPanelSubButton bloodMoonChanceRight;
   private GUIClickableText bloodMoonChance;
   private GUIPanelSubButton bloodMoonDelayLeft;
   private GUIPanelSubButton bloodMoonDelayRight;
   private GUIClickableText bloodMoonDelay;
   private GUIPanelSubButton allowBloodMoonSpringLeft;
   private GUIPanelSubButton allowBloodMoonSpringRight;
   private GUIClickableText allowBloodMoonSpring;
   private GUIPanelSubButton allowBloodMoonSummerLeft;
   private GUIPanelSubButton allowBloodMoonSummerRight;
   private GUIClickableText allowBloodMoonSummer;
   private GUIPanelSubButton allowBloodMoonAutumnLeft;
   private GUIPanelSubButton allowBloodMoonAutumnRight;
   private GUIClickableText allowBloodMoonAutumn;
   private GUIPanelSubButton allowBloodMoonWinterLeft;
   private GUIPanelSubButton allowBloodMoonWinterRight;
   private GUIClickableText allowBloodMoonWinter;
   private GUIPanelSubButton meteorShowerChanceLeft;
   private GUIPanelSubButton meteorShowerChanceRight;
   private GUIClickableText meteorShowerChance;
   private GUIPanelSubButton earthquakeChanceLeft;
   private GUIPanelSubButton earthquakeChanceRight;
   private GUIClickableText earthquakeChance;
   private GUIPanelSubButton electricalStormChanceLeft;
   private GUIPanelSubButton electricalStormChanceRight;
   private GUIClickableText electricalStormChance;
   private GUIPanelSubButton hailStormChanceLeft;
   private GUIPanelSubButton hailStormChanceRight;
   private GUIClickableText hailStormChance;
   private GUIPanelSubButton blightChanceLeft;
   private GUIPanelSubButton blightChanceRight;
   private GUIClickableText blightChance;
   private GUIClickableText resetNightmare;
   private GUIClickableText resetSurvival;
   private GUIClickableText resetTraditional;
   private GUIClickableText resetPeaceful;
   private String[] customListNames = new String[16];
   private GUIRoundButton[] customListDelete = new GUIRoundButton[16];
   private GUIClickableText[] customList = new GUIClickableText[16];
   private GUISquarePanelButton save;
   private GUIButtonInputText saveInput;
   private GUIButtonMainMenu accept;
   private GUIButtonMainMenu back;

   public GameModeConfigPanel(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/gameState/gameModeConfigPanel.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);
      this.baseSpawnRateLeft = new GUIPanelSubButton("baseSpawnRateLeft", "left");
      this.guiButtons.add(this.baseSpawnRateLeft);
      this.baseSpawnRateRight = new GUIPanelSubButton("baseSpawnRateRight", "right");
      this.guiButtons.add(this.baseSpawnRateRight);
      this.baseSpawnRate = new GUIClickableText("baseSpawnRate", 1);
      this.baseSpawnRate
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.baseSpawnRateHeader"),
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.baseSpawnRate")
         );
      this.guiButtons.add(this.baseSpawnRate);
      this.boostedMonsterChanceMultiplierLeft = new GUIPanelSubButton("boostedMonsterChanceMultiplierLeft", "left");
      this.guiButtons.add(this.boostedMonsterChanceMultiplierLeft);
      this.boostedMonsterChanceMultiplierRight = new GUIPanelSubButton("boostedMonsterChanceMultiplierRight", "right");
      this.guiButtons.add(this.boostedMonsterChanceMultiplierRight);
      this.boostedMonsterChanceMultiplier = new GUIClickableText("boostedMonsterChanceMultiplier", 1);
      this.boostedMonsterChanceMultiplier
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.boostedChanceMultiplierHeader"),
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.boostedChanceMultiplier")
         );
      this.guiButtons.add(this.boostedMonsterChanceMultiplier);
      this.corruptionStartDayLeft = new GUIPanelSubButton("corruptionStartDayLeft", "left");
      this.guiButtons.add(this.corruptionStartDayLeft);
      this.corruptionStartDayRight = new GUIPanelSubButton("corruptionStartDayRight", "right");
      this.guiButtons.add(this.corruptionStartDayRight);
      this.corruptionStartDay = new GUIClickableText("corruptionStartDay", 1);
      this.corruptionStartDay
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.corruptionStartDayHeader"),
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.corruptionStartDay")
         );
      this.guiButtons.add(this.corruptionStartDay);
      this.corruptionDesiredAmountStartLeft = new GUIPanelSubButton("corruptionDesiredAmountStartLeft", "left");
      this.guiButtons.add(this.corruptionDesiredAmountStartLeft);
      this.corruptionDesiredAmountStartRight = new GUIPanelSubButton("corruptionDesiredAmountStartRight", "right");
      this.guiButtons.add(this.corruptionDesiredAmountStartRight);
      this.corruptionDesiredAmountStart = new GUIClickableText("corruptionDesiredAmountStart", 1);
      this.corruptionDesiredAmountStart
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.corruptionDesiredAmountStartHeader"),
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.corruptionDesiredAmountStart")
         );
      this.guiButtons.add(this.corruptionDesiredAmountStart);
      this.corruptionDesiredAmountPerDayLeft = new GUIPanelSubButton("corruptionDesiredAmountPerDayLeft", "left");
      this.guiButtons.add(this.corruptionDesiredAmountPerDayLeft);
      this.corruptionDesiredAmountPerDayRight = new GUIPanelSubButton("corruptionDesiredAmountPerDayRight", "right");
      this.guiButtons.add(this.corruptionDesiredAmountPerDayRight);
      this.corruptionDesiredAmountPerDay = new GUIClickableText("corruptionDesiredAmountPerDay", 1);
      this.corruptionDesiredAmountPerDay
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.corruptionDesiredAmountPerDayHeader"),
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.corruptionDesiredAmountPerDay")
         );
      this.guiButtons.add(this.corruptionDesiredAmountPerDay);
      this.corruptionDesiredAmountPerBuildingLeft = new GUIPanelSubButton("corruptionDesiredAmountPerBuildingLeft", "left");
      this.guiButtons.add(this.corruptionDesiredAmountPerBuildingLeft);
      this.corruptionDesiredAmountPerBuildingRight = new GUIPanelSubButton("corruptionDesiredAmountPerBuildingRight", "right");
      this.guiButtons.add(this.corruptionDesiredAmountPerBuildingRight);
      this.corruptionDesiredAmountPerBuilding = new GUIClickableText("corruptionDesiredAmountPerBuilding", 1);
      this.corruptionDesiredAmountPerBuilding
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.corruptionDesiredAmountPerBuildingHeader"),
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.corruptionDesiredAmountPerBuilding")
         );
      this.guiButtons.add(this.corruptionDesiredAmountPerBuilding);
      this.corruptionBuildingRateLeft = new GUIPanelSubButton("corruptionBuildingRateLeft", "left");
      this.guiButtons.add(this.corruptionBuildingRateLeft);
      this.corruptionBuildingRateRight = new GUIPanelSubButton("corruptionBuildingRateRight", "right");
      this.guiButtons.add(this.corruptionBuildingRateRight);
      this.corruptionBuildingRate = new GUIClickableText("corruptionBuildingRate", 1);
      this.corruptionBuildingRate
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.buildingRateHeader"),
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.buildingRate")
         );
      this.guiButtons.add(this.corruptionBuildingRate);
      this.corruptionBuildingDensityLeft = new GUIPanelSubButton("corruptionBuildingDensityLeft", "left");
      this.guiButtons.add(this.corruptionBuildingDensityLeft);
      this.corruptionBuildingDensityRight = new GUIPanelSubButton("corruptionBuildingDensityRight", "right");
      this.guiButtons.add(this.corruptionBuildingDensityRight);
      this.corruptionBuildingDensity = new GUIClickableText("corruptionBuildingDensity", 1);
      this.corruptionBuildingDensity
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.buildingDensityHeader"),
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.buildingDensity")
         );
      this.guiButtons.add(this.corruptionBuildingDensity);
      this.corruptionBuildingDensityRangeLeft = new GUIPanelSubButton("corruptionBuildingDensityRangeLeft", "left");
      this.guiButtons.add(this.corruptionBuildingDensityRangeLeft);
      this.corruptionBuildingDensityRangeRight = new GUIPanelSubButton("corruptionBuildingDensityRangeRight", "right");
      this.guiButtons.add(this.corruptionBuildingDensityRangeRight);
      this.corruptionBuildingDensityRange = new GUIClickableText("corruptionBuildingDensityRange", 1);
      this.corruptionBuildingDensityRange
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.buildingDensityRangeHeader"),
            Text.getText("worldMapGameModeConfigPanel.corruption.tooltip.buildingDensityRange")
         );
      this.guiButtons.add(this.corruptionBuildingDensityRange);
      this.maximumLootBoxesLeft = new GUIPanelSubButton("maximumLootBoxesLeft", "left");
      this.guiButtons.add(this.maximumLootBoxesLeft);
      this.maximumLootBoxesRight = new GUIPanelSubButton("maximumLootBoxesRight", "right");
      this.guiButtons.add(this.maximumLootBoxesRight);
      this.maximumLootBoxes = new GUIClickableText("maximumLootBoxes", 1);
      this.maximumLootBoxes
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.lootBoxes.tooltip.maximumLootBoxesHeader"),
            Text.getText("worldMapGameModeConfigPanel.lootBoxes.tooltip.maximumLootBoxes")
         );
      this.guiButtons.add(this.maximumLootBoxes);
      this.lootBoxUncoverChanceLeft = new GUIPanelSubButton("lootBoxUncoverChanceLeft", "left");
      this.guiButtons.add(this.lootBoxUncoverChanceLeft);
      this.lootBoxUncoverChanceRight = new GUIPanelSubButton("lootBoxUncoverChanceRight", "right");
      this.guiButtons.add(this.lootBoxUncoverChanceRight);
      this.lootBoxUncoverChance = new GUIClickableText("lootBoxUncoverChance", 1);
      this.lootBoxUncoverChance
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.lootBoxes.tooltip.uncoverChanceHeader"),
            Text.getText("worldMapGameModeConfigPanel.lootBoxes.tooltip.uncoverChance")
         );
      this.guiButtons.add(this.lootBoxUncoverChance);
      this.lootBoxKeyChanceLeft = new GUIPanelSubButton("lootBoxKeyChanceLeft", "left");
      this.guiButtons.add(this.lootBoxKeyChanceLeft);
      this.lootBoxKeyChanceRight = new GUIPanelSubButton("lootBoxKeyChanceRight", "right");
      this.guiButtons.add(this.lootBoxKeyChanceRight);
      this.lootBoxKeyChance = new GUIClickableText("lootBoxKeyChance", 1);
      this.lootBoxKeyChance
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.lootBoxes.tooltip.keyChanceHeader"),
            Text.getText("worldMapGameModeConfigPanel.lootBoxes.tooltip.keyChance")
         );
      this.guiButtons.add(this.lootBoxKeyChance);
      this.baseNomadSpawnRateLeft = new GUIPanelSubButton("baseNomadSpawnRateLeft", "left");
      this.guiButtons.add(this.baseNomadSpawnRateLeft);
      this.baseNomadSpawnRateRight = new GUIPanelSubButton("baseNomadSpawnRateRight", "right");
      this.guiButtons.add(this.baseNomadSpawnRateRight);
      this.baseNomadSpawnRate = new GUIClickableText("baseNomadSpawnRate", 1);
      this.baseNomadSpawnRate
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.nomads.tooltip.baseSpawnRateHeader"),
            Text.getText("worldMapGameModeConfigPanel.nomads.tooltip.baseSpawnRate")
         );
      this.guiButtons.add(this.baseNomadSpawnRate);
      this.nomadSpawnAmountMultiplierLeft = new GUIPanelSubButton("nomadSpawnAmountMultiplierLeft", "left");
      this.guiButtons.add(this.nomadSpawnAmountMultiplierLeft);
      this.nomadSpawnAmountMultiplierRight = new GUIPanelSubButton("nomadSpawnAmountMultiplierRight", "right");
      this.guiButtons.add(this.nomadSpawnAmountMultiplierRight);
      this.nomadSpawnAmountMultiplier = new GUIClickableText("nomadSpawnAmountMultiplier", 1);
      this.nomadSpawnAmountMultiplier
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.nomads.tooltip.spawnAmountMultiplierHeader"),
            Text.getText("worldMapGameModeConfigPanel.nomads.tooltip.spawnAmountMultiplier")
         );
      this.guiButtons.add(this.nomadSpawnAmountMultiplier);
      this.nomadSpawnAmountMinimumLeft = new GUIPanelSubButton("nomadSpawnAmountMinimumLeft", "left");
      this.guiButtons.add(this.nomadSpawnAmountMinimumLeft);
      this.nomadSpawnAmountMinimumRight = new GUIPanelSubButton("nomadSpawnAmountMinimumRight", "right");
      this.guiButtons.add(this.nomadSpawnAmountMinimumRight);
      this.nomadSpawnAmountMinimum = new GUIClickableText("nomadSpawnAmountMinimum", 1);
      this.nomadSpawnAmountMinimum
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.nomads.tooltip.spawnAmountMinimumHeader"),
            Text.getText("worldMapGameModeConfigPanel.nomads.tooltip.spawnAmountMinimum")
         );
      this.guiButtons.add(this.nomadSpawnAmountMinimum);
      this.nomadSpawnAmountMaximumLeft = new GUIPanelSubButton("nomadSpawnAmountMaximumLeft", "left");
      this.guiButtons.add(this.nomadSpawnAmountMaximumLeft);
      this.nomadSpawnAmountMaximumRight = new GUIPanelSubButton("nomadSpawnAmountMaximumRight", "right");
      this.guiButtons.add(this.nomadSpawnAmountMaximumRight);
      this.nomadSpawnAmountMaximum = new GUIClickableText("nomadSpawnAmountMaximum", 1);
      this.nomadSpawnAmountMaximum
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.nomads.tooltip.spawnAmountMaxHeader"),
            Text.getText("worldMapGameModeConfigPanel.nomads.tooltip.spawnAmountMax")
         );
      this.guiButtons.add(this.nomadSpawnAmountMaximum);
      this.temperatureVarianceLeft = new GUIPanelSubButton("temperatureVarianceLeft", "left");
      this.guiButtons.add(this.temperatureVarianceLeft);
      this.temperatureVarianceRight = new GUIPanelSubButton("temperatureVarianceRight", "right");
      this.guiButtons.add(this.temperatureVarianceRight);
      this.temperatureVariance = new GUIClickableText("temperatureVariance", 1);
      this.temperatureVariance
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.other.tooltip.temperatureVarianceHeader"),
            Text.getText("worldMapGameModeConfigPanel.other.tooltip.temperatureVariance")
         );
      this.guiButtons.add(this.temperatureVariance);
      this.startingSeasonLeft = new GUIPanelSubButton("startingSeasonLeft", "left");
      this.guiButtons.add(this.startingSeasonLeft);
      this.startingSeasonRight = new GUIPanelSubButton("startingSeasonRight", "right");
      this.guiButtons.add(this.startingSeasonRight);
      this.startingSeason = new GUIClickableText("startingSeason", 1);
      this.startingSeason
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.other.tooltip.startingSeasonHeader"),
            Text.getText("worldMapGameModeConfigPanel.other.tooltip.startingSeason")
         );
      this.guiButtons.add(this.startingSeason);
      this.weatherChanceSpringLeft = new GUIPanelSubButton("weatherChanceSpringLeft", "left");
      this.guiButtons.add(this.weatherChanceSpringLeft);
      this.weatherChanceSpringRight = new GUIPanelSubButton("weatherChanceSpringRight", "right");
      this.guiButtons.add(this.weatherChanceSpringRight);
      this.weatherChanceSpring = new GUIClickableText("weatherChanceSpring", 1);
      Text.setVariableText("season", TimeModule.Season.SPRING.getText());
      this.weatherChanceSpring
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.rainOrSnowChanceHeader"),
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.rainOrSnowChance")
         );
      this.guiButtons.add(this.weatherChanceSpring);
      this.averageTemperatureSpringLeft = new GUIPanelSubButton("averageTemperatureSpringLeft", "left");
      this.guiButtons.add(this.averageTemperatureSpringLeft);
      this.averageTemperatureSpringRight = new GUIPanelSubButton("averageTemperatureSpringRight", "right");
      this.guiButtons.add(this.averageTemperatureSpringRight);
      this.averageTemperatureSpring = new GUIClickableText("averageTemperatureSpring", 1);
      Text.setVariableText("season", TimeModule.Season.SPRING.getText());
      this.averageTemperatureSpring
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.averageTemperatureHeader"),
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.averageTemperature")
         );
      this.guiButtons.add(this.averageTemperatureSpring);
      this.seasonLengthSpringLeft = new GUIPanelSubButton("seasonLengthSpringLeft", "left");
      this.guiButtons.add(this.seasonLengthSpringLeft);
      this.seasonLengthSpringRight = new GUIPanelSubButton("seasonLengthSpringRight", "right");
      this.guiButtons.add(this.seasonLengthSpringRight);
      this.seasonLengthSpring = new GUIClickableText("seasonLengthSpring", 1);
      Text.setVariableText("season", TimeModule.Season.SPRING.getText());
      this.seasonLengthSpring
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.seasonLengthHeader"),
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.seasonLength")
         );
      this.guiButtons.add(this.seasonLengthSpring);
      this.weatherChanceSummerLeft = new GUIPanelSubButton("weatherChanceSummerLeft", "left");
      this.guiButtons.add(this.weatherChanceSummerLeft);
      this.weatherChanceSummerRight = new GUIPanelSubButton("weatherChanceSummerRight", "right");
      this.guiButtons.add(this.weatherChanceSummerRight);
      this.weatherChanceSummer = new GUIClickableText("weatherChanceSummer", 1);
      Text.setVariableText("season", TimeModule.Season.SUMMER.getText());
      this.weatherChanceSummer
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.rainOrSnowChanceHeader"),
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.rainOrSnowChance")
         );
      this.guiButtons.add(this.weatherChanceSummer);
      this.averageTemperatureSummerLeft = new GUIPanelSubButton("averageTemperatureSummerLeft", "left");
      this.guiButtons.add(this.averageTemperatureSummerLeft);
      this.averageTemperatureSummerRight = new GUIPanelSubButton("averageTemperatureSummerRight", "right");
      this.guiButtons.add(this.averageTemperatureSummerRight);
      this.averageTemperatureSummer = new GUIClickableText("averageTemperatureSummer", 1);
      Text.setVariableText("season", TimeModule.Season.SUMMER.getText());
      this.averageTemperatureSummer
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.averageTemperatureHeader"),
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.averageTemperature")
         );
      this.guiButtons.add(this.averageTemperatureSummer);
      this.seasonLengthSummerLeft = new GUIPanelSubButton("seasonLengthSummerLeft", "left");
      this.guiButtons.add(this.seasonLengthSummerLeft);
      this.seasonLengthSummerRight = new GUIPanelSubButton("seasonLengthSummerRight", "right");
      this.guiButtons.add(this.seasonLengthSummerRight);
      this.seasonLengthSummer = new GUIClickableText("seasonLengthSummer", 1);
      Text.setVariableText("season", TimeModule.Season.SUMMER.getText());
      this.seasonLengthSummer
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.seasonLengthHeader"),
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.seasonLength")
         );
      this.guiButtons.add(this.seasonLengthSummer);
      this.weatherChanceAutumnLeft = new GUIPanelSubButton("weatherChanceAutumnLeft", "left");
      this.guiButtons.add(this.weatherChanceAutumnLeft);
      this.weatherChanceAutumnRight = new GUIPanelSubButton("weatherChanceAutumnRight", "right");
      this.guiButtons.add(this.weatherChanceAutumnRight);
      this.weatherChanceAutumn = new GUIClickableText("weatherChanceAutumn", 1);
      Text.setVariableText("season", TimeModule.Season.AUTUMN.getText());
      this.weatherChanceAutumn
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.rainOrSnowChanceHeader"),
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.rainOrSnowChance")
         );
      this.guiButtons.add(this.weatherChanceAutumn);
      this.averageTemperatureAutumnLeft = new GUIPanelSubButton("averageTemperatureAutumnLeft", "left");
      this.guiButtons.add(this.averageTemperatureAutumnLeft);
      this.averageTemperatureAutumnRight = new GUIPanelSubButton("averageTemperatureAutumnRight", "right");
      this.guiButtons.add(this.averageTemperatureAutumnRight);
      this.averageTemperatureAutumn = new GUIClickableText("averageTemperatureAutumn", 1);
      Text.setVariableText("season", TimeModule.Season.AUTUMN.getText());
      this.averageTemperatureAutumn
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.averageTemperatureHeader"),
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.averageTemperature")
         );
      this.guiButtons.add(this.averageTemperatureAutumn);
      this.seasonLengthAutumnLeft = new GUIPanelSubButton("seasonLengthAutumnLeft", "left");
      this.guiButtons.add(this.seasonLengthAutumnLeft);
      this.seasonLengthAutumnRight = new GUIPanelSubButton("seasonLengthAutumnRight", "right");
      this.guiButtons.add(this.seasonLengthAutumnRight);
      this.seasonLengthAutumn = new GUIClickableText("seasonLengthAutumn", 1);
      Text.setVariableText("season", TimeModule.Season.AUTUMN.getText());
      this.seasonLengthAutumn
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.seasonLengthHeader"),
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.seasonLength")
         );
      this.guiButtons.add(this.seasonLengthAutumn);
      this.weatherChanceWinterLeft = new GUIPanelSubButton("weatherChanceWinterLeft", "left");
      this.guiButtons.add(this.weatherChanceWinterLeft);
      this.weatherChanceWinterRight = new GUIPanelSubButton("weatherChanceWinterRight", "right");
      this.guiButtons.add(this.weatherChanceWinterRight);
      this.weatherChanceWinter = new GUIClickableText("weatherChanceWinter", 1);
      Text.setVariableText("season", TimeModule.Season.WINTER.getText());
      this.weatherChanceWinter
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.rainOrSnowChanceHeader"),
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.rainOrSnowChance")
         );
      this.guiButtons.add(this.weatherChanceWinter);
      this.averageTemperatureWinterLeft = new GUIPanelSubButton("averageTemperatureWinterLeft", "left");
      this.guiButtons.add(this.averageTemperatureWinterLeft);
      this.averageTemperatureWinterRight = new GUIPanelSubButton("averageTemperatureWinterRight", "right");
      this.guiButtons.add(this.averageTemperatureWinterRight);
      this.averageTemperatureWinter = new GUIClickableText("averageTemperatureWinter", 1);
      Text.setVariableText("season", TimeModule.Season.WINTER.getText());
      this.averageTemperatureWinter
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.averageTemperatureHeader"),
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.averageTemperature")
         );
      this.guiButtons.add(this.averageTemperatureWinter);
      this.seasonLengthWinterLeft = new GUIPanelSubButton("seasonLengthWinterLeft", "left");
      this.guiButtons.add(this.seasonLengthWinterLeft);
      this.seasonLengthWinterRight = new GUIPanelSubButton("seasonLengthWinterRight", "right");
      this.guiButtons.add(this.seasonLengthWinterRight);
      this.seasonLengthWinter = new GUIClickableText("seasonLengthWinter", 1);
      Text.setVariableText("season", TimeModule.Season.WINTER.getText());
      this.seasonLengthWinter
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.seasonLengthHeader"),
            Text.getText("worldMapGameModeConfigPanel.weather.tooltip.seasonLength")
         );
      this.guiButtons.add(this.seasonLengthWinter);
      this.fullMoonIntervalLeft = new GUIPanelSubButton("fullMoonIntervalLeft", "left");
      this.guiButtons.add(this.fullMoonIntervalLeft);
      this.fullMoonIntervalRight = new GUIPanelSubButton("fullMoonIntervalRight", "right");
      this.guiButtons.add(this.fullMoonIntervalRight);
      this.fullMoonInterval = new GUIClickableText("fullMoonInterval", 1);
      this.fullMoonInterval
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.fullMoonIntervalHeader"),
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.fullMoonInterval")
         );
      this.guiButtons.add(this.fullMoonInterval);
      this.bloodMoonChanceLeft = new GUIPanelSubButton("bloodMoonChanceLeft", "left");
      this.guiButtons.add(this.bloodMoonChanceLeft);
      this.bloodMoonChanceRight = new GUIPanelSubButton("bloodMoonChanceRight", "right");
      this.guiButtons.add(this.bloodMoonChanceRight);
      this.bloodMoonChance = new GUIClickableText("bloodMoonChance", 1);
      this.bloodMoonChance
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.bloodMoonChanceHeader"),
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.bloodMoonChance")
         );
      this.guiButtons.add(this.bloodMoonChance);
      this.bloodMoonDelayLeft = new GUIPanelSubButton("bloodMoonDelayLeft", "left");
      this.guiButtons.add(this.bloodMoonDelayLeft);
      this.bloodMoonDelayRight = new GUIPanelSubButton("bloodMoonDelayRight", "right");
      this.guiButtons.add(this.bloodMoonDelayRight);
      this.bloodMoonDelay = new GUIClickableText("bloodMoonDelay", 1);
      this.bloodMoonDelay
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.bloodMoonDelayHeader"),
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.bloodMoonDelay")
         );
      this.guiButtons.add(this.bloodMoonDelay);
      this.allowBloodMoonSpringLeft = new GUIPanelSubButton("allowBloodMoonSpringLeft", "left");
      this.guiButtons.add(this.allowBloodMoonSpringLeft);
      this.allowBloodMoonSpringRight = new GUIPanelSubButton("allowBloodMoonSpringRight", "right");
      this.guiButtons.add(this.allowBloodMoonSpringRight);
      this.allowBloodMoonSpring = new GUIClickableText("allowBloodMoonSpring", 1);
      Text.setVariableText("season", TimeModule.Season.SPRING.getText());
      this.allowBloodMoonSpring
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.bloodMoonHeader"),
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.bloodMoon")
         );
      this.guiButtons.add(this.allowBloodMoonSpring);
      this.allowBloodMoonSummerLeft = new GUIPanelSubButton("allowBloodMoonSummerLeft", "left");
      this.guiButtons.add(this.allowBloodMoonSummerLeft);
      this.allowBloodMoonSummerRight = new GUIPanelSubButton("allowBloodMoonSummerRight", "right");
      this.guiButtons.add(this.allowBloodMoonSummerRight);
      this.allowBloodMoonSummer = new GUIClickableText("allowBloodMoonSummer", 1);
      Text.setVariableText("season", TimeModule.Season.SUMMER.getText());
      this.allowBloodMoonSummer
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.bloodMoonHeader"),
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.bloodMoon")
         );
      this.guiButtons.add(this.allowBloodMoonSummer);
      this.allowBloodMoonAutumnLeft = new GUIPanelSubButton("allowBloodMoonAutumnLeft", "left");
      this.guiButtons.add(this.allowBloodMoonAutumnLeft);
      this.allowBloodMoonAutumnRight = new GUIPanelSubButton("allowBloodMoonAutumnRight", "right");
      this.guiButtons.add(this.allowBloodMoonAutumnRight);
      this.allowBloodMoonAutumn = new GUIClickableText("allowBloodMoonAutumn", 1);
      Text.setVariableText("season", TimeModule.Season.AUTUMN.getText());
      this.allowBloodMoonAutumn
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.bloodMoonHeader"),
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.bloodMoon")
         );
      this.guiButtons.add(this.allowBloodMoonAutumn);
      this.allowBloodMoonWinterLeft = new GUIPanelSubButton("allowBloodMoonWinterLeft", "left");
      this.guiButtons.add(this.allowBloodMoonWinterLeft);
      this.allowBloodMoonWinterRight = new GUIPanelSubButton("allowBloodMoonWinterRight", "right");
      this.guiButtons.add(this.allowBloodMoonWinterRight);
      this.allowBloodMoonWinter = new GUIClickableText("allowBloodMoonWinter", 1);
      Text.setVariableText("season", TimeModule.Season.WINTER.getText());
      this.allowBloodMoonWinter
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.bloodMoonHeader"),
            Text.getText("worldMapGameModeConfigPanel.moonPhases.tooltip.bloodMoon")
         );
      this.guiButtons.add(this.allowBloodMoonWinter);
      this.meteorShowerChanceLeft = new GUIPanelSubButton("meteorShowerChanceLeft", "left");
      this.guiButtons.add(this.meteorShowerChanceLeft);
      this.meteorShowerChanceRight = new GUIPanelSubButton("meteorShowerChanceRight", "right");
      this.guiButtons.add(this.meteorShowerChanceRight);
      this.meteorShowerChance = new GUIClickableText("meteorShowerChance", 1);
      this.meteorShowerChance
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.disasters.tooltip.meteorShowerChanceHeader"),
            Text.getText("worldMapGameModeConfigPanel.disasters.tooltip.meteorShowerChance")
         );
      this.guiButtons.add(this.meteorShowerChance);
      this.earthquakeChanceLeft = new GUIPanelSubButton("earthquakeChanceLeft", "left");
      this.guiButtons.add(this.earthquakeChanceLeft);
      this.earthquakeChanceRight = new GUIPanelSubButton("earthquakeChanceRight", "right");
      this.guiButtons.add(this.earthquakeChanceRight);
      this.earthquakeChance = new GUIClickableText("earthquakeChance", 1);
      this.earthquakeChance
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.disasters.tooltip.earthquakeChanceHeader"),
            Text.getText("worldMapGameModeConfigPanel.disasters.tooltip.earthquakeChance")
         );
      this.guiButtons.add(this.earthquakeChance);
      this.electricalStormChanceLeft = new GUIPanelSubButton("electricalStormChanceLeft", "left");
      this.guiButtons.add(this.electricalStormChanceLeft);
      this.electricalStormChanceRight = new GUIPanelSubButton("electricalStormChanceRight", "right");
      this.guiButtons.add(this.electricalStormChanceRight);
      this.electricalStormChance = new GUIClickableText("electricalStormChance", 1);
      this.electricalStormChance
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.disasters.tooltip.electricalStormChanceHeader"),
            Text.getText("worldMapGameModeConfigPanel.disasters.tooltip.electricalStormChance")
         );
      this.guiButtons.add(this.electricalStormChance);
      this.hailStormChanceLeft = new GUIPanelSubButton("hailStormChanceLeft", "left");
      this.guiButtons.add(this.hailStormChanceLeft);
      this.hailStormChanceRight = new GUIPanelSubButton("hailStormChanceRight", "right");
      this.guiButtons.add(this.hailStormChanceRight);
      this.hailStormChance = new GUIClickableText("hailStormChance", 1);
      this.hailStormChance
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.disasters.tooltip.hailStormChanceHeader"),
            Text.getText("worldMapGameModeConfigPanel.disasters.tooltip.hailStormChance")
         );
      this.guiButtons.add(this.hailStormChance);
      this.blightChanceLeft = new GUIPanelSubButton("blightChanceLeft", "left");
      this.guiButtons.add(this.blightChanceLeft);
      this.blightChanceRight = new GUIPanelSubButton("blightChanceRight", "right");
      this.guiButtons.add(this.blightChanceRight);
      this.blightChance = new GUIClickableText("blightChance", 1);
      this.blightChance
         .setToolTip(
            Text.getText("worldMapGameModeConfigPanel.disasters.tooltip.blightChanceHeader"),
            Text.getText("worldMapGameModeConfigPanel.disasters.tooltip.blightChance")
         );
      this.guiButtons.add(this.blightChance);
      this.resetNightmare = new GUIClickableText("resetNightmare", 2);
      this.guiButtons.add(this.resetNightmare);
      this.resetSurvival = new GUIClickableText("resetSurvival", 2);
      this.guiButtons.add(this.resetSurvival);
      this.resetTraditional = new GUIClickableText("resetTraditional", 2);
      this.guiButtons.add(this.resetTraditional);
      this.resetPeaceful = new GUIClickableText("resetPeaceful", 2);
      this.guiButtons.add(this.resetPeaceful);
      this.generateList();
      this.save = new GUISquarePanelButton("save", 0, "saveMap");
      this.save.setToolTip(Text.getText("worldMapGameModeConfigPanel.button.tooltip.saveConfig"));
      this.guiButtons.add(this.save);
      this.saveInput = new GUIButtonInputText(gc, "saveInput", Text.getText("worldMapGameModeConfigPanel.button.input.configName"), 10, 6, 1);
      this.guiButtons.add(this.saveInput);
      this.accept = new GUIButtonMainMenu("accept", Text.getText("worldMapGameModeConfigPanel.button.accept"), 1, 1);
      this.guiButtons.add(this.accept);
      this.back = new GUIButtonMainMenu("back", Text.getText("worldMapGameModeConfigPanel.button.back"), 1);
      this.guiButtons.add(this.back);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.panel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() / 2 - this.panel.getHeight() / 2;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      GameModeTemplateBase template = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
      this.font.drawString(this.x + 135, this.y + 4, Text.getText("worldMapGameModeConfigPanel.header"), Text.FontType.HEADER, 2, true);
      int alignX = 0;
      int alignY = 0;
      this.font
         .drawString(this.x + alignX + 90, this.y + alignY + 30, Text.getText("worldMapGameModeConfigPanel.corruption.header"), Text.FontType.BODY, 3, true);
      this.baseSpawnRate
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.corruption.baseSpawnRate"),
            this.x + alignX + 90,
            this.y + alignY + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.baseSpawnRateLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + alignY + 70, false, debug);
         this.baseSpawnRateRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + alignY + 70, false, debug);
      }

      if (template.getBaseSpawnRate() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + alignY + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getBaseSpawnRate());
         this.font
            .drawString(this.x + alignX + 90, this.y + alignY + 70, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inX"), Text.FontType.BODY, 1, true);
      }

      alignY += 35;
      this.boostedMonsterChanceMultiplier
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.corruption.boostedChanceMultiplier"),
            this.x + alignX + 90,
            this.y + alignY + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.boostedMonsterChanceMultiplierLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + alignY + 70, false, debug);
         this.boostedMonsterChanceMultiplierRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + alignY + 70, false, debug);
      }

      this.font
         .drawString(this.x + alignX + 90, this.y + alignY + 68, "$GRE0" + template.getBoostedMonsterChanceMultiplier() + "x", Text.FontType.BODY, 2, true);
      alignY += 35;
      this.corruptionStartDay
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.corruption.corruptionStartDay"),
            this.x + alignX + 90,
            this.y + alignY + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.corruptionStartDayLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + alignY + 70, false, debug);
         this.corruptionStartDayRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + alignY + 70, false, debug);
      }

      if (template.getCorruptionStartDay() == 0) {
         this.font
            .drawString(
               this.x + alignX + 90, this.y + alignY + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.never"), Text.FontType.BODY, 2, true
            );
      } else {
         this.font.drawString(this.x + alignX + 90, this.y + alignY + 70, "$GRE0" + template.getCorruptionStartDay(), Text.FontType.BODY, 1, true);
      }

      alignY += 35;
      this.corruptionDesiredAmountStart
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.corruption.corruptionDesiredAmountStart"),
            this.x + alignX + 90,
            this.y + alignY + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.corruptionDesiredAmountStartLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + alignY + 70, false, debug);
         this.corruptionDesiredAmountStartRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + alignY + 70, false, debug);
      }

      this.font.drawString(this.x + alignX + 90, this.y + alignY + 70, "$GRE0" + template.getCorruptionDesiredAmountStart(), Text.FontType.BODY, 1, true);
      alignY += 35;
      this.corruptionDesiredAmountPerDay
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.corruption.corruptionDesiredAmountPerDay"),
            this.x + alignX + 90,
            this.y + alignY + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.corruptionDesiredAmountPerDayLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + alignY + 70, false, debug);
         this.corruptionDesiredAmountPerDayRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + alignY + 70, false, debug);
      }

      this.font.drawString(this.x + alignX + 90, this.y + alignY + 70, "$GRE0" + template.getCorruptionDesiredAmountPerDay(), Text.FontType.BODY, 1, true);
      alignY += 35;
      this.corruptionDesiredAmountPerBuilding
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.corruption.corruptionDesiredAmountPerBuilding"),
            this.x + alignX + 90,
            this.y + alignY + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.corruptionDesiredAmountPerBuildingLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + alignY + 70, false, debug);
         this.corruptionDesiredAmountPerBuildingRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + alignY + 70, false, debug);
      }

      this.font.drawString(this.x + alignX + 90, this.y + alignY + 70, "$GRE0" + template.getCorruptionDesiredAmountPerBuilding(), Text.FontType.BODY, 1, true);
      alignY += 35;
      this.corruptionBuildingRate
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.corruption.buildingRate"),
            this.x + alignX + 90,
            this.y + alignY + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.corruptionBuildingRateLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + alignY + 70, false, debug);
         this.corruptionBuildingRateRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + alignY + 70, false, debug);
      }

      this.font
         .drawString(this.x + alignX + 90, this.y + alignY + 70, "$GRE0" + template.getCorruptionBuildingRate() / 1000 + "k", Text.FontType.BODY, 1, true);
      alignY += 35;
      this.corruptionBuildingDensity
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.corruption.buildingDensity"),
            this.x + alignX + 90,
            this.y + alignY + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.corruptionBuildingDensityLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + alignY + 70, false, debug);
         this.corruptionBuildingDensityRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + alignY + 70, false, debug);
      }

      this.font.drawString(this.x + alignX + 90, this.y + alignY + 70, "$GRE0" + template.getCorruptionBuildingDensity(), Text.FontType.BODY, 1, true);
      alignY += 35;
      this.corruptionBuildingDensityRange
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.corruption.buildingDensityRange"),
            this.x + alignX + 90,
            this.y + alignY + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.corruptionBuildingDensityRangeLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + alignY + 70, false, debug);
         this.corruptionBuildingDensityRangeRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + alignY + 70, false, debug);
      }

      this.font.drawString(this.x + alignX + 90, this.y + alignY + 70, "$GRE0" + template.getCorruptionBuildingDensityRange(), Text.FontType.BODY, 1, true);
      alignY += 35;
      alignY += 35;
      alignX += 150;
      int var20 = 0;
      this.font
         .drawString(this.x + alignX + 90, this.y + var20 + 30, Text.getText("worldMapGameModeConfigPanel.lootBoxes.header"), Text.FontType.BODY, 3, true);
      this.maximumLootBoxes
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.lootBoxes.maximumLootBoxes"),
            this.x + alignX + 90,
            this.y + var20 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.maximumLootBoxesLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var20 + 70, false, debug);
         this.maximumLootBoxesRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var20 + 70, false, debug);
      }

      if (template.getMaximumLootBoxes() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var20 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.never"), Text.FontType.BODY, 2, true);
      } else {
         this.font.drawString(this.x + alignX + 90, this.y + var20 + 68, "$GRE0" + template.getMaximumLootBoxes(), Text.FontType.BODY, 2, true);
      }

      var20 += 35;
      this.lootBoxUncoverChance
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.lootBoxes.uncoverChance"),
            this.x + alignX + 90,
            this.y + var20 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.lootBoxUncoverChanceLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var20 + 70, false, debug);
         this.lootBoxUncoverChanceRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var20 + 70, false, debug);
      }

      if (template.getLootBoxUncoverChance() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var20 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.never"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getLootBoxUncoverChance());
         this.font
            .drawString(this.x + alignX + 90, this.y + var20 + 70, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inX"), Text.FontType.BODY, 1, true);
      }

      var20 += 35;
      this.lootBoxKeyChance
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.lootBoxes.keyChance"),
            this.x + alignX + 90,
            this.y + var20 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.lootBoxKeyChanceLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var20 + 70, false, debug);
         this.lootBoxKeyChanceRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var20 + 70, false, debug);
      }

      if (template.getLootBoxKeyChance() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var20 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.never"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getLootBoxKeyChance());
         this.font
            .drawString(this.x + alignX + 90, this.y + var20 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inX"), Text.FontType.BODY, 2, true);
      }

      var20 += 70;
      var20 += 140;
      this.font.drawString(this.x + alignX + 90, this.y + var20 + 30, Text.getText("worldMapGameModeConfigPanel.nomads.header"), Text.FontType.BODY, 3, true);
      this.baseNomadSpawnRate
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.nomads.baseSpawnRate"),
            this.x + alignX + 90,
            this.y + var20 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.baseNomadSpawnRateLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var20 + 70, false, debug);
         this.baseNomadSpawnRateRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var20 + 70, false, debug);
      }

      if (template.getBaseNomadSpawnRate() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var20 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.never"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getBaseNomadSpawnRate() / 1000);
         this.font
            .drawString(this.x + alignX + 90, this.y + var20 + 70, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inXk"), Text.FontType.BODY, 1, true);
      }

      var20 += 35;
      this.nomadSpawnAmountMultiplier
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.nomads.spawnAmountMultiplier"),
            this.x + alignX + 90,
            this.y + var20 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.nomadSpawnAmountMultiplierLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var20 + 70, false, debug);
         this.nomadSpawnAmountMultiplierRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var20 + 70, false, debug);
      }

      this.font.drawString(this.x + alignX + 90, this.y + var20 + 68, "$GRE0" + template.getNomadSpawnAmountMultiplier() + "x", Text.FontType.BODY, 2, true);
      var20 += 35;
      this.nomadSpawnAmountMinimum
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.nomads.spawnAmountMinimum"),
            this.x + alignX + 90,
            this.y + var20 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.nomadSpawnAmountMinimumLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var20 + 70, false, debug);
         this.nomadSpawnAmountMinimumRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var20 + 70, false, debug);
      }

      this.font.drawString(this.x + alignX + 90, this.y + var20 + 68, "$GRE0" + template.getNomadSpawnAmountMinimum(), Text.FontType.BODY, 2, true);
      var20 += 35;
      this.nomadSpawnAmountMaximum
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.nomads.spawnAmountMax"),
            this.x + alignX + 90,
            this.y + var20 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.nomadSpawnAmountMaximumLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var20 + 70, false, debug);
         this.nomadSpawnAmountMaximumRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var20 + 70, false, debug);
      }

      this.font.drawString(this.x + alignX + 90, this.y + var20 + 68, "$GRE0" + template.getNomadSpawnAmountMaximum(), Text.FontType.BODY, 2, true);
      var20 += 70;
      this.font.drawString(this.x + alignX + 90, this.y + var20 + 30, Text.getText("worldMapGameModeConfigPanel.other.header"), Text.FontType.BODY, 3, true);
      this.temperatureVariance
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.other.temperatureVariance"),
            this.x + alignX + 90,
            this.y + var20 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.temperatureVarianceLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var20 + 70, false, debug);
         this.temperatureVarianceRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var20 + 70, false, debug);
      }

      if (template.getTemperatureVariance() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var20 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         this.font.drawString(this.x + alignX + 90, this.y + var20 + 68, "$GRE0" + template.getTemperatureVariance() + "%", Text.FontType.BODY, 2, true);
      }

      var20 += 35;
      this.startingSeason
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.other.startingSeason"),
            this.x + alignX + 90,
            this.y + var20 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.startingSeasonLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var20 + 70, false, debug);
         this.startingSeasonRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var20 + 70, false, debug);
      }

      this.font.drawString(this.x + alignX + 90, this.y + var20 + 70, "$GRE0" + template.getSeasonStart().getText(), Text.FontType.BODY, 1, true);
      var20 += 35;
      alignX += 150;
      int var31 = 0;
      this.font
         .drawString(this.x + alignX + 90, this.y + var31 + 30, Text.getText("worldMapGameModeConfigPanel.weather.header.spring"), Text.FontType.BODY, 3, true);
      this.weatherChanceSpring
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.weather.rainOrSnowChance"),
            this.x + alignX + 90,
            this.y + var31 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.weatherChanceSpringLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var31 + 70, false, debug);
         this.weatherChanceSpringRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var31 + 70, false, debug);
      }

      if (template.getWeatherChanceSpring() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var31 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getWeatherChanceSpring() / 1000);
         this.font
            .drawString(this.x + alignX + 90, this.y + var31 + 70, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inXk"), Text.FontType.BODY, 1, true);
      }

      var31 += 35;
      this.averageTemperatureSpring
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.weather.averageTemperature"),
            this.x + alignX + 90,
            this.y + var31 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.averageTemperatureSpringLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var31 + 70, false, debug);
         this.averageTemperatureSpringRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var31 + 70, false, debug);
      }

      this.font.drawString(this.x + alignX + 90, this.y + var31 + 68, "$GRE0" + template.getAverageTemperatureSpring() + "%", Text.FontType.BODY, 2, true);
      var31 += 35;
      this.seasonLengthSpring
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.weather.seasonLength"),
            this.x + alignX + 90,
            this.y + var31 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.seasonLengthSpringLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var31 + 70, false, debug);
         this.seasonLengthSpringRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var31 + 70, false, debug);
      }

      Text.setVariableText("days", template.getSeasonLengthSpring());
      this.font
         .drawString(this.x + alignX + 90, this.y + var31 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.xDays"), Text.FontType.BODY, 2, true);
      var31 += 70;
      this.font
         .drawString(this.x + alignX + 90, this.y + var31 + 30, Text.getText("worldMapGameModeConfigPanel.weather.header.summer"), Text.FontType.BODY, 3, true);
      this.weatherChanceSummer
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.weather.rainOrSnowChance"),
            this.x + alignX + 90,
            this.y + var31 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.weatherChanceSummerLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var31 + 70, false, debug);
         this.weatherChanceSummerRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var31 + 70, false, debug);
      }

      if (template.getWeatherChanceSummer() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var31 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getWeatherChanceSummer() / 1000);
         this.font
            .drawString(this.x + alignX + 90, this.y + var31 + 70, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inXk"), Text.FontType.BODY, 1, true);
      }

      var31 += 35;
      this.averageTemperatureSummer
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.weather.averageTemperature"),
            this.x + alignX + 90,
            this.y + var31 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.averageTemperatureSummerLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var31 + 70, false, debug);
         this.averageTemperatureSummerRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var31 + 70, false, debug);
      }

      this.font.drawString(this.x + alignX + 90, this.y + var31 + 68, "$GRE0" + template.getAverageTemperatureSummer() + "%", Text.FontType.BODY, 2, true);
      var31 += 35;
      this.seasonLengthSummer
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.weather.seasonLength"),
            this.x + alignX + 90,
            this.y + var31 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.seasonLengthSummerLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var31 + 70, false, debug);
         this.seasonLengthSummerRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var31 + 70, false, debug);
      }

      Text.setVariableText("days", template.getSeasonLengthSummer());
      this.font
         .drawString(this.x + alignX + 90, this.y + var31 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.xDays"), Text.FontType.BODY, 2, true);
      var31 += 70;
      this.font
         .drawString(this.x + alignX + 90, this.y + var31 + 30, Text.getText("worldMapGameModeConfigPanel.weather.header.autumn"), Text.FontType.BODY, 3, true);
      this.weatherChanceAutumn
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.weather.rainOrSnowChance"),
            this.x + alignX + 90,
            this.y + var31 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.weatherChanceAutumnLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var31 + 70, false, debug);
         this.weatherChanceAutumnRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var31 + 70, false, debug);
      }

      if (template.getWeatherChanceAutumn() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var31 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getWeatherChanceAutumn() / 1000);
         this.font
            .drawString(this.x + alignX + 90, this.y + var31 + 70, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inXk"), Text.FontType.BODY, 1, true);
      }

      var31 += 35;
      this.averageTemperatureAutumn
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.weather.averageTemperature"),
            this.x + alignX + 90,
            this.y + var31 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.averageTemperatureAutumnLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var31 + 70, false, debug);
         this.averageTemperatureAutumnRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var31 + 70, false, debug);
      }

      this.font.drawString(this.x + alignX + 90, this.y + var31 + 68, "$GRE0" + template.getAverageTemperatureAutumn() + "%", Text.FontType.BODY, 2, true);
      var31 += 35;
      this.seasonLengthAutumn
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.weather.seasonLength"),
            this.x + alignX + 90,
            this.y + var31 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.seasonLengthAutumnLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var31 + 70, false, debug);
         this.seasonLengthAutumnRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var31 + 70, false, debug);
      }

      Text.setVariableText("days", template.getSeasonLengthAutumn());
      this.font
         .drawString(this.x + alignX + 90, this.y + var31 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.xDays"), Text.FontType.BODY, 2, true);
      var31 += 70;
      this.font
         .drawString(this.x + alignX + 90, this.y + var31 + 30, Text.getText("worldMapGameModeConfigPanel.weather.header.winter"), Text.FontType.BODY, 3, true);
      this.weatherChanceWinter
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.weather.rainOrSnowChance"),
            this.x + alignX + 90,
            this.y + var31 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.weatherChanceWinterLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var31 + 70, false, debug);
         this.weatherChanceWinterRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var31 + 70, false, debug);
      }

      if (template.getWeatherChanceWinter() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var31 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getWeatherChanceWinter() / 1000);
         this.font
            .drawString(this.x + alignX + 90, this.y + var31 + 70, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inXk"), Text.FontType.BODY, 1, true);
      }

      var31 += 35;
      this.averageTemperatureWinter
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.weather.averageTemperature"),
            this.x + alignX + 90,
            this.y + var31 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.averageTemperatureWinterLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var31 + 70, false, debug);
         this.averageTemperatureWinterRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var31 + 70, false, debug);
      }

      this.font.drawString(this.x + alignX + 90, this.y + var31 + 68, "$GRE0" + template.getAverageTemperatureWinter() + "%", Text.FontType.BODY, 2, true);
      var31 += 35;
      this.seasonLengthWinter
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.weather.seasonLength"),
            this.x + alignX + 90,
            this.y + var31 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.seasonLengthWinterLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var31 + 70, false, debug);
         this.seasonLengthWinterRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var31 + 70, false, debug);
      }

      Text.setVariableText("days", template.getSeasonLengthWinter());
      this.font
         .drawString(this.x + alignX + 90, this.y + var31 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.xDays"), Text.FontType.BODY, 2, true);
      alignX += 150;
      int var43 = 0;
      this.font
         .drawString(this.x + alignX + 90, this.y + var43 + 30, Text.getText("worldMapGameModeConfigPanel.moonPhases.header"), Text.FontType.BODY, 3, true);
      this.fullMoonInterval
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.moonPhases.fullMoonInterval"),
            this.x + alignX + 90,
            this.y + var43 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.fullMoonIntervalLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var43 + 70, false, debug);
         this.fullMoonIntervalRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var43 + 70, false, debug);
      }

      if (template.getFullMoonInterval() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("days", template.getFullMoonInterval());
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.xDays"), Text.FontType.BODY, 2, true);
      }

      var43 += 35;
      this.bloodMoonChance
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.moonPhases.bloodMoonChance"),
            this.x + alignX + 90,
            this.y + var43 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.bloodMoonChanceLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var43 + 70, false, debug);
         this.bloodMoonChanceRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var43 + 70, false, debug);
      }

      if (template.getBloodMoonChance() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getBloodMoonChance());
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inX"), Text.FontType.BODY, 2, true);
      }

      var43 += 35;
      this.bloodMoonDelay
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.moonPhases.bloodMoonDelay"),
            this.x + alignX + 90,
            this.y + var43 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.bloodMoonDelayLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var43 + 70, false, debug);
         this.bloodMoonDelayRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var43 + 70, false, debug);
      }

      if (template.getBloodMoonDelay() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("days", template.getBloodMoonDelay());
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.xDays"), Text.FontType.BODY, 2, true);
      }

      var43 += 35;
      Text.setVariableText("season", TimeModule.Season.SPRING.getText());
      this.allowBloodMoonSpring
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.moonPhases.bloodMoon"),
            this.x + alignX + 90,
            this.y + var43 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.allowBloodMoonSpringLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var43 + 70, false, debug);
         this.allowBloodMoonSpringRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var43 + 70, false, debug);
      }

      if (template.allowBloodMoonSpring()) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.true"), Text.FontType.BODY, 2, true);
      } else {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.false"), Text.FontType.BODY, 2, true);
      }

      var43 += 35;
      Text.setVariableText("season", TimeModule.Season.SUMMER.getText());
      this.allowBloodMoonSummer
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.moonPhases.bloodMoon"),
            this.x + alignX + 90,
            this.y + var43 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.allowBloodMoonSummerLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var43 + 70, false, debug);
         this.allowBloodMoonSummerRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var43 + 70, false, debug);
      }

      if (template.allowBloodMoonSummer()) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.true"), Text.FontType.BODY, 2, true);
      } else {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.false"), Text.FontType.BODY, 2, true);
      }

      var43 += 35;
      Text.setVariableText("season", TimeModule.Season.AUTUMN.getText());
      this.allowBloodMoonAutumn
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.moonPhases.bloodMoon"),
            this.x + alignX + 90,
            this.y + var43 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.allowBloodMoonAutumnLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var43 + 70, false, debug);
         this.allowBloodMoonAutumnRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var43 + 70, false, debug);
      }

      if (template.allowBloodMoonAutumn()) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.true"), Text.FontType.BODY, 2, true);
      } else {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.false"), Text.FontType.BODY, 2, true);
      }

      var43 += 35;
      Text.setVariableText("season", TimeModule.Season.WINTER.getText());
      this.allowBloodMoonWinter
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.moonPhases.bloodMoon"),
            this.x + alignX + 90,
            this.y + var43 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.allowBloodMoonWinterLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var43 + 70, false, debug);
         this.allowBloodMoonWinterRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var43 + 70, false, debug);
      }

      if (template.allowBloodMoonWinter()) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.true"), Text.FontType.BODY, 2, true);
      } else {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.false"), Text.FontType.BODY, 2, true);
      }

      var43 += 70;
      this.font
         .drawString(this.x + alignX + 90, this.y + var43 + 30, Text.getText("worldMapGameModeConfigPanel.disasters.header"), Text.FontType.BODY, 3, true);
      this.meteorShowerChance
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.disasters.meteorShowerChance"),
            this.x + alignX + 90,
            this.y + var43 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.meteorShowerChanceLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var43 + 70, false, debug);
         this.meteorShowerChanceRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var43 + 70, false, debug);
      }

      if (template.getMeteorShowerChance() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getMeteorShowerChance() / 1000);
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 70, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inXk"), Text.FontType.BODY, 1, true);
      }

      var43 += 35;
      this.earthquakeChance
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.disasters.earthquakeChance"),
            this.x + alignX + 90,
            this.y + var43 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.earthquakeChanceLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var43 + 70, false, debug);
         this.earthquakeChanceRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var43 + 70, false, debug);
      }

      if (template.getEarthquakeChance() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getEarthquakeChance() / 1000);
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 70, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inXk"), Text.FontType.BODY, 1, true);
      }

      var43 += 35;
      this.electricalStormChance
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.disasters.electricalStormChance"),
            this.x + alignX + 90,
            this.y + var43 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.electricalStormChanceLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var43 + 70, false, debug);
         this.electricalStormChanceRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var43 + 70, false, debug);
      }

      if (template.getElectricalStormChance() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getElectricalStormChance() / 1000);
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 70, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inXk"), Text.FontType.BODY, 1, true);
      }

      var43 += 35;
      this.hailStormChance
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.disasters.hailStormChance"),
            this.x + alignX + 90,
            this.y + var43 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.hailStormChanceLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var43 + 70, false, debug);
         this.hailStormChanceRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var43 + 70, false, debug);
      }

      if (template.getHailStormChance() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getHailStormChance() / 1000);
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 70, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inXk"), Text.FontType.BODY, 1, true);
      }

      var43 += 35;
      this.blightChance
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.disasters.blightChance"),
            this.x + alignX + 90,
            this.y + var43 + 55,
            "$WHI0",
            "$GRE1",
            false,
            true,
            debug
         );
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.blightChanceLeft.render(this.g, this.mouse, this.x + alignX + 25, this.y + var43 + 70, false, debug);
         this.blightChanceRight.render(this.g, this.mouse, this.x + alignX + 118, this.y + var43 + 70, false, debug);
      }

      if (template.getBlightChance() == 0) {
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 68, Text.getText("worldMapGameModeConfigPanel.miscellaneous.none"), Text.FontType.BODY, 2, true);
      } else {
         Text.setVariableText("amount", template.getBlightChance() / 1000);
         this.font
            .drawString(this.x + alignX + 90, this.y + var43 + 70, Text.getText("worldMapGameModeConfigPanel.miscellaneous.1inXk"), Text.FontType.BODY, 1, true);
      }

      alignX += 150;
      int var55 = 0;
      this.font
         .drawString(this.x + alignX + 90, this.y + var55 + 30, Text.getText("worldMapGameModeConfigPanel.loadConfig.header"), Text.FontType.BODY, 3, true);
      this.resetNightmare
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.loadConfig.nightmare"),
            this.x + alignX + 25,
            this.y + var55 + 55,
            "$WHI0",
            "$GRE1",
            WorldMapGUIData.hasCustomConfig(),
            false,
            debug
         );
      var55 += 20;
      this.resetSurvival
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.loadConfig.survival"),
            this.x + alignX + 25,
            this.y + var55 + 55,
            "$WHI0",
            "$GRE1",
            WorldMapGUIData.hasCustomConfig(),
            false,
            debug
         );
      var55 += 20;
      this.resetTraditional
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.loadConfig.traditional"),
            this.x + alignX + 25,
            this.y + var55 + 55,
            "$WHI0",
            "$GRE1",
            WorldMapGUIData.hasCustomConfig(),
            false,
            debug
         );
      var55 += 20;
      this.resetPeaceful
         .render(
            this.g,
            this.mouse,
            Text.getText("worldMapGameModeConfigPanel.loadConfig.peaceful"),
            this.x + alignX + 25,
            this.y + var55 + 55,
            "$WHI0",
            "$GRE1",
            WorldMapGUIData.hasCustomConfig(),
            false,
            debug
         );
      var55 += 20;

      for (int i = 0; i < this.customList.length; i++) {
         if (this.customList[i] != null) {
            this.customList[i]
               .render(
                  this.g,
                  this.mouse,
                  this.customListNames[i],
                  this.x + alignX + 25,
                  this.y + var55 + 55,
                  "$WHI0",
                  "$GRE1",
                  WorldMapGUIData.hasCustomConfig(),
                  false,
                  debug
               );
            this.customListDelete[i].render(this.g, this.mouse, this.x + alignX + 138, this.y + var55 + 55, debug);
            var55 += 20;
         }
      }

      this.save.render(this.g, this.mouse, this.x + alignX + 30, this.y + 458, this.saveInput.getText().equals(""), debug);
      this.font.drawString(this.x + alignX + 80, this.y + 470, Text.getText("worldMapGameModeConfigPanel.loadConfig.configFileName"), Text.FontType.BODY, 0);
      this.saveInput.render(this.g, this.mouse, this.x + alignX + 78, this.y + 485, debug);
      if (!WorldMapGUIData.hasCustomConfig()) {
         this.accept.render(this.g, this.mouse, this.x + alignX + 23, this.y + 508, false, debug);
      } else {
         this.font
            .drawString(
               this.x + alignX + 30,
               this.y + 510,
               Text.wrapString(Text.getText("worldMapGameModeConfigPanel.loadConfig.configBlocked"), Text.FontType.BODY, 1, 132),
               Text.FontType.BODY,
               1
            );
      }

      this.back.render(this.g, this.mouse, this.x + alignX + 23, this.y + 564, false, debug);
   }

   public String getConfigName(int i) {
      return this.customListNames[i];
   }

   public void generateList() throws SlickException {
      for (int i = 0; i < this.customList.length; i++) {
         this.guiButtons.remove(this.customListNames[i]);
         this.guiButtons.remove(this.customListDelete[i]);
         this.guiButtons.remove(this.customList[i]);
         this.customListNames[i] = null;
         this.customListDelete[i] = null;
         this.customList[i] = null;
      }

      File configsFolder = new File("profiles/gameModeConfigs");
      configsFolder.mkdirs();
      File[] listOfConfigs = configsFolder.listFiles();

      for (int i = 0; i < 16; i++) {
         if (listOfConfigs.length > i && !listOfConfigs[i].isDirectory() && listOfConfigs[i].getName().contains("gameMode")) {
            this.customListNames[i] = listOfConfigs[i].getName().replaceAll(".gameMode", "");
            GUIRoundButton configDeleteIcon = new GUIRoundButton("customListDelete" + i, "buttonDismantle", 0);
            this.customListDelete[i] = configDeleteIcon;
            this.guiButtons.add(configDeleteIcon);
            GUIClickableText configText = new GUIClickableText("customList" + i, 2);
            this.customList[i] = configText;
            this.guiButtons.add(configText);
         }
      }
   }

   public void setFocusSaveInput(boolean b) {
      this.saveInput.setFocus(b);
   }

   public boolean hasFocusSaveInput() {
      return this.saveInput.hasFocus();
   }

   public void setSaveInputText(String s) {
      this.saveInput.setText(s);
   }

   public void resetSaveInputText() {
      this.saveInput.setFocus(false);
      this.saveInput.setText("");
   }

   public String getSaveInputText() {
      return this.saveInput.getText();
   }

   @Override
   public int getCenterX() {
      return this.x + this.panel.getWidth() / 2;
   }

   @Override
   public int getCenterY() {
      return this.y + this.panel.getHeight() / 2;
   }

   @Override
   public int getLeftX() {
      return this.x;
   }

   @Override
   public int getTopY() {
      return this.y;
   }

   @Override
   public int getRightX() {
      return this.x + this.panel.getWidth();
   }

   @Override
   public int getBottomY() {
      return this.y + this.panel.getHeight();
   }
}
