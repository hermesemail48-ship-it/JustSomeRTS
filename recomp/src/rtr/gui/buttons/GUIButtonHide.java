package rtr.gui.buttons;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;

public class GUIButtonHide extends GUIButton {
   private Image buttonOn;
   private Image buttonHalf;
   private Image buttonOff;
   private GUIButtonHide.ButtonHideState buttonState = GUIButtonHide.ButtonHideState.ON;
   private boolean allowHalf;

   public GUIButtonHide(String bName, boolean aHalf) throws SlickException {
      this.buttonName = bName;
      this.allowHalf = aHalf;
      this.buttonOn = ImageLoader.getImage("res/GUI/gameState/buttonHideOn.png");
      this.buttonHalf = ImageLoader.getImage("res/GUI/gameState/buttonHideHalf.png");
      this.buttonOff = ImageLoader.getImage("res/GUI/gameState/buttonHideOff.png");
      this.intersectBox = new Rectangle(0.0F, 0.0F, this.buttonOn.getWidth() - 6, this.buttonOn.getHeight() - 6);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean debug) {
      this.render(g, mouse, x, y, d, false, debug);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean center, boolean debug) {
      if (center) {
         x -= this.buttonOn.getWidth() / 2;
      }

      super.render(g, x, y, d);
      Image activeButton;
      if (this.buttonState == GUIButtonHide.ButtonHideState.ON) {
         activeButton = this.buttonOn;
      } else if (this.buttonState == GUIButtonHide.ButtonHideState.HALF) {
         activeButton = this.buttonHalf;
      } else {
         activeButton = this.buttonOff;
      }

      if (this.disabled) {
         activeButton.draw(x, y);
         activeButton.drawFlash(x, y, activeButton.getWidth(), activeButton.getHeight(), new Color(0, 0, 0, 100));
      } else if (this.intersects(mouse)) {
         activeButton.draw(x, y);
         GL11.glBlendFunc(774, 770);
         activeButton.drawFlash(x, y, activeButton.getWidth(), activeButton.getHeight(), Color.white);
         g.setDrawMode(Graphics.MODE_NORMAL);
      } else {
         activeButton.draw(x, y);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   public void cycleButton() {
      if (this.buttonState == GUIButtonHide.ButtonHideState.ON && this.allowHalf) {
         this.buttonState = GUIButtonHide.ButtonHideState.HALF;
      } else if (this.buttonState == GUIButtonHide.ButtonHideState.HALF || this.buttonState == GUIButtonHide.ButtonHideState.ON) {
         this.buttonState = GUIButtonHide.ButtonHideState.OFF;
      } else if (this.buttonState == GUIButtonHide.ButtonHideState.OFF) {
         this.buttonState = GUIButtonHide.ButtonHideState.ON;
      }
   }

   private static enum ButtonHideState {
      ON,
      HALF,
      OFF;
   }
}
