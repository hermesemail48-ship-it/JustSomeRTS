package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class GoldStorageFlags extends ObjectFlags {
   public GoldStorageFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.GOLD_STORAGE;
      this.width = 6;
      this.height = 6;
      String systemName = "goldStorage";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.FORGE});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.TOOLSMITHY});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ARMORSMITHY});
      switch (this.currentType) {
         case GOLD_STORAGE:
            this.name = Text.getText("object.building." + systemName + ".small.name");
            this.description = Text.getText("object.building." + systemName + ".small.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case GOLD_STORAGE_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            break;
         case GOLD_STORAGE_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".large.name");
            this.description = Text.getText("object.building." + systemName + ".large.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            break;
         case GOLD_STORAGE_UPGRADE_3:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 80);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            break;
         case GOLD_STORAGE_UPGRADE_4:
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
      if (this.currentType != MapTilesLoader.TileSet.GOLD_STORAGE) {
         this.baseRange = 16;
         this.baseDesirability = 1;
         this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_INGOT, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_COIN_SACK, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_INGOT);
         this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_COIN_SACK);
      }

      switch (this.currentType) {
         case GOLD_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 32);
            break;
         case GOLD_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 48);
            break;
         case GOLD_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 64);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 64);
            break;
         case GOLD_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 80);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 80);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;
      this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_INGOT, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_COIN_SACK, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_INGOT);
      this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_COIN_SACK);
      this.hasFillArt = true;
      switch (this.currentType) {
         case GOLD_STORAGE:
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 48);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.GOLD_STORAGE_UPGRADE_1;
            break;
         case GOLD_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 72);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.GOLD_STORAGE_UPGRADE_2;
            break;
         case GOLD_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 96);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.GOLD_STORAGE_UPGRADE_3;
            break;
         case GOLD_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 40);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 120);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.GOLD_STORAGE_UPGRADE_4;
            break;
         case GOLD_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_INGOT, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 144);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.IMPROVED_STORAGE);
   }
}
