package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class Tiles extends TileSetBase {
   public Tiles(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.tiles.name");
      this.fileName = "tiles";
      this.layer = 2;
      this.miniMapColor = new Color(-10067620);
   }
}
