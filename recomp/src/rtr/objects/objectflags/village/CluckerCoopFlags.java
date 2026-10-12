package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.gates.GateBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;

public class CluckerCoopFlags extends ObjectFlags {
   public CluckerCoopFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.CLUCKER_COOP;
      this.width = 6;
      this.height = 7;
      String systemName = "cluckerCoop";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.FARM});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.BOTTLER});
      this.recommendedSupportBuildings
         .add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.RAIN_CATCHER, MapTilesLoader.TileSet.WATER_PURIFIER, MapTilesLoader.TileSet.WELL});
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.RANGER_LODGE, MapTilesLoader.TileSet.OUTPOST});
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.KITCHEN});
      switch (this.currentType) {
         case CLUCKER_COOP:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case CLUCKER_COOP_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            break;
         case CLUCKER_COOP_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 96);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.CLUCKER_COOP) {
         this.canCatchRain = true;
         this.rainCatchRate = 300;
         this.workerJobType = MobJobBase.MobJobType.FARMER;
         this.occupantsTypes.clear();
         this.occupantsTypes.add(MobBase.MobType.CLUCKER);
         this.baseRange = 24;
         this.baseDesirability = 1;
         this.gateCoordinates = OrderedPair.getOrderedPair(4, 3);
         this.gateType = GateBase.GateType.WOOD_GATE_WE;
         this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
         this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      }

      switch (this.currentType) {
         case CLUCKER_COOP_UPGRADE_1:
            this.workersProvided = 2;
            this.occupantsMax = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 16);
            break;
         case CLUCKER_COOP_UPGRADE_2:
            this.workersProvided = 3;
            this.occupantsMax = 5;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 24);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.canCatchRain = true;
      this.rainCatchRate = 300;
      this.workerJobType = MobJobBase.MobJobType.FARMER;
      this.occupantsTypes.clear();
      this.occupantsTypes.add(MobBase.MobType.CLUCKER);
      this.baseRange = 24;
      this.baseDesirability = 1;
      this.gateCoordinates = OrderedPair.getOrderedPair(4, 3);
      this.gateType = GateBase.GateType.WOOD_GATE_WE;
      this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.hasFillArt = true;
      switch (this.currentType) {
         case CLUCKER_COOP:
            this.workersProvided = 2;
            this.occupantsMax = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 16);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CLUCKER_COOP_UPGRADE_1;
            break;
         case CLUCKER_COOP_UPGRADE_1:
            this.workersProvided = 3;
            this.occupantsMax = 5;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 24);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CLUCKER_COOP_UPGRADE_2;
            break;
         case CLUCKER_COOP_UPGRADE_2:
            this.workersProvided = 4;
            this.occupantsMax = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 32);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
