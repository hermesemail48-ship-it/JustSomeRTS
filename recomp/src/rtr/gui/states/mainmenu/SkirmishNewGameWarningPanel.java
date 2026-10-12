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

public class SkirmishNewGameWarningPanel extends MainMenuGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/mainMenu/genericPanel.png");
   private GUIButtonMainMenu no = new GUIButtonMainMenu("no", Text.getText("mainMenuSkirmishNewGameWarningPanel.button.no"), 0);
   private GUIButtonMainMenu yes;
   private GUIButtonMainMenu really;
   private boolean confirmation;
   private String mapName;
   private String gameMode;

   public SkirmishNewGameWarningPanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.guiButtons.add(this.no);
      this.yes = new GUIButtonMainMenu("yes", Text.getText("mainMenuSkirmishNewGameWarningPanel.button.yes"), 0);
      this.guiButtons.add(this.yes);
      this.really = new GUIButtonMainMenu("really", Text.getText("mainMenuSkirmishNewGameWarningPanel.button.yesConfirm"), 2);
      this.guiButtons.add(this.really);
   }

   public void render(boolean debug) {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.panel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() / 2 - this.panel.getHeight() / 2 - 100;
      this.panel.draw(this.x, this.y);
      this.font
         .drawString(this.x + this.panel.getWidth() / 2, this.y + 17, Text.getText("mainMenuSkirmishNewGameWarningPanel.header"), Text.FontType.BODY, 3, true);
      if (this.confirmation) {
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2,
               this.y + 59,
               Text.wrapString(Text.getText("mainMenuSkirmishNewGameWarningPanel.bodyConfirm"), Text.FontType.BODY, 2, 480),
               Text.FontType.BODY,
               2,
               true
            );
         this.yes.render(this.g, this.mouse, this.x + 322, this.y + 137, true, debug);
         this.really.render(this.g, this.mouse, this.x + 171, this.y + 197, false, debug);
      } else {
         Text.setVariableText("gameMode", this.gameMode);
         Text.setVariableText("mapName", this.mapName);
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2,
               this.y + 59,
               Text.wrapString(Text.getText("mainMenuSkirmishNewGameWarningPanel.body"), Text.FontType.BODY, 2, 480),
               Text.FontType.BODY,
               2,
               true
            );
         this.really.hideButton();
         this.yes.render(this.g, this.mouse, this.x + 322, this.y + 137, false, debug);
      }

      this.no.render(this.g, this.mouse, this.x + 100, this.y + 137, false, debug);
   }

   public void setMapName(String mN) {
      this.mapName = mN;
   }

   public void setGameMode(String gM) {
      this.gameMode = gM;
   }

   public void setConfirmation(boolean b) {
      this.confirmation = b;
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
