package rtr.gui.states.worldmap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import rtr.utilities.Utilities;

public class MobDot {
   private int dotX;
   private int dotY;
   private int startX;
   private int startY;
   private int radius = 64;
   private Color dotColor;
   private Color shadowColor;
   private byte[][] blockMap;

   public MobDot(int x, int y, byte[][] bM, Color c) {
      this.blockMap = bM;
      this.dotX = x + Utilities.randomInt(this.radius * -1, this.radius);

      for (this.dotY = y + Utilities.randomInt(this.radius * -1, this.radius);
         Utilities.isOutOfBounds(this.blockMap.length, this.blockMap[0].length, this.dotX, this.dotY) || this.blockMap[this.dotX][this.dotY] > 0;
         this.dotY = y + Utilities.randomInt(this.radius * -1, this.radius)
      ) {
         this.dotX = x + Utilities.randomInt(this.radius * -1, this.radius);
      }

      this.startX = x;
      this.startY = y;
      this.dotColor = c;
      this.shadowColor = new Color(0, 0, 0, 100);
   }

   public void render(Graphics g, float mapX, float mapY) {
      g.setColor(this.shadowColor);
      g.fillRectEmbedded(mapX + this.dotX, mapY + this.dotY, 1.0F, 2.0F);
      g.setColor(this.dotColor);
      g.fillRectEmbedded(mapX + this.dotX, mapY + this.dotY, 1.0F, 1.0F);
   }

   public void update() {
      if (Utilities.randomInt(50) == 0) {
         int randomDirection = Utilities.randomInt(4);
         switch (randomDirection) {
            case 0:
               if (this.dotX < this.startX + this.radius && this.blockMap[this.dotX + 1][this.dotY] == 0) {
                  this.dotX++;
               }
               break;
            case 1:
               if (this.dotX > this.startX - this.radius && this.blockMap[this.dotX - 1][this.dotY] == 0) {
                  this.dotX--;
               }
               break;
            case 2:
               if (this.dotY < this.startY + this.radius && this.blockMap[this.dotX][this.dotY + 1] == 0) {
                  this.dotY++;
               }
               break;
            case 3:
               if (this.dotY > this.startY - this.radius && this.blockMap[this.dotX][this.dotY - 1] == 0) {
                  this.dotY--;
               }
         }
      }
   }
}
