package rtr.system;

import rtr.platform.Platform;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.states.WorldMapGUIData;
import rtr.save.SaveModule;
import rtr.states.MainMenuState;
import rtr.states.MapEditorState;
import rtr.states.PlayState;
import rtr.states.StateBase;
import rtr.states.WorldMapState;
import rtr.system.threadpool.RTRThreadPool;
import rtr.utilities.Steamworks;

public class Game extends StateBasedGame {
   private static AppGameContainer appgc;
   private static final String ICONS = "res/icon.png";
   private static final String GAME_VERSION = "Update 2d";
   private static final String SETTINGS_VERSION = "InDev 29c";
   private static final String GAME_NAME = "Rise to Ruins Update 2d";
   public static final int TARGET_FRAME_RATE = 60;
   private static MainMenuState mainMenuState;
   private static int mainMenuStateID = 1;
   private static MapEditorState mapEditorState;
   private static int mapEditorStateID = 2;
   private static PlayState playState;
   private static int playStateID = 3;
   private static WorldMapState worldMapState;
   private static int worldMapStateID = 4;
   private static StateBase currentState;
   private static int activeProfile = -1;
   private static long startTime = 0L;
   private static boolean nonSteamMode = true;
   private static ArrayList<String> validGameVersions;

   public static boolean isNoneSteamMode() {
      return nonSteamMode;
   }

   public Game(String gamename) {
      super(gamename);
      validGameVersions = new ArrayList<>();
      validGameVersions.add("Update 2c");
      validGameVersions.add("Update 2b");
      validGameVersions.add("Update 2");
      validGameVersions.add("Update 2 Unstable 4c");
      validGameVersions.add("Update 2 Unstable 4b");
      validGameVersions.add("Update 2 Unstable 4");
      validGameVersions.add("Update 2 Unstable 4 PR3");
      validGameVersions.add("Update 2 Unstable 4 PR2");
      validGameVersions.add("Update 2 Unstable 4 PR1");
      validGameVersions.add("Update 2 Unstable 3c");
      validGameVersions.add("Update 2 Unstable 3b");
      validGameVersions.add("Update 2 Unstable 3");
      validGameVersions.add("Update 2 Unstable 3 PR2");
      validGameVersions.add("Update 2 Unstable 3 PR1b");
      validGameVersions.add("Update 2 Unstable 3 PR1");
      validGameVersions.add("Update 2 Unstable 2b");
      validGameVersions.add("Update 2 Unstable 2");
      validGameVersions.add("Update 2 Unstable 2 PR Test 3c");
      validGameVersions.add("Update 2 Unstable 2 PR Test 3b");
      validGameVersions.add("Update 2 Unstable 2 PR Test 3");
      validGameVersions.add("Update 2 Unstable 2 PR Test 2");
      validGameVersions.add("Update 2 Unstable 2 PR Test 1");
      validGameVersions.add("Update 2 Unstable 1");
      validGameVersions.add("Update 2 Unstable 1 PR Test 3");
      validGameVersions.add("Update 2 Unstable 1 PR Test 2");
      validGameVersions.add("Update 2 Unstable 1 PR Test");
      validGameVersions.add("Update 1c");
      validGameVersions.add("Update 1b");
      validGameVersions.add("Update 1b PR");
      validGameVersions.add("Update 1");
      validGameVersions.add("Update 1 Unstable 2");
      validGameVersions.add("Update 1 Unstable 2 PR Test");
      validGameVersions.add("Update 1 Unstable 1b");
      validGameVersions.add("Update 1 Unstable 1");
      validGameVersions.add("Release 1b");
      validGameVersions.add("Release 1");
      validGameVersions.add("InDev 34b");
      validGameVersions.add("InDev 34");
      validGameVersions.add("InDev 34 Unstable 4");
      validGameVersions.add("InDev 34 Unstable 3");
      validGameVersions.add("InDev 34 Unstable 3 PR Test");
      validGameVersions.add("InDev 34 Unstable 2b");
      validGameVersions.add("InDev 34 Unstable 2");
      validGameVersions.add("InDev 34 Unstable 2 PR Test 3");
      validGameVersions.add("InDev 34 Unstable 2 PR Test 2");
      validGameVersions.add("InDev 34 Unstable 2 PR");
      validGameVersions.add("InDev 34 Unstable 1");
      validGameVersions.add("InDev 34 Unstable 1 PR Save Test 3");
      validGameVersions.add("InDev 34 Unstable 1 PR Save Test 2");
      validGameVersions.add("InDev 34 Unstable 1 PR Save Test");
      validGameVersions.add("InDev 33f");
      validGameVersions.add("InDev 33e");
      validGameVersions.add("InDev 33d");
      validGameVersions.add("InDev 33c");
      validGameVersions.add("InDev 33c MEMTEST");
      validGameVersions.add("InDev 33b");
      validGameVersions.add("InDev 33");
      validGameVersions.add("InDev 33 Unstable 5");
      validGameVersions.add("InDev 33 Unstable 4b");
      validGameVersions.add("InDev 33 Unstable 4");
      validGameVersions.add("InDev 33 Unstable 3b");
      validGameVersions.add("InDev 33 Unstable 3");
      validGameVersions.add("InDev 33 Unstable 2b");
      validGameVersions.add("InDev 33 Unstable 2");
      validGameVersions.add("InDev 33 Unstable 1");
      validGameVersions.add("InDev 33 U1 PR Save Test 6");
      validGameVersions.add("InDev 33 U1 PR Save Test 5");
      validGameVersions.add("InDev 33 U1 PR Save Test 4");
      validGameVersions.add("InDev 33 U1 PR Save Test 3");
      validGameVersions.add("InDev 33 U1 PR Save Test 2");
      validGameVersions.add("InDev 33 U1 PR Save Test");
      validGameVersions.add("Update 2d");
      mainMenuState = new MainMenuState(mainMenuStateID);
      this.addState(mainMenuState);
      mapEditorState = new MapEditorState(mapEditorStateID);
      this.addState(mapEditorState);
      playState = new PlayState(playStateID);
      this.addState(playState);
      worldMapState = new WorldMapState(worldMapStateID);
      this.addState(worldMapState);
   }

   @Override
   public void initStatesList(GameContainer gc) throws SlickException {
      this.enterState(mainMenuStateID);
      gc.setShowFPS(false);
   }

   public static void launchGame() throws LWJGLException, SlickException {
      SettingsParser.loadBaseSettings();
      System.out.println("Rise to Ruins Update 2d");
      System.out.println();
      System.out.println("Attempting to initialize Steamworks");
      Steamworks.initializeSteam();
      Text.initializeLanguage();
      RTRThreadPool.startThreadPool();
      ScaleControl.initDisplay();

      try {
         System.out.println("Operating System: " + System.getProperty("os.name"));
         System.out.println("GPU Drivers: " + Display.getAdapter());
         System.out.println("GPU Driver Version: " + Display.getVersion());
         System.out.println("Maximum JVM Memory: " + Runtime.getRuntime().maxMemory() / 1048576L + "MB");
         appgc = new AppGameContainer(new Game("Rise to Ruins Update 2d"));
         reinitDisplay();
         appgc.setIcon("res/icon.png");
         appgc.setVSync(SettingsParser.getVSync());
         appgc.setTargetFrameRate(60);
         appgc.setClearEachFrame(false);
         appgc.setAlwaysRender(true);
         appgc.setMultiSample(0);
         startTime = System.nanoTime();
         appgc.start();
      } catch (SlickException var1) {
         var1.printStackTrace();
      }
   }

   public static void reinitDisplay() throws SlickException {
      if (SettingsParser.isFullScreen()) {
         if (SettingsParser.isBorderlessFullScreen()) {
            if (System.getProperty("org.lwjgl.opengl.Window.undecorated") != null && System.getProperty("org.lwjgl.opengl.Window.undecorated").equals("true")) {
               System.out.println("Reinitializing Display Mode Skipped: Borderless Full Screen Already Enabled");
            } else {
               System.out.println("Initializing Display Mode: Borderless Full Screen");
               System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
               Display.setLocation(0, 0);
               appgc.setDisplayMode(Platform.get().getScreenWidth(), Platform.get().getScreenHeight(), false, true);
            }
         } else {
            System.out.println("Initializing Display Mode: Full Screen");
            int setWidth = SettingsParser.getFullScreenResolutionW();
            int setHeight = SettingsParser.getFullScreenResolutionH();

            try {
               DisplayMode[] displayModes = Display.getAvailableDisplayModes();

               for (DisplayMode d : displayModes) {
                  if (d.getWidth() == setWidth && d.getHeight() == setHeight) {
                     System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                     appgc.setDisplayMode(setWidth, setHeight, true, true);
                     Display.setResizable(false);
                     return;
                  }
               }

               System.out.println("Set full screen resolution not valid, resetting to default.");
               SettingsParser.setFullScreenResolution(Platform.get().getScreenWidth(), Platform.get().getScreenHeight());
               System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
               appgc.setDisplayMode(Platform.get().getScreenWidth(), Platform.get().getScreenHeight(), true, true);
               Display.setResizable(false);
            } catch (LWJGLException var8) {
               var8.printStackTrace();
            }
         }
      } else {
         System.out.println("Initializing Display Mode: Windowed");
         System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
         Display.setLocation(50, 50);
         appgc.setDisplayMode(Platform.get().getScreenWidth() - 100, Platform.get().getScreenHeight() - 200, false, true);
         Display.setResizable(true);
      }
   }

   @Override
   public boolean closeRequested() {
      try {
         if (currentState.isModulesLoaded()) {
            System.out.println("Exit detected, saving game if required.");
            SaveModule save = (SaveModule)StateBase.getModule(ModuleBase.ModuleType.SAVE);
            if (currentState.getID() == playStateID) {
               save.saveFullGame();
            }

            if (currentState.getID() == worldMapStateID && WorldMapGUIData.getSelectedGameMode() != null) {
               save.getActiveWorldSave().fullSaveSingleThreaded();
            }
         } else {
            System.out.println("Exit detected, modules not loaded so the game was not saved.");
         }

         ShutdownHook.setSafeShutdown(true);
         if (Steamworks.isRunning()) {
            Steamworks.shutdownApi();
         }
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

      Steamworks.shutdownSteam();
      System.exit(0);
      return false;
   }

   public static String getVersion() {
      return "Update 2d";
   }

   public static String getSettingsVersion() {
      return "InDev 29c";
   }

   public static int getActiveProfile() {
      return activeProfile;
   }

   public static void setActiveProfile(int aP) {
      activeProfile = aP;
   }

   public static void setCS(StateBase s) {
      currentState = s;
   }

   public static StateBase getCS() {
      return currentState;
   }

   public static int getCSID() {
      return currentState.getID();
   }

   public static MainMenuState getMainMenuState() {
      return mainMenuState;
   }

   public static MapEditorState getMapEditorState() {
      return mapEditorState;
   }

   public static PlayState getPlayState() {
      return playState;
   }

   public static WorldMapState getWorldMapState() {
      return worldMapState;
   }

   public static ArrayList<String> getValidGameVersions() {
      return validGameVersions;
   }

   public static boolean isValidGameVersion(String s) {
      for (String v : validGameVersions) {
         if (v.equals(s)) {
            return true;
         }
      }

      return false;
   }

   public static long getStartTimeInSeconds() {
      return TimeUnit.NANOSECONDS.toSeconds(startTime);
   }
}
