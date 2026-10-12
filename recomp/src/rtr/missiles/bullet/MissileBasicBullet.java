package rtr.missiles.bullet;

import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.utilities.Utilities;

public class MissileBasicBullet extends MissileBulletBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.BASIC_BULLET;
      this.decayRate = 5000;
      this.damageTypesMin.put(MobBase.DamageType.CRUSHING, 7);
      this.damageTypesMax.put(MobBase.DamageType.CRUSHING, 10);
      this.velocity = 4.0F + Utilities.randomFloat() * 2.0F;
      this.friction = 0.01F;
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
