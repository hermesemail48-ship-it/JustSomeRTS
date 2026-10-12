package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class SnowWhite extends TileSetBase {
   public SnowWhite(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.snow.name");
      this.fileName = "snowWhite";
      this.layer = 2;
      this.miniMapColor = new Color(-3355444);
   }
}
