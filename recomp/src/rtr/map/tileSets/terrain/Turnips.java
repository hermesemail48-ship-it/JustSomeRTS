package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class Turnips extends TileSetBase {
   public Turnips(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.turnips.name");
      this.description = Text.getText("tileSet.terrain.turnips.description");
      this.fileName = "turnips";
      this.layer = 6;
      this.miniMapColor = new Color(-44096);
      this.resourceType = ResourceModule.ResourceType.RAW_VEGETABLE;
      this.resourceAmount = 2;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.PURPLE;
      this.canSpread = MapTilesLoader.TileSet.TURNIPS;
      this.spreadRate = 32000;
      this.spreadOnlyMotivated = true;
      this.essenceAmount = 2;
   }
}
