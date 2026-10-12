package rtr.gui.states.mapeditor;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import r2d.image.SpriteSheet;
import rtr.DataModule;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.font.FontModule;
import rtr.gui.GUIEnums;
import rtr.gui.buttons.GUIToolTipOverlay;
import rtr.gui.states.BrushLoader;
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.MapEditorGUIData;
import rtr.gui.states.MapEditorGUIPanelBase;
import rtr.map.MapModule;
import rtr.map.MapTilesLoader;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceModule;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class Overlay extends MapEditorGUIPanelBase {
   private MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   private ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   private FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   private DataModule data = (DataModule)StateBase.getModule(ModuleBase.ModuleType.DATA);
   private Image mouseLock;
   private SpriteSheet brushDots;
   private Image brushDot;
   private Image brushDotAccent;
   private Image roadPlacementSquare = ImageLoader.getImage("res/GUI/mobSelect.png");
   private GUIToolTipOverlay toolTip;
   private int toolTipTicker;
   private float mouseX;
   private float mouseY;
   private float mouseNewX;
   private float mouseNewY;

   public Overlay(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.mouseLock = ImageLoader.getImage("res/GUI/mouseLock.png");
      this.brushDots = new SpriteSheet(ImageLoader.getImage("res/GUI/brushDots.png"), 16, 16);
      this.brushDot = this.brushDots.getSprite(0, 0);
      this.brushDotAccent = this.brushDots.getSprite(1, 0);
      this.toolTip = new GUIToolTipOverlay();
   }

   public void render(boolean debug) throws SlickException {
      int tileX = Game.getCS().getMouseTileX();
      int tileY = Game.getCS().getMouseTileY();
      float mouseXPressed = Game.getCS().getMouseXPressed();
      float mouseYPressed = Game.getCS().getMouseYPressed();
      GUIEnums.InterfaceMode interfaceMode = this.controller.getGUIData().getInterfaceMode();
      if (!this.controller.intersectsAnyGUIMask()) {
         this.g.resetTransform();
         this.g.scale(ScaleControl.getWorldScale(), ScaleControl.getWorldScale());
         BrushLoader brushLoader = this.controller.getBrushLoader();
         if (interfaceMode == GUIEnums.InterfaceMode.BRUSH
            || interfaceMode == GUIEnums.InterfaceMode.ERASE
            || interfaceMode == GUIEnums.InterfaceMode.PATCH
            || interfaceMode == GUIEnums.InterfaceMode.HOLE) {
            int brushSize = brushLoader.getBrushSize();
            BrushLoader.BrushType brushType = brushLoader.getBrushType();
            this.brushDot.startUse();
            if (this.map.getAccentMode() && interfaceMode == GUIEnums.InterfaceMode.BRUSH) {
               byte[][] currentAccentBrush = brushLoader.getAccentBrush(brushSize, brushType);

               for (int x = 0; x < currentAccentBrush.length; x++) {
                  for (int y = 0; y < currentAccentBrush[x].length; y++) {
                     if (currentAccentBrush[x][y] == 2) {
                        int brushX = (int)(x * 16 + tileX * 16 - currentAccentBrush.length / 2 * 16 + this.map.getMapX());
                        int brushY = (int)(y * 16 + tileY * 16 - currentAccentBrush[x].length / 2 * 16 + this.map.getMapY());
                        this.brushDotAccent.drawEmbedded(brushX, brushY);
                     }
                  }
               }
            }

            byte[][] currentBrush = brushLoader.getBrush(brushSize, brushType);

            for (int x = 0; x < currentBrush.length; x++) {
               for (int yx = 0; yx < currentBrush[x].length; yx++) {
                  if (currentBrush[x][yx] == 2) {
                     int brushX = (int)(x * 16 + tileX * 16 - currentBrush.length / 2 * 16 + this.map.getMapX());
                     int brushY = (int)(yx * 16 + tileY * 16 - currentBrush[x].length / 2 * 16 + this.map.getMapY());
                     this.brushDot.drawEmbedded(brushX, brushY);
                  }
               }
            }

            this.brushDot.endUse();
         }

         MapTilesLoader.TileSet selectedObjectType = this.controller.getGUIData().getPlacementObjectType();
         ObjectBase.ObjectSubType objectSubTypeDisplayed = this.controller.getGUIData().getPlacementObjectSubTypeDisplayed();
         if (interfaceMode == GUIEnums.InterfaceMode.OBJECT) {
            this.data.renderObjectOutline(tileX, tileY, this.g, selectedObjectType, objectSubTypeDisplayed, true);
         }

         if (interfaceMode == GUIEnums.InterfaceMode.WALL) {
            this.data.renderWallOutline(tileX, tileY, this.g, selectedObjectType, true);
         }

         if (interfaceMode == GUIEnums.InterfaceMode.PLACE_ROAD) {
            this.roadPlacementSquare.draw(this.map.getMapX() + tileX * 16, this.map.getMapY() + tileY * 16);
         }

         this.g.resetTransform();
         this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         if (Game.getCS().isMapMoving()) {
            this.mouseLock.drawCentered(mouseXPressed, mouseYPressed);
         }
      }
   }

   public void renderToolTip(boolean debug) {
      if (!this.controller.intersectsAnyGUIMask()) {
         this.g.resetTransform();
         this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
         int tileX = Game.getCS().getMouseTileX();
         int tileY = Game.getCS().getMouseTileY();
         this.mouseNewX = this.mouse.getX();
         this.mouseNewY = this.mouse.getY();
         if (this.mouseNewX == this.mouseX && this.mouseNewY == this.mouseY && this.toolTipTicker < 30) {
            this.toolTipTicker++;
         } else if (this.toolTipTicker > 0) {
            this.toolTipTicker--;
         }

         if (!Utilities.isOutOfBoundsOfUsableMap(tileX, tileY) && this.toolTipTicker > 25) {
            int tileID = 0;

            for (int l = 11; l >= 0; l--) {
               tileID = this.map.getTileId(tileX, tileY, l);
               if (tileID != 0) {
                  break;
               }
            }

            String toolTipString = this.map.getMapTileLoader().getTileName(tileID);
            if (toolTipString != null) {
               this.toolTip.renderToolTip(this.g, this.mouse, GUIToolTipOverlay.ToolTipPosition.RIGHT, toolTipString, debug);
            }
         }

         this.mouseX = this.mouse.getX();
         this.mouseY = this.mouse.getY();
         this.g.resetTransform();
         this.g.scale(ScaleControl.getInterfaceScale(), ScaleControl.getInterfaceScale());
      }
   }
}
