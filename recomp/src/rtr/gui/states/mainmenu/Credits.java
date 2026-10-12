package rtr.gui.states.mainmenu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.font.Text;
import rtr.gui.buttons.GUIButtonMainMenu;
import rtr.gui.states.MainMenuGUIController;
import rtr.gui.states.MainMenuGUIData;
import rtr.gui.states.MainMenuGUIPanelBase;
import rtr.system.ScaleControl;

public class Credits extends MainMenuGUIPanelBase {
   private GUIButtonMainMenu back;
   private ArrayList<ArrayList<String>> roll = new ArrayList<>();
   private float rollY;

   public Credits(MainMenuGUIController controller, MainMenuGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.back = new GUIButtonMainMenu("back", Text.getText("mainMenuCredits.button.back"), 0);
      this.guiButtons.add(this.back);
      ArrayList<String> patreons1 = new ArrayList<>();
      ArrayList<String> patreons5 = new ArrayList<>();
      ArrayList<String> patreons10 = new ArrayList<>();
      ArrayList<String> kickstarters1 = new ArrayList<>();
      ArrayList<String> kickstarters5 = new ArrayList<>();
      ArrayList<String> kickstarters7 = new ArrayList<>();
      ArrayList<String> kickstarters10 = new ArrayList<>();
      ArrayList<String> kickstarters20 = new ArrayList<>();
      ArrayList<String> kickstarters30 = new ArrayList<>();
      ArrayList<String> kickstarters40 = new ArrayList<>();
      ArrayList<String> kickstarters50 = new ArrayList<>();
      ArrayList<String> kickstarters100 = new ArrayList<>();
      ArrayList<String> kickstarters150 = new ArrayList<>();
      ArrayList<String> kickstarters300 = new ArrayList<>();

      try {
         FileInputStream file = new FileInputStream("res/credits.txt");
         BufferedReader reader = new BufferedReader(new InputStreamReader(file));
         int fill = 1;

         String line;
         while ((line = reader.readLine()) != null) {
            if (line.equals("---1P---")) {
               fill = -1;
            } else if (line.equals("---5P---")) {
               fill = -5;
            } else if (line.equals("---10P---")) {
               fill = -10;
            } else if (line.equals("---1K---")) {
               fill = 1;
            } else if (line.equals("---5K---")) {
               fill = 5;
            } else if (line.equals("---7K---")) {
               fill = 7;
            } else if (line.equals("---10K---")) {
               fill = 10;
            } else if (line.equals("---20K---")) {
               fill = 20;
            } else if (line.equals("---30K---")) {
               fill = 30;
            } else if (line.equals("---40K---")) {
               fill = 40;
            } else if (line.equals("---50K---")) {
               fill = 50;
            } else if (line.equals("---100K---")) {
               fill = 100;
            } else if (line.equals("---150K---")) {
               fill = 150;
            } else if (line.equals("---300K---")) {
               fill = 300;
            } else {
               if (fill == -1) {
                  patreons1.add(new String(line.getBytes(), "UTF-8"));
               }

               if (fill == -5) {
                  patreons5.add(new String(line.getBytes(), "UTF-8"));
               }

               if (fill == -10) {
                  patreons10.add(new String(line.getBytes(), "UTF-8"));
               }

               if (fill == 1) {
                  kickstarters1.add(new String(line.getBytes(), "UTF-8"));
               }

               if (fill == 5) {
                  kickstarters5.add(new String(line.getBytes(), "UTF-8"));
               }

               if (fill == 7) {
                  kickstarters7.add(new String(line.getBytes(), "UTF-8"));
               }

               if (fill == 10) {
                  kickstarters10.add(new String(line.getBytes(), "UTF-8"));
               }

               if (fill == 20) {
                  kickstarters20.add(new String(line.getBytes(), "UTF-8"));
               }

               if (fill == 30) {
                  kickstarters30.add(new String(line.getBytes(), "UTF-8"));
               }

               if (fill == 40) {
                  kickstarters40.add(new String(line.getBytes(), "UTF-8"));
               }

               if (fill == 50) {
                  kickstarters50.add(new String(line.getBytes(), "UTF-8"));
               }

               if (fill == 100) {
                  kickstarters100.add(new String(line.getBytes(), "UTF-8"));
               }

               if (fill == 150) {
                  kickstarters150.add(new String(line.getBytes(), "UTF-8"));
               }

               if (fill == 300) {
                  kickstarters300.add(new String(line.getBytes(), "UTF-8"));
               }
            }
         }

         this.roll.add(patreons10);
         this.roll.add(patreons5);
         this.roll.add(patreons1);
         this.roll.add(kickstarters300);
         this.roll.add(kickstarters150);
         this.roll.add(kickstarters100);
         this.roll.add(kickstarters50);
         this.roll.add(kickstarters40);
         this.roll.add(kickstarters30);
         this.roll.add(kickstarters20);
         this.roll.add(kickstarters10);
         this.roll.add(kickstarters7);
         this.roll.add(kickstarters5);
         this.roll.add(kickstarters1);
         file.close();
         reader.close();
      } catch (IOException var24) {
         var24.printStackTrace();
      }
   }

   public void render(boolean debug) {
      this.x = ScaleControl.getInterfaceWidth() / 2;
      this.y = 0;
      this.controller.getLogo().render(this.x, this.y + this.rollY);
      this.rollY--;
      int alignY = 250;
      int alignX = 0;
      this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.developer"), Text.FontType.HEADER, 6, true);
      alignY += 35;
      this.font.drawString(this.x, this.y + alignY + this.rollY, "$RED0Raymond 'Rayvolution' Doerr", Text.FontType.HEADER, 4, true);
      alignY += 50;
      this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.music"), Text.FontType.HEADER, 5, true);
      alignY += 30;
      this.font.drawString(this.x, this.y + alignY + this.rollY, "$BLU0José Ramón 'Bibiki' García", Text.FontType.HEADER, 4, true);
      alignY += 50;
      this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.communityModerator"), Text.FontType.HEADER, 5, true);
      alignY += 30;
      this.font.drawString(this.x, this.y + alignY + this.rollY, "$ORA0Kyle 'Tiberiumkyle' Harris", Text.FontType.HEADER, 4, true);
      alignY += 50;
      this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.contributors"), Text.FontType.HEADER, 5, true);
      alignY += 35;
      this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.creditLine.fallenShogun"), Text.FontType.HEADER, 4, true);
      alignY += 25;
      this.font.drawString(this.x, this.y + alignY + this.rollY, "$MAG1FallenShogun", Text.FontType.HEADER, 4, true);
      alignY += 40;
      this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.creditLine.shade"), Text.FontType.HEADER, 4, true);
      alignY += 25;
      this.font.drawString(this.x, this.y + alignY + this.rollY, "$MAG1Mason 'Shaders...Ellipses' Hadley", Text.FontType.HEADER, 4, true);
      alignY += 40;
      this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.creditLine.veldrin"), Text.FontType.HEADER, 4, true);
      alignY += 25;
      this.font.drawString(this.x, this.y + alignY + this.rollY, "$MAG1Veldrin", Text.FontType.HEADER, 4, true);
      alignY += 80;
      this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.thanks"), Text.FontType.HEADER, 4, true);
      alignY += 40;

      for (int i = 0; i < this.roll.size(); i++) {
         if (i == 0) {
            Text.setVariableText("amount", 10);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.patreons"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         if (i == 1) {
            Text.setVariableText("amount", 5);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.patreons"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         if (i == 2) {
            Text.setVariableText("amount", 1);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.patreons"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         if (i == 3) {
            Text.setVariableText("amount", 300);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.kickstarter"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         if (i == 4) {
            Text.setVariableText("amount", 150);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.kickstarter"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         if (i == 5) {
            Text.setVariableText("amount", 100);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.kickstarter"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         if (i == 6) {
            Text.setVariableText("amount", 50);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.kickstarter"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         if (i == 7) {
            Text.setVariableText("amount", 40);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.kickstarter"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         if (i == 8) {
            Text.setVariableText("amount", 30);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.kickstarter"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         if (i == 9) {
            Text.setVariableText("amount", 20);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.kickstarter"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         if (i == 10) {
            Text.setVariableText("amount", 10);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.kickstarter"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         if (i == 11) {
            Text.setVariableText("amount", 7);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.kickstarter"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         if (i == 12) {
            Text.setVariableText("amount", 5);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.kickstarter"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         if (i == 13) {
            Text.setVariableText("amount", 1);
            this.font.drawString(this.x, this.y + alignY + this.rollY, Text.getText("mainMenuCredits.header.kickstarter"), Text.FontType.HEADER, 5, true);
            alignY += 15;
         }

         for (int k = 0; k < this.roll.get(i).size(); k++) {
            short var20;
            if (k % 2 == 0) {
               var20 = -200;
               alignY += 20;
            } else {
               var20 = 200;
            }

            if (this.y + alignY + this.rollY < ScaleControl.getInterfaceHeight() + 20 && this.y + alignY + this.rollY > -20.0F) {
               this.font.drawString(this.x + var20, this.y + alignY + this.rollY, "$CYA1" + this.roll.get(i).get(k), Text.FontType.BODY, 3, true);
            }
         }

         alignY += 80;
      }

      this.back.render(this.g, this.mouse, ScaleControl.getInterfaceWidth() / 2 - 50, ScaleControl.getInterfaceHeight() - 140, false, debug);
   }

   public void setRollY(int i) {
      this.rollY = i;
   }
}
