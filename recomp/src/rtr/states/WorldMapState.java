package rtr.states;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import rtr.GodModule;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.SettingsParser;
import rtr.SoundModule;
import rtr.chest.Chest;
import rtr.console.Console;
import rtr.font.Text;
import rtr.goal.Goal;
import rtr.goal.GoalModule;
import rtr.gui.buttons.GUIButtonGoalStats;
import rtr.gui.states.BrushLoader;
import rtr.gui.states.GUIControllerBase;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.gui.states.shared.EdgeScrolling;
import rtr.help.tips.TipBase;
import rtr.mobs.MobModule;
import rtr.save.RegionalSavedGame;
import rtr.save.SavedGamesHandler;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class WorldMapState extends StateBase {
   private WorldMapGUIController gui;
   private int mouseXPressed;
   private int mouseYPressed = 0;

   public WorldMapState(int s) {
      super(s);
      this.gameMode = GameModeTemplateBase.GameMode.WORLD_MAP;
      this.loadModuleMap.put(ModuleBase.ModuleType.BACKGROUND, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MOB, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.FONT, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.HELP, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.PARTICLE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.PROJECTILE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.RESOURCE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.SAVE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.SOUND, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.TRANSITION, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MIGRATION, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.CHEST, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.PERK, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.GOD, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.GOAL, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.TIME, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.CORRUPTION, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.INFLUENCE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.SPAWN, true);
   }

   @Override
   protected void loadState() throws SlickException {
      super.loadState();
      if (this.modulesLoaded) {
         this.gui = new WorldMapGUIController(this.g, gc, this.mouse);
         this.loadingRequired = false;
         this.updateRenderWindow();
         transition.enter();
         ScaleControl.setWorldScale(2);
         this.endTimeTotal = System.currentTimeMillis();
         Console.out("TOTAL TIME: " + (this.endTimeTotal - this.startTimeTotal), true);
         this.currentLoad = Text.getText("module.loading.starting");
      }
   }

   @Override
   public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException {
      super.leave(gc, sbg);
   }

   @Override
   public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
      super.render(gc, sbg, g);
      if (this.loadingRequired) {
         this.renderLoadScreen();
      } else {
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
         background.render();
         this.gui.getWorldMap().render(this.gui.isDebug());
         particle.renderWorldMap(this.gui.getWorldMap().getMapX(), this.gui.getWorldMap().getMapY());
         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         this.gui.render();
         particle.renderGUI();
         Console.renderBanner(g);
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
         Console.update();
         background.update();
         sound.update();
         projectile.update();
         if (WorldMapGUIData.getSelectedGameMode() != null) {
            help.update();
            chest.updateWorldMap();
            god.updateWorldMap();
            goal.updateWorldMap();
         }

         transition.update();
         this.gui.update();
      }
   }

   @Override
   protected void controlsKeyboard() throws SlickException {
      if (!transition.getControlLockout()) {
         if (!this.gui.getGameModeConfigPanel().hasFocusSaveInput() && !this.gui.getDoomWorldPanel().hasFocusInput()) {
            if (WorldMapGUIData.getSelectedGameMode() != null) {
               this.controlsKeyboardMap();
            }

            this.controlsKeyboardScreenshot();
            this.controlsKeyboardFullScreen();
            this.controlsKeyboardDebug();
            this.controlsKeyboardTips();
         }
      }
   }

   private void controlsKeyboardMap() throws SlickException {
      if (input.isKeyDown(SettingsParser.getKeyUp()) || input.isKeyDown(200)) {
         this.gui.getWorldMap().moveMapKeyboard(0.0F, 100.0F);
         this.gui.closeAll();
      }

      if (input.isKeyDown(SettingsParser.getKeyDown()) || input.isKeyDown(208)) {
         this.gui.getWorldMap().moveMapKeyboard(0.0F, -100.0F);
         this.gui.closeAll();
      }

      if (input.isKeyDown(SettingsParser.getKeyLeft()) || input.isKeyDown(203)) {
         this.gui.getWorldMap().moveMapKeyboard(100.0F, 0.0F);
         this.gui.closeAll();
      }

      if (input.isKeyDown(SettingsParser.getKeyRight()) || input.isKeyDown(205)) {
         this.gui.getWorldMap().moveMapKeyboard(-100.0F, 0.0F);
         this.gui.closeAll();
      }

      if (input.isKeyPressed(SettingsParser.getKeyZoomIn())) {
         ScaleControl.increaseWorldScale();
      }

      if (input.isKeyPressed(SettingsParser.getKeyZoomOut())) {
         ScaleControl.decreaseWorldScale();
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

   private void controlsKeyboardTips() throws SlickException {
      if (input.isKeyPressed(SettingsParser.getKeyCancel())) {
         if (!this.gui.isGameModeSelectOverlayPanelEnabled()) {
            if (this.gui.isTabsClosed()) {
               save.getActiveWorldSave().fullSaveSingleThreaded();
               this.gui.setDebug(false);
               transition.startTransition(Game.getMainMenuState());
            } else {
               this.gui.resetGUI();
            }
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   @Override
   protected void controlsMouse() throws SlickException {
      super.controlsMouse();
      if (!transition.getControlLockout()) {
         if (SettingsParser.getEdgeScrollingEnabled() && WorldMapGUIData.getSelectedGameMode() != null) {
            this.controlsEdgeScrolling();
         }

         if (input.isMousePressed(1)) {
            this.controlsMousePressedGameModeConfigPanel(true);
            if (this.gui.intersectsAnyGUIMask()) {
               return;
            }

            this.mouseXPressed = this.mouseX;
            this.mouseYPressed = this.mouseY;
         }

         if (input.isMouseButtonDown(1)) {
            if (this.gui.intersectsAnyGUIMask()
               || WorldMapGUIData.getSelectedGameMode() == null
               || this.gui.isGameModeConfigPanelEnabled()
               || this.gui.isGameModeSelectOverlayPanelEnabled()) {
               return;
            }

            this.gui.getWorldMap().moveMapMouse(this.mouseXPressed, this.mouseYPressed, this.mouseX, this.mouseY);
            this.gui.closeAll();
         }

         if (input.isMousePressed(0)) {
            Chest selectedChest = this.gui.getChestOpenPanel().getSelectedChest();
            GUIButtonGoalStats selectedGoal = this.gui.getGoalClaimPanel().getSelectedGoalButton();
            this.controlsMousePressedChestOpenPanel();
            this.controlsMousePressedActiveGoalsList();
            this.controlsMousePressedGoalClaimPanel();
            if ((selectedChest == null || !selectedChest.isOpening() && !selectedChest.isOpen()) && selectedGoal == null) {
               this.controlsMousePressedSystemPanel();
               this.controlsMousePressedTipsPanel();
               this.controlsMousePressedDoomWorldPanel();
               this.controlsMousePressedGameModeConfigPanel(false);
               this.controlsMousePressedGameModeSelectPanel();
               this.controlsMousePressedMapSelectPanel();
               this.controlsMousePressedRewardChestPanel();
               this.controlsMousePressedGoalsPanel();
               this.controlsMousePressedPerksPanel();
               if (this.gui.intersectsAnyGUIMask()) {
                  return;
               }

               if (WorldMapGUIData.getSelectedGameMode() != null) {
                  this.controlsMousePressedWorldMap();
               }
            }
         }
      }
   }

   private void controlsEdgeScrolling() {
      int edgeScrollSpeed = SettingsParser.getEdgeScrollSpeed();
      EdgeScrolling edge = this.gui.getEdgeScrolling();
      if (edge.intersectsNorth(this.mouse) && edge.intersectsEast(this.mouse)) {
         this.gui.getWorldMap().moveMapKeyboard(edgeScrollSpeed * -1, edgeScrollSpeed);
         this.gui.closeAll();
      } else if (edge.intersectsNorth(this.mouse) && edge.intersectsWest(this.mouse)) {
         this.gui.getWorldMap().moveMapKeyboard(edgeScrollSpeed, edgeScrollSpeed);
         this.gui.closeAll();
      } else if (edge.intersectsSouth(this.mouse) && edge.intersectsEast(this.mouse)) {
         this.gui.getWorldMap().moveMapKeyboard(edgeScrollSpeed * -1, edgeScrollSpeed * -1);
         this.gui.closeAll();
      } else if (edge.intersectsSouth(this.mouse) && edge.intersectsWest(this.mouse)) {
         this.gui.getWorldMap().moveMapKeyboard(edgeScrollSpeed, edgeScrollSpeed * -1);
         this.gui.closeAll();
      } else if (edge.intersectsNorth(this.mouse)) {
         this.gui.getWorldMap().moveMapKeyboard(0.0F, edgeScrollSpeed);
         this.gui.closeAll();
      } else if (edge.intersectsEast(this.mouse)) {
         this.gui.getWorldMap().moveMapKeyboard(edgeScrollSpeed * -1, 0.0F);
         this.gui.closeAll();
      } else if (edge.intersectsSouth(this.mouse)) {
         this.gui.getWorldMap().moveMapKeyboard(0.0F, edgeScrollSpeed * -1);
         this.gui.closeAll();
      } else if (edge.intersectsWest(this.mouse)) {
         this.gui.getWorldMap().moveMapKeyboard(edgeScrollSpeed, 0.0F);
         this.gui.closeAll();
      }
   }

   private void controlsMousePressedSystemPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_SYSTEM_PANEL, "mainMenu")) {
         save.getActiveWorldSave().fullSaveSingleThreaded();
         this.gui.setDebug(false);
         transition.startTransition(Game.getMainMenuState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_SYSTEM_PANEL, "doomWorld")) {
         this.gui.toggleDoomWorldPanel();
         this.gui.deselectAll();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_SYSTEM_PANEL, "viewGameModeConfig")) {
         this.gui.toggleGameModeConfigPanel();
         this.gui.deselectAll();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_SYSTEM_PANEL, "changeGameMode")) {
         this.gui.toggleGameModeSelectOverlayPanel();
         this.gui.deselectAll();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedTipsPanel() throws SlickException {
      if (!this.gui.isHideGUI() && !this.gui.intersectsAnyGUIMask()) {
         TipBase tip = help.getIntersectingTip(this.mouse);
         if (tip != null) {
            help.removeTip(tip);
            this.gui.getTipsPanel().setTip(tip);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_TIPS_PANEL, "ok")) {
         this.gui.getTipsPanel().clearActiveTip();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedDoomWorldPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_DOOM_WORLD_PANEL, "input")) {
         this.gui.getDoomWorldPanel().setFocusInput();
      } else {
         this.gui.getDoomWorldPanel().resetInput();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_DOOM_WORLD_PANEL, "doomWorld")) {
         goal.incrementGoal(GoalModule.GoalType.DOOOOMED);
         save.getActiveWorldSave().fullSaveSingleThreaded();
         GameModeTemplateBase.GameMode.CUSTOM.getTemplate().copyTemplate(GameModeTemplateBase.GameMode.SURVIVAL);
         save.resetWorldMap(WorldMapGUIData.getSelectedGameMode());
         this.gui.setDebug(false);
         transition.startTransition(Game.getWorldMapState());
         this.gui.deselectAll();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedGameModeConfigPanel(boolean rightClick) throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "baseSpawnRateLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseBaseSpawnRate(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "baseSpawnRateRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseBaseSpawnRate(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "boostedMonsterChanceMultiplierLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseBoostedMonsterChanceMultiplier(rightClick ? 1.0 : 0.25);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "boostedMonsterChanceMultiplierRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseBoostedMonsterChanceMultiplier(rightClick ? 1.0 : 0.25);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionStartDayLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseCorruptionStartDay(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionStartDayRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseCorruptionStartDay(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionDesiredAmountStartLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseCorruptionDesiredAmountStart(rightClick ? 1024 : 64);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionDesiredAmountStartRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseCorruptionDesiredAmountStart(rightClick ? 1024 : 64);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionDesiredAmountPerDayLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseCorruptionDesiredAmountPerDay(rightClick ? 1024 : 64);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionDesiredAmountPerDayRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseCorruptionDesiredAmountPerDay(rightClick ? 1024 : 64);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionDesiredAmountPerBuildingLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseCorruptionDesiredAmountPerBuilding(rightClick ? 64 : 16);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionDesiredAmountPerBuildingRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseCorruptionDesiredAmountPerBuilding(rightClick ? 64 : 16);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionBuildingRateLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseCorruptionBuildingRate(rightClick ? 5000 : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionBuildingRateRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseCorruptionBuildingRate(rightClick ? 5000 : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionBuildingDensityLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseCorruptionBuildingDensity(rightClick ? 8 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionBuildingDensityRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseCorruptionBuildingDensity(rightClick ? 8 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionBuildingDensityRangeLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseCorruptionBuildingDensityRange(rightClick ? 32 : 8);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "corruptionBuildingDensityRangeRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseCorruptionBuildingDensityRange(rightClick ? 32 : 8);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "maximumLootBoxesLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseMaximumLootBoxes(rightClick ? 10 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "maximumLootBoxesRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseMaximumLootBoxes(rightClick ? 10 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "lootBoxUncoverChanceLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseLootBoxUncoverChance(rightClick ? 500 : 50);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "lootBoxUncoverChanceRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseLootBoxUncoverChance(rightClick ? 500 : 50);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "lootBoxKeyChanceLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseLootBoxKeyChance(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "lootBoxKeyChanceRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseLootBoxKeyChance(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "baseNomadSpawnRateLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseBaseNomadSpawnRate(rightClick ? 5000 : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "baseNomadSpawnRateRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseBaseNomadSpawnRate(rightClick ? 5000 : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "nomadSpawnAmountMultiplierLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseNomadSpawnAmountMultiplier(rightClick ? 1.0 : 0.25);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "nomadSpawnAmountMultiplierRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseNomadSpawnAmountMultiplier(rightClick ? 1.0 : 0.25);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "nomadSpawnAmountMinimumLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseNomadSpawnAmountMinimum(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "nomadSpawnAmountMinimumRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseNomadSpawnAmountMinimum(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "nomadSpawnAmountMaximumLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseNomadSpawnAmountMaximum(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "nomadSpawnAmountMaximumRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseNomadSpawnAmountMaximum(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "temperatureVarianceLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseTemperatureVariance(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "temperatureVarianceRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseTemperatureVariance(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "startingSeasonLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseSeasonStart(1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "startingSeasonRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseSeasonStart(1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "weatherChanceSpringLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseWeatherChanceSpring(rightClick ? 10000 : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "weatherChanceSpringRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseWeatherChanceSpring(rightClick ? 10000 : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "averageTemperatureSpringLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseAverageTemperatureSpring(rightClick ? 10 : 5);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "averageTemperatureSpringRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseAverageTemperatureSpring(rightClick ? 10 : 5);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "seasonLengthSpringLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseSeasonLengthSpring(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "seasonLengthSpringRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseSeasonLengthSpring(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "weatherChanceSummerLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseWeatherChanceSummer(rightClick ? 10000 : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "weatherChanceSummerRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseWeatherChanceSummer(rightClick ? 10000 : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "averageTemperatureSummerLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseAverageTemperatureSummer(rightClick ? 10 : 5);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "averageTemperatureSummerRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseAverageTemperatureSummer(rightClick ? 10 : 5);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "seasonLengthSummerLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseSeasonLengthSummer(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "seasonLengthSummerRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseSeasonLengthSummer(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "weatherChanceAutumnLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseWeatherChanceAutumn(rightClick ? 10000 : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "weatherChanceAutumnRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseWeatherChanceAutumn(rightClick ? 10000 : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "averageTemperatureAutumnLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseAverageTemperatureAutumn(rightClick ? 10 : 5);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "averageTemperatureAutumnRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseAverageTemperatureAutumn(rightClick ? 10 : 5);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "seasonLengthAutumnLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseSeasonLengthAutumn(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "seasonLengthAutumnRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseSeasonLengthAutumn(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "weatherChanceWinterLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseWeatherChanceWinter(rightClick ? 10000 : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "weatherChanceWinterRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseWeatherChanceWinter(rightClick ? 10000 : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "averageTemperatureWinterLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseAverageTemperatureWinter(rightClick ? 10 : 5);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "averageTemperatureWinterRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseAverageTemperatureWinter(rightClick ? 10 : 5);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "seasonLengthWinterLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseSeasonLengthWinter(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "seasonLengthWinterRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseSeasonLengthWinter(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "fullMoonIntervalLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseFullMoonInterval(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "fullMoonIntervalRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseFullMoonInterval(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "bloodMoonChanceLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseBloodMoonChance(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "bloodMoonChanceRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseBloodMoonChance(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "bloodMoonDelayLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseBloodMoonDelay(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "bloodMoonDelayRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseBloodMoonDelay(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "allowBloodMoonSpringLeft")
         || this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "allowBloodMoonSpringRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.toggleAllowBloodMoonSpring();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "allowBloodMoonSummerLeft")
         || this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "allowBloodMoonSummerRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.toggleAllowBloodMoonSummer();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "allowBloodMoonAutumnLeft")
         || this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "allowBloodMoonAutumnRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.toggleAllowBloodMoonAutumn();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "allowBloodMoonWinterLeft")
         || this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "allowBloodMoonWinterRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.toggleAllowBloodMoonWinter();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "meteorShowerChanceLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseMeteorShowerChance(rightClick ? '썐' : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "meteorShowerChanceRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseMeteorShowerChance(rightClick ? '썐' : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "earthquakeChanceLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseEarthquakeChance(rightClick ? '썐' : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "earthquakeChanceRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseEarthquakeChance(rightClick ? '썐' : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "electricalStormChanceLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseElectricalStormChance(rightClick ? '썐' : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "electricalStormChanceRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseElectricalStormChance(rightClick ? '썐' : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "hailStormChanceLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseHailStormChance(rightClick ? '썐' : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "hailStormChanceRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseHailStormChance(rightClick ? '썐' : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "blightChanceLeft")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.decreaseBlightChance(rightClick ? '썐' : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "blightChanceRight")) {
         GameModeTemplateBase t = GameModeTemplateBase.GameMode.CUSTOM.getTemplate();
         t.increaseBlightChance(rightClick ? '썐' : 1000);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "resetNightmare")) {
         GameModeTemplateBase.GameMode.CUSTOM.getTemplate().copyTemplate(GameModeTemplateBase.GameMode.NIGHTMARE);
         this.gui.getGameModeConfigPanel().resetSaveInputText();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "resetSurvival")) {
         GameModeTemplateBase.GameMode.CUSTOM.getTemplate().copyTemplate(GameModeTemplateBase.GameMode.SURVIVAL);
         this.gui.getGameModeConfigPanel().resetSaveInputText();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "resetTraditional")) {
         GameModeTemplateBase.GameMode.CUSTOM.getTemplate().copyTemplate(GameModeTemplateBase.GameMode.TRADITIONAL);
         this.gui.getGameModeConfigPanel().resetSaveInputText();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "resetPeaceful")) {
         GameModeTemplateBase.GameMode.CUSTOM.getTemplate().copyTemplate(GameModeTemplateBase.GameMode.PEACEFUL);
         this.gui.getGameModeConfigPanel().resetSaveInputText();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "close")) {
         this.gui.closeAll();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      for (int i = 0; i < 16; i++) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "customListDelete" + i)) {
            GameModeTemplateBase.GameMode.CUSTOM.getTemplate().deleteTemplate(this.gui.getGameModeConfigPanel().getConfigName(i));
            this.gui.getGameModeConfigPanel().generateList();
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         } else if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "customList" + i)) {
            GameModeTemplateBase.GameMode.CUSTOM.getTemplate().loadTemplate(this.gui.getGameModeConfigPanel().getConfigName(i));
            this.gui.getGameModeConfigPanel().setSaveInputText(this.gui.getGameModeConfigPanel().getConfigName(i));
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "save")) {
         GameModeTemplateBase.GameMode.CUSTOM.getTemplate().saveTemplate(this.gui.getGameModeConfigPanel().getSaveInputText());
         this.gui.getGameModeConfigPanel().generateList();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "saveInput")) {
         this.gui.getGameModeConfigPanel().setFocusSaveInput(true);
      } else if (!this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "save")
         && this.gui.getGameModeConfigPanel().hasFocusSaveInput()) {
         this.gui.getGameModeConfigPanel().setFocusSaveInput(false);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "accept")) {
         SettingsParser.setLastGameMode(GameModeTemplateBase.GameMode.CUSTOM);
         save.resetWorldMap(GameModeTemplateBase.GameMode.CUSTOM);
         GameModeTemplateBase.GameMode.CUSTOM.getTemplate().saveSetCustomTemplate();
         GameModeTemplateBase.GameMode.CUSTOM.getTemplate().loadCustomProfileConfig();
         WorldMapGUIData.refreshData();
         RegionalSavedGame mapSave = this.gui.getMapSelectPanel().getMapSave();
         if (mapSave != null) {
            if (SavedGamesHandler.regionalSaveExist(
               mapSave.getMapPackFolderName(), mapSave.getMapFileName(), StateBase.GameType.WORLD_MAP, WorldMapGUIData.getSelectedGameMode()
            )) {
               this.gui
                  .getMapSelectPanel()
                  .setMapSave(
                     SavedGamesHandler.getRegionalSave(
                        mapSave.getMapPackFolderName(), mapSave.getMapFileName(), StateBase.GameType.WORLD_MAP, WorldMapGUIData.getSelectedGameMode()
                     )
                  );
            } else {
               this.gui
                  .getMapSelectPanel()
                  .setMapSave(
                     SavedGamesHandler.getRegionalSave(
                        mapSave.getMapPackFolderName(), mapSave.getMapFileName(), StateBase.GameType.WORLD_MAP, GameModeTemplateBase.GameMode.MAP_EDITOR
                     )
                  );
            }
         }

         this.gui.getWorldMap().initFlash();
         this.gui.getWorldMap().buildWorldMap();
         this.gui.deselectAll();
         this.gui.closeAll();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_CONFIG_PANEL, "back")) {
         if (!WorldMapGUIData.hasCustomConfig()) {
            this.gui.toggleGameModeSelectOverlayPanel();
         } else {
            this.gui.closeAll();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedGameModeSelectPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_SELECT_PANEL, "nightmare")) {
         if (WorldMapGUIData.getSelectedGameMode() != null) {
            save.getActiveWorldSave().fullSaveSingleThreaded();
         }

         SettingsParser.setLastGameMode(GameModeTemplateBase.GameMode.NIGHTMARE);
         this.gui.setDebug(false);
         transition.startTransition(Game.getWorldMapState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_SELECT_PANEL, "survival")) {
         if (WorldMapGUIData.getSelectedGameMode() != null) {
            save.getActiveWorldSave().fullSaveSingleThreaded();
         }

         SettingsParser.setLastGameMode(GameModeTemplateBase.GameMode.SURVIVAL);
         this.gui.setDebug(false);
         transition.startTransition(Game.getWorldMapState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_SELECT_PANEL, "traditional")) {
         if (WorldMapGUIData.getSelectedGameMode() != null) {
            save.getActiveWorldSave().fullSaveSingleThreaded();
         }

         SettingsParser.setLastGameMode(GameModeTemplateBase.GameMode.TRADITIONAL);
         this.gui.setDebug(false);
         transition.startTransition(Game.getWorldMapState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_SELECT_PANEL, "peaceful")) {
         if (WorldMapGUIData.getSelectedGameMode() != null) {
            save.getActiveWorldSave().fullSaveSingleThreaded();
         }

         SettingsParser.setLastGameMode(GameModeTemplateBase.GameMode.PEACEFUL);
         this.gui.setDebug(false);
         transition.startTransition(Game.getWorldMapState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_SELECT_PANEL, "custom")) {
         if (WorldMapGUIData.getSelectedGameMode() != null) {
            save.getActiveWorldSave().fullSaveSingleThreaded();
         }

         SettingsParser.setLastGameMode(GameModeTemplateBase.GameMode.CUSTOM);
         this.gui.setDebug(false);
         transition.startTransition(Game.getWorldMapState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GAME_MODE_SELECT_PANEL, "sandbox")) {
         if (WorldMapGUIData.getSelectedGameMode() != null) {
            save.getActiveWorldSave().fullSaveSingleThreaded();
         }

         SettingsParser.setLastGameMode(GameModeTemplateBase.GameMode.SANDBOX);
         this.gui.setDebug(false);
         transition.startTransition(Game.getWorldMapState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedMapSelectPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_MAP_SELECT_PANEL, "visitRegion")
         || this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_MAP_SELECT_PANEL, "establishVillage")) {
         save.getActiveWorldSave().fullSaveSingleThreaded();
         SavedGamesHandler.loadData();
         RegionalSavedGame s = this.gui.getMapSelectPanel().getMapSave();
         Console.out("Loading: " + s.getMapFileName(), true);
         if (s.getGameMode() == GameModeTemplateBase.GameMode.MAP_EDITOR) {
            SavedGamesHandler.createNewRegionalSave(s, WorldMapGUIData.getSelectedGameMode(), StateBase.GameType.WORLD_MAP);
            s = SavedGamesHandler.getRegionalSave(
               s.getMapPackFolderName(), s.getMapFileName(), StateBase.GameType.WORLD_MAP, WorldMapGUIData.getSelectedGameMode()
            );
         }

         Game.getPlayState().setGameMode(WorldMapGUIData.getSelectedGameMode());
         save.setActiveSave(s);
         this.gui.setDebug(false);
         transition.startTransition(Game.getPlayState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_MAP_SELECT_PANEL, "visitRegionResetAI")) {
         save.getActiveWorldSave().fullSaveSingleThreaded();
         SavedGamesHandler.loadData();
         RegionalSavedGame s = this.gui.getMapSelectPanel().getMapSave();
         Console.out("Loading: " + s.getMapFileName(), true);
         if (s.getGameMode() == GameModeTemplateBase.GameMode.MAP_EDITOR) {
            SavedGamesHandler.createNewRegionalSave(s, WorldMapGUIData.getSelectedGameMode(), StateBase.GameType.WORLD_MAP);
            s = SavedGamesHandler.getRegionalSave(
               s.getMapPackFolderName(), s.getMapFileName(), StateBase.GameType.WORLD_MAP, WorldMapGUIData.getSelectedGameMode()
            );
         }

         Game.getPlayState();
         MobModule thisMob = (MobModule)PlayState.getModule(ModuleBase.ModuleType.MOB);
         thisMob.toggleDebugResetAI();
         Game.getPlayState().setGameMode(WorldMapGUIData.getSelectedGameMode());
         save.setActiveSave(s);
         this.gui.setDebug(false);
         transition.startTransition(Game.getPlayState());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_MAP_SELECT_PANEL, "close")) {
         this.gui.closeAll();
         this.gui.deselectAll();
         this.gui.getWorldMap().deselectAllWorldMap();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      for (int i = 0; i < 8; i++) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_MAP_SELECT_PANEL, "saveSlot" + i)) {
            save.getActiveWorldSave().fullSaveSingleThreaded();
            RegionalSavedGame mapSave = this.gui.getMapSelectPanel().getMapSave();
            ArrayList<File> savesList = SavedGamesHandler.getSortedListOfSaves(mapSave.getBaseSaveFolder(), mapSave.getGameMode());
            savesList.remove(0);
            int saveNumber = Integer.parseInt(savesList.get(i).getName().replaceAll("[^0-9]", ""));
            RegionalSavedGame swap = new RegionalSavedGame(
               mapSave.getGameMode(), mapSave.getGameType(), mapSave.getMapPackFolderName(), mapSave.getMapFileName(), saveNumber
            );
            Console.out("Loading: " + swap.getMapFileName(), true);
            Game.getPlayState().setGameMode(WorldMapGUIData.getSelectedGameMode());
            save.setActiveSave(swap);
            this.gui.setDebug(false);
            transition.startTransition(Game.getPlayState());
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }
   }

   private void controlsMousePressedRewardChestPanel() throws SlickException {
      Chest selectedChest = chest.getIntersectedChest(this.mouse);
      if (selectedChest != null && god.getGodXP() >= selectedChest.getOpenCost()) {
         this.gui.closeAll();
         chest.removeChest(selectedChest);
         god.decreaseGodXP(selectedChest.getOpenCost());
         ArrayList<PerkModule.Perk> newPerks = perk.generateAndGiveNewRandomPerks(selectedChest.getPerksAmount());
         this.gui.getChestOpenPanel().startOpenSelectedChest(selectedChest, newPerks);
         save.getActiveWorldSave().fullSaveSingleThreaded();
      }
   }

   private void controlsMousePressedChestOpenPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_CHEST_OPEN_PANEL, "takeIt")) {
         this.gui.getChestOpenPanel().reset();
         this.gui.getPerksList().refreshPerksList();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedActiveGoalsList() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_ACTIVE_GOALS_LIST, "pageUp")) {
         this.gui.getActiveGoalsList().pageUp();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      } else if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_ACTIVE_GOALS_LIST, "pageDown")) {
         this.gui.getActiveGoalsList().pageDown();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      } else if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_ACTIVE_GOALS_LIST, "openGoalPanel")
         || this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_ACTIVE_GOALS_LIST, "activeGoalButton")) {
         this.gui.toggleGoalsPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedGoalsPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_GOALS_PANEL, "slider")) {
         this.gui.getGoalsPanel().lockSlider();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      HashMap<GoalModule.GoalType, Goal> allGoals = goal.getAllGoals();

      for (Goal g : allGoals.values()) {
         if (!g.isLocked()
            && !g.isClaimed()
            && this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_GOALS_PANEL, g.getSystemName())
            && (g.isCompleted() || g.isFreeChest() && !chest.isChestsFull())) {
            g.setClaimed();
            this.gui.getGoalsPanel().playEffects(g);
            sound.playSound(SoundModule.SoundType.BELL);
            sound.playSound(SoundModule.SoundType.HOLY_SOFT);
            sound.playSound(SoundModule.SoundType.LOOT_BOX_LOCKED);
            sound.playSound(SoundModule.SoundType.MAGIC_HEAL);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            god.increaseGodXP(g.getGodXPAmount(), GodModule.GodXPType.UNLOCKED_GOAL);
            if (g.isFreeChest()) {
               chest.grantChest(2);
            }

            save.getActiveWorldSave().fullSaveSingleThreaded();
         }
      }
   }

   private void controlsMousePressedGoalClaimPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_GOAL_CLAIM_PANEL, "takeIt")) {
         this.gui.getGoalClaimPanel().reset();
         this.gui.getPerksList().refreshPerksList();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedPerksPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_PERKS_PANEL, "pageUp")) {
         this.gui.getPerksList().pageUp();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_PERKS_PANEL, "pageDown")) {
         this.gui.getPerksList().pageDown();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      PerkModule.PerkType[] var4;
      for (PerkModule.PerkType t : PerkModule.PerkType.values()) {
         if (perk.getPerkTypeTotal(t) > 0.0 && this.gui.intersects(GUIControllerBase.GUIPanel.WORLD_MAP_PERKS_PANEL, "perkType" + t)) {
            this.gui.select(GUIControllerBase.GUIPanel.WORLD_MAP_PERKS_PANEL, "perkType" + t);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }
   }

   private void controlsMousePressedWorldMap() throws SlickException {
      for (String mapName : this.gui.getWorldMap().getWorldMapList()) {
         mapName = mapName.replaceAll(" ", "");
         if (this.gui.getWorldMap().intersectsWorldMap("worldMap" + mapName)) {
            if (SavedGamesHandler.regionalSaveExist("WorldMaps", mapName, StateBase.GameType.WORLD_MAP, WorldMapGUIData.getSelectedGameMode())) {
               this.gui
                  .getMapSelectPanel()
                  .setMapSave(SavedGamesHandler.getRegionalSave("WorldMaps", mapName, StateBase.GameType.WORLD_MAP, WorldMapGUIData.getSelectedGameMode()));
            } else {
               this.gui
                  .getMapSelectPanel()
                  .setMapSave(SavedGamesHandler.getRegionalSave("WorldMaps", mapName, StateBase.GameType.WORLD_MAP, GameModeTemplateBase.GameMode.MAP_EDITOR));
            }

            this.gui.toggleMapSelectPanel();
            this.gui.getWorldMap().selectWorldMap("worldMap" + mapName);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            break;
         }
      }
   }

   @Override
   public void mouseWheelMoved(int change) {
      if (!transition.getControlLockout() && !this.loadingRequired) {
         if (this.gui.getActiveGoalsList().intersectsGUIMask()) {
            if (change > 50) {
               this.gui.getActiveGoalsList().moveUp();
            }

            if (change < -50) {
               this.gui.getActiveGoalsList().moveDown();
            }
         } else if (this.gui.getPerksList().intersectsGUIMask()) {
            if (change > 50) {
               this.gui.getPerksList().moveUp();
            }

            if (change < -50) {
               this.gui.getPerksList().moveDown();
            }
         } else if (this.gui.getGoalsPanel().intersectsGUIMask()) {
            if (change > 50) {
               this.gui.getGoalsPanel().moveUp();
            }

            if (change < -50) {
               this.gui.getGoalsPanel().moveDown();
            }
         } else if (SettingsParser.getMouseWheelZoomingEnabled()) {
            if (change > 50) {
               ScaleControl.increaseWorldScale();
            }

            if (change < -50) {
               ScaleControl.decreaseWorldScale();
            }
         }
      }
   }

   @Override
   public int getMouseOnMapX() {
      return (int)Math.floor(this.gui.getWorldMap().getMapX() * -1.0F + this.mouseX / ScaleControl.getScalingDifference());
   }

   @Override
   public int getMouseOnMapY() {
      return (int)Math.floor(this.gui.getWorldMap().getMapY() * -1.0F + this.mouseY / ScaleControl.getScalingDifference());
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
