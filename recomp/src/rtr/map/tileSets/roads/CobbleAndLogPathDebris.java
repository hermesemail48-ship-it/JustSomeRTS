package rtr.map.tileSets.roads;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class CobbleAndLogPathDebris extends TileSetBase {
   public CobbleAndLogPathDebris(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.ROAD);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.road.cobbleAndLogPathDebris.name");
      this.description = Text.getText("tileSet.road.cobbleAndLogPathDebris.description");
      this.fileName = "cobbleAndLogPathDebris";
      this.layer = 5;
      this.miniMapColor = new Color(-5592406);
   }
}
