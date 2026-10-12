package rtr.gui.states.playstate;

import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.GUIEnums;
import rtr.gui.buttons.GUIButton;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.buttons.GUIButtonStatisticsMobIcon;
import rtr.gui.buttons.GUIButtonStatisticsPageIcon;
import rtr.gui.buttons.GUIButtonStatisticsResourceIcon;
import rtr.gui.buttons.GUIRoundButton;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.mobs.MobBase;
import rtr.resources.ResourceModule;
import rtr.system.ScaleControl;

public class StatisticsPanel extends PlayStateGUIPanelBase {
   public static final int GRAPH_LENGTH = 15;
   private Image panel;
   private Image configurationPanel;
   private Image[] statisticsLargeStatsBar = new Image[15];
   private Image[] statisticsSmallStatsBarLeft = new Image[15];
   private Image[] statisticsSmallStatsBarRight = new Image[15];
   private Rectangle mask;
   private GUIEnums.GUIPanelPage backPage;
   private StatisticsPanel.StatisticsChartType backPageDefaultChart;
   private GUIEnums.GUIPanelPage chartPage;
   private GUIButtonGenericWood back;
   private GUIButtonGenericWood categoryVillagers;
   private GUIButtonGenericWood categoryAnimals;
   private GUIButtonGenericWood categoryMonsters;
   private GUIButtonGenericWood categoryBuildings;
   private GUIButtonGenericWood categoryDefenses;
   private GUIButtonGenericWood categoryResources;
   private GUIButtonGenericWood categoryMagicAndEnergy;
   private GUIButtonGenericWood categoryOther;
   private GUIButtonGenericWood villagersTotal;
   private GUIButtonGenericWood villagersGender;
   private GUIButtonGenericWood villagersCategoryImmigration;
   private GUIButtonGenericWood villagersCategoryReproduction;
   private GUIButtonGenericWood villagersDeaths;
   private GUIButtonGenericWood villagersCategoryNeeds;
   private GUIButtonGenericWood villagersReproductionBirths;
   private GUIButtonGenericWood villagersReproductionGrownUp;
   private GUIButtonGenericWood villagersReproductionCoitus;
   private GUIButtonGenericWood villagersReproductionPregnancies;
   private GUIButtonGenericWood villagersReproductionNewPregnancies;
   private GUIButtonGenericWood villagersImmigrationCatjeetHired;
   private GUIButtonGenericWood villagersImmigrationNomadsJoined;
   private GUIButtonGenericWood villagersImmigrationNomadsVisited;
   private GUIButtonGenericWood villagersNeedsAverageHunger;
   private GUIButtonGenericWood villagersNeedsAverageThirst;
   private GUIButtonGenericWood villagersNeedsAverageEnergy;
   private GUIButtonGenericWood animalsTotal;
   private GUIButtonGenericWood animalsGender;
   private GUIButtonGenericWood animalsDomesticated;
   private GUIButtonGenericWood animalsDeaths;
   private GUIButtonGenericWood animalsCategoryNeeds;
   private GUIButtonGenericWood animalsNeedsAverageHunger;
   private GUIButtonGenericWood animalsNeedsAverageThirst;
   private GUIButtonGenericWood animalsNeedsAverageEnergy;
   private GUIButtonGenericWood buildingsTotal;
   private GUIButtonGenericWood buildingsBuilt;
   private GUIButtonGenericWood buildingsUpgraded;
   private GUIButtonGenericWood buildingsDestroyed;
   private GUIButtonGenericWood buildingsDismantled;
   private GUIButtonGenericWood defensesTowers;
   private GUIButtonGenericWood defensesCombobulators;
   private GUIButtonGenericWood defensesGolems;
   private GUIButtonGenericWood defensesRangers;
   private GUIButtonGenericWood defensesWalls;
   private GUIButtonGenericWood corruptionTotal;
   private GUIButtonGenericWood corruptionSpreadAmount;
   private GUIButtonGenericWood corruptedBuildings;
   private GUIButtonGenericWood dronesTotal;
   private GUIButtonGenericWood monstersSpawned;
   private GUIButtonGenericWood monstersKilled;
   private GUIButtonGenericWood resourcesTotal;
   private GUIButtonGenericWood resourcesProductionRate;
   private GUIButtonGenericWood resourcesFoodAndWaterRate;
   private GUIButtonGenericWood magicAndEnergyEssenceGenerated;
   private GUIButtonGenericWood magicAndEnergyEssenceDecayed;
   private GUIButtonGenericWood magicAndEnergyEnergy;
   private GUIButtonGenericWood magicAndEnergyInfluenceSpent;
   private GUIButtonGenericWood magicAndEnergyInfluenceGenerated;
   private HashMap<StatisticsPanel.StatisticsChartType, GUIButtonGenericWood> chartButtons = new HashMap<>();
   private GUIButtonGenericWood mobListAll;
   private GUIButtonStatisticsPageIcon mobListPreviousPage;
   private GUIButtonStatisticsPageIcon mobListNextPage;
   private ArrayList<GUIButtonStatisticsMobIcon> mobList;
   private int currentMobPage;
   private MobBase.MobType selectedMobType;
   private GUIRoundButton resourceCategoryLeft;
   private GUIRoundButton resourceCategoryRight;
   private ResourceModule.ResourceCategory resourceCategory;
   private GUIButtonGenericWood resourceListAll;
   private GUIButtonStatisticsPageIcon resourceListPreviousPage;
   private GUIButtonStatisticsPageIcon resourceListNextPage;
   private ArrayList<GUIButtonStatisticsResourceIcon> resourcesList;
   private int currentResourcePage;
   private ResourceModule.ResourceType selectedResourceType;
   private GUIRoundButton dayBack;
   private GUIRoundButton dayForward;
   private GUIRoundButton dayFirst;
   private GUIRoundButton dayLast;
   private Rectangle[] graphBarsOne;
   private Rectangle[] graphBarsTwo;
   private int startDay = 0;
   private StatisticsPanel.StatisticsChartType selectedChart;
   private boolean dayMoved;

   public StatisticsPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/gameState/statisticsPanel.png");
      this.configurationPanel = ImageLoader.getImage("res/GUI/gameState/statisticsConfigurationPanel.png");

      for (int i = 0; i < this.statisticsLargeStatsBar.length; i++) {
         this.statisticsLargeStatsBar[i] = ImageLoader.getImage("res/GUI/gameState/statisticsLargeStatsBar" + (i + 1) + ".png");
         this.statisticsSmallStatsBarLeft[i] = ImageLoader.getImage("res/GUI/gameState/statisticsSmallStatsBarLeft" + (i + 1) + ".png");
         this.statisticsSmallStatsBarRight[i] = ImageLoader.getImage("res/GUI/gameState/statisticsSmallStatsBarRight" + (i + 1) + ".png");
      }

      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);
      this.backPage = null;
      this.backPageDefaultChart = null;
      this.chartPage = GUIEnums.GUIPanelPage.STATS_CATEGORY;
      this.back = new GUIButtonGenericWood("back", Text.getText("playStatisticsPanel.button.back"));
      this.guiButtons.add(this.back);
      this.categoryVillagers = new GUIButtonGenericWood("categoryVillagers", Text.getText("playStatisticsPanel.button.primary.villagers"));
      this.guiButtons.add(this.categoryVillagers);
      this.categoryAnimals = new GUIButtonGenericWood("categoryAnimals", Text.getText("playStatisticsPanel.button.primary.animals"));
      this.guiButtons.add(this.categoryAnimals);
      this.categoryMonsters = new GUIButtonGenericWood("categoryMonsters", Text.getText("playStatisticsPanel.button.primary.monsters"));
      this.guiButtons.add(this.categoryMonsters);
      this.categoryBuildings = new GUIButtonGenericWood("categoryBuildings", Text.getText("playStatisticsPanel.button.primary.buildings"));
      this.guiButtons.add(this.categoryBuildings);
      this.categoryDefenses = new GUIButtonGenericWood("categoryDefenses", Text.getText("playStatisticsPanel.button.primary.defenses"));
      this.guiButtons.add(this.categoryDefenses);
      this.categoryResources = new GUIButtonGenericWood("categoryResources", Text.getText("playStatisticsPanel.button.primary.resources"));
      this.guiButtons.add(this.categoryResources);
      this.categoryMagicAndEnergy = new GUIButtonGenericWood("categoryMagicAndEnergy", Text.getText("playStatisticsPanel.button.primary.magicAndEnergy"));
      this.guiButtons.add(this.categoryMagicAndEnergy);
      this.categoryOther = new GUIButtonGenericWood("categoryOther", Text.getText("playStatisticsPanel.button.primary.other"));
      this.guiButtons.add(this.categoryOther);
      this.villagersTotal = new GUIButtonGenericWood("villagersTotal", Text.getText("playStatisticsPanel.button.villagers.total"));
      this.guiButtons.add(this.villagersTotal);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_TOTAL, this.villagersTotal);
      this.villagersGender = new GUIButtonGenericWood("villagersGender", Text.getText("playStatisticsPanel.button.villagers.gender"));
      this.guiButtons.add(this.villagersGender);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_GENDER, this.villagersGender);
      this.villagersCategoryImmigration = new GUIButtonGenericWood(
         "villagersCategoryImmigration", Text.getText("playStatisticsPanel.button.villagers.immigration")
      );
      this.guiButtons.add(this.villagersCategoryImmigration);
      this.villagersCategoryReproduction = new GUIButtonGenericWood(
         "villagersCategoryReproduction", Text.getText("playStatisticsPanel.button.villagers.reproduction")
      );
      this.guiButtons.add(this.villagersCategoryReproduction);
      this.villagersDeaths = new GUIButtonGenericWood("villagersDeaths", Text.getText("playStatisticsPanel.button.villagers.deaths"));
      this.guiButtons.add(this.villagersDeaths);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_DEATHS, this.villagersDeaths);
      this.villagersCategoryNeeds = new GUIButtonGenericWood("villagersCategoryNeeds", Text.getText("playStatisticsPanel.button.villagers.needs"));
      this.guiButtons.add(this.villagersCategoryNeeds);
      this.villagersImmigrationCatjeetHired = new GUIButtonGenericWood(
         "villagersImmigrationCatjeetHired", Text.getText("playStatisticsPanel.button.villagers.catjeetHired")
      );
      this.guiButtons.add(this.villagersImmigrationCatjeetHired);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_IMMIGRATION_CATJEET_HIRED, this.villagersImmigrationCatjeetHired);
      this.villagersImmigrationNomadsJoined = new GUIButtonGenericWood(
         "villagersImmigrationNomadsJoined", Text.getText("playStatisticsPanel.button.villagers.nomadsJoined")
      );
      this.guiButtons.add(this.villagersImmigrationNomadsJoined);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_IMMIGRATION_NOMADS_JOINED, this.villagersImmigrationNomadsJoined);
      this.villagersImmigrationNomadsVisited = new GUIButtonGenericWood(
         "villagersImmigrationNomadsVisited", Text.getText("playStatisticsPanel.button.villagers.nomadsVisited")
      );
      this.guiButtons.add(this.villagersImmigrationNomadsVisited);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_IMMIGRATION_NOMADS_VISITED, this.villagersImmigrationNomadsVisited);
      this.villagersReproductionBirths = new GUIButtonGenericWood("villagersReproductionBirths", Text.getText("playStatisticsPanel.button.villagers.births"));
      this.guiButtons.add(this.villagersReproductionBirths);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_BIRTHS, this.villagersReproductionBirths);
      this.villagersReproductionGrownUp = new GUIButtonGenericWood("villagersReproductionGrownUp", Text.getText("playStatisticsPanel.button.villagers.grownUp"));
      this.guiButtons.add(this.villagersReproductionGrownUp);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_GROWN_UP, this.villagersReproductionGrownUp);
      this.villagersReproductionCoitus = new GUIButtonGenericWood("villagersReproductionCoitus", Text.getText("playStatisticsPanel.button.villagers.coitus"));
      this.guiButtons.add(this.villagersReproductionCoitus);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_COITUS, this.villagersReproductionCoitus);
      this.villagersReproductionPregnancies = new GUIButtonGenericWood(
         "villagersReproductionPregnancies", Text.getText("playStatisticsPanel.button.villagers.pregnancies")
      );
      this.guiButtons.add(this.villagersReproductionPregnancies);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_PREGNANCIES, this.villagersReproductionPregnancies);
      this.villagersReproductionNewPregnancies = new GUIButtonGenericWood(
         "villagersReproductionNewPregnancies", Text.getText("playStatisticsPanel.button.villagers.newPregnancies")
      );
      this.guiButtons.add(this.villagersReproductionNewPregnancies);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_NEW_PREGNANCIES, this.villagersReproductionNewPregnancies);
      this.villagersNeedsAverageHunger = new GUIButtonGenericWood(
         "villagersNeedsAverageHunger", Text.getText("playStatisticsPanel.button.villagers.averageHunger")
      );
      this.guiButtons.add(this.villagersNeedsAverageHunger);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_NEEDS_AVERAGE_HUNGER, this.villagersNeedsAverageHunger);
      this.villagersNeedsAverageThirst = new GUIButtonGenericWood(
         "villagersNeedsAverageThirst", Text.getText("playStatisticsPanel.button.villagers.averageThirst")
      );
      this.guiButtons.add(this.villagersNeedsAverageThirst);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_NEEDS_AVERAGE_THIRST, this.villagersNeedsAverageThirst);
      this.villagersNeedsAverageEnergy = new GUIButtonGenericWood(
         "villagersNeedsAverageEnergy", Text.getText("playStatisticsPanel.button.villagers.averageEnergy")
      );
      this.guiButtons.add(this.villagersNeedsAverageEnergy);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.VILLAGERS_NEEDS_AVERAGE_ENERGY, this.villagersNeedsAverageEnergy);
      this.animalsTotal = new GUIButtonGenericWood("animalsTotal", Text.getText("playStatisticsPanel.button.animals.total"));
      this.guiButtons.add(this.animalsTotal);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.ANIMALS_TOTAL, this.animalsTotal);
      this.animalsGender = new GUIButtonGenericWood("animalsGender", Text.getText("playStatisticsPanel.button.animals.gender"));
      this.guiButtons.add(this.animalsGender);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.ANIMALS_GENDER, this.animalsGender);
      this.animalsDomesticated = new GUIButtonGenericWood("animalsDomesticated", Text.getText("playStatisticsPanel.button.animals.domesticated"));
      this.guiButtons.add(this.animalsDomesticated);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.ANIMALS_DOMESTICATED, this.animalsDomesticated);
      this.animalsDeaths = new GUIButtonGenericWood("animalsDeaths", Text.getText("playStatisticsPanel.button.animals.deaths"));
      this.guiButtons.add(this.animalsDeaths);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.ANIMALS_DEATHS, this.animalsDeaths);
      this.animalsCategoryNeeds = new GUIButtonGenericWood("animalsCategoryNeeds", Text.getText("playStatisticsPanel.button.animals.needs"));
      this.guiButtons.add(this.animalsCategoryNeeds);
      this.animalsNeedsAverageHunger = new GUIButtonGenericWood("animalsNeedsAverageHunger", Text.getText("playStatisticsPanel.button.animals.averageHunger"));
      this.guiButtons.add(this.animalsNeedsAverageHunger);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.ANIMALS_NEEDS_AVERAGE_HUNGER, this.animalsNeedsAverageHunger);
      this.animalsNeedsAverageThirst = new GUIButtonGenericWood("animalsNeedsAverageThirst", Text.getText("playStatisticsPanel.button.animals.averageThirst"));
      this.guiButtons.add(this.animalsNeedsAverageThirst);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.ANIMALS_NEEDS_AVERAGE_THIRST, this.animalsNeedsAverageThirst);
      this.animalsNeedsAverageEnergy = new GUIButtonGenericWood("animalsNeedsAverageEnergy", Text.getText("playStatisticsPanel.button.animals.averageEnergy"));
      this.guiButtons.add(this.animalsNeedsAverageEnergy);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.ANIMALS_NEEDS_AVERAGE_ENERGY, this.animalsNeedsAverageEnergy);
      this.buildingsTotal = new GUIButtonGenericWood("buildingsTotal", Text.getText("playStatisticsPanel.button.buildings.totalBuildings"));
      this.guiButtons.add(this.buildingsTotal);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.BUILDINGS_TOTAL, this.buildingsTotal);
      this.buildingsBuilt = new GUIButtonGenericWood("buildingsBuilt", Text.getText("playStatisticsPanel.button.buildings.builtBuildings"));
      this.guiButtons.add(this.buildingsBuilt);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.BUILDINGS_BUILT, this.buildingsBuilt);
      this.buildingsUpgraded = new GUIButtonGenericWood("buildingsUpgraded", Text.getText("playStatisticsPanel.button.buildings.upgradedBuildings"));
      this.guiButtons.add(this.buildingsUpgraded);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.BUILDINGS_UPGRADED, this.buildingsUpgraded);
      this.buildingsDestroyed = new GUIButtonGenericWood("buildingsDestroyed", Text.getText("playStatisticsPanel.button.buildings.destroyedBuildings"));
      this.guiButtons.add(this.buildingsDestroyed);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.BUILDINGS_DESTROYED, this.buildingsDestroyed);
      this.buildingsDismantled = new GUIButtonGenericWood("buildingsDismantled", Text.getText("playStatisticsPanel.button.buildings.dismantledBuildings"));
      this.guiButtons.add(this.buildingsDismantled);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.BUILDINGS_DISMANTLED, this.buildingsDismantled);
      this.defensesTowers = new GUIButtonGenericWood("defensesTowers", Text.getText("playStatisticsPanel.button.defenses.totalDefense"));
      this.guiButtons.add(this.defensesTowers);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.DEFENSES_TOWERS, this.defensesTowers);
      this.defensesCombobulators = new GUIButtonGenericWood("defensesCombobulators", Text.getText("playStatisticsPanel.button.defenses.combobulators"));
      this.guiButtons.add(this.defensesCombobulators);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.DEFENSES_COMBOBULATORS, this.defensesCombobulators);
      this.defensesGolems = new GUIButtonGenericWood("defensesGolems", Text.getText("playStatisticsPanel.button.defenses.golems"));
      this.guiButtons.add(this.defensesGolems);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.DEFENSES_GOLEMS, this.defensesGolems);
      this.defensesRangers = new GUIButtonGenericWood("defensesRangers", Text.getText("playStatisticsPanel.button.defenses.rangers"));
      this.guiButtons.add(this.defensesRangers);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.DEFENSES_RANGERS, this.defensesRangers);
      this.defensesWalls = new GUIButtonGenericWood("defensesWalls", Text.getText("playStatisticsPanel.button.defenses.walls"));
      this.guiButtons.add(this.defensesWalls);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.DEFENSES_WALLS, this.defensesWalls);
      this.corruptionTotal = new GUIButtonGenericWood("corruptionTotal", Text.getText("playStatisticsPanel.button.monsters.totalCorruption"));
      this.guiButtons.add(this.corruptionTotal);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.CORRUPTION_TOTAL, this.corruptionTotal);
      this.corruptionSpreadAmount = new GUIButtonGenericWood("corruptionSpreadAmount", Text.getText("playStatisticsPanel.button.monsters.spreadAmount"));
      this.guiButtons.add(this.corruptionSpreadAmount);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.CORRUPTION_SPREAD_AMOUNT, this.corruptionSpreadAmount);
      this.corruptedBuildings = new GUIButtonGenericWood("corruptedBuildings", Text.getText("playStatisticsPanel.button.monsters.buildings"));
      this.guiButtons.add(this.corruptedBuildings);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.CORRUPTED_BUILDINGS, this.corruptedBuildings);
      this.dronesTotal = new GUIButtonGenericWood("dronesTotal", Text.getText("playStatisticsPanel.button.monsters.drones"));
      this.guiButtons.add(this.dronesTotal);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.DRONES_TOTAL, this.dronesTotal);
      this.monstersSpawned = new GUIButtonGenericWood("monstersSpawned", Text.getText("playStatisticsPanel.button.monsters.spawned"));
      this.guiButtons.add(this.monstersSpawned);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.MONSTERS_SPAWNED, this.monstersSpawned);
      this.monstersKilled = new GUIButtonGenericWood("monstersKilled", Text.getText("playStatisticsPanel.button.monsters.killed"));
      this.guiButtons.add(this.monstersKilled);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.MONSTERS_KILLED, this.monstersKilled);
      this.resourcesTotal = new GUIButtonGenericWood("resourcesTotal", Text.getText("playStatisticsPanel.button.resources.totalResources"));
      this.guiButtons.add(this.resourcesTotal);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.RESOURCES_TOTAL, this.resourcesTotal);
      this.resourcesProductionRate = new GUIButtonGenericWood("resourcesProductionRate", Text.getText("playStatisticsPanel.button.resources.productionRate"));
      this.guiButtons.add(this.resourcesProductionRate);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.RESOURCES_PRODUCTION_RATE, this.resourcesProductionRate);
      this.resourcesFoodAndWaterRate = new GUIButtonGenericWood("resourcesFoodAndWaterRate", Text.getText("playStatisticsPanel.button.resources.foodAndWater"));
      this.guiButtons.add(this.resourcesFoodAndWaterRate);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.RESOURCES_FOOD_AND_WATER_RATE, this.resourcesFoodAndWaterRate);
      this.magicAndEnergyEssenceGenerated = new GUIButtonGenericWood(
         "magicAndEnergyEssenceGenerated", Text.getText("playStatisticsPanel.button.magicAndEnergy.essenceGenerated")
      );
      this.guiButtons.add(this.magicAndEnergyEssenceGenerated);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.MAGIC_AND_ENERGY_ESSENCE_GENERATED, this.magicAndEnergyEssenceGenerated);
      this.magicAndEnergyEssenceDecayed = new GUIButtonGenericWood(
         "magicAndEnergyEssenceDecayed", Text.getText("playStatisticsPanel.button.magicAndEnergy.essenceDecayed")
      );
      this.guiButtons.add(this.magicAndEnergyEssenceDecayed);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.MAGIC_AND_ENERGY_ESSENCE_DECAYED, this.magicAndEnergyEssenceDecayed);
      this.magicAndEnergyEnergy = new GUIButtonGenericWood("magicAndEnergyEnergy", Text.getText("playStatisticsPanel.button.magicAndEnergy.energy"));
      this.guiButtons.add(this.magicAndEnergyEnergy);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.MAGIC_AND_ENERGY_ENERGY, this.magicAndEnergyEnergy);
      this.magicAndEnergyInfluenceSpent = new GUIButtonGenericWood(
         "magicAndEnergyInfluenceSpent", Text.getText("playStatisticsPanel.button.magicAndEnergy.influenceSpent")
      );
      this.guiButtons.add(this.magicAndEnergyInfluenceSpent);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.MAGIC_AND_ENERGY_INFLUENCE_SPENT, this.magicAndEnergyInfluenceSpent);
      this.magicAndEnergyInfluenceGenerated = new GUIButtonGenericWood(
         "magicAndEnergyInfluenceGenerated", Text.getText("playStatisticsPanel.button.magicAndEnergy.influenceGenerated")
      );
      this.guiButtons.add(this.magicAndEnergyInfluenceGenerated);
      this.chartButtons.put(StatisticsPanel.StatisticsChartType.MAGIC_AND_ENERGY_INFLUENCE_GENERATED, this.magicAndEnergyInfluenceGenerated);
      this.mobListAll = new GUIButtonGenericWood("mobListAll", Text.getText("playStatisticsPanel.button.filter.all"));
      this.guiButtons.add(this.mobListAll);
      this.mobListPreviousPage = new GUIButtonStatisticsPageIcon("mobListPreviousPage", "last");
      this.mobListPreviousPage.setToolTip(Text.getText("playStatisticsPanel.button.filter.previousPage"));
      this.guiButtons.add(this.mobListPreviousPage);
      this.mobListNextPage = new GUIButtonStatisticsPageIcon("mobListNextPage", "next");
      this.mobListNextPage.setToolTip(Text.getText("playStatisticsPanel.button.filter.nextPage"));
      this.guiButtons.add(this.mobListNextPage);
      this.mobList = new ArrayList<>();

      MobBase.MobType[] var9;
      for (MobBase.MobType t : MobBase.MobType.values()) {
         GUIButtonStatisticsMobIcon button = new GUIButtonStatisticsMobIcon("mobList" + t.getName(), t);
         button.setToolTip(t.getNamePlural());
         this.mobList.add(button);
         this.guiButtons.add(button);
      }

      this.resourceListAll = new GUIButtonGenericWood("resourceListAll", Text.getText("playStatisticsPanel.button.filter.all"));
      this.guiButtons.add(this.resourceListAll);
      this.resourceListPreviousPage = new GUIButtonStatisticsPageIcon("resourceListPreviousPage", "last");
      this.resourceListPreviousPage.setToolTip(Text.getText("playStatisticsPanel.button.filter.previousPage"));
      this.guiButtons.add(this.resourceListPreviousPage);
      this.resourceListNextPage = new GUIButtonStatisticsPageIcon("resourceListNextPage", "next");
      this.resourceListNextPage.setToolTip(Text.getText("playStatisticsPanel.button.filter.nextPage"));
      this.guiButtons.add(this.resourceListNextPage);
      this.resourceCategoryLeft = new GUIRoundButton("resourceCategoryLeft", "buttonLeft", 0);
      this.resourceCategoryLeft.setToolTip(Text.getText("playStatisticsPanel.button.filter.previousCategory"));
      this.guiButtons.add(this.resourceCategoryLeft);
      this.resourceCategoryRight = new GUIRoundButton("resourceCategoryRight", "buttonRight", 0);
      this.resourceCategoryRight.setToolTip(Text.getText("playStatisticsPanel.button.filter.nextCategory"));
      this.guiButtons.add(this.resourceCategoryRight);
      this.resourceCategory = ResourceModule.ResourceCategory.RAW;
      this.resourcesList = new ArrayList<>();

      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         GUIButtonStatisticsResourceIcon button = new GUIButtonStatisticsResourceIcon("resourceList" + t.getTemplate().getName(), t);
         button.setToolTip(t.getTemplate().getNamePlural(), t.getTemplate().getDescription());
         this.resourcesList.add(button);
         this.guiButtons.add(button);
      }

      this.graphBarsOne = new Rectangle[15];

      for (int i = 0; i < this.graphBarsOne.length; i++) {
         this.graphBarsOne[i] = new Rectangle(0.0F, 0.0F, 1.0F, 1.0F);
      }

      this.graphBarsTwo = new Rectangle[15];

      for (int i = 0; i < this.graphBarsTwo.length; i++) {
         this.graphBarsTwo[i] = new Rectangle(0.0F, 0.0F, 1.0F, 1.0F);
      }

      this.selectedChart = StatisticsPanel.StatisticsChartType.VILLAGERS_TOTAL;
      this.dayBack = new GUIRoundButton("dayBack", "buttonLeft", 0);
      this.dayBack.setToolTip(Text.getText("playStatisticsPanel.button.graph.dayBack"));
      this.guiButtons.add(this.dayBack);
      this.dayForward = new GUIRoundButton("dayForward", "buttonRight", 0);
      this.dayForward.setToolTip(Text.getText("playStatisticsPanel.button.graph.dayForward"));
      this.guiButtons.add(this.dayForward);
      this.dayFirst = new GUIRoundButton("dayFirst", "buttonLast", 0);
      this.dayFirst.setToolTip(Text.getText("playStatisticsPanel.button.graph.firstDay"));
      this.guiButtons.add(this.dayFirst);
      this.dayLast = new GUIRoundButton("dayLast", "buttonFirst", 0);
      this.dayLast.setToolTip(Text.getText("playStatisticsPanel.button.graph.lastDay"));
      this.guiButtons.add(this.dayLast);
   }

   public void render(boolean gamePaused, boolean debug) throws SlickException {
      if (gamePaused) {
         this.x = ScaleControl.getInterfaceWidth() / 2 - this.panel.getWidth() / 2;
         this.y = 6;
      } else {
         this.x = this.controller.getResourceAndInfluenceBar().getRightX() - this.panel.getWidth() + 170;
         if (this.x + this.panel.getWidth() > this.controller.getRightPanel().getLeftX() - 46) {
            this.x = this.x - Math.abs(this.x + this.panel.getWidth() - this.controller.getRightPanel().getLeftX() + 46);
         }

         this.y = 133;
      }

      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      this.renderCategoryPage(this.x, this.y, debug);
      this.font.drawString(this.x + 274, this.y + 3, Text.getText("playStatisticsPanel.header.statistics"), Text.FontType.HEADER, 2, true);
      if (!this.dayMoved && this.startDay != this.time.getDay() - 15) {
         this.resetGraph();
      }

      ArrayList<Integer> chartOutOne = null;
      ArrayList<Integer> chartOutTwo = null;
      Color chartOneColorOne = Color.green;
      Color chartOneColorTwo = Color.red;
      Color chartTwoColorOne = Color.green;
      Color chartTwoColorTwo = Color.red;
      String nameOne = null;
      String nameTwo = null;
      String chartDescription = "";
      boolean useNegative = false;
      switch (this.selectedChart) {
         case VILLAGERS_TOTAL:
            chartOutOne = this.stats.getVillagerTotal(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.total");
            break;
         case VILLAGERS_GENDER:
            chartOutOne = this.stats.getVillagerMale(this.selectedMobType);
            chartOutTwo = this.stats.getVillagerFemale(this.selectedMobType);
            chartOneColorOne = Color.cyan;
            chartOneColorTwo = Color.cyan.darker(0.5F);
            chartTwoColorOne = Color.magenta;
            chartTwoColorTwo = Color.magenta.darker(0.5F);
            nameOne = Text.getText("playStatisticsPanel.descriptions.villagers.genderBarOne");
            nameTwo = Text.getText("playStatisticsPanel.descriptions.villagers.genderBarTwo");
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.gender");
            break;
         case VILLAGERS_DEATHS:
            chartOutOne = this.stats.getVillagerDeaths(this.selectedMobType);
            chartOneColorOne = Color.red;
            chartOneColorTwo = Color.green;
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.deaths");
            break;
         case VILLAGERS_IMMIGRATION_CATJEET_HIRED:
            chartOutOne = this.stats.getCatjeetHired(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.catjeetHired");
            break;
         case VILLAGERS_IMMIGRATION_NOMADS_JOINED:
            chartOutOne = this.stats.getNomadsJoined(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.nomadsJoined");
            break;
         case VILLAGERS_IMMIGRATION_NOMADS_VISITED:
            chartOutOne = this.stats.getNomadsVisited(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.nomadsVisited");
            break;
         case VILLAGERS_REPRODUCTION_BIRTHS:
            chartOutOne = this.stats.getVillagerBirths(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.births");
            break;
         case VILLAGERS_REPRODUCTION_GROWN_UP:
            chartOutOne = this.stats.getVillagerGrownUp(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.grownUp");
            break;
         case VILLAGERS_REPRODUCTION_COITUS:
            chartOutOne = this.stats.getVillagerCoitus(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.coitus");
            break;
         case VILLAGERS_REPRODUCTION_PREGNANCIES:
            chartOutOne = this.stats.getVillagerCurrentPregnancies(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.pregnancies");
            break;
         case VILLAGERS_REPRODUCTION_NEW_PREGNANCIES:
            chartOutOne = this.stats.getVillagerNewPregnancies(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.newPregnancies");
            break;
         case VILLAGERS_NEEDS_AVERAGE_HUNGER:
            chartOutOne = this.stats.getVillagerAverageHunger(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.averageHunger");
            break;
         case VILLAGERS_NEEDS_AVERAGE_THIRST:
            chartOutOne = this.stats.getVillagerAverageThirst(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.averageThirst");
            break;
         case VILLAGERS_NEEDS_AVERAGE_ENERGY:
            chartOutOne = this.stats.getVillagerAverageEnergy(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.villagers.averageEnergy");
            break;
         case ANIMALS_TOTAL:
            chartOutOne = this.stats.getAnimalTotal(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.animals.total");
            break;
         case ANIMALS_GENDER:
            chartOutOne = this.stats.getAnimalMale(this.selectedMobType);
            chartOutTwo = this.stats.getAnimalFemale(this.selectedMobType);
            chartOneColorOne = Color.cyan;
            chartOneColorTwo = Color.cyan.darker(0.5F);
            chartTwoColorOne = Color.magenta;
            chartTwoColorTwo = Color.magenta.darker(0.5F);
            nameOne = Text.getText("playStatisticsPanel.descriptions.animals.genderBarOne");
            nameTwo = Text.getText("playStatisticsPanel.descriptions.animals.genderBarTwo");
            chartDescription = Text.getText("playStatisticsPanel.descriptions.animals.gender");
            break;
         case ANIMALS_DEATHS:
            chartOutOne = this.stats.getAnimalDeaths(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.animals.deaths");
            break;
         case ANIMALS_DOMESTICATED:
            chartOutOne = this.stats.getAnimalDomesticated(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.animals.domesticated");
            break;
         case ANIMALS_NEEDS_AVERAGE_HUNGER:
            chartOutOne = this.stats.getAnimalAverageHunger(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.animals.averageHunger");
            break;
         case ANIMALS_NEEDS_AVERAGE_THIRST:
            chartOutOne = this.stats.getAnimalAverageThirst(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.animals.averageThirst");
            break;
         case ANIMALS_NEEDS_AVERAGE_ENERGY:
            chartOutOne = this.stats.getAnimalAverageEnergy(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.animals.averageEnergy");
            break;
         case CORRUPTION_TOTAL:
            chartOutOne = this.stats.getCorruptionTotal();
            nameOne = Text.getText("playStatisticsPanel.descriptions.monsters.totalCorruptionBar");
            chartDescription = Text.getText("playStatisticsPanel.descriptions.monsters.totalCorruption");
            break;
         case CORRUPTION_SPREAD_AMOUNT:
            chartOutOne = this.stats.getCorruptionSpreadAmount();
            nameOne = Text.getText("playStatisticsPanel.descriptions.monsters.spreadAmountBar");
            chartDescription = Text.getText("playStatisticsPanel.descriptions.monsters.spreadAmount");
            break;
         case CORRUPTED_BUILDINGS:
            chartOutOne = this.stats.getCorruptedBuildings();
            chartDescription = Text.getText("playStatisticsPanel.descriptions.monsters.buildings");
            break;
         case DRONES_TOTAL:
            chartOutOne = this.stats.getDronesTotal();
            chartDescription = Text.getText("playStatisticsPanel.descriptions.monsters.drones");
            break;
         case MONSTERS_SPAWNED:
            chartOutOne = this.stats.getMonstersSpawned(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.monsters.spawned");
            break;
         case MONSTERS_KILLED:
            chartOutOne = this.stats.getMonstersKilled(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.monsters.killed");
            break;
         case BUILDINGS_TOTAL:
            chartOutOne = this.stats.getBuildingsTotal();
            chartOutTwo = this.stats.getBuildingsSlots();
            chartTwoColorOne = Color.white;
            chartTwoColorTwo = Color.gray;
            nameOne = Text.getText("playStatisticsPanel.descriptions.buildings.totalBuildingsBarOne");
            nameTwo = Text.getText("playStatisticsPanel.descriptions.buildings.totalBuildingsBarTwo");
            chartDescription = Text.getText("playStatisticsPanel.descriptions.buildings.totalBuildings");
            break;
         case BUILDINGS_BUILT:
            chartOutOne = this.stats.getBuildingsBuilt();
            chartDescription = Text.getText("playStatisticsPanel.descriptions.buildings.builtBuildings");
            break;
         case BUILDINGS_UPGRADED:
            chartOutOne = this.stats.getBuildingsUpgraded();
            chartDescription = Text.getText("playStatisticsPanel.descriptions.buildings.upgradedBuildings");
            break;
         case BUILDINGS_DESTROYED:
            chartOutOne = this.stats.getBuildingsDestroyed();
            chartOneColorOne = Color.red;
            chartOneColorTwo = Color.green;
            chartDescription = Text.getText("playStatisticsPanel.descriptions.buildings.destroyedBuildings");
            break;
         case BUILDINGS_DISMANTLED:
            chartOutOne = this.stats.getBuildingsDismantled();
            chartOneColorOne = Color.red;
            chartOneColorTwo = Color.green;
            chartDescription = Text.getText("playStatisticsPanel.descriptions.buildings.dismantledBuildings");
            break;
         case DEFENSES_TOWERS:
            chartOutOne = this.stats.getTowersTotal();
            chartDescription = Text.getText("playStatisticsPanel.descriptions.defenses.totalDefense");
            break;
         case DEFENSES_COMBOBULATORS:
            chartOutOne = this.stats.getGolemTotal(null);
            chartOutTwo = this.stats.getCombobulatorsTotal();
            chartTwoColorOne = Color.cyan;
            chartTwoColorTwo = Color.red;
            nameOne = Text.getText("playStatisticsPanel.descriptions.defenses.combobulatorsBarOne");
            nameTwo = Text.getText("playStatisticsPanel.descriptions.defenses.combobulatorsBarTwo");
            chartDescription = Text.getText("playStatisticsPanel.descriptions.defenses.combobulators");
            break;
         case DEFENSES_GOLEMS:
            chartOutOne = this.stats.getGolemTotal(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.defenses.golems");
            break;
         case DEFENSES_RANGERS:
            chartOutOne = this.stats.getRangerTotal(this.selectedMobType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.defenses.rangers");
            break;
         case DEFENSES_WALLS:
            chartOutOne = this.stats.getWallsTotal();
            chartDescription = Text.getText("playStatisticsPanel.descriptions.defenses.walls");
            break;
         case RESOURCES_TOTAL:
            chartOutOne = this.stats.getResourcesTotal(this.selectedResourceType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.resources.totalResources");
            break;
         case RESOURCES_PRODUCTION_RATE:
            chartOutOne = this.stats.getResourcesProductionRate(this.selectedResourceType);
            chartDescription = Text.getText("playStatisticsPanel.descriptions.resources.productionRate");
            useNegative = true;
            break;
         case RESOURCES_FOOD_AND_WATER_RATE:
            chartOutOne = this.stats.getFoodProductionRate();
            chartOutTwo = this.stats.getWaterProductionRate();
            chartOneColorOne = Color.green;
            chartOneColorTwo = Color.red;
            chartTwoColorOne = Color.cyan;
            chartTwoColorTwo = Color.red;
            nameOne = Text.getText("playStatisticsPanel.descriptions.resources.foodAndWaterBarOne");
            nameTwo = Text.getText("playStatisticsPanel.descriptions.resources.foodAndWaterBarTwo");
            chartDescription = Text.getText("playStatisticsPanel.descriptions.resources.foodAndWater");
            useNegative = true;
            break;
         case MAGIC_AND_ENERGY_ESSENCE_GENERATED:
            chartOutOne = this.stats.getEssenceGenerated();
            chartDescription = Text.getText("playStatisticsPanel.descriptions.magicAndEnergy.essenceGenerated");
            break;
         case MAGIC_AND_ENERGY_ESSENCE_DECAYED:
            chartOutOne = this.stats.getEssenceDecayed();
            chartDescription = Text.getText("playStatisticsPanel.descriptions.magicAndEnergy.essenceDecayed");
            break;
         case MAGIC_AND_ENERGY_ENERGY:
            chartOutOne = this.stats.getEnergyAmount();
            chartOutTwo = this.stats.getEnergyMaximum();
            chartTwoColorOne = Color.white;
            chartTwoColorTwo = Color.red;
            nameOne = Text.getText("playStatisticsPanel.descriptions.magicAndEnergy.energyBarOne");
            nameTwo = Text.getText("playStatisticsPanel.descriptions.magicAndEnergy.energyBarTwo");
            chartDescription = Text.getText("playStatisticsPanel.descriptions.magicAndEnergy.energy");
            break;
         case MAGIC_AND_ENERGY_INFLUENCE_SPENT:
            chartOutOne = this.stats.getInfluenceSpent();
            chartDescription = Text.getText("playStatisticsPanel.descriptions.magicAndEnergy.influenceSpent");
            break;
         case MAGIC_AND_ENERGY_INFLUENCE_GENERATED:
            chartOutOne = this.stats.getInfluenceGenerated();
            chartDescription = Text.getText("playStatisticsPanel.descriptions.magicAndEnergy.influenceGenerated");
      }

      if (chartOutOne != null) {
         this.font.drawString(this.x + 423, this.y + 30, Text.wrapString(chartDescription, Text.FontType.BODY, 2, 435), Text.FontType.BODY, 2, true);
         if (useNegative) {
            this.renderChartNegatives(chartOutOne, chartOutTwo, chartOneColorOne, chartOneColorTwo, chartTwoColorOne, chartTwoColorTwo, nameOne, nameTwo);
         } else {
            this.renderChart(chartOutOne, chartOutTwo, chartOneColorOne, chartOneColorTwo, chartTwoColorOne, chartTwoColorTwo, nameOne, nameTwo);
         }

         if (this.selectedMobType != null) {
            Text.setVariableText("mobOrResourceName", this.selectedMobType.getNamePlural());
            this.font.drawString(this.x + 290, this.y + 82, Text.getText("playStatisticsPanel.filter"), Text.FontType.BODY, 1);
         }

         if (this.selectedResourceType != null) {
            Text.setVariableText("mobOrResourceName", this.selectedResourceType.getTemplate().getNamePlural());
            this.font.drawString(this.x + 290, this.y + 82, Text.getText("playStatisticsPanel.filter"), Text.FontType.BODY, 1);
         }
      }

      this.dayBack.render(this.g, this.mouse, this.x + 263, this.y + 80, debug);
      this.dayForward.render(this.g, this.mouse, this.x + 595, this.y + 80, debug);
      this.dayFirst.render(this.g, this.mouse, this.x + 238, this.y + 80, debug);
      this.dayLast.render(this.g, this.mouse, this.x + 620, this.y + 80, debug);
   }

   private void renderCategoryPage(int x, int y, boolean debug) throws SlickException {
      int alignY = 30;
      if (this.chartPage == GUIEnums.GUIPanelPage.STATS_CATEGORY) {
         this.backPage = null;
         this.backPageDefaultChart = null;
         this.font.drawString(x + 93, y + 3, Text.getText("playStatisticsPanel.header.category"), Text.FontType.HEADER, 2, true);
         this.categoryVillagers.render(this.g, this.mouse, x + 56, y + alignY, false, true, debug);
         this.categoryAnimals.render(this.g, this.mouse, x + 132, y + alignY, false, true, debug);
         alignY += 26;
         this.categoryMonsters.render(this.g, this.mouse, x + 56, y + alignY, false, true, debug);
         this.categoryBuildings.render(this.g, this.mouse, x + 132, y + alignY, false, true, debug);
         alignY += 26;
         this.categoryDefenses.render(this.g, this.mouse, x + 56, y + alignY, false, true, debug);
         this.categoryResources.render(this.g, this.mouse, x + 132, y + alignY, false, true, debug);
         alignY += 26;
         this.categoryMagicAndEnergy.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
      }

      if (this.chartPage == GUIEnums.GUIPanelPage.STATS_VILLAGERS) {
         this.backPage = GUIEnums.GUIPanelPage.STATS_CATEGORY;
         this.backPageDefaultChart = StatisticsPanel.StatisticsChartType.VILLAGERS_TOTAL;
         this.font.drawString(x + 93, y + 3, Text.getText("playStatisticsPanel.header.villagers"), Text.FontType.HEADER, 2, true);
         this.back.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 34;
         this.villagersTotal.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.villagersGender.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.villagersCategoryImmigration.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.villagersCategoryReproduction.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.villagersDeaths.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.villagersCategoryNeeds.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.renderMobListConfig(MobBase.MobGroup.VILLAGER, debug);
      }

      if (this.chartPage == GUIEnums.GUIPanelPage.STATS_VILLAGERS_CATEGORY_IMMIGRATION) {
         this.backPage = GUIEnums.GUIPanelPage.STATS_VILLAGERS;
         this.backPageDefaultChart = StatisticsPanel.StatisticsChartType.VILLAGERS_IMMIGRATION_NOMADS_JOINED;
         this.font.drawString(x + 93, y + 3, Text.getText("playStatisticsPanel.header.immigration"), Text.FontType.HEADER, 2, true);
         this.back.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 34;
         this.villagersImmigrationCatjeetHired.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.villagersImmigrationNomadsJoined.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.villagersImmigrationNomadsVisited.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         if (this.selectedChart == StatisticsPanel.StatisticsChartType.VILLAGERS_IMMIGRATION_NOMADS_JOINED
            || this.selectedChart == StatisticsPanel.StatisticsChartType.VILLAGERS_IMMIGRATION_NOMADS_VISITED) {
            this.renderMobListConfig(MobBase.MobGroup.NOMAD, debug);
         }
      }

      if (this.chartPage == GUIEnums.GUIPanelPage.STATS_VILLAGERS_CATEGORY_REPRODUCTION) {
         this.backPage = GUIEnums.GUIPanelPage.STATS_VILLAGERS;
         this.backPageDefaultChart = StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_BIRTHS;
         this.font.drawString(x + 93, y + 3, Text.getText("playStatisticsPanel.header.reproduction"), Text.FontType.HEADER, 2, true);
         this.back.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 34;
         this.villagersReproductionBirths.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.villagersReproductionGrownUp.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.villagersReproductionCoitus.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.villagersReproductionPregnancies.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.villagersReproductionNewPregnancies.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         if (this.selectedChart != StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_BIRTHS
            && this.selectedChart != StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_GROWN_UP) {
            this.renderVillagerAdultListConfig(debug);
         } else {
            this.renderVillagerChildrenListConfig(debug);
         }
      }

      if (this.chartPage == GUIEnums.GUIPanelPage.STATS_VILLAGERS_CATEGORY_NEEDS) {
         this.backPage = GUIEnums.GUIPanelPage.STATS_VILLAGERS;
         this.backPageDefaultChart = StatisticsPanel.StatisticsChartType.VILLAGERS_NEEDS_AVERAGE_HUNGER;
         this.font.drawString(x + 93, y + 3, Text.getText("playStatisticsPanel.header.needs"), Text.FontType.HEADER, 2, true);
         this.back.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 34;
         this.villagersNeedsAverageHunger.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.villagersNeedsAverageThirst.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.villagersNeedsAverageEnergy.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.renderMobListConfig(MobBase.MobGroup.VILLAGER, debug);
      }

      if (this.chartPage == GUIEnums.GUIPanelPage.STATS_ANIMALS) {
         this.backPage = GUIEnums.GUIPanelPage.STATS_CATEGORY;
         this.backPageDefaultChart = StatisticsPanel.StatisticsChartType.ANIMALS_TOTAL;
         this.font.drawString(x + 93, y + 3, Text.getText("playStatisticsPanel.header.animals"), Text.FontType.HEADER, 2, true);
         this.back.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 34;
         this.animalsTotal.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.animalsGender.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.animalsDomesticated.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.animalsDeaths.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.animalsCategoryNeeds.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.renderMobListConfig(MobBase.MobGroup.DOMESTICATED_ANIMAL, debug);
      }

      if (this.chartPage == GUIEnums.GUIPanelPage.STATS_ANIMALS_CATEGORY_NEEDS) {
         this.backPage = GUIEnums.GUIPanelPage.STATS_ANIMALS;
         this.backPageDefaultChart = StatisticsPanel.StatisticsChartType.ANIMALS_NEEDS_AVERAGE_HUNGER;
         this.font.drawString(x + 93, y + 3, Text.getText("playStatisticsPanel.header.needs"), Text.FontType.HEADER, 2, true);
         this.back.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 34;
         this.animalsNeedsAverageHunger.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.animalsNeedsAverageThirst.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.animalsNeedsAverageEnergy.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.renderMobListConfig(MobBase.MobGroup.DOMESTICATED_ANIMAL, debug);
      }

      if (this.chartPage == GUIEnums.GUIPanelPage.STATS_BUILDINGS) {
         this.backPage = GUIEnums.GUIPanelPage.STATS_CATEGORY;
         this.backPageDefaultChart = StatisticsPanel.StatisticsChartType.VILLAGERS_TOTAL;
         this.font.drawString(x + 93, y + 3, Text.getText("playStatisticsPanel.header.buildings"), Text.FontType.HEADER, 2, true);
         this.back.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 34;
         this.buildingsTotal.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.buildingsBuilt.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.buildingsUpgraded.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.buildingsDestroyed.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.buildingsDismantled.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
      }

      if (this.chartPage == GUIEnums.GUIPanelPage.STATS_DEFENSES) {
         this.backPage = GUIEnums.GUIPanelPage.STATS_CATEGORY;
         this.backPageDefaultChart = StatisticsPanel.StatisticsChartType.DEFENSES_TOWERS;
         this.font.drawString(x + 93, y + 3, Text.getText("playStatisticsPanel.header.defenses"), Text.FontType.HEADER, 2, true);
         this.back.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 34;
         this.defensesTowers.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.defensesCombobulators.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.defensesGolems.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.defensesRangers.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.defensesWalls.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         if (this.selectedChart == StatisticsPanel.StatisticsChartType.DEFENSES_RANGERS) {
            this.renderMobListConfig(MobBase.MobGroup.VILLAGER, debug);
         } else if (this.selectedChart == StatisticsPanel.StatisticsChartType.DEFENSES_GOLEMS) {
            this.renderMobListConfig(MobBase.MobGroup.GOLEM, debug);
         }
      }

      if (this.chartPage == GUIEnums.GUIPanelPage.STATS_MONSTERS) {
         this.backPage = GUIEnums.GUIPanelPage.STATS_CATEGORY;
         this.backPageDefaultChart = StatisticsPanel.StatisticsChartType.CORRUPTION_TOTAL;
         this.font.drawString(x + 93, y + 3, Text.getText("playStatisticsPanel.header.monsters"), Text.FontType.HEADER, 2, true);
         this.back.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 34;
         this.corruptionTotal.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.corruptionSpreadAmount.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.corruptedBuildings.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.dronesTotal.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.monstersSpawned.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.monstersKilled.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         if (this.selectedChart == StatisticsPanel.StatisticsChartType.MONSTERS_SPAWNED
            || this.selectedChart == StatisticsPanel.StatisticsChartType.MONSTERS_KILLED) {
            this.renderMobListConfig(MobBase.MobGroup.MONSTER, debug);
         }
      }

      if (this.chartPage == GUIEnums.GUIPanelPage.STATS_RESOURCES) {
         this.backPage = GUIEnums.GUIPanelPage.STATS_CATEGORY;
         this.backPageDefaultChart = StatisticsPanel.StatisticsChartType.RESOURCES_TOTAL;
         this.font.drawString(x + 93, y + 3, Text.getText("playStatisticsPanel.header.resources"), Text.FontType.HEADER, 2, true);
         this.back.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 34;
         this.resourcesTotal.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.resourcesProductionRate.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.resourcesFoodAndWaterRate.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         if (this.selectedChart != StatisticsPanel.StatisticsChartType.RESOURCES_FOOD_AND_WATER_RATE) {
            this.renderResourcesListConfig(debug);
         }
      }

      if (this.chartPage == GUIEnums.GUIPanelPage.STATS_MAGIC_AND_ENERGY) {
         this.backPage = GUIEnums.GUIPanelPage.STATS_CATEGORY;
         this.backPageDefaultChart = StatisticsPanel.StatisticsChartType.MAGIC_AND_ENERGY_ENERGY;
         this.font.drawString(x + 93, y + 6, Text.getText("playStatisticsPanel.header.magicAndEnergy"), Text.FontType.HEADER, 1, true);
         this.back.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 34;
         this.magicAndEnergyEssenceGenerated.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.magicAndEnergyEssenceDecayed.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.magicAndEnergyEnergy.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.magicAndEnergyInfluenceSpent.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
         this.magicAndEnergyInfluenceGenerated.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 26;
      }

      if (this.chartPage == GUIEnums.GUIPanelPage.STATS_OTHER) {
         this.backPage = GUIEnums.GUIPanelPage.STATS_CATEGORY;
         this.backPageDefaultChart = StatisticsPanel.StatisticsChartType.VILLAGERS_TOTAL;
         this.font.drawString(x + 93, y + 3, Text.getText("playStatisticsPanel.header.other"), Text.FontType.HEADER, 2, true);
         this.back.render(this.g, this.mouse, x + 93, y + alignY, false, true, debug);
         alignY += 34;
      }
   }

   private void renderMobListConfig(MobBase.MobGroup group, boolean debug) throws SlickException {
      this.configurationPanel.draw(this.x, this.y + 252);
      this.font.drawString(this.x + 93, this.y + 255, Text.getText("playStatisticsPanel.header.configuration"), Text.FontType.HEADER, 2, true);
      ArrayList<GUIButtonStatisticsMobIcon> sortedList = new ArrayList<>();

      for (GUIButtonStatisticsMobIcon b : this.mobList) {
         if (b.getMobGroup() == group) {
            sortedList.add(b);
         }
      }

      this.renderFinalSortedMobList(debug, sortedList);
   }

   private void renderVillagerChildrenListConfig(boolean debug) throws SlickException {
      this.configurationPanel.draw(this.x, this.y + 252);
      this.font.drawString(this.x + 93, this.y + 255, Text.getText("playStatisticsPanel.header.configuration"), Text.FontType.HEADER, 2, true);
      ArrayList<GUIButtonStatisticsMobIcon> sortedList = new ArrayList<>();

      for (GUIButtonStatisticsMobIcon b : this.mobList) {
         if (b.getMobGroup() == MobBase.MobGroup.VILLAGER
            && (
               b.getMobType() == MobBase.MobType.HUMAN_VILLAGER_CHILD
                  || b.getMobType() == MobBase.MobType.CATJEET_VILLAGER_CHILD
                  || b.getMobType() == MobBase.MobType.HUMAN_VILLAGER_CHILD_NEPHILIM
                  || b.getMobType() == MobBase.MobType.CATJEET_VILLAGER_CHILD_NEPHILIM
            )) {
            sortedList.add(b);
         }
      }

      this.renderFinalSortedMobList(debug, sortedList);
   }

   private void renderVillagerAdultListConfig(boolean debug) throws SlickException {
      this.configurationPanel.draw(this.x, this.y + 252);
      this.font.drawString(this.x + 93, this.y + 255, Text.getText("playStatisticsPanel.header.configuration"), Text.FontType.HEADER, 2, true);
      ArrayList<GUIButtonStatisticsMobIcon> sortedList = new ArrayList<>();

      for (GUIButtonStatisticsMobIcon b : this.mobList) {
         if (b.getMobGroup() == MobBase.MobGroup.VILLAGER && b.getMobInstance().getMobFlags().canSocialMate()) {
            sortedList.add(b);
         }
      }

      this.renderFinalSortedMobList(debug, sortedList);
   }

   private void renderFinalSortedMobList(boolean debug, ArrayList<GUIButtonStatisticsMobIcon> sortedList) throws SlickException {
      int alignX = 0;
      int alignY = 285;
      this.mobListAll.render(this.g, this.mouse, this.x + 94, this.y + alignY, false, true, debug);
      if (this.currentMobPage > 0) {
         this.mobListPreviousPage.render(this.g, this.mouse, this.x + 38, this.y + alignY, false, true, debug);
      }

      if (sortedList.size() > 12 && sortedList.size() - this.currentMobPage * 15 > 12) {
         this.mobListNextPage.render(this.g, this.mouse, this.x + 38 + 112, this.y + alignY, false, true, debug);
      }

      alignY += 30;
      int count = 0;

      for (int i = this.currentMobPage * 15; i < sortedList.size(); i++) {
         GUIButtonStatisticsMobIcon b = sortedList.get(i);
         b.render(this.g, this.mouse, this.x + alignX + 38, this.y + alignY, false, true, debug);
         alignX += 28;
         if (alignX >= 140) {
            alignX = 0;
            alignY += 28;
         }

         if (++count >= 15) {
            break;
         }
      }
   }

   private void renderResourcesListConfig(boolean debug) throws SlickException {
      int alignX = 0;
      int alignY = 285;
      this.configurationPanel.draw(this.x, this.y + 252);
      this.font.drawString(this.x + 93, this.y + 255, Text.getText("playStatisticsPanel.header.configuration"), Text.FontType.HEADER, 2, true);
      ArrayList<GUIButtonStatisticsResourceIcon> sortedList = new ArrayList<>();

      for (GUIButtonStatisticsResourceIcon b : this.resourcesList) {
         ResourceModule.ResourceType t = b.getResourceType();
         if (t.getTemplate().getResourceCategory() == this.resourceCategory) {
            sortedList.add(b);
         }
      }

      this.resourceListAll.render(this.g, this.mouse, this.x + 94, this.y + alignY, false, true, debug);
      if (this.currentResourcePage > 0) {
         this.resourceListPreviousPage.render(this.g, this.mouse, this.x + 38, this.y + alignY, false, true, debug);
      }

      if (sortedList.size() > 12 && sortedList.size() - this.currentResourcePage * 10 > 12) {
         this.resourceListNextPage.render(this.g, this.mouse, this.x + 38 + 112, this.y + alignY, false, true, debug);
      }

      alignY += 30;
      int count = 0;

      for (int i = this.currentResourcePage * 15; i < sortedList.size(); i++) {
         GUIButtonStatisticsResourceIcon bx = sortedList.get(i);
         bx.render(this.g, this.mouse, this.x + alignX + 38, this.y + alignY, false, true, debug);
         alignX += 28;
         if (alignX >= 140) {
            alignX = 0;
            alignY += 28;
         }

         if (++count >= 10) {
            break;
         }
      }

      int var9 = 380;
      this.resourceCategoryLeft.render(this.g, this.mouse, this.x + 18, this.y - 2 + var9, debug);
      this.resourceCategoryRight.render(this.g, this.mouse, this.x + 150, this.y - 2 + var9, debug);
      this.font.drawString(this.x + 94, this.y + var9, this.resourceCategory.getText(), Text.FontType.BODY, 1, true);
   }

   private void renderChart(
      ArrayList<Integer> chartOne,
      ArrayList<Integer> chartTwo,
      Color chartOneColorOne,
      Color chartOneColorTwo,
      Color chartTwoColorOne,
      Color chartTwoColorTwo,
      String nameOne,
      String nameTwo
   ) {
      int barMax = 0;
      int highestChartOne = 0;
      int highestChartTwo = 0;
      int bar = 0;

      for (int day = this.startDay; day < chartOne.size() && bar < 15; bar++) {
         int statAmount = chartOne.get(day);
         if (statAmount > barMax) {
            barMax = statAmount;
         }

         if (statAmount > highestChartOne) {
            highestChartOne = statAmount;
         }

         day++;
      }

      if (chartTwo != null) {
         bar = 0;

         for (int day = this.startDay; day < chartTwo.size() && bar < 15; bar++) {
            int statAmountx = chartTwo.get(day);
            if (statAmountx > barMax) {
               barMax = statAmountx;
            }

            if (statAmountx > highestChartTwo) {
               highestChartTwo = statAmountx;
            }

            day++;
         }
      }

      barMax = (barMax + 5) / 10 * 10;
      barMax = (int)(barMax + barMax * 0.1F) + 10;
      this.g.setColor(new Color(13, 13, 13));
      bar = (int)(barMax * 0.1F);

      for (int i = 0; i <= barMax; i += bar) {
         int barHeight = (int)Math.ceil((float)i / barMax * 304.0F);
         this.font.drawString(this.x + 218, this.y + 381 - barHeight, "$GRE0" + i, Text.FontType.BODY, 0, true);
         if (i != 0) {
            this.g.drawLine(this.x + 235, this.y + 387 - barHeight, this.x + 642, this.y + 387 - barHeight);
         }
      }

      int spacing = 27;
      int barSegmentsX = 2;

      for (int ix = 0; ix <= 15; ix += barSegmentsX) {
         this.font.drawString(this.x + ix * spacing + 250, this.y + 394, "$GRE0" + (this.startDay + ix), Text.FontType.BODY, 0, true);
         this.g.drawLine(this.x + ix * spacing + 249, this.y + 387, this.x + ix * spacing + 249, this.y + 75);
      }

      int alignX = 0;
      int barWidth = 15;
      if (chartTwo != null) {
         barWidth = 7;
      }

      int barHeightOne = 0;
      int barHeightTwo = 0;
      this.g.setWorldClip(this.x + 235, this.y + 75, 406.0F, 313.0F);
      int barx = 0;

      for (int day = this.startDay; day < chartOne.size() && barx < 15; barx++) {
         barHeightOne = (int)Math.ceil((float)chartOne.get(day).intValue() / barMax * 304.0F);
         if (barHeightOne <= 0) {
            barHeightOne = 1;
         }

         float colorPercent = (int)Math.ceil((float)chartOne.get(day).intValue() / highestChartOne * 100.0F);
         Color colorFinal = Color.white.blendColors(chartOneColorOne, chartOneColorTwo, colorPercent, 10.0F, 100.0F);
         this.g.setColor(colorFinal);
         if (chartTwo != null) {
            this.statisticsSmallStatsBarLeft[barx].draw(this.x + alignX + 242, this.y + 388 - barHeightOne, colorFinal);
         } else {
            this.statisticsLargeStatsBar[barx].draw(this.x + alignX + 242, this.y + 388 - barHeightOne, colorFinal);
         }

         this.graphBarsOne[barx].setBounds(this.x + alignX + 242, this.y + 388 - 304, barWidth, 304.0F);
         alignX += spacing;
         day++;
      }

      if (chartTwo != null) {
         alignX = 0;
         barx = 0;

         for (int day = this.startDay; day < chartTwo.size() && barx < 15; barx++) {
            barHeightTwo = (int)Math.ceil((float)chartTwo.get(day).intValue() / barMax * 304.0F);
            if (barHeightTwo == 0) {
               barHeightTwo = 1;
            }

            float colorPercent = (int)Math.ceil((float)chartTwo.get(day).intValue() / highestChartTwo * 100.0F);
            Color colorFinal = Color.white.blendColors(chartTwoColorOne, chartTwoColorTwo, colorPercent, 10.0F, 100.0F);
            this.g.setColor(colorFinal);
            this.statisticsSmallStatsBarRight[barx].draw(this.x + alignX + 250, this.y + 388 - barHeightTwo, colorFinal);
            this.graphBarsTwo[barx].setBounds(this.x + alignX + 250, this.y + 388 - 304, barWidth, 304.0F);
            alignX += spacing;
            day++;
         }
      }

      this.g.clearWorldClip();
      this.renderToolTip(nameOne, nameTwo, chartOne, chartTwo);
   }

   private void renderChartNegatives(
      ArrayList<Integer> chartOne,
      ArrayList<Integer> chartTwo,
      Color chartOneColorOne,
      Color chartOneColorTwo,
      Color chartTwoColorOne,
      Color chartTwoColorTwo,
      String nameOne,
      String nameTwo
   ) {
      int barMax = 0;
      int highestChartOne = 0;
      int highestChartTwo = 0;
      int lowestChartOne = 0;
      int lowestChartTwo = 0;
      int bar = 0;

      for (int day = this.startDay; day < chartOne.size() && bar < 15; bar++) {
         int statAmount = chartOne.get(day);
         if (Math.abs(statAmount) > barMax) {
            barMax = Math.abs(statAmount);
         }

         if (statAmount > highestChartOne) {
            highestChartOne = statAmount;
         } else if (statAmount < lowestChartOne) {
            lowestChartOne = statAmount;
         }

         day++;
      }

      if (chartTwo != null) {
         bar = 0;

         for (int day = this.startDay; day < chartTwo.size() && bar < 15; bar++) {
            int statAmountx = chartTwo.get(day);
            if (Math.abs(statAmountx) > barMax) {
               barMax = Math.abs(statAmountx);
            }

            if (statAmountx > highestChartTwo) {
               highestChartTwo = statAmountx;
            } else if (statAmountx < lowestChartTwo) {
               lowestChartTwo = statAmountx;
            }

            day++;
         }
      }

      barMax = (barMax + 5) / 10 * 10;
      barMax = (int)(barMax + barMax * 0.1F) + 10;
      this.g.setColor(new Color(13, 13, 13));
      bar = (int)(barMax * 0.2F);

      for (int i = 0; i <= barMax; i += bar) {
         int barHeight = (int)Math.ceil((float)i / barMax * 152.0F);
         this.font.drawString(this.x + 218, this.y + 229 - barHeight, "$GRE0" + i, Text.FontType.BODY, 0, true);
         this.g.drawLine(this.x + 235, this.y + 235 - barHeight, this.x + 642, this.y + 235 - barHeight);
         barHeight = (int)Math.ceil((float)i / (barMax * -1) * 152.0F);
         if (i != 0) {
            this.font.drawString(this.x + 218, this.y + 229 - barHeight, "$RED0-" + i, Text.FontType.BODY, 0, true);
            this.g.drawLine(this.x + 235, this.y + 235 - barHeight, this.x + 642, this.y + 235 - barHeight);
         }
      }

      int spacing = 27;
      int barSegmentsX = 2;

      for (int ix = 0; ix <= 15; ix += barSegmentsX) {
         this.font.drawString(this.x + ix * spacing + 250, this.y + 394, "$GRE0" + (this.startDay + ix), Text.FontType.BODY, 0, true);
         this.g.drawLine(this.x + ix * spacing + 249, this.y + 387, this.x + ix * spacing + 249, this.y + 75);
      }

      int alignX = 0;
      int barWidth = 15;
      if (chartTwo != null) {
         barWidth = 7;
      }

      int barHeightOne = 0;
      int barHeightTwo = 0;
      int barx = 0;

      for (int day = this.startDay; day < chartOne.size() && barx < 15; barx++) {
         barHeightOne = (int)Math.ceil((float)chartOne.get(day).intValue() / barMax * 152.0F);
         if (barHeightOne == 0) {
            barHeightOne = 1;
         }

         float colorPercent = 0.0F;
         if (chartOne.get(day) <= 0) {
            colorPercent = (int)Math.ceil((float)chartOne.get(day).intValue() / lowestChartOne * 100.0F);
         } else {
            colorPercent = (int)Math.ceil((float)chartOne.get(day).intValue() / highestChartOne * 100.0F);
         }

         Color midColor = Color.white.blendColors(chartOneColorOne, chartOneColorTwo, 50.0F, 10.0F, 100.0F);
         Color colorFinal = null;
         if (chartOne.get(day) <= 0) {
            colorFinal = Color.white.blendColors(chartOneColorTwo, midColor, colorPercent, 10.0F, 100.0F);
         } else {
            colorFinal = Color.white.blendColors(chartOneColorOne, midColor, colorPercent, 10.0F, 100.0F);
         }

         this.g.setColor(colorFinal);
         if (chartTwo != null) {
            if (barHeightOne < 0) {
               this.g.setWorldClip(this.x + 235, this.y + 236, 406.0F, 151.0F);
               this.statisticsSmallStatsBarLeft[barx]
                  .getFlippedCopy(false, true)
                  .draw(this.x + alignX + 242, this.y + 235 - barHeightOne - this.statisticsSmallStatsBarLeft[barx].getHeight(), colorFinal);
               this.g.clearWorldClip();
            } else {
               this.g.setWorldClip(this.x + 238, this.y + 75, 406.0F, 160.0F);
               this.statisticsSmallStatsBarLeft[barx].draw(this.x + alignX + 242, this.y + 235 - barHeightOne, colorFinal);
               this.g.clearWorldClip();
            }
         } else if (barHeightOne < 0) {
            this.g.setWorldClip(this.x + 235, this.y + 236, 406.0F, 151.0F);
            this.statisticsLargeStatsBar[barx]
               .getFlippedCopy(false, true)
               .draw(this.x + alignX + 242, this.y + 235 - barHeightOne - this.statisticsLargeStatsBar[barx].getHeight(), colorFinal);
            this.g.clearWorldClip();
         } else {
            this.g.setWorldClip(this.x + 238, this.y + 75, 406.0F, 160.0F);
            this.statisticsLargeStatsBar[barx].draw(this.x + alignX + 242, this.y + 235 - barHeightOne, colorFinal);
            this.g.clearWorldClip();
         }

         this.graphBarsOne[barx].setBounds(this.x + alignX + 242, this.y + 387 - 304, barWidth, 304.0F);
         alignX += spacing;
         day++;
      }

      if (chartTwo != null) {
         alignX = 0;
         barx = 0;

         for (int day = this.startDay; day < chartTwo.size() && barx < 15; barx++) {
            barHeightTwo = (int)Math.ceil((float)chartTwo.get(day).intValue() / barMax * 152.0F);
            if (barHeightTwo == 0) {
               barHeightTwo = 1;
            }

            float colorPercentx = 0.0F;
            if (chartTwo.get(day) <= 0) {
               colorPercentx = (int)Math.ceil((float)chartTwo.get(day).intValue() / lowestChartTwo * 100.0F);
            } else {
               colorPercentx = (int)Math.ceil((float)chartTwo.get(day).intValue() / highestChartTwo * 100.0F);
            }

            Color midColorx = Color.white.blendColors(chartTwoColorOne, chartTwoColorTwo, 50.0F, 10.0F, 100.0F);
            Color colorFinalx = null;
            if (chartTwo.get(day) <= 0) {
               colorFinalx = Color.white.blendColors(chartTwoColorTwo, midColorx, colorPercentx, 10.0F, 100.0F);
            } else {
               colorFinalx = Color.white.blendColors(chartTwoColorOne, midColorx, colorPercentx, 10.0F, 100.0F);
            }

            this.g.setColor(colorFinalx);
            if (barHeightTwo < 0) {
               this.g.setWorldClip(this.x + 235, this.y + 236, 406.0F, 151.0F);
               this.statisticsSmallStatsBarRight[barx]
                  .getFlippedCopy(false, true)
                  .draw(this.x + alignX + 250, this.y + 235 - barHeightTwo - this.statisticsSmallStatsBarRight[barx].getHeight(), colorFinalx);
               this.g.clearWorldClip();
            } else {
               this.g.setWorldClip(this.x + 238, this.y + 75, 406.0F, 160.0F);
               this.statisticsSmallStatsBarRight[barx].draw(this.x + alignX + 250, this.y + 235 - barHeightTwo, colorFinalx);
               this.g.clearWorldClip();
            }

            this.graphBarsTwo[barx].setBounds(this.x + alignX + 250, this.y + 387 - 304, barWidth, 304.0F);
            alignX += spacing;
            day++;
         }
      }

      this.renderToolTip(nameOne, nameTwo, chartOne, chartTwo);
   }

   private void renderToolTip(String nameOne, String nameTwo, ArrayList<Integer> chartOne, ArrayList<Integer> chartTwo) {
      this.g.setColor(new Color(255, 255, 255, 50));

      for (int i = 0; i < this.graphBarsOne.length; i++) {
         if (this.startDay + i < chartOne.size() && this.graphBarsOne[i].intersects(this.mouse)) {
            this.g.fill(this.graphBarsOne[i]);
            if (nameOne != null) {
               this.renderActualToolTip("$GRE0" + nameOne + ": " + chartOne.get(this.startDay + i));
            } else {
               this.renderActualToolTip("$GRE0" + chartOne.get(this.startDay + i));
            }
            break;
         }

         if (chartTwo != null && this.startDay + i < chartTwo.size() && this.graphBarsTwo[i].intersects(this.mouse)) {
            this.g.fill(this.graphBarsTwo[i]);
            if (nameTwo != null) {
               this.renderActualToolTip("$GRE0" + nameTwo + ": " + chartTwo.get(this.startDay + i));
            } else {
               this.renderActualToolTip("$GRE0" + chartTwo.get(this.startDay + i));
            }
         }
      }
   }

   private void renderActualToolTip(String description) {
      int fontWidth = Text.getWidth(description, Text.FontType.BODY, 2);
      int fontHeight = Text.getHeight(description, Text.FontType.BODY, 2);
      float drawX = this.mouse.getX() + 30.0F;
      float drawY = this.mouse.getY() + 20.0F;
      this.g.setColor(new Color(0, 0, 0, 200));
      if (drawY + Text.getHeight(description, Text.FontType.BODY, 2) > ScaleControl.getInterfaceHeight()
         && ScaleControl.getInterfaceWidth() - drawX < fontWidth) {
         this.g
            .fillRect(
               ScaleControl.getInterfaceWidth() - fontWidth - 2,
               ScaleControl.getInterfaceHeight() - Text.getHeight(description, Text.FontType.BODY, 2),
               fontWidth + 5,
               fontHeight
            );
         this.font
            .drawString(
               ScaleControl.getInterfaceWidth() - fontWidth,
               ScaleControl.getInterfaceHeight() - Text.getHeight(description, Text.FontType.BODY, 2),
               description,
               Text.FontType.BODY,
               2,
               false
            );
      } else {
         if (ScaleControl.getInterfaceWidth() - drawX < fontWidth) {
            this.g.fillRect(ScaleControl.getInterfaceWidth() - fontWidth - 2, drawY, fontWidth + 5, fontHeight);
            this.font.drawString(ScaleControl.getInterfaceWidth() - fontWidth, drawY, description, Text.FontType.BODY, 2, false);
            return;
         }

         if (drawY + Text.getHeight(description, Text.FontType.BODY, 2) > ScaleControl.getInterfaceHeight()) {
            this.g.fillRect(drawX - 2.0F, ScaleControl.getInterfaceHeight() - Text.getHeight(description, Text.FontType.BODY, 2), fontWidth + 5, fontHeight);
            this.font
               .drawString(
                  drawX, ScaleControl.getInterfaceHeight() - Text.getHeight(description, Text.FontType.BODY, 2), description, Text.FontType.BODY, 2, false
               );
            return;
         }

         this.g.fillRect(drawX - 2.0F, drawY, fontWidth + 5, fontHeight);
         this.font.drawString(drawX, drawY, description, Text.FontType.BODY, 2, false);
      }
   }

   @Override
   public void update() {
      for (GUIButton b : this.guiButtons) {
         b.deselect();
      }

      if (this.chartButtons.containsKey(this.selectedChart)) {
         this.chartButtons.get(this.selectedChart).select();
      }

      for (GUIButtonStatisticsMobIcon b : this.mobList) {
         if (this.selectedMobType == b.getMobType()) {
            b.select();
         } else {
            b.deselect();
         }
      }

      if (this.selectedMobType == null) {
         this.mobListAll.select();
      } else {
         this.mobListAll.deselect();
      }

      for (GUIButtonStatisticsResourceIcon bx : this.resourcesList) {
         if (this.selectedResourceType == bx.getResourceType()) {
            bx.select();
         } else {
            bx.deselect();
         }
      }

      if (this.selectedResourceType == null) {
         this.resourceListAll.select();
      } else {
         this.resourceListAll.deselect();
      }
   }

   public void resetGraph() {
      this.startDay = this.time.getDay() - 15;
      if (this.startDay < 0) {
         this.startDay = 0;
      }

      this.dayMoved = false;
   }

   public void dayForward(int i) {
      this.startDay += i;
      this.dayMoved = true;
      if (this.startDay > this.time.getDay() - 15) {
         this.startDay = this.time.getDay() - 15;
         this.dayMoved = false;
      }

      if (this.startDay < 0) {
         this.startDay = 0;
      }
   }

   public void dayBackward(int i) {
      this.startDay -= i;
      if (this.startDay < 0) {
         this.startDay = 0;
      }

      this.dayMoved = true;
   }

   public void setDay(int i) {
      this.startDay = i;
      this.dayMoved = true;
      if (this.startDay > this.time.getDay() - 15) {
         this.startDay = this.time.getDay() - 15;
         this.dayMoved = false;
      }

      if (this.startDay < 0) {
         this.startDay = 0;
      }
   }

   public void setChart(StatisticsPanel.StatisticsChartType t) {
      this.selectedChart = t;
      this.selectedMobType = null;
      this.currentMobPage = 0;
      this.selectedResourceType = null;
      this.currentResourcePage = 0;
   }

   public void setMobType(MobBase.MobType t) {
      this.selectedMobType = t;
   }

   public void setResourceType(ResourceModule.ResourceType t) {
      this.selectedResourceType = t;
   }

   public void setChartPage(GUIEnums.GUIPanelPage p) {
      this.chartPage = p;
   }

   public GUIEnums.GUIPanelPage getBackPage() {
      return this.backPage;
   }

   public StatisticsPanel.StatisticsChartType getBackPageDefaultChartPage() {
      return this.backPageDefaultChart;
   }

   public void setBuildingSelectionLeft() {
      if (this.resourceCategory.ordinal() != ResourceModule.ResourceCategory.values().length - 1) {
         this.resourceCategory = ResourceModule.ResourceCategory.values()[this.resourceCategory.ordinal() + 1];
      } else {
         this.resourceCategory = ResourceModule.ResourceCategory.values()[0];
      }
   }

   public void setBuildingSelectionRight() {
      if (this.resourceCategory.ordinal() != 0) {
         this.resourceCategory = ResourceModule.ResourceCategory.values()[this.resourceCategory.ordinal() - 1];
      } else {
         this.resourceCategory = ResourceModule.ResourceCategory.values()[ResourceModule.ResourceCategory.values().length - 1];
      }
   }

   public void setResourceCategoryLeft() {
      if (this.resourceCategory.ordinal() != ResourceModule.ResourceCategory.values().length - 1) {
         this.resourceCategory = ResourceModule.ResourceCategory.values()[this.resourceCategory.ordinal() + 1];
      } else {
         this.resourceCategory = ResourceModule.ResourceCategory.values()[0];
      }
   }

   public void setResourceCategoryRight() {
      if (this.resourceCategory.ordinal() != 0) {
         this.resourceCategory = ResourceModule.ResourceCategory.values()[this.resourceCategory.ordinal() - 1];
      } else {
         this.resourceCategory = ResourceModule.ResourceCategory.values()[ResourceModule.ResourceCategory.values().length - 1];
      }
   }

   public void setCurrentMobPageNext() {
      this.currentMobPage++;
   }

   public void setCurrentMobPageLast() {
      this.currentMobPage--;
   }

   public void setCurrentResourcePageNext() {
      this.currentResourcePage++;
   }

   public void setCurrentResourcePageLast() {
      this.currentResourcePage--;
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

   public static enum StatisticsChartType {
      VILLAGERS_TOTAL,
      VILLAGERS_GENDER,
      VILLAGERS_DEATHS,
      VILLAGERS_IMMIGRATION_CATJEET_HIRED,
      VILLAGERS_IMMIGRATION_NOMADS_JOINED,
      VILLAGERS_IMMIGRATION_NOMADS_VISITED,
      VILLAGERS_REPRODUCTION_BIRTHS,
      VILLAGERS_REPRODUCTION_GROWN_UP,
      VILLAGERS_REPRODUCTION_COITUS,
      VILLAGERS_REPRODUCTION_PREGNANCIES,
      VILLAGERS_REPRODUCTION_NEW_PREGNANCIES,
      VILLAGERS_NEEDS_AVERAGE_HUNGER,
      VILLAGERS_NEEDS_AVERAGE_THIRST,
      VILLAGERS_NEEDS_AVERAGE_ENERGY,
      ANIMALS_TOTAL,
      ANIMALS_GENDER,
      ANIMALS_DEATHS,
      ANIMALS_DOMESTICATED,
      ANIMALS_NEEDS_AVERAGE_HUNGER,
      ANIMALS_NEEDS_AVERAGE_THIRST,
      ANIMALS_NEEDS_AVERAGE_ENERGY,
      CORRUPTION_TOTAL,
      CORRUPTION_SPREAD_AMOUNT,
      CORRUPTED_BUILDINGS,
      DRONES_TOTAL,
      MONSTERS_SPAWNED,
      MONSTERS_KILLED,
      BUILDINGS_TOTAL,
      BUILDINGS_BUILT,
      BUILDINGS_UPGRADED,
      BUILDINGS_DESTROYED,
      BUILDINGS_DISMANTLED,
      DEFENSES_TOWERS,
      DEFENSES_COMBOBULATORS,
      DEFENSES_GOLEMS,
      DEFENSES_RANGERS,
      DEFENSES_WALLS,
      RESOURCES_TOTAL,
      RESOURCES_PRODUCTION_RATE,
      RESOURCES_FOOD_AND_WATER_RATE,
      MAGIC_AND_ENERGY_ESSENCE_GENERATED,
      MAGIC_AND_ENERGY_ESSENCE_DECAYED,
      MAGIC_AND_ENERGY_ENERGY,
      MAGIC_AND_ENERGY_INFLUENCE_SPENT,
      MAGIC_AND_ENERGY_INFLUENCE_GENERATED;
   }
}
