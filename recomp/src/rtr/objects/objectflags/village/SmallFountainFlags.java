package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class SmallFountainFlags extends ObjectFlags {
   public SmallFountainFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.SMALL_FOUNTAIN;
      this.width = 3;
      this.height = 3;
      String systemName = "smallFountain";
      this.baseName = Text.getText("object.building." + systemName + ".name");
      this.name = Text.getText("object.building." + systemName + ".name");
      this.description = Text.getText("object.building." + systemName + ".description");
      this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 24);
      this.requiredSupportBuildings
         .add(new MapTilesLoader.TileSet[]{MapTilesLoader.TileSet.RAIN_CATCHER, MapTilesLoader.TileSet.WATER_PURIFIER, MapTilesLoader.TileSet.WELL});
      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.isFountain = true;
      this.canCatchRain = true;
      this.rainCatchRate = 500;
      this.baseRange = 12;
      this.baseDesirability = 1;
      this.resourceStorageMax.put(ResourceModule.ResourceType.WATER_BUCKET, 48);
      this.buildingStorePriority.put(ResourceModule.ResourceType.WATER_BUCKET, ObjectBase.ResourceStorePriority.HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.WATER_BUCKET);
      this.hasFillArt = true;
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.WATER_STORAGE);
   }
}
