package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;

public class AICaptureSoul extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_RESOURCE_ON_GROUND = 1;
   private static final int PATHFIND_TO_BUILDING = 2;
   private static final int PICK_UP_RESOURCE_ON_GROUND = 3;
   private static final int PICK_UP_RESOURCE_IN_BUILDING = 4;
   private static final int LIFT_RESOURCE = 5;
   private static AIBase failBranch;
   private static AIBase exitBranch;

   public AICaptureSoul(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CAPTURE_SOUL;
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
         failBranch = this.thisMob.getAIPlaceVessel();
         exitBranch = this.thisMob.getAICaptureSoulInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToResourceOnGround();
               break;
            case 2:
               this.pathfindToBuilding();
               break;
            case 3:
               this.pickUpResourceOnGround();
               break;
            case 4:
               this.pickUpResourcesInBuilding();
               break;
            case 5:
               this.liftResource();
         }
      }
   }

   private void findWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canCaptureSouls()) {
            this.switchNode(failBranch);
         } else {
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            ArrayList<MobBase> allDeadVillagers = this.mob.getDeadMobsByGroups(MobBase.MobGroup.VILLAGER);
            ArrayList<MobBase> allOccultistsEnroute = this.mob.getMobsByAssignment(Assignment.AssignmentType.CAPTURING_SOUL, false);
            ArrayList<MobBase> allValidDead = new ArrayList<>();

            label74:
            for (MobBase m : allDeadVillagers) {
               if (this.thisMob.canReach(m, true) && !this.map.getMapThreatMap().isTileThreatened(m.getTileX(), m.getTileY()) && m.getBoundGhostBody() != null) {
                  for (MobBase m2 : allOccultistsEnroute) {
                     if (m2.getAssignment().getPrimaryMob() == m) {
                        continue label74;
                     }
                  }

                  allValidDead.add(m);
               }
            }

            if (allValidDead.size() == 0) {
               this.switchNode(failBranch);
            } else {
               ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
               ArrayList<ObjectBase> validStoresByTypeLowestPriority = AIUtilities.createLowestPriorityStorageListHashMap(
                  this.thisMob, ResourceModule.ResourceType.EMPTY_EERIE_VESSEL, objectList
               );
               ArrayList<ResourceBase> validResourcesOnGroundByType = AIUtilities.createAvailabeResourceList(
                  this.thisMob, ResourceModule.ResourceType.EMPTY_EERIE_VESSEL, true, this.thisMobFlags.getWorkNearbyResourcesOnGroundDistance()
               );
               if (validResourcesOnGroundByType.size() == 0 && validStoresByTypeLowestPriority.size() == 0) {
                  this.switchNode(failBranch);
               } else {
                  OrderedPair nearestDeadCoordinates = this.pathFinder
                     .searchForMob(this.thisMob.getTileX(), this.thisMob.getTileY(), allValidDead, false, true, this.thisMobFlags.getBlockMapGroup());
                  if (nearestDeadCoordinates == null) {
                     this.switchNode(failBranch);
                  } else {
                     ArrayList<OrderedPair> coordinatesList = AIUtilities.createAvailabeResourceCoordinatesList(
                        this.thisMob, validStoresByTypeLowestPriority, validResourcesOnGroundByType
                     );
                     OrderedPair closestResourceAnywhere = this.pathFinder
                        .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), coordinatesList, false, true, this.thisMobFlags.getBlockMapGroup());
                     ResourceBase pickupResource = this.resource.getResourceOnGround(closestResourceAnywhere);
                     if (pickupResource != null && pickupResource.getType() == ResourceModule.ResourceType.EMPTY_EERIE_VESSEL) {
                        MobBase deadOut = this.pathFinder.mobFetch(allValidDead, nearestDeadCoordinates.getX(), nearestDeadCoordinates.getY());
                        assignment.setPrimaryMob(deadOut);
                        assignment.setPrimaryCoordinates(closestResourceAnywhere);
                        assignment.addPickupResources(pickupResource);
                        assignment.setPickupResourceType(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL);
                        assignment.setAssignmentType(Assignment.AssignmentType.CAPTURING_SOUL);
                        assignment.finalizeAssignment();
                        this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
                        this.aiStep = 1;
                     } else {
                        ObjectBase pickupBuilding = this.object.getBuildingAtLocation(closestResourceAnywhere);
                        if (pickupBuilding != null && validStoresByTypeLowestPriority.contains(pickupBuilding)) {
                           MobBase deadOut = this.pathFinder.mobFetch(allValidDead, nearestDeadCoordinates.getX(), nearestDeadCoordinates.getY());
                           OrderedPair pickupOutCoordinates = pickupBuilding.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
                           assignment.setPrimaryMob(deadOut);
                           assignment.setPrimaryObject(pickupBuilding);
                           assignment.setPrimaryCoordinates(pickupOutCoordinates);
                           assignment.addPickupResources(pickupBuilding.getAvailableResource(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL));
                           assignment.setPickupResourceType(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL);
                           assignment.setAssignmentType(Assignment.AssignmentType.CAPTURING_SOUL);
                           assignment.finalizeAssignment();
                           this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
                           this.aiStep = 2;
                        } else {
                           this.switchNode(failBranch);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void pathfindToResourceOnGround() throws SlickException {
      Text.setVariableText("resourceName", this.thisMob.getAssignment().getPickupResourceType().getTemplate().getName());
      Text.setVariableText("mobName", this.thisMob.getAssignment().getPrimaryMobName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.captureSoul.pathfindToResourceOnGround"));
      this.runPath();
      OrderedPair o = this.thisMob.getAssignment().getPrimaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (!this.pathFailed
         && r != null
         && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
         && !this.road.isResourceNeededForRoad(r)
         && this.thisMob.getAssignment().containsPickupResources(r)) {
         if (this.pathCompleted) {
            this.aiStep = 3;
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void pickUpResourceOnGround() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      MobBase dead = this.thisMob.getAssignment().getPrimaryMob();
      if (dead != null && this.thisMob.canReach(dead, true) && this.resource.getResourceTypeOnGround(tileX, tileY) != null) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.addCarriedResource(this.resource.getResourceOnGround(tileX, tileY));
            this.thisMob.getAssignment().removeAndGetFirstPickupResources();
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 5;
         }
      } else {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionLift();
         this.aiStep = 0;
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObject().getFormattedName());
      Text.setVariableText("resourceName", this.thisMob.getAssignment().getPickupResourceType().getTemplate().getName());
      Text.setVariableText("mobName", this.thisMob.getAssignment().getPrimaryMobName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.captureSoul.pathfindToBuilding"));
      this.runPath();
      MobBase dead = this.thisMob.getAssignment().getPrimaryMob();
      if (!this.pathFailed && dead != null && this.thisMob.canReach(dead, true)) {
         if (this.pathCompleted) {
            this.aiStep = 4;
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void pickUpResourcesInBuilding() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase building = assignment.getPrimaryObject();
      MobBase dead = assignment.getPrimaryMob();
      if (building != null
         && dead != null
         && this.thisMob.canReach(dead, true)
         && building == this.object.getBuildingAtLocation(tileX, tileY)
         && building.getResourceCount(this.thisMob.getAssignment().getFirstPickupResources().getType()) > 0) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            ResourceModule.ResourceType pickupType = assignment.getPickupResources().get(0).getType();

            while (assignment.getPickupResources().size() > 0) {
               ResourceBase deliveryResource = assignment.removeAndGetFirstPickupResources();
               ResourceBase r = building.takeStores(deliveryResource);
               if (r == null) {
                  r = building.takeStores(pickupType);
               }

               if (r != null) {
                  this.thisMob.addCarriedResource(r, true);
               }
            }

            this.thisMob.getSprite().restart();
            if (this.thisMob.hasCarriedResources()) {
               this.thisMob.setPositionLift();
               this.aiStep = 5;
            } else {
               this.thisMob.setPositionFace();
               this.aiStep = 0;
            }
         }
      } else {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.aiStep = 0;
      }
   }

   private void liftResource() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      MobBase dead = assignment.getPrimaryMob();
      if (dead != null && this.thisMob.canReach(dead, true)) {
         this.thisMob.setPositionLift();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.switchNode(exitBranch);
            assignment.setPrimaryMob(dead);
            assignment.setPickupResourceType(this.thisMob.getCarriedResourcesListType());
            assignment.setAssignmentType(Assignment.AssignmentType.CAPTURING_SOUL);
            assignment.finalizeAssignment();
         }
      } else {
         this.switchNode(exitBranch);
      }
   }
}
