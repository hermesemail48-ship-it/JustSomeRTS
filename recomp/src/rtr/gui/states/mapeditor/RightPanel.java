package rtr.gui.states.mapeditor;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.GUIEnums;
import rtr.gui.buttons.GUIButtonHide;
import rtr.gui.buttons.GUIButtonPage;
import rtr.gui.buttons.GUIPanelButton;
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.MapEditorGUIData;
import rtr.gui.states.MapEditorGUIPanelBase;
import rtr.map.MapTilesLoader;
import rtr.objects.ObjectBase;
import rtr.system.ScaleControl;

public class RightPanel extends MapEditorGUIPanelBase {
   private Rectangle mask;
   private Image blankLargePanel1;
   private Image blankLargePanel2;
   private Image blankLargePanel3;
   private Image blankLargePanel4;
   private Image blankLargePanel5;
   private Image blankLargePanel6;
   private GUIEnums.GUIPanelPage panelCurrentPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_SANDBOX_TOOLS;
   private GUIEnums.GUIPanelPage nextPage;
   private GUIEnums.GUIPanelPage lastPage;
   private GUIEnums.GUIPanelPage backPage;
   private GUIButtonPage pageBackTop;
   private GUIButtonPage pageUpTop;
   private GUIButtonPage pageDownTop;
   private GUIButtonPage pageBackBottom;
   private GUIButtonPage pageUpBottom;
   private GUIButtonPage pageDownBottom;
   private GUIButtonHide hidePanel;
   private boolean hidden;
   private GUIPanelButton panelCategoryBuild;
   private GUIPanelButton panelCategoryTiles;
   private GUIPanelButton panelCategoryAncient;
   private GUIPanelButton panelCategoryCivics;
   private GUIPanelButton panelCategoryDefense;
   private GUIPanelButton panelCategoryFoodAndWater;
   private GUIPanelButton panelCategoryGatesAndWalls;
   private GUIPanelButton panelCategoryHarvesting;
   private GUIPanelButton panelCategoryHousing;
   private GUIPanelButton panelCategoryLighting;
   private GUIPanelButton panelCategoryMagic;
   private GUIPanelButton panelCategoryManufacturing;
   private GUIPanelButton panelCategoryRefining;
   private GUIPanelButton panelCategoryStorage;
   private GUIPanelButton panelCategoryTrash;
   private GUIPanelButton panelCategorySpecial;
   private GUIPanelButton panelAncientCullisGateAbandoned;
   private GUIPanelButton panelAncientCullisGate;
   private GUIPanelButton panelAncientRadiancePoolAbandoned;
   private GUIPanelButton panelAncientRadiancePool;
   private GUIPanelButton panelAncillary;
   private GUIPanelButton panelClinic;
   private GUIPanelButton panelCourierStation;
   private GUIPanelButton panelMaintenanceBuilding;
   private GUIPanelButton panelMarketplace;
   private GUIPanelButton panelWayMakerShack;
   private GUIPanelButton panelMigrationWayStation;
   private GUIPanelButton panelCategoryDefenseGolems;
   private GUIPanelButton panelCategoryDefenseTowers;
   private GUIPanelButton panelCategoryDefenseMiscellaneous;
   private GUIPanelButton panelCrystalGolemCombobulator;
   private GUIPanelButton panelStoneGolemCombobulator;
   private GUIPanelButton panelWoodGolemCombobulator;
   private GUIPanelButton panelBanishTower;
   private GUIPanelButton panelAttractTower;
   private GUIPanelButton panelBallistaTower;
   private GUIPanelButton panelBowTower;
   private GUIPanelButton panelBulletTower;
   private GUIPanelButton panelElementalBoltTower;
   private GUIPanelButton panelPhantomDartTower;
   private GUIPanelButton panelSlingTower;
   private GUIPanelButton panelSprayTower;
   private GUIPanelButton panelStaticTower;
   private GUIPanelButton panelLightningRod;
   private GUIPanelButton panelRecombobulatorTower;
   private GUIPanelButton panelAnimalPen;
   private GUIPanelButton panelBottler;
   private GUIPanelButton panelCluckerCoop;
   private GUIPanelButton panelFarm;
   private GUIPanelButton panelFishingHut;
   private GUIPanelButton panelSmallFountain;
   private GUIPanelButton panelLargeFountain;
   private GUIPanelButton panelKitchen;
   private GUIPanelButton panelOutpost;
   private GUIPanelButton panelRainCatcher;
   private GUIPanelButton panelRangerLodge;
   private GUIPanelButton panelWaterPurifier;
   private GUIPanelButton panelWell;
   private GUIPanelButton panelCategoryGatesAndWallsGates;
   private GUIPanelButton panelCategoryGatesAndWallsWalls;
   private GUIPanelButton panelCrylithiumWallGateNS;
   private GUIPanelButton panelCrylithiumWallGateWE;
   private GUIPanelButton panelStoneWallGateNS;
   private GUIPanelButton panelStoneWallGateWE;
   private GUIPanelButton panelWoodFenceGateNS;
   private GUIPanelButton panelWoodFenceGateWE;
   private GUIPanelButton panelCrylithiumWall;
   private GUIPanelButton panelCrylithiumCurtainWall;
   private GUIPanelButton panelCurtainWall;
   private GUIPanelButton panelStoneWall;
   private GUIPanelButton panelTrashyCubeWall;
   private GUIPanelButton panelWoodFence;
   private GUIPanelButton panelCrystalHarvestry;
   private GUIPanelButton panelLumberShack;
   private GUIPanelButton panelMiningFacility;
   private GUIPanelButton panelDoggoHouse;
   private GUIPanelButton panelHousing;
   private GUIPanelButton panelCrylithiumFirePit;
   private GUIPanelButton panelLargeCrylithiumFirePit;
   private GUIPanelButton panelFirePit;
   private GUIPanelButton panelLargeFirePit;
   private GUIPanelButton panelCrystalMotivator;
   private GUIPanelButton panelCullisGate;
   private GUIPanelButton panelEssenceAltar;
   private GUIPanelButton panelEssenceCollector;
   private GUIPanelButton panelReliquary;
   private GUIPanelButton panelArmorsmithy;
   private GUIPanelButton panelBowyer;
   private GUIPanelButton panelRockTumbler;
   private GUIPanelButton panelToolsmithy;
   private GUIPanelButton panelCrystillery;
   private GUIPanelButton panelLumberMill;
   private GUIPanelButton panelStoneCuttery;
   private GUIPanelButton panelForge;
   private GUIPanelButton panelAmmoStorage;
   private GUIPanelButton panelCrystalStorage;
   private GUIPanelButton panelEquipmentStorage;
   private GUIPanelButton panelFoodStorage;
   private GUIPanelButton panelGoldStorage;
   private GUIPanelButton panelKeyShack;
   private GUIPanelButton panelMineralStorage;
   private GUIPanelButton panelMiscellaneousStorage;
   private GUIPanelButton panelRockStorage;
   private GUIPanelButton panelWoodStorage;
   private GUIPanelButton panelBurner;
   private GUIPanelButton panelCubeEGolemCombobulator;
   private GUIPanelButton panelLandfill;
   private GUIPanelButton panelProcessor;
   private GUIPanelButton panelTrashCan;
   private GUIPanelButton panelTrashyCubePile;
   private GUIPanelButton panelCamp;
   private GUIPanelButton panelLootBox;
   private GUIPanelButton panelCategoryBricks;
   private GUIPanelButton panelCategoryCrystals;
   private GUIPanelButton panelCategoryDirt;
   private GUIPanelButton panelCategoryFlowers;
   private GUIPanelButton panelCategoryFood;
   private GUIPanelButton panelCategoryGrass;
   private GUIPanelButton panelCategoryGravel;
   private GUIPanelButton panelCategoryLava;
   private GUIPanelButton panelCategoryRoads;
   private GUIPanelButton panelCategoryRock;
   private GUIPanelButton panelCategorySand;
   private GUIPanelButton panelCategorySandstone;
   private GUIPanelButton panelCategorySnow;
   private GUIPanelButton panelCategoryTar;
   private GUIPanelButton panelCategoryTrees;
   private GUIPanelButton panelCategoryWater;
   private GUIPanelButton panelGrayBricks;
   private GUIPanelButton panelGrayTiles;
   private GUIPanelButton panelRedCrystals;
   private GUIPanelButton panelGreenCrystals;
   private GUIPanelButton panelBlueCrystals;
   private GUIPanelButton panelPurpleCrystals;
   private GUIPanelButton panelBrownDirt;
   private GUIPanelButton panelDarkBrownDirt;
   private GUIPanelButton panelLightBrownDirt;
   private GUIPanelButton panelBlackFlowers;
   private GUIPanelButton panelBlueFlowers;
   private GUIPanelButton panelPurpleFlowers;
   private GUIPanelButton panelRedFlowers;
   private GUIPanelButton panelWhiteFlowers;
   private GUIPanelButton panelYellowFlowers;
   private GUIPanelButton panelCactus;
   private GUIPanelButton panelCarrots;
   private GUIPanelButton panelHolyPotatoes;
   private GUIPanelButton panelMelons;
   private GUIPanelButton panelMushrooms;
   private GUIPanelButton panelPotatoes;
   private GUIPanelButton panelTurnips;
   private GUIPanelButton panelEmeraldGreenGrass;
   private GUIPanelButton panelGreenGrass;
   private GUIPanelButton panelTealGrass;
   private GUIPanelButton panelYellowBrownGrass;
   private GUIPanelButton panelBlueGravel;
   private GUIPanelButton panelGrayGravel;
   private GUIPanelButton panelRedGravel;
   private GUIPanelButton panelLava;
   private GUIPanelButton panelPath;
   private GUIPanelButton panelLogPathDebris;
   private GUIPanelButton panelLogPath;
   private GUIPanelButton panelCobbleAndLogPathDebris;
   private GUIPanelButton panelCobbleAndLogPath;
   private GUIPanelButton panelCobbleAndBoardRoadDebris;
   private GUIPanelButton panelCobbleAndBoardRoad;
   private GUIPanelButton panelCutStoneAndBoardRoadDebris;
   private GUIPanelButton panelCutStoneAndBoardRoad;
   private GUIPanelButton panelBlackRock;
   private GUIPanelButton panelBrownRock;
   private GUIPanelButton panelGrayRock;
   private GUIPanelButton panelRedRock;
   private GUIPanelButton panelWhiteRock;
   private GUIPanelButton panelBlackSand;
   private GUIPanelButton panelRedSand;
   private GUIPanelButton panelTanSand;
   private GUIPanelButton panelBlackSandstone;
   private GUIPanelButton panelRedSandstone;
   private GUIPanelButton panelTanSandstone;
   private GUIPanelButton panelWhiteSnow;
   private GUIPanelButton panelTar;
   private GUIPanelButton panelBlueDeadTrees;
   private GUIPanelButton panelBlueStumps;
   private GUIPanelButton panelBlueTrees;
   private GUIPanelButton panelBrownDeadTrees;
   private GUIPanelButton panelBrownStumps;
   private GUIPanelButton panelBrownTrees;
   private GUIPanelButton panelDarkGreenDeadTrees;
   private GUIPanelButton panelDarkGreenStumps;
   private GUIPanelButton panelDarkGreenTrees;
   private GUIPanelButton panelGreenDeadTrees;
   private GUIPanelButton panelGreenStumps;
   private GUIPanelButton panelGreenTrees;
   private GUIPanelButton panelLavenderDeadTrees;
   private GUIPanelButton panelLavenderStumps;
   private GUIPanelButton panelLavenderTrees;
   private GUIPanelButton panelPaleBlueDeadTrees;
   private GUIPanelButton panelPaleBlueStumps;
   private GUIPanelButton panelPaleBlueTrees;
   private GUIPanelButton panelRedDeadTrees;
   private GUIPanelButton panelRedStumps;
   private GUIPanelButton panelRedTrees;
   private GUIPanelButton panelWater;

   public RightPanel(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.mask = new Rectangle(0.0F, 0.0F, 190.0F, 530.0F);
      this.masks.add(this.mask);
      this.blankLargePanel1 = ImageLoader.getImage("res/GUI/gameState/panelLarge1.png");
      this.blankLargePanel2 = ImageLoader.getImage("res/GUI/gameState/panelLarge2.png");
      this.blankLargePanel3 = ImageLoader.getImage("res/GUI/gameState/panelLarge3.png");
      this.blankLargePanel4 = ImageLoader.getImage("res/GUI/gameState/panelLarge4.png");
      this.blankLargePanel5 = ImageLoader.getImage("res/GUI/gameState/panelLarge5.png");
      this.blankLargePanel6 = ImageLoader.getImage("res/GUI/gameState/panelLarge6.png");
      this.pageBackTop = new GUIButtonPage("pageBackTop", "back");
      this.pageBackTop.setToolTip(Text.getText("mapEditorPlayRightPanel.button.navigation.back"));
      this.guiButtons.add(this.pageBackTop);
      this.pageUpTop = new GUIButtonPage("pageUpTop", "up");
      this.pageUpTop.setToolTip(Text.getText("mapEditorPlayRightPanel.button.navigation.pageUp"));
      this.guiButtons.add(this.pageUpTop);
      this.pageDownTop = new GUIButtonPage("pageDownTop", "down");
      this.pageDownTop.setToolTip(Text.getText("mapEditorPlayRightPanel.button.navigation.pageDown"));
      this.guiButtons.add(this.pageDownTop);
      this.pageBackBottom = new GUIButtonPage("pageBackBottom", "back");
      this.pageBackBottom.setToolTip(Text.getText("mapEditorPlayRightPanel.button.navigation.back"));
      this.guiButtons.add(this.pageBackBottom);
      this.pageUpBottom = new GUIButtonPage("pageUpBottom", "up");
      this.pageUpBottom.setToolTip(Text.getText("mapEditorPlayRightPanel.button.navigation.pageUp"));
      this.guiButtons.add(this.pageUpBottom);
      this.pageDownBottom = new GUIButtonPage("pageDownBottom", "down");
      this.pageDownBottom.setToolTip(Text.getText("mapEditorPlayRightPanel.button.navigation.pageDown"));
      this.guiButtons.add(this.pageDownBottom);
      this.hidePanel = new GUIButtonHide("hidePanel", false);
      this.hidePanel.setToolTip(Text.getText("mapEditorPlayRightPanel.button.navigation.hidePanel"));
      this.guiButtons.add(this.hidePanel);
      this.panelCategoryBuild = new GUIPanelButton(
         "panelCategoryBuild", 1, "lumberMillAbandoned", Text.getText("mapEditorPlayRightPanel.button.main.build"), true
      );
      this.guiButtons.add(this.panelCategoryBuild);
      this.panelCategoryTiles = new GUIPanelButton("panelCategoryTiles", 1, "grayRock", Text.getText("mapEditorPlayRightPanel.button.main.tiles"), true);
      this.guiButtons.add(this.panelCategoryTiles);
      this.panelCategoryAncient = new GUIPanelButton(
         "panelCategoryAncient", 1, "ancientCullisGate", Text.getText("mapEditorPlayRightPanel.button.build.ancient"), true
      );
      this.guiButtons.add(this.panelCategoryAncient);
      this.panelCategoryCivics = new GUIPanelButton(
         "panelCategoryCivics", 1, "clinicAbandoned", Text.getText("mapEditorPlayRightPanel.button.build.civics"), true
      );
      this.guiButtons.add(this.panelCategoryCivics);
      this.panelCategoryDefense = new GUIPanelButton(
         "panelCategoryDefense", 1, "bowTowerAbandoned", Text.getText("mapEditorPlayRightPanel.button.build.defense"), true
      );
      this.guiButtons.add(this.panelCategoryDefense);
      this.panelCategoryFoodAndWater = new GUIPanelButton(
         "panelCategoryFoodAndWater", 1, "farmAbandoned", Text.getText("mapEditorPlayRightPanel.button.build.foodAndWater"), true
      );
      this.guiButtons.add(this.panelCategoryFoodAndWater);
      this.panelCategoryGatesAndWalls = new GUIPanelButton(
         "panelCategoryGatesAndWalls", 1, "curtainWall", Text.getText("mapEditorPlayRightPanel.button.build.gatesAndWalls"), true
      );
      this.guiButtons.add(this.panelCategoryGatesAndWalls);
      this.panelCategoryHarvesting = new GUIPanelButton(
         "panelCategoryHarvesting", 1, "lumberShackAbandoned", Text.getText("mapEditorPlayRightPanel.button.build.harvesting"), true
      );
      this.guiButtons.add(this.panelCategoryHarvesting);
      this.panelCategoryHousing = new GUIPanelButton(
         "panelCategoryHousing", 1, "housingAbandoned", Text.getText("mapEditorPlayRightPanel.button.build.housing"), true
      );
      this.guiButtons.add(this.panelCategoryHousing);
      this.panelCategoryLighting = new GUIPanelButton(
         "panelCategoryLighting", 1, "largeFirePitAbandoned", Text.getText("mapEditorPlayRightPanel.button.build.lighting"), true
      );
      this.guiButtons.add(this.panelCategoryLighting);
      this.panelCategoryMagic = new GUIPanelButton(
         "panelCategoryMagic", 1, "cullisGateAbandoned", Text.getText("mapEditorPlayRightPanel.button.build.magic"), true
      );
      this.guiButtons.add(this.panelCategoryMagic);
      this.panelCategoryManufacturing = new GUIPanelButton(
         "panelCategoryManufacturing", 1, "bowyerAbandoned", Text.getText("mapEditorPlayRightPanel.button.build.manufacturing"), true
      );
      this.guiButtons.add(this.panelCategoryManufacturing);
      this.panelCategoryRefining = new GUIPanelButton(
         "panelCategoryRefining", 1, "stoneCutteryAbandoned", Text.getText("mapEditorPlayRightPanel.button.build.refining"), true
      );
      this.guiButtons.add(this.panelCategoryRefining);
      this.panelCategoryStorage = new GUIPanelButton(
         "panelCategoryStorage", 1, "miscellaneousStorageAbandoned", Text.getText("mapEditorPlayRightPanel.button.build.storage"), true
      );
      this.guiButtons.add(this.panelCategoryStorage);
      this.panelCategoryTrash = new GUIPanelButton(
         "panelCategoryTrash", 1, "landfillAbandoned", Text.getText("mapEditorPlayRightPanel.button.build.trash"), true
      );
      this.guiButtons.add(this.panelCategoryTrash);
      this.panelCategorySpecial = new GUIPanelButton("panelCategorySpecial", 1, "camp", Text.getText("mapEditorPlayRightPanel.button.build.special"), true);
      this.guiButtons.add(this.panelCategorySpecial);
      this.panelAncientCullisGateAbandoned = new GUIPanelButton(
         "panelAncientCullisGateAbandoned",
         1,
         "ancientCullisGateAbandoned",
         Text.getText("mapEditorPlayRightPanel.button.ancient.ancientCullisGateAbandoned"),
         false
      );
      this.guiButtons.add(this.panelAncientCullisGateAbandoned);
      this.panelAncientCullisGate = new GUIPanelButton(
         "panelAncientCullisGate", 1, "ancientCullisGate", Text.getText("mapEditorPlayRightPanel.button.ancient.ancientCullisGate"), false
      );
      this.guiButtons.add(this.panelAncientCullisGate);
      this.panelAncientRadiancePoolAbandoned = new GUIPanelButton(
         "panelAncientRadiancePoolAbandoned",
         1,
         "ancientRadiancePoolAbandoned",
         Text.getText("mapEditorPlayRightPanel.button.ancient.ancientRadiancePoolAbandoned"),
         false
      );
      this.guiButtons.add(this.panelAncientRadiancePoolAbandoned);
      this.panelAncientRadiancePool = new GUIPanelButton(
         "panelAncientRadiancePool", 1, "ancientRadiancePool", Text.getText("mapEditorPlayRightPanel.button.ancient.ancientRadiancePool"), false
      );
      this.guiButtons.add(this.panelAncientRadiancePool);
      this.panelAncillary = new GUIPanelButton(
         "panelAncillary", 1, "ancillaryAbandoned", Text.getText("mapEditorPlayRightPanel.button.civics.ancillary"), false
      );
      this.guiButtons.add(this.panelAncillary);
      this.panelClinic = new GUIPanelButton("panelClinic", 1, "clinicAbandoned", Text.getText("mapEditorPlayRightPanel.button.civics.clinic"), false);
      this.guiButtons.add(this.panelClinic);
      this.panelCourierStation = new GUIPanelButton(
         "panelCourierStation", 1, "courierStationAbandoned", Text.getText("mapEditorPlayRightPanel.button.civics.courierStation"), false
      );
      this.guiButtons.add(this.panelCourierStation);
      this.panelMaintenanceBuilding = new GUIPanelButton(
         "panelMaintenanceBuilding", 1, "maintenanceBuildingAbandoned", Text.getText("mapEditorPlayRightPanel.button.civics.maintenanceBuilding"), false
      );
      this.guiButtons.add(this.panelMaintenanceBuilding);
      this.panelMarketplace = new GUIPanelButton(
         "panelMarketplace", 1, "marketplaceAbandoned", Text.getText("mapEditorPlayRightPanel.button.civics.marketplace"), false
      );
      this.guiButtons.add(this.panelMarketplace);
      this.panelWayMakerShack = new GUIPanelButton(
         "panelWayMakerShack", 1, "wayMakerShackAbandoned", Text.getText("mapEditorPlayRightPanel.button.civics.wayMakerShack"), false
      );
      this.guiButtons.add(this.panelWayMakerShack);
      this.panelMigrationWayStation = new GUIPanelButton(
         "panelMigrationWayStation", 1, "migrationWayStationAbandoned", Text.getText("mapEditorPlayRightPanel.button.civics.migrationWayStation"), false
      );
      this.guiButtons.add(this.panelMigrationWayStation);
      this.panelCategoryDefenseGolems = new GUIPanelButton(
         "panelCategoryDefenseGolems", 1, "woodGolemCombobulatorAbandoned", Text.getText("mapEditorPlayRightPanel.button.defense.golems"), true
      );
      this.guiButtons.add(this.panelCategoryDefenseGolems);
      this.panelCategoryDefenseTowers = new GUIPanelButton(
         "panelCategoryDefenseTowers", 1, "ballistaTowerAbandoned", Text.getText("mapEditorPlayRightPanel.button.defense.towers"), true
      );
      this.guiButtons.add(this.panelCategoryDefenseTowers);
      this.panelCategoryDefenseMiscellaneous = new GUIPanelButton(
         "panelCategoryDefenseMiscellaneous", 1, "lightningRodAbandoned", Text.getText("mapEditorPlayRightPanel.button.defense.miscellaneous"), true
      );
      this.guiButtons.add(this.panelCategoryDefenseMiscellaneous);
      this.panelCrystalGolemCombobulator = new GUIPanelButton(
         "panelCrystalGolemCombobulator",
         1,
         "crystalGolemCombobulatorAbandoned",
         Text.getText("mapEditorPlayRightPanel.button.golems.crystalGolemCombobulator"),
         false
      );
      this.guiButtons.add(this.panelCrystalGolemCombobulator);
      this.panelStoneGolemCombobulator = new GUIPanelButton(
         "panelStoneGolemCombobulator",
         1,
         "stoneGolemCombobulatorAbandoned",
         Text.getText("mapEditorPlayRightPanel.button.golems.stoneGolemCombobulator"),
         false
      );
      this.guiButtons.add(this.panelStoneGolemCombobulator);
      this.panelWoodGolemCombobulator = new GUIPanelButton(
         "panelWoodGolemCombobulator", 1, "woodGolemCombobulatorAbandoned", Text.getText("mapEditorPlayRightPanel.button.golems.woodGolemCombobulator"), false
      );
      this.guiButtons.add(this.panelWoodGolemCombobulator);
      this.panelAttractTower = new GUIPanelButton(
         "panelAttractTower", 1, "attractTowerAbandoned", Text.getText("mapEditorPlayRightPanel.button.towers.attractTower"), false
      );
      this.guiButtons.add(this.panelAttractTower);
      this.panelBanishTower = new GUIPanelButton(
         "panelBanishTower", 1, "banishTowerAbandoned", Text.getText("mapEditorPlayRightPanel.button.towers.banishTower"), false
      );
      this.guiButtons.add(this.panelBanishTower);
      this.panelBallistaTower = new GUIPanelButton(
         "panelBallistaTower", 1, "ballistaTowerAbandoned", Text.getText("mapEditorPlayRightPanel.button.towers.ballistaTower"), false
      );
      this.guiButtons.add(this.panelBallistaTower);
      this.panelBowTower = new GUIPanelButton("panelBowTower", 1, "bowTowerAbandoned", Text.getText("mapEditorPlayRightPanel.button.towers.bowTower"), false);
      this.guiButtons.add(this.panelBowTower);
      this.panelBulletTower = new GUIPanelButton(
         "panelBulletTower", 1, "bulletTowerAbandoned", Text.getText("mapEditorPlayRightPanel.button.towers.bulletTower"), false
      );
      this.guiButtons.add(this.panelBulletTower);
      this.panelElementalBoltTower = new GUIPanelButton(
         "panelElementalBoltTower", 1, "elementalBoltTowerAbandoned", Text.getText("mapEditorPlayRightPanel.button.towers.elementalBoltTower"), false
      );
      this.guiButtons.add(this.panelElementalBoltTower);
      this.panelPhantomDartTower = new GUIPanelButton(
         "panelPhantomDartTower", 1, "phantomDartTowerAbandoned", Text.getText("mapEditorPlayRightPanel.button.towers.phantomDartTower"), false
      );
      this.guiButtons.add(this.panelPhantomDartTower);
      this.panelSlingTower = new GUIPanelButton(
         "panelSlingTower", 1, "slingTowerAbandoned", Text.getText("mapEditorPlayRightPanel.button.towers.slingTower"), false
      );
      this.guiButtons.add(this.panelSlingTower);
      this.panelSprayTower = new GUIPanelButton(
         "panelSprayTower", 1, "sprayTowerAbandoned", Text.getText("mapEditorPlayRightPanel.button.towers.sprayTower"), false
      );
      this.guiButtons.add(this.panelSprayTower);
      this.panelStaticTower = new GUIPanelButton(
         "panelStaticTower", 1, "staticTowerAbandoned", Text.getText("mapEditorPlayRightPanel.button.towers.staticTower"), false
      );
      this.guiButtons.add(this.panelStaticTower);
      this.panelLightningRod = new GUIPanelButton(
         "panelLightningRod", 1, "lightningRodAbandoned", Text.getText("mapEditorPlayRightPanel.button.defenseMiscellaneous.lightningRod"), false
      );
      this.guiButtons.add(this.panelLightningRod);
      this.panelRecombobulatorTower = new GUIPanelButton(
         "panelRecombobulatorTower",
         1,
         "recombobulatorTowerAbandoned",
         Text.getText("mapEditorPlayRightPanel.button.defenseMiscellaneous.recombobulatorTower"),
         false
      );
      this.guiButtons.add(this.panelRecombobulatorTower);
      this.panelAnimalPen = new GUIPanelButton(
         "panelAnimalPen", 1, "animalPenAbandoned", Text.getText("mapEditorPlayRightPanel.button.foodAndWater.animalPen"), false
      );
      this.guiButtons.add(this.panelAnimalPen);
      this.panelBottler = new GUIPanelButton("panelBottler", 1, "bottlerAbandoned", Text.getText("mapEditorPlayRightPanel.button.foodAndWater.bottler"), false);
      this.guiButtons.add(this.panelBottler);
      this.panelCluckerCoop = new GUIPanelButton(
         "panelCluckerCoop", 1, "cluckerCoopAbandoned", Text.getText("mapEditorPlayRightPanel.button.foodAndWater.cluckerCoop"), false
      );
      this.guiButtons.add(this.panelCluckerCoop);
      this.panelFarm = new GUIPanelButton("panelFarm", 1, "farm", Text.getText("mapEditorPlayRightPanel.button.foodAndWater.farm"), false);
      this.guiButtons.add(this.panelFarm);
      this.panelFishingHut = new GUIPanelButton(
         "panelFishingHut", 1, "fishingHutAbandoned", Text.getText("mapEditorPlayRightPanel.button.foodAndWater.fishingHut"), false
      );
      this.guiButtons.add(this.panelFishingHut);
      this.panelSmallFountain = new GUIPanelButton(
         "panelSmallFountain", 1, "smallFountainAbandoned", Text.getText("mapEditorPlayRightPanel.button.foodAndWater.smallFountain"), false
      );
      this.guiButtons.add(this.panelSmallFountain);
      this.panelLargeFountain = new GUIPanelButton(
         "panelLargeFountain", 1, "largeFountainAbandoned", Text.getText("mapEditorPlayRightPanel.button.foodAndWater.largeFountain"), false
      );
      this.guiButtons.add(this.panelLargeFountain);
      this.panelKitchen = new GUIPanelButton("panelKitchen", 1, "kitchenAbandoned", Text.getText("mapEditorPlayRightPanel.button.foodAndWater.kitchen"), false);
      this.guiButtons.add(this.panelKitchen);
      this.panelOutpost = new GUIPanelButton("panelOutpost", 1, "outpostAbandoned", Text.getText("mapEditorPlayRightPanel.button.foodAndWater.outpost"), false);
      this.guiButtons.add(this.panelOutpost);
      this.panelRainCatcher = new GUIPanelButton(
         "panelRainCatcher", 1, "rainCatcherAbandoned", Text.getText("mapEditorPlayRightPanel.button.foodAndWater.rainCatcher"), false
      );
      this.guiButtons.add(this.panelRainCatcher);
      this.panelRangerLodge = new GUIPanelButton(
         "panelRangerLodge", 1, "rangerLodgeAbandoned", Text.getText("mapEditorPlayRightPanel.button.foodAndWater.rangerLodge"), false
      );
      this.guiButtons.add(this.panelRangerLodge);
      this.panelWaterPurifier = new GUIPanelButton(
         "panelWaterPurifier", 1, "waterPurifierAbandoned", Text.getText("mapEditorPlayRightPanel.button.foodAndWater.waterPurifier"), false
      );
      this.guiButtons.add(this.panelWaterPurifier);
      this.panelWell = new GUIPanelButton("panelWell", 1, "wellAbandoned", Text.getText("mapEditorPlayRightPanel.button.foodAndWater.well"), false);
      this.guiButtons.add(this.panelWell);
      this.panelCategoryGatesAndWallsGates = new GUIPanelButton(
         "panelCategoryGatesAndWallsGates", 1, "woodFenceGateNS", Text.getText("mapEditorPlayRightPanel.button.gatesAndWalls.gates"), false
      );
      this.guiButtons.add(this.panelCategoryGatesAndWallsGates);
      this.panelCategoryGatesAndWallsWalls = new GUIPanelButton(
         "panelCategoryGatesAndWallsWalls", 1, "curtainWall", Text.getText("mapEditorPlayRightPanel.button.gatesAndWalls.walls"), false
      );
      this.guiButtons.add(this.panelCategoryGatesAndWallsWalls);
      this.panelCrylithiumWallGateNS = new GUIPanelButton(
         "panelCrylithiumWallGateNS", 1, "crylithiumWallGateNSAbandoned", Text.getText("mapEditorPlayRightPanel.button.gates.crylithiumWallGateNS"), false
      );
      this.guiButtons.add(this.panelCrylithiumWallGateNS);
      this.panelCrylithiumWallGateWE = new GUIPanelButton(
         "panelCrylithiumWallGateWE", 1, "crylithiumWallGateWEAbandoned", Text.getText("mapEditorPlayRightPanel.button.gates.crylithiumWallGateWE"), false
      );
      this.guiButtons.add(this.panelCrylithiumWallGateWE);
      this.panelStoneWallGateNS = new GUIPanelButton(
         "panelStoneWallGateNS", 1, "stoneWallGateNSAbandoned", Text.getText("mapEditorPlayRightPanel.button.gates.stoneWallGateNS"), false
      );
      this.guiButtons.add(this.panelStoneWallGateNS);
      this.panelStoneWallGateWE = new GUIPanelButton(
         "panelStoneWallGateWE", 1, "stoneWallGateWEAbandoned", Text.getText("mapEditorPlayRightPanel.button.gates.stoneWallGateWE"), false
      );
      this.guiButtons.add(this.panelStoneWallGateWE);
      this.panelWoodFenceGateNS = new GUIPanelButton(
         "panelWoodFenceGateNS", 1, "woodFenceGateNSAbandoned", Text.getText("mapEditorPlayRightPanel.button.gates.woodFenceGateNS"), false
      );
      this.guiButtons.add(this.panelWoodFenceGateNS);
      this.panelWoodFenceGateWE = new GUIPanelButton(
         "panelWoodFenceGateWE", 1, "woodFenceGateWEAbandoned", Text.getText("mapEditorPlayRightPanel.button.gates.woodFenceGateWE"), false
      );
      this.guiButtons.add(this.panelWoodFenceGateWE);
      this.panelCrylithiumWall = new GUIPanelButton(
         "panelCrylithiumWall", 1, "crylithiumWall", Text.getText("mapEditorPlayRightPanel.button.walls.crylithiumWall"), false
      );
      this.guiButtons.add(this.panelCrylithiumWall);
      this.panelCrylithiumCurtainWall = new GUIPanelButton(
         "panelCrylithiumCurtainWall", 1, "crylithiumCurtainWall", Text.getText("mapEditorPlayRightPanel.button.walls.crylithiumCurtainWall"), false
      );
      this.guiButtons.add(this.panelCrylithiumCurtainWall);
      this.panelCurtainWall = new GUIPanelButton("panelCurtainWall", 1, "curtainWall", Text.getText("mapEditorPlayRightPanel.button.walls.curtainWall"), false);
      this.guiButtons.add(this.panelCurtainWall);
      this.panelStoneWall = new GUIPanelButton("panelStoneWall", 1, "stoneWall", Text.getText("mapEditorPlayRightPanel.button.walls.stoneWall"), false);
      this.guiButtons.add(this.panelStoneWall);
      this.panelTrashyCubeWall = new GUIPanelButton(
         "panelTrashyCubeWall", 1, "trashyCubeWall", Text.getText("mapEditorPlayRightPanel.button.walls.trashyCubeWall"), false
      );
      this.guiButtons.add(this.panelTrashyCubeWall);
      this.panelWoodFence = new GUIPanelButton("panelWoodFence", 1, "woodFence", Text.getText("mapEditorPlayRightPanel.button.walls.woodFence"), false);
      this.guiButtons.add(this.panelWoodFence);
      this.panelCrystalHarvestry = new GUIPanelButton(
         "panelCrystalHarvestry", 1, "crystalHarvestryAbandoned", Text.getText("mapEditorPlayRightPanel.button.harvesting.crystalHarvestry"), false
      );
      this.guiButtons.add(this.panelCrystalHarvestry);
      this.panelLumberShack = new GUIPanelButton(
         "panelLumberShack", 1, "lumberShackAbandoned", Text.getText("mapEditorPlayRightPanel.button.harvesting.lumberShack"), false
      );
      this.guiButtons.add(this.panelLumberShack);
      this.panelMiningFacility = new GUIPanelButton(
         "panelMiningFacility", 1, "miningFacilityAbandoned", Text.getText("mapEditorPlayRightPanel.button.harvesting.miningFacility"), false
      );
      this.guiButtons.add(this.panelMiningFacility);
      this.panelDoggoHouse = new GUIPanelButton(
         "panelDoggoHouse", 1, "doggoHouseAbandoned", Text.getText("mapEditorPlayRightPanel.button.housing.doggoHouse"), false
      );
      this.guiButtons.add(this.panelDoggoHouse);
      this.panelHousing = new GUIPanelButton("panelHousing", 1, "housingAbandoned", Text.getText("mapEditorPlayRightPanel.button.housing.housing"), false);
      this.guiButtons.add(this.panelHousing);
      this.panelCrylithiumFirePit = new GUIPanelButton(
         "panelCrylithiumFirePit", 1, "crylithiumfirePitAbandoned", Text.getText("mapEditorPlayRightPanel.button.lighting.crylithiumFirePit"), false
      );
      this.guiButtons.add(this.panelCrylithiumFirePit);
      this.panelLargeCrylithiumFirePit = new GUIPanelButton(
         "panelLargeCrylithiumFirePit",
         1,
         "largeCrylithiumFirePitAbandoned",
         Text.getText("mapEditorPlayRightPanel.button.lighting.largeCrylithiumFirePit"),
         false
      );
      this.guiButtons.add(this.panelLargeCrylithiumFirePit);
      this.panelFirePit = new GUIPanelButton("panelFirePit", 1, "firePitAbandoned", Text.getText("mapEditorPlayRightPanel.button.lighting.firePit"), false);
      this.guiButtons.add(this.panelFirePit);
      this.panelLargeFirePit = new GUIPanelButton(
         "panelLargeFirePit", 1, "largeFirePitAbandoned", Text.getText("mapEditorPlayRightPanel.button.lighting.largeFirePit"), false
      );
      this.guiButtons.add(this.panelLargeFirePit);
      this.panelCrystalMotivator = new GUIPanelButton(
         "panelCrystalMotivator", 1, "crystalMotivatorAbandoned", Text.getText("mapEditorPlayRightPanel.button.magic.crystalMotivator"), false
      );
      this.guiButtons.add(this.panelCrystalMotivator);
      this.panelCullisGate = new GUIPanelButton(
         "panelCullisGate", 1, "cullisGateAbandoned", Text.getText("mapEditorPlayRightPanel.button.magic.cullisGate"), false
      );
      this.guiButtons.add(this.panelCullisGate);
      this.panelEssenceAltar = new GUIPanelButton(
         "panelEssenceAltar", 1, "essenceAltarAbandoned", Text.getText("mapEditorPlayRightPanel.button.magic.essenceAltar"), false
      );
      this.guiButtons.add(this.panelEssenceAltar);
      this.panelEssenceCollector = new GUIPanelButton(
         "panelEssenceCollector", 1, "essenceCollectorAbandoned", Text.getText("mapEditorPlayRightPanel.button.magic.essenceCollector"), false
      );
      this.guiButtons.add(this.panelEssenceCollector);
      this.panelReliquary = new GUIPanelButton("panelReliquary", 1, "reliquaryAbandoned", Text.getText("mapEditorPlayRightPanel.button.magic.reliquary"), false);
      this.guiButtons.add(this.panelReliquary);
      this.panelArmorsmithy = new GUIPanelButton(
         "panelArmorsmithy", 1, "armorsmithyAbandoned", Text.getText("mapEditorPlayRightPanel.button.manufacturing.armorsmithy"), false
      );
      this.guiButtons.add(this.panelArmorsmithy);
      this.panelBowyer = new GUIPanelButton("panelBowyer", 1, "bowyerAbandoned", Text.getText("mapEditorPlayRightPanel.button.manufacturing.bowyer"), false);
      this.guiButtons.add(this.panelBowyer);
      this.panelRockTumbler = new GUIPanelButton(
         "panelRockTumbler", 1, "rockTumblerAbandoned", Text.getText("mapEditorPlayRightPanel.button.manufacturing.rockTumbler"), false
      );
      this.guiButtons.add(this.panelRockTumbler);
      this.panelToolsmithy = new GUIPanelButton(
         "panelToolsmithy", 1, "toolsmithyAbandoned", Text.getText("mapEditorPlayRightPanel.button.manufacturing.toolsmithy"), false
      );
      this.guiButtons.add(this.panelToolsmithy);
      this.panelCrystillery = new GUIPanelButton(
         "panelCrystillery", 1, "crystilleryAbandoned", Text.getText("mapEditorPlayRightPanel.button.refining.crystillery"), false
      );
      this.guiButtons.add(this.panelCrystillery);
      this.panelForge = new GUIPanelButton("panelForge", 1, "forgeAbandoned", Text.getText("mapEditorPlayRightPanel.button.refining.forge"), false);
      this.guiButtons.add(this.panelForge);
      this.panelLumberMill = new GUIPanelButton(
         "panelLumberMill", 1, "lumberMillAbandoned", Text.getText("mapEditorPlayRightPanel.button.refining.lumberMill"), false
      );
      this.guiButtons.add(this.panelLumberMill);
      this.panelStoneCuttery = new GUIPanelButton(
         "panelStoneCuttery", 1, "stoneCutteryAbandoned", Text.getText("mapEditorPlayRightPanel.button.refining.stoneCuttery"), false
      );
      this.guiButtons.add(this.panelStoneCuttery);
      this.panelAmmoStorage = new GUIPanelButton(
         "panelAmmoStorage", 1, "ammoStorageAbandoned", Text.getText("mapEditorPlayRightPanel.button.storage.ammoStorage"), false
      );
      this.guiButtons.add(this.panelAmmoStorage);
      this.panelCrystalStorage = new GUIPanelButton(
         "panelCrystalStorage", 1, "crystalStorageAbandoned", Text.getText("mapEditorPlayRightPanel.button.storage.crystalStorage"), false
      );
      this.guiButtons.add(this.panelCrystalStorage);
      this.panelEquipmentStorage = new GUIPanelButton(
         "panelEquipmentStorage", 1, "equipmentStorageAbandoned", Text.getText("mapEditorPlayRightPanel.button.storage.equipmentStorage"), false
      );
      this.guiButtons.add(this.panelEquipmentStorage);
      this.panelFoodStorage = new GUIPanelButton(
         "panelFoodStorage", 1, "foodStorageAbandoned", Text.getText("mapEditorPlayRightPanel.button.storage.foodStorage"), false
      );
      this.guiButtons.add(this.panelFoodStorage);
      this.panelGoldStorage = new GUIPanelButton(
         "panelGoldStorage", 1, "goldStorageAbandoned", Text.getText("mapEditorPlayRightPanel.button.storage.goldStorage"), false
      );
      this.guiButtons.add(this.panelGoldStorage);
      this.panelKeyShack = new GUIPanelButton("panelKeyShack", 1, "keyShackAbandoned", Text.getText("mapEditorPlayRightPanel.button.storage.keyShack"), false);
      this.guiButtons.add(this.panelKeyShack);
      this.panelMineralStorage = new GUIPanelButton(
         "panelMineralStorage", 1, "mineralStorageAbandoned", Text.getText("mapEditorPlayRightPanel.button.storage.mineralStorage"), false
      );
      this.guiButtons.add(this.panelMineralStorage);
      this.panelMiscellaneousStorage = new GUIPanelButton(
         "panelMiscellaneousStorage", 1, "miscellaneousStorageAbandoned", Text.getText("mapEditorPlayRightPanel.button.storage.miscellaneousStorage"), false
      );
      this.guiButtons.add(this.panelMiscellaneousStorage);
      this.panelRockStorage = new GUIPanelButton(
         "panelRockStorage", 1, "rockStorageAbandoned", Text.getText("mapEditorPlayRightPanel.button.storage.rockStorage"), false
      );
      this.guiButtons.add(this.panelRockStorage);
      this.panelWoodStorage = new GUIPanelButton(
         "panelWoodStorage", 1, "woodStorageAbandoned", Text.getText("mapEditorPlayRightPanel.button.storage.woodStorage"), false
      );
      this.guiButtons.add(this.panelWoodStorage);
      this.panelBurner = new GUIPanelButton("panelBurner", 1, "burnerAbandoned", Text.getText("mapEditorPlayRightPanel.button.trash.burner"), false);
      this.guiButtons.add(this.panelBurner);
      this.panelCubeEGolemCombobulator = new GUIPanelButton(
         "panelCubeEGolemCombobulator",
         1,
         "cubeEGolemCombobulatorAbandoned",
         Text.getText("mapEditorPlayRightPanel.button.trash.cubeEGolemCombobulator"),
         false
      );
      this.guiButtons.add(this.panelCubeEGolemCombobulator);
      this.panelLandfill = new GUIPanelButton("panelLandfill", 1, "landfillAbandoned", Text.getText("mapEditorPlayRightPanel.button.trash.landfill"), false);
      this.guiButtons.add(this.panelLandfill);
      this.panelProcessor = new GUIPanelButton("panelProcessor", 1, "processorAbandoned", Text.getText("mapEditorPlayRightPanel.button.trash.processor"), false);
      this.guiButtons.add(this.panelProcessor);
      this.panelTrashCan = new GUIPanelButton("panelTrashCan", 1, "trashCanAbandoned", Text.getText("mapEditorPlayRightPanel.button.trash.trashCan"), false);
      this.guiButtons.add(this.panelTrashCan);
      this.panelTrashyCubePile = new GUIPanelButton(
         "panelTrashyCubePile", 1, "trashyCubePileAbandoned", Text.getText("mapEditorPlayRightPanel.button.trash.trashyCubePile"), false
      );
      this.guiButtons.add(this.panelTrashyCubePile);
      this.panelCamp = new GUIPanelButton("panelCamp", 1, "camp", Text.getText("mapEditorPlayRightPanel.button.special.camp"), false);
      this.guiButtons.add(this.panelCamp);
      this.panelLootBox = new GUIPanelButton("panelLootBox", 1, "lootBox", Text.getText("mapEditorPlayRightPanel.button.special.lootBox"), false);
      this.guiButtons.add(this.panelLootBox);
      this.panelCategoryBricks = new GUIPanelButton("panelCategoryBricks", 1, "grayBricks", Text.getText("mapEditorPlayRightPanel.button.tiles.bricks"), true);
      this.guiButtons.add(this.panelCategoryBricks);
      this.panelCategoryCrystals = new GUIPanelButton(
         "panelCategoryCrystals", 1, "blueCrystals", Text.getText("mapEditorPlayRightPanel.button.tiles.crystals"), true
      );
      this.guiButtons.add(this.panelCategoryCrystals);
      this.panelCategoryDirt = new GUIPanelButton("panelCategoryDirt", 1, "brownDirt", Text.getText("mapEditorPlayRightPanel.button.tiles.dirt"), true);
      this.guiButtons.add(this.panelCategoryDirt);
      this.panelCategoryFlowers = new GUIPanelButton(
         "panelCategoryFlowers", 1, "redFlowers", Text.getText("mapEditorPlayRightPanel.button.tiles.flowers"), true
      );
      this.guiButtons.add(this.panelCategoryFlowers);
      this.panelCategoryFood = new GUIPanelButton("panelCategoryFood", 1, "carrots", Text.getText("mapEditorPlayRightPanel.button.tiles.food"), true);
      this.guiButtons.add(this.panelCategoryFood);
      this.panelCategoryGrass = new GUIPanelButton("panelCategoryGrass", 1, "greenGrass", Text.getText("mapEditorPlayRightPanel.button.tiles.grass"), true);
      this.guiButtons.add(this.panelCategoryGrass);
      this.panelCategoryGravel = new GUIPanelButton("panelCategoryGravel", 1, "grayGravel", Text.getText("mapEditorPlayRightPanel.button.tiles.gravel"), true);
      this.guiButtons.add(this.panelCategoryGravel);
      this.panelCategoryLava = new GUIPanelButton("panelCategoryLava", 1, "lava", Text.getText("mapEditorPlayRightPanel.button.tiles.lava"), true);
      this.guiButtons.add(this.panelCategoryLava);
      this.panelCategoryRoads = new GUIPanelButton(
         "panelCategoryRoads", 1, "cutStoneAndBoardRoad", Text.getText("mapEditorPlayRightPanel.button.tiles.roads"), true
      );
      this.guiButtons.add(this.panelCategoryRoads);
      this.panelCategoryRock = new GUIPanelButton("panelCategoryRock", 1, "grayRock", Text.getText("mapEditorPlayRightPanel.button.tiles.rock"), true);
      this.guiButtons.add(this.panelCategoryRock);
      this.panelCategorySand = new GUIPanelButton("panelCategorySand", 1, "tanSand", Text.getText("mapEditorPlayRightPanel.button.tiles.sand"), true);
      this.guiButtons.add(this.panelCategorySand);
      this.panelCategorySandstone = new GUIPanelButton(
         "panelCategorySandstone", 1, "tanSandstone", Text.getText("mapEditorPlayRightPanel.button.tiles.sandstone"), true
      );
      this.guiButtons.add(this.panelCategorySandstone);
      this.panelCategorySnow = new GUIPanelButton("panelCategorySnow", 1, "whiteSnow", Text.getText("mapEditorPlayRightPanel.button.tiles.snow"), true);
      this.guiButtons.add(this.panelCategorySnow);
      this.panelCategoryTar = new GUIPanelButton("panelCategoryTar", 1, "tar", Text.getText("mapEditorPlayRightPanel.button.tiles.tar"), true);
      this.guiButtons.add(this.panelCategoryTar);
      this.panelCategoryTrees = new GUIPanelButton("panelCategoryTrees", 1, "greenTrees", Text.getText("mapEditorPlayRightPanel.button.tiles.trees"), true);
      this.guiButtons.add(this.panelCategoryTrees);
      this.panelCategoryWater = new GUIPanelButton("panelCategoryWater", 1, "water", Text.getText("mapEditorPlayRightPanel.button.tiles.water"), true);
      this.guiButtons.add(this.panelCategoryWater);
      this.panelGrayBricks = new GUIPanelButton("panelGrayBricks", 1, "grayBricks", Text.getText("mapEditorPlayRightPanel.button.bricks.grayBricks"), false);
      this.guiButtons.add(this.panelGrayBricks);
      this.panelGrayTiles = new GUIPanelButton("panelGrayTiles", 1, "grayTiles", Text.getText("mapEditorPlayRightPanel.button.bricks.grayTiles"), false);
      this.guiButtons.add(this.panelGrayTiles);
      this.panelRedCrystals = new GUIPanelButton(
         "panelRedCrystals", 1, "redCrystals", Text.getText("mapEditorPlayRightPanel.button.crystals.redCrystals"), false
      );
      this.guiButtons.add(this.panelRedCrystals);
      this.panelGreenCrystals = new GUIPanelButton(
         "panelGreenCrystals", 1, "greenCrystals", Text.getText("mapEditorPlayRightPanel.button.crystals.greenCrystals"), false
      );
      this.guiButtons.add(this.panelGreenCrystals);
      this.panelBlueCrystals = new GUIPanelButton(
         "panelBlueCrystals", 1, "blueCrystals", Text.getText("mapEditorPlayRightPanel.button.crystals.blueCrystals"), false
      );
      this.guiButtons.add(this.panelBlueCrystals);
      this.panelPurpleCrystals = new GUIPanelButton(
         "panelPurpleCrystals", 1, "purpleCrystals", Text.getText("mapEditorPlayRightPanel.button.crystals.purpleCrystals"), false
      );
      this.guiButtons.add(this.panelPurpleCrystals);
      this.panelBrownDirt = new GUIPanelButton("panelBrownDirt", 1, "brownDirt", Text.getText("mapEditorPlayRightPanel.button.dirt.brownDirt"), false);
      this.guiButtons.add(this.panelBrownDirt);
      this.panelDarkBrownDirt = new GUIPanelButton(
         "panelDarkBrownDirt", 1, "darkBrownDirt", Text.getText("mapEditorPlayRightPanel.button.dirt.darkBrownDirt"), false
      );
      this.guiButtons.add(this.panelDarkBrownDirt);
      this.panelLightBrownDirt = new GUIPanelButton(
         "panelLightBrownDirt", 1, "lightBrownDirt", Text.getText("mapEditorPlayRightPanel.button.dirt.lightBrownDirt"), false
      );
      this.guiButtons.add(this.panelLightBrownDirt);
      this.panelBlackFlowers = new GUIPanelButton(
         "panelBlackFlowers", 1, "blackFlowers", Text.getText("mapEditorPlayRightPanel.button.flowers.blackFlowers"), false
      );
      this.guiButtons.add(this.panelBlackFlowers);
      this.panelBlueFlowers = new GUIPanelButton(
         "panelBlueFlowers", 1, "blueFlowers", Text.getText("mapEditorPlayRightPanel.button.flowers.blueFlowers"), false
      );
      this.guiButtons.add(this.panelBlueFlowers);
      this.panelPurpleFlowers = new GUIPanelButton(
         "panelPurpleFlowers", 1, "purpleFlowers", Text.getText("mapEditorPlayRightPanel.button.flowers.purpleFlowers"), false
      );
      this.guiButtons.add(this.panelPurpleFlowers);
      this.panelRedFlowers = new GUIPanelButton("panelRedFlowers", 1, "redFlowers", Text.getText("mapEditorPlayRightPanel.button.flowers.redFlowers"), false);
      this.guiButtons.add(this.panelRedFlowers);
      this.panelWhiteFlowers = new GUIPanelButton(
         "panelWhiteFlowers", 1, "whiteFlowers", Text.getText("mapEditorPlayRightPanel.button.flowers.whiteFlowers"), false
      );
      this.guiButtons.add(this.panelWhiteFlowers);
      this.panelYellowFlowers = new GUIPanelButton(
         "panelYellowFlowers", 1, "yellowFlowers", Text.getText("mapEditorPlayRightPanel.button.flowers.yellowFlowers"), false
      );
      this.guiButtons.add(this.panelYellowFlowers);
      this.panelCactus = new GUIPanelButton("panelCactus", 1, "cactus", Text.getText("mapEditorPlayRightPanel.button.food.cactus"), false);
      this.guiButtons.add(this.panelCactus);
      this.panelCarrots = new GUIPanelButton("panelCarrots", 1, "carrots", Text.getText("mapEditorPlayRightPanel.button.food.carrots"), false);
      this.guiButtons.add(this.panelCarrots);
      this.panelHolyPotatoes = new GUIPanelButton(
         "panelHolyPotatoes", 1, "holyPotatoes", Text.getText("mapEditorPlayRightPanel.button.food.holyPotatoes"), false
      );
      this.guiButtons.add(this.panelHolyPotatoes);
      this.panelMelons = new GUIPanelButton("panelMelons", 1, "melons", Text.getText("mapEditorPlayRightPanel.button.food.melons"), false);
      this.guiButtons.add(this.panelMelons);
      this.panelMushrooms = new GUIPanelButton("panelMushrooms", 1, "mushrooms", Text.getText("mapEditorPlayRightPanel.button.food.mushrooms"), false);
      this.guiButtons.add(this.panelMushrooms);
      this.panelPotatoes = new GUIPanelButton("panelPotatoes", 1, "potatoes", Text.getText("mapEditorPlayRightPanel.button.food.potatoes"), false);
      this.guiButtons.add(this.panelPotatoes);
      this.panelTurnips = new GUIPanelButton("panelTurnips", 1, "turnips", Text.getText("mapEditorPlayRightPanel.button.food.turnips"), false);
      this.guiButtons.add(this.panelTurnips);
      this.panelEmeraldGreenGrass = new GUIPanelButton(
         "panelEmeraldGreenGrass", 1, "emeraldGreenGrass", Text.getText("mapEditorPlayRightPanel.button.grass.emeraldGreenGrass"), false
      );
      this.guiButtons.add(this.panelEmeraldGreenGrass);
      this.panelGreenGrass = new GUIPanelButton("panelGreenGrass", 1, "greenGrass", Text.getText("mapEditorPlayRightPanel.button.grass.greenGrass"), false);
      this.guiButtons.add(this.panelGreenGrass);
      this.panelTealGrass = new GUIPanelButton("panelTealGrass", 1, "tealGrass", Text.getText("mapEditorPlayRightPanel.button.grass.tealGrass"), false);
      this.guiButtons.add(this.panelTealGrass);
      this.panelYellowBrownGrass = new GUIPanelButton(
         "panelYellowBrownGrass", 1, "yellowBrownGrass", Text.getText("mapEditorPlayRightPanel.button.grass.yellowBrownGrass"), false
      );
      this.guiButtons.add(this.panelYellowBrownGrass);
      this.panelBlueGravel = new GUIPanelButton("panelBlueGravel", 1, "blueGravel", Text.getText("mapEditorPlayRightPanel.button.gravel.blueGravel"), false);
      this.guiButtons.add(this.panelBlueGravel);
      this.panelGrayGravel = new GUIPanelButton("panelGrayGravel", 1, "grayGravel", Text.getText("mapEditorPlayRightPanel.button.gravel.grayGravel"), false);
      this.guiButtons.add(this.panelGrayGravel);
      this.panelRedGravel = new GUIPanelButton("panelRedGravel", 1, "redGravel", Text.getText("mapEditorPlayRightPanel.button.gravel.redGravel"), false);
      this.guiButtons.add(this.panelRedGravel);
      this.panelLava = new GUIPanelButton("panelLava", 1, "lava", Text.getText("mapEditorPlayRightPanel.button.lava.lava"), false);
      this.guiButtons.add(this.panelLava);
      this.panelPath = new GUIPanelButton("panelPath", 1, "path", Text.getText("mapEditorPlayRightPanel.button.roads.path"), false);
      this.guiButtons.add(this.panelPath);
      this.panelLogPathDebris = new GUIPanelButton(
         "panelLogPathDebris", 1, "logPathDebris", Text.getText("mapEditorPlayRightPanel.button.roads.logPathDebris"), false
      );
      this.guiButtons.add(this.panelLogPathDebris);
      this.panelLogPath = new GUIPanelButton("panelLogPath", 1, "logPath", Text.getText("mapEditorPlayRightPanel.button.roads.logPath"), false);
      this.guiButtons.add(this.panelLogPath);
      this.panelCobbleAndLogPathDebris = new GUIPanelButton(
         "panelCobbleAndLogPathDebris", 1, "cobbleAndLogPathDebris", Text.getText("mapEditorPlayRightPanel.button.roads.cobbleAndLogPathDebris"), false
      );
      this.guiButtons.add(this.panelCobbleAndLogPathDebris);
      this.panelCobbleAndLogPath = new GUIPanelButton(
         "panelCobbleAndLogPath", 1, "cobbleAndLogPath", Text.getText("mapEditorPlayRightPanel.button.roads.cobbleAndLogPath"), false
      );
      this.guiButtons.add(this.panelCobbleAndLogPath);
      this.panelCobbleAndBoardRoadDebris = new GUIPanelButton(
         "panelCobbleAndBoardRoadDebris", 1, "cobbleAndBoardRoadDebris", Text.getText("mapEditorPlayRightPanel.button.roads.cobbleAndBoardRoadDebris"), false
      );
      this.guiButtons.add(this.panelCobbleAndBoardRoadDebris);
      this.panelCobbleAndBoardRoad = new GUIPanelButton(
         "panelCobbleAndBoardRoad", 1, "cobbleAndBoardRoad", Text.getText("mapEditorPlayRightPanel.button.roads.cobbleAndBoardRoad"), false
      );
      this.guiButtons.add(this.panelCobbleAndBoardRoad);
      this.panelCutStoneAndBoardRoadDebris = new GUIPanelButton(
         "panelCutStoneAndBoardRoadDebris",
         1,
         "cutStoneAndBoardRoadDebris",
         Text.getText("mapEditorPlayRightPanel.button.roads.cutStoneAndBoardRoadDebris"),
         false
      );
      this.guiButtons.add(this.panelCutStoneAndBoardRoadDebris);
      this.panelCutStoneAndBoardRoad = new GUIPanelButton(
         "panelCutStoneAndBoardRoad", 1, "cutStoneAndBoardRoad", Text.getText("mapEditorPlayRightPanel.button.roads.cutStoneAndBoardRoad"), false
      );
      this.guiButtons.add(this.panelCutStoneAndBoardRoad);
      this.panelBlackRock = new GUIPanelButton("panelBlackRock", 1, "blackRock", Text.getText("mapEditorPlayRightPanel.button.rock.blackRock"), false);
      this.guiButtons.add(this.panelBlackRock);
      this.panelBrownRock = new GUIPanelButton("panelBrownRock", 1, "brownRock", Text.getText("mapEditorPlayRightPanel.button.rock.brownRock"), false);
      this.guiButtons.add(this.panelBrownRock);
      this.panelGrayRock = new GUIPanelButton("panelGrayRock", 1, "grayRock", Text.getText("mapEditorPlayRightPanel.button.rock.grayRock"), false);
      this.guiButtons.add(this.panelGrayRock);
      this.panelRedRock = new GUIPanelButton("panelRedRock", 1, "redRock", Text.getText("mapEditorPlayRightPanel.button.rock.redRock"), false);
      this.guiButtons.add(this.panelRedRock);
      this.panelWhiteRock = new GUIPanelButton("panelWhiteRock", 1, "whiteRock", Text.getText("mapEditorPlayRightPanel.button.rock.whiteRock"), false);
      this.guiButtons.add(this.panelWhiteRock);
      this.panelBlackSand = new GUIPanelButton("panelBlackSand", 1, "blackSand", Text.getText("mapEditorPlayRightPanel.button.sand.blackSand"), false);
      this.guiButtons.add(this.panelBlackSand);
      this.panelRedSand = new GUIPanelButton("panelRedSand", 1, "redSand", Text.getText("mapEditorPlayRightPanel.button.sand.redSand"), false);
      this.guiButtons.add(this.panelRedSand);
      this.panelTanSand = new GUIPanelButton("panelTanSand", 1, "tanSand", Text.getText("mapEditorPlayRightPanel.button.sand.tanSand"), false);
      this.guiButtons.add(this.panelTanSand);
      this.panelBlackSandstone = new GUIPanelButton(
         "panelBlackSandstone", 1, "blackSandstone", Text.getText("mapEditorPlayRightPanel.button.sandstone.blackSandstone"), false
      );
      this.guiButtons.add(this.panelBlackSandstone);
      this.panelRedSandstone = new GUIPanelButton(
         "panelRedSandstone", 1, "redSandstone", Text.getText("mapEditorPlayRightPanel.button.sandstone.redSandstone"), false
      );
      this.guiButtons.add(this.panelRedSandstone);
      this.panelTanSandstone = new GUIPanelButton(
         "panelTanSandstone", 1, "tanSandstone", Text.getText("mapEditorPlayRightPanel.button.sandstone.tanSandstone"), false
      );
      this.guiButtons.add(this.panelTanSandstone);
      this.panelWhiteSnow = new GUIPanelButton("panelWhiteSnow", 1, "whiteSnow", Text.getText("mapEditorPlayRightPanel.button.snow.whiteSnow"), false);
      this.guiButtons.add(this.panelWhiteSnow);
      this.panelTar = new GUIPanelButton("panelTar", 1, "tar", Text.getText("mapEditorPlayRightPanel.button.tar.tar"), false);
      this.guiButtons.add(this.panelTar);
      this.panelBlueDeadTrees = new GUIPanelButton(
         "panelBlueDeadTrees", 1, "blueDeadTrees", Text.getText("mapEditorPlayRightPanel.button.trees.blueDeadTrees"), false
      );
      this.guiButtons.add(this.panelBlueDeadTrees);
      this.panelBlueStumps = new GUIPanelButton("panelBlueStumps", 1, "blueStumps", Text.getText("mapEditorPlayRightPanel.button.trees.blueStumps"), false);
      this.guiButtons.add(this.panelBlueStumps);
      this.panelBlueTrees = new GUIPanelButton("panelBlueTrees", 1, "blueTrees", Text.getText("mapEditorPlayRightPanel.button.trees.blueTrees"), false);
      this.guiButtons.add(this.panelBlueTrees);
      this.panelBrownDeadTrees = new GUIPanelButton(
         "panelBrownDeadTrees", 1, "brownDeadTrees", Text.getText("mapEditorPlayRightPanel.button.trees.brownDeadTrees"), false
      );
      this.guiButtons.add(this.panelBrownDeadTrees);
      this.panelBrownStumps = new GUIPanelButton("panelBrownStumps", 1, "brownStumps", Text.getText("mapEditorPlayRightPanel.button.trees.brownStumps"), false);
      this.guiButtons.add(this.panelBrownStumps);
      this.panelBrownTrees = new GUIPanelButton("panelBrownTrees", 1, "brownTrees", Text.getText("mapEditorPlayRightPanel.button.trees.brownTrees"), false);
      this.guiButtons.add(this.panelBrownTrees);
      this.panelDarkGreenDeadTrees = new GUIPanelButton(
         "panelDarkGreenDeadTrees", 1, "darkGreenDeadTrees", Text.getText("mapEditorPlayRightPanel.button.trees.darkGreenDeadTrees"), false
      );
      this.guiButtons.add(this.panelDarkGreenDeadTrees);
      this.panelDarkGreenStumps = new GUIPanelButton(
         "panelDarkGreenStumps", 1, "darkGreenStumps", Text.getText("mapEditorPlayRightPanel.button.trees.darkGreenStumps"), false
      );
      this.guiButtons.add(this.panelDarkGreenStumps);
      this.panelDarkGreenTrees = new GUIPanelButton(
         "panelDarkGreenTrees", 1, "darkGreenTrees", Text.getText("mapEditorPlayRightPanel.button.trees.darkGreenTrees"), false
      );
      this.guiButtons.add(this.panelDarkGreenTrees);
      this.panelGreenDeadTrees = new GUIPanelButton(
         "panelGreenDeadTrees", 1, "greenDeadTrees", Text.getText("mapEditorPlayRightPanel.button.trees.greenDeadTrees"), false
      );
      this.guiButtons.add(this.panelGreenDeadTrees);
      this.panelGreenStumps = new GUIPanelButton("panelGreenStumps", 1, "greenStumps", Text.getText("mapEditorPlayRightPanel.button.trees.greenStumps"), false);
      this.guiButtons.add(this.panelGreenStumps);
      this.panelGreenTrees = new GUIPanelButton("panelGreenTrees", 1, "greenTrees", Text.getText("mapEditorPlayRightPanel.button.trees.greenTrees"), false);
      this.guiButtons.add(this.panelGreenTrees);
      this.panelLavenderDeadTrees = new GUIPanelButton(
         "panelLavenderDeadTrees", 1, "lavenderDeadTrees", Text.getText("mapEditorPlayRightPanel.button.trees.lavenderDeadTrees"), false
      );
      this.guiButtons.add(this.panelLavenderDeadTrees);
      this.panelLavenderStumps = new GUIPanelButton(
         "panelLavenderStumps", 1, "lavenderStumps", Text.getText("mapEditorPlayRightPanel.button.trees.lavenderStumps"), false
      );
      this.guiButtons.add(this.panelLavenderStumps);
      this.panelLavenderTrees = new GUIPanelButton(
         "panelLavenderTrees", 1, "lavenderTrees", Text.getText("mapEditorPlayRightPanel.button.trees.lavenderTrees"), false
      );
      this.guiButtons.add(this.panelLavenderTrees);
      this.panelPaleBlueDeadTrees = new GUIPanelButton(
         "panelPaleBlueDeadTrees", 1, "paleBlueDeadTrees", Text.getText("mapEditorPlayRightPanel.button.trees.paleBlueDeadTrees"), false
      );
      this.guiButtons.add(this.panelPaleBlueDeadTrees);
      this.panelPaleBlueStumps = new GUIPanelButton(
         "panelPaleBlueStumps", 1, "paleBlueStumps", Text.getText("mapEditorPlayRightPanel.button.trees.paleBlueStumps"), false
      );
      this.guiButtons.add(this.panelPaleBlueStumps);
      this.panelPaleBlueTrees = new GUIPanelButton(
         "panelPaleBlueTrees", 1, "paleBlueTrees", Text.getText("mapEditorPlayRightPanel.button.trees.paleBlueTrees"), false
      );
      this.guiButtons.add(this.panelPaleBlueTrees);
      this.panelRedDeadTrees = new GUIPanelButton(
         "panelRedDeadTrees", 1, "redDeadTrees", Text.getText("mapEditorPlayRightPanel.button.trees.redDeadTrees"), false
      );
      this.guiButtons.add(this.panelRedDeadTrees);
      this.panelRedStumps = new GUIPanelButton("panelRedStumps", 1, "redStumps", Text.getText("mapEditorPlayRightPanel.button.trees.redStumps"), false);
      this.guiButtons.add(this.panelRedStumps);
      this.panelRedTrees = new GUIPanelButton("panelRedTrees", 1, "redTrees", Text.getText("mapEditorPlayRightPanel.button.trees.redTrees"), false);
      this.guiButtons.add(this.panelRedTrees);
      this.panelWater = new GUIPanelButton("panelWater", 1, "water", Text.getText("mapEditorPlayRightPanel.button.water.water"), false);
      this.guiButtons.add(this.panelWater);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() - 196;
      this.y = ScaleControl.getInterfaceCenterY() - 265;
      if (this.y < 134) {
         this.y = 134;
      }

      if (this.hidden) {
         this.x = ScaleControl.getInterfaceWidth() - 10;
      }

      this.mask.setX(this.x);
      this.mask.setY(this.y);
      if (this.hidden) {
         this.hidePanel.render(this.g, this.mouse, this.x - 28, this.y, false, debug);
      } else {
         this.hidePanel.render(this.g, this.mouse, this.x + 162, this.y, false, debug);
      }

      if (this.backPage == null) {
         this.pageBackTop.render(this.g, this.mouse, this.x, this.y, true, debug);
      } else {
         this.pageBackTop.render(this.g, this.mouse, this.x, this.y, false, debug);
      }

      if (this.lastPage == null) {
         this.pageUpTop.render(this.g, this.mouse, this.x + 67, this.y, true, debug);
      } else {
         this.pageUpTop.render(this.g, this.mouse, this.x + 67, this.y, false, debug);
      }

      if (this.nextPage == null) {
         this.pageDownTop.render(this.g, this.mouse, this.x + 96, this.y, true, debug);
      } else {
         this.pageDownTop.render(this.g, this.mouse, this.x + 96, this.y, false, debug);
      }

      int yShift = 0;
      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_CATEGORY_SANDBOX_TOOLS) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = null;
         this.panelCategoryBuild.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryBuild.getHeight();
         this.panelCategoryTiles.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryTiles.getHeight();
         yShift = this.renderBlankPanels(4, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1) {
         this.lastPage = null;
         this.nextPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_2;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_SANDBOX_TOOLS;
         this.panelCategoryCivics.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryCivics.getHeight();
         this.panelCategoryAncient.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryAncient.getHeight();
         this.panelCategoryDefense.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryDefense.getHeight();
         this.panelCategoryFoodAndWater.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryFoodAndWater.getHeight();
         this.panelCategoryGatesAndWalls.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryGatesAndWalls.getHeight();
         this.panelCategoryHousing.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryHousing.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_2) {
         this.lastPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1;
         this.nextPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_3;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_SANDBOX_TOOLS;
         this.panelCategoryHarvesting.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryHarvesting.getHeight();
         this.panelCategoryLighting.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryLighting.getHeight();
         this.panelCategoryMagic.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryMagic.getHeight();
         this.panelCategoryManufacturing.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryManufacturing.getHeight();
         this.panelCategoryRefining.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryRefining.getHeight();
         this.panelCategoryStorage.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryStorage.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_3) {
         this.lastPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_2;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_SANDBOX_TOOLS;
         this.panelCategoryTrash.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryStorage.getHeight();
         if (SettingsParser.getDebug()) {
            this.panelCategorySpecial.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
            yShift += this.panelCategorySpecial.getHeight();
            yShift = this.renderBlankPanels(4, this.x, this.y, yShift, this.g);
         } else {
            yShift = this.renderBlankPanels(5, this.x, this.y, yShift, this.g);
         }
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_ANCIENT) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1;
         this.panelAncientCullisGateAbandoned
            .render(
               this.g,
               this.mouse,
               this.x,
               this.y + 28 + yShift,
               this.object.objectCount(MapTilesLoader.TileSet.ANCIENT_CULLIS_GATE, ObjectBase.ObjectSubType.ABANDONED),
               false,
               debug
            );
         yShift += this.panelAncientCullisGateAbandoned.getHeight();
         this.panelAncientCullisGate
            .render(
               this.g,
               this.mouse,
               this.x,
               this.y + 28 + yShift,
               this.object.objectCount(MapTilesLoader.TileSet.ANCIENT_CULLIS_GATE, ObjectBase.ObjectSubType.BUILT),
               false,
               debug
            );
         yShift += this.panelAncientCullisGate.getHeight();
         this.panelAncientRadiancePoolAbandoned
            .render(
               this.g,
               this.mouse,
               this.x,
               this.y + 28 + yShift,
               this.object.objectCount(MapTilesLoader.TileSet.ANCIENT_RADIANCE_POOL, ObjectBase.ObjectSubType.ABANDONED),
               false,
               debug
            );
         yShift += this.panelAncientRadiancePoolAbandoned.getHeight();
         this.panelAncientRadiancePool
            .render(
               this.g,
               this.mouse,
               this.x,
               this.y + 28 + yShift,
               this.object.objectCount(MapTilesLoader.TileSet.ANCIENT_RADIANCE_POOL, ObjectBase.ObjectSubType.BUILT),
               false,
               debug
            );
         yShift += this.panelAncientRadiancePool.getHeight();
         yShift = this.renderBlankPanels(2, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_CIVICS_1) {
         this.lastPage = null;
         this.nextPage = GUIEnums.GUIPanelPage.RIGHT_CIVICS_2;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1;
         this.panelAncillary.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.ANCILLARY), false, debug);
         yShift += this.panelAncillary.getHeight();
         this.panelClinic.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CLINIC), false, debug);
         yShift += this.panelClinic.getHeight();
         this.panelCourierStation
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.COURIER_STATION), false, debug);
         yShift += this.panelCourierStation.getHeight();
         this.panelLandfill.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.LANDFILL), false, debug);
         yShift += this.panelLandfill.getHeight();
         this.panelMaintenanceBuilding
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.MAINTENANCE_BUILDING), false, debug);
         yShift += this.panelMaintenanceBuilding.getHeight();
         this.panelMarketplace
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.MARKETPLACE), false, debug);
         yShift += this.panelMarketplace.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_CIVICS_2) {
         this.lastPage = GUIEnums.GUIPanelPage.RIGHT_CIVICS_1;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1;
         this.panelWayMakerShack
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.WAY_MAKER_SHACK), false, debug);
         yShift += this.panelWayMakerShack.getHeight();
         this.panelMigrationWayStation
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.MIGRATION_WAY_STATION), false, debug);
         yShift += this.panelMigrationWayStation.getHeight();
         yShift = this.renderBlankPanels(4, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_DEFENSE) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1;
         this.panelCategoryDefenseGolems.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryDefenseGolems.getHeight();
         this.panelCategoryDefenseTowers.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryDefenseTowers.getHeight();
         this.panelCategoryDefenseMiscellaneous.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryDefenseMiscellaneous.getHeight();
         yShift = this.renderBlankPanels(3, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_DEFENSE_GOLEMS) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_DEFENSE;
         this.panelCrystalGolemCombobulator
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR), false, debug);
         yShift += this.panelCrystalGolemCombobulator.getHeight();
         this.panelStoneGolemCombobulator
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR), false, debug);
         yShift += this.panelStoneGolemCombobulator.getHeight();
         this.panelWoodGolemCombobulator
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR), false, debug);
         yShift += this.panelWoodGolemCombobulator.getHeight();
         yShift = this.renderBlankPanels(3, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_DEFENSE_TOWERS_1) {
         this.lastPage = null;
         this.nextPage = GUIEnums.GUIPanelPage.RIGHT_DEFENSE_TOWERS_2;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_DEFENSE;
         this.panelAttractTower
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.ATTRACT_TOWER), false, debug);
         yShift += this.panelAttractTower.getHeight();
         this.panelBanishTower
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.BANISH_TOWER), false, debug);
         yShift += this.panelBanishTower.getHeight();
         this.panelBallistaTower
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.BALLISTA_TOWER), false, debug);
         yShift += this.panelBallistaTower.getHeight();
         this.panelBowTower.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.BOW_TOWER), false, debug);
         yShift += this.panelBowTower.getHeight();
         this.panelBulletTower
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.BULLET_TOWER), false, debug);
         yShift += this.panelBulletTower.getHeight();
         this.panelElementalBoltTower
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER), false, debug);
         yShift += this.panelElementalBoltTower.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_DEFENSE_TOWERS_2) {
         this.lastPage = GUIEnums.GUIPanelPage.RIGHT_DEFENSE_TOWERS_1;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_DEFENSE;
         this.panelPhantomDartTower
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.PHANTOM_DART_TOWER), false, debug);
         yShift += this.panelPhantomDartTower.getHeight();
         this.panelSlingTower
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.SLING_TOWER), false, debug);
         yShift += this.panelSlingTower.getHeight();
         this.panelSprayTower
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.SPRAY_TOWER), false, debug);
         yShift += this.panelSprayTower.getHeight();
         this.panelStaticTower
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.STATIC_TOWER), false, debug);
         yShift += this.panelStaticTower.getHeight();
         yShift = this.renderBlankPanels(2, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_DEFENSE_MISCELLANEOUS) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_DEFENSE;
         this.panelLightningRod
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.LIGHTNING_ROD), false, debug);
         yShift += this.panelLightningRod.getHeight();
         this.panelRecombobulatorTower
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER), false, debug);
         yShift += this.panelRecombobulatorTower.getHeight();
         yShift = this.renderBlankPanels(4, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_FOOD_AND_WATER_1) {
         this.lastPage = null;
         this.nextPage = GUIEnums.GUIPanelPage.RIGHT_FOOD_AND_WATER_2;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1;
         this.panelAnimalPen.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.ANIMAL_PEN), false, debug);
         yShift += this.panelAnimalPen.getHeight();
         this.panelBottler.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.BOTTLER), false, debug);
         yShift += this.panelBottler.getHeight();
         this.panelCluckerCoop
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CLUCKER_COOP), false, debug);
         yShift += this.panelCluckerCoop.getHeight();
         this.panelFarm.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.FARM), false, debug);
         yShift += this.panelFarm.getHeight();
         this.panelSmallFountain
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.SMALL_FOUNTAIN), false, debug);
         yShift += this.panelSmallFountain.getHeight();
         this.panelLargeFountain
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.LARGE_FOUNTAIN), false, debug);
         yShift += this.panelLargeFountain.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_FOOD_AND_WATER_2) {
         this.lastPage = GUIEnums.GUIPanelPage.RIGHT_FOOD_AND_WATER_1;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1;
         this.panelKitchen.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.KITCHEN), false, debug);
         yShift += this.panelKitchen.getHeight();
         this.panelOutpost.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.OUTPOST), false, debug);
         yShift += this.panelOutpost.getHeight();
         this.panelRainCatcher
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.RAIN_CATCHER), false, debug);
         yShift += this.panelRainCatcher.getHeight();
         this.panelRangerLodge
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.RANGER_LODGE), false, debug);
         yShift += this.panelRangerLodge.getHeight();
         this.panelWaterPurifier
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.WATER_PURIFIER), false, debug);
         yShift += this.panelWaterPurifier.getHeight();
         this.panelWell.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.WELL), false, debug);
         yShift += this.panelWell.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_GATES_AND_WALLS) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_2;
         this.panelCategoryGatesAndWallsGates.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryGatesAndWallsGates.getHeight();
         this.panelCategoryGatesAndWallsWalls.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryGatesAndWallsWalls.getHeight();
         yShift = this.renderBlankPanels(4, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_GATES_AND_WALLS_GATES) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_GATES_AND_WALLS;
         this.panelCrylithiumWallGateNS
            .render(
               this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_NS, true), false, debug
            );
         yShift += this.panelCrylithiumWallGateNS.getHeight();
         this.panelCrylithiumWallGateWE
            .render(
               this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_WE, true), false, debug
            );
         yShift += this.panelCrylithiumWallGateWE.getHeight();
         this.panelStoneWallGateNS
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.STONE_WALL_GATE_NS, true), false, debug);
         yShift += this.panelStoneWallGateNS.getHeight();
         this.panelStoneWallGateWE
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.STONE_WALL_GATE_WE, true), false, debug);
         yShift += this.panelStoneWallGateWE.getHeight();
         this.panelWoodFenceGateNS
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.WOOD_FENCE_GATE_NS, true), false, debug);
         yShift += this.panelWoodFenceGateNS.getHeight();
         this.panelWoodFenceGateWE
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.WOOD_FENCE_GATE_WE, true), false, debug);
         yShift += this.panelWoodFenceGateWE.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_GATES_AND_WALLS_WALLS) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_GATES_AND_WALLS;
         this.panelCrylithiumWall
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CRYLITHIUM_WALL, true), false, debug);
         yShift += this.panelCrylithiumWall.getHeight();
         this.panelCrylithiumCurtainWall
            .render(
               this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CRYLITHIUM_CURTAIN_WALL, true), false, debug
            );
         yShift += this.panelCrylithiumCurtainWall.getHeight();
         this.panelCurtainWall
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CURTAIN_WALL, true), false, debug);
         yShift += this.panelCurtainWall.getHeight();
         this.panelStoneWall
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.STONE_WALL, true), false, debug);
         yShift += this.panelStoneWall.getHeight();
         this.panelTrashyCubeWall
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.TRASHY_CUBE_WALL, true), false, debug);
         yShift += this.panelTrashyCubeWall.getHeight();
         this.panelWoodFence
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.WOOD_FENCE, true), false, debug);
         yShift += this.panelWoodFence.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_HARVESTING) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1;
         this.panelCrystalHarvestry
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CRYSTAL_HARVESTRY), false, debug);
         yShift += this.panelCrystalHarvestry.getHeight();
         this.panelLumberShack
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.LUMBER_SHACK), false, debug);
         yShift += this.panelLumberShack.getHeight();
         this.panelMiningFacility
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.MINING_FACILITY), false, debug);
         yShift += this.panelMiningFacility.getHeight();
         yShift = this.renderBlankPanels(3, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_HOUSING) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1;
         this.panelDoggoHouse
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.DOGGO_HOUSE), false, debug);
         yShift += this.panelDoggoHouse.getHeight();
         this.panelHousing.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.HOUSING), false, debug);
         yShift += this.panelHousing.getHeight();
         yShift = this.renderBlankPanels(4, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_LIGHTING) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1;
         this.panelCrylithiumFirePit
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CRYLITHIUM_FIRE_PIT), false, debug);
         yShift += this.panelCrylithiumFirePit.getHeight();
         this.panelLargeCrylithiumFirePit
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.LARGE_CRYLITHIUM_FIRE_PIT), false, debug);
         yShift += this.panelLargeCrylithiumFirePit.getHeight();
         this.panelFirePit.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.FIRE_PIT), false, debug);
         yShift += this.panelFirePit.getHeight();
         this.panelLargeFirePit
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.LARGE_FIRE_PIT), false, debug);
         yShift += this.panelLargeFirePit.getHeight();
         yShift = this.renderBlankPanels(2, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_MAGIC) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_2;
         this.panelCrystalMotivator
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CRYSTAL_MOTIVATOR), false, debug);
         yShift += this.panelCrystalMotivator.getHeight();
         this.panelCullisGate
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CULLIS_GATE), false, debug);
         yShift += this.panelCullisGate.getHeight();
         this.panelEssenceAltar
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.ESSENCE_ALTAR), false, debug);
         yShift += this.panelEssenceAltar.getHeight();
         this.panelEssenceCollector
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.ESSENCE_COLLECTOR), false, debug);
         yShift += this.panelEssenceCollector.getHeight();
         this.panelReliquary.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.RELIQUARY), false, debug);
         yShift += this.panelReliquary.getHeight();
         yShift = this.renderBlankPanels(1, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_MANUFACTURING) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_2;
         this.panelArmorsmithy
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.ARMORSMITHY), false, debug);
         yShift += this.panelArmorsmithy.getHeight();
         this.panelBowyer.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.BOWYER), false, debug);
         yShift += this.panelBowyer.getHeight();
         this.panelRockTumbler
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.ROCK_TUMBLER), false, debug);
         yShift += this.panelRockTumbler.getHeight();
         this.panelToolsmithy
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.TOOLSMITHY), false, debug);
         yShift += this.panelToolsmithy.getHeight();
         yShift = this.renderBlankPanels(2, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_REFINING) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_2;
         this.panelCrystillery
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CRYSTILLERY), false, debug);
         yShift += this.panelCrystillery.getHeight();
         this.panelForge.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.FORGE), false, debug);
         yShift += this.panelForge.getHeight();
         this.panelLumberMill
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.LUMBER_MILL), false, debug);
         yShift += this.panelLumberMill.getHeight();
         this.panelStoneCuttery
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.STONE_CUTTERY), false, debug);
         yShift += this.panelStoneCuttery.getHeight();
         yShift = this.renderBlankPanels(2, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_STORAGE_1) {
         this.lastPage = null;
         this.nextPage = GUIEnums.GUIPanelPage.RIGHT_STORAGE_2;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_2;
         this.panelAmmoStorage
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.AMMO_STORAGE), false, debug);
         yShift += this.panelAmmoStorage.getHeight();
         this.panelCrystalStorage
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CRYSTAL_STORAGE), false, debug);
         yShift += this.panelCrystalStorage.getHeight();
         this.panelEquipmentStorage
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.EQUIPMENT_STORAGE), false, debug);
         yShift += this.panelEquipmentStorage.getHeight();
         this.panelFoodStorage
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.FOOD_STORAGE), false, debug);
         yShift += this.panelFoodStorage.getHeight();
         this.panelGoldStorage
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.GOLD_STORAGE), false, debug);
         yShift += this.panelGoldStorage.getHeight();
         this.panelKeyShack.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.KEY_SHACK), false, debug);
         yShift += this.panelKeyShack.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_STORAGE_2) {
         this.lastPage = GUIEnums.GUIPanelPage.RIGHT_STORAGE_1;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_2;
         this.panelMineralStorage
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.MINERAL_STORAGE), false, debug);
         yShift += this.panelMineralStorage.getHeight();
         this.panelMiscellaneousStorage
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE), false, debug);
         yShift += this.panelMiscellaneousStorage.getHeight();
         this.panelRockStorage
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.ROCK_STORAGE), false, debug);
         yShift += this.panelRockStorage.getHeight();
         this.panelWoodStorage
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.WOOD_STORAGE), false, debug);
         yShift += this.panelWoodStorage.getHeight();
         yShift = this.renderBlankPanels(2, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_TRASH) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_2;
         this.panelBurner.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.BURNER), false, debug);
         yShift += this.panelBurner.getHeight();
         this.panelCubeEGolemCombobulator
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR), false, debug);
         yShift += this.panelCubeEGolemCombobulator.getHeight();
         this.panelLandfill.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.LANDFILL), false, debug);
         yShift += this.panelLandfill.getHeight();
         this.panelProcessor.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.PROCESSOR), false, debug);
         yShift += this.panelProcessor.getHeight();
         this.panelTrashCan.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.TRASH_CAN), false, debug);
         yShift += this.panelTrashCan.getHeight();
         this.panelTrashyCubePile
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.TRASHY_CUBE_PILE), false, debug);
         yShift += this.panelTrashyCubePile.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_SPECIAL) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_2;
         this.panelCamp.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.CASTLE_1), false, debug);
         yShift += this.panelCamp.getHeight();
         this.panelLootBox.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.object.objectCount(MapTilesLoader.TileSet.LOOT_BOX), false, debug);
         yShift += this.panelCamp.getHeight();
         yShift = this.renderBlankPanels(4, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_1) {
         this.lastPage = null;
         this.nextPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_2;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_SANDBOX_TOOLS;
         this.panelCategoryBricks.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryBricks.getHeight();
         this.panelCategoryCrystals.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryCrystals.getHeight();
         this.panelCategoryDirt.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryDirt.getHeight();
         this.panelCategoryFlowers.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryFlowers.getHeight();
         this.panelCategoryFood.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryFood.getHeight();
         this.panelCategoryGrass.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryGrass.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_2) {
         this.lastPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_1;
         this.nextPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_3;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_SANDBOX_TOOLS;
         this.panelCategoryGravel.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryGravel.getHeight();
         this.panelCategoryLava.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryLava.getHeight();
         this.panelCategoryRoads.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryRoads.getHeight();
         this.panelCategoryRock.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryRock.getHeight();
         this.panelCategorySand.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategorySand.getHeight();
         this.panelCategorySandstone.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategorySandstone.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_3) {
         this.lastPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_2;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_SANDBOX_TOOLS;
         this.panelCategorySnow.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategorySnow.getHeight();
         this.panelCategoryTar.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryTar.getHeight();
         this.panelCategoryTrees.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryTrees.getHeight();
         this.panelCategoryWater.render(this.g, this.mouse, this.x, this.y + 28 + yShift, false, debug);
         yShift += this.panelCategoryWater.getHeight();
         yShift = this.renderBlankPanels(2, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_BRICKS) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_1;
         this.panelGrayBricks.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.BRICKS), false, debug);
         yShift += this.panelGrayBricks.getHeight();
         this.panelGrayTiles.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TILES), false, debug);
         yShift += this.panelGrayTiles.getHeight();
         yShift = this.renderBlankPanels(4, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_CRYSTALS) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_1;
         this.panelRedCrystals
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.CRYSTAL_RED), false, debug);
         yShift += this.panelRedCrystals.getHeight();
         this.panelGreenCrystals
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.CRYSTAL_GREEN), false, debug);
         yShift += this.panelGreenCrystals.getHeight();
         this.panelBlueCrystals
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.CRYSTAL_BLUE), false, debug);
         yShift += this.panelBlueCrystals.getHeight();
         this.panelPurpleCrystals
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.CRYSTAL_PURPLE), false, debug);
         yShift += this.panelPurpleCrystals.getHeight();
         yShift = this.renderBlankPanels(2, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_DIRT) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_1;
         this.panelBrownDirt.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.DIRT_BROWN), false, debug);
         yShift += this.panelBrownDirt.getHeight();
         this.panelDarkBrownDirt
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.DIRT_DARK_BROWN), false, debug);
         yShift += this.panelDarkBrownDirt.getHeight();
         this.panelLightBrownDirt
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.DIRT_LIGHT_BROWN), false, debug);
         yShift += this.panelLightBrownDirt.getHeight();
         yShift = this.renderBlankPanels(3, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_FLOWERS) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_1;
         this.panelBlackFlowers
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.FLOWERS_BLACK), false, debug);
         yShift += this.panelBlackFlowers.getHeight();
         this.panelBlueFlowers
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.FLOWERS_BLUE), false, debug);
         yShift += this.panelBlueFlowers.getHeight();
         this.panelPurpleFlowers
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.FLOWERS_PURPLE), false, debug);
         yShift += this.panelPurpleFlowers.getHeight();
         this.panelRedFlowers.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.FLOWERS_RED), false, debug);
         yShift += this.panelRedFlowers.getHeight();
         this.panelWhiteFlowers
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.FLOWERS_WHITE), false, debug);
         yShift += this.panelWhiteFlowers.getHeight();
         this.panelYellowFlowers
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.FLOWERS_YELLOW), false, debug);
         yShift += this.panelYellowFlowers.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_FOOD_1) {
         this.lastPage = null;
         this.nextPage = GUIEnums.GUIPanelPage.RIGHT_FOOD_2;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_1;
         this.panelCactus.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.CACTUS_GREEN), false, debug);
         yShift += this.panelCactus.getHeight();
         this.panelCarrots.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.CARROTS), false, debug);
         yShift += this.panelCarrots.getHeight();
         this.panelHolyPotatoes
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.HOLY_POTATOES), false, debug);
         yShift += this.panelHolyPotatoes.getHeight();
         this.panelMelons.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.MELONS), false, debug);
         yShift += this.panelMelons.getHeight();
         this.panelMushrooms.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.MUSHROOMS), false, debug);
         yShift += this.panelMushrooms.getHeight();
         this.panelPotatoes.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.POTATOES), false, debug);
         yShift += this.panelPotatoes.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_FOOD_2) {
         this.lastPage = GUIEnums.GUIPanelPage.RIGHT_FOOD_1;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_1;
         this.panelTurnips.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TURNIPS), false, debug);
         yShift += this.panelTurnips.getHeight();
         yShift = this.renderBlankPanels(5, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_GRASS) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_1;
         this.panelEmeraldGreenGrass
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.GRASS_EMERALD_GREEN), false, debug);
         yShift += this.panelEmeraldGreenGrass.getHeight();
         this.panelGreenGrass.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.GRASS_GREEN), false, debug);
         yShift += this.panelGreenGrass.getHeight();
         this.panelTealGrass.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.GRASS_TEAL), false, debug);
         yShift += this.panelTealGrass.getHeight();
         this.panelYellowBrownGrass
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.GRASS_YELLOW_BROWN), false, debug);
         yShift += this.panelYellowBrownGrass.getHeight();
         yShift = this.renderBlankPanels(2, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_GRAVEL) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_2;
         this.panelBlueGravel.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.GRAVEL_BLUE), false, debug);
         yShift += this.panelBlueGravel.getHeight();
         this.panelGrayGravel.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.GRAVEL_GRAY), false, debug);
         yShift += this.panelGrayGravel.getHeight();
         this.panelRedGravel.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.GRAVEL_RED), false, debug);
         yShift += this.panelRedGravel.getHeight();
         yShift = this.renderBlankPanels(3, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_LAVA) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_2;
         this.panelLava.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.LAVA), false, debug);
         yShift += this.panelLava.getHeight();
         yShift = this.renderBlankPanels(5, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_ROADS_1) {
         this.lastPage = null;
         this.nextPage = GUIEnums.GUIPanelPage.RIGHT_ROADS_2;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_2;
         this.panelPath.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.PATH), false, debug);
         yShift += this.panelPath.getHeight();
         this.panelLogPathDebris
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.LOG_PATH_DEBRIS), false, debug);
         yShift += this.panelLogPathDebris.getHeight();
         this.panelLogPath.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.LOG_PATH), false, debug);
         yShift += this.panelLogPath.getHeight();
         this.panelCobbleAndLogPathDebris
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.COBBLE_AND_LOG_PATH_DEBRIS), false, debug);
         yShift += this.panelCobbleAndLogPathDebris.getHeight();
         this.panelCobbleAndLogPath
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.COBBLE_AND_LOG_PATH), false, debug);
         yShift += this.panelCobbleAndLogPath.getHeight();
         this.panelCobbleAndBoardRoadDebris
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.COBBLE_AND_BOARD_ROAD_DEBRIS), false, debug);
         yShift += this.panelCobbleAndBoardRoadDebris.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_ROADS_2) {
         this.lastPage = GUIEnums.GUIPanelPage.RIGHT_ROADS_1;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_2;
         this.panelCobbleAndBoardRoad
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.COBBLE_AND_BOARD_ROAD), false, debug);
         yShift += this.panelCobbleAndBoardRoad.getHeight();
         this.panelCutStoneAndBoardRoadDebris
            .render(
               this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.CUT_STONE_AND_BOARD_ROAD_DEBRIS), false, debug
            );
         yShift += this.panelCutStoneAndBoardRoadDebris.getHeight();
         this.panelCutStoneAndBoardRoad
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.CUT_STONE_AND_BOARD_ROAD), false, debug);
         yShift += this.panelCutStoneAndBoardRoad.getHeight();
         yShift = this.renderBlankPanels(3, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_ROCK) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_2;
         this.panelBlackRock.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.ROCK_BLACK), false, debug);
         yShift += this.panelBlackRock.getHeight();
         this.panelBrownRock.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.ROCK_BROWN), false, debug);
         yShift += this.panelBrownRock.getHeight();
         this.panelGrayRock.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.ROCK_GRAY), false, debug);
         yShift += this.panelGrayRock.getHeight();
         this.panelRedRock.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.ROCK_RED), false, debug);
         yShift += this.panelRedRock.getHeight();
         this.panelWhiteRock.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.ROCK_WHITE), false, debug);
         yShift += this.panelWhiteRock.getHeight();
         yShift = this.renderBlankPanels(1, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_SAND) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_2;
         this.panelBlackSand.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.SAND_BLACK), false, debug);
         yShift += this.panelBlackSand.getHeight();
         this.panelRedSand.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.SAND_RED), false, debug);
         yShift += this.panelRedSand.getHeight();
         this.panelTanSand.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.SAND_TAN), false, debug);
         yShift += this.panelTanSand.getHeight();
         yShift = this.renderBlankPanels(3, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_SANDSTONE) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_2;
         this.panelBlackSandstone
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.SANDSTONE_BLACK), false, debug);
         yShift += this.panelBlackSandstone.getHeight();
         this.panelRedSandstone
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.SANDSTONE_RED), false, debug);
         yShift += this.panelRedSandstone.getHeight();
         this.panelTanSandstone
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.SANDSTONE_TAN), false, debug);
         yShift += this.panelTanSandstone.getHeight();
         yShift = this.renderBlankPanels(3, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_SNOW) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_2;
         this.panelWhiteSnow.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.SNOW_WHITE), false, debug);
         yShift += this.panelWhiteSnow.getHeight();
         yShift = this.renderBlankPanels(5, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_TAR) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_3;
         this.panelTar.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TAR), false, debug);
         yShift += this.panelTar.getHeight();
         yShift = this.renderBlankPanels(5, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_TREES_1) {
         this.lastPage = null;
         this.nextPage = GUIEnums.GUIPanelPage.RIGHT_TREES_2;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_3;
         this.panelBlueDeadTrees
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_BLUE_DEAD), false, debug);
         yShift += this.panelBlueDeadTrees.getHeight();
         this.panelBlueStumps
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_BLUE_COLLECTED), false, debug);
         yShift += this.panelBlueStumps.getHeight();
         this.panelBlueTrees.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_BLUE), false, debug);
         yShift += this.panelBlueTrees.getHeight();
         this.panelBrownDeadTrees
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_BROWN_DEAD), false, debug);
         yShift += this.panelBrownDeadTrees.getHeight();
         this.panelBrownStumps
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_BROWN_COLLECTED), false, debug);
         yShift += this.panelBrownStumps.getHeight();
         this.panelBrownTrees.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_BROWN), false, debug);
         yShift += this.panelBrownTrees.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_TREES_2) {
         this.lastPage = GUIEnums.GUIPanelPage.RIGHT_TREES_1;
         this.nextPage = GUIEnums.GUIPanelPage.RIGHT_TREES_3;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_3;
         this.panelDarkGreenDeadTrees
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_DARK_GREEN_DEAD), false, debug);
         yShift += this.panelDarkGreenDeadTrees.getHeight();
         this.panelDarkGreenStumps
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_DARK_GREEN_COLLECTED), false, debug);
         yShift += this.panelDarkGreenStumps.getHeight();
         this.panelDarkGreenTrees
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_DARK_GREEN), false, debug);
         yShift += this.panelDarkGreenTrees.getHeight();
         this.panelGreenDeadTrees
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_GREEN_DEAD), false, debug);
         yShift += this.panelGreenDeadTrees.getHeight();
         this.panelGreenStumps
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_GREEN_COLLECTED), false, debug);
         yShift += this.panelGreenStumps.getHeight();
         this.panelGreenTrees.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_GREEN), false, debug);
         yShift += this.panelGreenTrees.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_TREES_3) {
         this.lastPage = GUIEnums.GUIPanelPage.RIGHT_TREES_2;
         this.nextPage = GUIEnums.GUIPanelPage.RIGHT_TREES_4;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_3;
         this.panelLavenderDeadTrees
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_LAVENDER_DEAD), false, debug);
         yShift += this.panelLavenderDeadTrees.getHeight();
         this.panelLavenderStumps
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_LAVENDER_COLLECTED), false, debug);
         yShift += this.panelLavenderStumps.getHeight();
         this.panelLavenderTrees
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_LAVENDER), false, debug);
         yShift += this.panelLavenderTrees.getHeight();
         this.panelPaleBlueDeadTrees
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_PALE_BLUE_DEAD), false, debug);
         yShift += this.panelPaleBlueDeadTrees.getHeight();
         this.panelPaleBlueStumps
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_PALE_BLUE_COLLECTED), false, debug);
         yShift += this.panelPaleBlueStumps.getHeight();
         this.panelPaleBlueTrees
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_PALE_BLUE), false, debug);
         yShift += this.panelPaleBlueTrees.getHeight();
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_TREES_4) {
         this.lastPage = GUIEnums.GUIPanelPage.RIGHT_TREES_3;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_3;
         this.panelRedDeadTrees
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_RED_DEAD), false, debug);
         yShift += this.panelRedDeadTrees.getHeight();
         this.panelRedStumps
            .render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_RED_COLLECTED), false, debug);
         yShift += this.panelRedStumps.getHeight();
         this.panelRedTrees.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.TREES_RED), false, debug);
         yShift += this.panelRedTrees.getHeight();
         yShift = this.renderBlankPanels(3, this.x, this.y, yShift, this.g);
      }

      if (this.panelCurrentPage == GUIEnums.GUIPanelPage.RIGHT_WATER) {
         this.lastPage = null;
         this.nextPage = null;
         this.backPage = GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_3;
         this.panelWater.render(this.g, this.mouse, this.x, this.y + 28 + yShift, this.map.getTileCount(MapTilesLoader.TileSet.WATER), false, debug);
         yShift += this.panelWater.getHeight();
         yShift = this.renderBlankPanels(5, this.x, this.y, yShift, this.g);
      }

      if (this.backPage == null) {
         this.pageBackBottom.render(this.g, this.mouse, this.x, this.y + yShift + 28, true, debug);
      } else {
         this.pageBackBottom.render(this.g, this.mouse, this.x, this.y + yShift + 28, false, debug);
      }

      if (this.lastPage == null) {
         this.pageUpBottom.render(this.g, this.mouse, this.x + 67, this.y + yShift + 28, true, debug);
      } else {
         this.pageUpBottom.render(this.g, this.mouse, this.x + 67, this.y + yShift + 28, false, debug);
      }

      if (this.nextPage == null) {
         this.pageDownBottom.render(this.g, this.mouse, this.x + 96, this.y + yShift + 28, true, debug);
      } else {
         this.pageDownBottom.render(this.g, this.mouse, this.x + 96, this.y + yShift + 28, false, debug);
      }
   }

   private int renderBlankPanels(int i, int x, int y, int yShift, Graphics g) {
      if (i >= 6) {
         g.setColor(new Color(26, 26, 26, 255));
         g.fillRect(x + 10, y + 28 + yShift + 19, this.blankLargePanel1.getWidth() - 20, this.blankLargePanel1.getHeight() - 29);
         this.blankLargePanel1.draw(x, y + 28 + yShift);
         this.blankLargePanel1.drawFlash(x, y + 28 + yShift, this.blankLargePanel1.getWidth(), this.blankLargePanel1.getHeight(), new Color(0, 0, 0, 200));
         yShift += this.blankLargePanel1.getHeight();
      }

      if (i >= 5) {
         g.setColor(new Color(26, 26, 26, 255));
         g.fillRect(x + 10, y + 28 + yShift + 19, this.blankLargePanel2.getWidth() - 20, this.blankLargePanel2.getHeight() - 29);
         this.blankLargePanel2.draw(x, y + 28 + yShift);
         this.blankLargePanel2.drawFlash(x, y + 28 + yShift, this.blankLargePanel2.getWidth(), this.blankLargePanel2.getHeight(), new Color(0, 0, 0, 200));
         yShift += this.blankLargePanel2.getHeight();
      }

      if (i >= 4) {
         g.setColor(new Color(26, 26, 26, 255));
         g.fillRect(x + 10, y + 28 + yShift + 19, this.blankLargePanel3.getWidth() - 20, this.blankLargePanel3.getHeight() - 29);
         this.blankLargePanel3.draw(x, y + 28 + yShift);
         this.blankLargePanel3.drawFlash(x, y + 28 + yShift, this.blankLargePanel3.getWidth(), this.blankLargePanel3.getHeight(), new Color(0, 0, 0, 200));
         yShift += this.blankLargePanel3.getHeight();
      }

      if (i >= 3) {
         g.setColor(new Color(26, 26, 26, 255));
         g.fillRect(x + 10, y + 28 + yShift + 19, this.blankLargePanel4.getWidth() - 20, this.blankLargePanel4.getHeight() - 29);
         this.blankLargePanel4.draw(x, y + 28 + yShift);
         this.blankLargePanel4.drawFlash(x, y + 28 + yShift, this.blankLargePanel4.getWidth(), this.blankLargePanel4.getHeight(), new Color(0, 0, 0, 200));
         yShift += this.blankLargePanel4.getHeight();
      }

      if (i >= 2) {
         g.setColor(new Color(26, 26, 26, 255));
         g.fillRect(x + 10, y + 28 + yShift + 19, this.blankLargePanel5.getWidth() - 20, this.blankLargePanel5.getHeight() - 29);
         this.blankLargePanel5.draw(x, y + 28 + yShift);
         this.blankLargePanel5.drawFlash(x, y + 28 + yShift, this.blankLargePanel5.getWidth(), this.blankLargePanel5.getHeight(), new Color(0, 0, 0, 200));
         yShift += this.blankLargePanel5.getHeight();
      }

      if (i >= 1) {
         g.setColor(new Color(26, 26, 26, 255));
         g.fillRect(x + 10, y + 28 + yShift + 19, this.blankLargePanel6.getWidth() - 20, this.blankLargePanel6.getHeight() - 29);
         this.blankLargePanel6.draw(x, y + 28 + yShift);
         this.blankLargePanel6.drawFlash(x, y + 28 + yShift, this.blankLargePanel6.getWidth(), this.blankLargePanel6.getHeight(), new Color(0, 0, 0, 200));
         yShift += this.blankLargePanel6.getHeight();
      }

      return yShift;
   }

   public void setPanelPage(GUIEnums.GUIPanelPage p) {
      this.panelCurrentPage = p;
   }

   public void nextPage() {
      if (this.nextPage != null) {
         this.panelCurrentPage = this.nextPage;
      }
   }

   public void lastPage() {
      if (this.lastPage != null) {
         this.panelCurrentPage = this.lastPage;
      }
   }

   public void backPage() {
      if (this.backPage != null) {
         this.panelCurrentPage = this.backPage;
      }
   }

   public void togglePanelHidden() {
      this.hidden = !this.hidden;
      this.hidePanel.cycleButton();
   }

   @Override
   public int getCenterX() {
      return (int)(this.x + this.mask.getWidth() / 2.0F);
   }

   @Override
   public int getCenterY() {
      return (int)(this.y + this.mask.getHeight() / 2.0F);
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
      return (int)(this.x + this.mask.getWidth());
   }

   @Override
   public int getBottomY() {
      return (int)(this.y + this.mask.getHeight());
   }
}
