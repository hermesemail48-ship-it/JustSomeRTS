package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class TreesLavenderDead extends TileSetBase {
   public TreesLavenderDead(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.deadTrees.name");
      this.description = Text.getText("tileSet.terrain.deadTrees.description");
      this.fileName = "treesLavenderDead";
      this.layer = 9;
      this.miniMapColor = new Color(-8231038);
      this.resourceType = ResourceModule.ResourceType.WOOD;
      this.resourceAmount = 2;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.BLUE;
      this.canRegrow = MapTilesLoader.TileSet.TREES_LAVENDER;
      this.regrowRate = 2000;
      this.essenceAmount = 2;
   }
}
