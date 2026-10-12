package rtr.objects.towerhead.spritesheet.templates;

import r2d.animation.Animation;

public class TowerSpriteTemplateSlingTower extends TowerSpriteTemplateBase {
   public TowerSpriteTemplateSlingTower() {
      this.load();
   }

   @Override
   public void load() {
      super.load();
      int baseDuration = 2;

      TowerSpriteTemplateBase.TowerAnimationConfig[] var5;
      for (TowerSpriteTemplateBase.TowerAnimationConfig a : TowerSpriteTemplateBase.TowerAnimationConfig.values()) {
         this.animationDurations.put(a, baseDuration);
      }

      this.animationFlags.put(TowerSpriteTemplateBase.TowerAnimationConfig.FIRE, Animation.AnimationMode.NO_LOOPING);
      this.animationDurations.put(TowerSpriteTemplateBase.TowerAnimationConfig.FIRE, 1);
      this.animationFlags.put(TowerSpriteTemplateBase.TowerAnimationConfig.RELOAD, Animation.AnimationMode.NO_LOOPING);
      this.animationDurations.put(TowerSpriteTemplateBase.TowerAnimationConfig.RELOAD, 15);
   }
}
