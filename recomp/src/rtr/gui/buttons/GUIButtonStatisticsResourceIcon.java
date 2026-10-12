package rtr.gui.buttons;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.resources.ResourceModule;

public class GUIButtonStatisticsResourceIcon extends GUIButton {
   private Image buttonFrame;
   private Image buttonResource;
   private ResourceModule.ResourceType resourceType;

   public GUIButtonStatisticsResourceIcon(String bName, ResourceModule.ResourceType rType) throws SlickException {
      this.buttonName = bName;
      this.resourceType = rType;
      this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/buttonStatisticsIcon.png");
      this.buttonResource = this.resource.getResourceSprite(this.resource.getResourceSpriteSet(this.resourceType, ResourceModule.ResourceColorSet.DEFAULT), 0);
      this.intersectBox = new Rectangle(0.0F, 0.0F, this.buttonFrame.getWidth() - 6, this.buttonFrame.getHeight() - 6);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean debug) {
      this.render(g, mouse, x, y, d, false, debug);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean center, boolean debug) {
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

      this.buttonResource.draw(x - 8 + this.buttonFrame.getWidth() / 2, y - 8 + this.buttonFrame.getHeight() / 2);
      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   public ResourceModule.ResourceType getResourceType() {
      return this.resourceType;
   }
}
