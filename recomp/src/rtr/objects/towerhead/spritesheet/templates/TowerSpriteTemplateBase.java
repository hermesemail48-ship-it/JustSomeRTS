package rtr.objects.towerhead.spritesheet.templates;

import java.util.EnumMap;
import r2d.animation.Animation;

public class TowerSpriteTemplateBase {
   protected EnumMap<TowerSpriteTemplateBase.TowerAnimationConfig, Animation.AnimationMode> animationFlags = new EnumMap<>(
      TowerSpriteTemplateBase.TowerAnimationConfig.class
   );
   protected EnumMap<TowerSpriteTemplateBase.TowerAnimationConfig, Integer> animationDurations = new EnumMap<>(
      TowerSpriteTemplateBase.TowerAnimationConfig.class
   );

   public TowerSpriteTemplateBase() {
      this.load();
   }

   public void load() {
      TowerSpriteTemplateBase.TowerAnimationConfig[] var4;
      for (TowerSpriteTemplateBase.TowerAnimationConfig a : TowerSpriteTemplateBase.TowerAnimationConfig.values()) {
         this.animationFlags.put(a, Animation.AnimationMode.LOOPING);
      }
   }

   public EnumMap<TowerSpriteTemplateBase.TowerAnimationConfig, Animation.AnimationMode> getAnimationFlags() {
      return this.animationFlags;
   }

   public EnumMap<TowerSpriteTemplateBase.TowerAnimationConfig, Integer> getAnimationDurations() {
      return this.animationDurations;
   }

   public static enum TowerAnimationConfig {
      FACE_UNLOADED,
      FACE_LOADED,
      FIRE,
      RELOAD;
   }
}
