package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class MaintenanceBuildingFlags extends ObjectFlags {
   public MaintenanceBuildingFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.MAINTENANCE_BUILDING;
      this.width = 8;
      this.height = 7;
      String systemName = "maintenanceBuilding";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.LUMBER_SHACK});
      switch (this.currentType) {
         case MAINTENANCE_BUILDING:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case MAINTENANCE_BUILDING_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case MAINTENANCE_BUILDING_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.MAINTENANCE_BUILDING) {
         this.baseRange = 16;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.MAINTAINER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.AXE, ObjectBase.ResourceStorePriority.LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.HAMMER, ObjectBase.ResourceStorePriority.LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.PICK_AXE, ObjectBase.ResourceStorePriority.LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.SHOVEL, ObjectBase.ResourceStorePriority.LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.AXE);
         this.canBuildingStore.add(ResourceModule.ResourceType.HAMMER);
         this.canBuildingStore.add(ResourceModule.ResourceType.PICK_AXE);
         this.canBuildingStore.add(ResourceModule.ResourceType.SHOVEL);
      }

      switch (this.currentType) {
         case MAINTENANCE_BUILDING_UPGRADE_1:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 6);
            break;
         case MAINTENANCE_BUILDING_UPGRADE_2:
            this.workersProvided = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 8);
            this.dismantlingSpeedReduction = 10.0;
            this.clearingSpeedReduction = 10.0;
            this.repairingSpeedReduction = 10.0;
            this.repairingRoadSpeedReduction = 10.0;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;
      this.workerJobType = MobJobBase.MobJobType.MAINTAINER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.AXE, ObjectBase.ResourceStorePriority.LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.HAMMER, ObjectBase.ResourceStorePriority.LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.PICK_AXE, ObjectBase.ResourceStorePriority.LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.SHOVEL, ObjectBase.ResourceStorePriority.LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.AXE);
      this.canBuildingStore.add(ResourceModule.ResourceType.HAMMER);
      this.canBuildingStore.add(ResourceModule.ResourceType.PICK_AXE);
      this.canBuildingStore.add(ResourceModule.ResourceType.SHOVEL);
      this.hasFillArt = true;
      switch (this.currentType) {
         case MAINTENANCE_BUILDING:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 6);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.MAINTENANCE_BUILDING_UPGRADE_1;
            break;
         case MAINTENANCE_BUILDING_UPGRADE_1:
            this.workersProvided = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 8);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.MAINTENANCE_BUILDING_UPGRADE_2;
            this.dismantlingSpeedReduction = 10.0;
            this.clearingSpeedReduction = 10.0;
            this.repairingSpeedReduction = 10.0;
            this.repairingRoadSpeedReduction = 10.0;
            break;
         case MAINTENANCE_BUILDING_UPGRADE_2:
            this.workersProvided = 8;
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 12);
            this.dismantlingSpeedReduction = 15.0;
            this.clearingSpeedReduction = 15.0;
            this.repairingSpeedReduction = 15.0;
            this.repairingRoadSpeedReduction = 15.0;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
