package rtr.gui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.gui.states.mapeditor.MainMenuPanel;
import rtr.gui.states.mapeditor.Overlay;
import rtr.gui.states.mapeditor.RegionalDataPanel;
import rtr.gui.states.mapeditor.RightPanel;
import rtr.gui.states.mapeditor.SystemAndDataBar;
import rtr.gui.states.mapeditor.TerrainBar;
import rtr.gui.states.mapeditor.WorldMapListPanel;
import rtr.gui.states.shared.DataViewsPanel;
import rtr.gui.states.shared.DebugData;
import rtr.gui.states.shared.EdgeScrolling;
import rtr.gui.states.shared.MinimapPanel;
import rtr.gui.states.shared.MusicPanel;
import rtr.gui.states.shared.RightObjectDescriptionPanel;
import rtr.gui.states.shared.SettingsPanel;

public class MapEditorGUIController extends GUIControllerBase {
   private MapEditorGUIData guiData = new MapEditorGUIData();
   private Overlay overlay;
   private TerrainBar harvestAndTerrainBar;
   private MainMenuPanel mainMenuPanel;
   private RightPanel rightPanel;
   private SystemAndDataBar systemAndDataBar;
   private RegionalDataPanel regionalDataPanel;
   private WorldMapListPanel worldMapListPanel;
   private DataViewsPanel dataViewsPanel;
   private EdgeScrolling edgeScrolling;
   private MinimapPanel minimapPanel;
   private MusicPanel musicPanel;
   private RightObjectDescriptionPanel rightObjectDescriptionPanel;
   private SettingsPanel settingsPanel;
   private DebugData debugData;
   private boolean mainMenuPanelEnabled;
   private boolean minimapPanelEnabled;
   private boolean settingsEnabled;
   private boolean settingsKeyChangeEnabled;
   private boolean dataViewsPanelEnabled;
   private boolean musicPanelEnabled;
   private boolean regionalDataPanelEnabled;
   private boolean worldMapListPanelEnabled;

   public MapEditorGUIController(Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      this.overlay = new Overlay(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.overlay);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAP_EDITOR_OVERLAY, this.overlay);
      this.harvestAndTerrainBar = new TerrainBar(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.harvestAndTerrainBar);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, this.harvestAndTerrainBar);
      this.mainMenuPanel = new MainMenuPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.mainMenuPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAP_EDITOR_MAIN_MENU_PANEL, this.mainMenuPanel);
      this.rightPanel = new RightPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.rightPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, this.rightPanel);
      this.systemAndDataBar = new SystemAndDataBar(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.systemAndDataBar);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAP_EDITOR_SYSTEM_AND_DATA_BAR, this.systemAndDataBar);
      this.regionalDataPanel = new RegionalDataPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.regionalDataPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAP_EDITOR_REGIONAL_DATA_PANEL, this.regionalDataPanel);
      this.worldMapListPanel = new WorldMapListPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.worldMapListPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAP_EDITOR_WORLD_MAP_LIST_PANEL, this.worldMapListPanel);
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
   }

   public void render() throws SlickException {
      for (GUIPanelBase g : this.allPanelsArray) {
         g.hideButtonsAndMasks();
      }

      if (!this.hideGUI) {
         this.edgeScrolling.render(this.debug);
         this.overlay.render(this.debug);
         this.systemAndDataBar.render(this.debug);
         this.rightPanel.render(this.debug);
         this.harvestAndTerrainBar.render(this.debug);
         if (this.mainMenuPanelEnabled) {
            this.mainMenuPanel.render(this.debug);
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

         if (this.musicPanelEnabled) {
            this.musicPanel.render(this.debug);
         }

         if (this.regionalDataPanelEnabled) {
            this.regionalDataPanel.render(this.debug);
         }

         if (this.worldMapListPanelEnabled) {
            this.worldMapListPanel.render(this.debug);
         }

         if (this.rightObjectDescriptionPanel.getHighlightedObject() != null) {
            this.rightObjectDescriptionPanel.render(this.debug);
         }

         this.overlay.renderToolTip(this.debug);

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
      this.closeTopPanels();
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

   public void toggleDataViewsPanel() {
      if (this.dataViewsPanelEnabled) {
         this.dataViewsPanelEnabled = !this.dataViewsPanelEnabled;
      } else {
         this.closeTopPanels();
         this.dataViewsPanelEnabled = !this.dataViewsPanelEnabled;
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

   public void toggleRegionalDataPanel() {
      if (this.regionalDataPanelEnabled) {
         this.regionalDataPanelEnabled = !this.regionalDataPanelEnabled;
      } else {
         this.closeTopPanels();
         this.regionalDataPanelEnabled = !this.regionalDataPanelEnabled;
      }
   }

   public void toggleWorldMapListPanel() {
      if (this.worldMapListPanelEnabled) {
         this.worldMapListPanelEnabled = !this.worldMapListPanelEnabled;
      } else {
         this.closeTopPanels();
         this.worldMapListPanelEnabled = !this.worldMapListPanelEnabled;
      }
   }

   private void closeTopPanels() {
      this.mainMenuPanelEnabled = false;
      this.mainMenuPanel.resetAttemptExit();
      this.settingsEnabled = false;
      this.settingsKeyChangeEnabled = false;
      this.minimapPanelEnabled = false;
      this.dataViewsPanelEnabled = false;
      this.musicPanelEnabled = false;
      this.regionalDataPanelEnabled = false;
      this.worldMapListPanelEnabled = false;
      this.regionalDataPanel.resetWorldMapInput();
   }

   public boolean isTabsClosed() {
      return !this.mainMenuPanelEnabled
         && !this.settingsEnabled
         && !this.settingsKeyChangeEnabled
         && !this.minimapPanelEnabled
         && !this.dataViewsPanelEnabled
         && !this.musicPanelEnabled
         && !this.regionalDataPanelEnabled
         && !this.worldMapListPanelEnabled;
   }

   public MapEditorGUIData getGUIData() {
      return this.guiData;
   }

   public Overlay getOverlay() {
      return this.overlay;
   }

   public TerrainBar getHarvestAndTerrainBar() {
      return this.harvestAndTerrainBar;
   }

   public MainMenuPanel getMainMenuPanel() {
      return this.mainMenuPanel;
   }

   public RightPanel getRightPanel() {
      return this.rightPanel;
   }

   public SystemAndDataBar getSystemAndDataBar() {
      return this.systemAndDataBar;
   }

   public RegionalDataPanel getRegionalDataPanel() {
      return this.regionalDataPanel;
   }

   public WorldMapListPanel getWorldMapListPanel() {
      return this.worldMapListPanel;
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

   public DataViewsPanel getDataViewPanel() {
      return this.dataViewsPanel;
   }

   public MusicPanel getMusicPanel() {
      return this.musicPanel;
   }

   public SettingsPanel getSettingsPanel() {
      return this.settingsPanel;
   }

   public boolean isMainMenuPanelEnabled() {
      return this.mainMenuPanelEnabled;
   }

   public boolean isRegionalDataPanelEnabled() {
      return this.regionalDataPanelEnabled;
   }

   public boolean isWorldMapListPanelEnabled() {
      return this.worldMapListPanelEnabled;
   }

   public boolean isSettingsEnabled() {
      return this.settingsEnabled;
   }

   public boolean isSettingsKeyChangeEnabled() {
      return this.settingsKeyChangeEnabled;
   }

   public boolean isMinimapPanelEnabled() {
      return this.minimapPanelEnabled;
   }

   public boolean isDataViewsPanelEnabled() {
      return this.dataViewsPanelEnabled;
   }

   public boolean isMusicPanelEnabled() {
      return this.musicPanelEnabled;
   }
}
