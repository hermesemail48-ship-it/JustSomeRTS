package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class TreesLavenderCollected extends TileSetBase {
   public TreesLavenderCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.treeStumps.name");
      this.description = Text.getText("tileSet.terrain.treeStumps.description");
      this.fileName = "treesLavenderCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-16760489);
      this.canRegrow = MapTilesLoader.TileSet.TREES_LAVENDER;
      this.regrowRate = 1000;
      this.essenceAmount = 1;
   }
}
