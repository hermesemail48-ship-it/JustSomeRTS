package rtr.gui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class PlayStateGUIPanelBase extends GUIPanelBase {
   protected PlayStateGUIController controller;
   protected PlayStateGUIData guiData;

   public PlayStateGUIPanelBase(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) {
      super(g, gc, mouse);
      this.controller = controller;
      this.guiData = guiData;
   }
}
