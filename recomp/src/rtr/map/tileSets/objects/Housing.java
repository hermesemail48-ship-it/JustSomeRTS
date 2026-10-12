package rtr.map.tileSets.objects;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class Housing extends TileSetBase {
   public Housing(int tGID, int uP) {
      super(tGID, uP, MapTilesLoader.TileSetType.OBJECT);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-16719391);
      this.layer = 11;
      this.objectWidth = 7;
      this.objectHeight = 7;
      this.fileName = "housing";
      this.description = Text.getText("object.building." + this.fileName + ".makeshift.description");
      switch (this.upgradePhase) {
         case 0:
            this.name = Text.getText("object.building." + this.fileName + ".makeshift.name");
            break;
         case 1:
            this.name = Text.getText("object.building." + this.fileName + ".baseHighOccupancy.name");
            this.fileName = this.fileName + "Upgrade1Occupancy";
            break;
         case 2:
            this.name = Text.getText("object.building." + this.fileName + ".largeHighOccupancy.name");
            this.fileName = this.fileName + "Upgrade2Occupancy";
            break;
         case 3:
            this.name = Text.getText("object.building." + this.fileName + ".establishedHighOccupancy.name");
            this.fileName = this.fileName + "Upgrade3Occupancy";
            break;
         case 4:
            this.name = Text.getText("object.building." + this.fileName + ".reinforcedHighOccupancy.name");
            this.fileName = this.fileName + "Upgrade4Occupancy";
            break;
         case 5:
            this.name = Text.getText("object.building." + this.fileName + ".baseStandard.name");
            this.fileName = this.fileName + "Upgrade1Standard";
            break;
         case 6:
            this.name = Text.getText("object.building." + this.fileName + ".largeStandard.name");
            this.fileName = this.fileName + "Upgrade2Standard";
            break;
         case 7:
            this.name = Text.getText("object.building." + this.fileName + ".establishedStandard.name");
            this.fileName = this.fileName + "Upgrade3Standard";
            break;
         case 8:
            this.name = Text.getText("object.building." + this.fileName + ".reinforcedStandard.name");
            this.fileName = this.fileName + "Upgrade4Standard";
            break;
         case 9:
            this.name = Text.getText("object.building." + this.fileName + ".baseHighQuality.name");
            this.fileName = this.fileName + "Upgrade1Quality";
            break;
         case 10:
            this.name = Text.getText("object.building." + this.fileName + ".largeHighQuality.name");
            this.fileName = this.fileName + "Upgrade2Quality";
            break;
         case 11:
            this.name = Text.getText("object.building." + this.fileName + ".establishedHighQuality.name");
            this.fileName = this.fileName + "Upgrade3Quality";
            break;
         case 12:
            this.name = Text.getText("object.building." + this.fileName + ".reinforcedHighQuality.name");
            this.fileName = this.fileName + "Upgrade4Quality";
      }
   }
}
