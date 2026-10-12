package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class TrashCanFlags extends ObjectFlags {
   public TrashCanFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.TRASH_CAN;
      this.width = 3;
      this.height = 3;
      String systemName = "trashCan";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.TRASH_CAN});
      switch (this.currentType) {
         case TRASH_CAN:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 8);
            break;
         case TRASH_CAN_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 4);
            break;
         case TRASH_CAN_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 4);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 4);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.TRASH_CAN) {
         this.baseRange = 6;
         this.baseDesirability = -1;
         this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, ObjectBase.ResourceStorePriority.LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.ORGANICY_TRASH, ObjectBase.ResourceStorePriority.LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.ROCKY_TRASH, ObjectBase.ResourceStorePriority.LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, ObjectBase.ResourceStorePriority.LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_TRASH, ObjectBase.ResourceStorePriority.LOW);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOODY_TRASH, ObjectBase.ResourceStorePriority.LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.ORGANICY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.ROCKY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOODY_TRASH);
         this.trashCan = true;
         this.allowDismantle = true;
         this.allowPause = true;
      }

      switch (this.currentType) {
         case TRASH_CAN_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 16);
            break;
         case TRASH_CAN_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 32);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 6;
      this.baseDesirability = -1;
      this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, ObjectBase.ResourceStorePriority.LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.ORGANICY_TRASH, ObjectBase.ResourceStorePriority.LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.ROCKY_TRASH, ObjectBase.ResourceStorePriority.LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, ObjectBase.ResourceStorePriority.LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_TRASH, ObjectBase.ResourceStorePriority.LOW);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOODY_TRASH, ObjectBase.ResourceStorePriority.LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.ORGANICY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.trashCan = true;
      this.hasFillArt = true;
      switch (this.currentType) {
         case TRASH_CAN:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 16);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.TRASH_CAN_UPGRADE_1;
            break;
         case TRASH_CAN_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 32);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.TRASH_CAN_UPGRADE_2;
            break;
         case TRASH_CAN_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 48);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.IMPROVED_STORAGE);
   }
}
