package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.objects.towerhead.TowerBase;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;

public class RangerLodgeFlags extends ObjectFlags {
   public RangerLodgeFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.RANGER_LODGE;
      this.width = 8;
      this.height = 9;
      String systemName = "rangerLodge";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case RANGER_LODGE:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            break;
         case RANGER_LODGE_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            break;
         case RANGER_LODGE_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 72);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 6);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 4);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.RANGER_LODGE) {
         this.baseRange = 24;
         this.baseDesirability = -1;
         this.workerJobType = MobJobBase.MobJobType.RANGER;
         this.occupantsJob = MobJobBase.MobJobType.RANGER;
         this.occupantsSleepBonusPercentOff = 15;
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_HELMET, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_SHIELD, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_SHIELD, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_HELMET, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BANDAGE, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.HEALING_POTION, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_SWORD, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD_SWORD, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BOW, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.QUIVER, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_CHEST_ARMOR);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_HELMET);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_SHIELD);
         this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_SHIELD);
         this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR);
         this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_HELMET);
         this.canBuildingStore.add(ResourceModule.ResourceType.BANDAGE);
         this.canBuildingStore.add(ResourceModule.ResourceType.HEALING_POTION);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_SWORD);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOOD_SWORD);
         this.canBuildingStore.add(ResourceModule.ResourceType.BOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.QUIVER);
      }

      switch (this.currentType) {
         case RANGER_LODGE_UPGRADE_1:
            this.workersProvided = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HEALING_POTION, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 6);
            this.occupantsMax = this.workersProvided;
            this.occupantsHealthRegenBonusTicks = 3;
            break;
         case RANGER_LODGE_UPGRADE_2:
            this.workersProvided = 12;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HEALING_POTION, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 12);
            this.occupantsMax = this.workersProvided;
            this.occupantsHealthRegenBonusTicks = 4;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 24;
      this.baseDesirability = -1;
      this.workerJobType = MobJobBase.MobJobType.RANGER;
      this.occupantsJob = MobJobBase.MobJobType.RANGER;
      this.occupantsSleepBonusPercentOff = 15;
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_HELMET, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_SHIELD, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_SHIELD, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_HELMET, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BANDAGE, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.HEALING_POTION, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_SWORD, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD_SWORD, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BOW, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.QUIVER, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_CHEST_ARMOR);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_HELMET);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_SHIELD);
      this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_SHIELD);
      this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR);
      this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_HELMET);
      this.canBuildingStore.add(ResourceModule.ResourceType.BANDAGE);
      this.canBuildingStore.add(ResourceModule.ResourceType.HEALING_POTION);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_SWORD);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOOD_SWORD);
      this.canBuildingStore.add(ResourceModule.ResourceType.BOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.QUIVER);
      this.hasFillArt = true;
      switch (this.currentType) {
         case RANGER_LODGE:
            this.workersProvided = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HEALING_POTION, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 6);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 6);
            this.occupantsMax = this.workersProvided;
            this.occupantsHealthRegenBonusTicks = 3;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.RANGER_LODGE_UPGRADE_1;
            break;
         case RANGER_LODGE_UPGRADE_1:
            this.workersProvided = 12;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HEALING_POTION, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 12);
            this.occupantsMax = this.workersProvided;
            this.occupantsHealthRegenBonusTicks = 4;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.RANGER_LODGE_UPGRADE_2;
            break;
         case RANGER_LODGE_UPGRADE_2:
            this.ammoMax = 20;
            this.workersProvided = 16;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HEALING_POTION, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 6);
            this.buildingStorePriority.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, ObjectBase.ResourceStorePriority.VERY_HIGH);
            this.canBuildingStore.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
            this.occupantsMax = this.workersProvided;
            this.occupantsHealthRegenBonusTicks = 5;
            this.towerCoordinates = OrderedPair.getOrderedPair(6, 1);
            this.towerType = TowerBase.TowerType.BOW;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.BASE;
            this.canBuildingFire.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
