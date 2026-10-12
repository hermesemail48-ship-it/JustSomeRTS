package rtr.objects.towerhead.towerheadflags;

import rtr.PerkModule;
import rtr.map.CollisionModule;
import rtr.mobs.MobBase;
import rtr.objects.towerhead.TowerBase;

public class StaticTowerHeadFlags extends TowerHeadFlags {
   public StaticTowerHeadFlags(TowerBase.TowerUpgradeStage subType) {
      super(subType);
   }

   @Override
   protected void configTower() {
      this.towerType = TowerBase.TowerType.STATIC;
      this.collisionHeight = CollisionModule.CollisionHeight.HIGH;
      this.canTargetMob = true;
      this.targetMobGroups.add(MobBase.MobGroup.MONSTER);
      if (this.upgradeStage == TowerBase.TowerUpgradeStage.BASE) {
         this.distance = 5;
         this.fireRate = 150;
         this.fireEnergyCost = 16;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_1) {
         this.distance = 5;
         this.fireRate = 130;
         this.fireEnergyCost = 14;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_2) {
         this.distance = 7;
         this.fireRate = 110;
         this.fireEnergyCost = 12;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_3) {
         this.distance = 7;
         this.fireRate = 90;
         this.fireEnergyCost = 10;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.STATIC_TOWER_RANGE) > 0.0) {
         this.distance = (int)(this.distance + this.perk.getPerkTypeTotal(PerkModule.PerkType.STATIC_TOWER_RANGE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.STATIC_TOWER_FIRE_RATE) > 0.0) {
         this.fireRate = (int)(this.fireRate - this.fireRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.STATIC_TOWER_FIRE_RATE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.STATIC_TOWER_ENERGY_COST) > 0.0) {
         this.fireEnergyCost = (int)(this.fireEnergyCost - this.fireEnergyCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.STATIC_TOWER_ENERGY_COST));
      }
   }
}
