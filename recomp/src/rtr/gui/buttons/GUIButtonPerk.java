package rtr.gui.buttons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EnumMap;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.PerkModule;
import rtr.font.Text;
import rtr.resources.ResourceModule;
import rtr.utilities.Utilities;

public class GUIButtonPerk extends GUIButton {
   private Image buttonFrame;
   private Image buttonFrameTrim;
   private Image perkBarListPanel;
   private Image iconImage;
   private Image iconGodXP;
   private PerkModule.PerkType perkType;
   private double value;

   public GUIButtonPerk(String bName, PerkModule.PerkType t) throws SlickException {
      this.buttonName = bName;
      this.perkType = t;
      this.value = this.perk.getPerkTypeTotal(this.perkType);
      BigDecimal bd = BigDecimal.valueOf(this.value);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      this.value = bd.doubleValue();

      try {
         if (this.perkType.getResourceIconType() != null) {
            this.iconImage = this.resource
               .getResourceSprite(this.resource.getResourceSpriteSet(this.perkType.getResourceIconType(), ResourceModule.ResourceColorSet.DEFAULT), 0);
         } else {
            this.iconImage = ImageLoader.getImage("res/GUI/icon/" + this.perkType.getIconName() + ".png");
         }
      } catch (Exception var10) {
         this.iconImage = ImageLoader.getImage("res/GUI/icon/iconNone.png");
      }

      this.iconGodXP = ImageLoader.getImage("res/GUI/icon/iconGodXP.png");
      switch (this.perkType.getPerkCategory()) {
         case REGULAR:
            this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/perkBarRegular" + (Utilities.randomInt(10) + 1) + ".png");
            break;
         case DEFENSE:
            this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/perkBarDefense" + (Utilities.randomInt(10) + 1) + ".png");
            break;
         case GOD:
            this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/perkBarGod" + (Utilities.randomInt(10) + 1) + ".png");
            break;
         case VILLAGE:
            this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/perkBarVillage" + (Utilities.randomInt(10) + 1) + ".png");
      }

      int totalPerks = 0;
      int havePerks = 0;

      PerkModule.Perk[] var9;
      for (PerkModule.Perk p : PerkModule.Perk.values()) {
         if (p.getPerkType() == this.perkType) {
            totalPerks++;
            if (this.perk.getAllUnlockedPerks().get(p)) {
               havePerks++;
            }
         }
      }

      if (totalPerks == 5) {
         if (havePerks == 5) {
            this.buttonFrameTrim = ImageLoader.getImage("res/GUI/gameState/perkBarGoldTrim.png");
         } else if (havePerks >= 3) {
            this.buttonFrameTrim = ImageLoader.getImage("res/GUI/gameState/perkBarSilverTrim.png");
         } else if (havePerks >= 2) {
            this.buttonFrameTrim = ImageLoader.getImage("res/GUI/gameState/perkBarBronzeTrim.png");
         }
      }

      if (totalPerks == 3) {
         if (havePerks == 3) {
            this.buttonFrameTrim = ImageLoader.getImage("res/GUI/gameState/perkBarGoldTrim.png");
         } else if (havePerks == 2) {
            this.buttonFrameTrim = ImageLoader.getImage("res/GUI/gameState/perkBarSilverTrim.png");
         } else if (havePerks == 1) {
            this.buttonFrameTrim = ImageLoader.getImage("res/GUI/gameState/perkBarBronzeTrim.png");
         }
      }

      this.perkBarListPanel = ImageLoader.getImage("res/GUI/gameState/perkBarListPanel.png");
      this.intersectBox = new Rectangle(0.0F, 0.0F, this.buttonFrame.getWidth() - 6, this.buttonFrame.getHeight() - 6);
   }

   public GUIButtonPerk(String bName, PerkModule.Perk p) throws SlickException {
      this.buttonName = bName;
      this.perkType = p.getPerkType();
      this.value = p.getValue();
      BigDecimal bd = BigDecimal.valueOf(this.value);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      this.value = bd.doubleValue();

      try {
         if (this.perkType.getResourceIconType() != null) {
            this.iconImage = this.resource
               .getResourceSprite(this.resource.getResourceSpriteSet(this.perkType.getResourceIconType(), ResourceModule.ResourceColorSet.DEFAULT), 0);
         } else {
            this.iconImage = ImageLoader.getImage("res/GUI/icon/" + this.perkType.getIconName() + ".png");
         }
      } catch (Exception var5) {
         this.iconImage = ImageLoader.getImage("res/GUI/icon/iconNone.png");
      }

      switch (this.perkType.getPerkCategory()) {
         case REGULAR:
            this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/perkBarRegular" + (Utilities.randomInt(10) + 1) + ".png");
            break;
         case DEFENSE:
            this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/perkBarDefense" + (Utilities.randomInt(10) + 1) + ".png");
            break;
         case GOD:
            this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/perkBarGod" + (Utilities.randomInt(10) + 1) + ".png");
            break;
         case VILLAGE:
            this.buttonFrame = ImageLoader.getImage("res/GUI/gameState/perkBarVillage" + (Utilities.randomInt(10) + 1) + ".png");
      }

      this.intersectBox = new Rectangle(0.0F, 0.0F, this.buttonFrame.getWidth() - 6, this.buttonFrame.getHeight() - 6);
   }

   public void render(Graphics g, Rectangle mouse, int x, int y, boolean debug) {
      super.render(g, x, y, false);
      if (!this.isSelected && !this.intersects(mouse)) {
         this.buttonFrame.draw(x, y);
      } else {
         this.buttonFrame.draw(x, y);
         GL11.glBlendFunc(774, 770);
         this.buttonFrame.drawFlash(x, y, this.buttonFrame.getWidth(), this.buttonFrame.getHeight(), Color.white);
         g.setDrawMode(Graphics.MODE_NORMAL);
      }

      if (this.buttonFrameTrim != null) {
         this.buttonFrameTrim.draw(x, y);
      }

      this.iconImage.draw(x + 7, y + 6);
      String stringOut = "";
      if (this.perkType.getDataFormat() == PerkModule.PerkDataFormat.WHOLE_NUMBER) {
         String valueOut = this.formatValue(String.format("%.2f", this.value));
         if (!this.isSelected && !this.intersects(mouse)) {
            stringOut = "+" + valueOut + " " + this.perkType.getPerkName();
         } else {
            stringOut = "$GRE1+" + valueOut + " " + this.perkType.getPerkName();
         }
      } else if (this.perkType.getDataFormat() == PerkModule.PerkDataFormat.PERCENT) {
         String valueOut = this.formatValue(String.format("%.2f", this.value * 100.0));
         if (!this.isSelected && !this.intersects(mouse)) {
            stringOut = "+" + valueOut + "% " + this.perkType.getPerkName();
         } else {
            stringOut = "$GRE1+" + valueOut + "% " + this.perkType.getPerkName();
         }
      } else if (this.perkType.getDataFormat() == PerkModule.PerkDataFormat.PERCENT_NEGATIVE) {
         String valueOut = this.formatValue(String.format("%.2f", this.value * 100.0));
         if (!this.isSelected && !this.intersects(mouse)) {
            stringOut = "-" + valueOut + "% " + this.perkType.getPerkName();
         } else {
            stringOut = "$GRE1-" + valueOut + "% " + this.perkType.getPerkName();
         }
      } else if (this.perkType.getDataFormat() == PerkModule.PerkDataFormat.SECONDS) {
         String valueOut = this.formatValue(String.format("%.2f", this.value));
         if (!this.isSelected && !this.intersects(mouse)) {
            stringOut = "+" + valueOut + " " + Text.getText("perk.seconds") + " " + this.perkType.getPerkName();
         } else {
            stringOut = "$GRE1+" + valueOut + " " + Text.getText("perk.seconds") + " " + this.perkType.getPerkName();
         }
      } else if (this.perkType.getDataFormat() == PerkModule.PerkDataFormat.SECONDS_NEGATIVE) {
         String valueOut = this.formatValue(String.format("%.2f", this.value));
         if (!this.isSelected && !this.intersects(mouse)) {
            stringOut = "-" + valueOut + " " + Text.getText("perk.seconds") + " " + this.perkType.getPerkName();
         } else {
            stringOut = "$GRE1-" + valueOut + " " + Text.getText("perk.seconds") + " " + this.perkType.getPerkName();
         }
      }

      if (Text.getWidth(stringOut, Text.FontType.BODY, 1) > 210) {
         this.font.drawString(x + 32, y + 7, stringOut, Text.FontType.BODY, 0);
      } else {
         this.font.drawString(x + 32, y + 6, stringOut, Text.FontType.BODY, 1);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   private String formatValue(String valueOut) {
      if (valueOut.endsWith(".0") || valueOut.endsWith(".00")) {
         valueOut = valueOut.replace(".00", "");
         valueOut = valueOut.replace(".0", "");
      }

      return valueOut;
   }

   public void renderPerkList(Graphics g, int x, int y) {
      ArrayList<PerkModule.Perk> perks = new ArrayList<>();
      EnumMap<PerkModule.Perk, Boolean> allPerks = this.perk.getAllUnlockedPerks();

      for (PerkModule.Perk p : allPerks.keySet()) {
         if (allPerks.get(p) && p.getPerkType() == this.perkType) {
            perks.add(p);
         }
      }

      int yOffset = perks.size() * 12 + 20;
      g.setWorldClip(x, y, this.perkBarListPanel.getWidth(), yOffset);
      this.perkBarListPanel.draw(x, y - this.perkBarListPanel.getHeight() + yOffset);
      g.clearWorldClip();
      int alignY = 0;

      for (PerkModule.Perk px : perks) {
         if (allPerks.get(px) && px.getPerkType() == this.perkType) {
            String stringOut = "";
            if (this.perkType.getDataFormat() == PerkModule.PerkDataFormat.WHOLE_NUMBER) {
               String valueOut = Double.toString(px.getValue());
               if (valueOut.endsWith(".0")) {
                  valueOut = valueOut.replace(".0", "");
               }

               stringOut = "($YEL1+" + valueOut + "$WHI0)";
            } else if (this.perkType.getDataFormat() == PerkModule.PerkDataFormat.PERCENT) {
               String valueOut = Double.toString(px.getValue() * 100.0);
               if (valueOut.endsWith(".0")) {
                  valueOut = valueOut.replace(".0", "");
               }

               stringOut = "($YEL1+" + valueOut + "%$WHI0)";
            } else if (this.perkType.getDataFormat() == PerkModule.PerkDataFormat.PERCENT_NEGATIVE) {
               String valueOut = Double.toString(px.getValue() * 100.0);
               if (valueOut.endsWith(".0")) {
                  valueOut = valueOut.replace(".0", "");
               }

               stringOut = "($YEL1-" + valueOut + "%$WHI0)";
            } else if (this.perkType.getDataFormat() == PerkModule.PerkDataFormat.SECONDS) {
               String valueOut = Double.toString(px.getValue());
               if (valueOut.endsWith(".0")) {
                  valueOut = valueOut.replace(".0", "");
               }

               stringOut = "($YEL1+" + (int)px.getValue() + "$WHI0)";
            } else if (this.perkType.getDataFormat() == PerkModule.PerkDataFormat.SECONDS_NEGATIVE) {
               String valueOut = Double.toString(px.getValue());
               if (valueOut.endsWith(".0")) {
                  valueOut = valueOut.replace(".0", "");
               }

               stringOut = "($YEL1-" + valueOut + "$WHI0)";
            }

            this.iconGodXP.draw(x + 11, y + 3 + alignY);
            this.font.drawString(x + 27, y + 3 + alignY, px.getPerkName(), Text.FontType.BODY, 1);
            this.font.drawString(x + 170, y + 3 + alignY, stringOut, Text.FontType.BODY, 1);
            alignY += 12;
         }
      }
   }

   @Override
   public void renderDescriptionToolTip(Rectangle mouse, Graphics g) {
   }

   public int getPerkOffset() {
      ArrayList<PerkModule.Perk> perks = new ArrayList<>();
      EnumMap<PerkModule.Perk, Boolean> allPerks = this.perk.getAllUnlockedPerks();

      for (PerkModule.Perk p : allPerks.keySet()) {
         if (allPerks.get(p) && p.getPerkType() == this.perkType) {
            perks.add(p);
         }
      }

      return perks.size() * 12 + 20;
   }
}
