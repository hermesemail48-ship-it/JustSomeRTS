package rtr.console;

import rtr.ModuleBase;
import rtr.font.FontModule;
import rtr.font.Text;
import rtr.states.StateBase;

public class ConsoleEntry {
   private FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   private float alphaCurrent = 0.0F;
   private float alphaMax = 255.0F;
   private String message;
   private boolean fadeIn = true;

   public ConsoleEntry(String m) {
      this.message = m;
   }

   public void render(int x, int y) {
      this.font.drawString(x, y, this.message, (int)this.alphaCurrent, Text.FontType.BODY, 1, false);
   }

   public void update() {
      if (this.fadeIn) {
         this.alphaCurrent += 10.0F;
         if (this.alphaCurrent > this.alphaMax) {
            this.fadeIn = false;
         }
      }
   }

   public void decreaseAlpha(float a) {
      this.alphaMax -= a;
      this.alphaCurrent -= a;
   }
}
