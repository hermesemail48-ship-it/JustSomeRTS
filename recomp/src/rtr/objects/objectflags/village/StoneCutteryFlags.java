package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class StoneCutteryFlags extends ObjectFlags {
   public StoneCutteryFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.STONE_CUTTERY;
      this.width = 8;
      this.height = 6;
      String systemName = "stoneCuttery";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.MINING_FACILITY});
      switch (this.currentType) {
         case STONE_CUTTERY:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            break;
         case STONE_CUTTERY_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 8);
            break;
         case STONE_CUTTERY_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.STONE_CUTTERY) {
         this.baseRange = 32;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.STONE_CUTTER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.ROCK, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.CUT_STONE, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.ROCK);
         this.canBuildingStore.add(ResourceModule.ResourceType.CUT_STONE);
      }

      switch (this.currentType) {
         case STONE_CUTTERY_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 12);
            break;
         case STONE_CUTTERY_UPGRADE_2:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 30);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 18);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.CUT_STONE, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 32;
      this.baseDesirability = 1;
      this.workerJobType = MobJobBase.MobJobType.STONE_CUTTER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.ROCK, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.CUT_STONE, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.ROCK);
      this.canBuildingStore.add(ResourceModule.ResourceType.CUT_STONE);
      this.hasFillArt = true;
      switch (this.currentType) {
         case STONE_CUTTERY:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 12);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.STONE_CUTTERY_UPGRADE_1;
            break;
         case STONE_CUTTERY_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 30);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 18);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.STONE_CUTTERY_UPGRADE_2;
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.CUT_STONE, 10.0);
            break;
         case STONE_CUTTERY_UPGRADE_2:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 42);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.CUT_STONE, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
