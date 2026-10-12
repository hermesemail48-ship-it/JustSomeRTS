package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class WoodStorageFlags extends ObjectFlags {
   public WoodStorageFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.WOOD_STORAGE;
      this.width = 6;
      this.height = 6;
      String systemName = "woodStorage";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.LUMBER_MILL});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.LUMBER_SHACK});
      switch (this.currentType) {
         case WOOD_STORAGE:
            this.name = Text.getText("object.building." + systemName + ".small.name");
            this.description = Text.getText("object.building." + systemName + ".small.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case WOOD_STORAGE_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            break;
         case WOOD_STORAGE_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".large.name");
            this.description = Text.getText("object.building." + systemName + ".large.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            break;
         case WOOD_STORAGE_UPGRADE_3:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 80);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            break;
         case WOOD_STORAGE_UPGRADE_4:
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
      if (this.currentType != MapTilesLoader.TileSet.WOOD_STORAGE) {
         this.baseRange = 16;
         this.baseDesirability = 1;
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BOARD, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
         this.canBuildingStore.add(ResourceModule.ResourceType.BOARD);
      }

      switch (this.currentType) {
         case WOOD_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 32);
            break;
         case WOOD_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 48);
            break;
         case WOOD_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 64);
            break;
         case WOOD_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 80);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 80);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BOARD, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
      this.canBuildingStore.add(ResourceModule.ResourceType.BOARD);
      this.hasFillArt = true;
      switch (this.currentType) {
         case WOOD_STORAGE:
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 32);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.WOOD_STORAGE_UPGRADE_1;
            break;
         case WOOD_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 48);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.WOOD_STORAGE_UPGRADE_2;
            break;
         case WOOD_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 64);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.WOOD_STORAGE_UPGRADE_3;
            break;
         case WOOD_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 80);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 80);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.WOOD_STORAGE_UPGRADE_4;
            break;
         case WOOD_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 96);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOARD, 96);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.IMPROVED_STORAGE);
   }
}
