package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class RockTumblerFlags extends ObjectFlags {
   public RockTumblerFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.ROCK_TUMBLER;
      this.width = 7;
      this.height = 8;
      String systemName = "rockTumbler";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.MINING_FACILITY});
      switch (this.currentType) {
         case ROCK_TUMBLER:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            break;
         case ROCK_TUMBLER_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 4);
            break;
         case ROCK_TUMBLER_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 8);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.ROCK_TUMBLER) {
         this.baseRange = 16;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.TUMBLER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.ROCK, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.STONE_BALLS, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.ROCK);
         this.canBuildingStore.add(ResourceModule.ResourceType.STONE_BALLS);
      }

      switch (this.currentType) {
         case ROCK_TUMBLER_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 8);
            break;
         case ROCK_TUMBLER_UPGRADE_2:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 12);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.STONE_BALLS, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;
      this.workerJobType = MobJobBase.MobJobType.TUMBLER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.ROCK, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.STONE_BALLS, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.ROCK);
      this.canBuildingStore.add(ResourceModule.ResourceType.STONE_BALLS);
      this.hasFillArt = true;
      switch (this.currentType) {
         case ROCK_TUMBLER:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 8);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ROCK_TUMBLER_UPGRADE_1;
            break;
         case ROCK_TUMBLER_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 12);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ROCK_TUMBLER_UPGRADE_2;
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.STONE_BALLS, 10.0);
            break;
         case ROCK_TUMBLER_UPGRADE_2:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 16);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.STONE_BALLS, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
