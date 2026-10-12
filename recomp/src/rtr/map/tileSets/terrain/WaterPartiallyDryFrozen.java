package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class WaterPartiallyDryFrozen extends TileSetBase {
   public WaterPartiallyDryFrozen(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.frozenMud.name");
      this.fileName = "waterPartiallyDryFrozen";
      this.layer = 6;
      this.miniMapColor = new Color(-10914192);
      this.resourceType = ResourceModule.ResourceType.DIRTY_WATER_BUCKET;
      this.resourceAmount = 2;
      this.resourceAmountMax = 10;
      this.resourceColorSet = ResourceModule.ResourceColorSet.BLUE;
   }
}
