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

public class PhantomDartTowerFlags extends ObjectFlags {
   public PhantomDartTowerFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.PHANTOM_DART_TOWER;
      this.width = 1;
      this.height = 1;
      String systemName = "phantomDartTower";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ESSENCE_COLLECTOR});
      switch (this.currentType) {
         case PHANTOM_DART_TOWER:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 8);
            break;
         case PHANTOM_DART_TOWER_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 12);
            break;
         case PHANTOM_DART_TOWER_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 4);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 24);
            break;
         case PHANTOM_DART_TOWER_UPGRADE_3_FIRE:
            this.name = Text.getText("object.building." + systemName + ".fire.name");
            this.description = Text.getText("object.building." + systemName + ".fire.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 6);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 4);
            break;
         case PHANTOM_DART_TOWER_UPGRADE_3_LIGHTNING:
            this.name = Text.getText("object.building." + systemName + ".lightning.name");
            this.description = Text.getText("object.building." + systemName + ".lightning.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 6);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 4);
            break;
         case PHANTOM_DART_TOWER_UPGRADE_3_ICE:
            this.name = Text.getText("object.building." + systemName + ".ice.name");
            this.description = Text.getText("object.building." + systemName + ".ice.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 6);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 4);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.PHANTOM_DART_TOWER) {
         this.baseRange = 12;
         this.baseDesirability = -2;
         this.canAcceptEssence = true;
         this.essenceReturnCoordinates = OrderedPair.getOrderedPair(0, 0);
         this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
         this.towerCoordinates = OrderedPair.getOrderedPair(0, 0);
         this.towerType = TowerBase.TowerType.PHANTOM_DART;
      }

      switch (this.currentType) {
         case PHANTOM_DART_TOWER_UPGRADE_1:
            this.energyMax = 75;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.BASE;
            break;
         case PHANTOM_DART_TOWER_UPGRADE_2:
            this.energyMax = 115;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_1;
            break;
         case PHANTOM_DART_TOWER_UPGRADE_3_FIRE:
         case PHANTOM_DART_TOWER_UPGRADE_3_LIGHTNING:
         case PHANTOM_DART_TOWER_UPGRADE_3_ICE:
            this.energyMax = 150;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_2;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 12;
      this.baseDesirability = -2;
      this.canAcceptEssence = true;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(0, 0);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
      this.towerCoordinates = OrderedPair.getOrderedPair(0, 0);
      this.towerType = TowerBase.TowerType.PHANTOM_DART;
      this.hasFillArt = true;
      switch (this.currentType) {
         case PHANTOM_DART_TOWER:
            this.energyMax = 75;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_1;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.BASE;
            break;
         case PHANTOM_DART_TOWER_UPGRADE_1:
            this.energyMax = 115;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_2;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_1;
            break;
         case PHANTOM_DART_TOWER_UPGRADE_2:
            this.energyMax = 150;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_3_FIRE;
            this.nextUpgradeType2 = MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_3_LIGHTNING;
            this.nextUpgradeType3 = MapTilesLoader.TileSet.PHANTOM_DART_TOWER_UPGRADE_3_ICE;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_2;
            break;
         case PHANTOM_DART_TOWER_UPGRADE_3_FIRE:
            this.energyMax = 150;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.FIRE;
            break;
         case PHANTOM_DART_TOWER_UPGRADE_3_LIGHTNING:
            this.energyMax = 150;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.LIGHTNING;
            break;
         case PHANTOM_DART_TOWER_UPGRADE_3_ICE:
            this.energyMax = 150;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.ICE;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.TOWER_STORAGE);
   }
}
