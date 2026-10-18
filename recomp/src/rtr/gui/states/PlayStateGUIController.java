package rtr.gui.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.font.FontModule;
import rtr.font.Text;
import rtr.gui.states.playstate.BuildingListPanel;
import rtr.gui.states.playstate.CastlePanel;
import rtr.gui.states.playstate.ConsolePanel;
import rtr.gui.states.playstate.GoalPopup;
import rtr.gui.states.playstate.HarvestAndTerrainBar;
import rtr.gui.states.playstate.LeftJobDescriptionPanel;
import rtr.gui.states.playstate.LeftMigrationDescriptionPanel;
import rtr.gui.states.playstate.LeftPanel;
import rtr.gui.states.playstate.MainMenuPanel;
import rtr.gui.states.playstate.Overlay;
import rtr.gui.states.playstate.PauseBanner;
import rtr.gui.states.playstate.PopulationListPanel;
import rtr.gui.states.playstate.PopulationTotalsPanel;
import rtr.gui.states.playstate.PositionArrows;
import rtr.gui.states.playstate.ProblemPanel;
import rtr.gui.states.playstate.ResourceAndInfluenceBar;
import rtr.gui.states.playstate.ResourceListPanel;
import rtr.gui.states.playstate.RightPanel;
import rtr.gui.states.playstate.SelectedMobPanel;
import rtr.gui.states.playstate.SelectedObjectPanel;
import rtr.gui.states.playstate.SelectedObjectUpgradePanel;
import rtr.gui.states.playstate.SpellBar;
import rtr.gui.states.playstate.SpellDescriptionPanel;
import rtr.gui.states.playstate.SpellSelectPanel;
import rtr.gui.states.playstate.StatisticsPanel;
import rtr.gui.states.playstate.SystemAndDataBar;
import rtr.gui.states.playstate.ToolsBar;
import rtr.gui.states.playstate.VillageEfficiencyPanel;
import rtr.gui.states.playstate.WeatherPanel;
import rtr.gui.states.playstate.YouLosePanel;
import rtr.gui.states.shared.DataViewsPanel;
import rtr.gui.states.shared.DebugData;
import rtr.gui.states.shared.EdgeScrolling;
import rtr.gui.states.shared.GoalsPanel;
import rtr.gui.states.shared.MinimapPanel;
import rtr.gui.states.shared.MusicPanel;
import rtr.gui.states.shared.RightObjectDescriptionPanel;
import rtr.gui.states.shared.SettingsPanel;
import rtr.gui.states.shared.TipsPanel;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.states.StateBase;
import rtr.system.ScaleControl;

public class PlayStateGUIController extends GUIControllerBase {
   private FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   private PlayStateGUIData guiData = new PlayStateGUIData();
   private Overlay overlay;
   private PauseBanner pauseBanner;
   private BuildingListPanel buildingListPanel;
   private ResourceListPanel resourceListPanel;
   private CastlePanel castlePanel;
   private ConsolePanel consolePanel;
   private GoalPopup goalPopup;
   private HarvestAndTerrainBar harvestAndTerrainBar;
   private LeftJobDescriptionPanel leftJobDescriptionPanel;
   private LeftMigrationDescriptionPanel leftMigrationDescriptionPanel;
   private LeftPanel leftPanel;
   private MainMenuPanel mainMenuPanel;
   private PopulationListPanel populationListPanel;
   private PopulationTotalsPanel populationTotalsPanel;
   private PositionArrows positionArrows;
   private ProblemPanel problemPanel;
   private ResourceAndInfluenceBar resourceAndInfluenceBar;
   /** Set by the phone port: its native build drawer and inspect cards replace the right panel and bottom panels. */
   public static boolean touchPanels = false;
   private RightPanel rightPanel;
   private SelectedMobPanel selectedMobPanel;
   private SelectedObjectPanel selectedObjectPanel;
   private SelectedObjectUpgradePanel selectedObjectUpgradePanel1;
   private SelectedObjectUpgradePanel selectedObjectUpgradePanel2;
   private SelectedObjectUpgradePanel selectedObjectUpgradePanel3;
   private SpellBar spellBar;
   private SpellSelectPanel spellSelectPanel;
   private SpellDescriptionPanel spellDescriptionPanel;
   private SystemAndDataBar systemAndDataBar;
   private ToolsBar toolsBar;
   private VillageEfficiencyPanel villageEfficiencyPanel;
   private WeatherPanel weatherPanel;
   private YouLosePanel youLosePanel;
   private StatisticsPanel statisticsPanel;
   private DataViewsPanel dataViewsPanel;
   private EdgeScrolling edgeScrolling;
   private MinimapPanel minimapPanel;
   private MusicPanel musicPanel;
   private RightObjectDescriptionPanel rightObjectDescriptionPanel;
   private SettingsPanel settingsPanel;
   private DebugData debugData;
   private TipsPanel tipsPanel;
   private GoalsPanel goalsPanel;
   private boolean consolePanelEnabled;
   private boolean objectPanelEnabled;
   private boolean mobPanelEnabled;
   private boolean castlePanelEnabled;
   private boolean spellSelectPanelEnabled;
   private boolean mainMenuPanelEnabled;
   private boolean problemPanelPanelEnabled;
   private boolean buildingListPanelEnabled;
   private boolean resourceListPanelEnabled;
   private boolean populationListPanelEnabled;
   private boolean villageEfficiencyPanelEnabled;
   private boolean jobDescriptionTabJob;
   private boolean youLosePanelEnabled;
   private boolean settingsEnabled;
   private boolean settingsKeyChangeEnabled;
   private boolean minimapPanelEnabled;
   private boolean statisticsPanelEnabled;
   private boolean dataViewsPanelEnabled;
   private boolean musicPanelEnabled;
   private boolean goalsPanelEnabled;

   public PlayStateGUIController(Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      this.overlay = new Overlay(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.overlay);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_OVERLAY, this.overlay);
      this.pauseBanner = new PauseBanner(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.pauseBanner);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_PAUSE_BANNER, this.pauseBanner);
      this.buildingListPanel = new BuildingListPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.buildingListPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_BUILDING_LIST_PANEL, this.buildingListPanel);
      this.resourceListPanel = new ResourceListPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.resourceListPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, this.resourceListPanel);
      this.castlePanel = new CastlePanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.castlePanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_CASTLE_PANEL, this.castlePanel);
      this.consolePanel = new ConsolePanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.consolePanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_CONSOLE_PANEL, this.consolePanel);
      this.goalPopup = new GoalPopup(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.goalPopup);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_GOAL_POPUP, this.goalPopup);
      this.harvestAndTerrainBar = new HarvestAndTerrainBar(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.harvestAndTerrainBar);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, this.harvestAndTerrainBar);
      this.leftJobDescriptionPanel = new LeftJobDescriptionPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.leftJobDescriptionPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_JOB_DESCRIPTION_PANEL, this.leftJobDescriptionPanel);
      this.leftMigrationDescriptionPanel = new LeftMigrationDescriptionPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.leftMigrationDescriptionPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_MIGRATION_DESCRIPTION_PANEL, this.leftMigrationDescriptionPanel);
      this.leftPanel = new LeftPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.leftPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, this.leftPanel);
      this.mainMenuPanel = new MainMenuPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.mainMenuPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_MAIN_MENU_PANEL, this.mainMenuPanel);
      this.populationListPanel = new PopulationListPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.populationListPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_POPULATION_LIST_PANEL, this.populationListPanel);
      this.populationTotalsPanel = new PopulationTotalsPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.populationTotalsPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_POPULATION_TOTAL_PANEL, this.populationTotalsPanel);
      this.positionArrows = new PositionArrows(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.positionArrows);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_POSITION_ARROWS, this.positionArrows);
      this.problemPanel = new ProblemPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.problemPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_PROBLEM_PANEL, this.problemPanel);
      this.resourceAndInfluenceBar = new ResourceAndInfluenceBar(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.resourceAndInfluenceBar);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_AND_INFLUENCE_BAR, this.resourceAndInfluenceBar);
      this.rightPanel = new RightPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.rightPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, this.rightPanel);
      this.selectedMobPanel = new SelectedMobPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.selectedMobPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_MOB_PANEL, this.selectedMobPanel);
      this.selectedObjectPanel = new SelectedObjectPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.selectedObjectPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, this.selectedObjectPanel);
      this.selectedObjectUpgradePanel1 = new SelectedObjectUpgradePanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.selectedObjectUpgradePanel1);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_UPGRADE_PANEL_1, this.selectedObjectUpgradePanel1);
      this.selectedObjectUpgradePanel2 = new SelectedObjectUpgradePanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.selectedObjectUpgradePanel2);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_UPGRADE_PANEL_2, this.selectedObjectUpgradePanel2);
      this.selectedObjectUpgradePanel3 = new SelectedObjectUpgradePanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.selectedObjectUpgradePanel3);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_UPGRADE_PANEL_3, this.selectedObjectUpgradePanel3);
      this.spellBar = new SpellBar(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.spellBar);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_SPELL_BAR, this.spellBar);
      this.spellSelectPanel = new SpellSelectPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.spellSelectPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_SPELL_SELECT_PANEL, this.spellSelectPanel);
      this.spellDescriptionPanel = new SpellDescriptionPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.spellDescriptionPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_SPELL_DESCRIPTION_PANEL, this.spellDescriptionPanel);
      this.systemAndDataBar = new SystemAndDataBar(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.systemAndDataBar);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_SYSTEM_AND_DATA_BAR, this.systemAndDataBar);
      this.toolsBar = new ToolsBar(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.toolsBar);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_TOOLS_BAR, this.toolsBar);
      this.villageEfficiencyPanel = new VillageEfficiencyPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.villageEfficiencyPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_VILLAGE_EFFICIENCY_PANEL, this.villageEfficiencyPanel);
      this.weatherPanel = new WeatherPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.weatherPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_WEATHER_PANEL, this.weatherPanel);
      this.youLosePanel = new YouLosePanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.youLosePanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_YOU_LOSE_PANEL, this.youLosePanel);
      this.statisticsPanel = new StatisticsPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.statisticsPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, this.statisticsPanel);
      this.dataViewsPanel = new DataViewsPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.dataViewsPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, this.dataViewsPanel);
      this.edgeScrolling = new EdgeScrolling(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.edgeScrolling);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_EDGE_SCROLLING, this.edgeScrolling);
      this.minimapPanel = new MinimapPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.minimapPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_MINIMAP_PANEL, this.minimapPanel);
      this.musicPanel = new MusicPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.musicPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_MUSIC_PANEL, this.musicPanel);
      this.rightObjectDescriptionPanel = new RightObjectDescriptionPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.rightObjectDescriptionPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_RIGHT_OBJECT_DESCRIPTION_PANEL, this.rightObjectDescriptionPanel);
      this.settingsPanel = new SettingsPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.settingsPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, this.settingsPanel);
      this.debugData = new DebugData(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.debugData);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_DEBUG_DATA, this.debugData);
      this.tipsPanel = new TipsPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.tipsPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_TIPS_PANEL, this.tipsPanel);
      this.goalsPanel = new GoalsPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.goalsPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_GOALS_PANEL, this.goalsPanel);
      this.consolePanelEnabled = true;
      this.castlePanelEnabled = true;
   }

   public void render(boolean gamePaused) throws SlickException {
      for (GUIPanelBase g : this.allPanelsArray) {
         g.hideButtonsAndMasks();
      }

      MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
      if (gamePaused && !map.isMapLost()) {
         if (this.tipsPanel.getActiveTip() == null) {
            this.pauseBanner.render(this.debug);
            if (this.settingsEnabled) {
               this.settingsPanel.render(this.debug);
            } else {
               this.weatherPanel.render(gamePaused, this.debug);
               this.statisticsPanel.render(gamePaused, this.debug);
               this.mainMenuPanel.render(gamePaused, this.debug);
            }
         } else {
            this.tipsPanel.render(this.debug);
            this.tipsPanel.renderDescriptionToolTip();
         }
      } else if (!this.hideGUI) {
         this.edgeScrolling.render(this.debug);
         this.overlay.render(this.debug);
         this.populationTotalsPanel.render(this.debug);
         this.resourceAndInfluenceBar.render(this.debug);
         this.weatherPanel.render(gamePaused, this.debug);
         this.spellBar.render(this.debug);
         this.systemAndDataBar.render(this.debug);
         this.leftPanel.render(this.debug);
         boolean phoneBuild = touchPanels
            && !(rtr.system.Game.getCS() instanceof rtr.states.PlayState && ((rtr.states.PlayState)rtr.system.Game.getCS()).isSandbox());
         if (!phoneBuild) {
            this.rightPanel.render(this.debug);
         }

         this.harvestAndTerrainBar.render(this.debug);
         if (touchPanels) {
            // The phone UI draws its own build drawer and inspect cards; skipping these panels also removes their
            // click masks. Goal popups keep their usual anchor.
            this.goalPopup.render(this.consolePanel.getY(), this.debug);
         } else {
            if (this.consolePanelEnabled) {
               this.goalPopup.render(this.consolePanel.getY(), this.debug);
               this.consolePanel.render(this.debug);
            }

            if (this.objectPanelEnabled) {
               this.goalPopup.render(this.selectedObjectPanel.getY(), this.debug);
               this.selectedObjectPanel.render(this.debug);
            }

            if (this.mobPanelEnabled) {
               this.goalPopup.render(this.selectedMobPanel.getY(), this.debug);
               this.selectedMobPanel.render(this.debug);
            }
         }

         this.toolsBar.render(this.debug);
         if (this.tipsPanel.getActiveTip() == null && this.castlePanelEnabled && this.object.objectCount(MapTilesLoader.TileSet.CASTLE_1) == 0) {
            this.castlePanel.render(this.debug);
         }

         if (this.spellSelectPanelEnabled) {
            this.spellSelectPanel.render(this.debug);
         }

         if (this.spellDescriptionPanel.getHighlightedSpell() != null) {
            this.spellDescriptionPanel.render(this.debug);
         }

         if (this.mainMenuPanelEnabled) {
            this.mainMenuPanel.render(gamePaused, this.debug);
         }

         if (this.settingsEnabled) {
            this.settingsPanel.render(this.debug);
         }

         if (this.settingsKeyChangeEnabled) {
            this.settingsPanel.renderKeyChange(this.debug);
         }

         if (this.minimapPanelEnabled) {
            this.minimapPanel.render(this.debug);
         }

         if (this.dataViewsPanelEnabled) {
            this.dataViewsPanel.render(this.debug);
         }

         if (this.statisticsPanelEnabled) {
            this.statisticsPanel.render(gamePaused, this.debug);
         }

         if (this.problemPanelPanelEnabled) {
            this.problemPanel.render(this.debug);
         }

         if (this.buildingListPanelEnabled) {
            this.buildingListPanel.render(this.debug);
         }

         if (this.resourceListPanelEnabled) {
            this.resourceListPanel.render(this.debug);
         }

         if (this.populationListPanelEnabled) {
            this.populationListPanel.render(this.debug);
         }

         if (this.villageEfficiencyPanelEnabled) {
            this.villageEfficiencyPanel.render(this.debug);
         }

         if (this.musicPanelEnabled) {
            this.musicPanel.render(this.debug);
         }

         if (this.goalsPanelEnabled) {
            this.goalsPanel.render(this.goalsPanelEnabled, this.debug);
         }

         if (this.leftJobDescriptionPanel.getHighlightedJob() != null) {
            this.leftJobDescriptionPanel.render(this.debug);
         }

         if (this.leftMigrationDescriptionPanel.getHighlightedRegion() != null) {
            this.leftMigrationDescriptionPanel.render(this.debug);
         }

         if (this.rightObjectDescriptionPanel.getHighlightedObject() != null) {
            this.rightObjectDescriptionPanel.render(this.debug);
         }

         if (this.youLosePanelEnabled) {
            this.youLosePanel.render(this.debug);
         }

         this.overlay.renderToolTip(this.debug);

         for (GUIPanelBase g : this.allPanelsArray) {
            g.renderDescriptionToolTip();
         }

         this.positionArrows.render(this.debug);
         if (SettingsParser.canUseBannerMessage()) {
            Graphics g = new Graphics();
            if (!SettingsParser.getBannerMessage1().equals("")) {
               g.setColor(new Color(0, 0, 0, 100));
               int fontWidth = Text.getWidth(SettingsParser.getBannerMessage1(), Text.FontType.HEADER, 6) + 32;
               int fontHeight = Text.getHeight(SettingsParser.getBannerMessage1(), Text.FontType.HEADER, 6);
               g.fillRect(ScaleControl.getInterfaceWidth() / 2 - fontWidth / 2, this.spellBar.getY() + 63, fontWidth, fontHeight);
               this.font
                  .drawString(
                     ScaleControl.getInterfaceWidth() / 2, this.spellBar.getY() + 65, SettingsParser.getBannerMessage1(), Text.FontType.HEADER, 6, true
                  );
            }

            if (!SettingsParser.getBannerMessage2().equals("")) {
               g.setColor(new Color(0, 0, 0, 100));
               int fontWidth = Text.getWidth(SettingsParser.getBannerMessage2(), Text.FontType.HEADER, 6) + 32;
               int fontHeight = Text.getHeight(SettingsParser.getBannerMessage2(), Text.FontType.HEADER, 6);
               g.fillRect(ScaleControl.getInterfaceWidth() / 2 - fontWidth / 2, this.spellBar.getY() + 111, fontWidth, fontHeight);
               this.font
                  .drawString(
                     ScaleControl.getInterfaceWidth() / 2, this.spellBar.getY() + 112, SettingsParser.getBannerMessage2(), Text.FontType.HEADER, 6, true
                  );
            }
         }

         if (this.debug) {
            for (GUIPanelBase gx : this.allPanelsArray) {
               gx.renderMasks();
            }

            this.debugData.render(65.0F, 30.0F);
         }
      }
   }

   @Override
   public void update() throws SlickException {
      super.update();
      this.guiData.update();
   }

   public void setSelectedMob(MobBase m) {
      this.guiData.setSelectedObject(null);
      this.guiData.setSelectedMob(m);
      this.closeBottomPanels();
      this.mobPanelEnabled = true;
   }

   public void setSelectedObject(ObjectBase o) {
      this.guiData.setSelectedObject(o);
      this.guiData.setSelectedMob(null);
      this.closeBottomPanels();
      this.objectPanelEnabled = true;
   }

   public void deselectAllSelected() {
      this.guiData.setSelectedObject(null);
      this.guiData.setSelectedMob(null);
      this.closeBottomPanels();
      this.consolePanelEnabled = true;
   }

   public void resetGUI() {
      this.closeTopPanels();
      this.closeBottomPanels();
      this.consolePanelEnabled = true;
   }

   public void toggleSpellSelectPanel() {
      if (this.spellSelectPanelEnabled) {
         this.spellSelectPanelEnabled = !this.spellSelectPanelEnabled;
      } else {
         this.closeTopPanels();
         this.spellSelectPanelEnabled = !this.spellSelectPanelEnabled;
      }
   }

   public void toggleMainMenuPanel() {
      if (this.mainMenuPanelEnabled) {
         this.mainMenuPanelEnabled = !this.mainMenuPanelEnabled;
      } else {
         this.closeTopPanels();
         this.mainMenuPanelEnabled = !this.mainMenuPanelEnabled;
      }
   }

   public void toggleSettings() {
      if (this.settingsEnabled) {
         this.settingsEnabled = !this.settingsEnabled;
      } else {
         this.closeTopPanels();
         this.settingsEnabled = !this.settingsEnabled;
      }
   }

   public void toggleSettingsKeyChange() {
      if (this.settingsKeyChangeEnabled) {
         this.settingsKeyChangeEnabled = !this.settingsKeyChangeEnabled;
      } else {
         this.closeTopPanels();
         this.settingsKeyChangeEnabled = !this.settingsKeyChangeEnabled;
      }
   }

   public void toggleMinimapPanel() {
      if (this.minimapPanelEnabled) {
         this.minimapPanelEnabled = !this.minimapPanelEnabled;
      } else {
         this.closeTopPanels();
         this.minimapPanelEnabled = !this.minimapPanelEnabled;
      }
   }

   public void toggleStatisticsPanel() {
      if (this.statisticsPanelEnabled) {
         this.statisticsPanelEnabled = !this.statisticsPanelEnabled;
      } else {
         this.closeTopPanels();
         this.statisticsPanelEnabled = !this.statisticsPanelEnabled;
      }
   }

   public void toggleDataViewsPanel() {
      if (this.dataViewsPanelEnabled) {
         this.dataViewsPanelEnabled = !this.dataViewsPanelEnabled;
      } else {
         this.closeTopPanels();
         this.dataViewsPanelEnabled = !this.dataViewsPanelEnabled;
      }
   }

   public void toggleProblemPanelPanel() {
      if (this.problemPanelPanelEnabled) {
         this.problemPanelPanelEnabled = !this.problemPanelPanelEnabled;
      } else {
         this.closeTopPanels();
         this.problemPanelPanelEnabled = !this.problemPanelPanelEnabled;
      }
   }

   public void toggleBuildingListPanel() {
      if (this.buildingListPanelEnabled) {
         this.buildingListPanelEnabled = !this.buildingListPanelEnabled;
      } else {
         this.closeTopPanels();
         this.buildingListPanelEnabled = !this.buildingListPanelEnabled;
      }
   }

   public void toggleResourceListPanel() {
      if (this.resourceListPanelEnabled) {
         this.resourceListPanelEnabled = !this.resourceListPanelEnabled;
      } else {
         this.closeTopPanels();
         this.resourceListPanelEnabled = !this.resourceListPanelEnabled;
      }
   }

   public void togglePopulationListPanel() {
      if (this.populationListPanelEnabled) {
         this.populationListPanelEnabled = !this.populationListPanelEnabled;
      } else {
         this.closeTopPanels();
         this.populationListPanelEnabled = !this.populationListPanelEnabled;
      }
   }

   public void toggleYouLosePanel() {
      if (this.youLosePanelEnabled) {
         this.youLosePanelEnabled = !this.youLosePanelEnabled;
      } else {
         this.closeTopPanels();
         this.closeBottomPanels();
         this.youLosePanelEnabled = !this.youLosePanelEnabled;
      }
   }

   public void toggleVillageEfficiencyPanel() {
      if (this.villageEfficiencyPanelEnabled) {
         this.villageEfficiencyPanelEnabled = !this.villageEfficiencyPanelEnabled;
      } else {
         this.closeTopPanels();
         this.villageEfficiencyPanelEnabled = !this.villageEfficiencyPanelEnabled;
      }
   }

   public void toggleMusicPanel() {
      if (this.musicPanelEnabled) {
         this.musicPanelEnabled = !this.musicPanelEnabled;
      } else {
         this.closeTopPanels();
         this.musicPanelEnabled = !this.musicPanelEnabled;
      }
   }

   public void toggleGoalsPanel() {
      if (this.goalsPanelEnabled) {
         this.closeTopPanels();
      } else {
         this.closeTopPanels();
         this.goalsPanelEnabled = true;
      }
   }

   private void closeTopPanels() {
      this.spellSelectPanelEnabled = false;
      this.mainMenuPanelEnabled = false;
      this.mainMenuPanel.resetAttemptExit();
      this.mainMenuPanel.resetAttemptLeaveRegion();
      this.settingsEnabled = false;
      this.settingsKeyChangeEnabled = false;
      this.minimapPanelEnabled = false;
      this.statisticsPanelEnabled = false;
      this.dataViewsPanelEnabled = false;
      this.problemPanelPanelEnabled = false;
      this.buildingListPanelEnabled = false;
      this.resourceListPanelEnabled = false;
      this.populationListPanelEnabled = false;
      this.villageEfficiencyPanelEnabled = false;
      this.musicPanelEnabled = false;
      this.goalsPanelEnabled = false;
   }

   private void closeBottomPanels() {
      this.consolePanelEnabled = false;
      this.objectPanelEnabled = false;
      this.selectedObjectPanel.resetAttemptDismantle();
      this.selectedObjectPanel.resetAttemptUpgrade();
      this.mobPanelEnabled = false;
   }

   public boolean isTabsClosed() {
      return !this.spellSelectPanelEnabled
         && !this.mainMenuPanelEnabled
         && !this.settingsEnabled
         && !this.settingsKeyChangeEnabled
         && !this.minimapPanelEnabled
         && !this.statisticsPanelEnabled
         && !this.dataViewsPanelEnabled
         && !this.problemPanelPanelEnabled
         && !this.buildingListPanelEnabled
         && !this.resourceListPanelEnabled
         && !this.populationListPanelEnabled
         && !this.villageEfficiencyPanelEnabled
         && !this.musicPanelEnabled
         && !this.objectPanelEnabled
         && !this.selectedObjectPanel.isAttemptDismantle()
         && !this.selectedObjectPanel.isAttemptUpgrade()
         && !this.mobPanelEnabled;
   }

   public void hideCastlePanel() {
      this.castlePanelEnabled = false;
   }

   public PlayStateGUIData getGUIData() {
      return this.guiData;
   }

   public Overlay getOverlay() {
      return this.overlay;
   }

   public BuildingListPanel getBuildingListPanel() {
      return this.buildingListPanel;
   }

   public ResourceListPanel getResourceListPanel() {
      return this.resourceListPanel;
   }

   public CastlePanel getCastlePanel() {
      return this.castlePanel;
   }

   public ConsolePanel getConsolePanel() {
      return this.consolePanel;
   }

   public HarvestAndTerrainBar getHarvestAndTerrainBar() {
      return this.harvestAndTerrainBar;
   }

   public LeftJobDescriptionPanel getLeftJobDescriptionPanel() {
      return this.leftJobDescriptionPanel;
   }

   public LeftMigrationDescriptionPanel getLeftMigrationDescriptionPanel() {
      return this.leftMigrationDescriptionPanel;
   }

   public LeftPanel getLeftPanel() {
      return this.leftPanel;
   }

   public MainMenuPanel getMainMenuPanel() {
      return this.mainMenuPanel;
   }

   public PopulationListPanel getPopulationListPanel() {
      return this.populationListPanel;
   }

   public PopulationTotalsPanel getPopulationTotalsPanel() {
      return this.populationTotalsPanel;
   }

   public PositionArrows getPositionArrows() {
      return this.positionArrows;
   }

   public ProblemPanel getProblemPanel() {
      return this.problemPanel;
   }

   public ResourceAndInfluenceBar getResourceAndInfluenceBar() {
      return this.resourceAndInfluenceBar;
   }

   public RightPanel getRightPanel() {
      return this.rightPanel;
   }

   public SelectedMobPanel getSelectedMobPanel() {
      return this.selectedMobPanel;
   }

   public SelectedObjectPanel getSelectedObjectPanel() {
      return this.selectedObjectPanel;
   }

   public SelectedObjectUpgradePanel getSelectedObjectUpgradePanel1() {
      return this.selectedObjectUpgradePanel1;
   }

   public SelectedObjectUpgradePanel getSelectedObjectUpgradePanel2() {
      return this.selectedObjectUpgradePanel2;
   }

   public SelectedObjectUpgradePanel getSelectedObjectUpgradePanel3() {
      return this.selectedObjectUpgradePanel3;
   }

   public SpellBar getSpellBar() {
      return this.spellBar;
   }

   public SpellSelectPanel getSpellSelectPanel() {
      return this.spellSelectPanel;
   }

   public SystemAndDataBar getSystemAndDataBar() {
      return this.systemAndDataBar;
   }

   public ToolsBar getToolsBar() {
      return this.toolsBar;
   }

   public VillageEfficiencyPanel getVillageEfficiencyPanel() {
      return this.villageEfficiencyPanel;
   }

   public WeatherPanel getWeatherPanel() {
      return this.weatherPanel;
   }

   public YouLosePanel getYouLosePanel() {
      return this.youLosePanel;
   }

   public StatisticsPanel getStatisticsPanel() {
      return this.statisticsPanel;
   }

   public DataViewsPanel getDataViewsPanel() {
      return this.dataViewsPanel;
   }

   public RightObjectDescriptionPanel getRightObjectDescriptionPanel() {
      return this.rightObjectDescriptionPanel;
   }

   public EdgeScrolling getEdgeScrolling() {
      return this.edgeScrolling;
   }

   public MinimapPanel getMinimapPanel() {
      return this.minimapPanel;
   }

   public MusicPanel getMusicPanel() {
      return this.musicPanel;
   }

   public SettingsPanel getSettingsPanel() {
      return this.settingsPanel;
   }

   public TipsPanel getTipsPanel() {
      return this.tipsPanel;
   }

   public GoalsPanel getGoalsPanel() {
      return this.goalsPanel;
   }

   public boolean isConsolePanelEnabled() {
      return this.consolePanelEnabled;
   }

   public boolean isObjectPanelEnabled() {
      return this.objectPanelEnabled;
   }

   public boolean isMobPanelEnabled() {
      return this.mobPanelEnabled;
   }

   public boolean isCastlePanelEnabled() {
      return this.castlePanelEnabled;
   }

   public boolean isSpellSelectPanelEnabled() {
      return this.spellSelectPanelEnabled;
   }

   public boolean isMainMenuPanelEnabled() {
      return this.mainMenuPanelEnabled;
   }

   public boolean isProblemPanelPanelEnabled() {
      return this.problemPanelPanelEnabled;
   }

   public boolean isBuildingListPanelEnabled() {
      return this.buildingListPanelEnabled;
   }

   public boolean isResourceListPanelEnabled() {
      return this.resourceListPanelEnabled;
   }

   public boolean isPopulationListPanelEnabled() {
      return this.populationListPanelEnabled;
   }

   public boolean isVillageEfficiencyPanelEnabled() {
      return this.villageEfficiencyPanelEnabled;
   }

   public boolean isJobDescriptionTabJob() {
      return this.jobDescriptionTabJob;
   }

   public boolean isYouLosePanelEnabled() {
      return this.youLosePanelEnabled;
   }

   public boolean isStatisticsPanelEnabled() {
      return this.statisticsPanelEnabled;
   }

   public boolean isDataViewsPanelEnabled() {
      return this.dataViewsPanelEnabled;
   }

   public boolean isMinimapPanelEnabled() {
      return this.minimapPanelEnabled;
   }

   public boolean isMusicPanelEnabled() {
      return this.musicPanelEnabled;
   }

   public boolean isSettingsEnabled() {
      return this.settingsEnabled;
   }

   public boolean isSettingsKeyChangeEnabled() {
      return this.settingsKeyChangeEnabled;
   }

   public boolean isGoalsPanelEnabled() {
      return this.goalsPanelEnabled;
   }
}
