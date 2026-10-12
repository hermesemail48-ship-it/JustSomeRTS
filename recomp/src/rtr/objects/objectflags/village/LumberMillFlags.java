package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class LumberMillFlags extends ObjectFlags {
   public LumberMillFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.LUMBER_MILL;
      this.width = 11;
      this.height = 7;
      String systemName = "lumberMill";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.LUMBER_SHACK});
      switch (this.currentType) {
         case LUMBER_MILL:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            break;
         case LUMBER_MILL_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 72);
            break;
         case LUMBER_MILL_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 96);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 96);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 8);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.LUMBER_MILL) {
         this.baseRange = 32;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.CARPENTER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BOARD, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
         this.canBuildingStore.add(ResourceModule.ResourceType.BOARD);
      }

      switch (this.currentType) {
         case LUMBER_MILL_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 12);
            break;
         case LUMBER_MILL_UPGRADE_2:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 24);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BOARD, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 32;
      this.baseDesirability = 1;
      this.workerJobType = MobJobBase.MobJobType.CARPENTER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BOARD, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
      this.canBuildingStore.add(ResourceModule.ResourceType.BOARD);
      this.hasFillArt = true;
      switch (this.currentType) {
         case LUMBER_MILL:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 12);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.LUMBER_MILL_UPGRADE_1;
            break;
         case LUMBER_MILL_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 24);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.LUMBER_MILL_UPGRADE_2;
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BOARD, 10.0);
            break;
         case LUMBER_MILL_UPGRADE_2:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 96);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 32);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BOARD, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
