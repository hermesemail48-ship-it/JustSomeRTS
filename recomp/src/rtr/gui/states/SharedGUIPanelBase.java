package rtr.gui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class SharedGUIPanelBase extends GUIPanelBase {
   protected PlayStateGUIController playStateController;
   protected PlayStateGUIData playStateGUIData;
   protected MapEditorGUIController mapEditorController;
   protected MapEditorGUIData mapEditorGUIData;
   protected MainMenuGUIController mainMenuController;
   protected MainMenuGUIData mainMenuGUIData;
   protected WorldMapGUIController worldMapController;
   protected WorldMapGUIData worldMapGUIData;

   public SharedGUIPanelBase(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) {
      super(g, gc, mouse);
      this.playStateController = controller;
      this.playStateGUIData = guiData;
   }

   public SharedGUIPanelBase(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) {
      super(g, gc, mouse);
      this.mapEditorController = controller;
      this.mapEditorGUIData = guiData;
   }

   public SharedGUIPanelBase(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) {
      super(g, gc, mouse);
      this.mainMenuController = controller;
      this.mainMenuGUIData = guiData;
   }

   public SharedGUIPanelBase(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) {
      super(g, gc, mouse);
      this.worldMapController = controller;
      this.worldMapGUIData = guiData;
   }
}
