package rtr.spawning;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.TimeModule;
import rtr.map.MapModule;
import rtr.states.StateBase;
import rtr.utilities.Utilities;
import rtr.weather.WeatherModule;

public class CorruptionFog {
   private static final int FOG_DURATION = 2000;
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private TimeModule time = (TimeModule)StateBase.getModule(ModuleBase.ModuleType.TIME);
   private WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   private static SpriteSheet corruptionFogs;
   private Image fog;
   private float x;
   private float y;
   private int tileX;
   private int tileY;
   private int durationTick;
   private float colorRed = 1.0F;
   private float colorGreen = 1.0F;
   private float colorBlue = 1.0F;
   private float colorAlpha = 0.0F;
   private float parallaxAmount = 1.0F;

   public CorruptionFog(int x, int y) throws SlickException {
      this.x = x;
      this.y = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
      if (corruptionFogs == null) {
         corruptionFogs = new SpriteSheet(ImageLoader.getImage("res/corruptionFogSheet.png"), 512, 64);
      }

      this.fog = corruptionFogs.getSprite(Utilities.randomInt(corruptionFogs.getHorizontalCount()), Utilities.randomInt(corruptionFogs.getVerticalCount()));
      this.parallaxAmount = this.parallaxAmount + Utilities.randomFloat() / 32.0F;
      if (Utilities.randomInt(3) == 0) {
         this.colorRed = this.colorRed - Utilities.randomFloat() / 2.0F;
         float darkness = Utilities.randomFloat() / 4.0F;
         this.colorGreen -= darkness;
         this.colorBlue -= darkness;
      } else {
         this.colorRed = this.colorGreen = this.colorBlue = 0.0F;
      }
   }

   public void render() {
      int startX = this.map.getMapRenderTileTopLeftX() - 4;
      int startY = this.map.getMapRenderTileTopLeftY() - 4;
      int endX = this.map.getMapRenderTileBottomRightX() + 4;
      int endY = this.map.getMapRenderTileBottomRightY() + 4;
      if (startX < 0) {
         startX = 0;
      }

      if (startY < 0) {
         startY = 0;
      }

      if (endX > 255) {
         endX = 255;
      }

      if (endY > 255) {
         endY = 255;
      }

      if (this.tileX > startX && this.tileX < endX && this.tileY > startY && this.tileY < endY) {
         int drawX = (int)((this.map.getMapX() + this.x - this.fog.getWidth() / 2) * this.parallaxAmount);
         int drawY = (int)((this.map.getMapY() + this.y - this.fog.getHeight() / 2) * this.parallaxAmount);
         this.fog
            .drawEmbedded(
               drawX,
               drawY,
               drawX + this.fog.getWidth(),
               drawY + this.fog.getHeight(),
               0.0F,
               0.0F,
               this.fog.getWidth(),
               this.fog.getHeight(),
               new Color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha)
            );
      }
   }

   public void update() {
      float alphaMax = this.getAlphaMax();
      if (this.durationTick < 400 && this.colorAlpha < alphaMax) {
         this.colorAlpha += 5.0E-4F;
      } else if (this.durationTick > 1600 && this.colorAlpha > 0.0F) {
         this.colorAlpha -= 5.0E-4F;
      }

      this.durationTick++;
   }

   private float getAlphaMax() {
      switch (this.time.getTimeOfDay()) {
         case NIGHT:
            return 0.1F;
         case DAWN:
            return 0.1F;
         case MORNING:
            return 0.15F;
         case MIDDAY:
            return 0.15F;
         case EVENING:
            return 0.15F;
         case DUSK:
            return 0.1F;
         default:
            if (this.weather.isRaining()) {
               this.colorAlpha -= 0.05F;
            }

            return 0.0F;
      }
   }

   public int getDurationTick() {
      return this.durationTick;
   }

   public static SpriteSheet getSpriteSheet() throws SlickException {
      if (corruptionFogs == null) {
         corruptionFogs = new SpriteSheet(ImageLoader.getImage("res/corruptionFogSheet.png"), 512, 64);
      }

      return corruptionFogs;
   }

   public static int getFogDuration() {
      return 2000;
   }
}
