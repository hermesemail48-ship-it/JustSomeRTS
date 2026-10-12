package rtr.gui.buttons;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceModule;

public class GUIButtonResource extends GUIButton {
   private Image resourceSprite;
   private ResourceModule.ResourceType resourceType;

   public GUIButtonResource(ResourceModule.ResourceType rType, String bName) throws SlickException {
      this.buttonName = bName;
      this.resourceType = rType;

      try {
         this.resourceSprite = this.resource
            .getResourceSprite(this.resource.getResourceSpriteSet(this.resourceType, ResourceModule.ResourceColorSet.DEFAULT), 0);
      } catch (Exception var4) {
         this.resourceSprite = ImageLoader.getImage("res/GUI/icon/iconNone.png");
      }

      this.intersectBox = new Rectangle(0.0F, 0.0F, this.resourceSprite.getWidth() - 6, this.resourceSprite.getHeight() - 6);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, ObjectBase o, boolean debug) {
      super.render(g, x, y, false);
      this.resourceSprite.draw(x, y);
      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }
}
