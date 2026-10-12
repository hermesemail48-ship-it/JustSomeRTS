package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIRepairWork extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_BUILDING = 1;
   private static final int REPAIR_BUILDING = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIRepairWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_REPAIR_WORK;
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
         failBranch = this.thisMob.getAIPray();
         if (this.thisMob.hasCarriedResources()) {
            this.runDropResource();
         } else {
            switch (this.aiStep) {
               case 0:
                  this.findWork();
                  break;
               case 1:
                  this.pathfindToBuilding();
                  break;
               case 2:
                  this.repairBuilding();
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
         if (!job.canRepair()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
            ArrayList<MobBase> mobArray = this.mob.getMobsByAssignment(Assignment.AssignmentType.REPAIRING, false);
            ArrayList<ObjectBase> damagedObjects = new ArrayList<>();

            for (ObjectBase objectCheck : objectList) {
               if ((!this.thisMob.getMobFlags().canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isObjectThreatened(objectCheck))
                  && this.thisMob.canReach(objectCheck, true)
                  && this.thisMob.getFriendlyMobGroups().contains(objectCheck.getObjectFlags().getMobGroup())
                  && objectCheck.getObjectFlags().getSubType() != ObjectBase.ObjectSubType.ABANDONED
                  && (objectCheck.getHitPoints() < objectCheck.getHitPointsMax() || objectCheck.getFireDamageArray().size() > 0)) {
                  int workerCount = 0;

                  for (MobBase m : mobArray) {
                     if (m.getAssignment() != null && m.getAssignment().getPrimaryObject() == objectCheck) {
                        workerCount++;
                     }
                  }

                  if (workerCount < 4
                     && (
                        objectCheck.getHitPoints() + workerCount * 25 < objectCheck.getHitPointsMax()
                           || objectCheck.getFireDamageArray().size() - workerCount > 0
                     )) {
                     damagedObjects.add(objectCheck);
                  }
               }
            }

            if (damagedObjects.size() > 0) {
               Collections.sort(damagedObjects, new Comparator<ObjectBase>() {
                  public int compare(ObjectBase o1, ObjectBase o2) {
                     return o1 == o2 ? 0 : o1.getHitPoints() - o2.getHitPoints();
                  }
               });
               ObjectBase objectCheckx = damagedObjects.get(0);
               OrderedPair randomCoordinates = objectCheckx.getRandomCoordinates(this.thisMob, true, true);
               Assignment assignment = this.thisMob.getAssignment();
               assignment.reset();
               assignment.setPrimaryObject(objectCheckx);
               assignment.setPrimaryCoordinates(randomCoordinates);
               assignment.setAssignmentType(Assignment.AssignmentType.REPAIRING);
               assignment.finalizeAssignment();
               this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
               this.aiStep = 1;
            } else {
               this.switchNode(failBranch);
            }
         }
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.repairWork.pathfindToRoad"));
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject != null && !currentObject.isDestroyed()) {
         if (this.thisMob.getMobFlags().canIgnoreThreatenedTiles() && this.map.getMapThreatMap().isObjectThreatened(currentObject)) {
            this.switchNode(resetBranch);
         } else {
            this.runPath();
            if (this.pathCompleted) {
               this.aiStep = 2;
            } else if (this.pathFailed || !this.thisMob.canReach(this.goalCoordinates, true)) {
               if (currentObject != null && this.thisMob.canReach(currentObject, true)) {
                  this.thisMob.getAssignment().setPrimaryCoordinates(currentObject.getBestRandomInteractCoordinates(this.thisMob, true, true, true));
                  this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
               } else {
                  this.switchNode(resetBranch);
               }

               return;
            }
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void repairBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.repairWork.repairRoad"));
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
            if (currentObject.getHitPoints() >= currentObject.getHitPointsMax() && currentObject.getFireDamageArray().size() <= 0) {
               this.switchNode(resetBranch);
            } else {
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
               this.thisMob.runWorkDelayTick(1);
               this.thisMob.doWork(1, 1, 1, 1, 0);
               if (this.thisMob.getWorkDelayTick() % 75 == 0) {
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

               if (this.thisMob.getWorkDelayTick() > this.mobJob.getCalculatedWorkTime(this.thisMob)) {
                  ObjectBase objectBeingBuilt = assignment.getPrimaryObject();
                  if (objectBeingBuilt.getHitPoints() < objectBeingBuilt.getHitPointsMax()) {
                     if (usingTools) {
                        this.thisMob.getInventoryToolSlot().decreaseUses(1);
                     }

                     this.thisMob.awardXP(Utilities.randomInt(5) + 5);
                     if (this.thisMobFlags.getAllowGodExperience()) {
                        this.god.increaseGodXP(0.01, GodModule.GodXPType.REPAIRING_BUILDINGS);
                     }

                     objectBeingBuilt.repair(25);
                  }

                  this.thisMob.resetWorkDelayTick();
                  if (this.continueRepairWork()) {
                     this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
                     this.aiStep = 1;
                  } else {
                     this.switchNode(resetBranch);
                  }
               }
            }
         } else {
            this.switchNode(resetBranch);
         }
      }
   }

   private boolean continueRepairWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         return false;
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canRepair()) {
            return false;
         } else {
            ObjectBase currentBuilding = this.thisMob.getAssignment().getPrimaryObject();
            if (currentBuilding != null && !currentBuilding.isDestroyed()) {
               ArrayList<MobBase> mobArray = this.mob.getMobsByAssignment(Assignment.AssignmentType.REPAIRING, false);
               if (this.thisMob.getMobFlags().canIgnoreThreatenedTiles() && this.map.getMapThreatMap().isObjectThreatened(currentBuilding)) {
                  return false;
               } else if (!this.thisMob.canReach(currentBuilding, true)) {
                  return false;
               } else if (currentBuilding.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.ABANDONED) {
                  return false;
               } else if (!this.thisMob.getFriendlyMobGroups().contains(currentBuilding.getObjectFlags().getMobGroup())) {
                  return false;
               } else if (currentBuilding.getHitPoints() >= currentBuilding.getHitPointsMax()) {
                  return false;
               } else {
                  int workerCount = 0;

                  for (MobBase m : mobArray) {
                     if (m != this.thisMob && m.getAssignment() != null && m.getAssignment().getPrimaryObject() == currentBuilding) {
                        workerCount++;
                     }
                  }

                  if (workerCount >= 4) {
                     return false;
                  } else if (currentBuilding.getHitPoints() + workerCount * 25 >= currentBuilding.getHitPointsMax()) {
                     return false;
                  } else {
                     this.thisMob.getAssignment().setPrimaryCoordinates(currentBuilding.getRandomCoordinates(this.thisMob, true, true));
                     return true;
                  }
               }
            } else {
               return false;
            }
         }
      }
   }
}
