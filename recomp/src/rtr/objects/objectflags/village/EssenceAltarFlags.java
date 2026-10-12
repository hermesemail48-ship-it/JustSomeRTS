package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;

public class EssenceAltarFlags extends ObjectFlags {
   public EssenceAltarFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.ESSENCE_ALTAR;
      this.width = 5;
      this.height = 5;
      String systemName = "essenceAltar";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case ESSENCE_ALTAR:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 16);
            break;
         case ESSENCE_ALTAR_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 8);
            break;
         case ESSENCE_ALTAR_UPGRADE_2:
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
      if (this.currentType != MapTilesLoader.TileSet.ESSENCE_ALTAR) {
         this.baseRange = 6;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.OCCULTIST;
         this.canDeliverEssenceToCollectors = true;
         this.canReleaseEssenceToGod = true;
         this.essenceReturnCoordinates = OrderedPair.getOrderedPair(2, 2);
         this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
      }

      switch (this.currentType) {
         case ESSENCE_ALTAR_UPGRADE_1:
            this.workersProvided = 4;
            this.energyMax = 200;
            break;
         case ESSENCE_ALTAR_UPGRADE_2:
            this.workersProvided = 4;
            this.prayingSpeedReduction = 2.0;
            this.energyMax = 300;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 6;
      this.baseDesirability = 3;
      this.workerJobType = MobJobBase.MobJobType.OCCULTIST;
      this.canDeliverEssenceToCollectors = true;
      this.canReleaseEssenceToGod = true;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(2, 2);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
      this.hasFillArt = true;
      switch (this.currentType) {
         case ESSENCE_ALTAR:
            this.workersProvided = 4;
            this.energyMax = 200;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ESSENCE_ALTAR_UPGRADE_1;
            break;
         case ESSENCE_ALTAR_UPGRADE_1:
            this.workersProvided = 4;
            this.prayingSpeedReduction = 2.0;
            this.energyMax = 300;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ESSENCE_ALTAR_UPGRADE_2;
            break;
         case ESSENCE_ALTAR_UPGRADE_2:
            this.workersProvided = 4;
            this.prayingSpeedReduction = 4.0;
            this.energyMax = 400;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
