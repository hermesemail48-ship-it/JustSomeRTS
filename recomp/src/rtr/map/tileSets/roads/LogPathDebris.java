package rtr.map.tileSets.roads;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class LogPathDebris extends TileSetBase {
   public LogPathDebris(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.ROAD);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.road.logPathDebris.name");
      this.description = Text.getText("tileSet.road.logPathDebris.description");
      this.fileName = "logPathDebris";
      this.layer = 5;
      this.miniMapColor = new Color(-5592406);
   }
}
