package rtr.objects.gates.spritesheet.templates;

public class GateSpriteTemplateGate extends GateSpriteTemplateBase {
   public GateSpriteTemplateGate() {
      this.load();
   }

   @Override
   public void load() {
      super.load();
      int baseDuration = 4;

      GateSpriteTemplateBase.GateAnimationConfig[] var5;
      for (GateSpriteTemplateBase.GateAnimationConfig a : GateSpriteTemplateBase.GateAnimationConfig.values()) {
         this.animationDurations.put(a, baseDuration);
      }
   }
}
