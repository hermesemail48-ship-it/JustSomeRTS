package rtr.console;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import rtr.ModuleBase;
import rtr.font.FontModule;
import rtr.font.Text;
import rtr.particles.ParticleModule;
import rtr.states.StateBase;
import rtr.system.ScaleControl;

public class ConsoleBanner {
   private FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   private ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   private float alpha = 0.0F;
   private String header;
   private String subtitle;
   private boolean fadeIn = true;
   private int decayTick;
   private int fadeTick;
   private float xShift = -100.0F;

   public ConsoleBanner(String h, String s) {
      this.header = h;
      this.subtitle = s;
   }

   public void render(float yOffset, Graphics g) {
      g.setColor(new Color(0.0F, 0.0F, 0.0F, this.alpha / 800.0F));
      g.fillRect(-10.0F, 290.0F + yOffset, ScaleControl.getInterfaceWidth() + 20, 26.0F);
      g.setColor(new Color(0.0F, 0.0F, 0.2F, this.alpha / 800.0F));
      g.fillRect(-10.0F, 316.0F + yOffset, ScaleControl.getInterfaceWidth() + 20, 20.0F);
      this.xShift += 0.3F;
      this.font
         .drawString(ScaleControl.getInterfaceWidth() / 2 + this.xShift, 288.0F + yOffset, this.header, (int)this.alpha * 2, Text.FontType.HEADER, 3, true);
      this.font.drawString(ScaleControl.getInterfaceWidth() / 2 - this.xShift, 313.0F + yOffset, this.subtitle, (int)this.alpha, Text.FontType.BODY, 2, true);
   }

   public void update(int totalBanners) {
      int fadeSpeed = totalBanners / 5 + 1;
      if (this.fadeIn) {
         this.alpha += 10.0F;
         if (this.alpha >= 255.0F) {
            this.fadeTick += fadeSpeed;
            this.alpha = 255.0F;
            if (this.fadeTick > 350) {
               this.fadeIn = false;
            }
         }
      } else if (this.alpha > 0.0F) {
         this.alpha -= 5.0F;
      } else {
         this.decayTick += fadeSpeed;
      }
   }

   public int getDecayTick() {
      return this.decayTick;
   }
}
