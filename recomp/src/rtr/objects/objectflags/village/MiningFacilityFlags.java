package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class MiningFacilityFlags extends ObjectFlags {
   public MiningFacilityFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.MINING_FACILITY;
      this.width = 8;
      this.height = 9;
      String systemName = "miningFacility";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case MINING_FACILITY:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            break;
         case MINING_FACILITY_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            break;
         case MINING_FACILITY_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 8);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.MINING_FACILITY) {
         this.baseRange = 32;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.MINER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.ROCK, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.ROCK);
      }

      switch (this.currentType) {
         case MINING_FACILITY_UPGRADE_1:
            this.workersProvided = 8;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 64);
            break;
         case MINING_FACILITY_UPGRADE_2:
            this.workersProvided = 12;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 96);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.ROCK, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 32;
      this.baseDesirability = 1;
      this.workerJobType = MobJobBase.MobJobType.MINER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.ROCK, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.ROCK);
      this.hasFillArt = true;
      switch (this.currentType) {
         case MINING_FACILITY:
            this.workersProvided = 8;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 64);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.MINING_FACILITY_UPGRADE_1;
            break;
         case MINING_FACILITY_UPGRADE_1:
            this.workersProvided = 12;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 96);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.ROCK, 10.0);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.MINING_FACILITY_UPGRADE_2;
            break;
         case MINING_FACILITY_UPGRADE_2:
            this.workersProvided = 15;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 128);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.ROCK, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
