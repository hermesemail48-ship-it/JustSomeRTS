package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIGolemPatrolBehavior extends AIBase {
   private static final int FIND_WAYPOINT = 0;
   private static final int PATHFIND_TO_WAYPOINT = 1;
   private static AIBase resetBranch;

   public AIGolemPatrolBehavior(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_GOLEM_PATROL_BEHAVIOR;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowGolemBehavior() && this.object.isInRange(this.thisMob.getTileX(), this.thisMob.getTileY());
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getGolemBehaviorChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         switch (this.aiStep) {
            case 0:
               this.findWaypoint();
               break;
            case 1:
               this.pathfindToWaypoint();
         }
      }
   }

   private void findWaypoint() throws SlickException {
      ArrayList<OrderedPair> waypoints = this.waypoint.getGolemWaypoints();
      ArrayList<OrderedPair> validWaypoints = new ArrayList<>();

      for (OrderedPair o : waypoints) {
         if (this.thisMob.canReach(o)) {
            validWaypoints.add(o);
         }
      }

      if (validWaypoints.size() <= 0) {
         this.switchNode(resetBranch);
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
         }
      }
   }

   private void pathfindToWaypoint() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.golemPatrolBehavior.pathfindToWaypoint"));
      this.runPath();
      if (this.pathFailed) {
         this.aiStep = 0;
      } else if (this.pathCompleted) {
         this.switchNode(resetBranch);
      }
   }
}
