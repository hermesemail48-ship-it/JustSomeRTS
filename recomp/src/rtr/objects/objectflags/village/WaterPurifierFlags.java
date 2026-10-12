package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class WaterPurifierFlags extends ObjectFlags {
   public WaterPurifierFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.WATER_PURIFIER;
      this.width = 6;
      this.height = 7;
      String systemName = "waterPurifier";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case WATER_PURIFIER:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            break;
         case WATER_PURIFIER_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            break;
         case WATER_PURIFIER_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.WATER_PURIFIER) {
         this.baseRange = 24;
         this.baseDesirability = 1;
         this.canPurifyWater = true;
         this.workerJobType = MobJobBase.MobJobType.WATER_MASTER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
         this.canBuildingStore.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      }

      switch (this.currentType) {
         case WATER_PURIFIER_UPGRADE_1:
            this.purifyWaterRate = 40;
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, 32);
            break;
         case WATER_PURIFIER_UPGRADE_2:
            this.purifyWaterRate = 30;
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, 48);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.WATER_BUCKET, 10.0);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 24;
      this.baseDesirability = 1;
      this.canPurifyWater = true;
      this.workerJobType = MobJobBase.MobJobType.WATER_MASTER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canBuildingStore.add(ResourceModule.ResourceType.DIRTY_WATER_BUCKET);
      this.hasFillArt = true;
      switch (this.currentType) {
         case WATER_PURIFIER:
            this.purifyWaterRate = 40;
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, 32);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.WATER_PURIFIER_UPGRADE_1;
            break;
         case WATER_PURIFIER_UPGRADE_1:
            this.purifyWaterRate = 30;
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, 48);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.WATER_PURIFIER_UPGRADE_2;
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.WATER_BUCKET, 10.0);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, 10.0);
            break;
         case WATER_PURIFIER_UPGRADE_2:
            this.purifyWaterRate = 20;
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 20);
            this.resourceStorageMax.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, 60);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.WATER_BUCKET, 20.0);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.DIRTY_WATER_BUCKET, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.WATER_STORAGE);
   }
}
