package rtr.gui.states.shared;

import java.util.HashMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.font.Text;
import rtr.goal.Goal;
import rtr.goal.GoalModule;
import rtr.gui.buttons.GUIButtonGoal;
import rtr.gui.buttons.GUIButtonGoalStats;
import rtr.gui.buttons.GUIButtonGoalsPanelSlider;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.SharedGUIPanelBase;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.states.StateBase;
import rtr.system.ScaleControl;
import rtr.utilities.Utilities;

public class GoalsPanel extends SharedGUIPanelBase {
   private GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   private Rectangle mask;
   private Image panel;
   private HashMap<Goal, GUIButtonGoalStats> goalStatsButtons;
   private HashMap<Goal, GUIButtonGoal> goalButtons;
   private GUIButtonGoalsPanelSlider slider;
   private int goalSheetTotalHeight;

   public GoalsPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.initialize();
   }

   public GoalsPanel(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.initialize();
   }

   private void initialize() throws SlickException {
      this.panel = ImageLoader.getImage("res/GUI/gameState/goalsPanel.png");
      this.goalButtons = new HashMap<>();
      this.goalStatsButtons = new HashMap<>();
      HashMap<GoalModule.GoalType, Goal> allGoals = this.goal.getAllGoals();
      int highestPoint = 0;

      for (Goal gL : allGoals.values()) {
         GUIButtonGoalStats gBS = new GUIButtonGoalStats("activeGoalButton", gL);
         this.goalStatsButtons.put(gL, gBS);
         this.guiButtons.add(gBS);
         GUIButtonGoal gB = new GUIButtonGoal(gL, gBS);
         this.goalButtons.put(gL, gB);
         this.guiButtons.add(gB);
         if (gL.getGoalPositionY() > highestPoint) {
            highestPoint = gL.getGoalPositionY();
         }
      }

      this.goalSheetTotalHeight = highestPoint * 60;
      this.slider = new GUIButtonGoalsPanelSlider("slider");
      this.guiButtons.add(this.slider);
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);
   }

   @Override
   public void update() throws SlickException {
   }

   public void render(boolean panelEnabled, boolean debug) throws SlickException {
      if (panelEnabled) {
         if (this.playStateController != null) {
            this.x = this.playStateController.getResourceAndInfluenceBar().getRightX() - this.panel.getWidth() + 170;
            if (this.x + this.panel.getWidth() > this.playStateController.getRightPanel().getLeftX() - 46) {
               this.x = this.x - Math.abs(this.x + this.panel.getWidth() - this.playStateController.getRightPanel().getLeftX() + 46);
            }

            this.y = 159;
         }

         if (this.worldMapController != null) {
            this.x = ScaleControl.getInterfaceCenterX() - this.panel.getWidth() / 2 + 9;
            this.y = 109;
         }

         this.panel.draw(this.x, this.y);
         this.font.drawString(this.x + 135, this.y, Text.getText("sharedGoalsPanel.goalsWeb"), Text.FontType.HEADER, 3, true);
         this.renderGoalSheet(debug);
         this.slider.render(this.g, this.mouse, this.x + this.panel.getWidth() - 40, this.y + 23, debug);
         this.mask.setBounds(this.x, this.y, this.panel.getWidth(), this.panel.getHeight());
      }
   }

   private void renderGoalSheet(boolean debug) throws SlickException {
      int adjustedPosition = (int)Utilities.getPercentage(this.slider.getSliderPosition(), this.slider.getSliderLength(), this.goalSheetTotalHeight - 300);
      int goalXStart = this.x + this.panel.getWidth() / 2 - 54;
      int goalYStart = this.y + 36 - adjustedPosition;
      this.g.setWorldClip(this.x, this.y + 28, this.panel.getWidth(), this.panel.getHeight() - 45);
      if (debug) {
         for (int i = 0; i < 100; i++) {
            this.font.drawString(this.x + 20, goalYStart + 5 + i * 60, "$BLA1" + (i + 1), Text.FontType.HEADER, 5);
         }
      }

      for (GUIButtonGoal b : this.goalButtons.values()) {
         if (goalYStart + b.getGoalPositionAdjustedY() > this.y - 20
            && goalYStart + b.getGoalPositionAdjustedY() < this.y + this.panel.getHeight() + b.getHeight() - 70) {
            b.renderBackground(this.g, goalXStart, goalYStart, this.mouse, debug);
         }
      }

      for (GUIButtonGoal bx : this.goalButtons.values()) {
         if (goalYStart + bx.getGoalPositionAdjustedY() > this.y - 20
            && goalYStart + bx.getGoalPositionAdjustedY() < this.y + this.panel.getHeight() + bx.getHeight() - 40) {
            bx.renderBar(this.g, goalXStart, goalYStart, this.mouse, debug);
         }
      }

      for (GUIButtonGoal bxx : this.goalButtons.values()) {
         if (goalYStart + bxx.getGoalPositionAdjustedY() > this.y - 20
            && goalYStart + bxx.getGoalPositionAdjustedY() < this.y + this.panel.getHeight() + bxx.getHeight() - 60) {
            bxx.renderButton(this.g, goalXStart, goalYStart, this.mouse, debug);
         }
      }

      this.g.clearWorldClip();
   }

   public void lockSlider() {
      this.slider.lock();
   }

   public void moveUp() {
      this.slider.moveUp(25);
   }

   public void moveDown() {
      this.slider.moveDown(25);
   }

   @Override
   public int getLeftX() {
      return this.x;
   }

   @Override
   public int getTopY() {
      return this.y;
   }

   public void playEffects(Goal g) {
      this.goalButtons.get(g).toggleEffects();
   }
}
