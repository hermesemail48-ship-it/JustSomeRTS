package rtr.gui.states;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.mobs.MobBase;
import rtr.mobs.friendly.human.HumanVillagerAdult;
import rtr.resources.ResourceBase;
import rtr.save.RegionalSavedGame;
import rtr.save.SavedGamesHandler;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class WorldMapGUIData {
   private static GameModeTemplateBase.GameMode selectedGameMode;
   private static ArrayList<MobBase> fullVillagerList = new ArrayList<>();
   private static ArrayList<MobBase> fullMigrantList = new ArrayList<>();
   private static ArrayList<MobBase> fullMonsterList = new ArrayList<>();
   private static ArrayList<ResourceBase> fullResourcesList = new ArrayList<>();
   private static HashMap<RegionalSavedGame, ArrayList<MobBase>> villagerList = new HashMap<>();
   private static HashMap<RegionalSavedGame, ArrayList<MobBase>> migrantList = new HashMap<>();
   private static HashMap<RegionalSavedGame, ArrayList<MobBase>> monsterList = new HashMap<>();
   private static HashMap<RegionalSavedGame, Integer> objectCount = new HashMap<>();
   private static HashMap<RegionalSavedGame, Integer> campType = new HashMap<>();
   private static HashMap<RegionalSavedGame, ArrayList<ResourceBase>> resourcesList = new HashMap<>();
   private static HashMap<RegionalSavedGame, Integer> currentDay = new HashMap<>();
   private static HashMap<RegionalSavedGame, Boolean> mapLost = new HashMap<>();
   private static HashMap<RegionalSavedGame, Boolean> mapStarted = new HashMap<>();
   private static boolean newGame;
   private static boolean hasCustomConfig;

   public static ArrayList<MobBase> getFullVillagerList() {
      return fullVillagerList;
   }

   public static ArrayList<MobBase> getFullMigrantList() {
      return fullMigrantList;
   }

   public static ArrayList<MobBase> getFullMonsterList() {
      return fullMonsterList;
   }

   public static ArrayList<ResourceBase> getFullResourcesList() {
      return fullResourcesList;
   }

   public static ArrayList<MobBase> getVillagerList(RegionalSavedGame s) {
      return villagerList.get(s);
   }

   public static ArrayList<MobBase> getMigrantList(RegionalSavedGame s) {
      return migrantList.get(s);
   }

   public static HashMap<RegionalSavedGame, ArrayList<MobBase>> getAllMigrantLists() {
      return migrantList;
   }

   public static ArrayList<MobBase> getMonsterList(RegionalSavedGame s) {
      return monsterList.get(s);
   }

   public static ArrayList<ResourceBase> getResourcesList(RegionalSavedGame s) {
      return resourcesList.get(s);
   }

   public static boolean isNewGame() {
      return newGame;
   }

   public static int getObjectCount(RegionalSavedGame s) {
      return objectCount.get(s);
   }

   public static int getCampType(RegionalSavedGame s) {
      return campType.containsKey(s) ? campType.get(s) : -1;
   }

   public static int getCurrentDay(RegionalSavedGame s) {
      return currentDay.containsKey(s) ? currentDay.get(s) : -1;
   }

   public static boolean isMapLost(RegionalSavedGame s) {
      return mapLost.containsKey(s) ? mapLost.get(s) : false;
   }

   public static boolean isMapStarted(RegionalSavedGame s) {
      return mapStarted.containsKey(s) ? mapStarted.get(s) : false;
   }

   public static void refreshData() throws SlickException {
      selectedGameMode = SettingsParser.getLastGameMode();
      if (selectedGameMode != null) {
         final HashMap<String, HashMap<StateBase.GameType, HashMap<GameModeTemplateBase.GameMode, RegionalSavedGame>>> saves = SavedGamesHandler.getRegionalSaves()
            .get("WorldMaps");
         File configOut = new File("profiles/profile" + Game.getActiveProfile() + "/customConfig.gameMode");
         hasCustomConfig = configOut.exists();
         if (hasCustomConfig) {
            GameModeTemplateBase.GameMode.CUSTOM.getTemplate().loadCustomProfileConfig();
         }

         fullVillagerList.clear();
         fullMigrantList.clear();
         fullMonsterList.clear();
         fullResourcesList.clear();
         villagerList.clear();
         migrantList.clear();
         monsterList.clear();
         objectCount.clear();
         campType.clear();
         resourcesList.clear();
         currentDay.clear();
         mapLost.clear();
         mapStarted.clear();
         newGame = true;
         final AtomicInteger count = new AtomicInteger();

         for (final String mapFileName : saves.keySet()) {
            if (SavedGamesHandler.regionalSaveExist("WorldMaps", mapFileName, StateBase.GameType.WORLD_MAP, GameModeTemplateBase.GameMode.MAP_EDITOR)) {
               RegionalSavedGame saveCheck = saves.get(mapFileName).get(StateBase.GameType.WORLD_MAP).get(GameModeTemplateBase.GameMode.MAP_EDITOR);
               villagerList.put(saveCheck, new ArrayList<>());
               migrantList.put(saveCheck, new ArrayList<>());
               monsterList.put(saveCheck, new ArrayList<>());
               resourcesList.put(saveCheck, new ArrayList<>());
            }

            if (SavedGamesHandler.regionalSaveExist("WorldMaps", mapFileName, StateBase.GameType.WORLD_MAP, selectedGameMode)) {
               Thread thread = new Thread("Save Loading Thread " + mapFileName) {
                  @Override
                  public void run() {
                     System.out.println("Save Loading Thread " + mapFileName + " Running.");
                     RegionalSavedGame saveCheck = saves.get(mapFileName).get(StateBase.GameType.WORLD_MAP).get(WorldMapGUIData.selectedGameMode);
                     WorldMapGUIData.villagerList.put(saveCheck, new ArrayList<>());
                     WorldMapGUIData.migrantList.put(saveCheck, new ArrayList<>());
                     WorldMapGUIData.monsterList.put(saveCheck, new ArrayList<>());
                     WorldMapGUIData.resourcesList.put(saveCheck, new ArrayList<>());
                     YMLDataMap resourceModuleDataMap = saveCheck.getAndUnloadModuleSaveData(ModuleBase.ModuleType.RESOURCE);
                     int resourceCount = 0;

                     while (resourceModuleDataMap.containsKey("resource" + resourceCount)) {
                        WorldMapGUIData.newGame = false;

                        try {
                           ResourceBase r = new ResourceBase(resourceModuleDataMap.getSubMap("resource" + resourceCount));
                           if (r.isInBuilding() || r.isCarried() || r.isInInventory() || r.isOnGround()) {
                              WorldMapGUIData.fullResourcesList.add(r);
                              WorldMapGUIData.resourcesList.get(saveCheck).add(r);
                           }

                           resourceCount++;
                        } catch (SlickException var11) {
                           var11.printStackTrace();
                        }
                     }

                     YMLDataMap mobModuleDataMap = saveCheck.getAndUnloadModuleSaveData(ModuleBase.ModuleType.MOB);
                     int mobCount = 0;

                     while (mobModuleDataMap.containsKey("mob" + mobCount)) {
                        WorldMapGUIData.newGame = false;

                        try {
                           MobBase m = new HumanVillagerAdult();
                           m.loadMob(mobModuleDataMap.getSubMap("mob" + mobCount));
                           if (m.getMobGroup() == MobBase.MobGroup.VILLAGER && !m.isDead()) {
                              WorldMapGUIData.fullVillagerList.add(m);
                              WorldMapGUIData.villagerList.get(saveCheck).add(m);
                           }

                           if (m.getMobGroup() == MobBase.MobGroup.MIGRANT && !m.isDead()) {
                              WorldMapGUIData.fullMigrantList.add(m);
                              WorldMapGUIData.migrantList.get(saveCheck).add(m);
                           }

                           if (m.getMobGroup() == MobBase.MobGroup.MONSTER && !m.isDead()) {
                              WorldMapGUIData.fullMonsterList.add(m);
                              WorldMapGUIData.monsterList.get(saveCheck).add(m);
                           }

                           mobCount++;
                        } catch (SlickException var10) {
                           var10.printStackTrace();
                        }
                     }

                     YMLDataMap objectModuleDataMap = saveCheck.getAndUnloadModuleSaveData(ModuleBase.ModuleType.OBJECT);
                     WorldMapGUIData.objectCount.put(saveCheck, Integer.parseInt(objectModuleDataMap.getOrDefault("activeBuildings", 0)));

                     for (int objectCount = 0; objectModuleDataMap.containsKey("object" + objectCount); objectCount++) {
                        YMLDataMap thisObject = objectModuleDataMap.getSubMap("object" + objectCount);
                        if (thisObject.getOrDefault("currentType", "null").contains("CASTLE")) {
                           WorldMapGUIData.campType.put(saveCheck, Integer.parseInt(thisObject.getOrDefault("currentType", "null").replace("CASTLE_", "")));
                           break;
                        }
                     }

                     YMLDataMap timeModuleDataMap = saveCheck.getAndUnloadModuleSaveData(ModuleBase.ModuleType.TIME);
                     WorldMapGUIData.currentDay.put(saveCheck, Integer.parseInt(timeModuleDataMap.getOrDefault("day", 0)));
                     YMLDataMap mapModuleDataMap = saveCheck.getAndUnloadModuleSaveData(ModuleBase.ModuleType.MAP);
                     WorldMapGUIData.mapLost.put(saveCheck, Boolean.parseBoolean(mapModuleDataMap.getOrDefault("mapLost", false)));
                     WorldMapGUIData.mapStarted.put(saveCheck, Boolean.parseBoolean(mapModuleDataMap.getOrDefault("mapStarted", false)));
                     count.incrementAndGet();
                     System.out.println("Save Loading Thread " + mapFileName + " Complete.");
                  }
               };
               thread.start();
            } else {
               count.incrementAndGet();
            }
         }

         while (count.get() < saves.keySet().size()) {
            try {
               Thread.sleep(100L);
            } catch (InterruptedException var6) {
               var6.printStackTrace();
            }
         }
      }
   }

   public static GameModeTemplateBase.GameMode getSelectedGameMode() {
      return selectedGameMode;
   }

   public static boolean hasCustomConfig() {
      return hasCustomConfig;
   }
}
