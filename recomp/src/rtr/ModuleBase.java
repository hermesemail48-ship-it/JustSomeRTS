package rtr;

import org.newdawn.slick.SlickException;
import rtr.chest.ChestModule;
import rtr.font.FontModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.help.HelpModule;
import rtr.influence.InfluenceModule;
import rtr.lighting.LightingModule;
import rtr.map.CollisionModule;
import rtr.map.DepthModule;
import rtr.map.MapModule;
import rtr.map.MiniMapModule;
import rtr.map.WaypointModule;
import rtr.map.WorkSelectionModule;
import rtr.map.ai.MapAIModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MigrationModule;
import rtr.mobs.MobModule;
import rtr.mobs.essence.EssenceModule;
import rtr.mobs.jobs.MobJobModule;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.LootBoxModule;
import rtr.resources.ResourceModule;
import rtr.resources.TradeModule;
import rtr.road.RoadModule;
import rtr.save.SaveModule;
import rtr.save.YMLDataMap;
import rtr.spawning.CorruptionModule;
import rtr.spawning.SpawnModule;
import rtr.states.StateBase;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.weather.CloudModule;
import rtr.weather.WeatherModule;

public abstract class ModuleBase {
   protected boolean initialized;
   protected boolean requiresSync;
   protected boolean synced;
   protected StateBase currentState;
   protected GameModeTemplateBase.GameMode gameMode;
   protected ModuleBase.ModuleType moduleType;
   protected BackgroundModule background;
   protected CollisionModule collision;
   protected CloudModule cloud;
   protected DataModule data;
   protected DepthModule depth;
   protected MobModule mob;
   protected MobJobModule mobJob;
   protected EssenceModule essence;
   protected FontModule font;
   protected HelpModule help;
   protected InfluenceModule influence;
   protected LightingModule lighting;
   protected MapAIModule mapAI;
   protected MapModule map;
   protected WaypointModule waypoint;
   protected RoadModule road;
   protected MissileModule missile;
   protected MiniMapModule miniMap;
   protected ObjectModule object;
   protected ParticleModule particle;
   protected ProfileModule profile;
   protected ProjectileModule projectile;
   protected ResourceModule resource;
   protected TradeModule trade;
   protected LootBoxModule lootBox;
   protected SaveModule save;
   protected ShadowModule shadow;
   protected SoundModule sound;
   protected SpawnModule spawn;
   protected TimeModule time;
   protected TransitionModule transition;
   protected WeatherModule weather;
   protected WorkSelectionModule workSelection;
   protected StatsModule stats;
   protected CorruptionModule corruption;
   protected MigrationModule migration;
   protected ChestModule chest;
   protected PerkModule perk;
   protected GodModule god;
   protected GoalModule goal;
   protected boolean debug = false;

   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      this.initialized = true;
      this.moduleType = mT;
      if (cS != null) {
         this.currentState = cS;
         this.gameMode = cS.getGameMode();
      }

      this.background = (BackgroundModule)StateBase.getModule(ModuleBase.ModuleType.BACKGROUND);
      this.collision = (CollisionModule)StateBase.getModule(ModuleBase.ModuleType.COLLISION);
      this.data = (DataModule)StateBase.getModule(ModuleBase.ModuleType.DATA);
      this.depth = (DepthModule)StateBase.getModule(ModuleBase.ModuleType.DEPTH);
      this.mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
      this.mobJob = (MobJobModule)StateBase.getModule(ModuleBase.ModuleType.MOB_JOB);
      this.essence = (EssenceModule)StateBase.getModule(ModuleBase.ModuleType.ESSENCE);
      this.font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
      this.help = (HelpModule)StateBase.getModule(ModuleBase.ModuleType.HELP);
      this.influence = (InfluenceModule)StateBase.getModule(ModuleBase.ModuleType.INFLUENCE);
      this.lighting = (LightingModule)StateBase.getModule(ModuleBase.ModuleType.LIGHTING);
      this.map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
      this.waypoint = (WaypointModule)StateBase.getModule(ModuleBase.ModuleType.WAYPOINT);
      this.road = (RoadModule)StateBase.getModule(ModuleBase.ModuleType.ROAD);
      this.mapAI = (MapAIModule)StateBase.getModule(ModuleBase.ModuleType.MAP_AI);
      this.missile = (MissileModule)StateBase.getModule(ModuleBase.ModuleType.MISSILE);
      this.miniMap = (MiniMapModule)StateBase.getModule(ModuleBase.ModuleType.MINI_MAP);
      this.object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
      this.particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
      this.profile = (ProfileModule)StateBase.getModule(ModuleBase.ModuleType.PROFILE);
      this.projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
      this.resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
      this.trade = (TradeModule)StateBase.getModule(ModuleBase.ModuleType.TRADE);
      this.lootBox = (LootBoxModule)StateBase.getModule(ModuleBase.ModuleType.LOOT_BOX);
      this.save = (SaveModule)StateBase.getModule(ModuleBase.ModuleType.SAVE);
      this.shadow = (ShadowModule)StateBase.getModule(ModuleBase.ModuleType.SHADOW);
      this.sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
      this.spawn = (SpawnModule)StateBase.getModule(ModuleBase.ModuleType.SPAWN);
      this.time = (TimeModule)StateBase.getModule(ModuleBase.ModuleType.TIME);
      this.transition = (TransitionModule)StateBase.getModule(ModuleBase.ModuleType.TRANSITION);
      this.weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
      this.cloud = (CloudModule)StateBase.getModule(ModuleBase.ModuleType.CLOUD);
      this.workSelection = (WorkSelectionModule)StateBase.getModule(ModuleBase.ModuleType.WORK_SELECTION);
      this.stats = (StatsModule)StateBase.getModule(ModuleBase.ModuleType.STATS);
      this.corruption = (CorruptionModule)StateBase.getModule(ModuleBase.ModuleType.CORRUPTION);
      this.migration = (MigrationModule)StateBase.getModule(ModuleBase.ModuleType.MIGRATION);
      this.chest = (ChestModule)StateBase.getModule(ModuleBase.ModuleType.CHEST);
      this.perk = (PerkModule)StateBase.getModule(ModuleBase.ModuleType.PERK);
      this.god = (GodModule)StateBase.getModule(ModuleBase.ModuleType.GOD);
      this.goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   }

   public void resetModule() throws SlickException {
      this.initialized = false;
      this.synced = false;
   }

   protected void initLoad() throws SlickException {
      switch (this.gameMode) {
         case MAIN_MENU:
            this.newMainMenu();
            break;
         case MAP_EDITOR:
            if (this.save.getActiveRegionalSave() != null && !this.save.getActiveRegionalSave().isNewGame()) {
               this.loadMapEditor();
            } else {
               this.newMapEditor();
            }
            break;
         case WORLD_MAP:
            if (this.save.getActiveWorldSave() != null && !this.save.getActiveWorldSave().isNewGame()) {
               this.loadWorldMap();
            } else {
               this.newWorldMap();
            }
            break;
         default:
            if (this.save.getActiveRegionalSave() != null && !this.save.getActiveRegionalSave().isNewGame()) {
               this.loadPlay();
            } else {
               this.newPlay();
            }
      }
   }

   protected abstract void newMainMenu() throws SlickException;

   protected abstract void newPlay() throws SlickException;

   protected abstract void loadPlay() throws SlickException;

   protected abstract void newMapEditor() throws SlickException;

   protected abstract void loadMapEditor() throws SlickException;

   protected abstract void newWorldMap() throws SlickException;

   protected abstract void loadWorldMap() throws SlickException;

   protected abstract void loadRegionalSaveData() throws SlickException;

   protected abstract void loadWorldSaveData() throws SlickException;

   public boolean isInitialized() {
      return this.initialized;
   }

   public boolean requiresSync() {
      return this.requiresSync;
   }

   public boolean isSynced() {
      return this.synced;
   }

   public void initLoadSync() throws SlickException {
      this.synced = true;
   }

   public YMLDataMap getRegionalSaveData() throws SlickException {
      return null;
   }

   public YMLDataMap getEditorSaveData() {
      return null;
   }

   public YMLDataMap getWorldMapSaveData() {
      return null;
   }

   public void resetWorldSaveData() throws SlickException {
   }

   public ModuleBase.ModuleType getModuleType() {
      return this.moduleType;
   }

   public static enum ModuleType {
      TIME("time", Text.getText("module.time")),
      BACKGROUND("background", Text.getText("module.background")),
      DATA("data", Text.getText("module.data")),
      GOAL("goal", Text.getText("module.goal")),
      GOD("god", Text.getText("module.god")),
      PERK("perk", Text.getText("module.perk")),
      ROAD("road", Text.getText("module.road")),
      SHADOW("shadow", Text.getText("module.shadow")),
      SOUND("sound", Text.getText("module.sound")),
      STATS("stats", Text.getText("module.stats")),
      TRANSITION("transition", Text.getText("module.transition")),
      CHEST("chest", Text.getText("module.chest")),
      FONT("font", Text.getText("module.font")),
      HELP("help", Text.getText("module.help")),
      INFLUENCE("influence", Text.getText("module.influence")),
      LIGHTING("lighting", Text.getText("module.lighting")),
      COLLISION("collision", Text.getText("module.collision")),
      CLOUD("cloud", Text.getText("module.cloud")),
      DEPTH("depth", Text.getText("module.depth")),
      MAP("map", Text.getText("module.map")),
      MINI_MAP("miniMap", Text.getText("module.miniMap")),
      WAYPOINT("waypoint", Text.getText("module.waypoint")),
      WORK_SELECTION("workSelection", Text.getText("module.workSelection")),
      MAP_AI("mapAI", Text.getText("module.mapAI")),
      MISSILE("missile", Text.getText("module.missile")),
      MIGRATION("migration", Text.getText("module.migration")),
      MOB("mob", Text.getText("module.mob")),
      ESSENCE("essence", Text.getText("module.essence")),
      MOB_JOB("mobJob", Text.getText("module.mobJob")),
      OBJECT("object", Text.getText("module.object")),
      PARTICLE("particle", Text.getText("module.particle")),
      LOOT_BOX("lootBox", Text.getText("module.lootBox")),
      RESOURCE("resource", Text.getText("module.resource")),
      TRADE("trade", Text.getText("module.trade")),
      SAVE("save", Text.getText("module.save")),
      CORRUPTION("corruption", Text.getText("module.corruption")),
      SPAWN("spawn", Text.getText("module.spawn")),
      WEATHER("weather", Text.getText("module.weather")),
      PROJECTILE("projectile", Text.getText("module.projectile")),
      PROFILE("profile", Text.getText("module.profile"));

      String systemName;
      String formattedName;

      private ModuleType(String systemName, String formattedName) {
         this.systemName = systemName;
         this.formattedName = formattedName;
      }

      public String getSystemName() {
         return this.systemName;
      }

      public String getFormattedName() {
         return this.formattedName;
      }
   }
}
