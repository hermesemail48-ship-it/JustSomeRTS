package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.TimeModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIPatrolFrontLine extends AIBase {
   private static final int FIND_WAYPOINT = 0;
   private static final int PATHFIND_TO_WAYPOINT = 1;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIPatrolFrontLine(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_RANGER_PATROL_FRONT_LINE;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowWork() && this.thisMob.getMobJobType() != MobJobBase.MobJobType.NONE;
   }

   @Override
   public boolean tryRun() {
      return this.allowRun();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         failBranch = this.thisMob.getAITerminateAnimalPregnancy();
         if (!this.thisMob.getMobJobType().getJob().isGuard()) {
            this.switchNode(failBranch);
         } else if (this.time.getTimeOfDay() != TimeModule.TimeOfDay.DUSK && this.time.getTimeOfDay() != TimeModule.TimeOfDay.NIGHT && !this.time.isEclipse()) {
            this.switchNode(failBranch);
         } else {
            switch (this.aiStep) {
               case 0:
                  this.findWaypoint();
                  return;
               case 1:
                  this.pathfindToWaypoint();
                  return;
            }
         }
      }
   }

   private void findWaypoint() throws SlickException {
      OrderedPair nextWaypoint = this.waypoint.getRandomFrontLinesWaypoint(this.thisMob);
      if (nextWaypoint == null) {
         this.switchNode(failBranch);
      } else {
         ArrayList<OrderedPair> validCoordinates = this.pathFinder
            .getCoordinatesInRange(nextWaypoint.getX(), nextWaypoint.getY(), 3, false, this.thisMobFlags.getBlockMapGroup());
         if (validCoordinates.size() <= 0) {
            this.switchNode(failBranch);
         } else {
            OrderedPair randomCoordinate = validCoordinates.get(Utilities.randomInt(validCoordinates.size()));
            if (randomCoordinate != null) {
               Assignment assignment = this.thisMob.getAssignment();
               assignment.reset();
               assignment.setPrimaryCoordinates(randomCoordinate);
               assignment.setAssignmentType(Assignment.AssignmentType.PATROL);
               assignment.finalizeAssignment();
               this.requestPath(randomCoordinate);
               this.aiStep = 1;
            }
         }
      }
   }

   private void pathfindToWaypoint() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.rangerPatrolFrontLine.pathfindToWaypoint"));
      this.runPath();
      if (this.pathFailed) {
         this.aiStep = 0;
      } else if (this.pathCompleted) {
         this.thisMob.awardXP(Utilities.randomInt(2) + 2);
         if (this.thisMobFlags.getAllowGodExperience()) {
            this.god.increaseGodXP(0.01, GodModule.GodXPType.PATROLLING);
         }

         this.switchNode(resetBranch);
      }
   }
}
