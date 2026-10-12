package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;

public class PotatoesHolyCollected extends TileSetBase {
   public PotatoesHolyCollected(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.potatoesHoly.name");
      this.description = Text.getText("tileSet.terrain.potatoesHoly.description");
      this.fileName = "potatoesHolyCollected";
      this.layer = 6;
      this.miniMapColor = new Color(-16040103);
      this.canRegrow = MapTilesLoader.TileSet.HOLY_POTATOES;
      this.regrowRate = 24000;
      this.essenceAmount = 1;
   }
}
