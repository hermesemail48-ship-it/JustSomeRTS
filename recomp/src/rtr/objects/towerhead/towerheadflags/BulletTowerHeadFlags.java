package rtr.objects.towerhead.towerheadflags;

import rtr.PerkModule;
import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.towerhead.TowerBase;
import rtr.resources.ResourceModule;

public class BulletTowerHeadFlags extends TowerHeadFlags {
   public BulletTowerHeadFlags(TowerBase.TowerUpgradeStage subType) {
      super(subType);
   }

   @Override
   protected void configTower() {
      this.towerType = TowerBase.TowerType.BULLET;
      this.collisionHeight = CollisionModule.CollisionHeight.HIGH;
      this.rotationRate = 100;
      this.findTargetRate = 60;
      this.changeTargetRate = 300;
      this.ammoType = ResourceModule.ResourceType.STONE_BALLS;
      this.canTargetMob = true;
      this.canTargetObject = true;
      this.targetMobGroups.add(MobBase.MobGroup.MONSTER);
      if (this.upgradeStage == TowerBase.TowerUpgradeStage.BASE) {
         this.distance = 10;
         this.reloadRate = 15;
         this.fireRate = 15;
         this.missileType = MissileModule.MissileType.BASIC_BULLET;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_1) {
         this.distance = 10;
         this.reloadRate = 15;
         this.fireRate = 15;
         this.missileType = MissileModule.MissileType.HEAVY_BULLET;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_2) {
         this.distance = 10;
         this.reloadRate = 15;
         this.fireRate = 15;
         this.missileType = MissileModule.MissileType.REINFORCED_BULLET;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.FIRE) {
         this.distance = 10;
         this.reloadRate = 15;
         this.fireRate = 15;
         this.fireEnergyCost = 5;
         this.missileType = MissileModule.MissileType.FIRE_BULLET;
         this.altMissileType = MissileModule.MissileType.REINFORCED_BULLET;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.LIGHTNING) {
         this.distance = 10;
         this.reloadRate = 15;
         this.fireRate = 15;
         this.fireEnergyCost = 5;
         this.missileType = MissileModule.MissileType.LIGHTNING_BULLET;
         this.altMissileType = MissileModule.MissileType.REINFORCED_BULLET;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.ICE) {
         this.distance = 10;
         this.reloadRate = 15;
         this.fireRate = 15;
         this.fireEnergyCost = 5;
         this.missileType = MissileModule.MissileType.ICE_BULLET;
         this.altMissileType = MissileModule.MissileType.REINFORCED_BULLET;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BULLET_TOWER_RANGE) > 0.0) {
         this.distance = (int)(this.distance + this.perk.getPerkTypeTotal(PerkModule.PerkType.BULLET_TOWER_RANGE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BULLET_TOWER_FIRE_RATE) > 0.0) {
         this.fireRate = (int)(this.fireRate - this.fireRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.BULLET_TOWER_FIRE_RATE));
      }
   }
}
