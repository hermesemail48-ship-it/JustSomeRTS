package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class GrassTealAccent extends TileSetBase {
   public GrassTealAccent(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.grass.name");
      this.fileName = "grassTealAccent";
      this.layer = 2;
      this.miniMapColor = new Color(-13084404);
   }
}
