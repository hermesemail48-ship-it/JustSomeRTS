package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class TreesBlueAccent extends TileSetBase {
   public TreesBlueAccent(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.trees.name");
      this.description = Text.getText("tileSet.terrain.trees.description");
      this.fileName = "treesBlueAccent";
      this.layer = 9;
      this.miniMapColor = new Color(-15765416);
      this.resourceType = ResourceModule.ResourceType.WOOD;
      this.resourceAmount = 4;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.GREEN;
   }
}
