package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleCrumb extends ParticleBase {
   @Override
   public void load() {
      this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECK;
      this.animationFlags.clear();
      this.animationFlags.add(Animation.AnimationMode.NO_LOOPING);
      this.animationSpeed = 10;
      this.angle = 270;
      this.angleVarience = 20;
      this.velocity = 1.0F;
      this.velocityVarience = 0.1F;
      this.friction = 0.05F;
      this.frictionVarience = 0.01F;
      this.alpha = 0.3F;
      this.alphaVarience = 0.1F;
      this.fadeOutPoint = 100;
      this.fadeOutPointVarience = 30;
      this.fadeInPoint = 10;
      this.fadeInPointVarience = 5;
      this.duration = 2500;
      this.durationVarience = 500;
      this.behaviorToss = true;
      this.behaviorWind = true;
   }
}
