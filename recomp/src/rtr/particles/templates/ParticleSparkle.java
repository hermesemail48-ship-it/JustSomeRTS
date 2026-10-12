package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleSparkle extends ParticleBase {
   public ParticleSparkle(int configNumber) {
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
      this.animationFlags.add(Animation.AnimationMode.MANUAL);
      this.animationSpeed = 1;
      this.colorChangeRate = 0.0075F;
      this.angle = 0;
      this.angleVarience = 360;
      this.velocity = 0.01F;
      this.velocityVarience = 0.005F;
      this.alpha = 0.4F;
      this.alphaVarience = 0.1F;
      this.fadeOutPoint = 70;
      this.fadeOutPointVarience = 5;
      this.fadeInPoint = 70;
      this.fadeInPointVarience = 5;
      this.duration = 160;
      this.durationVarience = 20;
      this.behaviorFloatAway = true;
      switch (configNumber) {
         case 0:
         default:
            break;
         case 1:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECKS_2PX;
            break;
         case 2:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECKS_3PX;
            break;
         case 3:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECKS_4PX;
      }
   }
}
