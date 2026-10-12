package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class TreesBrownCollected extends TileSetBase {
   public TreesBrownCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.treeStumps.name");
      this.description = Text.getText("tileSet.terrain.treeStumps.description");
      this.fileName = "treesBrownCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-11257855);
      this.canRegrow = MapTilesLoader.TileSet.TREES_BROWN;
      this.regrowRate = 1000;
      this.essenceAmount = 1;
   }
}
