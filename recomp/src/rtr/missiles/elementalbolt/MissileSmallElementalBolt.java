package rtr.missiles.elementalbolt;

import rtr.lighting.Light;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.utilities.Utilities;

public class MissileSmallElementalBolt extends MissileElementalBoltBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.SMALL_ELEMENTAL_BOLT;
      this.decayRate = 300;
      this.damageTypesMin.put(MobBase.DamageType.MAGIC_REGULAR, 20);
      this.damageTypesMax.put(MobBase.DamageType.MAGIC_REGULAR, 40);
      this.lightIntensity = 2;
      this.lightRadius = 16;
      this.light = new Light(this.tileX, this.tileY, this.lightIntensity, this.lightRadius, false);
      this.isTop = true;
      this.velX = Utilities.randomInt(-2, 2);
      this.velY = Utilities.randomInt(-2, 2);
      this.missileImage = this.missile.getMissileImage(this.missileType);
      this.updateMissileAngle();
   }
}
