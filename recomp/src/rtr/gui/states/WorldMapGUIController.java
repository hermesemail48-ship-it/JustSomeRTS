package rtr.gui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.gui.states.shared.DebugData;
import rtr.gui.states.shared.EdgeScrolling;
import rtr.gui.states.shared.GoalsPanel;
import rtr.gui.states.shared.TipsPanel;
import rtr.gui.states.worldmap.ActiveGoalsList;
import rtr.gui.states.worldmap.ChestOpenPanel;
import rtr.gui.states.worldmap.DoomWorldPanel;
import rtr.gui.states.worldmap.GameModeConfigPanel;
import rtr.gui.states.worldmap.GameModeSelectPanel;
import rtr.gui.states.worldmap.GoalClaimPanel;
import rtr.gui.states.worldmap.MapSelectPanel;
import rtr.gui.states.worldmap.MobOverlay;
import rtr.gui.states.worldmap.PerksList;
import rtr.gui.states.worldmap.RewardChestPanel;
import rtr.gui.states.worldmap.SystemPanel;
import rtr.gui.states.worldmap.WorldMap;

public class WorldMapGUIController extends GUIControllerBase {
   private WorldMapGUIData guiData = new WorldMapGUIData();
   private DoomWorldPanel doomWorldPanel;
   private GameModeConfigPanel gameModeConfigPanel;
   private GameModeSelectPanel gameModeSelectPanel;
   private MapSelectPanel mapSelectPanel;
   private MobOverlay mobOverlay;
   private RewardChestPanel rewardChestPanel;
   private ChestOpenPanel chestOpenPanel;
   private SystemPanel systemPanel;
   private GoalClaimPanel goalClaimPanel;
   private PerksList perksList;
   private WorldMap worldMap;
   private ActiveGoalsList activeGoalsList;
   private EdgeScrolling edgeScrolling;
   private DebugData debugData;
   private TipsPanel tipsPanel;
   private GoalsPanel goalsPanel;
   private boolean gameModeConfigPanelEnabled;
   private boolean gameModeSelectOverlayPanelEnabled;
   private boolean doomWorldPanelEnabled;
   private boolean mapSelectPanelEnabled;
   private boolean goalsPanelEnabled;

   public WorldMapGUIController(Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      this.doomWorldPanel = new DoomWorldPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.doomWorldPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.WORLD_MAP_DOOM_WORLD_PANEL, this.doomWorldPanel);
      this.gameModeConfigPanel = new GameModeConfigPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.gameModeConfigPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, this.gameModeConfigPanel);
      this.gameModeSelectPanel = new GameModeSelectPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.gameModeSelectPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_SELECT_PANEL, this.gameModeSelectPanel);
      this.mapSelectPanel = new MapSelectPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.mapSelectPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.WORLD_MAP_MAP_SELECT_PANEL, this.mapSelectPanel);
      this.mobOverlay = new MobOverlay(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.mobOverlay);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.WORLD_MAP_MOB_OVERLAY, this.mobOverlay);
      this.rewardChestPanel = new RewardChestPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.rewardChestPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.WORLD_MAP_REWARD_CHEST_PANEL, this.rewardChestPanel);
      this.chestOpenPanel = new ChestOpenPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.chestOpenPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.WORLD_MAP_CHEST_OPEN_PANEL, this.chestOpenPanel);
      this.systemPanel = new SystemPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.systemPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.WORLD_MAP_SYSTEM_PANEL, this.systemPanel);
      this.goalClaimPanel = new GoalClaimPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.goalClaimPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.WORLD_MAP_GOAL_CLAIM_PANEL, this.goalClaimPanel);
      this.perksList = new PerksList(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.perksList);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.WORLD_MAP_PERKS_PANEL, this.perksList);
      this.worldMap = new WorldMap(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.worldMap);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.WORLD_MAP, this.worldMap);
      this.activeGoalsList = new ActiveGoalsList(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.activeGoalsList);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.WORLD_MAP_ACTIVE_GOALS_LIST, this.activeGoalsList);
      this.edgeScrolling = new EdgeScrolling(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.edgeScrolling);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_EDGE_SCROLLING, this.edgeScrolling);
      this.debugData = new DebugData(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.debugData);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_DEBUG_DATA, this.debugData);
      this.tipsPanel = new TipsPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.tipsPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_TIPS_PANEL, this.tipsPanel);
      this.goalsPanel = new GoalsPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.goalsPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_GOALS_PANEL, this.goalsPanel);
   }

   public void render() throws SlickException {
      for (GUIPanelBase g : this.allPanelsArray) {
         g.hideButtonsAndMasks();
      }

      if (!this.hideGUI) {
         this.edgeScrolling.render(this.debug);
         if (!this.gameModeSelectOverlayPanelEnabled && !this.gameModeConfigPanelEnabled) {
            this.activeGoalsList.render(this.debug);
            this.rewardChestPanel.render(this.debug);
            this.systemPanel.render(this.debug);
            this.goalsPanel.render(this.goalsPanelEnabled, this.debug);
            this.perksList.render(this.debug);
         }

         if (this.goalClaimPanel.getSelectedGoalButton() != null) {
            this.goalClaimPanel.render(this.debug);
         }

         if (this.chestOpenPanel.getSelectedChest() != null) {
            this.chestOpenPanel.render(this.debug);
         }

         if (this.doomWorldPanelEnabled) {
            this.doomWorldPanel.render(this.debug);
         }

         if (this.mapSelectPanelEnabled) {
            this.mapSelectPanel.render(this.debug);
         }

         if (this.gameModeConfigPanelEnabled) {
            this.gameModeConfigPanel.render(this.debug);
         }

         if (this.gameModeSelectOverlayPanelEnabled) {
            this.gameModeSelectPanel.render(this.debug);
         }

         if (!this.gameModeSelectOverlayPanelEnabled && !this.gameModeConfigPanelEnabled) {
            this.tipsPanel.render(this.debug);
         }

         for (GUIPanelBase g : this.allPanelsArray) {
            g.renderDescriptionToolTip();
         }

         if (this.debug) {
            for (GUIPanelBase g : this.allPanelsArray) {
               g.renderMasks();
            }

            this.debugData.render(65.0F, 30.0F);
         }
      }
   }

   @Override
   public void update() throws SlickException {
      super.update();
   }

   public void resetGUI() {
      this.closeAll();
      this.worldMap.deselectAllWorldMap();
   }

   public void toggleGameModeConfigPanel() {
      this.closeAll();
      this.gameModeConfigPanelEnabled = true;
   }

   public void toggleGameModeSelectOverlayPanel() {
      this.closeAll();
      this.gameModeSelectOverlayPanelEnabled = true;
   }

   public void toggleDoomWorldPanel() {
      if (this.doomWorldPanelEnabled) {
         this.closeAll();
      } else {
         this.closeAll();
         this.doomWorldPanelEnabled = true;
      }
   }

   public void toggleMapSelectPanel() {
      this.closeAll();
      this.mapSelectPanelEnabled = true;
   }

   public void toggleGoalsPanel() {
      if (this.goalsPanelEnabled) {
         this.closeAll();
      } else {
         this.closeAll();
         this.goalsPanelEnabled = true;
      }
   }

   public void closeAll() {
      this.gameModeConfigPanelEnabled = false;
      this.gameModeSelectOverlayPanelEnabled = false;
      this.doomWorldPanelEnabled = false;
      this.mapSelectPanelEnabled = false;
      this.goalsPanelEnabled = false;
      this.deselectAll();
      if (this.worldMap != null) {
         this.worldMap.deselectAllWorldMap();
      }
   }

   public boolean isTabsClosed() {
      return !this.gameModeConfigPanelEnabled
         && !this.gameModeSelectOverlayPanelEnabled
         && !this.doomWorldPanelEnabled
         && !this.mapSelectPanelEnabled
         && !this.goalsPanelEnabled;
   }

   public WorldMapGUIData getGUIData() {
      return this.guiData;
   }

   public DoomWorldPanel getDoomWorldPanel() {
      return this.doomWorldPanel;
   }

   public GameModeConfigPanel getGameModeConfigPanel() {
      return this.gameModeConfigPanel;
   }

   public GameModeSelectPanel getGameModeSelectPanel() {
      return this.gameModeSelectPanel;
   }

   public MapSelectPanel getMapSelectPanel() {
      return this.mapSelectPanel;
   }

   public MobOverlay getMobOverlay() {
      return this.mobOverlay;
   }

   public RewardChestPanel getRewardChestPanel() {
      return this.rewardChestPanel;
   }

   public ChestOpenPanel getChestOpenPanel() {
      return this.chestOpenPanel;
   }

   public SystemPanel getSystemPanel() {
      return this.systemPanel;
   }

   public GoalClaimPanel getGoalClaimPanel() {
      return this.goalClaimPanel;
   }

   public PerksList getPerksList() {
      return this.perksList;
   }

   public WorldMap getWorldMap() {
      return this.worldMap;
   }

   public ActiveGoalsList getActiveGoalsList() {
      return this.activeGoalsList;
   }

   public EdgeScrolling getEdgeScrolling() {
      return this.edgeScrolling;
   }

   public TipsPanel getTipsPanel() {
      return this.tipsPanel;
   }

   public GoalsPanel getGoalsPanel() {
      return this.goalsPanel;
   }

   public boolean isGameModeConfigPanelEnabled() {
      return this.gameModeConfigPanelEnabled;
   }

   public boolean isGameModeSelectOverlayPanelEnabled() {
      return this.gameModeSelectOverlayPanelEnabled;
   }

   public boolean isDoomWorldPanelEnabled() {
      return this.doomWorldPanelEnabled;
   }

   public boolean isMapSelectPanelEnabled() {
      return this.mapSelectPanelEnabled;
   }

   public boolean isGoalsPanelEnabled() {
      return this.goalsPanelEnabled;
   }
}
