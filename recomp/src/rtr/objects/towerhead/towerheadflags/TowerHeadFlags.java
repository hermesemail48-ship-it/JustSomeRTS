package rtr.objects.towerhead.towerheadflags;

import java.util.ArrayList;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.map.CollisionModule;
import rtr.map.MapModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.objects.towerhead.TowerBase;
import rtr.resources.ResourceModule;
import rtr.states.StateBase;

public class TowerHeadFlags {
   protected PerkModule perk = (PerkModule)StateBase.getModule(ModuleBase.ModuleType.PERK);
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected TowerBase.TowerType towerType;
   TowerBase.TowerUpgradeStage upgradeStage;
   protected MissileModule.MissileType missileType;
   protected MissileModule.MissileType altMissileType;
   protected CollisionModule.CollisionHeight collisionHeight;
   protected int distance;
   protected int rotationRate;
   protected int findTargetRate;
   protected int changeTargetRate;
   protected int reloadRate;
   protected int fireRate;
   protected int fireEnergyCost;
   protected ResourceModule.ResourceType ammoType;
   protected boolean canTargetMob;
   protected boolean canTargetObject;
   protected boolean canTargetCoordinates;
   protected ArrayList<MobBase.MobGroup> targetMobGroups = new ArrayList<>();
   private boolean initialized;

   public TowerBase.TowerType getTowerType() {
      return this.towerType;
   }

   public TowerBase.TowerUpgradeStage getTowerUpgradeStage() {
      return this.upgradeStage;
   }

   public MissileModule.MissileType getMissileType() {
      return this.missileType;
   }

   public MissileModule.MissileType getAltMissileType() {
      return this.altMissileType;
   }

   public CollisionModule.CollisionHeight getCollisionHeight() {
      return this.collisionHeight;
   }

   public int getDistance() {
      return this.distance;
   }

   public int getRotationRate() {
      return this.rotationRate;
   }

   public int getFindTargetRate() {
      return this.findTargetRate;
   }

   public int getChangeTargetRate() {
      return this.changeTargetRate;
   }

   public int getReloadRate() {
      return this.reloadRate;
   }

   public int getFireRate() {
      return this.fireRate;
   }

   public int getFullReloadRate() {
      return this.fireRate + this.reloadRate;
   }

   public int getFireEnergyCost() {
      return this.fireEnergyCost;
   }

   public ResourceModule.ResourceType getAmmoType() {
      return this.ammoType;
   }

   public String getAmmoTypeName() {
      if (this.ammoType != null) {
         return this.ammoType.getTemplate().getNamePlural();
      } else {
         return this.fireEnergyCost > 0 ? "Energy" : "None";
      }
   }

   public boolean canTargetMob() {
      return this.canTargetMob;
   }

   public boolean canTargetObject() {
      return this.canTargetObject;
   }

   public boolean canTargetCoordinates() {
      return this.canTargetCoordinates;
   }

   public ArrayList<MobBase.MobGroup> getTargetMobGroups() {
      return this.targetMobGroups;
   }

   public boolean isInitialized() {
      return this.initialized;
   }

   public TowerHeadFlags(TowerBase.TowerUpgradeStage towerUpgradeStage) {
      this.upgradeStage = towerUpgradeStage;
   }

   public void initialize() {
      this.initialized = true;
      this.configTower();
   }

   protected void configTower() {
   }
}
