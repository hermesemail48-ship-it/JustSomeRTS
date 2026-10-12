package rtr.gui.states.mainmenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MainMenuGUIPanelBase;
import rtr.system.ScaleControl;

public class ExitGamePanel extends MainMenuGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/mainMenu/genericPanel.png");
   private GUIButtonMainMenu exit = new GUIButtonMainMenu("exit", Text.getText("mainMenuRestartGamePanel.button.exit"), 0, 1);

   public ExitGamePanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.guiButtons.add(this.exit);
   }

   public void render(boolean debug) {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.panel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() / 2 - this.panel.getHeight() / 2;
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + this.panel.getWidth() / 2, this.y + 16, Text.getText("mainMenuRestartGamePanel.header"), Text.FontType.HEADER, 3, true);
      this.font
         .drawString(
            this.x + this.panel.getWidth() / 2,
            this.y + 60,
            Text.wrapString(Text.getText("mainMenuRestartGamePanel.body"), Text.FontType.BODY, 2, 480),
            Text.FontType.BODY,
            2,
            true
         );
      this.exit.render(this.g, this.mouse, this.x + 222, this.y + this.panel.getHeight(), false, debug);
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
