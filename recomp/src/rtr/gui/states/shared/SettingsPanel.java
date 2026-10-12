package rtr.gui.states.shared;

import rtr.platform.Platform;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.SettingsParser;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonInputText;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.MapEditorGUIData;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.SharedGUIPanelBase;
import rtr.system.Game;
import rtr.system.ScaleControl;

public class SettingsPanel extends SharedGUIPanelBase {
   private Image settingsPanel;
   private Rectangle settingsMask;
   private GUIButtonMainMenu ok;
   private GUIButtonMainMenu cancel;
   private GUIButtonMainMenu reset;
   private GUIClickableText musicUp;
   private GUIClickableText musicDown;
   private GUIClickableText soundUp;
   private GUIClickableText soundDown;
   private GUIClickableText mapScrollInverted;
   private GUIClickableText edgeScrolling;
   private GUIClickableText edgeScrollingUp;
   private GUIClickableText edgeScrollingDown;
   private GUIClickableText fullScreen;
   private GUIClickableText borderlessFullScreen;
   private GUIClickableText fullScreenResolutionUp;
   private GUIClickableText fullScreenResolutionDown;
   private ArrayList<String> validFullScreenResolutions = new ArrayList<>();
   private GUIClickableText vSync;
   private GUIClickableText tipsEnabled;
   private GUIClickableText interfaceScaleUp;
   private GUIClickableText interfaceScaleDown;
   private GUIClickableText particleAmount;
   private GUIClickableText shadows;
   private GUIClickableText autosaveFrequency;
   private GUIClickableText clouds;
   private GUIClickableText background;
   private GUIClickableText screenshakeEnabled;
   private GUIButtonInputText twitchChannel;
   private GUIClickableText keyMapUp;
   private GUIClickableText keyMapDown;
   private GUIClickableText keyMapLeft;
   private GUIClickableText keyMapRight;
   private GUIClickableText keyZoomIn;
   private GUIClickableText keyZoomOut;
   private GUIClickableText keySpeedUp;
   private GUIClickableText keySpeedDown;
   private GUIClickableText keyMinimap;
   private GUIClickableText keyCycleBrushType;
   private GUIClickableText keyBrushSizeUp;
   private GUIClickableText keyBrushSizeDown;
   private GUIClickableText keyDebug;
   private GUIClickableText keyScreenShot;
   private GUIClickableText keyHideGUI;
   private GUIClickableText keyGrid;
   private GUIClickableText keyHideTopography;
   private GUIClickableText keyFullScreen;
   private GUIClickableText keyPopulationList;
   private GUIClickableText keyMainMenu;
   private GUIClickableText keyMapInformation;
   private GUIClickableText keyDataViews;
   private GUIClickableText keyProblemPanel;
   private GUIClickableText keyBuildingList;
   private GUIClickableText keyCancel;
   private GUIClickableText keyAccentModeToggle;
   private GUIClickableText keyEraseTool;
   private GUIClickableText keyUnassign;
   private GUIClickableText keyPatchTool;
   private GUIClickableText keyHoleTool;
   private GUIClickableText keyPause;
   private GUIClickableText keyPauseObjects;
   private GUIClickableText keyDismantleObjects;
   private GUIClickableText keyUpgrade;
   private GUIClickableText keyStartUpgrade2;
   private GUIClickableText keyStartUpgrade3;
   private GUIClickableText keyGrabSpell;
   private GUIClickableText keyActiveSpell1;
   private GUIClickableText keyActiveSpell2;
   private GUIClickableText keyActiveSpell3;
   private GUIClickableText keyActiveSpell4;
   private GUIClickableText keyActiveSpell5;
   private GUIClickableText keyHarvestWood;
   private GUIClickableText keyHarvestRock;
   private GUIClickableText keyHarvestFoodAndWater;
   private GUIClickableText keyHarvestCrystals;
   private GUIClickableText keyDestroyTerrain;
   private GUIClickableText keyAxisLock;
   ArrayList<String> keysString = new ArrayList<>();
   ArrayList<GUIClickableText> keys = new ArrayList<>();
   private Properties currentBaseProperties;
   private Properties currentProfileProperties;
   private Image keyChangePanel;
   private Rectangle keyChangeMask;
   private String keyChangeOldKey = Text.getText("sharedSettingsPanel.miscellaneous.nothingHere");
   private String keyName;
   private boolean keyChangeMode;

   public SettingsPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   public SettingsPanel(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   public SettingsPanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   private void init() throws SlickException {
      this.settingsPanel = ImageLoader.getImage("res/GUI/mainMenu/mainMenuSettings.png");
      this.settingsMask = new Rectangle(0.0F, 0.0F, this.settingsPanel.getWidth(), this.settingsPanel.getHeight());
      this.masks.add(this.settingsMask);
      this.ok = new GUIButtonMainMenu("ok", Text.getText("sharedSettingsPanel.button.ok"), 0, 1);
      this.guiButtons.add(this.ok);
      this.cancel = new GUIButtonMainMenu("cancel", Text.getText("sharedSettingsPanel.button.cancel"), 1, 2);
      this.guiButtons.add(this.cancel);
      this.reset = new GUIButtonMainMenu("reset", Text.getText("sharedSettingsPanel.button.reset"), 1);
      this.guiButtons.add(this.reset);
      this.musicUp = new GUIClickableText("musicUp", 3);
      this.guiButtons.add(this.musicUp);
      this.musicDown = new GUIClickableText("musicDown", 3);
      this.guiButtons.add(this.musicDown);
      this.soundUp = new GUIClickableText("soundUp", 3);
      this.guiButtons.add(this.soundUp);
      this.soundDown = new GUIClickableText("soundDown", 3);
      this.guiButtons.add(this.soundDown);
      this.mapScrollInverted = new GUIClickableText("mapScrollInverted", 2);
      this.guiButtons.add(this.mapScrollInverted);
      this.edgeScrolling = new GUIClickableText("edgeScrolling", 2);
      this.guiButtons.add(this.edgeScrolling);
      this.edgeScrollingUp = new GUIClickableText("edgeScrollingUp", 3);
      this.guiButtons.add(this.edgeScrollingUp);
      this.edgeScrollingDown = new GUIClickableText("edgeScrollingDown", 3);
      this.guiButtons.add(this.edgeScrollingDown);
      this.fullScreen = new GUIClickableText("fullScreen", 2);
      this.guiButtons.add(this.fullScreen);
      this.borderlessFullScreen = new GUIClickableText("borderlessFullScreen", 2);
      this.guiButtons.add(this.borderlessFullScreen);
      this.fullScreenResolutionUp = new GUIClickableText("fullScreenResolutionUp", 3);
      this.guiButtons.add(this.fullScreenResolutionUp);
      this.fullScreenResolutionDown = new GUIClickableText("fullScreenResolutionDown", 3);
      this.guiButtons.add(this.fullScreenResolutionDown);

      try {
         DisplayMode[] displayModes = Display.getAvailableDisplayModes();

         for (DisplayMode d : displayModes) {
            if (d.getWidth() >= 1280 && d.getHeight() >= 720 && !this.validFullScreenResolutions.contains(d.getWidth() + "x" + d.getHeight())) {
               this.validFullScreenResolutions.add(d.getWidth() + "x" + d.getHeight());
            }
         }

         Collections.sort(this.validFullScreenResolutions, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
               String[] o1s = ((String)o1).split("x");
               String[] o2s = ((String)o2).split("x");
               Integer x1 = Integer.parseInt(o1s[0]) * Integer.parseInt(o1s[1]);
               Integer x2 = Integer.parseInt(o2s[0]) * Integer.parseInt(o2s[1]);
               return x1.compareTo(x2);
            }
         });
      } catch (LWJGLException var6) {
         var6.printStackTrace();
      }

      this.vSync = new GUIClickableText("vSync", 2);
      this.guiButtons.add(this.vSync);
      this.tipsEnabled = new GUIClickableText("tipsEnabled", 2);
      this.guiButtons.add(this.tipsEnabled);
      this.interfaceScaleUp = new GUIClickableText("interfaceScaleUp", 3);
      this.guiButtons.add(this.interfaceScaleUp);
      this.interfaceScaleDown = new GUIClickableText("interfaceScaleDown", 3);
      this.guiButtons.add(this.interfaceScaleDown);
      this.particleAmount = new GUIClickableText("particleAmount", 2);
      this.guiButtons.add(this.particleAmount);
      this.shadows = new GUIClickableText("shadows", 2);
      this.guiButtons.add(this.shadows);
      this.autosaveFrequency = new GUIClickableText("autosaveFrequency", 2);
      this.guiButtons.add(this.autosaveFrequency);
      this.clouds = new GUIClickableText("clouds", 2);
      this.guiButtons.add(this.clouds);
      this.background = new GUIClickableText("background", 2);
      this.guiButtons.add(this.background);
      this.screenshakeEnabled = new GUIClickableText("screenshakeEnabled", 2);
      this.guiButtons.add(this.screenshakeEnabled);
      this.twitchChannel = new GUIButtonInputText(this.gc, "twitchChannel", Text.getText("sharedSettingsPanel.twitch.channelName"), 16, 2);
      this.guiButtons.add(this.twitchChannel);
      this.keyMapUp = new GUIClickableText("keyMapUp", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.mapUp"));
      this.guiButtons.add(this.keyMapUp);
      this.keys.add(this.keyMapUp);
      this.keyMapDown = new GUIClickableText("keyMapDown", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.mapDown"));
      this.guiButtons.add(this.keyMapDown);
      this.keys.add(this.keyMapDown);
      this.keyMapLeft = new GUIClickableText("keyMapLeft", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.mapLeft"));
      this.guiButtons.add(this.keyMapLeft);
      this.keys.add(this.keyMapLeft);
      this.keyMapRight = new GUIClickableText("keyMapRight", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.mapRight"));
      this.guiButtons.add(this.keyMapRight);
      this.keys.add(this.keyMapRight);
      this.keyZoomIn = new GUIClickableText("keyZoomIn", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.zoomIn"));
      this.guiButtons.add(this.keyZoomIn);
      this.keys.add(this.keyZoomIn);
      this.keyZoomOut = new GUIClickableText("keyZoomOut", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.zoomOut"));
      this.guiButtons.add(this.keyZoomOut);
      this.keys.add(this.keyZoomOut);
      this.keySpeedUp = new GUIClickableText("keySpeedUp", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.gameSpeedUp"));
      this.guiButtons.add(this.keySpeedUp);
      this.keys.add(this.keySpeedUp);
      this.keySpeedDown = new GUIClickableText("keySpeedDown", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.gameSpeedDown"));
      this.guiButtons.add(this.keySpeedDown);
      this.keys.add(this.keySpeedDown);
      this.keyMinimap = new GUIClickableText("keyMinimap", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.minimap"));
      this.guiButtons.add(this.keyMinimap);
      this.keys.add(this.keyMinimap);
      this.keyCycleBrushType = new GUIClickableText("keyCycleBrushType", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.cycleBrushType"));
      this.guiButtons.add(this.keyCycleBrushType);
      this.keys.add(this.keyCycleBrushType);
      this.keyBrushSizeUp = new GUIClickableText("keyBrushSizeUp", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.brushSizeUp"));
      this.guiButtons.add(this.keyBrushSizeUp);
      this.keys.add(this.keyBrushSizeUp);
      this.keyBrushSizeDown = new GUIClickableText("keyBrushSizeDown", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.brushSizeDown"));
      this.guiButtons.add(this.keyBrushSizeDown);
      this.keys.add(this.keyBrushSizeDown);
      this.keyDebug = new GUIClickableText("keyDebug", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.debugView"));
      this.guiButtons.add(this.keyDebug);
      this.keys.add(this.keyDebug);
      this.keyScreenShot = new GUIClickableText("keyScreenShot", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.screenShot"));
      this.guiButtons.add(this.keyScreenShot);
      this.keys.add(this.keyScreenShot);
      this.keyHideGUI = new GUIClickableText("keyHideGUI", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.hideGUI"));
      this.guiButtons.add(this.keyHideGUI);
      this.keys.add(this.keyHideGUI);
      this.keyGrid = new GUIClickableText("keyGrid", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.gridView"));
      this.guiButtons.add(this.keyGrid);
      this.keys.add(this.keyGrid);
      this.keyHideTopography = new GUIClickableText("keyHideTopography", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.hideTopography"));
      this.guiButtons.add(this.keyHideTopography);
      this.keys.add(this.keyHideTopography);
      this.keyFullScreen = new GUIClickableText("keyFullScreen", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.fullScreen"));
      this.guiButtons.add(this.keyFullScreen);
      this.keys.add(this.keyFullScreen);
      this.keyPopulationList = new GUIClickableText("keyPopulationList", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.populationList"));
      this.guiButtons.add(this.keyPopulationList);
      this.keys.add(this.keyPopulationList);
      this.keyMainMenu = new GUIClickableText("keyMainMenu", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.mainMenu"));
      this.guiButtons.add(this.keyMainMenu);
      this.keys.add(this.keyMainMenu);
      this.keyMapInformation = new GUIClickableText("keyMapInformation", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.mapInformation"));
      this.guiButtons.add(this.keyMapInformation);
      this.keys.add(this.keyMapInformation);
      this.keyDataViews = new GUIClickableText("keyDataViews", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.dataViews"));
      this.guiButtons.add(this.keyDataViews);
      this.keys.add(this.keyDataViews);
      this.keyProblemPanel = new GUIClickableText("keyProblemPanel", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.problemPanel"));
      this.guiButtons.add(this.keyProblemPanel);
      this.keys.add(this.keyProblemPanel);
      this.keyBuildingList = new GUIClickableText("keyBuildingList", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.buildingList"));
      this.guiButtons.add(this.keyBuildingList);
      this.keys.add(this.keyBuildingList);
      this.keyCancel = new GUIClickableText("keyCancel", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.cancel"));
      this.guiButtons.add(this.keyCancel);
      this.keys.add(this.keyCancel);
      this.keyAccentModeToggle = new GUIClickableText("keyAccentModeToggle", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.accent"));
      this.guiButtons.add(this.keyAccentModeToggle);
      this.keys.add(this.keyAccentModeToggle);
      this.keyEraseTool = new GUIClickableText("keyEraseTool", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.erase"));
      this.guiButtons.add(this.keyEraseTool);
      this.keys.add(this.keyEraseTool);
      this.keyUnassign = new GUIClickableText("keyUnassign", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.unassign"));
      this.guiButtons.add(this.keyUnassign);
      this.keys.add(this.keyUnassign);
      this.keyPatchTool = new GUIClickableText("keyPatchTool", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.patch"));
      this.guiButtons.add(this.keyPatchTool);
      this.keys.add(this.keyPatchTool);
      this.keyHoleTool = new GUIClickableText("keyHoleTool", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.hole"));
      this.guiButtons.add(this.keyHoleTool);
      this.keys.add(this.keyHoleTool);
      this.keyPause = new GUIClickableText("keyPause", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.pause"));
      this.guiButtons.add(this.keyPause);
      this.keys.add(this.keyPause);
      this.keyPauseObjects = new GUIClickableText("keyPauseObjects", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.pauseObject"));
      this.guiButtons.add(this.keyPauseObjects);
      this.keys.add(this.keyPauseObjects);
      this.keyDismantleObjects = new GUIClickableText("keyDismantleObjects", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.dismantleObject"));
      this.guiButtons.add(this.keyDismantleObjects);
      this.keys.add(this.keyDismantleObjects);
      this.keyUpgrade = new GUIClickableText("keyUpgrade", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.upgrade"));
      this.guiButtons.add(this.keyUpgrade);
      this.keys.add(this.keyUpgrade);
      this.keyStartUpgrade2 = new GUIClickableText("keyStartUpgrade2", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.startUpgrade2"));
      this.guiButtons.add(this.keyStartUpgrade2);
      this.keys.add(this.keyStartUpgrade2);
      this.keyStartUpgrade3 = new GUIClickableText("keyStartUpgrade3", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.startUpgrade3"));
      this.guiButtons.add(this.keyStartUpgrade3);
      this.keys.add(this.keyStartUpgrade3);
      this.keyGrabSpell = new GUIClickableText("keyGrabSpell", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.grabSpell"));
      this.guiButtons.add(this.keyGrabSpell);
      this.keys.add(this.keyGrabSpell);
      this.keyActiveSpell1 = new GUIClickableText("keyActiveSpell1", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.activeSpell1"));
      this.guiButtons.add(this.keyActiveSpell1);
      this.keys.add(this.keyActiveSpell1);
      this.keyActiveSpell2 = new GUIClickableText("keyActiveSpell2", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.activeSpell2"));
      this.guiButtons.add(this.keyActiveSpell2);
      this.keys.add(this.keyActiveSpell2);
      this.keyActiveSpell3 = new GUIClickableText("keyActiveSpell3", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.activeSpell3"));
      this.guiButtons.add(this.keyActiveSpell3);
      this.keys.add(this.keyActiveSpell3);
      this.keyActiveSpell4 = new GUIClickableText("keyActiveSpell4", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.activeSpell4"));
      this.guiButtons.add(this.keyActiveSpell4);
      this.keys.add(this.keyActiveSpell4);
      this.keyActiveSpell5 = new GUIClickableText("keyActiveSpell5", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.activeSpell5"));
      this.guiButtons.add(this.keyActiveSpell5);
      this.keys.add(this.keyActiveSpell5);
      this.keyHarvestWood = new GUIClickableText("keyHarvestWood", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.harvestWood"));
      this.guiButtons.add(this.keyHarvestWood);
      this.keys.add(this.keyHarvestWood);
      this.keyHarvestRock = new GUIClickableText("keyHarvestRock", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.harvestRock"));
      this.guiButtons.add(this.keyHarvestRock);
      this.keys.add(this.keyHarvestRock);
      this.keyHarvestFoodAndWater = new GUIClickableText("keyHarvestFoodAndWater", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.harvestFoodAndWater"));
      this.guiButtons.add(this.keyHarvestFoodAndWater);
      this.keys.add(this.keyHarvestFoodAndWater);
      this.keyHarvestCrystals = new GUIClickableText("keyHarvestCrystals", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.harvestCrystal"));
      this.guiButtons.add(this.keyHarvestCrystals);
      this.keys.add(this.keyHarvestCrystals);
      this.keyDestroyTerrain = new GUIClickableText("keyDestroyTerrain", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.destroyTerrain"));
      this.guiButtons.add(this.keyDestroyTerrain);
      this.keys.add(this.keyDestroyTerrain);
      this.keyAxisLock = new GUIClickableText("keyAxisLock", 0);
      this.keysString.add(Text.getText("sharedSettingsPanel.keys.axisLock"));
      this.guiButtons.add(this.keyAxisLock);
      this.keys.add(this.keyAxisLock);
      if (this.playStateController != null) {
         for (GUIClickableText k : this.keys) {
            k.setToolTip(Text.getText("sharedSettingsPanel.keys.setOnMainMenu"));
         }
      }

      this.keyChangePanel = ImageLoader.getImage("res/GUI/mainMenu/genericPanel.png");
      this.keyChangeMask = new Rectangle(0.0F, 0.0F, this.keyChangePanel.getWidth(), this.keyChangePanel.getHeight());
      this.masks.add(this.keyChangeMask);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.settingsPanel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() / 2 - this.settingsPanel.getHeight() / 2;
      if (this.x < 256) {
         this.x = 256;
      }

      this.settingsMask.setX(this.x);
      this.settingsMask.setY(this.y);
      this.settingsPanel.draw(this.x, this.y);
      this.font.drawString(this.x + 148, this.y + 16, Text.getText("sharedSettingsPanel.audio.header"), Text.FontType.HEADER, 3, true);
      this.font.drawString(this.x + 148, this.y + 49, Text.getText("sharedSettingsPanel.audio.musicVolume"), Text.FontType.HEADER, 1, true);
      this.musicDown.render(this.g, this.mouse, "<<<<", this.x + 37, this.y + 50, "$ORA0", "$ORA1", false, true, debug);
      int musicVolume = Integer.parseInt(this.currentBaseProperties.getProperty("musicVolume", "8"));
      String musicString = "";

      for (int i = 0; i < musicVolume; i++) {
         musicString = musicString + "O";
      }

      this.font.drawString(this.x + 147, this.y + 63, "$GRE1" + musicString, Text.FontType.BODY, 0, true);
      this.musicUp.render(this.g, this.mouse, ">>>>", this.x + 256, this.y + 50, "$ORA0", "$ORA1", false, true, debug);
      this.font.drawString(this.x + 148, this.y + 83, Text.getText("sharedSettingsPanel.audio.soundVolume"), Text.FontType.HEADER, 1, true);
      this.soundDown.render(this.g, this.mouse, "<<<<", this.x + 37, this.y + 84, "$ORA0", "$ORA1", false, true, debug);
      int soundVolume = Integer.parseInt(this.currentBaseProperties.getProperty("soundVolume", "15"));
      String soundString = "";

      for (int i = 0; i < soundVolume; i++) {
         soundString = soundString + "O";
      }

      this.font.drawString(this.x + 147, this.y + 97, "$GRE1" + soundString, Text.FontType.BODY, 0, true);
      this.soundUp.render(this.g, this.mouse, ">>>>", this.x + 256, this.y + 84, "$ORA0", "$ORA1", false, true, debug);
      this.font.drawString(this.x + 148, this.y + 147, Text.getText("sharedSettingsPanel.controls.header"), Text.FontType.HEADER, 3, true);
      this.font.drawString(this.x + 108, this.y + 183, Text.getText("sharedSettingsPanel.controls.invertMapScrolling"), Text.FontType.BODY, 2, true);
      if (this.currentProfileProperties.getProperty("mapScrollInverted").equals("false")) {
         this.mapScrollInverted
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.false"), this.x + 238, this.y + 183, "$RED0", "$RED1", false, true, debug
            );
      } else {
         this.mapScrollInverted
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.true"), this.x + 238, this.y + 183, "$GRE0", "$GRE1", false, true, debug
            );
      }

      this.font.drawString(this.x + 108, this.y + 213, Text.getText("sharedSettingsPanel.controls.edgeScrolling"), Text.FontType.BODY, 2, true);
      this.font
         .drawString(this.x + 108, this.y + 230, Text.getText("sharedSettingsPanel.controls.edgeScrollingFullScreenRecommended"), Text.FontType.BODY, 0, true);
      if (this.currentProfileProperties.getProperty("edgeScrollingEnabled").equals("false")) {
         this.edgeScrolling
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.false"), this.x + 238, this.y + 217, "$RED0", "$RED1", false, true, debug
            );
      } else {
         this.edgeScrolling
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.true"), this.x + 238, this.y + 217, "$GRE0", "$GRE1", false, true, debug
            );
      }

      this.font.drawString(this.x + 148, this.y + 248, Text.getText("sharedSettingsPanel.controls.edgeScrollingAmount"), Text.FontType.HEADER, 1, true);
      this.edgeScrollingDown.render(this.g, this.mouse, "<<<<", this.x + 37, this.y + 250, "$ORA0", "$ORA1", false, true, debug);
      int edgeScrollAmount = Integer.parseInt(this.currentProfileProperties.getProperty("edgeScrollSpeed", "10"));
      String edgeString = "";

      for (int i = 0; i < edgeScrollAmount / 10; i++) {
         edgeString = edgeString + "O";
      }

      this.font.drawString(this.x + 147, this.y + 262, edgeString, Text.FontType.BODY, 0, true);
      this.edgeScrollingUp.render(this.g, this.mouse, ">>>>", this.x + 256, this.y + 250, "$ORA0", "$ORA1", false, true, debug);
      this.font.drawString(this.x + 148, this.y + 312, Text.getText("sharedSettingsPanel.videoConfiguration.header"), Text.FontType.HEADER, 3, true);
      this.font.drawString(this.x + 108, this.y + 349, Text.getText("sharedSettingsPanel.videoConfiguration.fullScreen"), Text.FontType.BODY, 2, true);
      if (this.currentBaseProperties.getProperty("fullScreen").equals("false")) {
         this.fullScreen
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.false"), this.x + 238, this.y + 349, "$RED0", "$RED1", false, true, debug
            );
      } else {
         this.fullScreen
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.true"), this.x + 238, this.y + 349, "$GRE0", "$GRE1", false, true, debug
            );
      }

      if (this.currentBaseProperties.getProperty("fullScreen").equals("false")) {
         this.font
            .drawString(
               this.x + 108, this.y + 378, "$BLA1" + Text.getText("sharedSettingsPanel.videoConfiguration.borderlessFullScreen"), Text.FontType.BODY, 2, true
            );
         this.font
            .drawString(
               this.x + 108,
               this.y + 395,
               "$BLA1" + Text.getText("sharedSettingsPanel.videoConfiguration.edgeScrollingNotRecommended"),
               Text.FontType.BODY,
               0,
               true
            );
         this.font
            .drawString(this.x + 238, this.y + 380, Text.getText("sharedSettingsPanel.videoConfiguration.requiresFullScreen"), Text.FontType.BODY, 0, true);
      } else {
         this.font
            .drawString(this.x + 108, this.y + 378, Text.getText("sharedSettingsPanel.videoConfiguration.borderlessFullScreen"), Text.FontType.BODY, 2, true);
         this.font
            .drawString(
               this.x + 108, this.y + 395, Text.getText("sharedSettingsPanel.videoConfiguration.edgeScrollingNotRecommended"), Text.FontType.BODY, 0, true
            );
         if (this.currentBaseProperties.getProperty("borderlessFullScreen").equals("false")) {
            this.borderlessFullScreen
               .render(
                  this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.false"), this.x + 238, this.y + 383, "$RED0", "$RED1", false, true, debug
               );
         } else {
            this.borderlessFullScreen
               .render(
                  this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.true"), this.x + 238, this.y + 383, "$GRE0", "$GRE1", false, true, debug
               );
         }
      }

      if (!this.currentBaseProperties.getProperty("fullScreen").equals("false")
         && !this.currentBaseProperties.getProperty("borderlessFullScreen").equals("true")) {
         this.font
            .drawString(this.x + 148, this.y + 413, Text.getText("sharedSettingsPanel.videoConfiguration.fixedResolution"), Text.FontType.HEADER, 1, true);
         this.fullScreenResolutionDown.render(this.g, this.mouse, "<<<<", this.x + 37, this.y + 415, "$ORA0", "$ORA1", false, true, debug);
         String currentResolution = this.currentBaseProperties.getProperty("fullScreenResolutionW", "0")
            + " x "
            + this.currentBaseProperties.getProperty("fullScreenResolutionH", "0");
         if (Integer.parseInt(this.currentBaseProperties.getProperty("fullScreenResolutionW", "0")) == Platform.get().getScreenWidth()
            && Integer.parseInt(this.currentBaseProperties.getProperty("fullScreenResolutionH", "0")) == Platform.get().getScreenHeight()) {
            this.font.drawString(this.x + 147, this.y + 426, "$GRE1" + currentResolution, Text.FontType.BODY, 1, true);
         } else {
            this.font.drawString(this.x + 147, this.y + 426, currentResolution, Text.FontType.BODY, 1, true);
         }

         this.fullScreenResolutionUp.render(this.g, this.mouse, ">>>>", this.x + 256, this.y + 415, "$ORA0", "$ORA1", false, true, debug);
      } else {
         this.font
            .drawString(this.x + 148, this.y + 413, Text.getText("sharedSettingsPanel.videoConfiguration.fixedResolution"), Text.FontType.HEADER, 1, true);
         this.fullScreenResolutionDown.render(this.g, this.mouse, "<<<<", this.x + 37, this.y + 415, "$ORA0", "$ORA1", true, true, debug);
         this.font
            .drawString(
               this.x + 147, this.y + 426, Text.getText("sharedSettingsPanel.videoConfiguration.fullscreenWithoutBorderlessOnly"), Text.FontType.BODY, 0, true
            );
         this.fullScreenResolutionUp.render(this.g, this.mouse, ">>>>", this.x + 256, this.y + 415, "$ORA0", "$ORA1", true, true, debug);
      }

      this.font.drawString(this.x + 108, this.y + 451, Text.getText("sharedSettingsPanel.videoConfiguration.vSync"), Text.FontType.BODY, 2, true);
      if (this.currentBaseProperties.getProperty("vSync").equals("false")) {
         this.vSync
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.false"), this.x + 238, this.y + 451, "$RED0", "$RED1", false, true, debug
            );
      } else {
         this.vSync
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.true"), this.x + 238, this.y + 451, "$GRE0", "$GRE1", false, true, debug
            );
      }

      this.font.drawString(this.x + 446, this.y + 16, Text.getText("sharedSettingsPanel.performance.header"), Text.FontType.HEADER, 3, true);
      this.font.drawString(this.x + 406, this.y + 52, Text.getText("sharedSettingsPanel.performance.particleAmount"), Text.FontType.BODY, 2, true);
      if (this.currentBaseProperties.getProperty("particleAmount").equals("2")) {
         this.particleAmount
            .render(
               this.g,
               this.mouse,
               Text.getText("sharedSettingsPanel.performance.particleAmountFull"),
               this.x + 536,
               this.y + 52,
               "$GRE0",
               "$GRE1",
               false,
               true,
               debug
            );
      } else if (this.currentBaseProperties.getProperty("particleAmount").equals("1")) {
         this.particleAmount
            .render(
               this.g,
               this.mouse,
               Text.getText("sharedSettingsPanel.performance.particleAmountReduced"),
               this.x + 536,
               this.y + 52,
               "$YEL0",
               "$YEL1",
               false,
               true,
               debug
            );
      } else if (this.currentBaseProperties.getProperty("particleAmount").equals("0")) {
         this.particleAmount
            .render(
               this.g,
               this.mouse,
               Text.getText("sharedSettingsPanel.performance.particleAmountMinimal"),
               this.x + 536,
               this.y + 52,
               "$RED0",
               "$RED1",
               false,
               true,
               debug
            );
      }

      this.font.drawString(this.x + 406, this.y + 86, Text.getText("sharedSettingsPanel.performance.shadows"), Text.FontType.BODY, 2, true);
      if (this.currentBaseProperties.getProperty("shadows").equals("false")) {
         this.shadows
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.false"), this.x + 536, this.y + 86, "$RED0", "$RED1", false, true, debug
            );
      } else {
         this.shadows
            .render(this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.true"), this.x + 536, this.y + 86, "$GRE0", "$GRE1", false, true, debug);
      }

      this.font.drawString(this.x + 406, this.y + 120, Text.getText("sharedSettingsPanel.performance.autosaveFrequency"), Text.FontType.BODY, 2, true);
      if (this.currentBaseProperties.getProperty("autosaveFrequency").equals("2")) {
         this.autosaveFrequency
            .render(
               this.g,
               this.mouse,
               Text.getText("sharedSettingsPanel.performance.autosaveFrequencyFull"),
               this.x + 536,
               this.y + 120,
               "$GRE0",
               "$GRE1",
               false,
               true,
               debug
            );
      } else if (this.currentBaseProperties.getProperty("autosaveFrequency").equals("1")) {
         this.autosaveFrequency
            .render(
               this.g,
               this.mouse,
               Text.getText("sharedSettingsPanel.performance.autosaveFrequencyReduced"),
               this.x + 536,
               this.y + 120,
               "$YEL0",
               "$YEL1",
               false,
               true,
               debug
            );
      } else if (this.currentBaseProperties.getProperty("autosaveFrequency").equals("0")) {
         this.autosaveFrequency
            .render(
               this.g,
               this.mouse,
               Text.getText("sharedSettingsPanel.performance.autosaveFrequencyMinimal"),
               this.x + 536,
               this.y + 120,
               "$RED0",
               "$RED1",
               false,
               true,
               debug
            );
      }

      this.font.drawString(this.x + 406, this.y + 154, Text.getText("sharedSettingsPanel.performance.clouds"), Text.FontType.BODY, 2, true);
      if (this.currentBaseProperties.getProperty("clouds").equals("false")) {
         this.clouds
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.false"), this.x + 536, this.y + 154, "$RED0", "$RED1", false, true, debug
            );
      } else {
         this.clouds
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.true"), this.x + 536, this.y + 154, "$GRE0", "$GRE1", false, true, debug
            );
      }

      this.font.drawString(this.x + 406, this.y + 188, Text.getText("sharedSettingsPanel.performance.background"), Text.FontType.BODY, 2, true);
      if (this.currentBaseProperties.getProperty("background").equals("false")) {
         this.background
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.false"), this.x + 536, this.y + 188, "$RED0", "$RED1", false, true, debug
            );
      } else {
         this.background
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.true"), this.x + 536, this.y + 188, "$GRE0", "$GRE1", false, true, debug
            );
      }

      this.font.drawString(this.x + 446, this.y + 248, Text.getText("sharedSettingsPanel.screenshake.header"), Text.FontType.HEADER, 3, true);
      this.font.drawString(this.x + 406, this.y + 285, Text.getText("sharedSettingsPanel.screenshake.enabled"), Text.FontType.BODY, 2, true);
      if (this.currentProfileProperties.getProperty("screenshakeEnabled").equals("false")) {
         this.screenshakeEnabled
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.false"), this.x + 536, this.y + 285, "$RED0", "$RED1", false, true, debug
            );
      } else {
         this.screenshakeEnabled
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.true"), this.x + 536, this.y + 285, "$GRE0", "$GRE1", false, true, debug
            );
      }

      this.font.drawString(this.x + 446, this.y + 345, Text.getText("sharedSettingsPanel.tips.header"), Text.FontType.HEADER, 3, true);
      this.font.drawString(this.x + 406, this.y + 382, Text.getText("sharedSettingsPanel.tips.enabled"), Text.FontType.BODY, 2, true);
      if (this.currentProfileProperties.getProperty("tipsEnabled").equals("false")) {
         this.tipsEnabled
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.false"), this.x + 536, this.y + 382, "$RED0", "$RED1", false, true, debug
            );
      } else {
         this.tipsEnabled
            .render(
               this.g, this.mouse, Text.getText("sharedSettingsPanel.miscellaneous.true"), this.x + 536, this.y + 382, "$GRE0", "$GRE1", false, true, debug
            );
      }

      this.font.drawString(this.x + 446, this.y + 441, Text.getText("sharedSettingsPanel.interfaceScale.header"), Text.FontType.HEADER, 3, true);
      double interfaceScale = Double.parseDouble(this.currentBaseProperties.getProperty("forceInterfaceScale", "-1"));
      if (interfaceScale == -1.0) {
         this.font.drawString(this.x + 445, this.y + 474, Text.getText("sharedSettingsPanel.interfaceScale.auto"), Text.FontType.BODY, 2, true);
         this.font.drawString(this.x + 445, this.y + 489, Text.getText("sharedSettingsPanel.interfaceScale.recommended"), Text.FontType.BODY, 0, true);
      } else if (ScaleControl.MIN_HEIGHT * interfaceScale >= Display.getHeight() || ScaleControl.MIN_WIDTH * interfaceScale >= Display.getWidth()) {
         this.font.drawString(this.x + 425, this.y + 474, interfaceScale + "x", Text.FontType.BODY, 2, false);
         this.font.drawString(this.x + 445, this.y + 489, Text.getText("sharedSettingsPanel.interfaceScale.tooHighWarning"), Text.FontType.BODY, 0, true);
      } else if (interfaceScale % 1.0 != 0.0) {
         this.font.drawString(this.x + 425, this.y + 474, interfaceScale + "x", Text.FontType.BODY, 2, false);
         this.font.drawString(this.x + 445, this.y + 489, Text.getText("sharedSettingsPanel.interfaceScale.visualErrorsWarning"), Text.FontType.BODY, 0, true);
      } else {
         this.font.drawString(this.x + 425, this.y + 474, interfaceScale + "x", Text.FontType.BODY, 2, false);
         this.font.drawString(this.x + 445, this.y + 489, Text.getText("sharedSettingsPanel.interfaceScale.recommended"), Text.FontType.BODY, 0, true);
      }

      this.interfaceScaleDown.render(this.g, this.mouse, "<<<<", this.x + 335, this.y + 477, "$ORA0", "$ORA1", false, true, debug);
      this.interfaceScaleUp.render(this.g, this.mouse, ">>>>", this.x + 555, this.y + 477, "$ORA0", "$ORA1", false, true, debug);
      this.font.drawString(this.x + 446, this.y + 541, Text.getText("sharedSettingsPanel.twitch.header"), Text.FontType.HEADER, 3, true);
      this.twitchChannel.render(this.g, this.mouse, this.x + 317, this.y + 577, debug);
      this.font.drawString(this.x + 733, this.y + 16, Text.getText("sharedSettingsPanel.keys.header"), Text.FontType.HEADER, 3, true);
      int alignY = 0;

      for (int i = 0; i < this.keysString.size(); i++) {
         this.font.drawString(this.x + 625, this.y + 53 + alignY, this.keysString.get(i), Text.FontType.BODY, 0, false);
         String keyName = this.keys.get(i).getButtonName();
         String key = this.currentProfileProperties.getProperty(keyName, Text.getText("sharedSettingsPanel.keys.notSet")).toUpperCase();
         if (key.equals("")) {
            key = Text.getText("sharedSettingsPanel.keys.notSet");
         }

         this.keys.get(i).render(this.g, this.mouse, key, this.x + 829, this.y + 53 + alignY, "$YEL0", "$YEL1", false, debug);
         alignY += 10;
      }

      this.ok.render(this.g, this.mouse, this.x, this.y + 559, false, debug);
      this.cancel.render(this.g, this.mouse, this.x + 102, this.y + 559, false, debug);
      this.reset.render(this.g, this.mouse, this.x + this.settingsPanel.getWidth() - 137, this.y + 559, false, debug);
   }

   public void renderKeyChange(boolean debug) throws SlickException {
      int x2 = ScaleControl.getInterfaceWidth() / 2 - this.keyChangePanel.getWidth() / 2;
      int y2 = ScaleControl.getInterfaceHeight() / 2 - this.keyChangePanel.getHeight() / 2 - 100;
      this.keyChangeMask.setX(x2);
      this.keyChangeMask.setY(y2);
      this.keyChangePanel.draw(x2, y2);
      this.font
         .drawString(x2 + this.keyChangePanel.getWidth() / 2, y2 + 16, Text.getText("sharedSettingsPanel.changeKeyPanel.header"), Text.FontType.HEADER, 3, true);
      Text.setVariableText("keyName", this.keyChangeOldKey);
      this.font
         .drawString(x2 + this.keyChangePanel.getWidth() / 2, y2 + 59, Text.getText("sharedSettingsPanel.changeKeyPanel.message"), Text.FontType.BODY, 2, true);
   }

   public Properties getSettingsBaseProperties() {
      return this.currentBaseProperties;
   }

   public Properties getSettingsProfileProperties() {
      return this.currentProfileProperties;
   }

   public void settingsReloadProperties() {
      try {
         this.currentBaseProperties = new Properties();
         File settings = new File("profiles/settings.properties");
         FileInputStream in = new FileInputStream(settings);
         this.currentBaseProperties.load(in);
         in.close();
      } catch (IOException var4) {
         Console.out("Main Menu failed to load base properties!", true);
      }

      try {
         this.currentProfileProperties = new Properties();
         File settings = new File("profiles/profile" + Game.getActiveProfile() + "/profileSettings.properties");
         FileInputStream in = new FileInputStream(settings);
         this.currentProfileProperties.load(in);
         in.close();
      } catch (IOException var3) {
         Console.out("Main Menu failed to load profile properties!", true);
      }

      this.twitchChannel.setText(this.currentProfileProperties.getProperty("twitchChannel", ""));
   }

   public void setSettingsBaseSetting(String setting, String setTo) {
      this.currentBaseProperties.setProperty(setting, setTo);
   }

   public void setSettingsProfileSetting(String setting, String setTo) {
      this.currentProfileProperties.setProperty(setting, setTo);
   }

   public void setFocusSettingsTwitchChannel() {
      this.twitchChannel.setFocus(true);
   }

   public String getSettingsTwitchChannel() {
      return this.twitchChannel.getText();
   }

   public void resetSettingsTwitchChannel() {
      this.twitchChannel.setFocus(false);
      this.twitchChannel.setText("");
   }

   public boolean isSettingsTwitchChannelFocused() {
      return this.twitchChannel.hasFocus();
   }

   public boolean settingsIntersectsKey() {
      for (int i = 0; i < this.keys.size(); i++) {
         if (this.keys.get(i).intersects(this.mouse)) {
            return true;
         }
      }

      return false;
   }

   public String getSettingsKey() {
      for (int i = 0; i < this.keys.size(); i++) {
         if (this.keys.get(i).intersects(this.mouse)) {
            return this.keys.get(i).getButtonName();
         }
      }

      return null;
   }

   public void settingsToggleMapScroll() {
      if (this.currentProfileProperties.getProperty("mapScrollInverted").equals("true")) {
         this.currentProfileProperties.setProperty("mapScrollInverted", "false");
      } else {
         this.currentProfileProperties.setProperty("mapScrollInverted", "true");
      }
   }

   public void settingsToggleEdgeScrolling() {
      if (this.currentProfileProperties.getProperty("edgeScrollingEnabled").equals("true")) {
         this.currentProfileProperties.setProperty("edgeScrollingEnabled", "false");
      } else {
         this.currentProfileProperties.setProperty("edgeScrollingEnabled", "true");
      }
   }

   public void increaseSettingsEdgeScrollingAmount() {
      int edgeAmount = Integer.parseInt(this.currentProfileProperties.getProperty("edgeScrollSpeed", "10"));
      if (edgeAmount < 200) {
         edgeAmount += 10;
      }

      this.currentProfileProperties.setProperty("edgeScrollSpeed", Integer.toString(edgeAmount));
   }

   public void decreaseSettingsEdgeScrollingAmount() {
      int edgeAmount = Integer.parseInt(this.currentProfileProperties.getProperty("edgeScrollSpeed", "10"));
      if (edgeAmount > 20) {
         edgeAmount -= 10;
      }

      this.currentProfileProperties.setProperty("edgeScrollSpeed", Integer.toString(edgeAmount));
   }

   public void settingsToggleFullScreen() {
      if (this.currentBaseProperties.getProperty("fullScreen").equals("true")) {
         this.currentBaseProperties.setProperty("fullScreen", "false");
      } else {
         this.currentBaseProperties.setProperty("fullScreen", "true");
      }
   }

   public void settingsToggleBorderlessFullScreen() {
      if (this.currentBaseProperties.getProperty("borderlessFullScreen").equals("true")) {
         this.currentBaseProperties.setProperty("borderlessFullScreen", "false");
      } else {
         this.currentBaseProperties.setProperty("borderlessFullScreen", "true");
      }
   }

   public void increaseFullScreenResolution() {
      int width = Integer.parseInt(this.currentBaseProperties.getProperty("fullScreenResolutionW", "0"));
      int height = Integer.parseInt(this.currentBaseProperties.getProperty("fullScreenResolutionH", "0"));
      String currentResolution = width + "x" + height;
      String newResolution = this.validFullScreenResolutions.get(0);

      for (int x = 0; x < this.validFullScreenResolutions.size(); x++) {
         if (this.validFullScreenResolutions.get(x).equals(currentResolution)) {
            if (x == this.validFullScreenResolutions.size() - 1) {
               newResolution = this.validFullScreenResolutions.get(0);
            } else {
               newResolution = this.validFullScreenResolutions.get(x + 1);
            }
            break;
         }
      }

      String w = newResolution.split("x")[0];
      String h = newResolution.split("x")[1];
      this.currentBaseProperties.setProperty("fullScreenResolutionW", w);
      this.currentBaseProperties.setProperty("fullScreenResolutionH", h);
   }

   public void decreaseFullScreenResolution() {
      int width = Integer.parseInt(this.currentBaseProperties.getProperty("fullScreenResolutionW", "0"));
      int height = Integer.parseInt(this.currentBaseProperties.getProperty("fullScreenResolutionH", "0"));
      String currentResolution = width + "x" + height;
      String newResolution = this.validFullScreenResolutions.get(0);

      for (int x = 0; x < this.validFullScreenResolutions.size(); x++) {
         if (this.validFullScreenResolutions.get(x).equals(currentResolution)) {
            if (x == 0) {
               newResolution = this.validFullScreenResolutions.get(this.validFullScreenResolutions.size() - 1);
            } else {
               newResolution = this.validFullScreenResolutions.get(x - 1);
            }
            break;
         }
      }

      String w = newResolution.split("x")[0];
      String h = newResolution.split("x")[1];
      this.currentBaseProperties.setProperty("fullScreenResolutionW", w);
      this.currentBaseProperties.setProperty("fullScreenResolutionH", h);
   }

   public void settingsToggleVSync() {
      if (this.currentBaseProperties.getProperty("vSync").equals("true")) {
         this.currentBaseProperties.setProperty("vSync", "false");
      } else {
         this.currentBaseProperties.setProperty("vSync", "true");
      }
   }

   public void settingsToggleParticleAmount() {
      if (this.currentBaseProperties.getProperty("particleAmount").equals("2")) {
         this.currentBaseProperties.setProperty("particleAmount", "1");
      } else if (this.currentBaseProperties.getProperty("particleAmount").equals("1")) {
         this.currentBaseProperties.setProperty("particleAmount", "0");
      } else if (this.currentBaseProperties.getProperty("particleAmount").equals("0")) {
         this.currentBaseProperties.setProperty("particleAmount", "2");
      }
   }

   public void settingsToggleShadows() {
      if (this.currentBaseProperties.getProperty("shadows").equals("true")) {
         this.currentBaseProperties.setProperty("shadows", "false");
      } else {
         this.currentBaseProperties.setProperty("shadows", "true");
      }
   }

   public void settingsToggleAutosaveFrequency() {
      if (this.currentBaseProperties.getProperty("autosaveFrequency").equals("2")) {
         this.currentBaseProperties.setProperty("autosaveFrequency", "1");
      } else if (this.currentBaseProperties.getProperty("autosaveFrequency").equals("1")) {
         this.currentBaseProperties.setProperty("autosaveFrequency", "0");
      } else if (this.currentBaseProperties.getProperty("autosaveFrequency").equals("0")) {
         this.currentBaseProperties.setProperty("autosaveFrequency", "2");
      }
   }

   public void settingsToggleClouds() {
      if (this.currentBaseProperties.getProperty("clouds").equals("true")) {
         this.currentBaseProperties.setProperty("clouds", "false");
      } else {
         this.currentBaseProperties.setProperty("clouds", "true");
      }
   }

   public void settingsToggleBackground() {
      if (this.currentBaseProperties.getProperty("background").equals("true")) {
         this.currentBaseProperties.setProperty("background", "false");
      } else {
         this.currentBaseProperties.setProperty("background", "true");
      }
   }

   public void settingsToggleTipsEnabled() {
      if (this.currentProfileProperties.getProperty("tipsEnabled").equals("true")) {
         this.currentProfileProperties.setProperty("tipsEnabled", "false");
      } else {
         this.currentProfileProperties.setProperty("tipsEnabled", "true");
      }
   }

   public void increaseSettingsInterfaceScale() {
      double interfaceScale = Double.parseDouble(this.currentBaseProperties.getProperty("forceInterfaceScale", "-1"));
      if (interfaceScale == -1.0) {
         interfaceScale = 1.0;
      } else if (interfaceScale < 4.0) {
         interfaceScale += 0.1;
      }

      String scaleOut = Double.toString(interfaceScale);
      scaleOut = scaleOut.substring(0, 3);
      this.currentBaseProperties.setProperty("forceInterfaceScale", scaleOut);
   }

   public void decreaseSettingsInterfaceScale() {
      double interfaceScale = Double.parseDouble(this.currentBaseProperties.getProperty("forceInterfaceScale", "-1"));
      if (interfaceScale <= 1.0) {
         interfaceScale = -1.0;
      } else {
         interfaceScale -= 0.1;
      }

      String scaleOut = Double.toString(interfaceScale);
      scaleOut = scaleOut.substring(0, 3);
      this.currentBaseProperties.setProperty("forceInterfaceScale", scaleOut);
   }

   public void settingsToggleScreenshakeEnabled() {
      if (this.currentProfileProperties.getProperty("screenshakeEnabled").equals("true")) {
         this.currentProfileProperties.setProperty("screenshakeEnabled", "false");
      } else {
         this.currentProfileProperties.setProperty("screenshakeEnabled", "true");
      }
   }

   public void increaseSettingsMusicVolume() {
      int musicVolume = Integer.parseInt(this.currentBaseProperties.getProperty("musicVolume", "8"));
      if (musicVolume < 25) {
         musicVolume++;
      }

      this.currentBaseProperties.setProperty("musicVolume", Integer.toString(musicVolume));
      this.sound.setCurrentMusicVolume(musicVolume);
   }

   public void decreaseSettingsMusicVolume() {
      int musicVolume = Integer.parseInt(this.currentBaseProperties.getProperty("musicVolume", "8"));
      if (musicVolume > 0) {
         musicVolume--;
      }

      this.currentBaseProperties.setProperty("musicVolume", Integer.toString(musicVolume));
      this.sound.setCurrentMusicVolume(musicVolume);
   }

   public void increaseSettingsSoundVolume() throws SlickException {
      int soundVolume = Integer.parseInt(this.currentBaseProperties.getProperty("soundVolume", "15"));
      if (soundVolume < 25) {
         soundVolume++;
      }

      this.currentBaseProperties.setProperty("soundVolume", Integer.toString(soundVolume));
      this.sound.setSoundVolume(soundVolume);
      this.sound.playSound(SoundModule.SoundType.VILLAGER_LEVEL_UP);
   }

   public void decreaseSettingsSoundVolume() throws SlickException {
      int soundVolume = Integer.parseInt(this.currentBaseProperties.getProperty("soundVolume", "15"));
      if (soundVolume > 0) {
         soundVolume--;
      }

      this.currentBaseProperties.setProperty("soundVolume", Integer.toString(soundVolume));
      this.sound.setSoundVolume(soundVolume);
      this.sound.playSound(SoundModule.SoundType.VILLAGER_LEVEL_UP);
   }

   public void settingsReset() {
      SettingsParser.getDefaultBaseSettings(this.currentBaseProperties);
      SettingsParser.getDefaultProfileSettings(this.currentProfileProperties);
      this.twitchChannel.setText("");
   }

   public String getSettingsKeyChangeOldKey() {
      return this.keyChangeOldKey;
   }

   public void setSettingsKeyChangeOldKey(String m) {
      this.keyChangeOldKey = m;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public void setKeyName(String m) {
      this.keyName = m;
   }

   public boolean isKeyChangeMode() {
      return this.keyChangeMode;
   }

   public void setKeyChangeMode(boolean b) {
      this.keyChangeMode = b;
   }

   @Override
   public int getCenterX() {
      return this.x + this.settingsPanel.getWidth() / 2;
   }

   @Override
   public int getCenterY() {
      return this.y + this.settingsPanel.getHeight() / 2;
   }

   @Override
   public int getLeftX() {
      return this.x;
   }

   @Override
   public int getTopY() {
      return this.y;
   }

   @Override
   public int getRightX() {
      return this.x + this.settingsPanel.getWidth();
   }

   @Override
   public int getBottomY() {
      return this.y + this.settingsPanel.getHeight();
   }
}
