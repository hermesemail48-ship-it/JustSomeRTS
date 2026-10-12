package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class KitchenFlags extends ObjectFlags {
   public KitchenFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.KITCHEN;
      this.width = 7;
      this.height = 4;
      String systemName = "kitchen";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings
         .add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.FARM, MapTilesLoader.TileSet.CLUCKER_COOP, MapTilesLoader.TileSet.ANIMAL_PEN});
      switch (this.currentType) {
         case KITCHEN:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 4);
            break;
         case KITCHEN_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 8);
            break;
         case KITCHEN_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 4);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.KITCHEN) {
         this.baseRange = 20;
         this.baseDesirability = 2;
         this.workerJobType = MobJobBase.MobJobType.COOK;
         this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.RATION, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_MEAT, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.COOKED_MEAT, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.EGG, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BOILED_EGG, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
         this.canBuildingStore.add(ResourceModule.ResourceType.RATION);
         this.canBuildingStore.add(ResourceModule.ResourceType.RAW_MEAT);
         this.canBuildingStore.add(ResourceModule.ResourceType.COOKED_MEAT);
         this.canBuildingStore.add(ResourceModule.ResourceType.EGG);
         this.canBuildingStore.add(ResourceModule.ResourceType.BOILED_EGG);
      }

      switch (this.currentType) {
         case KITCHEN_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 8);
            break;
         case KITCHEN_UPGRADE_2:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 12);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.RATION, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.COOKED_MEAT, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BOILED_EGG, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 20;
      this.baseDesirability = 2;
      this.workerJobType = MobJobBase.MobJobType.COOK;
      this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.RATION, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_MEAT, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.COOKED_MEAT, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.EGG, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BOILED_EGG, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canBuildingStore.add(ResourceModule.ResourceType.RATION);
      this.canBuildingStore.add(ResourceModule.ResourceType.RAW_MEAT);
      this.canBuildingStore.add(ResourceModule.ResourceType.COOKED_MEAT);
      this.canBuildingStore.add(ResourceModule.ResourceType.EGG);
      this.canBuildingStore.add(ResourceModule.ResourceType.BOILED_EGG);
      this.hasFillArt = true;
      switch (this.currentType) {
         case KITCHEN:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 8);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.KITCHEN_UPGRADE_1;
            break;
         case KITCHEN_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 12);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.KITCHEN_UPGRADE_2;
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.RATION, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.COOKED_MEAT, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BOILED_EGG, 10.0);
            break;
         case KITCHEN_UPGRADE_2:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RATION, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_MEAT, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.COOKED_MEAT, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.EGG, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOILED_EGG, 16);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.RATION, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.COOKED_MEAT, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BOILED_EGG, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
