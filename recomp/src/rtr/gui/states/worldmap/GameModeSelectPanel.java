package rtr.gui.states.worldmap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.gui.states.WorldMapGUIPanelBase;
import rtr.particles.ParticleModule;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class GameModeSelectPanel extends WorldMapGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/gameState/gameModeSelectOverlayPanel.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private GUIButtonMainMenu nightmare;
   private GUIButtonMainMenu survival;
   private GUIButtonMainMenu traditional;
   private GUIButtonMainMenu peaceful;
   private GUIButtonMainMenu custom;
   private GUIButtonMainMenu sandbox;

   public GameModeSelectPanel(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
      this.nightmare = new GUIButtonMainMenu("nightmare", Text.getText("worldMapGameModeSelectPanel.button.nightmare"), 1, 2);
      this.guiButtons.add(this.nightmare);
      this.survival = new GUIButtonMainMenu("survival", Text.getText("worldMapGameModeSelectPanel.button.survival"), 1, 1);
      this.guiButtons.add(this.survival);
      this.traditional = new GUIButtonMainMenu("traditional", Text.getText("worldMapGameModeSelectPanel.button.traditional"), 1, 1);
      this.guiButtons.add(this.traditional);
      this.peaceful = new GUIButtonMainMenu("peaceful", Text.getText("worldMapGameModeSelectPanel.button.peaceful"), 1);
      this.guiButtons.add(this.peaceful);
      this.custom = new GUIButtonMainMenu("custom", Text.getText("worldMapGameModeSelectPanel.button.custom"), 1);
      this.guiButtons.add(this.custom);
      this.sandbox = new GUIButtonMainMenu("sandbox", Text.getText("worldMapGameModeSelectPanel.button.sandbox"), 1);
      this.guiButtons.add(this.sandbox);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.panel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() / 2 - this.panel.getHeight() / 2;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);

      for (int i = 0; i < 10; i++) {
         this.particle
            .newParticleGroup(this.x + 26 + Utilities.randomInt(106), this.y + 28, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, true);
         this.particle
            .newParticleGroup(this.x + 26 + Utilities.randomInt(106), this.y + 71, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, true);
      }

      this.particle
         .newParticleGroup(this.x + 13, this.y + 40 + Utilities.randomInt(24), ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, true);
      this.particle
         .newParticleGroup(this.x + 143, this.y + 40 + Utilities.randomInt(24), ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, true);
      int randomA = Utilities.randomInt(10);
      this.particle
         .newParticleGroup(this.x + 143 - randomA, this.y + 63 + randomA, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, true);
      this.particle
         .newParticleGroup(this.x + 12 + randomA, this.y + 63 + randomA, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, true);
      this.particle
         .newParticleGroup(this.x + 143 - randomA, this.y + 36 - randomA, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, true);
      this.particle
         .newParticleGroup(this.x + 12 + randomA, this.y + 36 - randomA, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED, true);
      if (Utilities.randomInt(5) == 0) {
         this.particle
            .newParticleGroup(
               this.x + 28 + Utilities.randomInt(100), this.y + 28, ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.FIRE_RED, true
            );
         this.particle
            .newParticleGroup(
               this.x + 28 + Utilities.randomInt(100), this.y + 70, ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.FIRE_RED, true
            );
         this.particle
            .newParticleGroup(
               this.x + 13, this.y + 40 + Utilities.randomInt(24), ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.FIRE_RED, true
            );
         this.particle
            .newParticleGroup(
               this.x + 143, this.y + 40 + Utilities.randomInt(24), ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.FIRE_RED, true
            );
      }

      if (Utilities.randomInt(6) == 0) {
         this.particle
            .newParticleGroup(
               this.x + 26 + Utilities.randomInt(105),
               this.y + 214,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               true
            );
         this.particle
            .newParticleGroup(
               this.x + 26 + Utilities.randomInt(105),
               this.y + 214,
               ParticleModule.ParticleGroup.FLOATING_MAGIC_SQUARES,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               true
            );
      }

      this.nightmare.render(this.g, this.mouse, this.x + 10, this.y + 23, false, debug);
      this.survival.render(this.g, this.mouse, this.x + 10, this.y + 78, false, debug);
      this.traditional.render(this.g, this.mouse, this.x + 10, this.y + 133, false, debug);
      this.peaceful.render(this.g, this.mouse, this.x + 10, this.y + 188, false, debug);
      this.custom.render(this.g, this.mouse, this.x + 10, this.y + 243, false, debug);
      this.sandbox.render(this.g, this.mouse, this.x + 10, this.y + 298, false, debug);
      if (this.nightmare.intersects(this.mouse)) {
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 + 78,
               this.y + 18,
               Text.getText("worldMapGameModeSelectPanel.header.nightmare"),
               Text.FontType.HEADER,
               6,
               true
            );
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 - 68,
               this.y + 75,
               Text.wrapString(Text.getText("worldMapGameModeSelectPanel.description.nightmare"), Text.FontType.BODY, 2, 290),
               Text.FontType.BODY,
               2
            );
      } else if (this.survival.intersects(this.mouse)) {
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 + 78, this.y + 18, Text.getText("worldMapGameModeSelectPanel.header.survival"), Text.FontType.HEADER, 6, true
            );
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 - 68,
               this.y + 75,
               Text.wrapString(Text.getText("worldMapGameModeSelectPanel.description.survival"), Text.FontType.BODY, 2, 290),
               Text.FontType.BODY,
               2
            );
      } else if (this.traditional.intersects(this.mouse)) {
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 + 78,
               this.y + 18,
               Text.getText("worldMapGameModeSelectPanel.header.traditional"),
               Text.FontType.HEADER,
               6,
               true
            );
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 - 68,
               this.y + 75,
               Text.wrapString(Text.getText("worldMapGameModeSelectPanel.description.traditional"), Text.FontType.BODY, 2, 290),
               Text.FontType.BODY,
               2
            );
      } else if (this.peaceful.intersects(this.mouse)) {
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 + 78, this.y + 18, Text.getText("worldMapGameModeSelectPanel.header.peaceful"), Text.FontType.HEADER, 6, true
            );
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 - 68,
               this.y + 75,
               Text.wrapString(Text.getText("worldMapGameModeSelectPanel.description.peaceful"), Text.FontType.BODY, 2, 290),
               Text.FontType.BODY,
               2
            );
      } else if (this.custom.intersects(this.mouse)) {
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 + 78, this.y + 18, Text.getText("worldMapGameModeSelectPanel.header.custom"), Text.FontType.HEADER, 6, true
            );
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 - 68,
               this.y + 75,
               Text.wrapString(Text.getText("worldMapGameModeSelectPanel.description.custom"), Text.FontType.BODY, 2, 290),
               Text.FontType.BODY,
               2
            );
      } else if (this.sandbox.intersects(this.mouse)) {
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 + 78, this.y + 18, Text.getText("worldMapGameModeSelectPanel.header.sandbox"), Text.FontType.HEADER, 6, true
            );
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 - 68,
               this.y + 75,
               Text.wrapString(Text.getText("worldMapGameModeSelectPanel.description.sandbox"), Text.FontType.BODY, 2, 290),
               Text.FontType.BODY,
               2
            );
      } else {
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 + 78,
               this.y + 18,
               Text.getText("worldMapGameModeSelectPanel.header.selectGameMode"),
               Text.FontType.HEADER,
               6,
               true
            );
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 - 68,
               this.y + 75,
               Text.wrapString(Text.getText("worldMapGameModeSelectPanel.description.selectGameMode"), Text.FontType.BODY, 2, 290),
               Text.FontType.BODY,
               2
            );
      }
   }

   @Override
   public int getCenterX() {
      return this.x + this.panel.getWidth() / 2;
   }

   @Override
   public int getCenterY() {
      return this.y + this.panel.getHeight() / 2;
   }

   @Override
   public int getLeftX() {
      return this.x;
   }

   @Override
   public int getTopY() {
      return this.y;
   }

   @Override
   public int getRightX() {
      return this.x + this.panel.getWidth();
   }

   @Override
   public int getBottomY() {
      return this.y + this.panel.getHeight();
   }
}
