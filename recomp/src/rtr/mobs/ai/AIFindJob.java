package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;

public class AIFindJob extends AIBase {
   private static final int FIND_JOB = 0;
   private static final int PATHFIND_TO_JOB = 1;
   private static final int TAKE_JOB = 2;
   private static AIBase resetBranch;
   private static AIBase exitBranch;

   public AIFindJob(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_FIND_JOB;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowWork()
         && this.thisMob.getMobJobType() == MobJobBase.MobJobType.NONE
         && this.mobJob.getTotalCurrentWorkers(true) < this.mobJob.getTotalDesiredWorkers();
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
         exitBranch = this.thisMob.getAIAttemptWork();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findJob();
                  break;
               case 1:
                  this.pathfindToJob();
                  break;
               case 2:
                  this.takeJob();
            }
         }
      }
   }

   private void findJob() throws SlickException {
      if (this.mobJob.getTotalCurrentWorkers(true) >= this.mobJob.getTotalDesiredWorkers()) {
         this.switchNode(resetBranch);
      } else {
         MobJobBase.MobJobType jobType = this.mobJob.getHighestDemandJobType();
         ArrayList<ObjectBase> objectList = this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.PROVIDES_JOB_SLOTS);
         ArrayList<ObjectBase> validObjects = new ArrayList<>();

         for (ObjectBase objectCheck : objectList) {
            if (objectCheck.getObjectFlags().getWorkerJobType() == jobType && this.thisMob.canReach(objectCheck, true)) {
               validObjects.add(objectCheck);
            }
         }

         if (validObjects.size() == 0) {
            this.switchNode(resetBranch);
         } else {
            OrderedPair closestObjectCoordinates = this.pathFinder
               .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), validObjects, false, true, this.thisMobFlags.getBlockMapGroup());
            ObjectBase closestObject = this.object.getBuildingAtLocation(closestObjectCoordinates.getX(), closestObjectCoordinates.getY());
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            assignment.setPrimaryObject(closestObject);
            assignment.setPrimaryCoordinates(closestObject.getBestRandomInteractCoordinates(this.thisMob, true, true, true));
            assignment.setMobJobType(jobType);
            assignment.setAssignmentType(Assignment.AssignmentType.GET_JOB);
            assignment.finalizeAssignment();
            this.mobJob.calculateTotalWorkers();
            this.mobJob.calculateJobDemand();
            this.requestPath(assignment.getPrimaryCoordinates());
            this.aiStep = 1;
         }
      }
   }

   private void pathfindToJob() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.findJob.pathfindToJob"));
      this.runPath();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject != null && !currentObject.isDestroyed()) {
         if (this.pathCompleted) {
            this.aiStep = 2;
         } else if (this.pathFailed || !this.thisMob.canReach(this.goalCoordinates, true)) {
            if (this.thisMob.canReach(currentObject, true)) {
               this.thisMob.getAssignment().setPrimaryCoordinates(currentObject.getBestRandomInteractCoordinates(this.thisMob, true, true, true));
               this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
            } else {
               this.aiStep = 0;
            }

            return;
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void takeJob() throws SlickException {
      OrderedPair coordinates = this.thisMob.getAssignment().getPrimaryCoordinates();
      int workX = coordinates.getX();
      int workY = coordinates.getY();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject == null || currentObject.isDestroyed()) {
         this.switchNode(resetBranch);
      } else if (workX == tileX && workY == tileY) {
         this.thisMob.setMobJobType(currentObject.getObjectFlags().getWorkerJobType());
         this.font.newFloater("iconWorkers", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
         this.switchNode(exitBranch);
      } else {
         this.aiStep = 0;
      }
   }
}
