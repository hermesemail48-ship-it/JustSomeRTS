package rtr.objects.towerhead.towerheadflags;

import rtr.PerkModule;
import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.towerhead.TowerBase;

public class RecombobulatorTowerHeadFlags extends TowerHeadFlags {
   public RecombobulatorTowerHeadFlags(TowerBase.TowerUpgradeStage subType) {
      super(subType);
   }

   @Override
   protected void configTower() {
      this.towerType = TowerBase.TowerType.RECOMBOBULATOR;
      this.collisionHeight = CollisionModule.CollisionHeight.HIGH;
      this.rotationRate = 300;
      this.findTargetRate = 60;
      this.changeTargetRate = 300;
      this.canTargetMob = true;
      this.targetMobGroups.add(MobBase.MobGroup.GOLEM);
      if (this.upgradeStage == TowerBase.TowerUpgradeStage.BASE) {
         this.distance = 8;
         this.reloadRate = 10;
         this.fireRate = 250;
         this.fireEnergyCost = 25;
         this.missileType = MissileModule.MissileType.RECOMBOBULATOR_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_1) {
         this.distance = 8;
         this.reloadRate = 10;
         this.fireRate = 200;
         this.fireEnergyCost = 20;
         this.missileType = MissileModule.MissileType.RECOMBOBULATOR_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_2) {
         this.distance = 10;
         this.reloadRate = 10;
         this.fireRate = 150;
         this.fireEnergyCost = 15;
         this.missileType = MissileModule.MissileType.RECOMBOBULATOR_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_3) {
         this.distance = 12;
         this.reloadRate = 10;
         this.fireRate = 100;
         this.fireEnergyCost = 10;
         this.missileType = MissileModule.MissileType.RECOMBOBULATOR_BOLT;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.RECOMBOBULATOR_TOWER_RANGE) > 0.0) {
         this.distance = (int)(this.distance + this.perk.getPerkTypeTotal(PerkModule.PerkType.RECOMBOBULATOR_TOWER_RANGE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.RECOMBOBULATOR_TOWER_FIRE_RATE) > 0.0) {
         this.fireRate = (int)(this.fireRate - this.fireRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.RECOMBOBULATOR_TOWER_FIRE_RATE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.RECOMBOBULATOR_TOWER_ENERGY_COST) > 0.0) {
         this.fireEnergyCost = (int)(
            this.fireEnergyCost - this.fireEnergyCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.RECOMBOBULATOR_TOWER_ENERGY_COST)
         );
      }
   }
}
