package rtr.particles;

import rtr.ModuleBase;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class ParticleEmitter {
   private ParticleModule particles = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   private int emitterX;
   private int emitterY;
   private int rate;
   private ParticleModule.ParticleGroup group;
   private ParticleModule.ParticleType type;
   private ParticleModule.ParticleSet colorOne;
   private ParticleModule.ParticleSet colorTwo;
   private ParticleModule.ParticleSet colorThree;
   private boolean wholeTile;

   public ParticleEmitter(int x, int y, int r, ParticleModule.ParticleGroup g, ParticleModule.ParticleSet c1, boolean wT) {
      this(x, y, r, g, c1, c1, c1, wT);
   }

   public ParticleEmitter(int x, int y, int r, ParticleModule.ParticleGroup g, ParticleModule.ParticleSet c1, ParticleModule.ParticleSet c2, boolean wT) {
      this(x, y, r, g, c1, c2, c2, wT);
   }

   public ParticleEmitter(
      int x,
      int y,
      int r,
      ParticleModule.ParticleGroup g,
      ParticleModule.ParticleSet c1,
      ParticleModule.ParticleSet c2,
      ParticleModule.ParticleSet c3,
      boolean wT
   ) {
      this.emitterX = x;
      this.emitterY = y;
      this.rate = r;
      this.group = g;
      this.colorOne = c1;
      this.colorTwo = c2;
      this.colorThree = c3;
      this.wholeTile = wT;
   }

   public ParticleEmitter(int x, int y, int r, ParticleModule.ParticleType t, ParticleModule.ParticleSet c1, boolean wT) {
      this.emitterX = x;
      this.emitterY = y;
      this.rate = r;
      this.type = t;
      this.colorOne = c1;
      this.wholeTile = wT;
   }

   public void emit() {
      int modifiedRate = this.rate;
      if (ScaleControl.getWorldScale() == 1.0F) {
         modifiedRate *= 4;
      }

      if (this.wholeTile) {
         if (modifiedRate / 256 == 0 || Utilities.randomInt(modifiedRate / 256) == 0) {
            if (this.group != null) {
               this.particles
                  .newParticleGroup(
                     this.emitterX + Utilities.randomInt(16),
                     this.emitterY + Utilities.randomInt(16),
                     this.group,
                     this.colorOne,
                     this.colorTwo,
                     this.colorThree
                  );
            } else {
               this.particles.newParticle(this.emitterX + Utilities.randomInt(16), this.emitterY + Utilities.randomInt(16), this.type, this.colorOne);
            }
         }
      } else if (Utilities.randomInt(modifiedRate) == 0) {
         if (this.group != null) {
            this.particles.newParticleGroup(this.emitterX, this.emitterY, this.group, this.colorOne, this.colorTwo, this.colorThree);
         } else {
            this.particles.newParticle(this.emitterX, this.emitterY, this.type, this.colorOne);
         }
      }
   }

   public int getX() {
      return this.emitterX;
   }

   public int getY() {
      return this.emitterY;
   }
}
