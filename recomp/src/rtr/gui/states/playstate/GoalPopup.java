package rtr.gui.states.playstate;

import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.goal.Goal;
import rtr.goal.GoalModule;
import rtr.gui.buttons.GUIButtonGenericWood;
import rtr.gui.buttons.GUIButtonGoalStats;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.system.ScaleControl;

public class GoalPopup extends PlayStateGUIPanelBase {
   private HashMap<GoalModule.GoalType, GUIButtonGoalStats> goalsButtons;
   private ArrayList<GoalModule.GoalType> displayList = new ArrayList<>();
   private ArrayList<GoalModule.GoalType> displayListCompleted = new ArrayList<>();
   private ArrayList<GoalModule.GoalType> displayListUnlocked = new ArrayList<>();
   private HashMap<GoalModule.GoalType, Goal> completedGoals;
   private HashMap<GoalModule.GoalType, Goal> unlockedGoals;
   private GUIButtonGenericWood completedGoalBanner;
   private GUIButtonGenericWood claimOnGoalsPanel;
   private GUIButtonGenericWood unlockedGoalBanner;
   private boolean displayedGoalFlip = false;
   private float displayedGoalAlignY = 0.0F;
   private float completedGoalSpeed = 4.0F;
   private float speedAdjust;
   private int displayedGoalIdleTimerTick = 0;
   private int completedGoalIdleTimer = 90;
   private boolean whooshPlayed = false;

   public GoalPopup(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.goalsButtons = new HashMap<>();
      this.completedGoals = new HashMap<>();
      this.unlockedGoals = new HashMap<>();
      HashMap<GoalModule.GoalType, Goal> allGoals = this.goal.getAllGoals();

      for (Goal g1 : allGoals.values()) {
         if (g1.isClaimed() && g1.isCompleted()) {
            this.completedGoals.put(g1.getGoalType(), g1);
         } else if (!g1.isLocked() && !g1.isCompleted()) {
            this.unlockedGoals.put(g1.getGoalType(), g1);
         }
      }

      this.completedGoalBanner = new GUIButtonGenericWood("completedGoalBanner", Text.getText("playConsolePanel.button.completedGoalBanner"));
      this.guiButtons.add(this.completedGoalBanner);
      this.claimOnGoalsPanel = new GUIButtonGenericWood("claimOnGoalsPanel", Text.getText("playConsolePanel.button.claimOnGoalsPanel"));
      this.guiButtons.add(this.claimOnGoalsPanel);
      this.unlockedGoalBanner = new GUIButtonGenericWood("unlockedGoalBanner", Text.getText("playConsolePanel.button.unlockedGoalBanner"));
      this.guiButtons.add(this.unlockedGoalBanner);
   }

   public void render(float y, boolean debug) throws SlickException {
      this.displayListCompleted.clear();
      this.displayListUnlocked.clear();
      HashMap<GoalModule.GoalType, Goal> allGoals = this.goal.getAllGoals();

      for (Goal thisGoal : allGoals.values()) {
         GoalModule.GoalType thisGoalType = thisGoal.getGoalType();
         if (!thisGoal.isClaimed() && thisGoal.isCompleted() && !this.completedGoals.containsKey(thisGoalType)) {
            this.displayListCompleted.add(thisGoalType);
            this.completedGoals.put(thisGoalType, thisGoal);
            Text.setVariableText("goalName", thisGoal.getName());
            Console.out(Text.getText("playConsolePanel.completedGoal"), false);
         } else if (!thisGoal.isLocked() && !thisGoal.isCompleted() && !this.unlockedGoals.containsKey(thisGoalType)) {
            this.displayListUnlocked.add(thisGoalType);
            this.unlockedGoals.put(thisGoalType, thisGoal);
            Text.setVariableText("goalName", thisGoal.getName());
            Console.out(Text.getText("playConsolePanel.newGoalUnlocked"), false);
         }
      }

      this.displayList.addAll(this.displayListCompleted);
      this.displayList.addAll(this.displayListUnlocked);
      if (this.displayList.size() > 0) {
         GoalModule.GoalType gT = this.displayList.get(0);
         if (!this.goalsButtons.containsKey(gT)) {
            GUIButtonGoalStats gBS = new GUIButtonGoalStats("activeGoalButton", this.goal.getAllGoals().get(gT));
            this.goalsButtons.put(gT, gBS);
            this.guiButtons.add(gBS);
         }

         GUIButtonGoalStats gBS = this.goalsButtons.get(gT);
         boolean isCompleted = gBS.getGoal().isCompleted();
         boolean isCompletedPeviously = gBS.getGoal().isCompletedPreviously();
         switch ((int)this.displayedGoalAlignY) {
            case -120:
               this.speedAdjust = -3.75F;
               break;
            case -110:
               this.speedAdjust = -2.75F;
               break;
            case -100:
               this.speedAdjust = -2.5F;
               break;
            case -90:
               this.speedAdjust = -2.25F;
               break;
            case -80:
               this.speedAdjust = -1.75F;
               break;
            case -70:
               this.speedAdjust = -1.5F;
               break;
            case -60:
               this.speedAdjust = -1.25F;
         }

         if (!this.displayedGoalFlip) {
            if (this.displayedGoalAlignY == 0.0F) {
               this.sound.playSound(SoundModule.SoundType.MEDIUM_WOOSH);
            }

            if (this.displayedGoalAlignY > -120.0F) {
               this.displayedGoalAlignY = this.displayedGoalAlignY - (this.completedGoalSpeed + this.speedAdjust);
            } else {
               if (isCompleted && !isCompletedPeviously) {
                  gBS.toggleParticles(ScaleControl.getInterfaceWidth() / 2 - gBS.getWidth() / 2, (int)(y + this.displayedGoalAlignY) + 50);
                  this.sound
                     .playSound(
                        this.goal.getAllGoals().get(gT).isNegativeGoal() ? SoundModule.SoundType.COMPLETE_GOAL_NEGATIVE : SoundModule.SoundType.COMPLETE_GOAL
                     );
                  this.sound.playSound(SoundModule.SoundType.LARGE_WOOSH);
                  this.sound.playSound(SoundModule.SoundType.MAGIC_SPARKLES);
               }

               this.displayedGoalFlip = true;
            }
         }

         if (this.displayedGoalFlip) {
            this.displayedGoalIdleTimerTick++;
         }

         if (isCompleted && this.displayedGoalIdleTimerTick >= this.completedGoalIdleTimer * 3
            || !isCompleted && this.displayedGoalIdleTimerTick >= this.completedGoalIdleTimer) {
            if (this.displayedGoalAlignY < 0.0F) {
               if (!this.whooshPlayed && (int)this.displayedGoalAlignY == -110) {
                  if (isCompleted) {
                     gBS.toggleEffectsFadeout();
                  }

                  this.sound.playSound(SoundModule.SoundType.MEDIUM_WOOSH);
                  this.whooshPlayed = true;
               }

               this.displayedGoalAlignY = this.displayedGoalAlignY + (this.completedGoalSpeed + this.speedAdjust);
            } else {
               this.whooshPlayed = false;
               this.displayedGoalFlip = false;
               this.displayedGoalIdleTimerTick = 0;
               this.displayList.remove(0);
            }
         }

         gBS.renderEffects(this.g, ScaleControl.getInterfaceWidth() / 2 - gBS.getWidth() / 2, (int)(y + this.displayedGoalAlignY) + 50);
         gBS.render(this.g, this.mouse, ScaleControl.getInterfaceWidth() / 2 - gBS.getWidth() / 2, (int)(y + this.displayedGoalAlignY) + 50, debug);
         if (isCompletedPeviously) {
            this.claimOnGoalsPanel
               .render(this.g, this.mouse, ScaleControl.getInterfaceWidth() / 2, (int)(y + this.displayedGoalAlignY * 1.1F) + 34, false, true, debug);
         } else if (isCompleted) {
            this.completedGoalBanner
               .render(this.g, this.mouse, ScaleControl.getInterfaceWidth() / 2, (int)(y + this.displayedGoalAlignY * 1.1F) + 34, false, true, debug);
         } else {
            this.unlockedGoalBanner
               .render(this.g, this.mouse, ScaleControl.getInterfaceWidth() / 2, (int)(y + this.displayedGoalAlignY * 1.1F) + 34, false, true, debug);
         }
      }
   }
}
