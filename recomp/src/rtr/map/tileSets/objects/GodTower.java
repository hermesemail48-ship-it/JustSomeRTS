package rtr.map.tileSets.objects;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class GodTower extends TileSetBase {
   public GodTower(int tGID, int uP) {
      super(tGID, uP, MapTilesLoader.TileSetType.OBJECT);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-16719391);
      this.layer = 11;
      this.objectWidth = 3;
      this.objectHeight = 3;
      this.fileName = "godTower";
      this.name = Text.getText("object.building." + this.fileName + ".base.name");
      this.description = Text.getText("object.building." + this.fileName + ".base.description");
   }
}
