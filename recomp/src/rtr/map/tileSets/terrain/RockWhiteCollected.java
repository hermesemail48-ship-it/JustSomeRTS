package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class RockWhiteCollected extends TileSetBase {
   public RockWhiteCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.rockCollected.name");
      this.description = Text.getText("tileSet.terrain.rockCollected.description");
      this.fileName = "rockWhiteCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-7631989);
      this.canRegrow = MapTilesLoader.TileSet.ROCK_WHITE;
      this.regrowIgnoreWeather = true;
      this.regrowRate = 20000;
   }
}
