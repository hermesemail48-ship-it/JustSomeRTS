package rtr.gui.buttons;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.goal.Goal;
import rtr.particles.ParticleModule;
import rtr.utilities.Utilities;

public class GUIButtonGoalStats extends GUIButton {
   private Image goalPanelCompleted;
   private Image goalPanelLocked;
   private Image goalPanelUnlocked;
   private Image perksIcon;
   private Image iconImage;
   private Goal thisGoal;
   private boolean playEffects;
   private boolean appearing;
   private boolean alphaFlip;
   private int alphaLevel;
   private int appearAlphaLevel = 255;
   private String goalDescription;
   private float backgroundAlphaBlack;
   private float rotation;
   private float lengthMod = -500.0F;
   private boolean fadeOut;

   public GUIButtonGoalStats(String bName, Goal g) throws SlickException {
      this.buttonName = bName;
      this.thisGoal = g;
      this.perksIcon = ImageLoader.getImage("res/GUI/icon/iconGodXP.png");
      this.iconImage = g.getGoalIconImageUnlocked();
      this.goalPanelCompleted = ImageLoader.getImage("res/GUI/gameState/goalPanelCompleted" + (Utilities.randomInt(6) + 1) + ".png");
      this.goalPanelLocked = ImageLoader.getImage("res/GUI/gameState/goalPanelLocked" + (Utilities.randomInt(6) + 1) + ".png");
      this.goalPanelUnlocked = ImageLoader.getImage("res/GUI/gameState/goalPanelUnlocked" + (Utilities.randomInt(6) + 1) + ".png");
      this.goalDescription = g.getDescription();
      this.intersectBox = new Rectangle(0.0F, 0.0F, this.goalPanelCompleted.getWidth() - 6, this.goalPanelCompleted.getHeight() - 6);
   }

   public void render(Graphics g, Rectangle mouse, float x, float y, boolean debug) throws SlickException {
      super.render(g, (int)x, (int)y, false);
      Image goalPanel = this.goalPanelUnlocked;
      if (this.thisGoal.isLocked()) {
         goalPanel = this.goalPanelLocked;
      }

      if (this.thisGoal.isCompleted() || !this.thisGoal.isLocked() && this.thisGoal.isFreeChest()) {
         goalPanel = this.goalPanelCompleted;
      }

      int progressBarLength = (int)Utilities.getPercentage(this.thisGoal.getGoalAmount(), this.thisGoal.getGoalAmountRequired(), 126.0);
      if (progressBarLength > 126 || !this.thisGoal.isLocked() && this.thisGoal.isFreeChest()) {
         progressBarLength = 126;
      }

      if (this.thisGoal.isCompleted()) {
         g.setColor(new Color(255, 255, 255));
      } else {
         g.setColor(new Color(150, 255, 0));
      }

      g.fillRect(x + 57.0F, y + 49.0F, progressBarLength, 5.0F);
      if (this.iconImage != null) {
         this.iconImage.draw(x + 12.0F, y + 20.0F);
      }

      if (!this.thisGoal.isCompleted() && (this.thisGoal.isLocked() || !this.thisGoal.isFreeChest())) {
         if (!this.isSelected && !this.intersects(mouse)) {
            goalPanel.draw(x, y);
         } else {
            goalPanel.draw(x, y);
            GL11.glBlendFunc(774, 770);
            goalPanel.drawFlash(x, y, goalPanel.getWidth(), goalPanel.getHeight(), Color.white);
            g.setDrawMode(Graphics.MODE_NORMAL);
         }
      } else {
         this.alphaLevel = this.alphaLevel + (this.alphaFlip ? -10 : 10);
         if (this.alphaLevel > 200) {
            this.alphaFlip = true;
         }

         if (this.alphaLevel < 0) {
            this.alphaFlip = false;
         }

         goalPanel.draw(x, y);
         GL11.glBlendFunc(774, 770);
         if (this.intersects(mouse)) {
            goalPanel.drawFlash(x, y, goalPanel.getWidth(), goalPanel.getHeight(), Color.white);
         } else {
            goalPanel.drawFlash(x, y, goalPanel.getWidth(), goalPanel.getHeight(), new Color(255, 255, 255, this.alphaLevel));
         }

         g.setDrawMode(Graphics.MODE_NORMAL);
      }

      if (Text.getWidth(this.thisGoal.getName(), Text.FontType.BODY, 1) > 160) {
         if (!this.isSelected && !this.intersects(mouse)) {
            this.font.drawString(x + 84.0F, y + 4.0F, this.thisGoal.getName(), Text.FontType.BODY, 0, true);
         } else {
            this.font.drawString(x + 84.0F, y + 4.0F, "$GRE1" + this.thisGoal.getName(), Text.FontType.BODY, 0, true);
         }
      } else if (!this.isSelected && !this.intersects(mouse)) {
         this.font.drawString(x + 84.0F, y + 2.0F, this.thisGoal.getName(), Text.FontType.BODY, 1, true);
      } else {
         this.font.drawString(x + 84.0F, y + 2.0F, "$GRE1" + this.thisGoal.getName(), Text.FontType.BODY, 1, true);
      }

      this.perksIcon.draw(x + 186.0F, y + 49.0F);
      if (!this.isSelected && !this.intersects(mouse)) {
         this.font.drawString(x + 203.0F, y + 50.0F, Text.getText("playGoalPopup.godXP") + ": $GRE1" + this.thisGoal.getGodXPAmount(), Text.FontType.BODY, 0);
      } else {
         this.font
            .drawString(x + 203.0F, y + 50.0F, "$GRE1" + Text.getText("playGoalPopup.godXP") + ": " + this.thisGoal.getGodXPAmount(), Text.FontType.BODY, 0);
      }

      if (Text.getWidth(this.goalDescription, Text.FontType.BODY, 1) > 190) {
         if (Text.getWidth(this.goalDescription, Text.FontType.BODY, 0) > 190) {
            this.font
               .drawString(
                  x + 24.0F + goalPanel.getWidth() / 2,
                  y + 18.0F,
                  Text.wrapString(this.goalDescription, Text.FontType.BODY, 0, 190),
                  Text.FontType.BODY,
                  0,
                  true
               );
         } else {
            this.font.drawString(x + 24.0F + goalPanel.getWidth() / 2, y + 25.0F, this.goalDescription, Text.FontType.BODY, 0, true);
         }
      } else {
         this.font.drawString(x + 24.0F + goalPanel.getWidth() / 2, y + 24.0F, this.goalDescription, Text.FontType.BODY, 1, true);
      }

      if (this.appearing) {
         goalPanel.drawFlash(x, y, goalPanel.getWidth(), goalPanel.getHeight(), new Color(255, 255, 255, this.appearAlphaLevel));
         this.appearAlphaLevel -= 2;
         if (this.appearAlphaLevel <= 0) {
            this.appearing = false;
         }
      }

      if (debug) {
         this.font
            .drawString(
               x + 27.0F, y + 40.0F, "$BLU0" + this.thisGoal.getGoalAmount() + "/" + this.thisGoal.getGoalAmountRequired(), Text.FontType.BODY, 0, true
            );
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   public void toggleEffectsFadeout() {
      this.fadeOut = true;
   }

   public void toggleParticles(int x, int y) {
      for (int i = 0; i < 3; i++) {
         this.projectile
            .newExpandingEnergyRing(
               x + this.goalPanelCompleted.getWidth() / 2,
               y + this.goalPanelCompleted.getHeight() / 2,
               Utilities.randomInt(256) + 256,
               Utilities.randomInt(6) + 2,
               ParticleModule.ParticleSet.FIRE_YELLOW,
               ParticleModule.ParticleSet.WHITE,
               true
            );
      }

      this.projectile
         .newExpandingEnergyRing(
            x + this.goalPanelCompleted.getWidth() / 2,
            y + this.goalPanelCompleted.getHeight() / 2,
            Utilities.randomInt(256) + 512,
            8.0F,
            ParticleModule.ParticleSet.FIRE_YELLOW,
            ParticleModule.ParticleSet.WHITE,
            true
         );
   }

   public void renderEffects(Graphics g, int x, int y) {
      if (this.thisGoal.isCompleted()) {
         if (this.backgroundAlphaBlack < 200.0F) {
            this.backgroundAlphaBlack += 2.0F;
         }

         float finalRotation = this.rotation;
         if (this.fadeOut) {
            this.lengthMod -= 5.0F;
            if (this.lengthMod < -500.0F) {
               this.lengthMod = -500.0F;
            }
         } else {
            this.lengthMod += 5.0F;
            if (this.lengthMod > 100.0F) {
               this.lengthMod = 100.0F;
            }
         }

         for (int d = 0; d < 2; d++) {
            if (d == 1) {
               finalRotation = -finalRotation * 0.5F;
            } else {
               finalRotation = -finalRotation;
            }

            for (int c = 0; c < 15; c++) {
               float length = 14 * c;
               if (d == 1) {
                  length *= 2.0F;
                  g.setColor(new Color(255, 225, 130, 2));
               } else {
                  g.setColor(new Color(255, 255, 55, 3));
               }

               length += this.lengthMod;
               if (length < 0.0F) {
                  length = 0.0F;
               }

               for (int i = 0; i < 9; i++) {
                  g.fillArc(
                     x - length / 2.0F + this.goalPanelCompleted.getWidth() / 2 - 20.0F,
                     y - length / 2.0F + this.goalPanelCompleted.getHeight() / 2,
                     length,
                     length,
                     0.0F + finalRotation + i * 40,
                     20.0F + finalRotation + i * 40
                  );
                  g.fillArc(
                     x - length / 2.0F + this.goalPanelCompleted.getWidth() / 2 + 20.0F,
                     y - length / 2.0F + this.goalPanelCompleted.getHeight() / 2,
                     length,
                     length,
                     0.0F + finalRotation + i * 40,
                     20.0F + finalRotation + i * 40
                  );
               }
            }
         }

         this.rotation += 0.5F;
      }
   }

   public Goal getGoal() {
      return this.thisGoal;
   }

   public int getWidth() {
      return this.goalPanelCompleted.getWidth();
   }

   public int getHeight() {
      return this.goalPanelCompleted.getHeight();
   }
}
