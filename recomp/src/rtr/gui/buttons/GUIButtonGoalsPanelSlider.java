package rtr.gui.buttons;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.Utilities;

public class GUIButtonGoalsPanelSlider extends GUIButton {
   private Image button;
   private int sliderLength;
   private int sliderPosition;
   private int sliderPercentOfPosition;
   private boolean locked;

   public int getSliderLength() {
      return this.sliderLength;
   }

   public int getSliderPosition() {
      return this.sliderPosition;
   }

   public int getSliderPercentOfPosition() {
      return this.sliderPercentOfPosition;
   }

   public GUIButtonGoalsPanelSlider(String bName) throws SlickException {
      this.buttonName = bName;
      this.flashing = true;
      this.sliderLength = 443;
      this.button = ImageLoader.getImage("res/GUI/gameState/goalsPanelSlider.png");
      this.intersectBox = new Rectangle(0.0F, 0.0F, this.button.getWidth() - 6, this.button.getHeight() + this.sliderLength - 6);
   }

   public void lock() {
      this.locked = true;
   }

   public void moveUp(int amount) {
      this.sliderPosition -= amount;
      if (this.sliderPosition <= 0) {
         this.sliderPosition = 0;
      }
   }

   public void moveDown(int amount) {
      this.sliderPosition += amount;
      if (this.sliderPosition >= this.sliderLength) {
         this.sliderPosition = this.sliderLength;
      }
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean debug) {
      if (this.locked) {
         if (!StateBase.getInput().isMouseButtonDown(0)) {
            this.locked = false;
         }

         this.sliderPosition = (int)Game.getCS().getMouseY();
         this.sliderPosition -= y + 32;
      }

      if (this.sliderPosition >= this.sliderLength) {
         this.sliderPosition = this.sliderLength;
      } else if (this.sliderPosition <= 0) {
         this.sliderPosition = 0;
      }

      super.render(g, x, y, false);
      y += this.sliderPosition;
      this.sliderPercentOfPosition = (int)Utilities.getPercentage(this.sliderPosition, this.sliderLength);
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

      if (this.flashing) {
         GL11.glBlendFunc(774, 770);
         g.setColor(new Color(255, 255, 255, this.flashAlpha / 3));
         g.fillRect(x + 3, y + 3, this.button.getWidth() - 6, this.button.getHeight() - 6);
         g.setDrawMode(Graphics.MODE_NORMAL);
      }

      if (debug) {
         this.font.drawString(x, y - 15, "Y: " + this.sliderPercentOfPosition + "%", Text.FontType.BODY, 0);
         this.font.drawString(x, y - 25, "L: " + this.locked, Text.FontType.BODY, 0);
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }
}
