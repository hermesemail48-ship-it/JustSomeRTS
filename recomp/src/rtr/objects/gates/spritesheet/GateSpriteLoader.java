package rtr.objects.gates.spritesheet;

import java.util.EnumMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.objects.gates.GateBase;
import rtr.objects.gates.spritesheet.templates.GateSpriteTemplateBase;
import rtr.objects.gates.spritesheet.templates.GateSpriteTemplateGate;

public class GateSpriteLoader {
   private static final int ANIMATION_COUNT = 2;
   private static final int ANIMATION_FRAMES = 8;
   private static final int ROW_ONE_SIZE = 48;
   private static final int ROW_TWO_SIZE = 64;
   private Image towerBaseSpriteSheet;
   private EnumMap<GateBase.GateType, SpriteSheet> spriteSheets = new EnumMap<>(GateBase.GateType.class);
   private EnumMap<GateBase.GateType, GateSpriteTemplateBase> spriteTemplates = new EnumMap<>(GateBase.GateType.class);

   public GateSpriteLoader() throws SlickException {
      this.towerBaseSpriteSheet = ImageLoader.getImage("res/gates/gates.png");
      this.spriteTemplates.put(GateBase.GateType.WOOD_GATE_NS, new GateSpriteTemplateGate());
      this.spriteTemplates.put(GateBase.GateType.WOOD_GATE_WE, new GateSpriteTemplateGate());
      this.spriteTemplates.put(GateBase.GateType.STONE_GATE_NS, new GateSpriteTemplateGate());
      this.spriteTemplates.put(GateBase.GateType.STONE_GATE_WE, new GateSpriteTemplateGate());
      this.loadGate(GateBase.GateType.WOOD_GATE_NS, 0, 48);
      this.loadGate(GateBase.GateType.WOOD_GATE_WE, 1, 48);
      this.loadGate(GateBase.GateType.STONE_GATE_NS, 2, 48);
      this.loadGate(GateBase.GateType.STONE_GATE_WE, 3, 48);
   }

   private void loadGate(GateBase.GateType type, int posY, int sSize) {
      posY = posY * sSize * 2;
      this.spriteSheets.put(type, new SpriteSheet(this.towerBaseSpriteSheet.getSubImage(0, posY, 8 * sSize, 2 * sSize), sSize, sSize));
   }

   public Image getBaseSpriteSheet() throws SlickException {
      return this.towerBaseSpriteSheet;
   }

   public GateSprite createNewGateSprite(GateBase.GateType type) throws SlickException {
      return this.spriteTemplates.containsKey(type) ? new GateSprite(this.spriteSheets.get(type), this.spriteTemplates.get(type)) : null;
   }
}
