package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class ProcessorFlags extends ObjectFlags {
   public ProcessorFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.PROCESSOR;
      this.width = 5;
      this.height = 5;
      String systemName = "processor";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.TRASH_CAN, MapTilesLoader.TileSet.LANDFILL});
      switch (this.currentType) {
         case PROCESSOR:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            break;
         case PROCESSOR_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
            break;
         case PROCESSOR_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 128);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.PROCESSOR) {
         this.workerJobType = MobJobBase.MobJobType.TRASHER;
         this.baseRange = 24;
         this.baseDesirability = -3;
         this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.ORGANICY_TRASH, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.ROCKY_TRASH, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_CUBE, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_TRASH, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOODY_TRASH, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.ORGANICY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.ROCKY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_CUBE);
         this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOODY_TRASH);
         this.isProcessor = true;
      }

      switch (this.currentType) {
         case PROCESSOR_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 8);
            break;
         case PROCESSOR_UPGRADE_2:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 12);
            this.processingTrashSpeedReduction = 10.0;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.workerJobType = MobJobBase.MobJobType.TRASHER;
      this.baseRange = 24;
      this.baseDesirability = -3;
      this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.ORGANICY_TRASH, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.ROCKY_TRASH, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_CUBE, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_TRASH, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOODY_TRASH, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.ORGANICY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_CUBE);
      this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.isProcessor = true;
      this.hasFillArt = true;
      switch (this.currentType) {
         case PROCESSOR:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 8);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 8);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.PROCESSOR_UPGRADE_1;
            break;
         case PROCESSOR_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 12);
            this.processingTrashSpeedReduction = 10.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.PROCESSOR_UPGRADE_2;
            break;
         case PROCESSOR_UPGRADE_2:
            this.workersProvided = 4;
            this.processingTrashSpeedReduction = 20.0;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 16);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
