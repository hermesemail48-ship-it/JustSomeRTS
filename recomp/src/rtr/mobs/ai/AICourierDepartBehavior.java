package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AICourierDepartBehavior extends AIBase {
   private static final int FIND_DEPART_COORDINATES = 0;
   private static final int PATHFIND_TO_DEPART_COORDINATES = 1;
   private static final int LEAVE_MAP = 2;
   private static AIBase resetBranch;

   public AICourierDepartBehavior(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_COURIER_DEPART_BEHAVIOR;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowCourierBehavior();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getCourierDepartRegionChance()) == 0 && this.canLeave();
   }

   private boolean canLeave() {
      return true;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         switch (this.aiStep) {
            case 0:
               this.findLeaveCoordinates();
               break;
            case 1:
               this.pathfindToLeaveCoordinates();
               break;
            case 2:
               this.leaveRegion();
         }
      }
   }

   private void findLeaveCoordinates() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      if (assignment.getRegionFileName().equals("")) {
         String[] mapListRaw = this.map.getConnectedRegionList();
         ArrayList<String> mapList = new ArrayList<>();

         for (String s : mapListRaw) {
            if (!s.equals("null")) {
               mapList.add(s);
            }
         }

         Collections.shuffle(mapList);

         for (String sx : mapList) {
            if (this.canMigrateIncludeEnRoute(sx)) {
               OrderedPair leaveCoordinates = this.getLeaveCoordinates(sx);
               if (leaveCoordinates == null) {
                  this.switchNode(resetBranch);
                  return;
               }

               assignment.reset();
               assignment.setPrimaryCoordinates(leaveCoordinates);
               assignment.setRegionName(this.map.getConnectedRegionListWorldName(sx));
               assignment.setRegionFileName(sx);
               assignment.setAssignmentType(Assignment.AssignmentType.MIGRATION_LEAVING_REGION);
               assignment.finalizeAssignment();
               this.requestPath(leaveCoordinates);
               this.aiStep = 1;
            }
         }
      } else {
         OrderedPair leaveCoordinates = this.getLeaveCoordinates(assignment.getRegionFileName());
         if (leaveCoordinates == null) {
            this.switchNode(resetBranch);
            return;
         }

         assignment.setPrimaryCoordinates(leaveCoordinates);
         this.requestPath(leaveCoordinates);
         this.aiStep = 1;
      }
   }

   private boolean canMigrateIncludeEnRoute(String s) {
      ArrayList<MobBase> migratingMobs = this.mob.getMobsByAssignment(Assignment.AssignmentType.MIGRATION_LEAVING_REGION, false);
      migratingMobs.remove(this.thisMob);
      if (migratingMobs.size() >= this.migration.getTotalRequestedPopulation()) {
         return false;
      } else {
         int count = 0;

         for (MobBase m : migratingMobs) {
            if (m.getAssignment().getRegionFileName().equals(s)) {
               count++;
            }
         }

         return count < this.migration.getRequestedPopulation(s);
      }
   }

   private OrderedPair getLeaveCoordinates(String mapName) {
      int migratingX = this.map.getConnectedRegionListWorldMapX(mapName);
      int migratingY = this.map.getConnectedRegionListWorldMapY(mapName);
      int thisX = this.map.getWorldMapX();
      int thisY = this.map.getWorldMapY();
      OrderedPair angleCoordinate = null;
      float angle = Utilities.calculateAngle(thisX, thisY, migratingX, migratingY);
      float checkX = 128.0F;
      float checkY = 128.0F;
      double cos = Math.cos(angle);

      for (double sin = Math.sin(angle);
         !Utilities.isOutOfBoundsOfUsableMap((int)checkX, (int)checkY) && this.map.getTileId((int)checkX, (int)checkY, 0) != 0;
         checkY = (float)(checkY + sin)
      ) {
         angleCoordinate = OrderedPair.getOrderedPair((int)checkX, (int)checkY);
         checkX = (float)(checkX + cos);
      }

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

      final OrderedPair sortGoal = angleCoordinate;
      Collections.sort(finalCoordinates, new Comparator<OrderedPair>() {
         public int compare(OrderedPair o1, OrderedPair o2) {
            int dx1 = sortGoal.getX() - o1.getX();
            int dy1 = sortGoal.getY() - o1.getY();
            int hValue1 = (int)Math.sqrt(dx1 * dx1 + dy1 * dy1);
            int dx2 = sortGoal.getX() - o2.getX();
            int dy2 = sortGoal.getY() - o2.getY();
            int hValue2 = (int)Math.sqrt(dx2 * dx2 + dy2 * dy2);
            return hValue1 - hValue2;
         }
      });
      int randomVariance = 16;
      if (finalCoordinates.size() < randomVariance) {
         randomVariance = finalCoordinates.size();
      }

      return finalCoordinates.size() <= 0 ? null : finalCoordinates.get(Utilities.randomInt(randomVariance));
   }

   private void pathfindToLeaveCoordinates() throws SlickException {
      Text.setVariableText("regionName", this.thisMob.getAssignment().getRegionName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.courierDepartBehavior.pathfindToLeaveCoordinates"));
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

   private void leaveRegion() throws SlickException {
      if (this.thisMob.getAlpha() <= 0.0F) {
         String mapFileName = this.thisMob.getAssignment().getRegionFileName();
         this.migration.storeCourierGolem(mapFileName, this.thisMob);
         this.goal.incrementGoal(GoalModule.GoalType.PORTER, this.thisMob.getCarriedResourcesList().size());
         this.god.increaseGodXP(0.01 * this.thisMob.getCarriedResourcesList().size(), GodModule.GodXPType.COURIERS_DEPARTING);
      }
   }
}
