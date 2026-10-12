package rtr.mobs.ai.corruption;

import java.util.ArrayList;
import java.util.EnumMap;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.ai.AIBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.road.RoadModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;

public class AICorruptionDeliverRoadResourceOnGround extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_RESOURCE = 1;
   private static final int PICK_UP_RESOURCE = 2;
   private static final int LIFT_RESOURCE = 3;
   private static AIBase failBranch;
   private static AIBase failWithResourceBranch;
   private static AIBase exitBranch;

   public AICorruptionDeliverRoadResourceOnGround(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CORRUPTION_DELIVER_ROAD_RESOURCE_ON_GROUND;
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
         failBranch = this.thisMob.getAICorruptionDeliverResourceOnGround();
         failWithResourceBranch = this.thisMob.getAICorruptionDeliverResourceInHand();
         exitBranch = this.thisMob.getAICorruptionDeliverRoadResourceInHand();
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
         this.switchNode(failBranch);
      } else {
         ArrayList<ResourceModule.ResourceType> validResourceDeliveryTypes = new ArrayList<>();
         validResourceDeliveryTypes.add(ResourceModule.ResourceType.WOOD);
         validResourceDeliveryTypes.add(ResourceModule.ResourceType.ROCK);
         ArrayList<OrderedPair> validRoads = new ArrayList<>();

         for (OrderedPair o : roadSelectionListFiltered) {
            for (ResourceModule.ResourceType t : validResourceDeliveryTypes) {
               RoadModule.RoadType roadType = this.road.getRoadType(o.getX(), o.getY());
               if (roadType.getTemplate().getNextRoadType() != null && this.road.getNextRequiredResource(o.getX(), o.getY()) == t) {
                  if (this.thisMob.canReach(o)) {
                     ResourceBase resourceOnGround = this.resource.getResourceOnGround(o.getX(), o.getY());
                     if (resourceOnGround == null) {
                        validRoads.add(o);
                     }
                  }
                  break;
               }
            }
         }

         if (validRoads.size() == 0) {
            this.switchNode(failBranch);
         } else {
            EnumMap<ResourceModule.ResourceType, ArrayList<OrderedPair>> sortedValidRoads = new EnumMap<>(ResourceModule.ResourceType.class);

            for (OrderedPair o : validRoads) {
               ResourceModule.ResourceType tx = this.road.getNextRequiredResource(o.getX(), o.getY());
               if (sortedValidRoads.get(tx) == null) {
                  sortedValidRoads.put(tx, new ArrayList<>());
               }

               sortedValidRoads.get(tx).add(o);
            }

            int tileX = this.thisMob.getTileX();
            int tileY = this.thisMob.getTileY();
            ArrayList<ResourceBase> resourcesOnGround = this.resource
               .getResourcesNearby(this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getCorruptionWorkNearbyResourcesOnGroundDistance());
            if (resourcesOnGround.size() > 0) {
               resourcesOnGround = this.resource
                  .cullResourcesByDistance(
                     tileX,
                     tileY,
                     this.thisMobFlags.getCorruptionWorkNearbyResourcesOnGroundDistance() * 2,
                     resourcesOnGround,
                     this.thisMobFlags.getBlockMapGroup()
                  );
            }

            if (resourcesOnGround.size() == 0) {
               this.switchNode(failBranch);
            } else {
               ArrayList<ResourceBase> validResources = new ArrayList<>();

               for (ResourceBase r : resourcesOnGround) {
                  if (this.thisMob.canReach(r.getTileX(), r.getTileY())
                     && !r.isAssigned()
                     && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
                     && sortedValidRoads.get(r.getType()) != null) {
                     validResources.add(r);
                  }
               }

               if (validResources.size() == 0) {
                  this.switchNode(failBranch);
               } else {
                  OrderedPair nearestResourceCoordinates = this.pathFinder
                     .searchForResource(tileX, tileY, validResources, false, true, this.thisMobFlags.getBlockMapGroup());
                  if (nearestResourceCoordinates == null) {
                     this.switchNode(failBranch);
                  } else {
                     ResourceModule.ResourceType resourceTypeOnGround = this.resource
                        .getResourceTypeOnGround(nearestResourceCoordinates.getX(), nearestResourceCoordinates.getY());
                     OrderedPair deliveryCoordinatesOut = this.pathFinder
                        .searchFor(
                           nearestResourceCoordinates.getX(),
                           nearestResourceCoordinates.getY(),
                           sortedValidRoads.get(resourceTypeOnGround),
                           false,
                           true,
                           this.thisMobFlags.getBlockMapGroup()
                        );
                     Assignment assignment = this.thisMob.getAssignment();
                     assignment.reset();
                     assignment.setPrimaryCoordinates(nearestResourceCoordinates);
                     assignment.setSecondaryCoordinates(deliveryCoordinatesOut);
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
      this.thisMob.setCurrentTask(Text.getText("aiTask.corruptionDeliverRoadResourceOnGround.pathfindToResource"));
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
      OrderedPair deliveryCoordinates = this.thisMob.getAssignment().getSecondaryCoordinates();
      if (this.thisMob.canReach(deliveryCoordinates) && this.resource.getResourceTypeOnGround(tileX, tileY) != null) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.addCarriedResource(this.resource.getResourceOnGround(tileX, tileY));
            this.thisMob.getAssignment().removeAndGetFirstPickupResources();
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 3;
         }
      } else {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.aiStep = 0;
      }
   }

   private void liftResource() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      OrderedPair deliveryCoordinates = assignment.getSecondaryCoordinates();
      if (deliveryCoordinates != null && this.thisMob.canReach(deliveryCoordinates)) {
         this.thisMob.setPositionLift();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.switchNode(exitBranch);
            assignment.setSecondaryCoordinates(deliveryCoordinates);
            assignment.setAssignmentType(Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND);
            assignment.finalizeAssignment();
         }
      } else {
         this.switchNode(failWithResourceBranch);
      }
   }
}
