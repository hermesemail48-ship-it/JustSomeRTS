package rtr.weather;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.TimeModule;
import rtr.lighting.LightingModule;
import rtr.map.MapModule;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class Cloud {
   private static final int FIXED_MOUSE_DISTANCE = 256;
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private TimeModule time = (TimeModule)StateBase.getModule(ModuleBase.ModuleType.TIME);
   private LightingModule lighting = (LightingModule)StateBase.getModule(ModuleBase.ModuleType.LIGHTING);
   private static ArrayList<Image> cloudImages;
   private Image cloudImage;
   private Color cloudColor;
   private CloudCluster cluster;
   private float renderX;
   private float renderY;
   private float alpha;
   private float shadowAlpha;
   private float cloudX;
   private float cloudY;
   private float relatvePosX;
   private float relatvePosY;
   private float velXMod;
   private float velYMod;
   private boolean decaying;

   public Cloud(float cloudX, float cloudY, CloudCluster cluster) throws SlickException {
      this.cloudX = cloudX;
      this.cloudY = cloudY;
      this.cluster = cluster;
      this.cloudColor = cluster.getRandomClusterColor();
      this.relatvePosX = cloudX - cluster.getClusterX();
      this.relatvePosY = cloudY - cluster.getClusterY();
      this.velXMod = (Utilities.randomFloat() + Utilities.randomFloat() * -1.0F) / 100.0F;
      this.velYMod = (Utilities.randomFloat() + Utilities.randomFloat() * -1.0F) / 100.0F;
      this.loadCloudImage();
   }

   private void loadCloudImage() throws SlickException {
      if (cloudImages == null || cloudImages.size() == 0) {
         cloudImages = new ArrayList<>();
         SpriteSheet cloudsSheet = new SpriteSheet(ImageLoader.getImage("res/clouds/cloudShapes.png"), 64, 64);

         for (int h = 0; h < cloudsSheet.getHorizontalCount(); h++) {
            for (int v = 0; v < cloudsSheet.getVerticalCount(); v++) {
               Image cloudOut = null;
               cloudOut = cloudsSheet.getSprite(h, v);
               cloudImages.add(cloudOut);
               cloudOut = cloudsSheet.getSprite(h, v).getFlippedCopy(true, false);
               cloudImages.add(cloudOut);
               cloudOut = cloudsSheet.getSprite(h, v).getFlippedCopy(false, true);
               cloudImages.add(cloudOut);
               cloudOut = cloudsSheet.getSprite(h, v).getFlippedCopy(true, true);
               cloudImages.add(cloudOut);
            }
         }
      }

      this.cloudImage = cloudImages.get(Utilities.randomInt(cloudImages.size()));
   }

   public static void startUse() {
      cloudImages.get(0).startUse();
   }

   public static void endUse() {
      cloudImages.get(0).endUse();
   }

   public void render(Graphics g, boolean isShadow) throws SlickException {
      if (this.map.isInView(this.cloudX, this.cloudY, 64)) {
         this.renderX = 10.0F;
         this.renderY = 10.0F;
         if (isShadow) {
            this.cloudImage.setImageColor(0.0F, 0.0F, 0.0F, this.shadowAlpha);
            this.renderX = this.renderX + (this.map.getMapX() + this.cloudX - this.cloudImage.getWidth() / 2);
            this.renderY = this.renderY + (this.map.getMapY() + this.cloudY - this.cloudImage.getHeight() / 2);
         } else {
            this.cloudImage.setImageColor(this.cloudColor.r, this.cloudColor.g, this.cloudColor.b, this.alpha);
            this.renderX = this.renderX + (this.map.getMapX() + this.cloudX - this.cloudImage.getWidth() / 2);
            this.renderY = this.renderY + (this.map.getMapY() + this.cloudY - this.cloudImage.getHeight() / 2);
            float scaleFactor = ScaleControl.getWorldScale() * 0.1F;
            this.renderX = this.renderX + (this.map.getMapX() + this.cloudX - ScaleControl.getWorldCenterX()) * scaleFactor;
            this.renderY = this.renderY + (this.map.getMapY() + this.cloudY - ScaleControl.getWorldCenterY()) * scaleFactor;
         }

         if (g != null) {
            g.drawImageEmbedded(this.cloudImage, this.renderX, this.renderY);
         }
      }
   }

   public void updateNew() {
      float desiredAlpha = this.cloudColor.a;
      float desiredShadowAlpha = this.cloudColor.a / 1.5F;
      TimeModule.TimeOfDay tOD = this.time.getTimeOfDay();
      if (tOD == TimeModule.TimeOfDay.DAWN || tOD == TimeModule.TimeOfDay.DUSK) {
         desiredAlpha -= 0.3F;
         desiredShadowAlpha -= 0.3F;
      } else if (tOD == TimeModule.TimeOfDay.MORNING || tOD == TimeModule.TimeOfDay.EVENING) {
         desiredAlpha -= 0.2F;
         desiredShadowAlpha -= 0.2F;
      } else if (tOD == TimeModule.TimeOfDay.NIGHT) {
         desiredAlpha -= 0.4F;
         desiredShadowAlpha -= 0.4F;
      }

      int tileX = (int)(this.cloudX / 16.0F);
      int tileY = (int)(this.cloudY / 16.0F);
      if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
         float thisTileLight = this.lighting.getLightValueMap()[tileX][tileY];
         if (thisTileLight > 0.0F) {
            float desiredAlphaLightOffset = thisTileLight * 0.05F;
            if (desiredAlphaLightOffset > this.cloudColor.a) {
               desiredAlphaLightOffset = this.cloudColor.a;
            }

            desiredAlpha += desiredAlphaLightOffset;
         }
      }

      if (desiredAlpha < 0.15F) {
         desiredAlpha = 0.15F;
      } else if (desiredAlpha > this.cloudColor.a) {
         desiredAlpha = this.cloudColor.a;
      }

      if (desiredShadowAlpha < 0.15F) {
         desiredShadowAlpha = 0.15F;
      } else if (desiredShadowAlpha > this.cloudColor.a / 1.5F) {
         desiredShadowAlpha = this.cloudColor.a / 1.5F;
      }

      boolean nearMouse = this.isNearMouse();
      if (nearMouse) {
         this.alpha -= 0.025F;
         this.shadowAlpha -= 0.025F;
         if (!this.decaying) {
            if (this.alpha <= 0.15F) {
               this.alpha = 0.15F;
            }

            if (this.shadowAlpha <= 0.15F) {
               this.shadowAlpha = 0.15F;
            }
         }
      } else if (this.decaying) {
         this.alpha -= 5.0E-4F;
         this.shadowAlpha -= 5.0E-4F;
         if (this.alpha < 0.0F) {
            this.alpha = 0.0F;
         }

         if (this.shadowAlpha < 0.0F) {
            this.shadowAlpha = 0.0F;
         }

         if (this.alpha <= 0.01F && this.shadowAlpha <= 0.01F) {
            this.cluster.removeCloud(this);
         }
      } else {
         float amount = 2.0E-4F;
         if (this.alpha < desiredAlpha) {
            this.alpha += amount;
         } else if (this.alpha > desiredAlpha) {
            this.alpha -= amount;
         }

         if (this.shadowAlpha < desiredShadowAlpha) {
            this.shadowAlpha += amount / 2.0F;
         } else if (this.shadowAlpha > desiredShadowAlpha) {
            this.shadowAlpha -= amount / 2.0F;
         }
      }

      this.cloudX = this.cloudX + (this.cluster.getVelXMod() + this.velXMod);
      this.cloudY = this.cloudY + this.velYMod;
      float baseX = this.cluster.getClusterX() + this.relatvePosX;
      float baseY = this.cluster.getClusterY() + this.relatvePosY;
      if (this.cloudX > baseX) {
         this.cloudX -= 0.025F;
      }

      if (this.cloudX < baseX) {
         this.cloudX += 0.025F;
      }

      if (this.cloudY > baseY) {
         this.cloudY -= 0.025F;
      }

      if (this.cloudY < baseY) {
         this.cloudY += 0.025F;
      }

      if (nearMouse) {
         int mouseX = Game.getCS().getMouseOnMapX();
         int mouseY = Game.getCS().getMouseOnMapY();
         float angle = Utilities.calculateAngle(this.cloudX, this.cloudY, mouseX, mouseY);
         this.cloudX = (float)(this.cloudX - Math.cos(angle) / 2.5);
         this.cloudY = (float)(this.cloudY - Math.sin(angle) / 2.5);
      }
   }

   public void updateOld() {
      float desiredAlpha = this.cloudColor.a;
      float desiredShadowAlpha = this.cloudColor.a / 1.5F;
      switch (this.time.getTimeOfDay()) {
         case NIGHT:
            desiredAlpha -= 0.4F;
            desiredShadowAlpha -= 0.4F;
            break;
         case DAWN:
         case DUSK:
            desiredAlpha -= 0.3F;
            desiredShadowAlpha -= 0.3F;
            break;
         case MORNING:
         case EVENING:
            desiredAlpha -= 0.2F;
            desiredShadowAlpha -= 0.2F;
         case MIDDAY:
      }

      int tileX = (int)(this.cloudX / 16.0F);
      int tileY = (int)(this.cloudY / 16.0F);
      if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY)) {
         float thisTileLight = this.lighting.getLightValueMap()[tileX][tileY];
         if (thisTileLight > 0.0F) {
            float desiredAlphaLightOffset = thisTileLight * 0.05F;
            if (desiredAlphaLightOffset > this.cloudColor.a) {
               desiredAlphaLightOffset = this.cloudColor.a;
            }

            desiredAlpha += desiredAlphaLightOffset;
         }
      }

      if (desiredAlpha < 0.15F) {
         desiredAlpha = 0.15F;
      }

      if (desiredAlpha > this.cloudColor.a) {
         desiredAlpha = this.cloudColor.a;
      }

      if (desiredShadowAlpha < 0.15F) {
         desiredShadowAlpha = 0.15F;
      }

      if (desiredShadowAlpha > this.cloudColor.a / 1.5F) {
         desiredShadowAlpha = this.cloudColor.a / 1.5F;
      }

      boolean nearMouse = this.isNearMouse();
      if (nearMouse) {
         this.alpha -= 0.025F;
         if (this.alpha <= 0.15F && !this.decaying) {
            this.alpha = 0.15F;
         }

         this.shadowAlpha -= 0.025F;
         if (this.shadowAlpha <= 0.15F && !this.decaying) {
            this.shadowAlpha = 0.15F;
         }
      } else if (this.decaying) {
         this.alpha -= 5.0E-4F;
         if (this.alpha < 0.0F) {
            this.alpha = 0.0F;
         }

         this.shadowAlpha -= 5.0E-4F;
         if (this.shadowAlpha < 0.0F) {
            this.shadowAlpha = 0.0F;
         }

         if (this.alpha <= 0.01F && this.shadowAlpha <= 0.01F) {
            this.cluster.removeCloud(this);
         }
      } else {
         float amount = 2.0E-4F;
         if (this.alpha < desiredAlpha) {
            this.alpha += amount;
         } else if (this.alpha > desiredAlpha) {
            this.alpha -= amount;
         }

         if (this.shadowAlpha < desiredShadowAlpha) {
            this.shadowAlpha += amount / 2.0F;
         } else if (this.shadowAlpha > desiredShadowAlpha) {
            this.shadowAlpha -= amount / 2.0F;
         }
      }

      this.cloudX = this.cloudX + (this.cluster.getVelXMod() + this.velXMod);
      this.cloudY = this.cloudY + this.velYMod;
      if (this.cloudX > this.cluster.getClusterX() + this.relatvePosX) {
         this.cloudX -= 0.025F;
      }

      if (this.cloudX < this.cluster.getClusterX() + this.relatvePosX) {
         this.cloudX += 0.025F;
      }

      if (this.cloudY > this.cluster.getClusterY() + this.relatvePosY) {
         this.cloudY -= 0.025F;
      }

      if (this.cloudY < this.cluster.getClusterY() + this.relatvePosY) {
         this.cloudY += 0.025F;
      }

      if (nearMouse) {
         int mouseX = Game.getCS().getMouseOnMapX();
         int mouseY = Game.getCS().getMouseOnMapY();
         float angle = Utilities.calculateAngle(this.cloudX, this.cloudY, mouseX, mouseY);
         this.cloudX = (float)(this.cloudX - Math.cos(angle) / 2.5);
         this.cloudY = (float)(this.cloudY - Math.sin(angle) / 2.5);
      }
   }

   private boolean isNearMouse() {
      if (!this.map.isInView(this.cloudX, this.cloudY, 64)) {
         return false;
      } else {
         int mouseX = (int)(Game.getCS().getMouseX() / ScaleControl.getScalingDifference());
         int mouseY = (int)(Game.getCS().getMouseY() / ScaleControl.getScalingDifference());
         if (!(Math.abs(mouseX - this.renderX) > 256.0F) && !(Math.abs(mouseY - this.renderY) > 256.0F)) {
            int x2 = (int)this.renderX;
            int y2 = (int)this.renderY;
            double finalDistance = Math.sqrt((y2 - mouseY) * (y2 - mouseY) + (x2 - mouseX) * (x2 - mouseX));
            return finalDistance < 256.0;
         } else {
            return false;
         }
      }
   }

   public void triggerDecay() {
      this.decaying = true;
   }
}
