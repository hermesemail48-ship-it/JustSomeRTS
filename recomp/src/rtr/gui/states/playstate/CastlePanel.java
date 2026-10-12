package rtr.gui.states.playstate;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIPanelButton;
import rtr.gui.buttons.GUIRoundButton;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class CastlePanel extends PlayStateGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/gameState/castlePanel.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private GUIPanelButton placeCastle;
   private GUIRoundButton hidePanel;

   public CastlePanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
      this.placeCastle = new GUIPanelButton("placeCastle", 1, "camp", Text.getText("playCastlePanel.button.placeCamp"), false);
      this.guiButtons.add(this.placeCastle);
      this.placeCastle.setFlashing(true);
      this.hidePanel = new GUIRoundButton("hidePanel", "buttonDismantle", 0);
      this.hidePanel.setToolTip(Text.getText("playCastlePanel.button.tooltip.hidePanelHeader"), Text.getText("playCastlePanel.button.tooltip.hidePanel"));
      this.guiButtons.add(this.hidePanel);
   }

   public void render(boolean debug) throws SlickException {
      if (this.controller.isMinimapPanelEnabled()) {
         this.x = this.controller.getResourceAndInfluenceBar().getLeftX();
      } else {
         this.x = this.controller.getResourceAndInfluenceBar().getRightX() - this.panel.getWidth();
      }

      if (this.x + this.panel.getWidth() > this.controller.getRightPanel().getLeftX() - 46) {
         this.x = this.x - Math.abs(this.x + this.panel.getWidth() - this.controller.getRightPanel().getLeftX() + 46);
      }

      this.y = 169;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + 133, this.y + 3, Text.getText("playCastlePanel.header"), Text.FontType.HEADER, 2, true);
      this.font.drawString(this.x + 133, this.y + 31, Text.getText("playCastlePanel.body"), Text.FontType.BODY, 1, true);
      this.placeCastle.render(this.g, this.mouse, this.x + 39, this.y + 85, false, debug);
      if (Game.getCS().getGameMode() == GameModeTemplateBase.GameMode.SANDBOX) {
         this.hidePanel.render(this.g, this.mouse, this.x + 229, this.y + 30, debug);
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
