package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class WellFlags extends ObjectFlags {
   public WellFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.WELL;
      this.width = 3;
      this.height = 3;
      String systemName = "well";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case WELL:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 4);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 8);
            break;
         case WELL_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 4);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case WELL_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 6);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 4);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.WELL) {
         this.baseRange = 4;
         this.baseDesirability = 2;
         this.canCatchRain = true;
         this.rainCatchRate = 200;
         this.canGenerateWater = true;
         this.workerJobType = MobJobBase.MobJobType.WATER_MASTER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      }

      switch (this.currentType) {
         case WELL_UPGRADE_1:
            this.waterGenerationRate = 150;
            this.workersProvided = 1;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 2);
            break;
         case WELL_UPGRADE_2:
            this.waterGenerationRate = 100;
            this.workersProvided = 1;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 3);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 4;
      this.baseDesirability = 2;
      this.canCatchRain = true;
      this.rainCatchRate = 200;
      this.canGenerateWater = true;
      this.workerJobType = MobJobBase.MobJobType.WATER_MASTER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.hasFillArt = true;
      switch (this.currentType) {
         case WELL:
            this.waterGenerationRate = 150;
            this.workersProvided = 1;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 2);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.WELL_UPGRADE_1;
            break;
         case WELL_UPGRADE_1:
            this.waterGenerationRate = 100;
            this.workersProvided = 1;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 3);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.WELL_UPGRADE_2;
            break;
         case WELL_UPGRADE_2:
            this.waterGenerationRate = 50;
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 4);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.WATER_STORAGE);
   }
}
