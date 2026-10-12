package rtr.missiles.elementalbolt;

import org.newdawn.slick.SlickException;
import rtr.SoundModule;
import rtr.lighting.Light;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class MissileLightningBolt extends MissileElementalBoltBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.LIGHTNING_BOLT;
      this.decayRate = 300;
      this.damageTypesMin.put(MobBase.DamageType.MAGIC_REGULAR, 5);
      this.damageTypesMax.put(MobBase.DamageType.MAGIC_REGULAR, 10);
      this.damageTypesMin.put(MobBase.DamageType.MAGIC_ELECTRIC, 30);
      this.damageTypesMax.put(MobBase.DamageType.MAGIC_ELECTRIC, 50);
      this.lightIntensity = 2;
      this.lightRadius = 16;
      this.light = new Light(this.tileX, this.tileY, this.lightIntensity, this.lightRadius, false);
      this.isTop = true;
      this.velX = Utilities.randomInt(-2, 2);
      this.velY = Utilities.randomInt(-2, 2);
      this.missileImage = this.missile.getMissileImage(this.missileType);
      this.updateMissileAngle();
   }

   @Override
   protected void runFlightEffects() {
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 3; i++) {
            this.particles.newParticleGroup(this.missileX, this.missileY, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.MAGIC_YELLOW);
            this.particles
               .newParticleGroup(
                  this.missileX + Utilities.randomInt(-1, 1),
                  this.missileY + Utilities.randomInt(-1, 1),
                  ParticleModule.ParticleGroup.FIRE,
                  ParticleModule.ParticleSet.MAGIC_YELLOW
               );
         }
      }
   }

   @Override
   protected void runHitEffects() throws SlickException {
      this.sound.playSound(SoundModule.SoundType.MAGIC_HIT, this.missileX, this.missileY);
      if (this.map.isInView(this.missileX, this.missileY, 10)) {
         for (int i = 0; i < 5; i++) {
            this.projectile.newSmallBlast((int)this.missileX, (int)this.missileY, ParticleModule.ParticleSet.MAGIC_YELLOW);
         }
      }
   }
}
