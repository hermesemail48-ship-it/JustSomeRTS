package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleHail extends ParticleBase {
   @Override
   public void load() {
      this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECK;
      this.animationFlags.clear();
      this.animationFlags.add(Animation.AnimationMode.NO_LOOPING);
      this.animationSpeed = 10;
      this.top = true;
      this.essential = true;
      this.angle = 90;
      this.angleVarience = 1;
      this.velocity = 5.0F;
      this.velocityVarience = 0.5F;
      this.friction = 0.15F;
      this.frictionVarience = 0.05F;
      this.flowResistance = 20;
      this.flowResistanceVarience = 5;
      this.alpha = 0.95F;
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
