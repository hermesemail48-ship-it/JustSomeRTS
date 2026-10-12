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
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIBuildWork extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_BUILDING = 1;
   private static final int BUILD = 2;
   private static final int MAXIMUM_WORKERS = 6;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIBuildWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_BUILD_WORK;
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
         failBranch = this.thisMob.getAIClearResourcesFromRoadWork();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findWork();
                  break;
               case 1:
                  this.pathfindToBuilding();
                  break;
               case 2:
                  this.build();
            }
         }
      }
   }

   private void findWork() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      if (assignment.getPrimaryObject() != null) {
         this.findWorkWithBuilding();
      } else {
         this.findWorkNewBuilding();
      }
   }

   private void findWorkWithBuilding() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canWorkerBuild()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<ResourceModule.ResourceType> canBuildResources = new ArrayList<>();
            ObjectBase objectCheck = this.thisMob.getAssignment().getPrimaryObject();
            if (!this.thisMob.canReach(objectCheck, true)) {
               this.switchNode(failBranch);
            } else {
               if (objectCheck.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.CONSTRUCTION) {
                  int startX = objectCheck.getObjectX();
                  int startY = objectCheck.getObjectY();
                  int endX = objectCheck.getObjectX() + objectCheck.getObjectFlags().getWidth();
                  int endY = objectCheck.getObjectY() + objectCheck.getObjectFlags().getHeight();

                  for (int x = startX; x < endX; x++) {
                     for (int y = startY; y < endY; y++) {
                        if (this.map.canClearAtCoord(x, y)) {
                           this.switchNode(failBranch);
                           return;
                        }
                     }
                  }
               }

               ResourceModule.ResourceType[] var17;
               for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
                  if (job.canWorkerBuild(type)
                     && !objectCheck.isPaused()
                     && objectCheck.getObjectFlags().canBuildingBeBuilt(type)
                     && objectCheck.getResourceValueCount(type) < objectCheck.getObjectFlags().getResourceValueBaseCount(type)
                     && objectCheck.isResourceAvailable(type)) {
                     canBuildResources.add(type);
                  }
               }

               if (canBuildResources.size() == 0) {
                  this.switchNode(failBranch);
               } else {
                  for (ResourceModule.ResourceType typex : canBuildResources) {
                     if (!objectCheck.isPaused()
                        && objectCheck.getObjectFlags().canBuildingBeBuilt(typex)
                        && objectCheck.getResourceValueCount(typex) < objectCheck.getObjectFlags().getResourceValueBaseCount(typex)
                        && objectCheck.isResourceAvailable(typex)
                        && !this.workerCountTooHigh(objectCheck)) {
                        OrderedPair randomCoordinates = objectCheck.getRandomCoordinates(this.thisMob, true, true);
                        Assignment assignment = this.thisMob.getAssignment();
                        assignment.reset();
                        assignment.setPrimaryObject(objectCheck);
                        assignment.setPrimaryCoordinates(randomCoordinates);
                        assignment.setAssignmentType(Assignment.AssignmentType.BUILDING);
                        assignment.finalizeAssignment();
                        this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
                        this.aiStep = 1;
                        return;
                     }
                  }

                  this.switchNode(failBranch);
               }
            }
         }
      }
   }

   private void findWorkNewBuilding() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canWorkerBuild()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<ResourceModule.ResourceType> canBuildResources = new ArrayList<>();
            ArrayList<ObjectBase> objectList = new ArrayList<>();
            objectList.addAll(this.object.getObjectMasterBuildingPriorityList());
            ArrayList<ObjectBase> removeList = new ArrayList<>();

            label119:
            for (ObjectBase o : objectList) {
               if (o.isPaused()) {
                  removeList.add(o);
               } else if (!o.getObjectFlags().canBuildingBeBuilt()) {
                  removeList.add(o);
               } else if (!this.thisMob.canReach(o, true)) {
                  removeList.add(o);
               } else if (o.getObjectFlags().getSubType() == ObjectBase.ObjectSubType.CONSTRUCTION) {
                  int startX = o.getObjectX();
                  int startY = o.getObjectY();
                  int endX = o.getObjectX() + o.getObjectFlags().getWidth();
                  int endY = o.getObjectY() + o.getObjectFlags().getHeight();

                  for (int x = startX; x < endX; x++) {
                     for (int y = startY; y < endY; y++) {
                        if (this.map.canClearAtCoord(x, y)) {
                           removeList.add(o);
                           continue label119;
                        }
                     }
                  }
               }
            }

            objectList.removeAll(removeList);
            if (objectList.size() == 0) {
               this.switchNode(failBranch);
            } else {
               ResourceModule.ResourceType[] var20;
               for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
                  if (job.canWorkerBuild(type)) {
                     for (ObjectBase ox : objectList) {
                        if (ox.getObjectFlags().canBuildingBeBuilt(type)
                           && ox.getResourceValueCount(type) < ox.getObjectFlags().getResourceValueBaseCount(type)
                           && ox.isResourceAvailable(type)) {
                           canBuildResources.add(type);
                           break;
                        }
                     }
                  }
               }

               if (canBuildResources.size() == 0) {
                  this.switchNode(failBranch);
               } else {
                  for (ObjectBase oxx : objectList) {
                     for (ResourceModule.ResourceType typex : canBuildResources) {
                        if (oxx.getObjectFlags().canBuildingBeBuilt(typex)
                           && oxx.getResourceValueCount(typex) < oxx.getObjectFlags().getResourceValueBaseCount(typex)
                           && oxx.isResourceAvailable(typex)) {
                           if (!this.workerCountTooHigh(oxx)) {
                              OrderedPair randomCoordinates = oxx.getRandomCoordinates(this.thisMob, true, true);
                              Assignment assignment = this.thisMob.getAssignment();
                              assignment.reset();
                              assignment.setPrimaryObject(oxx);
                              assignment.setPrimaryCoordinates(randomCoordinates);
                              assignment.setAssignmentType(Assignment.AssignmentType.BUILDING);
                              assignment.finalizeAssignment();
                              this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
                              this.aiStep = 1;
                              return;
                           }
                           break;
                        }
                     }
                  }

                  this.switchNode(failBranch);
               }
            }
         }
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.buildWork.pathfindToBuilding"));
      this.runPath();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject != null && !currentObject.isDestroyed()) {
         ResourceBase buildWith = null;
         MobJobBase job = this.thisMob.getMobJobType().getJob();

         ResourceModule.ResourceType[] var7;
         for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
            if (job.canWorkerBuild(type)
               && !currentObject.isPaused()
               && currentObject.getObjectFlags().canBuildingBeBuilt(type)
               && currentObject.getResourceValueCount(type) < currentObject.getObjectFlags().getResourceValueBaseCount(type)
               && currentObject.isResourceAvailable(type)) {
               buildWith = currentObject.getAvailableResource(type);
               break;
            }
         }

         if (buildWith == null) {
            this.switchNode(resetBranch);
         } else {
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
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void build() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.buildWork.build"));
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
         if (currentObject != null && !currentObject.isDestroyed()) {
            ResourceBase buildWith = null;
            MobJobBase job = this.thisMob.getMobJobType().getJob();

            ResourceModule.ResourceType[] possibleTrash;
            for (ResourceModule.ResourceType type : possibleTrash = ResourceModule.ResourceType.values()) {
               if (job.canWorkerBuild(type)
                  && !currentObject.isPaused()
                  && currentObject.getObjectFlags().canBuildingBeBuilt(type)
                  && currentObject.getResourceValueCount(type) < currentObject.getObjectFlags().getResourceValueBaseCount(type)
                  && currentObject.isResourceAvailable(type)) {
                  buildWith = currentObject.getAvailableResource(type);
                  break;
               }
            }

            if (buildWith == null) {
               this.switchNode(resetBranch);
            } else if (workX == tileX && workY == tileY) {
               byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
               boolean foundSurface = false;
               if (blockMap[tileX + 1][tileY] > 0 && currentObject.isBuildingAtLocation(tileX + 1, tileY)) {
                  this.thisMob.setPosition(MobBase.MobPosition.EAST);
                  foundSurface = true;
               } else if (blockMap[tileX - 1][tileY] > 0 && currentObject.isBuildingAtLocation(tileX - 1, tileY)) {
                  this.thisMob.setPosition(MobBase.MobPosition.WEST);
                  foundSurface = true;
               } else if (blockMap[tileX][tileY + 1] > 0 && currentObject.isBuildingAtLocation(tileX, tileY + 1)) {
                  this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
                  foundSurface = true;
               } else if (blockMap[tileX][tileY - 1] > 0 && currentObject.isBuildingAtLocation(tileX, tileY - 1)) {
                  this.thisMob.setPosition(MobBase.MobPosition.NORTH);
                  foundSurface = true;
               }

               boolean usingTools = this.hasPreferredTools();
               this.thisMob.setPositionInteract(usingTools, !foundSurface);
               this.thisMob.runWorkDelayTick(1);
               this.thisMob.doWork(1, 1, 1, 1, 0);
               if (this.thisMob.getWorkDelayTick() % 75 == 0) {
                  this.sound.playSound(SoundModule.SoundType.BUILDING, this.thisMob.getMobX(), this.thisMob.getMobY());
               }

               if (this.thisMob.getWorkDelayTick() % 35 == 0) {
                  int attackX = assignment.getPrimaryX();
                  int attackY = assignment.getPrimaryY();
                  if (blockMap[attackX + 1][attackY] > 0 && currentObject.isBuildingAtLocation(attackX + 1, attackY)) {
                     attackX++;
                  } else if (blockMap[attackX - 1][attackY] > 0 && currentObject.isBuildingAtLocation(attackX - 1, attackY)) {
                     attackX--;
                  } else if (blockMap[attackX][attackY + 1] > 0 && currentObject.isBuildingAtLocation(attackX, attackY + 1)) {
                     attackY++;
                  } else if (blockMap[attackX][attackY - 1] > 0 && currentObject.isBuildingAtLocation(attackX, attackY - 1)) {
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
                  ResourceBase possibleTrashx = null;
                  if (!currentObject.isPaused() && currentObject.getTotalResourcesCount() > 0 && currentObject.getObjectFlags().canBuildingBeBuilt()) {
                     if (usingTools) {
                        this.thisMob.getInventoryToolSlot().decreaseUses(1);
                     }

                     this.thisMob.awardXP(Utilities.randomInt(5) + 5);
                     if (this.thisMobFlags.getAllowGodExperience()) {
                        this.god.increaseGodXP(0.01, GodModule.GodXPType.BUILDING);
                     }

                     if (buildWith != null) {
                        possibleTrashx = currentObject.build(buildWith, true, 20);
                     }
                  }

                  this.thisMob.resetWorkDelayTick();
                  if (buildWith != null && possibleTrashx == null && this.continueBuildWork()) {
                     this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
                     this.aiStep = 1;
                  } else {
                     if (possibleTrashx != null) {
                        this.thisMob.addCarriedResource(possibleTrashx, true);
                     }

                     this.switchNode(resetBranch);
                  }
               }
            } else {
               this.switchNode(resetBranch);
            }
         } else {
            this.switchNode(resetBranch);
         }
      }
   }

   public boolean continueBuildWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         return false;
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canWorkerBuild()) {
            return false;
         } else {
            ArrayList<ResourceModule.ResourceType> canBuildResources = new ArrayList<>();
            ObjectBase currentBuilding = this.thisMob.getAssignment().getPrimaryObject();
            if (currentBuilding != null && !currentBuilding.isDestroyed()) {
               ResourceModule.ResourceType[] assignment;
               for (ResourceModule.ResourceType type : assignment = ResourceModule.ResourceType.values()) {
                  if (job.canWorkerBuild(type)
                     && !currentBuilding.isPaused()
                     && currentBuilding.getObjectFlags().canBuildingBeBuilt(type)
                     && this.thisMob.canReach(currentBuilding, true)
                     && currentBuilding.getResourceValueCount(type) < currentBuilding.getObjectFlags().getResourceValueBaseCount(type)
                     && currentBuilding.isResourceAvailable(type)) {
                     canBuildResources.add(type);
                  }
               }

               if (canBuildResources.size() == 0) {
                  return false;
               } else {
                  for (ResourceModule.ResourceType typex : canBuildResources) {
                     if (!currentBuilding.isPaused()) {
                        if (!this.thisMob.canReach(currentBuilding, true)) {
                           return false;
                        }

                        if (currentBuilding.getObjectFlags().canBuildingBeBuilt(typex)
                           && currentBuilding.getResourceValueCount(typex) < currentBuilding.getObjectFlags().getResourceValueBaseCount(typex)
                           && currentBuilding.isResourceAvailable(typex)) {
                           if (this.workerCountTooHigh(currentBuilding)) {
                              return false;
                           }

                           OrderedPair randomCoordinates = currentBuilding.getRandomCoordinates(this.thisMob, true, true);
                           Assignment assignmentx = this.thisMob.getAssignment();
                           assignmentx.reset();
                           assignmentx.setPrimaryObject(currentBuilding);
                           assignmentx.setPrimaryCoordinates(randomCoordinates);
                           assignmentx.setAssignmentType(Assignment.AssignmentType.BUILDING);
                           assignmentx.finalizeAssignment();
                           return true;
                        }
                     }
                  }

                  return false;
               }
            } else {
               return false;
            }
         }
      }
   }

   private boolean workerCountTooHigh(ObjectBase o) {
      ArrayList<MobBase> mobArray = this.mob.getMobsByAssignment(Assignment.AssignmentType.BUILDING, false);
      int workerCount = 0;
      int workerCountOnSite = 0;
      int totalResourcesAvailable = 0;

      label83:
      for (MobBase m : mobArray) {
         if (m.getAssignment() != null && m.getAssignment().getPrimaryObject() == o) {
            workerCount++;
            if (this.object.getBuildingAtLocation(m.getTileX(), m.getTileY()) == o) {
               workerCountOnSite++;
            } else {
               for (int x = -1; x < 2; x++) {
                  for (int y = -1; y < 2; y++) {
                     if ((x != 0 || y != 0) && this.object.getBuildingAtLocation(m.getTileX() + x, m.getTileY() + y) == o) {
                        workerCountOnSite++;
                        continue label83;
                     }
                  }
               }
            }
         }
      }

      MobJobBase job = this.thisMob.getMobJobType().getJob();

      ResourceModule.ResourceType[] var10;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         if (job.canWorkerBuild(type) && !o.isPaused() && o.getObjectFlags().canBuildingBeBuilt(type) && o.isResourceAvailable(type)) {
            totalResourcesAvailable += o.getResourceCount(type);
         }
      }

      int totalResourcesNeededLeft = o.getObjectFlags().getTotalValueBase() - o.getTotalValue();
      return workerCount > 6 || workerCountOnSite > totalResourcesNeededLeft || workerCountOnSite > totalResourcesAvailable;
   }
}
