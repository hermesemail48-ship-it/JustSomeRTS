package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleFloatingDust extends ParticleBase {
   public ParticleFloatingDust(int configNumber) {
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
      this.animationSpeed = 30;
      this.animationSpeedVarience = 10;
      this.top = true;
      this.essential = true;
      this.angle = 0;
      this.angleVarience = 180;
      this.velocity = 0.05F;
      this.velocityVarience = 0.025F;
      this.alpha = 0.5F;
      this.alphaVarience = 0.2F;
      this.fadeOutPoint = 50;
      this.fadeOutPointVarience = 20;
      this.fadeInPoint = 50;
      this.fadeInPointVarience = 20;
      this.duration = 500;
      this.durationVarience = 200;
      this.behaviorFloatAway = true;
      this.behaviorOrbit = true;
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
            this.animationFlags.add(Animation.AnimationMode.PING_PONG);
      }
   }
}
