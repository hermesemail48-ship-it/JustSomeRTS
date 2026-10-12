package rtr.missiles.stoneball;

import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.utilities.Utilities;

public class MissileReinforcedStoneBall extends MissileStoneBallBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.REINFORCED_STONE_BALL;
      this.decayRate = 5000;
      this.damageTypesMin.put(MobBase.DamageType.CRUSHING, 8);
      this.damageTypesMax.put(MobBase.DamageType.CRUSHING, 10);
      this.velocity = 1.5F + Utilities.randomFloat();
      this.friction = 0.01F;
      this.isTop = false;
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
