package rtr.font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import rtr.ModuleBase;
import rtr.map.MapModule;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class FloaterObject {
   private FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private String text;
   private Image icon;
   private int fade = 255;
   private int fadeSpeed = 3;
   private float alpha = 1.0F;
   private float xPos;
   private float yPos;
   private float randomX;
   private float randomY;
   private boolean fixed;

   public FloaterObject(String t, float x, float y, boolean f) {
      this(null, t, x, y, f);
   }

   public FloaterObject(Image i, float x, float y, boolean f) {
      this(i, null, x, y, f);
   }

   public FloaterObject(String t, float x, float y) {
      this(null, t, x, y, false);
   }

   public FloaterObject(Image i, float x, float y) {
      this(i, null, x, y, false);
   }

   public FloaterObject(Image i, String t, float x, float y, boolean f) {
      this.icon = i;
      if (f) {
         this.randomX = (Utilities.randomFloat() - Utilities.randomFloat()) / 100.0F;
         this.randomY = (Utilities.randomFloat() - Utilities.randomFloat()) / 50.0F;
      } else {
         this.randomX = (Utilities.randomFloat() - Utilities.randomFloat()) / 10.0F;
         this.randomY = (Utilities.randomFloat() - Utilities.randomFloat()) / 5.0F;
      }

      this.text = t;
      this.xPos = x;
      this.yPos = y;
      this.fixed = f;
   }

   public void render(Graphics g) {
      float mapX = this.map.getMapX();
      float mapY = this.map.getMapY();
      float transformedX = this.xPos + mapX;
      float transformedY = this.yPos + mapY;
      if (this.map.isInView(this.xPos, this.yPos, 10)) {
         if (this.icon != null && this.text != null) {
            g.drawImage(this.icon, transformedX - 5.0F, transformedY, new Color(255.0F, 255.0F, 255.0F, this.alpha));
         }

         if (this.icon != null && this.text == null) {
            g.drawImage(this.icon, transformedX - 4.0F, transformedY, new Color(255.0F, 255.0F, 255.0F, this.alpha));
         }
      }

      if (this.text != null) {
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         transformedX = ScaleControl.getTransformedPositionX(this.xPos);
         transformedY = ScaleControl.getTransformedPositionY(this.yPos);
         if (this.map.isInView(this.xPos, this.yPos, 10)) {
            if (this.icon != null) {
               this.font.drawString(transformedX + 10.0F, transformedY, this.text, this.fade, Text.FontType.HEADER, 4, false);
            }

            if (this.icon == null) {
               this.font.drawString(transformedX, transformedY, this.text, this.fade, Text.FontType.HEADER, 4, true);
            }
         }

         g.resetTransform();
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
      }
   }

   public void update() {
      if (this.fixed) {
         this.xPos = this.xPos + this.randomX;
         this.yPos = this.yPos - (this.randomY + 0.03F);
      } else {
         this.xPos = this.xPos + this.randomX;
         this.yPos = this.yPos - (this.randomY + 0.3F);
      }

      this.fade = this.fade - this.fadeSpeed;
      if (this.icon != null) {
         this.alpha = this.fade * 0.01F;
      }

      if (this.fade <= 0) {
         this.font.removeFloater(this);
      }
   }
}
