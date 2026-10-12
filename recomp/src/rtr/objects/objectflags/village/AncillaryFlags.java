package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class AncillaryFlags extends ObjectFlags {
   public AncillaryFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.ANCILLARY;
      this.width = 9;
      this.height = 7;
      String systemName = "ancillary";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case ANCILLARY:
            this.name = Text.getText("object.building." + systemName + ".small.name");
            this.description = Text.getText("object.building." + systemName + ".small.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 8);
            break;
         case ANCILLARY_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            break;
         case ANCILLARY_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".large.name");
            this.description = Text.getText("object.building." + systemName + ".large.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            break;
         case ANCILLARY_UPGRADE_3:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 72);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 32);
            break;
         case ANCILLARY_UPGRADE_4:
            this.name = Text.getText("object.building." + systemName + ".reinforced.name");
            this.description = Text.getText("object.building." + systemName + ".reinforced.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 72);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.ANCILLARY) {
         this.baseDesirability = 2;
         this.workerJobType = MobJobBase.MobJobType.ORGANIZER;

         ResourceModule.ResourceType[] var4;
         for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
            if (t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
               && t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
               && t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
               && t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
               && t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO
               && !t.getTemplate().dontAddMisc()) {
               this.buildingStorePriority.put(t, ObjectBase.ResourceStorePriority.LOW);
               this.canBuildingStore.add(t);
            }
         }
      }

      switch (this.currentType) {
         case ANCILLARY_UPGRADE_1:
            this.workersProvided = 2;
            this.buildingsProvided = 6;
            this.baseRange = 20;

            ResourceModule.ResourceType[] var20;
            for (ResourceModule.ResourceType txxxx : ResourceModule.ResourceType.values()) {
               if (!txxxx.getTemplate().dontAddMisc()
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxx, 6);
               }
            }

            this.globalWorkSpeedReduction = 1.0;
            this.buildingSpeedReduction = 1.0;
            break;
         case ANCILLARY_UPGRADE_2:
            this.workersProvided = 3;
            this.buildingsProvided = 8;
            this.baseRange = 22;

            ResourceModule.ResourceType[] var19;
            for (ResourceModule.ResourceType txxx : ResourceModule.ResourceType.values()) {
               if (!txxx.getTemplate().dontAddMisc()
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxx, 8);
               }
            }

            this.globalWorkSpeedReduction = 1.0;
            this.buildingSpeedReduction = 1.0;
            break;
         case ANCILLARY_UPGRADE_3:
            this.workersProvided = 4;
            this.buildingsProvided = 10;
            this.baseRange = 24;

            ResourceModule.ResourceType[] var18;
            for (ResourceModule.ResourceType txx : ResourceModule.ResourceType.values()) {
               if (!txx.getTemplate().dontAddMisc()
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txx, 10);
               }
            }

            this.globalWorkSpeedReduction = 1.0;
            this.buildingSpeedReduction = 1.0;
            break;
         case ANCILLARY_UPGRADE_4:
            this.workersProvided = 5;
            this.buildingsProvided = 12;
            this.baseRange = 26;

            ResourceModule.ResourceType[] var17;
            for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
               if (!tx.getTemplate().dontAddMisc()
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(tx, 12);
               }
            }

            this.globalWorkSpeedReduction = 1.0;
            this.buildingSpeedReduction = 1.0;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseDesirability = 2;
      this.workerJobType = MobJobBase.MobJobType.ORGANIZER;

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
            && t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
            && t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
            && t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
            && t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO
            && !t.getTemplate().dontAddMisc()) {
            this.buildingStorePriority.put(t, ObjectBase.ResourceStorePriority.LOW);
            this.canBuildingStore.add(t);
         }
      }

      this.hasFillArt = true;
      switch (this.currentType) {
         case ANCILLARY:
            this.workersProvided = 2;
            this.buildingsProvided = 6;
            this.baseRange = 20;

            for (ResourceModule.ResourceType txxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxx.getTemplate().dontAddMisc()
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxx, 6);
               }
            }

            this.globalWorkSpeedReduction = 1.0;
            this.buildingSpeedReduction = 1.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ANCILLARY_UPGRADE_1;
            break;
         case ANCILLARY_UPGRADE_1:
            this.workersProvided = 3;
            this.buildingsProvided = 8;
            this.baseRange = 22;

            for (ResourceModule.ResourceType txxxx : ResourceModule.ResourceType.values()) {
               if (!txxxx.getTemplate().dontAddMisc()
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxx, 8);
               }
            }

            this.globalWorkSpeedReduction = 1.0;
            this.buildingSpeedReduction = 1.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ANCILLARY_UPGRADE_2;
            break;
         case ANCILLARY_UPGRADE_2:
            this.workersProvided = 4;
            this.buildingsProvided = 10;
            this.baseRange = 24;

            for (ResourceModule.ResourceType txxx : ResourceModule.ResourceType.values()) {
               if (!txxx.getTemplate().dontAddMisc()
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxx, 10);
               }
            }

            this.globalWorkSpeedReduction = 1.0;
            this.buildingSpeedReduction = 1.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ANCILLARY_UPGRADE_3;
            break;
         case ANCILLARY_UPGRADE_3:
            this.workersProvided = 5;
            this.buildingsProvided = 12;
            this.baseRange = 26;

            for (ResourceModule.ResourceType txx : ResourceModule.ResourceType.values()) {
               if (!txx.getTemplate().dontAddMisc()
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txx, 12);
               }
            }

            this.globalWorkSpeedReduction = 1.0;
            this.buildingSpeedReduction = 1.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ANCILLARY_UPGRADE_4;
            break;
         case ANCILLARY_UPGRADE_4:
            this.workersProvided = 6;
            this.buildingsProvided = 14;
            this.baseRange = 28;

            for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
               if (!tx.getTemplate().dontAddMisc()
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(tx, 16);
               }
            }

            this.globalWorkSpeedReduction = 2.0;
            this.buildingSpeedReduction = 2.0;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.IMPROVED_STORAGE);
   }
}
