package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class AmmoStorageFlags extends ObjectFlags {
   public AmmoStorageFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.AMMO_STORAGE;
      this.width = 6;
      this.height = 6;
      String systemName = "ammoStorage";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.BOWYER});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ROCK_TUMBLER});
      switch (this.currentType) {
         case AMMO_STORAGE:
            this.name = Text.getText("object.building." + systemName + ".small.name");
            this.description = Text.getText("object.building." + systemName + ".small.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case AMMO_STORAGE_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            break;
         case AMMO_STORAGE_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".large.name");
            this.description = Text.getText("object.building." + systemName + ".large.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            break;
         case AMMO_STORAGE_UPGRADE_3:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 80);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            break;
         case AMMO_STORAGE_UPGRADE_4:
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
      if (this.currentType != MapTilesLoader.TileSet.AMMO_STORAGE) {
         this.baseRange = 16;
         this.baseDesirability = 1;
         this.buildingStorePriority.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.STONE_BALLS, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.QUIVER, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
         this.canBuildingStore.add(ResourceModule.ResourceType.STONE_BALLS);
         this.canBuildingStore.add(ResourceModule.ResourceType.QUIVER);
      }

      switch (this.currentType) {
         case AMMO_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 24);
            break;
         case AMMO_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 32);
            break;
         case AMMO_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 48);
            break;
         case AMMO_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 64);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 64);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 64);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;
      this.buildingStorePriority.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.STONE_BALLS, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.QUIVER, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.BALLISTAE_BOLTS);
      this.canBuildingStore.add(ResourceModule.ResourceType.STONE_BALLS);
      this.canBuildingStore.add(ResourceModule.ResourceType.QUIVER);
      this.hasFillArt = true;
      switch (this.currentType) {
         case AMMO_STORAGE:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 24);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.AMMO_STORAGE_UPGRADE_1;
            break;
         case AMMO_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 32);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.AMMO_STORAGE_UPGRADE_2;
            break;
         case AMMO_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 48);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.AMMO_STORAGE_UPGRADE_3;
            break;
         case AMMO_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 64);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 64);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 64);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.AMMO_STORAGE_UPGRADE_4;
            break;
         case AMMO_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.BALLISTAE_BOLTS, 80);
            this.resourceStorageMax.put(ResourceModule.ResourceType.STONE_BALLS, 80);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 80);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.IMPROVED_STORAGE);
   }
}
