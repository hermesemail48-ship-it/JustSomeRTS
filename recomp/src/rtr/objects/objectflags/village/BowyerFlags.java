package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class BowyerFlags extends ObjectFlags {
   public BowyerFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.BOWYER;
      this.width = 6;
      this.height = 7;
      String systemName = "bowyer";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.LUMBER_SHACK});
      switch (this.currentType) {
         case BOWYER:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 8);
            break;
         case BOWYER_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case BOWYER_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.BOWYER) {
         this.baseRange = 16;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.FLETCHER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.FEATHER, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.SILK, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BOW, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.QUIVER, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
         this.canBuildingStore.add(ResourceModule.ResourceType.FEATHER);
         this.canBuildingStore.add(ResourceModule.ResourceType.SILK);
         this.canBuildingStore.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
         this.canBuildingStore.add(ResourceModule.ResourceType.BOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.QUIVER);
      }

      switch (this.currentType) {
         case BOWYER_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.FEATHER, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SILK, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 8);
            break;
         case BOWYER_UPGRADE_2:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.FEATHER, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SILK, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 12);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BOW, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.QUIVER, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;
      this.workerJobType = MobJobBase.MobJobType.FLETCHER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.FEATHER, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.SILK, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BOW, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.QUIVER, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
      this.canBuildingStore.add(ResourceModule.ResourceType.FEATHER);
      this.canBuildingStore.add(ResourceModule.ResourceType.SILK);
      this.canBuildingStore.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
      this.canBuildingStore.add(ResourceModule.ResourceType.BOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.QUIVER);
      this.hasFillArt = true;
      switch (this.currentType) {
         case BOWYER:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.FEATHER, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SILK, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 8);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.BOWYER_UPGRADE_1;
            break;
         case BOWYER_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.FEATHER, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SILK, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 12);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.BOWYER_UPGRADE_2;
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BOW, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.QUIVER, 10.0);
            break;
         case BOWYER_UPGRADE_2:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.FEATHER, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SILK, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 16);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BOW, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.QUIVER, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
