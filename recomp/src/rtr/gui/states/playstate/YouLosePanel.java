package rtr.gui.states.playstate;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.system.ScaleControl;

public class YouLosePanel extends PlayStateGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/gameState/youLosePanel.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private GUIButtonGenericWood exit;

   public YouLosePanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
      this.exit = new GUIButtonGenericWood("exit", Text.getText("playYouLosePanel.button.abandonRegion"));
      this.guiButtons.add(this.exit);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceCenterX() - this.panel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceCenterY() - this.panel.getHeight() / 2;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + this.panel.getWidth() / 2, this.y + 18, Text.getText("playYouLosePanel.congratulations"), Text.FontType.HEADER, 5, true);
      this.font.drawString(this.x + this.panel.getWidth() / 2, this.y + 65, Text.getText("playYouLosePanel.youLose"), Text.FontType.HEADER, 3, true);
      Text.setVariableText("days", this.time.getDay());
      if (this.time.getDay() == 1) {
         this.font.drawString(this.x + this.panel.getWidth() / 2, this.y + 95, Text.getText("playYouLosePanel.daysSurvived"), Text.FontType.BODY, 2, true);
      } else {
         this.font
            .drawString(this.x + this.panel.getWidth() / 2, this.y + 95, Text.getText("playYouLosePanel.daysSurvivedPlural"), Text.FontType.BODY, 2, true);
      }

      this.font.drawString(this.x + this.panel.getWidth() / 2, this.y + 151, Text.getText("playYouLosePanel.acceptFate"), Text.FontType.BODY, 2, true);
      this.exit.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + 182, false, true, debug);
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
