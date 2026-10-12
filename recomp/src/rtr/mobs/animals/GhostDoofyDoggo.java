package rtr.mobs.animals;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class GhostDoofyDoggo extends MobBase {
   public GhostDoofyDoggo() throws SlickException {
   }

   @Override
   public void configNew(int id, float x, float y, boolean findReachMap) throws SlickException {
      this.configNew(id, x, y, null, findReachMap);
   }

   @Override
   public void configNew(int id, float x, float y, String message, boolean findReachMap) throws SlickException {
      super.configNew(id, x, y);
      this.gender = MobBase.MobGender.MALE;
      this.firstName = "Moon Moon";
      this.fullName = "Moon Moon";
      this.mobType = MobBase.MobType.GHOST_DOOFY_DOGGO;
      this.mobGroup = MobBase.MobGroup.GHOST;
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
      if (this.alpha > 0.0F && Utilities.randomInt(10) == 0) {
         this.particle
            .newParticleGroup(
               this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
               this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
               ParticleModule.ParticleGroup.SMOKE_BELLOW,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               ParticleModule.ParticleSet.MAGIC_BLUE
            );
      }
   }

   @Override
   public void renderSpriteParticles(float x, float y) {
      if (this.alpha > 0.0F && Utilities.randomInt(10) == 0) {
         this.particle
            .newParticleGroup(
               x + this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
               y + this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
               ParticleModule.ParticleGroup.SMOKE_BELLOW,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               ParticleModule.ParticleSet.MAGIC_BLUE
            );
      }
   }
}
