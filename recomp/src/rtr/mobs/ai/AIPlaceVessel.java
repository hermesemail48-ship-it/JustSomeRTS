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

public class AIPlaceVessel extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_RESOURCE_ON_GROUND = 1;
   private static final int PICK_UP_RESOURCE_ON_GROUND = 2;
   private static final int LIFT_RESOURCE = 3;
   private static AIBase failBranch;
   private static AIBase exitBranch;

   public AIPlaceVessel(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_PLACE_VESSEL;
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
         failBranch = this.thisMob.getAICaptureAnimals();
         exitBranch = this.thisMob.getAIPlaceVesselInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToResourceOnGround();
               break;
            case 2:
               this.pickUpResourceOnGround();
               break;
            case 3:
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
            int totalVessels = this.resource.getResourceTotal(ResourceModule.ResourceType.EERIE_VESSEL, false);
            if (totalVessels == 0) {
               this.switchNode(failBranch);
            } else {
               Assignment assignment = this.thisMob.getAssignment();
               assignment.reset();
               ArrayList<OrderedPair> allVesselSpots = new ArrayList<>();

               for (ObjectBase o : this.object.getObjectMasterBuildingPriorityList()) {
                  if (o.getVesselPoints().size() != 0 && this.thisMob.canReach(o, true)) {
                     allVesselSpots.addAll(o.getVesselPoints());
                  }
               }

               if (allVesselSpots.size() == 0) {
                  this.switchNode(failBranch);
               } else {
                  ArrayList<MobBase> allOccultistsEnroute = this.mob.getMobsByAssignment(Assignment.AssignmentType.PLACING_VESSEL, false);
                  ArrayList<OrderedPair> validVesselSpots = new ArrayList<>();
                  ArrayList<ResourceBase> vesselsInSpots = new ArrayList<>();

                  label76:
                  for (OrderedPair ox : allVesselSpots) {
                     if (this.thisMob.canReach(ox, true)) {
                        ResourceBase vesselInSpot = this.resource.getResourceOnGround(ox.getX(), ox.getY());
                        if (vesselInSpot != null) {
                           vesselsInSpots.add(vesselInSpot);
                        } else {
                           for (MobBase m : allOccultistsEnroute) {
                              if (m != this.thisMob && m.getAssignment().getSecondaryCoordinates() == ox) {
                                 continue label76;
                              }
                           }

                           validVesselSpots.add(ox);
                        }
                     }
                  }

                  if (validVesselSpots.size() == 0) {
                     this.switchNode(failBranch);
                  } else {
                     ArrayList<ResourceBase> validVessels = AIUtilities.createAvailabeResourceList(
                        this.thisMob, ResourceModule.ResourceType.EERIE_VESSEL, true, this.thisMobFlags.getWorkNearbyResourcesOnGroundDistance()
                     );
                     validVessels.removeAll(vesselsInSpots);
                     if (validVessels.size() == 0) {
                        this.switchNode(failBranch);
                     } else {
                        ArrayList<OrderedPair> coordinatesList = AIUtilities.createAvailabeResourceCoordinatesList(this.thisMob, null, validVessels);
                        OrderedPair closestVesselAnywhere = this.pathFinder
                           .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), coordinatesList, false, true, this.thisMobFlags.getBlockMapGroup());
                        OrderedPair vesselSpotCoordinates = this.pathFinder
                           .searchFor(
                              closestVesselAnywhere.getX(), closestVesselAnywhere.getY(), validVesselSpots, false, true, this.thisMobFlags.getBlockMapGroup()
                           );
                        ResourceBase pickupVessel = this.resource.getResourceOnGround(closestVesselAnywhere);
                        if (pickupVessel != null && pickupVessel.getType() == ResourceModule.ResourceType.EERIE_VESSEL && validVessels.contains(pickupVessel)) {
                           assignment.setPrimaryCoordinates(closestVesselAnywhere);
                           assignment.setSecondaryCoordinates(vesselSpotCoordinates);
                           assignment.addPickupResources(pickupVessel);
                           assignment.setPickupResourceType(pickupVessel.getType());
                           assignment.setAssignmentType(Assignment.AssignmentType.PLACING_VESSEL);
                           assignment.finalizeAssignment();
                           this.requestPath(closestVesselAnywhere);
                           this.aiStep = 1;
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
      this.thisMob.setCurrentTask(Text.getText("aiTask.placeVessel.pathfindToResourceOnGround"));
      this.runPath();
      OrderedPair o = this.thisMob.getAssignment().getPrimaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (!this.pathFailed
         && r != null
         && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
         && !this.road.isResourceNeededForRoad(r)
         && this.thisMob.getAssignment().containsPickupResources(r)) {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void pickUpResourceOnGround() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      OrderedPair deliveryCoordinates = this.thisMob.getAssignment().getSecondaryCoordinates();
      if (this.thisMob.canReach(deliveryCoordinates, true) && this.resource.getResourceTypeOnGround(tileX, tileY) != null) {
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
      OrderedPair plantingCoordinates = assignment.getSecondaryCoordinates();
      if (plantingCoordinates != null && this.thisMob.canReach(plantingCoordinates, true)) {
         this.thisMob.setPositionLift();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.switchNode(exitBranch);
            assignment.setSecondaryCoordinates(plantingCoordinates);
            assignment.setAssignmentType(Assignment.AssignmentType.PLACING_VESSEL);
            assignment.finalizeAssignment();
         }
      } else {
         this.switchNode(exitBranch);
      }
   }
}
