package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class RockBrownAccent extends TileSetBase {
   public RockBrownAccent(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.rock.name");
      this.description = Text.getText("tileSet.terrain.rock.description");
      this.fileName = "rockBrownAccent";
      this.layer = 9;
      this.miniMapColor = new Color(-6846084);
      this.resourceType = ResourceModule.ResourceType.ROCK;
      this.resourceAmount = 10;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.BROWN;
   }
}
