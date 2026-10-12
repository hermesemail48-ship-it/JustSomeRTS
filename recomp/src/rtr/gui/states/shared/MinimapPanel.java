package rtr.gui.states.shared;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.GUIEnums;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.buttons.GUIClickableArea;
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.MapEditorGUIData;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.SharedGUIPanelBase;

public class MinimapPanel extends SharedGUIPanelBase {
   private Image panel;
   private Image panelLarge;
   private Rectangle mask;
   private GUIButtonGenericWood sizeToggleSmall;
   private GUIButtonGenericWood sizeToggleLarge;
   private GUIClickableArea center;
   private float minimapX;
   private float minimapY;
   private int minimapScale = 1;

   public MinimapPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   public MinimapPanel(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.init();
   }

   private void init() throws SlickException {
      this.panel = ImageLoader.getImage("res/GUI/gameState/miniMapPanel.png");
      this.panelLarge = ImageLoader.getImage("res/GUI/gameState/miniMapLargePanel.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);
      this.sizeToggleSmall = new GUIButtonGenericWood("sizeToggleSmall", Text.getText("sharedMinimapPanel.button.small"));
      this.guiButtons.add(this.sizeToggleSmall);
      this.sizeToggleLarge = new GUIButtonGenericWood("sizeToggleLarge", Text.getText("sharedMinimapPanel.button.large"));
      this.guiButtons.add(this.sizeToggleLarge);
      this.center = new GUIClickableArea("center", 256, 256);
      this.guiButtons.add(this.center);
   }

   public void render(boolean debug) throws SlickException {
      if (this.minimapScale == 1) {
         this.renderMiniMapSmall(this.x, this.y, debug);
      } else if (this.minimapScale == 2) {
         this.renderMiniMapLarge(this.x, this.y, debug);
      }
   }

   private void renderMiniMapSmall(int x, int y, boolean debug) throws SlickException {
      GUIEnums.InterfaceMode interfaceMode = null;
      if (this.playStateController != null) {
         x = this.playStateController.getResourceAndInfluenceBar().getRightX() - this.panel.getWidth();
         if (x + this.panel.getWidth() > this.playStateController.getRightPanel().getLeftX() - 46) {
            x -= Math.abs(x + this.panel.getWidth() - this.playStateController.getRightPanel().getLeftX() + 46);
         }

         y = 133;
         interfaceMode = this.playStateController.getGUIData().getInterfaceMode();
      }

      if (this.mapEditorController != null) {
         x = this.mapEditorController.getSystemAndDataBar().getX();
         if (x + this.panel.getWidth() > this.mapEditorController.getRightPanel().getLeftX()) {
            x -= Math.abs(x + this.panel.getWidth() - this.mapEditorController.getRightPanel().getLeftX());
         }

         y = 49;
         interfaceMode = this.mapEditorController.getGUIData().getInterfaceMode();
      }

      this.mask.setBounds(x, y, this.panel.getWidth(), this.panel.getHeight());
      this.minimapX = x + 17;
      this.minimapY = y + 28;
      this.miniMap.render(x + 17, y + 28, this.minimapScale, this.g, interfaceMode, false);
      this.center.setIntersectBoxSize(256 * this.minimapScale, 256 * this.minimapScale);
      this.center.render(this.g, this.mouse, x + 17, y + 28, false, debug);
      this.panel.draw(x, y);
      this.sizeToggleSmall.render(this.g, this.mouse, x + 183, y, false, true, debug);
      this.font.drawString(x + 89, y + 3, Text.getText("sharedMinimapPanel.header"), Text.FontType.HEADER, 2, true);
   }

   private void renderMiniMapLarge(int x, int y, boolean debug) throws SlickException {
      GUIEnums.InterfaceMode interfaceMode = null;
      if (this.playStateController != null) {
         x = this.playStateController.getResourceAndInfluenceBar().getRightX() - this.panelLarge.getWidth() + 179;
         if (x + this.panelLarge.getWidth() > this.playStateController.getRightPanel().getLeftX() - 46) {
            x -= Math.abs(x + this.panelLarge.getWidth() - this.playStateController.getRightPanel().getLeftX() + 46);
         }

         y = 133;
         interfaceMode = this.playStateController.getGUIData().getInterfaceMode();
      }

      if (this.mapEditorController != null) {
         x = this.mapEditorController.getSystemAndDataBar().getX() + 179;
         if (x + this.panelLarge.getWidth() > this.mapEditorController.getRightPanel().getLeftX()) {
            x -= Math.abs(x + this.panelLarge.getWidth() - this.mapEditorController.getRightPanel().getLeftX());
         }

         y = 49;
         interfaceMode = this.mapEditorController.getGUIData().getInterfaceMode();
      }

      this.mask.setBounds(x, y, this.panelLarge.getWidth(), this.panelLarge.getHeight());
      this.minimapX = x + 17;
      this.minimapY = y + 28;
      this.miniMap.render(x + 17, y + 28, this.minimapScale, this.g, interfaceMode, false);
      this.center.setIntersectBoxSize(256 * this.minimapScale, 256 * this.minimapScale);
      this.center.render(this.g, this.mouse, x + 17, y + 28, false, debug);
      this.panelLarge.draw(x, y);
      this.sizeToggleLarge.render(this.g, this.mouse, x + 183, y, false, true, debug);
      this.font.drawString(x + 89, y + 3, Text.getText("sharedMinimapPanel.header"), Text.FontType.HEADER, 2, true);
   }

   public float getMinimapX() {
      return this.minimapX;
   }

   public float getMinimapY() {
      return this.minimapY;
   }

   public float getMinimapScale() {
      return this.minimapScale;
   }

   public void toggleMinimapScale() {
      if (this.minimapScale == 1) {
         this.minimapScale = 2;
      } else {
         this.minimapScale = 1;
      }
   }

   public int getWidth() {
      return this.minimapScale == 1 ? this.panel.getWidth() : this.panelLarge.getWidth();
   }
}
