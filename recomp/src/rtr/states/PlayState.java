package rtr.states;

import rtr.road.RoadModule;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import rtr.DataModule;
import rtr.GodModule;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.goal.Goal;
import rtr.goal.GoalModule;
import rtr.gui.GUIEnums;
import rtr.gui.states.BrushLoader;
import rtr.gui.states.GUIControllerBase;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.playstate.StatisticsPanel;
import rtr.gui.states.shared.EdgeScrolling;
import rtr.help.tips.TipBase;
import rtr.influence.SpellBase;
import rtr.map.MapData;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceModule;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class PlayState extends StateBase {
   private PlayStateGUIController gui;
   private boolean gamePaused;
   private int advanceTimeTo = 2;
   private boolean advanceTimeRunning;
   private boolean triggerCloseSpellSelectOnRelease;

   public PlayState(int s) {
      super(s);
      this.gameMode = GameModeTemplateBase.GameMode.SURVIVAL;
      this.loadModuleMap.put(ModuleBase.ModuleType.BACKGROUND, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.COLLISION, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.DATA, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.DEPTH, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MOB, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MOB_JOB, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.ESSENCE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.FONT, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.LIGHTING, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MAP, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MAP_AI, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.WAYPOINT, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MINI_MAP, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MISSILE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.OBJECT, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.PARTICLE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.PROFILE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.PROJECTILE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.RESOURCE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.TRADE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.LOOT_BOX, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.ROAD, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.SAVE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.SHADOW, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.SOUND, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.SPAWN, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.TIME, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.TRANSITION, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.INFLUENCE, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.WEATHER, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.CLOUD, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.WORK_SELECTION, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.HELP, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.STATS, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.CORRUPTION, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.MIGRATION, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.CHEST, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.PERK, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.GOD, true);
      this.loadModuleMap.put(ModuleBase.ModuleType.GOAL, true);
   }

   @Override
   protected void loadState() throws SlickException {
      super.loadState();
      if (this.modulesLoaded) {
         this.loadingRequired = false;
         this.gui = new PlayStateGUIController(this.g, gc, this.mouse);
         this.updateRenderWindow();
         transition.enter();
         if (influence.getMobGrabbed() != null || influence.getResourceGrabbed() != null) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.INFLUENCE);
            this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_SPELL_BAR, "spellGrab");
            influence.setSelectedSpell(SpellBase.SpellType.DROP);
         }

         this.endTimeTotal = System.currentTimeMillis();
         Console.out("TOTAL TIME: " + (this.endTimeTotal - this.startTimeTotal), true);
         this.currentLoad = Text.getText("module.loading.starting");
      }
   }

   @Override
   public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException {
      super.leave(gc, sbg);
      this.gamePaused = false;
   }

   @Override
   public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
      super.render(gc, sbg, g);
      if (this.loadingRequired) {
         this.renderLoadScreen();
      } else {
         g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
         boolean profiling = false;
         int profilingCap = 10;
         if (profiling) {
            Utilities.startTimer();
         }

         background.render();
         if (profiling) {
            Utilities.endTimer("Background: ", profilingCap);
            Utilities.startTimer();
         }

         map.render();
         if (profiling) {
            Utilities.endTimer("Map: ", profilingCap);
            Utilities.startTimer();
         }

         shadow.render();
         if (profiling) {
            Utilities.endTimer("Shadow: ", profilingCap);
         }

         if (data.getDisplayMode() != DataModule.DataDisplayMode.HIDE_TOPOGRAPHY && data.getLockedDisplayMode() != DataModule.DataDisplayMode.HIDE_TOPOGRAPHY) {
            if (profiling) {
               Utilities.startTimer();
            }

            if (profiling) {
               Utilities.endTimer("Map: ", profilingCap);
               Utilities.startTimer();
            }

            particle.renderBottom();
            if (profiling) {
               Utilities.endTimer("Particle Bottom: ", profilingCap);
            }
         }

         if (profiling) {
            Utilities.startTimer();
         }

         resource.renderResourcesOnGround(g, this.gui.isDebug());
         if (profiling) {
            Utilities.endTimer("Resource On Ground: ", profilingCap);
            Utilities.startTimer();
         }

         missile.renderBottom();
         if (profiling) {
            Utilities.endTimer("Missile: ", profilingCap);
            Utilities.startTimer();
         }

         object.renderGates();
         if (profiling) {
            Utilities.endTimer("Object Gates: ", profilingCap);
            Utilities.startTimer();
         }

         mob.render(g, this.gamePaused, this.gui.isHideGUI());
         if (profiling) {
            Utilities.endTimer("Mob: ", profilingCap);
            Utilities.startTimer();
         }

         missile.renderTop();
         if (profiling) {
            Utilities.endTimer("Missile: ", profilingCap);
            Utilities.startTimer();
         }

         object.renderTowers();
         if (profiling) {
            Utilities.endTimer("Object Towers: ", profilingCap);
            Utilities.startTimer();
         }

         essence.render(g);
         if (profiling) {
            Utilities.endTimer("Essence: ", profilingCap);
            Utilities.startTimer();
         }

         particle.renderTop();
         if (profiling) {
            Utilities.endTimer("Particle Top: ", profilingCap);
            Utilities.startTimer();
         }

         resource.renderResourcesInAir();
         if (profiling) {
            Utilities.endTimer("Resource In Air: ", profilingCap);
            Utilities.startTimer();
         }

         corruption.render(g);
         if (profiling) {
            Utilities.endTimer("Corruption: ", profilingCap);
            Utilities.startTimer();
         }

         lighting.render(g);
         if (profiling) {
            Utilities.endTimer("Lighting: ", profilingCap);
            Utilities.startTimer();
         }

         cloud.render(g);
         if (profiling) {
            Utilities.endTimer("Cloud: ", profilingCap);
            Utilities.startTimer();
         }

         data.render(g, this.gui.isDebug());
         if (profiling) {
            Utilities.endTimer("Data: ", profilingCap);
            Utilities.startTimer();
         }

         if (!this.gui.isHideGUI()) {
            workSelection.render(g);
         }

         if (profiling) {
            Utilities.endTimer("Work Selection: ", profilingCap);
            Utilities.startTimer();
         }

         if (!this.gui.isHideGUI()) {
            mob.renderOverlay(g, this.gui.isDebug());
         }

         if (profiling) {
            Utilities.endTimer("Mob: ", profilingCap);
            Utilities.startTimer();
         }

         if (!this.gui.isHideGUI()) {
            object.renderOverlay(g, true, this.gui.isDebug());
         }

         if (profiling) {
            Utilities.endTimer("Object Overlay: ", profilingCap);
            Utilities.startTimer();
         }

         if (!this.gui.isHideGUI()) {
            resource.renderResourcesOnGroundArrows();
         }

         if (profiling) {
            Utilities.endTimer("Resource Arrows: ", profilingCap);
            Utilities.startTimer();
         }

         if (!this.gui.isHideGUI()) {
            font.render(g);
         }

         if (profiling) {
            Utilities.endTimer("Font: ", profilingCap);
         }

         if (this.gui.isDebug()) {
            waypoint.render(g);
         }

         g.resetTransform();
         g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         if (profiling) {
            Utilities.startTimer();
         }

         if (!this.gui.isHideGUI()) {
            help.render(g, this.mouse, this.gui.isDebug());
         }

         if (profiling) {
            Utilities.endTimer("Help: ", profilingCap);
         }

         this.renderGUI();
         this.screenShotListener();
         transition.render(g);
      }
   }

   private void renderGUI() throws SlickException {
      this.gui.render(this.gamePaused);
      this.mouse.setX(this.mouseX);
      this.mouse.setY(this.mouseY);
      if (!this.gui.isHideGUI()) {
         particle.renderGUI();
         Console.renderBanner(this.g);
      }
   }

   private void gameOver() {
      sound.playGoodbyeRetroSky();
      this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SELECT);
      this.gui.toggleYouLosePanel();
      this.gamePaused = true;
   }

   @Override
   public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
      super.update(gc, sbg, delta);
      if (!this.loadingRequired) {
         Console.update();
         if (save.isAutosaving()) {
            save.update();
         } else {
            boolean profiling = false;
            boolean pauseWhenDone = false;
            int profilingCap = 10;
            if (profiling) {
               Utilities.startTimer();
            }

            particle.update();
            if (profiling) {
               Utilities.endTimer("Particles: ", profilingCap);
               Utilities.startTimer();
            }

            for (int i = 0; i < this.gameSpeed || this.advancingTime(); i++) {
               if (i % 500 == 0 && this.advancingTime()) {
                  System.out
                     .println(
                        "Tick: "
                           + i
                           + " Time Advancing To: Day "
                           + this.advanceTimeTo
                           + " (Current Day: "
                           + time.getDay()
                           + " Time of Day: "
                           + time.getTimeOfDay()
                           + ")"
                     );
                  pauseWhenDone = true;
               }

               if (!this.gamePaused) {
                  if (profiling) {
                     Utilities.startTimer();
                  }

                  if (!this.advancingTime()) {
                     save.update();
                  }

                  if (profiling) {
                     Utilities.endTimer("Save: ", profilingCap);
                     Utilities.startTimer();
                  }

                  map.update();
                  if (profiling) {
                     Utilities.endTimer("Map: ", profilingCap);
                     Utilities.startTimer();
                  }

                  road.update();
                  if (profiling) {
                     Utilities.endTimer("Road: ", profilingCap);
                     Utilities.startTimer();
                  }

                  mapAI.update();
                  if (profiling) {
                     Utilities.endTimer("Map AI: ", profilingCap);
                     Utilities.startTimer();
                  }

                  waypoint.update();
                  if (profiling) {
                     Utilities.endTimer("Waypoint: ", profilingCap);
                     Utilities.startTimer();
                  }

                  resource.update();
                  if (profiling) {
                     Utilities.endTimer("Resource: ", profilingCap);
                     Utilities.startTimer();
                  }

                  lootBox.update(this.getMouseOnMapX(), this.getMouseOnMapY());
                  if (profiling) {
                     Utilities.endTimer("Loot Crate: ", profilingCap);
                     Utilities.startTimer();
                  }

                  trade.update();
                  if (profiling) {
                     Utilities.endTimer("Trade: ", profilingCap);
                     Utilities.startTimer();
                  }

                  if (!this.advancingTime()) {
                     background.update();
                  }

                  if (profiling) {
                     Utilities.endTimer("Background: ", profilingCap);
                     Utilities.startTimer();
                  }

                  time.update();
                  if (profiling) {
                     Utilities.endTimer("Time: ", profilingCap);
                     Utilities.startTimer();
                  }

                  projectile.update();
                  if (profiling) {
                     Utilities.endTimer("Projectile: ", profilingCap);
                     Utilities.startTimer();
                  }

                  missile.update();
                  if (profiling) {
                     Utilities.endTimer("Missile: ", profilingCap);
                     Utilities.startTimer();
                  }

                  mob.update(this.getMouseTileX(), this.getMouseTileY());
                  if (profiling) {
                     Utilities.endTimer("Mob:", profilingCap);
                     Utilities.startTimer();
                  }

                  mobJob.update();
                  if (profiling) {
                     Utilities.endTimer("MobJob: ", profilingCap);
                     Utilities.startTimer();
                  }

                  essence.update();
                  if (profiling) {
                     Utilities.endTimer("Essence: ", profilingCap);
                     Utilities.startTimer();
                  }

                  spawn.update();
                  if (profiling) {
                     Utilities.endTimer("Spawn: ", profilingCap);
                     Utilities.startTimer();
                  }

                  object.update(true);
                  if (profiling) {
                     Utilities.endTimer("Object: ", profilingCap);
                     Utilities.startTimer();
                  }

                  influence.update();
                  if (profiling) {
                     Utilities.endTimer("Influence: ", profilingCap);
                     Utilities.startTimer();
                  }

                  weather.update();
                  if (profiling) {
                     Utilities.endTimer("Weather: ", profilingCap);
                     Utilities.startTimer();
                  }

                  cloud.update();
                  if (profiling) {
                     Utilities.endTimer("Cloud: ", profilingCap);
                     Utilities.startTimer();
                  }

                  data.update();
                  if (profiling) {
                     Utilities.endTimer("Data: ", profilingCap);
                     Utilities.startTimer();
                  }

                  if (!this.advancingTime()) {
                     lighting.update();
                  }

                  if (profiling) {
                     Utilities.endTimer("Lighting: ", profilingCap);
                     Utilities.startTimer();
                  }

                  stats.update();
                  if (profiling) {
                     Utilities.endTimer("Stats: ", profilingCap);
                     Utilities.startTimer();
                  }

                  corruption.update();
                  if (profiling) {
                     Utilities.endTimer("Corruption: ", profilingCap);
                     Utilities.startTimer();
                  }

                  migration.update();
                  if (profiling) {
                     Utilities.endTimer("Migration: ", profilingCap);
                     Utilities.startTimer();
                  }

                  chest.update();
                  if (profiling) {
                     Utilities.endTimer("Chest: ", profilingCap);
                     Utilities.startTimer();
                  }

                  god.update();
                  if (profiling) {
                     Utilities.endTimer("God: ", profilingCap);
                     Utilities.startTimer();
                  }

                  workSelection.update();
                  if (profiling) {
                     Utilities.endTimer("Work Selection: ", profilingCap);
                  }

                  goal.update();
                  if (profiling) {
                     Utilities.endTimer("Goal: ", profilingCap);
                  }
               }
            }

            if (pauseWhenDone) {
               System.out.println("Advance complete.");
               pauseWhenDone = false;
               this.advanceTimeRunning = false;
               this.gamePaused = true;
               this.advanceTimeTo = time.getDay() + 1;
            }

            if (profiling) {
               Utilities.startTimer();
            }

            sound.update();
            if (profiling) {
               Utilities.endTimer("Sound: ", profilingCap);
               Utilities.startTimer();
            }

            font.update();
            if (profiling) {
               Utilities.endTimer("Font: ", profilingCap);
               Utilities.startTimer();
            }

            map.updateNoSpeed();
            if (profiling) {
               Utilities.endTimer("Map No Speed: ", profilingCap);
               Utilities.startTimer();
            }

            help.update();
            if (profiling) {
               Utilities.endTimer("Help: ", profilingCap);
            }

            transition.update();
            this.gui.update();
            if (this.gui.getTipsPanel().getActiveTip() != null) {
               this.gamePaused = true;
               this.cancelTasks();
            }

            if (map.isMapLost() && !this.gui.isYouLosePanelEnabled()) {
               this.gameOver();
            }
         }
      }
   }

   private boolean advancingTime() {
      return this.advanceTimeRunning && this.advanceTimeTo > time.getDay();
   }

   public void increaseAdvanceTime() {
      this.advanceTimeTo++;
   }

   public void decreaseAdvanceTime() {
      if (this.advanceTimeTo > time.getDay() + 1) {
         this.advanceTimeTo--;
      }
   }

   public int getAdvanceTime() {
      return this.advanceTimeTo;
   }

   public boolean startAdvanceTime() {
      return this.advanceTimeRunning = true;
   }

   @Override
   protected void controlsKeyboard() throws SlickException {
      if (transition.getControlLockout() || this.gui.getSettingsPanel().isSettingsTwitchChannelFocused()) {
         input.clearKeyPressedRecord();
      } else if (this.gui.getSettingsPanel().isKeyChangeMode()) {
         this.controlsKeyboardSettingsKeyChange();
      } else {
         this.controlsKeyboardScreenshot();
         this.controlsKeyboardPause();
         if (!this.gamePaused) {
            this.controlsKeyboardFullScreen();
            if (!map.isMapLost()) {
               this.controlsKeyboardMap();
               this.controlsKeyboardSpellBar();
               this.controlsKeyboardSystemAndData();
               this.controlsKeyboardHarvestAndTerrainButtons();
               this.controlsKeyboardSelectedObjectPanel();
               this.controlsKeyboardToolBar();
               this.controlsKeyboardOther();
            }
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

   private void controlsKeyboardPause() {
      if (input.isKeyPressed(SettingsParser.getKeyPause())) {
         this.pauseGame();
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

      if (input.isKeyPressed(SettingsParser.getKeySpeedUp())) {
         this.increaseGameSpeed();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeySpeedDown())) {
         this.decreaseGameSpeed();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyZoomIn())) {
         ScaleControl.increaseWorldScale();
      }

      if (input.isKeyPressed(SettingsParser.getKeyZoomOut())) {
         ScaleControl.decreaseWorldScale();
      }
   }

   private void controlsKeyboardSpellBar() throws SlickException {
      if (input.isKeyPressed(SettingsParser.getKeyGrabSpell())) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.INFLUENCE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_SPELL_BAR, "spellGrab");
         influence.setSelectedSpell(SpellBase.SpellType.GRAB);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyActiveSpell1())) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.INFLUENCE);
         this.gui.deselectAll();
         influence.setSelectedSpell(influence.getActiveSpell(0));
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyActiveSpell2())) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.INFLUENCE);
         this.gui.deselectAll();
         influence.setSelectedSpell(influence.getActiveSpell(1));
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyActiveSpell3())) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.INFLUENCE);
         this.gui.deselectAll();
         influence.setSelectedSpell(influence.getActiveSpell(2));
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyActiveSpell4())) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.INFLUENCE);
         this.gui.deselectAll();
         influence.setSelectedSpell(influence.getActiveSpell(3));
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyActiveSpell5())) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.INFLUENCE);
         this.gui.deselectAll();
         influence.setSelectedSpell(influence.getActiveSpell(4));
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
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

      if (input.isKeyPressed(SettingsParser.getKeyProblemPanel())) {
         this.gui.toggleProblemPanelPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyBuildingList())) {
         this.gui.toggleBuildingListPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyPopulationList())) {
         this.gui.togglePopulationListPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsKeyboardHarvestAndTerrainButtons() throws SlickException {
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

      if (input.isKeyPressed(SettingsParser.getKeyHarvestWood()) && object.objectCount(MapTilesLoader.TileSet.CASTLE_1) > 0) {
         this.gui.getGUIData().setInterfaceModeResource(GUIEnums.InterfaceMode.ASSIGN_HARVEST_WORK, ResourceModule.ResourceType.WOOD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "harvestWood");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyHarvestRock()) && object.objectCount(MapTilesLoader.TileSet.CASTLE_1) > 0) {
         this.gui.getGUIData().setInterfaceModeResource(GUIEnums.InterfaceMode.ASSIGN_HARVEST_WORK, ResourceModule.ResourceType.ROCK);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "harvestRock");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyHarvestFoodAndWater())
         && object.objectCount(MapTilesLoader.TileSet.FARM) + object.objectCount(MapTilesLoader.TileSet.WATER_PURIFIER) > 0) {
         this.gui.getGUIData().setInterfaceModeResource(GUIEnums.InterfaceMode.ASSIGN_HARVEST_WORK, ResourceModule.ResourceType.RAW_VEGETABLE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "harvestFoodAndWater");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyHarvestCrystals()) && object.objectCount(MapTilesLoader.TileSet.CASTLE_1) > 0) {
         this.gui.getGUIData().setInterfaceModeResource(GUIEnums.InterfaceMode.ASSIGN_HARVEST_WORK, ResourceModule.ResourceType.CRYSTAL);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "harvestCrystals");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gameMode == GameModeTemplateBase.GameMode.SANDBOX) {
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
            this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseAll");
         }

         if (input.isKeyPressed(SettingsParser.getKeyPatchTool())) {
            this.gui.getHarvestAndTerrainBar().setHolePatchPatch();
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.PATCH);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "patch");
         }

         if (input.isKeyPressed(SettingsParser.getKeyHoleTool())) {
            this.gui.getHarvestAndTerrainBar().setHolePatchHole();
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.HOLE);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "hole");
         }
      }
   }

   private void controlsKeyboardSelectedObjectPanel() throws SlickException {
      ObjectBase selectedBuilding = this.gui.getGUIData().getSelectedObject();
      if (selectedBuilding != null && selectedBuilding.getObjectFlags().getNextUpgradeType() != null && selectedBuilding.isInRange()) {
         if (input.isKeyPressed(SettingsParser.getKeyUpgrade())) {
            if (!this.gui.getSelectedObjectPanel().isAttemptUpgrade()
               && (selectedBuilding.getObjectFlags().getNextUpgradeType2() != null || selectedBuilding.getObjectFlags().getNextUpgradeType3() != null)) {
               this.gui.getSelectedObjectPanel().setObjectPanelAttemptUpgrade(true);
               sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            } else {
               this.gui.getGUIData().getSelectedObject().toggleUpgrade(this.gui.getGUIData().getSelectedObject().getObjectFlags().getNextUpgradeType());
               this.gui.getSelectedObjectPanel().setObjectPanelAttemptUpgrade(false);
               sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            }
         }

         if (input.isKeyPressed(SettingsParser.getKeyStartUpgrade2()) && selectedBuilding.getObjectFlags().getNextUpgradeType2() != null) {
            this.gui.getGUIData().getSelectedObject().toggleUpgrade(this.gui.getGUIData().getSelectedObject().getObjectFlags().getNextUpgradeType2());
            this.gui.getSelectedObjectPanel().setObjectPanelAttemptUpgrade(false);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (input.isKeyPressed(SettingsParser.getKeyStartUpgrade3()) && selectedBuilding.getObjectFlags().getNextUpgradeType3() != null) {
            this.gui.getGUIData().getSelectedObject().toggleUpgrade(this.gui.getGUIData().getSelectedObject().getObjectFlags().getNextUpgradeType3());
            this.gui.getSelectedObjectPanel().setObjectPanelAttemptUpgrade(false);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }
   }

   private void controlsKeyboardToolBar() throws SlickException {
      if (input.isKeyPressed(SettingsParser.getKeyPauseObjects())) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.PAUSE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_TOOLS_BAR, "pauseObjects");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyDismantleObjects())) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.DISMANTLE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_TOOLS_BAR, "dismantleObjects");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (input.isKeyPressed(SettingsParser.getKeyDestroyTerrain()) && object.objectCount(MapTilesLoader.TileSet.CASTLE_1) > 0) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ASSIGN_CLEAR_WORK);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_TOOLS_BAR, "destroyTerrain");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
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
         if (!this.gamePaused && (influence.getMobGrabbed() != null || influence.getResourceGrabbed() != null)) {
            int distance = 4;
            int tileX = this.getMouseTileX();
            int tileY = this.getMouseTileY();
            mob.tryConfuseInArea(tileX, tileY, distance, true, 1);
            mob.tryPanicInArea(tileX, tileY, distance, true, 2);
            if (influence.getSelectedSpell() != SpellBase.SpellType.DROP || this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.INFLUENCE) {
               influence.forceCastSpell(0, 0, SpellBase.SpellType.DROP);
            }
         }

         if (map.isMapLost()) {
            if (input.isMousePressed(0)) {
               this.controlsMouseYouLosePanel();
            }
         } else {
            if (!this.gamePaused && SettingsParser.getEdgeScrollingEnabled() && !touchMode) {
               this.controlsEdgeScrolling();
            }

            if (input.isMousePressed(1) && (!this.gamePaused || map.isMapLost())) {
               this.controlsMousePressedLeftPanel(true);
               this.controlsMousePressedRightPanel(true);
               this.controlsMousePressedHarvestAndTerrainButtons(true);
               this.controlsMousePressedSelectedObjectPanel(true);
               this.buttonCycles();
               if (this.gui.intersectsAnyGUIMask()) {
                  return;
               }

               this.mouseXPressed = this.mouseX;
               this.mouseYPressed = this.mouseY;
            }

            if (input.isMouseButtonDown(1)) {
               if (this.gamePaused && !map.isMapLost()) {
                  this.movingMap = false;
               } else {
                  if (this.gui.intersectsAnyGUIMask()) {
                     return;
                  }

                  this.movingMap = true;
                  map.moveMapMouse(this.mouseXPressed, this.mouseYPressed, this.mouseX, this.mouseY);
               }
            } else {
               this.movingMap = false;
            }

            if (input.isMousePressed(0)) {
               if (this.gui.isSettingsEnabled()) {
                  this.controlsMousePressedSettingsPanel();
                  return;
               }

               if (!this.gamePaused || map.isMapLost()) {
                  this.controlsMousePressedResourceAndInfluenceBar();
                  this.controlsMousePressedSpellBar();
                  this.controlsMousePressedSpellSelectBar();
                  this.controlsMousePressedSystemAndData();
                  this.controlsMousePressedCastlePanel();
                  this.controlsMousePressedMinimapPanel();
                  this.controlsMousePressedDataViewsPanel();
                  this.controlsMousePressedBuildingListPanel();
                  this.controlsMousePressedResourceListPanel();
                  this.controlsMousePressedPopulationListPanel();
                  this.controlsMousePressedMusicPanel();
                  this.controlsMousePressedLeftPanel(false);
                  this.controlsMousePressedRightPanel(false);
                  this.controlsMousePressedHarvestAndTerrainButtons(false);
                  this.controlsMousePressedConsolePanel();
                  this.controlsMousePressedGoalPopup();
                  this.controlsMousePressedSelectedObjectPanel(false);
                  this.controlsMousePressedToolBar();
                  this.controlsMousePressedGoalsPanel();
                  this.buttonCycles();
               }

               this.controlsMousePressedWeatherPanel();
               this.controlsMousePressedTipsPanel();
               this.controlsMousePressedMainMenuPanel();
               this.controlsMousePressedSettingsKeyChange();
               this.controlsMousePressedStatisticsPanel();
               if (!this.gamePaused || map.isMapLost()) {
                  if (this.gui.intersectsAnyGUIMask()) {
                     return;
                  }

                  this.controlsMousePressedInterfaceMode();
               }
            }

            if (input.isMouseButtonDown(0) && (!this.gamePaused || map.isMapLost())) {
               this.controlsMouseDownMinimapPanel();
               if (this.gui.intersectsAnyGUIMask()) {
                  return;
               }

               this.controlsMouseDownInterfaceMode();
            }

            if (input.isMousePressed(2) && (!this.gamePaused || map.isMapLost())) {
               this.cancelTasks();
               sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            }

            if (this.triggerCloseSpellSelectOnRelease && input.isMouseButtonReleased(0)) {
               this.gui.deselectAll();
               this.triggerCloseSpellSelectOnRelease = false;
               this.gui.toggleSpellSelectPanel();
            }
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

   private void controlsMouseYouLosePanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_YOU_LOSE_PANEL, "exit")) {
         if (save.getActiveRegionalSave().getGameType() == StateBase.GameType.SKIRMISH) {
            save.getActiveRegionalSave().deleteSaveData();
         }

         this.gui.setDebug(false);
         if (save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP) {
            save.saveFullGame();
            transition.startTransition(Game.getWorldMapState());
         } else {
            transition.startTransition(Game.getMainMenuState());
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedResourceAndInfluenceBar() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_AND_INFLUENCE_BAR, "resourceFilterToggle")) {
         this.gui.getResourceAndInfluenceBar().toggleResourceFilter();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedWeatherPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_WEATHER_PANEL, "speedDecrease")) {
         if (this.gameSpeed == 1 && !this.gamePaused) {
            this.pauseGame();
         } else {
            this.decreaseGameSpeed();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_WEATHER_PANEL, "speedIncrease")) {
         if (this.gamePaused) {
            this.pauseGame();
         } else {
            this.increaseGameSpeed();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedSpellBar() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SPELL_BAR, "spellGrab")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.INFLUENCE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_SPELL_BAR, "spellGrab");
         influence.setSelectedSpell(SpellBase.SpellType.GRAB);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      for (int i = 0; i < 5; i++) {
         if (this.gui.getSpellBar().intersectsActiveSpell(i)) {
            SpellBase.SpellType t = this.gui.getSpellBar().getIntersectsActiveSpell(i);
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.INFLUENCE);
            this.gui.deselectAll();
            influence.setSelectedSpell(t);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SPELL_BAR, "changeSpell" + i)) {
            this.gui.toggleSpellSelectPanel();
            this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_SPELL_BAR, "changeSpell" + i);
            this.gui.getSpellSelectPanel().setSpellSelectSlot(i);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }
   }

   private void controlsMousePressedSpellSelectBar() throws SlickException {
      if (this.gui.getSpellSelectPanel().intersectsSpellSelectSpell()) {
         SpellBase.SpellType t = this.gui.getSpellSelectPanel().setSpellSlot();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.INFLUENCE);
         this.triggerCloseSpellSelectOnRelease = true;
         influence.setSelectedSpell(t);
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
         this.gamePaused = false;
         this.gui.getTipsPanel().clearActiveTip();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedSystemAndData() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SYSTEM_AND_DATA_BAR, "mainMenu")) {
         this.gui.toggleMainMenuPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SYSTEM_AND_DATA_BAR, "minimap")) {
         this.gui.toggleMinimapPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SYSTEM_AND_DATA_BAR, "dataViews")) {
         this.gui.toggleDataViewsPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SYSTEM_AND_DATA_BAR, "statistics")) {
         this.gui.toggleStatisticsPanel();
         this.gui.getStatisticsPanel().resetGraph();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SYSTEM_AND_DATA_BAR, "problemPanel")) {
         this.gui.toggleProblemPanelPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SYSTEM_AND_DATA_BAR, "buildingList")) {
         this.gui.toggleBuildingListPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SYSTEM_AND_DATA_BAR, "resourceList")) {
         this.gui.toggleResourceListPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SYSTEM_AND_DATA_BAR, "populationList")) {
         this.gui.togglePopulationListPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SYSTEM_AND_DATA_BAR, "villageEfficiency")) {
         this.gui.toggleVillageEfficiencyPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SYSTEM_AND_DATA_BAR, "music")) {
         this.gui.toggleMusicPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SYSTEM_AND_DATA_BAR, "goals")) {
         this.gui.toggleGoalsPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedCastlePanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_CASTLE_PANEL, "placeCastle")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CASTLE_1, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_CASTLE_PANEL, "placeCastle");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_CASTLE_PANEL, "hidePanel")) {
         this.gui.hideCastlePanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedMainMenuPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_MAIN_MENU_PANEL, "settings")) {
         this.gui.getSettingsPanel().settingsReloadProperties();
         this.gui.toggleSettings();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_MAIN_MENU_PANEL, "goToWorldMap")
         || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_MAIN_MENU_PANEL, "returnToMenu")) {
         this.gui.getMainMenuPanel().attemptLeaveRegion();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_MAIN_MENU_PANEL, "exitGame")) {
         this.gui.getMainMenuPanel().attemptExit();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_MAIN_MENU_PANEL, "areYouSure")) {
         save.saveFullGame();
         this.gui.setDebug(false);
         if (this.gui.getMainMenuPanel().getAttemptLeaveRegion()) {
            if (save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP) {
               transition.startTransition(Game.getWorldMapState());
            } else {
               transition.startTransition(Game.getMainMenuState());
            }
         } else {
            transition.startTransition(null);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

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
         if (this.gui.getSettingsPanel().isSettingsTwitchChannelFocused()) {
            this.gui.getSettingsPanel().resetSettingsTwitchChannel();
         }

         Game.reinitDisplay();
         this.updateRenderWindow();
         sound.resetVolume();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_SETTINGS_PANEL, "cancel")) {
         this.gui.toggleMainMenuPanel();
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

   private void controlsMousePressedStatisticsPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "back")) {
         this.gui.getStatisticsPanel().setChartPage(this.gui.getStatisticsPanel().getBackPage());
         this.gui.getStatisticsPanel().setChart(this.gui.getStatisticsPanel().getBackPageDefaultChartPage());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "categoryVillagers")) {
         this.gui.getStatisticsPanel().setChartPage(GUIEnums.GUIPanelPage.STATS_VILLAGERS);
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_TOTAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "categoryAnimals")) {
         this.gui.getStatisticsPanel().setChartPage(GUIEnums.GUIPanelPage.STATS_ANIMALS);
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.ANIMALS_TOTAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "categoryMonsters")) {
         this.gui.getStatisticsPanel().setChartPage(GUIEnums.GUIPanelPage.STATS_MONSTERS);
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.CORRUPTION_TOTAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "categoryBuildings")) {
         this.gui.getStatisticsPanel().setChartPage(GUIEnums.GUIPanelPage.STATS_BUILDINGS);
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.BUILDINGS_TOTAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "categoryDefenses")) {
         this.gui.getStatisticsPanel().setChartPage(GUIEnums.GUIPanelPage.STATS_DEFENSES);
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.DEFENSES_TOWERS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "categoryResources")) {
         this.gui.getStatisticsPanel().setChartPage(GUIEnums.GUIPanelPage.STATS_RESOURCES);
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.RESOURCES_TOTAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "categoryMagicAndEnergy")) {
         this.gui.getStatisticsPanel().setChartPage(GUIEnums.GUIPanelPage.STATS_MAGIC_AND_ENERGY);
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.RESOURCES_TOTAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "categoryOther")) {
         this.gui.getStatisticsPanel().setChartPage(GUIEnums.GUIPanelPage.STATS_OTHER);
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.RESOURCES_TOTAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersTotal")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_TOTAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersGender")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_GENDER);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersCategoryImmigration")) {
         this.gui.getStatisticsPanel().setChartPage(GUIEnums.GUIPanelPage.STATS_VILLAGERS_CATEGORY_IMMIGRATION);
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_IMMIGRATION_NOMADS_JOINED);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersImmigrationCatjeetHired")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_IMMIGRATION_CATJEET_HIRED);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersImmigrationNomadsJoined")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_IMMIGRATION_NOMADS_JOINED);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersImmigrationNomadsVisited")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_IMMIGRATION_NOMADS_VISITED);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersCategoryReproduction")) {
         this.gui.getStatisticsPanel().setChartPage(GUIEnums.GUIPanelPage.STATS_VILLAGERS_CATEGORY_REPRODUCTION);
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_PREGNANCIES);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersReproductionBirths")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_BIRTHS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersReproductionGrownUp")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_GROWN_UP);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersReproductionCoitus")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_COITUS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersReproductionPregnancies")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_PREGNANCIES);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersReproductionNewPregnancies")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_REPRODUCTION_NEW_PREGNANCIES);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersDeaths")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_DEATHS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersCategoryNeeds")) {
         this.gui.getStatisticsPanel().setChartPage(GUIEnums.GUIPanelPage.STATS_VILLAGERS_CATEGORY_NEEDS);
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_NEEDS_AVERAGE_HUNGER);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersNeedsAverageHunger")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_NEEDS_AVERAGE_HUNGER);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersNeedsAverageThirst")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_NEEDS_AVERAGE_THIRST);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "villagersNeedsAverageEnergy")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.VILLAGERS_NEEDS_AVERAGE_ENERGY);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "animalsTotal")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.ANIMALS_TOTAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "animalsGender")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.ANIMALS_GENDER);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "animalsDomesticated")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.ANIMALS_DOMESTICATED);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "animalsDeaths")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.ANIMALS_DEATHS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "animalsCategoryNeeds")) {
         this.gui.getStatisticsPanel().setChartPage(GUIEnums.GUIPanelPage.STATS_ANIMALS_CATEGORY_NEEDS);
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.ANIMALS_NEEDS_AVERAGE_HUNGER);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "animalsNeedsAverageHunger")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.ANIMALS_NEEDS_AVERAGE_HUNGER);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "animalsNeedsAverageThirst")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.ANIMALS_NEEDS_AVERAGE_THIRST);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "animalsNeedsAverageEnergy")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.ANIMALS_NEEDS_AVERAGE_ENERGY);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "corruptionTotal")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.CORRUPTION_TOTAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "corruptionSpreadAmount")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.CORRUPTION_SPREAD_AMOUNT);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "corruptedBuildings")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.CORRUPTED_BUILDINGS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "dronesTotal")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.DRONES_TOTAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "monstersKilled")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.MONSTERS_KILLED);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "monstersSpawned")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.MONSTERS_SPAWNED);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "buildingsTotal")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.BUILDINGS_TOTAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "buildingsBuilt")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.BUILDINGS_BUILT);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "buildingsUpgraded")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.BUILDINGS_UPGRADED);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "buildingsDestroyed")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.BUILDINGS_DESTROYED);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "buildingsDismantled")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.BUILDINGS_DISMANTLED);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "defensesTowers")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.DEFENSES_TOWERS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "defensesCombobulators")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.DEFENSES_COMBOBULATORS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "defensesGolems")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.DEFENSES_GOLEMS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "defensesRangers")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.DEFENSES_RANGERS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "defensesWalls")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.DEFENSES_WALLS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "resourcesTotal")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.RESOURCES_TOTAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "resourcesProductionRate")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.RESOURCES_PRODUCTION_RATE);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "resourcesFoodAndWaterRate")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.RESOURCES_FOOD_AND_WATER_RATE);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "magicAndEnergyEssenceGenerated")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.MAGIC_AND_ENERGY_ESSENCE_GENERATED);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "magicAndEnergyEssenceDecayed")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.MAGIC_AND_ENERGY_ESSENCE_DECAYED);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "magicAndEnergyEnergy")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.MAGIC_AND_ENERGY_ENERGY);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "magicAndEnergyInfluenceSpent")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.MAGIC_AND_ENERGY_INFLUENCE_SPENT);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "magicAndEnergyInfluenceGenerated")) {
         this.gui.getStatisticsPanel().setChart(StatisticsPanel.StatisticsChartType.MAGIC_AND_ENERGY_INFLUENCE_GENERATED);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "mobListAll")) {
         this.gui.getStatisticsPanel().setMobType(null);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "mobListPreviousPage")) {
         this.gui.getStatisticsPanel().setCurrentMobPageLast();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "mobListNextPage")) {
         this.gui.getStatisticsPanel().setCurrentMobPageNext();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      MobBase.MobType[] var4;
      for (MobBase.MobType t : MobBase.MobType.values()) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "mobList" + t.getName())) {
            this.gui.getStatisticsPanel().setMobType(t);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "resourceListAll")) {
         this.gui.getStatisticsPanel().setResourceType(null);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "resourceListPreviousPage")) {
         this.gui.getStatisticsPanel().setCurrentResourcePageLast();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "resourceListNextPage")) {
         this.gui.getStatisticsPanel().setCurrentResourcePageNext();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "resourceCategoryLeft")) {
         this.gui.getStatisticsPanel().setResourceCategoryLeft();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "resourceCategoryRight")) {
         this.gui.getStatisticsPanel().setResourceCategoryRight();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "resourceList" + tx.getTemplate().getName())) {
            this.gui.getStatisticsPanel().setResourceType(tx);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "dayBack")) {
         this.gui.getStatisticsPanel().dayBackward(1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "dayForward")) {
         this.gui.getStatisticsPanel().dayForward(1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "dayFirst")) {
         this.gui.getStatisticsPanel().setDay(0);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_STATISTICS_PANEL, "dayLast")) {
         this.gui.getStatisticsPanel().resetGraph();
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

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "essenceBuildupMap")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.ESSENCE_BUILDUP_MAP) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.ESSENCE_BUILDUP_MAP);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "corruptionResistance")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.CORRUPTION_RESISTANCE_MAP) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.CORRUPTION_RESISTANCE_MAP);
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

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "villageThreatMap")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.VILLAGE_THREAT_MAP) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.VILLAGE_THREAT_MAP);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "threatenedTileMap")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.THREATENED_TILE_MAP) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.THREATENED_TILE_MAP);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_DATA_VIEWS_PANEL, "corruptedTiles")) {
         if (data.getDisplayMode() == DataModule.DataDisplayMode.CORRUPTED_TILES) {
            data.setDisplayMode(DataModule.DataDisplayMode.NONE);
         } else {
            data.setDisplayMode(DataModule.DataDisplayMode.CORRUPTED_TILES);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedBuildingListPanel() throws SlickException {
      if (this.gui.getBuildingListPanel().intersectsBuildingsList()) {
         ObjectBase selectedBuilding = this.gui.getBuildingListPanel().getSelectedBuildingsList();
         this.gui.setSelectedObject(selectedBuilding);
         map.setMapPosition(
            (selectedBuilding.getObjectX() + selectedBuilding.getObjectFlags().getWidth() / 2) * 16 * -1 + ScaleControl.getWorldWidth() / 2,
            (selectedBuilding.getObjectY() + selectedBuilding.getObjectFlags().getHeight() / 2) * 16 * -1 + ScaleControl.getWorldHeight() / 2
         );
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getBuildingListPanel().intersectsBuildingsPriorityUp()) {
         ObjectBase selectedBuilding = this.gui.getBuildingListPanel().getSelectedBuildingsPriorityUp();
         object.increasePriority(selectedBuilding);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getBuildingListPanel().intersectsBuildingsPriorityDown()) {
         ObjectBase selectedBuilding = this.gui.getBuildingListPanel().getSelectedBuildingsPriorityDown();
         object.decreasePriority(selectedBuilding);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getBuildingListPanel().intersectsBuildingsPriorityTop()) {
         ObjectBase selectedBuilding = this.gui.getBuildingListPanel().getSelectedBuildingsPriorityTop();
         object.setPriorityTop(selectedBuilding);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getBuildingListPanel().intersectsBuildingsPriorityBottom()) {
         ObjectBase selectedBuilding = this.gui.getBuildingListPanel().getSelectedBuildingsPriorityBottom();
         object.setPriorityBottom(selectedBuilding);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.getBuildingListPanel().intersectsPrioritySortOrder()) {
         ObjectBase selectedBuilding = this.gui.getBuildingListPanel().getPrioritySortOrder();
         object.toggleGroupSortOrder(selectedBuilding);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_BUILDING_LIST_PANEL, "listTop")) {
         this.gui.getBuildingListPanel().setPageTop();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_BUILDING_LIST_PANEL, "listBottom")) {
         this.gui.getBuildingListPanel().setPageBottom();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_BUILDING_LIST_PANEL, "listUp")) {
         this.gui.getBuildingListPanel().setPageUp();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_BUILDING_LIST_PANEL, "listDown")) {
         this.gui.getBuildingListPanel().setPageDown();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedResourceListPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, "resourceFilterToggle")) {
         this.gui.getResourceListPanel().toggleResourceFilter();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, t.toString() + "Show")) {
            resource.setOnResourceBarMain(t, true);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, t.toString() + "Hide")) {
            resource.setOnResourceBarMain(t, false);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, "totalGoldShowButtons")) {
         resource.setOnResourceBarRight(ResourceModule.ResourceBarType.GOLD, true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, "totalGoldHideButtons")) {
         resource.setOnResourceBarRight(ResourceModule.ResourceBarType.GOLD, false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, "totalWaterShowButtons")) {
         resource.setOnResourceBarRight(ResourceModule.ResourceBarType.WATER, true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, "totalWaterHideButtons")) {
         resource.setOnResourceBarRight(ResourceModule.ResourceBarType.WATER, false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, "totalDirtyWaterShowButtons")) {
         resource.setOnResourceBarRight(ResourceModule.ResourceBarType.DIRTY_WATER, true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, "totalDirtyWaterHideButtons")) {
         resource.setOnResourceBarRight(ResourceModule.ResourceBarType.DIRTY_WATER, false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, "totalFoodShowButtons")) {
         resource.setOnResourceBarRight(ResourceModule.ResourceBarType.FOOD, true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, "totalFoodHideButtons")) {
         resource.setOnResourceBarRight(ResourceModule.ResourceBarType.FOOD, false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, "totalEnergyShowButtons")) {
         resource.setOnResourceBarRight(ResourceModule.ResourceBarType.ENERGY, true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, "totalEnergyHideButtons")) {
         resource.setOnResourceBarRight(ResourceModule.ResourceBarType.ENERGY, false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, "totalFaithShowButtons")) {
         resource.setOnResourceBarRight(ResourceModule.ResourceBarType.FAITH, true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RESOURCE_LIST_PANEL, "totalFaithHideButtons")) {
         resource.setOnResourceBarRight(ResourceModule.ResourceBarType.FAITH, false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedPopulationListPanel() throws SlickException {
      if (this.gui.getPopulationListPanel().intersectsPopulationList()) {
         MobBase selectedMob = this.gui.getPopulationListPanel().getSelectedPopulationList();
         this.gui.setSelectedMob(selectedMob);
         map.setMapPosition(selectedMob.getMobX() * -1.0F + ScaleControl.getWorldWidth() / 2, selectedMob.getMobY() * -1.0F + ScaleControl.getWorldHeight() / 2);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_POPULATION_LIST_PANEL, "filterLeft")) {
         this.gui.getPopulationListPanel().setFilterDown();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_POPULATION_LIST_PANEL, "filterRight")) {
         this.gui.getPopulationListPanel().setFilterUp();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_POPULATION_LIST_PANEL, "listTop")) {
         this.gui.getPopulationListPanel().listPageTop();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_POPULATION_LIST_PANEL, "listBottom")) {
         this.gui.getPopulationListPanel().listPageBottom();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_POPULATION_LIST_PANEL, "listUp")) {
         this.gui.getPopulationListPanel().listPageUp();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_POPULATION_LIST_PANEL, "listDown")) {
         this.gui.getPopulationListPanel().listPageDown();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedMusicPanel() throws SlickException {
      if (this.gui.getMusicPanel().intersectsMusic()) {
         sound.playMusic(this.gui.getMusicPanel().getMusicTitle());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedLeftPanel(boolean rightClick) throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceTab")) {
         this.gui.getLeftPanel().setPanelPage(GUIEnums.GUIPanelPage.LEFT_WORKFORCE);
         if (this.gui.getLeftPanel().isPanelHidden()) {
            this.gui.getLeftPanel().togglePanelHidden();
         }

         if (this.gui.getGUIData().getSelectedObject() != null || this.gui.getGUIData().getSelectedMob() != null) {
            this.gui.deselectAllSelected();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "resourceManagementTab")) {
         this.gui.getLeftPanel().setPanelPage(GUIEnums.GUIPanelPage.LEFT_RESOURCE_MANAGEMENT);
         if (this.gui.getLeftPanel().isPanelHidden()) {
            this.gui.getLeftPanel().togglePanelHidden();
         }

         if (this.gui.getGUIData().getSelectedObject() != null || this.gui.getGUIData().getSelectedMob() != null) {
            this.gui.deselectAllSelected();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "tradeTab")) {
         this.gui.getLeftPanel().setPanelPage(GUIEnums.GUIPanelPage.LEFT_TRADE);
         if (this.gui.getLeftPanel().isPanelHidden()) {
            this.gui.getLeftPanel().togglePanelHidden();
         }

         if (this.gui.getGUIData().getSelectedObject() != null || this.gui.getGUIData().getSelectedMob() != null) {
            this.gui.deselectAllSelected();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "migrationTab")) {
         this.gui.getLeftPanel().setPanelPage(GUIEnums.GUIPanelPage.LEFT_MIGRATION);
         if (this.gui.getLeftPanel().isPanelHidden()) {
            this.gui.getLeftPanel().togglePanelHidden();
         }

         if (this.gui.getGUIData().getSelectedObject() != null || this.gui.getGUIData().getSelectedMob() != null) {
            this.gui.deselectAllSelected();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "courierTab")) {
         this.gui.getLeftPanel().setPanelPage(GUIEnums.GUIPanelPage.LEFT_COURIER);
         if (this.gui.getLeftPanel().isPanelHidden()) {
            this.gui.getLeftPanel().togglePanelHidden();
         }

         if (this.gui.getGUIData().getSelectedObject() != null || this.gui.getGUIData().getSelectedMob() != null) {
            this.gui.deselectAllSelected();
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "hidePanel")) {
         this.gui.getLeftPanel().togglePanelHidden();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      ResourceModule.ResourceType[] var5;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "tradeBuy" + t.getTemplate().getName())) {
            if (trade.isModeBuy(t)) {
               trade.resetTradeMode(t);
            } else {
               trade.setTradeModeBuy(t);
            }

            trade.resetTradeAmount(t);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "tradeSell" + t.getTemplate().getName())) {
            if (trade.isModeSell(t)) {
               trade.resetTradeMode(t);
            } else {
               trade.setTradeModeSell(t);
            }

            trade.resetTradeAmount(t);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "tradeResourceBuyDown" + t.getTemplate().getName())
            || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "tradeResourceSellDown" + t.getTemplate().getName())) {
            trade.decreaseTradeAmount(t, rightClick ? 10 : 1);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "tradeResourceBuyUp" + t.getTemplate().getName())
            || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "tradeResourceSellUp" + t.getTemplate().getName())) {
            trade.increaseTradeAmount(t, rightClick ? 10 : 1);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "tradeLaborHire")) {
         if (trade.isLaborModeBuy()) {
            trade.resetLaborTradeMode();
         } else {
            trade.setLaborTradeModeBuy();
         }

         trade.resetLaborTradeAmount();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "tradeLaborDown")) {
         trade.decreaseLaborTradeAmount(rightClick ? 10 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "tradeLaborUp")) {
         trade.increaseLaborTradeAmount(rightClick ? 10 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftTradePageLeftTop")
         || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftTradePageLeftBottom")) {
         this.gui.getLeftPanel().setTradeCategoryLeft();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftTradePageRightTop")
         || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftTradePageRightBottom")) {
         this.gui.getLeftPanel().setTradeCategoryRight();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceDesiredIncrease" + t)) {
            mobJob.increaseDesiredWorkers(t, rightClick ? 5 : 1);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceDesiredDecrease" + t)) {
            mobJob.decreaseDesiredWorkers(t, rightClick ? 5 : 1);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceJobColorBase" + t)) {
            this.gui.deselectAllSelected();
            this.gui.getLeftPanel().setWorkforceColorSelectPanel(t, this.gui.getLeftPanel().getPanelPage(), false);
            this.gui.getLeftPanel().setPanelPage(GUIEnums.GUIPanelPage.LEFT_WORKFORCE_COLOR_SELECT);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceJobColorAccent" + t)) {
            this.gui.deselectAllSelected();
            this.gui.getLeftPanel().setWorkforceColorSelectPanel(t, this.gui.getLeftPanel().getPanelPage(), false);
            this.gui.getLeftPanel().setPanelPage(GUIEnums.GUIPanelPage.LEFT_WORKFORCE_COLOR_SELECT);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftWorkforcePageUpTop")
         || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftWorkforcePageUpBottom")) {
         this.gui.getLeftPanel().setWorkforcePageDown();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftWorkforcePageDownTop")
         || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftWorkforcePageDownBottom")) {
         this.gui.getLeftPanel().setWorkforcePageUp();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      MobJobBase.MobJobType mobJobType = this.gui.getLeftPanel().getWorkforceColorSelectType();

      MobJobBase.MobJobType[] var6;
      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectPresets" + t)) {
            if (!this.gui.getLeftPanel().isWorkforceColorSelectAccentMode()) {
               mobJob.setCustomBaseColor(mobJobType, t.getJob().getBaseColor());
            } else {
               mobJob.setCustomAccentColor(mobJobType, t.getJob().getBaseColor());
            }

            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectPresetsAccent" + t)) {
            if (!this.gui.getLeftPanel().isWorkforceColorSelectAccentMode()) {
               mobJob.setCustomBaseColor(mobJobType, t.getJob().getAccentColor());
            } else {
               mobJob.setCustomAccentColor(mobJobType, t.getJob().getAccentColor());
            }

            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectDefaultBase")) {
         mobJob.resetCustomBaseColor(mobJobType);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectDefaultAccent")) {
         mobJob.resetCustomAccentColor(mobJobType);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectCurrentBase")) {
         this.gui.getLeftPanel().setWorkforceColorSelectPanel(mobJobType, this.gui.getLeftPanel().getPanelPage(), false);
         this.gui.getLeftPanel().setPanelPage(GUIEnums.GUIPanelPage.LEFT_WORKFORCE_COLOR_SELECT);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectCurrentAccent")) {
         this.gui.getLeftPanel().setWorkforceColorSelectPanel(mobJobType, this.gui.getLeftPanel().getPanelPage(), true);
         this.gui.getLeftPanel().setPanelPage(GUIEnums.GUIPanelPage.LEFT_WORKFORCE_COLOR_SELECT);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectDecreaseRed")) {
         if (!this.gui.getLeftPanel().isWorkforceColorSelectAccentMode()) {
            mobJob.adjustBaseColor(mobJobType, rightClick ? -10 : -1, 0, 0, 0);
         } else {
            mobJob.adjustAccentColor(mobJobType, rightClick ? -10 : -1, 0, 0, 0);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectIncreaseRed")) {
         if (!this.gui.getLeftPanel().isWorkforceColorSelectAccentMode()) {
            mobJob.adjustBaseColor(mobJobType, rightClick ? 10 : 1, 0, 0, 0);
         } else {
            mobJob.adjustAccentColor(mobJobType, rightClick ? 10 : 1, 0, 0, 0);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectDecreaseGreen")) {
         if (!this.gui.getLeftPanel().isWorkforceColorSelectAccentMode()) {
            mobJob.adjustBaseColor(mobJobType, 0, rightClick ? -10 : -1, 0, 0);
         } else {
            mobJob.adjustAccentColor(mobJobType, 0, rightClick ? -10 : -1, 0, 0);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectIncreaseGreen")) {
         if (!this.gui.getLeftPanel().isWorkforceColorSelectAccentMode()) {
            mobJob.adjustBaseColor(mobJobType, 0, rightClick ? 10 : 1, 0, 0);
         } else {
            mobJob.adjustAccentColor(mobJobType, 0, rightClick ? 10 : 1, 0, 0);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectDecreaseBlue")) {
         if (!this.gui.getLeftPanel().isWorkforceColorSelectAccentMode()) {
            mobJob.adjustBaseColor(mobJobType, 0, 0, rightClick ? -10 : -1, 0);
         } else {
            mobJob.adjustAccentColor(mobJobType, 0, 0, rightClick ? -10 : -1, 0);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectIncreaseBlue")) {
         if (!this.gui.getLeftPanel().isWorkforceColorSelectAccentMode()) {
            mobJob.adjustBaseColor(mobJobType, 0, 0, rightClick ? 10 : 1, 0);
         } else {
            mobJob.adjustAccentColor(mobJobType, 0, 0, rightClick ? 10 : 1, 0);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectDecreaseAlpha")) {
         if (!this.gui.getLeftPanel().isWorkforceColorSelectAccentMode()) {
            mobJob.adjustBaseColor(mobJobType, 0, 0, 0, rightClick ? -10 : -1);
         } else {
            mobJob.adjustAccentColor(mobJobType, 0, 0, 0, rightClick ? -10 : -1);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "workforceColorSelectIncreaseAlpha")) {
         if (!this.gui.getLeftPanel().isWorkforceColorSelectAccentMode()) {
            mobJob.adjustBaseColor(mobJobType, 0, 0, 0, rightClick ? 10 : 1);
         } else {
            mobJob.adjustAccentColor(mobJobType, 0, 0, 0, rightClick ? 10 : 1);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "resourceManagementFilterToggle")) {
         this.gui.getLeftPanel().toggleResourceManagementFilter();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "resourceManagementResourceMake" + t.getTemplate().getName() + "Down")) {
            if (input.isKeyDown(42)) {
               resource.setResourceMakeAmount(t, 0);
            } else {
               resource.decreaseResourceMakeAmount(t, rightClick ? 10 : 1);
            }

            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "resourceManagementResourceMake" + t.getTemplate().getName() + "Up")) {
            if (input.isKeyDown(42)) {
               resource.increaseResourceMakeAmount(t, ResourceModule.RESOURCE_INFINITE_AMOUNT);
            } else {
               resource.increaseResourceMakeAmount(t, rightClick ? 10 : 1);
            }

            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "resourceManagementResourceMaintain" + t.getTemplate().getName() + "Down")) {
            if (input.isKeyDown(42)) {
               resource.setResourceMaintainAmount(t, 0);
            } else {
               resource.decreaseResourceMaintainAmount(t, rightClick ? 10 : 1);
            }

            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "resourceManagementResourceMaintain" + t.getTemplate().getName() + "Up")) {
            if (input.isKeyDown(42)) {
               resource.increaseResourceMaintainAmount(t, ResourceModule.RESOURCE_INFINITE_AMOUNT);
            } else {
               resource.increaseResourceMaintainAmount(t, rightClick ? 10 : 1);
            }

            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftResourceManagementPageLeftTop")
         || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftResourceManagementPageLeftBottom")) {
         this.gui.getLeftPanel().setResourceManagementCategoryLeft();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftResourceManagementPageRightTop")
         || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftResourceManagementPageRightBottom")) {
         this.gui.getLeftPanel().setResourceManagementCategoryRight();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      String[] mapList = map.getConnectedRegionList();

      for (int i = 0; i < 8; i++) {
         if (!mapList[i].equals("null")) {
            if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "migrationDecrease" + i)) {
               migration.decreaseRequestedPopulation(mapList[i], rightClick ? 10 : 1);
               sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            }

            if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "migrationIncrease" + i)) {
               migration.increaseRequestedPopulation(mapList[i], rightClick ? 10 : 1);
               sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            }
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "courierFilterToggle")) {
         this.gui.getLeftPanel().toggleCourierFilter();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "courierLastRegion")) {
         this.gui.getLeftPanel().lastCourierMap();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "courierNextRegion")) {
         this.gui.getLeftPanel().nextCourierMap();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      ResourceModule.ResourceType[] var7;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "courierResourceSendNow" + t.getTemplate().getName() + "Down")) {
            if (input.isKeyDown(42)) {
               migration.setCourierResourceSendAmount(this.gui.getLeftPanel().getSelectedCourierMap(), t, 0);
            } else {
               migration.decreaseCourierResourceSendAmount(this.gui.getLeftPanel().getSelectedCourierMap(), t, rightClick ? 10 : 1);
            }

            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "courierResourceSendNow" + t.getTemplate().getName() + "Up")) {
            if (input.isKeyDown(42)) {
               migration.setCourierResourceSendAmount(this.gui.getLeftPanel().getSelectedCourierMap(), t, ResourceModule.RESOURCE_INFINITE_AMOUNT);
            } else {
               migration.increaseCourierResourceSendAmount(this.gui.getLeftPanel().getSelectedCourierMap(), t, rightClick ? 10 : 1);
            }

            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "courierResourceDaily" + t.getTemplate().getName() + "Down")) {
            if (input.isKeyDown(42)) {
               migration.setCourierResourceDailyAmount(this.gui.getLeftPanel().getSelectedCourierMap(), t, 0);
            } else {
               migration.decreaseCourierResourceDailyAmount(this.gui.getLeftPanel().getSelectedCourierMap(), t, rightClick ? 10 : 1);
            }

            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "courierResourceDaily" + t.getTemplate().getName() + "Up")) {
            if (input.isKeyDown(42)) {
               migration.setCourierResourceDailyAmount(this.gui.getLeftPanel().getSelectedCourierMap(), t, ResourceModule.RESOURCE_INFINITE_AMOUNT);
            } else {
               migration.increaseCourierResourceDailyAmount(this.gui.getLeftPanel().getSelectedCourierMap(), t, rightClick ? 10 : 1);
            }

            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftCourierPageLeftTop")
         || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftCourierPageLeftBottom")) {
         this.gui.getLeftPanel().setCourierCategoryLeft();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftCourierPageRightTop")
         || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_LEFT_PANEL, "leftCourierPageRightBottom")) {
         this.gui.getLeftPanel().setCourierCategoryRight();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedRightPanel(boolean rightClick) throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "pageBackTop") || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "pageBackBottom")) {
         this.gui.getRightPanel().backPage();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "pageUpTop") || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "pageUpBottom")) {
         this.gui.getRightPanel().lastPage();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "pageDownTop") || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "pageDownBottom")) {
         this.gui.getRightPanel().nextPage();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "hidePanel")) {
         this.gui.getRightPanel().togglePanelHidden();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "buildTab")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1);
         if (this.gui.getRightPanel().isPanelHidden()) {
            this.gui.getRightPanel().togglePanelHidden();
         }
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "roadsAndDiggingTab")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CATEGORY_ROADS_AND_DIGGING);
         if (this.gui.getRightPanel().isPanelHidden()) {
            this.gui.getRightPanel().togglePanelHidden();
         }
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "tilesTab")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_1);
         if (this.gui.getRightPanel().isPanelHidden()) {
            this.gui.getRightPanel().togglePanelHidden();
         }
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "sandboxToolsTab")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CATEGORY_SANDBOX_TOOLS);
         if (this.gui.getRightPanel().isPanelHidden()) {
            this.gui.getRightPanel().togglePanelHidden();
         }
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryBuild")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CATEGORY_BUILD_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryTiles")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TILES_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryTime")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CATEGORY_TIME);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategorySpawning")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CATEGORY_SPAWNING);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryParticles")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CATEGORY_PARTICLES);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryMisc")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CATEGORY_MISC);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryCivics")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CIVICS_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryDefense")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_DEFENSE);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryFoodAndWater")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_FOOD_AND_WATER_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryGatesAndWalls")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_GATES_AND_WALLS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryHarvesting")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_HARVESTING);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryHousing")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_HOUSING);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryLighting")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_LIGHTING);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryMagic")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_MAGIC);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryManufacturing")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_MANUFACTURING);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryRefining")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_REFINING);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryStorage")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_STORAGE_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryTrash")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_TRASH);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategorySpecial")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_SPECIAL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelAncillary")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ANCILLARY, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelAncillary");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelClinic")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CLINIC, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelClinic");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCourierStation")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.COURIER_STATION, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCourierStation");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMaintenanceBuilding")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.MAINTENANCE_BUILDING, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMaintenanceBuilding");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMarketplace")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.MARKETPLACE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMarketplace");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWayMakerShack")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.WAY_MAKER_SHACK, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWayMakerShack");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMigrationWayStation")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.MIGRATION_WAY_STATION, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMigrationWayStation");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryDefenseGolems")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_DEFENSE_GOLEMS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryDefenseTowers")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_DEFENSE_TOWERS_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryDefenseMiscellaneous")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_DEFENSE_MISCELLANEOUS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrystalGolemCombobulator")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYSTAL_GOLEM_COMBOBULATOR, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrystalGolemCombobulator");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelStoneGolemCombobulator")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.STONE_GOLEM_COMBOBULATOR, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelStoneGolemCombobulator");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWoodGolemCombobulator")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.WOOD_GOLEM_COMBOBULATOR, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWoodGolemCombobulator");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelAttractTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ATTRACT_TOWER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelAttractTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBanishTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BANISH_TOWER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBanishTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBallistaTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BALLISTA_TOWER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBallistaTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBowTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BOW_TOWER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBowTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBulletTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BULLET_TOWER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBulletTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelElementalBoltTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ELEMENTAL_BOLT_TOWER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelElementalBoltTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPhantomDartTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.PHANTOM_DART_TOWER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPhantomDartTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSlingTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.SLING_TOWER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSlingTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSprayTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.SPRAY_TOWER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSprayTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelStaticTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.STATIC_TOWER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelStaticTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLightningRod")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LIGHTNING_ROD, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLightningRod");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRecombobulatorTower")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.RECOMBOBULATOR_TOWER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRecombobulatorTower");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelAnimalPen")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ANIMAL_PEN, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelAnimalPen");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBottler")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BOTTLER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBottler");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCluckerCoop")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CLUCKER_COOP, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCluckerCoop");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelFarm")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.FARM, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelFarm");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSmallFountain")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.SMALL_FOUNTAIN, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSmallFountain");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLargeFountain")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LARGE_FOUNTAIN, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLargeFountain");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelKitchen")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.KITCHEN, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelKitchen");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelOutpost")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.OUTPOST, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelOutpost");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRainCatcher")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.RAIN_CATCHER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRainCatcher");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRangerLodge")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.RANGER_LODGE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRangerLodge");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWaterPurifier")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.WATER_PURIFIER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWaterPurifier");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWell")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.WELL, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWell");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryGatesAndWallsGates")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_GATES_AND_WALLS_GATES);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryGatesAndWallsWalls")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_GATES_AND_WALLS_WALLS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrylithiumWallGateNS")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_NS, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrylithiumWallGateNS");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrylithiumWallGateWE")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYLITHIUM_WALL_GATE_WE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrylithiumWallGateWE");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelStoneWallGateNS")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.STONE_WALL_GATE_NS, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelStoneWallGateNS");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelStoneWallGateWE")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.STONE_WALL_GATE_WE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelStoneWallGateWE");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWoodFenceGateNS")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.WOOD_FENCE_GATE_NS, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWoodFenceGateNS");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWoodFenceGateWE")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.WOOD_FENCE_GATE_WE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWoodFenceGateWE");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrylithiumWall")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYLITHIUM_WALL, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrylithiumWall");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCurtainWall")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CURTAIN_WALL, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCurtainWall");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrylithiumCurtainWall")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYLITHIUM_CURTAIN_WALL, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrylithiumCurtainWall");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelStoneWall")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.STONE_WALL, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelStoneWall");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTrashyCubeWall")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.TRASHY_CUBE_WALL, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTrashyCubeWall");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWoodFence")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.WOOD_FENCE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWoodFence");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrystalHarvestry")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYSTAL_HARVESTRY, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrystalHarvestry");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLumberShack")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LUMBER_SHACK, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLumberShack");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMiningFacility")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.MINING_FACILITY, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMiningFacility");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDoggoHouse")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.DOGGO_HOUSE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDoggoHouse");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelHousing")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.HOUSING, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelHousing");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrylithiumFirePit")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYLITHIUM_FIRE_PIT, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrylithiumFirePit");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLargeCrylithiumFirePit")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LARGE_CRYLITHIUM_FIRE_PIT, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLargeCrylithiumFirePit");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelFirePit")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.FIRE_PIT, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelFirePit");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLargeFirePit")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LARGE_FIRE_PIT, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLargeFirePit");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrystalMotivator")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYSTAL_MOTIVATOR, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrystalMotivator");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCullisGate")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CULLIS_GATE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCullisGate");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEssenceAltar")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ESSENCE_ALTAR, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEssenceAltar");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEssenceCollector")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ESSENCE_COLLECTOR, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEssenceCollector");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelReliquary")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.RELIQUARY, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelReliquary");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelArmorsmithy")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ARMORSMITHY, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelArmorsmithy");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBowyer")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BOWYER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBowyer");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRockTumbler")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ROCK_TUMBLER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRockTumbler");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelToolsmithy")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.TOOLSMITHY, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelToolsmithy");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPavePath")) {
         this.gui.getGUIData().setInterfaceModeRoad(GUIEnums.InterfaceMode.ASSIGN_ROAD_WORK, RoadModule.RoadType.PATH);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPavePath");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaveLogPath")) {
         this.gui.getGUIData().setInterfaceModeRoad(GUIEnums.InterfaceMode.ASSIGN_ROAD_WORK, RoadModule.RoadType.LOG_PATH);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaveLogPath");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaveCobbleAndLogPath")) {
         this.gui.getGUIData().setInterfaceModeRoad(GUIEnums.InterfaceMode.ASSIGN_ROAD_WORK, RoadModule.RoadType.COBBLE_AND_LOG_PATH);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaveCobbleAndLogPath");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaveCobbleAndBoardRoad")) {
         this.gui.getGUIData().setInterfaceModeRoad(GUIEnums.InterfaceMode.ASSIGN_ROAD_WORK, RoadModule.RoadType.COBBLE_AND_BOARD_ROAD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaveCobbleAndBoardRoad");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaveCutStoneAndBoardRoad")) {
         this.gui.getGUIData().setInterfaceModeRoad(GUIEnums.InterfaceMode.ASSIGN_ROAD_WORK, RoadModule.RoadType.CUT_STONE_AND_BOARD_ROAD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaveCutStoneAndBoardRoad");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDigHole")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ASSIGN_DIG_WORK);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDigHole");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDismantleRoads")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ASSIGN_DISMANTLE_ROAD_WORK);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDismantleRoads");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrystillery")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYSTILLERY, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrystillery");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelForge")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.FORGE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelForge");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLumberMill")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LUMBER_MILL, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLumberMill");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelStoneCuttery")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.STONE_CUTTERY, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelStoneCuttery");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelAmmoStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.AMMO_STORAGE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelAmmoStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrystalStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CRYSTAL_STORAGE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCrystalStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEquipmentStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.EQUIPMENT_STORAGE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEquipmentStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelFoodStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.FOOD_STORAGE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelFoodStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGoldStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.GOLD_STORAGE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGoldStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelKeyShack")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.KEY_SHACK, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelKeyShack");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMineralStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.MINERAL_STORAGE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMineralStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMiscellaneousStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.MISCELLANEOUS_STORAGE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMiscellaneousStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRockStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.ROCK_STORAGE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRockStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWoodStorage")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.WOOD_STORAGE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWoodStorage");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBurner")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.BURNER, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBurner");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCubeEGolemCombobulator")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CUBE_E_GOLEM_COMBOBULATOR, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCubeEGolemCombobulator");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLandfill")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LANDFILL, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLandfill");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelProcessor")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.PROCESSOR, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelProcessor");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTrashCan")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.TRASH_CAN, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTrashCan");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTrashyCubePile")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.TRASHY_CUBE_PILE, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTrashyCubePile");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCamp")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.CASTLE_1, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCamp");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLootBox")) {
         this.gui.getGUIData().setPlacementObject(MapTilesLoader.TileSet.LOOT_BOX, ObjectBase.ObjectSubType.BUILT, ObjectBase.ObjectSubType.BUILT);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLootBox");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryBricks")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_BRICKS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryCrystals")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_CRYSTALS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryDirt")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_DIRT);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryFlowers")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_FLOWERS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryFood")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_FOOD_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryGrass")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_GRASS);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryGravel")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_GRAVEL);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryLava")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_LAVA);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryRoads")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_ROADS_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryRock")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_ROCK);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategorySand")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_SAND);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategorySandstone")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_SANDSTONE);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategorySnow")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_SNOW);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryTar")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_TAR);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryTrees")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_TREES_1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCategoryWater")) {
         this.gui.getRightPanel().setPanelPage(GUIEnums.GUIPanelPage.RIGHT_WATER);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGrayBricks")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.BRICKS);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGrayBricks");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGrayTiles")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TILES);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGrayTiles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedCrystals")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.CRYSTAL_RED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedCrystals");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGreenCrystals")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.CRYSTAL_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGreenCrystals");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlueCrystals")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.CRYSTAL_BLUE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlueCrystals");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPurpleCrystals")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.CRYSTAL_PURPLE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPurpleCrystals");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBrownDirt")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.DIRT_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBrownDirt");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDarkBrownDirt")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.DIRT_DARK_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDarkBrownDirt");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLightBrownDirt")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.DIRT_LIGHT_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLightBrownDirt");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlackFlowers")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.FLOWERS_BLACK);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlackFlowers");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlueFlowers")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.FLOWERS_BLUE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlueFlowers");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPurpleFlowers")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.FLOWERS_PURPLE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPurpleFlowers");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedFlowers")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.FLOWERS_RED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedFlowers");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWhiteFlowers")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.FLOWERS_WHITE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWhiteFlowers");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelYellowFlowers")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.FLOWERS_YELLOW);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelYellowFlowers");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCactus")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.CACTUS_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCactus");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCarrots")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.CARROTS);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCarrots");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelHolyPotatoes")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.HOLY_POTATOES);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelHolyPotatoes");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMelons")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.MELONS);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMelons");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMushrooms")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.MUSHROOMS);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMushrooms");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPotatoes")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.POTATOES);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPotatoes");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTurnips")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TURNIPS);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTurnips");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmeraldGreenGrass")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRASS_EMERALD_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmeraldGreenGrass");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGreenGrass")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRASS_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGreenGrass");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTealGrass")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRASS_TEAL);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTealGrass");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelYellowBrownGrass")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRASS_YELLOW_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelYellowBrownGrass");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlueGravel")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRAVEL_BLUE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlueGravel");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGrayGravel")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRAVEL_GRAY);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGrayGravel");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedGravel")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.GRAVEL_RED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedGravel");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLava")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.LAVA);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLava");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPath")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.PATH);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPath");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLogPathDebris")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.LOG_PATH_DEBRIS);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLogPathDebris");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLogPath")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.LOG_PATH);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLogPath");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCobbleAndLogPathDebris")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.COBBLE_AND_LOG_PATH_DEBRIS);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCobbleAndLogPathDebris");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCobbleAndLogPath")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.COBBLE_AND_LOG_PATH);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCobbleAndLogPath");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCobbleAndBoardRoadDebris")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.COBBLE_AND_BOARD_ROAD_DEBRIS);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCobbleAndBoardRoadDebris");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCobbleAndBoardRoad")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.COBBLE_AND_BOARD_ROAD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCobbleAndBoardRoad");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCutStoneAndBoardRoadDebris")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.CUT_STONE_AND_BOARD_ROAD_DEBRIS);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCutStoneAndBoardRoadDebris");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCutStoneAndBoardRoad")) {
         this.gui.getGUIData().setPlacementRoadType(RoadModule.RoadType.CUT_STONE_AND_BOARD_ROAD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCutStoneAndBoardRoad");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlackRock")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.ROCK_BLACK);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlackRock");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBrownRock")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.ROCK_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBrownRock");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGrayRock")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.ROCK_GRAY);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGrayRock");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedRock")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.ROCK_RED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedRock");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWhiteRock")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.ROCK_WHITE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWhiteRock");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlackSand")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SAND_BLACK);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlackSand");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedSand")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SAND_RED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedSand");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTanSand")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SAND_TAN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTanSand");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlackSandstone")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SANDSTONE_BLACK);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlackSandstone");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedSandstone")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SANDSTONE_RED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedSandstone");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTanSandstone")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SANDSTONE_TAN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTanSandstone");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWhiteSnow")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.SNOW_WHITE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWhiteSnow");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTar")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TAR);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTar");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlueDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_BLUE_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlueDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlueStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_BLUE_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlueStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlueTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_BLUE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBlueTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBrownDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_BROWN_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBrownDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBrownStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_BROWN_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBrownStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBrownTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_BROWN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelBrownTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDarkGreenDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_DARK_GREEN_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDarkGreenDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDarkGreenStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_DARK_GREEN_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDarkGreenStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDarkGreenTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_DARK_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelDarkGreenTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGreenDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_GREEN_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGreenDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGreenStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_GREEN_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGreenStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGreenTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_GREEN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGreenTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLavenderDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_LAVENDER_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLavenderDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLavenderStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_LAVENDER_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLavenderStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLavenderTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_LAVENDER);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLavenderTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaleBlueDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_PALE_BLUE_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaleBlueDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaleBlueStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_PALE_BLUE_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaleBlueStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaleBlueTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_PALE_BLUE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelPaleBlueTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedDeadTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_RED_DEAD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedDeadTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedStumps")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_RED_COLLECTED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedStumps");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedTrees")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.TREES_RED);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRedTrees");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWater")) {
         this.gui.getGUIData().setPlacementTerrainType(MapTilesLoader.TileSet.WATER);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelWater");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCurrentDayMinus")) {
         time.setDayBack();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelCurrentDayPlus")) {
         time.setDayForward();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTimeOfDayLeft")) {
         time.setTimeOfDayBack(false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelTimeOfDayRight")) {
         time.setTimeOfDayForward(false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSeasonLeft")) {
         time.setSeasonBack();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSeasonRight")) {
         time.setSeasonForward();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelToggleTime") || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelToggleTimeFrozen")) {
         time.toggleFreezeTime();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelAdvanceTimeMinus")) {
         this.decreaseAdvanceTime();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelAdvanceTimePlus")) {
         this.increaseAdvanceTime();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelAdvanceTimeStart")) {
         this.startAdvanceTime();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMobTypeLeft")) {
         this.gui.getRightPanel().setSelectedSpawnMobTypeLeft();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SPAWN_MOB);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnMob");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMobTypeRight")) {
         this.gui.getRightPanel().setSelectedSpawnMobTypeRight();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SPAWN_MOB);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnMob");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMobAmountMinus")) {
         this.gui.getRightPanel().decreaseSelectedSpawnMobAmount(rightClick ? 10 : 1);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SPAWN_MOB);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnMob");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMobAmountPlus")) {
         this.gui.getRightPanel().increaseSelectedSpawnMobAmount(rightClick ? 10 : 1);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SPAWN_MOB);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnMob");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnMob")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SPAWN_MOB);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnMob");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelResourceTypeLeft")) {
         this.gui.getRightPanel().setSelectedSpawnResourceTypeLeft();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SPAWN_RESOURCE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnResource");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelResourceTypeRight")) {
         this.gui.getRightPanel().setSelectedSpawnResourceTypeRight();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SPAWN_RESOURCE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnResource");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelResourceAmountMinus")) {
         this.gui.getRightPanel().decreaseSelectedSpawnResourceAmount(rightClick ? 10 : 1);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SPAWN_RESOURCE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnResource");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelResourceAmountPlus")) {
         this.gui.getRightPanel().increaseSelectedSpawnResourceAmount(rightClick ? 10 : 1);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SPAWN_RESOURCE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnResource");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnResource")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SPAWN_RESOURCE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnResource");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnChanceMinus")) {
         spawn.decreaseBaseSpawnRate(rightClick ? 10 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelSpawnChancePlus")) {
         spawn.increaseBaseSpawnRate(rightClick ? 10 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelParticleTypeLeft")) {
         this.gui.getRightPanel().setSelectedSpawnParticleTypeLeft();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.EMIT_SINGLE_PARTICLES);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelParticleTypeRight")) {
         this.gui.getRightPanel().setSelectedSpawnParticleTypeRight();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.EMIT_SINGLE_PARTICLES);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelParticleGroupLeft")) {
         this.gui.getRightPanel().setSelectedSpawnParticleGroupLeft();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.EMIT_GROUP_PARTICLES);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelParticleGroupRight")) {
         this.gui.getRightPanel().setSelectedSpawnParticleGroupRight();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.EMIT_GROUP_PARTICLES);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelParticleColorLeft")) {
         this.gui.getRightPanel().setSelectedSpawnParticleColorLeft();
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelParticleColorRight")) {
         this.gui.getRightPanel().setSelectedSpawnParticleColorRight();
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelParticleAmountMinus")) {
         this.gui.getRightPanel().decreaseSelectedSpawnParticleAmount(rightClick ? 20 : 1);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelParticleAmountPlus")) {
         this.gui.getRightPanel().increaseSelectedSpawnParticleAmount(rightClick ? 20 : 1);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelParticleSpreadMinus")) {
         this.gui.getRightPanel().decreaseSelectedSpawnParticleSpread(rightClick ? 10 : 1);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelParticleSpreadPlus")) {
         this.gui.getRightPanel().increaseSelectedSpawnParticleSpread(rightClick ? 10 : 1);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelParticleAngleMinus")) {
         this.gui.getRightPanel().decreaseSelectedSpawnParticleAngle(rightClick ? 10 : 1);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelParticleAnglePlus")) {
         this.gui.getRightPanel().increaseSelectedSpawnParticleAngle(rightClick ? 10 : 1);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.EMIT_SINGLE_PARTICLES);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitSingleParticles")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.EMIT_SINGLE_PARTICLES);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitSingleParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitGroupParticles")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.EMIT_GROUP_PARTICLES);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitGroupParticles");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEssenceAmountMinus")) {
         this.gui.getRightPanel().decreaseEssenceAmount(rightClick ? 10 : 1);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.EMIT_ESSENCE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitEssence");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEssenceAmountPlus")) {
         this.gui.getRightPanel().increaseEssenceAmount(rightClick ? 10 : 1);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.EMIT_ESSENCE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitEssence");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEssenceValueMinus")) {
         this.gui.getRightPanel().decreaseEssenceValue(rightClick ? 5 : 1);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.EMIT_ESSENCE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitEssence");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEssenceValuePlus")) {
         this.gui.getRightPanel().increaseEssenceValue(rightClick ? 5 : 1);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.EMIT_ESSENCE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitEssence");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitEssence")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.EMIT_ESSENCE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelEmitEssence");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelToggleWeatherOn") || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelToggleWeatherOff")) {
         if (weather.isWeather()) {
            weather.stopWeather();
         } else {
            weather.startWeather();
         }
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLightning")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.LIGHTNING);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelLightning");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRestoreBasicNeeds")) {
         for (MobBase m : mob.getMobArray()) {
            if (m.isDead()) continue;
            m.increaseHunger(m.getHungerMax());
            m.increaseThirst(m.getThirstMax());
            m.increaseEnergy(m.getEnergyMax());
            m.increaseFaith(m.getFaithMax(), false);
            m.setBodyTemp(m.getMobFlags().getThermalBaseline());
         }
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelRestoreHealth")) {
         for (MobBase m : mob.getMobArray()) {
            if (m.isDead()) continue;
            m.increaseHitPoints(m.getHitPointsMax());
         }
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMissileTypeMinus")) {
         this.gui.getRightPanel().decreaseMissileType();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.FIRE_MISSILE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelFireMissile");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelMissileTypePlus")) {
         this.gui.getRightPanel().increaseMissileType();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.FIRE_MISSILE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelFireMissile");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelFireMissile")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.FIRE_MISSILE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelFireMissile");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGenerateCloud")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.GENERATE_CLOUD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_RIGHT_PANEL, "panelGenerateCloud");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedHarvestAndTerrainButtons(boolean rightClick) throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "brushSizeMinus")) {
         this.gui.getBrushLoader().setBrushSizeDown(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "brushSizePlus")) {
         this.gui.getBrushLoader().setBrushSizeUp(rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "harvestWood")) {
         this.gui.getGUIData().setInterfaceModeResource(GUIEnums.InterfaceMode.ASSIGN_HARVEST_WORK, ResourceModule.ResourceType.WOOD);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "harvestWood");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "harvestRock")) {
         this.gui.getGUIData().setInterfaceModeResource(GUIEnums.InterfaceMode.ASSIGN_HARVEST_WORK, ResourceModule.ResourceType.ROCK);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "harvestRock");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "harvestFoodAndWater")) {
         this.gui.getGUIData().setInterfaceModeResource(GUIEnums.InterfaceMode.ASSIGN_HARVEST_WORK, ResourceModule.ResourceType.RAW_VEGETABLE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "harvestFoodAndWater");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "harvestCrystals")) {
         this.gui.getGUIData().setInterfaceModeResource(GUIEnums.InterfaceMode.ASSIGN_HARVEST_WORK, ResourceModule.ResourceType.CRYSTAL);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "harvestCrystals");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "accentMode")) {
         map.lockAccentMode();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseAll")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.ERASE
            || this.gui.getGUIData().getSelectedEraseMode() != MapModule.EraseMode.ALL) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
            this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.ALL);
            this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseAll");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseTerrain")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.ERASE
            || this.gui.getGUIData().getSelectedEraseMode() != MapModule.EraseMode.TERRAIN) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
            this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.TERRAIN);
            this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseTerrain");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseTopography")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.ERASE
            || this.gui.getGUIData().getSelectedEraseMode() != MapModule.EraseMode.TOPOGRAPHY) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
            this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.TOPOGRAPHY);
            this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseTopography");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseObjects")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.ERASE
            || this.gui.getGUIData().getSelectedEraseMode() != MapModule.EraseMode.OBJECTS) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
            this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.OBJECTS);
            this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseObjects");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseRoads")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.ERASE
            || this.gui.getGUIData().getSelectedEraseMode() != MapModule.EraseMode.ROADS) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
            this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.ROADS);
            this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseRoads");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "hole")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.HOLE) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.HOLE);
            this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "hole");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "patch")) {
         if (this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.PATCH) {
            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.PATCH);
            this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "patch");
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            return;
         }

         this.buttonCycles();
      }
   }

   private void controlsMousePressedConsolePanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_CONSOLE_PANEL, "hidePanel")) {
         this.gui.getConsolePanel().toggleConsolePanelHidden();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedGoalPopup() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_GOAL_POPUP, "activeGoalButton")
         || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_GOAL_POPUP, "completedGoalBanner")
         || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_GOAL_POPUP, "unlockedGoalBanner")) {
         this.gui.toggleGoalsPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedGoalsPanel() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_GOALS_PANEL, "activeGoalButton")) {
         this.gui.toggleGoalsPanel();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_GOALS_PANEL, "slider")) {
         this.gui.getGoalsPanel().lockSlider();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      HashMap<GoalModule.GoalType, Goal> allGoals = goal.getAllGoals();

      for (Goal g : allGoals.values()) {
         if (!g.isLocked() && !g.isClaimed() && this.gui.intersects(GUIControllerBase.GUIPanel.SHARED_GOALS_PANEL, g.getSystemName()) && g.isCompleted()) {
            g.setClaimed();
            this.gui.getGoalsPanel().playEffects(g);
            sound.playSound(SoundModule.SoundType.BELL);
            sound.playSound(SoundModule.SoundType.HOLY_SOFT);
            sound.playSound(SoundModule.SoundType.LOOT_BOX_LOCKED);
            sound.playSound(SoundModule.SoundType.MAGIC_HEAL);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
            god.increaseGodXP(g.getGodXPAmount(), GodModule.GodXPType.UNLOCKED_GOAL);
            save.getActiveWorldSave().fullSaveSingleThreaded();
         }
      }
   }

   private void controlsMousePressedSelectedObjectPanel(boolean rightClick) throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "desiredWorkerIncrease")) {
         mobJob.increaseDesiredWorkers(this.gui.getGUIData().getSelectedObject().getObjectFlags().getWorkerJobType(), rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "desiredWorkerDecrease")) {
         mobJob.decreaseDesiredWorkers(this.gui.getGUIData().getSelectedObject().getObjectFlags().getWorkerJobType(), rightClick ? 5 : 1);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "priorityTop")) {
         ObjectBase selectedBuilding = this.gui.getGUIData().getSelectedObject();
         object.setPriorityTop(selectedBuilding);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "priorityUp")) {
         ObjectBase selectedBuilding = this.gui.getGUIData().getSelectedObject();
         object.increasePriority(selectedBuilding);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "priorityDown")) {
         ObjectBase selectedBuilding = this.gui.getGUIData().getSelectedObject();
         object.decreasePriority(selectedBuilding);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "priorityBottom")) {
         ObjectBase selectedBuilding = this.gui.getGUIData().getSelectedObject();
         object.setPriorityBottom(selectedBuilding);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "pause")
         || this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "resume")) {
         this.gui.getGUIData().getSelectedObject().togglePaused();
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "reclaim")) {
         this.gui.getGUIData().getSelectedObject().togglePaused();
         object.setPriorityBottom(this.gui.getGUIData().getSelectedObject());
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "upgrade")) {
         this.gui.getSelectedObjectPanel().setObjectPanelAttemptUpgrade(true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "startUpgrade1")) {
         this.gui.getGUIData().getSelectedObject().toggleUpgrade(this.gui.getGUIData().getSelectedObject().getObjectFlags().getNextUpgradeType());
         this.gui.getSelectedObjectPanel().setObjectPanelAttemptUpgrade(false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "startUpgrade2")) {
         this.gui.getGUIData().getSelectedObject().toggleUpgrade(this.gui.getGUIData().getSelectedObject().getObjectFlags().getNextUpgradeType2());
         this.gui.getSelectedObjectPanel().setObjectPanelAttemptUpgrade(false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "startUpgrade3")) {
         this.gui.getGUIData().getSelectedObject().toggleUpgrade(this.gui.getGUIData().getSelectedObject().getObjectFlags().getNextUpgradeType3());
         this.gui.getSelectedObjectPanel().setObjectPanelAttemptUpgrade(false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "upgradeBack")) {
         this.gui.getSelectedObjectPanel().setObjectPanelAttemptUpgrade(false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "dismantle")) {
         this.gui.getSelectedObjectPanel().setObjectPanelAttemptDismantle(true);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "dismantleYes")) {
         this.gui.getGUIData().getSelectedObject().startDismantle();
         this.gui.getSelectedObjectPanel().setObjectPanelAttemptDismantle(false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_SELECTED_OBJECT_PANEL, "dismantleNo")) {
         this.gui.getSelectedObjectPanel().setObjectPanelAttemptDismantle(false);
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedToolBar() throws SlickException {
      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_TOOLS_BAR, "pauseObjects")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.PAUSE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_TOOLS_BAR, "pauseObjects");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_TOOLS_BAR, "dismantleObjects")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.DISMANTLE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_TOOLS_BAR, "dismantleObjects");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_TOOLS_BAR, "destroyTerrain")) {
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ASSIGN_CLEAR_WORK);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_TOOLS_BAR, "destroyTerrain");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   private void controlsMousePressedInterfaceMode() throws SlickException {
      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.SELECT) {
         MobBase mobSelection = mob.getMobAt(this.mouse, true);
         if (mobSelection == null && touchMode) {
            mobSelection = this.getMobNearTouch();
         }

         if (mobSelection != null) {
            if (mobSelection.isDead()) {
               mobSelection.poke();
               return;
            }

            if (!mobSelection.getNoSelect()) {
               this.gui.setSelectedMob(mobSelection);
               return;
            }
         }

         ObjectBase objectSelection = object.getObjectAt(this.mouse);
         if (objectSelection != null) {
            if (!objectSelection.getObjectFlags().canNotSelect()) {
               this.gui.setSelectedObject(objectSelection);
               return;
            }

            if (objectSelection.getObjectFlags().isLootBox()) {
               objectSelection.pokeLootBox();
               return;
            }
         }

         lootBox.pokeLootBoxHidingSpot(this.getMouseTileX(), this.getMouseTileY());
         if (!Utilities.isOutOfBoundsOfUsableMap(this.getMouseTileX(), this.getMouseTileY())) {
            int tileID = 0;

            for (int l = 11; l >= 0; l--) {
               tileID = map.getTileId(this.getMouseTileX(), this.getMouseTileY(), l);
               if (tileID != 0) {
                  break;
               }
            }

            if (tileID != 0) {
               String s = map.getMapTileLoader().getTileSetByTileID(tileID).toString();
               if (s.contains("GRASS")) {
                  goal.incrementGoal(GoalModule.GoalType.TOUCH_GRASS);
               }
            }
         }

         this.gui.deselectAllSelected();
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.SPAWN_MOB) {
         for (int x = 0; x < this.gui.getRightPanel().getSelectedMobAmount(); x++) {
            mob.newMob(this.getMouseTileX(), this.getMouseTileY(), this.gui.getRightPanel().getSelectedSpawnMobType(), true);
         }
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.SPAWN_RESOURCE) {
         for (int x = 0; x < this.gui.getRightPanel().getSelectedResourceAmount(); x++) {
            if (!resource.isResourcesOnGround(this.getMouseTileX(), this.getMouseTileY())) {
               resource.createResourceOnGround(
                  this.gui.getRightPanel().getSelectedSpawnResourceType(), ResourceModule.ResourceColorSet.DEFAULT, this.getMouseTileX(), this.getMouseTileY()
               );
            } else {
               OrderedPair o = resource.findOpenSpace(this.getMouseTileX(), this.getMouseTileY(), MapData.BlockMapGroup.STANDARD);
               resource.createResourceOnGround(
                  this.gui.getRightPanel().getSelectedSpawnResourceType(), ResourceModule.ResourceColorSet.DEFAULT, o.getX(), o.getY()
               );
            }
         }
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.LIGHTNING) {
         weather.newLightning(this.getMouseTileX(), this.getMouseTileY(), 500, true, ParticleModule.ParticleSet.MAGIC_BLUE);
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.INFLUENCE
         && !influence.getSpellFactory().getSpellInstance(influence.getSelectedSpell()).allowContinuousCast()) {
         if (this.gameMode == GameModeTemplateBase.GameMode.SANDBOX && input.isKeyDown(SettingsParser.getKeyAccentModeToggle())) {
            influence.forceCastSpell(this.getMouseOnMapX(), this.getMouseOnMapY(), influence.getSelectedSpell());
         } else {
            influence.castSelectedSpell(this.getMouseOnMapX(), this.getMouseOnMapY());
         }
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.PAUSE) {
         ObjectBase selectedObject = object.getObjectAt(this.mouse);
         if (selectedObject != null
            && !selectedObject.getObjectFlags().canNotSelect()
            && selectedObject.isInRange()
            && selectedObject.getObjectFlags().allowPause()) {
            selectedObject.togglePaused();
         }
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.DISMANTLE) {
         ObjectBase selectedObject = object.getObjectAt(this.mouse);
         if (this.gameMode == GameModeTemplateBase.GameMode.SANDBOX && input.isKeyDown(SettingsParser.getKeyAccentModeToggle())) {
            if (selectedObject != null) {
               object.flagObjectForDestructionAndRemoval(selectedObject);
            }
         } else if (selectedObject != null
            && !selectedObject.getObjectFlags().canNotSelect()
            && selectedObject.isInRange()
            && selectedObject.getObjectFlags().allowDismantle()
            && selectedObject.getObjectFlags().getSubType() != ObjectBase.ObjectSubType.DISMANTLE) {
            selectedObject.startDismantle();
         }
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.EMIT_ESSENCE) {
         essence.newEssence(
            this.getMouseOnMapX(), this.getMouseOnMapY(), this.gui.getRightPanel().getEssenceValue(), this.gui.getRightPanel().getEssenceAmount()
         );
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.FIRE_MISSILE) {
         ArrayList<MobBase> mobList = mob.getMobsNearby(this.getMouseTileX(), this.getMouseTileY(), 16, MobBase.MobGroup.MONSTER);
         if (mobList.size() > 0) {
            mob.sortMobsByDistance(this.getMouseTileX(), this.getMouseTileY(), mobList);
            missile.newMissile(this.getMouseOnMapX(), this.getMouseOnMapY(), mobList.get(0), this.gui.getRightPanel().getMissileType());
         }
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.GENERATE_CLOUD) {
         cloud.addNewCluster(this.getMouseOnMapX() - 128, this.getMouseOnMapY() - 128);
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

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.ASSIGN_HARVEST_WORK) {
         if (input.isKeyDown(SettingsParser.getKeyUnassign())) {
            workSelection.deselectHarvestResources(
               this.getMouseTileX(),
               this.getMouseTileY(),
               this.gui.getGUIData().getInterfaceModeResourceType(),
               this.gui.getBrushLoader().getBrushSize(),
               this.gui.getBrushLoader().getBrushType()
            );
            if (this.gui.getGUIData().getInterfaceModeResourceType() == ResourceModule.ResourceType.RAW_VEGETABLE) {
               workSelection.deselectHarvestResources(
                  this.getMouseTileX(),
                  this.getMouseTileY(),
                  ResourceModule.ResourceType.DIRTY_WATER_BUCKET,
                  this.gui.getBrushLoader().getBrushSize(),
                  this.gui.getBrushLoader().getBrushType()
               );
            }
         } else {
            workSelection.selectHarvestResources(
               this.getMouseTileX(),
               this.getMouseTileY(),
               this.gui.getGUIData().getInterfaceModeResourceType(),
               this.gui.getBrushLoader().getBrushSize(),
               this.gui.getBrushLoader().getBrushType()
            );
            if (this.gui.getGUIData().getInterfaceModeResourceType() == ResourceModule.ResourceType.RAW_VEGETABLE) {
               workSelection.selectHarvestResources(
                  this.getMouseTileX(),
                  this.getMouseTileY(),
                  ResourceModule.ResourceType.DIRTY_WATER_BUCKET,
                  this.gui.getBrushLoader().getBrushSize(),
                  this.gui.getBrushLoader().getBrushType()
               );
            }
         }
      } else if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.ASSIGN_CLEAR_WORK) {
         if (input.isKeyDown(SettingsParser.getKeyUnassign())) {
            workSelection.deselectClearResources(
               this.getMouseTileX(), this.getMouseTileY(), this.gui.getBrushLoader().getBrushSize(), this.gui.getBrushLoader().getBrushType()
            );
         } else {
            workSelection.selectClearResources(
               this.getMouseTileX(), this.getMouseTileY(), this.gui.getBrushLoader().getBrushSize(), this.gui.getBrushLoader().getBrushType()
            );
         }
      } else if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.ASSIGN_DISMANTLE_ROAD_WORK) {
         if (input.isKeyDown(SettingsParser.getKeyUnassign())) {
            workSelection.deselectClearRoad(this.getMouseTileX(), this.getMouseTileY(), input.isKeyDown(SettingsParser.getKeyAccentModeToggle()));
         } else {
            workSelection.selectClearRoad(this.getMouseTileX(), this.getMouseTileY(), input.isKeyDown(SettingsParser.getKeyAccentModeToggle()));
         }
      } else if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.ASSIGN_DIG_WORK) {
         if (input.isKeyDown(SettingsParser.getKeyUnassign())) {
            workSelection.deselectDig(
               this.getMouseTileX(), this.getMouseTileY(), this.gui.getBrushLoader().getBrushSize(), this.gui.getBrushLoader().getBrushType()
            );
         } else {
            workSelection.selectDig(
               this.getMouseTileX(), this.getMouseTileY(), this.gui.getBrushLoader().getBrushSize(), this.gui.getBrushLoader().getBrushType()
            );
         }
      } else if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.ASSIGN_ROAD_WORK) {
         if (input.isKeyDown(SettingsParser.getKeyUnassign())) {
            workSelection.deselectRoadConstruction(this.getMouseTileX(), this.getMouseTileY(), input.isKeyDown(SettingsParser.getKeyAccentModeToggle()));
         } else {
            workSelection.selectRoadConstruction(
               this.getMouseTileX(),
               this.getMouseTileY(),
               this.gui.getGUIData().getInterfaceModeRoadType(),
               input.isKeyDown(SettingsParser.getKeyAccentModeToggle())
            );
         }
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.OBJECT) {
         if (this.gui.getGUIData().getPlacementObjectType() == MapTilesLoader.TileSet.CASTLE_1) {
            if (this.gameMode == GameModeTemplateBase.GameMode.SANDBOX && input.isKeyDown(SettingsParser.getKeyAccentModeToggle())) {
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

            for (ObjectBase o : object.getObjectsArrayList()) {
               if (o.getObjectFlags().getBaseType() == MapTilesLoader.TileSet.CASTLE_1) {
                  map.setMapStarted(true);
                  break;
               }
            }

            this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SELECT);
            if (save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP
               && migration.getEnroutePopulation(map.getFileName()) == 0
               && mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false).size() == 0) {
               for (ObjectBase ox : object.getObjectsArrayList()) {
                  if (ox.getObjectFlags().getBaseType() == MapTilesLoader.TileSet.CASTLE_1) {
                     OrderedPair c = ox.getCenterCoordinates();
                     influence.forceCastSpell(c.getX() * 16, c.getY() * 16, SpellBase.SpellType.SPECIAL_INITIAL_WORLD_MAP_SPAWN_IN);
                     break;
                  }
               }
            }
         } else if (this.gameMode == GameModeTemplateBase.GameMode.SANDBOX && input.isKeyDown(SettingsParser.getKeyAccentModeToggle())) {
            map.placeObject(this.getMouseTileX(), this.getMouseTileY(), this.gui.getGUIData().getPlacementObjectType(), ObjectBase.ObjectSubType.BUILT, true);
         } else if (this.gui.getGUIData().getPlacementObjectType() == MapTilesLoader.TileSet.ANCILLARY && object.canBuildNewAncillary()
            || this.gui.getGUIData().getPlacementObjectType() != MapTilesLoader.TileSet.ANCILLARY && object.canBuildNewBuilding()) {
            map.placeObject(
               this.getMouseTileX(),
               this.getMouseTileY(),
               this.gui.getGUIData().getPlacementObjectType(),
               this.gui.getGUIData().getPlacementObjectSubType(),
               false
            );
         }
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.WALL) {
         if (this.gameMode == GameModeTemplateBase.GameMode.SANDBOX && input.isKeyDown(SettingsParser.getKeyAccentModeToggle())) {
            map.placeWall(this.getMouseTileX(), this.getMouseTileY(), this.gui.getGUIData().getPlacementObjectType(), ObjectBase.ObjectSubType.BUILT, true);
         } else {
            map.placeWall(
               this.getMouseTileX(),
               this.getMouseTileY(),
               this.gui.getGUIData().getPlacementObjectType(),
               this.gui.getGUIData().getPlacementObjectSubType(),
               false
            );
         }
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.INFLUENCE
         && influence.getSpellFactory().getSpellInstance(influence.getSelectedSpell()).allowContinuousCast()) {
         if (this.gameMode == GameModeTemplateBase.GameMode.SANDBOX && input.isKeyDown(SettingsParser.getKeyAccentModeToggle())) {
            influence.forceCastSpell(this.getMouseOnMapX(), this.getMouseOnMapY(), influence.getSelectedSpell());
         } else {
            influence.castSelectedSpell(this.getMouseOnMapX(), this.getMouseOnMapY());
         }
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.EMIT_SINGLE_PARTICLES) {
         int spread = this.gui.getRightPanel().getSelectedSpawnParticleSpread() / 2;

         for (int x = 0; x < this.gui.getRightPanel().getSelectedSpawnParticleAmount(); x++) {
            particle.newParticle(
               this.getMouseOnMapX() + Utilities.randomInt(spread * -1, spread),
               this.getMouseOnMapY() + Utilities.randomInt(spread * -1, spread),
               this.gui.getRightPanel().getSelectedSpawnParticleAngle(),
               this.gui.getRightPanel().getSelectedSpawnParticleType(),
               this.gui.getRightPanel().getSelectedSpawnParticleColor()
            );
         }
      }

      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.EMIT_GROUP_PARTICLES) {
         int spread = this.gui.getRightPanel().getSelectedSpawnParticleSpread() / 2;

         for (int x = 0; x < this.gui.getRightPanel().getSelectedSpawnParticleAmount(); x++) {
            particle.newParticleGroup(
               this.getMouseOnMapX() + Utilities.randomInt(spread * -1, spread),
               this.getMouseOnMapY() + Utilities.randomInt(spread * -1, spread),
               this.gui.getRightPanel().getSelectedSpawnParticleAngle(),
               this.gui.getRightPanel().getSelectedSpawnParticleGroup(),
               this.gui.getRightPanel().getSelectedSpawnParticleColor()
            );
         }
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

   private void pauseGame() {
      this.gamePaused = !this.gamePaused;
   }

   @Override
   protected void updateCursor() throws SlickException {
      if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.INFLUENCE && influence.canCastSelectedSpell() && Utilities.randomInt(2) == 0) {
         particle.newParticleGroup(
            this.getMouseOnMapX() + 1 + Utilities.randomInt(-1, 1),
            this.getMouseOnMapY() + 1 + Utilities.randomInt(-1, 1),
            ParticleModule.ParticleGroup.FALL,
            ParticleModule.ParticleSet.FIRE_BLUE
         );
         particle.newParticleGroup(
            this.getMouseOnMapX() + 1 + Utilities.randomInt(-1, 1),
            this.getMouseOnMapY() + 1 + Utilities.randomInt(-1, 1),
            ParticleModule.ParticleGroup.SPARKLES,
            ParticleModule.ParticleSet.MAGIC_BLUE,
            ParticleModule.ParticleSet.MAGIC_BLUE
         );

         for (int x = 0; x < 2; x++) {
            particle.newParticleGroup(
               this.getMouseOnMapX() + 1 + Utilities.randomInt(-1, 1),
               this.getMouseOnMapY() + 1 + Utilities.randomInt(-1, 1),
               ParticleModule.ParticleGroup.MAGIC_SMOKE,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               ParticleModule.ParticleSet.MAGIC_BLUE
            );
         }
      }

      try {
         if (input.isMouseButtonDown(0)) {
            if (!Utilities.isOutOfBoundsOfUsableMap(this.getMouseTileX(), this.getMouseTileY())
               && corruption.isTileCorrupted(this.getMouseTileX(), this.getMouseTileY())) {
               Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BLACK_CLICK));
            } else if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.INFLUENCE) {
               if (influence.canCastSelectedSpell()) {
                  Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BLUE_CLICK));
               } else if (influence.getSpellCooldown(influence.getSelectedSpell()) > 0) {
                  Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.RED_CLICK));
               } else {
                  Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.RED_CLICK_STOP));
               }
            } else if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.PAUSE) {
               Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BROWN_CLICK_PAUSE));
            } else if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.DISMANTLE) {
               Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BROWN_CLICK_X));
            } else {
               Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BROWN_CLICK));
            }
         } else if (input.isMouseButtonDown(1)) {
            if (!Utilities.isOutOfBoundsOfUsableMap(this.getMouseTileX(), this.getMouseTileY())
               && corruption.isTileCorrupted(this.getMouseTileX(), this.getMouseTileY())) {
               Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BLACK_GRAB));
            } else if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.INFLUENCE) {
               if (influence.canCastSelectedSpell()) {
                  Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BLUE_GRAB));
               } else if (influence.getSpellCooldown(influence.getSelectedSpell()) > 0) {
                  Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.RED_GRAB));
               } else {
                  Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.RED_GRAB_STOP));
               }
            } else if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.PAUSE) {
               Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BROWN_GRAB_PAUSE));
            } else if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.DISMANTLE) {
               Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BROWN_GRAB_X));
            } else {
               Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BROWN_GRAB));
            }
         } else if (!Utilities.isOutOfBoundsOfUsableMap(this.getMouseTileX(), this.getMouseTileY())
            && corruption.isTileCorrupted(this.getMouseTileX(), this.getMouseTileY())) {
            Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BLACK));
         } else if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.INFLUENCE) {
            if (influence.canCastSelectedSpell()) {
               Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BLUE));
            } else if (influence.getSpellCooldown(influence.getSelectedSpell()) > 0) {
               Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.RED));
            } else {
               Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.RED_STOP));
            }
         } else if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.PAUSE) {
            Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BROWN_PAUSE));
         } else if (this.gui.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.DISMANTLE) {
            Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BROWN_X));
         } else {
            Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BROWN));
         }
      } catch (LWJGLException var2) {
         var2.printStackTrace();
      }
   }

   /**
    * Set by touch builds: disables edge scrolling (the pointer rests wherever the last tap was) and makes villager
    * selection forgiving (a fingertip covers several sprites' worth of pixels).
    */
   public static boolean touchMode = false;

   /** Nearest-first search for a mob around the pointer, growing the hit box until something is found. */
   private MobBase getMobNearTouch() {
      int[] radii = new int[]{4, 8, 14};

      for (int r : radii) {
         org.newdawn.slick.geom.Rectangle area = new org.newdawn.slick.geom.Rectangle(this.mouse.getX() - r, this.mouse.getY() - r, r * 2 + 1, r * 2 + 1);
         MobBase m = mob.getMobAt(area, true);
         if (m != null) {
            return m;
         }
      }

      return null;
   }

   /** Leaves any placement/assignment tool and returns to selection (what right-click does on the map). */
   public void cancelInterfaceMode() {
      this.cancelTasks();
   }

   /**
    * True if a point on the logical screen (display pixels, top-left origin) is over the game's own GUI. Panels share
    * this state's pointer rectangle, so it is moved there for the test and put back.
    */
   public boolean isGuiAt(int displayX, int displayY) {
      if (this.gui == null || this.mouse == null) {
         return false;
      }

      float oldX = this.mouse.getX();
      float oldY = this.mouse.getY();
      this.mouse.setX(displayX / ScaleControl.getInterfaceScale());
      this.mouse.setY(displayY / ScaleControl.getInterfaceScale());

      boolean hit;
      try {
         hit = this.gui.intersectsAnyGUIMask();
      } finally {
         this.mouse.setX(oldX);
         this.mouse.setY(oldY);
      }

      return hit;
   }

   /** Whether clicking now would place the selected building (same checks as the placement click). */
   public boolean canPlaceAtPointer() {
      if (this.gui == null || this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.OBJECT) {
         return false;
      }

      MapTilesLoader.TileSet type = this.gui.getGUIData().getPlacementObjectType();
      if (type == null) {
         return false;
      }

      try {
         if (type == MapTilesLoader.TileSet.CASTLE_1) {
            return map.canPlaceObject(this.getMouseTileX(), this.getMouseTileY(), type, true);
         }

         boolean slotFree = type == MapTilesLoader.TileSet.ANCILLARY ? object.canBuildNewAncillary() : object.canBuildNewBuilding();
         return slotFree && map.canPlaceObject(this.getMouseTileX(), this.getMouseTileY(), type, false);
      } catch (SlickException var3) {
         return false;
      }
   }

   // ---- Touch UI API (rtr.mobile): everything runs on the render thread between updates. ----

   public void startPlacing(MapTilesLoader.TileSet type) throws SlickException {
      this.cancelTasks();
      this.gui.getGUIData().setPlacementObject(type, ObjectBase.ObjectSubType.CONSTRUCTION, ObjectBase.ObjectSubType.BUILT);
      sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
   }

   public void startRoadTool(RoadModule.RoadType type) throws SlickException {
      this.cancelTasks();
      this.gui.getGUIData().setInterfaceModeRoad(GUIEnums.InterfaceMode.ASSIGN_ROAD_WORK, type);
      sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
   }

   public void startTool(GUIEnums.InterfaceMode mode) throws SlickException {
      this.cancelTasks();
      this.gui.getGUIData().setInterfaceMode(mode);
      sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
   }

   public boolean isSandbox() {
      return save.getActiveRegionalSave().getGameMode() == GameModeTemplateBase.GameMode.SANDBOX;
   }

   public boolean isSkirmish() {
      return save.getActiveRegionalSave().getGameType() == StateBase.GameType.SKIRMISH;
   }

   public boolean canBuildRoads() {
      return object.objectCount(MapTilesLoader.TileSet.WAY_MAKER_SHACK, ObjectBase.ObjectSubType.BUILT) > 0
         || object.objectCount(MapTilesLoader.TileSet.WAY_MAKER_SHACK, ObjectBase.ObjectSubType.CONSTRUCTION) > 0;
   }

   public boolean hasCamp() {
      return object.objectCount(MapTilesLoader.TileSet.CASTLE_1) > 0;
   }

   public int countBuildings(MapTilesLoader.TileSet type) {
      return object.objectCount(type);
   }

   /** {used, max} building slots. */
   public int[] getBuildingSlots() {
      return new int[]{object.getActiveBuildings(), object.getMaximumBuildings()};
   }

   public rtr.objects.objectflags.ObjectFlags getBuiltFlags(MapTilesLoader.TileSet type) {
      return object.getObjectFlagFactory().getObjectFlags(type, ObjectBase.ObjectSubType.BUILT);
   }

   private MapTilesLoader.TileSet ghostFlagsType;
   private rtr.objects.objectflags.ObjectFlags ghostFlags;

   /** The placement ghost's footprint on the logical screen as {x, y, w, h} in display pixels, or null when not placing. */
   public float[] getPlacementGhostBounds() {
      if (this.gui == null || this.gui.getGUIData().getInterfaceMode() != GUIEnums.InterfaceMode.OBJECT) {
         return null;
      }

      MapTilesLoader.TileSet type = this.gui.getGUIData().getPlacementObjectType();
      if (type == null) {
         return null;
      }

      // Same anchoring as DataModule.renderObjectOutline: the ghost is centred on the pointer's tile. Flags are built
      // per call, so keep the last ones.
      if (type != this.ghostFlagsType) {
         this.ghostFlags = this.getBuiltFlags(type);
         this.ghostFlagsType = type;
      }

      rtr.objects.objectflags.ObjectFlags f = this.ghostFlags;
      int left = this.getMouseTileX() - f.getWidth() / 2;
      int top = this.getMouseTileY() - f.getHeight() / 2;
      float s = ScaleControl.getInterfaceScale();
      float tile = 16.0F * ScaleControl.getWorldScale();
      return new float[]{
         ScaleControl.getTransformedPositionX(left * 16) * s, ScaleControl.getTransformedPositionY(top * 16) * s, f.getWidth() * tile, f.getHeight() * tile
      };
   }

   /** Drags the map by a distance in display pixels, so the ground follows the finger. */
   public void panByDisplay(float dx, float dy) {
      float k = ScaleControl.getWorldScale();
      map.setMapPosition(map.getMapX() + dx / k, map.getMapY() + dy / k);
   }

   public boolean isBuildSidebarOpen() {
      return !this.gui.getRightPanel().isPanelHidden();
   }

   public void setBuildSidebarOpen(boolean open) {
      if (open != this.isBuildSidebarOpen()) {
         this.gui.getRightPanel().togglePanelHidden();
      }
   }

   /** The build sidebar (desktop right panel) as {x, y, w, h} in display pixels, where it sits when open. */
   public float[] getBuildSidebarBounds() {
      float s = ScaleControl.getInterfaceScale();
      return new float[]{(ScaleControl.getInterfaceWidth() - 196) * s, this.gui.getRightPanel().getY() * s, 196.0F * s, 530.0F * s};
   }

   /** The selected villager, or null; drops a selection whose mob no longer exists. */
   public MobBase getTouchSelectedMob() {
      MobBase m = this.gui.getGUIData().getSelectedMob();
      if (m != null && (!mob.getMobArray().contains(m) || m.getNoSelect())) {
         this.gui.deselectAllSelected();
         return null;
      }

      return m;
   }

   /** The selected building, or null; drops a selection whose object no longer exists. */
   public ObjectBase getTouchSelectedObject() {
      ObjectBase o = this.gui.getGUIData().getSelectedObject();
      if (o != null && !object.objectExists(o)) {
         this.gui.deselectAllSelected();
         return null;
      }

      return o;
   }

   public void deselectAll() {
      this.gui.deselectAllSelected();
   }

   /** {current (incl. en route), desired, maximum} workers for the building's job, or null if it has no workers. */
   public int[] getWorkers(ObjectBase o) {
      MobJobBase.MobJobType jt = o.getObjectFlags().getWorkerJobType();
      if (jt == null || jt == MobJobBase.MobJobType.NONE) {
         return null;
      }

      return new int[]{mobJob.getCurrentWorkers(jt, true), mobJob.getDesiredWorkers(jt), mobJob.getMaximumWorkers(jt)};
   }

   public void changeWorkers(ObjectBase o, int delta) throws SlickException {
      MobJobBase.MobJobType jt = o.getObjectFlags().getWorkerJobType();
      if (jt != null && jt != MobJobBase.MobJobType.NONE) {
         if (delta > 0) {
            mobJob.increaseDesiredWorkers(jt, delta);
         } else if (delta < 0) {
            mobJob.decreaseDesiredWorkers(jt, -delta);
         }

         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }
   }

   public void togglePausedTouch(ObjectBase o) throws SlickException {
      o.togglePaused();
      sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
   }

   public void dismantleTouch(ObjectBase o) throws SlickException {
      o.startDismantle();
      sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
   }

   public void upgradeTouch(ObjectBase o, MapTilesLoader.TileSet next) throws SlickException {
      o.toggleUpgrade(next);
      sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
   }

   public void togglePauseGame() {
      this.gamePaused = !this.gamePaused;
   }

   public boolean isGamePaused() {
      return this.gamePaused;
   }

   public GUIEnums.InterfaceMode getInterfaceMode() {
      return this.gui == null || this.gui.getGUIData() == null ? null : this.gui.getGUIData().getInterfaceMode();
   }

   private void cancelTasks() {
      this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.SELECT);
      influence.setSelectedSpell(null);
      this.gui.deselectAllSelected();
      this.gui.deselectAll();
   }

   @Override
   public void mouseWheelMoved(int change) {
      if (!transition.getControlLockout() && !this.loadingRequired && !this.gamePaused && !map.isMapLost()) {
         try {
            if (this.gui.intersectsAnyGUIMask()) {
               if (this.gui.getGoalsPanel().intersectsGUIMask()) {
                  if (change > 50) {
                     this.gui.getGoalsPanel().moveUp();
                  }

                  if (change < -50) {
                     this.gui.getGoalsPanel().moveDown();
                  }
               }

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

               if (this.gui.getLeftPanel().intersectsGUIMask()) {
                  if (change > 50) {
                     if (this.gui.getLeftPanel().getPanelPage() == GUIEnums.GUIPanelPage.LEFT_WORKFORCE) {
                        this.gui.getLeftPanel().setWorkforcePageDown();
                     } else if ((
                           this.gui.getGUIData().getSelectedObject() == null
                              || this.gui.getGUIData().getSelectedObject().getObjectFlags().getBaseType() != MapTilesLoader.TileSet.MARKETPLACE
                        )
                        && this.gui.getLeftPanel().getPanelPage() != GUIEnums.GUIPanelPage.LEFT_TRADE) {
                        this.gui.getLeftPanel().setResourceManagementCategoryLeft();
                     } else {
                        this.gui.getLeftPanel().setTradeCategoryLeft();
                     }

                     sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
                  }

                  if (change < -50) {
                     if (this.gui.getLeftPanel().getPanelPage() == GUIEnums.GUIPanelPage.LEFT_WORKFORCE) {
                        this.gui.getLeftPanel().setWorkforcePageUp();
                     } else if ((
                           this.gui.getGUIData().getSelectedObject() == null
                              || this.gui.getGUIData().getSelectedObject().getObjectFlags().getBaseType() != MapTilesLoader.TileSet.MARKETPLACE
                        )
                        && this.gui.getLeftPanel().getPanelPage() != GUIEnums.GUIPanelPage.LEFT_TRADE) {
                        this.gui.getLeftPanel().setResourceManagementCategoryRight();
                     } else {
                        this.gui.getLeftPanel().setTradeCategoryRight();
                     }

                     sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
                  }
               }

               if (this.gui.getBuildingListPanel().intersectsGUIMask()) {
                  if (change > 50) {
                     this.gui.getBuildingListPanel().setPageUp();
                     sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
                  }

                  if (change < -50) {
                     this.gui.getBuildingListPanel().setPageDown();
                     sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
                  }
               }

               if (this.gui.getPopulationListPanel().intersectsGUIMask()) {
                  if (change > 50) {
                     this.gui.getPopulationListPanel().listPageUp();
                     sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
                  }

                  if (change < -50) {
                     this.gui.getPopulationListPanel().listPageDown();
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
      if (!this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "brushSizePlus")
         && !this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "brushSizeMinus")) {
         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "brushCircle")) {
            this.gui.getBrushLoader().setBrushType(BrushLoader.BrushType.FILLETED_SQUARE);
            this.gui.getHarvestAndTerrainBar().setBrush(BrushLoader.BrushType.FILLETED_SQUARE);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "brushFilletedSquare")) {
            this.gui.getBrushLoader().setBrushType(BrushLoader.BrushType.CHAMFERED_SQUARE);
            this.gui.getHarvestAndTerrainBar().setBrush(BrushLoader.BrushType.CHAMFERED_SQUARE);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "brushChamferedSquare")) {
            this.gui.getBrushLoader().setBrushType(BrushLoader.BrushType.SQUARE);
            this.gui.getHarvestAndTerrainBar().setBrush(BrushLoader.BrushType.SQUARE);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }

         if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "brushSquare")) {
            this.gui.getBrushLoader().setBrushType(BrushLoader.BrushType.ROUND);
            this.gui.getHarvestAndTerrainBar().setBrush(BrushLoader.BrushType.ROUND);
            sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
         }
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseAll")) {
         this.gui.getHarvestAndTerrainBar().setEraseMode(MapModule.EraseMode.TERRAIN);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
         this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.TERRAIN);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseTerrain");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseTerrain")) {
         this.gui.getHarvestAndTerrainBar().setEraseMode(MapModule.EraseMode.TOPOGRAPHY);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
         this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.TOPOGRAPHY);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseTopography");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseTopography")) {
         this.gui.getHarvestAndTerrainBar().setEraseMode(MapModule.EraseMode.OBJECTS);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
         this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.OBJECTS);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseObjects");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseObjects")) {
         this.gui.getHarvestAndTerrainBar().setEraseMode(MapModule.EraseMode.ROADS);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
         this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.ROADS);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseRoads");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseRoads")) {
         this.gui.getHarvestAndTerrainBar().setEraseMode(MapModule.EraseMode.ALL);
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.ERASE);
         this.gui.getGUIData().setSelectedEraseMode(MapModule.EraseMode.ALL);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "eraseAll");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "hole")) {
         this.gui.getHarvestAndTerrainBar().setHolePatchPatch();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.PATCH);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "patch");
         sound.playSound(SoundModule.SoundType.BUTTON_CLICK);
      }

      if (this.gui.intersects(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "patch")) {
         this.gui.getHarvestAndTerrainBar().setHolePatchHole();
         this.gui.getGUIData().setInterfaceMode(GUIEnums.InterfaceMode.HOLE);
         this.gui.select(GUIControllerBase.GUIPanel.PLAY_STATE_HARVEST_AND_TERRAIN_BAR, "hole");
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
}
