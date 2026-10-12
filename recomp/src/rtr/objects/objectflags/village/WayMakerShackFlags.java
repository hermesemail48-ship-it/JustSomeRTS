package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class WayMakerShackFlags extends ObjectFlags {
   public WayMakerShackFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.WAY_MAKER_SHACK;
      this.width = 7;
      this.height = 6;
      String systemName = "wayMakerShack";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.LUMBER_MILL});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.LUMBER_SHACK});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.MINING_FACILITY});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.STONE_CUTTERY});
      switch (this.currentType) {
         case WAY_MAKER_SHACK:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case WAY_MAKER_SHACK_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case WAY_MAKER_SHACK_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.WAY_MAKER_SHACK) {
         this.baseRange = 16;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.WAY_MAKER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BOARD, ObjectBase.ResourceStorePriority.LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.ROCK, ObjectBase.ResourceStorePriority.LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.CUT_STONE, ObjectBase.ResourceStorePriority.LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
         this.canBuildingStore.add(ResourceModule.ResourceType.BOARD);
         this.canBuildingStore.add(ResourceModule.ResourceType.ROCK);
         this.canBuildingStore.add(ResourceModule.ResourceType.CUT_STONE);
      }

      switch (this.currentType) {
         case WAY_MAKER_SHACK_UPGRADE_1:
            this.workersProvided = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 12);
            break;
         case WAY_MAKER_SHACK_UPGRADE_2:
            this.workersProvided = 10;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 16);
            this.buildingRoadSpeedReduction = 10.0;
            this.repairingRoadSpeedReduction = 10.0;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;
      this.workerJobType = MobJobBase.MobJobType.WAY_MAKER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BOARD, ObjectBase.ResourceStorePriority.LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.ROCK, ObjectBase.ResourceStorePriority.LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.CUT_STONE, ObjectBase.ResourceStorePriority.LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
      this.canBuildingStore.add(ResourceModule.ResourceType.BOARD);
      this.canBuildingStore.add(ResourceModule.ResourceType.ROCK);
      this.canBuildingStore.add(ResourceModule.ResourceType.CUT_STONE);
      this.hasFillArt = true;
      switch (this.currentType) {
         case WAY_MAKER_SHACK:
            this.workersProvided = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 8);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.WAY_MAKER_SHACK_UPGRADE_1;
            break;
         case WAY_MAKER_SHACK_UPGRADE_1:
            this.workersProvided = 10;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 12);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.WAY_MAKER_SHACK_UPGRADE_2;
            this.buildingRoadSpeedReduction = 10.0;
            this.repairingRoadSpeedReduction = 10.0;
            break;
         case WAY_MAKER_SHACK_UPGRADE_2:
            this.workersProvided = 14;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 16);
            this.buildingRoadSpeedReduction = 15.0;
            this.repairingRoadSpeedReduction = 15.0;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
