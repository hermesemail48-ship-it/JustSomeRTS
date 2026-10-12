package rtr.gui.buttons;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.resources.ResourceModule;

public class GUIIcon extends GUIButton {
   private Image iconImage;

   public GUIIcon(String icon) throws SlickException {
      try {
         this.iconImage = ImageLoader.getImage("res/GUI/icon/" + icon + ".png");
      } catch (Exception var3) {
         this.iconImage = ImageLoader.getImage("res/GUI/icon/iconNone.png");
      }

      this.intersectBox = new Rectangle(0.0F, 0.0F, this.iconImage.getWidth() - 6, this.iconImage.getHeight() - 6);
   }

   public GUIIcon(ResourceModule.ResourceType t) throws SlickException {
      try {
         this.iconImage = this.resource.getResourceSprite(this.resource.getResourceSpriteSet(t, ResourceModule.ResourceColorSet.DEFAULT), 0);
      } catch (Exception var3) {
         this.iconImage = ImageLoader.getImage("res/GUI/icon/iconNone.png");
      }

      this.intersectBox = new Rectangle(0.0F, 0.0F, this.iconImage.getWidth(), this.iconImage.getHeight());
   }

   @Override
   public void render(Graphics g, int x, int y, boolean debug) {
      super.render(g, x, y, false);
      this.iconImage.draw(x, y);
      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }
}
