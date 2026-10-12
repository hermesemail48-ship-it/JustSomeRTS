package rtr;

import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class ShadowModule extends ModuleBase {
   private static final int SHADOW_FBO_TILE_SIZE = 256;
   private Color shadowColor = new Color(0.0F, 0.0F, 0.0F, 0.4F);
   private HashMap<Integer, Color> shadowColorMaps = new HashMap<>();
   private Image shadowFBO;
   private HashMap<Integer, byte[][][]> shadowMasks;
   private Image[][] shadowTiles;
   private SpriteSheet shadowSheet;
   private Color[][] shadowMap;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      if (this.shadowFBO == null) {
         this.shadowFBO = new Image(4096, 4096);
         this.shadowSheet = new SpriteSheet(this.shadowFBO, 256, 256);
         this.shadowTiles = new Image[this.shadowSheet.getHorizontalCount()][this.shadowSheet.getVerticalCount()];

         for (int x = 0; x < this.shadowTiles.length; x++) {
            for (int y = 0; y < this.shadowTiles[x].length; y++) {
               this.shadowTiles[x][y] = this.shadowSheet.getSprite(x, y);
            }
         }
      }

      if (this.shadowMasks == null) {
         this.shadowMasks = this.map.getMapTileLoader().getShadowTiles();
      }

      this.buildShadowMap();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.shadowMap = new Color[4096][4096];
      Graphics g = this.shadowFBO.getGraphics();
      g.clearArea(0, 0, this.shadowFBO.getWidth(), this.shadowFBO.getHeight());
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
   }

   @Override
   protected void loadPlay() throws SlickException {
   }

   @Override
   protected void newMapEditor() throws SlickException {
   }

   @Override
   protected void loadMapEditor() throws SlickException {
   }

   @Override
   protected void newWorldMap() throws SlickException {
   }

   @Override
   protected void loadWorldMap() throws SlickException {
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   public void render() throws SlickException {
      if (SettingsParser.getShadows()) {
         int startX = this.getMapRenderSheetTopLeftX() - 1;
         int startY = this.getMapRenderSheetTopLeftY() - 1;
         int endX = this.getMapRenderSheetBottomRightX() + 1;
         int endY = this.getMapRenderSheetBottomRightY() + 1;
         this.shadowFBO.startUse();

         for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
               if (x >= 0 && x <= this.shadowTiles.length - 1 && y >= 0 && y <= this.shadowTiles[x].length - 1) {
                  this.shadowTiles[x][y]
                     .drawEmbedded(
                        this.map.getMapX() + x * 256,
                        this.map.getMapY() + y * 256,
                        this.map.getMapX() + x * 256 + 256.0F,
                        this.map.getMapY() + y * 256 + 256.0F,
                        0.0F,
                        0.0F,
                        256.0F,
                        256.0F,
                        this.shadowColor
                     );
               }
            }
         }

         this.shadowFBO.endUse();
      }
   }

   public Image getShadowImage() throws SlickException {
      this.shadowFBO.setAlpha(this.shadowColor.a);
      return this.shadowFBO;
   }

   private void buildShadowMap() throws SlickException {
      this.shadowMap = new Color[4096][4096];

      for (int x = 0; x < 256; x++) {
         for (int y = 0; y < 256; y++) {
            for (int l = 0; l < 12; l++) {
               int tileID = this.map.getTileId(x, y, l);
               if (this.map.getMapTileLoader().isTileShadow(tileID)) {
                  byte[][][] mask = this.shadowMasks.get(tileID);

                  for (int w = 0; w < 16; w++) {
                     for (int h = 0; h < 16; h++) {
                        if (mask[w][h][0] > 0 || mask[w][h][1] > 0 || mask[w][h][2] > 0) {
                           int colorElement = mask[w][h][0] + 255 * (mask[w][h][1] + 255 * mask[w][h][2]);
                           Color c = this.shadowColorMaps.get(colorElement);
                           if (c == null) {
                              this.shadowColorMaps.put(colorElement, new Color(mask[w][h][0], mask[w][h][1], mask[w][h][2]));
                           }

                           c = this.shadowColorMaps.get(colorElement);
                           int cX = x * 16 + w;
                           int cY = y * 16 + h;
                           this.shadowMap[cX][cY] = c;
                        }
                     }
                  }
               }
            }
         }
      }

      Graphics g = null;
      g = this.shadowFBO.getGraphics();
      g.clearArea(0, 0, this.shadowFBO.getWidth(), this.shadowFBO.getHeight());
      g.setColor(Color.black);
      g.glBeginQuads();

      for (int x = 0; x < this.shadowMap.length; x++) {
         for (int y = 0; y < this.shadowMap[0].length; y++) {
            if (this.shadowMap[x][y] != null) {
               int shadowStop = this.shadowMap[x][y].getGreen() + this.shadowMap[x][y].getBlue();
               if (shadowStop > 0) {
                  int shadowStart = this.shadowMap[x][y].getRed();

                  for (int length = 0; length < shadowStop; length++) {
                     int cX = x + length;
                     int cY = y + length;
                     if (!Utilities.isOutOfBounds(4096, 4096, cX, cY)) {
                        if (this.shadowMap[cX][cY] == null) {
                           if (length >= shadowStart) {
                              g.fillRectEmbedded(cX, cY, 1.0F, 1.0F);
                           }
                        } else {
                           int thisPixelHeight = this.shadowMap[cX][cY].getGreen() + this.shadowMap[cX][cY].getBlue();
                           if (shadowStop > length + thisPixelHeight && shadowStart <= length + thisPixelHeight) {
                              g.fillRectEmbedded(cX, cY, 1.0F, 1.0F);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      g.glEnd();
      g.flush();
   }

   public void refreshPartMap(int tileX, int tileY, int refreshArea) throws SlickException {
      refreshArea /= 2;
      refreshArea += 3;
      int refreshStartX = tileX - refreshArea;
      int refreshStartY = tileY - refreshArea;
      int refreshEndX = tileX + refreshArea;
      int refreshEndY = tileY + refreshArea;
      int refreshStartPixelX = (tileX - refreshArea - 4) * 16;
      int refreshStartPixelY = (tileY - refreshArea - 4) * 16;
      int refreshEndPixelX = (tileX + refreshArea + 4) * 16;
      int refreshEndPixelY = (tileY + refreshArea + 4) * 16;

      for (int x = refreshStartPixelX; x <= refreshEndPixelX; x++) {
         for (int y = refreshStartPixelY; y <= refreshEndPixelY; y++) {
            if (!Utilities.isOutOfBounds(4096, 4096, x, y)) {
               this.shadowMap[x][y] = null;
            }
         }
      }

      for (int x = refreshStartX - 4; x <= refreshEndX + 4; x++) {
         for (int yx = refreshStartY - 4; yx <= refreshEndY + 4; yx++) {
            if (!Utilities.isOutOfBounds(256, 256, x, yx)) {
               for (int l = 0; l < 12; l++) {
                  int tileID = this.map.getTileId(x, yx, l);
                  if (this.map.getMapTileLoader().isTileShadow(tileID)) {
                     byte[][][] mask = this.shadowMasks.get(tileID);

                     for (int w = 0; w < 16; w++) {
                        for (int h = 0; h < 16; h++) {
                           if (mask[w][h][0] > 0 || mask[w][h][1] > 0 || mask[w][h][2] > 0) {
                              int colorElement = mask[w][h][0] + 255 * (mask[w][h][1] + 255 * mask[w][h][2]);
                              Color c = this.shadowColorMaps.get(colorElement);
                              if (c == null) {
                                 this.shadowColorMaps.put(colorElement, new Color(mask[w][h][0], mask[w][h][1], mask[w][h][2]));
                              }

                              c = this.shadowColorMaps.get(colorElement);
                              int cX = x * 16 + w;
                              int cY = yx * 16 + h;
                              this.shadowMap[cX][cY] = c;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      Graphics g = this.shadowFBO.getGraphics();
      g.clearArea(refreshStartX * 16, refreshStartY * 16, (1 + refreshArea * 2) * 16, (1 + refreshArea * 2) * 16);
      g.glBeginQuads();

      for (int x = refreshStartPixelX; x <= refreshEndPixelX; x++) {
         for (int yxx = refreshStartPixelY; yxx <= refreshEndPixelY; yxx++) {
            if (!Utilities.isOutOfBoundsOfUsableMapPixels(x, yxx) && this.shadowMap[x][yxx] != null) {
               int shadowStop = this.shadowMap[x][yxx].getGreen() + this.shadowMap[x][yxx].getBlue();
               if (shadowStop > 0) {
                  int shadowStart = this.shadowMap[x][yxx].getRed();

                  for (int length = 0; length < shadowStop; length++) {
                     int cX = x + length;
                     int cY = yxx + length;
                     if (!Utilities.isOutOfBoundsOfUsableMapPixels(cX, cY)) {
                        if (this.shadowMap[cX][cY] == null) {
                           if (length >= shadowStart) {
                              g.fillRectEmbedded(cX, cY, 1.0F, 1.0F);
                           }
                        } else {
                           int thisPixelHeight = this.shadowMap[cX][cY].getGreen() + this.shadowMap[cX][cY].getBlue();
                           if (shadowStop > length + thisPixelHeight && shadowStart <= length + thisPixelHeight) {
                              g.fillRectEmbedded(cX, cY, 1.0F, 1.0F);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      g.glEnd();
      g.flush();
   }

   public Color[][] getShadowMap() {
      return this.shadowMap;
   }

   public int getMapRenderSheetTopLeftX() {
      return (int)(this.map.getMapX() * -1.0F) / 256;
   }

   public int getMapRenderSheetTopLeftY() {
      return (int)(this.map.getMapY() * -1.0F) / 256;
   }

   public int getMapRenderSheetTopRightX() {
      return (int)(this.map.getMapX() * -1.0F + ScaleControl.getWorldWidth()) / 256;
   }

   public int getMapRenderSheetTopRightY() {
      return (int)(this.map.getMapY() * -1.0F) / 256;
   }

   public int getMapRenderSheetBottomLeftX() {
      return (int)(this.map.getMapX() * -1.0F) / 256;
   }

   public int getMapRenderSheetBottomLeftY() {
      return (int)(this.map.getMapY() * -1.0F + ScaleControl.getWorldHeight()) / 256;
   }

   public int getMapRenderSheetBottomRightX() {
      return (int)(this.map.getMapX() * -1.0F + ScaleControl.getWorldWidth()) / 256;
   }

   public int getMapRenderSheetBottomRightY() {
      return (int)(this.map.getMapY() * -1.0F + ScaleControl.getWorldHeight()) / 256;
   }
}
