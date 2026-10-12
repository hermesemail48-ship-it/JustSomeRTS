package rtr.gui.states.playstate;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.system.ScaleControl;

public class PauseBanner extends PlayStateGUIPanelBase {
   public PauseBanner(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
   }

   public void render(boolean debug) throws SlickException {
      this.g.setColor(new Color(0, 0, 0, 120));
      this.g.fillRect(0.0F, 0.0F, ScaleControl.getInterfaceWidth(), ScaleControl.getInterfaceHeight());
      if (this.controller.getTipsPanel().getActiveTip() == null) {
         this.g.setColor(new Color(0, 0, 0, 150));
         this.g.fillRect(0.0F, ScaleControl.getInterfaceHeight() / 2 + 80, ScaleControl.getInterfaceWidth(), 80.0F);
         this.g.setColor(new Color(0, 100, 200, 120));
         this.g.fillRect(0.0F, ScaleControl.getInterfaceHeight() / 2 + 104, ScaleControl.getInterfaceWidth(), 30.0F);
         this.font
            .drawString(
               ScaleControl.getInterfaceWidth() / 2,
               ScaleControl.getInterfaceHeight() / 2 + 101,
               Text.getText("playPauseBanner.header"),
               Text.FontType.HEADER,
               5,
               true
            );
         Text.setVariableText("shortcutKey", SettingsParser.getKeyPauseName().toUpperCase());
         this.font
            .drawString(
               ScaleControl.getInterfaceWidth() / 2,
               ScaleControl.getInterfaceHeight() / 2 + 135,
               Text.getText("playPauseBanner.body"),
               Text.FontType.BODY,
               3,
               true
            );
      }
   }
}
