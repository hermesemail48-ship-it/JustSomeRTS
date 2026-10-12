package rtr.objects.objectflags.walls;

import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class StoneWallFlags extends ObjectFlags {
   public StoneWallFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.STONE_WALL;
      this.width = 1;
      this.height = 1;
      String systemName = "stoneWall";
      this.baseName = Text.getText("object.wall." + systemName + ".name");
      this.name = Text.getText("object.wall." + systemName + ".name");
      this.description = Text.getText("object.wall." + systemName + ".description");
      this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 2);
      this.canNotBeAttacked = true;
      this.allowDismantle = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
   }
}
