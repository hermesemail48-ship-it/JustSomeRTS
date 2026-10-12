package rtr.gui.states.mapeditor;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonInputText;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.buttons.GUIOvalButton;
import rtr.gui.buttons.GUIRoundButton;
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.MapEditorGUIData;
import rtr.gui.states.MapEditorGUIPanelBase;

public class RegionalDataPanel extends MapEditorGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/gameState/dataViewsPanel.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private GUIClickableText[] regionalDataList;
   private GUIRoundButton[] regionalDataClear;
   private GUIButtonInputText worldMapXInput;
   private GUIButtonInputText worldMapYInput;
   private GUIOvalButton worldMapCoordinatesSave;
   private int regionSlotSelected;

   public RegionalDataPanel(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
      this.regionalDataList = new GUIClickableText[8];
      this.regionalDataClear = new GUIRoundButton[8];
      this.worldMapXInput = new GUIButtonInputText(gc, "worldMapXInput", "worldMapX", 4, 2, 1);
      this.guiButtons.add(this.worldMapXInput);
      this.worldMapYInput = new GUIButtonInputText(gc, "worldMapYInput", "worldMapY", 4, 2, 1);
      this.guiButtons.add(this.worldMapYInput);
      this.worldMapXInput.setText(Integer.toString(this.map.getWorldMapX()));
      this.worldMapYInput.setText(Integer.toString(this.map.getWorldMapY()));
      this.worldMapCoordinatesSave = new GUIOvalButton("worldMapCoordinatesSave", Text.getText("mapEditorRegionalDataPanel.button.save"), 0);
      this.guiButtons.add(this.worldMapCoordinatesSave);

      for (int i = 0; i < this.regionalDataList.length; i++) {
         this.regionalDataList[i] = new GUIClickableText("regionalDataList" + i, 1);
         this.guiButtons.add(this.regionalDataList[i]);
         this.regionalDataClear[i] = new GUIRoundButton("regionalDataClear" + i, "buttonDismantle", 0);
         this.guiButtons.add(this.regionalDataClear[i]);
      }
   }

   public void render(boolean debug) throws SlickException {
      this.x = this.controller.getSystemAndDataBar().getX() + this.panel.getWidth() - 228;
      if (this.x + this.panel.getWidth() > this.controller.getRightPanel().getLeftX()) {
         this.x = this.x - Math.abs(this.x + this.panel.getWidth() - this.controller.getRightPanel().getLeftX());
      }

      this.y = 49;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + 94, this.y + 3, Text.getText("mapEditorRegionalDataPanel.header"), Text.FontType.HEADER, 2, true);
      this.font.drawString(this.x + 94, this.y + 30, Text.getText("mapEditorRegionalDataPanel.connectedRegions"), Text.FontType.BODY, 1, true);
      int alignY = 0;

      for (int i = 0; i < this.regionalDataList.length; i++) {
         if (this.map.getConnectedRegionList()[i].equals("null")) {
            this.regionalDataList[i]
               .render(
                  this.g,
                  this.mouse,
                  Text.getText("mapEditorRegionalDataPanel.notSetPlaceholder"),
                  this.x + 37,
                  this.y + 56 + alignY,
                  "$GRE1",
                  "$ORA1",
                  false,
                  debug
               );
         } else {
            this.regionalDataClear[i].render(this.g, this.mouse, this.x + 18, this.y + 54 + alignY, debug);
            this.regionalDataList[i]
               .render(this.g, this.mouse, this.map.getConnectedRegionList()[i], this.x + 37, this.y + 56 + alignY, "$GRE1", "$WHI0", false, debug);
         }

         alignY += 20;
      }

      this.worldMapXInput.render(this.g, this.mouse, this.x + 23, this.y + 260, debug);
      this.font.drawString(this.x + 65, this.y + 258, "x", Text.FontType.HEADER, 2, true);
      this.worldMapYInput.render(this.g, this.mouse, this.x + 75, this.y + 260, debug);
      this.font.drawString(this.x + 94, this.y + 243, Text.getText("mapEditorRegionalDataPanel.worldMapCoordinates"), Text.FontType.BODY, 1, true);
      this.worldMapCoordinatesSave.render(this.g, this.mouse, this.x + 125, this.y + 258, debug);
   }

   public boolean intersectsRegionList() {
      for (int i = 0; i < this.regionalDataList.length; i++) {
         if (this.regionalDataList[i].intersects(this.mouse)) {
            this.regionSlotSelected = i;
            return true;
         }
      }

      return false;
   }

   public boolean intersectsRegionClearList() {
      for (int i = 0; i < this.regionalDataClear.length; i++) {
         if (this.regionalDataClear[i].intersects(this.mouse)) {
            this.regionSlotSelected = i;
            return true;
         }
      }

      return false;
   }

   public int getRegionSlotSelected() {
      return this.regionSlotSelected;
   }

   public void setFocusWorldMapXInput() {
      this.worldMapXInput.setFocus(true);
   }

   public String getWorldMapXInput() {
      return this.worldMapXInput.getText();
   }

   public void setFocusWorldMapYInput() {
      this.worldMapYInput.setFocus(true);
   }

   public String getWorldMapYInput() {
      return this.worldMapYInput.getText();
   }

   public void resetWorldMapInput() {
      this.worldMapXInput.setText(Integer.toString(this.map.getWorldMapX()));
      this.worldMapYInput.setText(Integer.toString(this.map.getWorldMapY()));
   }

   @Override
   public int getCenterX() {
      return this.x + this.panel.getWidth() / 2;
   }

   @Override
   public int getCenterY() {
      return this.y + this.panel.getHeight() / 2;
   }

   @Override
   public int getLeftX() {
      return this.x;
   }

   @Override
   public int getTopY() {
      return this.y;
   }

   @Override
   public int getRightX() {
      return this.x + this.panel.getWidth();
   }

   @Override
   public int getBottomY() {
      return this.y + this.panel.getHeight();
   }
}
