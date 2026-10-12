package rtr.missiles.ballistaarrow;

import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.utilities.Utilities;

public class MissileSturdyBallistaArrow extends MissileBallistaArrowBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.STURDY_BALLISTA_ARROW;
      this.decayRate = 5000;
      this.damageTypesMin.put(MobBase.DamageType.PIERCING, 100);
      this.damageTypesMax.put(MobBase.DamageType.PIERCING, 175);
      this.velocity = 6.0F + Utilities.randomFloat();
      this.friction = 0.02F;
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
