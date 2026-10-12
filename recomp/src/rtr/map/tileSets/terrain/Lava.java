package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class Lava extends TileSetBase {
   public Lava(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.lava.name");
      this.fileName = "lava";
      this.layer = 6;
      this.miniMapColor = new Color(-15083);
   }
}
