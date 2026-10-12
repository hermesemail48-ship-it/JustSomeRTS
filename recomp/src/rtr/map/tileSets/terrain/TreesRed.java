package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class TreesRed extends TileSetBase {
   public TreesRed(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.trees.name");
      this.description = Text.getText("tileSet.terrain.trees.description");
      this.fileName = "treesRed";
      this.layer = 9;
      this.miniMapColor = new Color(-10407145);
      this.resourceType = ResourceModule.ResourceType.WOOD;
      this.resourceAmount = 4;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.GRAY;
      this.canSpread = MapTilesLoader.TileSet.TREES_RED;
      this.spreadRate = 1000;
      this.spreadOnlyMotivated = true;
      this.essenceAmount = 3;
   }
}
