package rtr.gui.states.mainmenu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MainMenuGUIPanelBase;
import rtr.system.ScaleControl;

public class DisclaimerPanel extends MainMenuGUIPanelBase {
   private GUIClickableText steam = new GUIClickableText("steam", 3);
   private GUIClickableText twitter;
   private GUIClickableText discord;
   private GUIButtonMainMenu letsPlay;
   private GUIButtonMainMenu dontShowAgain;
   private static boolean disclaimerDisplayed;

   public DisclaimerPanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.guiButtons.add(this.steam);
      this.twitter = new GUIClickableText("twitter", 3);
      this.guiButtons.add(this.twitter);
      this.discord = new GUIClickableText("discord", 3);
      this.guiButtons.add(this.discord);
      this.letsPlay = new GUIButtonMainMenu("letsPlay", Text.getText("mainMenuDisclaimerPanel.button.letsPlay"), 2, 1);
      this.guiButtons.add(this.letsPlay);
      this.dontShowAgain = new GUIButtonMainMenu("dontShowAgain", Text.getText("mainMenuDisclaimerPanel.button.dontShow"), 2);
      this.guiButtons.add(this.dontShowAgain);
   }

   public void render(boolean debug) {
      this.x = ScaleControl.getInterfaceWidth() / 2;
      this.y = 0;
      this.g.setColor(new Color(0, 0, 0, 130));
      this.g.fillRect(-10.0F, 10.0F, ScaleControl.getInterfaceWidth() + 10, 500.0F);
      this.font.drawString(this.x, 20.0F, "$GRE1Thank you!", Text.FontType.HEADER, 5, true);
      this.font
         .drawString(
            ScaleControl.getInterfaceWidth() / 2 - 415,
            95.0F,
            Text.wrapString(
               "Hello Fellow Deity,~~    Thank you for purchasing Rise to Ruins, and I hope you'll enjoy what this game has to offer. I do not believe in DRM, paid DLC, or microtransactions. You will never see any of that in this game. What you will see is regular and free on-going content patches throughout the game's continued development that I hope you'll enjoy for years to come. Don't forget to post your feedback over on the the $BLA0Steam forums$WHI0. I read everything posted, and reply to almost all of it.~~    It's because of you I get to do my dream job, so thank you! :)~~Enjoy and Good Luck!~~ $GRE0- Raymond 'Rayvolution' Doerr~$WHI0    $BLA0Follow me on twitter$WHI0 for the latest updates about the game, or join the $BLA0Discord server$WHI0!",
               Text.FontType.BODY,
               3,
               835
            ),
            Text.FontType.BODY,
            3,
            false
         );
      if (ScaleControl.getInterfaceScale() >= 2.0F) {
         this.steam.render(this.g, this.mouse, "Steam forums", this.x - 87, this.y + 242, "$YEL1", "$GRE1", false, true, debug);
         this.twitter.render(this.g, this.mouse, "Follow me on twitter", this.x - 310, this.y + 438, "$YEL1", "$GRE1", false, true, debug);
         this.discord.render(this.g, this.mouse, "join the Discord server", this.x + 246, this.y + 438, "$YEL1", "$GRE1", false, true, debug);
      } else {
         this.steam.render(this.g, this.mouse, "Steam forums", this.x - 79, this.y + 245, "$YEL1", "$GRE1", false, true, debug);
         this.twitter.render(this.g, this.mouse, "Follow me on twitter", this.x - 308, this.y + 445, "$YEL1", "$GRE1", false, true, debug);
         this.discord.render(this.g, this.mouse, "join the Discord server", this.x + 259, this.y + 445, "$YEL1", "$GRE1", false, true, debug);
      }

      this.letsPlay.render(this.g, this.mouse, this.x - 88, this.y + 520, false, debug);
      this.dontShowAgain.render(this.g, this.mouse, this.x - 88, this.y + 580, false, debug);
   }

   public boolean isDisclaimerDisplayed() {
      return disclaimerDisplayed;
   }

   public void setDisclaimerDisplayed(boolean b) {
      disclaimerDisplayed = b;
   }
}
