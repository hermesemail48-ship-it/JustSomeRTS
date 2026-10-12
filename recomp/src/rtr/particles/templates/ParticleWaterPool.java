package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleWaterPool extends ParticleBase {
   @Override
   public void load() {
      this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECK;
      this.animationFlags.clear();
      this.animationFlags.add(Animation.AnimationMode.NO_LOOPING);
      this.animationSpeed = 10;
      this.flowResistance = 3;
      this.flowResistanceVarience = 2;
      this.alpha = 0.5F;
      this.alphaVarience = 0.1F;
      this.fadeOutPoint = 100;
      this.fadeOutPointVarience = 30;
      this.fadeInPoint = 10;
      this.fadeInPointVarience = 5;
      this.duration = 2500;
      this.durationVarience = 500;
      this.behaviorLiquid = true;
   }
}
