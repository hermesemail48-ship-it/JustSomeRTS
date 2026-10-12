package rtr.gui.states.mainmenu;

import java.util.Properties;
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
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MainMenuGUIPanelBase;
import rtr.save.RegionalSavedGame;
import rtr.save.SavedGamesHandler;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class MapEditorLoadMapPanel extends MainMenuGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/mainMenu/mainMenuMapEditorLoad.png");
   private Image mapPreview;
   private GUIButtonMainMenu loadMap = new GUIButtonMainMenu("loadMap", Text.getText("mapEditorLoadMapPanel.button.loadMap"), 1, 1);
   private GUIButtonMainMenu deleteMap;
   private GUIButtonMapList mapList;
   private RegionalSavedGame selectedSave;

   public MapEditorLoadMapPanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.guiButtons.add(this.loadMap);
      this.deleteMap = new GUIButtonMainMenu("deleteMap", Text.getText("mapEditorLoadMapPanel.button.deleteMap"), 2, 2);
      this.guiButtons.add(this.deleteMap);
      this.mapList = new GUIButtonMapList("UserMaps", 21, false, SettingsParser.getAllowOfficial());
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

         this.font.drawString(this.x + 753, this.y + 15, "$ORA1" + mapName, Text.FontType.HEADER, 3, true);
         this.g.setColor(new Color(0, 0, 0, 150));
         this.g.fillRect(this.x + 625, this.y + 52, 253.0F, 22.0F);
         Text.setVariableText("authorName", this.selectedSave.getModuleSaveData(ModuleBase.ModuleType.MAP).getOrDefault("mapAuthor", "null"));
         this.font.drawString(this.x + 628, this.y + 52, Text.getText("mapEditorLoadMapPanel.mapAuthor"), Text.FontType.BODY, 2, false);
      }

      this.mapList.render(this.g, this.mouse, this.x + 304, this.y + 22, debug);
      if (this.selectedSave != null) {
         boolean allowDelete = true;
         Properties mapPackProperties = SavedGamesHandler.getMapPackProperties(this.selectedSave.getMapPackFolderName());
         if (mapPackProperties.getProperty("mapPackOfficial", "false").toLowerCase().equals("true")) {
            allowDelete = false;
         }

         this.loadMap.render(this.g, this.mouse, this.x + 682, this.y + 397, false, debug);
         this.deleteMap.render(this.g, this.mouse, this.x + 662, this.y + 457, !allowDelete, debug);
      } else {
         this.loadMap.render(this.g, this.mouse, this.x + 682, this.y + 397, true, debug);
         this.deleteMap.render(this.g, this.mouse, this.x + 662, this.y + 457, true, debug);
      }
   }

   public void selectMap(Rectangle mouse) throws SlickException {
      this.selectedSave = this.mapList.getMapSave(mouse, GameModeTemplateBase.GameMode.MAP_EDITOR);
      this.mapPreview = this.selectedSave.getMiniMapImagePreview();
      this.mapList.selectMap(mouse);
   }

   public void selectFirstMap() throws SlickException {
      if (this.mapList.getTotalMaps() > 0) {
         this.selectedSave = this.mapList.getMapSave(GameModeTemplateBase.GameMode.MAP_EDITOR, 0);

         try {
            this.mapPreview = this.selectedSave.getMiniMapImagePreview();
         } catch (Exception var2) {
            this.mapPreview = ImageLoader.getImage("res/ungeneratedMap.bmp");
         }

         this.mapList.selectMap(0);
      } else {
         this.mapPreview = ImageLoader.getImage("res/ungeneratedMap.bmp");
         this.selectedSave = null;
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

   public boolean intersectsMapMapList() {
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
      this.mapPreview.destroy();
   }

   public void reloadMapList() throws SlickException {
      this.mapList = new GUIButtonMapList(this.mapList.getMapPack(), 21, false, SettingsParser.getAllowOfficial());
      this.selectFirstMap();
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
