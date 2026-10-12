package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.TimeModule;
import rtr.font.FontModule;
import rtr.goal.GoalModule;
import rtr.lighting.LightingModule;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.map.ai.MapAIModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.mobs.essence.EssenceModule;
import rtr.mobs.jobs.MobJobModule;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.ResourceModule;
import rtr.save.SaveModule;
import rtr.spawning.CorruptionModule;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.Utilities;
import rtr.weather.WeatherModule;

public class SpellBase {
   protected ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   protected ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   protected InfluenceModule influence = (InfluenceModule)StateBase.getModule(ModuleBase.ModuleType.INFLUENCE);
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected LightingModule lighting = (LightingModule)StateBase.getModule(ModuleBase.ModuleType.LIGHTING);
   protected MapAIModule mapAI = (MapAIModule)StateBase.getModule(ModuleBase.ModuleType.MAP_AI);
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected MobJobModule mobJob = (MobJobModule)StateBase.getModule(ModuleBase.ModuleType.MOB_JOB);
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   protected SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   protected SaveModule save = (SaveModule)StateBase.getModule(ModuleBase.ModuleType.SAVE);
   protected TimeModule time = (TimeModule)StateBase.getModule(ModuleBase.ModuleType.TIME);
   protected WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   protected EssenceModule essence = (EssenceModule)StateBase.getModule(ModuleBase.ModuleType.ESSENCE);
   protected MissileModule missile = (MissileModule)StateBase.getModule(ModuleBase.ModuleType.MISSILE);
   protected FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   protected GodModule god = (GodModule)StateBase.getModule(ModuleBase.ModuleType.GOD);
   protected PerkModule perk = (PerkModule)StateBase.getModule(ModuleBase.ModuleType.PERK);
   protected CorruptionModule corruption = (CorruptionModule)StateBase.getModule(ModuleBase.ModuleType.CORRUPTION);
   protected GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   protected int targetX;
   protected int targetY;
   protected int tileX;
   protected int tileY;
   protected SpellBase.SpellType type;
   protected String name;
   protected String description;
   protected int spellStep = 0;
   protected int cooldown = 0;
   protected int castCost = 0;
   protected int maintainCost = 0;
   protected int duration = 0;
   protected int durationTick = 0;
   protected int radius = 0;
   protected MapTilesLoader.TileSet placementObject;
   protected ArrayList<MobBase.MobType> mobTypesList;
   protected boolean allowContinuousCast;
   protected boolean hideCooldownWheel;

   public void config(int x, int y, boolean freeCast) {
   }

   public void render() {
   }

   public void update() throws SlickException {
   }

   public int getCooldown() {
      return this.cooldown;
   }

   public int getCastCost() {
      return this.castCost;
   }

   public int getMaintainCost() {
      return this.maintainCost;
   }

   public int getDuration() {
      return this.duration;
   }

   public int getRadius() {
      return this.radius;
   }

   public MapTilesLoader.TileSet getPlacementObject() {
      return this.placementObject;
   }

   public ArrayList<MobBase.MobType> getMobTypesList() {
      return this.mobTypesList;
   }

   public SpellBase.SpellType getSpellType() {
      return this.type;
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public boolean allowContinuousCast() {
      return this.allowContinuousCast;
   }

   public boolean hideCooldownWheel() {
      return this.hideCooldownWheel;
   }

   public boolean canCast() throws SlickException {
      if (Utilities.isOutOfBoundsOfUsableMap(Game.getCS().getMouseTileX(), Game.getCS().getMouseTileY())) {
         return false;
      } else if (this.corruption.isTileCorrupted(Game.getCS().getMouseTileX(), Game.getCS().getMouseTileY())) {
         return false;
      } else if (this.map.getTileId(Game.getCS().getMouseTileX(), Game.getCS().getMouseTileY(), 0) == 0) {
         return false;
      } else if (this.influence.getInfluenceAmount() < this.castCost) {
         return false;
      } else {
         int spellCooldownAmount = this.influence.getSpellCooldown(this.type);
         return spellCooldownAmount == 0;
      }
   }

   public static enum SpellType {
      BANISH,
      CHARM,
      COLD_AURA,
      COMET,
      CONJURE_ESSENCE,
      CONJURE_MATERIAL,
      CONSTRUCT,
      DISPEL_GOD_STRUCTURE,
      DISPEL_GOLEM,
      DISSOLVE,
      DIVINE_BLESSING,
      DROP,
      EARTHQUAKE,
      FLAME,
      GOD_TOWER,
      GOD_WALL,
      GRAB,
      METEOR,
      RECALL,
      HARVEST,
      HEALING_AURA,
      HOLY_POTATOES,
      HOLY_WOOD,
      ILLUMINATE,
      LIGHTNING_BOLT,
      MAGIC_BOLTS,
      MEND,
      MOTIVATE_LAND,
      REGENERATE,
      RESURRECT,
      SPECIAL_INITIAL_WORLD_MAP_SPAWN_IN,
      STORM,
      SUMMON_HOLY_GOLEM,
      SUMMON_LABOR_GOLEM;
   }
}
