package rtr.mobs.ai.corruption;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.ai.AIBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AICorruptionBuildWork extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_BUILDING = 1;
   private static final int BUILD = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AICorruptionBuildWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CORRUPTION_BUILD_WORK;
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
         resetBranch = this.thisMob.getAIStart();
         failBranch = this.thisMob.getAICorruptionDeliverRoadResourceOnGround();
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
      ArrayList<ResourceModule.ResourceType> canBuildResources = new ArrayList<>();
      ArrayList<ObjectBase> objectList = this.object.getObjectsByGroup(MobBase.MobGroup.MONSTER);
      ArrayList<ObjectBase> removeList = new ArrayList<>();

      label164:
      for (ObjectBase o : objectList) {
         if (o.isPaused()) {
            removeList.add(o);
         } else if (!o.getObjectFlags().canBuildingBeBuilt()) {
            removeList.add(o);
         } else if (!this.thisMob.canReach(o)) {
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
                     continue label164;
                  }
               }
            }
         }
      }

      objectList.removeAll(removeList);
      if (objectList.size() == 0) {
         this.switchNode(failBranch);
      } else {
         ResourceModule.ResourceType[] var19;
         for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
            for (ObjectBase objectCheck : objectList) {
               if (objectCheck.getObjectFlags().canBuildingBeBuilt(type) && objectCheck.isOnCorruption()) {
                  int assignedResources = 0;

                  for (ResourceBase r : objectCheck.getStorageResources(type)) {
                     if (r.isAssigned() && r.getAssigned().getAssignment().getAssignmentType() == Assignment.AssignmentType.BUILDING) {
                        assignedResources++;
                     }
                  }

                  if (objectCheck.getResourceValueCount(type) + assignedResources < objectCheck.getObjectFlags().getResourceValueBaseCount(type)
                     && objectCheck.isResourceAvailable(type)) {
                     canBuildResources.add(type);
                     break;
                  }
               }
            }
         }

         if (canBuildResources.size() == 0) {
            this.switchNode(failBranch);
         } else {
            ArrayList<MobBase> mobArray = this.mob.getMobsByAssignment(Assignment.AssignmentType.BUILDING, false);

            for (ObjectBase objectCheckx : objectList) {
               for (ResourceModule.ResourceType type : canBuildResources) {
                  if (!objectCheckx.isOnCorruption()) {
                     break;
                  }

                  if (objectCheckx.getObjectFlags().canBuildingBeBuilt(type)) {
                     int assignedResources = 0;

                     for (ResourceBase rx : objectCheckx.getStorageResources(type)) {
                        if (rx.isAssigned() && rx.getAssigned().getAssignment().getAssignmentType() == Assignment.AssignmentType.BUILDING) {
                           assignedResources++;
                        }
                     }

                     if (objectCheckx.getResourceValueCount(type) + assignedResources < objectCheckx.getObjectFlags().getResourceValueBaseCount(type)
                        && objectCheckx.isResourceAvailable(type)) {
                        int workerCount = 0;

                        for (MobBase m : mobArray) {
                           if (m.getAssignment() != null && m.getAssignment().getPrimaryObject() == objectCheckx) {
                              workerCount++;
                           }
                        }

                        if (workerCount <= 16) {
                           OrderedPair randomCoordinates = objectCheckx.getRandomCoordinates(this.thisMob, true, false);
                           Assignment assignment = this.thisMob.getAssignment();
                           assignment.reset();
                           assignment.setPrimaryObject(objectCheckx);
                           assignment.setPrimaryCoordinates(randomCoordinates);
                           assignment.addPickupResources(objectCheckx.getAvailableResource(type));
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
            }

            this.switchNode(failBranch);
         }
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.corruptionBuildWork.pathfindToBuilding"));
      this.runPath();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject != null && !currentObject.isDestroyed()) {
         if (this.pathCompleted) {
            this.aiStep = 2;
         } else if (this.pathFailed || !this.thisMob.canReach(this.goalCoordinates)) {
            if (this.thisMob.canReach(currentObject)) {
               this.thisMob.getAssignment().setPrimaryCoordinates(currentObject.getBestRandomInteractCoordinates(this.thisMob, true, false, true));
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

   private void build() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.corruptionBuildWork.build"));
      Assignment assignment = this.thisMob.getAssignment();
      int workX = assignment.getPrimaryX();
      int workY = assignment.getPrimaryY();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase currentObject = assignment.getPrimaryObject();
      if (currentObject == null || currentObject.isDestroyed()) {
         this.switchNode(resetBranch);
      } else if (workX == tileX && workY == tileY) {
         boolean usingTools = this.hasPreferredTools();
         this.thisMob.setPositionInteract(usingTools, false);
         byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
         if (blockMap[tileX + 1][tileY] > 0 && currentObject.isBuildingAtLocation(tileX + 1, tileY)) {
            this.thisMob.setPosition(MobBase.MobPosition.EAST);
         } else if (blockMap[tileX - 1][tileY] > 0 && currentObject.isBuildingAtLocation(tileX - 1, tileY)) {
            this.thisMob.setPosition(MobBase.MobPosition.WEST);
         } else if (blockMap[tileX][tileY + 1] > 0 && currentObject.isBuildingAtLocation(tileX, tileY + 1)) {
            this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
         } else if (blockMap[tileX][tileY - 1] > 0 && currentObject.isBuildingAtLocation(tileX, tileY - 1)) {
            this.thisMob.setPosition(MobBase.MobPosition.NORTH);
         }

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

         if (this.thisMob.getWorkDelayTick() > this.thisMobFlags.getCorruptionWorkTime()) {
            ResourceBase possibleTrash = null;
            if (!currentObject.isPaused() && currentObject.getTotalResourcesCount() > 0 && currentObject.getObjectFlags().canBuildingBeBuilt()) {
               if (usingTools) {
                  this.thisMob.getInventoryToolSlot().decreaseUses(1);
               }

               this.thisMob.awardXP(Utilities.randomInt(5) + 5);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.BUILDING);
               }

               ResourceBase buildWith = this.thisMob.getAssignment().removeAndGetFirstPickupResources();
               buildWith.unassign();
               possibleTrash = currentObject.build(buildWith, true, 20);
            }

            this.thisMob.resetWorkDelayTick();
            if (possibleTrash == null && this.continueBuildWork()) {
               this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
               this.aiStep = 1;
            } else {
               if (possibleTrash != null) {
                  this.thisMob.addCarriedResource(possibleTrash, true);
               }

               this.switchNode(resetBranch);
            }
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   public boolean continueBuildWork() throws SlickException {
      ArrayList<ResourceModule.ResourceType> canBuildResources = new ArrayList<>();
      ObjectBase currentBuilding = this.thisMob.getAssignment().getPrimaryObject();
      if (currentBuilding != null && !currentBuilding.isDestroyed()) {
         ResourceModule.ResourceType[] assignedResources;
         for (ResourceModule.ResourceType type : assignedResources = ResourceModule.ResourceType.values()) {
            if (!currentBuilding.isPaused()
               && currentBuilding.getObjectFlags().canBuildingBeBuilt(type)
               && this.thisMob.canReach(currentBuilding)
               && currentBuilding.isOnCorruption()) {
               int assignedResourcesx = 0;

               for (ResourceBase r : currentBuilding.getStorageResources(type)) {
                  if (r.isAssigned() && r.getAssigned().getAssignment().getAssignmentType() == Assignment.AssignmentType.BUILDING) {
                     assignedResourcesx++;
                  }
               }

               if (currentBuilding.getResourceValueCount(type) + assignedResourcesx < currentBuilding.getObjectFlags().getResourceValueBaseCount(type)
                  && currentBuilding.isResourceAvailable(type)) {
                  canBuildResources.add(type);
               }
            }
         }

         if (canBuildResources.size() == 0) {
            return false;
         } else {
            ArrayList<MobBase> mobArray = this.mob.getMobsByAssignment(Assignment.AssignmentType.BUILDING, false);

            for (ResourceModule.ResourceType typex : canBuildResources) {
               if (!currentBuilding.isOnCorruption()) {
                  return false;
               }

               if (currentBuilding.getObjectFlags().canBuildingBeBuilt(typex)) {
                  int assignedResourcesx = 0;

                  for (ResourceBase rx : currentBuilding.getStorageResources(typex)) {
                     if (rx.isAssigned() && rx.getAssigned().getAssignment().getAssignmentType() == Assignment.AssignmentType.BUILDING) {
                        assignedResourcesx++;
                     }
                  }

                  if (currentBuilding.getResourceValueCount(typex) + assignedResourcesx < currentBuilding.getObjectFlags().getResourceValueBaseCount(typex)
                     && currentBuilding.isResourceAvailable(typex)) {
                     int workerCount = 0;

                     for (MobBase m : mobArray) {
                        if (m.getAssignment() != null && m.getAssignment().getPrimaryObject() == currentBuilding) {
                           workerCount++;
                        }
                     }

                     if (workerCount > 16) {
                        return false;
                     }

                     OrderedPair randomCoordinates = currentBuilding.getRandomCoordinates(this.thisMob, true, false);
                     Assignment assignment = this.thisMob.getAssignment();
                     assignment.reset();
                     assignment.setPrimaryObject(currentBuilding);
                     assignment.setPrimaryCoordinates(randomCoordinates);
                     assignment.addPickupResources(currentBuilding.getAvailableResource(typex));
                     assignment.setAssignmentType(Assignment.AssignmentType.BUILDING);
                     assignment.finalizeAssignment();
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
