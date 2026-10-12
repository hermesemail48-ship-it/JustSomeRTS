package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class CrystalsPurpleCollected extends TileSetBase {
   public CrystalsPurpleCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.crystalsCollected.name");
      this.description = Text.getText("tileSet.terrain.crystalsCollected.description");
      this.fileName = "crystalsPurpleCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-11651719);
      this.canRegrow = MapTilesLoader.TileSet.CRYSTAL_PURPLE;
      this.regrowRate = 100;
   }
}
