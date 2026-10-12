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

public class SlingTowerFlags extends ObjectFlags {
   public SlingTowerFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.SLING_TOWER;
      this.width = 3;
      this.height = 3;
      String systemName = "slingTower";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ROCK_TUMBLER});
      switch (this.currentType) {
         case SLING_TOWER:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 4);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 2);
            break;
         case SLING_TOWER_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 4);
            break;
         case SLING_TOWER_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 46);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 8);
            break;
         case SLING_TOWER_UPGRADE_3_EXPLODING:
            this.name = Text.getText("object.building." + systemName + ".exploding.name");
            this.description = Text.getText("object.building." + systemName + ".exploding.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 8);
            break;
         case SLING_TOWER_UPGRADE_3_LIGHTNING:
            this.name = Text.getText("object.building." + systemName + ".lightning.name");
            this.description = Text.getText("object.building." + systemName + ".lightning.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 8);
            break;
         case SLING_TOWER_UPGRADE_3_ICE:
            this.name = Text.getText("object.building." + systemName + ".ice.name");
            this.description = Text.getText("object.building." + systemName + ".ice.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 8);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.SLING_TOWER) {
         this.baseRange = 12;
         this.baseDesirability = -2;
         this.ammoMax = 10;
         this.buildingStorePriority.put(ResourceModule.ResourceType.STONE_BALLS, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.STONE_BALLS);
         this.towerCoordinates = OrderedPair.getOrderedPair(1, 1);
         this.towerType = TowerBase.TowerType.SLING;
         this.canBuildingFire.add(ResourceModule.ResourceType.STONE_BALLS);
      }

      switch (this.currentType) {
         case SLING_TOWER_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 6);
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.BASE;
            break;
         case SLING_TOWER_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 8);
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_1;
            break;
         case SLING_TOWER_UPGRADE_3_EXPLODING:
         case SLING_TOWER_UPGRADE_3_LIGHTNING:
         case SLING_TOWER_UPGRADE_3_ICE:
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 10);
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_2;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 12;
      this.baseDesirability = -2;
      this.ammoMax = 10;
      this.buildingStorePriority.put(ResourceModule.ResourceType.STONE_BALLS, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.STONE_BALLS);
      this.towerCoordinates = OrderedPair.getOrderedPair(1, 1);
      this.towerType = TowerBase.TowerType.SLING;
      this.canBuildingFire.add(ResourceModule.ResourceType.STONE_BALLS);
      this.hasFillArt = true;
      switch (this.currentType) {
         case SLING_TOWER:
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 6);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_1;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.BASE;
            break;
         case SLING_TOWER_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 8);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_2;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_1;
            break;
         case SLING_TOWER_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 10);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_3_EXPLODING;
            this.nextUpgradeType2 = MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_3_LIGHTNING;
            this.nextUpgradeType3 = MapTilesLoader.TileSet.SLING_TOWER_UPGRADE_3_ICE;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_2;
            break;
         case SLING_TOWER_UPGRADE_3_EXPLODING:
            this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ESSENCE_COLLECTOR});
            this.canAcceptEssence = true;
            this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
            this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
            this.energyMax = 250;
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 10);
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.FIRE;
            break;
         case SLING_TOWER_UPGRADE_3_LIGHTNING:
            this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ESSENCE_COLLECTOR});
            this.canAcceptEssence = true;
            this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
            this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
            this.energyMax = 250;
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 10);
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.LIGHTNING;
            break;
         case SLING_TOWER_UPGRADE_3_ICE:
            this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ESSENCE_COLLECTOR});
            this.canAcceptEssence = true;
            this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
            this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
            this.energyMax = 250;
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 10);
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.ICE;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.TOWER_STORAGE);
   }
}
