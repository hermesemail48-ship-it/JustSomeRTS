package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class FarmFlags extends ObjectFlags {
   public FarmFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.FARM;
      this.width = 9;
      this.height = 7;
      String systemName = "farm";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings
         .add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.RAIN_CATCHER, MapTilesLoader.TileSet.WATER_PURIFIER, MapTilesLoader.TileSet.WELL});
      switch (this.currentType) {
         case FARM:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case FARM_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            break;
         case FARM_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 96);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.FARM) {
         this.canCatchRain = true;
         this.rainCatchRate = 300;
         this.workerJobType = MobJobBase.MobJobType.FARMER;
         this.baseRange = 24;
         this.baseDesirability = 1;
         this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, ObjectBase.ResourceStorePriority.HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
         this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
         this.canBuildingStore.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      }

      switch (this.currentType) {
         case FARM_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, 24);
            break;
         case FARM_UPGRADE_2:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, 32);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.RAW_VEGETABLE, 10.0);
            this.farmingSpeedReduction = 5.0;
            this.tendAnimalsSpeedReduction = 5.0;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.canCatchRain = true;
      this.rainCatchRate = 300;
      this.workerJobType = MobJobBase.MobJobType.FARMER;
      this.baseRange = 24;
      this.baseDesirability = 1;
      this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, ObjectBase.ResourceStorePriority.HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canBuildingStore.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      this.hasFillArt = true;
      switch (this.currentType) {
         case FARM:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, 16);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.FARM_UPGRADE_1;
            break;
         case FARM_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, 24);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.RAW_VEGETABLE, 10.0);
            this.farmingSpeedReduction = 5.0;
            this.tendAnimalsSpeedReduction = 5.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.FARM_UPGRADE_2;
            break;
         case FARM_UPGRADE_2:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, 32);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.RAW_VEGETABLE, 20.0);
            this.farmingSpeedReduction = 10.0;
            this.tendAnimalsSpeedReduction = 10.0;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
