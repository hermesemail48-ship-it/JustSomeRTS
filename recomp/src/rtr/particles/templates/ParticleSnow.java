package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleSnow extends ParticleBase {
   @Override
   public void load() {
      this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECK;
      this.animationFlags.clear();
      this.animationFlags.add(Animation.AnimationMode.NO_LOOPING);
      this.animationSpeed = 10;
      this.top = true;
      this.essential = true;
      this.angle = 80;
      this.angleVarience = 25;
      this.velocity = 0.4F;
      this.velocityVarience = 0.2F;
      this.friction = 0.003F;
      this.frictionVarience = 0.001F;
      this.alpha = 1.0F;
      this.alphaVarience = 0.0F;
      this.fadeOutPoint = 100;
      this.fadeOutPointVarience = 5;
      this.fadeInPoint = 15;
      this.fadeInPointVarience = 5;
      this.duration = 400;
      this.durationVarience = 100;
      this.behaviorToss = true;
      this.behaviorWind = true;
   }
}
