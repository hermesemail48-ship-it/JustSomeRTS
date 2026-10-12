package rtr.gui.states.playstate;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.console.Console;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonHide;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.system.ScaleControl;

public class ConsolePanel extends PlayStateGUIPanelBase {
   private Image panelHalf;
   private Image panel = ImageLoader.getImage("res/GUI/gameState/consolePanel.png");
   private Rectangle halfMask;
   private Rectangle mask;
   private GUIButtonHide hidePanel;
   private boolean consoleHidden;
   private boolean consoleHalf;

   public ConsolePanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panelHalf = ImageLoader.getImage("res/GUI/gameState/consolePanelHalf.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);
      this.halfMask = new Rectangle(0.0F, 0.0F, this.panelHalf.getWidth(), this.panelHalf.getHeight());
      this.masks.add(this.halfMask);
      this.hidePanel = new GUIButtonHide("hidePanel", true);
      this.hidePanel.setToolTip(Text.getText("playConsolePanel.button.tooltip.hidePanelHeader"));
      this.guiButtons.add(this.hidePanel);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceCenterX() - this.panel.getWidth() / 2;
      this.y = 0;
      if (this.consoleHalf) {
         this.y = ScaleControl.getInterfaceHeight() - this.panelHalf.getHeight() - 3;
      } else {
         this.y = ScaleControl.getInterfaceHeight() - this.panel.getHeight() - 3;
      }

      if (this.consoleHidden) {
         this.y = ScaleControl.getInterfaceHeight() - 10;
      }

      if (this.consoleHalf) {
         this.halfMask.setX(this.x);
         this.halfMask.setY(this.y);
      } else {
         this.mask.setX(this.x);
         this.mask.setY(this.y);
      }

      if (this.consoleHalf) {
         this.panelHalf.draw(this.x, this.y);
      } else {
         this.panel.draw(this.x, this.y);
      }

      if (this.consoleHalf) {
         Console.renderConsole(this.g, this.x + 10, this.y + this.panelHalf.getHeight() - 13, 5);
      } else {
         Console.renderConsole(this.g, this.x + 10, this.y + this.panel.getHeight() - 13, 11);
      }

      if (this.consoleHidden) {
         this.hidePanel.render(this.g, this.mouse, this.x + this.panel.getWidth() - 37, this.y - 27, false, debug);
      } else {
         this.hidePanel.render(this.g, this.mouse, this.x + this.panel.getWidth() - 37, this.y + 9, false, debug);
      }
   }

   public void toggleConsolePanelHidden() {
      if (this.consoleHidden) {
         this.consoleHidden = false;
         this.consoleHalf = false;
      } else if (!this.consoleHidden && !this.consoleHalf) {
         this.consoleHalf = true;
      } else if (this.consoleHalf) {
         this.consoleHidden = true;
      }

      this.hidePanel.cycleButton();
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
