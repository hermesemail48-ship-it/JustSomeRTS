package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class DoggoHouseFlags extends ObjectFlags {
   public DoggoHouseFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.DOGGO_HOUSE;
      this.width = 3;
      this.height = 3;
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.BOTTLER});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.FARM});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.KITCHEN});
      this.recommendedSupportBuildings
         .add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.RAIN_CATCHER, MapTilesLoader.TileSet.WATER_PURIFIER, MapTilesLoader.TileSet.WELL});
      String systemName = "doggoHouse";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case DOGGO_HOUSE:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 12);
            break;
         case DOGGO_HOUSE_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 8);
            break;
         case DOGGO_HOUSE_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 36);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.DOGGO_HOUSE) {
         this.occupantsTypes.clear();
         this.occupantsTypes.add(MobBase.MobType.DOGGO);
         this.occupantsTypes.add(MobBase.MobType.DOOFY_DOGGO);
         this.baseRange = 16;
         this.baseDesirability = 3;
         this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.RATION, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_MEAT, ObjectBase.ResourceStorePriority.HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.COOKED_MEAT, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.EGG, ObjectBase.ResourceStorePriority.HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BOILED_EGG, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BOTTLE, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
         this.canBuildingStore.add(ResourceModule.ResourceType.RATION);
         this.canBuildingStore.add(ResourceModule.ResourceType.RAW_MEAT);
         this.canBuildingStore.add(ResourceModule.ResourceType.COOKED_MEAT);
         this.canBuildingStore.add(ResourceModule.ResourceType.EGG);
         this.canBuildingStore.add(ResourceModule.ResourceType.BOILED_EGG);
         this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
         this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BOTTLE);
         this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, this.occupantsMax);
         this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, this.occupantsMax * 2);
         this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, this.occupantsMax);
         this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, this.occupantsMax * 2);
         this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, this.occupantsMax);
         this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, this.occupantsMax * 2);
         this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, this.occupantsMax);
         this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, this.occupantsMax * 2);
      }

      switch (this.currentType) {
         case DOGGO_HOUSE_UPGRADE_1:
            this.occupantsMax = 6;
            this.occupantsSleepBonusPercentOff = 50;
            this.occupantsHealthRegenBonusTicks = 2;
            this.nextUpgradeType = MapTilesLoader.TileSet.DOGGO_HOUSE_UPGRADE_1;
            break;
         case DOGGO_HOUSE_UPGRADE_2:
            this.occupantsMax = 12;
            this.occupantsSleepBonusPercentOff = 65;
            this.occupantsHealthRegenBonusTicks = 3;
            this.nextUpgradeType = MapTilesLoader.TileSet.DOGGO_HOUSE_UPGRADE_2;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.occupantsTypes.clear();
      this.occupantsTypes.add(MobBase.MobType.DOGGO);
      this.occupantsTypes.add(MobBase.MobType.DOOFY_DOGGO);
      this.baseRange = 16;
      this.baseDesirability = 3;
      this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.RATION, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_MEAT, ObjectBase.ResourceStorePriority.HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.COOKED_MEAT, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.EGG, ObjectBase.ResourceStorePriority.HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BOILED_EGG, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BOTTLE, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canBuildingStore.add(ResourceModule.ResourceType.RATION);
      this.canBuildingStore.add(ResourceModule.ResourceType.RAW_MEAT);
      this.canBuildingStore.add(ResourceModule.ResourceType.COOKED_MEAT);
      this.canBuildingStore.add(ResourceModule.ResourceType.EGG);
      this.canBuildingStore.add(ResourceModule.ResourceType.BOILED_EGG);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BOTTLE);
      switch (this.currentType) {
         case DOGGO_HOUSE:
            this.occupantsMax = 6;
            this.occupantsSleepBonusPercentOff = 50;
            this.occupantsHealthRegenBonusTicks = 2;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.DOGGO_HOUSE_UPGRADE_1;
            break;
         case DOGGO_HOUSE_UPGRADE_1:
            this.occupantsMax = 12;
            this.occupantsSleepBonusPercentOff = 65;
            this.occupantsHealthRegenBonusTicks = 3;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.DOGGO_HOUSE_UPGRADE_2;
            break;
         case DOGGO_HOUSE_UPGRADE_2:
            this.occupantsMax = 18;
            this.occupantsSleepBonusPercentOff = 80;
            this.occupantsHealthRegenBonusTicks = 4;
      }

      this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, this.occupantsMax);
      this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, this.occupantsMax * 2);
      this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, this.occupantsMax);
      this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, this.occupantsMax * 2);
      this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, this.occupantsMax);
      this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, this.occupantsMax * 2);
      this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, this.occupantsMax);
      this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, this.occupantsMax * 2);
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_HOUSING);
   }
}
