package rtr.gui.buttons;

import java.io.File;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.imageout.ImageOut;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.utilities.Utilities;

public class GUIPanelButton extends GUIButton {
   private Image buttonFrame;
   private Image buttonImage;
   private Image buttonCategoryIcon;
   private String label;
   private int size;
   private boolean category;

   public GUIPanelButton(String bName, int bSize, String bLabel) throws SlickException {
      this(bName, bSize, "null", bLabel, false);
   }

   public GUIPanelButton(String bName, int bSize, String bImage, String bLabel, boolean isCategory) throws SlickException {
      this.buttonName = bName;
      this.label = bLabel;
      this.size = bSize;
      this.category = isCategory;
      if (this.category) {
         this.buttonCategoryIcon = ImageLoader.getImage("res/GUI/gameState/panelCategoryIcon.png");
      }

      if (this.size == 0) {
         this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/panelSmall" + (Utilities.randomInt(10) + 1) + ".png");
         File imageCheck = new File("res/GUI/gameState/images/" + bImage + ".png");
         if (imageCheck.exists()) {
            this.buttonImage = ImageLoader.getImage("res/GUI/gameState/images/" + bImage + ".png");
         }
      } else {
         this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/panelLarge" + (Utilities.randomInt(10) + 1) + ".png");
         File imageCheck = new File("res/GUI/gameState/images/" + bImage + ".png");
         if (imageCheck.exists()) {
            this.buttonImage = ImageLoader.getImage("res/GUI/gameState/images/" + bImage + ".png");
         }
      }

      this.intersectBox = new Rectangle(0.0F, 0.0F, this.buttonFrame.getWidth() - 2, this.buttonFrame.getHeight() - 2);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean debug) {
      this.render(g, mouse, x, y, -1, d, debug);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, int count, boolean d, boolean debug) {
      super.render(g, x, y, d);
      this.intersectBox.setX(x + 1);
      this.intersectBox.setY(y + 1);
      if (this.disabled) {
         if (this.buttonImage != null) {
            if (this.size == 0) {
               g.setWorldClip(this.intersectBox);
            }

            this.buttonImage.draw(x + 10, y + 19);
            this.buttonImage.drawFlash(x + 10, y + 19, this.buttonImage.getWidth(), this.buttonImage.getHeight(), new Color(0, 0, 0, 100));
            if (this.size == 0) {
               g.clearWorldClip();
            }
         } else {
            g.setColor(new Color(26, 26, 26, 255));
            g.fillRect(x + 10, y + 19, this.buttonFrame.getWidth() - 20, this.buttonFrame.getHeight() - 29);
         }

         if (count != -1) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRoundRect(
               x + 6,
               y + 16,
               Text.getWidth(Integer.toString(count), Text.FontType.BODY, 1) + 10,
               Text.getHeight(Integer.toString(count), Text.FontType.BODY, 1) + 2,
               4
            );
         }

         this.buttonFrame.draw(x, y);
         this.buttonFrame.drawFlash(x, y, this.buttonFrame.getWidth(), this.buttonFrame.getHeight(), new Color(0, 0, 0, 100));
         if (this.category) {
            this.buttonCategoryIcon.draw(x + this.buttonFrame.getWidth() - 45, y + 24);
            this.buttonCategoryIcon
               .drawFlash(
                  x + this.buttonFrame.getWidth() - 45,
                  y + 24,
                  this.buttonCategoryIcon.getWidth(),
                  this.buttonCategoryIcon.getHeight(),
                  new Color(0, 0, 0, 100)
               );
         }
      } else if (!this.intersects(mouse) && !this.isSelected) {
         if (this.buttonImage != null) {
            if (this.size == 0) {
               g.setWorldClip(
                  this.intersectBox.getX() + 6.0F, this.intersectBox.getY() + 6.0F, this.intersectBox.getWidth() - 12.0F, this.intersectBox.getHeight() - 12.0F
               );
            }

            this.buttonImage.draw(x + 10, y + 19);
            if (this.size == 0) {
               g.clearWorldClip();
            }
         } else {
            g.setColor(new Color(26, 26, 26, 255));
            g.fillRect(x + 10, y + 19, this.buttonFrame.getWidth() - 20, this.buttonFrame.getHeight() - 29);
         }

         if (count != -1) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRoundRect(
               x + 6,
               y + 16,
               Text.getWidth(Integer.toString(count), Text.FontType.BODY, 1) + 10,
               Text.getHeight(Integer.toString(count), Text.FontType.BODY, 1) + 2,
               4
            );
         }

         this.buttonFrame.draw(x, y);
         if (this.category) {
            this.buttonCategoryIcon
               .draw(x + this.buttonFrame.getWidth() - 45, y + 24, this.buttonCategoryIcon.getWidth(), this.buttonCategoryIcon.getHeight(), Color.white);
         }
      } else {
         if (this.buttonImage != null) {
            if (this.size == 0) {
               g.setWorldClip(
                  this.intersectBox.getX() + 6.0F, this.intersectBox.getY() + 6.0F, this.intersectBox.getWidth() - 12.0F, this.intersectBox.getHeight() - 12.0F
               );
            }

            this.buttonImage.draw(x + 10, y + 19);
            GL11.glBlendFunc(774, 770);
            this.buttonImage.drawFlash(x + 10, y + 19, this.buttonImage.getWidth(), this.buttonImage.getHeight(), new Color(255, 255, 255, 100));
            g.setDrawMode(Graphics.MODE_NORMAL);
            if (this.size == 0) {
               g.clearWorldClip();
            }
         } else {
            g.setColor(new Color(26, 26, 26, 255));
            g.fillRect(x + 10, y + 19, this.buttonFrame.getWidth() - 20, this.buttonFrame.getHeight() - 29);
            GL11.glBlendFunc(774, 770);
            g.fillRect(x + 10, y + 19, this.buttonFrame.getWidth() - 20, this.buttonFrame.getHeight() - 29);
            g.setDrawMode(Graphics.MODE_NORMAL);
         }

         if (count != -1) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRoundRect(
               x + 6,
               y + 16,
               Text.getWidth(Integer.toString(count), Text.FontType.BODY, 1) + 10,
               Text.getHeight(Integer.toString(count), Text.FontType.BODY, 1) + 2,
               4
            );
         }

         this.buttonFrame.draw(x, y);
         GL11.glBlendFunc(774, 770);
         this.buttonFrame.drawFlash(x, y, this.buttonFrame.getWidth(), this.buttonFrame.getHeight(), Color.white);
         g.setDrawMode(Graphics.MODE_NORMAL);
         if (this.category) {
            this.buttonCategoryIcon.draw(x + this.buttonFrame.getWidth() - 45, y + 24);
            GL11.glBlendFunc(774, 770);
            this.buttonCategoryIcon
               .drawFlash(x + this.buttonFrame.getWidth() - 45, y + 24, this.buttonCategoryIcon.getWidth(), this.buttonCategoryIcon.getHeight(), Color.white);
            g.setDrawMode(Graphics.MODE_NORMAL);
         }
      }

      if (this.flashing) {
         GL11.glBlendFunc(774, 770);
         g.setColor(new Color(255, 255, 255, this.flashAlpha));
         g.fillRect(x + 3, y + 3, this.buttonFrame.getWidth() - 6, this.buttonFrame.getHeight() - 6);
         g.setDrawMode(Graphics.MODE_NORMAL);
      }

      if (this.size == 0) {
         if (this.intersects(mouse)) {
            this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 4, "$GRE1" + this.label, Text.FontType.BODY, 0, true);
         } else if (this.isSelected) {
            this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 4, "$CYA1" + this.label, Text.FontType.BODY, 0, true);
         } else {
            this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 4, this.label, Text.FontType.BODY, 0, true);
         }
      } else if (this.intersects(mouse)) {
         this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 3, "$GRE1" + this.label, Text.FontType.BODY, 1, true);
      } else if (this.isSelected) {
         this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 3, "$CYA1" + this.label, Text.FontType.BODY, 1, true);
      } else {
         this.font.drawString(x + this.buttonFrame.getWidth() / 2, y + 3, this.label, Text.FontType.BODY, 1, true);
      }

      if (count != -1) {
         this.font.drawString(x + 13, y + 18, "$GRE0" + count, Text.FontType.BODY, 1, false);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   public int getSize() {
      return this.size;
   }

   public int getHeight() {
      return this.buttonFrame.getHeight();
   }

   public int getWidth() {
      return this.buttonFrame.getWidth();
   }

   @Override
   public void drawToDisk() throws SlickException {
      File screenShotFolder = new File("screenShots/GUI/");
      if (!screenShotFolder.exists()) {
         screenShotFolder.mkdirs();
      }

      File screenShotFile = new File("screenShots/GUI/" + this.label + ".png");
      Image screenShot = new Image(this.buttonFrame.getWidth(), this.buttonFrame.getHeight());
      Graphics g = screenShot.getGraphics();
      g.setColor(Color.white);
      g.fillRect(0.0F, 0.0F, this.buttonFrame.getWidth(), this.buttonFrame.getHeight());
      if (this.buttonImage != null) {
         if (this.size == 0) {
            g.setWorldClip(this.intersectBox);
         }

         g.drawImage(this.buttonImage, 10.0F, 19.0F);
         if (this.size == 0) {
            g.clearWorldClip();
         }
      }

      g.drawImage(this.buttonFrame, 0.0F, 0.0F);
      if (this.category) {
         g.drawImage(this.buttonCategoryIcon, this.buttonFrame.getWidth() - 45, 24.0F);
      }

      if (this.size == 0) {
         this.font.drawString(this.buttonFrame.getWidth() / 2, 4.0F, this.label, Text.FontType.BODY, 0, true);
      } else {
         this.font.drawString(this.buttonFrame.getWidth() / 2, 3.0F, this.label, Text.FontType.BODY, 1, true);
      }

      g.flush();
      ImageOut.write(screenShot, screenShotFile.toString(), false);
      System.gc();
   }
}
