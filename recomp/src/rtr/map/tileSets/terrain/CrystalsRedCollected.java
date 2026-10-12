package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class CrystalsRedCollected extends TileSetBase {
   public CrystalsRedCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.crystalsCollected.name");
      this.description = Text.getText("tileSet.terrain.crystalsCollected.description");
      this.fileName = "crystalsRedCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-10279908);
      this.canRegrow = MapTilesLoader.TileSet.CRYSTAL_RED;
      this.regrowRate = 100;
      this.essenceAmount = 1;
   }
}
