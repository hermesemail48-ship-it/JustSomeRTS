package rtr.spawning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.TimeModule;
import rtr.goal.GoalModule;
import rtr.gui.states.BrushLoader;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.pathfinder.PathFinder;
import rtr.objects.ObjectBase;
import rtr.objects.rangebuilder.RangeBuilder;
import rtr.particles.ParticleModule;
import rtr.resources.FarmlandBase;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class CorruptionModule extends ModuleBase {
   private static final int CORRUPTION_DESIRED_AMOUNT_MAX = 65536;
   private static final double CORRUPTION_POWER_OF_OFFSET = 2.0;
   public static final double CORRUPTION_POWER_THREAT_BOOST_MAX = 1000.0;
   private static final int FOG_SPAWN_RATE = 10000;
   private static final int CRYSTAL_SPAWN_RATE = 20000000;
   private static final int RESOURCE_KILL_RATE = 800000;
   private static final int ROAD_UPDATE_RATE = 2000;
   private CorruptionRoadPathFinder roadPath;
   private ArrayList<CorruptionFog> corruptionFog = new ArrayList<>();
   private byte[][] corruptedTiles;
   private ArrayList<OrderedPair> corruptedRoadMap;
   private ArrayList<OrderedPair> corruptedRoadMapNew;
   private ArrayList<OrderedPair> corruptedTilesArray;
   private boolean runningRoadUpdate;
   private int roadUpdateTick;
   private ArrayList<OrderedPair> validCoordinates = new ArrayList<>();
   private boolean corruptionStarted;
   private int fogSpawnRateTick;
   private int crystalSpawnRateTick;
   private int resourceKillRateTick;
   private int corruptionStartDay;
   private int corruptionDesiredAmountStart;
   private int corruptionDesiredAmountPerDay;
   private int corruptionDesiredAmountPerBuilding;
   private int corruptionDesiredAmount;
   private double corruptionThreatenedPercentBoost;
   private HashMap<MapTilesLoader.TileSet, Integer> buildingTypes;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.roadPath = new CorruptionRoadPathFinder();
      this.corruptedTiles = new byte[256][256];
      this.corruptedRoadMap = new ArrayList<>();
      this.corruptedRoadMapNew = new ArrayList<>();
      this.corruptedTilesArray = new ArrayList<>();
      GameModeTemplateBase corruptionTemplate = this.save.getActiveRegionalSave().getGameMode().getTemplate();
      this.corruptionStartDay = corruptionTemplate.getCorruptionStartDay();
      this.corruptionDesiredAmountStart = corruptionTemplate.getCorruptionDesiredAmountStart();
      this.corruptionDesiredAmountPerDay = corruptionTemplate.getCorruptionDesiredAmountPerDay();
      this.corruptionDesiredAmountPerBuilding = corruptionTemplate.getCorruptionDesiredAmountPerBuilding();
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.corruptionFog.clear();
      this.corruptedTiles = new byte[256][256];
      this.corruptedRoadMap.clear();
      this.corruptedRoadMapNew.clear();
      this.corruptedTilesArray.clear();
      this.runningRoadUpdate = false;
      this.roadUpdateTick = 0;
      this.validCoordinates.clear();
      this.corruptionStarted = false;
      this.fogSpawnRateTick = 0;
      this.crystalSpawnRateTick = 0;
      this.resourceKillRateTick = 0;
      this.corruptionStartDay = 0;
      this.corruptionDesiredAmount = this.corruptionDesiredAmountStart;
      this.corruptionThreatenedPercentBoost = 0.0;
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
      this.updateCorruptionPower();
   }

   @Override
   protected void loadPlay() throws SlickException {
      this.loadRegionalSaveData();
      this.updateCorruptionPower();
   }

   @Override
   protected void newMapEditor() throws SlickException {
   }

   @Override
   protected void loadMapEditor() throws SlickException {
   }

   @Override
   protected void newWorldMap() throws SlickException {
   }

   @Override
   protected void loadWorldMap() throws SlickException {
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(this.moduleType);
      if (dataMap.containsKey("corruptedRoadMap")) {
         this.corruptedRoadMap = dataMap.unpackCoordinatesList("corruptedRoadMap");
      }

      if (dataMap.containsKey("corruptedRoadMapNew")) {
         this.corruptedRoadMapNew = dataMap.unpackCoordinatesList("corruptedRoadMapNew");
      }

      for (int x = 0; x < this.corruptedTiles.length; x++) {
         for (int y = 0; y < this.corruptedTiles[x].length; y++) {
            if (this.map.getMap()[x][y][4] != 0) {
               this.corruptedTiles[x][y] = 1;
               this.corruptedTilesArray.add(OrderedPair.getOrderedPair(x, y));
            }
         }
      }

      this.runningRoadUpdate = Boolean.parseBoolean(dataMap.getOrDefault("runningRoadUpdate", false));
      this.roadUpdateTick = Integer.parseInt(dataMap.getOrDefault("roadUpdateTick", 0));
      if (dataMap.containsKey("validCoordinates")) {
         this.validCoordinates = dataMap.unpackCoordinatesList("validCoordinates");
      }

      this.corruptionStarted = Boolean.parseBoolean(dataMap.getOrDefault("corruptionStarted", false));
      this.fogSpawnRateTick = Integer.parseInt(dataMap.getOrDefault("fogSpawnRateTick", 0));
      this.crystalSpawnRateTick = Integer.parseInt(dataMap.getOrDefault("crystalSpawnRateTick", 0));
      this.resourceKillRateTick = Integer.parseInt(dataMap.getOrDefault("resourceKillRateTick", 0));
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      if (this.corruptedRoadMap.size() > 0) {
         dataMap.put("corruptedRoadMap", dataMap.packCoordinatesList(this.corruptedRoadMap));
      }

      if (this.corruptedRoadMapNew.size() > 0) {
         dataMap.put("corruptedRoadMapNew", dataMap.packCoordinatesList(this.corruptedRoadMapNew));
      }

      if (this.runningRoadUpdate) {
         dataMap.put("runningRoadUpdate", this.runningRoadUpdate);
      }

      if (this.roadUpdateTick != 0) {
         dataMap.put("roadUpdateTick", this.roadUpdateTick);
      }

      if (this.validCoordinates.size() > 0) {
         dataMap.put("validCoordinates", dataMap.packCoordinatesList(this.validCoordinates));
      }

      if (this.corruptionStarted) {
         dataMap.put("corruptionStarted", this.corruptionStarted);
      }

      if (this.fogSpawnRateTick != 0) {
         dataMap.put("fogSpawnRateTick", this.fogSpawnRateTick);
      }

      if (this.crystalSpawnRateTick != 0) {
         dataMap.put("crystalSpawnRateTick", this.crystalSpawnRateTick);
      }

      if (this.resourceKillRateTick != 0) {
         dataMap.put("resourceKillRateTick", this.resourceKillRateTick);
      }

      return dataMap;
   }

   public void update() throws SlickException {
      this.updateCorruptionPower();
      if (this.corruptionStartDay != 0) {
         this.corruptionNewSpotUpdate();
         this.corruptionFogUpdate();
         this.spawnDronesUpdate();
         this.corruptionTilesUpdate();
         this.roadMapUpdate();
      }
   }

   public void render(Graphics g) throws SlickException {
      CorruptionFog.getSpriteSheet().startUse();

      for (CorruptionFog c : this.corruptionFog) {
         c.render();
      }

      CorruptionFog.getSpriteSheet().endUse();
   }

   public void updateCorruptionPower() {
      this.corruptionDesiredAmount = this.corruptionDesiredAmountPerDay * (this.time.getDay() - this.corruptionStartDay - 1)
         + this.corruptionDesiredAmountStart;
      double desiredPerTimeOfDay = (double)this.corruptionDesiredAmountPerDay / TimeModule.TimeOfDay.values().length;
      double currentTimeOfDayMultiplier = 1.0;
      switch (this.time.getTimeOfDay()) {
         case NIGHT:
            currentTimeOfDayMultiplier = 6.0;
            break;
         case DAWN:
            currentTimeOfDayMultiplier = 1.0;
            break;
         case MORNING:
            currentTimeOfDayMultiplier = 2.0;
            break;
         case MIDDAY:
            currentTimeOfDayMultiplier = 3.0;
            break;
         case EVENING:
            currentTimeOfDayMultiplier = 4.0;
            break;
         case DUSK:
            currentTimeOfDayMultiplier = 5.0;
      }

      this.corruptionDesiredAmount = (int)(this.corruptionDesiredAmount + desiredPerTimeOfDay * currentTimeOfDayMultiplier);

      for (ObjectBase o : this.object.getObjectsByGroup(MobBase.MobGroup.MONSTER)) {
         if (o.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.BUILT) {
            this.corruptionDesiredAmount = this.corruptionDesiredAmount + this.corruptionDesiredAmountPerBuilding;
         }
      }

      if (this.corruptionDesiredAmount > 65536) {
         this.corruptionDesiredAmount = 65536;
      }

      double desiredOffset = this.corruptionDesiredAmount - this.corruptedTilesArray.size() - this.corruptionDesiredAmountPerDay;
      if (desiredOffset < 0.0) {
         desiredOffset = 0.0;
      }

      this.corruptionThreatenedPercentBoost = Math.pow(desiredOffset / 2000.0, 2.0) - 1.0;
      if (this.corruptionThreatenedPercentBoost > 1000.0) {
         this.corruptionThreatenedPercentBoost = 1000.0;
      }

      if (this.corruptionThreatenedPercentBoost < 0.0) {
         this.corruptionThreatenedPercentBoost = 0.0;
      }
   }

   public double getCorruptionThreatenedPercentBoost() {
      return this.corruptionThreatenedPercentBoost;
   }

   private boolean openArea(int tileX, int tileY, boolean checkBlockMap) {
      for (int x = -1; x <= 1; x++) {
         for (int y = -1; y <= 1; y++) {
            if ((x != -1 || y != -1)
               && (x != 1 || y != 1)
               && (x != -1 || y != 1)
               && (x != 1 || y != -1)
               && !Utilities.isOutOfBoundsOfUsableMap(tileX + x, tileY + y)
               && (!checkBlockMap || this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[tileX + x][tileY + y] != 1)
               && this.corruptedTiles[tileX + x][tileY + y] == 0) {
               return true;
            }
         }
      }

      return false;
   }

   public void addCorruption(OrderedPair o) throws SlickException {
      this.map.drawToMap(o.getX(), o.getY(), MapTilesLoader.TileSet.CORRUPTION, 1, BrushLoader.BrushType.SQUARE, false);
      this.corruptedTiles[o.getX()][o.getY()] = 1;
      this.corruptedTilesArray.add(o);
   }

   public void removeCorruption(OrderedPair o) throws SlickException {
      this.map.erase(o.getX(), o.getY(), MapModule.EraseMode.CORRUPTION, 1, BrushLoader.BrushType.SQUARE);
      this.corruptedTiles[o.getX()][o.getY()] = 0;
      this.corruptedTilesArray.remove(o);
      this.goal.incrementGoal(GoalModule.GoalType.TAKE_IT_BACK);
   }

   private void corruptionNewSpotUpdate() throws SlickException {
      if (this.time.getDay() >= this.corruptionStartDay && !this.corruptionStarted && this.object.getObjectMasterBuildingPriorityList().size() > 0) {
         ObjectBase villageBuilding = this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1);
         if (villageBuilding == null) {
            villageBuilding = this.object.getObjectMasterBuildingPriorityList().get(0);
         }

         OrderedPair villageCenter = villageBuilding.getRandomCoordinates();
         int failCount = 0;
         int randomX = Utilities.randomInt(256);

         int randomY;
         for (randomY = Utilities.randomInt(256);
            this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[randomX][randomY] > 0
               || this.object.isInRange(randomX, randomY)
               || this.map.getTileId(randomX, randomY, 6) > 0
               || this.map.getTileId(randomX, randomY, 9) > 0
               || Utilities.coordinatesWithin(villageCenter.getX(), villageCenter.getY(), randomX, randomY, 192 - failCount / 10)
               || this.mob.getPathfinder().getCoordinatesInRange(randomX, randomY, 64, false, MapData.BlockMapGroup.STANDARD).size() < 512 - failCount / 10;
            failCount++
         ) {
            randomX = Utilities.randomInt(256);
            randomY = Utilities.randomInt(256);
         }

         ArrayList<OrderedPair> nearbyCoordinates = Utilities.getAllCoordinatesWithin(randomX, randomY, 24);
         boolean foundWood = false;
         boolean foundRock = false;
         Iterator nearbyCoordinatesReachable = nearbyCoordinates.iterator();

         while (true) {
            if (nearbyCoordinatesReachable.hasNext()) {
               OrderedPair o = (OrderedPair)nearbyCoordinatesReachable.next();
               ResourceModule.ResourceType t = this.resource.getResourceType()[o.getX()][o.getY()];
               if (t == ResourceModule.ResourceType.WOOD) {
                  foundWood = true;
               } else if (t == ResourceModule.ResourceType.ROCK) {
                  foundRock = true;
               }

               if (!foundWood || !foundRock) {
                  continue;
               }
            }

            if (!foundWood || !foundRock) {
               return;
            }

            PathFinder path = this.mob.getPathfinder();
            ArrayList<OrderedPair> nearbyCoordinatesReachablex = path.getCoordinatesInRange(randomX, randomY, 32, false, MapData.BlockMapGroup.STANDARD);
            foundWood = false;
            foundRock = false;

            for (OrderedPair ox : nearbyCoordinatesReachablex) {
               for (int x = -1; x < 2; x++) {
                  for (int y = -1; y < 2; y++) {
                     ResourceModule.ResourceType tx = this.resource.getResourceType()[ox.getX() + x][ox.getY() + y];
                     if (tx == ResourceModule.ResourceType.WOOD) {
                        foundWood = true;
                     } else if (tx == ResourceModule.ResourceType.ROCK) {
                        foundRock = true;
                     }
                  }
               }

               if (foundWood && foundRock) {
                  break;
               }
            }

            if (foundWood && foundRock) {
               this.addCorruption(OrderedPair.getOrderedPair(randomX, randomY));
               this.corruptionStarted = true;
            }
            break;
         }
      }
   }

   private void corruptionFogUpdate() {
      for (int x = this.corruptionFog.size() - 1; x >= 0; x--) {
         this.corruptionFog.get(x).update();
         if (this.corruptionFog.get(x).getDurationTick() > CorruptionFog.getFogDuration()) {
            this.corruptionFog.remove(x);
         }
      }
   }

   private void spawnDronesUpdate() throws SlickException {
      if (this.corruptedTilesArray.size() > 0) {
         ArrayList<ObjectBase> allBuildings = this.object.getObjectsByGroup(MobBase.MobGroup.MONSTER);
         ArrayList<ObjectBase> corruptedBuildings = new ArrayList<>();

         for (ObjectBase o : allBuildings) {
            OrderedPair center = o.getCenterCoordinates();
            if (this.corruptedTiles[center.getX()][center.getY()] > 0) {
               corruptedBuildings.add(o);
            }
         }

         int totalDrones = corruptedBuildings.size() / 4 + this.corruptedTilesArray.size() / 2048 + 3;
         if (totalDrones > this.mob.getMobsByType(MobBase.MobType.DRONE, false).size()) {
            OrderedPair randomSpawnCoordinates = this.getRandomCorruptionCoordinate(true);
            if (randomSpawnCoordinates == null) {
               return;
            }

            this.mob.newMob(randomSpawnCoordinates.getX(), randomSpawnCoordinates.getY(), MobBase.MobType.DRONE, true);
         }
      }
   }

   private void corruptionTilesUpdate() throws SlickException {
      int startX = this.map.getMapRenderTileTopLeftX() - 4;
      int startY = this.map.getMapRenderTileTopLeftY() - 4;
      int endX = this.map.getMapRenderTileBottomRightX() + 4;
      int endY = this.map.getMapRenderTileBottomRightY() + 4;
      if (startX < 0) {
         startX = 0;
      }

      if (startY < 0) {
         startY = 0;
      }

      if (endX > 255) {
         endX = 255;
      }

      if (endY > 255) {
         endY = 255;
      }

      this.fogSpawnRateTick = this.fogSpawnRateTick + this.corruptedTilesArray.size();
      if (this.corruptedTilesArray.size() > 0 && this.fogSpawnRateTick > 10000) {
         this.fogSpawnRateTick = 0;
         OrderedPair o = this.corruptedTilesArray.get(Utilities.randomInt(this.corruptedTilesArray.size()));
         int posX = o.getX() * 16 + Utilities.randomInt(16);
         int posY = o.getY() * 16 + Utilities.randomInt(16);
         this.corruptionFog.add(new CorruptionFog(posX, posY));
      }

      this.crystalSpawnRateTick = this.crystalSpawnRateTick + this.corruptedTilesArray.size();
      if (this.corruptedTilesArray.size() > 0 && this.crystalSpawnRateTick > 20000000) {
         this.crystalSpawnRateTick = 0;
         OrderedPair o = this.corruptedTilesArray.get(Utilities.randomInt(this.corruptedTilesArray.size()));
         if (this.map.getTileId(o.getX(), o.getY(), 6) == 0 && this.map.getTileId(o.getX(), o.getY(), 9) == 0) {
            this.map.drawToMap(o.getX(), o.getY(), MapTilesLoader.TileSet.CRYSTAL_PURPLE, 1, BrushLoader.BrushType.SQUARE, false);
         }
      }

      this.resourceKillRateTick = this.resourceKillRateTick + this.corruptedTilesArray.size();
      if (this.corruptedTilesArray.size() > 0 && this.resourceKillRateTick > 800000) {
         this.resourceKillRateTick = 0;
         OrderedPair o = this.corruptedTilesArray.get(Utilities.randomInt(this.corruptedTilesArray.size()));
         if (this.resource.getResourceValues()[o.getX()][o.getY()] > 0) {
            this.map.killResourceTerrain(o.getX(), o.getY());
         }

         ObjectBase possibleFarm = this.object.getBuildingAtLocation(o.getX(), o.getY());
         if (possibleFarm != null && possibleFarm.getLocalFarmland().size() > 0) {
            for (FarmlandBase f : possibleFarm.getLocalFarmland()) {
               f.damageCrops();
            }
         }
      }

      ArrayList<OrderedPair> allOpenTiles = new ArrayList<>();

      for (int x = 0; x < this.corruptedTiles.length; x++) {
         for (int y = 0; y < this.corruptedTiles.length; y++) {
            if (this.corruptedTiles[x][y] == 1 && this.openArea(x, y, false)) {
               allOpenTiles.add(OrderedPair.getOrderedPair(x, y));
            }
         }
      }

      if (allOpenTiles.size() > 0) {
         boolean trySpread = false;
         boolean tryRetract = false;
         ArrayList<OrderedPair> nonThreatenedTiles = new ArrayList<>();
         nonThreatenedTiles.addAll(allOpenTiles);
         ArrayList<OrderedPair> threatenedTiles = new ArrayList<>();

         for (OrderedPair ox : allOpenTiles) {
            int tileX = ox.getX();
            int tileY = ox.getY();
            if (this.object.getRangeMap(RangeBuilder.RangeMapLayer.CORRUPTION_RESISTANCE)[tileX][tileY] != 0 && !threatenedTiles.contains(ox)) {
               threatenedTiles.add(ox);
               nonThreatenedTiles.remove(ox);
            }
         }

         int spreadChance = 30;
         if (this.corruptionDesiredAmount - this.corruptedTilesArray.size() > this.corruptionDesiredAmountPerDay * 4) {
            spreadChance /= 8;
         } else if (this.corruptionDesiredAmount - this.corruptedTilesArray.size() > this.corruptionDesiredAmountPerDay * 3) {
            spreadChance /= 6;
         } else if (this.corruptionDesiredAmount - this.corruptedTilesArray.size() > this.corruptionDesiredAmountPerDay * 2) {
            spreadChance /= 4;
         } else if (this.corruptionDesiredAmount - this.corruptedTilesArray.size() > this.corruptionDesiredAmountPerDay) {
            spreadChance /= 2;
         }

         if (nonThreatenedTiles.size() > 0 && this.corruptedTilesArray.size() < this.corruptionDesiredAmount && Utilities.randomInt(spreadChance) == 0) {
            trySpread = true;
         }

         if ((this.corruptedTilesArray.size() > this.corruptionDesiredAmount || threatenedTiles.size() > 0) && Utilities.randomInt(16) == 0) {
            int tryRetractChance = 1;
            if (this.corruptedTilesArray.size() < this.corruptionDesiredAmount) {
               tryRetractChance += Math.abs(this.corruptedTilesArray.size() - this.corruptionDesiredAmount) / 1500;
            }

            if (this.corruptedTilesArray.size() < this.corruptionDesiredAmountStart) {
               tryRetractChance *= Math.abs(this.corruptedTilesArray.size() - this.corruptionDesiredAmountStart) / 512 + 1;
            }

            if (this.corruptedTilesArray.size() < this.corruptionDesiredAmountStart / 2) {
               tryRetractChance *= Math.abs(this.corruptedTilesArray.size() - this.corruptionDesiredAmountStart) / 512 + 1;
            }

            if (Utilities.randomInt(tryRetractChance) == 0) {
               tryRetract = true;
            }
         }

         if (trySpread) {
            this.corruptionSpread(nonThreatenedTiles);
         }

         if (tryRetract) {
            this.corruptionRetract(nonThreatenedTiles, threatenedTiles);
         }
      }

      for (int x = startX; x < endX; x++) {
         for (int yx = startY; yx < endY; yx++) {
            if (this.corruptedTiles[x][yx] == 1 && Utilities.randomInt(64) == 0) {
               int posX = x * 16 + Utilities.randomInt(16);
               int posY = yx * 16 + Utilities.randomInt(16);
               if (this.openArea(x, yx, true)) {
                  for (int i = 0; i < 5; i++) {
                     this.particle.newParticle(posX, posY, ParticleModule.ParticleType.MAGIC_SMOKE_SPECK, ParticleModule.ParticleSet.FIRE_RED);
                  }
               }

               if (Utilities.randomInt(24) == 0) {
                  for (int i = 0; i < 5; i++) {
                     this.particle.newParticle(posX, posY, ParticleModule.ParticleType.MAGIC_SMOKE_SPECK, ParticleModule.ParticleSet.FIRE_RED);
                  }
               }

               if (Utilities.randomInt(1024) == 0) {
                  this.projectile.newGroundStatic(posX, posY, ParticleModule.ParticleSet.FIRE_RED);
               }

               if (Utilities.randomInt(8192) == 0) {
                  this.sound.playSound(SoundModule.SoundType.CORRUPTION, posX, posY);
               }
            }
         }
      }
   }

   private void corruptionSpread(ArrayList<OrderedPair> nonThreatenedTiles) throws SlickException {
      while (nonThreatenedTiles.size() > 0) {
         OrderedPair o = nonThreatenedTiles.get(Utilities.randomInt(nonThreatenedTiles.size()));
         int tileX = o.getX();
         int tileY = o.getY();
         int corruptionChance = 3;

         for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
               if ((x != -1 || y != -1)
                  && (x != 1 || y != 1)
                  && (x != -1 || y != 1)
                  && (x != 1 || y != -1)
                  && !Utilities.isOutOfBoundsOfUsableMap(tileX + x, tileY + y)) {
                  if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[tileX + x][tileY + y] == 0
                     && this.map.getMapData().getMovementCost(MapData.BlockMapGroup.STANDARD, tileX + x, tileY + y) <= 1000
                     && this.corruptedTiles[tileX + x][tileY + y] == 0) {
                     corruptionChance--;
                  } else if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[tileX + x][tileY + y] > 0) {
                     corruptionChance += 2;
                  } else if (this.map.getMapData().getMovementCost(MapData.BlockMapGroup.STANDARD, tileX + x, tileY + y) > 1000) {
                     corruptionChance++;
                  }
               }
            }
         }

         if (corruptionChance <= 0 || Utilities.randomInt(corruptionChance) == 0) {
            ArrayList<OrderedPair> validCoordinates = new ArrayList<>();

            for (int x = -1; x <= 1; x++) {
               for (int yx = -1; yx <= 1; yx++) {
                  if ((x != -1 || yx != -1)
                     && (x != 1 || yx != 1)
                     && (x != -1 || yx != 1)
                     && (x != 1 || yx != -1)
                     && !Utilities.isOutOfBoundsOfUsableMap(tileX + x, tileY + yx)
                     && this.corruptedTiles[tileX + x][tileY + yx] != 1
                     && this.object.getRangeMap(RangeBuilder.RangeMapLayer.CORRUPTION_RESISTANCE)[tileX + x][tileY + yx] <= 0
                     && this.map.getMap()[tileX + x][tileY + yx][0] != 0) {
                     validCoordinates.add(OrderedPair.getOrderedPair(tileX + x, tileY + yx));
                  }
               }
            }

            if (validCoordinates.size() != 0) {
               OrderedPair out = validCoordinates.get(Utilities.randomInt(validCoordinates.size()));
               this.addCorruption(out);
               break;
            }

            nonThreatenedTiles.remove(o);
         }
      }
   }

   private void corruptionRetract(ArrayList<OrderedPair> nonThreatenedTiles, ArrayList<OrderedPair> threatenedTiles) throws SlickException {
      ArrayList<OrderedPair> tilesUsed = threatenedTiles.size() > 0 ? threatenedTiles : nonThreatenedTiles;

      OrderedPair o;
      int corruptionChance;
      do {
         o = tilesUsed.get(Utilities.randomInt(tilesUsed.size()));
         int tileX = o.getX();
         int tileY = o.getY();
         corruptionChance = 20;

         for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
               if ((x != -1 || y != -1)
                  && (x != 1 || y != 1)
                  && (x != -1 || y != 1)
                  && (x != 1 || y != -1)
                  && !Utilities.isOutOfBoundsOfUsableMap(tileX + x, tileY + y)) {
                  if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[tileX + x][tileY + y] == 0
                     && this.map.getMapData().getMovementCost(MapData.BlockMapGroup.STANDARD, tileX + x, tileY + y) <= 1000
                     && this.corruptedTiles[tileX + x][tileY + y] == 0) {
                     corruptionChance = (int)(corruptionChance * 1.3F);
                  } else if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[tileX + x][tileY + y] > 0) {
                     corruptionChance = (int)(corruptionChance * 0.7F);
                  } else if (this.map.getMapData().getMovementCost(MapData.BlockMapGroup.STANDARD, tileX + x, tileY + y) > 1000) {
                     corruptionChance = (int)(corruptionChance * 1.1F);
                  }
               }
            }
         }
      } while (Utilities.randomInt(corruptionChance) != 0);

      this.removeCorruption(o);
      if ((Utilities.randomInt(corruptionChance) == 0 || this.corruptedTilesArray.size() < this.corruptionDesiredAmountStart)
         && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[o.getX()][o.getY()] == 0) {
         MobBase.MobType mobType = this.spawn.getValidMonsterSpawnTypes().get(Utilities.randomInt(this.spawn.getValidMonsterSpawnTypes().size()));
         this.mob.newMob(o.getX(), o.getY(), mobType, true);
      }
   }

   private void roadMapUpdate() throws SlickException {
      this.roadUpdateTick++;
      if (!this.runningRoadUpdate && this.roadUpdateTick > 2000) {
         ArrayList<ObjectBase> corruptedBuildings = this.object.getObjectsByGroup(MobBase.MobGroup.MONSTER);
         if (corruptedBuildings.size() <= 1) {
            return;
         }

         this.validCoordinates.clear();

         for (ObjectBase o : corruptedBuildings) {
            if (o.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.BUILT && o.hasInteractPoints()) {
               this.validCoordinates.add(o.getBestRandomInteractCoordinates(true));
            }
         }

         if (this.validCoordinates.size() == 0) {
            return;
         }

         this.corruptedRoadMapNew.clear();
         this.runningRoadUpdate = true;
      }

      if (this.runningRoadUpdate && this.roadUpdateTick % 10 == 0) {
         if (this.validCoordinates.size() > 0) {
            OrderedPair o1 = this.validCoordinates.get(0);
            this.validCoordinates.remove(0);
            OrderedPair o2 = this.mob.getPathfinder().searchFor(o1.getX(), o1.getY(), this.validCoordinates, false, true, MapData.BlockMapGroup.ROAD);
            if (o2 == null) {
               return;
            }

            ArrayList<OrderedPair> coordinates = this.roadPath.findPath(o1.getX(), o1.getY(), o2.getX(), o2.getY());
            this.corruptedRoadMapNew.addAll(coordinates);
         } else {
            this.corruptedRoadMap.clear();
            this.corruptedRoadMap.addAll(this.corruptedRoadMapNew);
            this.runningRoadUpdate = false;
            this.roadUpdateTick = 0;
         }
      }
   }

   public int calculateCorruptionPercentOnScreen() {
      int startX = this.map.getMapRenderTileTopLeftX();
      int startY = this.map.getMapRenderTileTopLeftY();
      int endX = this.map.getMapRenderTileBottomRightX();
      int endY = this.map.getMapRenderTileBottomRightY();
      if (startX < 0) {
         startX = 0;
      }

      if (startY < 0) {
         startY = 0;
      }

      if (endX > 255) {
         endX = 255;
      }

      if (endY > 255) {
         endY = 255;
      }

      double corruptedTilesCount = 0.0;
      double totalTilesCount = 0.0;

      for (int x = startX; x < endX; x++) {
         for (int y = startY; y < endY; y++) {
            if (this.corruptedTiles[x][y] == 1) {
               corruptedTilesCount++;
            }

            totalTilesCount++;
         }
      }

      return (int)Math.ceil(corruptedTilesCount / totalTilesCount * 100.0);
   }

   public OrderedPair getRandomCorruptionCoordinate(boolean tryOpen) {
      if (this.corruptedTilesArray.size() <= 0) {
         return null;
      } else {
         OrderedPair o = this.corruptedTilesArray.get(Utilities.randomInt(this.corruptedTilesArray.size()));

         for (int count = 0; tryOpen && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[o.getX()][o.getY()] > 0; count++) {
            o = this.corruptedTilesArray.get(Utilities.randomInt(this.corruptedTilesArray.size()));
            if (count > 1000) {
               break;
            }
         }

         return o;
      }
   }

   public boolean isTileCorrupted(int tileX, int tileY) {
      return this.corruptedTiles[tileX][tileY] >= 1;
   }

   public ArrayList<OrderedPair> getCorruptedRoadMapArray() {
      return this.corruptedRoadMap;
   }

   public ArrayList<OrderedPair> getCorruptionTilesArray() {
      return this.corruptedTilesArray;
   }

   public int getCorruptionDesiredAmountStart() {
      return this.corruptionDesiredAmountStart;
   }

   public int getCorruptionDesiredAmountPerDay() {
      return this.corruptionDesiredAmountPerDay;
   }

   public int getCorruptionDesiredAmountPerBuilding() {
      return this.corruptionDesiredAmountPerBuilding;
   }

   public int getCorruptionDesiredAmount() {
      return this.corruptionDesiredAmount;
   }

   public HashMap<MapTilesLoader.TileSet, Integer> getCorruptionBuildingTypes() {
      if (this.buildingTypes == null) {
         this.buildingTypes = new HashMap<>();
         this.buildingTypes.put(MapTilesLoader.TileSet.CORRUPTED_GRAVEYARD_SMALL, 5);
         this.buildingTypes.put(MapTilesLoader.TileSet.CORRUPTED_GRAVEYARD_MEDIUM, 5);
         this.buildingTypes.put(MapTilesLoader.TileSet.CORRUPTED_GRAVEYARD_LARGE, 5);
         this.buildingTypes.put(MapTilesLoader.TileSet.CORRUPTED_FIRE_BOW_TOWER, 30);
         this.buildingTypes.put(MapTilesLoader.TileSet.CORRUPTED_ICE_BOW_TOWER, 30);
         this.buildingTypes.put(MapTilesLoader.TileSet.CORRUPTED_POISON_BOW_TOWER, 30);
         this.buildingTypes.put(MapTilesLoader.TileSet.CORRUPTED_LIGHTNING_BOW_TOWER, 30);
         this.buildingTypes.put(MapTilesLoader.TileSet.CORRUPTED_FIRE_PHANTOM_DART_TOWER, 20);
         this.buildingTypes.put(MapTilesLoader.TileSet.CORRUPTED_ICE_PHANTOM_DART_TOWER, 20);
         this.buildingTypes.put(MapTilesLoader.TileSet.CORRUPTED_POISON_PHANTOM_DART_TOWER, 20);
         this.buildingTypes.put(MapTilesLoader.TileSet.CORRUPTED_LIGHTNING_PHANTOM_DART_TOWER, 20);
         this.buildingTypes.put(MapTilesLoader.TileSet.CORRUPTED_LARGE_FIRE_PIT, 60);
         this.buildingTypes.put(MapTilesLoader.TileSet.CORRUPTED_FIRE_PIT, 50);
      }

      return this.buildingTypes;
   }
}
