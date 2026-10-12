package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class FlowersRed extends TileSetBase {
   public FlowersRed(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.flowers.name");
      this.fileName = "flowersRed";
      this.layer = 6;
      this.miniMapColor = new Color(-2293760);
      this.essenceAmount = 2;
   }
}
