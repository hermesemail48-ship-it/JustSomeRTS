package rtr.map.tileSets.objects;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class FoodStorage extends TileSetBase {
   public FoodStorage(int tGID, int uP) {
      super(tGID, uP, MapTilesLoader.TileSetType.OBJECT);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-16719391);
      this.layer = 11;
      this.objectWidth = 6;
      this.objectHeight = 6;
      this.fileName = "foodStorage";
      this.description = Text.getText("object.building." + this.fileName + ".small.description");
      switch (this.upgradePhase) {
         case 0:
            this.name = Text.getText("object.building." + this.fileName + ".small.name");
            break;
         case 1:
            this.name = Text.getText("object.building." + this.fileName + ".base.name");
            this.fileName = this.fileName + "Upgrade1";
            break;
         case 2:
            this.name = Text.getText("object.building." + this.fileName + ".large.name");
            this.fileName = this.fileName + "Upgrade2";
            break;
         case 3:
            this.name = Text.getText("object.building." + this.fileName + ".established.name");
            this.fileName = this.fileName + "Upgrade3";
            break;
         case 4:
            this.name = Text.getText("object.building." + this.fileName + ".reinforced.name");
            this.fileName = this.fileName + "Upgrade4";
      }
   }
}
