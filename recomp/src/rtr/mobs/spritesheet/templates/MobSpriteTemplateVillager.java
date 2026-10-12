package rtr.mobs.spritesheet.templates;

import r2d.animation.Animation;

public class MobSpriteTemplateVillager extends MobSpriteTemplateBase {
   public MobSpriteTemplateVillager() {
      this.load();
   }

   @Override
   public void load() {
      super.load();
      this.layers = 5;
      int baseDuration = 5;
      int baseResourceYOffset = -14;

      MobSpriteTemplateBase.MobAnimationConfig[] var6;
      for (MobSpriteTemplateBase.MobAnimationConfig a : MobSpriteTemplateBase.MobAnimationConfig.values()) {
         this.animationDurations.put(a, baseDuration);
         if (a != MobSpriteTemplateBase.MobAnimationConfig.INTERACT
            && a != MobSpriteTemplateBase.MobAnimationConfig.INTERACT_TOOL
            && a != MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND
            && a != MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND_TOOL) {
            this.carriedResourceYOffset.put(a, baseResourceYOffset);
         }
      }

      this.animationFlags.put(MobSpriteTemplateBase.MobAnimationConfig.DEATH, Animation.AnimationMode.NO_LOOPING);
      this.animationDurations.put(MobSpriteTemplateBase.MobAnimationConfig.DEATH, 3);
      this.animationFlags.put(MobSpriteTemplateBase.MobAnimationConfig.DECAY, Animation.AnimationMode.MANUAL_NO_LOOPING);
      this.animationFlags.put(MobSpriteTemplateBase.MobAnimationConfig.FACE, Animation.AnimationMode.MANUAL_RANDOM);
      this.animationDurations.put(MobSpriteTemplateBase.MobAnimationConfig.FACE, 1000);
      this.animationFlags.put(MobSpriteTemplateBase.MobAnimationConfig.HIT, Animation.AnimationMode.NO_LOOPING);
      this.animationDurations.put(MobSpriteTemplateBase.MobAnimationConfig.HIT, 3);
      this.animationFlags.put(MobSpriteTemplateBase.MobAnimationConfig.HOLD, Animation.AnimationMode.MANUAL_RANDOM);
      this.animationDurations.put(MobSpriteTemplateBase.MobAnimationConfig.HOLD, 1000);
      this.animationFlags.put(MobSpriteTemplateBase.MobAnimationConfig.SLEEP, Animation.AnimationMode.NO_LOOPING);
      this.animationFlags.put(MobSpriteTemplateBase.MobAnimationConfig.BOW_IDLE, Animation.AnimationMode.MANUAL_RANDOM);
      this.animationDurations.put(MobSpriteTemplateBase.MobAnimationConfig.BOW_IDLE, 1000);
      this.animationFlags.put(MobSpriteTemplateBase.MobAnimationConfig.PICKUP, Animation.AnimationMode.NO_LOOPING);
      this.animationDurations.put(MobSpriteTemplateBase.MobAnimationConfig.PICKUP, 1);
      this.carriedResourceYOffset.put(MobSpriteTemplateBase.MobAnimationConfig.PICKUP, 0);
      this.animationFlags.put(MobSpriteTemplateBase.MobAnimationConfig.LIFT_EQUIP, Animation.AnimationMode.NO_LOOPING);
      this.animationDurations.put(MobSpriteTemplateBase.MobAnimationConfig.LIFT_EQUIP, 1);
      this.carriedResourceYOffset.put(MobSpriteTemplateBase.MobAnimationConfig.LIFT_EQUIP, baseResourceYOffset + 2);
      this.animationFlags.put(MobSpriteTemplateBase.MobAnimationConfig.LIFT_HAUL, Animation.AnimationMode.NO_LOOPING);
      this.animationDurations.put(MobSpriteTemplateBase.MobAnimationConfig.LIFT_HAUL, 1);
      this.carriedResourceYOffset.put(MobSpriteTemplateBase.MobAnimationConfig.LIFT_HAUL, baseResourceYOffset + 2);
      this.animationFlags.put(MobSpriteTemplateBase.MobAnimationConfig.DROP, Animation.AnimationMode.NO_LOOPING);
      this.animationDurations.put(MobSpriteTemplateBase.MobAnimationConfig.DROP, 1);
      this.carriedResourceYOffset.put(MobSpriteTemplateBase.MobAnimationConfig.DROP, 0);
   }
}
