package rtr.objects.objectflags.corruption;

import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class CorruptedLargeFirePitFlags extends ObjectFlags {
   public CorruptedLargeFirePitFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.CORRUPTED_LARGE_FIRE_PIT;
      this.width = 1;
      this.height = 1;
      String systemName = "corruptedLargeFirePit";
      this.baseName = Text.getText("object.building." + systemName + ".name");
      this.name = Text.getText("object.building." + systemName + ".name");
      this.description = Text.getText("object.building." + systemName + ".description");
      this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 12);
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.MONSTER;
      this.destroyIfIgnored = true;
      this.destroyIfIgnoredDelay = 100000;
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.MONSTER;
      this.baseDesirability = -1;
   }
}
