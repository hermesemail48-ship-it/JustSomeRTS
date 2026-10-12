package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class ToolsmithyFlags extends ObjectFlags {
   public ToolsmithyFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.TOOLSMITHY;
      this.width = 7;
      this.height = 9;
      String systemName = "toolsmithy";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.FORGE, MapTilesLoader.TileSet.LUMBER_MILL});
      switch (this.currentType) {
         case TOOLSMITHY:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 4);
            break;
         case TOOLSMITHY_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
            break;
         case TOOLSMITHY_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 56);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 40);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.TOOLSMITHY) {
         this.baseRange = 16;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.TOOLSMITH;
         this.buildingStorePriority.put(ResourceModule.ResourceType.AXE, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.HAMMER, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.HOE, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.PICK_AXE, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.SHOVEL, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_SWORD, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_INGOT, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD_SWORD, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BOARD, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.AXE);
         this.canBuildingStore.add(ResourceModule.ResourceType.HAMMER);
         this.canBuildingStore.add(ResourceModule.ResourceType.HOE);
         this.canBuildingStore.add(ResourceModule.ResourceType.PICK_AXE);
         this.canBuildingStore.add(ResourceModule.ResourceType.SHOVEL);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_SWORD);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_INGOT);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOOD_SWORD);
         this.canBuildingStore.add(ResourceModule.ResourceType.BOARD);
      }

      switch (this.currentType) {
         case TOOLSMITHY_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 24);
            break;
         case TOOLSMITHY_UPGRADE_2:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 32);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.AXE, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.HAMMER, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.HOE, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.PICK_AXE, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.SHOVEL, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_SWORD, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;
      this.workerJobType = MobJobBase.MobJobType.TOOLSMITH;
      this.buildingStorePriority.put(ResourceModule.ResourceType.AXE, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.HAMMER, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.HOE, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.PICK_AXE, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.SHOVEL, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_SWORD, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_INGOT, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD_SWORD, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BOARD, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.AXE);
      this.canBuildingStore.add(ResourceModule.ResourceType.HAMMER);
      this.canBuildingStore.add(ResourceModule.ResourceType.HOE);
      this.canBuildingStore.add(ResourceModule.ResourceType.PICK_AXE);
      this.canBuildingStore.add(ResourceModule.ResourceType.SHOVEL);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_SWORD);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOOD_SWORD);
      this.canBuildingStore.add(ResourceModule.ResourceType.BOARD);
      this.hasFillArt = true;
      switch (this.currentType) {
         case TOOLSMITHY:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 24);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.TOOLSMITHY_UPGRADE_1;
            break;
         case TOOLSMITHY_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 32);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.TOOLSMITHY_UPGRADE_2;
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.AXE, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.HAMMER, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.HOE, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.PICK_AXE, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.SHOVEL, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_SWORD, 10.0);
            break;
         case TOOLSMITHY_UPGRADE_2:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 40);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.AXE, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.HAMMER, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.HOE, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.PICK_AXE, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.SHOVEL, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_SWORD, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
