package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class MarketplaceFlags extends ObjectFlags {
   public MarketplaceFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.MARKETPLACE;
      this.width = 13;
      this.height = 13;
      String systemName = "marketplace";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      switch (this.currentType) {
         case MARKETPLACE:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            break;
         case MARKETPLACE_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            break;
         case MARKETPLACE_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 72);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.MARKETPLACE) {
         this.baseRange = 24;
         this.baseDesirability = 3;
         this.workerJobType = MobJobBase.MobJobType.PROVISIONER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_COIN_SACK, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_COIN_SACK);
      }

      switch (this.currentType) {
         case MARKETPLACE_UPGRADE_1:
            this.workersProvided = 6;
            this.catjeetProvisionersProvided = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 48);
            break;
         case MARKETPLACE_UPGRADE_2:
            this.workersProvided = 12;
            this.catjeetProvisionersProvided = 12;
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 64);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 24;
      this.baseDesirability = 3;
      this.workerJobType = MobJobBase.MobJobType.PROVISIONER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.GOLD_COIN_SACK, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.GOLD_COIN_SACK);
      this.hasFillArt = true;
      switch (this.currentType) {
         case MARKETPLACE:
            this.workersProvided = 6;
            this.catjeetProvisionersProvided = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 48);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.MARKETPLACE_UPGRADE_1;
            break;
         case MARKETPLACE_UPGRADE_1:
            this.workersProvided = 12;
            this.catjeetProvisionersProvided = 12;
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 64);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.MARKETPLACE_UPGRADE_2;
            break;
         case MARKETPLACE_UPGRADE_2:
            this.workersProvided = 16;
            this.catjeetProvisionersProvided = 16;
            this.resourceStorageMax.put(ResourceModule.ResourceType.GOLD_COIN_SACK, 96);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.MARKET_CATJEET_CAPACITY);
   }
}
