package rtr.gui.buttons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.font.Text;
import rtr.save.SavedGamesHandler;

public class GUIButtonMapPackList extends GUIButton {
   private ArrayList<GUIClickableText> mapPackList = new ArrayList<>();
   private ArrayList<Properties> properties = new ArrayList<>();
   private GUIClickableText buttonMapListDown;
   private GUIClickableText buttonMapListUp;
   private GUIButtonInputText buttonNewMapPack;
   private int mapListStart = 0;
   private int mapPage = 1;
   private int currentPosition = 0;
   private boolean allowOfficial;

   public GUIButtonMapPackList(GameContainer gc, String mP, boolean aO) throws SlickException {
      this.allowOfficial = aO;
      this.reloadMapPackList();

      for (int i = 0; i < this.mapPackList.size(); i++) {
         if (this.mapPackList.get(i).getButtonName().equals(mP)) {
            this.currentPosition = i;
         }
      }

      this.selectMap(this.currentPosition);
      this.buttonNewMapPack = new GUIButtonInputText(gc, "buttonNewMapPack", "New Map Pack", 24, 2);
      this.buttonMapListDown = new GUIClickableText("buttonMapListDown", 2);
      this.buttonMapListUp = new GUIClickableText("buttonMapListUp", 2);
   }

   @Override
   public void hideButton() {
      for (int i = 0; i < this.mapPackList.size(); i++) {
         this.mapPackList.get(i).hideButton();
      }

      this.buttonNewMapPack.hideButton();
      this.buttonMapListDown.hideButton();
      this.buttonMapListUp.hideButton();
   }

   public void reloadMapPackList() throws SlickException {
      this.properties = new ArrayList<>();
      this.mapPackList = new ArrayList<>();
      HashMap<String, Properties> mapPacks = SavedGamesHandler.getAllMapPackProperties();

      for (String s : mapPacks.keySet()) {
         Properties p = mapPacks.get(s);
         if (this.allowOfficial || !p.getProperty("mapPackOfficial", "false").equals("true")) {
            this.properties.add(p);
            this.mapPackList.add(new GUIClickableText(s, 2));
         }
      }
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean debug) {
      int totalPages = this.mapPackList.size() / 9;
      if (this.mapPackList.size() % 9 > 0) {
         totalPages++;
      }

      int alignY = 65;
      Text.setVariableText("totalMaps", this.mapPackList.size());
      this.font.drawString(x - 251, y + 40, Text.getText("mainMenuMapListButton.totalMaps"), Text.FontType.BODY, 2);
      Text.setVariableText("currentPage", this.mapPage);
      Text.setVariableText("totalPages", totalPages);
      this.font.drawString(x + 173, y + 40, Text.getText("mainMenuMapListButton.pageList"), Text.FontType.BODY, 2);
      if (this.mapPackList.size() > 0) {
         for (int i = 0; i < this.mapPackList.size(); i++) {
            this.mapPackList.get(i).hideButton();
         }
      }

      if (this.mapPackList.size() > 0) {
         for (int i = 0; i < 9; i++) {
            if (this.mapListStart + i < this.mapPackList.size()) {
               try {
                  String parsedMapName;
                  if (this.properties.get(this.mapListStart + i).getProperty("mapPackName").length() > 50) {
                     parsedMapName = this.properties.get(this.mapListStart + i).getProperty("mapPackName").substring(0, 50) + "..";
                  } else {
                     parsedMapName = this.properties.get(this.mapListStart + i).getProperty("mapPackName");
                  }

                  if (!this.buttonNewMapPack.getText().equals("")) {
                     this.font.drawString(x, y + alignY - 3, "$GRE2" + this.mapPackList.get(this.mapListStart + i).getText(), Text.FontType.BODY, 2, true);
                  } else {
                     this.mapPackList.get(this.mapListStart + i).render(g, mouse, parsedMapName, x, y + alignY, "$WHI0", "$GRE1", false, true, debug);
                  }
               } catch (Exception var11) {
                  this.font.drawString(x, y + alignY - 3, "$RED2" + this.mapPackList.get(this.mapListStart + i).getButtonName(), Text.FontType.BODY, 2, true);
               }

               alignY += 20;
            }
         }
      }

      if (totalPages != 0) {
         if (this.mapPage != 1) {
            this.buttonMapListDown.render(g, mouse, Text.getText("mainMenuMapListButton.button.lastPage"), x, y + 40, "$GRE1", "$GRE0", false, true, debug);
         } else {
            this.buttonMapListDown.render(g, mouse, Text.getText("mainMenuMapListButton.button.lastPage"), x, y + 40, "$BLA2", "$BLA2", false, true, debug);
         }

         if (this.mapPage != totalPages) {
            this.buttonMapListUp.render(g, mouse, Text.getText("mainMenuMapListButton.button.nextPage"), x, y + 70 + 180, "$GRE1", "$GRE0", false, true, debug);
         } else {
            this.buttonMapListUp.render(g, mouse, Text.getText("mainMenuMapListButton.button.nextPage"), x, y + 70 + 180, "$BLA2", "$BLA2", false, true, debug);
         }
      }

      this.buttonNewMapPack.render(g, mouse, x - 160, y + 70 + 200, debug);
   }

   @Override
   public boolean intersects(Rectangle mouse) {
      for (int i = 0; i < this.mapPackList.size(); i++) {
         if (this.mapPackList.get(i).intersects(mouse)) {
            return true;
         }
      }

      return false;
   }

   public String getMapPackName() {
      return this.buttonNewMapPack.getText().equals("") ? this.mapPackList.get(this.currentPosition).getText() : this.buttonNewMapPack.getText();
   }

   public Properties getMapProperties(Rectangle mouse) {
      for (int i = 0; i < this.mapPackList.size(); i++) {
         if (this.mapPackList.get(i).intersects(mouse)) {
            return this.properties.get(i);
         }
      }

      return null;
   }

   public Properties getMapProperties(int m) {
      return this.properties.get(m);
   }

   public void selectMap(Rectangle mouse) {
      for (int i = 0; i < this.mapPackList.size(); i++) {
         if (this.mapPackList.get(i).intersects(mouse)) {
            this.currentPosition = i;
            this.mapPackList.get(i).select();
         } else {
            this.mapPackList.get(i).deselect();
         }
      }
   }

   public void selectMap(int m) {
      for (int i = 0; i < this.mapPackList.size(); i++) {
         if (i == m) {
            this.currentPosition = i;
            this.mapPackList.get(i).select();
         } else {
            this.mapPackList.get(i).deselect();
         }
      }
   }

   public boolean intersectsPageUp(Rectangle mouse) {
      return this.buttonMapListUp.intersects(mouse);
   }

   public boolean intersectsPageDown(Rectangle mouse) {
      return this.buttonMapListDown.intersects(mouse);
   }

   public boolean intersectsNewPack(Rectangle mouse) {
      return this.buttonNewMapPack.intersects(mouse);
   }

   public void mapListPageUp() {
      if (this.mapListStart + 9 < this.mapPackList.size()) {
         this.mapListStart += 9;
         this.mapPage++;
      }
   }

   public void mapListPageDown() {
      if (this.mapListStart != 0) {
         this.mapListStart -= 9;
         this.mapPage--;
      }
   }

   public void setFocusMapListNewPack() {
      this.buttonNewMapPack.setFocus(true);
   }
}
