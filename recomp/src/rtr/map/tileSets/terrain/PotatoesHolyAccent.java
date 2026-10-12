package rtr.map.tileSets.terrain;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.map.tileSets.TileSetBase;
import rtr.resources.ResourceModule;

public class PotatoesHolyAccent extends TileSetBase {
   public PotatoesHolyAccent(int tGID) {
      super(tGID, MapTilesLoader.TileSetType.TERRAIN);
   }

   @Override
   public void initialize() {
      this.name = Text.getText("tileSet.terrain.potatoesHoly.name");
      this.description = Text.getText("tileSet.terrain.potatoesHoly.description");
      this.fileName = "potatoesHoly";
      this.layer = 6;
      this.miniMapColor = new Color(-3684409);
      this.resourceType = ResourceModule.ResourceType.RAW_VEGETABLE;
      this.resourceAmount = 2;
      this.resourceAmountMax = (byte)(this.resourceAmount * 2);
      this.resourceColorSet = ResourceModule.ResourceColorSet.WHITE;
   }
}
