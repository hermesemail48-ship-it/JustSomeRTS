package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.resources.ResourceBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;

public class AIClearResourcesFromRoadWork extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_RESOURCE = 1;
   private static final int PICK_UP_RESOURCE = 2;
   private static final int LIFT_RESOURCE = 3;
   private static final int DROP_RESOURCE = 4;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIClearResourcesFromRoadWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CLEAR_RESOURCES_FROM_ROAD_WORK;
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
         resetBranch = this.thisMob.getAIStart();
         failBranch = this.thisMob.getAIConstructionHarvestWork();
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
               break;
            case 4:
               this.dropResource();
         }
      }
   }

   private void findWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canRepairRoad()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<OrderedPair> clearResourceList = new ArrayList<>();
            clearResourceList.addAll(this.road.getRoadClearResourceList());
            if (clearResourceList.size() == 0) {
               this.switchNode(failBranch);
            } else {
               clearResourceList = OrderedPair.sortPairsDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), clearResourceList);

               for (int i = 0; i < clearResourceList.size(); i++) {
                  int checkX = clearResourceList.get(i).getX();
                  int checkY = clearResourceList.get(i).getY();
                  if ((!this.thisMob.getMobFlags().canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isTileThreatened(checkX, checkY))
                     && this.thisMob.canReach(checkX, checkY)
                     && this.resource.getResourceOnGround(checkX, checkY) != null
                     && !this.resource.getResourceOnGround(checkX, checkY).isAssigned()
                     && !this.mob.isWorkAssigned(checkX, checkY, false, true, Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND)) {
                     Assignment assignment = this.thisMob.getAssignment();
                     assignment.reset();
                     assignment.setPrimaryCoordinates(clearResourceList.get(i));
                     assignment.addPickupResources(this.resource.getResourceOnGround(clearResourceList.get(i).getX(), clearResourceList.get(i).getY()));
                     assignment.setAssignmentType(Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND);
                     assignment.finalizeAssignment();
                     this.requestPath(clearResourceList.get(i));
                     this.aiStep = 1;
                     return;
                  }
               }

               this.switchNode(failBranch);
            }
         }
      }
   }

   private void pathfindToResource() throws SlickException {
      this.runPath();
      ResourceBase currentResource = this.thisMob.getAssignment().getFirstPickupResources();
      OrderedPair o = this.thisMob.getAssignment().getPrimaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (!this.pathFailed
         && r != null
         && r == currentResource
         && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
         && !this.road.isResourceNeededForRoad(r)
         && this.thisMob.getAssignment().containsPickupResources(r)) {
         Text.setVariableText("resourceName", r.getType().getTemplate().getName());
         this.thisMob.setCurrentTask(Text.getText("aiTask.clearResourcesFromRoad.pathfindToResource"));
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void pickUpResource() throws SlickException {
      ResourceBase currentResource = this.thisMob.getAssignment().getFirstPickupResources();
      OrderedPair o = this.thisMob.getAssignment().getPrimaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (r != null && r == currentResource) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.addCarriedResource(r);
            this.road.updateClearedResourceStatus(o.getX(), o.getY());
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 3;
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void liftResource() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.aiStep = 4;
      }
   }

   private void dropResource() throws SlickException {
      if (this.runDropResource(true)) {
         this.switchNode(resetBranch);
      }
   }
}
