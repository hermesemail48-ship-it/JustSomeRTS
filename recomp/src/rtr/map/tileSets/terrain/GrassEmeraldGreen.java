package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class GrassEmeraldGreen extends TileSetBase {
   public GrassEmeraldGreen(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.grass.name");
      this.fileName = "grassEmeraldGreen";
      this.layer = 2;
      this.miniMapColor = new Color(-15833556);
   }
}
