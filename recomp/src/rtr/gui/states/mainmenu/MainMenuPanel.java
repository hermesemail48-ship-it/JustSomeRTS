package rtr.gui.states.mainmenu;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.SettingsParser;
import rtr.TimeModule;
import rtr.font.Text;
import rtr.gui.GUIEnums;
import rtr.gui.buttons.GUIButtonInputText;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.buttons.GUIButtonSetLanguage;
import rtr.gui.buttons.GUIClickableArea;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MainMenuGUIPanelBase;
import rtr.particles.ParticleModule;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;
import rtr.utilities.devtools.LineCounter;

public class MainMenuPanel extends MainMenuGUIPanelBase {
   private Image mainMenuSidePanel;
   private Image pillar;
   private GUIEnums.MainMenuPage currentPage = GUIEnums.MainMenuPage.MAIN;
   private boolean streamLive;
   private GUIButtonSetLanguage setLanguage;
   private GUIButtonMainMenu play;
   private GUIButtonMainMenu worldMap;
   private GUIButtonMainMenu mapEditor;
   private GUIButtonMainMenu newMap;
   private GUIButtonMainMenu loadMap;
   private GUIButtonMainMenu settings;
   private GUIButtonMainMenu devTools;
   private GUIButtonMainMenu tutorialVideo;
   private GUIButtonMainMenu skirmish;
   private GUIButtonMainMenu credits;
   private GUIButtonMainMenu changeProfile;
   private GUIButtonMainMenu exit;
   private GUIButtonMainMenu back;
   private GUIButtonMainMenu hide;
   private GUIButtonMainMenu review;
   private GUIButtonMainMenu goWatch;
   private GUIButtonMainMenu scrubMapImages;
   private GUIButtonMainMenu generateMobSheet;
   private GUIButtonMainMenu generateMobSheetOverwrite;
   private GUIButtonMainMenu lineCounter;
   private GUIButtonMainMenu generateVideoSheet;
   private GUIButtonInputText generateVideoSheetName;
   private GUIClickableArea twitchURLIcon;
   private GUIClickableArea twitterURLIcon;
   private GUIClickableArea xURLIcon;
   private GUIClickableArea discordURLIcon;
   private Image twitchIcon;
   private Image twitterIcon;
   private Image xIcon;
   private Image discordIcon;
   private GUIClickableArea announcementButton;
   private String announcementTitle;
   private String announcementText;
   private URI announcementURISteam;
   private URI announcementURIOfficialWebsite;

   public MainMenuPanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.mainMenuSidePanel = ImageLoader.getImage("res/GUI/mainMenu/mainMenuSidePanel.png");
      this.pillar = ImageLoader.getImage("res/GUI/mainMenu/pillar.png");
      this.currentPage = GUIEnums.MainMenuPage.MAIN;
      if (!SettingsParser.getOfflineMode()) {
         this.streamLive = Utilities.isStreamLive();
      }

      this.setLanguage = new GUIButtonSetLanguage("setLanguage");
      this.setLanguage.setToolTip(Text.getText("mainMenuMainMenuPanel.button.tooltip.setLanguageHeader"));
      this.guiButtons.add(this.setLanguage);
      if (TimeModule.isHalloween()) {
         this.play = new GUIButtonMainMenu("play", Text.getText("mainMenuMainMenuPanel.button.play"), 2, 2);
         this.guiButtons.add(this.play);
         this.worldMap = new GUIButtonMainMenu("worldMap", Text.getText("mainMenuMainMenuPanel.button.worldMap"), 2, 2);
         this.guiButtons.add(this.worldMap);
         this.skirmish = new GUIButtonMainMenu("skirmish", Text.getText("mainMenuMainMenuPanel.button.skirmish"), 2, 2);
         this.guiButtons.add(this.skirmish);
      } else {
         this.play = new GUIButtonMainMenu("play", Text.getText("mainMenuMainMenuPanel.button.play"), 2, 1);
         this.guiButtons.add(this.play);
         this.worldMap = new GUIButtonMainMenu("worldMap", Text.getText("mainMenuMainMenuPanel.button.worldMap"), 2, 1);
         this.guiButtons.add(this.worldMap);
         this.skirmish = new GUIButtonMainMenu("skirmish", Text.getText("mainMenuMainMenuPanel.button.skirmish"), 2, 1);
         this.guiButtons.add(this.skirmish);
      }

      this.mapEditor = new GUIButtonMainMenu("mapEditor", Text.getText("mainMenuMainMenuPanel.button.mapEditor"), 2);
      this.guiButtons.add(this.mapEditor);
      this.newMap = new GUIButtonMainMenu("newMap", Text.getText("mainMenuMainMenuPanel.button.newMap"), 2);
      this.guiButtons.add(this.newMap);
      this.loadMap = new GUIButtonMainMenu("loadMap", Text.getText("mainMenuMainMenuPanel.button.loadMap"), 2);
      this.guiButtons.add(this.loadMap);
      this.devTools = new GUIButtonMainMenu("devTools", Text.getText("mainMenuMainMenuPanel.button.devTools"), 2, 1);
      this.guiButtons.add(this.devTools);
      this.tutorialVideo = new GUIButtonMainMenu("tutorialVideo", Text.getText("mainMenuMainMenuPanel.button.tutorialVideo"), 2);
      this.guiButtons.add(this.tutorialVideo);
      this.settings = new GUIButtonMainMenu("settings", Text.getText("mainMenuMainMenuPanel.button.settings"), 2);
      this.guiButtons.add(this.settings);
      this.credits = new GUIButtonMainMenu("credits", Text.getText("mainMenuMainMenuPanel.button.credits"), 2);
      this.guiButtons.add(this.credits);
      this.changeProfile = new GUIButtonMainMenu("changeProfile", Text.getText("mainMenuMainMenuPanel.button.changeProfile"), 2);
      this.guiButtons.add(this.changeProfile);
      this.exit = new GUIButtonMainMenu("exit", Text.getText("mainMenuMainMenuPanel.button.exit"), 2, 2);
      this.guiButtons.add(this.exit);
      this.back = new GUIButtonMainMenu("back", Text.getText("mainMenuMainMenuPanel.button.back"), 2);
      this.guiButtons.add(this.back);
      this.hide = new GUIButtonMainMenu("hide", Text.getText("mainMenuMainMenuPanel.reviewPopup.button.hide"), 0);
      this.guiButtons.add(this.hide);
      this.review = new GUIButtonMainMenu("review", Text.getText("mainMenuMainMenuPanel.reviewPopup.button.goReview"), 1);
      this.guiButtons.add(this.review);
      this.goWatch = new GUIButtonMainMenu("goWatch", Text.getText("mainMenuMainMenuPanel.twitchPopup.button.goWatch"), 1, 1);
      this.guiButtons.add(this.goWatch);
      this.scrubMapImages = new GUIButtonMainMenu("scrubMapImages", Text.getText("mainMenuMainMenuPanel.button.scrubMapImages"), 2);
      this.guiButtons.add(this.scrubMapImages);
      this.generateMobSheet = new GUIButtonMainMenu("generateMobSheet", Text.getText("mainMenuMainMenuPanel.button.generateMobSheet"), 2);
      this.guiButtons.add(this.generateMobSheet);
      this.generateMobSheetOverwrite = new GUIButtonMainMenu(
         "generateMobSheetOverwrite", Text.getText("mainMenuMainMenuPanel.button.generateMobSheetOverwrite"), 2
      );
      this.guiButtons.add(this.generateMobSheetOverwrite);
      this.lineCounter = new GUIButtonMainMenu("lineCounter", Text.getText("mainMenuMainMenuPanel.button.runLineCounter"), 2);
      this.guiButtons.add(this.lineCounter);
      this.generateVideoSheet = new GUIButtonMainMenu("generateVideoSheet", Text.getText("mainMenuMainMenuPanel.button.generateVideoSheet"), 2);
      this.guiButtons.add(this.generateVideoSheet);
      this.generateVideoSheetName = new GUIButtonInputText(gc, "generateVideoSheetName", Text.getText("mainMenuMainMenuPanel.videoSheetName"), 64, 12, 2);
      this.guiButtons.add(this.generateVideoSheetName);
      this.twitchURLIcon = new GUIClickableArea("twitchURLIcon", 24, 24);
      this.guiButtons.add(this.twitchURLIcon);
      this.twitchIcon = ImageLoader.getImage("res/GUI/mainMenu/twitchIcon.png");
      this.twitterURLIcon = new GUIClickableArea("twitterURLIcon", 24, 24);
      this.guiButtons.add(this.twitterURLIcon);
      this.twitterIcon = ImageLoader.getImage("res/GUI/mainMenu/twitterIcon.png");
      this.xURLIcon = new GUIClickableArea("xURLIcon", 24, 24);
      this.guiButtons.add(this.xURLIcon);
      this.xIcon = ImageLoader.getImage("res/GUI/mainMenu/xIcon.png");
      this.discordURLIcon = new GUIClickableArea("discordURLIcon", 24, 24);
      this.guiButtons.add(this.discordURLIcon);
      this.discordIcon = ImageLoader.getImage("res/GUI/mainMenu/discordIcon.png");
      if (!SettingsParser.getOfflineMode()) {
         try {
            URL url = new URL("https://www.risetoruins.com/news.txt");
            Scanner s = new Scanner(url.openStream());
            this.announcementButton = new GUIClickableArea("announcementButton", 149, 128);
            this.guiButtons.add(this.announcementButton);
            this.announcementTitle = s.nextLine();
            this.announcementText = s.nextLine();
            this.announcementURISteam = new URI(s.nextLine());
            this.announcementURIOfficialWebsite = new URI(s.nextLine());
            s.close();
         } catch (URISyntaxException | IOException var8) {
            var8.printStackTrace();
         }
      }
   }

   public void render(boolean fullMenu, boolean debug) throws SlickException {
      this.x = 112;
      this.y = ScaleControl.getInterfaceHeight() / 2;
      this.pillar.draw(this.x - 80, 0.0F);
      this.setLanguage.render(this.g, this.mouse, ScaleControl.getInterfaceWidth() - 100, 20, false, debug);
      int alignY = -350;
      switch (this.currentPage) {
         case MAIN:
            this.play.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            if (SettingsParser.getDebug()) {
               alignY += 60;
               this.devTools.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
               alignY += 60;
            } else {
               alignY += 80;
            }

            this.mapEditor.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            alignY += 60;
            this.settings.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            alignY += 60;
            this.credits.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            alignY += 60;
            this.changeProfile.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            this.font.drawString(this.x, this.y + 37 + alignY, this.profile.getProfileName(), Text.FontType.BODY, 0, true);
            alignY += 108;
            this.exit.render(this.g, this.mouse, this.x, this.y + 26, false, true, debug);
            alignY += 85;
            break;
         case PLAY:
            this.worldMap.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            alignY += 60;
            this.skirmish.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            alignY += 108;
            this.back.render(this.g, this.mouse, this.x, this.y + 26, false, true, debug);
            alignY += 85;
            break;
         case MAP_EDITOR:
            this.newMap.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            alignY += 60;
            this.loadMap.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            alignY += 108;
            this.back.render(this.g, this.mouse, this.x, this.y + 26, false, true, debug);
            alignY += 85;
            break;
         case DEV_TOOLS:
            this.scrubMapImages.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            alignY += 60;
            this.generateMobSheet.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            alignY += 60;
            this.generateMobSheetOverwrite.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            alignY += 60;
            this.lineCounter.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            alignY += 60;
            this.generateVideoSheet.render(this.g, this.mouse, this.x, this.y + alignY, false, true, debug);
            alignY += 60;
            this.generateVideoSheetName.render(this.g, this.mouse, this.x - 97, this.y + alignY, debug);
            alignY += 20;
            Text.setVariableText("amount", LineCounter.getLinesOfCode());
            this.font.drawString(this.x, this.y + alignY, Text.getText("mainMenuMainMenuPanel.linesOfCode"), Text.FontType.BODY, 4, true);
            alignY += 60;
            this.back.render(this.g, this.mouse, this.x, this.y + 26, false, true, debug);
            alignY += 85;
      }

      int var27 = 111;
      this.g.setColor(new Color(0, 0, 0, 200));
      this.g.fillRect(this.x - 75, this.y - 5 + var27, 149.0F, 32.0F);
      int alignX = -56;
      if (Utilities.randomInt(5) == 0) {
         this.particle
            .newParticleGroup(
               this.x + alignX + Utilities.randomInt(22),
               this.y + var27 + Utilities.randomInt(22),
               ParticleModule.ParticleGroup.MAGIC_FALL,
               ParticleModule.ParticleSet.MAGIC_PURPLE,
               true
            );
      }

      this.twitchIcon.draw(this.x + alignX, this.y + var27);
      this.twitchURLIcon.render(this.g, this.mouse, this.x + alignX, this.y + var27, false, debug);
      if (this.twitchURLIcon.intersects(this.mouse)) {
         GL11.glBlendFunc(774, 770);
         this.twitchIcon.drawFlash(this.x + alignX, this.y + var27, this.twitterIcon.getWidth(), this.twitterIcon.getHeight(), Color.white);
         this.g.setDrawMode(Graphics.MODE_NORMAL);
         this.font
            .drawString(
               this.mouse.getX() + 5.0F, this.mouse.getY() + 25.0F, Text.getText("mainMenuMainMenuPanel.button.rollover.twitch"), Text.FontType.BODY, 2
            );
      }

      alignX += 30;
      if (Utilities.randomInt(5) == 0) {
         this.particle
            .newParticleGroup(
               this.x + alignX + Utilities.randomInt(22),
               this.y + var27 + Utilities.randomInt(22),
               ParticleModule.ParticleGroup.FALL,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               true
            );
      }

      this.twitterIcon.draw(this.x + alignX, this.y + var27);
      this.twitterURLIcon.render(this.g, this.mouse, this.x + alignX, this.y + var27, false, debug);
      if (this.twitterURLIcon.intersects(this.mouse)) {
         GL11.glBlendFunc(774, 770);
         this.twitterIcon.drawFlash(this.x + alignX, this.y + var27, this.twitterIcon.getWidth(), this.twitterIcon.getHeight(), Color.white);
         this.g.setDrawMode(Graphics.MODE_NORMAL);
         this.font
            .drawString(
               this.mouse.getX() + 5.0F, this.mouse.getY() + 25.0F, Text.getText("mainMenuMainMenuPanel.button.rollover.twitter"), Text.FontType.BODY, 2
            );
      }

      alignX += 30;
      if (Utilities.randomInt(5) == 0) {
         this.particle
            .newParticleGroup(
               this.x + alignX + Utilities.randomInt(22),
               this.y + var27 + Utilities.randomInt(22),
               ParticleModule.ParticleGroup.FALL,
               ParticleModule.ParticleSet.WHITE,
               true
            );
      }

      this.xIcon.draw(this.x + alignX, this.y + var27);
      this.xURLIcon.render(this.g, this.mouse, this.x + alignX, this.y + var27, false, debug);
      if (this.xURLIcon.intersects(this.mouse)) {
         GL11.glBlendFunc(774, 770);
         this.xIcon.drawFlash(this.x + alignX, this.y + var27, this.xIcon.getWidth(), this.xIcon.getHeight(), Color.white);
         this.g.setDrawMode(Graphics.MODE_NORMAL);
         this.font
            .drawString(this.mouse.getX() + 5.0F, this.mouse.getY() + 25.0F, Text.getText("mainMenuMainMenuPanel.button.rollover.x"), Text.FontType.BODY, 2);
      }

      alignX += 30;
      if (Utilities.randomInt(5) == 0) {
         this.particle
            .newParticleGroup(
               this.x + alignX + Utilities.randomInt(22),
               this.y + var27 + Utilities.randomInt(22),
               ParticleModule.ParticleGroup.FALL,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               true
            );
      }

      this.discordIcon.draw(this.x + alignX, this.y + var27);
      this.discordURLIcon.render(this.g, this.mouse, this.x + alignX, this.y + var27, false, debug);
      if (this.discordURLIcon.intersects(this.mouse)) {
         GL11.glBlendFunc(774, 770);
         this.discordIcon.drawFlash(this.x + alignX, this.y + var27, this.twitterIcon.getWidth(), this.twitterIcon.getHeight(), Color.white);
         this.g.setDrawMode(Graphics.MODE_NORMAL);
         this.font
            .drawString(
               this.mouse.getX() + 5.0F, this.mouse.getY() + 25.0F, Text.getText("mainMenuMainMenuPanel.button.rollover.discord"), Text.FontType.BODY, 2
            );
      }

      alignX += 30;
      var27 += 64;
      if (!SettingsParser.getOfflineMode() && this.announcementURISteam != null) {
         this.g.setColor(new Color(0, 0, 0, 200));
         this.g.fillRect(this.x - 75, this.y + var27, 149.0F, 128.0F);
         this.announcementButton.render(this.g, this.mouse, this.x - 77, this.y + var27 - 3, false, debug);
         String announcementTextOut = this.announcementText;
         if (announcementTextOut.length() > 170) {
            announcementTextOut = announcementTextOut.substring(0, 170) + "...";
         }

         if (this.announcementButton.intersects(this.mouse)) {
            this.font.drawString(this.x, this.y + var27, "$CYA1" + this.announcementTitle, Text.FontType.BODY, 2, true);
            this.font
               .drawString(
                  this.x - 70,
                  this.y + 20 + var27,
                  "$BLU0" + Text.wrapString(announcementTextOut + " [more]", Text.FontType.BODY, 1, 140, true),
                  Text.FontType.BODY,
                  1
               );
            this.font
               .drawString(
                  this.mouse.getX() + 30.0F, this.mouse.getY(), Text.getText("mainMenuMainMenuPanel.button.rollover.announcement"), Text.FontType.BODY, 2
               );
         } else {
            this.font.drawString(this.x, this.y + var27, "$YEL1" + this.announcementTitle, Text.FontType.BODY, 2, true);
            this.font
               .drawString(
                  this.x - 70, this.y + 20 + var27, Text.wrapString(announcementTextOut + " [more]", Text.FontType.BODY, 1, 140, true), Text.FontType.BODY, 1
               );
         }
      }

      this.g.setColor(new Color(0, 0, 0, 200));
      this.g.fillRect(this.x - 75, ScaleControl.getInterfaceHeight() - 33, 149.0F, 50.0F);
      Text.setVariableText("version", Game.getVersion());
      if (Game.getVersion().length() > 20) {
         this.font.drawString(this.x, ScaleControl.getInterfaceHeight() - 32, Text.getText("mainMenuMainMenuPanel.buildVersion"), Text.FontType.BODY, 0, true);
      } else if (Game.getVersion().length() > 10) {
         this.font.drawString(this.x, ScaleControl.getInterfaceHeight() - 32, Text.getText("mainMenuMainMenuPanel.buildVersion"), Text.FontType.BODY, 1, true);
      } else {
         this.font.drawString(this.x, ScaleControl.getInterfaceHeight() - 32, Text.getText("mainMenuMainMenuPanel.buildVersion"), Text.FontType.BODY, 2, true);
      }

      this.font.drawString(this.x, ScaleControl.getInterfaceHeight() - 14, "(C)2023 SixtyGig Games", Text.FontType.BODY, 0, true);
      if (!fullMenu) {
         var27 += 164;
         if (!this.profile.hideReview() && this.profile.getDaysSurvived() >= 40 && !Game.isNoneSteamMode()) {
            this.x += 90;
            this.y = ScaleControl.getInterfaceHeight() - this.mainMenuSidePanel.getHeight() - 64;
            this.mainMenuSidePanel.draw(this.x, this.y);
            this.font
               .drawString(
                  this.x + this.mainMenuSidePanel.getWidth() / 2,
                  this.y + 15,
                  Text.getText("mainMenuMainMenuPanel.reviewPopup.header"),
                  Text.FontType.HEADER,
                  3,
                  true
               );
            this.font
               .drawString(
                  this.x + this.mainMenuSidePanel.getWidth() / 2,
                  this.y + 60,
                  Text.getText("mainMenuMainMenuPanel.reviewPopup.subHeader"),
                  Text.FontType.BODY,
                  2,
                  true
               );
            this.font
               .drawString(
                  this.x + this.mainMenuSidePanel.getWidth() / 2,
                  this.y + 80,
                  Text.wrapString(Text.getText("mainMenuMainMenuPanel.reviewPopup.body"), Text.FontType.BODY, 1, 235),
                  Text.FontType.BODY,
                  1,
                  true
               );
            this.hide.render(this.g, this.mouse, this.x + 11, this.y + 146, false, debug);
            this.review.render(this.g, this.mouse, this.x + 145, this.y + 146, false, debug);
         } else if (this.streamLive) {
            this.x += 293;
            this.y = ScaleControl.getInterfaceHeight() - this.mainMenuSidePanel.getHeight() - 64;
            this.mainMenuSidePanel.draw(this.x, this.y);
            this.font
               .drawString(
                  this.x + this.mainMenuSidePanel.getWidth() / 2,
                  this.y + 15,
                  Text.getText("mainMenuMainMenuPanel.twitchPopup.header"),
                  Text.FontType.HEADER,
                  3,
                  true
               );
            this.font
               .drawString(
                  this.x + this.mainMenuSidePanel.getWidth() / 2,
                  this.y + 65,
                  Text.getText("mainMenuMainMenuPanel.twitchPopup.subHeader"),
                  Text.FontType.BODY,
                  2,
                  true
               );
            this.font
               .drawString(
                  this.x + this.mainMenuSidePanel.getWidth() / 2,
                  this.y + 90,
                  Text.wrapString(Text.getText("mainMenuMainMenuPanel.twitchPopup.body"), Text.FontType.BODY, 1, 235),
                  Text.FontType.BODY,
                  1,
                  true
               );
            this.goWatch.render(this.g, this.mouse, this.x + 145, this.y + 146, false, debug);
         }
      }
   }

   public URI getAnnouncementURL() {
      return Game.isNoneSteamMode() ? this.announcementURIOfficialWebsite : this.announcementURISteam;
   }

   public void setPanelPage(GUIEnums.MainMenuPage p) {
      this.currentPage = p;
   }

   public void setFocusGenerateVideoSheetName() {
      this.generateVideoSheetName.setFocus(true);
   }

   public String getGenerateVideoSheetName() {
      return this.generateVideoSheetName.getText();
   }

   public void resetGenerateVideoSheetName() {
      this.generateVideoSheetName.setFocus(false);
      this.generateVideoSheetName.setText("");
   }
}
