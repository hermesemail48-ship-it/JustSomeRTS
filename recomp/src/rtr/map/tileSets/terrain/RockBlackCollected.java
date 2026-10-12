package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class RockBlackCollected extends TileSetBase {
   public RockBlackCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.rockCollected.name");
      this.description = Text.getText("tileSet.terrain.rockCollected.description");
      this.fileName = "rockBlackCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-13355980);
      this.canRegrow = MapTilesLoader.TileSet.ROCK_BLACK;
      this.regrowIgnoreWeather = true;
      this.regrowRate = 20000;
   }
}
