package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleFlashFast extends ParticleBase {
   @Override
   public void load() {
      this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECK;
      this.animationFlags.clear();
      this.animationFlags.add(Animation.AnimationMode.NO_LOOPING);
      this.animationSpeed = 10;
      this.top = true;
      this.essential = true;
      this.alpha = 0.8F;
      this.alphaVarience = 0.2F;
      this.fadeOutPoint = 5;
      this.fadeInPoint = 5;
      this.duration = 20;
   }
}
