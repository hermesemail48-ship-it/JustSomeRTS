package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class Water extends TileSetBase {
   public Water(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.water.name");
      this.description = Text.getText("tileSet.terrain.water.description");
      this.fileName = "water";
      this.layer = 6;
      this.miniMapColor = new Color(-15891789);
      this.resourceType = ResourceModule.ResourceType.DIRTY_WATER_BUCKET;
      this.resourceAmount = 5;
      this.resourceAmountMax = 10;
      this.resourceColorSet = ResourceModule.ResourceColorSet.BLUE;
   }
}
