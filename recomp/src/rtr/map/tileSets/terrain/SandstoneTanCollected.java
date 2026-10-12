package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class SandstoneTanCollected extends TileSetBase {
   public SandstoneTanCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.sandstoneCollected.name");
      this.description = Text.getText("tileSet.terrain.sandstoneCollected.description");
      this.fileName = "sandstoneTanCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-5593217);
      this.canRegrow = MapTilesLoader.TileSet.SANDSTONE_TAN;
      this.regrowIgnoreWeather = true;
      this.regrowRate = 20000;
   }
}
