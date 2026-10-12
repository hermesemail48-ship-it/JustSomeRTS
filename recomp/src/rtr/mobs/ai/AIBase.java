package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.StatsModule;
import rtr.TimeModule;
import rtr.font.FontModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.influence.InfluenceModule;
import rtr.map.CollisionModule;
import rtr.map.MapModule;
import rtr.map.WaypointModule;
import rtr.map.WorkSelectionModule;
import rtr.map.ai.MapAIModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MigrationModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.mobs.MobModule;
import rtr.mobs.essence.EssenceModule;
import rtr.mobs.jobs.MobJobModule;
import rtr.mobs.pathfinder.PathFinder;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.resources.TradeModule;
import rtr.road.RoadModule;
import rtr.save.SaveModule;
import rtr.save.YMLDataMap;
import rtr.spawning.CorruptionModule;
import rtr.spawning.SpawnModule;
import rtr.states.StateBase;
import rtr.system.threadpool.RTRThreadPool;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;
import rtr.weather.WeatherModule;

public class AIBase {
   protected static final int FIXED_RESOURCE_PICKUP_SPEED = 80;
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected MapAIModule mapAI = (MapAIModule)StateBase.getModule(ModuleBase.ModuleType.MAP_AI);
   protected WaypointModule waypoint = (WaypointModule)StateBase.getModule(ModuleBase.ModuleType.WAYPOINT);
   protected ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   protected ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   protected CollisionModule collision = (CollisionModule)StateBase.getModule(ModuleBase.ModuleType.COLLISION);
   protected ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   protected TradeModule trade = (TradeModule)StateBase.getModule(ModuleBase.ModuleType.TRADE);
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected MobJobModule mobJob = (MobJobModule)StateBase.getModule(ModuleBase.ModuleType.MOB_JOB);
   protected SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   protected FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   protected TimeModule time = (TimeModule)StateBase.getModule(ModuleBase.ModuleType.TIME);
   protected EssenceModule essence = (EssenceModule)StateBase.getModule(ModuleBase.ModuleType.ESSENCE);
   protected SaveModule save = (SaveModule)StateBase.getModule(ModuleBase.ModuleType.SAVE);
   protected WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   protected WorkSelectionModule workSelection = (WorkSelectionModule)StateBase.getModule(ModuleBase.ModuleType.WORK_SELECTION);
   protected RoadModule road = (RoadModule)StateBase.getModule(ModuleBase.ModuleType.ROAD);
   protected MissileModule missile = (MissileModule)StateBase.getModule(ModuleBase.ModuleType.MISSILE);
   protected StatsModule stats = (StatsModule)StateBase.getModule(ModuleBase.ModuleType.STATS);
   protected CorruptionModule corruption = (CorruptionModule)StateBase.getModule(ModuleBase.ModuleType.CORRUPTION);
   protected SpawnModule spawn = (SpawnModule)StateBase.getModule(ModuleBase.ModuleType.SPAWN);
   protected MigrationModule migration = (MigrationModule)StateBase.getModule(ModuleBase.ModuleType.MIGRATION);
   protected GodModule god = (GodModule)StateBase.getModule(ModuleBase.ModuleType.GOD);
   protected PerkModule perk = (PerkModule)StateBase.getModule(ModuleBase.ModuleType.PERK);
   protected GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   protected InfluenceModule influence = (InfluenceModule)StateBase.getModule(ModuleBase.ModuleType.INFLUENCE);
   protected MobBase thisMob;
   protected MobFlagsBase thisMobFlags;
   protected boolean interruptAll;
   protected boolean interruptDeath;
   protected boolean interruptStuck;
   protected boolean interruptAggroMobs;
   protected boolean interruptAggroBuildings;
   protected boolean interruptEnergy;
   protected boolean interruptOnFire;
   protected boolean interruptFrozen;
   protected boolean interruptOverheating;
   protected boolean interruptCold;
   protected AIBase.AIName aiName;
   protected int aiStep = 0;
   protected int aiStepDropResource = 0;
   protected boolean sleeping;
   protected boolean eating;
   protected boolean drinking;
   protected boolean reattack;
   protected OrderedPair coordinates;
   protected PathFinder pathFinder = this.mob.getPathfinder();
   protected ArrayList<OrderedPair> pathCoordinatesList;
   protected ArrayList<OrderedPair> pathCoordinatesBlockList;
   protected OrderedPair goalCoordinates;
   protected boolean stuck;
   protected boolean pathCompleted;
   protected boolean pathFailed;
   protected int pathStep;
   protected boolean waitingForPath;

   public void loadRegionalProperties(YMLDataMap dataMap) {
      this.aiStep = Integer.parseInt(dataMap.getOrDefault("aiStep", 0));
      this.aiStepDropResource = Integer.parseInt(dataMap.getOrDefault("aiStepDropResource", 0));
      this.sleeping = Boolean.parseBoolean(dataMap.getOrDefault("sleeping", false));
      this.eating = Boolean.parseBoolean(dataMap.getOrDefault("eating", false));
      this.drinking = Boolean.parseBoolean(dataMap.getOrDefault("drinking", false));
      this.reattack = Boolean.parseBoolean(dataMap.getOrDefault("reattack", false));
      if (dataMap.containsKey("coordinates")) {
         this.coordinates = dataMap.unpackCoordinates("coordinates");
      }

      if (dataMap.containsKey("pathCoordinatesList")) {
         this.pathCoordinatesList = dataMap.unpackCoordinatesList("pathCoordinatesList");
      }

      if (dataMap.containsKey("pathCoordinatesBlockList")) {
         this.pathCoordinatesBlockList = dataMap.unpackCoordinatesList("pathCoordinatesBlockList");
      }

      if (dataMap.containsKey("goalCoordinates")) {
         this.goalCoordinates = dataMap.unpackCoordinates("goalCoordinates");
      }

      this.stuck = Boolean.parseBoolean(dataMap.getOrDefault("stuck", false));
      this.pathCompleted = Boolean.parseBoolean(dataMap.getOrDefault("pathCompleted", false));
      this.pathFailed = Boolean.parseBoolean(dataMap.getOrDefault("pathFailed", false));
      this.pathStep = Integer.parseInt(dataMap.getOrDefault("pathStep", 0));
      this.waitingForPath = Boolean.parseBoolean(dataMap.getOrDefault("waitingForPath", false));
   }

   public void loadSync() {
   }

   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      if (this.aiStep != 0) {
         dataMap.put("aiStep", this.aiStep);
      }

      if (this.aiStepDropResource != 0) {
         dataMap.put("aiStepDropResource", this.aiStepDropResource);
      }

      if (this.sleeping) {
         dataMap.put("sleeping", this.sleeping);
      }

      if (this.eating) {
         dataMap.put("eating", this.eating);
      }

      if (this.drinking) {
         dataMap.put("drinking", this.drinking);
      }

      if (this.reattack) {
         dataMap.put("reattack", this.reattack);
      }

      if (this.coordinates != null) {
         dataMap.put("coordinates", dataMap.packCoordinates(this.coordinates));
      }

      if (this.pathCoordinatesList != null && this.pathCoordinatesList.size() > 0) {
         dataMap.put("pathCoordinatesList", dataMap.packCoordinatesList(this.pathCoordinatesList));
      }

      if (this.pathCoordinatesBlockList != null && this.pathCoordinatesBlockList.size() > 0) {
         dataMap.put("pathCoordinatesBlockList", dataMap.packCoordinatesList(this.pathCoordinatesBlockList));
      }

      if (this.goalCoordinates != null) {
         dataMap.put("goalCoordinates", dataMap.packCoordinates(this.goalCoordinates));
      }

      if (this.stuck) {
         dataMap.put("stuck", this.stuck);
      }

      if (this.pathCompleted) {
         dataMap.put("pathCompleted", this.pathCompleted);
      }

      if (this.pathFailed) {
         dataMap.put("pathFailed", this.pathFailed);
      }

      if (this.pathStep != 0) {
         dataMap.put("pathStep", this.pathStep);
      }

      if (this.waitingForPath) {
         dataMap.put("waitingForPath", this.waitingForPath);
      }

      return dataMap;
   }

   public AIBase(MobBase m) {
      this.thisMob = m;
      this.thisMobFlags = m.getMobFlags();
   }

   public boolean allowRun() {
      return false;
   }

   public boolean tryRun() throws SlickException {
      return false;
   }

   public void execute() throws SlickException {
      while (this.waitingForPath) {
         try {
            Thread.sleep(3L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }
   }

   public void switchNode(AIBase a) throws SlickException {
      this.aiStep = 0;
      this.aiStepDropResource = 0;
      this.coordinates = null;
      this.pathCoordinatesList = null;
      this.pathCoordinatesBlockList = null;
      this.waitingForPath = false;
      this.sleeping = false;
      this.eating = false;
      this.drinking = false;
      this.reattack = false;
      this.thisMob.getAssignment().reset();
      this.thisMob.setConfusedTarget(null);
      this.thisMob.resetCombatDelayTick();
      this.thisMob.resetWorkDelayTick();
      this.thisMob.getSprite().restart();
      this.thisMob.setPositionFace();
      if (a.allowRun()) {
         this.thisMob.setActiveNode(a);
      } else {
         this.thisMob.setActiveNode(this.thisMob.getAIStart());
      }
   }

   public void interruptNode(AIBase a) throws SlickException {
      this.switchNode(a);
   }

   public boolean interruptCheck() throws SlickException {
      if ((this.interruptAll || this.interruptDeath) && this.thisMob.getHitPoints() <= 0 && this.thisMob.getAIDeath().tryRun()) {
         this.thisMob.setIdle(false);
         this.switchNode(this.thisMob.getAIDeath());
         return true;
      } else if ((this.interruptAll || this.interruptStuck) && this.thisMob.getAIStuck().tryRun()) {
         this.thisMob.setIdle(false);
         this.switchNode(this.thisMob.getAIStuck());
         return true;
      } else if ((this.interruptAll || this.interruptOnFire) && this.thisMob.getAIOnFirePanic().tryRun()) {
         this.thisMob.setIdle(false);
         this.switchNode(this.thisMob.getAIOnFirePanic());
         return true;
      } else if ((this.interruptAll || this.interruptFrozen) && this.thisMob.getAIFrozen().tryRun()) {
         this.thisMob.setIdle(false);
         this.switchNode(this.thisMob.getAIFrozen());
         return true;
      } else {
         if (this.thisMobFlags.getAllowThermal() && this.thisMob.getHitPoints() <= this.thisMob.getHitPointsMax() * 0.75) {
            if ((this.interruptAll || this.interruptOverheating)
               && this.thisMobFlags.getCanThermalOverheatCritical()
               && this.thisMob.getBodyTemp() >= this.thisMobFlags.getThermalOverheatCritical()) {
               if (this.thisMob.getAIFindWaterCheck().tryRun()) {
                  this.thisMob.setIdle(false);
                  this.switchNode(this.thisMob.getAIFindWaterCheck());
                  return true;
               }

               ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
               if (home != null && this.thisMob.canReach(home)) {
                  this.thisMob.setIdle(false);
                  this.switchNode(this.thisMob.getAIGoHome());
                  return true;
               }
            }

            if ((this.interruptAll || this.interruptCold)
               && this.thisMobFlags.getCanThermalFreezeCritical()
               && this.thisMob.getBodyTemp() <= this.thisMobFlags.getThermalFreezeCritical()) {
               ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
               if (home != null && this.thisMob.canReach(home)) {
                  this.thisMob.setIdle(false);
                  this.switchNode(this.thisMob.getAIGoHome());
                  return true;
               }
            }
         }

         if (!this.thisMob.hasCarriedResources()
            && (this.interruptAll || this.interruptAggroMobs)
            && this.thisMobFlags.getAllowCombat()
            && this.thisMob.getAICombatMobs().tryRun()) {
            this.thisMob.setIdle(false);
            this.switchNode(this.thisMob.getAICombatMobs());
            return true;
         } else if ((this.interruptAll || this.interruptAggroBuildings) && this.thisMobFlags.getAllowCombat() && this.thisMob.getAICombatBuildings().tryRun()) {
            this.thisMob.setIdle(false);
            this.switchNode(this.thisMob.getAICombatBuildings());
            return true;
         } else {
            if ((this.interruptAll || this.interruptEnergy) && this.thisMobFlags.getAllowSleep()) {
               ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
               if (this.thisMob.getEnergy() <= 0) {
                  this.thisMob.setIdle(false);
                  this.switchNode(this.thisMob.getAISleepOutside());
                  return true;
               }

               if (this.thisMob.getEnergy() < this.thisMobFlags.getSleepHomeReturnLevel() && home != null && this.thisMob.canReach(home)) {
                  this.thisMob.setIdle(false);
                  this.switchNode(this.thisMob.getAIGoHome());
                  return true;
               }

               if (this.thisMob.getEnergy() < this.thisMobFlags.getSleepOutsideEnergyLevel()) {
                  this.thisMob.setIdle(false);
                  this.switchNode(this.thisMob.getAISleepOutside());
                  return true;
               }
            }

            return false;
         }
      }
   }

   public void runPath() throws SlickException {
      if (this.pathCoordinatesList != null) {
         this.pathFindWalk();
      }
   }

   public void runStuckPath() throws SlickException {
      if (this.pathCoordinatesList != null) {
         this.stuckPathfind();
      }
   }

   public void runFleePath() throws SlickException {
      if (this.pathCoordinatesList != null) {
         this.pathFindFlee();
      }
   }

   protected void requestPath(OrderedPair o) throws SlickException {
      this.goalCoordinates = o;
      this.waitingForPath = true;
      RTRThreadPool.addNewPathRequest(
         this.thisMob,
         this.thisMob.getTileX(),
         this.thisMob.getTileY(),
         o.getX(),
         o.getY(),
         this.thisMobFlags.getAllowForcedPathfinding(),
         this.thisMobFlags.getWanderPreferVillage(),
         this.thisMobFlags.getBlockMapGroup()
      );
   }

   protected void requestPath(int goalX, int goalY) throws SlickException {
      this.goalCoordinates = OrderedPair.getOrderedPair(goalX, goalY);
      this.waitingForPath = true;
      RTRThreadPool.addNewPathRequest(
         this.thisMob,
         this.thisMob.getTileX(),
         this.thisMob.getTileY(),
         goalX,
         goalY,
         this.thisMobFlags.getAllowForcedPathfinding(),
         this.thisMobFlags.getWanderPreferVillage(),
         this.thisMobFlags.getBlockMapGroup()
      );
   }

   public void loadPathCoordinatesList(ArrayList<OrderedPair> list, ArrayList<OrderedPair> blockList) {
      this.pathCoordinatesList = list;
      this.pathCoordinatesBlockList = blockList;
      this.pathFailed = false;
      this.pathCompleted = false;
      this.waitingForPath = false;
      if (this.pathCoordinatesList == null) {
         this.pathFailed = true;
      } else {
         this.pathStep = this.pathCoordinatesList.size() - 1;
      }
   }

   public boolean isWaitingForPath() {
      return this.waitingForPath;
   }

   protected void pathFindWalk() throws SlickException {
      this.thisMob.setPositionWalk();
      this.pathFind();
   }

   protected void pathFindFlee() throws SlickException {
      this.thisMob.setPositionFlee();
      this.pathFind();
   }

   protected void pathFind() throws SlickException {
      int stepX = this.pathCoordinatesList.get(this.pathStep).getX();
      int stepY = this.pathCoordinatesList.get(this.pathStep).getY();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      if (tileX == stepX && tileY == stepY) {
         if (this.pathStep <= 0) {
            this.pathCoordinatesList = null;
            this.pathCoordinatesBlockList = null;
            this.goalCoordinates = null;
            this.pathCompleted = true;
            this.thisMob.setPositionFace();
         } else {
            this.pathStep--;
         }
      } else {
         int goalX = this.pathCoordinatesList.get(0).getX();
         int goalY = this.pathCoordinatesList.get(0).getY();
         if (!this.thisMob.canReach(goalX, goalY) && !this.thisMobFlags.getAllowForcedPathfinding()) {
            this.pathFailed = true;
         } else {
            byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
            if (blockMap[goalX][goalY] > 0
               && this.object.getBuildingAtLocation(goalX, goalY) != null
               && this.object.getBuildingAtLocation(goalX, goalY).getObjectFlags().getMobGroup() == this.thisMob.getMobGroup()) {
               this.pathFailed = true;
            } else if (blockMap[stepX][stepY] > 0
               || tileX < stepX && tileY < stepY && blockMap[stepX - 1][stepY] > 0 && blockMap[stepX][stepY - 1] > 0
               || tileX > stepX && tileY > stepY && blockMap[stepX + 1][stepY] > 0 && blockMap[stepX][stepY + 1] > 0
               || tileX > stepX && tileY < stepY && blockMap[stepX + 1][stepY] > 0 && blockMap[stepX][stepY - 1] > 0
               || tileX < stepX && tileY > stepY && blockMap[stepX - 1][stepY] > 0 && blockMap[stepX][stepY + 1] > 0) {
               if (this.pathCoordinatesBlockList != null
                  && !this.pathCoordinatesBlockList.contains(OrderedPair.getOrderedPair(stepX, stepY))
                  && !this.pathCoordinatesBlockList.contains(OrderedPair.getOrderedPair(stepX - 1, stepY - 1))
                  && !this.pathCoordinatesBlockList.contains(OrderedPair.getOrderedPair(stepX + 1, stepY + 1))
                  && !this.pathCoordinatesBlockList.contains(OrderedPair.getOrderedPair(stepX + 1, stepY - 1))
                  && !this.pathCoordinatesBlockList.contains(OrderedPair.getOrderedPair(stepX - 1, stepY + 1))) {
                  this.requestPath(goalX, goalY);
               } else {
                  if (this.thisMobFlags.getAllowForcedPathfinding()) {
                     if (blockMap[stepX][stepY] > 0
                        && this.object.getBuildingAtLocation(stepX, stepY) != null
                        && this.object.getBuildingAtLocation(stepX, stepY).getObjectFlags().getMobGroup() == this.thisMob.getMobGroup()) {
                        this.requestPath(goalX, goalY);
                     } else {
                        this.pathfindBruteForce(stepX, stepY);
                     }
                  } else if (this.thisMob.canReach(goalX, goalY)) {
                     this.requestPath(goalX, goalY);
                  } else {
                     this.pathFailed = true;
                  }
               }
            } else {
               ObjectBase o = this.object.getBuildingAtLocation(stepX, stepY);
               if (o != null && o.getGate() != null && o.getGate().getX() == stepX && o.getGate().getY() == stepY && !o.getGate().isOpen()) {
                  this.thisMob.setPositionFace();
               } else {
                  this.thisMob.moveMob(stepX, stepY);
               }
            }
         }
      }
   }

   private void pathfindBruteForce(int attackX, int attackY) throws SlickException {
      if (this.thisMob.getMobGroup() == MobBase.MobGroup.MONSTER || this.thisMob.getMobGroup() == MobBase.MobGroup.CHARMED_MONSTER) {
         this.thisMob.setPositionAttack();
      } else if (this.thisMob.getInventoryToolSlot() == null && this.thisMob.getInventoryMeleeWeaponSlot() == null) {
         this.thisMob.setPositionInteract(true, false);
      } else {
         this.thisMob.setPositionInteract(true, false);
      }

      this.thisMob.setCurrentTask(Text.getText("aiTask.base.pathfindingBruteForce"));
      this.thisMob.runCombatDelayTick(1);
      if (this.thisMob.getCombatDelayTick() > this.thisMob.combatGetCalculatedAttackSpeed(false)) {
         this.thisMob.resetCombatDelayTick();
         if (this.object.isObject(attackX, attackY)) {
            ObjectBase objectBase = this.object.getWallAtLocation(attackX, attackY);
            if (objectBase == null) {
               objectBase = this.object.getBuildingAtLocation(attackX, attackY);
            }

            objectBase.takeDamage(
               this.thisMob.combatGetCalculatedRandomMeleeDamage(), this.thisMob, this.thisMobFlags.getCombatDamageType(), MobBase.DamageCause.COMBAT
            );
            this.thisMob.awardXP(Utilities.randomInt(4) + 1);
         } else if (Utilities.randomInt(8) == 0) {
            this.resource.damageResource(attackX, attackY);
            this.thisMob.awardXP(1);
         }

         if (this.thisMob.getInventoryMeleeWeaponSlot() != null) {
            this.thisMob.getInventoryMeleeWeaponSlot().decreaseUses(1);
         } else if (this.thisMob.getInventoryToolSlot() != null) {
            this.thisMob.getInventoryToolSlot().decreaseUses(2);
         }

         double xDiff = this.thisMob.getCenterPixelX(true) - attackX * 16 - 8;
         double yDiff = this.thisMob.getCenterPixelY(true) - attackY * 16 - 8;
         float angle = (float)Math.toDegrees(Math.atan2(yDiff, xDiff));
         int randomParticles = Utilities.randomInt(4) + 1;

         for (int x = 0; x < randomParticles; x++) {
            this.particle
               .newParticleGroup(
                  attackX * 16 + Utilities.randomInt(16),
                  attackY * 16 + Utilities.randomInt(16),
                  angle,
                  ParticleModule.ParticleGroup.DUST,
                  ParticleModule.ParticleSet.BLACK
               );
            this.particle
               .newParticleGroup(
                  attackX * 16 + Utilities.randomInt(8) + 2,
                  (attackY - 1) * 16 + Utilities.randomInt(8) + 2,
                  ParticleModule.ParticleGroup.SMOKE_BELLOW,
                  ParticleModule.ParticleSet.GRAY
               );
         }
      }
   }

   protected void stuckPathfind() throws SlickException {
      int stepX = this.pathCoordinatesList.get(this.pathStep).getX();
      int stepY = this.pathCoordinatesList.get(this.pathStep).getY();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      if (tileX == stepX && tileY == stepY) {
         if (this.pathStep <= 0) {
            this.pathCoordinatesList = null;
            this.pathCoordinatesBlockList = null;
            this.pathCompleted = true;
            this.thisMob.setPositionFace();
            this.thisMob.findReachMap();
            return;
         }

         this.pathStep--;
      }

      this.thisMob.setPositionWalk();
      this.thisMob.moveMob(stepX, stepY);
   }

   protected void followMob(MobBase followingMob) throws SlickException {
      if (followingMob.isAvailable() && this.thisMob.canReach(followingMob) && this.pathCoordinatesList != null) {
         int stepX = this.pathCoordinatesList.get(this.pathStep).getX();
         int stepY = this.pathCoordinatesList.get(this.pathStep).getY();
         int goalX = this.pathCoordinatesList.get(0).getX();
         int goalY = this.pathCoordinatesList.get(0).getY();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         int followingX = followingMob.getTileX();
         int followingY = followingMob.getTileY();
         if (Utilities.coordinatesWithin(tileX, tileY, followingX, followingY, 1)) {
            this.pathCoordinatesList = null;
            this.pathCoordinatesBlockList = null;
            this.pathCompleted = true;
            this.thisMob.setPositionFace();
         } else if (tileX == stepX && tileY == stepY) {
            if (this.goalCoordinates.getX() == followingX && this.goalCoordinates.getY() == followingY) {
               if (this.pathStep <= 0) {
                  this.pathCoordinatesList = null;
                  this.pathCoordinatesBlockList = null;
                  this.goalCoordinates = null;
                  this.pathCompleted = true;
                  this.thisMob.setPositionFace();
               } else {
                  this.pathStep--;
               }
            } else {
               if (this.thisMob.canReach(followingX, followingY)) {
                  this.requestPath(followingX, followingY);
               } else {
                  this.pathFailed = true;
               }
            }
         } else {
            this.thisMob.setPositionWalk();
            byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
            if (blockMap[stepX][stepY] > 0
               || tileX < stepX && tileY < stepY && blockMap[stepX - 1][stepY] > 0 && blockMap[stepX][stepY - 1] > 0
               || tileX > stepX && tileY > stepY && blockMap[stepX + 1][stepY] > 0 && blockMap[stepX][stepY + 1] > 0
               || tileX > stepX && tileY < stepY && blockMap[stepX + 1][stepY] > 0 && blockMap[stepX][stepY - 1] > 0
               || tileX < stepX && tileY > stepY && blockMap[stepX - 1][stepY] > 0 && blockMap[stepX][stepY + 1] > 0) {
               if (this.pathCoordinatesBlockList != null
                  && !this.pathCoordinatesBlockList.contains(OrderedPair.getOrderedPair(stepX, stepY))
                  && !this.pathCoordinatesBlockList.contains(OrderedPair.getOrderedPair(stepX - 1, stepY - 1))
                  && !this.pathCoordinatesBlockList.contains(OrderedPair.getOrderedPair(stepX + 1, stepY + 1))
                  && !this.pathCoordinatesBlockList.contains(OrderedPair.getOrderedPair(stepX + 1, stepY - 1))
                  && !this.pathCoordinatesBlockList.contains(OrderedPair.getOrderedPair(stepX - 1, stepY + 1))) {
                  this.requestPath(goalX, goalY);
               } else {
                  if (this.thisMobFlags.getAllowForcedPathfinding()) {
                     if (blockMap[stepX][stepY] > 0
                        && this.object.getBuildingAtLocation(stepX, stepY) != null
                        && this.object.getBuildingAtLocation(stepX, stepY).getObjectFlags().getMobGroup() == this.thisMob.getMobGroup()) {
                        this.requestPath(goalX, goalY);
                     } else {
                        this.pathfindBruteForce(stepX, stepY);
                     }
                  } else if (this.thisMob.canReach(goalX, goalY)) {
                     this.requestPath(goalX, goalY);
                  } else {
                     this.pathFailed = true;
                  }
               }
            } else {
               ObjectBase o = this.object.getBuildingAtLocation(stepX, stepY);
               if (o != null && o.getGate() != null && o.getGate().getX() == stepX && o.getGate().getY() == stepY && !o.getGate().isOpen()) {
                  this.thisMob.setPositionFace();
               } else {
                  this.thisMob.moveMob(stepX, stepY);
               }
            }
         }
      } else {
         this.pathFailed = true;
      }
   }

   public ArrayList<OrderedPair> getPathCoordinatesList() {
      return this.pathCoordinatesList;
   }

   public ArrayList<OrderedPair> getPathCoordinatesBlockList() {
      return this.pathCoordinatesBlockList;
   }

   public int getPathStep() {
      return this.pathStep;
   }

   public boolean runDropResource() throws SlickException {
      return this.runDropResource(false);
   }

   public boolean runDropResource(boolean noRoads) throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      switch (this.aiStepDropResource) {
         case 0:
            if (this.thisMob.getCarriedResourcesList().size() == 0) {
               this.aiStepDropResource = 0;
               return true;
            }

            this.thisMob.setCurrentTask(Text.getText("aiTask.base.droppingResource.findingLocation"));
            if ((!noRoads || this.road.getRoadType(tileX, tileY) != RoadModule.RoadType.NONE || this.resource.isResourcesOnGround(tileX, tileY))
               && (noRoads || this.resource.isResourcesOnGround(tileX, tileY))) {
               OrderedPair dropResourcesSearch = this.resource.findOpenSpace(tileX, tileY, this.thisMobFlags.getBlockMapGroup(), noRoads);
               if (this.thisMob.canReach(dropResourcesSearch)) {
                  this.requestPath(dropResourcesSearch);
                  this.aiStepDropResource++;
                  return false;
               }

               this.aiStepDropResource = 3;
               return false;
            }

            this.aiStepDropResource = 3;
            return false;
         case 1:
            this.thisMob.setCurrentTask(Text.getText("aiTask.base.droppingResource.pathfinding"));
            this.runPath();
            if (this.pathFailed) {
               this.aiStepDropResource = 0;
               return false;
            }

            if (this.pathCompleted) {
               this.aiStepDropResource++;
            }
            break;
         case 2:
            this.thisMob.setCurrentTask(Text.getText("aiTask.base.droppingResource.dropping"));
            if (!this.resource.isResourcesOnGround(tileX, tileY)) {
               this.aiStepDropResource = 3;
               return false;
            }

            this.aiStepDropResource = 0;
            break;
         case 3:
            if (this.resource.isResourcesOnGround(tileX, tileY)) {
               this.thisMob.getSprite().restart();
               this.thisMob.setPositionFace();
               this.aiStepDropResource = 0;
               return false;
            }

            this.thisMob.setPositionDrop();
            if (this.thisMob.getSprite().isStopped()) {
               ResourceBase r = this.thisMob.removeFirstCarriedResource();
               this.resource.setResourcesOnGround(tileX, tileY, r);
               this.thisMob.getSprite().restart();
               this.thisMob.setPositionLift();
               this.aiStepDropResource++;
               return false;
            }
            break;
         case 4:
            this.thisMob.setPositionLift();
            if (this.thisMob.getSprite().isStopped()) {
               this.thisMob.getSprite().restart();
               this.thisMob.setPositionFace();
               this.aiStepDropResource = 0;
               return false;
            }
      }

      return false;
   }

   protected boolean hasPreferredTools() {
      if (this.thisMob.getInventoryToolSlot() == null) {
         return false;
      } else {
         ArrayList<ResourceModule.ResourceType> preferredTools = this.thisMob.getMobJobType().getJob().getPreferredTools();
         return preferredTools.contains(this.thisMob.getInventoryToolSlot().getType());
      }
   }

   protected void eatFoodInInventory() throws SlickException {
      if (this.thisMob.getInventoryFoodSlot() != null && this.thisMob.getInventoryFoodSlot().getType().getTemplate().getFoodPerUse() > 0) {
         this.eatFood(this.thisMob.getInventoryFoodSlot());
      }
   }

   protected void drinkWaterInInventory() throws SlickException {
      if (this.thisMob.getInventoryWaterSlot() != null && this.thisMob.getInventoryWaterSlot().getType().getTemplate().getWaterPerUse() > 0) {
         this.drinkWater(this.thisMob.getInventoryWaterSlot());
      }
   }

   protected void eatFood(ResourceBase r) throws SlickException {
      Text.setVariableText("resourceName", r.getType().getTemplate().getName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.base.eatingFood"));
      this.thisMob.setPositionEat();
      if (!this.thisMob.isInsideBuilding()) {
         if (Utilities.randomInt(50) == 0) {
            this.particle
               .newParticle(
                  this.thisMob.getCenterPixelX(true) + Utilities.randomInt(2),
                  this.thisMob.getCenterPixelY(true) - 3,
                  90.0F,
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.BLOOD_GREEN
               );
         }

         if (Utilities.randomInt(20) == 0) {
            this.particle
               .newParticle(
                  this.thisMob.getCenterPixelX(true) + Utilities.randomInt(2),
                  this.thisMob.getCenterPixelY(true) - 3,
                  90.0F,
                  ParticleModule.ParticleType.CRUMB,
                  ParticleModule.ParticleSet.BLOOD_GREEN
               );
         }
      }

      this.thisMob.runHungerEatTimeTick(1);
      if (this.thisMob.getHungerEatTimeTick() >= this.thisMobFlags.getHungerEatTime()) {
         this.thisMob.consumeResource(r);
         this.thisMob.resetHungerEatTimeTick();
      }
   }

   protected void drinkWater(ResourceBase r) throws SlickException {
      Text.setVariableText("resourceName", r.getType().getTemplate().getName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.base.drinkingWater"));
      this.thisMob.setPositionDrinkBottle();
      if (!this.thisMob.isInsideBuilding() && Utilities.randomInt(20) == 0) {
         this.particle
            .newParticle(
               this.thisMob.getCenterPixelX(true) + Utilities.randomInt(2),
               this.thisMob.getCenterPixelY(true) - 3,
               90.0F,
               ParticleModule.ParticleType.CRUMB,
               ParticleModule.ParticleSet.WATER
            );
      }

      this.thisMob.runThirstDrinkTimeTick(1);
      if (this.thisMob.getThirstDrinkTimeTick() >= this.thisMobFlags.getThirstDrinkTime()) {
         this.thisMob.consumeResource(r);
         this.thisMob.resetThirstDrinkTimeTick();
      }
   }

   protected void repairHealthInInventory(boolean resetAI) throws SlickException {
      if (this.thisMob.getInventoryToolSlot() != null && this.thisMob.getInventoryToolSlot().getType().getTemplate().getHealthPercentPerUse() > 0) {
         this.repairHealth(this.thisMob.getInventoryToolSlot(), resetAI);
      } else if (this.thisMob.getInventoryRecoverySlot() != null
         && this.thisMob.getInventoryRecoverySlot().getType().getTemplate().getHealthPercentPerUse() > 0) {
         this.repairHealth(this.thisMob.getInventoryRecoverySlot(), resetAI);
      }
   }

   protected void repairHealth(ResourceBase r, boolean resetAI) throws SlickException {
      Text.setVariableText("resourceName", r.getType().getTemplate().getName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.base.recoveringHealth"));
      this.thisMob.setPositionInteract(false, false);
      if (!this.thisMob.isInsideBuilding()) {
         if (Utilities.randomInt(50) == 0) {
            this.particle
               .newParticle(
                  this.thisMob.getCenterPixelX(true) + Utilities.randomInt(2),
                  this.thisMob.getCenterPixelY(true) - 3,
                  90.0F,
                  ParticleModule.ParticleType.SMOKE_SPECK,
                  ParticleModule.ParticleSet.GRAY
               );
         }

         if (Utilities.randomInt(20) == 0) {
            this.particle
               .newParticle(
                  this.thisMob.getCenterPixelX(true) + Utilities.randomInt(2),
                  this.thisMob.getCenterPixelY(true) - 3,
                  90.0F,
                  ParticleModule.ParticleType.CRUMB,
                  ParticleModule.ParticleSet.GRAY
               );
         }
      }

      this.thisMob.runHealthRepairTimeTick(1);
      if (this.thisMob.getHealthRepairTimeTick() >= r.getType().getTemplate().getUseTime()) {
         this.thisMob.increaseHitPoints((int)(this.thisMob.getHitPointsMax() * (r.getType().getTemplate().getHealthPercentPerUse() / 100.0F)));
         r.decreaseUses(1);
         this.thisMob.resetHealthRepairTimeTick();
         if (resetAI) {
            this.switchNode(this.thisMob.getAIStart());
         }
      }
   }

   public AIBase.AIName getAIName() {
      return this.aiName;
   }

   public int getAIStep() {
      return this.aiStep;
   }

   public static enum AIInterrupts {
      ALL,
      DEATH,
      STUCK,
      AGGRO_MOBS,
      AGGRO_BUILDINGS,
      ENERGY,
      ON_FIRE,
      FROZEN;
   }

   public static enum AIName {
      AI_ATTEMPT_WORK,
      AI_BIRTH,
      AI_BUILD_WORK,
      AI_BUY_FROM_PROVISIONER,
      AI_BUY_LABORER,
      AI_CHAT,
      AI_CLEAROUT_RESOURCE_FROM_BUILDING,
      AI_CLEAR_TOPOGRAPHY_FROM_ROAD_WORK,
      AI_CLEAR_RESOURCES_FROM_ROAD_WORK,
      AI_CLEAR_TOPOGRAPHY_WORK,
      AI_COMBAT_BUILDINGS,
      AI_COMBAT_MOBS,
      AI_CONFUSED,
      AI_CONSTRUCTION_HARVEST_WORK,
      AI_COURIER_ARRIVE_BEHAVIOR,
      AI_COURIER_DEPART_BEHAVIOR,
      AI_COURIER_PARK_BEHAVIOR,
      AI_CRITICAL_REPAIR_WORK,
      AI_DEATH,
      AI_DELIVER_CONSTRUCTION_RESOURCE,
      AI_DELIVER_CONSTRUCTION_RESOURCE_IN_HAND,
      AI_DELIVER_COURIER_RESOURCE,
      AI_DELIVER_COURIER_RESOURCE_IN_HAND,
      AI_DELIVER_ROAD_CONSTRUCTION_RESOURCE,
      AI_DELIVER_ROAD_RESOURCE_IN_HAND,
      AI_DIG_WORK,
      AI_DISMANTLE_WORK,
      AI_EQUIP_RESOURCE,
      AI_FIND_BETTER_HOME,
      AI_FIND_FOOD,
      AI_FIND_FOOD_CHECK,
      AI_FIND_FOOD_HARVEST,
      AI_FIND_HOME,
      AI_FIND_JOB,
      AI_FIND_SPAWN,
      AI_FIND_WATER_CHECK,
      AI_FIND_WATER_FOUNTAIN,
      AI_FIND_WATER_HARVEST,
      AI_FROZEN,
      AI_GHOST_BEHAVIOR,
      AI_GO_HOME,
      AI_GOLEM_GUARD_BEHAVIOR,
      AI_GOLEM_HEAL_BEHAVIOR,
      AI_GOLEM_PATROL_BEHAVIOR,
      AI_GRABBED,
      AI_GROWTH,
      AI_RANGER_PATROL_FRONT_LINE,
      AI_RANGER_PATROL_VILLAGE,
      AI_RANGER_SUMMONED,
      AI_HARVEST_FARM_WORK,
      AI_HARVEST_WATER_WORK,
      AI_HARVEST_WORK,
      AI_HEALTH_REPAIR,
      AI_INCAPACITATED,
      AI_IN_HOME,
      AI_IN_HOME_HIDING,
      AI_LABORER_LEAVE,
      AI_LABORER_POST,
      AI_MATE_COITUS,
      AI_MATE_GO_COITUS,
      AI_MATE_GO_COITUS_OTHER,
      AI_MEDIC_WORK,
      AI_MIGRANT_DEPART_BEHAVIOR,
      AI_MIGRATION_PREPERATION_BEHAVIOR,
      AI_NOMAD_BEHAVIOR,
      AI_NOMAD_BEHAVIOR_OTHER,
      AI_ON_FIRE_PANIC,
      AI_PANIC,
      AI_PLANT_RESOURCE,
      AI_PLANT_RESOURCE_IN_HAND,
      AI_PROVISIONER_LEAVE,
      AI_PROVISIONER_POST,
      AI_QUIT_JOB,
      AI_REDISTRIBUTE_RESOURCES,
      AI_REFINE_RESOURCE,
      AI_REFINE_WATER,
      AI_REMOVE_ROAD_WORK,
      AI_REPAIR_ROAD_WORK,
      AI_REPAIR_WORK,
      AI_ROAD_WORK,
      AI_SELL_RESOURCE,
      AI_SELL_RESOURCE_IN_HAND,
      AI_SLEEP_OUTSIDE,
      AI_SPAWN_REPAIR,
      AI_START,
      AI_STORE_RESOURCE,
      AI_STORE_RESOURCE_IN_HAND,
      AI_STUCK,
      AI_TEND_FARMS_WORK,
      AI_TRADE,
      AI_VISIT_MATE,
      AI_WANDER,
      AI_WILD_DOGGO_DOMESTICATE,
      AI_WILD_DOGGO_WANDER_IN,
      AI_WILD_DOGGO_WANDER_IN_OTHER,
      AI_ATTEMPT_CORRUPTION_WORK,
      AI_CORRUPTION_BUILDING_PLACEMENT,
      AI_CORRUPTION_BUILD_WORK,
      AI_CORRUPTION_CLEAR_TOPOGRAPHY_WORK,
      AI_CORRUPTION_DELIVER_RESOURCE_IN_HAND,
      AI_CORRUPTION_DELIVER_RESOURCE_ON_GROUND,
      AI_CORRUPTION_DELIVER_ROAD_RESOURCE_IN_HAND,
      AI_CORRUPTION_DELIVER_ROAD_RESOURCE_ON_GROUND,
      AI_CORRUPTION_HARVEST_WORK,
      AI_CORRUPTION_ROAD_WORK,
      AI_HOSTILE_BEHAVIOR_ATTACK,
      AI_HOSTILE_BEHAVIOR_ATTACK_OTHER,
      AI_MONSTER_LEAVE,
      AI_PROCESS_TRASH,
      AI_MAKE_TRASHY_CUBE,
      AI_STORE_TRASH_IN_TRASH_CAN_IN_HAND,
      AI_STORE_TRASH_IN_TRASH_CAN,
      AI_CORRUPTION_CLEAR_TOPOGRAPHY_TO_BUILD_WORK,
      AI_ANIMAL_WANDER_BEHAVIOR,
      AI_CAPTURE_ANIMALS,
      AI_CAPTURE_ANIMALS_IN_HAND,
      AI_BEING_SLAUGHTERED,
      AI_SLAUGHTER_ANIMAL_IN_HAND,
      AI_SLAUGHTER_ANIMAL,
      AI_PRAY,
      AI_TERMINATE_ANIMAL_PREGNANCY,
      AI_CAPTURE_SOUL,
      AI_CAPTURE_SOUL_IN_HAND,
      AI_PLACE_VESSEL;
   }
}
