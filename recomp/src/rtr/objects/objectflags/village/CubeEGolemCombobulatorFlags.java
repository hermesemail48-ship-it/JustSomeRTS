package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;

public class CubeEGolemCombobulatorFlags extends ObjectFlags {
   public CubeEGolemCombobulatorFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR;
      this.width = 3;
      this.height = 3;
      String systemName = "cubeEGolemCombobulator";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ESSENCE_COLLECTOR});
      switch (this.currentType) {
         case CUBE_E_GOLEM_COMBOBULATOR:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 4);
            break;
         case CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 8);
            break;
         case CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 72);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 12);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.combobulatorSpawnTypes.clear();
      if (this.currentType != MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR) {
         this.canAcceptEssence = true;
         this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
         this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
         this.energyMax = 500;
         this.isCombobulator = true;
         this.combobulatorEnergyLossPerGolem = 1;
         this.combobulatorEnergyLossPerGolemRate = 1500;
         this.combobulatorResourceDistance = 32;
         this.combobulatorResourceType = ResourceModule.ResourceType.IRON_INGOT;
         this.combobulatorSpawnTypes.add(MobBase.MobType.CUBE_E_GOLEM);
         this.baseRange = 8;
         this.baseDesirability = -4;
         this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 6);
         this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_INGOT, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.IRON_INGOT);
      }

      switch (this.currentType) {
         case CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_1:
            this.combobulatorMax = 2;
            this.combobulatorEnergyRequired = 100;
            this.combobulatorEnergyRate = 80;
            break;
         case CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_2:
            this.combobulatorMax = 3;
            this.combobulatorBonusLevel = 4;
            this.combobulatorEnergyRequired = 125;
            this.combobulatorEnergyRate = 60;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.combobulatorSpawnTypes.clear();
      this.canAcceptEssence = true;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(1, 1);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
      this.energyMax = 500;
      this.isCombobulator = true;
      this.combobulatorEnergyLossPerGolem = 1;
      this.combobulatorEnergyLossPerGolemRate = 1500;
      this.combobulatorResourceDistance = 32;
      this.combobulatorResourceType = ResourceModule.ResourceType.IRON_INGOT;
      this.combobulatorSpawnTypes.add(MobBase.MobType.CUBE_E_GOLEM);
      this.baseRange = 8;
      this.baseDesirability = -4;
      this.hasFillArt = true;
      this.resourceStorageMax.put(ResourceModule.ResourceType.IRON_INGOT, 6);
      this.buildingStorePriority.put(ResourceModule.ResourceType.IRON_INGOT, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.IRON_INGOT);
      switch (this.currentType) {
         case CUBE_E_GOLEM_COMBOBULATOR:
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_1;
            this.combobulatorMax = 2;
            this.combobulatorEnergyRequired = 100;
            this.combobulatorEnergyRate = 80;
            break;
         case CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_1:
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_2;
            this.combobulatorMax = 3;
            this.combobulatorBonusLevel = 4;
            this.combobulatorEnergyRequired = 125;
            this.combobulatorEnergyRate = 60;
            break;
         case CUBE_E_GOLEM_COMBOBULATOR_UPGRADE_2:
            this.combobulatorMax = 4;
            this.combobulatorBonusLevel = 8;
            this.combobulatorEnergyRequired = 150;
            this.combobulatorEnergyRate = 40;
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
