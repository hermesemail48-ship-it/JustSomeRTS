package rtr.map.tileSets.objects.corrupted;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class CorruptedGraveyardLarge extends TileSetBase {
   public CorruptedGraveyardLarge(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.OBJECT);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-65411);
      this.layer = 11;
      this.objectWidth = 7;
      this.objectHeight = 7;
      this.fileName = "corruptedGraveyardLarge";
      this.name = Text.getText("object.building." + this.fileName + ".name");
      this.description = Text.getText("object.building." + this.fileName + ".description");
   }
}
