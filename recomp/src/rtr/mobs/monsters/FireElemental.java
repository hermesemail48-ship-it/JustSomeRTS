package rtr.mobs.monsters;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class FireElemental extends MobBase {
   public FireElemental() throws SlickException {
   }

   @Override
   public void configNew(int id, float x, float y, boolean findReachMap) throws SlickException {
      this.configNew(id, x, y, null, findReachMap);
   }

   @Override
   public void configNew(int id, float x, float y, String message, boolean findReachMap) throws SlickException {
      super.configNew(id, x, y);
      this.gender = Utilities.randomInt(2) == 0 ? MobBase.MobGender.MALE : MobBase.MobGender.FEMALE;
      this.fullName = MobBase.MobType.FIRE_ELEMENTAL.getName();
      this.mobType = MobBase.MobType.FIRE_ELEMENTAL;
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
      if (!this.dead) {
         for (int i = 0; i < 2; i++) {
            this.particle
               .newParticleGroup(
                  this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                  this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.FIRE,
                  ParticleModule.ParticleSet.FIRE_RED
               );
         }

         if (Utilities.randomInt(5) == 0 && this.weather.isRaining()) {
            this.particle
               .newParticleGroup(
                  this.getCenterPixelX(true) + Utilities.randomInt(-2, 2),
                  this.getCenterPixelY(true) + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.GRAY,
                  ParticleModule.ParticleSet.WHITE
               );
         }
      } else {
         if (Utilities.randomInt(40) == 0) {
            this.particle
               .newParticleGroup(
                  this.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                  this.getBottomPixelY(true) - 2 - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.FIRE,
                  ParticleModule.ParticleSet.FIRE_RED
               );
         }

         if (Utilities.randomInt(20) == 0) {
            this.particle
               .newParticleGroup(
                  this.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                  this.getBottomPixelY(true) - 2 - Utilities.randomInt(2),
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.BLACK,
                  ParticleModule.ParticleSet.DARK_GRAY
               );
         }
      }
   }

   @Override
   public void renderSpriteParticles(float x, float y) {
      if (!this.dead) {
         for (int i = 0; i < 3; i++) {
            this.particle
               .newParticleGroup(
                  x + this.getCenterPixelX(false) + Utilities.randomInt(-2, 2),
                  y + this.getCenterPixelY(false) + Utilities.randomInt(-6, 6),
                  ParticleModule.ParticleGroup.FIRE,
                  ParticleModule.ParticleSet.FIRE_RED,
                  true
               );
         }
      } else if (Utilities.randomInt(20) == 0) {
         this.particle
            .newParticleGroup(
               x + this.getCenterPixelX(false) + Utilities.randomInt(-8, 8),
               y + this.getBottomPixelY(false) - 2.0F - Utilities.randomInt(2),
               ParticleModule.ParticleGroup.FIRE,
               ParticleModule.ParticleSet.FIRE_RED,
               true
            );
      }
   }

   @Override
   protected void updateSpecial() throws SlickException {
      if (!this.grabbed && !this.dead && Utilities.randomInt(20) == 0) {
         int tileID = this.map.getTileId(this.tileX, this.tileY, 6);
         if (this.map.getMapTileLoader().isTileWater(tileID)
            && !this.map.isTileFrozen(this.tileX, this.tileY)
            && this.resource.getResourceValues()[this.tileX][this.tileY] > 0) {
            this.takeDamage(
               Utilities.randomInt(20) + 1, this.getCenterPixelX(true), this.getCenterPixelY(true), MobBase.DamageType.WATER, MobBase.DamageCause.WATER
            );
            if (this.hitPoints <= 0) {
               this.goal.incrementGoal(GoalModule.GoalType.TAKE_THAT);
            }

            for (int i = 0; i < 50; i++) {
               this.particle
                  .newParticleGroup(
                     this.getCenterPixelX(true) + Utilities.randomInt(-8, 8),
                     this.getCenterPixelY(true) + Utilities.randomInt(12),
                     ParticleModule.ParticleGroup.SMOKE_BELLOW,
                     ParticleModule.ParticleSet.GRAY,
                     ParticleModule.ParticleSet.WHITE
                  );
            }
         }
      }

      if (this.weather.isRaining() && Utilities.randomInt(50) == 0) {
         this.takeDamage(
            Utilities.randomInt(10) + 1, this.getCenterPixelX(true), this.getCenterPixelY(true), MobBase.DamageType.WATER, MobBase.DamageCause.RAIN
         );
      }
   }
}
