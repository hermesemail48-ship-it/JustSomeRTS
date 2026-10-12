package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class BottlerFlags extends ObjectFlags {
   public BottlerFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.BOTTLER;
      this.width = 4;
      this.height = 6;
      String systemName = "bottler";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings
         .add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.RAIN_CATCHER, MapTilesLoader.TileSet.WATER_PURIFIER, MapTilesLoader.TileSet.WELL});
      switch (this.currentType) {
         case BOTTLER:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case BOTTLER_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case BOTTLER_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 42);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 20);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.BOTTLER) {
         this.isFountain = true;
         this.canCatchRain = true;
         this.rainCatchRate = 500;
         this.baseRange = 12;
         this.baseDesirability = 2;
         this.workerJobType = MobJobBase.MobJobType.BOTTLER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BOTTLE, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
         this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BOTTLE);
      }

      switch (this.currentType) {
         case BOTTLER_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 12);
            break;
         case BOTTLER_UPGRADE_2:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 16);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.WATER_BOTTLE, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.isFountain = true;
      this.canCatchRain = true;
      this.rainCatchRate = 500;
      this.baseRange = 12;
      this.baseDesirability = 2;
      this.workerJobType = MobJobBase.MobJobType.BOTTLER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BOTTLE, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BOTTLE);
      this.hasFillArt = true;
      switch (this.currentType) {
         case BOTTLER:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 12);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.BOTTLER_UPGRADE_1;
            break;
         case BOTTLER_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 16);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.WATER_BOTTLE, 10.0);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.BOTTLER_UPGRADE_2;
            break;
         case BOTTLER_UPGRADE_2:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 20);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.WATER_BOTTLE, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
