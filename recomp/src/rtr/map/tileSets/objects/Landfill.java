package rtr.map.tileSets.objects;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class Landfill extends TileSetBase {
   public Landfill(int tGID, int uP) {
      super(tGID, uP, MapTilesLoader.TileSetType.OBJECT);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-16719391);
      this.layer = 11;
      this.objectWidth = 7;
      this.objectHeight = 7;
      this.fileName = "landfill";
      this.description = Text.getText("object.building." + this.fileName + ".makeshift.description");
      switch (this.upgradePhase) {
         case 0:
            this.name = Text.getText("object.building." + this.fileName + ".makeshift.name");
            break;
         case 1:
            this.name = Text.getText("object.building." + this.fileName + ".base.name");
            this.fileName = this.fileName + "Upgrade1";
            break;
         case 2:
            this.name = Text.getText("object.building." + this.fileName + ".established.name");
            this.fileName = this.fileName + "Upgrade2";
      }
   }
}
