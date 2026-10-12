package rtr.gui.states.mainmenu;

import java.io.File;
import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonInputText;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.buttons.GUIButtonMapPackList;
import rtr.gui.buttons.GUIPanelButton;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MainMenuGUIPanelBase;
import rtr.map.MapTilesLoader;
import rtr.system.ScaleControl;

public class MapEditorNewMapPanel extends MainMenuGUIPanelBase {
   private Image panel;
   private GUIButtonMainMenu createMap;
   private GUIButtonMainMenu deleteMapPack;
   private GUIButtonMapPackList mapPackList;
   private GUIButtonInputText mapName;
   private GUIButtonInputText mapAuthor;
   private GUIPanelButton terrainGrassGreen;
   private GUIPanelButton terrainGrassYellowBrown;
   private GUIPanelButton terrainGrassEmeraldGreen;
   private GUIPanelButton terrainGrassTeal;
   private GUIPanelButton terrainSnowWhite;
   private GUIPanelButton terrainDirtLightBrown;
   private GUIPanelButton terrainDirtBrown;
   private GUIPanelButton terrainDirtDarkBrown;
   private GUIPanelButton terrainGravelGray;
   private GUIPanelButton terrainGravelBlue;
   private GUIPanelButton terrainGravelRed;
   private GUIPanelButton terrainBricks;
   private GUIPanelButton terrainTiles;
   private GUIPanelButton terrainSandTan;
   private GUIPanelButton terrainSandBlack;
   private GUIPanelButton terrainSandRed;
   private ArrayList<GUIPanelButton> terrains = new ArrayList<>();
   private MapTilesLoader.TileSet baseTerrain = MapTilesLoader.TileSet.GRASS_GREEN;
   private String mapPackName = "UserMaps";

   public MapEditorNewMapPanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/mainMenu/mainMenuMapEditorNew.png");
      this.createMap = new GUIButtonMainMenu("createMap", Text.getText("mapEditorNewMapPanel.button.create"), 2, 1);
      this.guiButtons.add(this.createMap);
      this.deleteMapPack = new GUIButtonMainMenu("deleteMapPack", Text.getText("mapEditorNewMapPanel.button.delete"), 2, 2);
      this.guiButtons.add(this.deleteMapPack);
      this.mapName = new GUIButtonInputText(gc, "mapName", Text.getText("mapEditorNewMapPanel.textInput.name"), 32, 22, 2);
      this.guiButtons.add(this.mapName);
      this.mapAuthor = new GUIButtonInputText(gc, "mapAuthor", Text.getText("mapEditorNewMapPanel.textInput.author"), 32, 22, 2);
      this.guiButtons.add(this.mapAuthor);
      this.terrainGrassGreen = new GUIPanelButton("terrainGrassGreen", 0, "greenGrass", Text.getText("mapEditorNewMapPanel.button.grassGreen"), false);
      this.guiButtons.add(this.terrainGrassGreen);
      this.terrains.add(this.terrainGrassGreen);
      this.terrainGrassGreen.select();
      this.terrainGrassYellowBrown = new GUIPanelButton(
         "terrainGrassYellowBrown", 0, "yellowBrownGrass", Text.getText("mapEditorNewMapPanel.button.grassYellowBrown"), false
      );
      this.guiButtons.add(this.terrainGrassYellowBrown);
      this.terrains.add(this.terrainGrassYellowBrown);
      this.terrainGrassEmeraldGreen = new GUIPanelButton(
         "terrainGrassEmeraldGreen", 0, "emeraldGreenGrass", Text.getText("mapEditorNewMapPanel.button.grassEmeraldGreen"), false
      );
      this.guiButtons.add(this.terrainGrassEmeraldGreen);
      this.terrains.add(this.terrainGrassEmeraldGreen);
      this.terrainGrassTeal = new GUIPanelButton("terrainGrassTeal", 0, "grassTeal", Text.getText("mapEditorNewMapPanel.button.grassTeal"), false);
      this.guiButtons.add(this.terrainGrassTeal);
      this.terrains.add(this.terrainGrassTeal);
      this.terrainSnowWhite = new GUIPanelButton("terrainSnowWhite", 0, "snowWhite", Text.getText("mapEditorNewMapPanel.button.snowWhite"), false);
      this.guiButtons.add(this.terrainSnowWhite);
      this.terrains.add(this.terrainSnowWhite);
      this.terrainDirtLightBrown = new GUIPanelButton(
         "terrainDirtLightBrown", 0, "lightBrownDirt", Text.getText("mapEditorNewMapPanel.button.dirtLightBrown"), false
      );
      this.guiButtons.add(this.terrainDirtLightBrown);
      this.terrains.add(this.terrainDirtLightBrown);
      this.terrainDirtBrown = new GUIPanelButton("terrainDirtBrown", 0, "brownDirt", Text.getText("mapEditorNewMapPanel.button.dirtBrown"), false);
      this.guiButtons.add(this.terrainDirtBrown);
      this.terrains.add(this.terrainDirtBrown);
      this.terrainDirtDarkBrown = new GUIPanelButton(
         "terrainDirtDarkBrown", 0, "darkBrownDirt", Text.getText("mapEditorNewMapPanel.button.dirtDarkBrown"), false
      );
      this.guiButtons.add(this.terrainDirtDarkBrown);
      this.terrains.add(this.terrainDirtDarkBrown);
      this.terrainGravelGray = new GUIPanelButton("terrainGravelGray", 0, "grayGravel", Text.getText("mapEditorNewMapPanel.button.gravelGray"), false);
      this.guiButtons.add(this.terrainGravelGray);
      this.terrains.add(this.terrainGravelGray);
      this.terrainGravelBlue = new GUIPanelButton("terrainGravelBlue", 0, "blueGravel", Text.getText("mapEditorNewMapPanel.button.gravelBlue"), false);
      this.guiButtons.add(this.terrainGravelBlue);
      this.terrains.add(this.terrainGravelBlue);
      this.terrainGravelRed = new GUIPanelButton("terrainGravelRed", 0, "redGravel", Text.getText("mapEditorNewMapPanel.button.gravelRed"), false);
      this.guiButtons.add(this.terrainGravelRed);
      this.terrains.add(this.terrainGravelRed);
      this.terrainBricks = new GUIPanelButton("terrainBricks", 0, "grayBricks", Text.getText("mapEditorNewMapPanel.button.bricksGray"), false);
      this.guiButtons.add(this.terrainBricks);
      this.terrains.add(this.terrainBricks);
      this.terrainTiles = new GUIPanelButton("terrainTiles", 0, "grayTiles", Text.getText("mapEditorNewMapPanel.button.tilesGray"), false);
      this.guiButtons.add(this.terrainTiles);
      this.terrains.add(this.terrainTiles);
      this.terrainSandTan = new GUIPanelButton("terrainSandTan", 0, "tanSand", Text.getText("mapEditorNewMapPanel.button.sandTan"), false);
      this.guiButtons.add(this.terrainSandTan);
      this.terrains.add(this.terrainSandTan);
      this.terrainSandBlack = new GUIPanelButton("terrainSandBlack", 0, "blackSand", Text.getText("mapEditorNewMapPanel.button.sandBlack"), false);
      this.guiButtons.add(this.terrainSandBlack);
      this.terrains.add(this.terrainSandBlack);
      this.terrainSandRed = new GUIPanelButton("terrainSandRed", 0, "redSand", Text.getText("mapEditorNewMapPanel.button.sandRed"), false);
      this.guiButtons.add(this.terrainSandRed);
      this.terrains.add(this.terrainSandRed);
      this.mapPackList = new GUIButtonMapPackList(gc, "UserMaps", SettingsParser.getAllowOfficial());
   }

   public void render(boolean debug) {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.panel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() / 2 - this.panel.getHeight() / 2;
      if (this.x < 256) {
         this.x = 256;
      }

      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + 155, this.y + 16, Text.getText("mapEditorNewMapPanel.header.selectPack"), Text.FontType.HEADER, 3, true);
      this.mapPackList.render(this.g, this.mouse, this.x + 285, this.y + 22, debug);
      this.font.drawString(this.x + 752, this.y + 16, Text.getText("mapEditorNewMapPanel.header.mapName"), Text.FontType.HEADER, 3, true);
      this.mapName.render(this.g, this.mouse, this.x + 623, this.y + 57, debug);
      File mapFolder = new File("maps/" + this.getMapPackFileName() + "/" + this.mapName.getText().replaceAll("[^a-zA-Z0-9]", "") + "/");
      if (mapFolder.exists() && !this.mapName.getText().equals("")) {
         Text.setVariableText("mapPackName", this.mapPackName.toUpperCase());
         this.font.drawString(this.x + 800, this.y + 75, Text.getText("mapEditorNewMapPanel.mapExistsWarning"), Text.FontType.BODY, 1, true);
      }

      this.font.drawString(this.x + 752, this.y + 122, Text.getText("mapEditorNewMapPanel.header.author"), Text.FontType.HEADER, 3, true);
      this.mapAuthor.render(this.g, this.mouse, this.x + 623, this.y + 163, debug);
      this.font.drawString(this.x + 155, this.y + 368, Text.getText("mapEditorNewMapPanel.header.baseTerrain"), Text.FontType.HEADER, 3, true);
      int xShift = 0;
      int yShift = 0;

      for (int i = 0; i < this.terrains.size(); i++) {
         if (i > 0 && i % 7 == 0) {
            xShift = 0;
            yShift += this.terrains.get(i).getHeight();
         }

         this.terrains.get(i).render(this.g, this.mouse, this.x + 28 + xShift, this.y + 414 + yShift, false, debug);
         xShift += this.terrains.get(i).getWidth() + 46;
      }

      if (!this.mapName.getText().equals("") && !this.mapAuthor.getText().equals("") && !mapFolder.exists()) {
         this.createMap.render(this.g, this.mouse, this.x + 798, this.y + 214, false, debug);
      } else {
         this.createMap.render(this.g, this.mouse, this.x + 798, this.y + 214, true, debug);
      }

      boolean allowDelete = true;
      File mapDelete = new File("maps/" + this.getMapPackFileName() + "/");
      File[] listOfMapFiles = mapDelete.listFiles();
      if (!mapDelete.exists() || mapDelete.getName().toLowerCase().equals("usermaps")) {
         allowDelete = false;
      }

      if (allowDelete) {
         for (int i = 0; i < listOfMapFiles.length; i++) {
            if (listOfMapFiles[i].isDirectory()) {
               allowDelete = false;
               break;
            }
         }
      }

      this.deleteMapPack.render(this.g, this.mouse, this.x + 798, this.y + 274, !allowDelete, debug);
   }

   public void selectMapPack() throws SlickException {
      this.mapPackName = this.mapPackList.getMapPackName();
      this.mapPackList.selectMap(this.mouse);
   }

   public void setFocusMapName() {
      this.mapName.setFocus(true);
   }

   public void setFocusMapAuthor() {
      this.mapAuthor.setFocus(true);
   }

   public void mapListUp() {
      this.mapPackList.mapListPageUp();
   }

   public void mapListDown() {
      this.mapPackList.mapListPageDown();
   }

   public void setFocusMapListNewPack() {
      this.mapPackList.setFocusMapListNewPack();
   }

   public boolean intersectsMapPackListNewPack() {
      return this.mapPackList.intersectsNewPack(this.mouse);
   }

   public boolean intersectsMapPackList() {
      return this.mapPackList.intersects(this.mouse);
   }

   public boolean intersectsMapPackListPageUp() {
      return this.mapPackList.intersectsPageUp(this.mouse);
   }

   public boolean intersectsMapPackListPageDown() {
      return this.mapPackList.intersectsPageDown(this.mouse);
   }

   public void setBaseTerrain(MapTilesLoader.TileSet bT) {
      this.baseTerrain = bT;
   }

   public String getMapName() {
      return this.mapName.getText();
   }

   public String getMapFileName() {
      return this.mapName.getText().replaceAll("[^a-zA-Z0-9]", "");
   }

   public String getMapAuthor() {
      return this.mapAuthor.getText();
   }

   public MapTilesLoader.TileSet getBaseTerrain() {
      return this.baseTerrain;
   }

   public String getMapPackName() {
      return this.mapPackList.getMapPackName();
   }

   public String getMapPackFileName() {
      return this.mapPackList.getMapPackName().replaceAll("[^a-zA-Z0-9]", "");
   }

   public void reloadMapList(GameContainer gc) throws SlickException {
      this.mapPackList = new GUIButtonMapPackList(gc, "UserMaps", SettingsParser.getAllowOfficial());
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
