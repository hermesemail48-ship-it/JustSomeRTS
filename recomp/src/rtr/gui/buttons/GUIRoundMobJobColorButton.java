package rtr.gui.buttons;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.mobs.jobs.MobJobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class GUIRoundMobJobColorButton extends GUIButton {
   private Image buttonFrame;
   private Image buttonHighLight;
   private Image buttonSelected;
   private boolean accent = false;
   private Color color = null;
   private MobJobBase.MobJobType mobJobType = null;

   public void setColor(Color c) {
      this.color = c;
      this.mobJobType = null;
   }

   public void setMobJobType(MobJobBase.MobJobType mJobType) {
      this.color = null;
      this.mobJobType = mJobType;
   }

   public GUIRoundMobJobColorButton(String bName, Color c, int type) throws SlickException {
      this(bName, null, c, false, type);
   }

   public GUIRoundMobJobColorButton(String bName, MobJobBase.MobJobType mJobType, boolean a, int type) throws SlickException {
      this(bName, mJobType, null, a, type);
   }

   public GUIRoundMobJobColorButton(String bName, MobJobBase.MobJobType mJobType, Color c, boolean a, int type) throws SlickException {
      this.intersectBox = new Rectangle(0.0F, 0.0F, 12.0F, 12.0F);
      this.buttonName = bName;
      this.mobJobType = mJobType;
      this.color = c;
      this.accent = a;
      String buttonType = "BlackEmpty";
      switch (type) {
         case 0:
            buttonType = "BlackEmpty";
            break;
         case 1:
            buttonType = "BrownEmpty";
            break;
         case 2:
            buttonType = "BlackEmptyLinkedLeft";
            break;
         case 3:
            buttonType = "BrownEmptyLinkedLeft";
            break;
         case 4:
            buttonType = "BlackEmptyLinkedRight";
            break;
         case 5:
            buttonType = "BrownEmptyLinkedRight";
      }

      this.buttonFrame = ImageLoader.getImage("res/GUI/buttonRound/buttonRound" + buttonType + ".png");
      this.buttonHighLight = ImageLoader.getImage("res/GUI/buttonRound/buttonRoundHighlight" + buttonType + ".png");
      this.buttonSelected = ImageLoader.getImage("res/GUI/buttonRound/buttonRoundSelected" + buttonType + ".png");
   }

   public void render(Graphics g, Rectangle mouse, int positionX, int positionY, boolean debug) {
      this.intersectBox.setX(positionX + 4);
      this.intersectBox.setY(positionY + 4);
      if (this.color != null) {
         g.setColor(this.color);
      } else if (this.accent) {
         g.setColor(this.mobJob.getCustomAccentColor(this.mobJobType));
      } else {
         g.setColor(this.mobJob.getCustomBaseColor(this.mobJobType));
      }

      g.fillRect(positionX + 3, positionY + 4, 14.0F, 12.0F);
      if (this.isSelected) {
         this.buttonSelected.draw(positionX, positionY);
         if (Utilities.randomInt(3) == 0) {
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
      } else if (mouse.intersects(this.intersectBox)) {
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
      } else {
         this.buttonFrame.draw(positionX, positionY);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }
}
