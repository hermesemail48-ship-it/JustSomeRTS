package rtr.map.tileSets.objects.corrupted;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class CorruptedLargeFirePit extends TileSetBase {
   public CorruptedLargeFirePit(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.OBJECT);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-65411);
      this.layer = 11;
      this.objectWidth = 1;
      this.objectHeight = 1;
      this.fileName = "corruptedLargeFirePit";
      this.name = Text.getText("object.building." + this.fileName + ".name");
      this.description = Text.getText("object.building." + this.fileName + ".description");
   }
}
