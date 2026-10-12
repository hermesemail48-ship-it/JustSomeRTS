package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class RockRedAccent extends TileSetBase {
   public RockRedAccent(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.rock.name");
      this.description = Text.getText("tileSet.terrain.rock.description");
      this.fileName = "rockRedAccent";
      this.layer = 9;
      this.miniMapColor = new Color(-5735300);
      this.resourceType = ResourceModule.ResourceType.ROCK;
      this.resourceAmount = 10;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.RED;
   }
}
