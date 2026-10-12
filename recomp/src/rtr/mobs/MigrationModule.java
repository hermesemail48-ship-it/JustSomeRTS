package rtr.mobs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.SoundModule;
import rtr.TimeModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.map.MapTilesLoader;
import rtr.objects.rangebuilder.RangeBuilder;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.save.RegionalSavedGame;
import rtr.save.SaveConversionUtilities;
import rtr.save.SavedGamesHandler;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class MigrationModule extends ModuleBase {
   private static final int MAXIMUM_MIGRATION_ARRIVALS_PER_DAY = 20;
   private static final int MAXIMUM_COURIER_ARRIVALS_PER_BATCH = 4;
   private static final int MAXIMUM_MIGRATION_INITIAL = 25;
   private static final int MIGRATION_ARRIVAL_CHANCE = 10000;
   public static final int MINIMUM_MIGRANTS_PER_REGION = 10;
   public static final int MIGRATION_MAX_LENGTH = 8;
   private static final int MAX_REQUESTED = 200;
   private static final int MINIMUM_VILLAGER_COUNT = 15;
   private HashMap<String, HashMap<ResourceModule.ResourceType, Integer>> currentResources = new HashMap<>();
   private HashMap<String, Integer> currentPopulation;
   private HashMap<String, Integer> requestedPopulation;
   private HashMap<String, ArrayList<MobBase>> migratingMobs;
   private HashMap<String, ArrayList<ResourceBase>> migratingMobsEquipment;
   private HashMap<String, ArrayList<MobBase>> courierGolems;
   private HashMap<String, ArrayList<ResourceBase>> courierGolemsSupplies;
   private int availableVillagers;
   private int totalMigratedToday;
   private int lastDayReset;
   private int mobID;
   private int resourceID;
   private int lastMigrationDay;
   private int initialMigrantSpawnDelay;
   private int initialCourierSpawnDelay;
   public static int RESOURCE_INFINITE_AMOUNT = 501;
   private HashMap<String, int[]> courierResourceSendAmounts = new HashMap<>();
   private HashMap<String, int[]> courierResourceDailyAmounts = new HashMap<>();
   private HashMap<String, int[]> courierResourceDailySentTodayAmounts = new HashMap<>();
   private int lastCourierDay;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.currentPopulation = new HashMap<>();
      this.requestedPopulation = new HashMap<>();
      this.migratingMobs = new HashMap<>();
      this.migratingMobsEquipment = new HashMap<>();
      this.courierGolems = new HashMap<>();
      this.courierGolemsSupplies = new HashMap<>();
      if (Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.WORLD_MAP) {
         String[] mapList = this.map.getConnectedRegionList();

         for (String s : mapList) {
            if (!s.equals("null")) {
               this.currentPopulation.put(s, 0);
               this.requestedPopulation.put(s, 0);
               this.courierResourceSendAmounts.put(s, new int[ResourceModule.ResourceType.values().length]);
               this.courierResourceDailyAmounts.put(s, new int[ResourceModule.ResourceType.values().length]);
               this.courierResourceDailySentTodayAmounts.put(s, new int[ResourceModule.ResourceType.values().length]);
               this.currentResources.put(s, new HashMap<>());

               ResourceModule.ResourceType[] var11;
               for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
                  this.courierResourceSendAmounts.get(s)[t.ordinal()] = 0;
                  this.courierResourceDailyAmounts.get(s)[t.ordinal()] = 0;
                  this.courierResourceDailySentTodayAmounts.get(s)[t.ordinal()] = 0;
                  this.currentResources.get(s).put(t, 0);
               }
            }
         }
      }

      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.currentResources.clear();
      this.currentPopulation.clear();
      this.requestedPopulation.clear();
      this.migratingMobs.clear();
      this.migratingMobsEquipment.clear();
      this.courierGolems.clear();
      this.courierGolemsSupplies.clear();
      this.availableVillagers = 0;
      this.totalMigratedToday = 0;
      this.lastDayReset = 0;
      this.mobID = 0;
      this.resourceID = 0;
      this.lastMigrationDay = 0;
      this.initialMigrantSpawnDelay = 0;
      this.initialCourierSpawnDelay = 0;
      this.courierResourceSendAmounts.clear();
      this.courierResourceDailyAmounts.clear();
      this.courierResourceDailySentTodayAmounts.clear();
      this.lastCourierDay = 0;
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
      String[] mapList = this.map.getConnectedRegionList();

      for (String s : mapList) {
         if (!s.equals("null")) {
            this.updateCurrentPopulationAndResources(s);
         }
      }

      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP && !this.save.getActiveWorldSave().isNewGame()) {
         this.loadWorldSaveData();
      }
   }

   @Override
   protected void loadPlay() throws SlickException {
      this.loadRegionalSaveData();
      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP && !this.save.getActiveWorldSave().isNewGame()) {
         this.loadWorldSaveData();
      }
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
      this.loadWorldSaveData();
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(this.moduleType);
      String[] mapList = this.map.getConnectedRegionList();

      for (String s : mapList) {
         if (!s.equals("null")) {
            this.updateCurrentPopulationAndResources(s);
            this.requestedPopulation.put(s, Integer.parseInt(dataMap.getOrDefault(s + "requestedPopulation", 0)));
         }
      }

      this.availableVillagers = Integer.parseInt(dataMap.getOrDefault("availableVillagers", 0));
      this.totalMigratedToday = Integer.parseInt(dataMap.getOrDefault("totalMigratedToday", 0));
      this.lastDayReset = Integer.parseInt(dataMap.getOrDefault("lastDayReset", 0));
      this.lastMigrationDay = Integer.parseInt(dataMap.getOrDefault("lastMigrationDay", 0));
      this.initialMigrantSpawnDelay = Integer.parseInt(dataMap.getOrDefault("initialMigrantSpawnDelay", 0));
      this.initialCourierSpawnDelay = Integer.parseInt(dataMap.getOrDefault("initialCourierSpawnDelay", 0));

      for (String sx : mapList) {
         if (!sx.equals("null")) {
            ResourceModule.ResourceType[] var10;
            for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
               this.courierResourceSendAmounts.get(sx)[t.ordinal()] = Integer.parseInt(dataMap.getOrDefault("courierResourceSendAmounts" + sx + t, 0));
               this.courierResourceDailyAmounts.get(sx)[t.ordinal()] = Integer.parseInt(dataMap.getOrDefault("courierResourceDailyAmounts" + sx + t, 0));
               this.courierResourceDailySentTodayAmounts.get(sx)[t.ordinal()] = Integer.parseInt(
                  dataMap.getOrDefault("courierResourceDailySentTodayAmounts" + sx + t, 0)
               );
            }
         }
      }

      this.lastCourierDay = Integer.parseInt(dataMap.getOrDefault("lastCourierDay", 0));

      for (String sxx : mapList) {
         if (!sxx.equals("null") && this.map.getConnectedRegionListWorldLost(sxx)) {
            ResourceModule.ResourceType[] var22;
            for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
               this.setCourierResourceDailyAmount(sxx, t, 0);
               this.setCourierResourceSendAmount(sxx, t, 0);
            }

            this.setRequestedPopulation(sxx, 0);
         }
      }
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
      this.migratingMobs.clear();
      this.migratingMobsEquipment.clear();
      YMLDataMap dataMap = this.save.getActiveWorldSave().getModuleSaveData(this.moduleType);
      YMLDataMap migrationDataMap = dataMap.getSubMap("migration");

      for (int migrationCount = 0; migrationDataMap.containsKey("mob" + migrationCount); migrationCount++) {
         YMLDataMap thisMap = migrationDataMap.getSubMap("mob" + migrationCount);
         MobBase.MobType type = null;
         String typeString = thisMap.getOrDefault("mobType", "null");
         typeString = SaveConversionUtilities.mobTypeConversion(this, typeString);

         MobBase.MobType[] mobMap;
         for (MobBase.MobType t : mobMap = MobBase.MobType.values()) {
            if (t.toString().equals(typeString)) {
               type = t;
               break;
            }
         }

         if (type != null) {
            MobBase newMob = MobFactory.getNewInstance(type);
            newMob.loadMob(thisMap);
            String region = thisMap.getOrDefault("region", "null");
            if (!this.migratingMobs.containsKey(region)) {
               this.migratingMobs.put(region, new ArrayList<>());
               this.migratingMobsEquipment.put(region, new ArrayList<>());
            }

            this.migratingMobs.get(region).add(newMob);
         }
      }

      for (int migrationResourceCount = 0; migrationDataMap.containsKey("resource" + migrationResourceCount); migrationResourceCount++) {
         YMLDataMap thisMap = migrationDataMap.getSubMap("resource" + migrationResourceCount);
         ResourceBase newResource = new ResourceBase(thisMap, true);
         String region = thisMap.getOrDefault("region", "null");
         if (!this.migratingMobs.containsKey(region)) {
            this.migratingMobs.put(region, new ArrayList<>());
            this.migratingMobsEquipment.put(region, new ArrayList<>());
         }

         this.migratingMobsEquipment.get(region).add(newResource);
      }

      YMLDataMap courierDataMap = dataMap.getSubMap("courier");

      for (int courierCount = 0; courierDataMap.containsKey("mob" + courierCount); courierCount++) {
         YMLDataMap thisMap = courierDataMap.getSubMap("mob" + courierCount);
         MobBase.MobType type = null;
         String typeString = thisMap.getOrDefault("mobType", "null");
         typeString = SaveConversionUtilities.mobTypeConversion(this, typeString);

         MobBase.MobType[] resourceList;
         for (MobBase.MobType tx : resourceList = MobBase.MobType.values()) {
            if (tx.toString().equals(typeString)) {
               type = tx;
               break;
            }
         }

         if (type != null) {
            MobBase newMob = MobFactory.getNewInstance(type);
            newMob.loadMob(thisMap);
            String region = thisMap.getOrDefault("region", "null");
            if (!this.courierGolems.containsKey(region)) {
               this.courierGolems.put(region, new ArrayList<>());
               this.courierGolemsSupplies.put(region, new ArrayList<>());
            }

            this.courierGolems.get(region).add(newMob);
         }
      }

      for (int courierResourceCount = 0; courierDataMap.containsKey("resource" + courierResourceCount); courierResourceCount++) {
         YMLDataMap thisMap = courierDataMap.getSubMap("resource" + courierResourceCount);
         ResourceBase newResource = new ResourceBase(thisMap, true);
         String region = thisMap.getOrDefault("region", "null");
         if (!this.courierGolems.containsKey(region)) {
            this.courierGolems.put(region, new ArrayList<>());
            this.courierGolemsSupplies.put(region, new ArrayList<>());
         }

         this.courierGolemsSupplies.get(region).add(newResource);
      }

      this.mobID = Integer.parseInt(dataMap.getOrDefault("mobID", 0));
      this.resourceID = Integer.parseInt(dataMap.getOrDefault("resourceID", 0));

      for (String region : this.migratingMobs.keySet()) {
         HashMap<Integer, MobBase> mobMapx = new HashMap<>();
         ArrayList<MobBase> mobList = this.migratingMobs.get(region);

         for (MobBase m : mobList) {
            mobMapx.put(m.getID(), m);
         }

         HashMap<Integer, ResourceBase> resourceMap = new HashMap<>();

         for (ResourceBase e : this.migratingMobsEquipment.get(region)) {
            resourceMap.put(e.getID(), e);
         }

         for (MobBase m : mobList) {
            m.loadSyncMigration(mobMapx, resourceMap);
         }
      }

      for (String region : this.courierGolems.keySet()) {
         HashMap<Integer, MobBase> mobMapx = new HashMap<>();
         ArrayList<MobBase> mobList = this.courierGolems.get(region);

         for (MobBase m : mobList) {
            mobMapx.put(m.getID(), m);
         }

         HashMap<Integer, ResourceBase> resourceMap = new HashMap<>();

         for (ResourceBase e : this.courierGolemsSupplies.get(region)) {
            resourceMap.put(e.getID(), e);
         }

         for (MobBase m : mobList) {
            m.loadSyncMigration(mobMapx, resourceMap);
         }
      }
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());

      for (String s : this.currentPopulation.keySet()) {
         if (this.requestedPopulation.get(s) != 0) {
            dataMap.put(s + "requestedPopulation", this.requestedPopulation.get(s));
         }
      }

      if (this.availableVillagers != 0) {
         dataMap.put("availableVillagers", this.availableVillagers);
      }

      if (this.totalMigratedToday != 0) {
         dataMap.put("totalMigratedToday", this.totalMigratedToday);
      }

      if (this.lastDayReset != 0) {
         dataMap.put("lastDayReset", this.lastDayReset);
      }

      if (this.lastMigrationDay != 0) {
         dataMap.put("lastMigrationDay", this.lastMigrationDay);
      }

      if (this.initialMigrantSpawnDelay != 0) {
         dataMap.put("initialMigrantSpawnDelay", this.initialMigrantSpawnDelay);
      }

      if (this.initialCourierSpawnDelay != 0) {
         dataMap.put("initialCourierSpawnDelay", this.initialCourierSpawnDelay);
      }

      for (String sx : this.currentPopulation.keySet()) {
         ResourceModule.ResourceType[] var7;
         for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
            if (this.courierResourceSendAmounts.get(sx)[t.ordinal()] != 0) {
               dataMap.put("courierResourceSendAmounts" + sx + t, this.courierResourceSendAmounts.get(sx)[t.ordinal()]);
            }

            if (this.courierResourceDailyAmounts.get(sx)[t.ordinal()] != 0) {
               dataMap.put("courierResourceDailyAmounts" + sx + t, this.courierResourceDailyAmounts.get(sx)[t.ordinal()]);
            }

            if (this.courierResourceDailySentTodayAmounts.get(sx)[t.ordinal()] != 0) {
               dataMap.put("courierResourceDailySentTodayAmounts" + sx + t, this.courierResourceDailySentTodayAmounts.get(sx)[t.ordinal()]);
            }
         }
      }

      if (this.lastCourierDay != 0) {
         dataMap.put("lastCourierDay", this.lastCourierDay);
      }

      return dataMap;
   }

   @Override
   public YMLDataMap getWorldMapSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      YMLDataMap migrationDataMap = new YMLDataMap();
      int mobCount = 0;
      int resourceCount = 0;

      for (String mapName : this.migratingMobs.keySet()) {
         for (MobBase m : this.migratingMobs.get(mapName)) {
            YMLDataMap thisMob = m.getRegionalSaveData();
            thisMob.put("region", mapName);
            migrationDataMap.put("mob" + mobCount, thisMob);
            mobCount++;
         }

         for (ResourceBase r : this.migratingMobsEquipment.get(mapName)) {
            YMLDataMap thisResource = r.getRegionalSaveData();
            thisResource.put("region", mapName);
            migrationDataMap.put("resource" + resourceCount, thisResource);
            resourceCount++;
         }
      }

      dataMap.put("migration", migrationDataMap);
      YMLDataMap courierDataMap = new YMLDataMap();
      mobCount = 0;
      resourceCount = 0;

      for (String mapName : this.courierGolems.keySet()) {
         for (MobBase m : this.courierGolems.get(mapName)) {
            YMLDataMap thisMob = m.getRegionalSaveData();
            thisMob.put("region", mapName);
            courierDataMap.put("mob" + mobCount, thisMob);
            mobCount++;
         }

         for (ResourceBase r : this.courierGolemsSupplies.get(mapName)) {
            YMLDataMap thisResource = r.getRegionalSaveData();
            thisResource.put("region", mapName);
            courierDataMap.put("resource" + resourceCount, thisResource);
            resourceCount++;
         }
      }

      dataMap.put("courier", courierDataMap);
      dataMap.put("mobID", this.mobID);
      dataMap.put("resourceID", this.resourceID);
      return dataMap;
   }

   @Override
   public void resetWorldSaveData() throws SlickException {
      this.resetModule();
   }

   public void update() throws SlickException {
      if (Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.WORLD_MAP
         && this.save.getActiveRegionalSave().getGameType() != StateBase.GameType.SKIRMISH) {
         this.updateStatistics();
         this.updateMigration();
      }
   }

   private void updateStatistics() {
      ArrayList<MobBase> villagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
      villagers.removeAll(this.mob.getMobsByAssignment(Assignment.AssignmentType.VILLAGER_BECOMING_MIGRANTS, false));
      this.availableVillagers = -15;

      for (MobBase m : villagers) {
         if (this.isMobQualifiedToLeave(m)) {
            this.availableVillagers++;
         }
      }

      if (this.availableVillagers < 0) {
         this.availableVillagers = 0;
      }

      if (this.time.getDay() != this.lastDayReset) {
         this.lastDayReset = this.time.getDay();
         this.totalMigratedToday = 0;
      }
   }

   private void updateMigration() throws SlickException {
      if (this.lastMigrationDay != this.time.getDay()
            && (this.time.getTimeOfDay() == TimeModule.TimeOfDay.MORNING || this.time.getTimeOfDay() == TimeModule.TimeOfDay.MIDDAY)
            && Utilities.randomInt(10000) != 0
         || this.time.getDay() == 1
            && this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1) != null
            && this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false).size() == 0
            && this.mob.getMobsByGroup(MobBase.MobGroup.MIGRANT, false).size() == 0
            && this.initialMigrantSpawnDelay++ > 300) {
         if (this.migratingMobs.get(this.map.getFileName()) != null && this.migratingMobs.get(this.map.getFileName()).size() > 0) {
            int maximumMigrantsToday = Utilities.randomInt(10) + 10;
            if (this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false).size() == 0 && this.mob.getMobsByGroup(MobBase.MobGroup.MIGRANT, false).size() == 0) {
               maximumMigrantsToday = 25;
            }

            int arrivedTotalInitialBatch = 0;

            while (arrivedTotalInitialBatch < maximumMigrantsToday && this.migratingMobs.get(this.map.getFileName()).size() > 0) {
               int arrivedMigrantsToday = 0;
               ArrayList<MobBase> mobList = this.migratingMobs.get(this.map.getFileName());
               ArrayList<MobBase> toRemove = new ArrayList<>();
               String regionName = this.migratingMobs.get(this.map.getFileName()).get(0).getAssignment().getRegionName();
               String regionFileName = this.migratingMobs.get(this.map.getFileName()).get(0).getAssignment().getRegionFileName();

               for (MobBase m : mobList) {
                  if (m.getAssignment().getRegionFileName().equals(regionFileName)) {
                     ResourceModule.ResourceSlot[] var13;
                     for (ResourceModule.ResourceSlot s : ResourceModule.ResourceSlot.values()) {
                        if (m.getResourceInInventory(s) != null) {
                           this.resource.addResourceToMasterList(m.getResourceInInventory(s));
                        }
                     }

                     for (ResourceModule.ResourceSlot sx : ResourceModule.ResourceSlot.values()) {
                        if (m.getResourceInInventory(sx) != null) {
                           this.migratingMobsEquipment.get(this.map.getFileName()).remove(m.getResourceInInventory(sx));
                        }
                     }

                     OrderedPair o = this.getMigrantArriveCoordinates(m, regionFileName);
                     m.move(o);
                     this.mob.spawnMob(m, m.getMobFlags().getMigrantDepartArrivalType(), false);
                     toRemove.add(m);
                     arrivedMigrantsToday++;
                     if (arrivedMigrantsToday >= maximumMigrantsToday || ++arrivedTotalInitialBatch >= maximumMigrantsToday) {
                        break;
                     }
                  }
               }

               mobList.removeAll(toRemove);
               this.sound.playSound(SoundModule.SoundType.NOMADS);
               Text.setVariableText("amount", arrivedMigrantsToday);
               Text.setVariableText("regionName", regionName);
               Console.newBanner(
                  Text.getText("console.banner.migration.migrantsArrived.header"), Text.getText("console.banner.migration.migrantsArrived.subtitle")
               );
               if (arrivedTotalInitialBatch >= maximumMigrantsToday) {
                  break;
               }
            }
         }

         this.lastMigrationDay = this.time.getDay();
      }

      if ((
            (this.time.getTimeOfDay() == TimeModule.TimeOfDay.MORNING || this.time.getTimeOfDay() == TimeModule.TimeOfDay.MIDDAY)
                  && Utilities.randomInt(10000) == 0
               || this.time.getDay() == 1
                  && this.object.getFirstBaseTypeFound(MapTilesLoader.TileSet.CASTLE_1) != null
                  && this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false).size() == 0
                  && this.mob.getMobsByAssignment(Assignment.AssignmentType.COURIER_ARRIVING, false).size() == 0
                  && this.initialCourierSpawnDelay++ > 400
         )
         && this.courierGolems.get(this.map.getFileName()) != null
         && this.courierGolems.get(this.map.getFileName()).size() > 0) {
         int maximumCourierGolemsThisBatch = Utilities.randomInt(2) + 2;
         if (this.courierGolems.get(this.map.getFileName()).size() > 0) {
            int arrivedCouriersToday = 0;
            ArrayList<MobBase> mobList = this.courierGolems.get(this.map.getFileName());
            ArrayList<MobBase> toRemove = new ArrayList<>();
            String regionFileName = this.courierGolems.get(this.map.getFileName()).get(0).getAssignment().getRegionFileName();
            ArrayList<MobBase> dayOneOut = new ArrayList<>();
            if (this.time.getDay() == 1) {
               for (MobBase mx : mobList) {
                  if (mx.getAssignment().getRegionFileName().equals(regionFileName)
                     && mx.getCarriedResourcesListType() != null
                     && (
                        mx.getCarriedResourcesListType() == ResourceModule.ResourceType.WOOD
                           || mx.getCarriedResourcesListType() == ResourceModule.ResourceType.ROCK
                     )) {
                     dayOneOut.add(mx);
                  }
               }
            }

            ArrayList<MobBase> finalOut = null;
            if (dayOneOut.size() > 0) {
               finalOut = dayOneOut;
            } else {
               finalOut = mobList;
            }

            for (MobBase mxx : finalOut) {
               if (mxx.getAssignment().getRegionFileName().equals(regionFileName)) {
                  OrderedPair o = this.getMigrantArriveCoordinates(mxx, regionFileName);
                  this.mob.addMobAndEquipmentToMasterList(mxx);
                  mxx.move(o);
                  mxx.setActiveNode(mxx.getAICourierArriveBehavior());
                  toRemove.add(mxx);

                  ResourceModule.ResourceSlot[] var14;
                  for (ResourceModule.ResourceSlot sxx : ResourceModule.ResourceSlot.values()) {
                     if (mxx.getResourceInInventory(sxx) != null) {
                        this.courierGolemsSupplies.get(this.map.getFileName()).remove(mxx.getResourceInInventory(sxx));
                     }
                  }

                  for (ResourceBase r : mxx.getCarriedResourcesList()) {
                     this.courierGolemsSupplies.get(this.map.getFileName()).remove(r);
                  }

                  this.goal.incrementGoal(GoalModule.GoalType.IVE_GOT_PRIME, mxx.getCarriedResourcesList().size());
                  if (++arrivedCouriersToday >= maximumCourierGolemsThisBatch) {
                     break;
                  }
               }
            }

            mobList.removeAll(toRemove);
         }
      }

      if (this.lastCourierDay != this.time.getDay()) {
         String[] mapList = this.map.getConnectedRegionList();

         for (String sxxx : mapList) {
            if (!sxxx.equals("null")) {
               ResourceModule.ResourceType[] var35;
               for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
                  this.courierResourceDailySentTodayAmounts.get(sxxx)[t.ordinal()] = 0;
               }
            }
         }

         this.lastCourierDay = this.time.getDay();
      }
   }

   public int getCourierResourceSendAmount(String s, ResourceModule.ResourceType t) {
      return this.courierResourceSendAmounts.get(s)[t.ordinal()];
   }

   public int getTotalCourierResourceSendAmount(ResourceModule.ResourceType t) {
      int amount = 0;

      for (int[] i : this.courierResourceSendAmounts.values()) {
         amount += i[t.ordinal()];
      }

      return amount;
   }

   public void setCourierResourceSendAmount(String s, ResourceModule.ResourceType t, int amount) {
      this.courierResourceSendAmounts.get(s)[t.ordinal()] = amount;
   }

   public void increaseCourierResourceSendAmount(String s, ResourceModule.ResourceType t, int amount) {
      this.courierResourceSendAmounts.get(s)[t.ordinal()] += amount;
      if (this.courierResourceSendAmounts.get(s)[t.ordinal()] > RESOURCE_INFINITE_AMOUNT) {
         this.courierResourceSendAmounts.get(s)[t.ordinal()] = RESOURCE_INFINITE_AMOUNT;
      }
   }

   public void decreaseCourierResourceSendAmount(String s, ResourceModule.ResourceType t, int amount) {
      this.courierResourceSendAmounts.get(s)[t.ordinal()] -= amount;
      if (this.courierResourceSendAmounts.get(s)[t.ordinal()] < 0) {
         this.courierResourceSendAmounts.get(s)[t.ordinal()] = 0;
      }
   }

   public int getCourierResourceDailyAmount(String s, ResourceModule.ResourceType t) {
      return this.courierResourceDailyAmounts.get(s)[t.ordinal()];
   }

   public int getTotalCourierResourceDailyAmount(ResourceModule.ResourceType t) {
      int amount = 0;

      for (int[] i : this.courierResourceDailyAmounts.values()) {
         amount += i[t.ordinal()];
      }

      return amount;
   }

   public void setCourierResourceDailyAmount(String s, ResourceModule.ResourceType t, int amount) {
      this.courierResourceDailyAmounts.get(s)[t.ordinal()] = amount;
   }

   public int getCourierResourceDailySentTodayAmounts(String s, ResourceModule.ResourceType t) {
      return this.courierResourceDailySentTodayAmounts.get(s)[t.ordinal()];
   }

   public boolean canCourierSendMore(MobBase thisCourier) {
      if (thisCourier.getAssignment().getAssignmentType() != Assignment.AssignmentType.COURIER_PARKED) {
         return false;
      } else if (!thisCourier.canCarryMore()) {
         return false;
      } else {
         ResourceModule.ResourceType type = thisCourier.getCarriedResourcesListType();
         ArrayList<MobBase> workersEnRoute = null;
         int enrouteToThisCourierAmount = 0;
         workersEnRoute = this.mob.getMobsByAssignment(Assignment.AssignmentType.COURIER_SUPPLIER_DELIVERING, false);

         for (MobBase m : workersEnRoute) {
            if (m.getAssignment().getPrimaryMob() == thisCourier) {
               enrouteToThisCourierAmount = m.getCarriedResourcesList().size();
               if (type == null) {
                  if (m.getAssignment().getPickupResourceType() != null) {
                     type = m.getAssignment().getPickupResourceType();
                  } else if (m.getCarriedResourcesListType() != null) {
                     type = m.getCarriedResourcesListType();
                  }
               }
               break;
            }
         }

         if (thisCourier.getCarriedResourcesList().size() + enrouteToThisCourierAmount >= thisCourier.getCarryMax()) {
            return false;
         } else if (type == null) {
            return true;
         } else {
            String mapName = thisCourier.getAssignment().getRegionFileName();
            if (mapName.equals("")) {
               for (MobBase mx : workersEnRoute) {
                  if (mx.getAssignment().getPrimaryMob() == thisCourier) {
                     mapName = mx.getAssignment().getRegionFileName();
                     break;
                  }
               }
            }

            return this.getDailySendAmountRemaining(mapName, type) > 0;
         }
      }
   }

   public int getDailySendAmountRemaining(String mapName, ResourceModule.ResourceType type) {
      int enrouteAmount = this.getCouriersEnrouteAmount(mapName, type);
      int sendAmountTotal = this.courierResourceSendAmounts.get(mapName)[type.ordinal()];
      int dailyAmountTotal = this.courierResourceDailyAmounts.get(mapName)[type.ordinal()];
      int dailySentAmountTotal = this.courierResourceDailySentTodayAmounts.get(mapName)[type.ordinal()];
      return dailyAmountTotal - dailySentAmountTotal + sendAmountTotal - enrouteAmount;
   }

   public int getCouriersEnrouteAmount(String mapName, ResourceModule.ResourceType type) {
      int enrouteAmount = 0;
      ArrayList<MobBase> couriers = this.mob.getMobsByAssignment(Assignment.AssignmentType.COURIER_PARKED, false);
      couriers.addAll(this.mob.getMobsByAssignment(Assignment.AssignmentType.COURIER_DEPARTING, false));

      for (MobBase m : couriers) {
         if (!m.getAssignment().getRegionFileName().equals("")
            && m.getAssignment().getRegionFileName().equals(mapName)
            && m.getCarriedResourcesListType() == type) {
            enrouteAmount += m.getCarriedResourcesList().size();
         }
      }

      for (MobBase mx : this.mob.getMobsByAssignment(Assignment.AssignmentType.COURIER_SUPPLIER_DELIVERING, false)) {
         if (mx.getAssignment() != null
            && mx.getAssignment().getRegionFileName().equals(mapName)
            && (mx.getAssignment().getPickupResourceType() == type || mx.getCarriedResourcesListType() == type)) {
            ArrayList<ResourceBase> pickupResources = mx.getAssignment().getPickupResources();
            if (pickupResources.size() != 0 || mx.hasCarriedResources()) {
               if (pickupResources.size() > 0 && (type == null || pickupResources.get(0).getType() == type)) {
                  enrouteAmount += pickupResources.size();
               }

               if (mx.hasCarriedResources() && (type == null || mx.getCarriedResourcesListType() == type)) {
                  enrouteAmount += mx.getCarriedResourcesList().size();
               }
            }
         }
      }

      return enrouteAmount;
   }

   public void increaseCourierResourceDailyAmount(String s, ResourceModule.ResourceType t, int amount) {
      this.courierResourceDailyAmounts.get(s)[t.ordinal()] += amount;
      if (this.courierResourceDailyAmounts.get(s)[t.ordinal()] > RESOURCE_INFINITE_AMOUNT) {
         this.courierResourceDailyAmounts.get(s)[t.ordinal()] = RESOURCE_INFINITE_AMOUNT;
      }
   }

   public void decreaseCourierResourceDailyAmount(String s, ResourceModule.ResourceType t, int amount) {
      this.courierResourceDailyAmounts.get(s)[t.ordinal()] -= amount;
      if (this.courierResourceDailyAmounts.get(s)[t.ordinal()] < 0) {
         this.courierResourceDailyAmounts.get(s)[t.ordinal()] = 0;
      }
   }

   public void storeMigratingMob(String regionName, MobBase m) throws SlickException {
      if (!this.migratingMobs.containsKey(regionName)) {
         this.migratingMobs.put(regionName, new ArrayList<>());
         this.migratingMobsEquipment.put(regionName, new ArrayList<>());
      }

      this.mob.deleteMob(m);

      ResourceModule.ResourceSlot[] var6;
      for (ResourceModule.ResourceSlot slot : ResourceModule.ResourceSlot.values()) {
         ResourceBase r = m.getResourceInInventory(slot);
         if (r != null) {
            this.resource.prepareForMigrationModule(r);
            r.setID(this.getNextResourceID());
            this.migratingMobsEquipment.get(regionName).add(r);
         }
      }

      m.prepareForMigrationModule();
      m.setID(this.getNextMobID());
      m.getAssignment().setRegionName(this.map.getMapName());
      m.getAssignment().setRegionFileName(this.map.getFileName());
      this.migratingMobs.get(regionName).add(m);
   }

   public void storeCourierGolem(String regionName, MobBase m) throws SlickException {
      if (!this.courierGolems.containsKey(regionName)) {
         this.courierGolems.put(regionName, new ArrayList<>());
         this.courierGolemsSupplies.put(regionName, new ArrayList<>());
      }

      this.mob.deleteMob(m);

      ResourceModule.ResourceSlot[] var6;
      for (ResourceModule.ResourceSlot slot : ResourceModule.ResourceSlot.values()) {
         ResourceBase r = m.getResourceInInventory(slot);
         if (r != null) {
            this.resource.prepareForMigrationModule(r);
            r.setID(this.getNextResourceID());
            this.courierGolemsSupplies.get(regionName).add(r);
            if (this.courierResourceDailySentTodayAmounts.get(regionName)[r.getType().ordinal()]
               >= this.courierResourceDailyAmounts.get(regionName)[r.getType().ordinal()]) {
               this.migration.decreaseCourierResourceSendAmount(regionName, m.getCarriedResourcesListType(), 1);
            } else {
               this.courierResourceDailySentTodayAmounts.get(regionName)[r.getType().ordinal()]++;
            }
         }
      }

      for (ResourceBase r : m.getCarriedResourcesList()) {
         this.resource.prepareForMigrationModule(r);
         r.setID(this.getNextResourceID());
         this.courierGolemsSupplies.get(regionName).add(r);
         if (this.courierResourceDailySentTodayAmounts.get(regionName)[r.getType().ordinal()]
            >= this.courierResourceDailyAmounts.get(regionName)[r.getType().ordinal()]) {
            this.migration.decreaseCourierResourceSendAmount(regionName, m.getCarriedResourcesListType(), 1);
         } else {
            this.courierResourceDailySentTodayAmounts.get(regionName)[r.getType().ordinal()]++;
         }
      }

      m.prepareForMigrationModule();
      m.setID(this.getNextMobID());
      m.getAssignment().setRegionName(this.map.getMapName());
      m.getAssignment().setRegionFileName(this.map.getFileName());
      this.courierGolems.get(regionName).add(m);
   }

   public void updateCurrentPopulationAndResources(String mapName) throws SlickException {
      RegionalSavedGame saveCheck = SavedGamesHandler.getRegionalSave(
         this.save.getActiveRegionalSave().getMapPackFolderName(), mapName, StateBase.GameType.WORLD_MAP, this.save.getActiveRegionalSave().getGameMode()
      );
      if (saveCheck != null) {
         YMLDataMap mobDataMap = saveCheck.getAndUnloadModuleSaveData(ModuleBase.ModuleType.MOB);
         int mobCount = 0;

         int totalMobCount;
         for (totalMobCount = 0; mobDataMap.containsKey("mob" + mobCount); mobCount++) {
            YMLDataMap thisMap = mobDataMap.getSubMap("mob" + mobCount);
            if (thisMap.getOrDefault("mobType", "null").equals(MobBase.MobGroup.VILLAGER.toString()) && thisMap.getOrDefault("dead", "false").equals("false")) {
               totalMobCount++;
            }
         }

         this.currentPopulation.put(mapName, totalMobCount);
         YMLDataMap resourceDataMap = saveCheck.getAndUnloadModuleSaveData(ModuleBase.ModuleType.RESOURCE);

         for (int resourceCount = 0; resourceDataMap.containsKey("resource" + resourceCount); resourceCount++) {
            YMLDataMap thisMap = resourceDataMap.getSubMap("resource" + resourceCount);
            ResourceBase r = new ResourceBase(thisMap, true);
            if (r.isInBuilding() || r.isCarried() || r.isInInventory() || r.isOnGround()) {
               this.currentResources.get(mapName).put(r.getType(), this.currentResources.get(mapName).get(r.getType()) + 1);
            }
         }
      }
   }

   public int getNextMobID() {
      return this.mobID++;
   }

   public int getNextResourceID() {
      return this.resourceID++;
   }

   private OrderedPair getMigrantArriveCoordinates(MobBase m, String mapName) throws SlickException {
      int migratingX = this.map.getConnectedRegionListWorldMapX(mapName);
      int migratingY = this.map.getConnectedRegionListWorldMapY(mapName);
      int thisX = this.map.getWorldMapX();
      int thisY = this.map.getWorldMapY();
      MobFlagsBase f = m.getMobFlags();
      OrderedPair angleCoordinate = null;
      float angle = Utilities.calculateAngle(thisX, thisY, migratingX, migratingY);
      float checkX = 128.0F;
      float checkY = 128.0F;
      double cos = Math.cos(angle);

      for (double sin = Math.sin(angle);
         !Utilities.isOutOfBoundsOfUsableMap((int)checkX, (int)checkY) && this.map.getTileId((int)checkX, (int)checkY, 0) != 0;
         checkY = (float)(checkY + sin)
      ) {
         angleCoordinate = OrderedPair.getOrderedPair((int)checkX, (int)checkY);
         checkX = (float)(checkX + cos);
      }

      ArrayList<OrderedPair> validCoordinatesWithoutTopography = new ArrayList<>();
      ArrayList<OrderedPair> validCoordinatesWithTopography = new ArrayList<>();

      for (OrderedPair o : this.map.getMapData().getEdgeTiles()) {
         int x = o.getX();
         int y = o.getY();
         if (this.map.getMapData().getBlockMap(f.getBlockMapGroup())[x][y] < 1
            && this.object.getRangeMap(RangeBuilder.RangeMapLayer.REACH_MAP)[x][y] != 0
            && !this.corruption.isTileCorrupted(x, y)) {
            if (this.map.getTileId(x, y, 6) == 0 || this.map.getTileId(x, y, 9) == 0) {
               validCoordinatesWithoutTopography.add(OrderedPair.getOrderedPair(x, y));
            }

            validCoordinatesWithTopography.add(OrderedPair.getOrderedPair(x, y));
         }
      }

      ArrayList<OrderedPair> finalCoordinates = null;
      if (validCoordinatesWithoutTopography.size() == 0) {
         finalCoordinates = validCoordinatesWithTopography;
      } else {
         finalCoordinates = validCoordinatesWithoutTopography;
      }

      final OrderedPair angleTarget = angleCoordinate;
      if (finalCoordinates.size() > 0 && angleCoordinate != null) {
         System.out.println(finalCoordinates.size());
         Collections.sort(finalCoordinates, new Comparator<OrderedPair>() {
            public int compare(OrderedPair o1, OrderedPair o2) {
               if (o1 == o2) {
                  return 0;
               } else {
                  int dx1 = angleTarget.getX() - o1.getX();
                  int dy1 = angleTarget.getY() - o1.getY();
                  int hValue1 = (int)Math.sqrt(dx1 * dx1 + dy1 * dy1);
                  int dx2 = angleTarget.getX() - o2.getX();
                  int dy2 = angleTarget.getY() - o2.getY();
                  int hValue2 = (int)Math.sqrt(dx2 * dx2 + dy2 * dy2);
                  return hValue1 - hValue2;
               }
            }
         });
         int randomVariance = 16;
         if (finalCoordinates.size() < randomVariance) {
            randomVariance = finalCoordinates.size();
         }

         return finalCoordinates.get(Utilities.randomInt(randomVariance));
      } else {
         return this.getMigrantRandomArriveCoordinatesCoordinates();
      }
   }

   private OrderedPair getMigrantRandomArriveCoordinatesCoordinates() throws SlickException {
      MobBase n = MobFactory.getMobInstance(MobBase.MobType.HUMAN_MIGRANT_DEPARTING);
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

   public Integer getTotalCurrentResourcesCount(String s) {
      int amount = 0;

      ResourceModule.ResourceType[] var6;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         amount += this.currentResources.get(s).get(t);
      }

      return amount;
   }

   public int getTotalEnrouteResourcesCount(String s) {
      int amount = 0;
      ArrayList<ResourceBase> courierGolemsSuppliesByMap = this.courierGolemsSupplies.get(s);
      if (courierGolemsSuppliesByMap == null) {
         return amount;
      } else {
         ResourceModule.ResourceType[] var7;
         for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
            for (ResourceBase r : courierGolemsSuppliesByMap) {
               if (r.getType() == t) {
                  amount++;
               }
            }
         }

         return amount;
      }
   }

   public Integer getCurrentResourcesCount(String s, ResourceModule.ResourceType t) {
      return this.currentResources.get(s).get(t);
   }

   public int getEnrouteResourcesCount(String s, ResourceModule.ResourceType t) {
      int amount = 0;
      ArrayList<ResourceBase> courierGolemsSuppliesByMap = this.courierGolemsSupplies.get(s);
      if (courierGolemsSuppliesByMap == null) {
         return amount;
      } else {
         for (ResourceBase r : courierGolemsSuppliesByMap) {
            if (r.getType() == t) {
               amount++;
            }
         }

         return amount;
      }
   }

   public int getEnrouteResourcesCount(String s) {
      return this.courierGolemsSupplies.get(s) != null ? this.courierGolemsSupplies.get(s).size() : 0;
   }

   public HashMap<String, Integer> getCurrentPopulation() {
      return this.currentPopulation;
   }

   public int getEnroutePopulation(String s) {
      return this.migratingMobs.get(s) != null ? this.migratingMobs.get(s).size() : 0;
   }

   public HashMap<String, ArrayList<MobBase>> getAllEnroutePopulation() {
      return this.migratingMobs;
   }

   public HashMap<String, Integer> getRequestedPopulation() {
      return this.requestedPopulation;
   }

   public int getRequestedPopulation(String s) {
      return this.requestedPopulation.get(s);
   }

   public int getTotalRequestedPopulation() {
      String[] mapList = this.map.getConnectedRegionList();
      int count = 0;

      for (String s : mapList) {
         if (!s.equals("null")) {
            count += this.getRequestedPopulation(s);
         }
      }

      return count;
   }

   public boolean canMobPrepareToMigrate() {
      return this.availableVillagers <= 0 ? false : this.canMobMigrate();
   }

   public boolean canMobMigrate() {
      if (this.totalMigratedToday >= this.object.getMaximumMigrationsPerDay()) {
         return false;
      } else {
         for (String s : this.requestedPopulation.keySet()) {
            if (this.requestedPopulation.get(s) > 0) {
               return true;
            }
         }

         return false;
      }
   }

   public int getAvailableVillagers() {
      return this.availableVillagers;
   }

   public int getTotalMigratedToday() {
      return this.totalMigratedToday;
   }

   public void incrementTotalMigratedToday() {
      this.totalMigratedToday++;
   }

   public void increaseRequestedPopulation(String mapName, int i) {
      this.requestedPopulation.put(mapName, this.requestedPopulation.get(mapName) + i);
      if (this.requestedPopulation.get(mapName) > 200) {
         this.requestedPopulation.put(mapName, 200);
      }
   }

   public void decreaseRequestedPopulation(String mapName, int i) {
      this.requestedPopulation.put(mapName, this.requestedPopulation.get(mapName) - i);
      if (this.requestedPopulation.get(mapName) < 0) {
         this.requestedPopulation.put(mapName, 0);
      }
   }

   public void setRequestedPopulation(String mapName, int i) {
      this.requestedPopulation.put(mapName, i);
      if (this.requestedPopulation.get(mapName) > 200) {
         this.requestedPopulation.put(mapName, 200);
      }

      if (this.requestedPopulation.get(mapName) < 0) {
         this.requestedPopulation.put(mapName, 0);
      }
   }

   public boolean isMobQualifiedToLeave(MobBase m) {
      MobFlagsBase f = m.getMobFlags();
      if (!f.getAllowMigrationPreperationBehavior()) {
         return false;
      } else if (m.isCompromised()) {
         return false;
      } else {
         if (f.getAllowHunger()) {
            int foodInInventory = 0;
            if (m.getInventoryFoodSlot() != null) {
               foodInInventory += m.getInventoryFoodSlot().getUses() * m.getInventoryFoodSlot().getType().getTemplate().getFoodPerUse();
            }

            if (m.getHunger() + foodInInventory < m.getHungerMax() * (f.getMigrationPreperationHungerPercent() / 100.0)) {
               return false;
            }
         }

         if (f.getAllowThirst()) {
            int waterInInventory = 0;
            if (m.getInventoryWaterSlot() != null) {
               waterInInventory += m.getInventoryWaterSlot().getUses() * m.getInventoryWaterSlot().getType().getTemplate().getWaterPerUse();
            }

            if (m.getThirst() + waterInInventory < m.getThirstMax() * (f.getMigrationPreperationThirstPercent() / 100.0)) {
               return false;
            }
         }

         if (f.getAllowAging() && m.getAge() > f.getAgingElderAge() * (f.getMigrationPreperationAgePercent() / 100.0)) {
            return false;
         } else {
            if (f.getAllowThermal()) {
               if (f.getCanThermalOverheat() && m.getBodyTemp() >= f.getThermalOverheat()) {
                  return false;
               }

               if (f.getCanThermalFreeze() && m.getBodyTemp() <= f.getThermalFreeze()) {
                  return false;
               }
            }

            return !f.getAllowSleep() || m.getEnergy() >= f.getSleepHomeEnergyLevel();
         }
      }
   }
}
