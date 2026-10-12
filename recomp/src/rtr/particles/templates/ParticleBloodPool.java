package rtr.particles.templates;

import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleBloodPool extends ParticleBase {
   public ParticleBloodPool(int configNumber) {
      this.load(configNumber);
   }

   @Override
   public void load() {
      this.load(this.configNumber);
   }

   @Override
   public void load(int configNumber) {
      this.configNumber = configNumber;
      this.animationType = ParticleAnimationLoader.ParticleAnimation.SPECKS_3PX;
      this.animationFlags.clear();
      this.animationFlags.add(Animation.AnimationMode.START_RANDOM_FRAME);
      this.animationSpeed = -1;
      this.flowResistance = 20;
      this.flowResistanceVarience = 5;
      this.alpha = 0.9F;
      this.alphaVarience = 0.1F;
      this.fadeOutPoint = 100;
      this.fadeOutPointVarience = 30;
      this.fadeInPoint = 10;
      this.fadeInPointVarience = 5;
      this.duration = 1500;
      this.durationVarience = 500;
      this.behaviorLiquid = true;
      switch (configNumber) {
         case 0:
         default:
            break;
         case 1:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.SPLATTER_3PX;
            break;
         case 2:
            this.animationType = ParticleAnimationLoader.ParticleAnimation.SPLATTER_5PX;
      }
   }
}
