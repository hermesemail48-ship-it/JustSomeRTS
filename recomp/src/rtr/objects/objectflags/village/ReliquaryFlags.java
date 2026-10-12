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

public class ReliquaryFlags extends ObjectFlags {
   public ReliquaryFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.RELIQUARY;
      this.width = 9;
      this.height = 11;
      String systemName = "reliquary";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case RELIQUARY:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 16);
            break;
         case RELIQUARY_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 8);
            break;
         case RELIQUARY_UPGRADE_2:
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
      if (this.currentType != MapTilesLoader.TileSet.RELIQUARY) {
         this.baseRange = 12;
         this.baseDesirability = 2;
         this.workerJobType = MobJobBase.MobJobType.OCCULTIST;
         this.canDeliverEssenceToCollectors = true;
         this.canReleaseEssenceToGod = true;
         this.essenceReturnCoordinates = OrderedPair.getOrderedPair(8, 2);
         this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(0, 0);
         this.buildingStorePriority.put(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_INGOT, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_INGOT, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_INGOT);
         this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_INGOT);
         this.resourceStorageMax.put(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL, 4);
         this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 4);
         this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 4);
      }

      switch (this.currentType) {
         case RELIQUARY_UPGRADE_1:
            this.workersProvided = 4;
            this.energyMax = 200;
            break;
         case RELIQUARY_UPGRADE_2:
            this.workersProvided = 4;
            this.prayingSpeedReduction = 2.0;
            this.capturingSoulsSpeedReduction = 2.0;
            this.energyMax = 300;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 12;
      this.baseDesirability = 2;
      this.workerJobType = MobJobBase.MobJobType.OCCULTIST;
      this.canDeliverEssenceToCollectors = true;
      this.canReleaseEssenceToGod = true;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(8, 2);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(0, 0);
      this.buildingStorePriority.put(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_INGOT, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_INGOT, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_INGOT);
      this.resourceStorageMax.put(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL, 4);
      this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 4);
      this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 4);
      this.hasFillArt = true;
      switch (this.currentType) {
         case RELIQUARY:
            this.workersProvided = 2;
            this.energyMax = 200;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.RELIQUARY_UPGRADE_1;
            break;
         case RELIQUARY_UPGRADE_1:
            this.workersProvided = 2;
            this.prayingSpeedReduction = 2.0;
            this.capturingSoulsSpeedReduction = 2.0;
            this.energyMax = 300;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.RELIQUARY_UPGRADE_2;
            break;
         case RELIQUARY_UPGRADE_2:
            this.workersProvided = 2;
            this.prayingSpeedReduction = 4.0;
            this.capturingSoulsSpeedReduction = 4.0;
            this.energyMax = 400;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
