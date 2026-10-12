package rtr.gui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class MainMenuGUIPanelBase extends GUIPanelBase {
   protected MainMenuGUIController controller;
   protected MainMenuGUIData guiData;

   public MainMenuGUIPanelBase(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) {
      super(g, gc, mouse);
      this.controller = controller;
      this.guiData = guiData;
   }
}
