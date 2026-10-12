package rtr.utilities.devtools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;

public class MobSheetGenerator {
   private static final int ANIMATION_COUNT = 101;
   private static final int ANIMATION_FRAMES = 8;

   public static void buildSpriteSheet(boolean overwriteMode) throws SlickException {
      Image mobBaseSpriteSheet = ImageLoader.getImage("res/mobs/baseMobsSheet.png");
      SpriteSheet sheet = new SpriteSheet(mobBaseSpriteSheet, 128, 1616);

      for (int number = 0; number < sheet.getHorizontalCount(); number++) {
         if (!overwriteMode) {
            File fileCheck = new File("res/mobs/" + number + ".png");
            if (fileCheck.exists()) {
               System.out.println("Mob Sheet " + number + " already exists, skipping.");
               continue;
            }
         }

         Image currentSheet = sheet.getSprite(number, 0);
         HashMap<Integer, ArrayList<Color>> mapping = new HashMap<>();
         HashMap<Integer, Boolean> dontColorMapping = new HashMap<>();
         int layers = 0;

         for (int l = 0; l < 16; l++) {
            Color layerCheckColor = currentSheet.getColor(l, 0);
            if (layerCheckColor.a == 0.0F) {
               break;
            }

            layers = l + 1;
            mapping.put(l, new ArrayList<>());
            Color dontColorCheck = currentSheet.getColor(l, 15);
            if (dontColorCheck.a > 0.0F) {
               dontColorMapping.put(l, true);
            } else {
               dontColorMapping.put(l, false);
            }

            for (int colorLayer = 0; layerCheckColor.a != 0.0F; layerCheckColor = currentSheet.getColor(l, ++colorLayer)) {
               mapping.get(l).add(layerCheckColor);
            }
         }

         int shiftX = 128;
         Image parsedSheet = new Image(128 * layers, 1616);
         Graphics g = parsedSheet.getGraphics();

         for (int w = 0; w < currentSheet.getWidth(); w++) {
            label153:
            for (int h = 16; h < currentSheet.getHeight(); h++) {
               Color currentSheetColor = currentSheet.getColor(w, h);
               if (currentSheetColor.a != 0.0F) {
                  for (Integer i : mapping.keySet()) {
                     for (Color c : mapping.get(i)) {
                        if (currentSheetColor.equals(c)) {
                           if (!dontColorMapping.get(i)) {
                              if (currentSheetColor.r >= currentSheetColor.b && currentSheetColor.r >= currentSheetColor.g) {
                                 currentSheetColor.b = currentSheetColor.r;
                                 currentSheetColor.g = currentSheetColor.r;
                              } else if (currentSheetColor.g >= currentSheetColor.r && currentSheetColor.g >= currentSheetColor.b) {
                                 currentSheetColor.r = currentSheetColor.g;
                                 currentSheetColor.b = currentSheetColor.g;
                              } else if (currentSheetColor.b >= currentSheetColor.r && currentSheetColor.b >= currentSheetColor.g) {
                                 currentSheetColor.r = currentSheetColor.b;
                                 currentSheetColor.g = currentSheetColor.b;
                              }
                           }

                           g.setColor(currentSheetColor);
                           g.fillRect(w + i * shiftX, h - 16, 1.0F, 1.0F);
                           continue label153;
                        }
                     }
                  }
               }
            }
         }

         g.flush();
         ImageOut.write(parsedSheet, "res/mobs/" + number + ".png", true);
         parsedSheet = new Image(256, 1616);
         g = parsedSheet.getGraphics();
         SpriteSheet currentSheetSprites = new SpriteSheet(currentSheet, 16, 16);

         for (int x2 = 0; x2 < currentSheetSprites.getHorizontalCount(); x2++) {
            for (int y2 = 0; y2 < currentSheetSprites.getVerticalCount(); y2++) {
               Image currentSprite = currentSheetSprites.getSprite(x2, y2);
               Image flip = currentSprite.getFlippedCopy(false, true);
               flip = flip.getScaledCopy(0.75F);
               flip.setImageColor(0.0F, 0.0F, 0.0F);
               flip.drawSheared(x2 * 16 * 2, (y2 - 1) * 16, 13.0F, 0.0F, Color.black);
               flip.drawSheared(x2 * 16 * 2 + 1, (y2 - 1) * 16, 13.0F, 0.0F, Color.black);
               g.setColor(Color.black);

               for (int x3 = 0; x3 < currentSprite.getWidth(); x3++) {
                  for (int y3 = 0; y3 < currentSprite.getHeight(); y3++) {
                     if (currentSprite.getColor(x3, y3).getAlpha() > 0) {
                        int pixelX = x2 * 2 * 16 + x3;
                        int pixelY = (y2 - 1) * 16;
                        if (x3 != 0 && x3 != 16) {
                           g.fillRect(pixelX, pixelY, 1.0F, 1.0F);
                           g.fillRect(pixelX, pixelY + 3, 1.0F, 1.0F);
                        }

                        g.fillRect(pixelX, pixelY + 1, 1.0F, 1.0F);
                        g.fillRect(pixelX, pixelY + 2, 1.0F, 1.0F);
                        if (x3 != 0) {
                           g.fillRect(pixelX - 1, pixelY + 1, 1.0F, 1.0F);
                           g.fillRect(pixelX - 1, pixelY + 2, 1.0F, 1.0F);
                        }

                        if (x3 != 16) {
                           g.fillRect(pixelX + 1, pixelY + 1, 1.0F, 1.0F);
                           g.fillRect(pixelX + 1, pixelY + 2, 1.0F, 1.0F);
                        }
                        break;
                     }
                  }
               }

               currentSprite.flushPixelData();
            }
         }

         g.flush();
         ImageOut.write(parsedSheet, "res/mobs/" + number + "shadow.png", true);
         currentSheet.flushPixelData();

         try {
            Thread.sleep(10L);
         } catch (InterruptedException var21) {
            var21.printStackTrace();
         }

         System.gc();

         try {
            Thread.sleep(10L);
         } catch (InterruptedException var20) {
            var20.printStackTrace();
         }

         parsedSheet.destroy();
         Image var26 = null;
         System.out.println("Mob Sheet " + number + " regenerated.");
      }
   }
}
