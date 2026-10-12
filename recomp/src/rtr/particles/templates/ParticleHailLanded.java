package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleHailLanded extends ParticleBase {
   @Override
   public void load() {
      this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECK;
      this.animationFlags.clear();
      this.animationFlags.add(Animation.AnimationMode.NO_LOOPING);
      this.animationSpeed = 10;
      this.top = true;
      this.angle = 270;
      this.velocity = 2.0F;
      this.velocityVarience = 0.8F;
      this.friction = 0.15F;
      this.frictionVarience = 0.05F;
      this.flowResistance = 3;
      this.flowResistanceVarience = 2;
      this.alpha = 0.95F;
      this.fadeOutPoint = 100;
      this.fadeOutPointVarience = 30;
      this.fadeInPoint = 10;
      this.fadeInPointVarience = 5;
      this.duration = 1500;
      this.durationVarience = 500;
      this.behaviorToss = true;
      this.behaviorLiquid = true;
      this.behaviorWind = true;
   }
}
