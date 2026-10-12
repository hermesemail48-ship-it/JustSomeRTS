package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class SandstoneRedCollected extends TileSetBase {
   public SandstoneRedCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.sandstoneCollected.name");
      this.description = Text.getText("tileSet.terrain.sandstoneCollected.description");
      this.fileName = "sandstoneRedCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-162468);
      this.canRegrow = MapTilesLoader.TileSet.SANDSTONE_RED;
      this.regrowIgnoreWeather = true;
      this.regrowRate = 20000;
   }
}
