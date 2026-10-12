package rtr.mobs.monsters;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class Specter extends MobBase {
   public Specter() throws SlickException {
   }

   @Override
   public void configNew(int id, float x, float y, boolean findReachMap) throws SlickException {
      this.configNew(id, x, y, null, findReachMap);
   }

   @Override
   public void configNew(int id, float x, float y, String message, boolean findReachMap) throws SlickException {
      super.configNew(id, x, y);
      this.gender = Utilities.randomInt(2) == 0 ? MobBase.MobGender.MALE : MobBase.MobGender.FEMALE;
      this.fullName = MobBase.MobType.SPECTER.getName();
      this.mobType = MobBase.MobType.SPECTER;
      this.mobGroup = MobBase.MobGroup.MONSTER;
      this.hostileMobGroups.add(MobBase.MobGroup.VILLAGER);
      this.hostileMobGroups.add(MobBase.MobGroup.DOMESTICATED_ANIMAL);
      this.hostileMobGroups.add(MobBase.MobGroup.GOLEM);
      this.hostileMobGroups.add(MobBase.MobGroup.CHARMED_MONSTER);
      this.hostileMobGroups.add(MobBase.MobGroup.NOMAD);
      this.hostileMobGroups.add(MobBase.MobGroup.MIGRANT);
      this.friendlyMobGroups.add(MobBase.MobGroup.MONSTER);
      this.sprite = this.mob.createMobSpriteSheet(this.mobType, this.gender);
      this.mobFlags = MobFactory.getMobFlagsInstance(this.mobType);
      Color[][] colorLayersArrays = this.mobFlags.getColorLayersArray();
      int randomColor = Utilities.randomInt(colorLayersArrays[0].length);
      this.colorLayers[0] = colorLayersArrays[0][randomColor];
      randomColor = Utilities.randomInt(colorLayersArrays[1].length);
      this.colorLayers[1] = colorLayersArrays[1][randomColor];
      randomColor = Utilities.randomInt(colorLayersArrays[2].length);
      this.colorLayers[2] = colorLayersArrays[2][randomColor];
      randomColor = Utilities.randomInt(colorLayersArrays[3].length);
      this.colorLayers[3] = colorLayersArrays[3][randomColor];
      this.configureNewFinalize(message, findReachMap, true);
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
