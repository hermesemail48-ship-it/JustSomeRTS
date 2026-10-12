package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;

public class CourierStationFlags extends ObjectFlags {
   public CourierStationFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.COURIER_STATION;
      this.width = 8;
      this.height = 8;
      String systemName = "courierStation";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ESSENCE_COLLECTOR});
      switch (this.currentType) {
         case COURIER_STATION:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 4);
            break;
         case COURIER_STATION_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 8);
            break;
         case COURIER_STATION_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 4);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.combobulatorSpawnTypes.clear();
      if (this.currentType != MapTilesLoader.TileSet.COURIER_STATION) {
         this.canAcceptEssence = true;
         this.essenceReturnCoordinates = OrderedPair.getOrderedPair(2, 5);
         this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
         this.energyMax = 200;
         this.workerJobType = MobJobBase.MobJobType.COURIER_SUPPLIER;
         this.isCombobulator = true;
         this.combobulatorEnergyLossPerGolem = 0;
         this.combobulatorEnergyLossPerGolemRate = 1000;
         this.combobulatorResourceDistance = 32;
         this.combobulatorResourceType = ResourceModule.ResourceType.ROCK;
         this.combobulatorSpawnTypes.add(MobBase.MobType.COURIER_GOLEM);
         this.baseRange = 16;
         this.baseDesirability = -2;
      }

      switch (this.currentType) {
         case COURIER_STATION_UPGRADE_1:
            this.workersProvided = 2;
            this.combobulatorMax = 4;
            this.combobulatorEnergyRequired = 100;
            this.combobulatorEnergyRate = 80;
            break;
         case COURIER_STATION_UPGRADE_2:
            this.workersProvided = 3;
            this.combobulatorMax = 6;
            this.combobulatorBonusLevel = 4;
            this.combobulatorEnergyRequired = 75;
            this.combobulatorEnergyRate = 60;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.combobulatorSpawnTypes.clear();
      this.canAcceptEssence = true;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(2, 5);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
      this.energyMax = 200;
      this.workerJobType = MobJobBase.MobJobType.COURIER_SUPPLIER;
      this.isCombobulator = true;
      this.combobulatorEnergyLossPerGolem = 1;
      this.combobulatorEnergyLossPerGolemRate = 2500;
      this.combobulatorResourceDistance = 32;
      this.combobulatorResourceType = ResourceModule.ResourceType.ROCK;
      this.combobulatorSpawnTypes.add(MobBase.MobType.COURIER_GOLEM);
      this.baseRange = 16;
      this.baseDesirability = -2;
      this.hasFillArt = true;
      switch (this.currentType) {
         case COURIER_STATION:
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.COURIER_STATION_UPGRADE_1;
            this.workersProvided = 2;
            this.combobulatorMax = 4;
            this.combobulatorEnergyRequired = 100;
            this.combobulatorEnergyRate = 80;
            break;
         case COURIER_STATION_UPGRADE_1:
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.COURIER_STATION_UPGRADE_2;
            this.workersProvided = 3;
            this.combobulatorMax = 6;
            this.combobulatorBonusLevel = 4;
            this.combobulatorEnergyRequired = 75;
            this.combobulatorEnergyRate = 60;
            break;
         case COURIER_STATION_UPGRADE_2:
            this.workersProvided = 4;
            this.combobulatorMax = 8;
            this.combobulatorBonusLevel = 8;
            this.combobulatorEnergyRequired = 50;
            this.combobulatorEnergyRate = 40;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
