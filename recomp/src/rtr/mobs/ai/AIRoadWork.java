package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.map.WorkSelectionModule;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIRoadWork extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_ROAD = 1;
   private static final int BUILD_ROAD = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIRoadWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_ROAD_WORK;
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
         failBranch = this.thisMob.getAIBuildWork();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findWork();
                  break;
               case 1:
                  this.pathfindToRoad();
                  break;
               case 2:
                  this.buildRoad();
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
         if (!job.canWorkerBuildRoad()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<OrderedPair> roadWorkList = new ArrayList<>();
            roadWorkList.addAll(this.workSelection.getRoadSelectionMapArray().keySet());
            if (roadWorkList.size() == 0) {
               this.switchNode(failBranch);
            } else {
               roadWorkList = OrderedPair.sortPairsDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), roadWorkList);
               byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
               int i = 0;

               int checkX;
               int checkY;
               ResourceBase resourceOnGround;
               while (true) {
                  if (i >= roadWorkList.size()) {
                     this.switchNode(failBranch);
                     return;
                  }

                  checkX = roadWorkList.get(i).getX();
                  checkY = roadWorkList.get(i).getY();
                  if ((!this.thisMob.getMobFlags().canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isTileThreatened(checkX, checkY))
                     && this.thisMob.canReach(checkX, checkY, true)
                     && (
                        blockMap[checkX][checkY - 1] == 0
                           || blockMap[checkX - 1][checkY] == 0
                           || blockMap[checkX + 1][checkY] == 0
                           || blockMap[checkX][checkY + 1] == 0
                     )
                     && !this.mob.isWorkAssigned(checkX, checkY, Assignment.AssignmentType.BUILDING_ROAD)) {
                     ResourceModule.ResourceType requiredResource = this.road.getNextRequiredResource(checkX, checkY);
                     resourceOnGround = null;
                     if (requiredResource == null) {
                        break;
                     }

                     if (job.canWorkerBuildRoad(requiredResource)) {
                        resourceOnGround = this.resource.getResourceOnGround(checkX, checkY);
                        if (resourceOnGround != null && resourceOnGround.getType() == requiredResource && !resourceOnGround.isAssigned()) {
                           break;
                        }
                     }
                  }

                  i++;
               }

               ArrayList<OrderedPair> validLocations = new ArrayList<>();
               if (this.thisMob.canReach(checkX, checkY - 1, true)) {
                  validLocations.add(OrderedPair.getOrderedPair(checkX, checkY - 1));
               }

               if (this.thisMob.canReach(checkX, checkY + 1, true)) {
                  validLocations.add(OrderedPair.getOrderedPair(checkX, checkY + 1));
               }

               if (this.thisMob.canReach(checkX - 1, checkY, true)) {
                  validLocations.add(OrderedPair.getOrderedPair(checkX - 1, checkY));
               }

               if (this.thisMob.canReach(checkX + 1, checkY, true)) {
                  validLocations.add(OrderedPair.getOrderedPair(checkX + 1, checkY));
               }

               if (this.thisMob.canReach(checkX, checkY, true)) {
                  validLocations.add(OrderedPair.getOrderedPair(checkX, checkY));
               }

               Collections.shuffle(validLocations);
               Assignment assignment = this.thisMob.getAssignment();
               assignment.reset();
               assignment.setPrimaryCoordinates(validLocations.get(0));
               assignment.setSecondaryCoordinates(roadWorkList.get(i));
               if (resourceOnGround != null) {
                  assignment.addPickupResources(resourceOnGround);
               }

               assignment.setAssignmentType(Assignment.AssignmentType.BUILDING_ROAD);
               assignment.finalizeAssignment();
               this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
               this.aiStep = 1;
            }
         }
      }
   }

   private void pathfindToRoad() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.roadWork.pathfindToRoad"));
      this.runPath();
      if (this.thisMob.getMobFlags().canIgnoreThreatenedTiles()
         && this.map.getMapThreatMap().isTileThreatened(this.thisMob.getAssignment().getPrimaryCoordinates())) {
         this.switchNode(resetBranch);
      } else if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void buildRoad() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.roadWork.buildRoad"));
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(resetBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         if (this.workSelection.getSelectionMap()[assignment.getSecondaryX()][assignment.getSecondaryY()]
               == WorkSelectionModule.SelectionType.BUILD_ROAD.ordinal()
            && assignment.getPrimaryX() == tileX
            && assignment.getPrimaryY() == tileY) {
            ResourceBase resourceAssigned = null;
            if (this.thisMob.getAssignment().getPickupResources().size() > 0) {
               resourceAssigned = this.thisMob.getAssignment().getFirstPickupResources();
            }

            if (resourceAssigned != null) {
               if (resourceAssigned.getTileX() != assignment.getSecondaryX() && resourceAssigned.getTileY() != assignment.getSecondaryY()
                  || resourceAssigned.getAssigned() != this.thisMob) {
                  this.switchNode(resetBranch);
                  return;
               }
            } else {
               ResourceModule.ResourceType requiredResource = this.road.getNextRequiredResource(assignment.getSecondaryX(), assignment.getSecondaryY());
               ResourceBase resourceOnGround = null;
               if (requiredResource != null) {
                  resourceOnGround = this.resource.getResourceOnGround(assignment.getSecondaryX(), assignment.getSecondaryY());
                  if (resourceOnGround == null || resourceOnGround.getType() != requiredResource || resourceOnGround.getAssigned() != this.thisMob) {
                     this.switchNode(resetBranch);
                  }

                  return;
               }
            }

            boolean usingTools = this.hasPreferredTools();
            this.thisMob.setPositionInteract(usingTools, true);
            if (assignment.getSecondaryX() == tileX + 1 && assignment.getSecondaryY() == tileY) {
               this.thisMob.setPosition(MobBase.MobPosition.EAST);
            } else if (assignment.getSecondaryX() == tileX - 1 && assignment.getSecondaryY() == tileY) {
               this.thisMob.setPosition(MobBase.MobPosition.WEST);
            } else if (assignment.getSecondaryX() == tileX && assignment.getSecondaryY() == tileY + 1) {
               this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
            } else if (assignment.getSecondaryX() == tileX && assignment.getSecondaryY() == tileY - 1) {
               this.thisMob.setPosition(MobBase.MobPosition.NORTH);
            }

            this.thisMob.runWorkDelayTick(1);
            this.thisMob.doWork(1, 1, 1, 1, 0);
            if (this.thisMob.getWorkDelayTick() % 35 == 0) {
               float attackX = assignment.getSecondaryX() * 16 + 8;
               float attackY = assignment.getSecondaryY() * 16 + 8;
               double xDiff = this.thisMob.getCenterPixelX(true) - attackX;
               double yDiff = this.thisMob.getCenterPixelY(true) - attackY;
               float angle = (float)Math.toDegrees(Math.atan2(yDiff, xDiff));
               int randomParticles = Utilities.randomInt(2) + 1;

               for (int x = 0; x < randomParticles; x++) {
                  this.particle
                     .newParticleGroup(
                        attackX * 16.0F + Utilities.randomInt(16),
                        attackY * 16.0F + Utilities.randomInt(16),
                        angle,
                        ParticleModule.ParticleGroup.DUST,
                        ParticleModule.ParticleSet.BLACK
                     );
                  this.particle
                     .newParticleGroup(
                        attackX * 16.0F + Utilities.randomInt(8) + 2.0F,
                        (attackY - 1.0F) * 16.0F + Utilities.randomInt(8) + 2.0F,
                        ParticleModule.ParticleGroup.SMOKE_BELLOW,
                        ParticleModule.ParticleSet.GRAY
                     );
               }
            }

            if (this.thisMob.getWorkDelayTick() > this.mobJob.getCalculatedWorkTime(this.thisMob)) {
               if (usingTools) {
                  this.thisMob.getInventoryToolSlot().decreaseUses(1);
               }

               this.thisMob.awardXP(Utilities.randomInt(5) + 5);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.BUILDING_ROADS);
               }

               if (resourceAssigned != null) {
                  this.resource.deleteResource(resourceAssigned);
               }

               OrderedPair o = assignment.getSecondaryCoordinates();
               this.road.upgradeRoad(o.getX(), o.getY());
               this.switchNode(resetBranch);
            }
         } else {
            this.switchNode(resetBranch);
         }
      }
   }
}
