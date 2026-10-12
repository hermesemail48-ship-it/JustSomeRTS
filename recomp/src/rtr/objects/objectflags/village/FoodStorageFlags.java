package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class FoodStorageFlags extends ObjectFlags {
   public FoodStorageFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.FOOD_STORAGE;
      this.width = 6;
      this.height = 6;
      String systemName = "foodStorage";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.FARM});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.KITCHEN});
      switch (this.currentType) {
         case FOOD_STORAGE:
            this.name = Text.getText("object.building." + systemName + ".small.name");
            this.description = Text.getText("object.building." + systemName + ".small.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case FOOD_STORAGE_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            break;
         case FOOD_STORAGE_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".large.name");
            this.description = Text.getText("object.building." + systemName + ".large.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            break;
         case FOOD_STORAGE_UPGRADE_3:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 80);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            break;
         case FOOD_STORAGE_UPGRADE_4:
            this.name = Text.getText("object.building." + systemName + ".reinforced.name");
            this.description = Text.getText("object.building." + systemName + ".reinforced.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 96);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 8);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.FOOD_STORAGE) {
         this.baseRange = 16;
         this.baseDesirability = 1;
         this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.RATION, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_MEAT, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.COOKED_MEAT, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.EGG, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BOILED_EGG, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BOTTLE, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
         this.canBuildingStore.add(ResourceModule.ResourceType.RATION);
         this.canBuildingStore.add(ResourceModule.ResourceType.RAW_MEAT);
         this.canBuildingStore.add(ResourceModule.ResourceType.COOKED_MEAT);
         this.canBuildingStore.add(ResourceModule.ResourceType.EGG);
         this.canBuildingStore.add(ResourceModule.ResourceType.BOILED_EGG);
         this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BOTTLE);
      }

      switch (this.currentType) {
         case FOOD_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 16);
            break;
         case FOOD_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 24);
            break;
         case FOOD_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 32);
            break;
         case FOOD_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 40);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;
      this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.RATION, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_MEAT, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.COOKED_MEAT, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.EGG, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BOILED_EGG, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BOTTLE, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canBuildingStore.add(ResourceModule.ResourceType.RATION);
      this.canBuildingStore.add(ResourceModule.ResourceType.RAW_MEAT);
      this.canBuildingStore.add(ResourceModule.ResourceType.COOKED_MEAT);
      this.canBuildingStore.add(ResourceModule.ResourceType.EGG);
      this.canBuildingStore.add(ResourceModule.ResourceType.BOILED_EGG);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BOTTLE);
      this.hasFillArt = true;
      switch (this.currentType) {
         case FOOD_STORAGE:
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 16);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.FOOD_STORAGE_UPGRADE_1;
            break;
         case FOOD_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 24);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.FOOD_STORAGE_UPGRADE_2;
            break;
         case FOOD_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 32);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.FOOD_STORAGE_UPGRADE_3;
            break;
         case FOOD_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 40);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.FOOD_STORAGE_UPGRADE_4;
            break;
         case FOOD_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BOTTLE, 48);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.IMPROVED_STORAGE);
   }
}
