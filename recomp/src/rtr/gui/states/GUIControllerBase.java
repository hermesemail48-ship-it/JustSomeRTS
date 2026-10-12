package rtr.gui.states;

import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.ProfileModule;
import rtr.objects.ObjectModule;
import rtr.states.StateBase;

public class GUIControllerBase {
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected ProfileModule profile = (ProfileModule)StateBase.getModule(ModuleBase.ModuleType.PROFILE);
   protected BrushLoader brushLoader;
   protected ArrayList<GUIPanelBase> allPanelsArray = new ArrayList<>();
   protected HashMap<GUIControllerBase.GUIPanel, GUIPanelBase> allPanelsMap = new HashMap<>();
   protected boolean hideGUI;
   protected boolean debug;

   public GUIControllerBase() throws SlickException {
      this.brushLoader = new BrushLoader();
   }

   public void update() throws SlickException {
      for (GUIPanelBase g : this.allPanelsArray) {
         g.update();
      }
   }

   public BrushLoader getBrushLoader() {
      return this.brushLoader;
   }

   public void select(GUIControllerBase.GUIPanel p, String buttonName) {
      for (GUIPanelBase g : this.allPanelsArray) {
         g.deselectAll();
      }

      this.allPanelsMap.get(p).select(buttonName);
   }

   public void deselectAll() {
      for (GUIPanelBase g : this.allPanelsArray) {
         g.deselectAll();
      }
   }

   public boolean intersects(GUIControllerBase.GUIPanel p, String buttonName) {
      return this.allPanelsMap.get(p).intersects(buttonName);
   }

   public String getIntersects() {
      for (GUIPanelBase g : this.allPanelsArray) {
         String button = g.getIntersects();
         if (button != null) {
            return button;
         }
      }

      return null;
   }

   public boolean intersectsAnyGUIMask() {
      for (GUIPanelBase g : this.allPanelsArray) {
         if (g.intersectsGUIMask()) {
            return true;
         }
      }

      return false;
   }

   public boolean isHideGUI() {
      return this.hideGUI;
   }

   public void setHideGUI(boolean hideGUI) {
      this.hideGUI = hideGUI;
   }

   public void toggleHideGUI() {
      this.hideGUI = !this.hideGUI;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public void setDebug(boolean debug) {
      this.debug = debug;
   }

   public void toggleDebug() {
      this.debug = !this.debug;
   }

   public static enum GUIPanel {
      PLAY_STATE_OVERLAY,
      PLAY_STATE_PAUSE_BANNER,
      PLAY_STATE_BUILDING_LIST_PANEL,
      PLAY_STATE_RESOURCE_LIST_PANEL,
      PLAY_STATE_CASTLE_PANEL,
      PLAY_STATE_CONSOLE_PANEL,
      PLAY_STATE_GOAL_POPUP,
      PLAY_STATE_HARVEST_AND_TERRAIN_BAR,
      PLAY_STATE_LEFT_JOB_DESCRIPTION_PANEL,
      PLAY_STATE_LEFT_MIGRATION_DESCRIPTION_PANEL,
      PLAY_STATE_LEFT_PANEL,
      PLAY_STATE_MAIN_MENU_PANEL,
      PLAY_STATE_POPULATION_LIST_PANEL,
      PLAY_STATE_POPULATION_TOTAL_PANEL,
      PLAY_STATE_POSITION_ARROWS,
      PLAY_STATE_PROBLEM_PANEL,
      PLAY_STATE_RESOURCE_AND_INFLUENCE_BAR,
      PLAY_STATE_RIGHT_PANEL,
      PLAY_STATE_SELECTED_MOB_PANEL,
      PLAY_STATE_SELECTED_OBJECT_PANEL,
      PLAY_STATE_SELECTED_OBJECT_UPGRADE_PANEL_1,
      PLAY_STATE_SELECTED_OBJECT_UPGRADE_PANEL_2,
      PLAY_STATE_SELECTED_OBJECT_UPGRADE_PANEL_3,
      PLAY_STATE_SPELL_BAR,
      PLAY_STATE_SPELL_SELECT_PANEL,
      PLAY_STATE_SPELL_DESCRIPTION_PANEL,
      PLAY_STATE_SYSTEM_AND_DATA_BAR,
      PLAY_STATE_TOOLS_BAR,
      PLAY_STATE_VILLAGE_EFFICIENCY_PANEL,
      PLAY_STATE_WEATHER_PANEL,
      PLAY_STATE_YOU_LOSE_PANEL,
      PLAY_STATE_STATISTICS_PANEL,
      MAP_EDITOR_OVERLAY,
      MAP_EDITOR_TERRAIN_BAR,
      MAP_EDITOR_MAIN_MENU_PANEL,
      MAP_EDITOR_RIGHT_PANEL,
      MAP_EDITOR_SYSTEM_AND_DATA_BAR,
      MAP_EDITOR_TOOLS_BAR,
      MAP_EDITOR_REGIONAL_DATA_PANEL,
      MAP_EDITOR_WORLD_MAP_LIST_PANEL,
      MAIN_MENU_CREDITS,
      MAIN_MENU_DELETE_PROFILE_PANEL,
      MAIN_MENU_DISCLAIMER_PANEL,
      MAIN_MENU_WARNING_PANEL,
      MAIN_MENU_LANGUAGE_SELECT_PANEL,
      MAIN_MENU_LOGO,
      MAIN_MENU_MAIN_MENU_PANEL,
      MAIN_MENU_MAP_EDITOR_LOAD_MAP_DELETE_WARNING_PANEL,
      MAIN_MENU_MAP_EDITOR_LOAD_MAP_PANEL,
      MAIN_MENU_MAP_EDITOR_NEW_MAP_DELETE_WARNING_PANEL,
      MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL,
      MAIN_MENU_NEW_PROFILE_PANEL,
      MAIN_MENU_RESTART_GAME_PANEL,
      MAIN_MENU_SELECT_PROFILE_PANEL,
      MAIN_MENU_SKIRMISH_NEW_GAME_WARNING_PANEL,
      MAIN_MENU_SKIRMISH_PANEL,
      MAIN_MENU_UPLOAD_PROFILE_PANEL,
      WORLD_MAP_DOOM_WORLD_PANEL,
      WORLD_MAP_GAME_MODE_CONFIG_PANEL,
      WORLD_MAP_GAME_MODE_SELECT_PANEL,
      WORLD_MAP_MAP_SELECT_PANEL,
      WORLD_MAP_MOB_OVERLAY,
      WORLD_MAP_REWARD_CHEST_PANEL,
      WORLD_MAP_CHEST_OPEN_PANEL,
      WORLD_MAP_SYSTEM_PANEL,
      WORLD_MAP_GOAL_CLAIM_PANEL,
      WORLD_MAP_PERKS_PANEL,
      WORLD_MAP,
      WORLD_MAP_ACTIVE_GOALS_LIST,
      SHARED_DATA_VIEWS_PANEL,
      SHARED_EDGE_SCROLLING,
      SHARED_MINIMAP_PANEL,
      SHARED_MUSIC_PANEL,
      SHARED_RIGHT_OBJECT_DESCRIPTION_PANEL,
      SHARED_SETTINGS_PANEL,
      SHARED_DEBUG_DATA,
      SHARED_TIPS_PANEL,
      SHARED_GOALS_PANEL;
   }

   public static enum ResourceFilter {
      ALL,
      GROUND,
      STORAGE,
      EQUIPPED,
      CARRIED,
      RATE;
   }
}
