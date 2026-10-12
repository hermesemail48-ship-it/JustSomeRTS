package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class DirtBrown extends TileSetBase {
   public DirtBrown(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.dirt.name");
      this.fileName = "dirtBrown";
      this.layer = 2;
      this.miniMapColor = new Color(-7974358);
   }
}
