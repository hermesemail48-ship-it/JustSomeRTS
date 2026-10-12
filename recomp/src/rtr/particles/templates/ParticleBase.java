package rtr.particles.templates;

import java.util.ArrayList;
import r2d.animation.Animation;
import rtr.particles.ParticleAnimationLoader;

public class ParticleBase {
   protected int configNumber;
   protected ParticleAnimationLoader.ParticleAnimation animationType;
   protected ArrayList<Animation.AnimationMode> animationFlags = new ArrayList<>();
   protected int animationSpeed;
   protected int animationSpeedVarience;
   protected boolean top;
   protected boolean essential;
   protected float colorChangeRate;
   protected int angle;
   protected int angleVarience;
   protected float velocity;
   protected float velocityVarience;
   protected float friction;
   protected float frictionVarience;
   protected int flowResistance;
   protected int flowResistanceVarience;
   protected float alpha;
   protected float alphaVarience;
   protected int fadeOutPoint;
   protected int fadeOutPointVarience;
   protected int fadeInPoint;
   protected int fadeInPointVarience;
   protected int duration;
   protected int durationVarience;
   protected boolean behaviorFloatAway;
   protected boolean behaviorDestroyOnCollision;
   protected boolean behaviorToss;
   protected boolean behaviorLiquid;
   protected boolean behaviorOrbit;
   protected boolean behaviorShake;
   protected boolean behaviorWind;

   public ParticleBase() {
      this.load();
   }

   public ParticleBase(int configNumber) {
      this.load(configNumber);
   }

   public void load() {
   }

   public void load(int configNumber) {
   }

   public ParticleAnimationLoader.ParticleAnimation getAnimationType() {
      return this.animationType;
   }

   public ArrayList<Animation.AnimationMode> getAnimationFlags() {
      return this.animationFlags;
   }

   public int getAnimationSpeed() {
      return this.animationSpeed;
   }

   public int getAnimationSpeedVarience() {
      return this.animationSpeedVarience;
   }

   public boolean isTop() {
      return this.top;
   }

   public boolean isEssential() {
      return this.essential;
   }

   public float getColorChangeRate() {
      return this.colorChangeRate;
   }

   public int getAngle() {
      return this.angle;
   }

   public int getAngleVarience() {
      return this.angleVarience;
   }

   public float getVelocity() {
      return this.velocity;
   }

   public float getVelocityVarience() {
      return this.velocityVarience;
   }

   public float getFriction() {
      return this.friction;
   }

   public float getFrictionVarience() {
      return this.frictionVarience;
   }

   public int getFlowResistance() {
      return this.flowResistance;
   }

   public int getFlowResistanceVarience() {
      return this.flowResistanceVarience;
   }

   public float getAlpha() {
      return this.alpha;
   }

   public float getAlphaVarience() {
      return this.alphaVarience;
   }

   public int getFadeOutPoint() {
      return this.fadeOutPoint;
   }

   public int getFadeOutPointVarience() {
      return this.fadeOutPointVarience;
   }

   public int getFadeInPoint() {
      return this.fadeInPoint;
   }

   public int getFadeInPointVarience() {
      return this.fadeInPointVarience;
   }

   public int getDuration() {
      return this.duration;
   }

   public int getDurationVarience() {
      return this.durationVarience;
   }

   public boolean canBehaviorFloatAway() {
      return this.behaviorFloatAway;
   }

   public boolean canBehaviorDestroyOnCollision() {
      return this.behaviorDestroyOnCollision;
   }

   public boolean canBehaviorToss() {
      return this.behaviorToss;
   }

   public boolean canBehaviorLiquid() {
      return this.behaviorLiquid;
   }

   public boolean canBehaviorOrbit() {
      return this.behaviorOrbit;
   }

   public boolean canBehaviorShake() {
      return this.behaviorShake;
   }

   public boolean canBehaviorWind() {
      return this.behaviorWind;
   }
}
