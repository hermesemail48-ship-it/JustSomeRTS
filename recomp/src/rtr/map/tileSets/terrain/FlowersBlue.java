package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class FlowersBlue extends TileSetBase {
   public FlowersBlue(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.flowers.name");
      this.fileName = "flowersBlue";
      this.layer = 6;
      this.miniMapColor = new Color(-16776995);
      this.essenceAmount = 2;
   }
}
