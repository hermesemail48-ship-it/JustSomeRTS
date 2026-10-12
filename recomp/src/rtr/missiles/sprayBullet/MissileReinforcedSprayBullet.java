package rtr.missiles.sprayBullet;

import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.utilities.Utilities;

public class MissileReinforcedSprayBullet extends MissileSprayBulletBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.REINFORCED_SPRAY_BULLET;
      this.decayRate = 5000;
      this.damageTypesMin.put(MobBase.DamageType.CRUSHING, 3);
      this.damageTypesMax.put(MobBase.DamageType.CRUSHING, 4);
      this.velocity = 4.0F + (Utilities.randomFloat() - Utilities.randomFloat());
      this.friction = 0.02F + (Utilities.randomFloat() - Utilities.randomFloat()) / 100.0F;
      this.isTop = true;
      if (this.targetMob != null) {
         this.angle = Utilities.calculateProjectileAngle(
            x, y, this.targetX, this.targetY, this.velocity, this.friction, this.targetMob.getAngle(), this.targetMob.getCurrentVelocity()
         );
      } else {
         this.angle = Utilities.calculateProjectileAngle(x, y, this.targetX, this.targetY, this.velocity, this.friction, 0.0F, 0.0F);
      }

      this.angle = this.angle + (Utilities.randomFloat() - Utilities.randomFloat()) / 2.0F;
      this.missileImage = this.missile.getMissileImage(this.missileType);
      this.updateMissileAngle();
   }
}
