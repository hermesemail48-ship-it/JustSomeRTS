package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class CactusGreenCollected extends TileSetBase {
   public CactusGreenCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.cactus.name");
      this.description = Text.getText("tileSet.terrain.cactus.description");
      this.fileName = "cactusGreenCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-15450098);
      this.canRegrow = MapTilesLoader.TileSet.CACTUS_GREEN;
      this.regrowRate = 24000;
      this.essenceAmount = 1;
   }
}
