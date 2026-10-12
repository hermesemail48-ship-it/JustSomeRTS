package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class RockGrayCollected extends TileSetBase {
   public RockGrayCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.rockCollected.name");
      this.description = Text.getText("tileSet.terrain.rockCollected.description");
      this.fileName = "rockGrayCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-9868951);
      this.canRegrow = MapTilesLoader.TileSet.ROCK_GRAY;
      this.regrowIgnoreWeather = true;
      this.regrowRate = 20000;
   }
}
