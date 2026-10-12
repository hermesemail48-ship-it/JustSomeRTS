package rtr.objects.towerhead.towerheadflags;

import rtr.PerkModule;
import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.towerhead.TowerBase;
import rtr.resources.ResourceModule;

public class SlingTowerHeadFlags extends TowerHeadFlags {
   public SlingTowerHeadFlags(TowerBase.TowerUpgradeStage subType) {
      super(subType);
   }

   @Override
   protected void configTower() {
      this.towerType = TowerBase.TowerType.SLING;
      this.collisionHeight = CollisionModule.CollisionHeight.MEDIUM;
      this.rotationRate = 500;
      this.findTargetRate = 60;
      this.changeTargetRate = 300;
      this.ammoType = ResourceModule.ResourceType.STONE_BALLS;
      this.canTargetMob = true;
      this.canTargetObject = true;
      this.targetMobGroups.add(MobBase.MobGroup.MONSTER);
      if (this.upgradeStage == TowerBase.TowerUpgradeStage.BASE) {
         this.distance = 12;
         this.reloadRate = 200;
         this.fireRate = 150;
         this.missileType = MissileModule.MissileType.BASIC_STONE_BALL;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_1) {
         this.distance = 12;
         this.reloadRate = 175;
         this.fireRate = 125;
         this.missileType = MissileModule.MissileType.HEAVY_STONE_BALL;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_2) {
         this.distance = 12;
         this.reloadRate = 150;
         this.fireRate = 100;
         this.missileType = MissileModule.MissileType.REINFORCED_STONE_BALL;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.FIRE) {
         this.distance = 12;
         this.reloadRate = 150;
         this.fireRate = 100;
         this.fireEnergyCost = 25;
         this.missileType = MissileModule.MissileType.EXPLODING_STONE_BALL;
         this.altMissileType = MissileModule.MissileType.REINFORCED_STONE_BALL;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.LIGHTNING) {
         this.distance = 12;
         this.reloadRate = 150;
         this.fireRate = 100;
         this.fireEnergyCost = 25;
         this.missileType = MissileModule.MissileType.LIGHTNING_STONE_BALL;
         this.altMissileType = MissileModule.MissileType.REINFORCED_STONE_BALL;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.ICE) {
         this.distance = 12;
         this.reloadRate = 150;
         this.fireRate = 100;
         this.fireEnergyCost = 25;
         this.missileType = MissileModule.MissileType.ICE_STONE_BALL;
         this.altMissileType = MissileModule.MissileType.REINFORCED_STONE_BALL;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.SLING_TOWER_RANGE) > 0.0) {
         this.distance = (int)(this.distance + this.perk.getPerkTypeTotal(PerkModule.PerkType.SLING_TOWER_RANGE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.SLING_TOWER_FIRE_RATE) > 0.0) {
         this.fireRate = (int)(this.fireRate - this.fireRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.SLING_TOWER_FIRE_RATE));
      }
   }
}
