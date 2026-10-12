package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class CrystalsRedAccent extends TileSetBase {
   public CrystalsRedAccent(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.crystals.name");
      this.description = Text.getText("tileSet.terrain.crystals.description");
      this.fileName = "crystalsRedAccent";
      this.layer = 6;
      this.miniMapColor = new Color(-338658);
      this.resourceType = ResourceModule.ResourceType.CRYSTAL;
      this.resourceAmount = 4;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.RED;
   }
}
