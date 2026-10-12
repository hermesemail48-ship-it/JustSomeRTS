package rtr.gui.states.mainmenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.ProfileModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.buttons.GUIButtonSetLanguage;
import rtr.gui.buttons.GUIButtonUpload;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MainMenuGUIPanelBase;
import rtr.system.Game;
import rtr.system.ScaleControl;

public class SelectProfilePanel extends MainMenuGUIPanelBase {
   private Image panel;
   private GUIButtonSetLanguage setLanguage;
   private GUIButtonMainMenu play;
   private GUIButtonMainMenu delete;
   private GUIButtonMainMenu exit;
   private Properties[] selectProfileProperties = new Properties[5];
   private GUIClickableText[] selectProfile = new GUIClickableText[5];
   private GUIButtonUpload[] upload = new GUIButtonUpload[5];
   private int selectedProfile = 0;

   public SelectProfilePanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/mainMenu/mainMenuProfile.png");
      this.setLanguage = new GUIButtonSetLanguage("setLanguage");
      this.setLanguage.setToolTip(Text.getText("mainMenuMainMenuPanel.button.tooltip.setLanguageHeader"));
      this.guiButtons.add(this.setLanguage);
      this.play = new GUIButtonMainMenu("play", Text.getText("mainMenuSelectProfilePanel.button.play"), 1);
      this.guiButtons.add(this.play);
      this.delete = new GUIButtonMainMenu("delete", Text.getText("mainMenuSelectProfilePanel.button.delete"), 1);
      this.guiButtons.add(this.delete);
      this.exit = new GUIButtonMainMenu("exit", Text.getText("mainMenuSelectProfilePanel.button.exit"), 0);
      this.guiButtons.add(this.exit);

      for (int i = 0; i < this.selectProfile.length; i++) {
         this.selectProfile[i] = new GUIClickableText("selectProfile" + i, 2);
         this.guiButtons.add(this.selectProfile[i]);
      }

      for (int i = 0; i < this.upload.length; i++) {
         this.upload[i] = new GUIButtonUpload("upload" + i);
         this.upload[i]
            .setToolTip(
               Text.getText("mainMenuSelectProfilePanel.button.tooltip.sendProfileHeader"),
               Text.getText("mainMenuSelectProfilePanel.button.tooltip.sendProfile")
            );
         this.guiButtons.add(this.upload[i]);
      }

      if (ProfileModule.getCurrentProfileSlot() == -1) {
         this.selectProfile[0].select();
      } else {
         this.selectProfile[ProfileModule.getCurrentProfileSlot()].select();
      }

      this.loadProfiles();
   }

   public void render(boolean debug) {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.panel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() / 2 - this.panel.getHeight() / 2;
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + 185, this.y + 16, Text.getText("mainMenuSelectProfilePanel.header.profiles"), Text.FontType.HEADER, 3, true);
      int profileAdjustY = 0;

      for (int i = 0; i < this.selectProfile.length; i++) {
         if (this.selectProfileProperties[i] == null) {
            this.selectProfile[i]
               .render(
                  this.g,
                  this.mouse,
                  Text.getText("mainMenuSelectProfilePanel.button.createNewProfile"),
                  this.x + 185,
                  this.y + 52 + profileAdjustY,
                  "$WHI0",
                  "$YEL1",
                  false,
                  true,
                  debug
               );
         } else if (this.selectProfileProperties[i].getProperty("profileName", "null").equals("null")) {
            this.selectProfile[i]
               .render(
                  this.g,
                  this.mouse,
                  Text.getText("mainMenuSelectProfilePanel.corruptProfile"),
                  this.x + 185,
                  this.y + 52 + profileAdjustY,
                  "$WHI0",
                  "$YEL1",
                  false,
                  true,
                  debug
               );
            this.upload[i].render(this.g, this.mouse, this.x + 325, this.y + 49 + profileAdjustY, false, debug);
         } else if (!Game.isValidGameVersion(this.selectProfileProperties[i].getProperty("worldMapVersion", "null"))) {
            Text.setVariableText("profileName", this.selectProfileProperties[i].getProperty("profileName"));
            this.selectProfile[i]
               .render(
                  this.g,
                  this.mouse,
                  Text.getText("mainMenuSelectProfilePanel.outdatedProfilePrefix"),
                  this.x + 185,
                  this.y + 52 + profileAdjustY,
                  "$WHI0",
                  "$RED1",
                  false,
                  true,
                  debug
               );
            this.upload[i].render(this.g, this.mouse, this.x + 325, this.y + 49 + profileAdjustY, false, debug);
         } else {
            this.selectProfile[i]
               .render(
                  this.g,
                  this.mouse,
                  this.selectProfileProperties[i].getProperty("profileName"),
                  this.x + 185,
                  this.y + 52 + profileAdjustY,
                  "$WHI0",
                  "$GRE1",
                  false,
                  true,
                  debug
               );
            this.upload[i].render(this.g, this.mouse, this.x + 325, this.y + 49 + profileAdjustY, false, debug);
         }

         profileAdjustY += 34;
      }

      if (this.selectProfileProperties[this.selectedProfile] == null
         || !this.selectProfileProperties[this.selectedProfile].getProperty("profileName", "null").equals("null")
            && Game.isValidGameVersion(this.selectProfileProperties[this.selectedProfile].getProperty("worldMapVersion", "null"))) {
         this.setLanguage.render(this.g, this.mouse, this.x + 146, this.y + this.panel.getHeight(), false, debug);
         this.delete.render(this.g, this.mouse, this.x + 8, this.y + this.panel.getHeight(), this.selectProfileProperties[this.selectedProfile] == null, debug);
         this.play.render(this.g, this.mouse, this.x + 222, this.y + this.panel.getHeight(), false, debug);
         this.exit.render(this.g, this.mouse, this.x + 135, this.y + this.panel.getHeight() + 54, false, debug);
      } else {
         this.g.setColor(new Color(100, 100, 150, 150));
         this.g.fillRect(this.x + this.panel.getWidth() / 2 - 2500, this.y + 230, 5000.0F, 30.0F);
         this.font
            .drawString(
               this.x + this.panel.getWidth() / 2, this.y + 230, Text.getText("mainMenuSelectProfilePanel.outdatedWarning"), Text.FontType.BODY, 1, true
            );
         this.font.drawString(this.x + this.panel.getWidth() / 2, this.y + 226, "$RED1->>                        <<-", Text.FontType.HEADER, 5, true);
         this.setLanguage.render(this.g, this.mouse, this.x + 146, this.y + this.panel.getHeight() + 30, false, debug);
         this.delete.render(this.g, this.mouse, this.x + 8, this.y + this.panel.getHeight() + 30, false, debug);
         this.play.render(this.g, this.mouse, this.x + 222, this.y + this.panel.getHeight() + 30, true, debug);
         this.exit.render(this.g, this.mouse, this.x + 135, this.y + this.panel.getHeight() + 84, false, debug);
      }
   }

   public void loadProfiles() {
      for (int i = 0; i < this.selectProfileProperties.length; i++) {
         this.selectProfileProperties[i] = null;
         File profileFolder = new File("profiles/profile" + i + "/");
         if (profileFolder.exists()) {
            try {
               File profileData = new File("profiles/profile" + i + "/profile.properties");
               this.selectProfileProperties[i] = new Properties();
               FileInputStream in = new FileInputStream(profileData);
               this.selectProfileProperties[i].load(in);
               in.close();
            } catch (IOException var5) {
               this.selectProfileProperties[i] = null;
               Console.out("Failed to load profile in slot " + i + "!", true);
            }
         }
      }
   }

   public int getSelectedProfile() {
      return this.selectedProfile;
   }

   public String getSelectedProfileName() {
      return this.selectProfileProperties[this.selectedProfile].getProperty("profileName", "null");
   }

   public void selectProfile(int profile) {
      for (int i = 0; i < this.selectProfile.length; i++) {
         this.selectProfile[i].deselect();
      }

      this.selectProfile[profile].select();
      this.selectedProfile = profile;
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
