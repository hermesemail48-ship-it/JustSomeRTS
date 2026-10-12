package rtr.mobs.golems;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class GoddoGolem extends MobBase {
   public GoddoGolem() throws SlickException {
   }

   @Override
   public void configNew(int id, float x, float y, boolean findReachMap) throws SlickException {
      this.configNew(id, x, y, null, findReachMap);
   }

   @Override
   public void configNew(int id, float x, float y, String message, boolean findReachMap) throws SlickException {
      super.configNew(id, x, y);
      this.gender = MobBase.MobGender.NONE;
      this.fullName = MobBase.MobType.GODDO_GOLEM.getName();
      this.mobType = MobBase.MobType.GODDO_GOLEM;
      this.mobGroup = MobBase.MobGroup.GOLEM;
      this.hostileMobGroups.add(MobBase.MobGroup.MONSTER);
      this.friendlyMobGroups.add(MobBase.MobGroup.GOLEM);
      this.friendlyMobGroups.add(MobBase.MobGroup.CHARMED_MONSTER);
      this.friendlyMobGroups.add(MobBase.MobGroup.VILLAGER);
      this.sprite = this.mob.createMobSpriteSheet(this.mobType, this.gender);
      this.mobFlags = MobFactory.getMobFlagsInstance(this.mobType);
      Color[][] colorLayersArrays = this.mobFlags.getColorLayersArray();
      int randomColor = Utilities.randomInt(colorLayersArrays[0].length);
      this.colorLayers[0] = colorLayersArrays[0][randomColor];
      if (Utilities.randomInt(5) == 0) {
         randomColor = Utilities.randomInt(colorLayersArrays[1].length);
      }

      this.colorLayers[1] = colorLayersArrays[1][randomColor];
      if (Utilities.randomInt(5) == 0) {
         randomColor = Utilities.randomInt(colorLayersArrays[2].length);
      }

      this.colorLayers[2] = colorLayersArrays[2][randomColor];
      if (Utilities.randomInt(5) == 0) {
         randomColor = Utilities.randomInt(colorLayersArrays[3].length);
      }

      this.colorLayers[3] = colorLayersArrays[3][randomColor];
      if (Utilities.randomInt(5) == 0) {
         randomColor = Utilities.randomInt(colorLayersArrays[4].length);
      }

      this.colorLayers[4] = colorLayersArrays[4][randomColor];
      this.colorLayers[5] = colorLayersArrays[5][0];
      this.configureNewFinalize(message, findReachMap, false);
   }

   @Override
   public void renderParticles(int depthAmount) {
      if (!this.dead) {
         this.particle
            .newParticleGroup(
               this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
               this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.FIRE_BLUE
            );
         if (Utilities.randomInt(9) == 0) {
            this.particle
               .newParticleGroup(
                  this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                  this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
                  ParticleModule.ParticleSet.GRAY
               );
         }

         if (Utilities.randomInt(30) == 0) {
            this.particle
               .newParticle(
                  this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                  this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleType.SPECK,
                  ParticleModule.ParticleSet.MAGIC_BLUE
               );
         }

         if (Utilities.randomInt(200) == 0) {
            this.projectile
               .newExpandingEnergyTrail(
                  this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                  this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleSet.MAGIC_BLUE
               );
         }
      } else {
         if (Utilities.randomInt(60) == 0) {
            this.particle
               .newParticleGroup(
                  this.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                  this.getBottomPixelY(true) - 2 - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
         }

         if (Utilities.randomInt(30) == 0) {
            this.particle
               .newParticleGroup(
                  this.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                  this.getBottomPixelY(true) - 2 - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
                  ParticleModule.ParticleSet.GRAY
               );
         }
      }
   }

   @Override
   public void renderSpriteParticles(float x, float y) {
      if (!this.dead) {
         this.particle
            .newParticleGroup(
               x + this.getCenterPixelX(false) + Utilities.randomInt(-2, 2),
               y + this.getCenterPixelY(false) + Utilities.randomInt(-6, 6),
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.FIRE_BLUE,
               true
            );
         if (Utilities.randomInt(9) == 0) {
            this.particle
               .newParticleGroup(
                  x + this.getCenterPixelX(false) + Utilities.randomInt(-2, 2),
                  y + this.getCenterPixelY(false) + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
                  ParticleModule.ParticleSet.GRAY,
                  true
               );
         }
      } else {
         if (Utilities.randomInt(60) == 0) {
            this.particle
               .newParticleGroup(
                  x + this.getCenterPixelX(false) + Utilities.randomInt(-8, 8),
                  y + this.getBottomPixelY(false) - 2.0F - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
         }

         if (Utilities.randomInt(30) == 0) {
            this.particle
               .newParticleGroup(
                  x + this.getCenterPixelX(false) + Utilities.randomInt(-8, 8),
                  y + this.getBottomPixelY(false) - 2.0F - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS,
                  ParticleModule.ParticleSet.GRAY,
                  true
               );
         }
      }
   }
}
