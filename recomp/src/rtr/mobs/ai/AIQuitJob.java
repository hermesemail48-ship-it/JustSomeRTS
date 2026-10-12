package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;

public class AIQuitJob extends AIBase {
   private static final int FIND_WORK_BUILDING = 0;
   private static final int PATHFIND_TO_WORK_BUILDING = 1;
   private static final int QUIT_JOB = 2;
   private static AIBase resetBranch;

   public AIQuitJob(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_QUIT_JOB;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowWork()
            && this.thisMob.getMobJobType() != MobJobBase.MobJobType.NONE
            && this.mobJob.getCurrentWorkers(this.thisMob.getMobJobType(), true) > this.mobJob.getDesiredWorkers(this.thisMob.getMobJobType())
         || !this.thisMobFlags.getAllowWork() && this.thisMob.getMobJobType() != MobJobBase.MobJobType.NONE;
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
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findWorkBuilding();
                  break;
               case 1:
                  this.pathfindToWorkBuilding();
                  break;
               case 2:
                  this.quitJob();
            }
         }
      }
   }

   private void findWorkBuilding() throws SlickException {
      if (this.mobJob.getCurrentWorkers(this.thisMob.getMobJobType(), true) <= this.mobJob.getDesiredWorkers(this.thisMob.getMobJobType())) {
         this.switchNode(resetBranch);
      } else {
         MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
         ArrayList<ObjectBase> objectList = this.object.getObjectsByJob(jobType);
         ArrayList<ObjectBase> validObjects = new ArrayList<>();

         for (ObjectBase objectCheck : objectList) {
            if (objectCheck.getObjectFlags().getWorkerJobType() == jobType && this.thisMob.canReach(objectCheck, true)) {
               validObjects.add(objectCheck);
            }
         }

         if (validObjects.size() == 0) {
            for (ObjectBase objectCheckx : this.object.getObjectMasterBuildingPriorityList()) {
               if (objectCheckx.getObjectFlags().getBaseType() == MapTilesLoader.TileSet.CASTLE_1 && this.thisMob.canReach(objectCheckx, true)) {
                  validObjects.add(objectCheckx);
               }
            }
         }

         if (validObjects.size() == 0) {
            this.thisMob.setMobJobType(MobJobBase.MobJobType.NONE);
            this.font.newFloater("iconWorkers", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            this.switchNode(resetBranch);
         } else {
            OrderedPair closestObjectCoordinates = this.pathFinder
               .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), validObjects, false, true, this.thisMobFlags.getBlockMapGroup());
            ObjectBase closestObject = this.object.getBuildingAtLocation(closestObjectCoordinates.getX(), closestObjectCoordinates.getY());
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            assignment.setPrimaryObject(closestObject);
            assignment.setPrimaryCoordinates(closestObject.getBestRandomInteractCoordinates(this.thisMob, true, true, true));
            assignment.setMobJobType(this.thisMob.getMobJobType());
            assignment.setAssignmentType(Assignment.AssignmentType.QUIT_JOB);
            assignment.finalizeAssignment();
            this.mobJob.calculateTotalWorkers();
            this.mobJob.calculateJobDemand();
            this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
            this.aiStep = 1;
         }
      }
   }

   private void pathfindToWorkBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.quitJob.pathfindToWorkBuilding"));
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
         this.aiStep = 2;
      }
   }

   private void quitJob() throws SlickException {
      OrderedPair coordinates = this.thisMob.getAssignment().getPrimaryCoordinates();
      int workX = coordinates.getX();
      int workY = coordinates.getY();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject == null || currentObject.isDestroyed()) {
         this.aiStep = 0;
      } else if (workX == tileX && workY == tileY) {
         this.thisMob.setMobJobType(MobJobBase.MobJobType.NONE);
         this.font.newFloater("iconWorkers", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
         this.switchNode(resetBranch);
      } else {
         this.aiStep = 0;
      }
   }
}
