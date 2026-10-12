package rtr.objects.objectflags.corruption;

import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.objects.towerhead.TowerBase;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;

public class CorruptedPoisonPhantomDartTowerFlags extends ObjectFlags {
   public CorruptedPoisonPhantomDartTowerFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.CORRUPTED_POISON_PHANTOM_DART_TOWER;
      this.width = 1;
      this.height = 1;
      String systemName = "corruptedPoisonPhantomDartTower";
      this.baseName = Text.getText("object.building." + systemName + ".name");
      this.name = Text.getText("object.building." + systemName + ".name");
      this.description = Text.getText("object.building." + systemName + ".description");
      this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 12);
      this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 8);
      this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 2);
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.MONSTER;
      this.destroyIfIgnored = true;
      this.destroyIfIgnoredDelay = 100000;
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.MONSTER;
      this.canEnergyRegenerateFree = true;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(0, 0);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
      this.energyMax = 250;
      this.energyRegenerationRate = 150;
      this.baseDesirability = -2;
      this.towerCoordinates = OrderedPair.getOrderedPair(1, 1);
      this.towerType = TowerBase.TowerType.CORRUPTED_POISON_PHANTOM_DART;
      this.towerUpgradeStage = TowerBase.TowerUpgradeStage.BASE;
      this.hasFillArt = true;
   }
}
