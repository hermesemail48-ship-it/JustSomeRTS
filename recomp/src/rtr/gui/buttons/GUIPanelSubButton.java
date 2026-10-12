package rtr.gui.buttons;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.utilities.Utilities;

public class GUIPanelSubButton extends GUIButton {
   private Image button;
   private String type;

   public GUIPanelSubButton(String bName, String bType) throws SlickException {
      this.buttonName = bName;
      this.type = bType;
      if (this.type.equals("plus")) {
         this.button = ImageLoader.getImage("res/GUI/gameState/panelSubButtonPlus" + (Utilities.randomInt(5) + 1) + ".png");
      } else if (this.type.equals("minus")) {
         this.button = ImageLoader.getImage("res/GUI/gameState/panelSubButtonMinus" + (Utilities.randomInt(5) + 1) + ".png");
      } else if (this.type.equals("up")) {
         this.button = ImageLoader.getImage("res/GUI/gameState/panelSubButtonUp" + (Utilities.randomInt(5) + 1) + ".png");
      } else if (this.type.equals("down")) {
         this.button = ImageLoader.getImage("res/GUI/gameState/panelSubButtonDown" + (Utilities.randomInt(5) + 1) + ".png");
      } else if (this.type.equals("left")) {
         this.button = ImageLoader.getImage("res/GUI/gameState/panelSubButtonLeft" + (Utilities.randomInt(5) + 1) + ".png");
      } else if (this.type.equals("right")) {
         this.button = ImageLoader.getImage("res/GUI/gameState/panelSubButtonRight" + (Utilities.randomInt(5) + 1) + ".png");
      }

      this.intersectBox = new Rectangle(0.0F, 0.0F, this.button.getWidth() - 6, this.button.getHeight() - 6);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean debug) {
      super.render(g, x, y, d);
      if (this.disabled) {
         this.button.draw(x, y);
         this.button.drawFlash(x, y, this.button.getWidth(), this.button.getHeight(), new Color(0, 0, 0, 100));
      } else if (!this.intersects(mouse) && !this.isSelected) {
         this.button.draw(x, y);
      } else {
         this.button.draw(x, y);
         GL11.glBlendFunc(774, 770);
         this.button.drawFlash(x, y, this.button.getWidth(), this.button.getHeight(), Color.white);
         g.setDrawMode(Graphics.MODE_NORMAL);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   public int getHeight() {
      return this.button.getHeight();
   }

   public int getWidth() {
      return this.button.getWidth();
   }
}
