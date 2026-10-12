package rtr.gui.states.mainmenu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MainMenuGUIPanelBase;
import rtr.system.ScaleControl;

public class WarningPanel extends MainMenuGUIPanelBase {
   private GUIButtonMainMenu gotIt = new GUIButtonMainMenu("gotIt", Text.getText("mainMenuWarningPanel.button.gotIt"), 2, 1);
   private GUIButtonMainMenu openInstallFolder;

   public WarningPanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.guiButtons.add(this.gotIt);
      this.openInstallFolder = new GUIButtonMainMenu("openInstallFolder", Text.getText("mainMenuWarningPanel.button.openInstallFolder"), 2, 1);
      this.guiButtons.add(this.openInstallFolder);
   }

   public void render(boolean debug) {
      this.x = ScaleControl.getInterfaceWidth() / 2;
      this.y = 0;
      this.g.setColor(new Color(0, 0, 0, 130));
      this.g.fillRect(-10.0F, 200.0F, 3000.0F, 300.0F);
      this.font.drawString(this.x, 210.0F, Text.getText("mainMenuWarningPanel.header"), Text.FontType.HEADER, 5, true);
      this.font
         .drawString(
            ScaleControl.getInterfaceWidth() / 2 - 525,
            255.0F,
            Text.wrapString(
               "    " + Text.getText("mainMenuWarningPanel.body.line1") + "~~" + "    " + Text.getText("mainMenuWarningPanel.body.line2"),
               Text.FontType.BODY,
               3,
               1035
            ),
            Text.FontType.BODY,
            3,
            false
         );
      this.gotIt.render(this.g, this.mouse, this.x - 188, this.y + 520, false, debug);
      this.openInstallFolder.render(this.g, this.mouse, this.x + 18, this.y + 520, false, debug);
   }
}
