package rtr.gui.states.mainmenu;

import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.LocalizationTemplate;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonLanguageBar;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.buttons.GUIButtonModLink;
import rtr.gui.buttons.GUIButtonSteamWorkshop;
import rtr.gui.buttons.GUIClickableArea;
import rtr.gui.states.GUIControllerBase;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MainMenuGUIPanelBase;
import rtr.system.ScaleControl;
import rtr.utilities.Steamworks;

public class LanguageSelectPanel extends MainMenuGUIPanelBase {
   public static int LANGUAGE_SELECT_PANEL_MAX_LANGUAGES = 8;
   private Image panel;
   private GUIButtonMainMenu refresh;
   private GUIButtonMainMenu back;
   private GUIButtonSteamWorkshop steamWorkshop;
   private GUIButtonLanguageBar[] languageBars;
   private GUIButtonModLink[] languageBarsLinks;
   private GUIClickableArea[] languagePercentageCompleteTooltips;
   private GUIControllerBase.GUIPanel originPanel = null;

   public LanguageSelectPanel(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/mainMenu/mainMenuLanguageSelect.png");
      this.refresh = new GUIButtonMainMenu("refresh", Text.getText("mainMenuLanguageSelectPanel.button.refresh"), 1);
      this.refresh
         .setToolTip(
            Text.getText("mainMenuLanguageSelectPanel.button.tooltip.refreshHeader"), Text.getText("mainMenuLanguageSelectPanel.button.tooltip.refresh")
         );
      this.guiButtons.add(this.refresh);
      this.back = new GUIButtonMainMenu("back", Text.getText("mainMenuLanguageSelectPanel.button.back"), 1);
      this.guiButtons.add(this.back);
      this.steamWorkshop = new GUIButtonSteamWorkshop("steamWorkshop");
      this.steamWorkshop
         .setToolTip(
            Text.getText("mainMenuLanguageSelectPanel.button.tooltip.steamWorkshopHeader"),
            Text.getText("mainMenuLanguageSelectPanel.button.tooltip.steamWorkshop")
         );
      this.steamWorkshop.setFlashing(true);
      this.guiButtons.add(this.steamWorkshop);
      this.refreshLoadedLanguages();
   }

   public void refreshPanel() throws SlickException {
      for (int i = 0; i < this.languageBars.length; i++) {
         this.guiButtons.remove(this.languageBars[i]);
         this.guiButtons.remove(this.languageBarsLinks[i]);
      }

      this.refreshLoadedLanguages();
   }

   private void refreshLoadedLanguages() throws SlickException {
      this.languageBars = new GUIButtonLanguageBar[LANGUAGE_SELECT_PANEL_MAX_LANGUAGES];
      this.languagePercentageCompleteTooltips = new GUIClickableArea[LANGUAGE_SELECT_PANEL_MAX_LANGUAGES];
      this.languageBarsLinks = new GUIButtonModLink[LANGUAGE_SELECT_PANEL_MAX_LANGUAGES];
      ArrayList<LocalizationTemplate> availableLocalizations = Text.getAvailableLocalizations();
      this.languageBars[0] = new GUIButtonLanguageBar("localization0", Text.getDefaultTemplate());
      this.guiButtons.add(this.languageBars[0]);
      this.languagePercentageCompleteTooltips[0] = new GUIClickableArea("languagePercentageCompleteTooltips0", 48, 17);
      this.languagePercentageCompleteTooltips[0].setToolTip(Text.getText("mainMenuLanguageSelectPanel.button.tooltip.languagePercentCompleteHeader"));
      this.guiButtons.add(this.languagePercentageCompleteTooltips[0]);

      for (int i = 1; i < this.languageBars.length && i < availableLocalizations.size(); i++) {
         LocalizationTemplate t = availableLocalizations.get(i);
         this.languageBars[i] = new GUIButtonLanguageBar("localization" + i, t);
         this.guiButtons.add(this.languageBars[i]);
         this.languagePercentageCompleteTooltips[i] = new GUIClickableArea("languagePercentageCompleteTooltips" + i, 48, 17);
         this.languagePercentageCompleteTooltips[i].setToolTip(Text.getText("mainMenuLanguageSelectPanel.button.tooltip.languagePercentCompleteHeader"));
         this.guiButtons.add(this.languagePercentageCompleteTooltips[i]);
         if (t.getLanguageLink() != null) {
            this.languageBarsLinks[i] = new GUIButtonModLink("localizationLink" + i);
            this.languageBarsLinks[i]
               .setToolTip(
                  Text.getText("mainMenuLanguageSelectPanel.button.tooltip.languageLinksHeader"),
                  Text.getText("mainMenuLanguageSelectPanel.button.tooltip.languageLinks")
               );
            this.guiButtons.add(this.languageBarsLinks[i]);
         }
      }
   }

   public void render(boolean debug) {
      this.x = ScaleControl.getInterfaceWidth() / 2 - this.panel.getWidth() / 2;
      this.y = ScaleControl.getInterfaceHeight() / 2 - this.panel.getHeight() / 2;
      this.panel.draw(this.x, this.y);
      this.font.drawString(this.x + 133, this.y + 16, Text.getText("mainMenuLanguageSelectPanel.header"), Text.FontType.HEADER, 3, true);
      int alignY = 0;

      for (GUIButtonLanguageBar b : this.languageBars) {
         if (b != null) {
            b.render(this.g, this.mouse, this.x + 24, this.y + alignY + 58, false, debug);
         }

         alignY += 29;
      }

      int var7 = 0;

      for (GUIClickableArea b : this.languagePercentageCompleteTooltips) {
         if (b != null) {
            b.render(this.g, this.mouse, this.x + 303, this.y + var7 + 59, false, debug);
         }

         var7 += 29;
      }

      var7 = 0;

      for (GUIButtonModLink b : this.languageBarsLinks) {
         if (b != null) {
            b.render(this.g, this.mouse, this.x + 365, this.y + var7 + 59, false, debug);
         }

         var7 += 29;
      }

      this.refresh.render(this.g, this.mouse, this.x + 25, this.y + this.panel.getHeight() - 105, false, debug);
      this.back.render(this.g, this.mouse, this.x + 25, this.y + this.panel.getHeight() - 50, false, debug);
      if (Steamworks.isRunning()) {
         this.steamWorkshop.render(this.g, this.mouse, this.x + 205, this.y + 292, false, debug);
      }

      this.font
         .drawString(
            this.x + 205,
            this.y + 426,
            Text.wrapString(Text.getText("mainMenuLanguageSelectPanel.languageAccuracy"), Text.FontType.BODY, 2, 400),
            Text.FontType.BODY,
            2,
            true
         );
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

   public void setOriginPanel(GUIControllerBase.GUIPanel o) {
      this.originPanel = o;
   }

   public GUIControllerBase.GUIPanel getOriginPanel() {
      return this.originPanel;
   }
}
