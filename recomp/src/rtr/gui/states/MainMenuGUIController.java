package rtr.gui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ProfileModule;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.states.mainmenu.Credits;
import rtr.gui.states.mainmenu.DeleteProfilePanel;
import rtr.gui.states.mainmenu.DisclaimerPanel;
import rtr.gui.states.mainmenu.ExitGamePanel;
import rtr.gui.states.mainmenu.LanguageSelectPanel;
import rtr.gui.states.mainmenu.Logo;
import rtr.gui.states.mainmenu.MainMenuPanel;
import rtr.gui.states.mainmenu.MapEditorLoadMapDeleteWarningPanel;
import rtr.gui.states.mainmenu.MapEditorLoadMapPanel;
import rtr.gui.states.mainmenu.MapEditorNewMapDeleteWarningPanel;
import rtr.gui.states.mainmenu.MapEditorNewMapPanel;
import rtr.gui.states.mainmenu.NewProfilePanel;
import rtr.gui.states.mainmenu.SelectProfilePanel;
import rtr.gui.states.mainmenu.SkirmishNewGameWarningPanel;
import rtr.gui.states.mainmenu.SkirmishPanel;
import rtr.gui.states.mainmenu.UploadProfilePanel;
import rtr.gui.states.mainmenu.WarningPanel;
import rtr.gui.states.shared.DebugData;
import rtr.gui.states.shared.SettingsPanel;
import rtr.system.Game;
import rtr.system.ScaleControl;

public class MainMenuGUIController extends GUIControllerBase {
   private MainMenuGUIData guiData = new MainMenuGUIData();
   private Credits credits;
   private DeleteProfilePanel deleteProfilePanel;
   private DisclaimerPanel disclaimerPanel;
   private WarningPanel warningPanel;
   private LanguageSelectPanel languageSelectPanel;
   private Logo logo;
   private MainMenuPanel mainMenuPanel;
   private MapEditorLoadMapDeleteWarningPanel mapEditorLoadMapDeleteWarningPanel;
   private MapEditorLoadMapPanel mapEditorLoadMapPanel;
   private MapEditorNewMapDeleteWarningPanel mapEditorNewMapDeleteWarningPanel;
   private MapEditorNewMapPanel mapEditorNewMapPanel;
   private NewProfilePanel newProfilePanel;
   private ExitGamePanel exitGamePanel;
   private SelectProfilePanel selectProfilePanel;
   private SkirmishNewGameWarningPanel skirmishNewGameWarningPanel;
   private SkirmishPanel skirmishPanel;
   private UploadProfilePanel uploadProfilePanel;
   private SettingsPanel settingsPanel;
   private DebugData debugData;
   private boolean mainEnabled;
   private boolean disclaimerEnabled;
   private boolean warningEnabled;
   private boolean languageSelectEnabled;
   private boolean selectProfileEnabled;
   private boolean uploadProfileEnabled;
   private boolean newProfileEnabled;
   private boolean exitGamePanelEnabled;
   private boolean deleteProfileEnabled;
   private boolean skirmishEnabled;
   private boolean skirmishNewGameWarningEnabled;
   private boolean mapEditorLoadMapEnabled;
   private boolean mapEditorLoadMapDeleteWarningEnabled;
   private boolean mapEditorNewMapEnabled;
   private boolean mapEditorNewMapDeleteWarningEnabled;
   private boolean creditsEnabled;
   private boolean settingsEnabled;
   private boolean settingsKeyChangeEnabled;

   public MainMenuGUIController(Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      this.credits = new Credits(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.credits);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_CREDITS, this.credits);
      this.deleteProfilePanel = new DeleteProfilePanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.deleteProfilePanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_DELETE_PROFILE_PANEL, this.deleteProfilePanel);
      this.disclaimerPanel = new DisclaimerPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.disclaimerPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_DISCLAIMER_PANEL, this.disclaimerPanel);
      this.warningPanel = new WarningPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.warningPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_WARNING_PANEL, this.warningPanel);
      this.languageSelectPanel = new LanguageSelectPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.languageSelectPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_LANGUAGE_SELECT_PANEL, this.languageSelectPanel);
      this.logo = new Logo(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.logo);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_LOGO, this.logo);
      this.mainMenuPanel = new MainMenuPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.mainMenuPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, this.mainMenuPanel);
      this.mapEditorLoadMapDeleteWarningPanel = new MapEditorLoadMapDeleteWarningPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.mapEditorLoadMapDeleteWarningPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_LOAD_MAP_DELETE_WARNING_PANEL, this.mapEditorLoadMapDeleteWarningPanel);
      this.mapEditorLoadMapPanel = new MapEditorLoadMapPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.mapEditorLoadMapPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_LOAD_MAP_PANEL, this.mapEditorLoadMapPanel);
      this.mapEditorNewMapDeleteWarningPanel = new MapEditorNewMapDeleteWarningPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.mapEditorNewMapDeleteWarningPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_DELETE_WARNING_PANEL, this.mapEditorNewMapDeleteWarningPanel);
      this.mapEditorNewMapPanel = new MapEditorNewMapPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.mapEditorNewMapPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, this.mapEditorNewMapPanel);
      this.newProfilePanel = new NewProfilePanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.newProfilePanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_NEW_PROFILE_PANEL, this.newProfilePanel);
      this.exitGamePanel = new ExitGamePanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.exitGamePanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_RESTART_GAME_PANEL, this.exitGamePanel);
      this.selectProfilePanel = new SelectProfilePanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.selectProfilePanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_SELECT_PROFILE_PANEL, this.selectProfilePanel);
      this.skirmishNewGameWarningPanel = new SkirmishNewGameWarningPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.skirmishNewGameWarningPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_NEW_GAME_WARNING_PANEL, this.skirmishNewGameWarningPanel);
      this.skirmishPanel = new SkirmishPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.skirmishPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_PANEL, this.skirmishPanel);
      this.uploadProfilePanel = new UploadProfilePanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.uploadProfilePanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, this.uploadProfilePanel);
      this.settingsPanel = new SettingsPanel(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.settingsPanel);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, this.settingsPanel);
      this.debugData = new DebugData(this, this.guiData, g, gc, mouse);
      this.allPanelsArray.add(this.debugData);
      this.allPanelsMap.put(GUIControllerBase.GUIPanel.SHARED_DEBUG_DATA, this.debugData);
      if (!this.disclaimerPanel.isDisclaimerDisplayed()
         && !SettingsParser.getHideDisclaimer()
         && !Game.isNoneSteamMode()
         && Text.getLoadedTemplate().getFontName(Text.FontType.BODY).equals("")) {
         this.disclaimerEnabled = true;
         this.disclaimerPanel.setDisclaimerDisplayed(true);
      } else if (ProfileModule.getCurrentProfileSlot() != -1) {
         if (!this.profile.getWorldMapVersion().equals(Game.getVersion())) {
            this.warningEnabled = true;
         } else {
            this.mainEnabled = true;
         }
      } else {
         this.selectProfileEnabled = true;
      }
   }

   public void render() throws SlickException {
      for (GUIPanelBase g : this.allPanelsArray) {
         g.hideButtonsAndMasks();
      }

      if (!this.hideGUI) {
         if (!this.deleteProfileEnabled
            && !this.newProfileEnabled
            && !this.exitGamePanelEnabled
            && !this.selectProfileEnabled
            && !this.creditsEnabled
            && !this.disclaimerEnabled
            && !this.warningEnabled
            && !this.languageSelectEnabled) {
            if (this.mainEnabled) {
               this.logo.render();
               this.mainMenuPanel.render(false, this.debug);
            } else {
               this.mainMenuPanel.render(true, this.debug);
            }
         }

         if (this.disclaimerEnabled) {
            this.disclaimerPanel.render(this.debug);
         }

         if (this.warningEnabled) {
            this.warningPanel.render(this.debug);
         }

         if (this.languageSelectEnabled) {
            this.languageSelectPanel.render(this.debug);
         }

         if (this.selectProfileEnabled) {
            this.logo.render();
            this.selectProfilePanel.render(this.debug);
         }

         if (this.uploadProfileEnabled) {
            this.uploadProfilePanel.render(this.debug);
         }

         if (this.deleteProfileEnabled) {
            this.deleteProfilePanel.render(this.debug);
         }

         if (this.newProfileEnabled) {
            this.newProfilePanel.render(this.debug);
         }

         if (this.exitGamePanelEnabled) {
            this.exitGamePanel.render(this.debug);
         }

         if (this.skirmishEnabled) {
            this.mainMenuPanel.deselectAll();
            this.mainMenuPanel.select("skirmish");
            this.skirmishPanel.render(this.debug);
         }

         if (this.skirmishNewGameWarningEnabled) {
            this.skirmishNewGameWarningPanel.render(this.debug);
         }

         if (this.mapEditorLoadMapEnabled) {
            this.mainMenuPanel.deselectAll();
            this.mainMenuPanel.select("loadMap");
            this.mapEditorLoadMapPanel.render(this.debug);
         }

         if (this.mapEditorLoadMapDeleteWarningEnabled) {
            this.mapEditorLoadMapDeleteWarningPanel.render(this.debug);
         }

         if (this.mapEditorNewMapEnabled) {
            this.mainMenuPanel.deselectAll();
            this.mainMenuPanel.select("newMap");
            this.mapEditorNewMapPanel.render(this.debug);
         }

         if (this.mapEditorNewMapDeleteWarningEnabled) {
            this.mapEditorNewMapDeleteWarningPanel.render(this.debug);
         }

         if (this.settingsEnabled) {
            this.mainMenuPanel.deselectAll();
            this.mainMenuPanel.select("settings");
            this.settingsPanel.render(this.debug);
         }

         if (this.settingsKeyChangeEnabled) {
            this.settingsPanel.renderKeyChange(this.debug);
         }

         if (this.creditsEnabled) {
            this.credits.render(this.debug);
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
   }

   public void toggleMain() {
      this.closeAll();
      this.mainEnabled = true;
   }

   public void toggleWarning() {
      this.closeAll();
      this.warningEnabled = true;
   }

   public void toggleSelectProfile() {
      this.closeAll();
      this.selectProfileEnabled = true;
   }

   public void toggleUploadProfile() {
      this.closeAll();
      this.uploadProfileEnabled = true;
   }

   public void toggleNewProfile() {
      this.closeAll();
      this.newProfileEnabled = true;
   }

   public void toggleExitGamePanel() {
      this.closeAll();
      this.exitGamePanelEnabled = true;
   }

   public void toggleDeleteProfile() {
      this.closeAll();
      this.deleteProfileEnabled = true;
   }

   public void toggleSkirmish() {
      this.closeAll();
      this.skirmishEnabled = true;
   }

   public void toggleSkirmishNewGameWarning() {
      this.closeAll();
      this.skirmishNewGameWarningEnabled = true;
   }

   public void toggleMapEditorLoadMap() {
      this.closeAll();
      this.mapEditorLoadMapEnabled = true;
   }

   public void toggleMapEditorLoadMapDeleteWarning() {
      this.closeAll();
      this.mapEditorLoadMapDeleteWarningEnabled = true;
   }

   public void toggleMapEditorNewMap() {
      this.closeAll();
      this.mapEditorNewMapEnabled = true;
   }

   public void toggleMapEditorNewMapDeleteWarning() {
      this.closeAll();
      this.mapEditorNewMapDeleteWarningEnabled = true;
   }

   public void toggleSettings() {
      this.closeAll();
      this.settingsEnabled = true;
   }

   public void toggleSettingsKeyChange() {
      this.closeAll();
      this.settingsKeyChangeEnabled = true;
   }

   public void toggleCredits() {
      this.closeAll();
      this.credits.setRollY(ScaleControl.getInterfaceHeight());
      this.creditsEnabled = true;
   }

   public void toggleLanguageSelect() {
      this.closeAll();
      this.languageSelectEnabled = true;
   }

   private void closeAll() {
      this.mainEnabled = false;
      this.disclaimerEnabled = false;
      this.warningEnabled = false;
      this.languageSelectEnabled = false;
      this.selectProfileEnabled = false;
      this.uploadProfileEnabled = false;
      this.newProfileEnabled = false;
      this.exitGamePanelEnabled = false;
      this.deleteProfileEnabled = false;
      this.skirmishEnabled = false;
      this.skirmishNewGameWarningEnabled = false;
      this.mapEditorLoadMapEnabled = false;
      this.mapEditorLoadMapDeleteWarningEnabled = false;
      this.mapEditorNewMapEnabled = false;
      this.mapEditorNewMapDeleteWarningEnabled = false;
      this.settingsEnabled = false;
      this.settingsKeyChangeEnabled = false;
      this.creditsEnabled = false;
   }

   public MainMenuGUIData getGUIData() {
      return this.guiData;
   }

   public Credits getCredits() {
      return this.credits;
   }

   public DeleteProfilePanel getDeleteProfilePanel() {
      return this.deleteProfilePanel;
   }

   public DisclaimerPanel getDisclaimerPanel() {
      return this.disclaimerPanel;
   }

   public WarningPanel getWarningPanel() {
      return this.warningPanel;
   }

   public LanguageSelectPanel getLanguageSelectPanel() {
      return this.languageSelectPanel;
   }

   public Logo getLogo() {
      return this.logo;
   }

   public MainMenuPanel getMainMenuPanel() {
      return this.mainMenuPanel;
   }

   public MapEditorLoadMapDeleteWarningPanel getMapEditorLoadMapDeleteWarningPanel() {
      return this.mapEditorLoadMapDeleteWarningPanel;
   }

   public MapEditorLoadMapPanel getMapEditorLoadMapPanel() {
      return this.mapEditorLoadMapPanel;
   }

   public MapEditorNewMapDeleteWarningPanel getMapEditorNewMapDeleteWarningPanel() {
      return this.mapEditorNewMapDeleteWarningPanel;
   }

   public MapEditorNewMapPanel getMapEditorNewMapPanel() {
      return this.mapEditorNewMapPanel;
   }

   public NewProfilePanel getNewProfilePanel() {
      return this.newProfilePanel;
   }

   public ExitGamePanel getExitGamePanel() {
      return this.exitGamePanel;
   }

   public SelectProfilePanel getSelectProfilePanel() {
      return this.selectProfilePanel;
   }

   public SkirmishNewGameWarningPanel getSkirmishNewGameWarningPanel() {
      return this.skirmishNewGameWarningPanel;
   }

   public SkirmishPanel getSkirmishPanel() {
      return this.skirmishPanel;
   }

   public UploadProfilePanel getUploadProfilePanel() {
      return this.uploadProfilePanel;
   }

   public SettingsPanel getSettingsPanel() {
      return this.settingsPanel;
   }

   public DebugData getDebugData() {
      return this.debugData;
   }

   public boolean isMainEnabled() {
      return this.mainEnabled;
   }

   public boolean isDisclaimerEnabled() {
      return this.disclaimerEnabled;
   }

   public boolean isWarningEnabled() {
      return this.warningEnabled;
   }

   public boolean isSelectProfileEnabled() {
      return this.selectProfileEnabled;
   }

   public boolean isUploadProfileEnabled() {
      return this.uploadProfileEnabled;
   }

   public boolean isNewProfileEnabled() {
      return this.newProfileEnabled;
   }

   public boolean isExitGamePanelEnabled() {
      return this.exitGamePanelEnabled;
   }

   public boolean isDeleteProfileEnabled() {
      return this.deleteProfileEnabled;
   }

   public boolean isSkirmishEnabled() {
      return this.skirmishEnabled;
   }

   public boolean isSkirmishNewGameWarningEnabled() {
      return this.skirmishNewGameWarningEnabled;
   }

   public boolean isMapEditorLoadMapEnabled() {
      return this.mapEditorLoadMapEnabled;
   }

   public boolean isMapEditorLoadMapDeleteWarningEnabled() {
      return this.mapEditorLoadMapDeleteWarningEnabled;
   }

   public boolean isMapEditorNewMapEnabled() {
      return this.mapEditorNewMapEnabled;
   }

   public boolean isMapEditorNewMapDeleteWarningEnabled() {
      return this.mapEditorNewMapDeleteWarningEnabled;
   }

   public boolean isCreditsEnabled() {
      return this.creditsEnabled;
   }

   public boolean isSettingsEnabled() {
      return this.settingsEnabled;
   }

   public boolean isSettingsKeyChangeEnabled() {
      return this.settingsKeyChangeEnabled;
   }
}
