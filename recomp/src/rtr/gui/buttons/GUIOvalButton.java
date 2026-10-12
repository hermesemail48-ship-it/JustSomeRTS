package rtr.gui.buttons;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class GUIOvalButton extends GUIButton {
   private Image buttonFrame;
   private Image buttonHighLight;
   private Image buttonSelected;
   private String buttonText;
   private Image buttonImage;
   private boolean hasImage = false;

   public GUIOvalButton(String bName, int type) throws SlickException {
      this(bName, bName, type);
   }

   public GUIOvalButton(String bName, String text, int type) throws SlickException {
      this.intersectBox = new Rectangle(0.0F, 0.0F, 32.0F, 12.0F);
      this.buttonName = bName;

      try {
         this.buttonImage = ImageLoader.getImage("res/GUI/buttonRound/" + text + ".png");
         this.hasImage = true;
      } catch (Exception var5) {
         this.buttonText = text;
         this.hasImage = false;
      }

      String buttonType = "Black";
      if (type == 0) {
         buttonType = "Black";
      } else if (type == 1) {
         buttonType = "Brown";
      }

      this.buttonFrame = ImageLoader.getImage("res/GUI/buttonRound/buttonOval" + buttonType + ".png");
      this.buttonHighLight = ImageLoader.getImage("res/GUI/buttonRound/buttonOvalHighlight" + buttonType + ".png");
      this.buttonSelected = ImageLoader.getImage("res/GUI/buttonRound/buttonOvalSelected" + buttonType + ".png");
   }

   public void render(Graphics g, Rectangle mouse, int positionX, int positionY, boolean debug) {
      this.intersectBox.setX(positionX + 4);
      this.intersectBox.setY(positionY + 4);
      this.buttonFrame.draw(positionX, positionY);
      if (mouse.intersects(this.intersectBox)) {
         this.buttonHighLight.draw(positionX, positionY);
         if (Utilities.randomInt(2) == 0) {
            int randomX = Utilities.randomInt(this.buttonFrame.getWidth() + 1);

            int randomY;
            for (randomY = Utilities.randomInt(this.buttonFrame.getHeight() + 1);
               randomX < this.buttonFrame.getWidth() && randomX > 0 && randomY != 0 && randomY != this.buttonFrame.getHeight();
               randomY = Utilities.randomInt(this.buttonFrame.getHeight() + 1)
            ) {
               randomX = Utilities.randomInt(this.buttonFrame.getWidth() + 1);
            }

            this.particle.newParticleGroup(positionX + randomX, positionY + randomY, ParticleModule.ParticleGroup.FALL, ParticleModule.ParticleSet.GRAY, true);
         }

         if (!this.hasImage) {
            this.font.drawString(positionX + 20, positionY + 2, this.buttonText, Text.FontType.BODY, 1, true);
         }
      } else if (!this.hasImage) {
         this.font.drawString(positionX + 20, positionY + 2, "$YEL1" + this.buttonText, Text.FontType.BODY, 1, true);
      }

      if (this.isSelected) {
         this.buttonSelected.draw(positionX, positionY);
         if (Utilities.randomInt(10) == 0) {
            int randomX = Utilities.randomInt(this.buttonFrame.getWidth() + 1);

            int randomY;
            for (randomY = Utilities.randomInt(this.buttonFrame.getHeight() + 1);
               randomX < this.buttonFrame.getWidth() && randomX > 0 && randomY != 0 && randomY != this.buttonFrame.getHeight();
               randomY = Utilities.randomInt(this.buttonFrame.getHeight() + 1)
            ) {
               randomX = Utilities.randomInt(this.buttonFrame.getWidth() + 1);
            }

            this.particle.newParticleGroup(positionX + randomX, positionY + randomY, ParticleModule.ParticleGroup.FALL, ParticleModule.ParticleSet.GRAY, true);
         }

         if (!this.hasImage) {
            this.font.drawString(positionX + 19, positionY + 2, "$GRE1" + this.buttonText, Text.FontType.BODY, 1, true);
         }
      }

      if (this.hasImage) {
         this.buttonImage.draw(positionX, positionY);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }
}
