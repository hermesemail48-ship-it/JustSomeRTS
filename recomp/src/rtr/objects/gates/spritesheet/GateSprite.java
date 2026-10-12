package rtr.objects.gates.spritesheet;

import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.animation.Animation;
import r2d.image.SpriteSheet;
import rtr.objects.gates.spritesheet.templates.GateSpriteTemplateBase;

public class GateSprite {
   private Animation animation;
   private HashMap<Integer, Animation> animationMap = new HashMap<>();
   private GateSpriteTemplateBase spriteTemplate;
   private SpriteSheet spriteArray;
   private int spriteSize;

   public GateSprite(SpriteSheet sheet, GateSpriteTemplateBase t) throws SlickException {
      this.spriteArray = sheet;
      this.spriteTemplate = t;
      this.spriteSize = this.spriteArray.getSprite(0, 0).getWidth();

      GateSprite.GateAnimationFrames[] var6;
      for (GateSprite.GateAnimationFrames tA : GateSprite.GateAnimationFrames.values()) {
         int row = tA.getID();
         GateSpriteTemplateBase.GateAnimationConfig c = tA.getConfig();
         Image[] animationImages = new Image[]{
            this.spriteArray.getSprite(0, row),
            this.spriteArray.getSprite(1, row),
            this.spriteArray.getSprite(2, row),
            this.spriteArray.getSprite(3, row),
            this.spriteArray.getSprite(4, row),
            this.spriteArray.getSprite(5, row),
            this.spriteArray.getSprite(6, row),
            this.spriteArray.getSprite(7, row)
         };
         this.animationMap.put(row, new Animation(animationImages));
         this.animationMap.get(row).setSpeed(t.getAnimationDurations().get(c));
         this.animationMap.get(row).setMode(Animation.AnimationMode.NO_LOOPING);
      }

      this.setPosition(GateSprite.GateAnimationFrames.CLOSE);
   }

   public void render(float spriteX, float spriteY, boolean drawEmbedded) throws SlickException {
      if (drawEmbedded) {
         this.animation.renderEmbedded(spriteX - this.spriteSize / 2 + 8.0F, spriteY - this.spriteSize / 2 + 8.0F);
      } else {
         this.animation.render(spriteX - this.spriteSize / 2 + 8.0F, spriteY - this.spriteSize / 2 + 8.0F);
      }
   }

   public void update() throws SlickException {
      this.animation.update();
   }

   public void playAnimation(GateSprite.GateAnimationFrames a) {
      this.animation = this.animationMap.get(a.getID());
      this.animationMap.get(a.getID()).restart();
   }

   public void setPosition(GateSprite.GateAnimationFrames position) {
      if (this.animation != this.animationMap.get(position.getID())) {
         if (this.animation != null) {
            this.animation.restart();
         }

         this.animation = this.animationMap.get(position.getID());
         this.animation.restart();
      }
   }

   public int getSpriteWidth() {
      return this.animation.getCurrentFrame().getWidth();
   }

   public int getSpriteHeight() {
      return this.animation.getCurrentFrame().getHeight();
   }

   public GateSpriteTemplateBase getSpriteTemplate() {
      return this.spriteTemplate;
   }

   public boolean isStopped() {
      return this.animation.isStopped();
   }

   public void restart() {
      this.animation.restart();
   }

   public static enum GateAnimationFrames {
      OPEN(0, GateSpriteTemplateBase.GateAnimationConfig.OPEN),
      CLOSE(1, GateSpriteTemplateBase.GateAnimationConfig.CLOSE);

      private final int mobAnimID;
      private final GateSpriteTemplateBase.GateAnimationConfig mobAnimConfig;

      private GateAnimationFrames(int mobAnimID, GateSpriteTemplateBase.GateAnimationConfig mobAnimConfig) {
         this.mobAnimID = mobAnimID;
         this.mobAnimConfig = mobAnimConfig;
      }

      public int getID() {
         return this.mobAnimID;
      }

      public GateSpriteTemplateBase.GateAnimationConfig getConfig() {
         return this.mobAnimConfig;
      }
   }
}
