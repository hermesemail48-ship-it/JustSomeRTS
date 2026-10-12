package rtr.gui.states.worldmap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.buttons.GUIButtonInputText;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.gui.states.WorldMapGUIPanelBase;
import rtr.system.ScaleControl;

public class DoomWorldPanel extends WorldMapGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/gameState/genericPanelLarge.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private GUIButtonInputText input;
   private GUIButtonGenericWood doomWorld;

   public DoomWorldPanel(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
      this.input = new GUIButtonInputText(gc, "input", Text.getText("worldMapDoomWorldPanel.button.confirmInputBox"), 32, 20, 1);
      this.guiButtons.add(this.input);
      this.doomWorld = new GUIButtonGenericWood("doomWorld", Text.getText("worldMapDoomWorldPanel.button.doomWorld"));
      this.guiButtons.add(this.doomWorld);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() / 2 + 78;
      this.y = 110;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      this.font
         .drawString(
            this.x + 155, this.y + 20, Text.wrapString(Text.getText("worldMapDoomWorldPanel.warning"), Text.FontType.BODY, 1, 250), Text.FontType.BODY, 1, true
         );
      Text.setVariableText("reset", "$GRE1RESET$ORA1");
      this.font
         .drawString(
            this.x + 155,
            this.y + 70,
            Text.wrapString(Text.getText("worldMapDoomWorldPanel.instructions"), Text.FontType.BODY, 1, 250),
            Text.FontType.BODY,
            1,
            true
         );
      this.input.render(this.g, this.mouse, this.x + 40, this.y + 105, debug);
      if (this.input.getText().toLowerCase().equals("reset")) {
         this.doomWorld.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + 125, false, true, debug);
      }
   }

   public void setFocusInput() {
      this.input.setFocus(true);
   }

   public boolean hasFocusInput() {
      return this.input.hasFocus();
   }

   public void resetInput() {
      this.input.setFocus(false);
      this.input.setText("");
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
