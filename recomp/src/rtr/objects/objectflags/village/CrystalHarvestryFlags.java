package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class CrystalHarvestryFlags extends ObjectFlags {
   public CrystalHarvestryFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.CRYSTAL_HARVESTRY;
      this.width = 10;
      this.height = 10;
      String systemName = "crystalHarvestry";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case CRYSTAL_HARVESTRY:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            break;
         case CRYSTAL_HARVESTRY_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            break;
         case CRYSTAL_HARVESTRY_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 26);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 128);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 4);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.CRYSTAL_HARVESTRY) {
         this.baseRange = 32;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.CRYSTAL_HARVESTER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTAL, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTAL);
      }

      switch (this.currentType) {
         case CRYSTAL_HARVESTRY_UPGRADE_1:
            this.workersProvided = 8;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 64);
            break;
         case CRYSTAL_HARVESTRY_UPGRADE_2:
            this.workersProvided = 12;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 96);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.CRYSTAL, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 32;
      this.baseDesirability = 1;
      this.workerJobType = MobJobBase.MobJobType.CRYSTAL_HARVESTER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTAL, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTAL);
      this.hasFillArt = true;
      switch (this.currentType) {
         case CRYSTAL_HARVESTRY:
            this.workersProvided = 8;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 64);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CRYSTAL_HARVESTRY_UPGRADE_1;
            break;
         case CRYSTAL_HARVESTRY_UPGRADE_1:
            this.workersProvided = 12;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 96);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.CRYSTAL, 10.0);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CRYSTAL_HARVESTRY_UPGRADE_2;
            break;
         case CRYSTAL_HARVESTRY_UPGRADE_2:
            this.workersProvided = 16;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 128);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.CRYSTAL, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
