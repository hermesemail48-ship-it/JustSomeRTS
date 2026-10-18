package rtr.mobile.ui;

import java.util.ArrayList;
import rtr.gui.GUIEnums;
import rtr.map.MapTilesLoader;
import rtr.road.RoadModule;

/**
 * Everything the desktop build panel (rtr.gui.states.playstate.RightPanel) offers outside sandbox mode, grouped for the
 * phone build drawer. Generated from RightPanel's buttons and PlayState's right-panel click handlers.
 */
public final class BuildCatalog {
   public enum Group {
      HOUSING("Housing"),
      FOOD("Food & Water"),
      HARVESTING("Harvesting"),
      CIVICS("Civics"),
      STORAGE("Storage"),
      REFINING("Refining"),
      MANUFACTURING("Manufacturing"),
      DEFENSE("Defense"),
      WALLS("Walls & Gates"),
      LIGHTING("Lighting"),
      MAGIC("Magic"),
      TRASH("Trash"),
      ROADS("Roads & Digging");

      public final String label;

      Group(String label) {
         this.label = label;
      }
   }

   public static final class Entry {
      public final Group group;
      /** Card image: res/GUI/gameState/images/&lt;image&gt;.png. */
      public final String image;
      public final String labelKey;
      /** Exactly one of these is set. */
      public final MapTilesLoader.TileSet building;
      public final RoadModule.RoadType road;
      public final GUIEnums.InterfaceMode tool;

      Entry(Group group, String image, String labelKey, MapTilesLoader.TileSet building, RoadModule.RoadType road, GUIEnums.InterfaceMode tool) {
         this.group = group;
         this.image = image;
         this.labelKey = labelKey;
         this.building = building;
         this.road = road;
         this.tool = tool;
      }

      public String imagePath() {
         return "res/GUI/gameState/images/" + this.image + ".png";
      }
   }

   public static final Entry[] ALL = new Entry[]{
      new Entry(Group.CIVICS, "ancillary", "mapEditorPlayRightPanel.button.civics.ancillary", MapTilesLoader.TileSet.ANCILLARY, null, null),
      new Entry(Group.CIVICS, "clinic", "mapEditorPlayRightPanel.button.civics.clinic", MapTilesLoader.TileSet.CLINIC, null, null),
      new Entry(Group.CIVICS, "courierStation", "mapEditorPlayRightPanel.button.civics.courierStation", MapTilesLoader.TileSet.COURIER_STATION, null, null),
      new Entry(Group.CIVICS, "maintenanceBuilding", "mapEditorPlayRightPanel.button.civics.maintenanceBuilding", MapTilesLoader.TileSet.MAINTENANCE_BUILDING, null, null),
      new Entry(Group.CIVICS, "marketplace", "mapEditorPlayRightPanel.button.civics.marketplace", MapTilesLoader.TileSet.MARKETPLACE, null, null),
      new Entry(Group.CIVICS, "wayMakerShack", "mapEditorPlayRightPanel.button.civics.wayMakerShack", MapTilesLoader.TileSet.WAY_MAKER_SHACK, null, null),
      new Entry(Group.CIVICS, "migrationWayStation", "mapEditorPlayRightPanel.button.civics.migrationWayStation", MapTilesLoader.TileSet.MIGRATION_WAY_STATION, null, null),
      new Entry(Group.DEFENSE, "crystalGolemCombobulator", "mapEditorPlayRightPanel.button.golems.crystalGolemCombobulator", MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR, null, null),
      new Entry(Group.DEFENSE, "stoneGolemCombobulator", "mapEditorPlayRightPanel.button.golems.stoneGolemCombobulator", MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR, null, null),
      new Entry(Group.DEFENSE, "woodGolemCombobulator", "mapEditorPlayRightPanel.button.golems.woodGolemCombobulator", MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR, null, null),
      new Entry(Group.DEFENSE, "attractTower", "mapEditorPlayRightPanel.button.towers.attractTower", MapTilesLoader.TileSet.ATTRACT_TOWER, null, null),
      new Entry(Group.DEFENSE, "banishTower", "mapEditorPlayRightPanel.button.towers.banishTower", MapTilesLoader.TileSet.BANISH_TOWER, null, null),
      new Entry(Group.DEFENSE, "ballistaTower", "mapEditorPlayRightPanel.button.towers.ballistaTower", MapTilesLoader.TileSet.BALLISTA_TOWER, null, null),
      new Entry(Group.DEFENSE, "bowTower", "mapEditorPlayRightPanel.button.towers.bowTower", MapTilesLoader.TileSet.BOW_TOWER, null, null),
      new Entry(Group.DEFENSE, "bulletTower", "mapEditorPlayRightPanel.button.towers.bulletTower", MapTilesLoader.TileSet.BULLET_TOWER, null, null),
      new Entry(Group.DEFENSE, "elementalBoltTower", "mapEditorPlayRightPanel.button.towers.elementalBoltTower", MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER, null, null),
      new Entry(Group.DEFENSE, "phantomDartTower", "mapEditorPlayRightPanel.button.towers.phantomDartTower", MapTilesLoader.TileSet.PHANTOM_DART_TOWER, null, null),
      new Entry(Group.DEFENSE, "slingTower", "mapEditorPlayRightPanel.button.towers.slingTower", MapTilesLoader.TileSet.SLING_TOWER, null, null),
      new Entry(Group.DEFENSE, "sprayTower", "mapEditorPlayRightPanel.button.towers.sprayTower", MapTilesLoader.TileSet.SPRAY_TOWER, null, null),
      new Entry(Group.DEFENSE, "staticTower", "mapEditorPlayRightPanel.button.towers.staticTower", MapTilesLoader.TileSet.STATIC_TOWER, null, null),
      new Entry(Group.DEFENSE, "lightningRod", "mapEditorPlayRightPanel.button.defenseMiscellaneous.lightningRod", MapTilesLoader.TileSet.LIGHTNING_ROD, null, null),
      new Entry(Group.DEFENSE, "recombobulatorTower", "mapEditorPlayRightPanel.button.defenseMiscellaneous.recombobulatorTower", MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER, null, null),
      new Entry(Group.FOOD, "animalPen", "mapEditorPlayRightPanel.button.foodAndWater.animalPen", MapTilesLoader.TileSet.ANIMAL_PEN, null, null),
      new Entry(Group.FOOD, "bottler", "mapEditorPlayRightPanel.button.foodAndWater.bottler", MapTilesLoader.TileSet.BOTTLER, null, null),
      new Entry(Group.FOOD, "cluckerCoop", "mapEditorPlayRightPanel.button.foodAndWater.cluckerCoop", MapTilesLoader.TileSet.CLUCKER_COOP, null, null),
      new Entry(Group.FOOD, "farm", "mapEditorPlayRightPanel.button.foodAndWater.farm", MapTilesLoader.TileSet.FARM, null, null),
      new Entry(Group.FOOD, "smallFountain", "mapEditorPlayRightPanel.button.foodAndWater.smallFountain", MapTilesLoader.TileSet.SMALL_FOUNTAIN, null, null),
      new Entry(Group.FOOD, "largeFountain", "mapEditorPlayRightPanel.button.foodAndWater.largeFountain", MapTilesLoader.TileSet.LARGE_FOUNTAIN, null, null),
      new Entry(Group.FOOD, "kitchen", "mapEditorPlayRightPanel.button.foodAndWater.kitchen", MapTilesLoader.TileSet.KITCHEN, null, null),
      new Entry(Group.FOOD, "outpost", "mapEditorPlayRightPanel.button.foodAndWater.outpost", MapTilesLoader.TileSet.OUTPOST, null, null),
      new Entry(Group.FOOD, "rainCatcher", "mapEditorPlayRightPanel.button.foodAndWater.rainCatcher", MapTilesLoader.TileSet.RAIN_CATCHER, null, null),
      new Entry(Group.FOOD, "rangerLodge", "mapEditorPlayRightPanel.button.foodAndWater.rangerLodge", MapTilesLoader.TileSet.RANGER_LODGE, null, null),
      new Entry(Group.FOOD, "waterPurifier", "mapEditorPlayRightPanel.button.foodAndWater.waterPurifier", MapTilesLoader.TileSet.WATER_PURIFIER, null, null),
      new Entry(Group.FOOD, "well", "mapEditorPlayRightPanel.button.foodAndWater.well", MapTilesLoader.TileSet.WELL, null, null),
      new Entry(Group.WALLS, "crylithiumWallGateNS", "mapEditorPlayRightPanel.button.gates.crylithiumWallGateNS", MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_NS, null, null),
      new Entry(Group.WALLS, "crylithiumWallGateWE", "mapEditorPlayRightPanel.button.gates.crylithiumWallGateWE", MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_WE, null, null),
      new Entry(Group.WALLS, "stoneWallGateNS", "mapEditorPlayRightPanel.button.gates.stoneWallGateNS", MapTilesLoader.TileSet.STONE_WALL_GATE_NS, null, null),
      new Entry(Group.WALLS, "stoneWallGateWE", "mapEditorPlayRightPanel.button.gates.stoneWallGateWE", MapTilesLoader.TileSet.STONE_WALL_GATE_WE, null, null),
      new Entry(Group.WALLS, "woodFenceGateNS", "mapEditorPlayRightPanel.button.gates.woodFenceGateNS", MapTilesLoader.TileSet.WOOD_FENCE_GATE_NS, null, null),
      new Entry(Group.WALLS, "woodFenceGateWE", "mapEditorPlayRightPanel.button.gates.woodFenceGateWE", MapTilesLoader.TileSet.WOOD_FENCE_GATE_WE, null, null),
      new Entry(Group.WALLS, "crylithiumWall", "mapEditorPlayRightPanel.button.walls.crylithiumWall", MapTilesLoader.TileSet.CRYLITHIUM_WALL, null, null),
      new Entry(Group.WALLS, "crylithiumCurtainWall", "mapEditorPlayRightPanel.button.walls.crylithiumCurtainWall", MapTilesLoader.TileSet.CRYLITHIUM_CURTAIN_WALL, null, null),
      new Entry(Group.WALLS, "curtainWall", "mapEditorPlayRightPanel.button.walls.curtainWall", MapTilesLoader.TileSet.CURTAIN_WALL, null, null),
      new Entry(Group.WALLS, "stoneWall", "mapEditorPlayRightPanel.button.walls.stoneWall", MapTilesLoader.TileSet.STONE_WALL, null, null),
      new Entry(Group.WALLS, "trashyCubeWall", "mapEditorPlayRightPanel.button.walls.trashyCubeWall", MapTilesLoader.TileSet.TRASHY_CUBE_WALL, null, null),
      new Entry(Group.WALLS, "woodFence", "mapEditorPlayRightPanel.button.walls.woodFence", MapTilesLoader.TileSet.WOOD_FENCE, null, null),
      new Entry(Group.HARVESTING, "crystalHarvestry", "mapEditorPlayRightPanel.button.harvesting.crystalHarvestry", MapTilesLoader.TileSet.CRYSTAL_HARVESTRY, null, null),
      new Entry(Group.HARVESTING, "lumberShack", "mapEditorPlayRightPanel.button.harvesting.lumberShack", MapTilesLoader.TileSet.LUMBER_SHACK, null, null),
      new Entry(Group.HARVESTING, "miningFacility", "mapEditorPlayRightPanel.button.harvesting.miningFacility", MapTilesLoader.TileSet.MINING_FACILITY, null, null),
      new Entry(Group.HOUSING, "doggoHouse", "mapEditorPlayRightPanel.button.housing.doggoHouse", MapTilesLoader.TileSet.DOGGO_HOUSE, null, null),
      new Entry(Group.HOUSING, "housing", "mapEditorPlayRightPanel.button.housing.housing", MapTilesLoader.TileSet.HOUSING, null, null),
      new Entry(Group.LIGHTING, "crylithiumFirePit", "mapEditorPlayRightPanel.button.lighting.crylithiumFirePit", MapTilesLoader.TileSet.CRYLITHIUM_FIRE_PIT, null, null),
      new Entry(Group.LIGHTING, "largeCrylithiumFirePit", "mapEditorPlayRightPanel.button.lighting.largeCrylithiumFirePit", MapTilesLoader.TileSet.LARGE_CRYLITHIUM_FIRE_PIT, null, null),
      new Entry(Group.LIGHTING, "firePit", "mapEditorPlayRightPanel.button.lighting.firePit", MapTilesLoader.TileSet.FIRE_PIT, null, null),
      new Entry(Group.LIGHTING, "largeFirePit", "mapEditorPlayRightPanel.button.lighting.largeFirePit", MapTilesLoader.TileSet.LARGE_FIRE_PIT, null, null),
      new Entry(Group.MAGIC, "crystalMotivator", "mapEditorPlayRightPanel.button.magic.crystalMotivator", MapTilesLoader.TileSet.CRYSTAL_MOTIVATOR, null, null),
      new Entry(Group.MAGIC, "cullisGate", "mapEditorPlayRightPanel.button.magic.cullisGate", MapTilesLoader.TileSet.CULLIS_GATE, null, null),
      new Entry(Group.MAGIC, "essenceAltar", "mapEditorPlayRightPanel.button.magic.essenceAltar", MapTilesLoader.TileSet.ESSENCE_ALTAR, null, null),
      new Entry(Group.MAGIC, "essenceCollector", "mapEditorPlayRightPanel.button.magic.essenceCollector", MapTilesLoader.TileSet.ESSENCE_COLLECTOR, null, null),
      new Entry(Group.MAGIC, "reliquary", "mapEditorPlayRightPanel.button.magic.reliquary", MapTilesLoader.TileSet.RELIQUARY, null, null),
      new Entry(Group.MANUFACTURING, "armorsmithy", "mapEditorPlayRightPanel.button.manufacturing.armorsmithy", MapTilesLoader.TileSet.ARMORSMITHY, null, null),
      new Entry(Group.MANUFACTURING, "bowyer", "mapEditorPlayRightPanel.button.manufacturing.bowyer", MapTilesLoader.TileSet.BOWYER, null, null),
      new Entry(Group.MANUFACTURING, "rockTumbler", "mapEditorPlayRightPanel.button.manufacturing.rockTumbler", MapTilesLoader.TileSet.ROCK_TUMBLER, null, null),
      new Entry(Group.MANUFACTURING, "toolsmithy", "mapEditorPlayRightPanel.button.manufacturing.toolsmithy", MapTilesLoader.TileSet.TOOLSMITHY, null, null),
      new Entry(Group.REFINING, "crystillery", "mapEditorPlayRightPanel.button.refining.crystillery", MapTilesLoader.TileSet.CRYSTILLERY, null, null),
      new Entry(Group.REFINING, "forge", "mapEditorPlayRightPanel.button.refining.forge", MapTilesLoader.TileSet.FORGE, null, null),
      new Entry(Group.REFINING, "lumberMill", "mapEditorPlayRightPanel.button.refining.lumberMill", MapTilesLoader.TileSet.LUMBER_MILL, null, null),
      new Entry(Group.REFINING, "stoneCuttery", "mapEditorPlayRightPanel.button.refining.stoneCuttery", MapTilesLoader.TileSet.STONE_CUTTERY, null, null),
      new Entry(Group.STORAGE, "ammoStorage", "mapEditorPlayRightPanel.button.storage.ammoStorage", MapTilesLoader.TileSet.AMMO_STORAGE, null, null),
      new Entry(Group.STORAGE, "crystalStorage", "mapEditorPlayRightPanel.button.storage.crystalStorage", MapTilesLoader.TileSet.CRYSTAL_STORAGE, null, null),
      new Entry(Group.STORAGE, "equipmentStorage", "mapEditorPlayRightPanel.button.storage.equipmentStorage", MapTilesLoader.TileSet.EQUIPMENT_STORAGE, null, null),
      new Entry(Group.STORAGE, "foodStorage", "mapEditorPlayRightPanel.button.storage.foodStorage", MapTilesLoader.TileSet.FOOD_STORAGE, null, null),
      new Entry(Group.STORAGE, "goldStorage", "mapEditorPlayRightPanel.button.storage.goldStorage", MapTilesLoader.TileSet.GOLD_STORAGE, null, null),
      new Entry(Group.STORAGE, "keyShack", "mapEditorPlayRightPanel.button.storage.keyShack", MapTilesLoader.TileSet.KEY_SHACK, null, null),
      new Entry(Group.STORAGE, "mineralStorage", "mapEditorPlayRightPanel.button.storage.mineralStorage", MapTilesLoader.TileSet.MINERAL_STORAGE, null, null),
      new Entry(Group.STORAGE, "miscellaneousStorage", "mapEditorPlayRightPanel.button.storage.miscellaneousStorage", MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE, null, null),
      new Entry(Group.STORAGE, "rockStorage", "mapEditorPlayRightPanel.button.storage.rockStorage", MapTilesLoader.TileSet.ROCK_STORAGE, null, null),
      new Entry(Group.STORAGE, "woodStorage", "mapEditorPlayRightPanel.button.storage.woodStorage", MapTilesLoader.TileSet.WOOD_STORAGE, null, null),
      new Entry(Group.TRASH, "burner", "mapEditorPlayRightPanel.button.trash.burner", MapTilesLoader.TileSet.BURNER, null, null),
      new Entry(Group.TRASH, "cubeEGolemCombobulator", "mapEditorPlayRightPanel.button.trash.cubeEGolemCombobulator", MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR, null, null),
      new Entry(Group.TRASH, "landfill", "mapEditorPlayRightPanel.button.trash.landfill", MapTilesLoader.TileSet.LANDFILL, null, null),
      new Entry(Group.TRASH, "processor", "mapEditorPlayRightPanel.button.trash.processor", MapTilesLoader.TileSet.PROCESSOR, null, null),
      new Entry(Group.TRASH, "trashCan", "mapEditorPlayRightPanel.button.trash.trashCan", MapTilesLoader.TileSet.TRASH_CAN, null, null),
      new Entry(Group.TRASH, "trashyCubePile", "mapEditorPlayRightPanel.button.trash.trashyCubePile", MapTilesLoader.TileSet.TRASHY_CUBE_PILE, null, null),
      new Entry(Group.ROADS, "path", "mapEditorPlayRightPanel.button.roads.path", null, RoadModule.RoadType.PATH, null),
      new Entry(Group.ROADS, "logPath", "mapEditorPlayRightPanel.button.roads.logPath", null, RoadModule.RoadType.LOG_PATH, null),
      new Entry(Group.ROADS, "cobbleAndLogPath", "mapEditorPlayRightPanel.button.roads.cobbleAndLogPath", null, RoadModule.RoadType.COBBLE_AND_LOG_PATH, null),
      new Entry(Group.ROADS, "cobbleAndBoardRoad", "mapEditorPlayRightPanel.button.roads.cobbleAndBoardRoad", null, RoadModule.RoadType.COBBLE_AND_BOARD_ROAD, null),
      new Entry(Group.ROADS, "cutStoneAndBoardRoad", "mapEditorPlayRightPanel.button.roads.cutStoneAndBoardRoad", null, RoadModule.RoadType.CUT_STONE_AND_BOARD_ROAD, null),
      new Entry(Group.ROADS, "digHole", "mapEditorPlayRightPanel.button.roads.digHole", null, null, GUIEnums.InterfaceMode.ASSIGN_DIG_WORK),
      new Entry(Group.ROADS, "dismantleRoads", "mapEditorPlayRightPanel.button.roads.dismantleRoads", null, null, GUIEnums.InterfaceMode.ASSIGN_DISMANTLE_ROAD_WORK)
   };

   private BuildCatalog() {
   }

   public static ArrayList<Entry> inGroup(Group g) {
      ArrayList<Entry> out = new ArrayList<Entry>();

      for (Entry e : ALL) {
         if (e.group == g) {
            out.add(e);
         }
      }

      return out;
   }
}
