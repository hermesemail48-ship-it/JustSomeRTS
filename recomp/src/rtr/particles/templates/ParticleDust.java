package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleDust extends ParticleBase {
   public ParticleDust(int configNumber) {
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
      this.animationFlags.add(Animation.AnimationMode.START_RANDOM_FRAME);
      this.animationFlags.add(Animation.AnimationMode.MANUAL);
      this.animationSpeed = -1;
      this.angle = 270;
      this.angleVarience = 20;
      this.velocity = 2.0F;
      this.velocityVarience = 0.8F;
      this.friction = 0.1F;
      this.frictionVarience = 0.03F;
      this.alpha = 0.3F;
      this.alphaVarience = 0.1F;
      this.fadeOutPoint = 100;
      this.fadeOutPointVarience = 30;
      this.fadeInPoint = 10;
      this.fadeInPointVarience = 5;
      this.duration = 2500;
      this.durationVarience = 500;
      this.behaviorToss = true;
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
      }
   }
}
