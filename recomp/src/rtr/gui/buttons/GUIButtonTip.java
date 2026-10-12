package rtr.gui.buttons;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.help.tips.TipBase;
import rtr.utilities.Utilities;

public class GUIButtonTip extends GUIButton {
   private Image buttonFrame;
   private TipBase tip;
   private float tipBounceSpeed;
   private float tipBounce;

   public GUIButtonTip(TipBase t) throws SlickException {
      this.tip = t;
      this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/buttonTip" + (Utilities.randomInt(10) + 1) + ".png");
      this.intersectBox = new Rectangle(0.0F, 0.0F, this.buttonFrame.getWidth() - 6, this.buttonFrame.getHeight() - 6);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean debug) {
      super.render(g, x, (int)(y + this.tipBounce), d);
      if (this.disabled) {
         this.buttonFrame.draw(x, y + this.tipBounce);
         this.buttonFrame.drawFlash(x, y + this.tipBounce, this.buttonFrame.getWidth(), this.buttonFrame.getHeight(), new Color(0, 0, 0, 200));
      } else if (!this.intersects(mouse) && !this.isSelected) {
         this.buttonFrame.draw(x, y + this.tipBounce);
      } else {
         this.buttonFrame.draw(x, y + this.tipBounce);
         GL11.glBlendFunc(774, 770);
         this.buttonFrame.drawFlash(x, y + this.tipBounce, this.buttonFrame.getWidth(), this.buttonFrame.getHeight(), Color.white);
         g.setDrawMode(Graphics.MODE_NORMAL);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   public TipBase getTip() {
      return this.tip;
   }

   public void update() {
      this.tipBounceSpeed += 0.025F;
      this.tipBounce = this.tipBounce + this.tipBounceSpeed;
      if (this.tipBounce >= 0.0F) {
         this.tipBounceSpeed = -0.5F;
      }
   }
}
