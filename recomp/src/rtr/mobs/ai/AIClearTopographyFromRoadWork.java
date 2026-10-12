package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.gui.states.BrushLoader;
import rtr.map.MapModule;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIClearTopographyFromRoadWork extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_TOPOGRAPHY = 1;
   private static final int CLEAR_TOPOGRAPHY = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIClearTopographyFromRoadWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CLEAR_TOPOGRAPHY_FROM_ROAD_WORK;
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
         failBranch = this.thisMob.getAIClearTopographyWork();
         if (this.thisMob.hasCarriedResources()) {
            this.runDropResource();
         } else {
            switch (this.aiStep) {
               case 0:
                  this.findWork();
                  break;
               case 1:
                  this.pathfindToTopography();
                  break;
               case 2:
                  this.clearTopography();
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
         if (!job.canRepairRoad()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<OrderedPair> clearList = new ArrayList<>();
            clearList.addAll(this.road.getRoadClearTopographyList());
            if (clearList.size() == 0) {
               this.switchNode(failBranch);
            } else {
               clearList = OrderedPair.sortPairsDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), clearList);
               byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());

               for (int i = 0; i < clearList.size(); i++) {
                  int checkX = clearList.get(i).getX();
                  int checkY = clearList.get(i).getY();
                  if ((!this.thisMob.getMobFlags().canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isTileThreatened(checkX, checkY))
                     && (
                        blockMap[checkX][checkY - 1] == 0
                           || blockMap[checkX - 1][checkY] == 0
                           || blockMap[checkX + 1][checkY] == 0
                           || blockMap[checkX][checkY + 1] == 0
                     )
                     && !this.mob.isWorkAssigned(checkX, checkY, false, true, Assignment.AssignmentType.CLEARING)) {
                     ArrayList<OrderedPair> nearbyCoordinates = new ArrayList<>();
                     if (this.thisMob.canReach(checkX, checkY - 1)) {
                        nearbyCoordinates.add(OrderedPair.getOrderedPair(checkX, checkY - 1));
                     }

                     if (this.thisMob.canReach(checkX, checkY + 1)) {
                        nearbyCoordinates.add(OrderedPair.getOrderedPair(checkX, checkY + 1));
                     }

                     if (this.thisMob.canReach(checkX - 1, checkY)) {
                        nearbyCoordinates.add(OrderedPair.getOrderedPair(checkX - 1, checkY));
                     }

                     if (this.thisMob.canReach(checkX + 1, checkY)) {
                        nearbyCoordinates.add(OrderedPair.getOrderedPair(checkX + 1, checkY));
                     }

                     if (nearbyCoordinates.size() != 0) {
                        Collections.shuffle(nearbyCoordinates);
                        Assignment assignment = this.thisMob.getAssignment();
                        assignment.reset();
                        assignment.setPrimaryCoordinates(nearbyCoordinates.get(0));
                        assignment.setSecondaryCoordinates(clearList.get(i));
                        assignment.setAssignmentType(Assignment.AssignmentType.CLEARING);
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
   }

   private void pathfindToTopography() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.clearTopographyFromRoadWork.pathfindToTopography"));
      this.runPath();
      Assignment assignment = this.thisMob.getAssignment();
      if (this.thisMob.getMobFlags().canIgnoreThreatenedTiles()
         && this.map.getMapThreatMap().isTileThreatened(assignment.getSecondaryX(), assignment.getSecondaryY())) {
         this.switchNode(resetBranch);
      } else if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void clearTopography() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.clearTopographyFromRoadWork.clearTopography"));
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(resetBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         if (this.road.getRoadClearTopographyList().contains(assignment.getSecondaryCoordinates())
            && assignment.getPrimaryX() == tileX
            && assignment.getPrimaryY() == tileY) {
            byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
            boolean usingTools = this.hasPreferredTools();
            boolean foundSurface = false;
            if (blockMap[assignment.getSecondaryX()][assignment.getSecondaryY()] > 0) {
               foundSurface = true;
            }

            this.thisMob.setPositionInteract(usingTools, !foundSurface);
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
               int attackX = assignment.getSecondaryX();
               int attackY = assignment.getSecondaryY();
               double xDiff = this.thisMob.getCenterPixelX(true) - attackX * 16 - 8;
               double yDiff = this.thisMob.getCenterPixelY(true) - attackY * 16 - 8;
               float angle = (float)Math.toDegrees(Math.atan2(yDiff, xDiff));
               int randomParticles = Utilities.randomInt(2) + 1;

               for (int x = 0; x < randomParticles; x++) {
                  this.particle
                     .newParticleGroup(
                        attackX * 16 + Utilities.randomInt(16),
                        attackY * 16 + Utilities.randomInt(16),
                        angle,
                        ParticleModule.ParticleGroup.DUST,
                        ParticleModule.ParticleSet.BLACK
                     );
                  this.particle
                     .newParticleGroup(
                        attackX * 16 + Utilities.randomInt(8) + 2,
                        (attackY - 1) * 16 + Utilities.randomInt(8) + 2,
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
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.CLEARING_ROADS);
               }

               int x = assignment.getSecondaryX();
               int y = assignment.getSecondaryY();
               if (this.resource.getResourceType()[x][y] == ResourceModule.ResourceType.DIRTY_WATER_BUCKET) {
                  this.resource.getResourceValues()[x][y] = 0;
                  this.map.erase(x, y, MapModule.EraseMode.TOPOGRAPHY, 1, BrushLoader.BrushType.ROUND);
               } else if (Utilities.randomInt(15) == 0 && this.resource.getResourceValues()[x][y] > 0) {
                  this.thisMob.addCarriedResource(this.resource.harvestResource(x, y, 2), true);
                  this.goal.incrementGoal(GoalModule.GoalType.THE_GRIND);
                  ResourceModule.ResourceType harvestedType = this.thisMob.getCarriedResourcesListType();
                  if (harvestedType == ResourceModule.ResourceType.CRYSTAL) {
                     this.goal.incrementGoal(GoalModule.GoalType.KYLE_POWERED);
                  }

                  if (harvestedType == ResourceModule.ResourceType.WOOD) {
                     this.goal.incrementGoal(GoalModule.GoalType.DEFORESTATION);
                  }

                  if (harvestedType == ResourceModule.ResourceType.ROCK) {
                     this.goal.incrementGoal(GoalModule.GoalType.QUARRY);
                  }

                  if (harvestedType == ResourceModule.ResourceType.RAW_VEGETABLE) {
                     this.goal.incrementGoal(GoalModule.GoalType.FORAGING);
                  }
               } else {
                  this.resource.damageResource(x, y);
               }

               this.road.updateClearedTopographyStatus(x, y);
               this.thisMob.resetWorkDelayTick();
            }
         } else {
            this.switchNode(resetBranch);
         }
      }
   }
}
