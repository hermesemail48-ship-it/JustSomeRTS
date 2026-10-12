package rtr.objects.objectflags.village;

import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class LootBoxFlags extends ObjectFlags {
   public LootBoxFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.LOOT_BOX;
      this.width = 1;
      this.height = 1;
      String systemName = "lootBox";
      this.baseName = Text.getText("object.building." + systemName + ".name");
      this.name = Text.getText("object.building." + systemName + ".name");
      this.description = Text.getText("object.building." + systemName + ".description");
      this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 4);
      this.canNotSelect = true;
      this.canNotBeAttacked = true;
      this.invulnerable = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.NONE;
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.NONE;
      this.buildingStorePriority.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, ObjectBase.ResourceStorePriority.VERY_HIGH);
      this.canBuildingStore.add(ResourceModule.ResourceType.SUSPICICIOUS_KEY);
      this.isLootBox = true;
      this.resourceStorageMax.put(ResourceModule.ResourceType.SUSPICICIOUS_KEY, 1);
   }
}
