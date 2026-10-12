package rtr;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.utilities.ArrayFillUtil;
import rtr.utilities.Utilities;

public class BackgroundModule extends ModuleBase {
   private static final int particleCount = 10000;
   private boolean particleColorsGenerated;
   private SpriteSheet parallaxStars;
   private Color[] particleColors = new Color[10000];
   private float[] particleY = new float[10000];
   private float[] particleX = new float[10000];
   private float[] particleSpeedX = new float[10000];
   private float[] particleSpeedY = new float[10000];

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      if (!this.particleColorsGenerated) {
         this.parallaxStars = new SpriteSheet(ImageLoader.getImage("res/parallaxStars.png"), 1, 1);

         for (int x = 0; x < 10000; x++) {
            if (x < 2500) {
               this.particleColors[x] = this.parallaxStars.getSprite(Utilities.randomInt(8), Utilities.randomInt(4)).getColor(0, 0);
               this.particleSpeedX[x] = Utilities.randomFloat();
               this.particleSpeedY[x] = Utilities.randomFloat() / 32.0F;
            }

            if (x < 5000 && x >= 2500) {
               this.particleColors[x] = this.parallaxStars.getSprite(Utilities.randomInt(16), Utilities.randomInt(4)).getColor(0, 0);
               this.particleSpeedX[x] = Utilities.randomFloat() / 8.0F;
               this.particleSpeedY[x] = Utilities.randomFloat() / 32.0F;
            }

            if (x < 7500 && x >= 5000) {
               this.particleColors[x] = this.parallaxStars.getSprite(Utilities.randomInt(24), Utilities.randomInt(4)).getColor(0, 0);
               this.particleSpeedX[x] = Utilities.randomFloat() / 16.0F;
               this.particleSpeedY[x] = Utilities.randomFloat() / 32.0F;
            }

            if (x < 10000 && x >= 7500) {
               this.particleColors[x] = this.parallaxStars.getSprite(Utilities.randomInt(32), Utilities.randomInt(4)).getColor(0, 0);
               this.particleSpeedX[x] = Utilities.randomFloat() / 24.0F;
               this.particleSpeedY[x] = Utilities.randomFloat() / 32.0F;
            }

            this.particleX[x] = Utilities.randomInt(4096);
            this.particleY[x] = Utilities.randomInt(4096) & -2;
         }

         this.particleColorsGenerated = true;
      }
   }

   public void resetModule(StateBase cS) throws SlickException {
      super.resetModule();
      ArrayFillUtil.fill(this.particleY, 0.0F);
      ArrayFillUtil.fill(this.particleX, 0.0F);
      ArrayFillUtil.fill(this.particleSpeedX, 0.0F);
      ArrayFillUtil.fill(this.particleSpeedY, 0.0F);
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

   public void update() {
      if (SettingsParser.getBackground()) {
         for (int x = 0; x < 10000; x++) {
            if (this.particleX[x] >= 4196.0F) {
               this.particleX[x] = Utilities.randomInt(20) - 100;
               this.particleY[x] = Utilities.randomInt(4096) & -2;
            } else {
               this.particleX[x] = this.particleX[x] + this.particleSpeedX[x];
               this.particleY[x] = this.particleY[x] + this.particleSpeedY[x];
            }
         }
      }
   }

   public void render() throws SlickException {
      if (SettingsParser.getBackground()) {
         float mapX = this.map.getMapX();
         float mapY = this.map.getMapY();
         this.render(mapX, mapY, false);
      }
   }

   public void render(float posX, float posY, boolean useInterface) throws SlickException {
      if (SettingsParser.getBackground()) {
         Graphics g = new Graphics();
         int displayWidth;
         int displayHeight;
         if (useInterface) {
            displayWidth = ScaleControl.getInterfaceWidth();
            displayHeight = ScaleControl.getInterfaceHeight();
         } else {
            displayWidth = ScaleControl.getWorldWidth();
            displayHeight = ScaleControl.getWorldHeight();
         }

         g.glBeginQuads();

         for (int x = 0; x < 2500; x++) {
            if (this.particleX[x] + (posX - 128.0F) * 0.8F > -10.0F
               && this.particleX[x] + (posX - 128.0F) * 0.8F < displayWidth + 10
               && this.particleY[x] + (posY - 128.0F) * 0.8F > -10.0F
               && this.particleY[x] + (posY - 128.0F) * 0.8F < displayHeight + 10) {
               g.setColor(this.particleColors[x]);
               g.fillRectEmbedded((int)this.particleX[x] + (posX - 128.0F) * 0.8F, (int)this.particleY[x] + (posY - 128.0F) * 0.8F, 1.0F, 1.0F);
            }
         }

         for (int xx = 0; xx < 2500; xx++) {
            if (this.particleX[xx + 2500] + (posX - 128.0F) * 0.7F > -10.0F
               && this.particleX[xx + 2500] + (posX - 128.0F) * 0.7F < displayWidth + 10
               && this.particleY[xx + 2500] + (posY - 128.0F) * 0.7F > -10.0F
               && this.particleY[xx + 2500] + (posY - 128.0F) * 0.7F < displayHeight + 10) {
               g.setColor(this.particleColors[xx + 2500]);
               g.fillRectEmbedded((int)this.particleX[xx + 2500] + (posX - 128.0F) * 0.7F, (int)this.particleY[xx + 2500] + (posY - 128.0F) * 0.7F, 1.0F, 1.0F);
            }
         }

         for (int xxx = 0; xxx < 2500; xxx++) {
            if (this.particleX[xxx + 5000] + (posX - 128.0F) * 0.6F > -10.0F
               && this.particleX[xxx + 5000] + (posX - 128.0F) * 0.6F < displayWidth + 10
               && this.particleY[xxx + 5000] + (posY - 128.0F) * 0.6F > -10.0F
               && this.particleY[xxx + 5000] + (posY - 128.0F) * 0.6F < displayHeight + 10) {
               g.setColor(this.particleColors[xxx + 5000]);
               g.fillRectEmbedded(
                  (int)this.particleX[xxx + 5000] + (posX - 128.0F) * 0.6F, (int)this.particleY[xxx + 5000] + (posY - 128.0F) * 0.6F, 1.0F, 1.0F
               );
            }
         }

         for (int xxxx = 0; xxxx < 2500; xxxx++) {
            if (this.particleX[xxxx + 7500] + (posX - 128.0F) * 0.5F > -10.0F
               && this.particleX[xxxx + 7500] + (posX - 128.0F) * 0.5F < displayWidth + 10
               && this.particleY[xxxx + 7500] + (posY - 128.0F) * 0.5F > -10.0F
               && this.particleY[xxxx + 7500] + (posY - 128.0F) * 0.5F < displayHeight + 10) {
               g.setColor(this.particleColors[xxxx + 7500]);
               g.fillRectEmbedded(
                  (int)this.particleX[xxxx + 7500] + (posX - 128.0F) * 0.5F, (int)this.particleY[xxxx + 7500] + (posY - 128.0F) * 0.5F, 1.0F, 1.0F
               );
            }
         }

         g.glEnd();
      }
   }
}
