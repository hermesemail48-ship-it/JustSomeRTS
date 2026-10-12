package rtr.states;

import rtr.platform.Platform;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.SoundModule;
import rtr.TimeModule;
import rtr.console.Console;
import rtr.font.LocalizationTemplate;
import rtr.font.Text;
import rtr.gui.GUIEnums;
import rtr.gui.states.BrushLoader;
import rtr.gui.states.GUIControllerBase;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.mainmenu.LanguageSelectPanel;
import rtr.map.MapTilesLoader;
import rtr.missiles.MissileModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobModule;
import rtr.objects.ObjectBase;
import rtr.save.RegionalSavedGame;
import rtr.save.SavedGamesHandler;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.Utilities;
import rtr.utilities.devtools.ImageScrubber;
import rtr.utilities.devtools.LineCounter;
import rtr.utilities.devtools.MobSheetGenerator;
import rtr.utilities.devtools.VideoSheetGenerator;
import rtr.weather.disasters.DisasterBase;

public class MainMenuState extends StateBase {
   private MainMenuGUIController gui;
   private float floatVelX = Utilities.randomFloat() - Utilities.randomFloat();
   private float floatVelY = Utilities.randomFloat() - Utilities.randomFloat();
   private int mainMenuScrollCooldown = 0;

   public MainMenuState(int s) {
      super(s);
      this.gameMode = GameModeTemplateBase.GameMode.MAIN_MENU;
      this.loadModuleMap.put(ModuleBase.ModuleType.BACKGROUND, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.COLLISION, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.DEPTH, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MOB, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MOB_JOB, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.ESSENCE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.FONT, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.LIGHTING, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MAP, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MINI_MAP, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MISSILE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.OBJECT, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.PARTICLE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.PROFILE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.PROJECTILE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.RESOURCE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.ROAD, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.SAVE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.SHADOW, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.SOUND, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.TIME, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.TRANSITION, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.WEATHER, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.CLOUD, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.CORRUPTION, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.GOD, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.GOAL, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.PERK, true);
   }

   @Override
   protected void loadState() throws SlickException {
      if (this.initLoad) {
         SavedGamesHandler.loadData();
         File[] listOfPacks = new File("maps/WorldMaps/").listFiles();
         ArrayList<String> mapName = new ArrayList<>();

         for (int i = 0; i < listOfPacks.length; i++) {
            if (listOfPacks[i].getName().contains(".zip")) {
               String mapFileName = listOfPacks[i].getName().replace(".zip", "");
               RegionalSavedGame s = SavedGamesHandler.getRegionalSave(
                  "WorldMaps", mapFileName, StateBase.GameType.SKIRMISH, GameModeTemplateBase.GameMode.MAP_EDITOR
               );
               mapName.add(s.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapFileName", "null"));
            }
         }

         RegionalSavedGame s = SavedGamesHandler.getRegionalSave(
            "WorldMaps", mapName.get(Utilities.randomInt(mapName.size())), StateBase.GameType.SKIRMISH, GameModeTemplateBase.GameMode.MAP_EDITOR
         );
         save.setActiveSave(s);
      }

      super.loadState();
      if (this.modulesLoaded) {
         if (profile.profileValid(SettingsParser.getLastProfile())) {
            profile.loadProfile(SettingsParser.getLastProfile());
         }

         this.gui = new MainMenuGUIController(this.g, gc, this.mouse);
         if (profile.profileValid(SettingsParser.getLastProfile())) {
            this.gui.getSelectProfilePanel().selectProfile(SettingsParser.getLastProfile());
         }

         int randomX = Utilities.randomInt(-127, 127);
         int randomY = Utilities.randomInt(-127, 127);
         map.setMapPosition(-2048 + randomX, -2048 + randomY);
         this.loadingRequired = false;
         this.updateRenderWindow();
         transition.enter();
         if (TimeModule.isHalloween()) {
            weather.forceNewDisaster(DisasterBase.DisasterType.BLOOD_RAIN, false);

            for (int x = 0; x < 100; x++) {
               map.placeObject(
                  Utilities.randomInt(256), Utilities.randomInt(256), MapTilesLoader.TileSet.LARGE_FIRE_PIT, ObjectBase.ObjectSubType.BUILT, true, true
               );
               map.placeObject(Utilities.randomInt(256), Utilities.randomInt(256), MapTilesLoader.TileSet.FIRE_PIT, ObjectBase.ObjectSubType.BUILT, true, true);
               map.placeObject(
                  Utilities.randomInt(256),
                  Utilities.randomInt(256),
                  MapTilesLoader.TileSet.CORRUPTED_GRAVEYARD_LARGE,
                  ObjectBase.ObjectSubType.BUILT,
                  true,
                  true
               );
               map.placeObject(
                  Utilities.randomInt(256),
                  Utilities.randomInt(256),
                  MapTilesLoader.TileSet.CORRUPTED_GRAVEYARD_MEDIUM,
                  ObjectBase.ObjectSubType.BUILT,
                  true,
                  true
               );
               map.placeObject(
                  Utilities.randomInt(256),
                  Utilities.randomInt(256),
                  MapTilesLoader.TileSet.CORRUPTED_GRAVEYARD_SMALL,
                  ObjectBase.ObjectSubType.BUILT,
                  true,
                  true
               );
            }
         }

         ScaleControl.setWorldScale(2);
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
         particle.renderBottom();
         missile.renderBottom();
         mob.render(g, false, false);
         object.renderTowers();
         missile.renderTop();
         essence.render(g);
         particle.renderTop();
         lighting.render(g);
         cloud.render(g);
         font.render(g);
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         this.gui.render();
         particle.renderGUI();
         this.mouse.setX(this.mouseX);
         this.mouse.setY(this.mouseY);
         this.screenShotListener();
         transition.render(g);
      }
   }

   @Override
   public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
      super.update(gc, sbg, delta);
      if (!this.loadingRequired) {
         particle.update();
         map.update();
         road.update();
         background.update();
         sound.update();
         lighting.update();
         weather.update();
         cloud.update();
         projectile.update();
         missile.update();
         mob.update(this.getMouseTileX(), this.getMouseTileY());
         essence.update();
         font.update();
         map.updateNoSpeed();
         transition.update();
         this.gui.update();
         if (this.mainMenuScrollCooldown > 0 && !map.isMoving()) {
            this.mainMenuScrollCooldown--;
         }

         if (SettingsParser.getMainMenuDrift() && this.mainMenuScrollCooldown <= 150 && !map.isMoving()) {
            boolean noX = false;
            boolean noY = false;
            if (map.getMapX() * -1.0F < 256.0F) {
               this.floatVelX -= 0.01F;
               noX = true;
            } else if (map.getMapX() * -1.0F > 4096 - ScaleControl.getInterfaceHeight() - ScaleControl.getInterfaceWidth() / 2) {
               this.floatVelX += 0.01F;
               noX = true;
            } else if (map.getMapY() * -1.0F < 256.0F) {
               this.floatVelY -= 0.01F;
               noY = true;
            } else if (map.getMapY() * -1.0F > 4096 - ScaleControl.getInterfaceHeight() - ScaleControl.getInterfaceHeight() / 2) {
               this.floatVelY += 0.01F;
               noY = true;
            }

            if (!noX) {
               if (this.floatVelX < 0.0F) {
                  this.floatVelX -= 0.01F;
               } else {
                  this.floatVelX += 0.01F;
               }
            }

            if (!noY) {
               if (this.floatVelY < 0.0F) {
                  this.floatVelY -= 0.01F;
               } else {
                  this.floatVelY += 0.01F;
               }
            }

            if (this.floatVelX < -3.0F) {
               this.floatVelX = -3.0F;
            }

            if (this.floatVelX > 3.0F) {
               this.floatVelX = 3.0F;
            }

            if (this.floatVelY < -3.0F) {
               this.floatVelY = -3.0F;
            }

            if (this.floatVelY > 3.0F) {
               this.floatVelY = 3.0F;
            }

            if (this.mainMenuScrollCooldown > 125) {
               this.floatVelX *= 0.75F;
               this.floatVelY *= 0.75F;
            }

            map.moveMapKeyboard(this.floatVelX, this.floatVelY);
         }
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
            this.controlsKeyboardDebug();
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
         this.takeScreenShot = true;
      }
   }

   private void controlsKeyboardFullScreen() throws SlickException {
      if (input.isKeyPressed(SettingsParser.getKeyFullScreen()) || (input.isKeyDown(56) || input.isKeyDown(184)) && input.isKeyPressed(28)) {
         this.toggleFullScreen();
         this.updateRenderWindow();
         input.clearKeyPressedRecord();
      }
   }

   private void controlsKeyboardDebug() {
      if (input.isKeyPressed(SettingsParser.getKeyDebug())) {
         this.gui.toggleDebug();
      }
   }

   @Override
   protected void controlsMouse() throws SlickException {
      super.controlsMouse();
      if (!transition.getControlLockout()) {
         if (input.isMousePressed(1)) {
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
            this.mainMenuScrollCooldown = 175;
         } else {
            this.movingMap = false;
         }

         if (input.isMousePressed(0)) {
            for (int e = 0; e < mob.getMobArray().size(); e++) {
               ArrayList<MobBase> targets = new ArrayList<>();
               MobBase target = mob.getMobArray().get(e);
               if (!target.isDead() && Math.abs(target.getTileX() - this.getMouseTileX()) < 8 && Math.abs(target.getTileY() - this.getMouseTileY()) < 8) {
                  targets.add(target);
               }

               if (targets.size() > 0) {
                  missile.newMissile(
                     this.getMouseOnMapX(),
                     this.getMouseOnMapY(),
                     targets.get(Utilities.randomInt(targets.size())),
                     MissileModule.MissileType.SPECIAL_MAIN_MENU
                  );
               }
            }

            this.controlsMousePressedMainMenuPanel();
            this.controlsMousePressedDisclaimer();
            this.controlsMousePressedWarning();
            this.controlsMousePressedLanguageSelect();
            this.controlsMousePressedExitGame();
            this.controlsMousePressedSelectProfile();
            this.controlsMousePressedUploadProfile();
            this.controlsMousePressedNewProfile();
            this.controlsMousePressedDeleteProfile();
            this.controlsMousePressedSkirmish();
            this.controlsMousePressedSkirmishNewGameWarning();
            this.controlsMousePressedMapEditorLoadMap();
            this.controlsMousePressedMapEditorLoadMapDeleteWarning();
            this.controlsMousePressedMapEditorNewMap();
            this.controlsMousePressedMapEditorNewMapDeleteWarningPanel();
            this.controlsMousePressedSettingsPanel();
            this.controlsMousePressedSettingKeyChange();
            this.controlsMousePressedCredits();
         }
      }
   }

   private void controlsMousePressedMainMenuPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "setLanguage")) {
         this.gui.getLanguageSelectPanel().setOriginPanel(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL);
         this.gui.toggleLanguageSelect();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "play")) {
         this.gui.toggleMain();
         this.gui.getMainMenuPanel().setPanelPage(GUIEnums.MainMenuPage.PLAY);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "worldMap")) {
         this.gui.setDebug(false);
         transition.startTransition(Game.getWorldMapState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "skirmish")) {
         this.gui.getSkirmishPanel().reloadMapList();
         this.gui.toggleSkirmish();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "tutorialVideo")) {
         try {
            URI oURL = new URI("http://www.risetoruins.com/tutorialVideo/");
            Platform.get().browse(oURL.toString());
         } catch (Exception var8) {
            var8.printStackTrace();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "settings")) {
         this.gui.getSettingsPanel().settingsReloadProperties();
         this.gui.toggleSettings();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "credits")) {
         sound.playCredits();
         this.gui.toggleCredits();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "mapEditor")) {
         this.gui.toggleMain();
         this.gui.getMainMenuPanel().setPanelPage(GUIEnums.MainMenuPage.MAP_EDITOR);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "newMap")) {
         SavedGamesHandler.loadData();
         this.gui.getMapEditorNewMapPanel().reloadMapList(gc);
         this.gui.toggleMapEditorNewMap();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "loadMap")) {
         SavedGamesHandler.loadData();
         this.gui.getMapEditorLoadMapPanel().reloadMapList();
         this.gui.toggleMapEditorLoadMap();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "devTools")) {
         this.gui.toggleMain();
         this.gui.getMainMenuPanel().setPanelPage(GUIEnums.MainMenuPage.DEV_TOOLS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "changeProfile")) {
         this.gui.toggleSelectProfile();
         this.gui.getSelectProfilePanel().loadProfiles();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "exit")) {
         this.gui.setDebug(false);
         transition.startTransition(null);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "scrubMapImages")) {
         ImageScrubber.scrubMapSheets();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "generateMobSheet")) {
         MobSheetGenerator.buildSpriteSheet(false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "generateMobSheetOverwrite")) {
         MobSheetGenerator.buildSpriteSheet(true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "lineCounter")) {
         LineCounter.countLines();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "generateVideoSheet")) {
         VideoSheetGenerator.buildSpriteSheet(this.gui.getMainMenuPanel().getGenerateVideoSheetName());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "generateVideoSheetName")) {
         this.gui.getMainMenuPanel().setFocusGenerateVideoSheetName();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "back")) {
         this.gui.toggleMain();
         this.gui.getMainMenuPanel().setPanelPage(GUIEnums.MainMenuPage.MAIN);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      try {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "patreonURLIcon")) {
            URI oURL = new URI("http://patreon.com/RaymondDoerr");
            Platform.get().browse(oURL.toString());
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "twitchURLIcon")) {
            URI oURL = new URI("http://twitch.tv/RaymondDoerr");
            Platform.get().browse(oURL.toString());
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "twitterURLIcon")) {
            URI oURL = new URI("http://twitter.com/RaymondDoerr");
            Platform.get().browse(oURL.toString());
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "xURLIcon")) {
            URI oURL = new URI("http://x.com/RaymondDoerr");
            Platform.get().browse(oURL.toString());
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "discordURLIcon")) {
            URI oURL = new URI("https://discord.gg/rmsvJTY");
            Platform.get().browse(oURL.toString());
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "announcementButton")) {
         try {
            URI oURL = this.gui.getMainMenuPanel().getAnnouncementURL();
            Platform.get().browse(oURL.toString());
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "review")) {
         try {
            URI oURL = new URI("steam://store/328080");
            Platform.get().browse(oURL.toString());
         } catch (Exception var5) {
            var5.printStackTrace();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "hide")) {
         profile.setHideReview();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL, "goWatch")) {
         try {
            URI oURL = new URI("https://www.twitch.tv/raymonddoerr");
            Platform.get().browse(oURL.toString());
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedDisclaimer() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_DISCLAIMER_PANEL, "officialWebsite")) {
         try {
            URI oURL = new URI("http://www.risetoruins.com");
            Platform.get().browse(oURL.toString());
         } catch (Exception var7) {
            var7.printStackTrace();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_DISCLAIMER_PANEL, "steam")) {
         try {
            URI oURL = new URI("http://steamcommunity.com/app/328080/discussions/");
            Platform.get().browse(oURL.toString());
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_DISCLAIMER_PANEL, "unstable")) {
         try {
            URI oURL = new URI("http://risetoruins.com/index.php?/topic/263-how-to-access-unstable-builds-on-steam/");
            Platform.get().browse(oURL.toString());
         } catch (Exception var5) {
            var5.printStackTrace();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_DISCLAIMER_PANEL, "twitter")) {
         try {
            URI oURL = new URI("http://twitter.com/RaymondDoerr");
            Platform.get().browse(oURL.toString());
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_DISCLAIMER_PANEL, "discord")) {
         try {
            URI oURL = new URI("https://discord.gg/rmsvJTY");
            Platform.get().browse(oURL.toString());
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_DISCLAIMER_PANEL, "letsPlay")) {
         if (SettingsParser.getLastProfile() == -1 || !profile.profileValid(SettingsParser.getLastProfile())) {
            this.gui.toggleSelectProfile();
         } else if (!profile.getWorldMapVersion().equals(Game.getVersion())) {
            this.gui.toggleWarning();
         } else {
            this.gui.getMainMenuPanel().setPanelPage(GUIEnums.MainMenuPage.MAIN);
            this.gui.toggleMain();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_DISCLAIMER_PANEL, "dontShowAgain")) {
         if (SettingsParser.getLastProfile() == -1 || !profile.profileValid(SettingsParser.getLastProfile())) {
            this.gui.toggleSelectProfile();
         } else if (!profile.getWorldMapVersion().equals(Game.getVersion())) {
            this.gui.toggleWarning();
         } else {
            this.gui.getMainMenuPanel().setPanelPage(GUIEnums.MainMenuPage.MAIN);
            this.gui.toggleMain();
         }

         SettingsParser.setHideDisclaimer(true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedWarning() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_WARNING_PANEL, "gotIt")) {
         profile.loadProfile(SettingsParser.getLastProfile());
         profile.resetWorldMapVersion();
         this.gui.toggleMain();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_WARNING_PANEL, "openInstallFolder")) {
         Platform.get().open(new File(System.getProperty("user.dir")));

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedLanguageSelect() throws SlickException {
      for (int i = 0; i < LanguageSelectPanel.LANGUAGE_SELECT_PANEL_MAX_LANGUAGES && i < Text.getAvailableLocalizations().size(); i++) {
         LocalizationTemplate thisLanguage = Text.getAvailableLocalizations().get(i);
         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_LANGUAGE_SELECT_PANEL, "localization" + i)) {
            SettingsParser.setLanguage(thisLanguage.getLanguageFolderName());
            SettingsParser.saveBaseSettings();
            this.gui.toggleExitGamePanel();
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_LANGUAGE_SELECT_PANEL, "localizationLink" + i)) {
            try {
               URI oURL = new URI(thisLanguage.getLanguageLink());
               Platform.get().browse(oURL.toString());
            } catch (Exception var7) {
               var7.printStackTrace();
            }

            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_LANGUAGE_SELECT_PANEL, "steamWorkshop")) {
         try {
            URI oURL = new URI("https://steamcommunity.com/app/328080/workshop/");
            Platform.get().browse(oURL.toString());
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_LANGUAGE_SELECT_PANEL, "refresh")) {
         Text.initializeLanguage();
         this.gui.getLanguageSelectPanel().refreshPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_LANGUAGE_SELECT_PANEL, "back")) {
         if (this.gui.getLanguageSelectPanel().getOriginPanel() == GUIControllerBase.GUIPanel.MAIN_MENU_MAIN_MENU_PANEL) {
            this.gui.toggleMain();
         } else {
            this.gui.toggleSelectProfile();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedExitGame() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_RESTART_GAME_PANEL, "exit")) {
         this.gui.setDebug(false);
         transition.startTransition(null);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedSelectProfile() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SELECT_PROFILE_PANEL, "setLanguage")) {
         this.gui.getLanguageSelectPanel().setOriginPanel(GUIControllerBase.GUIPanel.MAIN_MENU_SELECT_PROFILE_PANEL);
         this.gui.toggleLanguageSelect();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SELECT_PROFILE_PANEL, "play")) {
         File profilePath = new File("profiles/profile" + this.gui.getSelectProfilePanel().getSelectedProfile() + "/profile.properties");
         if (profilePath.exists()) {
            profile.loadProfile(this.gui.getSelectProfilePanel().getSelectedProfile());
            if (!profile.getWorldMapVersion().equals(Game.getVersion())) {
               this.gui.toggleWarning();
            } else {
               this.gui.getMainMenuPanel().setPanelPage(GUIEnums.MainMenuPage.MAIN);
               this.gui.toggleMain();
            }
         } else {
            this.gui.toggleNewProfile();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SELECT_PROFILE_PANEL, "delete")) {
         this.gui.toggleDeleteProfile();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      for (int i = 0; i < 5; i++) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SELECT_PROFILE_PANEL, "selectProfile" + i)) {
            this.gui.getSelectProfilePanel().selectProfile(i);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SELECT_PROFILE_PANEL, "upload" + i)) {
            this.gui.getSelectProfilePanel().selectProfile(i);
            this.gui.toggleUploadProfile();
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SELECT_PROFILE_PANEL, "exit")) {
         this.gui.setDebug(false);
         transition.startTransition(null);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedUploadProfile() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "crash")) {
         this.gui.deselectAll();
         this.gui.getUploadProfilePanel().setUploadProfileCategory("Crash");
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "crash");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "lag")) {
         this.gui.deselectAll();
         this.gui.getUploadProfilePanel().setUploadProfileCategory("Lag");
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "lag");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "corrupted")) {
         this.gui.deselectAll();
         this.gui.getUploadProfilePanel().setUploadProfileCategory("Corrupted");
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "corrupted");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "devRequest")) {
         this.gui.deselectAll();
         this.gui.getUploadProfilePanel().setUploadProfileCategory("DevRequest");
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "devRequest");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "other")) {
         this.gui.deselectAll();
         this.gui.getUploadProfilePanel().setUploadProfileCategory("Other");
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "other");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "comments")) {
         this.gui.getUploadProfilePanel().setFocusUploadProfileComments();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "username")) {
         this.gui.getUploadProfilePanel().setFocusUploadProfileUsername();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "back")) {
         this.gui.toggleSelectProfile();
         this.gui.getUploadProfilePanel().resetUploadProfileSent();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_UPLOAD_PROFILE_PANEL, "upload")) {
         try {
            Utilities.sendProfile(
               this.gui.getSelectProfilePanel().getSelectedProfile(),
               this.gui.getSelectProfilePanel().getSelectedProfileName(),
               this.gui.getUploadProfilePanel().getUploadProfileCategory(),
               this.gui.getUploadProfilePanel().getUploadProfileComments(),
               this.gui.getUploadProfilePanel().getUploadProfileUsername()
            );
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }
   }

   private void controlsMousePressedNewProfile() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_NEW_PROFILE_PANEL, "name")) {
         this.gui.getNewProfilePanel().setFocusName();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_NEW_PROFILE_PANEL, "twitchChannel")) {
         this.gui.getNewProfilePanel().setFocusTwitchChannel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_NEW_PROFILE_PANEL, "back")) {
         this.gui.toggleSelectProfile();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_NEW_PROFILE_PANEL, "create")) {
         profile.createAndLoadProfile(
            this.gui.getSelectProfilePanel().getSelectedProfile(),
            this.gui.getNewProfilePanel().getName(),
            this.gui.getNewProfilePanel().getTwitchChannel(),
            this.gui.getNewProfilePanel().getTipsEnabled()
         );
         this.gui.getNewProfilePanel().resetName();
         this.gui.getMainMenuPanel().setPanelPage(GUIEnums.MainMenuPage.MAIN);
         this.gui.toggleMain();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_NEW_PROFILE_PANEL, "tips")) {
         this.gui.getNewProfilePanel().toggleTipsEnabled();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedDeleteProfile() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_DELETE_PROFILE_PANEL, "no")) {
         this.gui.getDeleteProfilePanel().setConfirm(false);
         this.gui.toggleSelectProfile();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_DELETE_PROFILE_PANEL, "yes")) {
         this.gui.getDeleteProfilePanel().setConfirm(true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_DELETE_PROFILE_PANEL, "really")) {
         profile.deleteProfile(this.gui.getSelectProfilePanel().getSelectedProfile());
         this.gui.getSelectProfilePanel().loadProfiles();
         this.gui.getDeleteProfilePanel().setConfirm(false);
         this.gui.toggleSelectProfile();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedSkirmish() throws SlickException {
      if (this.gui.getSkirmishPanel().intersectsMapList()) {
         this.gui.getSkirmishPanel().selectMap();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_PANEL, "newGame")) {
         RegionalSavedGame selectedSave = this.gui.getSkirmishPanel().getMapSave();
         String mapPackFolderName = selectedSave.getMapPackFolderName();
         String mapFileName = selectedSave.getMapFileName();
         String mapName = selectedSave.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapName", "null");
         if (SavedGamesHandler.regionalSaveExist(mapPackFolderName, mapFileName, StateBase.GameType.SKIRMISH, this.gui.getSkirmishPanel().getSelectedGameMode())
            )
          {
            this.gui.getSkirmishNewGameWarningPanel().setMapName(mapName);
            this.gui.getSkirmishNewGameWarningPanel().setGameMode(this.gui.getSkirmishPanel().getSelectedGameMode().getFolderName());
            this.gui.toggleSkirmishNewGameWarning();
         } else {
            Console.out("Loading: " + mapName, true);
            Game.getPlayState().setGameMode(this.gui.getSkirmishPanel().getSelectedGameMode());
            SavedGamesHandler.createNewRegionalSave(selectedSave, this.gui.getSkirmishPanel().getSelectedGameMode(), StateBase.GameType.SKIRMISH);
            save.setActiveSave(
               SavedGamesHandler.getRegionalSave(mapPackFolderName, mapFileName, StateBase.GameType.SKIRMISH, this.gui.getSkirmishPanel().getSelectedGameMode())
            );
            this.gui.setDebug(false);
            transition.startTransition(Game.getPlayState());
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_PANEL, "resumeGame")) {
         RegionalSavedGame selectedSave = this.gui.getSkirmishPanel().getMapSave();
         String mapPackFolderName = selectedSave.getMapPackFolderName();
         String mapFileName = selectedSave.getMapFileName();
         String mapName = selectedSave.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapName", "null");
         Console.out("Loading: " + mapName, true);
         Game.getPlayState().setGameMode(this.gui.getSkirmishPanel().getSelectedGameMode());
         save.setActiveSave(
            SavedGamesHandler.getRegionalSave(mapPackFolderName, mapFileName, StateBase.GameType.SKIRMISH, this.gui.getSkirmishPanel().getSelectedGameMode())
         );
         this.gui.setDebug(false);
         transition.startTransition(Game.getPlayState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_PANEL, "resumeGameResetAI")) {
         RegionalSavedGame selectedSave = this.gui.getSkirmishPanel().getMapSave();
         String mapPackFolderName = selectedSave.getMapPackFolderName();
         String mapFileName = selectedSave.getMapFileName();
         String mapName = selectedSave.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapName", "null");
         Console.out("Loading: " + mapName, true);
         Game.getPlayState();
         MobModule thisMob = (MobModule)PlayState.getModule(ModuleBase.ModuleType.MOB);
         thisMob.toggleDebugResetAI();
         Game.getPlayState().setGameMode(this.gui.getSkirmishPanel().getSelectedGameMode());
         save.setActiveSave(
            SavedGamesHandler.getRegionalSave(mapPackFolderName, mapFileName, StateBase.GameType.SKIRMISH, this.gui.getSkirmishPanel().getSelectedGameMode())
         );
         this.gui.setDebug(false);
         transition.startTransition(Game.getPlayState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getSkirmishPanel().intersectsMapListPageUp()) {
         this.gui.getSkirmishPanel().mapListUp();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getSkirmishPanel().intersectsMapListPageDown()) {
         this.gui.getSkirmishPanel().mapListDown();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getSkirmishPanel().intersectsMapPackLeft()) {
         this.gui.getSkirmishPanel().mapPackDown();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getSkirmishPanel().intersectsMapPackRight()) {
         this.gui.getSkirmishPanel().mapPackUp();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_PANEL, "gameModeNightmare")) {
         this.gui.getSkirmishPanel().setGameMode(GameModeTemplateBase.GameMode.NIGHTMARE);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_PANEL, "gameModeSurvival")) {
         this.gui.getSkirmishPanel().setGameMode(GameModeTemplateBase.GameMode.SURVIVAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_PANEL, "gameModeTraditional")) {
         this.gui.getSkirmishPanel().setGameMode(GameModeTemplateBase.GameMode.TRADITIONAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_PANEL, "gameModePeaceful")) {
         this.gui.getSkirmishPanel().setGameMode(GameModeTemplateBase.GameMode.PEACEFUL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_PANEL, "gameModeSandbox")) {
         this.gui.getSkirmishPanel().setGameMode(GameModeTemplateBase.GameMode.SANDBOX);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      for (int i = 0; i < 8; i++) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_PANEL, "saveSlot" + i)) {
            RegionalSavedGame mapSave = this.gui.getSkirmishPanel().getMapSave();
            ArrayList<File> savesList = SavedGamesHandler.getSortedListOfSaves(mapSave.getBaseSaveFolder(), mapSave.getGameMode());
            savesList.remove(0);
            int saveNumber = Integer.parseInt(savesList.get(i).getName().replaceAll("[^0-9]", ""));
            RegionalSavedGame swap = new RegionalSavedGame(
               mapSave.getGameMode(), mapSave.getGameType(), mapSave.getMapPackFolderName(), mapSave.getMapFileName(), saveNumber
            );
            Console.out("Loading: " + swap.getMapFileName(), true);
            Game.getPlayState().setGameMode(this.gui.getSkirmishPanel().getSelectedGameMode());
            save.setActiveSave(swap);
            this.gui.setDebug(false);
            transition.startTransition(Game.getPlayState());
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }
   }

   private void controlsMousePressedSkirmishNewGameWarning() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_NEW_GAME_WARNING_PANEL, "no")) {
         this.gui.toggleSkirmish();
         this.gui.getSkirmishNewGameWarningPanel().setConfirmation(false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_NEW_GAME_WARNING_PANEL, "yes")) {
         this.gui.getSkirmishNewGameWarningPanel().setConfirmation(true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_SKIRMISH_NEW_GAME_WARNING_PANEL, "really")) {
         RegionalSavedGame selectedSave = this.gui.getSkirmishPanel().getMapSave();
         String mapPackFolderName = selectedSave.getMapPackFolderName();
         String mapFileName = selectedSave.getMapFileName();
         String mapName = selectedSave.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapName", "null");
         Console.out("Loading: " + mapName, true);
         Game.getPlayState().setGameMode(this.gui.getSkirmishPanel().getSelectedGameMode());
         SavedGamesHandler.createNewRegionalSave(selectedSave, this.gui.getSkirmishPanel().getSelectedGameMode(), StateBase.GameType.SKIRMISH);
         save.setActiveSave(
            SavedGamesHandler.getRegionalSave(mapPackFolderName, mapFileName, StateBase.GameType.SKIRMISH, this.gui.getSkirmishPanel().getSelectedGameMode())
         );
         this.gui.setDebug(false);
         transition.startTransition(Game.getPlayState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedMapEditorLoadMap() throws SlickException {
      if (this.gui.getMapEditorLoadMapPanel().intersectsMapMapList()) {
         this.gui.getMapEditorLoadMapPanel().selectMap(this.mouse);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_LOAD_MAP_PANEL, "deleteMap")) {
         RegionalSavedGame s = this.gui.getMapEditorLoadMapPanel().getMapSave();
         this.gui.getMapEditorLoadMapDeleteWarningPanel().setMapName(s.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapName", "null"));
         this.gui.toggleMapEditorLoadMapDeleteWarning();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_LOAD_MAP_PANEL, "loadMap")) {
         save.setActiveSave(this.gui.getMapEditorLoadMapPanel().getMapSave());
         this.gui.setDebug(false);
         transition.startTransition(Game.getMapEditorState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getMapEditorLoadMapPanel().intersectsMapListPageUp()) {
         this.gui.getMapEditorLoadMapPanel().mapListUp();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getMapEditorLoadMapPanel().intersectsMapListPageDown()) {
         this.gui.getMapEditorLoadMapPanel().mapListDown();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getMapEditorLoadMapPanel().intersectsMapPackLeft()) {
         this.gui.getMapEditorLoadMapPanel().mapPackDown();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getMapEditorLoadMapPanel().intersectsMapPackRight()) {
         this.gui.getMapEditorLoadMapPanel().mapPackUp();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedMapEditorLoadMapDeleteWarning() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_LOAD_MAP_DELETE_WARNING_PANEL, "no")) {
         this.gui.getMapEditorLoadMapDeleteWarningPanel().setConfirmation(false);
         this.gui.toggleMapEditorLoadMap();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_LOAD_MAP_DELETE_WARNING_PANEL, "yes")) {
         this.gui.getMapEditorLoadMapDeleteWarningPanel().setConfirmation(true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_LOAD_MAP_DELETE_WARNING_PANEL, "really")) {
         RegionalSavedGame s = this.gui.getMapEditorLoadMapPanel().getMapSave();
         Console.out("Deleting: " + s.getMapPackFolderName() + "/" + s.getMapFileName(), true);
         this.gui.getMapEditorLoadMapPanel().releaseMapResources();
         this.gui.getSkirmishPanel().releaseMapResources();
         System.gc();

         try {
            Thread.sleep(250L);
         } catch (InterruptedException var5) {
            var5.printStackTrace();
         }

         File mapDelete = new File("maps/" + s.getMapPackFolderName() + "/" + s.getMapFileName() + ".zip");

         try {
            FileUtils.forceDelete(mapDelete);
         } catch (IOException var4) {
            var4.printStackTrace();
         }

         SavedGamesHandler.loadData();
         this.gui.getMapEditorLoadMapPanel().reloadMapList();
         this.gui.getMapEditorNewMapPanel().reloadMapList(gc);
         this.gui.getSkirmishPanel().reloadMapList();
         this.gui.getMapEditorLoadMapDeleteWarningPanel().setConfirmation(false);
         this.gui.toggleMapEditorLoadMap();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedMapEditorNewMap() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "deleteMapPack")) {
         this.gui.getMapEditorNewMapDeleteWarningPanel().setMapName(this.gui.getMapEditorNewMapPanel().getMapPackName());
         this.gui.toggleMapEditorNewMapDeleteWarning();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "mapName")) {
         this.gui.getMapEditorNewMapPanel().setFocusMapName();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "mapAuthor")) {
         this.gui.getMapEditorNewMapPanel().setFocusMapAuthor();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "createMap")) {
         String mapName = this.gui.getMapEditorNewMapPanel().getMapName();
         String mapFileName = this.gui.getMapEditorNewMapPanel().getMapFileName();
         String mapAuthor = this.gui.getMapEditorNewMapPanel().getMapAuthor();
         MapTilesLoader.TileSet baseTerrain = this.gui.getMapEditorNewMapPanel().getBaseTerrain();
         String mapPackName = this.gui.getMapEditorNewMapPanel().getMapPackName();
         String mapPackFileName = this.gui.getMapEditorNewMapPanel().getMapPackFileName();
         if (mapFileName.equals("")) {
            return;
         }

         if (mapAuthor.equals("")) {
            return;
         }

         SavedGamesHandler.createNewMap(mapName, mapAuthor, mapPackName, baseTerrain);
         RegionalSavedGame s = SavedGamesHandler.getRegionalSave(
            mapPackFileName, mapFileName, StateBase.GameType.SKIRMISH, GameModeTemplateBase.GameMode.MAP_EDITOR
         );
         save.setActiveSave(s);
         this.gui.setDebug(false);
         transition.startTransition(Game.getMapEditorState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getMapEditorNewMapPanel().intersectsMapPackList()) {
         this.gui.getMapEditorNewMapPanel().selectMapPack();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getMapEditorNewMapPanel().intersectsMapPackListPageUp()) {
         this.gui.getMapEditorNewMapPanel().mapListUp();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getMapEditorNewMapPanel().intersectsMapPackListPageDown()) {
         this.gui.getMapEditorNewMapPanel().mapListDown();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getMapEditorNewMapPanel().intersectsMapPackListNewPack()) {
         this.gui.getMapEditorNewMapPanel().setFocusMapListNewPack();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGrassGreen")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.GRASS_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGrassGreen");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGrassYellowBrown")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.GRASS_YELLOW_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGrassYellowBrown");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGrassEmeraldGreen")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.GRASS_EMERALD_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGrassEmeraldGreen");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGrassTeal")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.GRASS_TEAL);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGrassTeal");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainSnowWhite")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.SNOW_WHITE);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainSnowWhite");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainDirtLightBrown")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.DIRT_LIGHT_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainDirtLightBrown");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainDirtBrown")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.DIRT_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainDirtBrown");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainDirtDarkBrown")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.DIRT_DARK_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainDirtDarkBrown");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGravelGray")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.GRAVEL_GRAY);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGravelGray");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGravelBlue")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.GRAVEL_BLUE);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGravelBlue");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGravelRed")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.GRAVEL_RED);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainGravelRed");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainBricks")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.BRICKS);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainBricks");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainTiles")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.TILES);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainTiles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainSandTan")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.SAND_TAN);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainSandTan");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainSandBlack")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.SAND_BLACK);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainSandBlack");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainSandRed")) {
         this.gui.getMapEditorNewMapPanel().setBaseTerrain(MapTilesLoader.TileSet.SAND_RED);
         this.gui.select(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_PANEL, "terrainSandRed");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedMapEditorNewMapDeleteWarningPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_DELETE_WARNING_PANEL, "no")) {
         this.gui.toggleMapEditorNewMap();
         this.gui.getMapEditorNewMapDeleteWarningPanel().setConfirmation(false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_DELETE_WARNING_PANEL, "yes")) {
         this.gui.getMapEditorNewMapDeleteWarningPanel().setConfirmation(true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_MAP_EDITOR_NEW_MAP_DELETE_WARNING_PANEL, "really")) {
         Console.out("Deleting: " + this.gui.getMapEditorNewMapPanel().getMapPackFileName(), true);
         this.gui.getMapEditorLoadMapPanel().releaseMapResources();
         this.gui.getSkirmishPanel().releaseMapResources();
         System.gc();
         File mapDelete = new File("maps/" + this.gui.getMapEditorNewMapPanel().getMapPackFileName());

         try {
            FileUtils.deleteDirectory(mapDelete);
         } catch (IOException var3) {
            var3.printStackTrace();
         }

         SavedGamesHandler.loadData();
         this.gui.getMapEditorLoadMapPanel().reloadMapList();
         this.gui.getMapEditorNewMapPanel().reloadMapList(gc);
         this.gui.getSkirmishPanel().reloadMapList();
         this.gui.getMapEditorNewMapDeleteWarningPanel().setConfirmation(false);
         this.gui.toggleMapEditorNewMap();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedSettingsPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "ok")) {
         this.gui.toggleMain();

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
         if (this.gui.getSettingsPanel().isSettingsTwitchChannelFocused()) {
            this.gui.getSettingsPanel().resetSettingsTwitchChannel();
         }

         Game.reinitDisplay();
         this.updateRenderWindow();
         sound.resetVolume();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "cancel")) {
         this.gui.toggleMain();
         this.gui.getSettingsPanel().settingsReloadProperties();
         if (this.gui.getSettingsPanel().isSettingsTwitchChannelFocused()) {
            this.gui.getSettingsPanel().resetSettingsTwitchChannel();
         }

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

   private void controlsMousePressedSettingKeyChange() throws SlickException {
      if (this.gui.getSettingsPanel().settingsIntersectsKey()) {
         this.gui.toggleSettingsKeyChange();
         String keyName = this.gui.getSettingsPanel().getSettingsKey();
         this.gui.getSettingsPanel().setKeyName(keyName);
         this.gui.getSettingsPanel().setSettingsKeyChangeOldKey(this.gui.getSettingsPanel().getSettingsProfileProperties().getProperty(keyName));
         this.gui.getSettingsPanel().setKeyChangeMode(true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedCredits() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.MAIN_MENU_CREDITS, "back")) {
         sound.playMainMenuTheme();
         this.gui.toggleMain();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   @Override
   public BrushLoader getBrushLoader() {
      return this.gui.getBrushLoader();
   }

   @Override
   public GUIControllerBase getGUI() {
      return this.gui;
   }

   @Override
   public void mouseWheelMoved(int change) {
      if (!transition.getControlLockout() && !this.loadingRequired) {
         if (SettingsParser.getMouseWheelZoomingEnabled()) {
            if (change > 50) {
               ScaleControl.increaseWorldScale();
            }

            if (change < -50) {
               ScaleControl.decreaseWorldScale();
            }
         }
      }
   }
}
