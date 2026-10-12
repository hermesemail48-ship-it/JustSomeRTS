package rtr.gui.states.worldmap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.buttons.GUISquarePanelButton;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.gui.states.WorldMapGUIPanelBase;
import rtr.system.ScaleControl;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class SystemPanel extends WorldMapGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/gameState/systemPanel.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private GUISquarePanelButton mainMenu;
   private GUISquarePanelButton doomWorld;
   private GUISquarePanelButton viewGameModeConfig;
   private GUIButtonGenericWood changeGameMode;

   public SystemPanel(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
      this.mainMenu = new GUISquarePanelButton("mainMenu", 0, "mainMenu");
      this.mainMenu.setToolTip(Text.getText("worldMapSystemPanel.button.tooltip.mainMenuHeader"), Text.getText("worldMapSystemPanel.button.tooltip.mainMenu"));
      this.guiButtons.add(this.mainMenu);
      this.doomWorld = new GUISquarePanelButton("doomWorld", 0, "doomWorld");
      this.doomWorld
         .setToolTip(Text.getText("worldMapSystemPanel.button.tooltip.doomWorldHeader"), Text.getText("worldMapSystemPanel.button.tooltip.doomWorld"));
      this.guiButtons.add(this.doomWorld);
      this.viewGameModeConfig = new GUISquarePanelButton("viewGameModeConfig", 0, "viewGameModeConfig");
      this.viewGameModeConfig
         .setToolTip(
            Text.getText("worldMapSystemPanel.button.tooltip.viewGameModeConfigHeader"), Text.getText("worldMapSystemPanel.button.tooltip.viewGameModeConfig")
         );
      this.guiButtons.add(this.viewGameModeConfig);
      this.changeGameMode = new GUIButtonGenericWood("changeGameMode", Text.getText("worldMapSystemPanel.button.changeGameMode"));
      this.guiButtons.add(this.changeGameMode);
   }

   public void render(boolean debug) throws SlickException {
      this.x = ScaleControl.getInterfaceWidth() / 2 + 78;
      this.y = 7;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + 152, this.y + 1, Text.getText("worldMapSystemPanel.header"), Text.FontType.HEADER, 3, true);
      int alignX = 0;
      this.mainMenu.render(this.g, this.mouse, this.x + 22 + alignX, this.y + 33, false, debug);
      alignX += this.mainMenu.getWidth();
      this.doomWorld.render(this.g, this.mouse, this.x + 22 + alignX, this.y + 33, false, debug);
      alignX += this.doomWorld.getWidth();
      if (WorldMapGUIData.getSelectedGameMode() == GameModeTemplateBase.GameMode.CUSTOM) {
         this.viewGameModeConfig.render(this.g, this.mouse, this.x + 22 + alignX, this.y + 33, false, debug);
      }

      alignX += this.viewGameModeConfig.getWidth();
      this.changeGameMode.render(this.g, this.mouse, this.x + 22 + alignX, this.y + 33, false, debug);
      GameModeTemplateBase.GameMode gM = WorldMapGUIData.getSelectedGameMode();
      this.font.drawString(this.x + alignX + 85, this.y + 59, gM.getName(), Text.FontType.BODY, 3, true);
   }

   @Override
   public void update() {
      if (this.controller.isDoomWorldPanelEnabled()) {
         this.doomWorld.select();
      } else {
         this.doomWorld.deselect();
      }
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
