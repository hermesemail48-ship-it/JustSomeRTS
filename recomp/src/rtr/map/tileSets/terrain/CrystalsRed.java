package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class CrystalsRed extends TileSetBase {
   public CrystalsRed(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.crystals.name");
      this.description = Text.getText("tileSet.terrain.crystals.description");
      this.fileName = "crystalsRed";
      this.layer = 6;
      this.miniMapColor = new Color(-2073317);
      this.resourceType = ResourceModule.ResourceType.CRYSTAL;
      this.resourceAmount = 4;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.RED;
      this.canSpread = MapTilesLoader.TileSet.CRYSTAL_RED;
      this.spreadRate = 1000;
      this.essenceAmount = 4;
   }
}
