package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class ForgeFlags extends ObjectFlags {
   public ForgeFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.FORGE;
      this.width = 9;
      this.height = 8;
      String systemName = "forge";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.MINING_FACILITY});
      switch (this.currentType) {
         case FORGE:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            break;
         case FORGE_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 34);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
            break;
         case FORGE_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.FORGE) {
         this.baseRange = 22;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.SMELTER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_INGOT, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_ORE, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_INGOT, ObjectBase.ResourceStorePriority.HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_ORE, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_COIN_SACK, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_INGOT);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_ORE);
         this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_INGOT);
         this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_ORE);
         this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_COIN_SACK);
      }

      switch (this.currentType) {
         case FORGE_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_ORE, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_ORE, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 16);
            break;
         case FORGE_UPGRADE_2:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_ORE, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_ORE, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 24);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_INGOT, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.GOLD_INGOT, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 22;
      this.baseDesirability = 1;
      this.workerJobType = MobJobBase.MobJobType.SMELTER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_INGOT, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_ORE, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_INGOT, ObjectBase.ResourceStorePriority.HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_ORE, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_COIN_SACK, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_ORE);
      this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_INGOT);
      this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_ORE);
      this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_COIN_SACK);
      this.hasFillArt = true;
      switch (this.currentType) {
         case FORGE:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_ORE, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_ORE, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 16);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.FORGE_UPGRADE_1;
            break;
         case FORGE_UPGRADE_1:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_ORE, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_ORE, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 24);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.FORGE_UPGRADE_2;
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_INGOT, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.GOLD_INGOT, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 10.0);
            break;
         case FORGE_UPGRADE_2:
            this.workersProvided = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_ORE, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_ORE, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 32);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_INGOT, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.GOLD_INGOT, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
