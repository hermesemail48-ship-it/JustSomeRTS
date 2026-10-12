package rtr.gui.buttons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.font.Text;
import rtr.save.RegionalSavedGame;
import rtr.save.SavedGamesHandler;
import rtr.states.StateBase;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class GUIButtonMapList extends GUIButton {
   private ArrayList<GUIClickableText> mapList = new ArrayList<>();
   private ArrayList<String> mapNames = new ArrayList<>();
   private ArrayList<String> mapFileNames = new ArrayList<>();
   private GUIClickableText buttonMapListDown;
   private GUIClickableText buttonMapListUp;
   private int mapListStart = 0;
   private int mapPage = 1;
   private int mapListLength;
   private GUIClickableText mapPackLeft;
   private GUIClickableText mapPackRight;
   private Image saveGameIcons;
   private SpriteSheet saveGameIconSheet;
   private Image nightmareSaveIcon;
   private Image survivalSaveIcon;
   private Image traditionalSaveIcon;
   private Image peacefulSaveIcon;
   private Image sandBoxSaveIcon;
   private Image nightmareNoSaveIcon;
   private Image survivalNoSaveIcon;
   private Image traditionalNoSaveIcon;
   private Image peacefulNoSaveIcon;
   private Image sandBoxNoSaveIcon;
   private ArrayList<String> mapPackName = new ArrayList<>();
   private ArrayList<String> mapPackList = new ArrayList<>();
   private int currentPosition = 0;

   public GUIButtonMapList(String mP, int length, boolean allowSaves, boolean allowOfficial) throws SlickException {
      this.mapListLength = length;
      this.saveGameIcons = ImageLoader.getImage("res/GUI/saveGameIcons.png");
      this.saveGameIconSheet = new SpriteSheet(this.saveGameIcons, 16, 16);
      this.nightmareSaveIcon = this.saveGameIconSheet.getSprite(0, 0);
      this.survivalSaveIcon = this.saveGameIconSheet.getSprite(1, 0);
      this.traditionalSaveIcon = this.saveGameIconSheet.getSprite(2, 0);
      this.peacefulSaveIcon = this.saveGameIconSheet.getSprite(3, 0);
      this.sandBoxSaveIcon = this.saveGameIconSheet.getSprite(4, 0);
      this.nightmareNoSaveIcon = this.saveGameIconSheet.getSprite(0, 1);
      this.survivalNoSaveIcon = this.saveGameIconSheet.getSprite(1, 1);
      this.traditionalNoSaveIcon = this.saveGameIconSheet.getSprite(2, 1);
      this.peacefulNoSaveIcon = this.saveGameIconSheet.getSprite(3, 1);
      this.sandBoxNoSaveIcon = this.saveGameIconSheet.getSprite(4, 1);
      HashMap<String, Properties> mapPacks = SavedGamesHandler.getAllMapPackProperties();

      for (String s : mapPacks.keySet()) {
         Properties p = mapPacks.get(s);
         if (allowOfficial || !p.getProperty("mapPackOfficial", "false").equals("true")) {
            this.mapPackName.add(p.getProperty("mapPackName"));
            this.mapPackList.add(s);
         }
      }

      for (int i = 0; i < this.mapPackList.size(); i++) {
         if (this.mapPackList.get(i).equals(mP)) {
            this.currentPosition = i;
         }
      }

      this.loadMapPack();
      this.buttonMapListDown = new GUIClickableText("buttonMapListDown", 2);
      this.buttonMapListUp = new GUIClickableText("buttonMapListUp", 2);
      this.mapPackLeft = new GUIClickableText("mapPackLeft", 3);
      this.mapPackRight = new GUIClickableText("mapPackRight", 3);
   }

   @Override
   public void hideButton() {
      for (int i = 0; i < this.mapList.size(); i++) {
         this.mapList.get(i).hideButton();
      }

      this.buttonMapListDown.hideButton();
      this.buttonMapListUp.hideButton();
      this.mapPackLeft.hideButton();
      this.mapPackRight.hideButton();
   }

   private void loadMapPack() throws SlickException {
      this.mapList.clear();
      this.mapNames.clear();
      this.mapFileNames.clear();
      HashMap<String, HashMap<StateBase.GameType, HashMap<GameModeTemplateBase.GameMode, RegionalSavedGame>>> saves = SavedGamesHandler.getRegionalSaves()
         .get(this.mapPackList.get(this.currentPosition));

      for (String mapFileName : saves.keySet()) {
         RegionalSavedGame save = SavedGamesHandler.getRegionalSave(
            this.mapPackList.get(this.currentPosition), mapFileName, StateBase.GameType.SKIRMISH, GameModeTemplateBase.GameMode.MAP_EDITOR
         );
         this.mapList.add(new GUIClickableText(save.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapName", "null"), 2));
         this.mapNames.add(save.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapName", "null"));
         this.mapFileNames.add(save.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapFileName", "null"));
      }
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean debug) {
      int totalPages = this.mapList.size() / this.mapListLength;
      if (this.mapList.size() % this.mapListLength > 0) {
         totalPages++;
      }

      this.mapPackLeft.render(g, mouse, "<<<<", x - 268, y - 4, "$WHI0", "$GRE1", false, true, debug);
      this.font.drawString(x, y - 6, "$ORA1" + this.getMapPackName(), Text.FontType.HEADER, 3, true);
      this.mapPackRight.render(g, mouse, ">>>>", x + 262, y - 4, "$WHI0", "$GRE1", false, true, debug);
      Text.setVariableText("totalMaps", this.mapList.size());
      this.font.drawString(x - 270, y + 40, Text.getText("mainMenuMapListButton.totalMaps"), Text.FontType.BODY, 2);
      Text.setVariableText("currentPage", this.mapPage);
      Text.setVariableText("totalPages", totalPages);
      this.font.drawString(x + 173, y + 40, Text.getText("mainMenuMapListButton.pageList"), Text.FontType.BODY, 2);
      if (this.mapList.size() > 0) {
         for (int i = 0; i < this.mapList.size(); i++) {
            this.mapList.get(i).hideButton();
         }
      }

      int alignY = 65;
      if (this.mapList.size() > 0) {
         for (int i = 0; i < this.mapListLength; i++) {
            if (this.mapListStart + i < this.mapList.size()) {
               try {
                  this.mapList
                     .get(this.mapListStart + i)
                     .render(g, mouse, this.mapNames.get(this.mapListStart + i), x, y + alignY, "$WHI0", "$GRE1", false, true, debug);
                  if (SavedGamesHandler.regionalSaveExist(
                     this.mapPackList.get(this.currentPosition),
                     this.mapFileNames.get(this.mapListStart + i),
                     StateBase.GameType.SKIRMISH,
                     GameModeTemplateBase.GameMode.NIGHTMARE
                  )) {
                     this.nightmareSaveIcon.draw(x + 173, y + alignY - 3);
                  } else {
                     this.nightmareNoSaveIcon.draw(x + 173, y + alignY - 3);
                  }

                  if (SavedGamesHandler.regionalSaveExist(
                     this.mapPackList.get(this.currentPosition),
                     this.mapFileNames.get(this.mapListStart + i),
                     StateBase.GameType.SKIRMISH,
                     GameModeTemplateBase.GameMode.SURVIVAL
                  )) {
                     this.survivalSaveIcon.draw(x + 188, y + alignY - 3);
                  } else {
                     this.survivalNoSaveIcon.draw(x + 188, y + alignY - 3);
                  }

                  if (SavedGamesHandler.regionalSaveExist(
                     this.mapPackList.get(this.currentPosition),
                     this.mapFileNames.get(this.mapListStart + i),
                     StateBase.GameType.SKIRMISH,
                     GameModeTemplateBase.GameMode.TRADITIONAL
                  )) {
                     this.traditionalSaveIcon.draw(x + 203, y + alignY - 3);
                  } else {
                     this.traditionalNoSaveIcon.draw(x + 203, y + alignY - 3);
                  }

                  if (SavedGamesHandler.regionalSaveExist(
                     this.mapPackList.get(this.currentPosition),
                     this.mapFileNames.get(this.mapListStart + i),
                     StateBase.GameType.SKIRMISH,
                     GameModeTemplateBase.GameMode.PEACEFUL
                  )) {
                     this.peacefulSaveIcon.draw(x + 218, y + alignY - 3);
                  } else {
                     this.peacefulNoSaveIcon.draw(x + 218, y + alignY - 3);
                  }

                  if (SavedGamesHandler.regionalSaveExist(
                     this.mapPackList.get(this.currentPosition),
                     this.mapFileNames.get(this.mapListStart + i),
                     StateBase.GameType.SKIRMISH,
                     GameModeTemplateBase.GameMode.SANDBOX
                  )) {
                     this.sandBoxSaveIcon.draw(x + 233, y + alignY - 3);
                  } else {
                     this.sandBoxNoSaveIcon.draw(x + 233, y + alignY - 3);
                  }
               } catch (Exception var10) {
                  this.font.drawString(x, y + alignY - 3, "$RED2" + this.mapList.get(this.mapListStart + i).getButtonName(), Text.FontType.BODY, 1, true);
               }

               alignY += 20;
            }
         }
      }

      if (totalPages != 0) {
         if (this.mapPage != 1) {
            this.buttonMapListDown.render(g, mouse, Text.getText("mainMenuMapListButton.button.lastPage"), x, y + 40, "$WHI0", "$GRE1", false, true, debug);
         } else {
            this.buttonMapListDown.render(g, mouse, Text.getText("mainMenuMapListButton.button.lastPage"), x, y + 40, "$BLA2", "$BLA2", false, true, debug);
         }

         if (this.mapPage != totalPages) {
            this.buttonMapListUp
               .render(
                  g, mouse, Text.getText("mainMenuMapListButton.button.nextPage"), x, y + 70 + this.mapListLength * 20, "$WHI0", "$GRE1", false, true, debug
               );
         } else {
            this.buttonMapListUp
               .render(
                  g, mouse, Text.getText("mainMenuMapListButton.button.nextPage"), x, y + 70 + this.mapListLength * 20, "$BLA2", "$BLA2", false, true, debug
               );
         }
      }
   }

   @Override
   public boolean intersects(Rectangle mouse) {
      for (int i = 0; i < this.mapList.size(); i++) {
         if (this.mapList.get(i).intersects(mouse)) {
            return true;
         }
      }

      return false;
   }

   public String getMapPack() {
      return this.mapPackList.get(this.currentPosition);
   }

   public String getMapPackName() {
      return this.mapPackName.get(this.currentPosition);
   }

   public RegionalSavedGame getMapSave(Rectangle mouse, GameModeTemplateBase.GameMode gM) {
      for (int i = 0; i < this.mapList.size(); i++) {
         if (this.mapList.get(i).intersects(mouse)) {
            if (SavedGamesHandler.regionalSaveExist(this.mapPackList.get(this.currentPosition), this.mapFileNames.get(i), StateBase.GameType.SKIRMISH, gM)) {
               return SavedGamesHandler.getRegionalSave(this.mapPackList.get(this.currentPosition), this.mapFileNames.get(i), StateBase.GameType.SKIRMISH, gM);
            }

            return SavedGamesHandler.getRegionalSave(
               this.mapPackList.get(this.currentPosition), this.mapFileNames.get(i), StateBase.GameType.SKIRMISH, GameModeTemplateBase.GameMode.MAP_EDITOR
            );
         }
      }

      return null;
   }

   public RegionalSavedGame getMapSave(GameModeTemplateBase.GameMode gM, int m) {
      return SavedGamesHandler.regionalSaveExist(this.mapPackList.get(this.currentPosition), this.mapFileNames.get(m), StateBase.GameType.SKIRMISH, gM)
         ? SavedGamesHandler.getRegionalSave(this.mapPackList.get(this.currentPosition), this.mapFileNames.get(m), StateBase.GameType.SKIRMISH, gM)
         : SavedGamesHandler.getRegionalSave(
            this.mapPackList.get(this.currentPosition), this.mapFileNames.get(m), StateBase.GameType.SKIRMISH, GameModeTemplateBase.GameMode.MAP_EDITOR
         );
   }

   public void deleteMapProperties(RegionalSavedGame s) {
      for (int i = 0; i < this.mapFileNames.size(); i++) {
         if (this.mapFileNames.get(i).equals(s.getMapFileName())) {
            this.mapList.remove(i);
            this.mapNames.remove(i);
            this.mapFileNames.remove(i);
         }
      }
   }

   public int getTotalMaps() {
      return this.mapList.size();
   }

   public void selectMap(Rectangle mouse) {
      for (int i = 0; i < this.mapList.size(); i++) {
         if (this.mapList.get(i).intersects(mouse)) {
            this.mapList.get(i).select();
         } else {
            this.mapList.get(i).deselect();
         }
      }
   }

   public void selectMap(int m) {
      for (int i = 0; i < this.mapList.size(); i++) {
         if (i == m) {
            this.mapList.get(i).select();
         } else {
            this.mapList.get(i).deselect();
         }
      }
   }

   public boolean intersectsPageUp(Rectangle mouse) {
      return this.buttonMapListUp.intersects(mouse);
   }

   public boolean intersectsPageDown(Rectangle mouse) {
      return this.buttonMapListDown.intersects(mouse);
   }

   public void mapListPageUp() {
      if (this.mapListStart + this.mapListLength < this.mapList.size()) {
         this.mapListStart = this.mapListStart + this.mapListLength;
         this.mapPage++;
      }
   }

   public void mapListPageDown() {
      if (this.mapListStart != 0) {
         this.mapListStart = this.mapListStart - this.mapListLength;
         this.mapPage--;
      }
   }

   public void mapPackUp() throws SlickException {
      if (this.currentPosition == this.mapPackList.size() - 1) {
         this.currentPosition = 0;
      } else {
         this.currentPosition++;
      }

      this.loadMapPack();
   }

   public void mapPackDown() throws SlickException {
      if (this.currentPosition == 0) {
         this.currentPosition = this.mapPackList.size() - 1;
      } else {
         this.currentPosition--;
      }

      this.loadMapPack();
   }

   public boolean intersectsMapPackLeft(Rectangle mouse) {
      return this.mapPackLeft.intersects(mouse);
   }

   public boolean intersectsMapPackRight(Rectangle mouse) {
      return this.mapPackRight.intersects(mouse);
   }
}
