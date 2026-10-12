package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class MelonsCollected extends TileSetBase {
   public MelonsCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.melons.name");
      this.description = Text.getText("tileSet.terrain.melons.description");
      this.fileName = "melonsCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-15902720);
      this.canRegrow = MapTilesLoader.TileSet.MELONS;
      this.regrowRate = 24000;
      this.essenceAmount = 1;
   }
}
