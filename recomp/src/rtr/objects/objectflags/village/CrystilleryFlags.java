package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class CrystilleryFlags extends ObjectFlags {
   public CrystilleryFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.CRYSTILLERY;
      this.width = 8;
      this.height = 6;
      String systemName = "crystillery";
      this.baseName = Text.getText("object.building." + systemName + ".base.name");
      this.requiredSupportBuildings.add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.CRYSTAL_HARVESTRY});
      switch (this.currentType) {
         case CRYSTILLERY:
            this.name = Text.getText("object.building." + systemName + ".makeshift.name");
            this.description = Text.getText("object.building." + systemName + ".makeshift.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 16);
            break;
         case CRYSTILLERY_UPGRADE_1:
            this.name = Text.getText("object.building." + systemName + ".base.name");
            this.description = Text.getText("object.building." + systemName + ".base.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 48);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
            break;
         case CRYSTILLERY_UPGRADE_2:
            this.name = Text.getText("object.building." + systemName + ".established.name");
            this.description = Text.getText("object.building." + systemName + ".established.description");
            this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 64);
            this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 32);
      }

      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      if (this.currentType != MapTilesLoader.TileSet.CRYSTILLERY) {
         this.baseRange = 32;
         this.baseDesirability = 1;
         this.workerJobType = MobJobBase.MobJobType.CRYSILLER;
         this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTAL, ObjectBase.ResourceStorePriority.VERY_HIGH);
         this.buildingStorePriority.put(ResourceModule.ResourceType.CRYLITHIUM, ObjectBase.ResourceStorePriority.VERY_LOW);
         this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTAL);
         this.canBuildingStore.add(ResourceModule.ResourceType.CRYLITHIUM);
      }

      switch (this.currentType) {
         case CRYSTILLERY_UPGRADE_1:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 12);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 12);
            break;
         case CRYSTILLERY_UPGRADE_2:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 24);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 24);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.CRYLITHIUM, 10.0);
      }
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 32;
      this.baseDesirability = 1;
      this.workerJobType = MobJobBase.MobJobType.CRYSILLER;
      this.buildingStorePriority.put(ResourceModule.ResourceType.CRYSTAL, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.buildingStorePriority.put(ResourceModule.ResourceType.CRYLITHIUM, ObjectBase.ResourceStorePriority.VERY_LOW);
      this.canBuildingStore.add(ResourceModule.ResourceType.CRYSTAL);
      this.canBuildingStore.add(ResourceModule.ResourceType.CRYLITHIUM);
      this.hasFillArt = true;
      switch (this.currentType) {
         case CRYSTILLERY:
            this.workersProvided = 2;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 18);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 12);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CRYSTILLERY_UPGRADE_1;
            break;
         case CRYSTILLERY_UPGRADE_1:
            this.workersProvided = 3;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 30);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 24);
            this.canUpgrade = true;
            this.nextUpgradeType = MapTilesLoader.TileSet.CRYSTILLERY_UPGRADE_2;
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.CRYLITHIUM, 10.0);
            break;
         case CRYSTILLERY_UPGRADE_2:
            this.workersProvided = 4;
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYSTAL, 38);
            this.resourceStorageMax.put(ResourceModule.ResourceType.CRYLITHIUM, 32);
            this.refiningSpeedReduction.put(ResourceModule.ResourceType.CRYLITHIUM, 20.0);
      }
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
