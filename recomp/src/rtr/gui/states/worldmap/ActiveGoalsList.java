package rtr.gui.states.worldmap;

import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ModuleBase;
import rtr.font.Text;
import rtr.goal.Goal;
import rtr.goal.GoalModule;
import rtr.gui.buttons.GUIButtonGoalStats;
import rtr.gui.buttons.GUIButtonPage;
import rtr.gui.buttons.GUISquarePanelButton;
import rtr.gui.states.SharedGUIPanelBase;
import rtr.gui.states.WorldMapGUIController;
import rtr.gui.states.WorldMapGUIData;
import rtr.states.StateBase;
import rtr.system.ScaleControl;

public class ActiveGoalsList extends SharedGUIPanelBase {
   private GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   private Rectangle mask;
   private ArrayList<GUIButtonGoalStats> allGoalsButtons;
   private ArrayList<GUIButtonGoalStats> validGoalsButtons;
   private int goalPosition;
   private int goalButtonWidth = 0;
   private int goalButtonHeight = 0;
   private int maxGoals;
   private GUIButtonPage pageUp;
   private GUIButtonPage pageDown;
   private GUISquarePanelButton openGoalPanel;

   public ActiveGoalsList(WorldMapGUIController controller, WorldMapGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.allGoalsButtons = new ArrayList<>();
      this.validGoalsButtons = new ArrayList<>();
      HashMap<GoalModule.GoalType, Goal> allGoals = this.goal.getAllGoals();
      int highestPoint = 0;

      for (Goal gL : allGoals.values()) {
         GUIButtonGoalStats gBS = new GUIButtonGoalStats("activeGoalButton", gL);
         this.allGoalsButtons.add(gBS);
         this.guiButtons.add(gBS);
         this.goalButtonWidth = gBS.getWidth();
         this.goalButtonHeight = gBS.getHeight();
         if (gL.getGoalPositionY() > highestPoint) {
            highestPoint = gL.getGoalPositionY();
         }
      }

      this.pageUp = new GUIButtonPage("pageUp", "up");
      this.pageUp.setToolTip(Text.getText("worldMapActiveGoalsList.button.tooltip.pageUp"));
      this.guiButtons.add(this.pageUp);
      this.pageDown = new GUIButtonPage("pageDown", "down");
      this.pageDown.setToolTip(Text.getText("worldMapActiveGoalsList.button.tooltip.pageDown"));
      this.guiButtons.add(this.pageDown);
      this.openGoalPanel = new GUISquarePanelButton("openGoalPanel", 0, "goals");
      this.openGoalPanel
         .setToolTip(
            Text.getText("worldMapActiveGoalsList.button.tooltip.openGoalPanelHeader"), Text.getText("worldMapActiveGoalsList.button.tooltip.openGoalPanel")
         );
      this.guiButtons.add(this.openGoalPanel);
      this.mask = new Rectangle(0.0F, 0.0F, this.goalButtonWidth, ScaleControl.getInterfaceHeight());
      this.masks.add(this.mask);
   }

   @Override
   public void update() throws SlickException {
      this.maxGoals = ScaleControl.getInterfaceHeight() / this.goalButtonHeight - 2;
      this.validGoalsButtons.clear();

      for (int i = 0; i < this.allGoalsButtons.size(); i++) {
         GUIButtonGoalStats gB = this.allGoalsButtons.get(i);
         Goal thisGoal = gB.getGoal();
         if (!thisGoal.isLocked() && !thisGoal.isClaimed()) {
            this.validGoalsButtons.add(gB);
         }
      }
   }

   public void render(boolean debug) throws SlickException {
      this.mask.setBounds(0.0F, 0.0F, this.goalButtonWidth, ScaleControl.getInterfaceHeight());

      for (int pass = 0; pass < 2; pass++) {
         this.y = 45;

         for (int i = this.goalPosition - 3; i < this.goalPosition; i++) {
            if (i >= 0) {
               int offsetY = 0;
               if (i == this.goalPosition - 3) {
                  offsetY = this.y - this.goalButtonHeight / 6 * 3;
               } else if (i == this.goalPosition - 2) {
                  offsetY = this.y - this.goalButtonHeight / 6 * 2;
               } else if (i == this.goalPosition - 1) {
                  offsetY = this.y - this.goalButtonHeight / 6;
               }

               GUIButtonGoalStats gB = this.validGoalsButtons.get(i);
               if (pass == 0) {
                  gB.renderEffects(this.g, this.x, offsetY);
               } else {
                  gB.render(this.g, this.mouse, this.x, offsetY, debug);
               }
            }
         }

         for (int ix = this.goalPosition; ix < this.goalPosition + this.maxGoals && ix < this.validGoalsButtons.size(); ix++) {
            GUIButtonGoalStats gB = this.validGoalsButtons.get(ix);
            if (pass == 0) {
               gB.renderEffects(this.g, this.x, this.y);
            } else {
               gB.render(this.g, this.mouse, this.x, this.y, debug);
            }

            this.y = this.y + this.goalButtonHeight + 2;
         }

         for (int ix = this.goalPosition + this.maxGoals; ix < this.goalPosition + this.maxGoals + 3 && ix < this.validGoalsButtons.size(); ix++) {
            GUIButtonGoalStats gB = this.validGoalsButtons.get(ix);
            if (pass == 0) {
               gB.renderEffects(this.g, this.x, this.y);
            } else {
               gB.render(this.g, this.mouse, this.x, this.y, debug);
            }

            this.y = this.y + this.goalButtonHeight / 6;
         }

         this.y = 3;
         this.openGoalPanel.render(this.g, this.mouse, this.x + 169, this.y, false, debug);
         if (this.validGoalsButtons.size() > this.maxGoals + 1) {
            this.pageUp.render(this.g, this.mouse, this.x + 215, this.y + 18, this.goalPosition == 0, debug);
            this.pageDown.render(this.g, this.mouse, this.x + 245, this.y + 18, this.goalPosition >= this.validGoalsButtons.size() - this.maxGoals - 1, debug);
         }
      }
   }

   public void moveUp() {
      this.goalPosition--;
      if (this.goalPosition < 0) {
         this.goalPosition = 0;
      }
   }

   public void moveDown() {
      this.goalPosition++;
      if (this.goalPosition > this.validGoalsButtons.size() - this.maxGoals - 1) {
         this.goalPosition = this.validGoalsButtons.size() - this.maxGoals - 1;
      }

      if (this.goalPosition < 0) {
         this.goalPosition = 0;
      }
   }

   public void pageUp() {
      this.goalPosition = this.goalPosition - this.maxGoals;
      if (this.goalPosition < 0) {
         this.goalPosition = 0;
      }
   }

   public void pageDown() {
      this.goalPosition = this.goalPosition + this.maxGoals;
      if (this.goalPosition > this.validGoalsButtons.size() - this.maxGoals - 1) {
         this.goalPosition = this.validGoalsButtons.size() - this.maxGoals - 1;
      }

      if (this.goalPosition < 0) {
         this.goalPosition = 0;
      }
   }
}
