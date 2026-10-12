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

public class GUIButtonMainMenu extends GUIButton {
   private Image buttonFrame;
   private String label;

   public GUIButtonMainMenu(String bName, String bLabel, int size) throws SlickException {
      this(bName, bLabel, size, 0);
   }

   public GUIButtonMainMenu(String bName, String bLabel, int size, int buttonColor) throws SlickException {
      this.buttonName = bName;
      this.label = bLabel;
      String altButton = "";
      if (buttonColor == 1) {
         altButton = "Green";
      } else if (buttonColor == 2) {
         altButton = "Red";
      }

      if (size == 0) {
         this.buttonFrame = ImageLoader.getImage("res/GUI/mainMenu/mainMenu" + altButton + "ButtonSmall" + (Utilities.randomInt(4) + 1) + ".png");
      } else if (size == 1) {
         this.buttonFrame = ImageLoader.getImage("res/GUI/mainMenu/mainMenu" + altButton + "ButtonMedium" + (Utilities.randomInt(4) + 1) + ".png");
      } else if (size == 2) {
         this.buttonFrame = ImageLoader.getImage("res/GUI/mainMenu/mainMenu" + altButton + "ButtonLarge" + (Utilities.randomInt(4) + 1) + ".png");
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

      if (this.label.length() > 20) {
         if (this.disabled) {
            this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 20, "$BLA1" + this.label, Text.FontType.BODY, 1, true);
         } else if (!this.isSelected && !this.intersects(mouse)) {
            this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 20, "$YEL1" + this.label, Text.FontType.BODY, 1, true);
         } else {
            this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 20, this.label, Text.FontType.BODY, 1, true);
         }
      } else if (this.label.length() > 14) {
         if (this.disabled) {
            this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 17, "$BLA1" + this.label, Text.FontType.BODY, 2, true);
         } else if (!this.isSelected && !this.intersects(mouse)) {
            this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 17, "$YEL1" + this.label, Text.FontType.BODY, 2, true);
         } else {
            this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 17, this.label, Text.FontType.BODY, 2, true);
         }
      } else if (this.disabled) {
         this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 15, "$BLA1" + this.label, Text.FontType.BODY, 3, true);
      } else if (!this.isSelected && !this.intersects(mouse)) {
         this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 15, "$YEL1" + this.label, Text.FontType.BODY, 3, true);
      } else {
         this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 15, this.label, Text.FontType.BODY, 3, true);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }
}
