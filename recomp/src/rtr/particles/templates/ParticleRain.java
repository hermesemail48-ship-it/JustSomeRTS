package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleRain extends ParticleBase {
   @Override
   public void load() {
      this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECK;
      this.animationFlags.clear();
      this.animationFlags.add(Animation.AnimationMode.NO_LOOPING);
      this.animationSpeed = 10;
      this.top = true;
      this.essential = true;
      this.angle = 90;
      this.angleVarience = 2;
      this.velocity = 3.0F;
      this.velocityVarience = 0.5F;
      this.alpha = 0.8F;
      this.alphaVarience = 0.2F;
      this.fadeOutPoint = 15;
      this.fadeOutPointVarience = 5;
      this.fadeInPoint = 15;
      this.fadeInPointVarience = 5;
      this.duration = 80;
      this.durationVarience = 20;
      this.behaviorFloatAway = true;
      this.behaviorWind = true;
   }
}
