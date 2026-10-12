package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class CorruptionAccent extends TileSetBase {
   public CorruptionAccent(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.corruption.name");
      this.description = Text.getText("tileSet.terrain.corruption.description");
      this.fileName = "corruption";
      this.layer = 4;
      this.miniMapColor = new Color(-16185067);
   }
}
