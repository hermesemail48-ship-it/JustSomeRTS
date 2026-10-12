package rtr.spawning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.TimeModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.mobs.MobFlagsBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.objects.rangebuilder.RangeBuilder;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class SpawnModule extends ModuleBase {
   private static final int SPAWN_TICK_RATE = 1000;
   private static final int MAXIMUM_ANIMALS_MULTIPLIER = 3;
   private int nextSpawnTick = 0;
   private double nextSpawnAmountRemainder = 0.0;
   private ArrayList<MobBase.MobType> allMonsterSpawnTypes = new ArrayList<>();
   private ArrayList<MobBase.MobType> validMonsterSpawnTypes = new ArrayList<>();
   private EnumMap<TimeModule.TimeOfDay, Double> monsterSpawnCountsPerTimeOfDay = new EnumMap<>(TimeModule.TimeOfDay.class);
   private ArrayList<MobBase.MobType> allAnimalSpawnTypes = new ArrayList<>();
   private ArrayList<MobBase.MobType> allAnimalSpawnWeights = new ArrayList<>();
   private int lastDaySpawnUpdate;
   private boolean nomadsArrived;
   private int lastNomadTick;
   private int baseSpawnRate;
   private int baseNomadSpawnRate;
   private double nomadSpawnAmountMultiplier;
   private int nomadSpawnAmountMinimum;
   private int nomadSpawnAmountMaximum;
   private int animalSpawnRate;
   private int corruptionStartDay;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);

      TimeModule.TimeOfDay[] var6;
      for (TimeModule.TimeOfDay t : TimeModule.TimeOfDay.values()) {
         this.monsterSpawnCountsPerTimeOfDay.put(t, 0.0);
      }

      for (MobBase.MobType t : MobBase.MobType.values()) {
         MobFlagsBase thisFlags = MobFactory.getMobFlagsInstance(t);
         if (thisFlags.canAnimalSpawn()) {
            this.allAnimalSpawnTypes.add(t);
            int weight = thisFlags.getAnimalSpawnAmountWeight();

            for (int i = 0; i < weight; i++) {
               this.allAnimalSpawnWeights.add(t);
            }
         }

         if (thisFlags.canMonsterSpawn()) {
            this.allMonsterSpawnTypes.add(t);
         }
      }

      this.requiresSync = true;
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.allMonsterSpawnTypes.clear();
      this.validMonsterSpawnTypes.clear();
      this.allAnimalSpawnTypes.clear();
      this.allAnimalSpawnWeights.clear();

      TimeModule.TimeOfDay[] var4;
      for (TimeModule.TimeOfDay t : TimeModule.TimeOfDay.values()) {
         this.monsterSpawnCountsPerTimeOfDay.put(t, 0.0);
      }

      this.lastDaySpawnUpdate = 0;
      this.lastNomadTick = 0;
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
      GameModeTemplateBase template = this.gameMode.getTemplate();
      this.baseSpawnRate = template.getBaseSpawnRate();
      this.animalSpawnRate = template.getAnimalSpawnRate();
      this.corruptionStartDay = template.getCorruptionStartDay();
      this.baseNomadSpawnRate = template.getBaseNomadSpawnRate();
      this.nomadSpawnAmountMultiplier = template.getNomadSpawnAmountMultiplier();
      this.nomadSpawnAmountMinimum = template.getNomadSpawnAmountMinimum();
      this.nomadSpawnAmountMaximum = template.getNomadSpawnAmountMaximum();
   }

   @Override
   protected void loadPlay() throws SlickException {
      this.newPlay();
      this.loadRegionalSaveData();
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
      this.nextSpawnTick = Integer.parseInt(dataMap.getOrDefault("nextSpawnTick", 0));
      this.nextSpawnAmountRemainder = Double.parseDouble(dataMap.getOrDefault("nextSpawnAmountRemainder", 0.0));
      if (dataMap.containsMap("spawnCountsPerTimeOfDay")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 2", "Using old spawnCountsPerTimeOfDay for monsterSpawnCountsPerTimeOfDay");
         YMLDataMap monsterSpawnCountsPerTimeOfDayDataMap = dataMap.getSubMap("spawnCountsPerTimeOfDay");

         TimeModule.TimeOfDay[] var6;
         for (TimeModule.TimeOfDay t : TimeModule.TimeOfDay.values()) {
            this.monsterSpawnCountsPerTimeOfDay.put(t, Double.parseDouble(monsterSpawnCountsPerTimeOfDayDataMap.getOrDefault(t.toString(), 0.0)));
         }
      }

      if (dataMap.containsMap("monsterSpawnCountsPerTimeOfDay")) {
         YMLDataMap monsterSpawnCountsPerTimeOfDayDataMap = dataMap.getSubMap("monsterSpawnCountsPerTimeOfDay");

         TimeModule.TimeOfDay[] var11;
         for (TimeModule.TimeOfDay t : TimeModule.TimeOfDay.values()) {
            this.monsterSpawnCountsPerTimeOfDay.put(t, Double.parseDouble(monsterSpawnCountsPerTimeOfDayDataMap.getOrDefault(t.toString(), 0.0)));
         }
      }

      this.lastDaySpawnUpdate = Integer.parseInt(dataMap.getOrDefault("lastDaySpawnUpdate", 0));
      this.nomadsArrived = Boolean.parseBoolean(dataMap.getOrDefault("nomadsArrived", false));
      this.lastNomadTick = Integer.parseInt(dataMap.getOrDefault("lastNomadTick", 0));
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      if (this.nextSpawnTick != 0) {
         dataMap.put("nextSpawnTick", this.nextSpawnTick);
      }

      if (this.nextSpawnAmountRemainder != 0.0) {
         dataMap.put("nextSpawnAmountRemainder", this.nextSpawnAmountRemainder);
      }

      Map<String, Object> monsterSpawnCountsPerTimeOfDayDataMap = new HashMap<>();

      TimeModule.TimeOfDay[] var6;
      for (TimeModule.TimeOfDay t : TimeModule.TimeOfDay.values()) {
         if (this.monsterSpawnCountsPerTimeOfDay.get(t) != 0.0) {
            monsterSpawnCountsPerTimeOfDayDataMap.put(t.toString(), this.monsterSpawnCountsPerTimeOfDay.get(t));
         }
      }

      if (monsterSpawnCountsPerTimeOfDayDataMap.size() > 0) {
         dataMap.put("monsterSpawnCountsPerTimeOfDay", monsterSpawnCountsPerTimeOfDayDataMap);
      }

      if (this.lastDaySpawnUpdate != 0) {
         dataMap.put("lastDaySpawnUpdate", this.lastDaySpawnUpdate);
      }

      if (this.nomadsArrived) {
         dataMap.put("nomadsArrived", this.nomadsArrived);
      }

      if (this.lastNomadTick != 0) {
         dataMap.put("lastNomadTick", this.lastNomadTick);
      }

      return dataMap;
   }

   @Override
   public void initLoadSync() {
      this.updateValidMonsterSpawns();
      this.synced = true;
   }

   public void update() throws SlickException {
      this.updateDailySpawns();
      this.monsterSpawningUpdate();
      this.animalSpawningUpdate();
      this.nomadSpawningUpdate();
   }

   private void updateDailySpawns() {
      if (this.lastDaySpawnUpdate != this.time.getDay()) {
         this.lastDaySpawnUpdate = this.time.getDay();

         TimeModule.TimeOfDay[] var4;
         for (TimeModule.TimeOfDay t : TimeModule.TimeOfDay.values()) {
            this.monsterSpawnCountsPerTimeOfDay.put(t, 0.0);
         }

         if (this.time.getDay() >= this.corruptionStartDay) {
            this.updateValidMonsterSpawns();
            double modifiedBaseSpawnRate = this.baseSpawnRate + this.baseSpawnRate * (this.corruption.getCorruptionThreatenedPercentBoost() / 100.0);
            double dayModifier = 1.0;

            for (int x = 1; x < this.time.getDay(); x++) {
               float randomModifer = (Utilities.randomFloat() - Utilities.randomFloat()) / 12.0F;
               dayModifier += 0.15 + randomModifer;
            }

            modifiedBaseSpawnRate *= dayModifier;
            if (this.time.getDay() == this.corruptionStartDay) {
               modifiedBaseSpawnRate /= 2.0;
            }

            if (this.time.isEclipseDay()) {
               this.monsterSpawnCountsPerTimeOfDay.put(TimeModule.TimeOfDay.MIDDAY, modifiedBaseSpawnRate / 2.0);
            } else {
               this.monsterSpawnCountsPerTimeOfDay.put(TimeModule.TimeOfDay.MIDDAY, 0.0);
            }

            if (this.time.isFullMoonDay()) {
               this.monsterSpawnCountsPerTimeOfDay.put(TimeModule.TimeOfDay.EVENING, 0.0);
               this.monsterSpawnCountsPerTimeOfDay.put(TimeModule.TimeOfDay.DUSK, 0.0);
               this.monsterSpawnCountsPerTimeOfDay.put(TimeModule.TimeOfDay.NIGHT, 0.0);
            } else {
               this.monsterSpawnCountsPerTimeOfDay.put(TimeModule.TimeOfDay.EVENING, 0.0);
               this.monsterSpawnCountsPerTimeOfDay.put(TimeModule.TimeOfDay.DUSK, modifiedBaseSpawnRate / 3.0);
               this.monsterSpawnCountsPerTimeOfDay.put(TimeModule.TimeOfDay.NIGHT, modifiedBaseSpawnRate);
            }
         }
      }
   }

   private void updateValidMonsterSpawns() {
      this.validMonsterSpawnTypes.clear();

      for (MobBase.MobType t : this.allMonsterSpawnTypes) {
         MobFlagsBase f = MobFactory.getMobFlagsInstance(t);
         if ((this.time.getDay() >= f.getMonsterSpawnDayDefaultMinimum() || f.getMonsterSpawnDayDefaultMinimum() == -1)
            && (this.time.getDay() <= f.getMonsterSpawnDayDefaultMaximum() || f.getMonsterSpawnDayDefaultMaximum() == -1)) {
            this.validMonsterSpawnTypes.add(t);
         }
      }
   }

   private void monsterSpawningUpdate() throws SlickException {
      TimeModule.TimeOfDay currentTimeOfDay = this.time.getTimeOfDay();
      if (this.monsterSpawnCountsPerTimeOfDay.get(currentTimeOfDay) <= 0.0) {
         this.nextSpawnTick = 0;
      } else {
         this.nextSpawnTick++;
         if (this.nextSpawnTick >= 1000) {
            double currentTimeOfDayDuration = currentTimeOfDay.getDuration(this.time.getSeason());
            double spawnsThisTick = this.monsterSpawnCountsPerTimeOfDay.get(currentTimeOfDay) / currentTimeOfDayDuration * 1000.0;
            this.nextSpawnTick = 0;
            ArrayList<ObjectBase> corruptedBuildings = this.object.getObjectsByGroup(MobBase.MobGroup.MONSTER);
            MobBase.MobType mobType = this.validMonsterSpawnTypes.get(Utilities.randomInt(this.validMonsterSpawnTypes.size()));

            for (this.nextSpawnAmountRemainder += spawnsThisTick % 1.0; this.nextSpawnAmountRemainder > 1.0; spawnsThisTick++) {
               this.nextSpawnAmountRemainder--;
            }

            for (int i = 0; i < (int)spawnsThisTick; i++) {
               OrderedPair o = null;
               if (corruptedBuildings.size() > 0 && Utilities.randomInt(4) != 0) {
                  ObjectBase spawnBuilding = corruptedBuildings.get(Utilities.randomInt(corruptedBuildings.size()));
                  int attempt = 0;

                  ObjectFlags flags;
                  for (flags = spawnBuilding.getObjectFlags();
                     attempt < 100 && (flags.getSubType() == ObjectBase.ObjectSubType.CONSTRUCTION || flags.getWidth() == 1 && flags.getHeight() == 1);
                     attempt++
                  ) {
                     spawnBuilding = corruptedBuildings.get(Utilities.randomInt(corruptedBuildings.size()));
                  }

                  if (flags.getSubType() != ObjectBase.ObjectSubType.CONSTRUCTION && (flags.getWidth() != 1 || flags.getHeight() != 1)) {
                     o = spawnBuilding.getRandomCoordinates();
                  } else {
                     o = this.corruption.getRandomCorruptionCoordinate(true);
                  }
               } else {
                  o = this.corruption.getRandomCorruptionCoordinate(true);
               }

               if (o != null) {
                  this.mob.newMob(o.getX(), o.getY(), mobType, true);
               }
            }
         }
      }
   }

   private void animalSpawningUpdate() throws SlickException {
      if (this.animalSpawnRate != 0) {
         int modifiedAnimalSpawnRate = this.animalSpawnRate;
         if (this.perk.getPerkTypeTotal(PerkModule.PerkType.ANIMAL_APPEARANCE_RATE) > 0.0) {
            modifiedAnimalSpawnRate = (int)(
               modifiedAnimalSpawnRate - modifiedAnimalSpawnRate * this.perk.getPerkTypeTotal(PerkModule.PerkType.ANIMAL_APPEARANCE_RATE)
            );
         }

         if (Utilities.randomInt(modifiedAnimalSpawnRate) == 0) {
            int waypointCount = 0;

            ResourceModule.ResourceType[] thisFlags;
            for (ResourceModule.ResourceType t : thisFlags = ResourceModule.ResourceType.values()) {
               ArrayList<OrderedPair> thisWaypoints = this.waypoint.getAnimalResourceWaypoints(t);
               if (thisWaypoints != null) {
                  waypointCount += thisWaypoints.size();
               }
            }

            int totalAnimals = this.mob.getMobsByGroup(MobBase.MobGroup.ANIMAL, false).size();
            if (totalAnimals >= waypointCount * 3) {
               return;
            }

            MobBase.MobType typeOut = this.allAnimalSpawnWeights.get(Utilities.randomInt(this.allAnimalSpawnWeights.size()));
            OrderedPair spawnCoordinates = this.getValidEdgeSpawnCoordinates(typeOut);
            MobFlagsBase thisFlagsx = MobFactory.getMobFlagsInstance(typeOut);
            int spawnAmount = 0;
            if (thisFlagsx.getAnimalSpawnAmountMinimum() == thisFlagsx.getAnimalSpawnAmountMaximum()) {
               spawnAmount = thisFlagsx.getAnimalSpawnAmountMinimum();
            } else {
               spawnAmount = Utilities.randomInt(thisFlagsx.getAnimalSpawnAmountMinimum(), thisFlagsx.getAnimalSpawnAmountMaximum());
            }

            for (int i = 0; i < spawnAmount; i++) {
               this.mob.newMob(spawnCoordinates.getX(), spawnCoordinates.getY(), typeOut, true);
            }
         }
      }
   }

   private void nomadSpawningUpdate() throws SlickException {
      if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.NIGHT && this.nomadsArrived) {
         this.nomadsArrived = false;
      }

      if (this.time.getDay() != 1) {
         if ((
               this.time.getTimeOfDay() == TimeModule.TimeOfDay.DAWN
                  || this.time.getTimeOfDay() == TimeModule.TimeOfDay.MORNING
                  || this.time.getTimeOfDay() == TimeModule.TimeOfDay.MIDDAY
            )
            && !this.nomadsArrived) {
            ArrayList<ObjectBase> villageSizeList = this.object.getObjectMasterBuildingPriorityList();
            int villageDesirability = 0;
            int totalOccupancy = 0;
            int openOccupancy = 0;
            int overPopulation = 0;
            int openJobs = 0;
            int availableFood = 0;
            int availableWater = 0;
            int defense = 0;
            int totalPopulation = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false).size();

            for (ObjectBase o : villageSizeList) {
               ObjectFlags objectFlags = o.getObjectFlags();
               if (objectFlags.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                  if (objectFlags.getBaseDesirability() > 0) {
                     villageDesirability += objectFlags.getBaseDesirability();
                  }

                  totalOccupancy += objectFlags.getOccupantsMax();
                  openOccupancy += objectFlags.getOccupantsMax() - o.getOccupants().size();
                  if (o.getTower() != null || objectFlags.isCombobulator()) {
                     defense++;
                  }
               }

               openJobs += objectFlags.getWorkersProvided();
            }

            openJobs -= totalPopulation;
            overPopulation = totalOccupancy - totalPopulation;
            if (overPopulation > 0) {
               overPopulation = 0;
            }

            ResourceModule.ResourceType[] spawnX;
            for (ResourceModule.ResourceType t : spawnX = ResourceModule.ResourceType.values()) {
               if (t.getTemplate().getFoodPerUse() != 0) {
                  availableFood += this.resource.getResourceTotal(t, true);
               }
            }

            availableFood -= totalPopulation;
            availableWater = this.resource.getTotalAvailableWater() / 20;
            if (availableWater > 2000) {
               availableWater = 2000;
            }

            if (villageDesirability > 0) {
               villageDesirability *= 100;
               if (villageDesirability > 20000) {
                  villageDesirability = 20000;
               }

               openOccupancy *= 100;
               if (openOccupancy > 10000) {
                  openOccupancy = 10000;
               }

               openJobs *= 100;
               if (openJobs > 7000) {
                  openJobs = 7000;
               } else if (openJobs < -7000) {
                  openJobs = -7000;
               }

               defense *= 100;
               if (defense > 5000) {
                  defense = 5000;
               }

               overPopulation *= 2000;
               availableFood *= 25;
               if (availableFood > 5000) {
                  availableFood = 5000;
               } else if (availableFood < -5000) {
                  availableFood = -5000;
               }

               totalPopulation *= 100;
               this.lastNomadTick++;
               int baseChance = this.baseNomadSpawnRate
                  + totalPopulation
                  - openOccupancy
                  - openJobs
                  - defense
                  - villageDesirability
                  - overPopulation
                  - availableFood
                  - availableWater
                  - this.lastNomadTick / 3;
               if (this.perk.getPerkTypeTotal(PerkModule.PerkType.NOMAD_APPEARANCE_RATE) > 0.0) {
                  baseChance = (int)(baseChance - baseChance * this.perk.getPerkTypeTotal(PerkModule.PerkType.NOMAD_APPEARANCE_RATE));
               }

               if (this.time.getDay() == 2) {
                  baseChance /= 3;
               }

               if (this.time.getDay() == 3) {
                  baseChance /= 2;
               }

               if (baseChance < 5000) {
                  baseChance = 5000;
                  if (this.time.getDay() <= 3) {
                     baseChance -= 2500;
                  }
               }

               if (Utilities.randomInt(baseChance) == 0) {
                  int randomAmount = Utilities.randomInt(3 + this.time.getDay() / 8) + Utilities.randomInt(3 + this.time.getDay() / 8) + 1;
                  randomAmount = (int)(randomAmount * this.nomadSpawnAmountMultiplier);
                  if (this.perk.getPerkTypeTotal(PerkModule.PerkType.NOMAD_AMOUNT) > 0.0) {
                     randomAmount += Utilities.randomInt(1 + (int)this.perk.getPerkTypeTotal(PerkModule.PerkType.NOMAD_AMOUNT));
                  }

                  if (randomAmount > this.nomadSpawnAmountMaximum) {
                     randomAmount = this.nomadSpawnAmountMaximum;
                  }

                  if (randomAmount < this.nomadSpawnAmountMinimum) {
                     randomAmount = this.nomadSpawnAmountMinimum;
                  }

                  OrderedPair spawnCoordinates = this.getValidEdgeSpawnCoordinates(MobBase.MobType.HUMAN_NOMAD);
                  int spawnXx = spawnCoordinates.getX();
                  int spawnY = spawnCoordinates.getY();
                  Console.newBanner(Text.getText("console.banner.spawn.nomadsArrived.header"), Text.getText("console.banner.spawn.nomadsArrived.subtitle"));
                  this.nomadsArrived = true;
                  this.sound.playSound(SoundModule.SoundType.NOMADS);
                  if (Utilities.randomInt(50) == 0) {
                     this.mob.newMob(spawnXx, spawnY, MobBase.MobType.CATJEET_NOMAD, true);
                     this.stats.increaseNomadsVisitedThisPeriod(MobBase.MobType.CATJEET_NOMAD);

                     for (int i = 0; i < randomAmount - 1; i++) {
                        if (Utilities.randomInt(5) == 0) {
                           this.mob.newMob(spawnXx, spawnY, MobBase.MobType.CATJEET_NOMAD_CHILD, true);
                           this.stats.increaseNomadsVisitedThisPeriod(MobBase.MobType.CATJEET_NOMAD_CHILD);
                        } else if (Utilities.randomInt(20) == 0) {
                           this.mob.newMob(spawnXx, spawnY, MobBase.MobType.CATJEET_NOMAD_ELDER, true);
                           this.stats.increaseNomadsVisitedThisPeriod(MobBase.MobType.CATJEET_NOMAD_ELDER);
                        } else {
                           this.mob.newMob(spawnXx, spawnY, MobBase.MobType.CATJEET_NOMAD, true);
                           this.stats.increaseNomadsVisitedThisPeriod(MobBase.MobType.CATJEET_NOMAD);
                        }
                     }
                  } else {
                     this.mob.newMob(spawnXx, spawnY, MobBase.MobType.HUMAN_NOMAD, true);
                     this.stats.increaseNomadsVisitedThisPeriod(MobBase.MobType.HUMAN_NOMAD);

                     for (int ix = 0; ix < randomAmount - 1; ix++) {
                        if (Utilities.randomInt(20) == 0) {
                           if (Utilities.randomInt(5) == 0) {
                              this.mob.newMob(spawnXx, spawnY, MobBase.MobType.CATJEET_NOMAD_CHILD, true);
                              this.stats.increaseNomadsVisitedThisPeriod(MobBase.MobType.CATJEET_NOMAD_CHILD);
                           } else if (Utilities.randomInt(20) == 0) {
                              this.mob.newMob(spawnXx, spawnY, MobBase.MobType.CATJEET_NOMAD_ELDER, true);
                              this.stats.increaseNomadsVisitedThisPeriod(MobBase.MobType.CATJEET_NOMAD_ELDER);
                           } else {
                              this.mob.newMob(spawnXx, spawnY, MobBase.MobType.CATJEET_NOMAD, true);
                              this.stats.increaseNomadsVisitedThisPeriod(MobBase.MobType.CATJEET_NOMAD);
                           }
                        } else if (Utilities.randomInt(5) == 0) {
                           this.mob.newMob(spawnXx, spawnY, MobBase.MobType.HUMAN_NOMAD_CHILD, true);
                           this.stats.increaseNomadsVisitedThisPeriod(MobBase.MobType.HUMAN_NOMAD_CHILD);
                        } else if (Utilities.randomInt(20) == 0) {
                           this.mob.newMob(spawnXx, spawnY, MobBase.MobType.HUMAN_NOMAD_ELDER, true);
                           this.stats.increaseNomadsVisitedThisPeriod(MobBase.MobType.HUMAN_NOMAD_ELDER);
                        } else {
                           this.mob.newMob(spawnXx, spawnY, MobBase.MobType.HUMAN_NOMAD, true);
                           this.stats.increaseNomadsVisitedThisPeriod(MobBase.MobType.HUMAN_NOMAD);
                        }
                     }
                  }

                  this.lastNomadTick = 0;
               }
            }
         }
      }
   }

   private OrderedPair getValidEdgeSpawnCoordinates(MobBase.MobType t) throws SlickException {
      MobBase n = MobFactory.getMobInstance(t);
      ArrayList<OrderedPair> edgeTiles = this.map.getMapData().getEdgeTiles();
      ArrayList<OrderedPair> edgeTilesShuffled = new ArrayList<>();
      edgeTilesShuffled.addAll(edgeTiles);
      Collections.shuffle(edgeTilesShuffled);
      OrderedPair spawnCoordinates = null;

      for (OrderedPair o : edgeTilesShuffled) {
         int x = o.getX();
         int y = o.getY();
         if (this.map.getMapData().getBlockMap(n.getMobFlags().getBlockMapGroup())[x][y] < 1
            && !this.object.isInRange(x, y)
            && !this.corruption.isTileCorrupted(x, y)
            && this.map.getTileId(x, y, 6) == 0
            && this.map.getTileId(x, y, 9) == 0
            && this.object.getRangeMap(RangeBuilder.RangeMapLayer.REACH_MAP)[x][y] != 0) {
            spawnCoordinates = OrderedPair.getOrderedPair(x, y);
            break;
         }
      }

      if (spawnCoordinates == null) {
         int randomX = Utilities.randomInt(256);
         int randomY = Utilities.randomInt(256);

         for (int attempts = 0;
            this.map.getMapData().getBlockMap(n.getMobFlags().getBlockMapGroup())[randomX][randomY] > 0
               || attempts < 500 && this.object.isInRange(randomX, randomY)
               || this.map.getTileId(randomX, randomY, 6) > 0
               || this.map.getTileId(randomX, randomY, 9) > 0
               || attempts < 100 && this.object.getRangeMap(RangeBuilder.RangeMapLayer.REACH_MAP)[randomX][randomY] == 0;
            attempts++
         ) {
            randomX = Utilities.randomInt(256);
            randomY = Utilities.randomInt(256);
         }

         spawnCoordinates = OrderedPair.getOrderedPair(randomX, randomY);
      }

      return spawnCoordinates;
   }

   public void increaseBaseSpawnRate(int i) {
      if (this.baseSpawnRate + i < 1000) {
         this.baseSpawnRate += i;
      } else {
         this.baseSpawnRate = 1000;
      }
   }

   public void decreaseBaseSpawnRate(int i) {
      if (this.baseSpawnRate - i > 5) {
         this.baseSpawnRate -= i;
      } else {
         this.baseSpawnRate = 5;
      }
   }

   public void setBaseSpawnRate(int i) {
      this.baseSpawnRate = i;
   }

   public int getSpawnRate() {
      return this.baseSpawnRate;
   }

   public int getSpawnDelay() {
      return this.corruptionStartDay;
   }

   public ArrayList<MobBase.MobType> getValidMonsterSpawnTypes() {
      return this.validMonsterSpawnTypes;
   }

   public ArrayList<MobBase.MobType> getAllMonsterSpawnTypes() {
      return this.allMonsterSpawnTypes;
   }

   public boolean tooManyDaytimeMobs() {
      int maximumDaytimeMobs = this.time.getDay() / 6;
      ArrayList<MobBase> monsters = this.mob.getMobsByGroup(MobBase.MobGroup.MONSTER, false);
      int count = 0;

      for (int i = monsters.size() - 1; i >= 0; i--) {
         MobBase m = monsters.get(i);
         if (!m.isBoosted() && m.getMobType() != MobBase.MobType.DRONE) {
            if (++count > maximumDaytimeMobs) {
               return true;
            }
         }
      }

      return false;
   }
}
