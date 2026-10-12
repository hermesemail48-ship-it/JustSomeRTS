package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class SandBlack extends TileSetBase {
   public SandBlack(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.sand.name");
      this.fileName = "sandBlack";
      this.layer = 2;
      this.miniMapColor = new Color(-13224394);
   }
}
