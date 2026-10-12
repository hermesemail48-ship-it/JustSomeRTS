package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleMagicFloat extends ParticleBase {
   public ParticleMagicFloat(int configNumber) {
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
      this.animationSpeed = 50;
      this.animationSpeedVarience = 10;
      this.top = true;
      this.essential = true;
      this.angle = 0;
      this.angleVarience = 180;
      this.velocity = 0.1F;
      this.velocityVarience = 0.05F;
      this.alpha = 0.4F;
      this.alphaVarience = 0.2F;
      this.fadeOutPoint = 50;
      this.fadeOutPointVarience = 20;
      this.fadeInPoint = 50;
      this.fadeInPointVarience = 20;
      this.duration = 500;
      this.durationVarience = 200;
      this.behaviorOrbit = true;
      switch (configNumber) {
         case 0:
         default:
            break;
         case 1:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECKS_2PX;
            break;
         case 2:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.EXPANDING_RING_4PX_DISSIPATING;
            this.animationSpeed = 100;
            break;
         case 3:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.EXPANDING_RING_8PX_DISSIPATING;
            this.animationSpeed = 70;
            break;
         case 4:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.EXPANDING_RING_12PX_DISSIPATING;
            this.animationSpeed = 50;
            break;
         case 5:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.EXPANDING_RING_16PX_DISSIPATING;
            this.animationSpeed = 40;
            break;
         case 6:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.EXPANDING_SQUARE_RING_4PX_DISSIPATING;
            this.animationSpeed = 100;
            break;
         case 7:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.EXPANDING_SQUARE_RING_8PX_DISSIPATING;
            this.animationSpeed = 70;
            break;
         case 8:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.EXPANDING_SQUARE_RING_12PX_DISSIPATING;
            this.animationSpeed = 50;
            break;
         case 9:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.EXPANDING_SQUARE_RING_16PX_DISSIPATING;
            this.animationSpeed = 40;
      }
   }
}
