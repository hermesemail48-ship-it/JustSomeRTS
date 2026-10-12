package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleFire extends ParticleBase {
   public ParticleFire(int configNumber) {
      this.load(configNumber);
   }

   @Override
   public void load() {
      this.load(this.configNumber);
   }

   @Override
   public void load(int configNumber) {
      this.configNumber = configNumber;
      this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECK;
      this.animationFlags.clear();
      this.animationFlags.add(Animation.AnimationMode.NO_LOOPING);
      this.animationSpeed = 5;
      this.colorChangeRate = 0.02F;
      this.top = true;
      this.essential = true;
      this.angle = 270;
      this.angleVarience = 20;
      this.velocity = 0.1F;
      this.velocityVarience = 0.1F;
      this.alpha = 0.6F;
      this.alphaVarience = 0.2F;
      this.fadeOutPoint = 7;
      this.fadeOutPointVarience = 0;
      this.fadeInPoint = 5;
      this.fadeInPointVarience = 5;
      this.duration = 60;
      this.durationVarience = 10;
      this.behaviorFloatAway = true;
      this.behaviorShake = true;
      this.behaviorWind = true;
      switch (configNumber) {
         case 0:
         default:
            break;
         case 1:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.EXPANDING_CIRCLE_2PX;
            break;
         case 2:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.EXPANDING_CIRCLE_4PX;
      }
   }
}
