package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleSmoke extends ParticleBase {
   public ParticleSmoke(int configNumber) {
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
      this.animationSpeed = 10;
      this.colorChangeRate = 0.0025F;
      this.top = true;
      this.essential = true;
      this.angle = 270;
      this.angleVarience = 10;
      this.velocity = 0.05F;
      this.velocityVarience = 0.02F;
      this.alpha = 0.4F;
      this.alphaVarience = 0.3F;
      this.fadeOutPoint = 50;
      this.fadeOutPointVarience = 10;
      this.fadeInPoint = 50;
      this.fadeInPointVarience = 5;
      this.duration = 300;
      this.durationVarience = 10;
      this.behaviorFloatAway = true;
      this.behaviorWind = true;
      switch (configNumber) {
         case 0:
         default:
            break;
         case 1:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.EXPANDING_CIRCLE_2PX;
            break;
         case 2:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.PUFF_4PX;
            break;
         case 3:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.PUFF_6PX;
            break;
         case 4:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.PUFF_8PX;
      }
   }
}
