package rtr.missiles;

import java.util.EnumMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.lighting.Light;
import rtr.lighting.LightingModule;
import rtr.map.CollisionModule;
import rtr.map.MapModule;
import rtr.map.ai.MapAIModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;

public class MissileBase {
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected ParticleModule particles = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   protected ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   protected MissileModule missile = (MissileModule)StateBase.getModule(ModuleBase.ModuleType.MISSILE);
   protected CollisionModule collision = (CollisionModule)StateBase.getModule(ModuleBase.ModuleType.COLLISION);
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected MapAIModule mapAI = (MapAIModule)StateBase.getModule(ModuleBase.ModuleType.MAP_AI);
   protected ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected LightingModule lighting = (LightingModule)StateBase.getModule(ModuleBase.ModuleType.LIGHTING);
   protected SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   protected Image missileImage;
   protected MissileModule.MissileType missileType;
   protected float originX;
   protected float originY;
   protected float missileX;
   protected float missileY;
   protected int tileX;
   protected int tileY;
   protected float velocity;
   protected float velX;
   protected float velY;
   protected float velMod;
   protected float friction;
   protected float angle;
   protected float angleDraw;
   protected int decayRate;
   protected int decayRateTick;
   protected EnumMap<MobBase.DamageType, Integer> damageTypesMin = new EnumMap<>(MobBase.DamageType.class);
   protected EnumMap<MobBase.DamageType, Integer> damageTypesMax = new EnumMap<>(MobBase.DamageType.class);
   protected byte lightRadius;
   protected byte lightIntensity;
   protected Light light;
   private static Color shadow = new Color(0, 0, 0, 100);
   protected MobBase.DamageCause damageCause = MobBase.DamageCause.TOWER;
   protected MobBase firingMob;
   protected int firingMobID = -1;
   protected ObjectBase firingObject;
   protected int firingObjectID = -1;
   protected MobBase targetMob;
   protected int targetMobID = -1;
   protected ObjectBase targetObject;
   protected int targetObjectID = -1;
   protected int targetCoordinatesX = -1;
   protected int targetCoordinatesY = -1;
   protected MobBase.MobGroup mobGroup;
   protected float targetX;
   protected float targetY;
   protected boolean isTop;
   protected boolean isActive;

   public void configNew() {
      this.configNew(0, 0);
      this.initMissile(0, 0);
   }

   public void configNew(int x, int y) {
      this.missileX = x;
      this.missileY = y;
      this.originX = x;
      this.originY = y;
      this.tileUpdate();
      this.isActive = true;
   }

   public void configNew(int x, int y, ObjectBase o, int tX, int tY) {
      this.configNew(x, y);
      this.targetObject = o;
      this.mobGroup = this.targetObject.getObjectFlags().getMobGroup();
      this.targetX = tX * 16 + 8;
      this.targetY = tY * 16 + 8;
      this.initMissile(x, y);
   }

   public void configNew(int x, int y, MobBase m) {
      this.configNew(x, y);
      this.targetMob = m;
      this.mobGroup = this.targetMob.getMobGroup();
      this.targetX = this.targetMob.getCenterPixelX(true);
      this.targetY = this.targetMob.getCenterPixelY(true);
      this.initMissile(x, y);
   }

   public void configNew(int x, int y, int tX, int tY) {
      this.configNew(x, y);
      this.targetCoordinatesX = tX;
      this.targetCoordinatesY = tY;
      this.targetX = tX * 16 + 8;
      this.targetY = tY * 16 + 8;
      this.initMissile(x, y);
   }

   public void loadMissile(int id, YMLDataMap dataMap) {
      String missileTypeString = dataMap.getOrDefault("missileType", "null");

      MissileModule.MissileType[] var7;
      for (MissileModule.MissileType mT : MissileModule.MissileType.values()) {
         if (mT.toString().equals(missileTypeString)) {
            this.missileType = mT;
            break;
         }
      }

      this.missileImage = this.missile.getMissileImage(this.missileType);
      this.originX = Float.parseFloat(dataMap.getOrDefault("originX", 0.0F));
      this.originY = Float.parseFloat(dataMap.getOrDefault("originY", 0.0F));
      this.missileX = Float.parseFloat(dataMap.getOrDefault("missileX", 0.0F));
      this.missileY = Float.parseFloat(dataMap.getOrDefault("missileY", 0.0F));
      this.tileX = Integer.parseInt(dataMap.getOrDefault("tileX", 0));
      this.tileY = Integer.parseInt(dataMap.getOrDefault("tileY", 0));
      this.velocity = Float.parseFloat(dataMap.getOrDefault("velocity", 0.0F));
      this.velX = Float.parseFloat(dataMap.getOrDefault("velX", 0.0F));
      this.velY = Float.parseFloat(dataMap.getOrDefault("velY", 0.0F));
      this.velMod = Float.parseFloat(dataMap.getOrDefault("velMod", 0.0F));
      this.friction = Float.parseFloat(dataMap.getOrDefault("friction", 0.0F));
      this.angle = Float.parseFloat(dataMap.getOrDefault("angle", 0.0F));
      this.angleDraw = Float.parseFloat(dataMap.getOrDefault("angleDraw", 0.0F));
      this.decayRate = Integer.parseInt(dataMap.getOrDefault("decayRate", 0));
      this.decayRateTick = Integer.parseInt(dataMap.getOrDefault("decayRateTick", 0));
      if (dataMap.containsMap("damageTypesMin")) {
         this.damageTypesMin = dataMap.unpackDamageTypeMap("damageTypesMin");
      }

      if (dataMap.containsMap("damageTypesMax")) {
         this.damageTypesMax = dataMap.unpackDamageTypeMap("damageTypesMax");
      }

      this.lightRadius = Byte.parseByte(dataMap.getOrDefault("lightRadius", 0));
      this.lightIntensity = Byte.parseByte(dataMap.getOrDefault("lightIntensity", 0));
      if (this.lighting.isInitialized() && this.lightRadius > 0 && this.lightIntensity > 0) {
         this.light = new Light(this.tileX, this.tileY, this.lightRadius, this.lightIntensity, false);
      }

      String damageCauseString = dataMap.getOrDefault("damageCause", MobBase.DamageCause.NONE.toString());

      MobBase.DamageCause[] var8;
      for (MobBase.DamageCause dC : MobBase.DamageCause.values()) {
         if (dC.toString().equals(damageCauseString)) {
            this.damageCause = dC;
            break;
         }
      }

      this.firingMobID = Integer.parseInt(dataMap.getOrDefault("firingMobID", -1));
      this.firingObjectID = Integer.parseInt(dataMap.getOrDefault("firingObjectID", -1));
      this.targetMobID = Integer.parseInt(dataMap.getOrDefault("targetMobID", -1));
      this.targetObjectID = Integer.parseInt(dataMap.getOrDefault("targetObjectID", -1));
      this.targetCoordinatesX = Integer.parseInt(dataMap.getOrDefault("targetCoordinatesX", 0));
      this.targetCoordinatesY = Integer.parseInt(dataMap.getOrDefault("targetCoordinatesY", 0));
      String mobGroupString = dataMap.getOrDefault("mobGroup", "null");

      MobBase.MobGroup[] var9;
      for (MobBase.MobGroup type : MobBase.MobGroup.values()) {
         if (type.toString().equals(mobGroupString)) {
            this.mobGroup = type;
            break;
         }
      }

      this.targetX = Float.parseFloat(dataMap.getOrDefault("targetX", 0.0F));
      this.targetY = Float.parseFloat(dataMap.getOrDefault("targetY", 0.0F));
      this.isTop = Boolean.parseBoolean(dataMap.getOrDefault("isTop", false));
      this.isActive = Boolean.parseBoolean(dataMap.getOrDefault("isActive", false));
   }

   protected void initMissile(int x, int y) {
   }

   protected void updateMissileAngle() {
      this.updateMissileAngle(true);
   }

   protected void updateMissileAngle(boolean recalculateMobPosition) {
      double xDiff = 0.0;
      double yDiff = 0.0;
      if (this.targetMob != null && recalculateMobPosition) {
         this.targetX = this.targetMob.getMobX();
         this.targetY = this.targetMob.getMobY();
         xDiff = this.targetX - this.missileX;
         yDiff = this.targetY - this.missileY;
      } else {
         xDiff = this.targetX - this.originX;
         yDiff = this.targetY - this.originY;
      }

      this.angleDraw = (float)Math.toDegrees(Math.atan2(yDiff, xDiff)) + 90.0F;
   }

   public void render() throws SlickException {
      if (this.missileImage != null) {
         float mapX = this.map.getMapX();
         float mapY = this.map.getMapY();
         if (this.map.isInView(this.missileX, this.missileY, 10)) {
            int shadowHeight = 1;
            if (this.isActive) {
               shadowHeight = 5;
            }

            this.missileImage
               .drawEmbedded(
                  this.missileX + mapX - this.missileImage.getWidth() / 2,
                  this.missileY + mapY - this.missileImage.getHeight() / 2 + shadowHeight,
                  this.missileImage.getWidth(),
                  this.missileImage.getHeight(),
                  this.angleDraw,
                  shadow
               );
            this.missileImage
               .drawEmbedded(
                  this.missileX + mapX - this.missileImage.getWidth() / 2,
                  this.missileY + mapY - this.missileImage.getHeight() / 2,
                  this.missileImage.getWidth(),
                  this.missileImage.getHeight(),
                  this.angleDraw,
                  Color.white
               );
         }
      }
   }

   public void update() throws SlickException {
      this.decayRateTick++;
      if (this.decayRateTick > this.decayRate) {
         this.missile.remove(this);
      }

      if (this.light != null) {
         this.light.setLightX(this.tileX);
         this.light.setLightY(this.tileY);
      }

      this.tileUpdate();
   }

   private void tileUpdate() {
      this.tileX = (int)(this.missileX / 16.0F);
      this.tileY = (int)(this.missileY / 16.0F);
      if (this.tileX < 0) {
         this.tileX = 0;
      }

      if (this.tileX > 255) {
         this.tileX = 255;
      }

      if (this.tileY < 0) {
         this.tileY = 0;
      }

      if (this.tileY > 255) {
         this.tileY = 255;
      }
   }

   public MobBase getTargetMob() {
      return this.targetMob;
   }

   public ObjectBase getTargetObject() {
      return this.targetObject;
   }

   public int getTargetCoordinatesX() {
      return this.targetCoordinatesX;
   }

   public int getTargetCoordinatesY() {
      return this.targetCoordinatesY;
   }

   protected boolean canMissileHitTarget(MobBase m) {
      int thisTargetX = m.getCenterPixelX(true);
      int thisTargetY = m.getCenterPixelY(true);
      if (!(this.velocity >= 1.0F)) {
         return (int)this.missileX >= thisTargetX - 6
            && (int)this.missileX <= thisTargetX + 6
            && (int)this.missileY >= thisTargetY - 6
            && (int)this.missileY <= thisTargetY + 6
            && m.isAvailable();
      } else {
         boolean hit = false;

         for (int x = 0; x < this.velocity; x++) {
            float missileXPrevious = this.missileX - (float)(Math.cos(this.angle) * x);
            float missileYPrevious = this.missileY - (float)(Math.sin(this.angle) * x);
            hit = (int)missileXPrevious >= thisTargetX - 6
               && (int)missileXPrevious <= thisTargetX + 6
               && (int)missileYPrevious >= thisTargetY - 6
               && (int)missileYPrevious <= thisTargetY + 6
               && m.isAvailable();
            if (hit) {
               break;
            }
         }

         return hit;
      }
   }

   protected boolean canMissileHitTarget(int thisTargetX, int thisTargetY) {
      if (!(this.velocity >= 1.0F)) {
         return (int)this.missileX >= thisTargetX - 3
            && (int)this.missileX <= thisTargetX + 3
            && (int)this.missileY >= thisTargetY - 3
            && (int)this.missileY <= thisTargetY + 3;
      } else {
         boolean hit = false;

         for (int x = 0; x < this.velocity; x++) {
            float missileXPrevious = this.missileX - (float)(Math.cos(this.angle) * x);
            float missileYPrevious = this.missileY - (float)(Math.sin(this.angle) * x);
            hit = (int)missileXPrevious >= thisTargetX - 3
               && (int)missileXPrevious <= thisTargetX + 3
               && (int)missileYPrevious >= thisTargetY - 3
               && (int)missileYPrevious <= thisTargetY + 3;
            if (hit) {
               break;
            }
         }

         return hit;
      }
   }

   public void overrideDamage(int i) {
      int damageOut = i / this.damageTypesMin.size();
      if (damageOut <= 0) {
         damageOut = 1;
      }

      for (MobBase.DamageType t : this.damageTypesMin.keySet()) {
         this.damageTypesMin.put(t, i);
      }

      for (MobBase.DamageType t : this.damageTypesMax.keySet()) {
         this.damageTypesMax.put(t, i + 1);
      }
   }

   public void setFiringMob(MobBase m) {
      this.firingMob = m;
      this.damageCause = MobBase.DamageCause.COMBAT;
   }

   public void setFiringObject(ObjectBase o) {
      this.firingObject = o;
   }

   public int getMinDamage() {
      int minDamage = 0;

      for (MobBase.DamageType t : this.damageTypesMin.keySet()) {
         minDamage += this.damageTypesMin.get(t);
      }

      return minDamage;
   }

   public int getAverageDamage() {
      return (this.getMinDamage() + this.getMaxDamage()) / 2;
   }

   public int getMaxDamage() {
      int maxDamage = 0;

      for (MobBase.DamageType t : this.damageTypesMax.keySet()) {
         maxDamage += this.damageTypesMax.get(t);
      }

      return maxDamage;
   }

   public MissileModule.MissileType getType() {
      return this.missileType;
   }

   public Light getLight() {
      return this.light;
   }

   public boolean isTop() {
      return this.isTop;
   }

   public boolean isActive() {
      return this.isActive;
   }

   public EnumMap<MobBase.DamageType, Integer> getDamageTypesMin() {
      return this.damageTypesMin;
   }

   public EnumMap<MobBase.DamageType, Integer> getDamageTypesMax() {
      return this.damageTypesMax;
   }

   public void loadSync() {
      if (this.firingMobID != -1) {
         this.firingMob = this.mob.getMobHashMap().get(this.firingMobID);
      }

      if (this.firingObjectID != -1) {
         this.firingObject = this.object.getObjects().get(this.firingObjectID);
      }

      if (this.targetMobID != -1) {
         this.targetMob = this.mob.getMobHashMap().get(this.targetMobID);
      }

      if (this.targetObjectID != -1) {
         this.targetObject = this.object.getObjects().get(this.targetObjectID);
      }

      this.updateMissileAngle(false);
   }

   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      if (this.missileType != null) {
         dataMap.put("missileType", this.missileType != null ? this.missileType.toString() : "null");
      }

      if (this.originX != 0.0) {
         dataMap.put("originX", this.originX);
      }

      if (this.originY != 0.0) {
         dataMap.put("originY", this.originY);
      }

      if (this.missileX != 0.0) {
         dataMap.put("missileX", this.missileX);
      }

      if (this.missileY != 0.0) {
         dataMap.put("missileY", this.missileY);
      }

      if (this.tileX != 0) {
         dataMap.put("tileX", this.tileX);
      }

      if (this.tileY != 0) {
         dataMap.put("tileY", this.tileY);
      }

      if (this.velocity != 0.0) {
         dataMap.put("velocity", this.velocity);
      }

      if (this.velX != 0.0) {
         dataMap.put("velX", this.velX);
      }

      if (this.velY != 0.0) {
         dataMap.put("velY", this.velY);
      }

      if (this.velMod != 0.0) {
         dataMap.put("velMod", this.velMod);
      }

      if (this.friction != 0.0) {
         dataMap.put("friction", this.friction);
      }

      if (this.angle != 0.0) {
         dataMap.put("angle", this.angle);
      }

      if (this.angleDraw != 0.0) {
         dataMap.put("angleDraw", this.angleDraw);
      }

      if (this.decayRate != 0) {
         dataMap.put("decayRate", this.decayRate);
      }

      if (this.decayRateTick != 0) {
         dataMap.put("decayRateTick", this.decayRateTick);
      }

      if (this.damageTypesMin.size() > 0) {
         dataMap.put("damageTypesMin", dataMap.packDamageTypeMap(this.damageTypesMin));
      }

      if (this.damageTypesMax.size() > 0) {
         dataMap.put("damageTypesMax", dataMap.packDamageTypeMap(this.damageTypesMax));
      }

      if (this.lightRadius != 0) {
         dataMap.put("lightRadius", this.lightRadius);
      }

      if (this.lightIntensity != 0) {
         dataMap.put("lightIntensity", this.lightIntensity);
      }

      if (this.damageCause != null) {
         dataMap.put("damageCause", this.damageCause.toString());
      }

      if (this.firingMob != null) {
         dataMap.put("firingMobID", this.firingMob.getID());
      }

      if (this.firingObject != null) {
         dataMap.put("firingObjectID", this.firingObject.getID());
      }

      if (this.targetMob != null) {
         dataMap.put("targetMobID", this.targetMob.getID());
      }

      if (this.targetObject != null) {
         dataMap.put("targetObjectID", this.targetObject.getID());
      }

      if (this.targetCoordinatesX != 0) {
         dataMap.put("targetCoordinatesX", this.targetCoordinatesX);
      }

      if (this.targetCoordinatesY != 0) {
         dataMap.put("targetCoordinatesY", this.targetCoordinatesY);
      }

      if (this.mobGroup != null) {
         dataMap.put("mobGroup", this.mobGroup != null ? this.mobGroup.toString() : "null");
      }

      if (this.targetX != 0.0) {
         dataMap.put("targetX", this.targetX);
      }

      if (this.targetY != 0.0) {
         dataMap.put("targetY", this.targetY);
      }

      if (this.isTop) {
         dataMap.put("isTop", this.isTop);
      }

      if (this.isActive) {
         dataMap.put("isActive", this.isActive);
      }

      return dataMap;
   }
}
