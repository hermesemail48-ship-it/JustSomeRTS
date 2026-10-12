package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class DirtDarkBrown extends TileSetBase {
   public DirtDarkBrown(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.dirt.name");
      this.fileName = "dirtDarkBrown";
      this.layer = 2;
      this.miniMapColor = new Color(-9552617);
   }
}
