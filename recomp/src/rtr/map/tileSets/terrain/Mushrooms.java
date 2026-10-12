package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class Mushrooms extends TileSetBase {
   public Mushrooms(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.mushrooms.name");
      this.description = Text.getText("tileSet.terrain.mushrooms.description");
      this.fileName = "mushrooms";
      this.layer = 6;
      this.miniMapColor = new Color(-8241138);
      this.resourceType = ResourceModule.ResourceType.RAW_VEGETABLE;
      this.resourceAmount = 2;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.DARK_BROWN;
      this.canSpread = MapTilesLoader.TileSet.MUSHROOMS;
      this.spreadRate = 32000;
      this.spreadOnlyMotivated = true;
      this.essenceAmount = 2;
   }
}
