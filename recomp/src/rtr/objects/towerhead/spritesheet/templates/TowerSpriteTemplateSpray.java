package rtr.objects.towerhead.spritesheet.templates;

public class TowerSpriteTemplateSpray extends TowerSpriteTemplateBase {
   public TowerSpriteTemplateSpray() {
      this.load();
   }

   @Override
   public void load() {
      super.load();
      int baseDuration = 20;

      TowerSpriteTemplateBase.TowerAnimationConfig[] var5;
      for (TowerSpriteTemplateBase.TowerAnimationConfig a : TowerSpriteTemplateBase.TowerAnimationConfig.values()) {
         this.animationDurations.put(a, baseDuration);
      }

      this.animationDurations.put(TowerSpriteTemplateBase.TowerAnimationConfig.FIRE, 2);
      this.animationDurations.put(TowerSpriteTemplateBase.TowerAnimationConfig.RELOAD, 2);
   }
}
