package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class BricksAccent extends TileSetBase {
   public BricksAccent(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.bricks.name");
      this.fileName = "bricksAccent";
      this.layer = 2;
      this.miniMapColor = new Color(-12698050);
   }
}
