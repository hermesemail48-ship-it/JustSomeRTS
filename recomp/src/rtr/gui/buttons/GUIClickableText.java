package rtr.gui.buttons;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.font.Text;

public class GUIClickableText extends GUIButton {
   private int size;
   private String text = "";

   public GUIClickableText(String bName, int bSize) throws SlickException {
      this.buttonName = bName;
      this.size = bSize;
      this.intersectBox = new Rectangle(0.0F, 0.0F, 1.0F, 1.0F);
   }

   public void render(Graphics g, Rectangle mouse, String text, int x, int y, boolean d, boolean debug) {
      this.render(g, mouse, text, x, y, "$WHI0", "$BLA2", d, false, debug);
   }

   public void render(Graphics g, Rectangle mouse, String text, int x, int y, boolean d, boolean center, boolean debug) {
      this.render(g, mouse, text, x, y, "$WHI0", "$BLA2", d, center, debug);
   }

   public void render(Graphics g, Rectangle mouse, String text, int x, int y, String colorOne, String colorTwo, boolean d, boolean debug) {
      this.render(g, mouse, text, x, y, colorOne, colorTwo, d, false, debug);
   }

   public void render(Graphics g, Rectangle mouse, String text, int x, int y, String colorOne, String colorTwo, boolean d, boolean center, boolean debug) {
      int fontWidth = Text.getWidth(text, Text.FontType.BODY, this.size);
      int fontHeight = Text.getHeight(text, Text.FontType.BODY, this.size);
      if (center) {
         x -= fontWidth / 2;
      }

      super.render(g, x, y, d);
      this.intersectBox.setBounds(x, y + 2, fontWidth, fontHeight - 6);
      this.text = text;
      if (this.disabled) {
         this.font.drawString(x, y, "$BLA1" + text, Text.FontType.BODY, this.size);
      } else if (!this.intersects(mouse) && !this.isSelected) {
         this.font.drawString(x, y, colorTwo + text, Text.FontType.BODY, this.size);
      } else {
         this.font.drawString(x, y, colorOne + text, Text.FontType.BODY, this.size);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   public String getText() {
      return this.text;
   }
}
