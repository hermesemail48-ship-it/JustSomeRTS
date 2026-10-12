package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class Melons extends TileSetBase {
   public Melons(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.melons.name");
      this.description = Text.getText("tileSet.terrain.melons.description");
      this.fileName = "melons";
      this.layer = 6;
      this.miniMapColor = new Color(-3090294);
      this.resourceType = ResourceModule.ResourceType.RAW_VEGETABLE;
      this.resourceAmount = 2;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.RED;
      this.canSpread = MapTilesLoader.TileSet.MELONS;
      this.spreadRate = 32000;
      this.spreadOnlyMotivated = true;
      this.essenceAmount = 2;
   }
}
