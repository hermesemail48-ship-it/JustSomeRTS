package rtr.map.tileSets.objects.corrupted;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class CorruptedGraveyardMedium extends TileSetBase {
   public CorruptedGraveyardMedium(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.OBJECT);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-65411);
      this.layer = 11;
      this.objectWidth = 5;
      this.objectHeight = 5;
      this.fileName = "corruptedGraveyardMedium";
      this.name = Text.getText("object.building." + this.fileName + ".name");
      this.description = Text.getText("object.building." + this.fileName + ".description");
   }
}
