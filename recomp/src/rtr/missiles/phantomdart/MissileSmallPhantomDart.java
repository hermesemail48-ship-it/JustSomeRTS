package rtr.missiles.phantomdart;

import rtr.lighting.Light;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.utilities.Utilities;

public class MissileSmallPhantomDart extends MissilePhantomDartBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.SMALL_PHANTOM_DART;
      this.decayRate = 300;
      this.damageTypesMin.put(MobBase.DamageType.MAGIC_REGULAR, 4);
      this.damageTypesMax.put(MobBase.DamageType.MAGIC_REGULAR, 6);
      this.lightIntensity = 2;
      this.lightRadius = 16;
      this.light = new Light(this.tileX, this.tileY, this.lightIntensity, this.lightRadius, false);
      this.velocity = 3.0F + Utilities.randomFloat();
      this.isTop = true;
      if (this.targetMob != null) {
         this.angle = Utilities.calculateProjectileAngle(
            x, y, this.targetX, this.targetY, this.velocity, this.friction, this.targetMob.getAngle(), this.targetMob.getCurrentVelocity()
         );
      } else {
         this.angle = Utilities.calculateProjectileAngle(x, y, this.targetX, this.targetY, this.velocity, this.friction, 0.0F, 0.0F);
      }

      this.missileImage = this.missile.getMissileImage(this.missileType);
      this.updateMissileAngle();
   }
}
