package rtr.gui.states.playstate;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.states.StateBase;

public class MainMenuPanel extends PlayStateGUIPanelBase {
   private Image panel = ImageLoader.getImage("res/GUI/gameState/mainMenuPanel.png");
   private Rectangle mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
   private GUIButtonGenericWood settings;
   private GUIButtonGenericWood goToWorldMap;
   private GUIButtonGenericWood returnToMenu;
   private GUIButtonGenericWood exitGame;
   private GUIButtonGenericWood areYouSure;
   private boolean attemptExit;
   private boolean attemptLeaveRegion;

   public MainMenuPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.masks.add(this.mask);
      this.settings = new GUIButtonGenericWood("settings", Text.getText("playMainMenuPanel.button.settings"));
      this.guiButtons.add(this.settings);
      this.goToWorldMap = new GUIButtonGenericWood("goToWorldMap", Text.getText("playMainMenuPanel.button.goToWorldMap"));
      this.guiButtons.add(this.goToWorldMap);
      this.returnToMenu = new GUIButtonGenericWood("returnToMenu", Text.getText("playMainMenuPanel.button.returnToMenu"));
      this.guiButtons.add(this.returnToMenu);
      this.exitGame = new GUIButtonGenericWood("exitGame", Text.getText("playMainMenuPanel.button.exitGame"));
      this.guiButtons.add(this.exitGame);
      this.areYouSure = new GUIButtonGenericWood("areYouSure", Text.getText("playMainMenuPanel.button.yes"));
      this.guiButtons.add(this.areYouSure);
   }

   public void render(boolean gamePaused, boolean debug) throws SlickException {
      if (gamePaused) {
         this.x = 6;
         this.y = 6;
      } else {
         this.x = this.controller.getResourceAndInfluenceBar().getRightX() - this.panel.getWidth() - 397;
         if (this.x + this.panel.getWidth() > this.controller.getRightPanel().getLeftX() - 46) {
            this.x = this.x - Math.abs(this.x + this.panel.getWidth() - this.controller.getRightPanel().getLeftX() + 46);
         }

         this.y = 133;
      }

      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + 83, this.y + 3, Text.getText("playMainMenuPanel.header"), Text.FontType.HEADER, 2, true);
      this.settings.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + 32, false, true, debug);
      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP) {
         this.goToWorldMap.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + 60, false, true, debug);
      } else {
         this.returnToMenu.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + 60, false, true, debug);
      }

      this.exitGame.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + 88, false, true, debug);
      if (!this.attemptExit && !this.attemptLeaveRegion) {
         this.areYouSure.hideButton();
      } else {
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2 + 2, this.y + 120, Text.getText("playMainMenuPanel.saveAndExitWarningMessage"), Text.FontType.BODY, 1, true
            );
         this.areYouSure.render(this.g, this.mouse, this.x + this.panel.getWidth() / 2, this.y + 135, false, true, debug);
      }
   }

   public void attemptExit() {
      this.attemptExit = true;
   }

   public void resetAttemptExit() {
      this.attemptExit = false;
   }

   public boolean getAttemptExit() {
      return this.attemptExit;
   }

   public void attemptLeaveRegion() {
      this.attemptLeaveRegion = true;
   }

   public void resetAttemptLeaveRegion() {
      this.attemptLeaveRegion = false;
   }

   public boolean getAttemptLeaveRegion() {
      return this.attemptLeaveRegion;
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
