package rtr.gui.buttons;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class GUIClickableArea extends GUIButton {
   public GUIClickableArea(String bName, int w, int h) throws SlickException {
      this.buttonName = bName;
      this.intersectBox = new Rectangle(0.0F, 0.0F, w, h);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean d, boolean debug) {
      super.render(g, x, y, d);
      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }
}
