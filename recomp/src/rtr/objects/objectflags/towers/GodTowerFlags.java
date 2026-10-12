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

public class GodTowerFlags extends ObjectFlags {
   public GodTowerFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.GOD_TOWER;
      this.width = 3;
      this.height = 3;
      String systemName = "godTower";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.name = Text.getText("object.building." + systemName + ".base.name");
      this.description = Text.getText("object.building." + systemName + ".base.description");
      this.resourceBaseValues.put(ResourceModule.ResourceType.GOD_DUST, 8);
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.towerCoordinates = OrderedPair.getOrderedPair(1, 1);
      this.towerType = TowerBase.TowerType.GOD_BOW;
      this.canEnergyRegenerateFree = true;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
      this.energyMax = 250;
      this.energyRegenerationRate = 10;
      this.hasFillArt = true;
      this.towerUpgradeStage = TowerBase.TowerUpgradeStage.BASE;
      this.influenceMaintainCost = 1000;
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.GOD_TOWER_SPELL_COST);
   }
}
