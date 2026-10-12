package rtr.objects.towerhead.towerheadflags;

import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.towerhead.TowerBase;

public class CorruptedIcePhantomDartTowerHeadFlags extends TowerHeadFlags {
   public CorruptedIcePhantomDartTowerHeadFlags(TowerBase.TowerUpgradeStage subType) {
      super(subType);
   }

   @Override
   protected void configTower() {
      this.towerType = TowerBase.TowerType.CORRUPTED_ICE_PHANTOM_DART;
      this.collisionHeight = CollisionModule.CollisionHeight.HIGH;
      this.rotationRate = 300;
      this.findTargetRate = 60;
      this.changeTargetRate = 300;
      this.canTargetMob = true;
      this.canTargetObject = true;
      this.targetMobGroups.add(MobBase.MobGroup.VILLAGER);
      this.targetMobGroups.add(MobBase.MobGroup.NOMAD);
      this.targetMobGroups.add(MobBase.MobGroup.MIGRANT);
      this.targetMobGroups.add(MobBase.MobGroup.CHARMED_MONSTER);
      this.targetMobGroups.add(MobBase.MobGroup.DOMESTICATED_ANIMAL);
      this.targetMobGroups.add(MobBase.MobGroup.GOLEM);
      this.distance = 16;
      this.reloadRate = 10;
      this.fireRate = 50;
      this.fireEnergyCost = 3;
      this.missileType = MissileModule.MissileType.PHANTOM_ICE_DART;
   }
}
