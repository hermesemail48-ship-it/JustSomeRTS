package rtr.states;

import rtr.platform.Platform;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.imageout.ImageOut;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import rtr.BackgroundModule;
import rtr.DataModule;
import rtr.GodModule;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.ProfileModule;
import rtr.SettingsParser;
import rtr.ShadowModule;
import rtr.SoundModule;
import rtr.StatsModule;
import rtr.TimeModule;
import rtr.TransitionModule;
import rtr.chest.ChestModule;
import rtr.console.Console;
import rtr.font.FontModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.gui.states.BrushLoader;
import rtr.gui.states.GUIControllerBase;
import rtr.help.HelpModule;
import rtr.influence.InfluenceModule;
import rtr.lighting.LightingModule;
import rtr.map.CollisionModule;
import rtr.map.DepthModule;
import rtr.map.MapModule;
import rtr.map.MiniMapModule;
import rtr.map.WaypointModule;
import rtr.map.WorkSelectionModule;
import rtr.map.ai.MapAIModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MigrationModule;
import rtr.mobs.MobModule;
import rtr.mobs.essence.EssenceModule;
import rtr.mobs.jobs.MobJobModule;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.LootBoxModule;
import rtr.resources.ResourceModule;
import rtr.resources.TradeModule;
import rtr.road.RoadModule;
import rtr.save.SaveModule;
import rtr.save.SavedGamesHandler;
import rtr.spawning.CorruptionModule;
import rtr.spawning.SpawnModule;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.system.threadpool.RTRThreadPool;
import rtr.utilities.Steamworks;
import rtr.utilities.Utilities;
import rtr.weather.CloudModule;
import rtr.weather.WeatherModule;

public class StateBase extends BasicGameState {
   protected GameModeTemplateBase.GameMode gameMode;
   protected static BackgroundModule background = new BackgroundModule();
   protected static CollisionModule collision = new CollisionModule();
   protected static DataModule data = new DataModule();
   protected static DepthModule depth = new DepthModule();
   protected static MobModule mob = new MobModule();
   protected static MobJobModule mobJob = new MobJobModule();
   protected static EssenceModule essence = new EssenceModule();
   protected static FontModule font = new FontModule();
   protected static LightingModule lighting = new LightingModule();
   protected static MapAIModule mapAI = new MapAIModule();
   protected static MapModule map = new MapModule();
   protected static WaypointModule waypoint = new WaypointModule();
   protected static RoadModule road = new RoadModule();
   protected static MiniMapModule miniMap = new MiniMapModule();
   protected static ObjectModule object = new ObjectModule();
   protected static ParticleModule particle = new ParticleModule();
   protected static ProfileModule profile = new ProfileModule();
   protected static ProjectileModule projectile = new ProjectileModule();
   protected static MissileModule missile = new MissileModule();
   protected static ResourceModule resource = new ResourceModule();
   protected static TradeModule trade = new TradeModule();
   protected static LootBoxModule lootBox = new LootBoxModule();
   protected static SaveModule save = new SaveModule();
   protected static ShadowModule shadow = new ShadowModule();
   protected static SoundModule sound = new SoundModule();
   protected static SpawnModule spawn = new SpawnModule();
   protected static TimeModule time = new TimeModule();
   protected static TransitionModule transition = new TransitionModule();
   protected static InfluenceModule influence = new InfluenceModule();
   protected static WeatherModule weather = new WeatherModule();
   protected static CloudModule cloud = new CloudModule();
   protected static WorkSelectionModule workSelection = new WorkSelectionModule();
   protected static HelpModule help = new HelpModule();
   protected static StatsModule stats = new StatsModule();
   protected static CorruptionModule corruption = new CorruptionModule();
   protected static MigrationModule migration = new MigrationModule();
   protected static ChestModule chest = new ChestModule();
   protected static PerkModule perk = new PerkModule();
   protected static GodModule god = new GodModule();
   protected static GoalModule goal = new GoalModule();
   public static HashMap<ModuleBase.ModuleType, ModuleBase> modulesMap = new HashMap<>();
   public static ArrayList<ModuleBase> modulesArray = new ArrayList<>();
   protected HashMap<ModuleBase.ModuleType, Boolean> loadModuleMap = new HashMap<>();
   protected boolean loadingRequired;
   protected boolean modulesLoaded;
   protected boolean initLoad;
   protected String currentLoad = "";
   protected long startTime;
   protected long endTime;
   protected long startTimeTotal;
   protected long endTimeTotal;
   protected Image loadingLogo;
   protected Image loadingPillar;
   protected int stateID;
   protected static GameContainer gc;
   protected static StateBasedGame sbg;
   protected Graphics g;
   protected Rectangle mouse;
   protected static Input input;
   protected boolean movingMap;
   protected int mouseXPressed;
   protected int mouseYPressed;
   protected int mouseX;
   protected int mouseY;
   protected int oldMouseX;
   protected int oldMouseY;
   protected int actualMouseX;
   protected int actualMouseY;
   protected boolean lockMouseX = false;
   protected boolean lockMouseY = false;
   protected boolean takeScreenShot;
   protected int gameSpeed = 1;
   protected HashMap<StateBase.CursorType, Cursor> cursors;
   private int savedCursorScale;

   public static ModuleBase getModule(ModuleBase.ModuleType t) {
      return modulesMap.get(t);
   }

   public StateBase(int s) {
      this.stateID = s;

      ModuleBase.ModuleType[] var5;
      for (ModuleBase.ModuleType t : ModuleBase.ModuleType.values()) {
         this.loadModuleMap.put(t, false);
      }

      modulesMap.put(ModuleBase.ModuleType.FONT, font);
      modulesArray.add(font);
      modulesMap.put(ModuleBase.ModuleType.SAVE, save);
      modulesArray.add(save);
      modulesMap.put(ModuleBase.ModuleType.TIME, time);
      modulesArray.add(time);
      modulesMap.put(ModuleBase.ModuleType.SOUND, sound);
      modulesArray.add(sound);
      modulesMap.put(ModuleBase.ModuleType.MAP, map);
      modulesArray.add(map);
      modulesMap.put(ModuleBase.ModuleType.ROAD, road);
      modulesArray.add(road);
      modulesMap.put(ModuleBase.ModuleType.MAP_AI, mapAI);
      modulesArray.add(mapAI);
      modulesMap.put(ModuleBase.ModuleType.MINI_MAP, miniMap);
      modulesArray.add(miniMap);
      modulesMap.put(ModuleBase.ModuleType.RESOURCE, resource);
      modulesArray.add(resource);
      modulesMap.put(ModuleBase.ModuleType.TRADE, trade);
      modulesArray.add(trade);
      modulesMap.put(ModuleBase.ModuleType.TRANSITION, transition);
      modulesArray.add(transition);
      modulesMap.put(ModuleBase.ModuleType.SHADOW, shadow);
      modulesArray.add(shadow);
      modulesMap.put(ModuleBase.ModuleType.LIGHTING, lighting);
      modulesArray.add(lighting);
      modulesMap.put(ModuleBase.ModuleType.PROJECTILE, projectile);
      modulesArray.add(projectile);
      modulesMap.put(ModuleBase.ModuleType.MISSILE, missile);
      modulesArray.add(missile);
      modulesMap.put(ModuleBase.ModuleType.PARTICLE, particle);
      modulesArray.add(particle);
      modulesMap.put(ModuleBase.ModuleType.COLLISION, collision);
      modulesArray.add(collision);
      modulesMap.put(ModuleBase.ModuleType.DEPTH, depth);
      modulesArray.add(depth);
      modulesMap.put(ModuleBase.ModuleType.CHEST, chest);
      modulesArray.add(chest);
      modulesMap.put(ModuleBase.ModuleType.PERK, perk);
      modulesArray.add(perk);
      modulesMap.put(ModuleBase.ModuleType.GOD, god);
      modulesArray.add(god);
      modulesMap.put(ModuleBase.ModuleType.GOAL, goal);
      modulesArray.add(goal);
      modulesMap.put(ModuleBase.ModuleType.OBJECT, object);
      modulesArray.add(object);
      modulesMap.put(ModuleBase.ModuleType.BACKGROUND, background);
      modulesArray.add(background);
      modulesMap.put(ModuleBase.ModuleType.MOB, mob);
      modulesArray.add(mob);
      modulesMap.put(ModuleBase.ModuleType.MOB_JOB, mobJob);
      modulesArray.add(mobJob);
      modulesMap.put(ModuleBase.ModuleType.ESSENCE, essence);
      modulesArray.add(essence);
      modulesMap.put(ModuleBase.ModuleType.SPAWN, spawn);
      modulesArray.add(spawn);
      modulesMap.put(ModuleBase.ModuleType.INFLUENCE, influence);
      modulesArray.add(influence);
      modulesMap.put(ModuleBase.ModuleType.WEATHER, weather);
      modulesArray.add(weather);
      modulesMap.put(ModuleBase.ModuleType.CLOUD, cloud);
      modulesArray.add(cloud);
      modulesMap.put(ModuleBase.ModuleType.WORK_SELECTION, workSelection);
      modulesArray.add(workSelection);
      modulesMap.put(ModuleBase.ModuleType.WAYPOINT, waypoint);
      modulesArray.add(waypoint);
      modulesMap.put(ModuleBase.ModuleType.DATA, data);
      modulesArray.add(data);
      modulesMap.put(ModuleBase.ModuleType.HELP, help);
      modulesArray.add(help);
      modulesMap.put(ModuleBase.ModuleType.LOOT_BOX, lootBox);
      modulesArray.add(lootBox);
      modulesMap.put(ModuleBase.ModuleType.STATS, stats);
      modulesArray.add(stats);
      modulesMap.put(ModuleBase.ModuleType.CORRUPTION, corruption);
      modulesArray.add(corruption);
      modulesMap.put(ModuleBase.ModuleType.MIGRATION, migration);
      modulesArray.add(migration);
      modulesMap.put(ModuleBase.ModuleType.PROFILE, profile);
      modulesArray.add(profile);
   }

   @Override
   public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
      StateBase.gc = gc;
      StateBase.sbg = sbg;
      this.g = gc.getGraphics();
      input = gc.getInput();
      input.enableKeyRepeat();
      this.mouse = new Rectangle(0.0F, 0.0F, 1.0F, 1.0F);
      this.initLoad = true;
      this.loadingLogo = ImageLoader.getImage("res/GUI/mainMenu/logo.png");
      this.loadingPillar = ImageLoader.getImage("res/GUI/mainMenu/pillar.png");
   }

   private void resetModules() throws SlickException {
      System.out.println("Resetting " + this.gameMode);

      while (!RTRThreadPool.allThreadsIdle()) {
         try {
            System.out.println("Dedicated threads have not completed, waiting.");
            Thread.sleep(100L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }
      }

      for (ModuleBase.ModuleType t : this.loadModuleMap.keySet()) {
         if (this.loadModuleMap.get(t) && modulesMap.get(t).isInitialized()) {
            modulesMap.get(t).resetModule();
         }
      }
   }

   protected void loadState() throws SlickException {
      if (!this.modulesLoaded) {
         if (this.initLoad) {
            this.startTimeTotal = System.currentTimeMillis();
            Console.out("Loading Game", true);
            if (this.gameMode != GameModeTemplateBase.GameMode.MAIN_MENU) {
               System.out.println("Loading " + this.gameMode.getFolderName());
               SavedGamesHandler.loadData();
            }

            this.initLoad = false;
         }

         for (ModuleBase m : modulesArray) {
            ModuleBase.ModuleType t = null;

            for (ModuleBase.ModuleType t2 : modulesMap.keySet()) {
               if (modulesMap.get(t2) == m) {
                  t = t2;
                  break;
               }
            }

            if (this.loadModuleMap.get(t) && !modulesMap.get(t).isInitialized()) {
               Text.setVariableText("moduleName", t.getFormattedName());
               String thisModuleString = Text.getText("module.loading.module");
               if (!this.currentLoad.equals(thisModuleString)) {
                  this.currentLoad = thisModuleString;
                  return;
               }

               this.startTime = System.currentTimeMillis();
               modulesMap.get(t).initModule(t, this);
               this.endTime = System.currentTimeMillis();
               Console.out(t.getSystemName() + " Module: " + (this.endTime - this.startTime), true);
               return;
            }
         }

         if (this.loadModuleMap.get(ModuleBase.ModuleType.MAP) && !map.isMapFBOLoaded()) {
            String thisModuleString = Text.getText("module.loading.mapFBO");
            if (!this.currentLoad.equals(thisModuleString)) {
               this.currentLoad = thisModuleString;
            } else {
               this.startTime = System.currentTimeMillis();
               map.refreshFullMap();
               this.endTime = System.currentTimeMillis();
               Console.out("Map FBO: " + (this.endTime - this.startTime), true);
            }
         } else {
            for (ModuleBase m : modulesArray) {
               if (m.isInitialized() && m.requiresSync()) {
                  ModuleBase.ModuleType t = m.getModuleType();
                  if (this.loadModuleMap.get(t) && !m.isSynced()) {
                     Text.setVariableText("moduleName", t.getFormattedName());
                     String thisModuleString = Text.getText("module.loading.syncing");
                     if (!this.currentLoad.equals(thisModuleString)) {
                        this.currentLoad = thisModuleString;
                        return;
                     }

                     this.startTime = System.currentTimeMillis();
                     modulesMap.get(t).initLoadSync();
                     this.endTime = System.currentTimeMillis();
                     Console.out(t.getSystemName() + " Module Sync: " + (this.endTime - this.startTime), true);
                     return;
                  }
               }
            }

            String thisModuleString = Text.getText("module.loading.done");
            if (!this.currentLoad.equals(thisModuleString)) {
               this.currentLoad = thisModuleString;
            } else {
               this.modulesLoaded = true;
            }
         }
      }
   }

   @Override
   public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
      g.clear();
      g.setColor(Color.black);
      g.fillRect(-16.0F, -16.0F, Display.getWidth() + 32, Display.getHeight() + 32);
      if (Display.wasResized()) {
         this.updateRenderWindow();
      }
   }

   protected void renderLoadScreen() {
      this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
      if (font.isInitialized()) {
         if (this.currentLoad.equals(Text.getText("module.loading.done"))) {
            font.drawString(
               ScaleControl.getInterfaceWidth() / 2,
               ScaleControl.getInterfaceHeight() / 2 - 40,
               Text.getText("module.loading.starting"),
               Text.FontType.HEADER,
               6,
               true
            );
         } else {
            font.drawString(
               ScaleControl.getInterfaceWidth() / 2,
               ScaleControl.getInterfaceHeight() / 2 - 40,
               Text.getText("module.loading.loading"),
               Text.FontType.HEADER,
               6,
               true
            );
            font.drawString(ScaleControl.getInterfaceWidth() / 2, ScaleControl.getInterfaceHeight() / 2, this.currentLoad, Text.FontType.BODY, 5, true);
         }
      }

      this.loadingLogo.drawCentered(ScaleControl.getInterfaceWidth() / 2, ScaleControl.getInterfaceHeight() / 2 - 110);
      this.loadingPillar.draw(-96.0F, 0.0F);
      this.loadingPillar.draw(ScaleControl.getInterfaceWidth() - this.loadingPillar.getWidth() + 96, 0.0F);
   }

   @Override
   public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
      if (Steamworks.isRunning()) {
         Steamworks.runCallbacks();
      }

      if (this.loadingRequired) {
         this.loadState();
      } else {
         ScaleControl.update();
         this.updateCursor();
         this.oldMouseX = this.mouseX;
         this.oldMouseY = this.mouseY;
         if (!this.lockMouseY) {
            this.mouseX = (int)(input.getMouseX() / ScaleControl.getInterfaceScale());
         }

         if (!this.lockMouseX) {
            this.mouseY = (int)(input.getMouseY() / ScaleControl.getInterfaceScale());
         }

         this.actualMouseX = (int)(input.getMouseX() / ScaleControl.getInterfaceScale());
         this.actualMouseY = (int)(input.getMouseY() / ScaleControl.getInterfaceScale());
         if (!input.isMouseButtonDown(0) || !input.isKeyDown(SettingsParser.getKeyAxisLock())) {
            this.lockMouseX = false;
            this.lockMouseY = false;
         } else if (!this.lockMouseX && !this.lockMouseY) {
            if (Math.abs(this.oldMouseX - this.mouseX) > SettingsParser.getAxisLockBuffer()) {
               this.lockMouseX = true;
            } else if (Math.abs(this.oldMouseY - this.mouseY) > SettingsParser.getAxisLockBuffer()) {
               this.lockMouseY = true;
            }
         }

         if (save.isInitialized() && save.isAutosaving()) {
            save.update();
         } else {
            this.controlsKeyboard();
            this.controlsMouse();
         }
      }
   }

   protected void controlsKeyboard() throws SlickException {
   }

   protected void controlsMouse() throws SlickException {
      if (particle.isInitialized()) {
         if (Utilities.randomInt(8) == 0) {
            particle.newParticleGroup(
               this.getMouseOnMapX() + Utilities.randomInt(4) - 3,
               this.getMouseOnMapY() + Utilities.randomInt(4) - 3,
               ParticleModule.ParticleGroup.MAGIC_SMOKE,
               ParticleModule.ParticleSet.GRAY
            );
         }

         if (Utilities.randomInt(2) == 0) {
            particle.newParticleGroup(
               this.getMouseOnMapX() + Utilities.randomInt(4) - 3,
               this.getMouseOnMapY() + Utilities.randomInt(4) - 3,
               ParticleModule.ParticleGroup.SPARKLES,
               ParticleModule.ParticleSet.MAGIC_GREEN,
               ParticleModule.ParticleSet.MAGIC_GREEN
            );
         }
      }
   }

   protected void updateCursor() throws SlickException {
      try {
         if (input.isMouseButtonDown(0)) {
            Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BROWN_CLICK));
         } else if (input.isMouseButtonDown(1)) {
            Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BROWN_GRAB));
         } else {
            Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BROWN));
         }
      } catch (LWJGLException var2) {
         var2.printStackTrace();
      }
   }

   @Override
   public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
      Game.setCS(this);
      this.loadingRequired = true;
      this.modulesLoaded = false;
      this.initLoad = true;
      this.currentLoad = "";
      this.endTimeTotal = 0L;
      this.startTimeTotal = 0L;
      this.updateRenderWindow();
   }

   public boolean isModulesLoaded() {
      return this.modulesLoaded;
   }

   public void loadCursors() throws LWJGLException, IOException {
      if (this.savedCursorScale != (int)ScaleControl.getInterfaceScale() && (this.savedCursorScale != 3 || (int)ScaleControl.getInterfaceScale() <= 3)) {
         this.savedCursorScale = (int)ScaleControl.getInterfaceScale();
         if (this.savedCursorScale > 3) {
            this.savedCursorScale = 3;
         }

         this.cursors = new HashMap<>();

         StateBase.CursorType[] var4;
         for (StateBase.CursorType c : StateBase.CursorType.values()) {
            Platform.PlatformImage img = Platform.get().readImage(new File("res/GUI/cursors/" + this.savedCursorScale + "/" + c.getFileName() + ".png"));
            int w = img.getWidth();
            int h = img.getHeight();
            int[] rgbData = new int[w * h];

            for (int i = 0; i < rgbData.length; i++) {
               int x = i % w;
               int y = h - 1 - i / w;
               rgbData[i] = img.getRGB(x, y);
            }

            IntBuffer buffer = BufferUtils.createIntBuffer(w * h);
            buffer.put(rgbData);
            ((Buffer)buffer).rewind();
            Cursor cursor = new Cursor(w, h, 2, h - 2, 1, buffer, null);
            this.cursors.put(c, cursor);
         }

         Mouse.setNativeCursor(this.cursors.get(StateBase.CursorType.BROWN));
      }
   }

   @Override
   public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException {
      this.gameSpeed = 1;
      this.resetModules();
   }

   protected void screenShotListener() throws SlickException {
      if (this.takeScreenShot) {
         boolean findNextScreenShot = true;
         int screenShotNumber = 1;
         DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
         Date date = new Date();
         File screenShotFolder = new File("screenShots/");
         if (!screenShotFolder.exists()) {
            screenShotFolder.mkdirs();
         }

         File screenShotFile = new File("screenShots/" + Game.getVersion() + "-" + dateFormat.format(date) + "-" + screenShotNumber + ".png");
         Image screenShot = new Image(gc.getWidth(), gc.getHeight());
         this.g.copyArea(screenShot, 0, 0);

         while (findNextScreenShot) {
            if (screenShotFile.exists() && !screenShotFile.isDirectory()) {
               screenShotFile = new File("screenShots/" + Game.getVersion() + "-" + dateFormat.format(date) + "-" + ++screenShotNumber + ".png");
            } else {
               ImageOut.write(screenShot, screenShotFile.toString(), false);
               findNextScreenShot = false;
            }
         }

         Text.setVariableText("filePath", screenShotFile.toString());
         Console.out(Text.getText("console.panel.screenshot"), false);
         File var8 = null;
         screenShotFile = null;
         screenShot.destroy();
         this.takeScreenShot = false;
         System.gc();
      }
   }

   protected void updateRenderWindow() throws SlickException {
      if (Display.wasResized()) {
         ((AppGameContainer)gc).setDisplayMode(Display.getWidth(), Display.getHeight(), false);
      }

      ScaleControl.setDisplay();
      Game.getCS();
      CloudModule cloud = (CloudModule)getModule(ModuleBase.ModuleType.CLOUD);
      if (cloud.isInitialized()) {
         cloud.reinitFBO();
      }

      try {
         this.loadCursors();
      } catch (IOException | LWJGLException var3) {
         var3.printStackTrace();
      }
   }

   protected void toggleFullScreen() throws SlickException {
      if (!SettingsParser.isFullScreen()) {
         SettingsParser.setFullScreen(true);
      } else {
         SettingsParser.setFullScreen(false);
      }

      Game.reinitDisplay();
   }

   protected void mapWriter() throws SlickException {
      boolean findNextScreenShot = true;
      int screenShotNumber = 1;
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date date = new Date();
      File screenShotFolder = new File("screenShots/");
      if (!screenShotFolder.exists()) {
         screenShotFolder.mkdirs();
      }

      File fullMapFile = new File("screenShots/" + map.getMapName() + "-" + dateFormat.format(date) + "-" + screenShotNumber + ".png");
      File fullMiniMapFile = new File("screenShots/" + map.getMapName() + "-MiniMap-" + dateFormat.format(date) + "-" + screenShotNumber + ".png");

      while (findNextScreenShot) {
         if (fullMapFile.exists() && !fullMapFile.isDirectory()) {
            fullMapFile = new File("screenShots/" + map.getMapName() + "-" + dateFormat.format(date) + "-" + ++screenShotNumber + ".png");
         } else {
            Image screenShot = new Image(4096, 4096);
            Graphics g = screenShot.getGraphics();
            g.drawImage(map.getMapImage(), 0.0F, 0.0F);
            g.drawImage(shadow.getShadowImage(), 0.0F, 0.0F);
            if (this.gameMode != GameModeTemplateBase.GameMode.MAP_EDITOR) {
               if (resource.isInitialized()) {
                  resource.renderResourcesOnGroundMapWriter(g);
               }

               if (mob.isInitialized()) {
                  mob.renderMapWriter();
               }

               object.renderTowersMapWriter();
               object.renderGatesMapWriter();
               if (resource.isInitialized()) {
                  resource.renderResourcesInAirMapWriter();
               }

               lighting.renderMapWriter(g);
            }

            ImageOut.write(screenShot, fullMapFile.toString(), false);
            ImageOut.write(miniMap.getMiniMapFull(), fullMiniMapFile.toString(), false);
            findNextScreenShot = false;
         }
      }

      Text.setVariableText("filePath", fullMapFile.toString());
      Console.out(Text.getText("console.panel.fullMapScreenshot"), false);
      File var10 = null;
      fullMapFile = null;
      fullMiniMapFile = null;
      this.takeScreenShot = false;
      System.gc();
   }

   public boolean isMapMoving() {
      return this.movingMap;
   }

   public float getMouseX() {
      return this.mouseX;
   }

   public float getMouseY() {
      return this.mouseY;
   }

   public int getMouseOnMapX() {
      return (int)Math.floor(map.getMapX() * -1.0F + this.mouseX / ScaleControl.getScalingDifference());
   }

   public int getMouseOnMapY() {
      return (int)Math.floor(map.getMapY() * -1.0F + this.mouseY / ScaleControl.getScalingDifference());
   }

   public int getMouseTileX() {
      return (int)Math.floor(map.getMapX() * -1.0F + this.mouseX / ScaleControl.getScalingDifference()) / 16;
   }

   public int getMouseTileY() {
      return (int)Math.floor(map.getMapY() * -1.0F + this.mouseY / ScaleControl.getScalingDifference()) / 16;
   }

   public float getMouseXPressed() {
      return this.mouseXPressed;
   }

   public float getMouseYPressed() {
      return this.mouseYPressed;
   }

   public Rectangle getMouseRectangle() {
      return this.mouse;
   }

   public GameModeTemplateBase.GameMode getGameMode() {
      return this.gameMode;
   }

   public void setGameMode(GameModeTemplateBase.GameMode gM) {
      this.gameMode = gM;
   }

   public void setGameSpeed(int i) {
      this.gameSpeed = i;
   }

   public void increaseGameSpeed() {
      this.gameSpeed++;
      if (!SettingsParser.getDebug() && this.gameSpeed > SettingsParser.getMaximumGameSpeed()) {
         this.gameSpeed = SettingsParser.getMaximumGameSpeed();
      }

      if (SettingsParser.getDebug() && this.gameSpeed > 32) {
         this.gameSpeed = 32;
      }
   }

   public void decreaseGameSpeed() {
      this.gameSpeed--;
      if (this.gameSpeed < 1) {
         this.gameSpeed = 1;
      }
   }

   public int getGameSpeed() {
      return this.gameSpeed;
   }

   public BrushLoader getBrushLoader() {
      return null;
   }

   public GUIControllerBase getGUI() {
      return null;
   }

   public static GameContainer getGC() {
      return gc;
   }

   public static StateBasedGame getSBG() {
      return sbg;
   }

   public static Input getInput() {
      return input;
   }

   @Override
   public int getID() {
      return this.stateID;
   }

   public static enum CursorType {
      BLACK("cursorBlack"),
      BLACK_CLICK("cursorBlackClick"),
      BLACK_GRAB("cursorBlackGrab"),
      BLACK_X("cursorBlackX"),
      BLACK_CLICK_X("cursorBlackClickX"),
      BLACK_GRAB_X("cursorBlackGrabX"),
      BLACK_STOP("cursorBlackStop"),
      BLACK_CLICK_STOP("cursorBlackClickStop"),
      BLACK_GRAB_STOP("cursorBlackGrabStop"),
      BLACK_PAUSE("cursorBlackPause"),
      BLACK_CLICK_PAUSE("cursorBlackClickPause"),
      BLACK_GRAB_PAUSE("cursorBlackGrabPause"),
      BLUE("cursorBlue"),
      BLUE_CLICK("cursorBlueClick"),
      BLUE_GRAB("cursorBlueGrab"),
      BLUE_X("cursorBlueX"),
      BLUE_CLICK_X("cursorBlueClickX"),
      BLUE_GRAB_X("cursorBlueGrabX"),
      BLUE_STOP("cursorBlueStop"),
      BLUE_CLICK_STOP("cursorBlueClickStop"),
      BLUE_GRAB_STOP("cursorBlueGrabStop"),
      BLUE_PAUSE("cursorBluePause"),
      BLUE_CLICK_PAUSE("cursorBlueClickPause"),
      BLUE_GRAB_PAUSE("cursorBlueGrabPause"),
      BROWN("cursorBrown"),
      BROWN_CLICK("cursorBrownClick"),
      BROWN_GRAB("cursorBrownGrab"),
      BROWN_X("cursorBrownX"),
      BROWN_CLICK_X("cursorBrownClickX"),
      BROWN_GRAB_X("cursorBrownGrabX"),
      BROWN_STOP("cursorBrownStop"),
      BROWN_CLICK_STOP("cursorBrownClickStop"),
      BROWN_GRAB_STOP("cursorBrownGrabStop"),
      BROWN_PAUSE("cursorBrownPause"),
      BROWN_CLICK_PAUSE("cursorBrownClickPause"),
      BROWN_GRAB_PAUSE("cursorBrownGrabPause"),
      RED("cursorRed"),
      RED_CLICK("cursorRedClick"),
      RED_GRAB("cursorRedGrab"),
      RED_X("cursorRedX"),
      RED_CLICK_X("cursorRedClickX"),
      RED_GRAB_X("cursorRedGrabX"),
      RED_STOP("cursorRedStop"),
      RED_CLICK_STOP("cursorRedClickStop"),
      RED_GRAB_STOP("cursorRedGrabStop"),
      RED_PAUSE("cursorRedPause"),
      RED_CLICK_PAUSE("cursorRedClickPause"),
      RED_GRAB_PAUSE("cursorRedGrabPause");

      private final String fileName;

      private CursorType(String fileName) {
         this.fileName = fileName;
      }

      public String getFileName() {
         return this.fileName;
      }
   }

   public static enum GameType {
      WORLD_MAP("worldMap"),
      SKIRMISH("skirmish");

      private final String gameTypeText;

      private GameType(String gameTypeText) {
         this.gameTypeText = gameTypeText;
      }

      public String getText() {
         return this.gameTypeText;
      }
   }
}
