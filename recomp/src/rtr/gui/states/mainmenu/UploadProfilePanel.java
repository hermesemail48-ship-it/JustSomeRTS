package rtr.gui.states.mainmenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonInputText;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.buttons.GUIClickableText;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MainMenuGUIPanelBase;
import rtr.particles.ParticleModule;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class UploadProfilePanel extends MainMenuGUIPanelBase {
   private GUIClickableText crash;
   private GUIClickableText lag;
   private GUIClickableText corrupted;
   private GUIClickableText devRequest;
   private GUIClickableText other;
   private GUIButtonInputText comments;
   private GUIButtonInputText username;
   private GUIButtonMainMenu back;
   private GUIButtonMainMenu upload;
   private String uploadProfileCategory = "Other";
   private boolean uploadProfileSent;

   public UploadProfilePanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.crash = new GUIClickableText("crash", 2);
      this.guiButtons.add(this.crash);
      this.lag = new GUIClickableText("lag", 2);
      this.guiButtons.add(this.lag);
      this.corrupted = new GUIClickableText("corrupted", 2);
      this.guiButtons.add(this.corrupted);
      this.devRequest = new GUIClickableText("devRequest", 2);
      this.guiButtons.add(this.devRequest);
      this.other = new GUIClickableText("other", 2);
      this.guiButtons.add(this.other);
      this.other.select();
      this.comments = new GUIButtonInputText(gc, "comments", Text.getText("mainMenuUploadProfilePanel.button.textInput.comments"), 256, 32, 2);
      this.guiButtons.add(this.comments);
      this.username = new GUIButtonInputText(gc, "username", Text.getText("mainMenuUploadProfilePanel.button.textInput.username"), 64, 32, 2);
      this.guiButtons.add(this.username);
      this.back = new GUIButtonMainMenu("back", Text.getText("mainMenuUploadProfilePanel.button.back"), 1);
      this.guiButtons.add(this.back);
      this.upload = new GUIButtonMainMenu("upload", Text.getText("mainMenuUploadProfilePanel.button.upload"), 1);
      this.guiButtons.add(this.upload);
   }

   public void render(boolean debug) {
      this.x = ScaleControl.getInterfaceWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() / 2;
      if (Utilities.isSendingProfile()) {
         this.font.drawString(this.x, this.y - 10, Text.getText("mainMenuUploadProfilePanel.message.sending"), Text.FontType.HEADER, 6, true);
         if (Utilities.getSendingProfileAttempts() > 0) {
            Text.setVariableText("attemptCount", Utilities.getSendingProfileAttempts() + 1);
            this.font.drawString(this.x, this.y + 50, Text.getText("mainMenuUploadProfilePanel.message.sendingAttempt"), Text.FontType.BODY, 4, true);
         }

         if (Utilities.getSendingProfileAttempts() > 1) {
            this.font.drawString(this.x, this.y + 85, Text.getText("mainMenuUploadProfilePanel.message.sendingAttemptFailure"), Text.FontType.BODY, 2, true);
         }

         for (int i = 0; i < 50; i++) {
            this.particle
               .newParticle(
                  this.x + Utilities.randomInt(-280, 280),
                  this.y + 38 + Utilities.randomInt(-1, 1),
                  -90.0F,
                  ParticleModule.ParticleType.FIRE_SPECK,
                  ParticleModule.ParticleSet.FIRE_YELLOW,
                  true
               );
         }

         this.uploadProfileSent = true;
      } else if (this.uploadProfileSent) {
         if (Utilities.isSendingProfileFailed()) {
            this.font.drawString(this.x, this.y, Text.getText("mainMenuUploadProfilePanel.message.sendingFailure"), Text.FontType.BODY, 2, true);
         } else {
            this.font.drawString(this.x, this.y, Text.getText("mainMenuUploadProfilePanel.message.sendingSuccess"), Text.FontType.BODY, 2, true);
         }

         this.back.render(this.g, this.mouse, this.x - 70, this.y + 30, false, debug);
      } else {
         this.font
            .drawString(
               this.x, this.y - 170, Text.wrapString(Text.getText("mainMenuUploadProfilePanel.body"), Text.FontType.BODY, 3, 600), Text.FontType.BODY, 3, true
            );
         this.font.drawString(this.x, this.y - 55, Text.getText("mainMenuUploadProfilePanel.header"), Text.FontType.BODY, 1, true);
         this.font.drawString(this.x, this.y - 45, "$GRE1" + this.controller.getSelectProfilePanel().getSelectedProfileName(), Text.FontType.HEADER, 3, true);
         this.font.drawString(this.x, this.y - 12, Text.getText("mainMenuUploadProfilePanel.category"), Text.FontType.BODY, 1, true);
         this.crash
            .render(this.g, this.mouse, Text.getText("mainMenuUploadProfilePanel.button.crash"), this.x - 230, this.y, "$GRE1", "$YEL0", false, true, debug);
         this.lag
            .render(this.g, this.mouse, Text.getText("mainMenuUploadProfilePanel.button.laggy"), this.x - 130, this.y, "$GRE1", "$YEL0", false, true, debug);
         this.corrupted
            .render(this.g, this.mouse, Text.getText("mainMenuUploadProfilePanel.button.corrupt"), this.x, this.y, "$GRE1", "$YEL0", false, true, debug);
         this.devRequest
            .render(
               this.g, this.mouse, Text.getText("mainMenuUploadProfilePanel.button.devRequest"), this.x + 130, this.y, "$GRE1", "$YEL0", false, true, debug
            );
         this.other
            .render(this.g, this.mouse, Text.getText("mainMenuUploadProfilePanel.button.other"), this.x + 230, this.y, "$GRE1", "$YEL0", false, true, debug);
         this.comments.render(this.g, this.mouse, this.x - 245, this.y + 35, debug);
         this.username.render(this.g, this.mouse, this.x - 245, this.y + 65, debug);
         this.back.render(this.g, this.mouse, this.x - 195, this.y + 95, false, debug);
         this.upload.render(this.g, this.mouse, this.x + 65, this.y + 95, false, debug);
      }
   }

   public void setFocusUploadProfileComments() {
      this.comments.setFocus(true);
   }

   public void setFocusUploadProfileUsername() {
      this.username.setFocus(true);
   }

   public void setUploadProfileCategory(String s) {
      this.uploadProfileCategory = s;
   }

   public String getUploadProfileCategory() {
      return this.uploadProfileCategory;
   }

   public String getUploadProfileComments() {
      return this.comments.getText();
   }

   public String getUploadProfileUsername() {
      return this.username.getText();
   }

   public void resetUploadProfileSent() {
      this.uploadProfileSent = false;
   }
}
