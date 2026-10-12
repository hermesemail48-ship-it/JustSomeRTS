package rtr.mobs.ai.corruption;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.ai.AIBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.road.RoadModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AICorruptionDeliverRoadResourceInHand extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_ROAD = 1;
   private static final int DROP_OFF_RESOURCE = 2;
   private static final int DROP_OFF_LIFT = 3;
   private static AIBase failWithResourceBranch;
   private static AIBase nextBranch;

   public AICorruptionDeliverRoadResourceInHand(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CORRUPTION_DELIVER_ROAD_RESOURCE_IN_HAND;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMob.hasCarriedResources();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         failWithResourceBranch = this.thisMob.getAIStart();
         nextBranch = this.thisMob.getAICorruptionRoadWork();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToRoad();
               break;
            case 2:
               this.dropOffResource();
               break;
            case 3:
               this.dropOffLift();
         }
      }
   }

   private void findWork() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      OrderedPair deliveryOut = assignment.getSecondaryCoordinates();
      if (deliveryOut == null) {
         ResourceModule.ResourceType resourceTypeCarried = this.thisMob.getCarriedResourcesListType();
         ArrayList<ResourceModule.ResourceType> validResourceDeliveryTypes = new ArrayList<>();
         validResourceDeliveryTypes.add(ResourceModule.ResourceType.WOOD);
         validResourceDeliveryTypes.add(ResourceModule.ResourceType.ROCK);
         if (!validResourceDeliveryTypes.contains(resourceTypeCarried)) {
            this.switchNode(failWithResourceBranch);
            return;
         }

         ArrayList<OrderedPair> roadSelectionList = this.corruption.getCorruptedRoadMapArray();
         ArrayList<OrderedPair> roadSelectionListFiltered = new ArrayList<>();
         roadSelectionListFiltered.addAll(roadSelectionList);

         for (MobBase m : this.mob.getMobsByGroup(MobBase.MobGroup.MONSTER, false)) {
            if (m.getAssignment() != null) {
               if (m.getAssignment().getPrimaryCoordinates() != null) {
                  roadSelectionListFiltered.remove(m.getAssignment().getPrimaryCoordinates());
               }

               if (m.getAssignment().getSecondaryCoordinates() != null) {
                  roadSelectionListFiltered.remove(m.getAssignment().getSecondaryCoordinates());
               }
            }
         }

         if (roadSelectionListFiltered.size() <= 0) {
            this.switchNode(failWithResourceBranch);
            return;
         }

         ArrayList<OrderedPair> validRoads = new ArrayList<>();

         for (OrderedPair o : roadSelectionListFiltered) {
            RoadModule.RoadType roadType = this.road.getRoadType(o.getX(), o.getY());
            if (roadType.getTemplate().getNextRoadType() != null
               && this.road.getNextRequiredResource(o.getX(), o.getY()) == resourceTypeCarried
               && this.thisMob.canReach(o)) {
               ResourceBase resourceOnGround = this.resource.getResourceOnGround(o.getX(), o.getY());
               if (resourceOnGround == null) {
                  validRoads.add(o);
               }
            }
         }

         if (validRoads.size() == 0) {
            this.switchNode(failWithResourceBranch);
            return;
         }

         deliveryOut = this.pathFinder
            .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), validRoads, false, true, this.thisMobFlags.getBlockMapGroup());
      }

      assignment.reset();
      assignment.setSecondaryCoordinates(deliveryOut);
      assignment.setAssignmentType(Assignment.AssignmentType.DELIVERY);
      assignment.finalizeAssignment();
      this.requestPath(deliveryOut);
      this.aiStep = 1;
   }

   private void pathfindToRoad() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.corruptionDeliverRoadResourceInHand.pathfindToRoad"));
      this.runPath();
      if (this.pathCompleted) {
         this.aiStep = 2;
      } else if (this.pathFailed) {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
         return;
      }
   }

   private void dropOffResource() throws SlickException {
      OrderedPair coordinates = this.thisMob.getAssignment().getSecondaryCoordinates();
      int workX = coordinates.getX();
      int workY = coordinates.getY();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      if (workX == tileX && workY == tileY && this.corruption.getCorruptedRoadMapArray().contains(coordinates)) {
         this.thisMob.setPositionDrop();
         if (!this.thisMob.getSprite().isStopped()) {
            return;
         }

         this.thisMob.awardXP(Utilities.randomInt(5) + 5);
         if (this.thisMobFlags.getAllowGodExperience()) {
            this.god.increaseGodXP(0.01, GodModule.GodXPType.DELIVERING_RESOURCES_TO_ROAD);
         }

         ResourceBase resourceOnGroundCheck = this.resource.getResourceOnGround(tileX, tileY);
         if (resourceOnGroundCheck != null) {
            OrderedPair o = this.resource.findOpenSpace(tileX, tileY, this.thisMobFlags.getBlockMapGroup());
            this.resource.moveResourcesOnGround(tileX, tileY, o.getX(), o.getY(), resourceOnGroundCheck);
         }

         this.resource.setResourcesOnGround(tileX, tileY, this.thisMob.removeFirstCarriedResource());
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionLift();
         this.aiStep = 3;
      } else {
         this.switchNode(failWithResourceBranch);
      }
   }

   private void dropOffLift() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         if (this.thisMob.hasCarriedResources()) {
            this.thisMob.getAssignment().reset();
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 0;
         } else {
            this.switchNode(nextBranch);
         }
      }
   }
}
