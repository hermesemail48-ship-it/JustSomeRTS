package rtr.objects.towerhead.towerheadflags;

import rtr.PerkModule;
import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.objects.towerhead.TowerBase;

public class MotivatorTowerHeadFlags extends TowerHeadFlags {
   public MotivatorTowerHeadFlags(TowerBase.TowerUpgradeStage subType) {
      super(subType);
   }

   @Override
   protected void configTower() {
      this.towerType = TowerBase.TowerType.MOTIVATOR;
      this.collisionHeight = CollisionModule.CollisionHeight.HIGH;
      this.rotationRate = 300;
      this.findTargetRate = 60;
      this.changeTargetRate = 10;
      this.canTargetCoordinates = true;
      if (this.upgradeStage == TowerBase.TowerUpgradeStage.BASE) {
         this.distance = 8;
         this.reloadRate = 5;
         this.fireRate = 100;
         this.fireEnergyCost = 3;
         this.missileType = MissileModule.MissileType.MOTIVATION_BOLT;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATOR_TOWER_RANGE) > 0.0) {
         this.distance = (int)(this.distance + this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATOR_TOWER_RANGE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATOR_TOWER_FIRE_RATE) > 0.0) {
         this.fireRate = (int)(this.fireRate - this.fireRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATOR_TOWER_FIRE_RATE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATOR_TOWER_ENERGY_COST) > 0.0) {
         this.fireEnergyCost = (int)(this.fireEnergyCost - this.fireEnergyCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.MOTIVATOR_TOWER_ENERGY_COST));
      }
   }
}
