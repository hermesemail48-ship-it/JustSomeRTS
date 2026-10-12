package rtr.particles;

import java.util.Comparator;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import r2d.animation.Animation;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.map.CollisionModule;
import rtr.map.MapModule;
import rtr.particles.templates.ParticleBase;
import rtr.states.StateBase;
import rtr.utilities.FastMath;
import rtr.utilities.Utilities;
import rtr.weather.WeatherModule;

public class Particle implements Comparable<Particle>, Comparator<Particle> {
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected CollisionModule collision = (CollisionModule)StateBase.getModule(ModuleBase.ModuleType.COLLISION);
   protected WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   protected ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   private Animation particleAnimation;
   private float particleX;
   private float particleY;
   private int centerX;
   private int centerY;
   private boolean top;
   private float colorChangeRate;
   private float angle;
   private float velocity;
   private float friction;
   private int flowResistance;
   private Color startColor;
   private Color endColor;
   private float alphaMax;
   private int fadeOutPoint;
   private int fadeInPoint;
   private int duration;
   private float velocityX;
   private float velocityY;
   private float alpha;
   private float fadeOutPerTick;
   private float fadeInPerTick;
   private boolean behaviorFloatAway;
   private boolean behaviorDestroyOnCollision;
   private boolean behaviorToss;
   private boolean behaviorLiquid;
   private boolean behaviorOrbit;
   private boolean behaviorShake;
   private boolean behaviorWind;
   private boolean behaviorLiquidInactive;
   private boolean behaviorLiquidOnMap;

   public void configure(float x, float y, ParticleBase p, ParticleModule.ParticleSet startColor, ParticleModule.ParticleSet endColor, float a) {
      Image[] imageSet = this.particle.getParticleAnimationLoader().getParticleAnimationImages(p.getAnimationType());
      this.particleAnimation = new Animation(imageSet);

      for (Animation.AnimationMode flag : p.getAnimationFlags()) {
         this.particleAnimation.setMode(flag);
      }

      if (p.getAnimationSpeedVarience() != 0) {
         this.particleAnimation.setSpeed(p.getAnimationSpeed() + Utilities.randomInt(-p.getAnimationSpeedVarience(), p.getAnimationSpeedVarience()));
      } else {
         this.particleAnimation.setSpeed(p.getAnimationSpeed());
      }

      this.centerX = 0;
      this.centerY = 0;
      if (imageSet[0].getWidth() > 2) {
         this.centerX = imageSet[0].getWidth() / 2;
      }

      if (imageSet[0].getHeight() > 2) {
         this.centerY = imageSet[0].getHeight() / 2;
      }

      this.particleX = x;
      this.particleY = y;
      this.top = p.isTop();
      this.colorChangeRate = p.getColorChangeRate();
      if (a != -1.0F) {
         this.angle = a;
      } else {
         this.angle = p.getAngle();
      }

      if (p.getAngleVarience() != 0) {
         this.angle = this.angle + Utilities.randomInt(p.getAngleVarience() * -1, p.getAngleVarience());
      }

      this.angle = (float)Math.toRadians(this.angle);
      this.velocity = p.getVelocity();
      if (p.getVelocityVarience() != 0.0F) {
         this.velocity = this.velocity
            + ((p.getVelocityVarience() - p.getVelocityVarience() * -1.0F) * Utilities.randomFloat() + p.getVelocityVarience() * -1.0F);
      }

      this.friction = p.getFriction();
      if (p.getFrictionVarience() != 0.0F) {
         this.friction = this.friction
            + ((p.getFrictionVarience() - p.getFrictionVarience() * -1.0F) * Utilities.randomFloat() + p.getFrictionVarience() * -1.0F);
      }

      this.flowResistance = p.getFlowResistance();
      if (p.getFlowResistanceVarience() != 0) {
         this.flowResistance = (int)(
            this.flowResistance
               + ((p.getFlowResistanceVarience() - p.getFlowResistanceVarience() * -1) * Utilities.randomFloat() + p.getFlowResistanceVarience() * -1)
         );
      }

      this.startColor = this.particle.getParticleColor(startColor);
      this.endColor = null;
      if (endColor != null) {
         this.endColor = this.particle.getParticleColor(endColor);
      }

      this.alphaMax = p.getAlpha();
      if (p.getVelocityVarience() != 0.0F) {
         this.alphaMax = this.alphaMax + ((p.getAlphaVarience() - p.getAlphaVarience() * -1.0F) * Utilities.randomFloat() + p.getAlphaVarience() * -1.0F);
      }

      this.fadeOutPoint = p.getFadeOutPoint();
      if (p.getFadeOutPointVarience() != 0) {
         this.fadeOutPoint = this.fadeOutPoint + Utilities.randomInt(p.getFadeOutPointVarience() * -1, p.getFadeOutPointVarience());
      }

      this.fadeInPoint = p.getFadeInPoint();
      if (p.getFadeInPointVarience() != 0) {
         this.fadeInPoint = this.fadeInPoint + Utilities.randomInt(p.getFadeInPointVarience() * -1, p.getFadeInPointVarience());
      }

      this.duration = p.getDuration();
      if (p.getDurationVarience() != 0) {
         this.duration = this.duration + Utilities.randomInt(p.getDurationVarience() * -1, p.getDurationVarience());
      }

      this.behaviorFloatAway = p.canBehaviorFloatAway();
      this.behaviorDestroyOnCollision = p.canBehaviorDestroyOnCollision();
      this.behaviorToss = p.canBehaviorToss();
      this.behaviorLiquid = p.canBehaviorLiquid();
      this.behaviorOrbit = p.canBehaviorOrbit();
      this.behaviorShake = p.canBehaviorShake();
      this.behaviorWind = p.canBehaviorWind();
      this.alpha = 0.0F;
      this.startColor.a = this.alpha;
      this.fadeOutPerTick = 1.0F / this.fadeOutPoint;
      this.fadeInPerTick = 1.0F / this.fadeInPoint;
      this.velocityX = FastMath.cos(this.angle) * this.velocity;
      this.velocityY = FastMath.sin(this.angle) * this.velocity;
      this.behaviorLiquidInactive = false;
   }

   public boolean canRender() {
      return this.map.isInView(this.particleX, this.particleY, 2);
   }

   public void render() {
      if (SettingsParser.getRetroParticles()) {
         this.particleAnimation
            .renderEmbedded(
               (int)(this.map.getMapX() + (this.particleX - this.centerX)), (int)(this.map.getMapY() + (this.particleY - this.centerY)), this.startColor
            );
      } else {
         this.particleAnimation
            .renderEmbedded(this.map.getMapX() + (this.particleX - this.centerX), this.map.getMapY() + (this.particleY - this.centerY), this.startColor);
      }
   }

   public void renderGUI() {
      if (SettingsParser.getRetroParticles()) {
         this.particleAnimation.renderEmbedded((int)(this.particleX - this.centerX), (int)(this.particleY - this.centerY), this.startColor);
      } else {
         this.particleAnimation.renderEmbedded(this.particleX - this.centerX, this.particleY - this.centerY, this.startColor);
      }
   }

   public void renderWorldMap(float mapX, float mapY) {
      if (SettingsParser.getRetroParticles()) {
         this.particleAnimation.renderEmbedded((int)(mapX + (this.particleX - this.centerX)), (int)(mapY + (this.particleY - this.centerY)), this.startColor);
      } else {
         this.particleAnimation.renderEmbedded(mapX + (this.particleX - this.centerX), mapY + (this.particleY - this.centerY), this.startColor);
      }
   }

   public void update() {
      this.particleAnimation.update();
      if (this.duration < this.fadeOutPoint && this.alpha > 0.0F) {
         this.alpha = this.alpha - this.fadeOutPerTick;
         if (this.alpha < 0.0F) {
            this.alpha = 0.0F;
         }

         this.startColor.a = this.alpha;
      } else if (this.duration > this.fadeOutPoint && this.alpha < this.alphaMax) {
         this.alpha = this.alpha + this.fadeInPerTick;
         if (this.alpha > this.alphaMax) {
            this.alpha = this.alphaMax;
         }

         this.startColor.a = this.alpha;
      }

      if (this.endColor != null) {
         if (this.endColor.r != this.startColor.r) {
            this.startColor.r = this.startColor.r + (this.endColor.r > this.startColor.r ? this.colorChangeRate : -this.colorChangeRate);
         }

         if (this.endColor.g != this.startColor.g) {
            this.startColor.g = this.startColor.g + (this.endColor.g > this.startColor.g ? this.colorChangeRate : -this.colorChangeRate);
         }

         if (this.endColor.b != this.startColor.b) {
            this.startColor.b = this.startColor.b + (this.endColor.b > this.startColor.b ? this.colorChangeRate : -this.colorChangeRate);
         }
      }

      if (this.behaviorFloatAway) {
         this.behaviorFloatAway();
      }

      if (this.behaviorDestroyOnCollision) {
         this.behaviorDestroyOnCollision();
      }

      if (this.behaviorToss) {
         this.behaviorToss();
      }

      if (this.behaviorLiquid) {
         this.behaviorLiquid();
      }

      if (this.behaviorOrbit) {
         this.behaviorOrbit();
      }

      if (this.behaviorShake) {
         this.behaviorShake();
      }

      if (this.behaviorWind) {
         this.behaviorWind();
      }

      this.duration--;
   }

   private void behaviorFloatAway() {
      this.particleX = this.particleX + this.velocityX;
      this.particleY = this.particleY + this.velocityY;
   }

   private void behaviorDestroyOnCollision() {
      if (this.collision.checkParticleCollision((int)this.particleX, (int)this.particleY, CollisionModule.CollisionHeight.MEDIUM)
            != this.collision.getCollisionNone()
         && this.duration > this.fadeOutPoint) {
         this.duration = this.fadeOutPoint;
      }
   }

   private void behaviorToss() {
      this.particleX = this.particleX + this.velocityX;
      this.particleY = this.particleY + this.velocityY;
      if (this.velocity > 0.0F) {
         if (this.collision.checkParticleCollision((int)this.particleX, (int)this.particleY, CollisionModule.CollisionHeight.MEDIUM)
            != this.collision.getCollisionNone()) {
            this.velocity = this.velocity - this.friction * 4.0F;
         } else {
            this.velocity = this.velocity - this.friction;
         }
      }

      if (this.velocity <= 0.0F) {
         this.particleX = (int)this.particleX;
         this.particleY = (int)this.particleY;
         this.velocity = 0.0F;
         this.behaviorToss = false;
         this.top = false;
      }

      this.velocityX = (float)(Math.cos(this.angle) * this.velocity);
      this.velocityY = (float)(Math.sin(this.angle) * this.velocity);
   }

   private void behaviorLiquid() {
      if (this.velocity <= 0.0F) {
         if (!this.behaviorLiquidInactive) {
            this.adjustParticleBounds();
            if (Utilities.randomInt(this.flowResistance) == 0) {
               byte collisionCheck = this.collision.checkParticleCollision((int)this.particleX, (int)this.particleY, CollisionModule.CollisionHeight.ANY);
               if (collisionCheck == this.collision.getCollisionNorthLow()
                  || collisionCheck == this.collision.getCollisionNorthMedium()
                  || collisionCheck == this.collision.getCollisionNorthHigh()) {
                  this.particleY--;
               } else if (collisionCheck == this.collision.getCollisionEastLow()
                  || collisionCheck == this.collision.getCollisionEastMedium()
                  || collisionCheck == this.collision.getCollisionEastHigh()) {
                  this.particleX++;
               } else if (collisionCheck == this.collision.getCollisionSouthLow()
                  || collisionCheck == this.collision.getCollisionSouthMedium()
                  || collisionCheck == this.collision.getCollisionSouthHigh()) {
                  this.particleY++;
               } else if (collisionCheck != this.collision.getCollisionWestLow()
                  && collisionCheck != this.collision.getCollisionWestMedium()
                  && collisionCheck != this.collision.getCollisionWestHigh()) {
                  byte collisionType = this.collision.checkParticleCollision((int)this.particleX, (int)this.particleY, CollisionModule.CollisionHeight.ANY);

                  while (
                     this.particle.checkLiquidMap((int)this.particleX, (int)this.particleY)
                        && (
                           collisionType == this.collision.getCollisionNone()
                              || collisionType == this.collision.getCollisionAllLow()
                              || collisionType == this.collision.getCollisionAllMedium()
                              || collisionType == this.collision.getCollisionAllHigh()
                        )
                  ) {
                     int random = Utilities.randomInt(4);
                     switch (random) {
                        case 0:
                           this.particleX++;
                           break;
                        case 1:
                           this.particleX--;
                           break;
                        case 2:
                           this.particleY++;
                           break;
                        case 3:
                           this.particleY--;
                     }

                     collisionType = this.collision.checkParticleCollision((int)this.particleX, (int)this.particleY, CollisionModule.CollisionHeight.ANY);
                     this.adjustParticleBounds();
                  }

                  if (!this.particle.checkLiquidMap((int)this.particleX, (int)this.particleY)) {
                     this.particle.addLiquidMap((int)this.particleX, (int)this.particleY);
                     this.behaviorLiquidOnMap = true;
                     this.behaviorLiquidInactive = true;
                  }
               } else {
                  this.particleX--;
               }

               if (Utilities.randomInt(100 - this.flowResistance) == 0) {
                  this.behaviorLiquidInactive = true;
               }
            }
         }

         if (this.behaviorLiquidOnMap && (this.duration == 500 || this.duration < this.fadeOutPoint + 10)) {
            this.startColor = this.startColor.darker(0.2F);
            this.particle.removeLiquidMap((int)this.particleX, (int)this.particleY);
            this.behaviorLiquidOnMap = false;
         }
      }
   }

   private void behaviorOrbit() {
      this.particleX = this.particleX + this.velocityX;
      this.particleY = this.particleY + this.velocityY;
      this.angle = this.angle + this.velocity / 10.0F;
      this.velocityX = FastMath.cos(this.angle) * this.velocity;
      this.velocityY = FastMath.sin(this.angle) * this.velocity;
   }

   private void behaviorShake() {
      if (Utilities.randomInt(2) == 0) {
         if (Utilities.randomInt(2) == 0) {
            this.particleX = this.particleX + this.velocityY;
         } else {
            this.particleX = this.particleX - this.velocityY;
         }
      } else if (Utilities.randomInt(2) == 0) {
         this.particleY = this.particleY + this.velocityX;
      } else {
         this.particleY = this.particleY - this.velocityX;
      }
   }

   private void behaviorWind() {
      this.particleX = this.particleX + this.weather.getWindVelocity();
      if (this.velocity <= 0.0F) {
         this.behaviorWind = false;
      }
   }

   public boolean isTop() {
      return this.top;
   }

   public int getDuration() {
      return this.duration;
   }

   private void adjustParticleBounds() {
      while (Utilities.isOutOfBounds(4095, 4095, (int)this.particleX, (int)this.particleY)) {
         if ((int)this.particleX >= 4095) {
            this.particleX--;
         } else if ((int)this.particleX < 0) {
            this.particleX++;
         } else if ((int)this.particleY >= 4095) {
            this.particleY--;
         } else if ((int)this.particleY < 0) {
            this.particleY++;
         }
      }
   }

   public float getParticleX() {
      return this.particleX;
   }

   public float getParticleY() {
      return this.particleY;
   }

   public int compareTo(Particle p) {
      return (int)(this.particleY - p.getParticleY());
   }

   public int compare(Particle p1, Particle p2) {
      return p1.getParticleY() == p2.getParticleY() ? (int)p1.getParticleY() : (int)(p1.getParticleY() - p2.getParticleY());
   }
}
