package rtr.objects.objectflags.ruins;

import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class AncientCullisGateFlags extends ObjectFlags {
   public AncientCullisGateFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.ANCIENT_CULLIS_GATE;
      this.width = 3;
      this.height = 3;
      String systemName = "ancientCullisGate";
      this.baseName = Text.getText("object.building." + systemName + ".name");
      this.name = Text.getText("object.building." + systemName + ".name");
      this.description = Text.getText("object.building." + systemName + ".description");
      this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 8);
      this.resourceBaseValues.put(ResourceModule.ResourceType.CUT_STONE, 24);
      this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 16);
      this.resourceBaseValues.put(ResourceModule.ResourceType.CRYLITHIUM, 6);
      this.resourceBaseValues.put(ResourceModule.ResourceType.IRON_INGOT, 10);
      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configConstruction() {
      this.mobGroup = MobBase.MobGroup.NONE;
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.NONE;
      this.isCullisGate = true;
      this.canInstability = true;
      this.instabilityAmountMax = 5000;
   }
}
