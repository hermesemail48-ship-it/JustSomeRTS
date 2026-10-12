package rtr.mobs.ai.corruption;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.ai.AIBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AICorruptionDeliverResourceInHand extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_BUILDING = 1;
   private static final int DROP_OFF_RESOURCE = 2;
   private static final int DROP_OFF_LIFT = 3;
   private static final int PATHFIND_TO_EXTRA_RESOURCE = 4;
   private static final int PICKUP_EXTRA_RESOURCE = 5;
   private static final int LIFT_EXTRA_RESOURCE = 6;
   private static AIBase resetBranch;
   private static AIBase failWithResourceBranch;

   public AICorruptionDeliverResourceInHand(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CORRUPTION_DELIVER_RESOURCE_IN_HAND;
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
         resetBranch = this.thisMob.getAIStart();
         failWithResourceBranch = this.thisMob.getAICorruptionDeliverRoadResourceInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToBuilding();
               break;
            case 2:
               this.dropOffResource();
               break;
            case 3:
               this.dropOffLift();
               break;
            case 4:
               this.pathfindToExtraResource();
               break;
            case 5:
               this.pickupExtraResource();
               break;
            case 6:
               this.liftExtraResource();
         }
      }
   }

   private void findWork() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      ObjectBase deliveryOut = assignment.getDeliveryObject();
      if (deliveryOut == null) {
         ResourceModule.ResourceType resourceTypeCarried = this.thisMob.getCarriedResourcesListType();
         ArrayList<ResourceModule.ResourceType> canDeliverResources = new ArrayList<>();
         ArrayList<ObjectBase> objectList = this.object.getObjectsByGroup(MobBase.MobGroup.MONSTER);

         for (ObjectBase objectCheck : objectList) {
            if (!objectCheck.isPaused()
               && objectCheck.getObjectFlags().canBuildingBeBuilt(resourceTypeCarried)
               && this.thisMob.canReach(objectCheck)
               && objectCheck.isOnCorruption()
               && objectCheck.getAcceptBuildResourceCount(resourceTypeCarried, -1) > 0) {
               canDeliverResources.add(resourceTypeCarried);
               break;
            }
         }

         if (canDeliverResources.size() == 0) {
            this.switchNode(failWithResourceBranch);
            return;
         }

         ArrayList<ObjectBase> validObjects = new ArrayList<>();

         for (ObjectBase objectCheckx : objectList) {
            if (!objectCheckx.isPaused()
               && objectCheckx.getObjectFlags().canBuildingBeBuilt(resourceTypeCarried)
               && this.thisMob.canReach(objectCheckx)
               && objectCheckx.isOnCorruption()
               && objectCheckx.getAcceptBuildResourceCount(resourceTypeCarried, -1) > 0) {
               validObjects.add(objectCheckx);
            }
         }

         if (validObjects.size() == 0) {
            this.switchNode(failWithResourceBranch);
            return;
         }

         OrderedPair deliveryCoordinatesOut = this.pathFinder
            .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), validObjects, false, true, this.thisMobFlags.getBlockMapGroup());
         deliveryOut = this.object.getBuildingAtLocation(deliveryCoordinatesOut.getX(), deliveryCoordinatesOut.getY());
      }

      OrderedPair randomOut = deliveryOut.getBestRandomInteractCoordinates(this.thisMob, true, false, true);
      assignment.reset();
      assignment.setDeliveryObject(deliveryOut);
      assignment.setPrimaryCoordinates(randomOut);
      assignment.setAssignmentType(Assignment.AssignmentType.DELIVERY);
      assignment.finalizeAssignment();
      this.requestPath(randomOut);
      this.aiStep = 1;
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getDeliveryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.corruptionDeliverResourceInHand.pathfindToBuilding"));
      this.runPath();
      ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
      if (deliveryObject == null || !this.object.objectExists(deliveryObject)) {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      } else if (this.pathCompleted) {
         this.aiStep = 2;
      } else if (!this.pathFailed && this.thisMob.canReach(this.goalCoordinates)) {
         if (this.searchForMore()) {
            this.aiStep = 4;
         }
      } else {
         if (deliveryObject != null && this.thisMob.canReach(deliveryObject) && deliveryObject.isOnCorruption()) {
            OrderedPair randomRecalc = deliveryObject.getBestRandomInteractCoordinates(this.thisMob, true, false, true);
            this.thisMob.getAssignment().setPrimaryCoordinates(randomRecalc);
            this.requestPath(randomRecalc);
         } else {
            this.thisMob.getAssignment().reset();
            this.aiStep = 0;
         }
      }
   }

   private void dropOffResource() throws SlickException {
      OrderedPair coordinates = this.thisMob.getAssignment().getPrimaryCoordinates();
      int workX = coordinates.getX();
      int workY = coordinates.getY();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase deliveryBuilding = this.thisMob.getAssignment().getDeliveryObject();
      ResourceModule.ResourceType resourceType = this.thisMob.getCarriedResourcesListType();
      if (workX == tileX && workY == tileY) {
         if (this.object.objectExists(deliveryBuilding)
            && deliveryBuilding.getResourceValueCount(resourceType) < deliveryBuilding.getObjectFlags().getResourceValueBaseCount(resourceType)) {
            this.thisMob.setPositionDrop();
            if (this.thisMob.getSprite().isStopped()) {
               this.thisMob.awardXP(Utilities.randomInt(5) + 5);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.DELIVERING_RESOURCES_TO_CONSTRUCTION);
               }

               ResourceBase r = this.thisMob.removeFirstCarriedResource();
               deliveryBuilding.increaseStores(r);
               this.resource.setResourceInBuilding(deliveryBuilding, r);
               this.thisMob.getSprite().restart();
               this.thisMob.setPositionLift();
               this.aiStep = 3;
            }
         } else {
            this.thisMob.getAssignment().reset();
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.aiStep = 0;
         }
      } else {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      }
   }

   private void dropOffLift() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase deliveryBuilding = this.thisMob.getAssignment().getDeliveryObject();
      ResourceModule.ResourceType resourceType = this.thisMob.getCarriedResourcesListType();
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         if (this.thisMob.hasCarriedResources() && deliveryBuilding.getAcceptBuildResourceCount(resourceType, this.thisMob.getID()) > 0) {
            if (this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup())[tileX][tileY] > 0) {
               return;
            }

            OrderedPair randomOut = deliveryBuilding.getBestRandomInteractCoordinates(this.thisMob, true, false, true);
            if (randomOut == null) {
               this.thisMob.getAssignment().reset();
               this.thisMob.getSprite().restart();
               this.thisMob.setPositionFace();
               this.aiStep = 0;
               return;
            }

            this.thisMob.getAssignment().setPrimaryCoordinates(randomOut);
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.requestPath(randomOut);
            this.aiStep = 1;
         } else {
            this.switchNode(resetBranch);
         }
      }
   }

   private void pathfindToExtraResource() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getDeliveryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.corruptionDeliverResourceInHand.pathfindToExtraResource"));
      this.runPath();
      OrderedPair o = this.thisMob.getAssignment().getSecondaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (!this.pathFailed
         && r != null
         && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
         && r.getType() == this.thisMob.getCarriedResourcesListType()) {
         if (this.pathCompleted) {
            this.aiStep = 5;
         }
      } else {
         ObjectBase currentObject = this.thisMob.getAssignment().getDeliveryObject();
         if (currentObject != null && this.thisMob.canReach(currentObject)) {
            OrderedPair randomRecalc = currentObject.getBestRandomInteractCoordinates(this.thisMob, true, false, true);
            this.thisMob.getAssignment().setPrimaryCoordinates(randomRecalc);
            this.requestPath(randomRecalc);
            this.aiStep = 1;
         } else {
            this.thisMob.getAssignment().reset();
            this.aiStep = 0;
         }
      }
   }

   private void pickupExtraResource() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
      if (deliveryObject != null && this.thisMob.canReach(deliveryObject)) {
         Assignment assignment = this.thisMob.getAssignment();
         ResourceBase resourceOnGround = this.resource.getResourceOnGround(tileX, tileY);
         if (resourceOnGround != null && assignment.containsPickupResources(resourceOnGround)) {
            this.thisMob.setPositionPickup();
            if (this.thisMob.getSprite().isStopped()) {
               this.thisMob.addCarriedResource(resourceOnGround);
               this.thisMob.getAssignment().removePickupResources(resourceOnGround);
               this.thisMob.getSprite().restart();
               this.thisMob.setPositionLift();
               this.aiStep = 6;
            }
         } else {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.thisMob.getAssignment().reset();
            this.aiStep = 0;
         }
      } else {
         this.switchNode(failWithResourceBranch);
      }
   }

   private void liftExtraResource() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
         if (deliveryObject != null && this.thisMob.canReach(deliveryObject)) {
            OrderedPair randomRecalc = deliveryObject.getBestRandomInteractCoordinates(this.thisMob, true, false, true);
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.thisMob.getAssignment().setPrimaryCoordinates(randomRecalc);
            this.requestPath(randomRecalc);
            this.aiStep = 1;
         } else {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.thisMob.getAssignment().reset();
            this.aiStep = 0;
         }
      }
   }

   private boolean searchForMore() throws SlickException {
      if (!this.thisMob.canCarryMore()) {
         return false;
      } else {
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         if (this.thisMob.getWorkFindExtraResourcesLastX() == tileX && this.thisMob.getWorkFindExtraResourcesLastY() == tileY) {
            return false;
         } else {
            this.thisMob.setWorkFindExtraResourcesLastX(tileX);
            this.thisMob.setWorkFindExtraResourcesLastY(tileY);
            Assignment assignment = this.thisMob.getAssignment();
            if (assignment.getDeliveryObject().getAcceptBuildResourceCount(this.thisMob.getCarriedResourcesListType(), -1) <= 0) {
               return false;
            } else {
               ArrayList<ResourceBase> resourcesInRange = this.resource
                  .getResourcesNearby(tileX, tileY, this.thisMobFlags.getCorruptionWorkFindExtraResourcesDistance());
               if (resourcesInRange.size() > 0) {
                  resourcesInRange = this.resource
                     .cullResourcesByDistance(
                        tileX, tileY, this.thisMobFlags.getCorruptionWorkFindExtraResourcesDistance(), resourcesInRange, this.thisMobFlags.getBlockMapGroup()
                     );
                  if (resourcesInRange.size() > 0) {
                     for (int i = resourcesInRange.size() - 1; i >= 0; i--) {
                        ResourceBase r = resourcesInRange.get(i);
                        if (r.getType() != this.thisMob.getCarriedResourcesListType()
                           || r.isAssigned()
                           || this.thisMobFlags.canIgnoreThreatenedTiles() && this.map.getMapThreatMap().isResourceThreatened(r)) {
                           resourcesInRange.remove(i);
                        }
                     }

                     if (resourcesInRange.size() > 0) {
                        OrderedPair pickupCoordinatesOut = this.pathFinder
                           .searchForResource(
                              this.thisMob.getTileX(), this.thisMob.getTileY(), resourcesInRange, false, true, this.thisMobFlags.getBlockMapGroup()
                           );
                        if (pickupCoordinatesOut != null) {
                           ResourceBase r = this.resource.getResourceOnGround(pickupCoordinatesOut.getX(), pickupCoordinatesOut.getY());
                           assignment.setSecondaryCoordinates(r.getTileX(), r.getTileY());
                           assignment.addPickupResources(r);
                           this.requestPath(r.getTileX(), r.getTileY());
                           return true;
                        }
                     }
                  }
               }

               return false;
            }
         }
      }
   }
}
