package rtr.goal;

import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.save.RegionalSavedGame;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class GoalModule extends ModuleBase {
   private HashMap<GoalModule.GoalType, Goal> allGoals;
   private int completedGoalsCount = 0;

   public HashMap<GoalModule.GoalType, Goal> getAllGoals() {
      return this.allGoals;
   }

   public int getCompletedGoalsCount() {
      return this.completedGoalsCount;
   }

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.allGoals = new HashMap<>();
      this.buildGoalMap();
      this.initLoad();
   }

   private void buildGoalMap() throws SlickException {
      this.addGoal(new Goal(GoalModule.GoalType.YOU_ALREADY_LOST, -3, 0, 0, 1));
      this.addGoal(new Goal(GoalModule.GoalType.DEFORESTATION, GoalModule.GoalType.YOU_ALREADY_LOST, Goal.GoalPosition.LEFT, 256));
      this.addGoal(new Goal(GoalModule.GoalType.FORAGING, GoalModule.GoalType.DEFORESTATION, Goal.GoalPosition.LEFT, 256));
      this.addGoal(new Goal(GoalModule.GoalType.ITS_HUNTIN_SEASON, GoalModule.GoalType.FORAGING, Goal.GoalPosition.LEFT, 16));
      this.addGoal(new Goal(GoalModule.GoalType.SURVIVE, GoalModule.GoalType.YOU_ALREADY_LOST, Goal.GoalPosition.RIGHT, 8));
      this.addGoal(new Goal(GoalModule.GoalType.DOOOOMED, GoalModule.GoalType.SURVIVE, Goal.GoalPosition.RIGHT, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.GOD_KING, 7, 0, 1, 1));
      this.addGoal(new Goal(GoalModule.GoalType.HOSPICE_CARE, 5, 0, 1, 24, true));
      this.addGoal(new Goal(GoalModule.GoalType.GOD_DAMMIT, 10, 0, 3, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.FACTORY_FARMING, GoalModule.GoalType.ITS_HUNTIN_SEASON, Goal.GoalPosition.DOWN_LEFT, 64));
      this.addGoal(new Goal(GoalModule.GoalType.QUARRY, GoalModule.GoalType.DEFORESTATION, Goal.GoalPosition.DOWN_LEFT, 256));
      this.addGoal(new Goal(GoalModule.GoalType.EGGCELLENT, GoalModule.GoalType.QUARRY, Goal.GoalPosition.LEFT, 64));
      this.addGoal(new Goal(GoalModule.GoalType.IMMIGRANT_SONG, GoalModule.GoalType.YOU_ALREADY_LOST, Goal.GoalPosition.DOWN_LEFT, 16));
      this.addGoal(new Goal(GoalModule.GoalType.BUILD_ALL_THE_THINGS, GoalModule.GoalType.YOU_ALREADY_LOST, Goal.GoalPosition.DOWN_RIGHT, 16));
      this.addGoal(new Goal(GoalModule.GoalType.THE_COST_OF_PROGRESS, GoalModule.GoalType.BUILD_ALL_THE_THINGS, Goal.GoalPosition.RIGHT, 16));
      this.addGoal(new Goal(GoalModule.GoalType.WELL_WELL_WELL, GoalModule.GoalType.THE_COST_OF_PROGRESS, Goal.GoalPosition.RIGHT, 3));
      this.addGoal(new Goal(GoalModule.GoalType.CATCH_IT_ALL, GoalModule.GoalType.WELL_WELL_WELL, Goal.GoalPosition.RIGHT, 2048));
      this.addGoal(new Goal(GoalModule.GoalType.NESTLED_PURE_LIFE_WATER, GoalModule.GoalType.CATCH_IT_ALL, Goal.GoalPosition.RIGHT, 2048));
      this.addGoal(new Goal(GoalModule.GoalType.SERIOUSLY, 9, 1, 5, 8, true));
      this.addGoal(new Goal(GoalModule.GoalType.WEAKEST_LINKS, 11, 1, 5, 24, true));
      this.addGoal(new Goal(GoalModule.GoalType.CLUCK_NUGGETS, GoalModule.GoalType.FACTORY_FARMING, Goal.GoalPosition.DOWN_RIGHT, 42));
      this.addGoal(new Goal(GoalModule.GoalType.KYLE_POWERED, GoalModule.GoalType.QUARRY, Goal.GoalPosition.DOWN_LEFT, 256));
      this.addGoal(new Goal(GoalModule.GoalType.STRAYS, GoalModule.GoalType.IMMIGRANT_SONG, Goal.GoalPosition.DOWN_LEFT, 16));
      this.addGoal(new Goal(GoalModule.GoalType.SARDINES, GoalModule.GoalType.BUILD_ALL_THE_THINGS, Goal.GoalPosition.DOWN_LEFT, 6));
      this.addGoal(new Goal(GoalModule.GoalType.GODS_VILLAGE, GoalModule.GoalType.BUILD_ALL_THE_THINGS, Goal.GoalPosition.DOWN_RIGHT, 12));
      this.addGoal(new Goal(GoalModule.GoalType.THE_DOGGO_HOUSE, GoalModule.GoalType.THE_COST_OF_PROGRESS, Goal.GoalPosition.DOWN_RIGHT, 6));
      this.addGoal(new Goal(GoalModule.GoalType.ANIMAL_RIGHTS_VIOLATIONS, GoalModule.GoalType.THE_DOGGO_HOUSE, Goal.GoalPosition.RIGHT, 12));
      this.addGoal(new Goal(GoalModule.GoalType.DUGGAR, GoalModule.GoalType.CATCH_IT_ALL, Goal.GoalPosition.DOWN_RIGHT, 19));
      this.addGoal(new Goal(GoalModule.GoalType.ANGST, GoalModule.GoalType.DUGGAR, Goal.GoalPosition.RIGHT, 24));
      this.addGoal(new Goal(GoalModule.GoalType.CHILL, 9, 2, 5, 1));
      this.addGoal(new Goal(GoalModule.GoalType.FREE_CHEST_6, GoalModule.GoalType.WEAKEST_LINKS, Goal.GoalPosition.DOWN, 50));
      this.addGoal(new Goal(GoalModule.GoalType.INDUSTRY, GoalModule.GoalType.KYLE_POWERED, Goal.GoalPosition.DOWN_LEFT, 128));
      this.addGoal(new Goal(GoalModule.GoalType.DIGGY_DIGGY_HOLE, GoalModule.GoalType.INDUSTRY, Goal.GoalPosition.LEFT, 192));
      this.addGoal(new Goal(GoalModule.GoalType.BURIED_TREASURE, GoalModule.GoalType.KYLE_POWERED, Goal.GoalPosition.DOWN_RIGHT, 12));
      this.addGoal(new Goal(GoalModule.GoalType.PRODIGIOUS_PLANNER, GoalModule.GoalType.SARDINES, Goal.GoalPosition.DOWN_LEFT, 12));
      this.addGoal(new Goal(GoalModule.GoalType.ACROPOLIS, GoalModule.GoalType.SARDINES, Goal.GoalPosition.DOWN_RIGHT, 6));
      this.addGoal(new Goal(GoalModule.GoalType.FIRST_WORLD_PROBLEMS, GoalModule.GoalType.GODS_VILLAGE, Goal.GoalPosition.DOWN_RIGHT, 6));
      this.addGoal(new Goal(GoalModule.GoalType.HELPFUL_DOGGOS, GoalModule.GoalType.THE_DOGGO_HOUSE, Goal.GoalPosition.DOWN_RIGHT, 64));
      this.addGoal(new Goal(GoalModule.GoalType.HOARDER, 4, 3, 8, 32));
      this.addGoal(new Goal(GoalModule.GoalType.THE_MOON, GoalModule.GoalType.CHILL, Goal.GoalPosition.DOWN_LEFT, 16));
      this.addGoal(new Goal(GoalModule.GoalType.WHO_TURNED_OFF_THE_SUN, GoalModule.GoalType.CHILL, Goal.GoalPosition.DOWN_RIGHT, 1));
      this.addGoal(new Goal(GoalModule.GoalType.THE_GRIND, GoalModule.GoalType.DIGGY_DIGGY_HOLE, Goal.GoalPosition.DOWN, 1024));
      this.addGoal(new Goal(GoalModule.GoalType.THE_LONGEST_JOURNEY, GoalModule.GoalType.INDUSTRY, Goal.GoalPosition.DOWN, 16));
      this.addGoal(new Goal(GoalModule.GoalType.STOP_POKING_ME, GoalModule.GoalType.BURIED_TREASURE, Goal.GoalPosition.DOWN, 12));
      this.addGoal(new Goal(GoalModule.GoalType.ALL_ROADS_LEAD_TO_HOME, GoalModule.GoalType.PRODIGIOUS_PLANNER, Goal.GoalPosition.DOWN, 128));
      this.addGoal(new Goal(GoalModule.GoalType.BURNED_ALIVE, -2, 4, 2, 1));
      this.addGoal(new Goal(GoalModule.GoalType.FREE_CHEST_3, GoalModule.GoalType.FIRST_WORLD_PROBLEMS, Goal.GoalPosition.DOWN, 50));
      this.addGoal(new Goal(GoalModule.GoalType.THE_BIG_DOGGO_HOUSE, GoalModule.GoalType.HELPFUL_DOGGOS, Goal.GoalPosition.DOWN, 6));
      this.addGoal(new Goal(GoalModule.GoalType.LITERAL_DUMPSTER_FIRE, GoalModule.GoalType.HOARDER, Goal.GoalPosition.DOWN_RIGHT, 192));
      this.addGoal(new Goal(GoalModule.GoalType.FREE_CHEST_2, GoalModule.GoalType.THE_MOON, Goal.GoalPosition.DOWN_LEFT, 50));
      this.addGoal(new Goal(GoalModule.GoalType.RAGNAROK, GoalModule.GoalType.WHO_TURNED_OFF_THE_SUN, Goal.GoalPosition.DOWN_LEFT, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.THE_RED_SUN_RISES, GoalModule.GoalType.WHO_TURNED_OFF_THE_SUN, Goal.GoalPosition.DOWN_RIGHT, 1));
      this.addGoal(new Goal(GoalModule.GoalType.PORTER, GoalModule.GoalType.THE_LONGEST_JOURNEY, Goal.GoalPosition.DOWN_LEFT, 128));
      this.addGoal(new Goal(GoalModule.GoalType.ELECTRONIC_SHARKS, GoalModule.GoalType.STOP_POKING_ME, Goal.GoalPosition.DOWN_LEFT, 8));
      this.addGoal(new Goal(GoalModule.GoalType.WHALE, GoalModule.GoalType.ELECTRONIC_SHARKS, Goal.GoalPosition.RIGHT, 16, true));
      this.addGoal(new Goal(GoalModule.GoalType.ATTABOY, GoalModule.GoalType.WHALE, Goal.GoalPosition.RIGHT, 1));
      this.addGoal(new Goal(GoalModule.GoalType.TOWER_DEFENSE, 1, 5, 12, 12));
      this.addGoal(new Goal(GoalModule.GoalType.POOR_WEAPONRY, GoalModule.GoalType.TOWER_DEFENSE, Goal.GoalPosition.LEFT, 1));
      this.addGoal(new Goal(GoalModule.GoalType.FREE_CHEST_4, GoalModule.GoalType.THE_BIG_DOGGO_HOUSE, Goal.GoalPosition.DOWN_RIGHT, 50));
      this.addGoal(new Goal(GoalModule.GoalType.WHAT_IS_MY_PURPOSE, GoalModule.GoalType.LITERAL_DUMPSTER_FIRE, Goal.GoalPosition.DOWN, 8));
      this.addGoal(new Goal(GoalModule.GoalType.I_WANT_MY_KEY_BACK, GoalModule.GoalType.WHAT_IS_MY_PURPOSE, Goal.GoalPosition.RIGHT, 1));
      this.addGoal(new Goal(GoalModule.GoalType.RISE_TO_RICHES, 11, 5, 10, 3072));
      this.addGoal(new Goal(GoalModule.GoalType.WELL_PROTECTED, GoalModule.GoalType.RISE_TO_RICHES, Goal.GoalPosition.LEFT, 64));
      this.addGoal(new Goal(GoalModule.GoalType.IVE_GOT_PRIME, GoalModule.GoalType.PORTER, Goal.GoalPosition.DOWN_RIGHT, 128));
      this.addGoal(new Goal(GoalModule.GoalType.MANA_MILLIONAIRE, -6, 6, 15, 50000));
      this.addGoal(new Goal(GoalModule.GoalType.POWER_OVERWHELMING, GoalModule.GoalType.MANA_MILLIONAIRE, Goal.GoalPosition.RIGHT, 4096));
      this.addGoal(new Goal(GoalModule.GoalType.FREE_CHEST_5, GoalModule.GoalType.POOR_WEAPONRY, Goal.GoalPosition.DOWN_LEFT, 50));
      this.addGoal(new Goal(GoalModule.GoalType.LORD_OF_THE_ARROWS, GoalModule.GoalType.TOWER_DEFENSE, Goal.GoalPosition.DOWN_LEFT, 32));
      this.addGoal(new Goal(GoalModule.GoalType.TAKE_THAT, GoalModule.GoalType.TOWER_DEFENSE, Goal.GoalPosition.DOWN_RIGHT, 1));
      this.addGoal(new Goal(GoalModule.GoalType.LET_IT_SNOW_ARROWS, GoalModule.GoalType.TAKE_THAT, Goal.GoalPosition.RIGHT, 4));
      this.addGoal(new Goal(GoalModule.GoalType.WHATS_THAT_SMELL, GoalModule.GoalType.WHAT_IS_MY_PURPOSE, Goal.GoalPosition.DOWN_RIGHT, 6, true));
      this.addGoal(new Goal(GoalModule.GoalType.CATJEET_HAS_WARES, GoalModule.GoalType.RISE_TO_RICHES, Goal.GoalPosition.DOWN, 256));
      this.addGoal(new Goal(GoalModule.GoalType.THE_DUST_MUST_FLOW, GoalModule.GoalType.CATJEET_HAS_WARES, Goal.GoalPosition.LEFT, 24));
      this.addGoal(new Goal(GoalModule.GoalType.HANDS_OFF_ME, GoalModule.GoalType.MANA_MILLIONAIRE, Goal.GoalPosition.DOWN_LEFT, 128));
      this.addGoal(new Goal(GoalModule.GoalType.WHOOPS_SORRY, GoalModule.GoalType.HANDS_OFF_ME, Goal.GoalPosition.LEFT, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.IMPATIENT, GoalModule.GoalType.HANDS_OFF_ME, Goal.GoalPosition.RIGHT, 32));
      this.addGoal(new Goal(GoalModule.GoalType.GOLEMS_WILL_TAKE_OUR_JOBS, GoalModule.GoalType.POWER_OVERWHELMING, Goal.GoalPosition.DOWN_RIGHT, 24));
      this.addGoal(new Goal(GoalModule.GoalType.RETROFIT, GoalModule.GoalType.LORD_OF_THE_ARROWS, Goal.GoalPosition.DOWN_RIGHT, 12));
      this.addGoal(new Goal(GoalModule.GoalType.CRUMBLING_DEFENSES, GoalModule.GoalType.RETROFIT, Goal.GoalPosition.LEFT, 24, true));
      this.addGoal(new Goal(GoalModule.GoalType.IT_NEVER_STOPS, GoalModule.GoalType.TAKE_THAT, Goal.GoalPosition.DOWN_RIGHT, 16));
      this.addGoal(new Goal(GoalModule.GoalType.TOOLS_FOR_ALL, GoalModule.GoalType.IT_NEVER_STOPS, Goal.GoalPosition.RIGHT, 64));
      this.addGoal(new Goal(GoalModule.GoalType.THE_SOUL_TRADE, GoalModule.GoalType.THE_DUST_MUST_FLOW, Goal.GoalPosition.DOWN_LEFT, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.MIGRANT_WORKERS, GoalModule.GoalType.CATJEET_HAS_WARES, Goal.GoalPosition.DOWN_LEFT, 12));
      this.addGoal(new Goal(GoalModule.GoalType.WILD_GROWTH, GoalModule.GoalType.HANDS_OFF_ME, Goal.GoalPosition.DOWN_RIGHT, 128));
      this.addGoal(new Goal(GoalModule.GoalType.ROSEBUD, GoalModule.GoalType.WILD_GROWTH, Goal.GoalPosition.LEFT, 256));
      this.addGoal(new Goal(GoalModule.GoalType.NEVER_GONNA_GIVE_YOU_UP, GoalModule.GoalType.ROSEBUD, Goal.GoalPosition.LEFT, 1));
      this.addGoal(new Goal(GoalModule.GoalType.BAPTIZED_GOLEMANCER, GoalModule.GoalType.GOLEMS_WILL_TAKE_OUR_JOBS, Goal.GoalPosition.DOWN_LEFT, 16));
      this.addGoal(new Goal(GoalModule.GoalType.HOLY_DEFENSE, GoalModule.GoalType.GOLEMS_WILL_TAKE_OUR_JOBS, Goal.GoalPosition.DOWN_RIGHT, 12));
      this.addGoal(new Goal(GoalModule.GoalType.TAKE_IT_BACK, GoalModule.GoalType.RETROFIT, Goal.GoalPosition.DOWN_LEFT, 256));
      this.addGoal(new Goal(GoalModule.GoalType.I_LIKE_BIG_BUTTRESSES, GoalModule.GoalType.RETROFIT, Goal.GoalPosition.DOWN_RIGHT, 128));
      this.addGoal(new Goal(GoalModule.GoalType.TYRANT, 8, 8, 20, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.FREE_CHEST_1, GoalModule.GoalType.TYRANT, Goal.GoalPosition.LEFT, 50));
      this.addGoal(new Goal(GoalModule.GoalType.CATJEETASTROPHIC, GoalModule.GoalType.MIGRANT_WORKERS, Goal.GoalPosition.DOWN, 6, true));
      this.addGoal(new Goal(GoalModule.GoalType.PRAISE_BE_UNTO_THEE, GoalModule.GoalType.WILD_GROWTH, Goal.GoalPosition.DOWN_LEFT, 512));
      this.addGoal(new Goal(GoalModule.GoalType.GHOST_TOWN, GoalModule.GoalType.PRAISE_BE_UNTO_THEE, Goal.GoalPosition.LEFT, 16));
      this.addGoal(new Goal(GoalModule.GoalType.DEIFIED_FARMER, GoalModule.GoalType.WILD_GROWTH, Goal.GoalPosition.DOWN_RIGHT, 128));
      this.addGoal(new Goal(GoalModule.GoalType.IM_DONE_PLAYING_WITH_YOU, GoalModule.GoalType.HOLY_DEFENSE, Goal.GoalPosition.DOWN_LEFT, 24));
      this.addGoal(new Goal(GoalModule.GoalType.FIGHT_THE_CORRUPTION, GoalModule.GoalType.TAKE_IT_BACK, Goal.GoalPosition.DOWN_LEFT, 6));
      this.addGoal(new Goal(GoalModule.GoalType.DUDE_WTH_MAN, GoalModule.GoalType.TAKE_IT_BACK, Goal.GoalPosition.DOWN_RIGHT, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.WATER_JOUSTING, GoalModule.GoalType.I_LIKE_BIG_BUTTRESSES, Goal.GoalPosition.DOWN_RIGHT, 8));
      this.addGoal(new Goal(GoalModule.GoalType.EXPLOSIVE_SIDE_EFFECTS, 5, 9, 23, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.A_WORTHY_SACRIFICE, GoalModule.GoalType.TYRANT, Goal.GoalPosition.DOWN_LEFT, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.RISE_FROM_THE_GRAVE, GoalModule.GoalType.PRAISE_BE_UNTO_THEE, Goal.GoalPosition.DOWN, 16));
      this.addGoal(new Goal(GoalModule.GoalType.WAS_I_A_GOOD_BOY, GoalModule.GoalType.RISE_FROM_THE_GRAVE, Goal.GoalPosition.LEFT, 4));
      this.addGoal(new Goal(GoalModule.GoalType.ECO_TERRORIST, GoalModule.GoalType.DEIFIED_FARMER, Goal.GoalPosition.DOWN, 128));
      this.addGoal(new Goal(GoalModule.GoalType.OBVIOUS_IN_HINDSIGHT, GoalModule.GoalType.ECO_TERRORIST, Goal.GoalPosition.RIGHT, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.WAR_CRIMES, GoalModule.GoalType.A_WORTHY_SACRIFICE, Goal.GoalPosition.DOWN_LEFT, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.FREE_CHEST_10, GoalModule.GoalType.WAR_CRIMES, Goal.GoalPosition.LEFT, 50));
      this.addGoal(new Goal(GoalModule.GoalType.FREE_CHEST_7, GoalModule.GoalType.A_WORTHY_SACRIFICE, Goal.GoalPosition.DOWN_RIGHT, 50));
      this.addGoal(new Goal(GoalModule.GoalType.BAD_AIM, GoalModule.GoalType.RISE_FROM_THE_GRAVE, Goal.GoalPosition.DOWN_LEFT, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.GOD_OR_MAGICIAN, GoalModule.GoalType.RISE_FROM_THE_GRAVE, Goal.GoalPosition.DOWN_RIGHT, 512));
      this.addGoal(new Goal(GoalModule.GoalType.LIGHT_THE_WAY, GoalModule.GoalType.ECO_TERRORIST, Goal.GoalPosition.DOWN_RIGHT, 8));
      this.addGoal(new Goal(GoalModule.GoalType.MIDGAR, GoalModule.GoalType.LIGHT_THE_WAY, Goal.GoalPosition.RIGHT, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.TWENTY_EIGHT_SECONDS_LATER, 5, 11, 25, 5, true));
      this.addGoal(new Goal(GoalModule.GoalType.OMG, GoalModule.GoalType.WAR_CRIMES, Goal.GoalPosition.DOWN_RIGHT, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.CHARMING_DEITY, GoalModule.GoalType.LIGHT_THE_WAY, Goal.GoalPosition.DOWN_RIGHT, 24));
      this.addGoal(new Goal(GoalModule.GoalType.POPCORN, GoalModule.GoalType.CHARMING_DEITY, Goal.GoalPosition.LEFT, 128));
      this.addGoal(new Goal(GoalModule.GoalType.FREE_CHEST_8, GoalModule.GoalType.OMG, Goal.GoalPosition.DOWN_LEFT, 50));
      this.addGoal(new Goal(GoalModule.GoalType.FREE_CHEST_9, GoalModule.GoalType.OMG, Goal.GoalPosition.DOWN_RIGHT, 50));
      this.addGoal(new Goal(GoalModule.GoalType.TOUCH_GRASS, -7, 13, 30, 16, true));
      this.addGoal(new Goal(GoalModule.GoalType.GOD_OF_LOVE_AND_THUNDER, GoalModule.GoalType.CHARMING_DEITY, Goal.GoalPosition.DOWN, 24));
      this.addGoal(new Goal(GoalModule.GoalType.GRANDMASTER_SPARKLES, GoalModule.GoalType.GOD_OF_LOVE_AND_THUNDER, Goal.GoalPosition.RIGHT, 1, true));
      this.addGoal(new Goal(GoalModule.GoalType.FINE_ILL_DO_IT_MYSELF, GoalModule.GoalType.GOD_OF_LOVE_AND_THUNDER, Goal.GoalPosition.DOWN_LEFT, 64));
   }

   private void refreshGoalData() {
      this.completedGoalsCount = 0;

      for (Goal g : this.allGoals.values()) {
         if (g.isCompleted()) {
            this.completedGoalsCount++;
         }
      }
   }

   private void addGoal(Goal g) {
      this.allGoals.put(g.getGoalType(), g);
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP && !this.save.getActiveWorldSave().isNewGame()) {
         this.loadWorldSaveData();
      }
   }

   @Override
   protected void loadPlay() throws SlickException {
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
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
      YMLDataMap dataMap = this.save.getActiveWorldSave().getModuleSaveData(this.moduleType);

      GoalModule.GoalType[] var5;
      for (GoalModule.GoalType gT : GoalModule.GoalType.values()) {
         int amount = Integer.parseInt(dataMap.getOrDefault(gT.toString() + "_AMOUNT", 0));
         boolean isClaimed = Boolean.parseBoolean(dataMap.getOrDefault(gT.toString() + "_CLAIMED", false));
         Goal g = this.allGoals.get(gT);
         g.load(amount, isClaimed);
      }
   }

   @Override
   public YMLDataMap getWorldMapSaveData() {
      YMLDataMap dataMap = new YMLDataMap();

      for (Goal g : this.allGoals.values()) {
         dataMap.put(g.getGoalType().toString() + "_AMOUNT", g.getGoalAmount());
         dataMap.put(g.getGoalType().toString() + "_CLAIMED", g.isClaimed());
      }

      return dataMap;
   }

   @Override
   public void resetWorldSaveData() throws SlickException {
   }

   public void update() throws SlickException {
      this.refreshGoalData();
   }

   public void updateWorldMap() throws SlickException {
      this.refreshGoalData();
   }

   public void incrementGoal(GoalModule.GoalType gT) {
      this.incrementGoal(gT, 1);
   }

   public void incrementGoal(GoalModule.GoalType gT, int amount) {
      if (Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.WORLD_MAP) {
         RegionalSavedGame thisSave = this.save.getActiveRegionalSave();
         if (thisSave.getGameType() == StateBase.GameType.SKIRMISH) {
            return;
         }
      }

      Goal g = this.allGoals.get(gT);
      if (!g.isLocked() && !g.isCompleted() && !g.isFreeChest()) {
         g.incrementGoal(amount);
      }
   }

   public static enum GoalType {
      SURVIVE,
      WEAKEST_LINKS,
      DEFORESTATION,
      QUARRY,
      KYLE_POWERED,
      THE_GRIND,
      FORAGING,
      FACTORY_FARMING,
      GODS_VILLAGE,
      SARDINES,
      BUILD_ALL_THE_THINGS,
      THE_COST_OF_PROGRESS,
      ACROPOLIS,
      FIRST_WORLD_PROBLEMS,
      EXPLOSIVE_SIDE_EFFECTS,
      ANIMAL_RIGHTS_VIOLATIONS,
      IVE_GOT_PRIME,
      RISE_FROM_THE_GRAVE,
      HELPFUL_DOGGOS,
      DIGGY_DIGGY_HOLE,
      TAKE_IT_BACK,
      PORTER,
      WAS_I_A_GOOD_BOY,
      CATJEET_HAS_WARES,
      MIGRANT_WORKERS,
      CATJEETASTROPHIC,
      ALL_ROADS_LEAD_TO_HOME,
      POOR_WEAPONRY,
      LITERAL_DUMPSTER_FIRE,
      BURIED_TREASURE,
      CHARMING_DEITY,
      ECO_TERRORIST,
      ELECTRONIC_SHARKS,
      FIGHT_THE_CORRUPTION,
      GHOST_TOWN,
      GOD_OF_LOVE_AND_THUNDER,
      HOARDER,
      I_LIKE_BIG_BUTTRESSES,
      IMMIGRANT_SONG,
      IT_NEVER_STOPS,
      ITS_HUNTIN_SEASON,
      LET_IT_SNOW_ARROWS,
      LIGHT_THE_WAY,
      LORD_OF_THE_ARROWS,
      PRAISE_BE_UNTO_THEE,
      RETROFIT,
      RISE_TO_RICHES,
      ROSEBUD,
      STRAYS,
      TAKE_THAT,
      THE_LONGEST_JOURNEY,
      TOOLS_FOR_ALL,
      TOWER_DEFENSE,
      TYRANT,
      WATER_JOUSTING,
      WILD_GROWTH,
      WHATS_THAT_SMELL,
      THE_DOGGO_HOUSE,
      THE_BIG_DOGGO_HOUSE,
      TWENTY_EIGHT_SECONDS_LATER,
      A_WORTHY_SACRIFICE,
      BAD_AIM,
      BURNED_ALIVE,
      DOOOOMED,
      GOD_DAMMIT,
      GRANDMASTER_SPARKLES,
      SERIOUSLY,
      THE_SOUL_TRADE,
      DUDE_WTH_MAN,
      CRUMBLING_DEFENSES,
      WELL_PROTECTED,
      WAR_CRIMES,
      OMG,
      YOU_ALREADY_LOST,
      ANGST,
      ATTABOY,
      BAPTIZED_GOLEMANCER,
      CHILL,
      CLUCK_NUGGETS,
      DEIFIED_FARMER,
      EGGCELLENT,
      GOD_KING,
      GOD_OR_MAGICIAN,
      HANDS_OFF_ME,
      HOLY_DEFENSE,
      HOSPICE_CARE,
      IM_DONE_PLAYING_WITH_YOU,
      IMPATIENT,
      INDUSTRY,
      I_WANT_MY_KEY_BACK,
      MANA_MILLIONAIRE,
      MIDGAR,
      NEVER_GONNA_GIVE_YOU_UP,
      OBVIOUS_IN_HINDSIGHT,
      POPCORN,
      POWER_OVERWHELMING,
      PRODIGIOUS_PLANNER,
      RAGNAROK,
      STOP_POKING_ME,
      THE_DUST_MUST_FLOW,
      THE_MOON,
      THE_RED_SUN_RISES,
      TOUCH_GRASS,
      WELL_WELL_WELL,
      WHALE,
      WHAT_IS_MY_PURPOSE,
      WHOOPS_SORRY,
      WHO_TURNED_OFF_THE_SUN,
      FINE_ILL_DO_IT_MYSELF,
      DUGGAR,
      GOLEMS_WILL_TAKE_OUR_JOBS,
      CATCH_IT_ALL,
      NESTLED_PURE_LIFE_WATER,
      FREE_CHEST_1,
      FREE_CHEST_2,
      FREE_CHEST_3,
      FREE_CHEST_4,
      FREE_CHEST_5,
      FREE_CHEST_6,
      FREE_CHEST_7,
      FREE_CHEST_8,
      FREE_CHEST_9,
      FREE_CHEST_10;
   }
}
