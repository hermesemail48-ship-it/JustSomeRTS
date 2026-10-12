package rtr.objects.towerhead.towerheadflags;

import rtr.PerkModule;
import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.towerhead.TowerBase;

public class ElementalBoltTowerHeadFlags extends TowerHeadFlags {
   public ElementalBoltTowerHeadFlags(TowerBase.TowerUpgradeStage subType) {
      super(subType);
   }

   @Override
   protected void configTower() {
      this.towerType = TowerBase.TowerType.ELEMENTAL_BOLT;
      this.collisionHeight = CollisionModule.CollisionHeight.HIGH;
      this.rotationRate = 300;
      this.findTargetRate = 60;
      this.changeTargetRate = 300;
      this.canTargetMob = true;
      this.canTargetObject = true;
      this.targetMobGroups.add(MobBase.MobGroup.MONSTER);
      if (this.upgradeStage == TowerBase.TowerUpgradeStage.BASE) {
         this.distance = 14;
         this.reloadRate = 10;
         this.fireRate = 50;
         this.fireEnergyCost = 10;
         this.missileType = MissileModule.MissileType.SMALL_ELEMENTAL_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_1) {
         this.distance = 14;
         this.reloadRate = 10;
         this.fireRate = 45;
         this.fireEnergyCost = 10;
         this.missileType = MissileModule.MissileType.ELEMENTAL_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_2) {
         this.distance = 14;
         this.reloadRate = 10;
         this.fireRate = 40;
         this.fireEnergyCost = 10;
         this.missileType = MissileModule.MissileType.LARGE_ELEMENTAL_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.FIRE) {
         this.distance = 14;
         this.reloadRate = 10;
         this.fireRate = 40;
         this.fireEnergyCost = 10;
         this.missileType = MissileModule.MissileType.FIRE_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.LIGHTNING) {
         this.distance = 14;
         this.reloadRate = 10;
         this.fireRate = 40;
         this.fireEnergyCost = 10;
         this.missileType = MissileModule.MissileType.LIGHTNING_BOLT;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.ICE) {
         this.distance = 14;
         this.reloadRate = 10;
         this.fireRate = 40;
         this.fireEnergyCost = 10;
         this.missileType = MissileModule.MissileType.ICE_BOLT;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_RANGE) > 0.0) {
         this.distance = (int)(this.distance + this.perk.getPerkTypeTotal(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_RANGE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_FIRE_RATE) > 0.0) {
         this.fireRate = (int)(this.fireRate - this.fireRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_FIRE_RATE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_ENERGY_COST) > 0.0) {
         this.fireEnergyCost = (int)(
            this.fireEnergyCost - this.fireEnergyCost * this.perk.getPerkTypeTotal(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_ENERGY_COST)
         );
      }
   }
}
