package rtr.objects.towerhead.towerheadflags;

import rtr.PerkModule;
import rtr.map.CollisionModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.towerhead.TowerBase;
import rtr.resources.ResourceModule;

public class BallistaTowerHeadFlags extends TowerHeadFlags {
   public BallistaTowerHeadFlags(TowerBase.TowerUpgradeStage subType) {
      super(subType);
   }

   @Override
   protected void configTower() {
      this.towerType = TowerBase.TowerType.BALLISTA;
      this.collisionHeight = CollisionModule.CollisionHeight.NONE;
      this.rotationRate = 300;
      this.findTargetRate = 60;
      this.changeTargetRate = 300;
      this.ammoType = ResourceModule.ResourceType.BALLISTAE_BOLTS;
      this.canTargetMob = true;
      this.canTargetObject = true;
      this.targetMobGroups.add(MobBase.MobGroup.MONSTER);
      if (this.upgradeStage == TowerBase.TowerUpgradeStage.BASE) {
         this.distance = 32;
         this.reloadRate = 750;
         this.fireRate = 50;
         this.missileType = MissileModule.MissileType.BASIC_BALLISTA_ARROW;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_1) {
         this.distance = 36;
         this.reloadRate = 600;
         this.fireRate = 50;
         this.missileType = MissileModule.MissileType.STURDY_BALLISTA_ARROW;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.UPGRADE_2) {
         this.distance = 40;
         this.reloadRate = 500;
         this.fireRate = 50;
         this.missileType = MissileModule.MissileType.REINFORCED_BALLISTA_ARROW;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.FIRE) {
         this.distance = 40;
         this.reloadRate = 500;
         this.fireRate = 50;
         this.fireEnergyCost = 15;
         this.missileType = MissileModule.MissileType.FIRE_BALLISTA_ARROW;
         this.altMissileType = MissileModule.MissileType.REINFORCED_BALLISTA_ARROW;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.POISON) {
         this.distance = 40;
         this.reloadRate = 500;
         this.fireRate = 50;
         this.fireEnergyCost = 15;
         this.missileType = MissileModule.MissileType.POISON_BALLISTA_ARROW;
         this.altMissileType = MissileModule.MissileType.REINFORCED_BALLISTA_ARROW;
      } else if (this.upgradeStage == TowerBase.TowerUpgradeStage.ICE) {
         this.distance = 40;
         this.reloadRate = 500;
         this.fireRate = 50;
         this.fireEnergyCost = 15;
         this.missileType = MissileModule.MissileType.ICE_BALLISTA_ARROW;
         this.altMissileType = MissileModule.MissileType.REINFORCED_BALLISTA_ARROW;
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BALLISTA_TOWER_RANGE) > 0.0) {
         this.distance = (int)(this.distance + this.perk.getPerkTypeTotal(PerkModule.PerkType.BALLISTA_TOWER_RANGE));
      }

      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BALLISTA_TOWER_FIRE_RATE) > 0.0) {
         this.fireRate = (int)(this.fireRate - this.fireRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.BALLISTA_TOWER_FIRE_RATE));
      }
   }
}
