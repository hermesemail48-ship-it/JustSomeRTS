package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class SandstoneTan extends TileSetBase {
   public SandstoneTan(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.sandstone.name");
      this.description = Text.getText("tileSet.terrain.sandstone.description");
      this.fileName = "sandstoneTan";
      this.layer = 9;
      this.miniMapColor = new Color(-3093606);
      this.resourceType = ResourceModule.ResourceType.ROCK;
      this.resourceAmount = 10;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.TAN;
   }
}
