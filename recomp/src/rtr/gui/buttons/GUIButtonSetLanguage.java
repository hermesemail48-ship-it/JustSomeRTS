package rtr.gui.buttons;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;

public class GUIButtonSetLanguage extends GUIButton {
   private Image button;

   public GUIButtonSetLanguage(String bName) throws SlickException {
      this.buttonName = bName;
      this.button = ImageLoader.getImage("res/GUI/mainMenu/mainMenuSetLanguageButton.png");
      this.intersectBox = new Rectangle(0.0F, 0.0F, this.button.getWidth() - 6, this.button.getHeight() - 6);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean debug) {
      super.render(g, x, y, d);
      if (this.disabled) {
         this.button.draw(x, y);
         this.button.drawFlash(x, y, this.button.getWidth(), this.button.getHeight(), new Color(0, 0, 0, 100));
      } else if (!this.isSelected && !this.intersects(mouse)) {
         this.button.draw(x, y);
      } else {
         this.button.draw(x, y);
         GL11.glBlendFunc(774, 770);
         this.button.drawFlash(x, y, this.button.getWidth(), this.button.getHeight(), new Color(255, 255, 255, 100));
         g.setDrawMode(Graphics.MODE_NORMAL);
      }

      Text.getLoadedTemplate().getIcon().draw(x + 16, y + 16);
      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }
}
