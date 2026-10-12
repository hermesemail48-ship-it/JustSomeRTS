package rtr.gui.states;

import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;

public class BrushLoader {
   private HashMap<Integer, byte[][]> brushMapRound = new HashMap<>();
   private HashMap<Integer, byte[][]> brushMapFilletedSquare = new HashMap<>();
   private HashMap<Integer, byte[][]> brushMapChamferedSquare = new HashMap<>();
   private HashMap<Integer, byte[][]> brushMapSquare = new HashMap<>();
   protected int brushSize = 4;
   protected BrushLoader.BrushType brushType = BrushLoader.BrushType.ROUND;

   public BrushLoader() throws SlickException {
      Image brushImage = ImageLoader.getImage("res/brushes.png");
      SpriteSheet brushSprites = new SpriteSheet(brushImage, 32, 32);
      int tileSheetH = brushSprites.getHorizontalCount();
      int tileSheetV = brushSprites.getVerticalCount();

      for (int y = 0; y < tileSheetV; y++) {
         for (int x = 0; x < tileSheetH; x++) {
            Image sprite = brushSprites.getSprite(x, y);
            byte[][] brushArray = new byte[32][32];

            for (int x1 = 0; x1 < brushSprites.getSprite(x, y).getWidth(); x1++) {
               for (int y1 = 0; y1 < brushSprites.getSprite(x, y).getHeight(); y1++) {
                  if (sprite.getColor(x1, y1).a == 0.0) {
                     brushArray[x1][y1] = 0;
                  } else if (sprite.getColor(x1, y1).r == 1.0) {
                     brushArray[x1][y1] = 2;
                  } else {
                     brushArray[x1][y1] = 1;
                  }
               }
            }

            if (y == 0) {
               this.brushMapRound.put(x, brushArray);
            }

            if (y == 1) {
               this.brushMapFilletedSquare.put(x, brushArray);
            }

            if (y == 2) {
               this.brushMapChamferedSquare.put(x, brushArray);
            }

            if (y == 3) {
               this.brushMapSquare.put(x, brushArray);
            }

            sprite.flushPixelData();
            brushArray = null;
         }
      }

      ImageLoader.unloadImage("res/brushes.png");
      SpriteSheet var11 = null;
   }

   private byte[][] getBrushRound(int brushSize) {
      return this.brushMapRound.get(brushSize - 1);
   }

   private byte[][] getBrushFilletedSquare(int brushSize) {
      return this.brushMapFilletedSquare.get(brushSize - 1);
   }

   private byte[][] getBrushChamferedSquare(int brushSize) {
      return this.brushMapChamferedSquare.get(brushSize - 1);
   }

   private byte[][] getBrushSquare(int brushSize) {
      return this.brushMapSquare.get(brushSize - 1);
   }

   public byte[][] getBrush(int i, BrushLoader.BrushType t) {
      if (t == BrushLoader.BrushType.ROUND) {
         return this.getBrushRound(i);
      } else if (t == BrushLoader.BrushType.FILLETED_SQUARE) {
         return this.getBrushFilletedSquare(i);
      } else if (t == BrushLoader.BrushType.CHAMFERED_SQUARE) {
         return this.getBrushChamferedSquare(i);
      } else {
         return t == BrushLoader.BrushType.SQUARE ? this.getBrushSquare(i) : null;
      }
   }

   public byte[][] getAccentBrush(int i, BrushLoader.BrushType t) {
      if (t == BrushLoader.BrushType.ROUND) {
         return this.getBrushRound(i + 2);
      } else if (t == BrushLoader.BrushType.FILLETED_SQUARE) {
         return this.getBrushFilletedSquare(i + 2);
      } else if (t == BrushLoader.BrushType.CHAMFERED_SQUARE) {
         return this.getBrushChamferedSquare(i + 2);
      } else {
         return t == BrushLoader.BrushType.SQUARE ? this.getBrushSquare(i + 2) : null;
      }
   }

   public int getBrushSize() {
      return this.brushSize;
   }

   public void setBrushSizeUp() {
      if (this.brushSize < 30) {
         this.brushSize++;
      }
   }

   public void setBrushSizeDown() {
      if (this.brushSize > 1) {
         this.brushSize--;
      }
   }

   public void setBrushSizeUp(int i) {
      this.brushSize += i;
      if (this.brushSize > 30) {
         this.brushSize = 30;
      }
   }

   public void setBrushSizeDown(int i) {
      this.brushSize -= i;
      if (this.brushSize <= 0) {
         this.brushSize = 1;
      }
   }

   public void setBrushSize(int i) {
      this.brushSize = i;
   }

   public BrushLoader.BrushType getBrushType() {
      return this.brushType;
   }

   public void setBrushType(BrushLoader.BrushType t) {
      this.brushType = t;
   }

   public static enum BrushType {
      ROUND,
      FILLETED_SQUARE,
      CHAMFERED_SQUARE,
      SQUARE;
   }
}
