package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;

public class LightningRodFlags extends ObjectFlags {
   public LightningRodFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.LIGHTNING_ROD;
      this.width = 3;
      this.height = 3;
      String systemName = "lightningRod";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case LIGHTNING_ROD:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 8);
            break;
         case LIGHTNING_ROD_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 12);
            break;
         case LIGHTNING_ROD_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.LIGHTNING_ROD) {
         this.baseRange = 8;
         this.baseDesirability = -1;
         this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
         this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
         this.isLightningRod = true;
         this.lightningRodRange = 32;
         this.lightningRodCoordinates = OrderedPair.getOrderedPair(1, 1);
         this.lightningRodDissipationRate = 250;
         this.hasFillArt = true;
      }

      switch (this.currentType) {
         case LIGHTNING_ROD_UPGRADE_1:
            this.energyMax = 300;
            break;
         case LIGHTNING_ROD_UPGRADE_2:
            this.energyMax = 500;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 8;
      this.baseDesirability = -1;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
      this.isLightningRod = true;
      this.lightningRodRange = 32;
      this.lightningRodCoordinates = OrderedPair.getOrderedPair(1, 1);
      this.lightningRodDissipationRate = 250;
      this.hasFillArt = true;
      switch (this.currentType) {
         case LIGHTNING_ROD:
            this.energyMax = 300;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.LIGHTNING_ROD_UPGRADE_1;
            break;
         case LIGHTNING_ROD_UPGRADE_1:
            this.energyMax = 500;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.LIGHTNING_ROD_UPGRADE_2;
            break;
         case LIGHTNING_ROD_UPGRADE_2:
            this.energyMax = 700;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
