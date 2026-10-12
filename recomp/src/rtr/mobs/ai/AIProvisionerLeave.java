package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIProvisionerLeave extends AIBase {
   private static final int FIND_LEAVE_COORDINATES = 0;
   private static final int PATHFIND_TO_LEAVE_COORDINATES = 1;
   private static final int LEAVE_MAP = 2;

   public AIProvisionerLeave(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_PROVISIONER_LEAVE;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowProvisionerBehavior();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getProvisionerLeaveVillageChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         switch (this.aiStep) {
            case 0:
               this.findLeaveCoordinates();
               break;
            case 1:
               this.pathfindToLeaveCoordinates();
               break;
            case 2:
               this.leaveMap();
         }
      }
   }

   private void findLeaveCoordinates() throws SlickException {
      ArrayList<OrderedPair> validCoordinatesWithoutTopography = new ArrayList<>();
      ArrayList<OrderedPair> validCoordinatesWithTopography = new ArrayList<>();

      for (OrderedPair o : this.map.getMapData().getEdgeTiles()) {
         int x = o.getX();
         int y = o.getY();
         if (this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup())[x][y] < 1
            && this.thisMob.canReach(x, y)
            && !this.corruption.isTileCorrupted(x, y)) {
            if (this.map.getTileId(x, y, 6) == 0 && this.map.getTileId(x, y, 9) == 0) {
               validCoordinatesWithoutTopography.add(OrderedPair.getOrderedPair(x, y));
            }

            validCoordinatesWithTopography.add(OrderedPair.getOrderedPair(x, y));
         }
      }

      ArrayList<OrderedPair> finalCoordinates = null;
      if (validCoordinatesWithoutTopography.size() == 0) {
         finalCoordinates = validCoordinatesWithTopography;
      } else {
         finalCoordinates = validCoordinatesWithoutTopography;
      }

      OrderedPair exitCoordinates = null;
      if (finalCoordinates.size() == 0) {
         int x = Utilities.randomInt(256);
         int y = Utilities.randomInt(256);

         for (int attempts = 0; !this.thisMob.canReach(x, y) || attempts < 500 && this.object.isInRange(x, y); y = Utilities.randomInt(256)) {
            attempts++;
            x = Utilities.randomInt(256);
         }

         exitCoordinates = OrderedPair.getOrderedPair(x, y);
      } else {
         exitCoordinates = finalCoordinates.get(Utilities.randomInt(finalCoordinates.size()));
      }

      Assignment assignment = this.thisMob.getAssignment();
      assignment.reset();
      assignment.setPrimaryCoordinates(exitCoordinates);
      assignment.setAssignmentType(Assignment.AssignmentType.CATJEET_LEAVING_REGION);
      assignment.finalizeAssignment();
      this.requestPath(exitCoordinates);
      this.aiStep = 1;
   }

   private void pathfindToLeaveCoordinates() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.provisionerLeave.pathfindToLeaveCoordinates"));
      this.runPath();
      if (this.pathFailed) {
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
            this.thisMob.fadeOut();
         }
      }
   }

   private void leaveMap() throws SlickException {
      if (this.thisMob.getAlpha() <= 0.0F) {
         this.resource.deleteMobInventory(this.thisMob);
         this.thisMob.unassignFromAll(false);
         this.mob.deleteMob(this.thisMob);
      }
   }
}
