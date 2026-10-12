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

public class StoneGolemCombobulatorFlags extends ObjectFlags {
   public StoneGolemCombobulatorFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR;
      this.width = 4;
      this.height = 4;
      String systemName = "stoneGolemCombobulator";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.ESSENCE_COLLECTOR});
      if (Utilities.randomInt(100) == 0) {
         this.combobulatorEasterEggMode = true;
      }

      switch (this.currentType) {
         case STONE_GOLEM_COMBOBULATOR:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 4);
            break;
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 30);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 8);
            break;
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 38);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 12);
            break;
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING:
            this.name = Text.getText("object.building." + systemName + ".lightning.name");
            this.description = Text.getText("object.building." + systemName + ".lightning.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 46);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
            break;
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_3_ICE:
            this.name = Text.getText("object.building." + systemName + ".ice.name");
            this.description = Text.getText("object.building." + systemName + ".ice.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 46);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 12);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
            break;
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_3_FIRE:
            this.name = Text.getText("object.building." + systemName + ".fire.name");
            this.description = Text.getText("object.building." + systemName + ".fire.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 46);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
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
      if (this.currentType != MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR) {
         this.canAcceptEssence = true;
         this.essenceReturnCoordinates = OrderedPair.getOrderedPair(2, 2);
         this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(0, 0);
         this.energyMax = 200;
         this.isCombobulator = true;
         this.combobulatorEnergyLossPerGolem = 1;
         this.combobulatorEnergyLossPerGolemRate = 500;
         this.combobulatorResourceDistance = 32;
         this.combobulatorResourceType = ResourceModule.ResourceType.ROCK;
         if (this.combobulatorEasterEggMode) {
            this.combobulatorSpawnTypes.add(MobBase.MobType.ROGGO_GOLEM);
         } else {
            this.combobulatorSpawnTypes.add(MobBase.MobType.STONE_GOLEM);
         }

         this.baseRange = 8;
         this.baseDesirability = -2;
         this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 6);
         this.buildingStorePriority.put(ResourceModule.ResourceType.ROCK, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.ROCK);
      }

      switch (this.currentType) {
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_1:
            this.combobulatorMax = 2;
            this.combobulatorEnergyRequired = 100;
            this.combobulatorEnergyRate = 150;
            break;
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_2:
            this.combobulatorMax = 3;
            this.combobulatorBonusLevel = 4;
            this.combobulatorEnergyRequired = 125;
            this.combobulatorEnergyRate = 120;
            break;
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING:
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_3_ICE:
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_3_FIRE:
            this.combobulatorMax = 4;
            this.combobulatorBonusLevel = 8;
            this.combobulatorEnergyRequired = 150;
            this.combobulatorEnergyRate = 90;
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
      this.combobulatorResourceType = ResourceModule.ResourceType.ROCK;
      this.baseRange = 8;
      this.baseDesirability = -2;
      this.hasFillArt = true;
      this.resourceStorageMax.put(ResourceModule.ResourceType.ROCK, 6);
      this.buildingStorePriority.put(ResourceModule.ResourceType.ROCK, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.ROCK);
      this.combobulatorSpawnTypes.clear();
      switch (this.currentType) {
         case STONE_GOLEM_COMBOBULATOR:
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_1;
            this.combobulatorMax = 2;
            this.combobulatorEnergyRequired = 100;
            this.combobulatorEnergyRate = 150;
            if (this.combobulatorEasterEggMode) {
               this.combobulatorSpawnTypes.add(MobBase.MobType.ROGGO_GOLEM);
            } else {
               this.combobulatorSpawnTypes.add(MobBase.MobType.STONE_GOLEM);
            }
            break;
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_1:
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_2;
            this.combobulatorMax = 3;
            this.combobulatorBonusLevel = 4;
            this.combobulatorEnergyRequired = 125;
            this.combobulatorEnergyRate = 120;
            if (this.combobulatorEasterEggMode) {
               this.combobulatorSpawnTypes.add(MobBase.MobType.ROGGO_GOLEM);
            } else {
               this.combobulatorSpawnTypes.add(MobBase.MobType.STONE_GOLEM);
            }
            break;
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_2:
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING;
            this.nextUpgradeType2 = MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_3_ICE;
            this.nextUpgradeType3 = MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR_UPGRADE_3_FIRE;
            this.combobulatorMax = 4;
            this.combobulatorBonusLevel = 8;
            this.combobulatorEnergyRequired = 150;
            this.combobulatorEnergyRate = 90;
            if (this.combobulatorEasterEggMode) {
               this.combobulatorSpawnTypes.add(MobBase.MobType.ROGGO_GOLEM);
            } else {
               this.combobulatorSpawnTypes.add(MobBase.MobType.STONE_GOLEM);
            }
            break;
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_3_LIGHTNING:
            this.combobulatorMax = 5;
            this.combobulatorBonusLevel = 12;
            this.combobulatorEnergyRequired = 200;
            this.combobulatorEnergyRate = 90;
            if (this.combobulatorEasterEggMode) {
               this.combobulatorSpawnTypes.add(MobBase.MobType.LIGHTNING_ROGGO_GOLEM);
            } else {
               this.combobulatorSpawnTypes.add(MobBase.MobType.LIGHTNING_STONE_GOLEM);
            }
            break;
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_3_ICE:
            this.combobulatorMax = 5;
            this.combobulatorBonusLevel = 12;
            this.combobulatorEnergyRequired = 200;
            this.combobulatorEnergyRate = 90;
            if (this.combobulatorEasterEggMode) {
               this.combobulatorSpawnTypes.add(MobBase.MobType.ICE_ROGGO_GOLEM);
            } else {
               this.combobulatorSpawnTypes.add(MobBase.MobType.ICE_STONE_GOLEM);
            }
            break;
         case STONE_GOLEM_COMBOBULATOR_UPGRADE_3_FIRE:
            this.combobulatorMax = 5;
            this.combobulatorBonusLevel = 12;
            this.combobulatorEnergyRequired = 200;
            this.combobulatorEnergyRate = 90;
            if (this.combobulatorEasterEggMode) {
               this.combobulatorSpawnTypes.add(MobBase.MobType.FIRE_ROGGO_GOLEM);
            } else {
               this.combobulatorSpawnTypes.add(MobBase.MobType.FIRE_STONE_GOLEM);
            }
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.STONE_GOLEM_LEVEL);
      this.perkAdjustment(PerkModule.PerkType.STONE_GOLEM_CAPACITY);
      this.perkAdjustment(PerkModule.PerkType.STONE_GOLEM_CHARGE_TIME);
   }
}
