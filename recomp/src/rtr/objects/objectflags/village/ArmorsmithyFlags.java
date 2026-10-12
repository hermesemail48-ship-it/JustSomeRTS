package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class ArmorsmithyFlags extends ObjectFlags {
   public ArmorsmithyFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.ARMORSMITHY;
      this.width = 9;
      this.height = 7;
      String systemName = "armorsmithy";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.FORGE, MapTilesLoader.TileSet.KITCHEN});
      switch (this.currentType) {
         case ARMORSMITHY:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 4);
            break;
         case ARMORSMITHY_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
            break;
         case ARMORSMITHY_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.ARMORSMITHY) {
         this.baseRange = 16;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.ARMORSMITH;
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_HELMET, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_SHIELD, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_HELMET, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_SHIELD, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_INGOT, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_HIDE, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_CHEST_ARMOR);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_HELMET);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_SHIELD);
         this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR);
         this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_HELMET);
         this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_SHIELD);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_INGOT);
         this.canBuildingStore.add(ResourceModule.ResourceType.RAW_HIDE);
      }

      switch (this.currentType) {
         case ARMORSMITHY_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_HIDE, 24);
            break;
         case ARMORSMITHY_UPGRADE_2:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_HIDE, 32);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_HELMET, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_SHIELD, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.LEATHER_HELMET, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;
      this.workerJobType = MobJobBase.MobJobType.ARMORSMITH;
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_HELMET, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_SHIELD, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_HELMET, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_SHIELD, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_INGOT, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_HIDE, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_CHEST_ARMOR);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_HELMET);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_SHIELD);
      this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR);
      this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_HELMET);
      this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_SHIELD);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canBuildingStore.add(ResourceModule.ResourceType.RAW_HIDE);
      this.hasFillArt = true;
      switch (this.currentType) {
         case ARMORSMITHY:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_HIDE, 24);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ARMORSMITHY_UPGRADE_1;
            break;
         case ARMORSMITHY_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_HIDE, 32);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ARMORSMITHY_UPGRADE_2;
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_HELMET, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_SHIELD, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.LEATHER_HELMET, 10.0);
            break;
         case ARMORSMITHY_UPGRADE_2:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_HIDE, 40);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_HELMET, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.IRON_SHIELD, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.LEATHER_HELMET, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
