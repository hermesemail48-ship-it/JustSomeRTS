package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class WoodGolemCombobulatorFlags extends ObjectFlags {
   public WoodGolemCombobulatorFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR;
      this.width = 4;
      this.height = 4;
      String systemName = "woodGolemCombobulator";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ESSENCE_COLLECTOR});
      if (Utilities.randomInt(100) == 0) {
         this.combobulatorEasterEggMode = true;
      }

      switch (this.currentType) {
         case WOOD_GOLEM_COMBOBULATOR:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 4);
            break;
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 30);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 8);
            break;
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 38);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 12);
            break;
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING:
            this.name = Text.getText("object.building." + systemName + ".lightning.name");
            this.description = Text.getText("object.building." + systemName + ".lightning.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 46);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
            break;
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_ICE:
            this.name = Text.getText("object.building." + systemName + ".ice.name");
            this.description = Text.getText("object.building." + systemName + ".ice.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 46);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
            break;
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_POISON:
            this.name = Text.getText("object.building." + systemName + ".poison.name");
            this.description = Text.getText("object.building." + systemName + ".poison.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 46);
            this.resourceBaseValues.put(ResourceModule.ResourceType.BOARD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.combobulatorSpawnTypes.clear();
      if (this.currentType != MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR) {
         this.canAcceptEssence = true;
         this.essenceReturnCoordinates = OrderedPair.getOrderedPair(2, 2);
         this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(0, 0);
         this.energyMax = 200;
         this.isCombobulator = true;
         this.combobulatorEnergyLossPerGolem = 1;
         this.combobulatorEnergyLossPerGolemRate = 500;
         this.combobulatorResourceDistance = 32;
         this.combobulatorResourceType = ResourceModule.ResourceType.WOOD;
         if (this.combobulatorEasterEggMode) {
            this.combobulatorSpawnTypes.add(MobBase.MobType.LOGGO_GOLEM);
         } else {
            this.combobulatorSpawnTypes.add(MobBase.MobType.WOOD_GOLEM);
         }

         this.baseRange = 8;
         this.baseDesirability = -2;
         this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 6);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
      }

      switch (this.currentType) {
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_1:
            this.combobulatorMax = 3;
            this.combobulatorEnergyRequired = 100;
            this.combobulatorEnergyRate = 120;
            break;
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_2:
            this.combobulatorMax = 4;
            this.combobulatorBonusLevel = 4;
            this.combobulatorEnergyRequired = 125;
            this.combobulatorEnergyRate = 90;
            break;
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING:
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_ICE:
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_POISON:
            this.combobulatorMax = 5;
            this.combobulatorBonusLevel = 8;
            this.combobulatorEnergyRequired = 150;
            this.combobulatorEnergyRate = 60;
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.canAcceptEssence = true;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(2, 2);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(0, 0);
      this.energyMax = 200;
      this.isCombobulator = true;
      this.combobulatorEnergyLossPerGolem = 1;
      this.combobulatorEnergyLossPerGolemRate = 500;
      this.combobulatorResourceDistance = 32;
      this.combobulatorResourceType = ResourceModule.ResourceType.WOOD;
      this.baseRange = 8;
      this.baseDesirability = -2;
      this.hasFillArt = true;
      this.resourceStorageMax.put(ResourceModule.ResourceType.WOOD, 6);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOOD, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOOD);
      this.combobulatorSpawnTypes.clear();
      switch (this.currentType) {
         case WOOD_GOLEM_COMBOBULATOR:
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_1;
            this.combobulatorMax = 3;
            this.combobulatorEnergyRequired = 100;
            this.combobulatorEnergyRate = 120;
            if (this.combobulatorEasterEggMode) {
               this.combobulatorSpawnTypes.add(MobBase.MobType.LOGGO_GOLEM);
            } else {
               this.combobulatorSpawnTypes.add(MobBase.MobType.WOOD_GOLEM);
            }
            break;
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_1:
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_2;
            this.combobulatorMax = 4;
            this.combobulatorBonusLevel = 4;
            this.combobulatorEnergyRequired = 125;
            this.combobulatorEnergyRate = 90;
            if (this.combobulatorEasterEggMode) {
               this.combobulatorSpawnTypes.add(MobBase.MobType.LOGGO_GOLEM);
            } else {
               this.combobulatorSpawnTypes.add(MobBase.MobType.WOOD_GOLEM);
            }
            break;
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_2:
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING;
            this.nextUpgradeType2 = MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_ICE;
            this.nextUpgradeType3 = MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_POISON;
            this.combobulatorMax = 5;
            this.combobulatorBonusLevel = 8;
            this.combobulatorEnergyRequired = 150;
            this.combobulatorEnergyRate = 60;
            if (this.combobulatorEasterEggMode) {
               this.combobulatorSpawnTypes.add(MobBase.MobType.LOGGO_GOLEM);
            } else {
               this.combobulatorSpawnTypes.add(MobBase.MobType.WOOD_GOLEM);
            }
            break;
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING:
            this.combobulatorMax = 6;
            this.combobulatorBonusLevel = 12;
            this.combobulatorEnergyRequired = 200;
            this.combobulatorEnergyRate = 60;
            if (this.combobulatorEasterEggMode) {
               this.combobulatorSpawnTypes.add(MobBase.MobType.LIGHTNING_LOGGO_GOLEM);
            } else {
               this.combobulatorSpawnTypes.add(MobBase.MobType.LIGHTNING_WOOD_GOLEM);
            }
            break;
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_ICE:
            this.combobulatorMax = 6;
            this.combobulatorBonusLevel = 12;
            this.combobulatorEnergyRequired = 200;
            this.combobulatorEnergyRate = 60;
            if (this.combobulatorEasterEggMode) {
               this.combobulatorSpawnTypes.add(MobBase.MobType.ICE_LOGGO_GOLEM);
            } else {
               this.combobulatorSpawnTypes.add(MobBase.MobType.ICE_WOOD_GOLEM);
            }
            break;
         case WOOD_GOLEM_COMBOBULATOR_UPGRADE_3_POISON:
            this.combobulatorMax = 6;
            this.combobulatorBonusLevel = 12;
            this.combobulatorEnergyRequired = 200;
            this.combobulatorEnergyRate = 60;
            if (this.combobulatorEasterEggMode) {
               this.combobulatorSpawnTypes.add(MobBase.MobType.POISON_LOGGO_GOLEM);
            } else {
               this.combobulatorSpawnTypes.add(MobBase.MobType.POISON_WOOD_GOLEM);
            }
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.WOOD_GOLEM_LEVEL);
      this.perkAdjustment(PerkModule.PerkType.WOOD_GOLEM_CAPACITY);
      this.perkAdjustment(PerkModule.PerkType.WOOD_GOLEM_CHARGE_TIME);
   }
}
