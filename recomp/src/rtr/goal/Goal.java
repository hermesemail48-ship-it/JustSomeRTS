package rtr.goal;

import java.io.File;
import java.text.DecimalFormat;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.font.Text;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.Steamworks;

public class Goal {
   protected GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   protected static final int GOD_EXPERIENCE_BASE = 20;
   protected static final int GOD_EXPERIENCE_MULTIPLIER = 3;
   protected String systemName;
   protected String name;
   protected String description;
   protected GoalModule.GoalType goalType;
   protected Image goalIconImageUnlocked;
   protected Image goalIconImageLocked;
   protected GoalModule.GoalType requiredGoalType;
   protected Goal requiredGoal;
   protected Goal.GoalPosition goalPosition;
   protected int goalPositionX = 0;
   protected int goalPositionY = 0;
   private boolean completed;
   private boolean completedPreviously;
   private boolean freeChest;
   protected boolean claimed;
   protected boolean negativeGoal;
   protected int goalAmount;
   protected int goalCountRequired;
   protected int goalAmountRequired;
   protected int godXPAmount;

   public String getSystemName() {
      return this.systemName;
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public GoalModule.GoalType getGoalType() {
      return this.goalType;
   }

   public Image getGoalIconImageUnlocked() {
      return this.goalIconImageUnlocked;
   }

   public Image getGoalIconImageLocked() {
      return this.goalIconImageLocked;
   }

   public Goal.GoalPosition getGoalPosition() {
      return this.goalPosition;
   }

   public int getGoalPositionX() {
      return this.goalPositionX;
   }

   public int getGoalPositionY() {
      return this.goalPositionY;
   }

   public boolean isCompleted() {
      return this.completed;
   }

   public boolean isCompletedPreviously() {
      return this.completedPreviously;
   }

   public boolean isFreeChest() {
      return this.freeChest;
   }

   public boolean isClaimed() {
      return this.claimed;
   }

   public void setClaimed() {
      this.claimed = true;
   }

   public boolean isNegativeGoal() {
      return this.negativeGoal;
   }

   public Goal(GoalModule.GoalType goalType, GoalModule.GoalType requiredGoalType, Goal.GoalPosition position, int requiredAmount) throws SlickException {
      this.finalizeGoal(goalType, requiredGoalType, position, 0, 0, 0, requiredAmount, false);
   }

   public Goal(GoalModule.GoalType goalType, int absoluteX, int absoluteY, int goalCountRequired, int requiredAmount) throws SlickException {
      this.finalizeGoal(goalType, null, null, absoluteX, absoluteY, goalCountRequired, requiredAmount, false);
   }

   public Goal(GoalModule.GoalType goalType, GoalModule.GoalType requiredGoalType, Goal.GoalPosition position, int requiredAmount, boolean negativeGoal) throws SlickException {
      this.finalizeGoal(goalType, requiredGoalType, position, 0, 0, 0, requiredAmount, negativeGoal);
   }

   public Goal(GoalModule.GoalType goalType, int absoluteX, int absoluteY, int goalCountRequired, int requiredAmount, boolean negativeGoal) throws SlickException {
      this.finalizeGoal(goalType, null, null, absoluteX, absoluteY, goalCountRequired, requiredAmount, negativeGoal);
   }

   public void finalizeGoal(
      GoalModule.GoalType goalType,
      GoalModule.GoalType requiredGoalType,
      Goal.GoalPosition goalPosition,
      int absoluteX,
      int absoluteY,
      int goalCountRequired,
      int goalAmountRequired,
      boolean negativeGoal
   ) throws SlickException {
      this.goalType = goalType;
      this.requiredGoalType = requiredGoalType;
      if (requiredGoalType != null) {
         this.requiredGoal = this.goal.getAllGoals().get(requiredGoalType);
      }

      this.goalPosition = goalPosition;
      this.goalPositionX = absoluteX;
      this.goalPositionY = absoluteY;
      this.goalCountRequired = goalCountRequired;
      this.goalAmountRequired = goalAmountRequired;
      this.negativeGoal = negativeGoal;
      this.systemName = goalType.toString().toLowerCase();

      for (int i = 0; i < this.systemName.length(); i++) {
         if (this.systemName.charAt(i) == '_') {
            String upperCase = this.systemName.substring(i + 1, i + 2).toUpperCase();
            this.systemName = this.systemName.substring(0, i) + upperCase + this.systemName.substring(i + 2);
         }
      }

      this.systemName = this.systemName.replaceAll("_", "");
      if (this.systemName.contains("freeChest")) {
         this.godXPAmount = 1024;
      } else if (this.requiredGoal == null) {
         this.godXPAmount = (20 + 20 * goalCountRequired) * 3;
      } else {
         int totalXPTally = 60;

         for (Goal nextGoal = this.requiredGoal; nextGoal != null; nextGoal = nextGoal.getRequiredGoal()) {
            totalXPTally += 60;
            if (nextGoal.getRequiredGoal() == null) {
               totalXPTally += 20 * nextGoal.getGoalCountRequired() * 3;
               break;
            }
         }

         this.godXPAmount = totalXPTally;
      }

      File imageCheckUnlocked = new File("res/GUI/goals/" + this.systemName + ".png");
      if (imageCheckUnlocked.exists()) {
         this.goalIconImageUnlocked = ImageLoader.getImage("res/GUI/goals/" + this.systemName + ".png");
      } else {
         this.goalIconImageUnlocked = ImageLoader.getImage("res/GUI/goals/noImage.png");
      }

      File imageCheckLocked = new File("res/GUI/goals/locked/" + this.systemName + ".png");
      if (imageCheckLocked.exists()) {
         this.goalIconImageLocked = ImageLoader.getImage("res/GUI/goals/locked/" + this.systemName + ".png");
      } else {
         this.goalIconImageLocked = ImageLoader.getImage("res/GUI/goals/locked/noImage.png");
      }

      DecimalFormat df = new DecimalFormat("#,###");
      Text.setVariableText("amount", df.format(goalAmountRequired));
      if (this.systemName.contains("freeChest")) {
         this.name = Text.getText("goal.freeChest.name");
         this.description = Text.getText("goal.freeChest.description");
         this.freeChest = true;
      } else {
         this.name = Text.getText("goal." + this.systemName + ".name");
         this.description = Text.getText("goal." + this.systemName + ".description");
      }

      if (this.requiredGoal != null) {
         int prevGoalX = this.requiredGoal.getGoalPositionX();
         int prevGoalY = this.requiredGoal.getGoalPositionY();
         switch (goalPosition) {
            case LEFT:
               this.goalPositionX = prevGoalX - 2;
               this.goalPositionY = prevGoalY;
               break;
            case RIGHT:
               this.goalPositionX = prevGoalX + 2;
               this.goalPositionY = prevGoalY;
               break;
            case DOWN:
               this.goalPositionX = prevGoalX;
               this.goalPositionY = prevGoalY + 1;
               break;
            case DOWN_LEFT:
               this.goalPositionX = prevGoalX - 1;
               this.goalPositionY = prevGoalY + 1;
               break;
            case DOWN_RIGHT:
               this.goalPositionX = prevGoalX + 1;
               this.goalPositionY = prevGoalY + 1;
         }
      }
   }

   public int getGoalAmount() {
      return this.goalAmount;
   }

   public int getGoalCountRequired() {
      return this.goalCountRequired;
   }

   public int getGoalAmountRequired() {
      return this.goalAmountRequired;
   }

   public int getGodXPAmount() {
      return this.godXPAmount;
   }

   public void incrementGoal(int amount) {
      this.goalAmount += amount;
      if (this.goalAmount > this.goalAmountRequired) {
         this.goalAmount = this.goalAmountRequired;
      }

      if (!this.completed && this.goalAmount >= this.goalAmountRequired) {
         this.completed = true;
         if (Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.SANDBOX) {
            Steamworks.grantAchievement(this.systemName, false);
         }
      }
   }

   public boolean isLocked() {
      return this.requiredGoal == null ? this.goal.getCompletedGoalsCount() < this.goalCountRequired : !this.requiredGoal.isCompleted();
   }

   public GoalModule.GoalType getRequiredGoalType() {
      return this.requiredGoalType;
   }

   public Goal getRequiredGoal() {
      return this.requiredGoal;
   }

   public void load(int goalAmount, boolean claimed) {
      this.goalAmount = goalAmount;
      this.claimed = claimed;
      if (!this.completed && goalAmount >= this.goalAmountRequired) {
         this.completed = true;
         this.completedPreviously = true;
         if (Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.SANDBOX) {
            Steamworks.grantAchievement(this.systemName, true);
         }
      }
   }

   public static enum GoalPosition {
      LEFT,
      RIGHT,
      DOWN,
      DOWN_LEFT,
      DOWN_RIGHT;
   }
}
