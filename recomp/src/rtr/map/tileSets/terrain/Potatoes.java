package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class Potatoes extends TileSetBase {
   public Potatoes(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.potatoes.name");
      this.description = Text.getText("tileSet.terrain.potatoes.description");
      this.fileName = "potatoes";
      this.layer = 6;
      this.miniMapColor = new Color(-6776986);
      this.resourceType = ResourceModule.ResourceType.RAW_VEGETABLE;
      this.resourceAmount = 2;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.BROWN;
      this.canSpread = MapTilesLoader.TileSet.POTATOES;
      this.spreadRate = 32000;
      this.spreadOnlyMotivated = true;
      this.essenceAmount = 2;
   }
}
