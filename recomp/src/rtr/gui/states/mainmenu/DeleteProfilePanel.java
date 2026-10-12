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

public class DeleteProfilePanel extends MainMenuGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/mainMenu/genericPanel.png");
   private GUIButtonMainMenu no = new GUIButtonMainMenu("no", Text.getText("mainMenuDeleteProfilePanel.button.no"), 0);
   private GUIButtonMainMenu yes;
   private GUIButtonMainMenu really;
   private boolean confirm;

   public DeleteProfilePanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.guiButtons.add(this.no);
      this.yes = new GUIButtonMainMenu("yes", Text.getText("mainMenuDeleteProfilePanel.button.yes"), 0);
      this.guiButtons.add(this.yes);
      this.really = new GUIButtonMainMenu("really", Text.getText("mainMenuDeleteProfilePanel.button.yesConfirm"), 2);
      this.guiButtons.add(this.really);
   }

   public void render(boolean debug) {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.panel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() / 2 - this.panel.getHeight() / 2;
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + this.panel.getWidth() / 2, this.y + 16, Text.getText("mainMenuDeleteProfilePanel.header"), Text.FontType.HEADER, 3, true);
      if (this.confirm) {
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2,
               this.y + 60,
               Text.wrapString(Text.getText("mainMenuDeleteProfilePanel.bodyConfirm"), Text.FontType.BODY, 2, 480),
               Text.FontType.BODY,
               2,
               true
            );
         this.really.render(this.g, this.mouse, this.x + 182, this.y + this.panel.getHeight() + 55, false, debug);
      } else {
         Text.setVariableText("profileName", this.controller.getSelectProfilePanel().getSelectedProfileName());
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2,
               this.y + 60,
               Text.wrapString(Text.getText("mainMenuDeleteProfilePanel.body"), Text.FontType.BODY, 2, 480),
               Text.FontType.BODY,
               2,
               true
            );
         this.really.hideButton();
      }

      this.no.render(this.g, this.mouse, this.x + 100, this.y + this.panel.getHeight(), false, debug);
      this.yes.render(this.g, this.mouse, this.x + 344, this.y + this.panel.getHeight(), false, debug);
   }

   public void setConfirm(boolean b) {
      this.confirm = b;
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
