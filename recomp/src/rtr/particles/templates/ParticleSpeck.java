package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleSpeck extends ParticleBase {
   @Override
   public void load() {
      this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECK;
      this.animationFlags.clear();
      this.animationFlags.add(Animation.AnimationMode.NO_LOOPING);
      this.animationSpeed = 10;
      this.alpha = 0.2F;
      this.alphaVarience = 0.05F;
      this.fadeOutPoint = 500;
      this.fadeOutPointVarience = 5;
      this.fadeInPoint = 50;
      this.fadeInPointVarience = 5;
      this.duration = 6000;
      this.durationVarience = 3000;
   }
}
