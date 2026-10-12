package rtr.mobs.golems;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class WoodGolem extends MobBase {
   public WoodGolem() throws SlickException {
   }

   @Override
   public void configNew(int id, float x, float y, boolean findReachMap) throws SlickException {
      this.configNew(id, x, y, null, findReachMap);
   }

   @Override
   public void configNew(int id, float x, float y, String message, boolean findReachMap) throws SlickException {
      super.configNew(id, x, y);
      this.gender = MobBase.MobGender.NONE;
      this.fullName = MobBase.MobType.WOOD_GOLEM.getName();
      this.mobType = MobBase.MobType.WOOD_GOLEM;
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
      randomColor = Utilities.randomInt(colorLayersArrays[1].length);
      this.colorLayers[1] = colorLayersArrays[1][randomColor];
      randomColor = Utilities.randomInt(colorLayersArrays[2].length);
      this.colorLayers[2] = colorLayersArrays[2][randomColor];
      this.colorLayers[3] = colorLayersArrays[3][0];
      this.configureNewFinalize(message, findReachMap, false);
   }

   @Override
   public void renderParticles(int depthAmount) {
      int centerX = this.getCenterPixelX(true);
      int centerY = this.getCenterPixelY(true);
      int bottomY = this.getBottomPixelY(true);
      if (!this.dead) {
         if (Utilities.randomInt(10) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-2, 2),
                  centerY + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-2, 2),
                  centerY + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.BROWN
               );
         }

         if (Utilities.randomInt(90) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-8, 8),
                  bottomY - 2 - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.MAGIC_BLUE
               );
         }
      } else {
         if (Utilities.randomInt(30) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-2, 2),
                  centerY + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-2, 2),
                  centerY + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.BROWN
               );
         }

         if (Utilities.randomInt(180) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-8, 8),
                  bottomY - 2 - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.MAGIC_BLUE
               );
         }
      }
   }

   @Override
   public void renderSpriteParticles(float x, float y) {
      int centerX = this.getCenterPixelX(false);
      int centerY = this.getCenterPixelY(false);
      int bottomY = this.getBottomPixelY(false);
      if (!this.dead) {
         if (Utilities.randomInt(10) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + x + Utilities.randomInt(-2, 2),
                  centerY + y + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
            this.particle
               .newParticleGroup(
                  centerX + x + Utilities.randomInt(-2, 2),
                  centerY + y + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.BROWN,
                  true
               );
         }

         if (Utilities.randomInt(90) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + x + Utilities.randomInt(-8, 8),
                  bottomY + y - 2.0F - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.MAGIC_BLUE,
                  true
               );
         }
      } else {
         if (Utilities.randomInt(30) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + x + Utilities.randomInt(-2, 2),
                  centerY + y + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
            this.particle
               .newParticleGroup(
                  centerX + x + Utilities.randomInt(-2, 2),
                  centerY + y + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.BROWN,
                  true
               );
         }

         if (Utilities.randomInt(180) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + x + Utilities.randomInt(-8, 8),
                  bottomY + y - 2.0F - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.MAGIC_BLUE,
                  true
               );
         }
      }
   }
}
