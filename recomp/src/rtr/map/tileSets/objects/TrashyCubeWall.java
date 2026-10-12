package rtr.map.tileSets.objects;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class TrashyCubeWall extends TileSetBase {
   public TrashyCubeWall(int tGID, int uP) {
      super(tGID, uP, MapTilesLoader.TileSetType.WALL);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-16719391);
      this.layer = 11;
      this.objectWidth = 1;
      this.objectHeight = 1;
      this.fileName = "trashyCubeWall";
      this.description = Text.getText("object.wall." + this.fileName + ".description");
      this.name = Text.getText("object.wall." + this.fileName + ".name");
      switch (this.upgradePhase) {
         case 0:
            this.fileName = this.fileName + "Outline";
            break;
         case 1:
            this.fileName = this.fileName + "Construction";
      }
   }
}
