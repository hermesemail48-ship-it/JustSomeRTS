package rtr.mobs.ai.corruption;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.ai.AIBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;

public class AICorruptionDeliverResourceOnGround extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_RESOURCE = 1;
   private static final int PICK_UP_RESOURCE = 2;
   private static final int LIFT_RESOURCE = 3;
   private static AIBase failBranch;
   private static AIBase exitOrFailBranch;

   public AICorruptionDeliverResourceOnGround(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CORRUPTION_DELIVER_RESOURCE_ON_GROUND;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return true;
   }

   @Override
   public boolean tryRun() {
      return this.allowRun();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         failBranch = this.thisMob.getAICorruptionHarvestWork();
         exitOrFailBranch = this.thisMob.getAICorruptionDeliverResourceInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToResource();
               break;
            case 2:
               this.pickUpResource();
               break;
            case 3:
               this.liftResource();
         }
      }
   }

   private void findWork() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ArrayList<ResourceBase> resourcesOnGround = this.resource
         .getResourcesNearby(this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getCorruptionWorkNearbyResourcesOnGroundDistance());
      if (resourcesOnGround.size() > 0) {
         resourcesOnGround = this.resource
            .cullResourcesByDistance(
               tileX, tileY, this.thisMobFlags.getCorruptionWorkNearbyResourcesOnGroundDistance() * 2, resourcesOnGround, this.thisMobFlags.getBlockMapGroup()
            );
      }

      if (resourcesOnGround.size() == 0) {
         this.switchNode(failBranch);
      } else {
         int mobID = this.thisMob.getID();
         ArrayList<ResourceModule.ResourceType> canDeliverResources = new ArrayList<>();
         ArrayList<ObjectBase> objectList = this.object.getObjectsByGroup(MobBase.MobGroup.MONSTER);
         ArrayList<ResourceModule.ResourceType> validResourceDeliveryTypes = new ArrayList<>();
         validResourceDeliveryTypes.add(ResourceModule.ResourceType.WOOD);
         validResourceDeliveryTypes.add(ResourceModule.ResourceType.ROCK);
         validResourceDeliveryTypes.add(ResourceModule.ResourceType.CRYSTAL);

         for (ResourceModule.ResourceType type : validResourceDeliveryTypes) {
            for (ObjectBase objectCheck : objectList) {
               if (!objectCheck.isPaused()
                  && objectCheck.getObjectFlags().canBuildingBeBuilt(type)
                  && objectCheck.isOnCorruption()
                  && this.thisMob.canReach(objectCheck)
                  && objectCheck.getAcceptBuildResourceCount(type, mobID) > 0) {
                  canDeliverResources.add(type);
                  break;
               }
            }
         }

         if (canDeliverResources.size() == 0) {
            this.switchNode(failBranch);
         } else {
            ArrayList<ResourceBase> availableResources = new ArrayList<>();

            for (int i = 0; i < resourcesOnGround.size(); i++) {
               ResourceBase r = resourcesOnGround.get(i);
               if (this.thisMob.canReach(r.getTileX(), r.getTileY())
                  && canDeliverResources.contains(r.getType())
                  && !r.isAssigned()
                  && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))) {
                  availableResources.add(r);
               }
            }

            if (availableResources.size() == 0) {
               this.switchNode(failBranch);
            } else {
               OrderedPair nearestResourceCoordinates = this.pathFinder
                  .searchForResource(tileX, tileY, availableResources, false, true, this.thisMobFlags.getBlockMapGroup());
               if (nearestResourceCoordinates == null) {
                  this.switchNode(failBranch);
               } else {
                  ResourceModule.ResourceType resourceTypeOnGround = this.resource
                     .getResourceTypeOnGround(nearestResourceCoordinates.getX(), nearestResourceCoordinates.getY());
                  ArrayList<ObjectBase> validObjects = new ArrayList<>();

                  for (ObjectBase objectCheckx : objectList) {
                     if (!objectCheckx.isPaused()
                        && objectCheckx.getObjectFlags().canBuildingBeBuilt(resourceTypeOnGround)
                        && this.thisMob.canReach(objectCheckx)
                        && objectCheckx.isOnCorruption()
                        && objectCheckx.getAcceptBuildResourceCount(resourceTypeOnGround, mobID) > 0) {
                        validObjects.add(objectCheckx);
                     }
                  }

                  if (validObjects.size() == 0) {
                     this.switchNode(failBranch);
                  } else {
                     OrderedPair deliveryCoordinatesOut = this.pathFinder
                        .searchForObject(
                           nearestResourceCoordinates.getX(),
                           nearestResourceCoordinates.getY(),
                           validObjects,
                           false,
                           true,
                           this.thisMobFlags.getBlockMapGroup()
                        );
                     ObjectBase deliveryOut = this.object.getBuildingAtLocation(deliveryCoordinatesOut.getX(), deliveryCoordinatesOut.getY());
                     Assignment assignment = this.thisMob.getAssignment();
                     assignment.reset();
                     assignment.setDeliveryObject(deliveryOut);
                     assignment.setPrimaryCoordinates(nearestResourceCoordinates);
                     assignment.addPickupResources(this.resource.getResourceOnGround(nearestResourceCoordinates.getX(), nearestResourceCoordinates.getY()));
                     assignment.setAssignmentType(Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND);
                     assignment.finalizeAssignment();
                     this.requestPath(nearestResourceCoordinates);
                     this.aiStep = 1;
                  }
               }
            }
         }
      }
   }

   private void pathfindToResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.corruptionDeliverResourceOnGround.pathfindToResource"));
      this.runPath();
      OrderedPair o = this.thisMob.getAssignment().getPrimaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (!this.pathFailed
         && r != null
         && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
         && this.thisMob.getAssignment().containsPickupResources(r)) {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void pickUpResource() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
      if (this.resource.getResourceTypeOnGround(tileX, tileY) != null && deliveryObject != null && this.thisMob.canReach(deliveryObject)) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.addCarriedResource(this.resource.getResourceOnGround(tileX, tileY));
            this.thisMob.getAssignment().removeAndGetFirstPickupResources();
            this.thisMob.getSprite().restart();
            if (this.thisMob.hasCarriedResources()) {
               this.thisMob.setPositionLift();
               this.aiStep = 3;
            } else {
               this.thisMob.setPositionFace();
               this.aiStep = 0;
            }
         }
      } else {
         this.switchNode(exitOrFailBranch);
      }
   }

   private void liftResource() throws SlickException {
      ObjectBase deliveryObject = this.thisMob.getAssignment().getDeliveryObject();
      if (deliveryObject != null && this.thisMob.canReach(deliveryObject)) {
         this.thisMob.setPositionLift();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.switchNode(exitOrFailBranch);
            Assignment assignment = this.thisMob.getAssignment();
            assignment.setDeliveryObject(deliveryObject);
            assignment.setAssignmentType(Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND);
            assignment.finalizeAssignment();
         }
      } else {
         this.switchNode(exitOrFailBranch);
      }
   }
}
