package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class EquipmentStorageFlags extends ObjectFlags {
   public EquipmentStorageFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.EQUIPMENT_STORAGE;
      this.width = 6;
      this.height = 6;
      String systemName = "equipmentStorage";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ARMORSMITHY});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.CLINIC});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.TOOLSMITHY});
      switch (this.currentType) {
         case EQUIPMENT_STORAGE:
            this.name = Text.getText("object.building." + systemName + ".small.name");
            this.description = Text.getText("object.building." + systemName + ".small.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case EQUIPMENT_STORAGE_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            break;
         case EQUIPMENT_STORAGE_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".large.name");
            this.description = Text.getText("object.building." + systemName + ".large.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            break;
         case EQUIPMENT_STORAGE_UPGRADE_3:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 80);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            break;
         case EQUIPMENT_STORAGE_UPGRADE_4:
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
      if (this.currentType != MapTilesLoader.TileSet.EQUIPMENT_STORAGE) {
         this.baseRange = 16;
         this.baseDesirability = 1;
         this.buildingStorePriority.put(ResourceModule.ResourceType.AXE, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BANDAGE, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.HAMMER, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.HOE, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_HELMET, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_HELMET, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.PICK_AXE, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.SHOVEL, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_SWORD, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD_SWORD, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.MEDKIT, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_SHIELD, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_SHIELD, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.BOW, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.QUIVER, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.AXE);
         this.canBuildingStore.add(ResourceModule.ResourceType.BANDAGE);
         this.canBuildingStore.add(ResourceModule.ResourceType.HAMMER);
         this.canBuildingStore.add(ResourceModule.ResourceType.HOE);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_CHEST_ARMOR);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_HELMET);
         this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR);
         this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_HELMET);
         this.canBuildingStore.add(ResourceModule.ResourceType.PICK_AXE);
         this.canBuildingStore.add(ResourceModule.ResourceType.SHOVEL);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_SWORD);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOOD_SWORD);
         this.canBuildingStore.add(ResourceModule.ResourceType.MEDKIT);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_SHIELD);
         this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_SHIELD);
         this.canBuildingStore.add(ResourceModule.ResourceType.BOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.QUIVER);
      }

      switch (this.currentType) {
         case EQUIPMENT_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 10);
            break;
         case EQUIPMENT_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 14);
            break;
         case EQUIPMENT_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 18);
            break;
         case EQUIPMENT_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 22);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;
      this.buildingStorePriority.put(ResourceModule.ResourceType.AXE, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BANDAGE, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.HAMMER, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.HOE, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_HELMET, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_HELMET, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.PICK_AXE, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.SHOVEL, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_SWORD, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD_SWORD, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.MEDKIT, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_SHIELD, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.LEATHER_SHIELD, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.BOW, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.QUIVER, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.AXE);
      this.canBuildingStore.add(ResourceModule.ResourceType.BANDAGE);
      this.canBuildingStore.add(ResourceModule.ResourceType.HAMMER);
      this.canBuildingStore.add(ResourceModule.ResourceType.HOE);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_CHEST_ARMOR);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_HELMET);
      this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR);
      this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_HELMET);
      this.canBuildingStore.add(ResourceModule.ResourceType.PICK_AXE);
      this.canBuildingStore.add(ResourceModule.ResourceType.SHOVEL);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_SWORD);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOOD_SWORD);
      this.canBuildingStore.add(ResourceModule.ResourceType.MEDKIT);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_SHIELD);
      this.canBuildingStore.add(ResourceModule.ResourceType.LEATHER_SHIELD);
      this.canBuildingStore.add(ResourceModule.ResourceType.BOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.QUIVER);
      this.hasFillArt = true;
      switch (this.currentType) {
         case EQUIPMENT_STORAGE:
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 10);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.EQUIPMENT_STORAGE_UPGRADE_1;
            break;
         case EQUIPMENT_STORAGE_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 14);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 14);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.EQUIPMENT_STORAGE_UPGRADE_2;
            break;
         case EQUIPMENT_STORAGE_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 18);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.EQUIPMENT_STORAGE_UPGRADE_3;
            break;
         case EQUIPMENT_STORAGE_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 22);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 22);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.EQUIPMENT_STORAGE_UPGRADE_4;
            break;
         case EQUIPMENT_STORAGE_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.AXE, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BANDAGE, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HAMMER, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.HOE, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_CHEST_ARMOR, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_HELMET, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_HELMET, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.PICK_AXE, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SHOVEL, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SWORD, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD_SWORD, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.MEDKIT, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_SHIELD, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.LEATHER_SHIELD, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.BOW, 26);
            this.resourceStorageMax.put(ResourceModule.ResourceType.QUIVER, 26);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.IMPROVED_STORAGE);
   }
}
