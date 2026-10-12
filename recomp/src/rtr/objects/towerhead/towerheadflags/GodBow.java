package rtr.objects.towerhead.towerheadflags;

import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.towerhead.TowerBase;

public class GodBow extends TowerHeadFlags {
   public GodBow(TowerBase.TowerUpgradeStage subType) {
      super(subType);
   }

   @Override
   protected void configTower() {
      this.towerType = TowerBase.TowerType.GOD_BOW;
      this.collisionHeight = CollisionModule.CollisionHeight.HIGH;
      this.rotationRate = 300;
      this.findTargetRate = 60;
      this.changeTargetRate = 300;
      this.canTargetMob = true;
      this.canTargetObject = true;
      this.targetMobGroups.add(MobBase.MobGroup.MONSTER);
      this.distance = 20;
      this.reloadRate = 75;
      this.fireRate = 75;
      this.fireEnergyCost = 10;
      this.missileType = MissileModule.MissileType.ICE_ARROW;
   }
}
