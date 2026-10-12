package rtr.states;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import rtr.DataModule;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.gui.GUIEnums;
import rtr.gui.states.BrushLoader;
import rtr.gui.states.GUIControllerBase;
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.shared.EdgeScrolling;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.objects.ObjectBase;
import rtr.road.RoadModule;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class MapEditorState extends StateBase {
   private MapEditorGUIController gui;

   public MapEditorState(int s) {
      super(s);
      this.gameMode = GameModeTemplateBase.GameMode.MAP_EDITOR;
      this.loadModuleMap.put(ModuleBase.ModuleType.BACKGROUND, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.COLLISION, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.DATA, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.FONT, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.LIGHTING, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MAP, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MINI_MAP, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.OBJECT, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.PARTICLE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.SAVE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.SHADOW, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.SOUND, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.TIME, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.TRANSITION, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.WEATHER, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.CLOUD, true);
   }

   @Override
   protected void loadState() throws SlickException {
      super.loadState();
      if (this.modulesLoaded) {
         this.loadingRequired = false;
         this.gui = new MapEditorGUIController(this.g, gc, this.mouse);
         this.updateRenderWindow();
         transition.enter();
         this.endTimeTotal = System.currentTimeMillis();
         Console.out("TOTAL TIME: " + (this.endTimeTotal - this.startTimeTotal), true);
         this.currentLoad = Text.getText("module.loading.starting");
      }
   }

   @Override
   public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
      super.render(gc, sbg, g);
      if (this.loadingRequired) {
         this.renderLoadScreen();
      } else {
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
         background.render();
         map.render();
         shadow.render();
         if (data.getDisplayMode() != DataModule.DataDisplayMode.HIDE_TOPOGRAPHY && data.getLockedDisplayMode() != DataModule.DataDisplayMode.HIDE_TOPOGRAPHY) {
            particle.renderBottom();
            object.renderGates();
            object.renderTowers();
            particle.renderTop();
         }

         lighting.render(g);
         cloud.render(g);
         data.render(g, this.gui.isDebug());
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         this.renderGUI();
         this.screenShotListener();
         transition.render(g);
      }
   }

   private void renderGUI() throws SlickException {
      this.gui.render();
      this.mouse.setX(this.mouseX);
      this.mouse.setY(this.mouseY);
      if (!this.gui.isHideGUI()) {
         particle.renderGUI();
      }
   }

   @Override
   public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
      super.update(gc, sbg, delta);
      if (!this.loadingRequired) {
         particle.update();
         map.update();
         map.updateNoSpeed();
         background.update();
         projectile.update();
         object.update(false);
         sound.update();
         lighting.update();
         weather.update();
         cloud.update();
         data.update();
         time.update();
         transition.update();
         this.gui.update();
      }
   }

   @Override
   protected void controlsKeyboard() throws SlickException {
      if (!transition.getControlLockout()) {
         if (this.gui.getSettingsPanel().isKeyChangeMode()) {
            this.controlsKeyboardSettingsKeyChange();
         } else {
            this.controlsKeyboardScreenshot();
            this.controlsKeyboardFullScreen();
            this.controlsKeyboardMap();
            this.controlsKeyboardSystemAndData();
            this.controlsKeyboardTerrainButtons();
            this.controlsKeyboardOther();
         }
      }
   }

   private void controlsKeyboardSettingsKeyChange() {
      input.pause();
      input.clearKeyPressedRecord();
      if (Keyboard.getEventKeyState()) {
         this.gui.getSettingsPanel().setSettingsProfileSetting(this.gui.getSettingsPanel().getKeyName(), Keyboard.getKeyName(Keyboard.getEventKey()));
         this.gui.toggleSettings();
         this.gui.getSettingsPanel().setKeyChangeMode(false);
         input.resume();
      }
   }

   private void controlsKeyboardScreenshot() throws SlickException {
      if (input.isKeyPressed(SettingsParser.getKeyScreenShot())) {
         if (input.isKeyDown(42)) {
            this.mapWriter();
         } else {
            this.takeScreenShot = true;
         }
      }
   }

   private void controlsKeyboardFullScreen() throws SlickException {
      if (input.isKeyPressed(SettingsParser.getKeyFullScreen()) || (input.isKeyDown(56) || input.isKeyDown(184)) && input.isKeyPressed(28)) {
         this.toggleFullScreen();
         this.updateRenderWindow();
         input.clearKeyPressedRecord();
      }
   }

   private void controlsKeyboardMap() throws SlickException {
      if (input.isKeyDown(SettingsParser.getKeyUp()) || input.isKeyDown(200)) {
         map.moveMapKeyboard(0.0F, 100.0F);
      }

      if (input.isKeyDown(SettingsParser.getKeyDown()) || input.isKeyDown(208)) {
         map.moveMapKeyboard(0.0F, -100.0F);
      }

      if (input.isKeyDown(SettingsParser.getKeyLeft()) || input.isKeyDown(203)) {
         map.moveMapKeyboard(100.0F, 0.0F);
      }

      if (input.isKeyDown(SettingsParser.getKeyRight()) || input.isKeyDown(205)) {
         map.moveMapKeyboard(-100.0F, 0.0F);
      }

      if (input.isKeyPressed(SettingsParser.getKeyZoomIn())) {
         ScaleControl.increaseWorldScale();
      }

      if (input.isKeyPressed(SettingsParser.getKeyZoomOut())) {
         ScaleControl.decreaseWorldScale();
      }
   }

   private void controlsKeyboardSystemAndData() throws SlickException {
      if (input.isKeyPressed(SettingsParser.getKeyMainMenu())) {
         this.gui.toggleMainMenuPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyMinimap())) {
         this.gui.toggleMinimapPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyDataViews())) {
         this.gui.toggleDataViewsPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsKeyboardTerrainButtons() throws SlickException {
      if (input.isKeyPressed(SettingsParser.getKeyCycleBrushType())) {
         if (this.gui.getHarvestAndTerrainBar().getBrush() == BrushLoader.BrushType.ROUND) {
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            this.gui.getBrushLoader().setBrushType(BrushLoader.BrushType.FILLETED_SQUARE);
            this.gui.getHarvestAndTerrainBar().setBrush(BrushLoader.BrushType.FILLETED_SQUARE);
         } else if (this.gui.getHarvestAndTerrainBar().getBrush() == BrushLoader.BrushType.FILLETED_SQUARE) {
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            this.gui.getBrushLoader().setBrushType(BrushLoader.BrushType.CHAMFERED_SQUARE);
            this.gui.getHarvestAndTerrainBar().setBrush(BrushLoader.BrushType.CHAMFERED_SQUARE);
         } else if (this.gui.getHarvestAndTerrainBar().getBrush() == BrushLoader.BrushType.CHAMFERED_SQUARE) {
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            this.gui.getBrushLoader().setBrushType(BrushLoader.BrushType.SQUARE);
            this.gui.getHarvestAndTerrainBar().setBrush(BrushLoader.BrushType.SQUARE);
         } else if (this.gui.getHarvestAndTerrainBar().getBrush() == BrushLoader.BrushType.SQUARE) {
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            this.gui.getBrushLoader().setBrushType(BrushLoader.BrushType.ROUND);
            this.gui.getHarvestAndTerrainBar().setBrush(BrushLoader.BrushType.ROUND);
         }
      }

      if (input.isKeyPressed(SettingsParser.getKeyBrushSizeUp())) {
         this.gui.getBrushLoader().setBrushSizeUp();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyBrushSizeDown())) {
         this.gui.getBrushLoader().setBrushSizeDown();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyDown(SettingsParser.getKeyAccentModeToggle())) {
         map.accentModeOn();
      } else {
         map.accentModeOff();
      }

      if (input.isKeyPressed(SettingsParser.getKeyEraseTool())) {
         this.gui.getHarvestAndTerrainBar().setEraseMode(MapModule.EraseMode.ALL);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
         this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.ALL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseAll");
      }

      if (input.isKeyPressed(SettingsParser.getKeyPatchTool())) {
         this.gui.getHarvestAndTerrainBar().setHolePatchPatch();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.PATCH);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "patch");
      }

      if (input.isKeyPressed(SettingsParser.getKeyHoleTool())) {
         this.gui.getHarvestAndTerrainBar().setHolePatchHole();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.HOLE);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "hole");
      }
   }

   private void controlsKeyboardOther() throws SlickException {
      if (input.isKeyPressed(SettingsParser.getKeyDebug())) {
         this.gui.toggleDebug();
      }

      if (input.isKeyPressed(SettingsParser.getKeyHideGUI())) {
         this.gui.toggleHideGUI();
      }

      if (input.isKeyPressed(SettingsParser.getKeyGrid())) {
         if (data.getDisplayMode() != DataModule.DataDisplayMode.GRID) {
            data.setDisplayMode(DataModule.DataDisplayMode.GRID);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyCancel())) {
         if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.SELECT) {
            if (this.gui.isTabsClosed()) {
               this.gui.toggleMainMenuPanel();
            } else {
               this.gui.resetGUI();
            }
         }

         this.cancelTasks();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyHideTopography())) {
         if (data.getDisplayMode() != DataModule.DataDisplayMode.HIDE_TOPOGRAPHY) {
            data.setDisplayMode(DataModule.DataDisplayMode.HIDE_TOPOGRAPHY);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   @Override
   protected void controlsMouse() throws SlickException {
      super.controlsMouse();
      if (!transition.getControlLockout()) {
         if (SettingsParser.getEdgeScrollingEnabled()) {
            this.controlsEdgeScrolling();
         }

         if (input.isMousePressed(1)) {
            this.buttonCycles();
            if (this.gui.intersectsAnyGUIMask()) {
               return;
            }

            this.mouseXPressed = this.mouseX;
            this.mouseYPressed = this.mouseY;
         }

         if (input.isMouseButtonDown(1)) {
            if (this.gui.intersectsAnyGUIMask()) {
               return;
            }

            this.movingMap = true;
            map.moveMapMouse(this.mouseXPressed, this.mouseYPressed, this.mouseX, this.mouseY);
         } else {
            this.movingMap = false;
         }

         if (input.isMousePressed(0)) {
            if (this.gui.isSettingsEnabled()) {
               this.controlsMousePressedSettingsPanel();
               return;
            }

            this.controlsMousePressedSystemAndData();
            this.controlsMousePressedMainMenuPanel();
            this.controlsMousePressedSettingsKeyChange();
            this.controlsMousePressedMinimapPanel();
            this.controlsMousePressedDataViewsPanel();
            this.controlsMousePressedMusicPanel();
            this.controlsMousePressedRegionalDataPanel();
            this.controlsMousePressedWorldMapListPanel();
            this.controlsMousePressedRightPanel();
            this.controlsMousePressedTerrainBar();
            this.buttonCycles();
            if (this.gui.intersectsAnyGUIMask()) {
               return;
            }
         }

         if (input.isMouseButtonDown(0)) {
            this.controlsMouseDownMinimapPanel();
            if (this.gui.intersectsAnyGUIMask()) {
               return;
            }

            this.controlsMouseDownInterfaceMode();
         }

         if (input.isMousePressed(0)) {
            this.cancelTasks();
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }
   }

   private void controlsEdgeScrolling() {
      int edgeScrollSpeed = SettingsParser.getEdgeScrollSpeed();
      EdgeScrolling edge = this.gui.getEdgeScrolling();
      if (edge.intersectsNorth(this.mouse) && edge.intersectsEast(this.mouse)) {
         map.moveMapKeyboard(edgeScrollSpeed * -1, edgeScrollSpeed);
      } else if (edge.intersectsNorth(this.mouse) && edge.intersectsWest(this.mouse)) {
         map.moveMapKeyboard(edgeScrollSpeed, edgeScrollSpeed);
      } else if (edge.intersectsSouth(this.mouse) && edge.intersectsEast(this.mouse)) {
         map.moveMapKeyboard(edgeScrollSpeed * -1, edgeScrollSpeed * -1);
      } else if (edge.intersectsSouth(this.mouse) && edge.intersectsWest(this.mouse)) {
         map.moveMapKeyboard(edgeScrollSpeed, edgeScrollSpeed * -1);
      } else if (edge.intersectsNorth(this.mouse)) {
         map.moveMapKeyboard(0.0F, edgeScrollSpeed);
      } else if (edge.intersectsEast(this.mouse)) {
         map.moveMapKeyboard(edgeScrollSpeed * -1, 0.0F);
      } else if (edge.intersectsSouth(this.mouse)) {
         map.moveMapKeyboard(0.0F, edgeScrollSpeed * -1);
      } else if (edge.intersectsWest(this.mouse)) {
         map.moveMapKeyboard(edgeScrollSpeed, 0.0F);
      }
   }

   private void controlsMousePressedSystemAndData() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_SYSTEM_AND_DATA_BAR, "mainMenu")) {
         this.gui.toggleMainMenuPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_SYSTEM_AND_DATA_BAR, "saveMap")) {
         this.saveGame();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_SYSTEM_AND_DATA_BAR, "minimap")) {
         this.gui.toggleMinimapPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_SYSTEM_AND_DATA_BAR, "dataViews")) {
         this.gui.toggleDataViewsPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_SYSTEM_AND_DATA_BAR, "hideTopography")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.HIDE_TOPOGRAPHY) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.HIDE_TOPOGRAPHY);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_SYSTEM_AND_DATA_BAR, "music")) {
         this.gui.toggleMusicPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_SYSTEM_AND_DATA_BAR, "regionalData")) {
         this.gui.toggleRegionalDataPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedMainMenuPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_MAIN_MENU_PANEL, "settings")) {
         this.gui.getSettingsPanel().settingsReloadProperties();
         this.gui.toggleSettings();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_MAIN_MENU_PANEL, "saveAndExit")) {
         this.saveGame();
         this.gui.setDebug(false);
         transition.startTransition(Game.getMainMenuState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_MAIN_MENU_PANEL, "exitOnly")) {
         this.gui.getMainMenuPanel().attemptExit();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_MAIN_MENU_PANEL, "exitOnlyYes")) {
         this.gui.setDebug(false);
         transition.startTransition(Game.getMainMenuState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedSettingsPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "ok")) {
         this.gui.toggleMainMenuPanel();

         try {
            File settings = new File("profiles/settings.properties");
            Properties newProperties = this.gui.getSettingsPanel().getSettingsBaseProperties();
            FileOutputStream out = new FileOutputStream(settings);
            newProperties.store(out, "settings");
            out.flush();
            out.close();
         } catch (IOException var5) {
            Console.out("Main Menu failed to save base properties file!", true);
         }

         try {
            File settings = new File("profiles/profile" + Game.getActiveProfile() + "/profileSettings.properties");
            Properties newProperties = this.gui.getSettingsPanel().getSettingsProfileProperties();
            newProperties.setProperty("twitchChannel", this.gui.getSettingsPanel().getSettingsTwitchChannel());
            FileOutputStream out = new FileOutputStream(settings);
            newProperties.store(out, "settings");
            out.flush();
            out.close();
         } catch (IOException var4) {
            Console.out("Main Menu failed to save profile properties file!", true);
         }

         SettingsParser.loadBaseSettings();
         SettingsParser.loadProfileSettings(Game.getActiveProfile());
         Game.reinitDisplay();
         this.updateRenderWindow();
         sound.resetVolume();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "cancel")) {
         this.gui.toggleMainMenuPanel();
         this.gui.getSettingsPanel().settingsReloadProperties();
         sound.resetVolume();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "reset")) {
         this.gui.getSettingsPanel().settingsReset();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "musicUp")) {
         this.gui.getSettingsPanel().increaseSettingsMusicVolume();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "musicDown")) {
         this.gui.getSettingsPanel().decreaseSettingsMusicVolume();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "soundUp")) {
         this.gui.getSettingsPanel().increaseSettingsSoundVolume();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "soundDown")) {
         this.gui.getSettingsPanel().decreaseSettingsSoundVolume();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "mapScrollInverted")) {
         this.gui.getSettingsPanel().settingsToggleMapScroll();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "edgeScrolling")) {
         this.gui.getSettingsPanel().settingsToggleEdgeScrolling();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "edgeScrollingUp")) {
         this.gui.getSettingsPanel().increaseSettingsEdgeScrollingAmount();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "edgeScrollingDown")) {
         this.gui.getSettingsPanel().decreaseSettingsEdgeScrollingAmount();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "fullScreen")) {
         this.gui.getSettingsPanel().settingsToggleFullScreen();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "borderlessFullScreen")) {
         this.gui.getSettingsPanel().settingsToggleBorderlessFullScreen();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "fullScreenResolutionDown")) {
         this.gui.getSettingsPanel().decreaseFullScreenResolution();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "fullScreenResolutionUp")) {
         this.gui.getSettingsPanel().increaseFullScreenResolution();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "vSync")) {
         this.gui.getSettingsPanel().settingsToggleVSync();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "twitchChannel")) {
         this.gui.getSettingsPanel().setFocusSettingsTwitchChannel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "particleAmount")) {
         this.gui.getSettingsPanel().settingsToggleParticleAmount();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "shadows")) {
         this.gui.getSettingsPanel().settingsToggleShadows();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "autosaveFrequency")) {
         this.gui.getSettingsPanel().settingsToggleAutosaveFrequency();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "clouds")) {
         this.gui.getSettingsPanel().settingsToggleClouds();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "background")) {
         this.gui.getSettingsPanel().settingsToggleBackground();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "screenshakeEnabled")) {
         this.gui.getSettingsPanel().settingsToggleScreenshakeEnabled();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "tipsEnabled")) {
         this.gui.getSettingsPanel().settingsToggleTipsEnabled();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "interfaceScaleDown")) {
         this.gui.getSettingsPanel().decreaseSettingsInterfaceScale();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "interfaceScaleUp")) {
         this.gui.getSettingsPanel().increaseSettingsInterfaceScale();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedSettingsKeyChange() throws SlickException {
      if (this.gui.getSettingsPanel().settingsIntersectsKey()) {
         this.gui.toggleSettingsKeyChange();
         String keyName = this.gui.getSettingsPanel().getSettingsKey();
         this.gui.getSettingsPanel().setKeyName(keyName);
         this.gui.getSettingsPanel().setSettingsKeyChangeOldKey(this.gui.getSettingsPanel().getSettingsProfileProperties().getProperty(keyName));
         this.gui.getSettingsPanel().setKeyChangeMode(true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedMinimapPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_MINIMAP_PANEL, "sizeToggleSmall")
         || this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_MINIMAP_PANEL, "sizeToggleLarge")) {
         this.gui.getMinimapPanel().toggleMinimapScale();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedDataViewsPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "resourceValue")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.RESOURCE_VALUE) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.RESOURCE_VALUE);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "movementCost")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.MOVEMENT_COST) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.MOVEMENT_COST);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "blockMap")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.BLOCK_MAP) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.BLOCK_MAP);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "range")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.RANGE_MAP) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.RANGE_MAP);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "desirability")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.DESIRABILITY_MAP) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.DESIRABILITY_MAP);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "towerRange")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.TOWER_RANGE_MAP) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.TOWER_RANGE_MAP);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "lightningRodRange")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.LIGHTNING_ROD_RANGE_MAP) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.LIGHTNING_ROD_RANGE_MAP);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "hideTopography")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.HIDE_TOPOGRAPHY) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.HIDE_TOPOGRAPHY);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "collisionMap")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.COLLISION_MAP) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.COLLISION_MAP);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "shadowMap")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.SHADOW_MAP) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.SHADOW_MAP);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "depthMap")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.DEPTH_MAP) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.DEPTH_MAP);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "lightValue")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.LIGHT_VALUE) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.LIGHT_VALUE);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "paths")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.PATHS) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.PATHS);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "traffic")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.TRAFFIC) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.TRAFFIC);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "roadDurability")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.ROAD_DURABILITY) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.ROAD_DURABILITY);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedMusicPanel() throws SlickException {
      if (this.gui.getMusicPanel().intersectsMusic()) {
         sound.playMusic(this.gui.getMusicPanel().getMusicTitle());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedRegionalDataPanel() throws SlickException {
      if (this.gui.getRegionalDataPanel().intersectsRegionList()) {
         this.gui.toggleWorldMapListPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getRegionalDataPanel().intersectsRegionClearList()) {
         map.setConnectedRegionList(this.gui.getRegionalDataPanel().getRegionSlotSelected(), "null");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_REGIONAL_DATA_PANEL, "worldMapXInput")) {
         this.gui.getRegionalDataPanel().setFocusWorldMapXInput();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_REGIONAL_DATA_PANEL, "worldMapYInput")) {
         this.gui.getRegionalDataPanel().setFocusWorldMapYInput();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_REGIONAL_DATA_PANEL, "worldMapCoordinatesSave")) {
         map.setWorldMapX(Integer.parseInt(this.gui.getRegionalDataPanel().getWorldMapXInput()));
         map.setWorldMapY(Integer.parseInt(this.gui.getRegionalDataPanel().getWorldMapYInput()));
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedWorldMapListPanel() throws SlickException {
      String worldMapClicked = this.gui.getWorldMapListPanel().getIntersectWorldMapName();
      if (worldMapClicked != null) {
         map.setConnectedRegionList(this.gui.getRegionalDataPanel().getRegionSlotSelected(), worldMapClicked);
         this.gui.toggleRegionalDataPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedRightPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "pageBackTop")
         || this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "pageBackBottom")) {
         this.gui.getRightPanel().backPage();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "pageUpTop")
         || this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "pageUpBottom")) {
         this.gui.getRightPanel().lastPage();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "pageDownTop")
         || this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "pageDownBottom")) {
         this.gui.getRightPanel().nextPage();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "hidePanel")) {
         this.gui.getRightPanel().togglePanelHidden();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryBuild")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryTiles")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryCivics")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CIVICS_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryAncient")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_ANCIENT);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryDefense")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_DEFENSE);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryFoodAndWater")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_FOOD_AND_WATER_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryGatesAndWalls")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_GATES_AND_WALLS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryHarvesting")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_HARVESTING);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryHousing")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_HOUSING);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryLighting")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_LIGHTING);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryMagic")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_MAGIC);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryManufacturing")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_MANUFACTURING);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryRefining")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_REFINING);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryStorage")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_STORAGE_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryTrash")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_TRASH);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategorySpecial")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_SPECIAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAncientCullisGateAbandoned")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.ANCIENT_CULLIS_GATE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAncientCullisGateAbandoned");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAncientCullisGate")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ANCIENT_CULLIS_GATE, ObjectBase.ObjectSubType.BUILT, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAncientCullisGate");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAncientRadiancePoolAbandoned")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.ANCIENT_RADIANCE_POOL, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAncientRadiancePoolAbandoned");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAncientRadiancePool")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ANCIENT_RADIANCE_POOL, ObjectBase.ObjectSubType.BUILT, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAncientRadiancePool");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAncillary")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ANCILLARY, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAncillary");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelClinic")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CLINIC, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelClinic");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCourierStation")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.COURIER_STATION, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCourierStation");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMaintenanceBuilding")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.MAINTENANCE_BUILDING, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMaintenanceBuilding");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMarketplace")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.MARKETPLACE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMarketplace");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWayMakerShack")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.WAY_MAKER_SHACK, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWayMakerShack");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMigrationWayStation")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.MIGRATION_WAY_STATION, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMigrationWayStation");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryDefenseGolems")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_DEFENSE_GOLEMS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryDefenseTowers")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_DEFENSE_TOWERS_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryDefenseMiscellaneous")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_DEFENSE_MISCELLANEOUS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrystalGolemCombobulator")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrystalGolemCombobulator");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelStoneGolemCombobulator")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelStoneGolemCombobulator");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWoodGolemCombobulator")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWoodGolemCombobulator");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAttractTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ATTRACT_TOWER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAttractTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBanishTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BANISH_TOWER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBanishTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBallistaTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BALLISTA_TOWER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBallistaTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBowTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BOW_TOWER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBowTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBulletTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BULLET_TOWER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBulletTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelElementalBoltTower")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelElementalBoltTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPhantomDartTower")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.PHANTOM_DART_TOWER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPhantomDartTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelSlingTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.SLING_TOWER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelSlingTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelSprayTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.SPRAY_TOWER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelSprayTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelStaticTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.STATIC_TOWER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelStaticTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLightningRod")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LIGHTNING_ROD, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLightningRod");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRecombobulatorTower")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRecombobulatorTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAnimalPen")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ANIMAL_PEN, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAnimalPen");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBottler")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BOTTLER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBottler");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCluckerCoop")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CLUCKER_COOP, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCluckerCoop");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelFarm")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.FARM, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelFarm");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelSmallFountain")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.SMALL_FOUNTAIN, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelSmallFountain");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLargeFountain")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LARGE_FOUNTAIN, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLargeFountain");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelKitchen")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.KITCHEN, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelKitchen");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelOutpost")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.OUTPOST, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelOutpost");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRangerLodge")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.RANGER_LODGE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRangerLodge");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRainCatcher")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.RAIN_CATCHER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRainCatcher");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWaterPurifier")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.WATER_PURIFIER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWaterPurifier");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWell")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.WELL, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWell");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryGatesAndWallsGates")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_GATES_AND_WALLS_GATES);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryGatesAndWallsWalls")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_GATES_AND_WALLS_WALLS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrylithiumWallGateNS")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_NS, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrylithiumWallGateNS");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrylithiumWallGateWE")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_WE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrylithiumWallGateWE");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelStoneWallGateNS")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.STONE_WALL_GATE_NS, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelStoneWallGateNS");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelStoneWallGateWE")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.STONE_WALL_GATE_WE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelStoneWallGateWE");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWoodFenceGateNS")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.WOOD_FENCE_GATE_NS, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWoodFenceGateNS");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWoodFenceGateWE")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.WOOD_FENCE_GATE_WE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWoodFenceGateWE");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrylithiumWall")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYLITHIUM_WALL, ObjectBase.ObjectSubType.BUILT, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrylithiumWall");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCurtainWall")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CURTAIN_WALL, ObjectBase.ObjectSubType.BUILT, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCurtainWall");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrylithiumCurtainWall")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.CRYLITHIUM_CURTAIN_WALL, ObjectBase.ObjectSubType.BUILT, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrylithiumCurtainWall");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelStoneWall")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.STONE_WALL, ObjectBase.ObjectSubType.BUILT, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelStoneWall");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTrashyCubeWall")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.TRASHY_CUBE_WALL, ObjectBase.ObjectSubType.BUILT, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTrashyCubeWall");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWoodFence")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.WOOD_FENCE, ObjectBase.ObjectSubType.BUILT, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWoodFence");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrystalHarvestry")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYSTAL_HARVESTRY, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrystalHarvestry");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLumberShack")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LUMBER_SHACK, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLumberShack");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMiningFacility")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.MINING_FACILITY, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMiningFacility");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelDoggoHouse")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.DOGGO_HOUSE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelDoggoHouse");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelHousing")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.HOUSING, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelHousing");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrylithiumFirePit")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.CRYLITHIUM_FIRE_PIT, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrylithiumFirePit");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLargeCrylithiumFirePit")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.LARGE_CRYLITHIUM_FIRE_PIT, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLargeCrylithiumFirePit");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelFirePit")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.FIRE_PIT, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelFirePit");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLargeFirePit")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LARGE_FIRE_PIT, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLargeFirePit");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrystalMotivator")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYSTAL_MOTIVATOR, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrystalMotivator");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCullisGate")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CULLIS_GATE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCullisGate");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelEssenceAltar")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ESSENCE_ALTAR, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelEssenceAltar");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelEssenceCollector")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ESSENCE_COLLECTOR, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelEssenceCollector");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelReliquary")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.RELIQUARY, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelReliquary");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelArmorsmithy")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ARMORSMITHY, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelArmorsmithy");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBowyer")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BOWYER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBowyer");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRockTumbler")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ROCK_TUMBLER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRockTumbler");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelToolsmithy")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.TOOLSMITHY, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelToolsmithy");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrystillery")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYSTILLERY, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrystillery");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelForge")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.FORGE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelForge");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLumberMill")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LUMBER_MILL, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLumberMill");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelStoneCuttery")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.STONE_CUTTERY, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelStoneCuttery");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAmmoStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.AMMO_STORAGE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelAmmoStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrystalStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYSTAL_STORAGE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCrystalStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelEquipmentStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.EQUIPMENT_STORAGE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelEquipmentStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelFoodStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.FOOD_STORAGE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelFoodStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGoldStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.GOLD_STORAGE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGoldStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelKeyShack")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.KEY_SHACK, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelKeyShack");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMineralStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.MINERAL_STORAGE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMineralStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMiscellaneousStorage")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMiscellaneousStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRockStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ROCK_STORAGE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRockStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWoodStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.WOOD_STORAGE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWoodStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBurner")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BURNER, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBurner");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCubeEGolemCombobulator")) {
         this.gui
            .getGUIData()
            .setPlacementObject(MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCubeEGolemCombobulator");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLandfill")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LANDFILL, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLandfill");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelProcessor")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.PROCESSOR, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelProcessor");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTrashCan")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.TRASH_CAN, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTrashCan");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTrashyCubePile")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.TRASHY_CUBE_PILE, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTrashyCubePile");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCamp")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CASTLE_1, ObjectBase.ObjectSubType.ABANDONED, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCamp");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLootBox")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LOOT_BOX, ObjectBase.ObjectSubType.BUILT, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLootBox");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryBricks")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_BRICKS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryCrystals")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CRYSTALS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryDirt")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_DIRT);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryFlowers")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_FLOWERS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryFood")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_FOOD_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryGrass")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_GRASS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryGravel")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_GRAVEL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryLava")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_LAVA);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryRoads")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_ROADS_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryRock")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_ROCK);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategorySand")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_SAND);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategorySandstone")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_SANDSTONE);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategorySnow")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_SNOW);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryTar")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_TAR);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryTrees")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_TREES_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCategoryWater")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_WATER);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGrayBricks")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.BRICKS);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGrayBricks");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGrayTiles")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TILES);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGrayTiles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedCrystals")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.CRYSTAL_RED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedCrystals");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGreenCrystals")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.CRYSTAL_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGreenCrystals");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlueCrystals")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.CRYSTAL_BLUE);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlueCrystals");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPurpleCrystals")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.CRYSTAL_PURPLE);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPurpleCrystals");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBrownDirt")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.DIRT_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBrownDirt");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelDarkBrownDirt")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.DIRT_DARK_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelDarkBrownDirt");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLightBrownDirt")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.DIRT_LIGHT_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLightBrownDirt");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlackFlowers")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.FLOWERS_BLACK);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlackFlowers");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlueFlowers")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.FLOWERS_BLUE);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlueFlowers");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPurpleFlowers")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.FLOWERS_PURPLE);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPurpleFlowers");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedFlowers")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.FLOWERS_RED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedFlowers");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWhiteFlowers")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.FLOWERS_WHITE);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWhiteFlowers");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelYellowFlowers")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.FLOWERS_YELLOW);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelYellowFlowers");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCactus")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.CACTUS_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCactus");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCarrots")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.CARROTS);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCarrots");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelHolyPotatoes")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.HOLY_POTATOES);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelHolyPotatoes");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMelons")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.MELONS);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMelons");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMushrooms")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.MUSHROOMS);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelMushrooms");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPotatoes")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.POTATOES);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPotatoes");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTurnips")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TURNIPS);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTurnips");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelEmeraldGreenGrass")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRASS_EMERALD_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelEmeraldGreenGrass");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGreenGrass")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRASS_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGreenGrass");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTealGrass")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRASS_TEAL);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTealGrass");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelYellowBrownGrass")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRASS_YELLOW_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelYellowBrownGrass");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlueGravel")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRAVEL_BLUE);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlueGravel");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGrayGravel")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRAVEL_GRAY);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGrayGravel");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedGravel")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRAVEL_RED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedGravel");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLava")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.LAVA);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLava");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPath")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.PATH);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPath");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLogPathDebris")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.LOG_PATH_DEBRIS);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLogPathDebris");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLogPath")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.LOG_PATH);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLogPath");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCobbleAndLogPathDebris")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.COBBLE_AND_LOG_PATH_DEBRIS);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCobbleAndLogPathDebris");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCobbleAndLogPath")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.COBBLE_AND_LOG_PATH);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCobbleAndLogPath");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCobbleAndBoardRoadDebris")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.COBBLE_AND_BOARD_ROAD_DEBRIS);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCobbleAndBoardRoadDebris");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCobbleAndBoardRoad")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.COBBLE_AND_BOARD_ROAD);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCobbleAndBoardRoad");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCutStoneAndBoardRoadDebris")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.CUT_STONE_AND_BOARD_ROAD_DEBRIS);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCutStoneAndBoardRoadDebris");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCutStoneAndBoardRoad")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.CUT_STONE_AND_BOARD_ROAD);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelCutStoneAndBoardRoad");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlackRock")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.ROCK_BLACK);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlackRock");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBrownRock")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.ROCK_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBrownRock");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGrayRock")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.ROCK_GRAY);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGrayRock");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedRock")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.ROCK_RED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedRock");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWhiteRock")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.ROCK_WHITE);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWhiteRock");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlackSand")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SAND_BLACK);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlackSand");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedSand")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SAND_RED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedSand");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTanSand")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SAND_TAN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTanSand");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlackSandstone")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SANDSTONE_BLACK);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlackSandstone");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedSandstone")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SANDSTONE_RED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedSandstone");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTanSandstone")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SANDSTONE_TAN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTanSandstone");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWhiteSnow")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SNOW_WHITE);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWhiteSnow");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTar")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TAR);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelTar");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlueDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_BLUE_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlueDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlueStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_BLUE_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlueStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlueTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_BLUE);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBlueTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBrownDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_BROWN_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBrownDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBrownStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_BROWN_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBrownStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBrownTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelBrownTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelDarkGreenDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_DARK_GREEN_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelDarkGreenDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelDarkGreenStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_DARK_GREEN_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelDarkGreenStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelDarkGreenTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_DARK_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelDarkGreenTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGreenDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_GREEN_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGreenDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGreenStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_GREEN_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGreenStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGreenTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelGreenTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLavenderDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_LAVENDER_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLavenderDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLavenderStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_LAVENDER_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLavenderStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLavenderTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_LAVENDER);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelLavenderTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPaleBlueDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_PALE_BLUE_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPaleBlueDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPaleBlueStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_PALE_BLUE_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPaleBlueStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPaleBlueTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_PALE_BLUE);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelPaleBlueTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_RED_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_RED_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_RED);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelRedTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWater")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.WATER);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_RIGHT_PANEL, "panelWater");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedTerrainBar() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "brushSizeMinus")) {
         this.gui.getBrushLoader().setBrushSizeDown();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "brushSizePlus")) {
         this.gui.getBrushLoader().setBrushSizeUp();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "accentMode")) {
         map.lockAccentMode();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseAll")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.ERASE
            || this.gui.getGUIData().getSelectedEraseMode() != MapModule.EraseMode.ALL) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
            this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.ALL);
            this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseAll");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseTerrain")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.ERASE
            || this.gui.getGUIData().getSelectedEraseMode() != MapModule.EraseMode.TERRAIN) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
            this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.TERRAIN);
            this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseTerrain");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseTopography")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.ERASE
            || this.gui.getGUIData().getSelectedEraseMode() != MapModule.EraseMode.TOPOGRAPHY) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
            this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.TOPOGRAPHY);
            this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseTopography");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseObjects")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.ERASE
            || this.gui.getGUIData().getSelectedEraseMode() != MapModule.EraseMode.OBJECTS) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
            this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.OBJECTS);
            this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseObjects");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseRoads")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.ERASE
            || this.gui.getGUIData().getSelectedEraseMode() != MapModule.EraseMode.ROADS) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
            this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.ROADS);
            this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseRoads");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "hole")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.HOLE) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.HOLE);
            this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "hole");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "patch")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.PATCH) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.PATCH);
            this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "patch");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }
   }

   private void controlsMouseDownMinimapPanel() {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_MINIMAP_PANEL, "center")) {
         float x = (this.gui.getMinimapPanel().getMinimapX() - this.mouse.getX()) * 16.0F / this.gui.getMinimapPanel().getMinimapScale()
            + ScaleControl.getWorldWidth() / 2;
         float y = (this.gui.getMinimapPanel().getMinimapY() - this.mouse.getY()) * 16.0F / this.gui.getMinimapPanel().getMinimapScale()
            + ScaleControl.getWorldHeight() / 2;
         map.setMapPosition(x, y);
      }
   }

   private void controlsMouseDownInterfaceMode() throws SlickException {
      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.BRUSH) {
         map.drawToMap(
            this.getMouseTileX(),
            this.getMouseTileY(),
            this.gui.getGUIData().getPlacementTerrainType(),
            this.gui.getBrushLoader().getBrushSize(),
            this.gui.getBrushLoader().getBrushType()
         );
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.PLACE_ROAD) {
         road.setRoad(this.getMouseTileX(), this.getMouseTileY(), this.gui.getGUIData().getPlacementRoadType());
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.ERASE) {
         map.erase(
            this.getMouseTileX(),
            this.getMouseTileY(),
            this.gui.getGUIData().getSelectedEraseMode(),
            this.gui.getBrushLoader().getBrushSize(),
            this.gui.getBrushLoader().getBrushType()
         );
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.PATCH) {
         map.patchTerrain(this.getMouseTileX(), this.getMouseTileY(), this.gui.getBrushLoader().getBrushSize(), this.gui.getBrushLoader().getBrushType());
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.HOLE) {
         map.holeTerrain(this.getMouseTileX(), this.getMouseTileY(), this.gui.getBrushLoader().getBrushSize(), this.gui.getBrushLoader().getBrushType());
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.OBJECT) {
         if (SettingsParser.getDebug() && input.isKeyDown(SettingsParser.getKeyAccentModeToggle())) {
            map.placeObject(this.getMouseTileX(), this.getMouseTileY(), this.gui.getGUIData().getPlacementObjectType(), ObjectBase.ObjectSubType.BUILT, true);
         } else {
            map.placeObject(
               this.getMouseTileX(),
               this.getMouseTileY(),
               this.gui.getGUIData().getPlacementObjectType(),
               this.gui.getGUIData().getPlacementObjectSubType(),
               true
            );
         }
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.WALL) {
         if (SettingsParser.getDebug() && input.isKeyDown(SettingsParser.getKeyAccentModeToggle())) {
            map.placeWall(this.getMouseTileX(), this.getMouseTileY(), this.gui.getGUIData().getPlacementObjectType(), ObjectBase.ObjectSubType.BUILT, true);
         } else {
            map.placeWall(
               this.getMouseTileX(),
               this.getMouseTileY(),
               this.gui.getGUIData().getPlacementObjectType(),
               this.gui.getGUIData().getPlacementObjectSubType(),
               true
            );
         }
      }
   }

   private void cancelTasks() {
      this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SELECT);
      this.gui.deselectAll();
   }

   @Override
   public void mouseWheelMoved(int change) {
      if (!transition.getControlLockout() && !this.loadingRequired) {
         try {
            if (this.gui.intersectsAnyGUIMask()) {
               if (this.gui.getRightPanel().intersectsGUIMask()) {
                  if (change > 50) {
                     this.gui.getRightPanel().lastPage();
                     sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
                  }

                  if (change < -50) {
                     this.gui.getRightPanel().nextPage();
                     sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
                  }
               }
            } else if (SettingsParser.getMouseWheelZoomingEnabled()) {
               if (input.isKeyDown(SettingsParser.getKeyAxisLock())) {
                  if (change > 50) {
                     this.gui.getBrushLoader().setBrushSizeUp(1);
                     sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
                  }

                  if (change < -50) {
                     this.gui.getBrushLoader().setBrushSizeDown(1);
                     sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
                  }
               } else {
                  if (change > 50) {
                     ScaleControl.increaseWorldScale();
                  }

                  if (change < -50) {
                     ScaleControl.decreaseWorldScale();
                  }
               }
            } else if (input.isKeyDown(SettingsParser.getKeyAxisLock())) {
               if (change > 50) {
                  this.gui.getBrushLoader().setBrushSizeUp(1);
                  sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
               }

               if (change < -50) {
                  this.gui.getBrushLoader().setBrushSizeDown(1);
                  sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
               }
            }
         } catch (SlickException var3) {
            var3.printStackTrace();
         }
      }
   }

   public void buttonCycles() throws SlickException {
      if (!this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "brushSizePlus")
         && !this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "brushSizeMinus")) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "brushCircle")) {
            this.gui.getBrushLoader().setBrushType(BrushLoader.BrushType.FILLETED_SQUARE);
            this.gui.getHarvestAndTerrainBar().setBrush(BrushLoader.BrushType.FILLETED_SQUARE);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "brushFilletedSquare")) {
            this.gui.getBrushLoader().setBrushType(BrushLoader.BrushType.CHAMFERED_SQUARE);
            this.gui.getHarvestAndTerrainBar().setBrush(BrushLoader.BrushType.CHAMFERED_SQUARE);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "brushChamferedSquare")) {
            this.gui.getBrushLoader().setBrushType(BrushLoader.BrushType.SQUARE);
            this.gui.getHarvestAndTerrainBar().setBrush(BrushLoader.BrushType.SQUARE);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "brushSquare")) {
            this.gui.getBrushLoader().setBrushType(BrushLoader.BrushType.ROUND);
            this.gui.getHarvestAndTerrainBar().setBrush(BrushLoader.BrushType.ROUND);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseAll")) {
         this.gui.getHarvestAndTerrainBar().setEraseMode(MapModule.EraseMode.TERRAIN);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
         this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.TERRAIN);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseTerrain");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseTerrain")) {
         this.gui.getHarvestAndTerrainBar().setEraseMode(MapModule.EraseMode.TOPOGRAPHY);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
         this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.TOPOGRAPHY);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseTopography");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseTopography")) {
         this.gui.getHarvestAndTerrainBar().setEraseMode(MapModule.EraseMode.OBJECTS);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
         this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.OBJECTS);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseObjects");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseObjects")) {
         this.gui.getHarvestAndTerrainBar().setEraseMode(MapModule.EraseMode.ROADS);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
         this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.ROADS);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseRoads");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseRoads")) {
         this.gui.getHarvestAndTerrainBar().setEraseMode(MapModule.EraseMode.ALL);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
         this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.ALL);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "eraseAll");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "hole")) {
         this.gui.getHarvestAndTerrainBar().setHolePatchPatch();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.PATCH);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "patch");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "patch")) {
         this.gui.getHarvestAndTerrainBar().setHolePatchHole();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.HOLE);
         this.gui.select(GUIControllerBase.GUIPanel.MAP_EDITOR_TERRAIN_BAR, "hole");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void saveGame() throws SlickException {
      save.saveFullGame();
   }

   @Override
   public BrushLoader getBrushLoader() {
      return this.gui.getBrushLoader();
   }

   @Override
   public GUIControllerBase getGUI() {
      return this.gui;
   }
}
