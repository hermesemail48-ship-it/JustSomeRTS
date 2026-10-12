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

public class AnimalPenFlags extends ObjectFlags {
   public AnimalPenFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.ANIMAL_PEN;
      this.width = 8;
      this.height = 9;
      String systemName = "animalPen";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.FARM});
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.BOTTLER});
      this.recommendedSupportBuildings
         .add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.RAIN_CATCHER, MapTilesLoader.TileSet.WATER_PURIFIER, MapTilesLoader.TileSet.WELL});
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.RANGER_LODGE, MapTilesLoader.TileSet.OUTPOST});
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.KITCHEN});
      switch (this.currentType) {
         case ANIMAL_PEN:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            break;
         case ANIMAL_PEN_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            break;
         case ANIMAL_PEN_UPGRADE_2:
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
      if (this.currentType != MapTilesLoader.TileSet.ANIMAL_PEN) {
         this.canCatchRain = true;
         this.rainCatchRate = 300;
         this.workerJobType = MobJobBase.MobJobType.FARMER;
         this.occupantsTypes.clear();
         this.occupantsTypes.add(MobBase.MobType.BEEFALO);
         this.occupantsTypes.add(MobBase.MobType.ROUS);
         this.occupantsTypes.add(MobBase.MobType.ENTLER);
         this.baseRange = 24;
         this.baseDesirability = 1;
         this.gateCoordinates = OrderedPair.getOrderedPair(3, 5);
         this.gateType = GateBase.GateType.WOOD_GATE_NS;
         this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.MEDIUM);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.MEDIUM);
         this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
         this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      }

      switch (this.currentType) {
         case ANIMAL_PEN_UPGRADE_1:
            this.workersProvided = 2;
            this.occupantsMax = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 16);
            break;
         case ANIMAL_PEN_UPGRADE_2:
            this.workersProvided = 3;
            this.occupantsMax = 8;
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
      this.occupantsTypes.add(MobBase.MobType.BEEFALO);
      this.occupantsTypes.add(MobBase.MobType.ROUS);
      this.occupantsTypes.add(MobBase.MobType.ENTLER);
      this.baseRange = 24;
      this.baseDesirability = 1;
      this.gateCoordinates = OrderedPair.getOrderedPair(3, 5);
      this.gateType = GateBase.GateType.WOOD_GATE_NS;
      this.buildingStorePriority.put(ResourceModule.ResourceType.RAW_VEGETABLE, ObjectBase.ResourceStorePriority.MEDIUM);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.MEDIUM);
      this.canBuildingStore.add(ResourceModule.ResourceType.RAW_VEGETABLE);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.hasFillArt = true;
      switch (this.currentType) {
         case ANIMAL_PEN:
            this.workersProvided = 2;
            this.occupantsMax = 6;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 16);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 16);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ANIMAL_PEN_UPGRADE_1;
            break;
         case ANIMAL_PEN_UPGRADE_1:
            this.workersProvided = 3;
            this.occupantsMax = 8;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 24);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.ANIMAL_PEN_UPGRADE_2;
            break;
         case ANIMAL_PEN_UPGRADE_2:
            this.workersProvided = 4;
            this.occupantsMax = 10;
            this.resourceStorageMax.put(ResourceModule.ResourceType.RAW_VEGETABLE, 32);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 32);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
