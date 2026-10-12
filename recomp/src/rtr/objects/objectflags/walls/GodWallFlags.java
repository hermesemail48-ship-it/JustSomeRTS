package rtr.objects.objectflags.walls;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class GodWallFlags extends ObjectFlags {
   public GodWallFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.GOD_WALL;
      this.width = 1;
      this.height = 1;
      String systemName = "godWall";
      this.baseName = Text.getText("object.wall." + systemName + ".name");
      this.name = Text.getText("object.wall." + systemName + ".name");
      this.description = Text.getText("object.wall." + systemName + ".description");
      this.resourceBaseValues.put(ResourceModule.ResourceType.GOD_DUST, 2);
      this.influenceMaintainCost = 50;
      this.canNotBeAttacked = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.GOD_WALL_SPELL_COST);
   }
}
