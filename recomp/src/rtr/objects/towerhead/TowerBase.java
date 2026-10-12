package rtr.objects.towerhead;

import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.map.CollisionModule;
import rtr.map.MapModule;
import rtr.map.ai.MapAIModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.objects.towerhead.spritesheet.TowerSprite;
import rtr.objects.towerhead.towerheadflags.TowerHeadFlags;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.Utilities;

public class TowerBase {
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected MapAIModule mapAI = (MapAIModule)StateBase.getModule(ModuleBase.ModuleType.MAP_AI);
   protected MissileModule missile = (MissileModule)StateBase.getModule(ModuleBase.ModuleType.MISSILE);
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   protected ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   protected CollisionModule collision = (CollisionModule)StateBase.getModule(ModuleBase.ModuleType.COLLISION);
   protected ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   protected SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   protected PerkModule perk = (PerkModule)StateBase.getModule(ModuleBase.ModuleType.PERK);
   protected TowerHeadFlags towerFlags;
   protected int towerX;
   protected int towerY;
   protected int towerCenterX;
   protected int towerCenterY;
   protected int reloadRateTick;
   protected boolean reloaded;
   protected boolean reloading;
   protected boolean firing;
   protected int fireRateTick;
   protected MobBase targetMob;
   protected int targetMobID = -1;
   protected ObjectBase targetObject;
   protected int targetObjectID = -1;
   protected int targetCoordinateX = -1;
   protected int targetCoordinateY = -1;
   protected ObjectBase tower;
   protected TowerSprite sprite;
   protected float angleCurrent = 0.0F;
   protected float angleDesired = 0.0F;

   public TowerHeadFlags getTowerFlags() {
      return this.towerFlags;
   }

   public TowerBase(int x, int y, ObjectBase towerObject) throws SlickException {
      this.towerX = x;
      this.towerY = y;
      this.towerCenterX = x * 16 + 8;
      this.towerCenterY = y * 16 + 8;
      this.tower = towerObject;
   }

   public TowerBase(YMLDataMap dataMap, ObjectBase towerObject) throws SlickException {
      String towerTypeString = dataMap.getOrDefault("towerType", "null");
      TowerBase.TowerType towerType = null;

      TowerBase.TowerType[] var8;
      for (TowerBase.TowerType type : TowerBase.TowerType.values()) {
         if (type.toString().equals(towerTypeString)) {
            towerType = type;
            break;
         }
      }

      String upgradeStageString = dataMap.getOrDefault("upgradeStage", "null");
      TowerBase.TowerUpgradeStage upgradeStage = null;

      TowerBase.TowerUpgradeStage[] var10;
      for (TowerBase.TowerUpgradeStage typex : TowerBase.TowerUpgradeStage.values()) {
         if (typex.toString().equals(upgradeStageString)) {
            upgradeStage = typex;
            break;
         }
      }

      if (upgradeStage == null) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 1", "Updating tower save data for tower: " + towerType);
         upgradeStage = towerObject.getObjectFlags().getTowerUpgradeStage();
         if (upgradeStage == null) {
            System.out.println("Failed to update tower save data from InDev 33f for tower " + towerType + " resetting to default.");
            upgradeStage = TowerBase.TowerUpgradeStage.BASE;
         } else {
            System.out.println("Updated data to: " + towerType + " - " + upgradeStage);
         }
      }

      this.towerFlags = this.object.getTowerHeadFlagFactory().getTowerFlags(towerType, upgradeStage);
      this.towerX = Integer.parseInt(dataMap.getOrDefault("towerX", 0));
      this.towerY = Integer.parseInt(dataMap.getOrDefault("towerY", 0));
      this.towerCenterX = Integer.parseInt(dataMap.getOrDefault("towerCenterX", 0));
      this.towerCenterY = Integer.parseInt(dataMap.getOrDefault("towerCenterY", 0));
      this.reloadRateTick = Integer.parseInt(dataMap.getOrDefault("reloadRateTick", 0));
      this.reloaded = Boolean.parseBoolean(dataMap.getOrDefault("reloaded", false));
      this.reloading = Boolean.parseBoolean(dataMap.getOrDefault("reloading", false));
      this.firing = Boolean.parseBoolean(dataMap.getOrDefault("firing", false));
      this.fireRateTick = Integer.parseInt(dataMap.getOrDefault("fireRateTick", 0));
      this.targetMobID = Integer.parseInt(dataMap.getOrDefault("targetMobID", -1));
      this.targetObjectID = Integer.parseInt(dataMap.getOrDefault("targetObjectID", -1));
      this.targetCoordinateX = Integer.parseInt(dataMap.getOrDefault("targetCoordinateX", -1));
      this.targetCoordinateY = Integer.parseInt(dataMap.getOrDefault("targetCoordinateY", -1));
      this.tower = towerObject;
      this.sprite = this.object.createTowerSpriteSheet(this.towerFlags.getTowerType());
      this.angleCurrent = Float.parseFloat(dataMap.getOrDefault("angleCurrent", 0.0F));
      this.angleDesired = Float.parseFloat(dataMap.getOrDefault("angleDesired", 0.0F));
   }

   public void render() throws SlickException {
      if (this.sprite != null) {
         float mapX = this.map.getMapX();
         float mapY = this.map.getMapY();
         if (this.map.isInView(this.towerX * 16, this.towerY * 16, 10)) {
            this.sprite.render(this.towerX * 16 + mapX, this.towerY * 16 + mapY, this.angleCurrent, true);
         }
      }
   }

   public void renderMapWriter() throws SlickException {
      if (this.sprite != null) {
         this.sprite.render(this.towerX * 16, this.towerY * 16, this.angleCurrent, false);
      }
   }

   public void update() throws SlickException {
      if (this.sprite != null) {
         this.sprite.update();
         if ((int)this.angleCurrent != (int)this.angleDesired) {
            int rate = 1;
            if (this.targetMob != null && Math.abs(this.angleDesired - this.angleCurrent) > 8.0F) {
               rate = 5;
            }

            if ((int)this.angleCurrent - (int)this.angleDesired <= -1) {
               this.angleCurrent += rate;
            } else if ((int)this.angleCurrent - (int)this.angleDesired >= 1) {
               this.angleCurrent -= rate;
            }
         }
      }
   }

   protected void findTarget() throws SlickException {
      if (this.towerFlags.canTargetMob()) {
         this.findTargetMob();
      }

      if (this.targetMob == null) {
         if (this.towerFlags.canTargetObject()) {
            this.findTargetObject();
         }

         if (this.targetObject == null) {
            if (this.towerFlags.canTargetCoordinates()) {
               this.findTargetCoordinate();
            }
         }
      }
   }

   protected void findTargetMob() {
   }

   protected void findTargetObject() {
   }

   protected void findTargetCoordinate() {
   }

   protected boolean verifyTarget() throws SlickException {
      if (this.towerFlags.canTargetMob() && this.targetMob != null) {
         return this.verifyTargetMob();
      } else if (this.towerFlags.canTargetObject() && this.targetObject != null) {
         return this.verifyTargetObject();
      } else {
         return this.towerFlags.canTargetCoordinates() && this.targetCoordinateX != -1 && this.targetCoordinateY != -1 ? this.verifyTargetCoordinate() : false;
      }
   }

   protected boolean verifyTargetMob() throws SlickException {
      return false;
   }

   protected boolean verifyTargetObject() throws SlickException {
      return false;
   }

   protected boolean verifyTargetCoordinate() throws SlickException {
      return false;
   }

   public boolean canFireMainAmmo() {
      if (this.tower.getObjectFlags().getAmmoMax() > 0) {
         if (this.tower.getAmmo() <= 0) {
            return false;
         } else {
            return this.towerFlags.getFireEnergyCost() > 0 ? this.tower.getEnergy() >= this.towerFlags.getFireEnergyCost() : true;
         }
      } else {
         return this.towerFlags.getFireEnergyCost() > 0 && this.tower.getEnergy() >= this.towerFlags.getFireEnergyCost();
      }
   }

   public boolean canFireAltAmmo() {
      return this.tower.getAmmo() > 0 && this.towerFlags.getAltMissileType() != null;
   }

   protected void resetTarget() {
      this.firing = false;
      this.targetMob = null;
      this.targetObject = null;
      this.targetCoordinateX = -1;
      this.targetCoordinateY = -1;
   }

   protected void setAnimation(TowerSprite.TowerAnimationFrames a) {
      this.sprite.setPosition(a);
   }

   public void setPositionFace() {
      if (this.sprite != null) {
         if (this.reloaded) {
            this.setAnimation(TowerSprite.TowerAnimationFrames.FACE_LOADED);
         } else {
            this.setAnimation(TowerSprite.TowerAnimationFrames.FACE_UNLOADED);
         }
      }
   }

   public void setPositionFire() {
      if (this.sprite != null) {
         this.setAnimation(TowerSprite.TowerAnimationFrames.FIRE);
      }
   }

   public void setPositionReload() {
      if (this.sprite != null) {
         this.setAnimation(TowerSprite.TowerAnimationFrames.RELOAD);
      }
   }

   protected void updateTargetDirection(int pixelX, int pixelY) {
      double xDiff = pixelX - this.towerX * 16;
      double yDiff = pixelY - this.towerY * 16;
      this.angleDesired = (float)Math.toDegrees(Math.atan2(yDiff, xDiff)) + 90.0F;
   }

   protected void runRandomRotation() {
      if (Utilities.randomInt(this.towerFlags.getRotationRate()) == 0) {
         this.angleDesired = Utilities.randomInt(360);
      }
   }

   public int getX() {
      return this.towerX;
   }

   public int getY() {
      return this.towerY;
   }

   public int getCenterPixelX() {
      return this.towerCenterX;
   }

   public int getCenterPixelY() {
      return this.towerCenterY;
   }

   public MobBase getTargetMob() {
      return this.targetMob;
   }

   public void loadSync() {
      if (this.targetMobID != -1) {
         this.targetMob = this.mob.getMobHashMap().get(this.targetMobID);
      }

      if (this.targetObjectID != -1) {
         this.targetObject = this.object.getObjects().get(this.targetObjectID);
      }
   }

   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      TowerBase.TowerType towerType = this.towerFlags.getTowerType();
      if (towerType != null) {
         dataMap.put("towerType", towerType.toString());
      }

      TowerBase.TowerUpgradeStage upgradeStage = this.towerFlags.getTowerUpgradeStage();
      if (upgradeStage != null) {
         dataMap.put("upgradeStage", upgradeStage.toString());
      }

      if (this.towerX != 0) {
         dataMap.put("towerX", this.towerX);
      }

      if (this.towerY != 0) {
         dataMap.put("towerY", this.towerY);
      }

      if (this.towerCenterX != 0) {
         dataMap.put("towerCenterX", this.towerCenterX);
      }

      if (this.towerCenterY != 0) {
         dataMap.put("towerCenterY", this.towerCenterY);
      }

      if (this.reloadRateTick != 0) {
         dataMap.put("reloadRateTick", this.reloadRateTick);
      }

      if (this.reloaded) {
         dataMap.put("reloaded", this.reloaded);
      }

      if (this.reloading) {
         dataMap.put("reloading", this.reloading);
      }

      if (this.firing) {
         dataMap.put("firing", this.firing);
      }

      if (this.fireRateTick != 0) {
         dataMap.put("fireRateTick", this.fireRateTick);
      }

      if (this.targetMob != null) {
         dataMap.put("targetMobID", Integer.toString(this.targetMob.getID()));
      }

      if (this.targetObject != null) {
         dataMap.put("targetObjectID", Integer.toString(this.targetObject.getID()));
      }

      if (this.targetCoordinateX != -1) {
         dataMap.put("targetCoordinateX", this.targetCoordinateX);
      }

      if (this.targetCoordinateY != -1) {
         dataMap.put("targetCoordinateY", this.targetCoordinateY);
      }

      if (this.angleCurrent != 0.0F) {
         dataMap.put("angleCurrent", this.angleCurrent);
      }

      if (this.angleDesired != 0.0F) {
         dataMap.put("angleDesired", this.angleDesired);
      }

      return dataMap;
   }

   public static enum TowerType {
      ELEMENTAL_BOLT,
      BOW,
      BALLISTA,
      BULLET,
      SLING,
      MOTIVATOR,
      SPRAY,
      BANISH,
      ATTRACT,
      RECOMBOBULATOR,
      PHANTOM_DART,
      STATIC,
      GOD_BOW,
      CORRUPTED_FIRE_BOW,
      CORRUPTED_ICE_BOW,
      CORRUPTED_POISON_BOW,
      CORRUPTED_LIGHTNING_BOW,
      CORRUPTED_FIRE_PHANTOM_DART,
      CORRUPTED_ICE_PHANTOM_DART,
      CORRUPTED_POISON_PHANTOM_DART,
      CORRUPTED_LIGHTNING_PHANTOM_DART;
   }

   public static enum TowerUpgradeStage {
      BASE,
      UPGRADE_1,
      UPGRADE_2,
      UPGRADE_3,
      FIRE,
      POISON,
      ICE,
      LIGHTNING;
   }
}
