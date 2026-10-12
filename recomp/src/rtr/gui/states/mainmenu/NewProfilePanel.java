package rtr.gui.states.mainmenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonInputText;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MainMenuGUIPanelBase;
import rtr.system.ScaleControl;

public class NewProfilePanel extends MainMenuGUIPanelBase {
   private Image panel;
   private GUIButtonInputText name;
   private GUIButtonInputText twitchChannel;
   private GUIButtonMainMenu back;
   private GUIButtonMainMenu create;
   private GUIClickableText tips;
   private boolean tipsEnabled = true;

   public NewProfilePanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/mainMenu/mainMenuProfileNew.png");
      this.name = new GUIButtonInputText(gc, "name", Text.getText("mainMenuNewProfilePanel.button.textInput.profileName"), 24, 2);
      this.guiButtons.add(this.name);
      this.twitchChannel = new GUIButtonInputText(gc, "twitchChannel", Text.getText("mainMenuNewProfilePanel.button.textInput.twitchChannel"), 32, 24, 2);
      this.guiButtons.add(this.twitchChannel);
      this.back = new GUIButtonMainMenu("back", Text.getText("mainMenuNewProfilePanel.button.back"), 1);
      this.guiButtons.add(this.back);
      this.create = new GUIButtonMainMenu("create", Text.getText("mainMenuNewProfilePanel.button.create"), 1);
      this.guiButtons.add(this.create);
      this.tips = new GUIClickableText("tips", 2);
      this.guiButtons.add(this.tips);
   }

   public void render(boolean debug) {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.panel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() / 2 - this.panel.getHeight() / 2;
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + 240, this.y + 16, Text.getText("mainMenuNewProfilePanel.header.inputProfileName"), Text.FontType.HEADER, 3, true);
      this.name.render(this.g, this.mouse, this.x + 53, this.y + 64, debug);
      this.twitchChannel.render(this.g, this.mouse, this.x + 53, this.y + 100, debug);
      if (this.name.getText().equals("")) {
         this.create.render(this.g, this.mouse, this.x + 280, this.y + this.panel.getHeight() + 6, true, debug);
      } else {
         this.create.render(this.g, this.mouse, this.x + 280, this.y + this.panel.getHeight() + 6, false, debug);
      }

      this.back.render(this.g, this.mouse, this.x + 65, this.y + this.panel.getHeight() + 6, false, debug);
      this.font.drawString(this.x + 195, this.y + 142, Text.getText("mainMenuNewProfilePanel.header.tipsEnabled"), Text.FontType.BODY, 2, true);
      if (!this.tipsEnabled) {
         this.tips
            .render(
               this.g,
               this.mouse,
               Text.getText("mainMenuNewProfilePanel.header.tipsEnabledFalse"),
               this.x + 334,
               this.y + 142,
               "$RED0",
               "$RED1",
               false,
               true,
               debug
            );
      } else {
         this.tips
            .render(
               this.g,
               this.mouse,
               Text.getText("mainMenuNewProfilePanel.header.tipsEnabledTrue"),
               this.x + 334,
               this.y + 142,
               "$GRE0",
               "$GRE1",
               false,
               true,
               debug
            );
      }
   }

   public void setFocusName() {
      this.name.setFocus(true);
   }

   public String getName() {
      return this.name.getText();
   }

   public void resetName() {
      this.name.setFocus(false);
      this.name.setText("");
   }

   public void setFocusTwitchChannel() {
      this.twitchChannel.setFocus(true);
   }

   public String getTwitchChannel() {
      return this.twitchChannel.getText();
   }

   public void resetTwitchChannel() {
      this.twitchChannel.setFocus(false);
      this.twitchChannel.setText("");
   }

   public boolean getTipsEnabled() {
      return this.tipsEnabled;
   }

   public void toggleTipsEnabled() {
      this.tipsEnabled = !this.tipsEnabled;
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
