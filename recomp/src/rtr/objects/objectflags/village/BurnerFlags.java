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

public class BurnerFlags extends ObjectFlags {
   public BurnerFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.BURNER;
      this.width = 4;
      this.height = 4;
      String systemName = "burner";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.recommendedSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.TRASH_CAN, MapTilesLoader.TileSet.LANDFILL});
      switch (this.currentType) {
         case BURNER:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 8);
            break;
         case BURNER_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 16);
            break;
         case BURNER_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 16);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 8);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 32);
            this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 8);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.BURNER) {
         this.baseRange = 12;
         this.baseDesirability = -4;
         this.workerJobType = MobJobBase.MobJobType.TRASHER;
         this.essenceReturnCoordinates = OrderedPair.getOrderedPair(2, 2);
         this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
         this.energyMax = 50;
         this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, ObjectBase.ResourceStorePriority.HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.ORGANICY_TRASH, ObjectBase.ResourceStorePriority.HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.ROCKY_TRASH, ObjectBase.ResourceStorePriority.HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, ObjectBase.ResourceStorePriority.HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_CUBE, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_TRASH, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.WOODY_TRASH, ObjectBase.ResourceStorePriority.HIGH);
         this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.ORGANICY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.ROCKY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_CUBE);
         this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_TRASH);
         this.canBuildingStore.add(ResourceModule.ResourceType.WOODY_TRASH);
      }

      switch (this.currentType) {
         case BURNER_UPGRADE_1:
            this.workersProvided = 2;
            this.isBurner = true;
            this.burnerRate = 500;
            this.burnerEnergyDissipationRate = 100;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 10);
            break;
         case BURNER_UPGRADE_2:
            this.workersProvided = 3;
            this.isBurner = true;
            this.burnerRate = 750;
            this.burnerEnergyDissipationRate = 100;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 15);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 15);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 15);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 15);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 15);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 15);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 15);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 12;
      this.baseDesirability = -4;
      this.workerJobType = MobJobBase.MobJobType.TRASHER;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(2, 2);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
      this.energyMax = 50;
      this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, ObjectBase.ResourceStorePriority.HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.ORGANICY_TRASH, ObjectBase.ResourceStorePriority.HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.ROCKY_TRASH, ObjectBase.ResourceStorePriority.HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, ObjectBase.ResourceStorePriority.HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_CUBE, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.TRASHY_TRASH, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WOODY_TRASH, ObjectBase.ResourceStorePriority.HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.ORGANICY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_CUBE);
      this.canBuildingStore.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canBuildingStore.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.hasFillArt = true;
      switch (this.currentType) {
         case BURNER:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 10);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 10);
            this.isBurner = true;
            this.burnerRate = 1000;
            this.burnerEnergyDissipationRate = 100;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.BURNER_UPGRADE_1;
            break;
         case BURNER_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 15);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 15);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 15);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 15);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 15);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 15);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 15);
            this.isBurner = true;
            this.burnerRate = 750;
            this.burnerEnergyDissipationRate = 100;
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.BURNER_UPGRADE_2;
            break;
         case BURNER_UPGRADE_2:
            this.workersProvided = 4;
            this.isBurner = true;
            this.burnerRate = 500;
            this.burnerEnergyDissipationRate = 100;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTALLY_TRASH, 20);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ORGANICY_TRASH, 20);
            this.resourceStorageMax.put(ResourceModule.ResourceType.ROCKY_TRASH, 20);
            this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUSY_TRASH, 20);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_CUBE, 20);
            this.resourceStorageMax.put(ResourceModule.ResourceType.TRASHY_TRASH, 20);
            this.resourceStorageMax.put(ResourceModule.ResourceType.WOODY_TRASH, 20);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_TRASH_BURNING);
   }
}
