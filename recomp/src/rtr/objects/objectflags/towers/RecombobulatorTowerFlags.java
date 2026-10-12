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

public class RecombobulatorTowerFlags extends ObjectFlags {
   public RecombobulatorTowerFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER;
      this.width = 3;
      this.height = 3;
      String systemName = "recombobulatorTower";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ESSENCE_COLLECTOR});
      switch (this.currentType) {
         case RECOMBOBULATOR_TOWER:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 8);
            break;
         case RECOMBOBULATOR_TOWER_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 28);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 6);
            break;
         case RECOMBOBULATOR_TOWER_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 46);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 12);
            break;
         case RECOMBOBULATOR_TOWER_UPGRADE_3:
            this.name = Text.getText("object.building." + systemName + ".reinforced.name");
            this.description = Text.getText("object.building." + systemName + ".reinforced.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 32);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER) {
         this.baseRange = 12;
         this.baseDesirability = -2;
         this.canAcceptEssence = true;
         this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
         this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
         this.towerCoordinates = OrderedPair.getOrderedPair(1, 1);
         this.towerType = TowerBase.TowerType.RECOMBOBULATOR;
      }

      switch (this.currentType) {
         case RECOMBOBULATOR_TOWER_UPGRADE_1:
            this.energyMax = 100;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.BASE;
            break;
         case RECOMBOBULATOR_TOWER_UPGRADE_2:
            this.energyMax = 150;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_1;
            break;
         case RECOMBOBULATOR_TOWER_UPGRADE_3:
            this.energyMax = 200;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_2;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 12;
      this.baseDesirability = -2;
      this.canAcceptEssence = true;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
      this.towerCoordinates = OrderedPair.getOrderedPair(1, 1);
      this.towerType = TowerBase.TowerType.RECOMBOBULATOR;
      this.hasFillArt = true;
      switch (this.currentType) {
         case RECOMBOBULATOR_TOWER:
            this.energyMax = 100;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER_UPGRADE_1;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.BASE;
            break;
         case RECOMBOBULATOR_TOWER_UPGRADE_1:
            this.energyMax = 150;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER_UPGRADE_2;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_1;
            break;
         case RECOMBOBULATOR_TOWER_UPGRADE_2:
            this.energyMax = 200;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER_UPGRADE_3;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_2;
            break;
         case RECOMBOBULATOR_TOWER_UPGRADE_3:
            this.energyMax = 250;
            this.towerUpgradeStage = TowerBase.TowerUpgradeStage.UPGRADE_3;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.TOWER_STORAGE);
   }
}
