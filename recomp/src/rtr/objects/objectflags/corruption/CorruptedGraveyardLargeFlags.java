package rtr.objects.objectflags.corruption;

import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class CorruptedGraveyardLargeFlags extends ObjectFlags {
   public CorruptedGraveyardLargeFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.CORRUPTED_GRAVEYARD_LARGE;
      this.width = 7;
      this.height = 7;
      String systemName = "corruptedGraveyardLarge";
      this.baseName = Text.getText("object.building." + systemName + ".name");
      this.name = Text.getText("object.building." + systemName + ".name");
      this.description = Text.getText("object.building." + systemName + ".description");
      this.resourceBaseValues.put(ResourceModule.ResourceType.WOOD, 42);
      this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 32);
      this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 12);
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.MONSTER;
      this.destroyIfIgnored = true;
      this.destroyIfIgnoredDelay = 100000;
      this.allowDamageMonsterSpawns = true;
      this.damageMonsterSpawnsDelay = 5000;
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.MONSTER;
      this.baseDesirability = -3;
      this.allowDamageMonsterSpawns = true;
      this.damageMonsterSpawnsDelay = 5000;
   }
}
