package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class RockStorageFlags extends ObjectFlags {
   public RockStorageFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.ROCK_STORAGE;
      this.width = 6;
      this.height = 6;
      String systemName = "rockStorage";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.MINING_FACILITY});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.STONE_CUTTERY});
      switch (this.currentType) {
         case ROCK_STORAGE:
            this.name = Text.getText("object.building." + systemName + ".small.name");
            this.description = Text.getText("object.building." + systemName + ".small.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case ROCK_STORAGE_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            break;
         case ROCK_STORAGE_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".large.name");
            this.description = Text.getText("object.building." + systemName + ".large.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            break;
         case ROCK_STORAGE_UPGRADE_3:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 80);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            break;
         case ROCK_STORAGE_UPGRADE_4:
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
      if (this.currentType != MapTilesLoader.TileSet.ROCK_STORAGE) {
         this.baseRange = 16;
         this.baseDesirability = 1;
         this.buildingStorePriority.put(ResourceModule.ResourceType.ROCK, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.CUT_STONE, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.ROCK);
         this.canBuildingStore.add(ResourceModule.ResourceType.CUT_STONE);
      }

      switch (this.currentType) {
         case ROCK_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 32);
            break;
         case ROCK_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 48);
            break;
         case ROCK_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 64);
            break;
         case ROCK_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 80);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 80);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;
      this.buildingStorePriority.put(ResourceModule.ResourceType.ROCK, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.CUT_STONE, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.ROCK);
      this.canBuildingStore.add(ResourceModule.ResourceType.CUT_STONE);
      this.hasFillArt = true;
      switch (this.currentType) {
         case ROCK_STORAGE:
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 32);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ROCK_STORAGE_UPGRADE_1;
            break;
         case ROCK_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 48);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ROCK_STORAGE_UPGRADE_2;
            break;
         case ROCK_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 64);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ROCK_STORAGE_UPGRADE_3;
            break;
         case ROCK_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 80);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 80);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ROCK_STORAGE_UPGRADE_4;
            break;
         case ROCK_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 96);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CUT_STONE, 96);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.IMPROVED_STORAGE);
   }
}
