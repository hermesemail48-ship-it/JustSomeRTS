package rtr;

import rtr.platform.Platform;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.lwjgl.input.Keyboard;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class SettingsParser {
   private static int keyMapUp;
   private static int keyMapDown;
   private static int keyMapLeft;
   private static int keyMapRight;
   private static int keyZoomIn;
   private static int keyZoomOut;
   private static int keySpeedUp;
   private static int keySpeedDown;
   private static int keyMinimap;
   private static int keyCycleBrushType;
   private static int keyBrushSizeUp;
   private static int keyBrushSizeDown;
   private static int keyDebug;
   private static int keyScreenShot;
   private static int keyHideGUI;
   private static int keyGrid;
   private static int keyHideTopography;
   private static int keyFullScreen;
   private static int keyPopulationList;
   private static int keyMainMenu;
   private static int keyMapInformation;
   private static int keyDataViews;
   private static int keyProblemPanel;
   private static int keyBuildingList;
   private static int keyCancel;
   private static int keyAccentModeToggle;
   private static int keyEraseTool;
   private static int keyPatchTool;
   private static int keyHoleTool;
   private static int keyPause;
   private static int keyPauseObjects;
   private static int keyDismantleObjects;
   private static int keyUpgrade;
   private static int keyStartUpgrade2;
   private static int keyStartUpgrade3;
   private static int keyGrabSpell;
   private static int keyActiveSpell1;
   private static int keyActiveSpell2;
   private static int keyActiveSpell3;
   private static int keyActiveSpell4;
   private static int keyActiveSpell5;
   private static int keyHarvestWood;
   private static int keyHarvestRock;
   private static int keyHarvestFoodAndWater;
   private static int keyHarvestCrystals;
   private static int keyDestroyTerrain;
   private static int keyAxisLock;
   private static int keyUnassign;
   private static boolean fullScreen;
   private static boolean borderlessFullScreen;
   private static int fullScreenResolutionW;
   private static int fullScreenResolutionH;
   private static boolean vSync;
   private static boolean tipsEnabled;
   private static boolean shadows;
   private static boolean clouds;
   private static boolean background;
   private static boolean debug;
   private static boolean allowOfficial;
   private static boolean useBannerMessage;
   private static boolean verboseConsole;
   private static boolean mapScrollInverted;
   private static boolean edgeScrollingEnabled;
   private static boolean screenshakeEnabled;
   private static boolean hideDisclaimer;
   private static boolean mainMenuDrift;
   private static boolean offlineMode;
   private static boolean mouseWheelZoomingEnabled;
   private static boolean retroParticles;
   private static int maximumGameSpeed;
   private static String bannerMessage1 = "";
   private static String bannerMessage2 = "";
   private static int axisLockBuffer = 1;
   private static int edgeScrollSpeed = 75;
   private static float forceInterfaceScale = -1.0F;
   private static SettingsParser.ParticleAmount particleAmount;
   private static SettingsParser.AutosaveFrequency autosaveFrequency;
   private static int musicVolume = 8;
   private static int soundVolume = 15;
   private static int lastProfile = -1;
   private static GameModeTemplateBase.GameMode lastGameMode;
   private static String twitchChannel = "";
   private static String language = "";
   private static Properties settingsProperties = new Properties();
   private static Properties profileProperties = new Properties();
   private static File profilesFolder = new File("profiles/");
   private static File baseSettings = new File("profiles/settings.properties");
   private static File profileFolder;
   private static File profileSettings;
   private static InputStream in;
   private static FileOutputStream out;

   /** Settings a port applies once over the player's base settings (null on desktop). */
   public static Properties platformDefaults;

   public static void loadBaseSettings() {
      if (!profilesFolder.exists()) {
         profilesFolder.mkdir();
      }

      try {
         in = new FileInputStream(baseSettings);
         settingsProperties.load(in);
         checkBaseSettings();
         in.close();
      } catch (IOException var1) {
         checkBaseSettings();
         System.out.println("Failed to load graphics properties file, resetting to default!");
      }

      refreshBaseProperties();
   }

   public static void loadProfileSettings(int activeProfile) {
      Game.setActiveProfile(activeProfile);

      try {
         profileProperties.clear();
         profileFolder = new File("profiles/profile" + activeProfile + "/");
         if (!profileFolder.exists()) {
            profileFolder.mkdir();
         }

         profileSettings = new File("profiles/profile" + activeProfile + "/profileSettings.properties");
         in = new FileInputStream(profileSettings);
         profileProperties.load(in);
         checkProfileSettings();
         in.close();
      } catch (IOException var2) {
         checkProfileSettings();
         System.out.println("Failed to load profile properties file, resetting to default!");
      }

      refreshProfileProperties();
   }

   public static void createProfileSettings(int activeProfile, String twitchChannel, boolean tipsEnabled) {
      Game.setActiveProfile(activeProfile);

      try {
         profileProperties.clear();
         profileFolder = new File("profiles/profile" + activeProfile + "/");
         if (!profileFolder.exists()) {
            profileFolder.mkdir();
         }

         profileSettings = new File("profiles/profile" + activeProfile + "/profileSettings.properties");
         in = new FileInputStream(profileSettings);
         profileProperties.load(in);
         checkProfileSettings(twitchChannel, tipsEnabled);
         in.close();
      } catch (IOException var4) {
         checkProfileSettings(twitchChannel, tipsEnabled);
         System.out.println("Failed to load profile properties file, resetting to default!");
      }

      refreshProfileProperties();
   }

   public static void saveBaseSettings() {
      try {
         out = new FileOutputStream(baseSettings);
         settingsProperties.store(out, "settings");
         out.flush();
         out.close();
      } catch (IOException var1) {
         System.out.println("Failed to save base properties file!");
      }
   }

   public static void saveProfileSettings() {
      try {
         out = new FileOutputStream(profileSettings);
         profileProperties.store(out, "settings");
         out.flush();
         out.close();
      } catch (IOException var1) {
         System.out.println("Failed to save profile properties file!");
      }
   }

   public static void refreshBaseProperties() {
      fullScreen = Boolean.parseBoolean(settingsProperties.getProperty("fullScreen"));
      borderlessFullScreen = Boolean.parseBoolean(settingsProperties.getProperty("borderlessFullScreen"));
      fullScreenResolutionW = Integer.parseInt(settingsProperties.getProperty("fullScreenResolutionW"));
      fullScreenResolutionH = Integer.parseInt(settingsProperties.getProperty("fullScreenResolutionH"));
      vSync = Boolean.parseBoolean(settingsProperties.getProperty("vSync"));
      debug = Boolean.parseBoolean(settingsProperties.getProperty("debug", "false"));
      allowOfficial = Boolean.parseBoolean(settingsProperties.getProperty("allowOfficial", "false"));
      verboseConsole = Boolean.parseBoolean(settingsProperties.getProperty("verboseConsole", "false"));
      useBannerMessage = Boolean.parseBoolean(settingsProperties.getProperty("useBannerMessage", "false"));
      bannerMessage1 = settingsProperties.getProperty("bannerMessage1", "");
      bannerMessage2 = settingsProperties.getProperty("bannerMessage2", "");
      retroParticles = Boolean.parseBoolean(settingsProperties.getProperty("retroParticles"));
      musicVolume = Integer.parseInt(settingsProperties.getProperty("musicVolume"));
      soundVolume = Integer.parseInt(settingsProperties.getProperty("soundVolume"));
      lastProfile = Integer.parseInt(settingsProperties.getProperty("lastProfile"));
      forceInterfaceScale = Float.parseFloat(settingsProperties.getProperty("forceInterfaceScale"));
      int pAmount = Integer.parseInt(settingsProperties.getProperty("particleAmount"));
      if (pAmount == 0) {
         particleAmount = SettingsParser.ParticleAmount.MINIMAL;
      } else if (pAmount == 1) {
         particleAmount = SettingsParser.ParticleAmount.REDUCED;
      } else if (pAmount == 2) {
         particleAmount = SettingsParser.ParticleAmount.FULL;
      }

      shadows = Boolean.parseBoolean(settingsProperties.getProperty("shadows"));
      int aFreq = Integer.parseInt(settingsProperties.getProperty("autosaveFrequency"));
      if (aFreq == 0) {
         autosaveFrequency = SettingsParser.AutosaveFrequency.MINIMAL;
      } else if (aFreq == 1) {
         autosaveFrequency = SettingsParser.AutosaveFrequency.REDUCED;
      } else if (aFreq == 2) {
         autosaveFrequency = SettingsParser.AutosaveFrequency.FULL;
      }

      clouds = Boolean.parseBoolean(settingsProperties.getProperty("clouds"));
      background = Boolean.parseBoolean(settingsProperties.getProperty("background"));
      hideDisclaimer = Boolean.parseBoolean(settingsProperties.getProperty("hideDisclaimer"));
      maximumGameSpeed = Integer.parseInt(settingsProperties.getProperty("maximumGameSpeed"));
      mainMenuDrift = Boolean.parseBoolean(settingsProperties.getProperty("mainMenuDrift"));
      offlineMode = Boolean.parseBoolean(settingsProperties.getProperty("offlineMode"));
      mouseWheelZoomingEnabled = Boolean.parseBoolean(settingsProperties.getProperty("mouseWheelZoomingEnabled"));
      axisLockBuffer = Integer.parseInt(settingsProperties.getProperty("axisLockBuffer"));
      language = settingsProperties.getProperty("language");
   }

   public static void refreshProfileProperties() {
      keyMapUp = Keyboard.getKeyIndex(profileProperties.getProperty("keyMapUp").toUpperCase());
      keyMapDown = Keyboard.getKeyIndex(profileProperties.getProperty("keyMapDown").toUpperCase());
      keyMapLeft = Keyboard.getKeyIndex(profileProperties.getProperty("keyMapLeft").toUpperCase());
      keyMapRight = Keyboard.getKeyIndex(profileProperties.getProperty("keyMapRight").toUpperCase());
      keyZoomIn = Keyboard.getKeyIndex(profileProperties.getProperty("keyZoomIn").toUpperCase());
      keyZoomOut = Keyboard.getKeyIndex(profileProperties.getProperty("keyZoomOut").toUpperCase());
      keySpeedUp = Keyboard.getKeyIndex(profileProperties.getProperty("keySpeedUp").toUpperCase());
      keySpeedDown = Keyboard.getKeyIndex(profileProperties.getProperty("keySpeedDown").toUpperCase());
      keyMinimap = Keyboard.getKeyIndex(profileProperties.getProperty("keyMinimap").toUpperCase());
      keyCycleBrushType = Keyboard.getKeyIndex(profileProperties.getProperty("keyCycleBrushType").toUpperCase());
      keyBrushSizeUp = Keyboard.getKeyIndex(profileProperties.getProperty("keyBrushSizeUp").toUpperCase());
      keyBrushSizeDown = Keyboard.getKeyIndex(profileProperties.getProperty("keyBrushSizeDown").toUpperCase());
      keyDebug = Keyboard.getKeyIndex(profileProperties.getProperty("keyDebug").toUpperCase());
      keyScreenShot = Keyboard.getKeyIndex(profileProperties.getProperty("keyScreenShot").toUpperCase());
      keyHideGUI = Keyboard.getKeyIndex(profileProperties.getProperty("keyHideGUI").toUpperCase());
      keyGrid = Keyboard.getKeyIndex(profileProperties.getProperty("keyGrid").toUpperCase());
      keyHideTopography = Keyboard.getKeyIndex(profileProperties.getProperty("keyHideTopography").toUpperCase());
      keyFullScreen = Keyboard.getKeyIndex(profileProperties.getProperty("keyFullScreen").toUpperCase());
      keyPopulationList = Keyboard.getKeyIndex(profileProperties.getProperty("keyPopulationList").toUpperCase());
      keyMainMenu = Keyboard.getKeyIndex(profileProperties.getProperty("keyMainMenu").toUpperCase());
      keyMapInformation = Keyboard.getKeyIndex(profileProperties.getProperty("keyMapInformation").toUpperCase());
      keyDataViews = Keyboard.getKeyIndex(profileProperties.getProperty("keyDataViews").toUpperCase());
      keyProblemPanel = Keyboard.getKeyIndex(profileProperties.getProperty("keyProblemPanel").toUpperCase());
      keyBuildingList = Keyboard.getKeyIndex(profileProperties.getProperty("keyBuildingList").toUpperCase());
      keyCancel = Keyboard.getKeyIndex(profileProperties.getProperty("keyCancel").toUpperCase());
      keyAccentModeToggle = Keyboard.getKeyIndex(profileProperties.getProperty("keyAccentModeToggle").toUpperCase());
      keyEraseTool = Keyboard.getKeyIndex(profileProperties.getProperty("keyEraseTool").toUpperCase());
      keyPatchTool = Keyboard.getKeyIndex(profileProperties.getProperty("keyPatchTool").toUpperCase());
      keyHoleTool = Keyboard.getKeyIndex(profileProperties.getProperty("keyHoleTool").toUpperCase());
      keyPause = Keyboard.getKeyIndex(profileProperties.getProperty("keyPause").toUpperCase());
      keyGrabSpell = Keyboard.getKeyIndex(profileProperties.getProperty("keyGrabSpell").toUpperCase());
      keyPauseObjects = Keyboard.getKeyIndex(profileProperties.getProperty("keyPauseObjects").toUpperCase());
      keyDismantleObjects = Keyboard.getKeyIndex(profileProperties.getProperty("keyDismantleObjects").toUpperCase());
      keyUpgrade = Keyboard.getKeyIndex(profileProperties.getProperty("keyUpgrade").toUpperCase());
      keyStartUpgrade2 = Keyboard.getKeyIndex(profileProperties.getProperty("keyStartUpgrade2").toUpperCase());
      keyStartUpgrade3 = Keyboard.getKeyIndex(profileProperties.getProperty("keyStartUpgrade3").toUpperCase());
      keyActiveSpell1 = Keyboard.getKeyIndex(profileProperties.getProperty("keyActiveSpell1").toUpperCase());
      keyActiveSpell2 = Keyboard.getKeyIndex(profileProperties.getProperty("keyActiveSpell2").toUpperCase());
      keyActiveSpell3 = Keyboard.getKeyIndex(profileProperties.getProperty("keyActiveSpell3").toUpperCase());
      keyActiveSpell4 = Keyboard.getKeyIndex(profileProperties.getProperty("keyActiveSpell4").toUpperCase());
      keyActiveSpell5 = Keyboard.getKeyIndex(profileProperties.getProperty("keyActiveSpell5").toUpperCase());
      keyHarvestWood = Keyboard.getKeyIndex(profileProperties.getProperty("keyHarvestWood").toUpperCase());
      keyHarvestRock = Keyboard.getKeyIndex(profileProperties.getProperty("keyHarvestRock").toUpperCase());
      keyHarvestFoodAndWater = Keyboard.getKeyIndex(profileProperties.getProperty("keyHarvestFoodAndWater").toUpperCase());
      keyHarvestCrystals = Keyboard.getKeyIndex(profileProperties.getProperty("keyHarvestCrystals").toUpperCase());
      keyDestroyTerrain = Keyboard.getKeyIndex(profileProperties.getProperty("keyDestroyTerrain").toUpperCase());
      tipsEnabled = Boolean.parseBoolean(profileProperties.getProperty("tipsEnabled"));
      screenshakeEnabled = Boolean.parseBoolean(profileProperties.getProperty("screenshakeEnabled"));
      mapScrollInverted = Boolean.parseBoolean(profileProperties.getProperty("mapScrollInverted", "false"));
      String gM = profileProperties.getProperty("lastGameMode", "null");
      lastGameMode = null;

      GameModeTemplateBase.GameMode[] var4;
      for (GameModeTemplateBase.GameMode m : GameModeTemplateBase.GameMode.values()) {
         if (m.toString().equals(gM)) {
            lastGameMode = m;
         }
      }

      twitchChannel = profileProperties.getProperty("twitchChannel", "");
      language = profileProperties.getProperty("language", "defaultEnglish");
      edgeScrollingEnabled = Boolean.parseBoolean(profileProperties.getProperty("edgeScrollingEnabled", "false"));
      edgeScrollSpeed = Integer.parseInt(profileProperties.getProperty("edgeScrollSpeed", "80"));
      keyAxisLock = Keyboard.getKeyIndex(profileProperties.getProperty("keyAxisLock").toUpperCase());
      keyUnassign = Keyboard.getKeyIndex(profileProperties.getProperty("keyUnassign").toUpperCase());
   }

   private static void checkBaseSettings() {
      if (!settingsProperties.getProperty("version", "-1").equals(Game.getSettingsVersion())) {
         settingsProperties = new Properties();
         settingsProperties.setProperty("version", Game.getSettingsVersion());
         System.out.println("Base settings file no longer valid, resetting!");
      }

      if (platformDefaults != null && !settingsProperties.containsKey("platformDefaultsApplied")) {
         // Applied once (e.g. lighter effects on phones); the player can change them in Settings afterwards.
         settingsProperties.putAll(platformDefaults);
         settingsProperties.setProperty("platformDefaultsApplied", "true");
      }

      if (!settingsProperties.containsKey("fullScreen")) {
         settingsProperties.setProperty("fullScreen", "false");
      }

      if (!settingsProperties.containsKey("borderlessFullScreen")) {
         if (System.getProperty("os.name").toLowerCase().contains("win")) {
            settingsProperties.setProperty("borderlessFullScreen", "true");
         } else {
            settingsProperties.setProperty("borderlessFullScreen", "false");
         }
      }

      if (!settingsProperties.containsKey("fullScreenResolutionW") || !settingsProperties.containsKey("fullScreenResolutionH")) {
         settingsProperties.setProperty("fullScreenResolutionW", Integer.toString(Platform.get().getScreenWidth()));
         settingsProperties.setProperty("fullScreenResolutionH", Integer.toString(Platform.get().getScreenHeight()));
      }

      if (!settingsProperties.containsKey("vSync")) {
         settingsProperties.setProperty("vSync", "true");
      }

      if (!settingsProperties.containsKey("retroParticles")) {
         settingsProperties.setProperty("retroParticles", "false");
      }

      if (!settingsProperties.containsKey("musicVolume")) {
         settingsProperties.setProperty("musicVolume", "8");
      }

      if (!settingsProperties.containsKey("soundVolume")) {
         settingsProperties.setProperty("soundVolume", "15");
      }

      if (!settingsProperties.containsKey("lastProfile")) {
         settingsProperties.setProperty("lastProfile", "-1");
      }

      if (!settingsProperties.containsKey("forceInterfaceScale")) {
         settingsProperties.setProperty("forceInterfaceScale", "-1");
      }

      if (!settingsProperties.containsKey("particleAmount")) {
         settingsProperties.setProperty("particleAmount", "2");
      }

      if (!settingsProperties.containsKey("shadows")) {
         settingsProperties.setProperty("shadows", "true");
      }

      if (!settingsProperties.containsKey("autosaveFrequency")) {
         settingsProperties.setProperty("autosaveFrequency", "2");
      }

      if (!settingsProperties.containsKey("clouds")) {
         settingsProperties.setProperty("clouds", "true");
      }

      if (!settingsProperties.containsKey("background")) {
         settingsProperties.setProperty("background", "true");
      }

      if (!settingsProperties.containsKey("mapScrollInverted")) {
         settingsProperties.setProperty("mapScrollInverted", "false");
      }

      if (!settingsProperties.containsKey("hideDisclaimer")) {
         settingsProperties.setProperty("hideDisclaimer", "false");
      }

      if (!settingsProperties.containsKey("maximumGameSpeed")) {
         settingsProperties.setProperty("maximumGameSpeed", "3");
      }

      if (!settingsProperties.containsKey("mainMenuDrift")) {
         settingsProperties.setProperty("mainMenuDrift", "true");
      }

      if (!settingsProperties.containsKey("offlineMode")) {
         settingsProperties.setProperty("offlineMode", "false");
      }

      if (!settingsProperties.containsKey("mouseWheelZoomingEnabled")) {
         settingsProperties.setProperty("mouseWheelZoomingEnabled", "true");
      }

      if (!settingsProperties.containsKey("axisLockBuffer")) {
         settingsProperties.setProperty("axisLockBuffer", "1");
      }

      if (!settingsProperties.containsKey("language")) {
         settingsProperties.setProperty("language", "defaultEnglish");
      }

      saveBaseSettings();
   }

   private static void checkProfileSettings() {
      checkProfileSettings("", true);
   }

   private static void checkProfileSettings(String twitchChannel, boolean tipsEnabled) {
      if (!profileProperties.getProperty("version", "-1").equals(Game.getSettingsVersion())) {
         profileProperties = new Properties();
         profileProperties.setProperty("version", Game.getSettingsVersion());
         System.out.println("Profile settings file no longer valid, resetting!");
      }

      if (!profileProperties.containsKey("keyMapUp")) {
         profileProperties.setProperty("keyMapUp", "w");
      }

      if (!profileProperties.containsKey("keyMapDown")) {
         profileProperties.setProperty("keyMapDown", "s");
      }

      if (!profileProperties.containsKey("keyMapLeft")) {
         profileProperties.setProperty("keyMapLeft", "a");
      }

      if (!profileProperties.containsKey("keyMapRight")) {
         profileProperties.setProperty("keyMapRight", "d");
      }

      if (!profileProperties.containsKey("keyZoomIn")) {
         profileProperties.setProperty("keyZoomIn", "rbracket");
      }

      if (!profileProperties.containsKey("keyZoomOut")) {
         profileProperties.setProperty("keyZoomOut", "lbracket");
      }

      if (!profileProperties.containsKey("keySpeedUp")) {
         profileProperties.setProperty("keySpeedUp", "f6");
      }

      if (!profileProperties.containsKey("keySpeedDown")) {
         profileProperties.setProperty("keySpeedDown", "f5");
      }

      if (!profileProperties.containsKey("keyMinimap")) {
         profileProperties.setProperty("keyMinimap", "tab");
      }

      if (!profileProperties.containsKey("keyCycleBrushType")) {
         profileProperties.setProperty("keyCycleBrushType", "z");
      }

      if (!profileProperties.containsKey("keyBrushSizeUp")) {
         profileProperties.setProperty("keyBrushSizeUp", "period");
      }

      if (!profileProperties.containsKey("keyBrushSizeDown")) {
         profileProperties.setProperty("keyBrushSizeDown", "comma");
      }

      if (!profileProperties.containsKey("keyDebug")) {
         profileProperties.setProperty("keyDebug", "f3");
      }

      if (!profileProperties.containsKey("keyScreenShot")) {
         profileProperties.setProperty("keyScreenShot", "f2");
      }

      if (!profileProperties.containsKey("keyHideGUI")) {
         profileProperties.setProperty("keyHideGUI", "f4");
      }

      if (!profileProperties.containsKey("keyGrid")) {
         profileProperties.setProperty("keyGrid", "g");
      }

      if (!profileProperties.containsKey("keyHideTopography")) {
         profileProperties.setProperty("keyHideTopography", "t");
      }

      if (!profileProperties.containsKey("keyFullScreen")) {
         profileProperties.setProperty("keyFullScreen", "f10");
      }

      if (!profileProperties.containsKey("keyPopulationList")) {
         profileProperties.setProperty("keyPopulationList", "p");
      }

      if (!profileProperties.containsKey("keyMainMenu")) {
         profileProperties.setProperty("keyMainMenu", "m");
      }

      if (!profileProperties.containsKey("keyMapInformation")) {
         profileProperties.setProperty("keyMapInformation", "i");
      }

      if (!profileProperties.containsKey("keyDataViews")) {
         profileProperties.setProperty("keyDataViews", "v");
      }

      if (!profileProperties.containsKey("keyProblemPanel")) {
         profileProperties.setProperty("keyProblemPanel", "r");
      }

      if (!profileProperties.containsKey("keyBuildingList")) {
         profileProperties.setProperty("keyBuildingList", "l");
      }

      if (!profileProperties.containsKey("keyCancel")) {
         profileProperties.setProperty("keyCancel", "escape");
      }

      if (!profileProperties.containsKey("keyAccentModeToggle")) {
         profileProperties.setProperty("keyAccentModeToggle", "x");
      }

      if (!profileProperties.containsKey("keyEraseTool")) {
         profileProperties.setProperty("keyEraseTool", "e");
      }

      if (!profileProperties.containsKey("keyPatchTool")) {
         profileProperties.setProperty("keyPatchTool", "p");
      }

      if (!profileProperties.containsKey("keyHoleTool")) {
         profileProperties.setProperty("keyHoleTool", "h");
      }

      if (!profileProperties.containsKey("keyPause")) {
         profileProperties.setProperty("keyPause", "space");
      }

      if (!profileProperties.containsKey("keyPauseObjects")) {
         profileProperties.setProperty("keyPauseObjects", "o");
      }

      if (!profileProperties.containsKey("keyDismantleObjects")) {
         profileProperties.setProperty("keyDismantleObjects", "");
      }

      if (!profileProperties.containsKey("keyUpgrade")) {
         profileProperties.setProperty("keyUpgrade", "");
      }

      if (!profileProperties.containsKey("keyStartUpgrade2")) {
         profileProperties.setProperty("keyStartUpgrade2", "");
      }

      if (!profileProperties.containsKey("keyStartUpgrade3")) {
         profileProperties.setProperty("keyStartUpgrade3", "");
      }

      if (!profileProperties.containsKey("keyGrabSpell")) {
         profileProperties.setProperty("keyGrabSpell", "grave");
      }

      if (!profileProperties.containsKey("keyActiveSpell1")) {
         profileProperties.setProperty("keyActiveSpell1", "1");
      }

      if (!profileProperties.containsKey("keyActiveSpell2")) {
         profileProperties.setProperty("keyActiveSpell2", "2");
      }

      if (!profileProperties.containsKey("keyActiveSpell3")) {
         profileProperties.setProperty("keyActiveSpell3", "3");
      }

      if (!profileProperties.containsKey("keyActiveSpell4")) {
         profileProperties.setProperty("keyActiveSpell4", "4");
      }

      if (!profileProperties.containsKey("keyActiveSpell5")) {
         profileProperties.setProperty("keyActiveSpell5", "5");
      }

      if (!profileProperties.containsKey("keyHarvestWood")) {
         profileProperties.setProperty("keyHarvestWood", "6");
      }

      if (!profileProperties.containsKey("keyHarvestRock")) {
         profileProperties.setProperty("keyHarvestRock", "7");
      }

      if (!profileProperties.containsKey("keyHarvestFoodAndWater")) {
         profileProperties.setProperty("keyHarvestFoodAndWater", "8");
      }

      if (!profileProperties.containsKey("keyHarvestCrystals")) {
         profileProperties.setProperty("keyHarvestCrystals", "9");
      }

      if (!profileProperties.containsKey("keyDestroyTerrain")) {
         profileProperties.setProperty("keyDestroyTerrain", "0");
      }

      if (!profileProperties.containsKey("keyAxisLock")) {
         profileProperties.setProperty("keyAxisLock", "lshift");
      }

      if (!profileProperties.containsKey("keyUnassign")) {
         profileProperties.setProperty("keyUnassign", "q");
      }

      if (!profileProperties.containsKey("tipsEnabled")) {
         if (tipsEnabled) {
            profileProperties.setProperty("tipsEnabled", "true");
         } else {
            profileProperties.setProperty("tipsEnabled", "false");
         }
      }

      if (!profileProperties.containsKey("screenshakeEnabled")) {
         profileProperties.setProperty("screenshakeEnabled", "true");
      }

      if (!profileProperties.containsKey("mapScrollInverted")) {
         profileProperties.setProperty("mapScrollInverted", "false");
      }

      if (!profileProperties.containsKey("edgeScrollingEnabled")) {
         profileProperties.setProperty("edgeScrollingEnabled", "false");
      }

      if (!profileProperties.containsKey("edgeScrollSpeed")) {
         profileProperties.setProperty("edgeScrollSpeed", "75");
      }

      if (!profileProperties.containsKey("lastGameMode")) {
         profileProperties.setProperty("lastGameMode", "null");
      }

      if (!profileProperties.containsKey("twitchChannel")) {
         profileProperties.setProperty("twitchChannel", twitchChannel);
      }

      saveProfileSettings();
   }

   public static void setFullScreen(boolean state) {
      settingsProperties.setProperty("fullScreen", Boolean.toString(state));
      fullScreen = Boolean.parseBoolean(settingsProperties.getProperty("fullScreen"));
      saveBaseSettings();
   }

   public static void setBorderlessFullScreen(boolean state) {
      settingsProperties.setProperty("borderlessFullScreen", Boolean.toString(state));
      borderlessFullScreen = Boolean.parseBoolean(settingsProperties.getProperty("borderlessFullScreen"));
      saveBaseSettings();
   }

   public static void setFullScreenResolution(int w, int h) {
      settingsProperties.setProperty("fullScreenResolutionW", Integer.toString(w));
      fullScreenResolutionW = w;
      settingsProperties.setProperty("fullScreenResolutionH", Integer.toString(h));
      fullScreenResolutionH = h;
      saveBaseSettings();
   }

   public static void setLastProfile(int i) {
      settingsProperties.setProperty("lastProfile", Integer.toString(i));
      lastProfile = i;
      saveBaseSettings();
   }

   public static void setLastGameMode(GameModeTemplateBase.GameMode gM) {
      profileProperties.setProperty("lastGameMode", gM.toString());
      lastGameMode = gM;
      saveProfileSettings();
   }

   public static void setTwitchChannel(String tC) {
      profileProperties.setProperty("twitchChannel", tC);
      twitchChannel = tC;
      saveProfileSettings();
   }

   public static void setLanguage(String l) {
      settingsProperties.setProperty("language", l);
      language = l;
      saveBaseSettings();
   }

   public static void setHideDisclaimer(boolean b) {
      settingsProperties.setProperty("hideDisclaimer", Boolean.toString(b));
      hideDisclaimer = b;
      saveBaseSettings();
   }

   public static int getKeyUp() {
      return keyMapUp;
   }

   public static String getKeyUpName() {
      return profileProperties.getProperty("keyMapUp");
   }

   public static int getKeyDown() {
      return keyMapDown;
   }

   public static String getKeyDownName() {
      return profileProperties.getProperty("keyMapDown");
   }

   public static int getKeyLeft() {
      return keyMapLeft;
   }

   public static String getKeyLeftName() {
      return profileProperties.getProperty("keyMapLeft");
   }

   public static int getKeyRight() {
      return keyMapRight;
   }

   public static String getKeyRightName() {
      return profileProperties.getProperty("keyMapRight");
   }

   public static int getKeyZoomIn() {
      return keyZoomIn;
   }

   public static String getKeyZoomInName() {
      return profileProperties.getProperty("keyZoomIn");
   }

   public static int getKeyZoomOut() {
      return keyZoomOut;
   }

   public static String getKeyZoomOutName() {
      return profileProperties.getProperty("keyZoomOut");
   }

   public static int getKeySpeedUp() {
      return keySpeedUp;
   }

   public static String getKeySpeedUpName() {
      return profileProperties.getProperty("keySpeedUp");
   }

   public static int getKeySpeedDown() {
      return keySpeedDown;
   }

   public static String getKeySpeedDownName() {
      return profileProperties.getProperty("keySpeedDown");
   }

   public static int getKeyMinimap() {
      return keyMinimap;
   }

   public static String getKeyMinimapName() {
      return profileProperties.getProperty("keyMinimap");
   }

   public static int getKeyCycleBrushType() {
      return keyCycleBrushType;
   }

   public static String getKeyCycleBrushTypeName() {
      return profileProperties.getProperty("keyCycleBrushType");
   }

   public static int getKeyBrushSizeUp() {
      return keyBrushSizeUp;
   }

   public static String getBrushSizeUpName() {
      return profileProperties.getProperty("keyBrushSizeUp");
   }

   public static int getKeyBrushSizeDown() {
      return keyBrushSizeDown;
   }

   public static String getBrushSizeDownName() {
      return profileProperties.getProperty("keyBrushSizeDown");
   }

   public static int getKeyDebug() {
      return keyDebug;
   }

   public static String getKeyDebugName() {
      return profileProperties.getProperty("keyDebug");
   }

   public static int getKeyScreenShot() {
      return keyScreenShot;
   }

   public static String getKeyScreenShotName() {
      return profileProperties.getProperty("keyScreenShot");
   }

   public static int getKeyHideGUI() {
      return keyHideGUI;
   }

   public static String getKeyHudeGUIName() {
      return profileProperties.getProperty("keyHideGUI");
   }

   public static int getKeyGrid() {
      return keyGrid;
   }

   public static String getKeyGridName() {
      return profileProperties.getProperty("keyGrid");
   }

   public static int getKeyHideTopography() {
      return keyHideTopography;
   }

   public static String getKeyHideTopographyName() {
      return profileProperties.getProperty("keyHideTopography");
   }

   public static int getKeyFullScreen() {
      return keyFullScreen;
   }

   public static String getKeyFullScreenName() {
      return profileProperties.getProperty("keyFullScreen");
   }

   public static int getKeyPopulationList() {
      return keyPopulationList;
   }

   public static String getKeyPopulationName() {
      return profileProperties.getProperty("keyPopulationList");
   }

   public static int getKeyMainMenu() {
      return keyMainMenu;
   }

   public static String getKeyMainMenuName() {
      return profileProperties.getProperty("keyMainMenu");
   }

   public static int getKeyMapInformation() {
      return keyMapInformation;
   }

   public static String getKeyMapInformationName() {
      return profileProperties.getProperty("keyMapInformation");
   }

   public static int getKeyDataViews() {
      return keyDataViews;
   }

   public static String getKeyDataViewsName() {
      return profileProperties.getProperty("keyDataViews");
   }

   public static int getKeyProblemPanel() {
      return keyProblemPanel;
   }

   public static String getKeyProblemPanelName() {
      return profileProperties.getProperty("keyProblemPanel");
   }

   public static int getKeyBuildingList() {
      return keyBuildingList;
   }

   public static String getKeyBuildingListName() {
      return profileProperties.getProperty("keyBuildingList");
   }

   public static int getKeyCancel() {
      return keyCancel;
   }

   public static String getKeyCancelName() {
      return profileProperties.getProperty("keyCancel");
   }

   public static int getKeyAccentModeToggle() {
      return keyAccentModeToggle;
   }

   public static String getKeyAccentModeToggleName() {
      return profileProperties.getProperty("keyAccentModeToggle");
   }

   public static int getKeyEraseTool() {
      return keyEraseTool;
   }

   public static String getKeyEraseToolName() {
      return profileProperties.getProperty("keyEraseTool");
   }

   public static int getKeyPatchTool() {
      return keyPatchTool;
   }

   public static String getKeyPatchToolName() {
      return profileProperties.getProperty("keyPatchTool");
   }

   public static int getKeyHoleTool() {
      return keyHoleTool;
   }

   public static String getKeyHoleToolName() {
      return profileProperties.getProperty("keyHoleTool");
   }

   public static int getKeyPause() {
      return keyPause;
   }

   public static String getKeyPauseName() {
      return profileProperties.getProperty("keyPause");
   }

   public static int getKeyPauseObjects() {
      return keyPauseObjects;
   }

   public static String getKeyPauseObjectsName() {
      return profileProperties.getProperty("keyPauseObjects");
   }

   public static int getKeyDismantleObjects() {
      return keyDismantleObjects;
   }

   public static String getKeyDismantleObjectsName() {
      return profileProperties.getProperty("keyDismantleObjects");
   }

   public static int getKeyUpgrade() {
      return keyUpgrade;
   }

   public static String getKeyUpgradeName() {
      return profileProperties.getProperty("keyUpgrade");
   }

   public static int getKeyStartUpgrade2() {
      return keyStartUpgrade2;
   }

   public static String getKeyStartUpgrade2Name() {
      return profileProperties.getProperty("keyStartUpgrade2");
   }

   public static int getKeyStartUpgrade3() {
      return keyStartUpgrade3;
   }

   public static String getKeyStartUpgrade3Name() {
      return profileProperties.getProperty("keyStartUpgrade3");
   }

   public static int getKeyGrabSpell() {
      return keyGrabSpell;
   }

   public static String getKeyGrabSpellName() {
      return profileProperties.getProperty("keyGrabSpell");
   }

   public static int getKeyAxisLock() {
      return keyAxisLock;
   }

   public static String getKeyAxisLockName() {
      return profileProperties.getProperty("keyAxisLock");
   }

   public static int getKeyUnassign() {
      return keyUnassign;
   }

   public static String getKeyUnassignName() {
      return profileProperties.getProperty("keyUnassign");
   }

   public static int getKeyActiveSpell1() {
      return keyActiveSpell1;
   }

   public static String getKeyActiveSpell1Name() {
      return profileProperties.getProperty("keyActiveSpell1");
   }

   public static int getKeyActiveSpell2() {
      return keyActiveSpell2;
   }

   public static String getKeyActiveSpell2Name() {
      return profileProperties.getProperty("keyActiveSpell2");
   }

   public static int getKeyActiveSpell3() {
      return keyActiveSpell3;
   }

   public static String getKeyActiveSpell3Name() {
      return profileProperties.getProperty("keyActiveSpell3");
   }

   public static int getKeyActiveSpell4() {
      return keyActiveSpell4;
   }

   public static String getKeyActiveSpell4Name() {
      return profileProperties.getProperty("keyActiveSpell4");
   }

   public static int getKeyActiveSpell5() {
      return keyActiveSpell5;
   }

   public static String getKeyActiveSpell5Name() {
      return profileProperties.getProperty("keyActiveSpell5");
   }

   public static int getKeyHarvestWood() {
      return keyHarvestWood;
   }

   public static String getKeyHarvestWoodName() {
      return profileProperties.getProperty("keyHarvestWood");
   }

   public static int getKeyHarvestRock() {
      return keyHarvestRock;
   }

   public static String getKeyHarvestRockName() {
      return profileProperties.getProperty("keyHarvestRock");
   }

   public static int getKeyHarvestFoodAndWater() {
      return keyHarvestFoodAndWater;
   }

   public static String getKeyHarvestFoodAndWaterName() {
      return profileProperties.getProperty("keyHarvestFoodAndWater");
   }

   public static int getKeyHarvestCrystals() {
      return keyHarvestCrystals;
   }

   public static String getKeyHarvestCrystalsName() {
      return profileProperties.getProperty("keyHarvestCrystals");
   }

   public static int getKeyDestroyTerrain() {
      return keyDestroyTerrain;
   }

   public static String getKeyDestroyTerrainName() {
      return profileProperties.getProperty("keyDestroyTerrain");
   }

   public static int getMusicVolume() {
      return musicVolume;
   }

   public static int getSoundVolume() {
      return soundVolume;
   }

   public static int getLastProfile() {
      return lastProfile;
   }

   public static boolean isFullScreen() {
      return fullScreen;
   }

   public static boolean isBorderlessFullScreen() {
      return borderlessFullScreen;
   }

   public static int getFullScreenResolutionW() {
      return fullScreenResolutionW;
   }

   public static int getFullScreenResolutionH() {
      return fullScreenResolutionH;
   }

   public static boolean getVSync() {
      return vSync;
   }

   public static boolean getTipsEnabled() {
      return tipsEnabled;
   }

   public static boolean getScreenshakeEnabled() {
      return screenshakeEnabled;
   }

   public static boolean getMapScrollInverted() {
      return mapScrollInverted;
   }

   public static boolean getEdgeScrollingEnabled() {
      return edgeScrollingEnabled;
   }

   public static int getEdgeScrollSpeed() {
      return edgeScrollSpeed;
   }

   public static GameModeTemplateBase.GameMode getLastGameMode() {
      return lastGameMode;
   }

   public static String getTwitchChannel() {
      return twitchChannel;
   }

   public static String getLanguage() {
      return language;
   }

   public static boolean getRetroParticles() {
      return retroParticles;
   }

   public static boolean getDebug() {
      return debug;
   }

   public static boolean getAllowOfficial() {
      return allowOfficial;
   }

   public static boolean canUseBannerMessage() {
      return useBannerMessage;
   }

   public static String getBannerMessage1() {
      return bannerMessage1;
   }

   public static String getBannerMessage2() {
      return bannerMessage2;
   }

   public static SettingsParser.ParticleAmount getParticleAmount() {
      return particleAmount;
   }

   public static boolean getShadows() {
      return shadows;
   }

   public static SettingsParser.AutosaveFrequency getSaveFrequency() {
      return autosaveFrequency;
   }

   public static boolean getClouds() {
      return clouds;
   }

   public static boolean getBackground() {
      return background;
   }

   public static boolean getVerboseConsole() {
      return verboseConsole;
   }

   public static boolean getHideDisclaimer() {
      return hideDisclaimer;
   }

   public static int getMaximumGameSpeed() {
      return maximumGameSpeed;
   }

   public static boolean getMainMenuDrift() {
      return mainMenuDrift;
   }

   public static boolean getOfflineMode() {
      return offlineMode;
   }

   public static boolean getMouseWheelZoomingEnabled() {
      return mouseWheelZoomingEnabled;
   }

   public static int getAxisLockBuffer() {
      return axisLockBuffer;
   }

   public static float getForceInterfaceScale() {
      return forceInterfaceScale;
   }

   public static void getDefaultBaseSettings(Properties resetBaseProperties) {
      resetBaseProperties.setProperty("version", Game.getSettingsVersion());
      resetBaseProperties.setProperty("fullScreen", "false");
      if (System.getProperty("os.name").toLowerCase().contains("win")) {
         resetBaseProperties.setProperty("borderlessFullScreen", "true");
      } else {
         resetBaseProperties.setProperty("borderlessFullScreen", "false");
      }
      resetBaseProperties.setProperty("fullScreenResolutionW", Integer.toString(Platform.get().getScreenWidth()));
      resetBaseProperties.setProperty("fullScreenResolutionH", Integer.toString(Platform.get().getScreenHeight()));
      resetBaseProperties.setProperty("vSync", "true");
      resetBaseProperties.setProperty("musicVolume", "8");
      resetBaseProperties.setProperty("soundVolume", "15");
      resetBaseProperties.setProperty("lastProfile", "-1");
      resetBaseProperties.setProperty("forceInterfaceScale", "-1");
      resetBaseProperties.setProperty("particleAmount", "2");
      resetBaseProperties.setProperty("shadows", "true");
      resetBaseProperties.setProperty("autosaveFrequency", "2");
      resetBaseProperties.setProperty("clouds", "true");
      resetBaseProperties.setProperty("background", "true");
      resetBaseProperties.setProperty("mapScrollInverted", "false");
      resetBaseProperties.setProperty("hideDisclaimer", "false");
      resetBaseProperties.setProperty("maximumGameSpeed", "3");
      resetBaseProperties.setProperty("axisLockBuffer", "1");
   }

   public static void getDefaultProfileSettings(Properties resetProfileProperties) {
      resetProfileProperties.setProperty("version", Game.getSettingsVersion());
      resetProfileProperties.setProperty("keyMapUp", "w");
      resetProfileProperties.setProperty("keyMapDown", "s");
      resetProfileProperties.setProperty("keyMapLeft", "a");
      resetProfileProperties.setProperty("keyMapRight", "d");
      resetProfileProperties.setProperty("keyZoomIn", "rbracket");
      resetProfileProperties.setProperty("keyZoomOut", "lbracket");
      resetProfileProperties.setProperty("keySpeedUp", "f6");
      resetProfileProperties.setProperty("keySpeedDown", "f5");
      resetProfileProperties.setProperty("keyMinimap", "tab");
      resetProfileProperties.setProperty("keyCycleBrushType", "z");
      resetProfileProperties.setProperty("keyBrushSizeUp", "period");
      resetProfileProperties.setProperty("keyBrushSizeDown", "comma");
      resetProfileProperties.setProperty("keyDebug", "f3");
      resetProfileProperties.setProperty("keyScreenShot", "f2");
      resetProfileProperties.setProperty("keyHideGUI", "f4");
      resetProfileProperties.setProperty("keyGrid", "g");
      resetProfileProperties.setProperty("keyHideTopography", "t");
      resetProfileProperties.setProperty("keyFullScreen", "f10");
      resetProfileProperties.setProperty("keyPopulationList", "p");
      resetProfileProperties.setProperty("keyMainMenu", "m");
      resetProfileProperties.setProperty("keyMapInformation", "i");
      resetProfileProperties.setProperty("keyDataViews", "v");
      resetProfileProperties.setProperty("keyProblemPanel", "r");
      resetProfileProperties.setProperty("keyBuildingList", "l");
      resetProfileProperties.setProperty("keyCancel", "escape");
      resetProfileProperties.setProperty("keyAccentModeToggle", "x");
      resetProfileProperties.setProperty("keyEraseTool", "e");
      resetProfileProperties.setProperty("keyPatchTool", "p");
      resetProfileProperties.setProperty("keyHoleTool", "h");
      resetProfileProperties.setProperty("keyPause", "space");
      resetProfileProperties.setProperty("keyPauseObjects", "o");
      resetProfileProperties.setProperty("keyDismantleObjects", "");
      resetProfileProperties.setProperty("keyUpgrade", "");
      resetProfileProperties.setProperty("keyStartUpgrade2", "");
      resetProfileProperties.setProperty("keyStartUpgrade3", "");
      resetProfileProperties.setProperty("keyGrabSpell", "grave");
      resetProfileProperties.setProperty("keyActiveSpell1", "1");
      resetProfileProperties.setProperty("keyActiveSpell2", "2");
      resetProfileProperties.setProperty("keyActiveSpell3", "3");
      resetProfileProperties.setProperty("keyActiveSpell4", "4");
      resetProfileProperties.setProperty("keyActiveSpell5", "5");
      resetProfileProperties.setProperty("keyHarvestWood", "6");
      resetProfileProperties.setProperty("keyHarvestRock", "7");
      resetProfileProperties.setProperty("keyHarvestFoodAndWater", "8");
      resetProfileProperties.setProperty("keyHarvestCrystals", "9");
      resetProfileProperties.setProperty("keyDestroyTerrain", "0");
      resetProfileProperties.setProperty("keyAxisLock", "lshift");
      resetProfileProperties.setProperty("tipsEnabled", "true");
      resetProfileProperties.setProperty("screenshakeEnabled", "true");
      resetProfileProperties.setProperty("mapScrollInverted", "false");
      resetProfileProperties.setProperty("edgeScrollingEnabled", "false");
      resetProfileProperties.setProperty("edgeScrollSpeed", "75");
      resetProfileProperties.setProperty("lastGameMode", "null");
      resetProfileProperties.setProperty("twitchChannel", "");
   }

   public static enum AutosaveFrequency {
      MINIMAL,
      REDUCED,
      FULL;
   }

   public static enum ParticleAmount {
      MINIMAL,
      REDUCED,
      FULL;
   }
}
