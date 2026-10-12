package r2d.image;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class SpriteSheet {
   private Image sheet;
   private Image[][] sprites;
   private int spriteWidth;
   private int spriteHeight;
   private int imageWidth;
   private int imageHeight;
   private int horizontalCount;
   private int verticalCount;

   public SpriteSheet(Image sheet, int spriteWidth, int spriteHeight) {
      this.sheet = sheet;
      this.spriteWidth = spriteWidth;
      this.spriteHeight = spriteHeight;
      this.imageWidth = sheet.getWidth();
      this.imageHeight = sheet.getHeight();
      this.horizontalCount = this.imageWidth / spriteWidth;
      this.verticalCount = this.imageHeight / spriteHeight;
      if (this.imageWidth % spriteWidth != 0) {
         System.out.println("WARNING: Spritesheet size is off by " + this.imageWidth % spriteWidth + " horizontally.");
      }

      if (this.imageHeight % spriteHeight != 0) {
         System.out.println("WARNING: Spritesheet size is off by " + this.imageHeight % spriteWidth + " vertically.");
      }

      this.sprites = new Image[this.horizontalCount][this.verticalCount];

      for (int x = 0; x < this.horizontalCount; x++) {
         for (int y = 0; y < this.verticalCount; y++) {
            this.sprites[x][y] = sheet.getSubImage(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
         }
      }
   }

   public void startUse() {
      this.sheet.startUse();
   }

   public void endUse() {
      this.sheet.endUse();
   }

   public Image getSheet() {
      return this.sheet;
   }

   public Image[][] getSprites() {
      return this.sprites;
   }

   public Image getSprite(int x, int y) {
      return this.sprites[x][y];
   }

   public int getSpriteWidth() {
      return this.spriteWidth;
   }

   public int getSpriteHeight() {
      return this.spriteHeight;
   }

   public int getImageWidth() {
      return this.imageWidth;
   }

   public int getImageHeight() {
      return this.imageHeight;
   }

   public int getHorizontalCount() {
      return this.horizontalCount;
   }

   public int getVerticalCount() {
      return this.verticalCount;
   }

   public void flushPixelData() {
      this.sheet.flushPixelData();
   }

   public Color getColor(int x, int y) {
      return this.sheet.getColor(x, y);
   }
}
