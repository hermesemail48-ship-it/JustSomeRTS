package rtr.gui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class MapEditorGUIPanelBase extends GUIPanelBase {
   protected MapEditorGUIController controller;
   protected MapEditorGUIData guiData;

   public MapEditorGUIPanelBase(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) {
      super(g, gc, mouse);
      this.controller = controller;
      this.guiData = guiData;
   }
}
