package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIDismantleWork extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_BUILDING = 1;
   private static final int DISMANTLE_BUILDING = 2;
   private static final int CONTINUE_DISMANTLE_WORK = 3;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIDismantleWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_DISMANTLE_WORK;
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
         failBranch = this.thisMob.getAIClearTopographyFromRoadWork();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findWork();
                  break;
               case 1:
                  this.pathfindToBuilding();
                  break;
               case 2:
                  this.dismantle();
                  break;
               case 3:
                  this.continueDismantleWork();
            }
         }
      }
   }

   private void findWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canDismantle()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
            ArrayList<MobBase> mobArray = this.mob.getMobsByAssignment(Assignment.AssignmentType.DISMANTLING, false);

            for (ObjectBase objectCheck : objectList) {
               if (objectCheck.canBeDismantle() && this.thisMob.canReach(objectCheck, true)) {
                  int workerCount = 0;

                  for (MobBase m : mobArray) {
                     if (m.getAssignment() != null && m.getAssignment().getPrimaryObject() == objectCheck) {
                        workerCount++;
                     }
                  }

                  if (workerCount <= 8 && objectCheck.getTotalValue() + objectCheck.getTotalResourcesCount() - workerCount > 0) {
                     OrderedPair randomCoordinates = objectCheck.getRandomCoordinates(this.thisMob, true, true);
                     Assignment assignment = this.thisMob.getAssignment();
                     assignment.reset();
                     assignment.setPrimaryObject(objectCheck);
                     assignment.setPrimaryCoordinates(randomCoordinates);
                     assignment.setAssignmentType(Assignment.AssignmentType.DISMANTLING);
                     assignment.finalizeAssignment();
                     this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
                     this.aiStep = 1;
                     return;
                  }
               }
            }

            this.switchNode(failBranch);
         }
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.dismantleWork.pathfindToBuilding"));
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
               this.switchNode(resetBranch);
            }

            return;
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void dismantle() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.dismantleWork.dismantle"));
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(resetBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         int workX = assignment.getPrimaryX();
         int workY = assignment.getPrimaryY();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         ObjectBase currentObject = assignment.getPrimaryObject();
         if (currentObject == null || currentObject.isDestroyed()) {
            this.switchNode(resetBranch);
         } else if (workX == tileX && workY == tileY) {
            byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
            boolean usingTools = this.hasPreferredTools();
            boolean foundSurface = false;
            if (blockMap[tileX + 1][tileY] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(tileX + 1, tileY)) {
               this.thisMob.setPosition(MobBase.MobPosition.EAST);
               foundSurface = true;
            } else if (blockMap[tileX - 1][tileY] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(tileX - 1, tileY)) {
               this.thisMob.setPosition(MobBase.MobPosition.WEST);
               foundSurface = true;
            } else if (blockMap[tileX][tileY + 1] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(tileX, tileY + 1)) {
               this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
               foundSurface = true;
            } else if (blockMap[tileX][tileY - 1] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(tileX, tileY - 1)) {
               this.thisMob.setPosition(MobBase.MobPosition.NORTH);
               foundSurface = true;
            }

            this.thisMob.setPositionInteract(usingTools, !foundSurface);
            if (currentObject.getTotalResourcesCount() > 0 && currentObject.canBeDismantle()) {
               ResourceModule.ResourceType[] var14;
               for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
                  if (currentObject.getResourceCount(type) > 0 && currentObject.isResourceAvailable(type)) {
                     int carryMax = this.thisMob.getCarryMax();
                     int availableResources = currentObject.getAvailableResourceCount(type, false);
                     int pickupAmount = availableResources > carryMax ? carryMax : availableResources;

                     for (int i = 0; i < pickupAmount; i++) {
                        this.thisMob.addCarriedResource(currentObject.takeStores(type), true);
                     }

                     return;
                  }
               }
            }

            this.thisMob.runWorkDelayTick(1);
            this.thisMob.doWork(1, 1, 1, 1, 0);
            if (this.thisMob.getWorkDelayTick() % 50 == 0) {
               this.sound.playSound(SoundModule.SoundType.BUILDING, this.thisMob.getMobX(), this.thisMob.getMobY());
            }

            if (this.thisMob.getWorkDelayTick() % 35 == 0) {
               int attackX = assignment.getPrimaryX();
               int attackY = assignment.getPrimaryY();
               if (blockMap[attackX + 1][attackY] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(attackX + 1, attackY)) {
                  attackX++;
               } else if (blockMap[attackX - 1][attackY] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(attackX - 1, attackY)) {
                  attackX--;
               } else if (blockMap[attackX][attackY + 1] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(attackX, attackY + 1)) {
                  attackY++;
               } else if (blockMap[attackX][attackY - 1] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(attackX, attackY - 1)) {
                  attackY--;
               }

               double xDiff = this.thisMob.getCenterPixelX(true) - attackX * 16 + 8;
               double yDiff = this.thisMob.getCenterPixelY(true) - attackY * 16 + 8;
               float angle = (float)Math.toDegrees(Math.atan2(yDiff, xDiff));
               int randomParticles = Utilities.randomInt(2) + 1;

               for (int x = 0; x < randomParticles; x++) {
                  this.particle
                     .newParticleGroup(
                        this.thisMob.getCenterPixelX(true),
                        this.thisMob.getCenterPixelY(true),
                        angle,
                        ParticleModule.ParticleGroup.DUST,
                        ParticleModule.ParticleSet.BLACK
                     );
                  this.particle
                     .newParticleGroup(
                        this.thisMob.getCenterPixelX(true),
                        this.thisMob.getCenterPixelY(true),
                        ParticleModule.ParticleGroup.SMOKE_BELLOW,
                        ParticleModule.ParticleSet.GRAY
                     );
               }
            }

            if (this.thisMob.getWorkDelayTick() > this.mobJob.getCalculatedWorkTime(this.thisMob)
               && currentObject.getTotalValue() > 0
               && currentObject.canBeDismantle()) {
               if (usingTools) {
                  this.thisMob.getInventoryToolSlot().decreaseUses(1);
               }

               this.thisMob.awardXP(Utilities.randomInt(5) + 5);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.DISMANTLING_BUILDING);
               }

               ResourceModule.ResourceType dismantleWith = this.resource.getRandomResourceType();

               while (currentObject.getResourceValueCount(dismantleWith) <= 0) {
                  dismantleWith = this.resource.getRandomResourceType();
               }

               this.thisMob.addCarriedResource(currentObject.dismantle(dismantleWith, true, 3), true);
               this.thisMob.resetWorkDelayTick();
               this.aiStep = 3;
            }
         } else {
            this.switchNode(resetBranch);
         }
      }
   }

   public void continueDismantleWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canDismantle()) {
            this.switchNode(failBranch);
         } else {
            Assignment assignment = this.thisMob.getAssignment();
            if (assignment == null) {
               this.switchNode(failBranch);
            } else {
               ObjectBase currentBuilding = this.thisMob.getAssignment().getPrimaryObject();
               if (currentBuilding != null && !currentBuilding.isDestroyed()) {
                  ArrayList<MobBase> mobArray = this.mob.getMobsByAssignment(Assignment.AssignmentType.DISMANTLING, false);
                  if (!currentBuilding.canBeDismantle()) {
                     this.switchNode(failBranch);
                  } else if (!this.thisMob.canReach(currentBuilding, true)) {
                     this.switchNode(failBranch);
                  } else {
                     int workerCount = 0;

                     for (MobBase m : mobArray) {
                        if (m != this.thisMob && m.getAssignment() != null && m.getAssignment().getPrimaryObject() == currentBuilding) {
                           workerCount++;
                        }
                     }

                     if (workerCount > 8) {
                        this.switchNode(failBranch);
                     } else if (currentBuilding.getTotalValue() + currentBuilding.getTotalResourcesCount() - workerCount <= 0) {
                        this.switchNode(failBranch);
                     } else {
                        assignment.setPrimaryCoordinates(currentBuilding.getRandomCoordinates(this.thisMob, true, true));
                        this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
                        this.aiStep = 1;
                     }
                  }
               } else {
                  this.switchNode(failBranch);
               }
            }
         }
      }
   }
}
