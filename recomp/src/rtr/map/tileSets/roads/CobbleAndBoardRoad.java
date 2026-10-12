package rtr.map.tileSets.roads;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class CobbleAndBoardRoad extends TileSetBase {
   public CobbleAndBoardRoad(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.ROAD);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.road.cobbleAndBoardRoad.name");
      this.description = Text.getText("tileSet.road.cobbleAndBoardRoad.description");
      this.fileName = "cobbleAndBoardRoad";
      this.layer = 5;
      this.miniMapColor = new Color(-5592406);
   }
}
