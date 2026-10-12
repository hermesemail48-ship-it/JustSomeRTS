package rtr.objects.towerhead.towerheadflags;

import rtr.PerkModule;
import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.towerhead.TowerBase;
import rtr.resources.ResourceModule;

public class BowTowerHeadFlags extends TowerHeadFlags {
   public BowTowerHeadFlags(TowerBase.TowerUpgradeStage subType) {
      super(subType);
   }

   @Override
   protected void configTower() {
      this.towerType = TowerBase.TowerType.BOW;
      this.collisionHeight = CollisionModule.CollisionHeight.HIGH;
      this.rotationRate = 300;
      this.findTargetRate = 60;
      this.changeTargetRate = 300;
      this.ammoType = ResourceModule.ResourceType.BALLISTAE_BOLTS;
      this.canTargetMob = true;
      this.canTargetObject = true;
      this.targetMobGroups.add(MobBase.MobGroup.MONSTER);
      if (this.upgradeStage == TowerBase.TowerUpgradeStage.BASE) {
         this.distance = 16;
         this.reloadRate = 75;
         this.fireRate = 75;
         this.missileType = MissileModule.MissileType.BASIC_ARROW;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_1) {
         this.distance = 18;
         this.reloadRate = 65;
         this.fireRate = 65;
         this.missileType = MissileModule.MissileType.STURDY_ARROW;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_2) {
         this.distance = 20;
         this.reloadRate = 55;
         this.fireRate = 55;
         this.missileType = MissileModule.MissileType.REINFORCED_ARROW;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.FIRE) {
         this.distance = 20;
         this.reloadRate = 55;
         this.fireRate = 55;
         this.fireEnergyCost = 10;
         this.missileType = MissileModule.MissileType.FIRE_ARROW;
         this.altMissileType = MissileModule.MissileType.REINFORCED_ARROW;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.POISON) {
         this.distance = 20;
         this.reloadRate = 55;
         this.fireRate = 55;
         this.fireEnergyCost = 10;
         this.missileType = MissileModule.MissileType.POISON_ARROW;
         this.altMissileType = MissileModule.MissileType.REINFORCED_ARROW;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.ICE) {
         this.distance = 20;
         this.reloadRate = 55;
         this.fireRate = 55;
         this.fireEnergyCost = 10;
         this.missileType = MissileModule.MissileType.ICE_ARROW;
         this.altMissileType = MissileModule.MissileType.REINFORCED_ARROW;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BOW_TOWER_RANGE) > 0.0) {
         this.distance = (int)(this.distance + this.perk.getPerkTypeTotal(PerkModule.PerkType.BOW_TOWER_RANGE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BOW_TOWER_FIRE_RATE) > 0.0) {
         this.fireRate = (int)(this.fireRate - this.fireRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.BOW_TOWER_FIRE_RATE));
      }
   }
}
