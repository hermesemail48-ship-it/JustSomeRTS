package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class TreesBrownDead extends TileSetBase {
   public TreesBrownDead(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.deadTrees.name");
      this.description = Text.getText("tileSet.terrain.deadTrees.description");
      this.fileName = "treesBrownDead";
      this.layer = 9;
      this.miniMapColor = new Color(-11649268);
      this.resourceType = ResourceModule.ResourceType.WOOD;
      this.resourceAmount = 2;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.RED;
      this.canRegrow = MapTilesLoader.TileSet.TREES_BROWN;
      this.regrowRate = 2000;
      this.essenceAmount = 2;
   }
}
