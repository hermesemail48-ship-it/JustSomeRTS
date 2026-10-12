package rtr.gui.states.mainmenu;

import rtr.utilities.JavaCompat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.buttons.GUIButtonMapList;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MainMenuGUIPanelBase;
import rtr.save.RegionalSavedGame;
import rtr.save.SavedGamesHandler;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class SkirmishPanel extends MainMenuGUIPanelBase {
   private Image panel;
   private Image panelSaveRollback;
   private Image mapPreview;
   private GUIButtonMainMenu newGame;
   private GUIButtonMainMenu resumeGame;
   private GUIButtonMainMenu resumeGameResetAI;
   private GUIClickableText gameModeNightmare;
   private GUIClickableText gameModeSurvival;
   private GUIClickableText gameModeTraditional;
   private GUIClickableText gameModePeaceful;
   private GUIClickableText gameModeSandbox;
   private GUIButtonMapList mapList;
   private RegionalSavedGame selectedSave;
   private GameModeTemplateBase.GameMode selectedGameMode = GameModeTemplateBase.GameMode.SURVIVAL;
   private GUIClickableText[] saveSlot = new GUIClickableText[7];

   public SkirmishPanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/mainMenu/mainMenuPlay.png");
      this.panelSaveRollback = ImageLoader.getImage("res/GUI/mainMenu/mainMenuPlaySaveRollback.png");
      this.mapPreview = ImageLoader.getImage("res/ungeneratedMap.bmp");
      this.newGame = new GUIButtonMainMenu("newGame", Text.getText("mainMenuSkirmishPanel.button.newGame"), 1);
      this.guiButtons.add(this.newGame);
      this.resumeGame = new GUIButtonMainMenu("resumeGame", Text.getText("mainMenuSkirmishPanel.button.resumeGame"), 2);
      this.guiButtons.add(this.resumeGame);
      this.resumeGameResetAI = new GUIButtonMainMenu("resumeGameResetAI", Text.getText("mainMenuSkirmishPanel.button.resumeGameResetAI"), 2);
      this.guiButtons.add(this.resumeGameResetAI);
      this.gameModeNightmare = new GUIClickableText("gameModeNightmare", 2);
      this.guiButtons.add(this.gameModeNightmare);
      this.gameModeSurvival = new GUIClickableText("gameModeSurvival", 2);
      this.guiButtons.add(this.gameModeSurvival);
      this.gameModeTraditional = new GUIClickableText("gameModeTraditional", 2);
      this.guiButtons.add(this.gameModeTraditional);
      this.gameModePeaceful = new GUIClickableText("gameModePeaceful", 2);
      this.guiButtons.add(this.gameModePeaceful);
      this.gameModeSandbox = new GUIClickableText("gameModeSandbox", 2);
      this.guiButtons.add(this.gameModeSandbox);
      this.gameModeSurvival.select();
      this.mapList = new GUIButtonMapList("OfficialMaps", 10, true, true);

      for (int i = 0; i < this.saveSlot.length; i++) {
         this.saveSlot[i] = new GUIClickableText("saveSlot" + i, 2);
         this.guiButtons.add(this.saveSlot[i]);
      }

      this.selectFirstMap();
   }

   public void render(boolean debug) {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.panel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() / 2 - this.panel.getHeight() / 2;
      if (this.x < 256) {
         this.x = 256;
      }

      this.mapPreview.draw(this.x + 623, this.y + 51);
      this.panel.draw(this.x, this.y);
      if (this.selectedSave != null) {
         String mapName = this.selectedSave.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapName", "null");

         while (Text.getWidth(mapName, Text.FontType.HEADER, 3) > 250) {
            mapName = mapName.substring(0, mapName.length() - 1);
         }

         this.font.drawString(this.x + 753, this.y + 16, "$ORA1" + mapName, Text.FontType.HEADER, 3, true);
         this.g.setColor(new Color(0, 0, 0, 150));
         this.g.fillRect(this.x + 625, this.y + 52, 253.0F, 22.0F);
         Text.setVariableText("authorName", this.selectedSave.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapAuthor", "null"));
         this.font.drawString(this.x + 628, this.y + 52, Text.getText("mainMenuSkirmishPanel.mapAuthor"), Text.FontType.BODY, 2, false);
      }

      this.mapList.render(this.g, this.mouse, this.x + 304, this.y + 22, debug);
      this.font.drawString(this.x + 130, this.y + 360, Text.getText("mainMenuSkirmishPanel.header.gameMode"), Text.FontType.HEADER, 3, true);
      this.gameModeNightmare
         .render(this.g, this.mouse, Text.getText("mainMenuSkirmishPanel.button.nightmare"), this.x + 111, this.y + 410, "$WHI0", "$RED1", false, true, debug);
      this.gameModeSurvival
         .render(this.g, this.mouse, Text.getText("mainMenuSkirmishPanel.button.survival"), this.x + 197, this.y + 410, "$WHI0", "$ORA1", false, true, debug);
      this.gameModeTraditional
         .render(this.g, this.mouse, Text.getText("mainMenuSkirmishPanel.button.traditional"), this.x + 111, this.y + 430, "$WHI0", "$GRE1", false, true, debug);
      this.gameModePeaceful
         .render(this.g, this.mouse, Text.getText("mainMenuSkirmishPanel.button.peaceful"), this.x + 197, this.y + 430, "$WHI0", "$CYA1", false, true, debug);
      this.gameModeSandbox
         .render(this.g, this.mouse, Text.getText("mainMenuSkirmishPanel.button.sandbox"), this.x + 153, this.y + 450, "$WHI0", "$MAG1", false, true, debug);
      if (this.selectedGameMode == GameModeTemplateBase.GameMode.NIGHTMARE) {
         this.font.drawString(this.x + 153, this.y + 473, Text.getText("mainMenuSkirmishPanel.gameMode.header.nightmare"), Text.FontType.HEADER, 3, true);
         this.font
            .drawString(
               this.x + 153,
               this.y + 500,
               Text.wrapString(Text.getText("mainMenuSkirmishPanel.gameMode.body.nightmare"), Text.FontType.BODY, 1, 230),
               Text.FontType.BODY,
               1,
               true
            );
      } else if (this.selectedGameMode == GameModeTemplateBase.GameMode.SURVIVAL) {
         this.font.drawString(this.x + 153, this.y + 473, Text.getText("mainMenuSkirmishPanel.gameMode.header.survival"), Text.FontType.HEADER, 3, true);
         this.font
            .drawString(
               this.x + 153,
               this.y + 500,
               Text.wrapString(Text.getText("mainMenuSkirmishPanel.gameMode.body.survival"), Text.FontType.BODY, 1, 230),
               Text.FontType.BODY,
               1,
               true
            );
      } else if (this.selectedGameMode == GameModeTemplateBase.GameMode.TRADITIONAL) {
         this.font.drawString(this.x + 153, this.y + 473, Text.getText("mainMenuSkirmishPanel.gameMode.header.traditional"), Text.FontType.HEADER, 3, true);
         this.font
            .drawString(
               this.x + 153,
               this.y + 500,
               Text.wrapString(Text.getText("mainMenuSkirmishPanel.gameMode.body.traditional"), Text.FontType.BODY, 1, 230),
               Text.FontType.BODY,
               1,
               true
            );
      } else if (this.selectedGameMode == GameModeTemplateBase.GameMode.PEACEFUL) {
         this.font.drawString(this.x + 153, this.y + 473, Text.getText("mainMenuSkirmishPanel.gameMode.header.peaceful"), Text.FontType.HEADER, 3, true);
         this.font
            .drawString(
               this.x + 153,
               this.y + 500,
               Text.wrapString(Text.getText("mainMenuSkirmishPanel.gameMode.body.peaceful"), Text.FontType.BODY, 1, 230),
               Text.FontType.BODY,
               1,
               true
            );
      } else if (this.selectedGameMode == GameModeTemplateBase.GameMode.SANDBOX) {
         this.font.drawString(this.x + 153, this.y + 473, Text.getText("mainMenuSkirmishPanel.gameMode.header.sandbox"), Text.FontType.HEADER, 3, true);
         this.font
            .drawString(
               this.x + 153,
               this.y + 500,
               Text.wrapString(Text.getText("mainMenuSkirmishPanel.gameMode.body.sandbox"), Text.FontType.BODY, 1, 230),
               Text.FontType.BODY,
               1,
               true
            );
      }

      String mapPackFolderName = null;
      String mapFileName = null;
      if (this.selectedSave != null) {
         mapPackFolderName = this.selectedSave.getMapPackFolderName();
         mapFileName = this.selectedSave.getMapFileName();
      }

      if (mapFileName == null) {
         this.resumeGame.render(this.g, this.mouse, this.x + 662, this.y + 337, true, debug);
         this.newGame.render(this.g, this.mouse, this.x + 682, this.y + 397, true, debug);
      } else if (!SettingsParser.getDebug() && !this.gc.getInput().isKeyDown(42)) {
         if (SavedGamesHandler.regionalSaveExist(mapPackFolderName, mapFileName, StateBase.GameType.SKIRMISH, this.selectedGameMode)) {
            this.resumeGame.render(this.g, this.mouse, this.x + 662, this.y + 337, false, debug);
         } else {
            this.resumeGame.render(this.g, this.mouse, this.x + 662, this.y + 337, true, debug);
         }

         this.newGame.render(this.g, this.mouse, this.x + 682, this.y + 397, false, debug);
      } else {
         if (SavedGamesHandler.regionalSaveExist(mapPackFolderName, mapFileName, StateBase.GameType.SKIRMISH, this.selectedGameMode)) {
            this.resumeGame.render(this.g, this.mouse, this.x + 662, this.y + 337, false, debug);
            this.resumeGameResetAI.render(this.g, this.mouse, this.x + 662, this.y + 397, false, debug);
            this.renderSaveRollback(this.x + 302, this.y + 344, debug);
         } else {
            this.resumeGame.render(this.g, this.mouse, this.x + 662, this.y + 337, true, debug);
            this.resumeGameResetAI.render(this.g, this.mouse, this.x + 662, this.y + 397, true, debug);
         }

         this.newGame.render(this.g, this.mouse, this.x + 682, this.y + 457, false, debug);
      }
   }

   private void renderSaveRollback(float x, float y, boolean debug) {
      this.panelSaveRollback.draw(x, y);
      this.font.drawString(x + 123.0F, y + 16.0F, Text.getText("mainMenuSkirmishPanel.header.saveRollback"), Text.FontType.HEADER, 3, true);
      float alignY = 0.0F;
      ArrayList<File> savesList = SavedGamesHandler.getSortedListOfSaves(this.selectedSave.getBaseSaveFolder(), this.selectedSave.getGameMode());
      if (savesList.size() > 0) {
         savesList.remove(0);

         for (int i = 0; i < savesList.size() && i < this.saveSlot.length; i++) {
            File thisSave = savesList.get(i);
            String parsedDate = JavaCompat.fileTimeIso(thisSave);
            parsedDate = parsedDate.replaceAll("T", " at ");
            parsedDate = parsedDate.substring(0, 19);
            this.saveSlot[i].render(this.g, this.mouse, parsedDate, (int)(x + 53.0F), (int)(y + 68.0F + alignY), "$YEL0", "$GRE0", false, false, debug);
            alignY += 18.0F;
         }
      }
   }

   public void selectMap() throws SlickException {
      this.selectedSave = this.mapList.getMapSave(this.mouse, this.selectedGameMode);
      this.refreshMap();
      this.mapList.selectMap(this.mouse);
   }

   public void selectFirstMap() throws SlickException {
      if (this.mapList.getTotalMaps() > 0) {
         this.selectedSave = this.mapList.getMapSave(this.selectedGameMode, 0);
         this.refreshMap();
         this.mapList.selectMap(0);
      } else {
         this.mapPreview = ImageLoader.getImage("res/ungeneratedMap.bmp");
         this.selectedSave = null;
      }
   }

   public void refreshMap() throws SlickException {
      try {
         this.mapPreview = this.selectedSave.getMiniMapImagePreview();
      } catch (Exception var2) {
         this.mapPreview = ImageLoader.getImage("res/ungeneratedMap.bmp");
      }
   }

   public void mapListUp() {
      this.mapList.mapListPageUp();
   }

   public void mapListDown() {
      this.mapList.mapListPageDown();
   }

   public void mapPackUp() throws SlickException {
      this.mapList.mapPackUp();
      this.selectFirstMap();
   }

   public void mapPackDown() throws SlickException {
      this.mapList.mapPackDown();
      this.selectFirstMap();
   }

   public RegionalSavedGame getMapSave() {
      return this.selectedSave;
   }

   public boolean intersectsMapList() {
      return this.mapList.intersects(this.mouse);
   }

   public boolean intersectsMapListPageUp() {
      return this.mapList.intersectsPageUp(this.mouse);
   }

   public boolean intersectsMapListPageDown() {
      return this.mapList.intersectsPageDown(this.mouse);
   }

   public boolean intersectsMapPackLeft() {
      return this.mapList.intersectsMapPackLeft(this.mouse);
   }

   public boolean intersectsMapPackRight() {
      return this.mapList.intersectsMapPackRight(this.mouse);
   }

   public void releaseMapResources() throws SlickException {
      this.mapList.deleteMapProperties(this.selectedSave);
      this.selectedSave = null;
      this.mapPreview = null;
   }

   public void reloadMapList() throws SlickException {
      this.mapList = new GUIButtonMapList("OfficialMaps", 10, true, true);
      this.selectFirstMap();
   }

   public void setGameMode(GameModeTemplateBase.GameMode gM) throws SlickException {
      this.unselectAllModes();
      this.selectedGameMode = gM;
      if (SavedGamesHandler.regionalSaveExist(this.selectedSave.getMapPackFolderName(), this.selectedSave.getMapFileName(), StateBase.GameType.SKIRMISH, gM)) {
         this.selectedSave = SavedGamesHandler.getRegionalSave(
            this.selectedSave.getMapPackFolderName(), this.selectedSave.getMapFileName(), StateBase.GameType.SKIRMISH, gM
         );
      } else {
         this.selectedSave = SavedGamesHandler.getRegionalSave(
            this.selectedSave.getMapPackFolderName(), this.selectedSave.getMapFileName(), StateBase.GameType.SKIRMISH, GameModeTemplateBase.GameMode.MAP_EDITOR
         );
      }

      if (this.selectedGameMode == GameModeTemplateBase.GameMode.NIGHTMARE) {
         this.gameModeNightmare.select();
      }

      if (this.selectedGameMode == GameModeTemplateBase.GameMode.SURVIVAL) {
         this.gameModeSurvival.select();
      }

      if (this.selectedGameMode == GameModeTemplateBase.GameMode.TRADITIONAL) {
         this.gameModeTraditional.select();
      }

      if (this.selectedGameMode == GameModeTemplateBase.GameMode.PEACEFUL) {
         this.gameModePeaceful.select();
      }

      if (this.selectedGameMode == GameModeTemplateBase.GameMode.SANDBOX) {
         this.gameModeSandbox.select();
      }

      this.refreshMap();
   }

   public void unselectAllModes() throws SlickException {
      this.gameModeNightmare.deselect();
      this.gameModeSurvival.deselect();
      this.gameModeTraditional.deselect();
      this.gameModeSandbox.deselect();
      this.gameModePeaceful.deselect();
   }

   public GameModeTemplateBase.GameMode getSelectedGameMode() {
      return this.selectedGameMode;
   }

   @Override
   public int getCenterX() {
      return this.x + this.panel.getWidth() / 2;
   }

   @Override
   public int getCenterY() {
      return this.y + this.panel.getHeight() / 2;
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
      return this.x + this.panel.getWidth();
   }

   @Override
   public int getBottomY() {
      return this.y + this.panel.getHeight();
   }
}
