package rtr.gui.states.shared;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.MapEditorGUIData;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.SharedGUIPanelBase;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.system.ScaleControl;

public class EdgeScrolling extends SharedGUIPanelBase {
   private Rectangle north;
   private Rectangle east;
   private Rectangle south;
   private Rectangle west;

   public EdgeScrolling(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   public EdgeScrolling(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   public EdgeScrolling(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   private void init() {
      this.north = new Rectangle(0.0F, 0.0F, 1.0F, 1.0F);
      this.east = new Rectangle(0.0F, 0.0F, 1.0F, 1.0F);
      this.south = new Rectangle(0.0F, 0.0F, 1.0F, 1.0F);
      this.west = new Rectangle(0.0F, 0.0F, 1.0F, 1.0F);
   }

   public void render(boolean debug) throws SlickException {
      this.north.setBounds(0.0F, 0.0F, ScaleControl.getInterfaceWidth(), 10.0F);
      this.east.setBounds(ScaleControl.getInterfaceWidth() - 10, 0.0F, 10.0F, ScaleControl.getInterfaceHeight());
      this.south.setBounds(0.0F, ScaleControl.getInterfaceHeight() - 10, ScaleControl.getInterfaceWidth(), 10.0F);
      this.west.setBounds(0.0F, 0.0F, 10.0F, ScaleControl.getInterfaceHeight());
   }

   @Override
   public void renderMasks() {
      super.renderMasks();
      this.g.setColor(Color.magenta);
      this.g.draw(this.north);
      this.g.draw(this.east);
      this.g.draw(this.south);
      this.g.draw(this.west);
   }

   public boolean intersectsNorth(Rectangle mouse) {
      return this.north.intersects(mouse);
   }

   public boolean intersectsEast(Rectangle mouse) {
      return this.east.intersects(mouse);
   }

   public boolean intersectsSouth(Rectangle mouse) {
      return this.south.intersects(mouse);
   }

   public boolean intersectsWest(Rectangle mouse) {
      return this.west.intersects(mouse);
   }
}
