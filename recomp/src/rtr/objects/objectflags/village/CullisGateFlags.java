package rtr.objects.objectflags.village;

import rtr.PerkModule;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;

public class CullisGateFlags extends ObjectFlags {
   public CullisGateFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.CULLIS_GATE;
      this.width = 7;
      this.height = 7;
      String systemName = "cullisGate";
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
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.allowDismantle = true;
      this.allowPause = true;
   }

   @Override
   protected void configBuilt() {
      this.mobGroup = MobBase.MobGroup.VILLAGER;
      this.baseRange = 12;
      this.baseDesirability = 1;
      this.isCullisGate = true;
      this.canInstability = true;
      this.instabilityAmountMax = 8000;
      this.allowDismantle = true;
   }

   @Override
   protected void configPerkAdjustments() {
      this.perkAdjustment(PerkModule.PerkType.EFFICIENT_CONSTRUCTION);
      this.perkAdjustment(PerkModule.PerkType.INCREASED_CULLIS_GATE_STABILITY);
   }
}
