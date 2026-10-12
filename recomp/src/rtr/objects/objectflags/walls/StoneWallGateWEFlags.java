package rtr.objects.objectflags.walls;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.gates.GateBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;

public class StoneWallGateWEFlags extends ObjectFlags {
   public StoneWallGateWEFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.STONE_WALL_GATE_WE;
      this.width = 3;
      this.height = 3;
      String systemName = "stoneWallGateWE";
      this.baseName = Text.getText("object.building." + systemName + ".name");
      this.name = Text.getText("object.building." + systemName + ".name");
      this.description = Text.getText("object.building." + systemName + ".description");
      this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 14);
      this.canNotBeAttacked = true;
      this.allowDismantle = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.allowPause = true;
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 6;
      this.baseDesirability = -2;
      this.gateCoordinates = OrderedPair.getOrderedPair(1, 1);
      this.gateType = GateBase.GateType.STONE_GATE_WE;
      this.allowPause = true;
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
   }
}
