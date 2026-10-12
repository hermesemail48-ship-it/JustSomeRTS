package rtr.objects.gates.spritesheet.templates;

import java.util.EnumMap;

public class GateSpriteTemplateBase {
   protected EnumMap<GateSpriteTemplateBase.GateAnimationConfig, Integer> animationDurations = new EnumMap<>(GateSpriteTemplateBase.GateAnimationConfig.class);

   public GateSpriteTemplateBase() {
      this.load();
   }

   public void load() {
   }

   public EnumMap<GateSpriteTemplateBase.GateAnimationConfig, Integer> getAnimationDurations() {
      return this.animationDurations;
   }

   public static enum GateAnimationConfig {
      OPEN,
      CLOSE;
   }
}
