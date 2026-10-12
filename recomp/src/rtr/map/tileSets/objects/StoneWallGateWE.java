package rtr.map.tileSets.objects;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class StoneWallGateWE extends TileSetBase {
   public StoneWallGateWE(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.OBJECT);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-16719391);
      this.layer = 11;
      this.objectWidth = 3;
      this.objectHeight = 3;
      this.fileName = "stoneWallGateWE";
      this.name = Text.getText("object.building." + this.fileName + ".name");
      this.description = Text.getText("object.building." + this.fileName + ".description");
   }
}
