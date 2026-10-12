package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class GravelGray extends TileSetBase {
   public GravelGray(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.gravel.name");
      this.fileName = "gravelGray";
      this.layer = 2;
      this.miniMapColor = new Color(-9211021);
   }
}
