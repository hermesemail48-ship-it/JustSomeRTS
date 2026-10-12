package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class KeyShackFlags extends ObjectFlags {
   public KeyShackFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.KEY_SHACK;
      this.width = 4;
      this.height = 4;
      String systemName = "keyShack";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case KEY_SHACK:
            this.name = Text.getText("object.building." + systemName + ".small.name");
            this.description = Text.getText("object.building." + systemName + ".small.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case KEY_SHACK_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            break;
         case KEY_SHACK_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".large.name");
            this.description = Text.getText("object.building." + systemName + ".large.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            break;
         case KEY_SHACK_UPGRADE_3:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 80);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            break;
         case KEY_SHACK_UPGRADE_4:
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
      if (this.currentType != MapTilesLoader.TileSet.KEY_SHACK) {
         this.baseRange = 12;
         this.baseDesirability = 1;
         this.buildingStorePriority.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.SUSPICICIOUS_KEY);
      }

      switch (this.currentType) {
         case KEY_SHACK_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, 4);
            break;
         case KEY_SHACK_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, 6);
            break;
         case KEY_SHACK_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, 8);
            break;
         case KEY_SHACK_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, 10);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 12;
      this.baseDesirability = 1;
      this.buildingStorePriority.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.SUSPICICIOUS_KEY);
      this.hasFillArt = true;
      switch (this.currentType) {
         case KEY_SHACK:
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, 4);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.KEY_SHACK_UPGRADE_1;
            break;
         case KEY_SHACK_UPGRADE_1:
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, 6);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.KEY_SHACK_UPGRADE_2;
            break;
         case KEY_SHACK_UPGRADE_2:
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, 8);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.KEY_SHACK_UPGRADE_3;
            break;
         case KEY_SHACK_UPGRADE_3:
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, 10);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.KEY_SHACK_UPGRADE_4;
            break;
         case KEY_SHACK_UPGRADE_4:
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, 12);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.IMPROVED_STORAGE);
   }
}
