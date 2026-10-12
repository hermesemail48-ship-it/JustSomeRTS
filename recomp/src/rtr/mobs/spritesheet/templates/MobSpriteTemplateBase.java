package rtr.mobs.spritesheet.templates;

import java.util.EnumMap;
import r2d.animation.Animation;

public class MobSpriteTemplateBase {
   protected int layers;
   protected EnumMap<MobSpriteTemplateBase.MobAnimationConfig, Animation.AnimationMode> animationFlags = new EnumMap<>(
      MobSpriteTemplateBase.MobAnimationConfig.class
   );
   protected EnumMap<MobSpriteTemplateBase.MobAnimationConfig, Integer> animationDurations = new EnumMap<>(MobSpriteTemplateBase.MobAnimationConfig.class);
   protected EnumMap<MobSpriteTemplateBase.MobAnimationConfig, Integer> carriedResourceXOffset = new EnumMap<>(MobSpriteTemplateBase.MobAnimationConfig.class);
   protected EnumMap<MobSpriteTemplateBase.MobAnimationConfig, Integer> carriedResourceYOffset = new EnumMap<>(MobSpriteTemplateBase.MobAnimationConfig.class);

   public MobSpriteTemplateBase() {
      this.load();
   }

   public void load() {
      MobSpriteTemplateBase.MobAnimationConfig[] var4;
      for (MobSpriteTemplateBase.MobAnimationConfig a : MobSpriteTemplateBase.MobAnimationConfig.values()) {
         this.animationFlags.put(a, Animation.AnimationMode.LOOPING);
         this.animationDurations.put(a, 5);
         this.carriedResourceXOffset.put(a, 0);
         this.carriedResourceYOffset.put(a, -14);
      }

      this.carriedResourceXOffset.put(MobSpriteTemplateBase.MobAnimationConfig.INTERACT_TOOL, 0);
      this.carriedResourceYOffset.put(MobSpriteTemplateBase.MobAnimationConfig.INTERACT_TOOL, 4);
      this.carriedResourceXOffset.put(MobSpriteTemplateBase.MobAnimationConfig.INTERACT, 0);
      this.carriedResourceYOffset.put(MobSpriteTemplateBase.MobAnimationConfig.INTERACT, 4);
      this.carriedResourceXOffset.put(MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND_TOOL, 0);
      this.carriedResourceYOffset.put(MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND_TOOL, 4);
      this.carriedResourceXOffset.put(MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND, 0);
      this.carriedResourceYOffset.put(MobSpriteTemplateBase.MobAnimationConfig.INTERACT_GROUND, 4);
   }

   public EnumMap<MobSpriteTemplateBase.MobAnimationConfig, Animation.AnimationMode> getAnimationFlags() {
      return this.animationFlags;
   }

   public EnumMap<MobSpriteTemplateBase.MobAnimationConfig, Integer> getAnimationDurations() {
      return this.animationDurations;
   }

   public int getCarriedResourceXOffset(MobSpriteTemplateBase.MobAnimationConfig c) {
      return this.carriedResourceXOffset.get(c);
   }

   public int getCarriedResourceYOffset(MobSpriteTemplateBase.MobAnimationConfig c) {
      return this.carriedResourceYOffset.get(c);
   }

   public int getLayers() {
      return this.layers;
   }

   public static enum MobAnimationConfig {
      WALK,
      FACE,
      DEATH,
      DECAY,
      ATTACK,
      CARRY,
      HOLD,
      INTERACT_TOOL,
      INTERACT,
      HIT,
      SLEEP,
      FLEE,
      EAT,
      DRINK,
      DRINK_BOTTLE,
      BOW_IDLE,
      BOW_FIRE,
      PRAY,
      PRAY_FINISHING,
      PICKUP,
      LIFT_EQUIP,
      LIFT_HAUL,
      DROP,
      INTERACT_GROUND_TOOL,
      INTERACT_GROUND;
   }
}
