package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class SandstoneBlackAccent extends TileSetBase {
   public SandstoneBlackAccent(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.sandstone.name");
      this.description = Text.getText("tileSet.terrain.sandstone.description");
      this.fileName = "sandstoneBlackAccent";
      this.layer = 9;
      this.miniMapColor = new Color(-9342607);
      this.resourceType = ResourceModule.ResourceType.ROCK;
      this.resourceAmount = 10;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.BLACK;
   }
}
