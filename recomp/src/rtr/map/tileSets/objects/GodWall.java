package rtr.map.tileSets.objects;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class GodWall extends TileSetBase {
   public GodWall(int tGID, int uP) {
      super(tGID, uP, MapTilesLoader.TileSetType.WALL);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-16719391);
      this.layer = 11;
      this.objectWidth = 1;
      this.objectHeight = 1;
      this.fileName = "godWall";
      this.name = Text.getText("object.wall." + this.fileName + ".name");
      this.description = Text.getText("object.wall." + this.fileName + ".description");
      switch (this.upgradePhase) {
         case 0:
            this.fileName = this.fileName + "Outline";
            break;
         case 1:
            this.fileName = this.fileName + "Construction";
      }
   }
}
