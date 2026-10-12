package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class RockBrownCollected extends TileSetBase {
   public RockBrownCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.rockCollected.name");
      this.description = Text.getText("tileSet.terrain.rockCollected.description");
      this.fileName = "rockBrownCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-12569817);
      this.canRegrow = MapTilesLoader.TileSet.ROCK_BROWN;
      this.regrowIgnoreWeather = true;
      this.regrowRate = 20000;
   }
}
