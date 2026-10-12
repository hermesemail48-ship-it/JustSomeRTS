package rtr.objects.towerhead.towerheadflags;

import rtr.PerkModule;
import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.objects.towerhead.TowerBase;

public class AttractTowerHeadFlags extends TowerHeadFlags {
   public AttractTowerHeadFlags(TowerBase.TowerUpgradeStage subType) {
      super(subType);
   }

   @Override
   protected void configTower() {
      this.towerType = TowerBase.TowerType.ATTRACT;
      this.collisionHeight = CollisionModule.CollisionHeight.HIGH;
      this.rotationRate = 300;
      this.findTargetRate = 60;
      this.changeTargetRate = 300;
      this.canTargetCoordinates = true;
      if (this.upgradeStage == TowerBase.TowerUpgradeStage.BASE) {
         this.distance = 18;
         this.reloadRate = 10;
         this.fireRate = 150;
         this.fireEnergyCost = 25;
         this.missileType = MissileModule.MissileType.ATTRACT_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_1) {
         this.distance = 20;
         this.reloadRate = 10;
         this.fireRate = 125;
         this.fireEnergyCost = 20;
         this.missileType = MissileModule.MissileType.ATTRACT_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_2) {
         this.distance = 22;
         this.reloadRate = 10;
         this.fireRate = 100;
         this.fireEnergyCost = 15;
         this.missileType = MissileModule.MissileType.ATTRACT_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_3) {
         this.distance = 24;
         this.reloadRate = 10;
         this.fireRate = 75;
         this.fireEnergyCost = 10;
         this.missileType = MissileModule.MissileType.ATTRACT_BOLT;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.ATTRACT_TOWER_RANGE) > 0.0) {
         this.distance = (int)(this.distance + this.perk.getPerkTypeTotal(PerkModule.PerkType.ATTRACT_TOWER_RANGE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.ATTRACT_TOWER_FIRE_RATE) > 0.0) {
         this.fireRate = (int)(this.fireRate - this.fireRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.ATTRACT_TOWER_FIRE_RATE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.ATTRACT_TOWER_ENERGY_COST) > 0.0) {
         this.fireEnergyCost = (int)(this.fireEnergyCost - this.fireEnergyCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.ATTRACT_TOWER_ENERGY_COST));
      }
   }
}
