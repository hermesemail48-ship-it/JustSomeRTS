package rtr.objects.objectflags.towers;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.objects.towerhead.TowerBase;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;

public class BowTowerFlags extends ObjectFlags {
   public BowTowerFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.BOW_TOWER;
      this.width = 3;
      this.height = 3;
      String systemName = "bowTower";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.BOWYER});
      switch (this.currentType) {
         case BOW_TOWER:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 2);
            break;
         case BOW_TOWER_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 4);
            break;
         case BOW_TOWER_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 8);
            break;
         case BOW_TOWER_UPGRADE_3_FIRE:
            this.name = Text.getText("object.building." + systemName + ".fire.name");
            this.description = Text.getText("object.building." + systemName + ".fire.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 4);
            break;
         case BOW_TOWER_UPGRADE_3_POISON:
            this.name = Text.getText("object.building." + systemName + ".poison.name");
            this.description = Text.getText("object.building." + systemName + ".poison.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 4);
            break;
         case BOW_TOWER_UPGRADE_3_ICE:
            this.name = Text.getText("object.building." + systemName + ".ice.name");
            this.description = Text.getText("object.building." + systemName + ".ice.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 4);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.BOW_TOWER) {
         this.baseRange = 12;
         this.baseDesirability = -2;
         this.ammoMax = 20;
         this.buildingStorePriority.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
         this.towerCoordinates = OrderedPair.getOrderedPair(1, 1);
         this.towerType = TowerBase.TowerType.BOW;
         this.canBuildingFire.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
      }

      switch (this.currentType) {
         case BOW_TOWER_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 6);
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.BASE;
            break;
         case BOW_TOWER_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 8);
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_1;
            break;
         case BOW_TOWER_UPGRADE_3_FIRE:
         case BOW_TOWER_UPGRADE_3_POISON:
         case BOW_TOWER_UPGRADE_3_ICE:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 10);
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_2;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 12;
      this.baseDesirability = -2;
      this.ammoMax = 20;
      this.buildingStorePriority.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
      this.towerCoordinates = OrderedPair.getOrderedPair(1, 1);
      this.towerType = TowerBase.TowerType.BOW;
      this.canBuildingFire.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
      this.hasFillArt = true;
      switch (this.currentType) {
         case BOW_TOWER:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 6);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_1;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.BASE;
            break;
         case BOW_TOWER_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 8);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_2;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_1;
            break;
         case BOW_TOWER_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 10);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_3_FIRE;
            this.nextUpgradeType2 = MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_3_POISON;
            this.nextUpgradeType3 = MapTilesLoader.TileSet.BOW_TOWER_UPGRADE_3_ICE;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_2;
            break;
         case BOW_TOWER_UPGRADE_3_FIRE:
            this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ESSENCE_COLLECTOR});
            this.canAcceptEssence = true;
            this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
            this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
            this.energyMax = 250;
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 10);
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.FIRE;
            break;
         case BOW_TOWER_UPGRADE_3_POISON:
            this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ESSENCE_COLLECTOR});
            this.canAcceptEssence = true;
            this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
            this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
            this.energyMax = 250;
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 10);
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.POISON;
            break;
         case BOW_TOWER_UPGRADE_3_ICE:
            this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ESSENCE_COLLECTOR});
            this.canAcceptEssence = true;
            this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
            this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
            this.energyMax = 250;
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 10);
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.ICE;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.TOWER_STORAGE);
   }
}
