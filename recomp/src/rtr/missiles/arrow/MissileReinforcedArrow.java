package rtr.missiles.arrow;

import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.utilities.Utilities;

public class MissileReinforcedArrow extends MissileArrowBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.REINFORCED_ARROW;
      this.decayRate = 5000;
      this.damageTypesMin.put(MobBase.DamageType.PIERCING, 30);
      this.damageTypesMax.put(MobBase.DamageType.PIERCING, 40);
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
