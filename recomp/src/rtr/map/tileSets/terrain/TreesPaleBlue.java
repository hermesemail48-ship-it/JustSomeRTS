package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class TreesPaleBlue extends TileSetBase {
   public TreesPaleBlue(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.trees.name");
      this.description = Text.getText("tileSet.terrain.trees.description");
      this.fileName = "treesPaleBlue";
      this.layer = 9;
      this.miniMapColor = new Color(-9988201);
      this.resourceType = ResourceModule.ResourceType.WOOD;
      this.resourceAmount = 4;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.LIGHT_GRAY;
      this.canSpread = MapTilesLoader.TileSet.TREES_PALE_BLUE;
      this.spreadRate = 2000;
      this.spreadOnlyMotivated = true;
      this.essenceAmount = 3;
   }
}
