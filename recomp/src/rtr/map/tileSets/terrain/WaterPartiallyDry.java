package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class WaterPartiallyDry extends TileSetBase {
   public WaterPartiallyDry(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.mud.name");
      this.fileName = "waterPartiallyDry";
      this.layer = 6;
      this.miniMapColor = new Color(-14398631);
      this.resourceType = ResourceModule.ResourceType.DIRTY_WATER_BUCKET;
      this.resourceAmount = 2;
      this.resourceAmountMax = 10;
      this.resourceColorSet = ResourceModule.ResourceColorSet.BLUE;
   }
}
