package rtr.gui.buttons;

import java.io.File;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.imageout.ImageOut;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.font.Text;
import rtr.goal.Goal;
import rtr.goal.GoalModule;
import rtr.particles.ParticleModule;
import rtr.states.StateBase;
import rtr.utilities.Utilities;

public class GUIButtonGoal extends GUIButton {
   public static final int GOAL_BUTTON_SPACING_H = 30;
   public static final int GOAL_BUTTON_SPACING_V = 60;
   private GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   private Image buttonFrameSilver;
   private Image buttonFrameBlue;
   private Image buttonFrameBrown;
   private Image buttonFrameBlack;
   private Image buttonFrameRed;
   private Image buttonFrameGreen;
   private Image buttonIconImageUnlocked;
   private Image buttonIconImageLocked;
   private Image goalButtonBackdrop;
   private Image goalProgressBarH;
   private Image goalProgressBarV;
   private Image goalProgressBarDL;
   private Image goalProgressBarDR;
   private Image goalProgressBarCompletedH;
   private Image goalProgressBarCompletedV;
   private Image goalProgressBarCompletedDL;
   private Image goalProgressBarCompletedDR;
   private Goal thisGoal;
   private GUIButtonGoalStats toolTipButton;
   private boolean playEffects;
   private int effectsStep = 0;
   private float rotation = 0.0F;
   private int backgroundAlphaBlack = 0;
   private float textFloatX;
   private float textFloatY;
   private float textAlpha;
   private boolean alphaFlip;
   private int rotationalPositionH = 0;
   private int rotationalPositionV = 0;

   public GUIButtonGoal(Goal goal, GUIButtonGoalStats toolTipButton) throws SlickException {
      this.buttonName = goal.getSystemName();
      this.toolTipButton = toolTipButton;
      this.setToolTip(goal.getName());
      this.thisGoal = goal;
      int randomButton = Utilities.randomInt(10) + 1;
      this.buttonFrameBrown = ImageLoader.getImage("res/GUI/gameState/buttonSmallBrown" + randomButton + ".png");
      this.buttonFrameSilver = ImageLoader.getImage("res/GUI/gameState/buttonSmallSilver" + randomButton + ".png");
      this.buttonFrameBlue = ImageLoader.getImage("res/GUI/gameState/buttonSmallBlue" + randomButton + ".png");
      this.buttonFrameBlack = ImageLoader.getImage("res/GUI/gameState/buttonSmallBlack" + randomButton + ".png");
      this.buttonFrameRed = ImageLoader.getImage("res/GUI/gameState/buttonSmallRed" + randomButton + ".png");
      this.buttonFrameGreen = ImageLoader.getImage("res/GUI/gameState/buttonSmallGreen" + randomButton + ".png");
      this.goalProgressBarH = ImageLoader.getImage("res/GUI/gameState/goalProgressBarH" + (Utilities.randomInt(12) + 1) + ".png");
      this.goalProgressBarV = ImageLoader.getImage("res/GUI/gameState/goalProgressBarV" + (Utilities.randomInt(12) + 1) + ".png");
      this.goalProgressBarDL = ImageLoader.getImage("res/GUI/gameState/goalProgressBarDL" + (Utilities.randomInt(12) + 1) + ".png");
      this.goalProgressBarDR = ImageLoader.getImage("res/GUI/gameState/goalProgressBarDR" + (Utilities.randomInt(12) + 1) + ".png");
      this.goalProgressBarCompletedH = ImageLoader.getImage("res/GUI/gameState/goalProgressBarCompletedH" + (Utilities.randomInt(12) + 1) + ".png");
      this.goalProgressBarCompletedV = ImageLoader.getImage("res/GUI/gameState/goalProgressBarCompletedV" + (Utilities.randomInt(12) + 1) + ".png");
      this.goalProgressBarCompletedDL = ImageLoader.getImage("res/GUI/gameState/goalProgressBarCompletedDL" + (Utilities.randomInt(12) + 1) + ".png");
      this.goalProgressBarCompletedDR = ImageLoader.getImage("res/GUI/gameState/goalProgressBarCompletedDR" + (Utilities.randomInt(12) + 1) + ".png");
      if (goal.getGoalIconImageUnlocked() != null) {
         this.buttonIconImageUnlocked = goal.getGoalIconImageUnlocked();
      }

      if (goal.getGoalIconImageLocked() != null) {
         this.buttonIconImageLocked = goal.getGoalIconImageLocked();
      }

      this.goalButtonBackdrop = ImageLoader.getImage("res/GUI/gameState/goalButtonBackdrop" + (Utilities.randomInt(6) + 1) + ".png");
      this.intersectBox = new Rectangle(0.0F, 0.0F, this.buttonFrameBrown.getWidth() - 6, this.buttonFrameBrown.getHeight() - 6);
   }

   private void generateButton() throws SlickException {
      String desc = this.thisGoal.getDescription();
      desc = desc.replace("$GRE1", "").replace("$WHI0", "");
      System.out.println("------------------------------------------------------");
      System.out.println("System Name: " + this.thisGoal.getSystemName());
      System.out.println("Name:        " + this.thisGoal.getName());
      System.out.println("Description: " + desc);
      System.out.println("------------------------------------------------------");
      int imageScale = 1;
      Image imageOut = this.buttonIconImageUnlocked.getScaledCopy(imageScale);
      File file = new File("goalOutput/");
      file.mkdir();
      Image buttonOut = new Image((this.buttonFrameSilver.getWidth() - 6) * imageScale, (this.buttonFrameSilver.getHeight() - 6) * imageScale);
      Graphics achievementFBO = buttonOut.getGraphics();
      achievementFBO.drawImage(imageOut, 4 * imageScale, 4 * imageScale);
      achievementFBO.drawImage(this.buttonFrameSilver.getScaledCopy(imageScale), -3 * imageScale, -3 * imageScale);
      achievementFBO.flush();
      ImageOut.write(buttonOut, "goalOutput/" + this.thisGoal.getSystemName() + ".png");
      imageOut = this.buttonIconImageLocked.getScaledCopy(imageScale);
      buttonOut = new Image((this.buttonFrameBlack.getWidth() - 6) * imageScale, (this.buttonFrameBlack.getHeight() - 6) * imageScale);
      achievementFBO = buttonOut.getGraphics();
      achievementFBO.drawImage(imageOut, 4 * imageScale, 4 * imageScale, Color.white);
      achievementFBO.drawImage(this.buttonFrameBlack.getScaledCopy(imageScale), -3 * imageScale, -3 * imageScale);
      achievementFBO.flush();
      ImageOut.write(buttonOut, "goalOutput/" + this.thisGoal.getSystemName() + "Locked.png");
   }

   public int getGoalPositionAdjustedX() {
      return this.thisGoal.getGoalPositionX() * 30;
   }

   public int getGoalPositionAdjustedY() {
      return this.thisGoal.getGoalPositionY() * 60;
   }

   public void renderButton(Graphics g, int x, int y, Rectangle mouse, boolean debug) throws SlickException {
      x += this.thisGoal.getGoalPositionX() * 30;
      y += this.thisGoal.getGoalPositionY() * 60;
      super.render(g, x, y, false);
      int centerX = x + this.buttonFrameBlack.getWidth() / 2;
      int centerY = y + this.buttonFrameBlack.getHeight() / 2;
      int requiredGoals = this.thisGoal.getGoalCountRequired();
      if (this.thisGoal.isLocked() && requiredGoals == 0) {
         if (this.buttonIconImageLocked != null) {
            this.buttonIconImageLocked.drawCentered(centerX, centerY);
         }

         if (this.thisGoal.isFreeChest()) {
            this.buttonFrameBlue.draw(x, y);
         } else {
            this.buttonFrameBlack.draw(x, y);
         }

         int size = this.buttonFrameBlack.getWidth();
         if (Utilities.randomInt(10) == 0) {
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(size),
                  y + Utilities.randomInt(size),
                  180 + Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.FALL,
                  ParticleModule.ParticleSet.BLACK,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(size),
                  y + Utilities.randomInt(size),
                  360 - Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.FALL,
                  ParticleModule.ParticleSet.BLACK,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(size),
                  y + Utilities.randomInt(size),
                  180 + Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.BLOOD_RED,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(size),
                  y + Utilities.randomInt(size),
                  360 - Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.BLOOD_RED,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(size), y + Utilities.randomInt(size), ParticleModule.ParticleGroup.MAGIC_FIRE, ParticleModule.ParticleSet.BLACK, true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(size),
                  y + Utilities.randomInt(size),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.BLOOD_RED,
                  true
               );
         }

         this.particle
            .newParticleGroup(
               x + Utilities.randomInt(size), y + Utilities.randomInt(size), ParticleModule.ParticleGroup.MAGIC_FIRE, ParticleModule.ParticleSet.BLACK, true
            );
         this.particle
            .newParticleGroup(x + 5 + Utilities.randomInt(size - 10), y + 5, ParticleModule.ParticleGroup.MAGIC_FIRE, ParticleModule.ParticleSet.BLACK, true);
      } else if (this.thisGoal.isLocked() && requiredGoals > 0) {
         if (this.buttonIconImageLocked != null) {
            this.buttonIconImageLocked.drawCentered(centerX, centerY);
         }

         g.setColor(new Color(0, 0, 0, 150));
         g.fillRect(x + 4, y + 4, this.buttonFrameRed.getWidth() - 8, this.buttonFrameRed.getHeight() - 8);
         this.buttonFrameRed.draw(x, y);
         int size = this.buttonFrameRed.getWidth();
         if (Utilities.randomInt(5) == 0) {
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(size),
                  y + Utilities.randomInt(size),
                  180 + Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.FALL,
                  ParticleModule.ParticleSet.BLACK,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(size),
                  y + Utilities.randomInt(size),
                  360 - Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.FALL,
                  ParticleModule.ParticleSet.BLACK,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(size),
                  y + Utilities.randomInt(size),
                  180 + Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.BLOOD_RED,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(size),
                  y + Utilities.randomInt(size),
                  360 - Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.BLOOD_RED,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(size), y + Utilities.randomInt(size), ParticleModule.ParticleGroup.MAGIC_FIRE, ParticleModule.ParticleSet.BLACK, true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(size),
                  y + Utilities.randomInt(size),
                  ParticleModule.ParticleGroup.MAGIC_FIRE,
                  ParticleModule.ParticleSet.BLOOD_RED,
                  true
               );
         }

         this.particle
            .newParticleGroup(
               x + 5 + Utilities.randomInt(size - 10), y + 5, ParticleModule.ParticleGroup.MAGIC_FIRE, ParticleModule.ParticleSet.BLOOD_RED, true
            );
         if (requiredGoals > 0) {
            int goalCount = this.thisGoal.getGoalCountRequired() - this.goal.getCompletedGoalsCount();
            this.font
               .drawString(
                  x + this.buttonFrameRed.getWidth() / 2, y + this.buttonFrameRed.getHeight() / 2 - 12, "$RED1" + goalCount, Text.FontType.BODY, 3, true
               );
         }
      } else if (this.thisGoal.isClaimed()) {
         if (this.buttonIconImageUnlocked != null) {
            this.buttonIconImageUnlocked.drawCentered(centerX, centerY);
         }

         this.buttonFrameGreen.draw(x, y);
         if (this.intersects(mouse)) {
            GL11.glBlendFunc(774, 770);
            this.buttonFrameSilver.drawFlash(x, y, this.buttonFrameSilver.getWidth(), this.buttonFrameSilver.getHeight(), Color.white);
            g.setDrawMode(Graphics.MODE_NORMAL);
         }

         if (Utilities.randomInt(30) == 0) {
            int sizex = this.buttonFrameBlack.getWidth();
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(sizex),
                  y + Utilities.randomInt(sizex),
                  180 + Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.FALL,
                  ParticleModule.ParticleSet.WHITE,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(sizex),
                  y + Utilities.randomInt(sizex),
                  360 - Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.FALL,
                  ParticleModule.ParticleSet.WHITE,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(sizex),
                  y + Utilities.randomInt(sizex),
                  180 + Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.FIRE_GREEN,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(sizex),
                  y + Utilities.randomInt(sizex),
                  360 - Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.FIRE_GREEN,
                  true
               );
         }
      } else if (this.thisGoal.isCompleted()) {
         this.renderEffects(g, x, y);
         if (this.buttonIconImageUnlocked != null) {
            this.buttonIconImageUnlocked.drawCentered(centerX, centerY);
         }

         this.buttonFrameSilver.draw(x, y);
         if (this.intersects(mouse)) {
            GL11.glBlendFunc(774, 770);
            this.buttonFrameSilver.drawFlash(x, y, this.buttonFrameSilver.getWidth(), this.buttonFrameSilver.getHeight(), Color.white);
            g.setDrawMode(Graphics.MODE_NORMAL);
         }

         if (Utilities.randomInt(5) == 0) {
            int sizex = this.buttonFrameBlack.getWidth();
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(sizex),
                  y + Utilities.randomInt(sizex),
                  180 + Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.FALL,
                  ParticleModule.ParticleSet.WHITE,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(sizex),
                  y + Utilities.randomInt(sizex),
                  360 - Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.FALL,
                  ParticleModule.ParticleSet.WHITE,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(sizex),
                  y + Utilities.randomInt(sizex),
                  180 + Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(sizex),
                  y + Utilities.randomInt(sizex),
                  360 - Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
         }

         if (this.rotationalPositionH < 37 && this.rotationalPositionV == 0) {
            this.rotationalPositionH++;
         } else if (this.rotationalPositionH == 37 && this.rotationalPositionV < 37) {
            this.rotationalPositionV++;
         } else if (this.rotationalPositionH > 0 && this.rotationalPositionV == 37) {
            this.rotationalPositionH--;
         } else if (this.rotationalPositionH == 0 && this.rotationalPositionV > 0) {
            this.rotationalPositionV--;
         }

         this.particle
            .newParticleGroup(
               x + this.rotationalPositionH + 4 + Utilities.randomInt(-2, 2),
               y + this.rotationalPositionV + 4 + Utilities.randomInt(-2, 2),
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.WHITE,
               true
            );
         this.particle
            .newParticleGroup(
               x + this.rotationalPositionH + 4 + Utilities.randomInt(-1, 1),
               y + this.rotationalPositionV + 4 + Utilities.randomInt(-1, 1),
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_YELLOW,
               true
            );
      } else if (this.thisGoal.isFreeChest() && !this.thisGoal.isLocked()) {
         this.renderEffects(g, x, y);
         if (this.buttonIconImageUnlocked != null) {
            this.buttonIconImageUnlocked.drawCentered(centerX, centerY);
         }

         this.buttonFrameBlue.draw(x, y);
         if (this.intersects(mouse)) {
            GL11.glBlendFunc(774, 770);
            this.buttonFrameBlue.drawFlash(x, y, this.buttonFrameBlue.getWidth(), this.buttonFrameBlue.getHeight(), Color.white);
            g.setDrawMode(Graphics.MODE_NORMAL);
         }

         if (Utilities.randomInt(5) == 0) {
            int sizex = this.buttonFrameBlack.getWidth();
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(sizex),
                  y + Utilities.randomInt(sizex),
                  180 + Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.FALL,
                  ParticleModule.ParticleSet.WHITE,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(sizex),
                  y + Utilities.randomInt(sizex),
                  360 - Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.FALL,
                  ParticleModule.ParticleSet.WHITE,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(sizex),
                  y + Utilities.randomInt(sizex),
                  180 + Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
            this.particle
               .newParticleGroup(
                  x + Utilities.randomInt(sizex),
                  y + Utilities.randomInt(sizex),
                  360 - Utilities.randomInt(24),
                  ParticleModule.ParticleGroup.MAGIC_SMOKE,
                  ParticleModule.ParticleSet.FIRE_BLUE,
                  true
               );
         }

         if (this.rotationalPositionH < 37 && this.rotationalPositionV == 0) {
            this.rotationalPositionH++;
         } else if (this.rotationalPositionH == 37 && this.rotationalPositionV < 37) {
            this.rotationalPositionV++;
         } else if (this.rotationalPositionH > 0 && this.rotationalPositionV == 37) {
            this.rotationalPositionH--;
         } else if (this.rotationalPositionH == 0 && this.rotationalPositionV > 0) {
            this.rotationalPositionV--;
         }

         this.particle
            .newParticleGroup(
               x + this.rotationalPositionH + 4 + Utilities.randomInt(-2, 2),
               y + this.rotationalPositionV + 4 + Utilities.randomInt(-2, 2),
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.WHITE,
               true
            );
         this.particle
            .newParticleGroup(
               x + this.rotationalPositionH + 4 + Utilities.randomInt(-1, 1),
               y + this.rotationalPositionV + 4 + Utilities.randomInt(-1, 1),
               ParticleModule.ParticleGroup.MAGIC_FIRE,
               ParticleModule.ParticleSet.MAGIC_BLUE,
               true
            );
      } else if (!this.thisGoal.isLocked()) {
         if (this.buttonIconImageUnlocked != null) {
            this.buttonIconImageUnlocked.drawCentered(centerX, centerY);
         }

         this.buttonFrameBrown.draw(x, y);
         if (this.intersects(mouse)) {
            GL11.glBlendFunc(774, 770);
            this.buttonFrameBrown.drawFlash(x, y, this.buttonFrameBrown.getWidth(), this.buttonFrameBrown.getHeight(), Color.white);
            g.setDrawMode(Graphics.MODE_NORMAL);
         }
      }

      if (this.playEffects) {
         switch (this.effectsStep) {
            case 0:
               for (int i = 0; i < 250; i++) {
                  int sizex = this.buttonFrameBlack.getWidth();
                  if (Utilities.randomInt(5) == 0) {
                     this.particle
                        .newParticleGroup(
                           x + Utilities.randomInt(sizex),
                           y + Utilities.randomInt(sizex),
                           180 + Utilities.randomInt(24),
                           ParticleModule.ParticleGroup.FALL,
                           ParticleModule.ParticleSet.WHITE,
                           true
                        );
                     this.particle
                        .newParticleGroup(
                           x + Utilities.randomInt(sizex),
                           y + Utilities.randomInt(sizex),
                           360 - Utilities.randomInt(24),
                           ParticleModule.ParticleGroup.FALL,
                           ParticleModule.ParticleSet.WHITE,
                           true
                        );
                  }

                  this.particle
                     .newParticleGroup(
                        x + Utilities.randomInt(sizex),
                        y + Utilities.randomInt(sizex),
                        180 + Utilities.randomInt(24),
                        ParticleModule.ParticleGroup.MAGIC_SMOKE,
                        ParticleModule.ParticleSet.FIRE_BLUE,
                        true
                     );
                  this.particle
                     .newParticleGroup(
                        x + Utilities.randomInt(sizex),
                        y + Utilities.randomInt(sizex),
                        360 - Utilities.randomInt(24),
                        ParticleModule.ParticleGroup.MAGIC_SMOKE,
                        ParticleModule.ParticleSet.FIRE_BLUE,
                        true
                     );
               }

               for (int i = 0; i < 10; i++) {
                  this.projectile
                     .newExpandingEnergyRing(
                        x + this.buttonFrameSilver.getWidth() / 2,
                        y + this.buttonFrameSilver.getHeight() / 2,
                        128,
                        12 - i,
                        ParticleModule.ParticleSet.MAGIC_GREEN,
                        ParticleModule.ParticleSet.MAGIC_BLUE,
                        true
                     );
               }

               this.effectsStep++;
               break;
            case 1:
               if (this.alphaFlip) {
                  this.textAlpha -= 10.0F;
               } else {
                  this.textAlpha += 30.0F;
               }

               this.textFloatY -= 0.2F;
               this.textFloatX += 0.1F;
               this.font
                  .drawString(
                     x + 15 + this.textFloatX, y + this.textFloatY, "+" + this.thisGoal.getGodXPAmount(), (int)this.textAlpha, Text.FontType.HEADER, 2, true
                  );
               if (this.textAlpha > 500.0F) {
                  this.alphaFlip = true;
               }

               if (this.textAlpha < 0.0F) {
                  this.effectsStep++;
               }
               break;
            case 2:
               this.playEffects = false;
         }
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   public void renderBar(Graphics g, int x, int y, Rectangle mouse, boolean debug) throws SlickException {
      x += this.thisGoal.getGoalPositionX() * 30;
      y += this.thisGoal.getGoalPositionY() * 60;
      int goalAmount = this.thisGoal.getGoalAmount();
      int goalAmountRequired = this.thisGoal.getGoalAmountRequired();
      int barLength = (int)Utilities.getPercentage(goalAmount, goalAmountRequired, 20.0);
      if (barLength > 20) {
         barLength = 20;
      }

      if (!this.thisGoal.isLocked() && this.thisGoal.isFreeChest()) {
         barLength = 20;
      }

      Color barColor;
      if (goalAmount >= goalAmountRequired || this.thisGoal.isFreeChest()) {
         barColor = Color.white;
      } else if (goalAmount >= goalAmountRequired / 2) {
         barColor = Color.yellow;
      } else {
         barColor = Color.red;
      }

      if (this.thisGoal.getGoalPosition() == Goal.GoalPosition.DOWN) {
         g.setColor(Color.black);
         g.fillRect(x + 21, y - 17, 4.0F, 20.0F);
         g.setColor(barColor);
         g.fillRect(x + 21, y - 17, 4.0F, barLength);
         if (goalAmount < goalAmountRequired) {
            this.goalProgressBarV.draw(x + 11, y - 17);
         } else {
            this.goalProgressBarCompletedV.draw(x + 11, y - 17);
         }
      }

      if (this.thisGoal.getGoalPosition() == Goal.GoalPosition.LEFT) {
         g.setColor(Color.black);
         g.fillRect(x + 63, y + 21, -20.0F, 4.0F);
         g.setColor(barColor);
         g.fillRect(x + 63, y + 21, -barLength, 4.0F);
         if (goalAmount < goalAmountRequired) {
            this.goalProgressBarH.draw(x + 43, y + 11);
         } else {
            this.goalProgressBarCompletedH.draw(x + 43, y + 11);
         }
      }

      if (this.thisGoal.getGoalPosition() == Goal.GoalPosition.RIGHT) {
         g.setColor(Color.black);
         g.fillRect(x - 17, y + 21, 20.0F, 4.0F);
         g.setColor(barColor);
         g.fillRect(x - 17, y + 21, barLength, 4.0F);
         if (goalAmount < goalAmountRequired) {
            this.goalProgressBarH.draw(x - 17, y + 11);
         } else {
            this.goalProgressBarCompletedH.draw(x - 17, y + 11);
         }
      }

      barLength = (int)Utilities.getPercentage(goalAmount, goalAmountRequired, 24.0);
      if (barLength > 24) {
         barLength = 24;
      }

      if (!this.thisGoal.isLocked() && this.thisGoal.isFreeChest()) {
         barLength = 24;
      }

      if (this.thisGoal.getGoalPosition() == Goal.GoalPosition.DOWN_LEFT) {
         g.rotate(x + 42.5F, y - 19, 26.0F);
         g.setColor(Color.black);
         g.fillRect(x + 42.5F, y - 19, 5.0F, 24.0F);
         g.setColor(barColor);
         g.fillRect(x + 42.5F, y - 19, 5.0F, barLength);
         g.rotate(x + 42.5F, y - 19, -26.0F);
         if (goalAmount < goalAmountRequired) {
            this.goalProgressBarDR.draw(x + 22, y - 17);
         } else {
            this.goalProgressBarCompletedDR.draw(x + 22, y - 17);
         }
      }

      if (this.thisGoal.getGoalPosition() == Goal.GoalPosition.DOWN_RIGHT) {
         g.rotate(x, y - 17, -26.0F);
         g.setColor(Color.black);
         g.fillRect(x, y - 17, 5.0F, 24.0F);
         g.setColor(barColor);
         g.fillRect(x, y - 17, 5.0F, barLength);
         g.rotate(x, y - 17, 26.0F);
         if (goalAmount < goalAmountRequired) {
            this.goalProgressBarDL.draw(x - 9, y - 17);
         } else {
            this.goalProgressBarCompletedDL.draw(x - 9, y - 17);
         }
      }
   }

   public void renderBackground(Graphics g, int x, int y, Rectangle mouse, boolean debug) throws SlickException {
      x += this.thisGoal.getGoalPositionX() * 30;
      y += this.thisGoal.getGoalPositionY() * 60;
      int centerX = x + this.buttonFrameBlack.getWidth() / 2;
      int centerY = y + this.buttonFrameBlack.getHeight() / 2;
      this.goalButtonBackdrop.drawCentered(centerX, centerY);
   }

   public void renderEffects(Graphics g, int x, int y) {
      if (this.backgroundAlphaBlack < 200) {
         this.backgroundAlphaBlack += 2;
      }

      float finalRotation = this.rotation;
      Color primaryColor = new Color(155, 255, 255, 2);
      Color secondaryColor = new Color(55, 55, 255, 3);
      if (this.thisGoal.isFreeChest()) {
         primaryColor = new Color(55, 155, 255, 5);
         secondaryColor = new Color(0, 25, 255, 6);
      }

      for (int d = 0; d < 2; d++) {
         if (d == 1) {
            finalRotation = -finalRotation * 1.5F;
         }

         for (int c = 0; c < 15; c++) {
            float length = c;
            if (d == 1) {
               length *= 10.0F;
            }

            if (d == 1) {
               g.setColor(primaryColor);
            } else {
               g.setColor(secondaryColor);
            }

            for (int i = 0; i < 9; i++) {
               g.fillArc(
                  x - length / 2.0F + this.buttonFrameSilver.getWidth() / 2 - 5.0F,
                  y - length / 2.0F + this.buttonFrameSilver.getHeight() / 2,
                  length,
                  length,
                  0.0F + finalRotation + i * 40,
                  20.0F + finalRotation + i * 40
               );
               g.fillArc(
                  x - length / 2.0F + this.buttonFrameSilver.getWidth() / 2 + 5.0F,
                  y - length / 2.0F + this.buttonFrameSilver.getHeight() / 2,
                  length,
                  length,
                  0.0F + finalRotation + i * 40,
                  20.0F + finalRotation + i * 40
               );
            }
         }
      }

      this.rotation += 0.25F;
   }

   public void toggleEffects() {
      this.playEffects = true;
   }

   public int getHeight() {
      return this.buttonFrameBlue.getHeight();
   }

   public int getWidth() {
      return this.buttonFrameBlue.getWidth();
   }

   @Override
   public void renderDescriptionToolTip(Rectangle mouse, Graphics g) throws SlickException {
      if (mouse.intersects(this.intersectBox)) {
         if (this.toolTipButton != null) {
            this.toolTipButton.render(g, mouse, (int)(mouse.getX() + 24.0F), (int)(mouse.getY() + 24.0F), false);
         }
      }
   }
}
