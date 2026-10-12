package rtr.gui.buttons;

import java.io.File;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class GUISquarePanelButton extends GUIButton {
   private Image buttonFrameSilver;
   private Image buttonFrameBlue;
   private Image buttonFrameBrown;
   private Image buttonFrameOrange;
   private Image buttonFrameRed;
   private Image buttonFrameGreen;
   private Image buttonFrame;
   private Image buttonImage;
   private int color;
   private String key;
   private int rotationalPositionH = 0;
   private int rotationalPositionV = 0;

   public GUISquarePanelButton(String bName, int bColor, String bImage) throws SlickException {
      this(bName, null, bColor, bImage);
   }

   public GUISquarePanelButton(String bName, String k, int bColor, String bImage) throws SlickException {
      this.buttonName = bName;
      this.color = bColor;
      if (k != null) {
         this.key = k.toUpperCase();
      }

      if (this.key != null && this.key.equals("GRAVE")) {
         this.key = "`";
      }

      int randomButton = Utilities.randomInt(10) + 1;
      this.buttonFrameBrown = ImageLoader.getImage("res/GUI/gameState/buttonSmallBrown" + randomButton + ".png");
      this.buttonFrameSilver = ImageLoader.getImage("res/GUI/gameState/buttonSmallSilver" + randomButton + ".png");
      this.buttonFrameBlue = ImageLoader.getImage("res/GUI/gameState/buttonSmallBlue" + randomButton + ".png");
      this.buttonFrameOrange = ImageLoader.getImage("res/GUI/gameState/buttonSmallOrange" + randomButton + ".png");
      this.buttonFrameGreen = ImageLoader.getImage("res/GUI/gameState/buttonSmallGreen" + randomButton + ".png");
      if (this.color == 0) {
         this.buttonFrame = this.buttonFrameBrown;
      } else if (this.color == 1) {
         this.buttonFrame = this.buttonFrameGreen;
      } else if (this.color == 2) {
         this.buttonFrame = this.buttonFrameBlue;
      } else if (this.color == 3) {
         this.buttonFrame = this.buttonFrameOrange;
      } else if (this.color == 4) {
         this.buttonFrame = this.buttonFrameSilver;
      }

      File imageCheck = new File("res/GUI/gameState/images/squarePanel/" + bImage + ".png");
      if (imageCheck.exists()) {
         this.buttonImage = ImageLoader.getImage("res/GUI/gameState/images/squarePanel/" + bImage + ".png");
      }

      this.intersectBox = new Rectangle(0.0F, 0.0F, this.buttonFrame.getWidth() - 6, this.buttonFrame.getHeight() - 6);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean debug) {
      this.render(g, mouse, x, y, d, -1, debug);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, int cooldownPercent, boolean debug) {
      super.render(g, x, y, d);
      if (this.disabled) {
         if (this.buttonImage != null) {
            this.buttonImage.draw(x + 7, y + 7);
            this.buttonImage.drawFlash(x + 7, y + 7, this.buttonImage.getWidth(), this.buttonImage.getHeight(), new Color(0, 0, 0, 200));
         } else {
            g.setColor(new Color(26, 26, 26, 255));
            g.fillRect(x + 4, y + 4, this.buttonFrame.getWidth() - 8, this.buttonFrame.getHeight() - 8);
         }

         if (cooldownPercent != -1) {
            g.setWorldClip(this.intersectBox);
            g.setColor(new Color(50, 0, 0, 200));
            g.fillArc(x + 2 - this.buttonFrame.getWidth() / 4, y + 2 - this.buttonFrame.getHeight() / 4, 64.0F, 64.0F, -cooldownPercent - 90, -90.0F);
            g.clearWorldClip();
         }

         if (this.key != null) {
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(x + 4, y + 27, Text.getWidth(this.key, Text.FontType.BODY, 0) + 6, 10.0F);
         }

         this.buttonFrame.draw(x, y);
         this.buttonFrame.drawFlash(x, y, this.buttonFrame.getWidth(), this.buttonFrame.getHeight(), new Color(0, 0, 0, 200));
      } else if (!this.intersects(mouse) && !this.isSelected) {
         if (this.buttonImage != null) {
            this.buttonImage.draw(x + 7, y + 7);
         } else {
            g.setColor(new Color(26, 26, 26, 255));
            g.fillRect(x + 4, y + 4, this.buttonFrame.getWidth() - 8, this.buttonFrame.getHeight() - 8);
         }

         if (cooldownPercent != -1) {
            g.setWorldClip(this.intersectBox);
            g.setColor(new Color(50, 0, 0, 200));
            g.fillArc(x + 2 - this.buttonFrame.getWidth() / 4, y + 2 - this.buttonFrame.getHeight() / 4, 64.0F, 64.0F, -cooldownPercent - 90, -90.0F);
            g.clearWorldClip();
         }

         if (this.key != null) {
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(x + 4, y + 27, Text.getWidth(this.key, Text.FontType.BODY, 0) + 6, 10.0F);
         }

         this.buttonFrame.draw(x, y);
      } else {
         if (this.buttonImage != null) {
            this.buttonImage.draw(x + 7, y + 7);
            GL11.glBlendFunc(774, 770);
            this.buttonImage.drawFlash(x + 7, y + 7, this.buttonImage.getWidth(), this.buttonImage.getHeight(), new Color(255, 255, 255, 100));
            g.setDrawMode(Graphics.MODE_NORMAL);
         } else {
            g.setColor(new Color(26, 26, 26, 255));
            g.fillRect(x + 4, y + 4, this.buttonFrame.getWidth() - 8, this.buttonFrame.getHeight() - 8);
            GL11.glBlendFunc(774, 770);
            g.fillRect(x + 4, y + 4, this.buttonFrame.getWidth() - 8, this.buttonFrame.getHeight() - 8);
            g.setDrawMode(Graphics.MODE_NORMAL);
         }

         if (cooldownPercent != -1) {
            g.setWorldClip(this.intersectBox);
            g.setColor(new Color(50, 0, 0, 200));
            g.fillArc(x + 2 - this.buttonFrame.getWidth() / 4, y + 2 - this.buttonFrame.getHeight() / 4, 64.0F, 64.0F, -cooldownPercent - 90, -90.0F);
            g.clearWorldClip();
         }

         if (this.key != null) {
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(x + 4, y + 27, Text.getWidth(this.key, Text.FontType.BODY, 0) + 6, 10.0F);
         }

         this.buttonFrame.draw(x, y);
         GL11.glBlendFunc(774, 770);
         this.buttonFrame.drawFlash(x, y, this.buttonFrame.getWidth(), this.buttonFrame.getHeight(), Color.white);
         g.setDrawMode(Graphics.MODE_NORMAL);
         if (this.rotationalPositionH < 37 && this.rotationalPositionV == 0) {
            this.rotationalPositionH++;
         } else if (this.rotationalPositionH == 37 && this.rotationalPositionV < 37) {
            this.rotationalPositionV++;
         } else if (this.rotationalPositionH > 0 && this.rotationalPositionV == 37) {
            this.rotationalPositionH--;
         } else if (this.rotationalPositionH == 0 && this.rotationalPositionV > 0) {
            this.rotationalPositionV--;
         }

         this.particle
            .newParticleGroup(
               x + this.rotationalPositionH + 4 + Utilities.randomInt(-2, 2),
               y + this.rotationalPositionV + 4 + Utilities.randomInt(-2, 2),
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.WHITE,
               true
            );
         this.particle
            .newParticleGroup(
               x + this.rotationalPositionH + 4 + Utilities.randomInt(-1, 1),
               y + this.rotationalPositionV + 4 + Utilities.randomInt(-1, 1),
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               true
            );
      }

      if (this.key != null) {
         this.font.drawString(x + 8, y + 27, this.key, Text.FontType.BODY, 0);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   public void setKey(String i) {
      this.key = i;
   }

   public int getHeight() {
      return this.buttonFrame.getHeight();
   }

   public int getWidth() {
      return this.buttonFrame.getWidth();
   }
}
