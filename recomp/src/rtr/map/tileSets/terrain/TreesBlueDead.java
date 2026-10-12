package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class TreesBlueDead extends TileSetBase {
   public TreesBlueDead(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.deadTrees.name");
      this.description = Text.getText("tileSet.terrain.deadTrees.description");
      this.fileName = "treesBlueDead";
      this.layer = 9;
      this.miniMapColor = new Color(-15968700);
      this.resourceType = ResourceModule.ResourceType.WOOD;
      this.resourceAmount = 2;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.GREEN;
      this.canRegrow = MapTilesLoader.TileSet.TREES_BLUE;
      this.regrowRate = 2000;
      this.essenceAmount = 2;
   }
}
