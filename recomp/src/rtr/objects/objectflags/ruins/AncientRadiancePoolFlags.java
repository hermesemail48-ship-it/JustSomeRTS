package rtr.objects.objectflags.ruins;

import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;

public class AncientRadiancePoolFlags extends ObjectFlags {
   public AncientRadiancePoolFlags(MapTilesLoader.TileSet currentType, ObjectBase.ObjectSubType subType) {
      super(currentType, subType);
   }

   @Override
   protected void configBasics() {
      this.baseType = MapTilesLoader.TileSet.ANCIENT_RADIANCE_POOL;
      this.width = 1;
      this.height = 1;
      String systemName = "ancientRadiancePool";
      this.baseName = Text.getText("object.building." + systemName + ".name");
      this.name = Text.getText("object.building." + systemName + ".name");
      this.description = Text.getText("object.building." + systemName + ".description");
      this.resourceBaseValues.put(ResourceModule.ResourceType.ROCK, 16);
      this.resourceBaseValues.put(ResourceModule.ResourceType.CRYSTAL, 16);
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
      this.canReleaseEssenceToGod = true;
      this.canEnergyRegenerateFree = true;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(0, 0);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
      this.energyMax = 250;
      this.energyRegenerationRate = 500;
      this.essenceReturnCoordinates = OrderedPair.getOrderedPair(0, 0);
      this.essenceReturnCoordinatesPixelOffset = OrderedPair.getOrderedPair(8, 8);
   }
}
