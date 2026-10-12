package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class MigrationWayStationFlags extends ObjectFlags {
   public MigrationWayStationFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.MIGRATION_WAY_STATION;
      this.width = 8;
      this.height = 7;
      String systemName = "migrationWayStation";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case MIGRATION_WAY_STATION:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            break;
         case MIGRATION_WAY_STATION_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            break;
         case MIGRATION_WAY_STATION_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 8);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.MIGRATION_WAY_STATION) {
         this.baseRange = 16;
         this.baseDesirability = 2;

         ResourceModule.ResourceType[] var4;
         for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
            if (t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH && !t.getTemplate().dontAddMisc()) {
               this.buildingStorePriority.put(t, ObjectBase.ResourceStorePriority.LOW);
               this.canBuildingStore.add(t);
            }
         }
      }

      switch (this.currentType) {
         case MIGRATION_WAY_STATION_UPGRADE_1:
            this.migrationsPerDayProvided = 6;

            ResourceModule.ResourceType[] var12;
            for (ResourceModule.ResourceType txx : ResourceModule.ResourceType.values()) {
               if (!txx.getTemplate().dontAddMisc() && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(txx, 6);
               }
            }
            break;
         case MIGRATION_WAY_STATION_UPGRADE_2:
            this.migrationsPerDayProvided = 10;

            ResourceModule.ResourceType[] var11;
            for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
               if (!tx.getTemplate().dontAddMisc() && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(tx, 10);
               }
            }
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 16;
      this.baseDesirability = 2;

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (t.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH && !t.getTemplate().dontAddMisc()) {
            this.buildingStorePriority.put(t, ObjectBase.ResourceStorePriority.LOW);
            this.canBuildingStore.add(t);
         }
      }

      this.hasFillArt = true;
      switch (this.currentType) {
         case MIGRATION_WAY_STATION:
            this.migrationsPerDayProvided = 6;

            for (ResourceModule.ResourceType txxx : ResourceModule.ResourceType.values()) {
               if (!txxx.getTemplate().dontAddMisc() && txxx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(txxx, 6);
               }
            }

            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.MIGRATION_WAY_STATION_UPGRADE_1;
            break;
         case MIGRATION_WAY_STATION_UPGRADE_1:
            this.migrationsPerDayProvided = 10;

            for (ResourceModule.ResourceType txx : ResourceModule.ResourceType.values()) {
               if (!txx.getTemplate().dontAddMisc() && txx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(txx, 10);
               }
            }

            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.MIGRATION_WAY_STATION_UPGRADE_2;
            break;
         case MIGRATION_WAY_STATION_UPGRADE_2:
            this.migrationsPerDayProvided = 14;

            for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
               if (!tx.getTemplate().dontAddMisc() && tx.getTemplate().getResourceCategory() != ResourceModule.ResourceCategory.TRASH) {
                  this.resourceStorageMax.put(tx, 14);
               }
            }
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
