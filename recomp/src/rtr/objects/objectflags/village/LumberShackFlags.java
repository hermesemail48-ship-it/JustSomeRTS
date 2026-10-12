package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class LumberShackFlags extends ObjectFlags {
   public LumberShackFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.LUMBER_SHACK;
      this.width = 8;
      this.height = 6;
      String systemName = "lumberShack";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case LUMBER_SHACK:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            break;
         case LUMBER_SHACK_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            break;
         case LUMBER_SHACK_UPGRADE_2:
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
      if (this.currentType != MapTilesLoader.TileSet.LUMBER_SHACK) {
         this.baseRange = 32;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.LUMBERJACK;
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
      }

      switch (this.currentType) {
         case LUMBER_SHACK_UPGRADE_1:
            this.workersProvided = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case LUMBER_SHACK_UPGRADE_2:
            this.workersProvided = 10;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 48);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.WOOD, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 32;
      this.baseDesirability = 1;
      this.workerJobType = MobJobBase.MobJobType.LUMBERJACK;
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
      this.hasFillArt = true;
      switch (this.currentType) {
         case LUMBER_SHACK:
            this.workersProvided = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 32);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.LUMBER_SHACK_UPGRADE_1;
            break;
         case LUMBER_SHACK_UPGRADE_1:
            this.workersProvided = 10;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 48);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.WOOD, 10.0);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.LUMBER_SHACK_UPGRADE_2;
            break;
         case LUMBER_SHACK_UPGRADE_2:
            this.workersProvided = 14;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 64);
            this.harvestingSpeedReduction.put(ResourceModule.ResourceType.WOOD, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
