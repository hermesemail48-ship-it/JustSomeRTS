package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleWindDust extends ParticleBase {
   @Override
   public void load() {
      this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECK;
      this.animationFlags.clear();
      this.animationFlags.add(Animation.AnimationMode.NO_LOOPING);
      this.animationSpeed = 10;
      this.top = true;
      this.angle = 0;
      this.angleVarience = 5;
      this.velocity = 3.0F;
      this.velocityVarience = 1.0F;
      this.friction = 0.02F;
      this.frictionVarience = 0.01F;
      this.alpha = 0.6F;
      this.alphaVarience = 0.1F;
      this.fadeOutPoint = 50;
      this.fadeOutPointVarience = 10;
      this.fadeInPoint = 25;
      this.fadeInPointVarience = 5;
      this.duration = 300;
      this.durationVarience = 200;
      this.behaviorToss = true;
      this.behaviorDestroyOnCollision = true;
      this.behaviorWind = true;
   }
}
