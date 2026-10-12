package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleFlash extends ParticleBase {
   public ParticleFlash(int configNumber) {
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
      this.animationSpeed = -1;
      this.colorChangeRate = 0.04F;
      this.top = true;
      this.essential = true;
      this.alpha = 0.8F;
      this.alphaVarience = 0.1F;
      this.fadeOutPoint = 20;
      this.fadeOutPointVarience = 5;
      this.fadeInPoint = 10;
      this.fadeInPointVarience = 5;
      this.duration = 40;
      this.durationVarience = 5;
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
            this.animationType = ParticleAnimationLoader.ParticleAnimation.LINE_2PX;
            break;
         case 5:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.LINE_3PX;
            break;
         case 6:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.LINE_4PX;
            break;
         case 7:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.LINE_5PX;
            break;
         case 8:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.LINE_6PX;
      }
   }
}
