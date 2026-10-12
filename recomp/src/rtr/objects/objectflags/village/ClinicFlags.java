package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class ClinicFlags extends ObjectFlags {
   public ClinicFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.CLINIC;
      this.width = 6;
      this.height = 8;
      String systemName = "clinic";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.LUMBER_SHACK});
      switch (this.currentType) {
         case CLINIC:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 8);
            break;
         case CLINIC_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case CLINIC_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.CLINIC) {
         this.baseRange = 20;
         this.baseDesirability = 3;
         this.workerJobType = MobJobBase.MobJobType.MEDIC;
         this.buildingStorePriority.put(ResourceModule.ResourceType.SILK, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BANDAGE, ObjectBase.ResourceStorePriority.LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.MEDKIT, ObjectBase.ResourceStorePriority.LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.SILK);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
         this.canBuildingStore.add(ResourceModule.ResourceType.BANDAGE);
         this.canBuildingStore.add(ResourceModule.ResourceType.MEDKIT);
      }

      switch (this.currentType) {
         case CLINIC_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.SILK, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 4);
            break;
         case CLINIC_UPGRADE_2:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.SILK, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 6);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BANDAGE, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.MEDKIT, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 20;
      this.baseDesirability = 3;
      this.workerJobType = MobJobBase.MobJobType.MEDIC;
      this.buildingStorePriority.put(ResourceModule.ResourceType.SILK, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BANDAGE, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.MEDKIT, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.SILK);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
      this.canBuildingStore.add(ResourceModule.ResourceType.BANDAGE);
      this.canBuildingStore.add(ResourceModule.ResourceType.MEDKIT);
      this.hasFillArt = true;
      switch (this.currentType) {
         case CLINIC:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.SILK, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 4);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CLINIC_UPGRADE_1;
            break;
         case CLINIC_UPGRADE_1:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.SILK, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 6);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CLINIC_UPGRADE_2;
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BANDAGE, 10.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.MEDKIT, 10.0);
            break;
         case CLINIC_UPGRADE_2:
            this.workersProvided = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.SILK, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 8);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.BANDAGE, 20.0);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.MEDKIT, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
