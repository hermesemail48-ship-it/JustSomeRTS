package rtr.gui.buttons;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;

public class GUIButtonStatisticsMobIcon extends GUIButton {
   private Image buttonFrame;
   private MobBase.MobType mobType;
   private MobBase.MobGroup mobGroup;
   private MobBase mobInstance;

   public GUIButtonStatisticsMobIcon(String bName, MobBase.MobType mType) throws SlickException {
      this.buttonName = bName;
      this.mobType = mType;
      this.mobInstance = MobFactory.getMobInstance(this.mobType);
      this.mobGroup = this.mobInstance.getMobGroup();
      this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/buttonStatisticsIcon.png");
      this.intersectBox = new Rectangle(0.0F, 0.0F, this.buttonFrame.getWidth() - 6, this.buttonFrame.getHeight() - 6);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean debug) throws SlickException {
      this.render(g, mouse, x, y, d, false, debug);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean center, boolean debug) throws SlickException {
      if (center) {
         x -= this.buttonFrame.getWidth() / 2;
      }

      super.render(g, x, y, d);
      if (this.disabled) {
         this.buttonFrame.draw(x, y);
         this.buttonFrame.drawFlash(x, y, this.buttonFrame.getWidth(), this.buttonFrame.getHeight(), new Color(0, 0, 0, 100));
      } else if (!this.isSelected && !this.intersects(mouse)) {
         this.buttonFrame.draw(x, y);
      } else {
         this.buttonFrame.draw(x, y);
         GL11.glBlendFunc(774, 770);
         this.buttonFrame.drawFlash(x, y, this.buttonFrame.getWidth(), this.buttonFrame.getHeight(), Color.white);
         g.setDrawMode(Graphics.MODE_NORMAL);
      }

      this.mobInstance.renderSprite(g, x + 6, y + 6, debug);
      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   public MobBase.MobType getMobType() {
      return this.mobType;
   }

   public MobBase.MobGroup getMobGroup() {
      return this.mobGroup;
   }

   public MobBase getMobInstance() {
      return this.mobInstance;
   }
}
