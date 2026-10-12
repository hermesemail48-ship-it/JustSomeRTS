package rtr.objects.towerhead.towerheadflags;

import rtr.PerkModule;
import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.towerhead.TowerBase;

public class PhantomDartTowerHeadFlags extends TowerHeadFlags {
   public PhantomDartTowerHeadFlags(TowerBase.TowerUpgradeStage subType) {
      super(subType);
   }

   @Override
   protected void configTower() {
      this.towerType = TowerBase.TowerType.PHANTOM_DART;
      this.collisionHeight = CollisionModule.CollisionHeight.HIGH;
      this.rotationRate = 300;
      this.findTargetRate = 60;
      this.changeTargetRate = 300;
      this.canTargetMob = true;
      this.canTargetObject = true;
      this.targetMobGroups.add(MobBase.MobGroup.MONSTER);
      if (this.upgradeStage == TowerBase.TowerUpgradeStage.BASE) {
         this.distance = 10;
         this.reloadRate = 10;
         this.fireRate = 50;
         this.fireEnergyCost = 3;
         this.missileType = MissileModule.MissileType.SMALL_PHANTOM_DART;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_1) {
         this.distance = 10;
         this.reloadRate = 10;
         this.fireRate = 50;
         this.fireEnergyCost = 3;
         this.missileType = MissileModule.MissileType.PHANTOM_DART;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_2) {
         this.distance = 10;
         this.reloadRate = 10;
         this.fireRate = 50;
         this.fireEnergyCost = 3;
         this.missileType = MissileModule.MissileType.LARGE_PHANTOM_DART;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.FIRE) {
         this.distance = 10;
         this.reloadRate = 10;
         this.fireRate = 50;
         this.fireEnergyCost = 3;
         this.missileType = MissileModule.MissileType.PHANTOM_FIRE_DART;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.LIGHTNING) {
         this.distance = 10;
         this.reloadRate = 10;
         this.fireRate = 50;
         this.fireEnergyCost = 3;
         this.missileType = MissileModule.MissileType.PHANTOM_LIGHTNING_DART;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.ICE) {
         this.distance = 10;
         this.reloadRate = 10;
         this.fireRate = 50;
         this.fireEnergyCost = 3;
         this.missileType = MissileModule.MissileType.PHANTOM_ICE_DART;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.PHANTOM_DART_TOWER_RANGE) > 0.0) {
         this.distance = (int)(this.distance + this.perk.getPerkTypeTotal(PerkModule.PerkType.PHANTOM_DART_TOWER_RANGE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.PHANTOM_DART_TOWER_FIRE_RATE) > 0.0) {
         this.fireRate = (int)(this.fireRate - this.fireRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.PHANTOM_DART_TOWER_FIRE_RATE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.PHANTOM_DART_TOWER_ENERGY_COST) > 0.0) {
         this.fireEnergyCost = (int)(this.fireEnergyCost - this.fireEnergyCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.PHANTOM_DART_TOWER_ENERGY_COST));
      }
   }
}
