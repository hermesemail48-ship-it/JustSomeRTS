package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;

public class EssenceCollectorFlags extends ObjectFlags {
   public EssenceCollectorFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.ESSENCE_COLLECTOR;
      this.width = 3;
      this.height = 3;
      String systemName = "essenceCollector";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case ESSENCE_COLLECTOR:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 16);
            break;
         case ESSENCE_COLLECTOR_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 8);
            break;
         case ESSENCE_COLLECTOR_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 40);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 4);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.ESSENCE_COLLECTOR) {
         this.baseRange = 8;
         this.baseDesirability = -1;
         this.canCollectEssence = true;
         this.canDeliverEssence = true;
         this.canReleaseEssenceToGod = true;
         this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
         this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
      }

      switch (this.currentType) {
         case ESSENCE_COLLECTOR_UPGRADE_1:
            this.energyMax = 400;
            break;
         case ESSENCE_COLLECTOR_UPGRADE_2:
            this.energyMax = 800;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 8;
      this.baseDesirability = -1;
      this.canCollectEssence = true;
      this.canDeliverEssence = true;
      this.canReleaseEssenceToGod = true;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
      this.hasFillArt = true;
      switch (this.currentType) {
         case ESSENCE_COLLECTOR:
            this.energyMax = 400;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ESSENCE_COLLECTOR_UPGRADE_1;
            break;
         case ESSENCE_COLLECTOR_UPGRADE_1:
            this.energyMax = 800;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ESSENCE_COLLECTOR_UPGRADE_2;
            break;
         case ESSENCE_COLLECTOR_UPGRADE_2:
            this.energyMax = 1200;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.ESSENCE_COLLECTOR_CAPACITY);
   }
}
