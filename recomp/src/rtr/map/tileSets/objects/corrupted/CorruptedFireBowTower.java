package rtr.map.tileSets.objects.corrupted;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class CorruptedFireBowTower extends TileSetBase {
   public CorruptedFireBowTower(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.OBJECT);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-65411);
      this.layer = 11;
      this.objectWidth = 3;
      this.objectHeight = 3;
      this.fileName = "corruptedFireBowTower";
      this.name = Text.getText("object.building." + this.fileName + ".name");
      this.description = Text.getText("object.building." + this.fileName + ".description");
   }
}
