package rtr.gui.states.mainmenu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.TimeModule;
import rtr.font.Text;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MainMenuGUIPanelBase;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class Logo extends MainMenuGUIPanelBase {
   private Image logo;
   private ArrayList<String> randomQuotes = new ArrayList<>();
   private String randomQuote;
   private int quoteCycle = 0;
   private int quoteAlpha = 255;
   private boolean quoteFade = false;
   private float quoteBounceSpeed = 0.0F;
   private float quoteBounce = 0.0F;

   public Logo(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      if (TimeModule.isHalloween()) {
         this.logo = ImageLoader.getImage("res/GUI/mainMenu/logoRed.png");
      } else if (Utilities.randomInt(500) == 0) {
         this.logo = ImageLoader.getImage("res/GUI/mainMenu/logoAlt.png");
      } else {
         this.logo = ImageLoader.getImage("res/GUI/mainMenu/logo.png");
      }

      try {
         FileInputStream file = new FileInputStream("res/quotes.txt");
         BufferedReader reader = new BufferedReader(new InputStreamReader(file));
         ArrayList<String> fontColors = new ArrayList<>();
         fontColors.add("$WHI0");
         fontColors.add("$YEL0");
         fontColors.add("$YEL1");
         fontColors.add("$RED0");
         fontColors.add("$RED1");
         fontColors.add("$GRE0");
         fontColors.add("$GRE1");
         fontColors.add("$BLU0");
         fontColors.add("$BLU1");
         fontColors.add("$CYA0");
         fontColors.add("$CYA1");
         fontColors.add("$PIN0");
         fontColors.add("$PIN1");
         fontColors.add("$ORA0");
         fontColors.add("$ORA1");
         fontColors.add("$MAG0");
         fontColors.add("$MAG1");

         String line;
         while ((line = reader.readLine()) != null) {
            String randomColor = fontColors.get(Utilities.randomInt(fontColors.size()));
            this.randomQuotes.add(randomColor + line);
         }

         file.close();
         reader.close();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

      this.randomQuote = this.randomQuotes.get(Utilities.randomInt(this.randomQuotes.size()));
   }

   public void render(float x, float y) {
      this.logo.draw(x - this.logo.getWidth() / 2, y);
      if (TimeModule.isHalloween()) {
         this.font.drawString(x, y + 184.0F + this.quoteBounce, Text.getText("mainMenuLogo.halloweenQuote"), Text.FontType.HEADER, 2, true);
      } else {
         this.font.drawString(x, y + 194.0F + this.quoteBounce, this.randomQuote, this.quoteAlpha, Text.FontType.HEADER, 2, true);
      }
   }

   public void render() {
      int x = ScaleControl.getInterfaceWidth() / 2;
      int y = 16;
      this.logo.draw(x - this.logo.getWidth() / 2, y);
      if (TimeModule.isHalloween()) {
         this.font.drawString(x, y + 184 + this.quoteBounce, Text.getText("mainMenuLogo.halloweenQuote"), Text.FontType.HEADER, 2, true);
      } else {
         this.font.drawString(x, y + 194 + this.quoteBounce, this.randomQuote, this.quoteAlpha, Text.FontType.HEADER, 2, true);
      }
   }

   @Override
   public void update() throws SlickException {
      this.quoteBounceSpeed += 0.025F;
      this.quoteBounce = this.quoteBounce + this.quoteBounceSpeed;
      this.quoteCycle++;
      if (this.quoteBounce >= 0.0F) {
         this.quoteBounceSpeed = -0.5F;
      }

      if (this.quoteCycle >= 500) {
         this.quoteFade = true;
         this.quoteCycle = 0;
      }

      if (this.quoteFade) {
         this.quoteAlpha -= 4;
      }

      if (this.quoteAlpha < 0 && this.quoteFade) {
         this.randomQuote = this.randomQuotes.get(Utilities.randomInt(this.randomQuotes.size()));
         this.quoteFade = false;
      }

      if (this.quoteAlpha < 255 && !this.quoteFade) {
         this.quoteAlpha += 4;
      }
   }

   @Override
   public int getCenterX() {
      return this.x + this.logo.getWidth() / 2;
   }

   @Override
   public int getCenterY() {
      return this.y + this.logo.getHeight() / 2;
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
      return this.x + this.logo.getWidth();
   }

   @Override
   public int getBottomY() {
      return this.y + this.logo.getHeight();
   }
}
