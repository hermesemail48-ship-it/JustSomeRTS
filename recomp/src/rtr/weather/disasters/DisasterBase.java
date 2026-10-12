package rtr.weather.disasters;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.TimeModule;
import rtr.font.FontModule;
import rtr.lighting.LightingModule;
import rtr.map.CollisionModule;
import rtr.map.MapModule;
import rtr.map.ai.MapAIModule;
import rtr.mobs.MobModule;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.weather.WeatherModule;

public class DisasterBase {
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected MapAIModule mapAI = (MapAIModule)StateBase.getModule(ModuleBase.ModuleType.MAP_AI);
   protected SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   protected WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   protected ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   protected ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   protected ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected LightingModule lighting = (LightingModule)StateBase.getModule(ModuleBase.ModuleType.LIGHTING);
   protected CollisionModule collision = (CollisionModule)StateBase.getModule(ModuleBase.ModuleType.COLLISION);
   protected TimeModule time = (TimeModule)StateBase.getModule(ModuleBase.ModuleType.TIME);
   protected FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   protected DisasterBase.DisasterType disasterType;
   protected int centerX;
   protected int centerY;
   protected int targetX;
   protected int targetY;
   protected boolean godTriggered;
   protected int duration;
   protected int durationTick;
   protected int disasterStep;
   protected Color enviromentColor;
   protected int enviromentLightAdjust;
   protected int enviromentTempAdjust;
   protected double enviromentWindAdjust;
   protected float velocityMultiplier;

   public DisasterBase() {
   }

   public DisasterBase(YMLDataMap dataMap) {
      this.loadDisaster(dataMap);
   }

   public void update() throws SlickException {
   }

   public DisasterBase.DisasterType getDisasterType() {
      return this.disasterType;
   }

   public Color getEnviromentColor() {
      return this.enviromentColor;
   }

   public int getEnviromentLightAdjust() {
      return this.enviromentLightAdjust;
   }

   public int getEnviromentTempAdjust() {
      return this.enviromentTempAdjust;
   }

   public double getEnviromentWindAdjust() {
      return this.enviromentWindAdjust;
   }

   public float getVelocityMultiplier() {
      return this.velocityMultiplier;
   }

   protected void loadDisaster(YMLDataMap dataMap) {
      String disasterTypeString = dataMap.getOrDefault("disasterType", DisasterBase.DisasterType.RAIN.toString());

      DisasterBase.DisasterType[] var6;
      for (DisasterBase.DisasterType t : DisasterBase.DisasterType.values()) {
         if (t.toString().equals(disasterTypeString)) {
            this.disasterType = t;
            break;
         }
      }

      this.centerX = Integer.parseInt(dataMap.getOrDefault("centerX", 0));
      this.centerY = Integer.parseInt(dataMap.getOrDefault("centerY", 0));
      this.targetX = Integer.parseInt(dataMap.getOrDefault("targetX", 0));
      this.targetY = Integer.parseInt(dataMap.getOrDefault("targetY", 0));
      this.godTriggered = Boolean.parseBoolean(dataMap.getOrDefault("godTriggered", false));
      this.duration = Integer.parseInt(dataMap.getOrDefault("duration", 0));
      this.durationTick = Integer.parseInt(dataMap.getOrDefault("durationTick", 0));
      this.disasterStep = Integer.parseInt(dataMap.getOrDefault("disasterStep", 0));
      if (dataMap.containsKey("enviromentColor")) {
         this.enviromentColor = dataMap.unpackColor("enviromentColor");
      }

      this.enviromentLightAdjust = Integer.parseInt(dataMap.getOrDefault("enviromentLightAdjust", 0));
      this.enviromentTempAdjust = Integer.parseInt(dataMap.getOrDefault("enviromentTempAdjust", 0));
      this.enviromentWindAdjust = Double.parseDouble(dataMap.getOrDefault("enviromentWindAdjust", 0.0));
      this.velocityMultiplier = Float.parseFloat(dataMap.getOrDefault("velocityMultiplier", 0.0F));
   }

   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      if (this.disasterType != null) {
         dataMap.put("disasterType", this.disasterType.toString());
      }

      if (this.centerX != 0) {
         dataMap.put("centerX", this.centerX);
      }

      if (this.centerY != 0) {
         dataMap.put("centerY", this.centerY);
      }

      if (this.targetX != 0) {
         dataMap.put("targetX", this.targetX);
      }

      if (this.targetY != 0) {
         dataMap.put("targetY", this.targetY);
      }

      if (this.godTriggered) {
         dataMap.put("godTriggered", this.godTriggered);
      }

      if (this.duration != 0) {
         dataMap.put("duration", this.duration);
      }

      if (this.durationTick != 0) {
         dataMap.put("durationTick", this.durationTick);
      }

      if (this.disasterStep != 0) {
         dataMap.put("disasterStep", this.disasterStep);
      }

      if (this.enviromentColor != null) {
         dataMap.put("enviromentColor", dataMap.packColor(this.enviromentColor));
      }

      if (this.enviromentLightAdjust != 0) {
         dataMap.put("enviromentLightAdjust", this.enviromentLightAdjust);
      }

      if (this.enviromentTempAdjust != 0) {
         dataMap.put("enviromentTempAdjust", this.enviromentTempAdjust);
      }

      if (this.enviromentWindAdjust != 0.0) {
         dataMap.put("enviromentWindAdjust", this.enviromentWindAdjust);
      }

      if (this.velocityMultiplier != 0.0) {
         dataMap.put("velocityMultiplier", this.velocityMultiplier);
      }

      return dataMap;
   }

   public static enum DisasterGroup {
      EARTHQUAKE,
      METEOR_SHOWER,
      WEATHER,
      BLIGHT,
      ECLIPSE,
      COMET;
   }

   public static enum DisasterType {
      EARTHQUAKE(DisasterBase.DisasterGroup.EARTHQUAKE),
      METEOR_SHOWER(DisasterBase.DisasterGroup.METEOR_SHOWER),
      ELECTRICAL_STORM(DisasterBase.DisasterGroup.WEATHER),
      RAIN(DisasterBase.DisasterGroup.WEATHER),
      SNOW(DisasterBase.DisasterGroup.WEATHER),
      HAIL_STORM(DisasterBase.DisasterGroup.WEATHER),
      BLOOD_RAIN(DisasterBase.DisasterGroup.WEATHER),
      BLIGHT(DisasterBase.DisasterGroup.BLIGHT),
      ECLIPSE(DisasterBase.DisasterGroup.ECLIPSE),
      MUSIC(DisasterBase.DisasterGroup.WEATHER),
      COMET(DisasterBase.DisasterGroup.COMET);

      private final DisasterBase.DisasterGroup group;

      private DisasterType(DisasterBase.DisasterGroup group) {
         this.group = group;
      }

      public DisasterBase.DisasterGroup getGroup() {
         return this.group;
      }
   }
}
