package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class CrystalStorageFlags extends ObjectFlags {
   public CrystalStorageFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.CRYSTAL_STORAGE;
      this.width = 6;
      this.height = 6;
      String systemName = "crystalStorage";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.CRYSTAL_HARVESTRY});
      switch (this.currentType) {
         case CRYSTAL_STORAGE:
            this.name = Text.getText("object.building." + systemName + ".small.name");
            this.description = Text.getText("object.building." + systemName + ".small.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case CRYSTAL_STORAGE_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            break;
         case CRYSTAL_STORAGE_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".large.name");
            this.description = Text.getText("object.building." + systemName + ".large.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            break;
         case CRYSTAL_STORAGE_UPGRADE_3:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 80);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            break;
         case CRYSTAL_STORAGE_UPGRADE_4:
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
      if (this.currentType != MapTilesLoader.TileSet.CRYSTAL_STORAGE) {
         this.baseRange = 16;
         this.baseDesirability = 1;
         this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTAL, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.CRYLITHIUM, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTAL);
         this.canBuildingStore.add(ResourceModule.ResourceType.CRYLITHIUM);
      }

      switch (this.currentType) {
         case CRYSTAL_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 16);
            break;
         case CRYSTAL_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 72);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 24);
            break;
         case CRYSTAL_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 96);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 32);
            break;
         case CRYSTAL_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 120);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 40);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;
      this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTAL, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.CRYLITHIUM, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTAL);
      this.canBuildingStore.add(ResourceModule.ResourceType.CRYLITHIUM);
      this.hasFillArt = true;
      switch (this.currentType) {
         case CRYSTAL_STORAGE:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 16);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CRYSTAL_STORAGE_UPGRADE_1;
            break;
         case CRYSTAL_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 72);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 24);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CRYSTAL_STORAGE_UPGRADE_2;
            break;
         case CRYSTAL_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 96);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 32);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CRYSTAL_STORAGE_UPGRADE_3;
            break;
         case CRYSTAL_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 120);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 40);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CRYSTAL_STORAGE_UPGRADE_4;
            break;
         case CRYSTAL_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 144);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 48);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.IMPROVED_STORAGE);
   }
}
