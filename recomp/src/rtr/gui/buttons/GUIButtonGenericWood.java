package rtr.gui.buttons;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.utilities.Utilities;

public class GUIButtonGenericWood extends GUIButton {
   private Image buttonFrame;
   private String label;

   public GUIButtonGenericWood(String bName, String bLabel) throws SlickException {
      this.buttonName = bName;
      this.label = bLabel;
      int fontWidth = Text.getWidth(this.label, Text.FontType.BODY, 1) + 16;
      if (fontWidth <= 40) {
         this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/buttonGenericWood40Px" + (Utilities.randomInt(3) + 1) + ".png");
      } else if (fontWidth <= 50) {
         this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/buttonGenericWood50Px" + (Utilities.randomInt(3) + 1) + ".png");
      } else if (fontWidth <= 60) {
         this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/buttonGenericWood60Px" + (Utilities.randomInt(3) + 1) + ".png");
      } else if (fontWidth <= 70) {
         this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/buttonGenericWood70Px" + (Utilities.randomInt(3) + 1) + ".png");
      } else if (fontWidth <= 80) {
         this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/buttonGenericWood80Px" + (Utilities.randomInt(3) + 1) + ".png");
      } else if (fontWidth <= 90) {
         this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/buttonGenericWood90Px" + (Utilities.randomInt(3) + 1) + ".png");
      } else if (fontWidth <= 100) {
         this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/buttonGenericWood100Px" + (Utilities.randomInt(3) + 1) + ".png");
      } else if (fontWidth <= 110) {
         this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/buttonGenericWood110Px" + (Utilities.randomInt(3) + 1) + ".png");
      } else {
         this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/buttonGenericWood120Px" + (Utilities.randomInt(3) + 1) + ".png");
      }

      this.intersectBox = new Rectangle(0.0F, 0.0F, this.buttonFrame.getWidth() - 6, this.buttonFrame.getHeight() - 6);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean debug) {
      this.render(g, mouse, x, y, d, false, debug);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean center, boolean debug) {
      if (center) {
         x -= this.buttonFrame.getWidth() / 2;
      }

      super.render(g, x, y, d);
      if (this.disabled) {
         this.buttonFrame.draw(x, y);
         this.buttonFrame.drawFlash(x, y, this.buttonFrame.getWidth(), this.buttonFrame.getHeight(), new Color(0, 0, 0, 100));
      } else if (!this.isSelected && !this.intersects(mouse)) {
         this.buttonFrame.draw(x, y);
      } else {
         this.buttonFrame.draw(x, y);
         GL11.glBlendFunc(774, 770);
         this.buttonFrame.drawFlash(x, y, this.buttonFrame.getWidth(), this.buttonFrame.getHeight(), Color.white);
         g.setDrawMode(Graphics.MODE_NORMAL);
      }

      if (this.disabled) {
         this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 6, "$BLA1" + this.label, Text.FontType.BODY, 1, true);
      } else if (!this.isSelected && !this.intersects(mouse)) {
         this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 6, this.label, Text.FontType.BODY, 1, true);
      } else {
         this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 6, "$GRE1" + this.label, Text.FontType.BODY, 1, true);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }
}
