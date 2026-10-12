package rtr.gui.buttons;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.system.ScaleControl;

public class GUIToolTipOverlay extends GUIButton {
   private Image[] toolTipFrames = new Image[15];
   private Image[] toolTipDescriptionFrames = new Image[12];

   public GUIToolTipOverlay() throws SlickException {
      for (int i = 0; i < this.toolTipFrames.length; i++) {
         this.toolTipFrames[i] = ImageLoader.getImage("res/GUI/gameState/toolTip" + (i * 10 + 50) + "px.png");
      }

      for (int i = 0; i < this.toolTipDescriptionFrames.length; i++) {
         this.toolTipDescriptionFrames[i] = ImageLoader.getImage("res/GUI/gameState/toolTipDescription" + (i + 1) + ".png");
      }

      this.intersectBox = new Rectangle(0.0F, 0.0F, 1.0F, 1.0F);
   }

   public void renderToolTip(Graphics g, Rectangle mouse, GUIToolTipOverlay.ToolTipPosition toolTipPosition, String title, boolean debug) {
      this.renderToolTip(g, mouse, toolTipPosition, title, null, debug);
   }

   public void renderToolTip(Graphics g, Rectangle mouse, GUIToolTipOverlay.ToolTipPosition toolTipPosition, String title, String description, boolean debug) {
      this.renderToolTip(g, mouse, toolTipPosition, 0, 0, title, description, debug);
   }

   public void renderToolTip(
      Graphics g, Rectangle mouse, GUIToolTipOverlay.ToolTipPosition toolTipPosition, int xOffset, int yOffset, String title, String description, boolean debug
   ) {
      if (description != null && !description.equals("")) {
         int height = Text.wrapString(description, Text.FontType.BODY, 1, 142).split("~").length * Text.getHeight(description, Text.FontType.BODY, 1);
         int frameNumber = height / 15 - 1;
         if (frameNumber < 0) {
            frameNumber = 0;
         }

         if (frameNumber > this.toolTipDescriptionFrames.length - 1) {
            frameNumber = this.toolTipDescriptionFrames.length - 1;
         }

         int x = 0;
         int y = 0;
         if (toolTipPosition == GUIToolTipOverlay.ToolTipPosition.LEFT) {
            x = this.getOffsetX((int)(mouse.getX() - this.toolTipDescriptionFrames[frameNumber].getWidth() - 24.0F), this.toolTipDescriptionFrames[frameNumber]);
            y = this.getOffsetY((int)(mouse.getY() + 24.0F), this.toolTipDescriptionFrames[frameNumber]);
         } else {
            x = this.getOffsetX((int)(mouse.getX() + 24.0F), this.toolTipDescriptionFrames[frameNumber]);
            y = this.getOffsetY((int)(mouse.getY() + 24.0F), this.toolTipDescriptionFrames[frameNumber]);
         }

         x += xOffset;
         y += yOffset;
         this.toolTipDescriptionFrames[frameNumber].draw(x, y);
         if (Text.getWidth(title, Text.FontType.BODY, 1) > 140) {
            this.font.drawString(x + this.toolTipDescriptionFrames[frameNumber].getWidth() / 2, y + 6, "$YEL1" + title, Text.FontType.BODY, 0, true);
         } else {
            this.font.drawString(x + this.toolTipDescriptionFrames[frameNumber].getWidth() / 2, y + 4, "$YEL1" + title, Text.FontType.BODY, 1, true);
         }

         this.font
            .drawString(
               x + this.toolTipDescriptionFrames[frameNumber].getWidth() / 2,
               y + 24,
               Text.wrapString(description, Text.FontType.BODY, 1, 142),
               Text.FontType.BODY,
               1,
               true
            );
      } else {
         int width = Text.getWidth(title, Text.FontType.BODY, 1);
         int frameNumberx = width / 10 - 2;
         if (frameNumberx < 0) {
            frameNumberx = 0;
         }

         if (frameNumberx > this.toolTipFrames.length - 1) {
            frameNumberx = this.toolTipFrames.length - 1;
         }

         int xx = 0;
         int yx = 0;
         if (toolTipPosition == GUIToolTipOverlay.ToolTipPosition.LEFT) {
            xx = this.getOffsetX((int)(mouse.getX() - this.toolTipFrames[frameNumberx].getWidth() - 24.0F), this.toolTipFrames[frameNumberx]);
            yx = this.getOffsetY((int)(mouse.getY() + 24.0F), this.toolTipFrames[frameNumberx]);
         } else {
            xx = this.getOffsetX((int)(mouse.getX() + 24.0F), this.toolTipFrames[frameNumberx]);
            yx = this.getOffsetY((int)(mouse.getY() + 24.0F), this.toolTipFrames[frameNumberx]);
         }

         xx += xOffset;
         yx += yOffset;
         this.toolTipFrames[frameNumberx].draw(xx, yx);
         if (width > 190) {
            this.font.drawString(xx + this.toolTipFrames[frameNumberx].getWidth() / 2, yx + 6, "$YEL1" + title, Text.FontType.BODY, 0, true);
         } else {
            this.font.drawString(xx + this.toolTipFrames[frameNumberx].getWidth() / 2, yx + 4, "$YEL1" + title, Text.FontType.BODY, 1, true);
         }
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   private int getOffsetX(int drawX, Image frame) {
      return drawX + frame.getWidth() > ScaleControl.getInterfaceWidth() - 12 ? ScaleControl.getInterfaceWidth() - frame.getWidth() - 12 : drawX;
   }

   private int getOffsetY(int drawY, Image frame) {
      return drawY + frame.getHeight() > ScaleControl.getInterfaceHeight() - 12 ? ScaleControl.getInterfaceHeight() - frame.getHeight() - 12 : drawY;
   }

   public static enum ToolTipPosition {
      LEFT,
      RIGHT;
   }
}
