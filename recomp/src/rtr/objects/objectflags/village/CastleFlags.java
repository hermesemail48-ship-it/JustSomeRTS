package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class CastleFlags extends ObjectFlags {
   public CastleFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.CASTLE_1;
      this.width = 13;
      this.height = 13;
      String systemName = "castle";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case CASTLE_1:
            this.name = Text.getText("object.building." + systemName + ".camp.name");
            this.description = Text.getText("object.building." + systemName + ".camp.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 6);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 6);
            break;
         case CASTLE_2:
            this.name = Text.getText("object.building." + systemName + ".largeCamp.name");
            this.description = Text.getText("object.building." + systemName + ".largeCamp.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 12);
            break;
         case CASTLE_3:
            this.name = Text.getText("object.building." + systemName + ".smallSettlement.name");
            this.description = Text.getText("object.building." + systemName + ".smallSettlement.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            break;
         case CASTLE_4:
            this.name = Text.getText("object.building." + systemName + ".largeSettlement.name");
            this.description = Text.getText("object.building." + systemName + ".largeSettlement.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 42);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 42);
            break;
         case CASTLE_5:
            this.name = Text.getText("object.building." + systemName + ".villageCenter.name");
            this.description = Text.getText("object.building." + systemName + ".villageCenter.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 50);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 50);
            break;
         case CASTLE_6:
            this.name = Text.getText("object.building." + systemName + ".largeVillageCenter.name");
            this.description = Text.getText("object.building." + systemName + ".largeVillageCenter.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 64);
            break;
         case CASTLE_7:
            this.name = Text.getText("object.building." + systemName + ".establishedVillageCenter.name");
            this.description = Text.getText("object.building." + systemName + ".establishedVillageCenter.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 72);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 72);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 8);
            break;
         case CASTLE_8:
            this.name = Text.getText("object.building." + systemName + ".smallKeep.name");
            this.description = Text.getText("object.building." + systemName + ".smallKeep.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 90);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 90);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            break;
         case CASTLE_9:
            this.name = Text.getText("object.building." + systemName + ".largeKeep.name");
            this.description = Text.getText("object.building." + systemName + ".largeKeep.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 128);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 40);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 128);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            break;
         case CASTLE_10:
            this.name = Text.getText("object.building." + systemName + ".establishedKeep.name");
            this.description = Text.getText("object.building." + systemName + ".establishedKeep.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 128);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 192);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
            break;
         case CASTLE_11:
            this.name = Text.getText("object.building." + systemName + ".smallStronghold.name");
            this.description = Text.getText("object.building." + systemName + ".smallStronghold.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 128);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 256);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 42);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 16);
            break;
         case CASTLE_12:
            this.name = Text.getText("object.building." + systemName + ".largeStronghold.name");
            this.description = Text.getText("object.building." + systemName + ".largeStronghold.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 192);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 384);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 72);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 24);
            break;
         case CASTLE_13:
            this.name = Text.getText("object.building." + systemName + ".establishedStronghold.name");
            this.description = Text.getText("object.building." + systemName + ".establishedStronghold.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 256);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 72);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 512);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 128);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 32);
            break;
         case CASTLE_14:
            this.name = Text.getText("object.building." + systemName + ".smallCastle.name");
            this.description = Text.getText("object.building." + systemName + ".smallCastle.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 256);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 128);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 768);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 192);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 64);
            break;
         case CASTLE_15:
            this.name = Text.getText("object.building." + systemName + ".largeCastle.name");
            this.description = Text.getText("object.building." + systemName + ".largeCastle.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 256);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 128);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 1024);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 256);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 72);
      }
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseDesirability = 2;
      this.workerJobType = MobJobBase.MobJobType.BUILDER;

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

      switch (this.currentType) {
         case CASTLE_1:
         case CASTLE_2:
            this.workersProvided = 12;
            this.buildingsProvided = 8;
            this.ancillariesProvided = 1;
            this.baseRange = 32;

            for (ResourceModule.ResourceType txxxxxxxxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxxxxxxxx, 10);
               }
            }

            this.globalWorkSpeedReduction = 2.0;
            this.buildingSpeedReduction = 4.0;
            break;
         case CASTLE_3:
            this.workersProvided = 13;
            this.buildingsProvided = 12;
            this.ancillariesProvided = 2;
            this.baseRange = 34;

            for (ResourceModule.ResourceType txxxxxxxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxxxxxxx, 15);
               }
            }

            this.globalWorkSpeedReduction = 2.0;
            this.buildingSpeedReduction = 4.0;
            break;
         case CASTLE_4:
            this.workersProvided = 14;
            this.buildingsProvided = 16;
            this.ancillariesProvided = 3;
            this.baseRange = 36;

            for (ResourceModule.ResourceType txxxxxxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxxxxxx, 20);
               }
            }

            this.globalWorkSpeedReduction = 3.0;
            this.buildingSpeedReduction = 6.0;
            break;
         case CASTLE_5:
            this.workersProvided = 15;
            this.buildingsProvided = 20;
            this.ancillariesProvided = 4;
            this.baseRange = 38;

            for (ResourceModule.ResourceType txxxxxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxxxxx, 25);
               }
            }

            this.globalWorkSpeedReduction = 4.0;
            this.buildingSpeedReduction = 8.0;
            break;
         case CASTLE_6:
            this.workersProvided = 16;
            this.buildingsProvided = 26;
            this.ancillariesProvided = 5;
            this.baseRange = 40;

            for (ResourceModule.ResourceType txxxxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxxxx, 30);
               }
            }

            this.globalWorkSpeedReduction = 5.0;
            this.buildingSpeedReduction = 10.0;
            break;
         case CASTLE_7:
            this.workersProvided = 17;
            this.buildingsProvided = 32;
            this.ancillariesProvided = 6;
            this.baseRange = 42;

            for (ResourceModule.ResourceType txxxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxxx, 35);
               }
            }

            this.globalWorkSpeedReduction = 6.0;
            this.buildingSpeedReduction = 12.0;
            break;
         case CASTLE_8:
            this.workersProvided = 18;
            this.buildingsProvided = 38;
            this.ancillariesProvided = 7;
            this.baseRange = 44;

            for (ResourceModule.ResourceType txxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxx, 40);
               }
            }

            this.globalWorkSpeedReduction = 7.0;
            this.buildingSpeedReduction = 14.0;
            break;
         case CASTLE_9:
            this.workersProvided = 19;
            this.buildingsProvided = 44;
            this.ancillariesProvided = 8;
            this.baseRange = 46;

            for (ResourceModule.ResourceType txxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxx, 45);
               }
            }

            this.globalWorkSpeedReduction = 8.0;
            this.buildingSpeedReduction = 16.0;
            break;
         case CASTLE_10:
            this.workersProvided = 20;
            this.buildingsProvided = 50;
            this.ancillariesProvided = 9;
            this.baseRange = 48;

            for (ResourceModule.ResourceType txxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxx.getTemplate().dontAddMisc()
                  && txxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxx, 50);
               }
            }

            this.globalWorkSpeedReduction = 9.0;
            this.buildingSpeedReduction = 18.0;
            break;
         case CASTLE_11:
            this.workersProvided = 21;
            this.buildingsProvided = 56;
            this.ancillariesProvided = 10;
            this.baseRange = 50;

            for (ResourceModule.ResourceType txxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxx.getTemplate().dontAddMisc()
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxx, 55);
               }
            }

            this.globalWorkSpeedReduction = 10.0;
            this.buildingSpeedReduction = 20.0;
            break;
         case CASTLE_12:
            this.workersProvided = 22;
            this.buildingsProvided = 62;
            this.ancillariesProvided = 11;
            this.baseRange = 52;

            for (ResourceModule.ResourceType txxxx : ResourceModule.ResourceType.values()) {
               if (!txxxx.getTemplate().dontAddMisc()
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxx, 60);
               }
            }

            this.globalWorkSpeedReduction = 11.0;
            this.buildingSpeedReduction = 22.0;
            break;
         case CASTLE_13:
            this.workersProvided = 23;
            this.buildingsProvided = 68;
            this.ancillariesProvided = 12;
            this.baseRange = 54;

            for (ResourceModule.ResourceType txxx : ResourceModule.ResourceType.values()) {
               if (!txxx.getTemplate().dontAddMisc()
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxx, 65);
               }
            }

            this.globalWorkSpeedReduction = 12.0;
            this.buildingSpeedReduction = 24.0;
            break;
         case CASTLE_14:
            this.workersProvided = 24;
            this.buildingsProvided = 74;
            this.ancillariesProvided = 13;
            this.baseRange = 56;

            for (ResourceModule.ResourceType txx : ResourceModule.ResourceType.values()) {
               if (!txx.getTemplate().dontAddMisc()
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txx, 70);
               }
            }

            this.globalWorkSpeedReduction = 13.0;
            this.buildingSpeedReduction = 26.0;
            break;
         case CASTLE_15:
            this.workersProvided = 25;
            this.buildingsProvided = 80;
            this.ancillariesProvided = 14;
            this.baseRange = 58;

            for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
               if (!tx.getTemplate().dontAddMisc()
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(tx, 75);
               }
            }

            this.globalWorkSpeedReduction = 14.0;
            this.buildingSpeedReduction = 28.0;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseDesirability = 2;
      this.workerJobType = MobJobBase.MobJobType.BUILDER;

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

      switch (this.currentType) {
         case CASTLE_1:
            this.workersProvided = 12;
            this.buildingsProvided = 8;
            this.ancillariesProvided = 1;
            this.baseRange = 32;

            for (ResourceModule.ResourceType txxxxxxxxxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxxxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxxxxxxxxx, 10);
               }
            }

            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_2;
            break;
         case CASTLE_2:
            this.workersProvided = 13;
            this.buildingsProvided = 12;
            this.ancillariesProvided = 2;
            this.baseRange = 34;

            for (ResourceModule.ResourceType txxxxxxxxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxxxxxxxx, 15);
               }
            }

            this.globalWorkSpeedReduction = 2.0;
            this.buildingSpeedReduction = 4.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_3;
            break;
         case CASTLE_3:
            this.workersProvided = 14;
            this.buildingsProvided = 16;
            this.ancillariesProvided = 3;
            this.baseRange = 36;

            for (ResourceModule.ResourceType txxxxxxxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxxxxxxx, 20);
               }
            }

            this.globalWorkSpeedReduction = 3.0;
            this.buildingSpeedReduction = 6.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_4;
            break;
         case CASTLE_4:
            this.workersProvided = 15;
            this.buildingsProvided = 20;
            this.ancillariesProvided = 4;
            this.baseRange = 38;

            for (ResourceModule.ResourceType txxxxxxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxxxxxx, 25);
               }
            }

            this.globalWorkSpeedReduction = 4.0;
            this.buildingSpeedReduction = 8.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_5;
            break;
         case CASTLE_5:
            this.workersProvided = 16;
            this.buildingsProvided = 26;
            this.ancillariesProvided = 5;
            this.baseRange = 40;

            for (ResourceModule.ResourceType txxxxxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxxxxx, 30);
               }
            }

            this.globalWorkSpeedReduction = 5.0;
            this.buildingSpeedReduction = 10.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_6;
            break;
         case CASTLE_6:
            this.workersProvided = 17;
            this.buildingsProvided = 32;
            this.ancillariesProvided = 6;
            this.baseRange = 42;

            for (ResourceModule.ResourceType txxxxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxxxx, 35);
               }
            }

            this.globalWorkSpeedReduction = 6.0;
            this.buildingSpeedReduction = 12.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_7;
            break;
         case CASTLE_7:
            this.workersProvided = 18;
            this.buildingsProvided = 38;
            this.ancillariesProvided = 7;
            this.baseRange = 44;

            for (ResourceModule.ResourceType txxxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxxx, 40);
               }
            }

            this.globalWorkSpeedReduction = 7.0;
            this.buildingSpeedReduction = 14.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_8;
            break;
         case CASTLE_8:
            this.workersProvided = 19;
            this.buildingsProvided = 44;
            this.ancillariesProvided = 8;
            this.baseRange = 46;

            for (ResourceModule.ResourceType txxxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxxx, 45);
               }
            }

            this.globalWorkSpeedReduction = 8.0;
            this.buildingSpeedReduction = 16.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_9;
            break;
         case CASTLE_9:
            this.workersProvided = 20;
            this.buildingsProvided = 50;
            this.ancillariesProvided = 9;
            this.baseRange = 48;

            for (ResourceModule.ResourceType txxxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxxx.getTemplate().dontAddMisc()
                  && txxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxxx, 50);
               }
            }

            this.globalWorkSpeedReduction = 9.0;
            this.buildingSpeedReduction = 18.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_10;
            break;
         case CASTLE_10:
            this.workersProvided = 21;
            this.buildingsProvided = 56;
            this.ancillariesProvided = 10;
            this.baseRange = 50;

            for (ResourceModule.ResourceType txxxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxxx.getTemplate().dontAddMisc()
                  && txxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxxx, 55);
               }
            }

            this.globalWorkSpeedReduction = 10.0;
            this.buildingSpeedReduction = 20.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_11;
            break;
         case CASTLE_11:
            this.workersProvided = 22;
            this.buildingsProvided = 62;
            this.ancillariesProvided = 11;
            this.baseRange = 52;

            for (ResourceModule.ResourceType txxxxx : ResourceModule.ResourceType.values()) {
               if (!txxxxx.getTemplate().dontAddMisc()
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxxx, 60);
               }
            }

            this.globalWorkSpeedReduction = 11.0;
            this.buildingSpeedReduction = 22.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_12;
            break;
         case CASTLE_12:
            this.workersProvided = 23;
            this.buildingsProvided = 68;
            this.ancillariesProvided = 12;
            this.baseRange = 54;

            for (ResourceModule.ResourceType txxxx : ResourceModule.ResourceType.values()) {
               if (!txxxx.getTemplate().dontAddMisc()
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxxx, 65);
               }
            }

            this.globalWorkSpeedReduction = 12.0;
            this.buildingSpeedReduction = 24.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_13;
            break;
         case CASTLE_13:
            this.workersProvided = 24;
            this.buildingsProvided = 74;
            this.ancillariesProvided = 13;
            this.baseRange = 56;

            for (ResourceModule.ResourceType txxx : ResourceModule.ResourceType.values()) {
               if (!txxx.getTemplate().dontAddMisc()
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txxx, 70);
               }
            }

            this.globalWorkSpeedReduction = 13.0;
            this.buildingSpeedReduction = 26.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_14;
            break;
         case CASTLE_14:
            this.workersProvided = 25;
            this.buildingsProvided = 80;
            this.ancillariesProvided = 14;
            this.baseRange = 58;

            for (ResourceModule.ResourceType txx : ResourceModule.ResourceType.values()) {
               if (!txx.getTemplate().dontAddMisc()
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(txx, 75);
               }
            }

            this.globalWorkSpeedReduction = 14.0;
            this.buildingSpeedReduction = 28.0;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CASTLE_15;
            break;
         case CASTLE_15:
            this.workersProvided = 26;
            this.buildingsProvided = 86;
            this.ancillariesProvided = 15;
            this.baseRange = 60;

            for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
               if (!tx.getTemplate().dontAddMisc()
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.FOOD_AND_WATER
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.BYPRODUCT
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.RECOVERY
                  && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TOWER_AMMO) {
                  this.resourceStorageMax.put(tx, 80);
               }
            }

            this.globalWorkSpeedReduction = 15.0;
            this.buildingSpeedReduction = 30.0;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
