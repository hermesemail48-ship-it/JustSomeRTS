package rtr.objects.towerhead.towerheadflags;

import rtr.PerkModule;
import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.towerhead.TowerBase;

public class BanishTowerHeadFlags extends TowerHeadFlags {
   public BanishTowerHeadFlags(TowerBase.TowerUpgradeStage subType) {
      super(subType);
   }

   @Override
   protected void configTower() {
      this.towerType = TowerBase.TowerType.BANISH;
      this.collisionHeight = CollisionModule.CollisionHeight.NONE;
      this.rotationRate = 300;
      this.findTargetRate = 60;
      this.changeTargetRate = 300;
      this.canTargetMob = true;
      this.targetMobGroups.add(MobBase.MobGroup.MONSTER);
      if (this.upgradeStage == TowerBase.TowerUpgradeStage.BASE) {
         this.distance = 5;
         this.reloadRate = 250;
         this.fireRate = 250;
         this.fireEnergyCost = 50;
         this.missileType = MissileModule.MissileType.BANISH_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_1) {
         this.distance = 5;
         this.reloadRate = 200;
         this.fireRate = 200;
         this.fireEnergyCost = 50;
         this.missileType = MissileModule.MissileType.BANISH_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_2) {
         this.distance = 5;
         this.reloadRate = 150;
         this.fireRate = 150;
         this.fireEnergyCost = 50;
         this.missileType = MissileModule.MissileType.BANISH_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_3) {
         this.distance = 5;
         this.reloadRate = 100;
         this.fireRate = 100;
         this.fireEnergyCost = 50;
         this.missileType = MissileModule.MissileType.BANISH_BOLT;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_TOWER_RANGE) > 0.0) {
         this.distance = (int)(this.distance + this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_TOWER_RANGE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_TOWER_FIRE_RATE) > 0.0) {
         this.fireRate = (int)(this.fireRate - this.fireRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_TOWER_FIRE_RATE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_TOWER_ENERGY_COST) > 0.0) {
         this.fireEnergyCost = (int)(this.fireEnergyCost - this.fireEnergyCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.BANISH_TOWER_ENERGY_COST));
      }
   }
}
