package rtr.gui.buttons;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.LocalizationTemplate;
import rtr.font.Text;

public class GUIButtonLanguageBar extends GUIButton {
   private Image button;
   private Image icon;
   private String languageName;
   private double percentCompleted;

   public GUIButtonLanguageBar(String bName, LocalizationTemplate t) throws SlickException {
      this.buttonName = bName;
      this.languageName = t.getLanguageName();
      this.percentCompleted = t.getPercentCompleted();
      this.icon = t.getIcon();
      this.button = ImageLoader.getImage("res/GUI/mainMenu/mainMenuLanguageSelectBar.png");
      this.intersectBox = new Rectangle(0.0F, 0.0F, this.button.getWidth() - 6, this.button.getHeight() - 6);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean debug) {
      super.render(g, x, y, d);
      Image finalIcon = null;
      if (this.icon.getWidth() == 44 && this.icon.getHeight() == 22) {
         finalIcon = this.icon;
      } else {
         finalIcon = this.icon.getScaledCopy(44, 22);
      }

      if (!this.isSelected && !this.intersects(mouse)) {
         this.button.draw(x, y);
         finalIcon.draw(x + 1, y + 1);
      } else {
         this.button.draw(x, y);
         finalIcon.draw(x + 1, y + 1);
         GL11.glBlendFunc(774, 770);
         this.button.drawFlash(x, y, this.button.getWidth(), this.button.getHeight(), new Color(255, 255, 255, 100));
         finalIcon.drawFlash(x + 1, y + 1, finalIcon.getWidth(), finalIcon.getHeight(), new Color(255, 255, 255, 100));
         g.setDrawMode(Graphics.MODE_NORMAL);
      }

      this.font.drawString(x + 55, y + 2, this.languageName, Text.FontType.BODY, 2);
      if (this.percentCompleted == 100.0) {
         this.font.drawString(x + 307, y + 2, "$GRE1100%", Text.FontType.BODY, 2, true);
      } else if (this.percentCompleted > 90.0) {
         this.font.drawString(x + 307, y + 2, "$GRE1" + this.percentCompleted + "%", Text.FontType.BODY, 2, true);
      } else if (this.percentCompleted > 70.0) {
         this.font.drawString(x + 307, y + 2, "$WHI0" + this.percentCompleted + "%", Text.FontType.BODY, 2, true);
      } else if (this.percentCompleted > 50.0) {
         this.font.drawString(x + 307, y + 2, "$YEL1" + this.percentCompleted + "%", Text.FontType.BODY, 2, true);
      } else if (this.percentCompleted > 30.0) {
         this.font.drawString(x + 307, y + 2, "$ORA1" + this.percentCompleted + "%", Text.FontType.BODY, 2, true);
      } else {
         this.font.drawString(x + 307, y + 2, "$RED1" + this.percentCompleted + "%", Text.FontType.BODY, 2, true);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }
}
