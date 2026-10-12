package rtr.map.tileSets.objects;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class CullisGate extends TileSetBase {
   public CullisGate(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.OBJECT);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-16719391);
      this.layer = 11;
      this.objectWidth = 7;
      this.objectHeight = 7;
      this.fileName = "cullisGate";
      this.name = Text.getText("object.building." + this.fileName + ".name");
      this.description = Text.getText("object.building." + this.fileName + ".description");
   }
}
