package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class TreesBlueCollected extends TileSetBase {
   public TreesBlueCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.treeStumps.name");
      this.description = Text.getText("tileSet.terrain.treeStumps.description");
      this.fileName = "treesBlueCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-13875933);
      this.canRegrow = MapTilesLoader.TileSet.TREES_BLUE;
      this.regrowRate = 1000;
      this.essenceAmount = 1;
   }
}
