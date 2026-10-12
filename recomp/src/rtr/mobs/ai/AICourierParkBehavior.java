package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AICourierParkBehavior extends AIBase {
   private static final int FIND_PARKING = 0;
   private static final int PATHFIND_TO_PARKING_SPOT = 1;
   private static final int PARK = 2;
   private static AIBase resetBranch;
   private static AIBase nextBranch;

   public AICourierParkBehavior(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_COURIER_PARK_BEHAVIOR;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowCourierBehavior() && this.thisMob.getAssignedSpawn() != -1;
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getCourierBehaviorChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         nextBranch = this.thisMob.getAICourierDepartBehavior();
         switch (this.aiStep) {
            case 0:
               this.findParking();
               break;
            case 1:
               this.pathfindToParkingSpot();
               break;
            case 2:
               this.park();
         }
      }
   }

   private void findParking() throws SlickException {
      ObjectBase thisSpawn = this.object.getObjects().get(this.thisMob.getAssignedSpawn());
      ArrayList<OrderedPair> allParking = thisSpawn.getCourierGolemParking();
      ArrayList<OrderedPair> validParking = new ArrayList<>();

      for (OrderedPair o : allParking) {
         if (this.thisMob.canReach(o)) {
            validParking.add(o);
         }
      }

      for (MobBase m : this.mob.getMobsByType(MobBase.MobType.COURIER_GOLEM, false)) {
         if (m.getAssignment().getAssignmentType() == Assignment.AssignmentType.COURIER_PARKED && m.getAssignment().getPrimaryCoordinates() != null) {
            validParking.remove(m.getAssignment().getPrimaryCoordinates());
         }
      }

      if (validParking.size() <= 0) {
         this.switchNode(resetBranch);
      } else {
         OrderedPair closestParkingSpot = this.pathFinder
            .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), validParking, false, true, this.thisMobFlags.getBlockMapGroup());
         if (closestParkingSpot != null) {
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            assignment.setPrimaryCoordinates(closestParkingSpot);
            assignment.setPrimaryObject(thisSpawn);
            assignment.setAssignmentType(Assignment.AssignmentType.COURIER_PARKED);
            assignment.finalizeAssignment();
            this.requestPath(closestParkingSpot);
            this.aiStep = 1;
         } else {
            this.switchNode(resetBranch);
         }
      }
   }

   private void pathfindToParkingSpot() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.courierParkBehavior.pathfindToParkingSpot"));
      this.runPath();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject == null || currentObject.isDestroyed()) {
         this.switchNode(resetBranch);
      } else if (this.pathFailed) {
         this.aiStep = 0;
      } else if (this.pathCompleted) {
         this.aiStep = 2;
      }
   }

   private void park() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.courierParkBehavior.park"));
      Assignment assignment = this.thisMob.getAssignment();
      ObjectBase currentObject = assignment.getPrimaryObject();
      if (currentObject != null && !currentObject.isDestroyed()) {
         if (this.thisMob.getAssignedSpawn() == -1) {
            this.switchNode(resetBranch);
         } else {
            if (this.thisMob.hasCarriedResources()) {
               String mapFileName = this.thisMob.getAssignment().getRegionFileName();
               ResourceModule.ResourceType type = this.thisMob.getCarriedResourcesListType();
               if (this.migration.getDailySendAmountRemaining(mapFileName, type) < 0) {
                  this.runDropResource();
                  return;
               }
            }

            if (this.thisMob.getTileX() == assignment.getPrimaryX() && this.thisMob.getTileY() == assignment.getPrimaryY()) {
               if (this.courierIdleWithResources()) {
                  this.thisMob.runCourierIdleWithResourcesDelayTick(1);
               } else {
                  this.thisMob.resetCourierIdleWithResourcesDelayTick();
               }

               if (this.thisMobFlags.getCourierDepartRegionTimeOfDay() == this.time.getTimeOfDay()
                  && Utilities.randomInt(this.thisMobFlags.getCourierDepartRegionChance()) == 0) {
                  String mapName = assignment.getRegionName();
                  String mapFileName = assignment.getRegionFileName();
                  boolean sendNow = this.canSendEarly();
                  if (!this.thisMob.canCarryMore() || sendNow) {
                     this.thisMob.unassignFromBuildings();
                     this.switchNode(nextBranch);
                     assignment.setRegionName(mapName);
                     assignment.setRegionFileName(mapFileName);
                     assignment.setAssignmentType(Assignment.AssignmentType.COURIER_DEPARTING);
                     assignment.finalizeAssignment();
                     return;
                  }
               }
            } else {
               this.aiStep = 1;
               this.requestPath(assignment.getPrimaryCoordinates());
            }
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   private boolean courierIdleWithResources() {
      if (!this.thisMob.hasCarriedResources()) {
         return false;
      } else {
         for (MobBase m : this.mob.getMobsByAssignment(Assignment.AssignmentType.COURIER_SUPPLIER_DELIVERING, false)) {
            if (m.getAssignment().getPrimaryMob() == this.thisMob) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean canSendEarly() {
      Assignment assignment = this.thisMob.getAssignment();
      String mapFileName = assignment.getRegionFileName();
      if (mapFileName != null && this.thisMob.hasCarriedResources()) {
         if (this.thisMob.getCourierIdleWithResourcesDelayTick() >= this.thisMobFlags.getCourierIdleWithResourcesDelay()) {
            return true;
         }

         ResourceModule.ResourceType type = this.thisMob.getCarriedResourcesListType();
         if (this.migration.getDailySendAmountRemaining(mapFileName, type) == 0) {
            return true;
         }
      }

      return false;
   }
}
