package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class RainCatcherFlags extends ObjectFlags {
   public RainCatcherFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.RAIN_CATCHER;
      this.width = 4;
      this.height = 4;
      String systemName = "rainCatcher";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case RAIN_CATCHER:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 16);
            break;
         case RAIN_CATCHER_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case RAIN_CATCHER_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.RAIN_CATCHER) {
         this.baseRange = 6;
         this.baseDesirability = 2;
         this.canCatchRain = true;
         this.rainCatchRate = 10;
         this.workerJobType = MobJobBase.MobJobType.WATER_MASTER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      }

      switch (this.currentType) {
         case RAIN_CATCHER_UPGRADE_1:
            this.workersProvided = 1;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 8);
            break;
         case RAIN_CATCHER_UPGRADE_2:
            this.workersProvided = 1;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 10);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 6;
      this.baseDesirability = 2;
      this.canCatchRain = true;
      this.rainCatchRate = 10;
      this.workerJobType = MobJobBase.MobJobType.WATER_MASTER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.hasFillArt = true;
      switch (this.currentType) {
         case RAIN_CATCHER:
            this.workersProvided = 1;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 8);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.RAIN_CATCHER_UPGRADE_1;
            break;
         case RAIN_CATCHER_UPGRADE_1:
            this.workersProvided = 1;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 10);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.RAIN_CATCHER_UPGRADE_2;
            break;
         case RAIN_CATCHER_UPGRADE_2:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 12);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.WATER_STORAGE);
   }
}
