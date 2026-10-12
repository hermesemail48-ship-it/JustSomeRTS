package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class HousingFlags extends ObjectFlags {
   public HousingFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.HOUSING;
      this.width = 7;
      this.height = 7;
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.BOTTLER});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.FARM});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.KITCHEN});
      this.recommendedSupportBuildings
         .add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.RAIN_CATCHER, MapTilesLoader.TileSet.WATER_PURIFIER, MapTilesLoader.TileSet.WELL});
      String systemName = "housing";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case HOUSING:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            break;
         case HOUSING_UPGRADE_1_OCCUPANCY:
            this.name = Text.getText("object.building." + systemName + ".baseHighOccupancy.name");
            this.description = Text.getText("object.building." + systemName + ".baseHighOccupancy.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case HOUSING_UPGRADE_2_OCCUPANCY:
            this.name = Text.getText("object.building." + systemName + ".largeHighOccupancy.name");
            this.description = Text.getText("object.building." + systemName + ".largeHighOccupancy.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 96);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            break;
         case HOUSING_UPGRADE_3_OCCUPANCY:
            this.name = Text.getText("object.building." + systemName + ".establishedHighOccupancy.name");
            this.description = Text.getText("object.building." + systemName + ".establishedHighOccupancy.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 128);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            break;
         case HOUSING_UPGRADE_4_OCCUPANCY:
            this.name = Text.getText("object.building." + systemName + ".reinforcedHighOccupancy.name");
            this.description = Text.getText("object.building." + systemName + ".reinforcedHighOccupancy.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 192);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 4);
            break;
         case HOUSING_UPGRADE_1_STANDARD:
            this.name = Text.getText("object.building." + systemName + ".baseStandard.name");
            this.description = Text.getText("object.building." + systemName + ".baseStandard.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case HOUSING_UPGRADE_2_STANDARD:
            this.name = Text.getText("object.building." + systemName + ".largeStandard.name");
            this.description = Text.getText("object.building." + systemName + ".largeStandard.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 96);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            break;
         case HOUSING_UPGRADE_3_STANDARD:
            this.name = Text.getText("object.building." + systemName + ".establishedStandard.name");
            this.description = Text.getText("object.building." + systemName + ".establishedStandard.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 128);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            break;
         case HOUSING_UPGRADE_4_STANDARD:
            this.name = Text.getText("object.building." + systemName + ".reinforcedStandard.name");
            this.description = Text.getText("object.building." + systemName + ".reinforcedStandard.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 192);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 4);
            break;
         case HOUSING_UPGRADE_1_QUALITY:
            this.name = Text.getText("object.building." + systemName + ".baseHighQuality.name");
            this.description = Text.getText("object.building." + systemName + ".baseHighQuality.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case HOUSING_UPGRADE_2_QUALITY:
            this.name = Text.getText("object.building." + systemName + ".largeHighQuality.name");
            this.description = Text.getText("object.building." + systemName + ".largeHighQuality.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 96);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            break;
         case HOUSING_UPGRADE_3_QUALITY:
            this.name = Text.getText("object.building." + systemName + ".establishedHighQuality.name");
            this.description = Text.getText("object.building." + systemName + ".establishedHighQuality.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 128);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            break;
         case HOUSING_UPGRADE_4_QUALITY:
            this.name = Text.getText("object.building." + systemName + ".reinforcedHighQuality.name");
            this.description = Text.getText("object.building." + systemName + ".reinforcedHighQuality.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 192);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 4);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.HOUSING) {
         this.baseRange = 16;
         this.baseDesirability = 3;
         this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.RATION, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.COOKED_MEAT, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.EGG, ObjectBase.ResourceStorePriority.HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BOILED_EGG, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BOTTLE, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
         this.canBuildingStore.add(ResourceModule.ResourceType.RATION);
         this.canBuildingStore.add(ResourceModule.ResourceType.COOKED_MEAT);
         this.canBuildingStore.add(ResourceModule.ResourceType.EGG);
         this.canBuildingStore.add(ResourceModule.ResourceType.BOILED_EGG);
         this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
         this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BOTTLE);
      }

      switch (this.currentType) {
         case HOUSING_UPGRADE_1_OCCUPANCY:
         case HOUSING_UPGRADE_1_STANDARD:
         case HOUSING_UPGRADE_1_QUALITY:
            this.occupantsMax = 4;
            this.occupantsSleepBonusPercentOff = 25;
            this.occupantsHealthRegenBonusTicks = 1;
            break;
         case HOUSING_UPGRADE_2_OCCUPANCY:
            this.occupantsMax = 12;
            this.occupantsSleepBonusPercentOff = 22;
            this.occupantsHealthRegenBonusTicks = 1;
            break;
         case HOUSING_UPGRADE_3_OCCUPANCY:
            this.occupantsMax = 20;
            this.occupantsSleepBonusPercentOff = 19;
            this.occupantsHealthRegenBonusTicks = 1;
            break;
         case HOUSING_UPGRADE_4_OCCUPANCY:
            this.occupantsMax = 28;
            this.occupantsSleepBonusPercentOff = 16;
            this.occupantsHealthRegenBonusTicks = 1;
            break;
         case HOUSING_UPGRADE_2_STANDARD:
            this.occupantsMax = 7;
            this.occupantsSleepBonusPercentOff = 30;
            this.occupantsHealthRegenBonusTicks = 2;
            break;
         case HOUSING_UPGRADE_3_STANDARD:
            this.occupantsMax = 10;
            this.occupantsSleepBonusPercentOff = 35;
            this.occupantsHealthRegenBonusTicks = 3;
            break;
         case HOUSING_UPGRADE_4_STANDARD:
            this.occupantsMax = 13;
            this.occupantsSleepBonusPercentOff = 40;
            this.occupantsHealthRegenBonusTicks = 4;
            this.occupantsFaithBonus = 1;
            break;
         case HOUSING_UPGRADE_2_QUALITY:
            this.occupantsMax = 5;
            this.occupantsSleepBonusPercentOff = 35;
            this.occupantsHealthRegenBonusTicks = 4;
            this.occupantsFaithBonus = 1;
            break;
         case HOUSING_UPGRADE_3_QUALITY:
            this.occupantsMax = 7;
            this.occupantsSleepBonusPercentOff = 45;
            this.occupantsHealthRegenBonusTicks = 6;
            this.occupantsFaithBonus = 2;
            break;
         case HOUSING_UPGRADE_4_QUALITY:
            this.occupantsMax = 9;
            this.occupantsSleepBonusPercentOff = 55;
            this.occupantsHealthRegenBonusTicks = 8;
            this.occupantsFaithBonus = 3;
      }

      this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, this.occupantsMax * 3);
      this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, this.occupantsMax * 3);
      this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, this.occupantsMax * 3);
      this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, this.occupantsMax * 3);
      this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, this.occupantsMax * 3);
      this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, this.occupantsMax * 3);
      this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, this.occupantsMax * 3);
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 3;
      this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.RATION, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.COOKED_MEAT, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.EGG, ObjectBase.ResourceStorePriority.HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BOILED_EGG, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BOTTLE, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canBuildingStore.add(ResourceModule.ResourceType.RATION);
      this.canBuildingStore.add(ResourceModule.ResourceType.COOKED_MEAT);
      this.canBuildingStore.add(ResourceModule.ResourceType.EGG);
      this.canBuildingStore.add(ResourceModule.ResourceType.BOILED_EGG);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BOTTLE);
      this.hasFillArt = true;
      switch (this.currentType) {
         case HOUSING:
            this.occupantsMax = 4;
            this.occupantsSleepBonusPercentOff = 25;
            this.occupantsHealthRegenBonusTicks = 1;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.HOUSING_UPGRADE_1_OCCUPANCY;
            this.nextUpgradeType2 = MapTilesLoader.TileSet.HOUSING_UPGRADE_1_STANDARD;
            this.nextUpgradeType3 = MapTilesLoader.TileSet.HOUSING_UPGRADE_1_QUALITY;
            break;
         case HOUSING_UPGRADE_1_OCCUPANCY:
            this.occupantsMax = 12;
            this.occupantsSleepBonusPercentOff = 22;
            this.occupantsHealthRegenBonusTicks = 1;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.HOUSING_UPGRADE_2_OCCUPANCY;
            break;
         case HOUSING_UPGRADE_2_OCCUPANCY:
            this.occupantsMax = 20;
            this.occupantsSleepBonusPercentOff = 19;
            this.occupantsHealthRegenBonusTicks = 1;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.HOUSING_UPGRADE_3_OCCUPANCY;
            break;
         case HOUSING_UPGRADE_3_OCCUPANCY:
            this.occupantsMax = 28;
            this.occupantsSleepBonusPercentOff = 16;
            this.occupantsHealthRegenBonusTicks = 1;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.HOUSING_UPGRADE_4_OCCUPANCY;
            break;
         case HOUSING_UPGRADE_4_OCCUPANCY:
            this.occupantsMax = 36;
            this.occupantsSleepBonusPercentOff = 13;
            this.occupantsHealthRegenBonusTicks = 1;
            break;
         case HOUSING_UPGRADE_1_STANDARD:
            this.occupantsMax = 7;
            this.occupantsSleepBonusPercentOff = 30;
            this.occupantsHealthRegenBonusTicks = 2;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.HOUSING_UPGRADE_2_STANDARD;
            break;
         case HOUSING_UPGRADE_2_STANDARD:
            this.occupantsMax = 10;
            this.occupantsSleepBonusPercentOff = 35;
            this.occupantsHealthRegenBonusTicks = 3;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.HOUSING_UPGRADE_3_STANDARD;
            break;
         case HOUSING_UPGRADE_3_STANDARD:
            this.occupantsMax = 13;
            this.occupantsSleepBonusPercentOff = 40;
            this.occupantsHealthRegenBonusTicks = 4;
            this.occupantsFaithBonus = 1;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.HOUSING_UPGRADE_4_STANDARD;
            break;
         case HOUSING_UPGRADE_4_STANDARD:
            this.occupantsMax = 16;
            this.occupantsSleepBonusPercentOff = 45;
            this.occupantsHealthRegenBonusTicks = 5;
            this.occupantsFaithBonus = 2;
            break;
         case HOUSING_UPGRADE_1_QUALITY:
            this.occupantsMax = 5;
            this.occupantsSleepBonusPercentOff = 35;
            this.occupantsHealthRegenBonusTicks = 4;
            this.occupantsFaithBonus = 1;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.HOUSING_UPGRADE_2_QUALITY;
            break;
         case HOUSING_UPGRADE_2_QUALITY:
            this.occupantsMax = 7;
            this.occupantsSleepBonusPercentOff = 45;
            this.occupantsHealthRegenBonusTicks = 6;
            this.occupantsFaithBonus = 2;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.HOUSING_UPGRADE_3_QUALITY;
            break;
         case HOUSING_UPGRADE_3_QUALITY:
            this.occupantsMax = 9;
            this.occupantsSleepBonusPercentOff = 55;
            this.occupantsHealthRegenBonusTicks = 8;
            this.occupantsFaithBonus = 3;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.HOUSING_UPGRADE_4_QUALITY;
            break;
         case HOUSING_UPGRADE_4_QUALITY:
            this.occupantsMax = 11;
            this.occupantsSleepBonusPercentOff = 65;
            this.occupantsHealthRegenBonusTicks = 10;
            this.occupantsFaithBonus = 5;
      }

      this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, this.occupantsMax * 3);
      this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, this.occupantsMax * 3);
      this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, this.occupantsMax * 3);
      this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, this.occupantsMax * 3);
      this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, this.occupantsMax * 3);
      this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, this.occupantsMax * 3);
      this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, this.occupantsMax * 3);
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_HOUSING);
   }
}
