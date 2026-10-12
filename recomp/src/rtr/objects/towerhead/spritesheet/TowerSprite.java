package rtr.objects.towerhead.spritesheet;

import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.animation.Animation;
import r2d.image.SpriteSheet;
import rtr.objects.towerhead.spritesheet.templates.TowerSpriteTemplateBase;
import rtr.utilities.Utilities;

public class TowerSprite {
   private Animation animation;
   private HashMap<Integer, Animation> animationMap = new HashMap<>();
   private static Color shadow = new Color(0, 0, 0, 150);
   private TowerSpriteTemplateBase spriteTemplate;
   private SpriteSheet spriteArray;
   private int spriteSize;
   private boolean randomFrames;
   private boolean playingAnimation;
   private int randomFramesChance = 1000;

   public TowerSprite(SpriteSheet sheet, TowerSpriteTemplateBase t) throws SlickException {
      this.spriteArray = sheet;
      this.spriteTemplate = t;
      this.spriteSize = this.spriteArray.getSprite(0, 0).getWidth();

      TowerSprite.TowerAnimationFrames[] var6;
      for (TowerSprite.TowerAnimationFrames tA : TowerSprite.TowerAnimationFrames.values()) {
         int row = tA.getID();
         TowerSpriteTemplateBase.TowerAnimationConfig c = tA.getConfig();
         Image[] animationImages = new Image[this.spriteArray.getHorizontalCount()];

         for (int i = 0; i < this.spriteArray.getHorizontalCount(); i++) {
            animationImages[i] = this.spriteArray.getSprite(i, row);
         }

         this.animationMap.put(row, new Animation(animationImages));
         this.animationMap.get(row).setSpeed(t.getAnimationDurations().get(c));
         this.animationMap.get(row).setMode(t.getAnimationFlags().get(c));
      }

      this.setPosition(TowerSprite.TowerAnimationFrames.FACE_UNLOADED);
   }

   public void render(float spriteX, float spriteY, float angle, boolean drawEmbedded) throws SlickException {
      if (drawEmbedded) {
         this.animation.renderEmbedded(spriteX - this.spriteSize / 2 + 8.0F, spriteY - this.spriteSize / 2 + 8.0F + 1.0F, angle, shadow);
         this.animation.renderEmbedded(spriteX - this.spriteSize / 2 + 8.0F, spriteY - this.spriteSize / 2 + 8.0F, angle, Color.white);
      } else {
         this.animation.render(spriteX - this.spriteSize / 2 + 8.0F, spriteY - this.spriteSize / 2 + 8.0F + 1.0F, angle, shadow);
         this.animation.render(spriteX - this.spriteSize / 2 + 8.0F, spriteY - this.spriteSize / 2 + 8.0F, angle);
      }
   }

   public void update() throws SlickException {
      if (this.randomFrames && Utilities.randomInt(this.randomFramesChance) == 0) {
         this.randomFrame();
      }

      this.animation.update();
      if (this.playingAnimation && this.animation.isStopped()) {
         this.playingAnimation = false;
      }
   }

   public void playAnimation(TowerSprite.TowerAnimationFrames a) {
      this.animation = this.animationMap.get(a.getID());
      this.animationMap.get(a.getID()).restart();
      this.playingAnimation = true;
   }

   public void setPosition(TowerSprite.TowerAnimationFrames position) {
      if (!this.playingAnimation) {
         this.animation = this.animationMap.get(position.getID());
         if (this.animation.getMode() == Animation.AnimationMode.MANUAL_RANDOM) {
            this.randomFrames = true;
            this.randomFramesChance = this.animationMap.get(position.getID()).getSpeed();
         } else {
            this.randomFrames = false;
         }
      }
   }

   public void randomFrame() {
      int randomFrame = Utilities.randomInt(this.animation.getFrameCount());
      this.animation.setFrame(randomFrame);
   }

   public void setFrame(int i) {
      this.animation.setFrame(i);
   }

   public int getFrameCount() {
      return this.animation.getFrameCount();
   }

   public int getSpriteWidth() {
      return this.animation.getCurrentFrame().getWidth();
   }

   public int getSpriteHeight() {
      return this.animation.getCurrentFrame().getHeight();
   }

   public TowerSpriteTemplateBase getSpriteTemplate() {
      return this.spriteTemplate;
   }

   public boolean isStopped() {
      return this.animation.isStopped();
   }

   public void restart() {
      this.animation.restart();
   }

   public static enum TowerAnimationFrames {
      FACE_LOADED(0, TowerSpriteTemplateBase.TowerAnimationConfig.FACE_LOADED),
      FACE_UNLOADED(1, TowerSpriteTemplateBase.TowerAnimationConfig.FACE_UNLOADED),
      FIRE(2, TowerSpriteTemplateBase.TowerAnimationConfig.FIRE),
      RELOAD(3, TowerSpriteTemplateBase.TowerAnimationConfig.RELOAD);

      private final int mobAnimID;
      private final TowerSpriteTemplateBase.TowerAnimationConfig mobAnimConfig;

      private TowerAnimationFrames(int mobAnimID, TowerSpriteTemplateBase.TowerAnimationConfig mobAnimConfig) {
         this.mobAnimID = mobAnimID;
         this.mobAnimConfig = mobAnimConfig;
      }

      public int getID() {
         return this.mobAnimID;
      }

      public TowerSpriteTemplateBase.TowerAnimationConfig getConfig() {
         return this.mobAnimConfig;
      }
   }
}
