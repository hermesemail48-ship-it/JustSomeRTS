package rtr.missiles.attractbolt;

import rtr.lighting.Light;
import rtr.missiles.MissileModule;
import rtr.utilities.Utilities;

public class MissileAttractBolt extends MissileAttractBoltBase {
   @Override
   protected void initMissile(int x, int y) {
      this.missileType = MissileModule.MissileType.ATTRACT_BOLT;
      this.decayRate = 300;
      this.lightIntensity = 2;
      this.lightRadius = 16;
      this.light = new Light(this.tileX, this.tileY, this.lightIntensity, this.lightRadius, false);
      this.velocity = 3.0F + Utilities.randomFloat();
      this.friction = 0.02F;
      this.isTop = true;
      this.angle = Utilities.calculateProjectileAngle(x, y, this.targetX, this.targetY, this.velocity, this.friction, 0.0F, 0.0F);
      this.missileImage = this.missile.getMissileImage(this.missileType);
      this.updateMissileAngle();
   }
}
