package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class MiscellaneousStorageFlags extends ObjectFlags {
   public MiscellaneousStorageFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE;
      this.width = 6;
      this.height = 6;
      String systemName = "miscellaneousStorage";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case MISCELLANEOUS_STORAGE:
            this.name = Text.getText("object.building." + systemName + ".small.name");
            this.description = Text.getText("object.building." + systemName + ".small.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case MISCELLANEOUS_STORAGE_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            break;
         case MISCELLANEOUS_STORAGE_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".large.name");
            this.description = Text.getText("object.building." + systemName + ".large.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            break;
         case MISCELLANEOUS_STORAGE_UPGRADE_3:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 80);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            break;
         case MISCELLANEOUS_STORAGE_UPGRADE_4:
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
      if (this.currentType != MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE) {
         this.baseRange = 16;
         this.baseDesirability = 1;

         ResourceModule.ResourceType[] var4;
         for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
            if (t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH && !t.getTemplate().dontAddMisc()) {
               this.buildingStorePriority.put(t, ObjectBase.ResourceStorePriority.MEDIUM);
               this.canBuildingStore.add(t);
            }
         }
      }

      switch (this.currentType) {
         case MISCELLANEOUS_STORAGE_UPGRADE_1:
            ResourceModule.ResourceType[] var20;
            for (ResourceModule.ResourceType txxxx : ResourceModule.ResourceType.values()) {
               if (!txxxx.getTemplate().dontAddMisc() && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(txxxx, 6);
               }
            }
            break;
         case MISCELLANEOUS_STORAGE_UPGRADE_2:
            ResourceModule.ResourceType[] var19;
            for (ResourceModule.ResourceType txxx : ResourceModule.ResourceType.values()) {
               if (!txxx.getTemplate().dontAddMisc() && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(txxx, 12);
               }
            }
            break;
         case MISCELLANEOUS_STORAGE_UPGRADE_3:
            ResourceModule.ResourceType[] var18;
            for (ResourceModule.ResourceType txx : ResourceModule.ResourceType.values()) {
               if (!txx.getTemplate().dontAddMisc() && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(txx, 18);
               }
            }
            break;
         case MISCELLANEOUS_STORAGE_UPGRADE_4:
            ResourceModule.ResourceType[] var17;
            for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
               if (!tx.getTemplate().dontAddMisc() && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(tx, 24);
               }
            }
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 1;

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH && !t.getTemplate().dontAddMisc()) {
            this.buildingStorePriority.put(t, ObjectBase.ResourceStorePriority.MEDIUM);
            this.canBuildingStore.add(t);
         }
      }

      this.hasFillArt = true;
      switch (this.currentType) {
         case MISCELLANEOUS_STORAGE:
            for (ResourceModule.ResourceType txxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxx.getTemplate().dontAddMisc() && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(txxxxx, 6);
               }
            }

            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE_UPGRADE_1;
            break;
         case MISCELLANEOUS_STORAGE_UPGRADE_1:
            for (ResourceModule.ResourceType txxxx : ResourceModule.ResourceType.values()) {
               if (!txxxx.getTemplate().dontAddMisc() && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(txxxx, 12);
               }
            }

            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE_UPGRADE_2;
            break;
         case MISCELLANEOUS_STORAGE_UPGRADE_2:
            for (ResourceModule.ResourceType txxx : ResourceModule.ResourceType.values()) {
               if (!txxx.getTemplate().dontAddMisc() && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(txxx, 18);
               }
            }

            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE_UPGRADE_3;
            break;
         case MISCELLANEOUS_STORAGE_UPGRADE_3:
            for (ResourceModule.ResourceType txx : ResourceModule.ResourceType.values()) {
               if (!txx.getTemplate().dontAddMisc() && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(txx, 24);
               }
            }

            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE_UPGRADE_4;
            break;
         case MISCELLANEOUS_STORAGE_UPGRADE_4:
            for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
               if (!tx.getTemplate().dontAddMisc() && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(tx, 30);
               }
            }
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.IMPROVED_STORAGE);
   }
}
