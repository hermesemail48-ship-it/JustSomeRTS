package rtr.map.tileSets.objects;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class Castle extends TileSetBase {
   public Castle(int tGID, int uP) {
      super(tGID, uP, MapTilesLoader.TileSetType.OBJECT);
   }

   @Override
   public void initialize() {
      this.miniMapColor = new Color(-16719391);
      this.layer = 11;
      this.objectWidth = 13;
      this.objectHeight = 13;
      switch (this.upgradePhase) {
         case 0:
            this.name = Text.getText("object.building.castle.camp.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 1:
            this.name = Text.getText("object.building.castle.largeCamp.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 2:
            this.name = Text.getText("object.building.castle.smallSettlement.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 3:
            this.name = Text.getText("object.building.castle.largeSettlement.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 4:
            this.name = Text.getText("object.building.castle.villageCenter.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 5:
            this.name = Text.getText("object.building.castle.largeVillageCenter.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 6:
            this.name = Text.getText("object.building.castle.establishedVillageCenter.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 7:
            this.name = Text.getText("object.building.castle.smallKeep.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 8:
            this.name = Text.getText("object.building.castle.largeKeep.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 9:
            this.name = Text.getText("object.building.castle.establishedKeep.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 10:
            this.name = Text.getText("object.building.castle.smallStronghold.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 11:
            this.name = Text.getText("object.building.castle.largeStronghold.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 12:
            this.name = Text.getText("object.building.castle.establishedStronghold.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 13:
            this.name = Text.getText("object.building.castle.smallCastle.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
            break;
         case 14:
            this.name = Text.getText("object.building.castle.largeCastle.name");
            this.fileName = "castle" + (this.upgradePhase + 1);
      }

      this.description = Text.getText("object.building.castle.camp.description");
   }
}
