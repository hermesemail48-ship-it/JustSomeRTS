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

public class AIPatrolVillage extends AIBase {
   private static final int FIND_WAYPOINT = 0;
   private static final int PATHFIND_TO_WAYPOINT = 1;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIPatrolVillage(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_RANGER_PATROL_VILLAGE;
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
         failBranch = this.thisMob.getAIPatrolFrontLine();
         if (this.waypoint.getRangerWaypoints().size() > 0) {
            this.switchNode(failBranch);
         } else if (!this.thisMob.getMobJobType().getJob().isGuard()) {
            this.switchNode(failBranch);
         } else if (this.time.getTimeOfDay() != TimeModule.TimeOfDay.DAWN && !this.time.isBloodMoon()) {
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
      ArrayList<OrderedPair> waypoints = this.waypoint.getRangerWaypoints();
      ArrayList<OrderedPair> validWaypoints = new ArrayList<>();

      for (OrderedPair o : waypoints) {
         if (this.thisMob.canReach(o)) {
            validWaypoints.add(o);
         }
      }

      if (validWaypoints.size() <= 0) {
         this.switchNode(failBranch);
      } else {
         OrderedPair closestWaypoint = this.pathFinder
            .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), validWaypoints, false, true, this.thisMobFlags.getBlockMapGroup());
         int waypointPosition = 0;
         if (Utilities.randomInt(2) == 0) {
            waypointPosition = validWaypoints.indexOf(closestWaypoint) - 1;
            if (waypointPosition < 0) {
               waypointPosition = validWaypoints.size() - 1;
            }
         } else {
            waypointPosition = validWaypoints.indexOf(closestWaypoint) + 1;
            if (waypointPosition >= validWaypoints.size()) {
               waypointPosition = 0;
            }
         }

         OrderedPair nextWaypoint = validWaypoints.get(waypointPosition);
         ArrayList<OrderedPair> validCoordinates = this.pathFinder
            .getCoordinatesInRange(nextWaypoint.getX(), nextWaypoint.getY(), 6, false, this.thisMobFlags.getBlockMapGroup());
         OrderedPair randomCoordinate = validCoordinates.get(Utilities.randomInt(validCoordinates.size()));
         if (randomCoordinate != null) {
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            assignment.setPrimaryCoordinates(randomCoordinate);
            assignment.setAssignmentType(Assignment.AssignmentType.PATROL);
            assignment.finalizeAssignment();
            this.requestPath(randomCoordinate);
            this.aiStep = 1;
         } else {
            this.switchNode(failBranch);
         }
      }
   }

   private void pathfindToWaypoint() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.rangerPatrolVillage.pathfindToWaypoint"));
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
