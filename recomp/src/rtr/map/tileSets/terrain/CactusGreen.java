package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class CactusGreen extends TileSetBase {
   public CactusGreen(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.cactus.name");
      this.description = Text.getText("tileSet.terrain.cactus.description");
      this.fileName = "cactusGreen";
      this.layer = 6;
      this.miniMapColor = new Color(-14056676);
      this.resourceType = ResourceModule.ResourceType.RAW_VEGETABLE;
      this.resourceAmount = 2;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.GREEN;
      this.canSpread = MapTilesLoader.TileSet.CACTUS_GREEN;
      this.spreadRate = 32000;
      this.spreadOnlyMotivated = true;
      this.essenceAmount = 2;
   }
}
