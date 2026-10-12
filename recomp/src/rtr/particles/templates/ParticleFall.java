package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleFall extends ParticleBase {
   private boolean fast = false;

   public ParticleFall(int configNumber, boolean fast) {
      this.load(configNumber, fast);
   }

   @Override
   public void load() {
      this.load(this.configNumber, this.fast);
   }

   public void load(int configNumber, boolean fast) {
      this.configNumber = configNumber;
      this.fast = fast;
      this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECK;
      this.animationFlags.clear();
      this.animationFlags.add(Animation.AnimationMode.NO_LOOPING);
      this.animationSpeed = 10;
      this.top = true;
      this.essential = true;
      this.angle = 90;
      this.angleVarience = 20;
      if (fast) {
         this.velocity = 0.3F;
         this.velocityVarience = 0.05F;
      } else {
         this.velocity = 0.1F;
         this.velocityVarience = 0.05F;
      }

      this.alpha = 0.6F;
      this.alphaVarience = 0.2F;
      this.fadeOutPoint = 15;
      this.fadeOutPointVarience = 5;
      this.fadeInPoint = 15;
      this.fadeInPointVarience = 5;
      this.duration = 100;
      this.durationVarience = 20;
      this.behaviorFloatAway = true;
      this.behaviorWind = true;
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
            break;
         case 4:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.SPLATTER_3PX;
            break;
         case 5:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.SPLATTER_5PX;
            break;
         case 6:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.EXPANDING_RING_4PX_DISSIPATING;
            break;
         case 7:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.EXPANDING_SQUARE_RING_4PX_DISSIPATING;
      }
   }
}
