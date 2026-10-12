package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIPlaceVesselInHand extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_BUILDING = 1;
   private static final int PLANT_CROP = 2;
   private static final int DROP_OFF_LIFT = 3;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIPlaceVesselInHand(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_PLANT_RESOURCE_IN_HAND;
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
         failBranch = this.thisMob.getAISellResourceInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToBuilding();
               break;
            case 2:
               this.placeVessel();
               break;
            case 3:
               this.dropOffLift();
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
         } else if (this.thisMob.getCarriedResourcesListType() != ResourceModule.ResourceType.EERIE_VESSEL) {
            this.switchNode(failBranch);
         } else {
            Assignment assignment = this.thisMob.getAssignment();
            OrderedPair vesselCoordinates = assignment.getSecondaryCoordinates();
            if (vesselCoordinates == null) {
               ArrayList<OrderedPair> allVesselSpots = new ArrayList<>();

               for (ObjectBase o : this.object.getObjectMasterBuildingPriorityList()) {
                  if (o.getVesselPoints().size() != 0 && this.thisMob.canReach(o, true)) {
                     allVesselSpots.addAll(o.getVesselPoints());
                  }
               }

               if (allVesselSpots.size() == 0) {
                  this.switchNode(failBranch);
                  return;
               }

               ArrayList<MobBase> allOccultistsEnroute = this.mob.getMobsByAssignment(Assignment.AssignmentType.PLACING_VESSEL, false);
               ArrayList<OrderedPair> validVesselSpots = new ArrayList<>();

               label65:
               for (OrderedPair ox : allVesselSpots) {
                  if (this.thisMob.canReach(ox, true)) {
                     ResourceBase vesselInSpot = this.resource.getResourceOnGround(ox.getX(), ox.getY());
                     if (vesselInSpot == null) {
                        for (MobBase m : allOccultistsEnroute) {
                           if (m != this.thisMob && m.getAssignment().getSecondaryCoordinates() == ox) {
                              continue label65;
                           }
                        }

                        validVesselSpots.add(ox);
                     }
                  }
               }

               if (validVesselSpots.size() == 0) {
                  this.switchNode(failBranch);
                  return;
               }

               vesselCoordinates = this.pathFinder
                  .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), validVesselSpots, false, true, this.thisMobFlags.getBlockMapGroup());
            }

            assignment.reset();
            assignment.setSecondaryCoordinates(vesselCoordinates);
            assignment.setAssignmentType(Assignment.AssignmentType.PLACING_VESSEL);
            assignment.finalizeAssignment();
            this.requestPath(vesselCoordinates);
            this.aiStep = 1;
         }
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("resourceName", this.thisMob.getCarriedResourcesListType().getTemplate().getName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.placeVesselInHand.pathfindToBuilding"));
      this.runPath();
      if (this.pathFailed) {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      } else if (this.pathCompleted) {
         this.aiStep = 2;
      }
   }

   private void placeVessel() throws SlickException {
      OrderedPair coordinates = this.thisMob.getAssignment().getSecondaryCoordinates();
      int workX = coordinates.getX();
      int workY = coordinates.getY();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      if (workX == tileX && workY == tileY) {
         this.thisMob.setPositionDrop();
         if (this.thisMob.getSprite().isStopped()) {
            this.resource.setResourcesOnGround(workX, workY, this.thisMob.removeFirstCarriedResource());
            this.thisMob.awardXP(Utilities.randomInt(10) + 5);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.01, GodModule.GodXPType.CAPTURING_SOULS);
            }

            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 3;
         }
      } else {
         if (this.thisMob.hasCarriedResources()) {
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.thisMob.getAssignment().reset();
            this.aiStep = 0;
         } else {
            this.switchNode(failBranch);
         }
      }
   }

   private void dropOffLift() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         if (this.thisMob.hasCarriedResources()) {
            this.thisMob.getAssignment().reset();
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionFace();
            this.aiStep = 0;
         } else {
            this.switchNode(resetBranch);
         }
      }
   }
}
