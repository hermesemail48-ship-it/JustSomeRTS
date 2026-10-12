package rtr.gui.states.mapeditor;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.states.MapEditorGUIController;
import rtr.gui.states.MapEditorGUIData;
import rtr.gui.states.MapEditorGUIPanelBase;

public class MainMenuPanel extends MapEditorGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/gameState/mainMenuPanel.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private GUIButtonGenericWood settings;
   private GUIButtonGenericWood saveAndExit;
   private GUIButtonGenericWood exitOnly;
   private GUIButtonGenericWood exitOnlyYes;
   private boolean attemptExit;

   public MainMenuPanel(MapEditorGUIController controller, MapEditorGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
      this.settings = new GUIButtonGenericWood("settings", Text.getText("mapEditorMainMenuPanel.button.settings"));
      this.guiButtons.add(this.settings);
      this.saveAndExit = new GUIButtonGenericWood("saveAndExit", Text.getText("mapEditorMainMenuPanel.button.saveAndExit"));
      this.guiButtons.add(this.saveAndExit);
      this.exitOnly = new GUIButtonGenericWood("exitOnly", Text.getText("mapEditorMainMenuPanel.button.exitOnly"));
      this.guiButtons.add(this.exitOnly);
      this.exitOnlyYes = new GUIButtonGenericWood("exitOnlyYes", Text.getText("mapEditorMainMenuPanel.button.yes"));
      this.guiButtons.add(this.exitOnlyYes);
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
      this.font.drawString(this.x + 83, this.y + 3, Text.getText("mapEditorMainMenuPanel.header"), Text.FontType.HEADER, 2, true);
      if (!this.attemptExit) {
         this.settings.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + 40, false, true, debug);
         this.saveAndExit.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + 70, false, true, debug);
         this.exitOnly.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + 100, false, true, debug);
         this.exitOnlyYes.hideButton();
      } else {
         this.font
            .drawString(this.x + this.panel.getWidth() / 2 + 2, this.y + 50, Text.getText("mapEditorMainMenuPanel.exitWarning"), Text.FontType.BODY, 2, true);
         this.exitOnlyYes.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + 135, false, true, debug);
      }
   }

   public void attemptExit() {
      this.attemptExit = true;
   }

   public void resetAttemptExit() {
      this.attemptExit = false;
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
