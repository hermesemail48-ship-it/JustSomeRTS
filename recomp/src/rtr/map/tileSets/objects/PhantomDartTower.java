package rtr.map.tileSets.objects;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class PhantomDartTower extends TileSetBase {
   public PhantomDartTower(int tGID, int uP) {
      super(tGID, uP, MapTilesLoader.TileSetType.OBJECT);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-16719391);
      this.layer = 11;
      this.objectWidth = 1;
      this.objectHeight = 1;
      this.fileName = "phantomDartTower";
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
            break;
         case 3:
            this.name = "Phantom Fire Dart Tower";
            this.fileName = this.fileName + "Upgrade3Fire";
            break;
         case 4:
            this.name = "Phantom Lightning Dart Tower";
            this.fileName = this.fileName + "Upgrade3Lightning";
            break;
         case 5:
            this.name = "Phantom Ice Dart Tower";
            this.fileName = this.fileName + "Upgrade3Ice";
      }
   }
}
