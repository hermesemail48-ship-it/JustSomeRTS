package rtr.mobs.golems;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class IceCrystalGolem extends MobBase {
   public IceCrystalGolem() throws SlickException {
   }

   @Override
   public void configNew(int id, float x, float y, boolean findReachMap) throws SlickException {
      this.configNew(id, x, y, null, findReachMap);
   }

   @Override
   public void configNew(int id, float x, float y, String message, boolean findReachMap) throws SlickException {
      super.configNew(id, x, y);
      this.gender = MobBase.MobGender.NONE;
      this.fullName = MobBase.MobType.ICE_CRYSTAL_GOLEM.getName();
      this.mobType = MobBase.MobType.ICE_CRYSTAL_GOLEM;
      this.mobGroup = MobBase.MobGroup.GOLEM;
      this.hostileMobGroups.add(MobBase.MobGroup.MONSTER);
      this.friendlyMobGroups.add(MobBase.MobGroup.GOLEM);
      this.friendlyMobGroups.add(MobBase.MobGroup.CHARMED_MONSTER);
      this.friendlyMobGroups.add(MobBase.MobGroup.VILLAGER);
      this.sprite = this.mob.createMobSpriteSheet(this.mobType, this.gender);
      this.mobFlags = MobFactory.getMobFlagsInstance(this.mobType);
      Color[][] colorLayersArrays = this.mobFlags.getColorLayersArray();
      int offset1 = 0;
      int offset2 = 1;
      switch (Utilities.randomInt(4)) {
         case 0:
            offset1 = 0;
            offset2 = 1;
            break;
         case 1:
            offset1 = 2;
            offset2 = 3;
            break;
         case 2:
            offset1 = 4;
            offset2 = 5;
            break;
         case 3:
            offset1 = 6;
            offset2 = 7;
      }

      int randomColor = Utilities.randomInt(offset1, offset2);
      this.colorLayers[0] = colorLayersArrays[0][randomColor];
      randomColor = Utilities.randomInt(offset1, offset2);
      this.colorLayers[1] = colorLayersArrays[1][randomColor];
      randomColor = Utilities.randomInt(offset1, offset2);
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
         if (Utilities.randomInt(2) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-2, 2),
                  centerY + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
         }

         if (Utilities.randomInt(90) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-8, 8),
                  bottomY - 2 - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
         }
      } else {
         if (Utilities.randomInt(4) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-2, 2),
                  centerY + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.FIRE_BLUE
               );
         }

         if (Utilities.randomInt(180) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-8, 8),
                  bottomY - 2 - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.FIRE_BLUE
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
         if (Utilities.randomInt(2) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-2, 2),
                  centerY + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
         }

         if (Utilities.randomInt(90) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-8, 8),
                  bottomY - 2 - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
         }
      } else {
         if (Utilities.randomInt(4) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-2, 2),
                  centerY + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
         }

         if (Utilities.randomInt(180) == 0) {
            this.particle
               .newParticleGroup(
                  centerX + Utilities.randomInt(-8, 8),
                  bottomY - 2 - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
         }
      }
   }
}
