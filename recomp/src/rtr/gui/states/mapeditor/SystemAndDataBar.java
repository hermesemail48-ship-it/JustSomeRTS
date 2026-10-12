package rtr.gui.states.mapeditor;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.buttons.GUISquarePanelButton;
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.MapEditorGUIData;
import rtr.gui.states.MapEditorGUIPanelBase;
import rtr.system.ScaleControl;

public class SystemAndDataBar extends MapEditorGUIPanelBase {
   private GUISquarePanelButton mainMenu = new GUISquarePanelButton("mainMenu", SettingsParser.getKeyMainMenuName(), 0, "mainMenu");
   private GUISquarePanelButton saveMap;
   private GUISquarePanelButton minimap;
   private GUISquarePanelButton dataViews;
   private GUISquarePanelButton hideTopography;
   private GUISquarePanelButton music;
   private GUISquarePanelButton regionalData;

   public SystemAndDataBar(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      Text.setVariableText("shortcutKey", SettingsParser.getKeyMainMenuName());
      this.mainMenu
         .setToolTip(Text.getText("mapEditorSystemAndDataBar.button.tooltip.mainMenuHeader"), Text.getText("mapEditorSystemAndDataBar.button.tooltip.mainMenu"));
      this.guiButtons.add(this.mainMenu);
      this.saveMap = new GUISquarePanelButton("saveMap", 0, "saveMap");
      this.saveMap.setToolTip(Text.getText("mapEditorSystemAndDataBar.button.tooltip.saveMapHeader"));
      this.guiButtons.add(this.saveMap);
      this.minimap = new GUISquarePanelButton("minimap", SettingsParser.getKeyMinimapName(), 0, "minimap");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyMinimapName());
      this.minimap
         .setToolTip(Text.getText("mapEditorSystemAndDataBar.button.tooltip.minimapHeader"), Text.getText("mapEditorSystemAndDataBar.button.tooltip.minimap"));
      this.guiButtons.add(this.minimap);
      this.dataViews = new GUISquarePanelButton("dataViews", SettingsParser.getKeyDataViewsName(), 0, "dataViews");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyDataViewsName());
      this.dataViews
         .setToolTip(
            Text.getText("mapEditorSystemAndDataBar.button.tooltip.dataViewsHeader"), Text.getText("mapEditorSystemAndDataBar.button.tooltip.dataViews")
         );
      this.guiButtons.add(this.dataViews);
      this.hideTopography = new GUISquarePanelButton("hideTopography", SettingsParser.getKeyHideTopographyName(), 0, "hideTopography");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyHideTopographyName());
      this.hideTopography
         .setToolTip(
            Text.getText("mapEditorSystemAndDataBar.button.tooltip.hideTopographyHeader"),
            Text.getText("mapEditorSystemAndDataBar.button.tooltip.hideTopography")
         );
      this.guiButtons.add(this.hideTopography);
      this.music = new GUISquarePanelButton("music", 0, "music");
      this.music
         .setToolTip(Text.getText("mapEditorSystemAndDataBar.button.tooltip.musicHeader"), Text.getText("mapEditorSystemAndDataBar.button.tooltip.music"));
      this.guiButtons.add(this.music);
      this.regionalData = new GUISquarePanelButton("regionalData", 0, "migration");
      this.regionalData
         .setToolTip(
            Text.getText("mapEditorSystemAndDataBar.button.tooltip.regionalDataHeader"), Text.getText("mapEditorSystemAndDataBar.button.tooltip.regionalData")
         );
      this.guiButtons.add(this.regionalData);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() - 512;
      this.y = 3;
      int alignX = 0;
      this.mainMenu.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.mainMenu.getWidth();
      this.saveMap.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.saveMap.getWidth() + 8;
      this.minimap.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.minimap.getWidth();
      this.dataViews.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.dataViews.getWidth() + 8;
      this.hideTopography.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.hideTopography.getWidth() + 8;
      this.music.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.music.getWidth() + 8;
      if (SettingsParser.getDebug()) {
         this.regionalData.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
         alignX += this.regionalData.getWidth();
      }
   }

   @Override
   public void update() {
      if (this.controller.isMainMenuPanelEnabled()) {
         this.mainMenu.select();
      } else {
         this.mainMenu.deselect();
      }

      if (this.controller.isMinimapPanelEnabled()) {
         this.minimap.select();
      } else {
         this.minimap.deselect();
      }

      if (this.controller.isMusicPanelEnabled()) {
         this.music.select();
      } else {
         this.music.deselect();
      }
   }
}
