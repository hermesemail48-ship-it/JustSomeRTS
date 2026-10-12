package rtr.gui.states.playstate;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.buttons.GUISquarePanelButton;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.map.MapTilesLoader;
import rtr.system.ScaleControl;

public class ToolsBar extends PlayStateGUIPanelBase {
   private GUISquarePanelButton pauseObjects = new GUISquarePanelButton("pauseObjects", SettingsParser.getKeyPauseObjectsName(), 0, "pauseObjects");
   private GUISquarePanelButton dismantleObjects;
   private GUISquarePanelButton destroyTerrain;

   public ToolsBar(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      Text.setVariableText("shortcutKey", SettingsParser.getKeyPauseObjectsName());
      this.pauseObjects
         .setToolTip(Text.getText("playToolsBar.button.tooltip.pauseBuildingsHeader"), Text.getText("playToolsBar.button.tooltip.pauseBuildings"));
      this.guiButtons.add(this.pauseObjects);
      this.dismantleObjects = new GUISquarePanelButton("dismantleObjects", SettingsParser.getKeyDismantleObjectsName(), 0, "dismantleObjects");
      Text.setVariableText("shortcutKey", SettingsParser.getKeyDismantleObjectsName());
      this.dismantleObjects
         .setToolTip(Text.getText("playToolsBar.button.tooltip.dismantleBuildingsHeader"), Text.getText("playToolsBar.button.tooltip.dismantleBuildings"));
      this.guiButtons.add(this.dismantleObjects);
      this.destroyTerrain = new GUISquarePanelButton("destroyTerrain", SettingsParser.getKeyDestroyTerrainName(), 0, "destroyTerrain");
      this.destroyTerrain
         .setToolTip(Text.getText("playToolsBar.button.tooltip.destroyTerrainHeader"), Text.getText("playToolsBar.button.tooltip.destroyTerrain"));
      this.guiButtons.add(this.destroyTerrain);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() - 141;
      this.y = ScaleControl.getInterfaceHeight() - 49;
      int alignX = 0;
      this.pauseObjects.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.pauseObjects.getWidth();
      this.dismantleObjects.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.dismantleObjects.getWidth();
      this.destroyTerrain.render(this.g, this.mouse, this.x + alignX, this.y, this.object.objectCount(MapTilesLoader.TileSet.CASTLE_1) == 0, debug);
      alignX += this.destroyTerrain.getWidth();
   }
}
