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

public class OutpostFlags extends ObjectFlags {
   public OutpostFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.OUTPOST;
      this.width = 4;
      this.height = 4;
      String systemName = "outpost";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case OUTPOST:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 8);
            break;
         case OUTPOST_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case OUTPOST_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 4);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.OUTPOST) {
         this.baseRange = 18;
         this.baseDesirability = -1;
         this.workerJobType = MobJobBase.MobJobType.RANGER;
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
         case OUTPOST_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HEALING_POTION, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 2);
            break;
         case OUTPOST_UPGRADE_2:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HEALING_POTION, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 3);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 18;
      this.baseDesirability = -1;
      this.workerJobType = MobJobBase.MobJobType.RANGER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, ObjectBase.ResourceStorePriority.VERY_HIGH);
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
      this.canBuildingStore.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
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
      switch (this.currentType) {
         case OUTPOST:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HEALING_POTION, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 2);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 2);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.OUTPOST_UPGRADE_1;
            break;
         case OUTPOST_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HEALING_POTION, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 3);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 3);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.OUTPOST_UPGRADE_2;
            break;
         case OUTPOST_UPGRADE_2:
            this.ammoMax = 20;
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 4);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 4);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 4);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 4);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 4);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 4);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 4);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HEALING_POTION, 4);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 4);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 4);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 4);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 4);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 6);
            this.buildingStorePriority.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, ObjectBase.ResourceStorePriority.VERY_HIGH);
            this.canBuildingStore.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
            this.towerCoordinates = OrderedPair.getOrderedPair(2, 2);
            this.towerType = TowerBase.TowerType.BOW;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.BASE;
            this.canBuildingFire.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
            this.hasFillArt = true;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
