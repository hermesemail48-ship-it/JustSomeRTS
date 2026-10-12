package rtr.gui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class WorldMapGUIPanelBase extends GUIPanelBase {
   protected WorldMapGUIController controller;
   protected WorldMapGUIData guiData;

   public WorldMapGUIPanelBase(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) {
      super(g, gc, mouse);
      this.controller = controller;
      this.guiData = guiData;
   }
}
