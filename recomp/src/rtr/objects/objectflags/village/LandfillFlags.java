package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class LandfillFlags extends ObjectFlags {
   public LandfillFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.LANDFILL;
      this.width = 7;
      this.height = 7;
      String systemName = "landfill";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.TRASH_CAN});
      switch (this.currentType) {
         case LANDFILL:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case LANDFILL_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            break;
         case LANDFILL_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 96);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.LANDFILL) {
         this.workerJobType = MobJobBase.MobJobType.TRASHER;
         this.baseRange = 24;
         this.baseDesirability = -3;
         this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.ORGANICY_TRASH, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.ROCKY_TRASH, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_CUBE, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_TRASH, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOODY_TRASH, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.ORGANICY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.ROCKY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_CUBE);
         this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOODY_TRASH);
         this.allowDismantle = true;
         this.allowPause = true;
      }

      switch (this.currentType) {
         case LANDFILL_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 128);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 128);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 128);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 128);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 128);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 128);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 128);
            break;
         case LANDFILL_UPGRADE_2:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 256);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 256);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 256);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 256);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 256);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 256);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 256);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.workerJobType = MobJobBase.MobJobType.TRASHER;
      this.baseRange = 24;
      this.baseDesirability = -3;
      this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.ORGANICY_TRASH, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.ROCKY_TRASH, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_CUBE, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_TRASH, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOODY_TRASH, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.ORGANICY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_CUBE);
      this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.hasFillArt = true;
      switch (this.currentType) {
         case LANDFILL:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 128);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 128);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 128);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 128);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 128);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 128);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 128);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.LANDFILL_UPGRADE_1;
            break;
         case LANDFILL_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 256);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 256);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 256);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 256);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 256);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 256);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 256);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.LANDFILL_UPGRADE_2;
            break;
         case LANDFILL_UPGRADE_2:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 512);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 512);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 512);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 512);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 512);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 512);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 512);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.IMPROVED_STORAGE);
   }
}
