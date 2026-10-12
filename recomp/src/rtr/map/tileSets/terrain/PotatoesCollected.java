package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class PotatoesCollected extends TileSetBase {
   public PotatoesCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.potatoes.name");
      this.description = Text.getText("tileSet.terrain.potatoes.description");
      this.fileName = "potatoesCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-15902720);
      this.canRegrow = MapTilesLoader.TileSet.POTATOES;
      this.regrowRate = 24000;
      this.essenceAmount = 1;
   }
}
