package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class TreesRedDead extends TileSetBase {
   public TreesRedDead(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.deadTrees.name");
      this.description = Text.getText("tileSet.terrain.deadTrees.description");
      this.fileName = "treesRedDead";
      this.layer = 9;
      this.miniMapColor = new Color(-10407145);
      this.resourceType = ResourceModule.ResourceType.WOOD;
      this.resourceAmount = 2;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.GRAY;
      this.canRegrow = MapTilesLoader.TileSet.TREES_RED;
      this.regrowRate = 2000;
      this.essenceAmount = 2;
   }
}
