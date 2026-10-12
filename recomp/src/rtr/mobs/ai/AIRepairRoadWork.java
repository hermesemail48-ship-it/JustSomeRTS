package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIRepairRoadWork extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_ROAD = 1;
   private static final int REPAIR_ROAD = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIRepairRoadWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_REPAIR_ROAD_WORK;
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
         failBranch = this.thisMob.getAIRoadWork();
         if (this.thisMob.hasCarriedResources()) {
            this.runDropResource();
         } else {
            switch (this.aiStep) {
               case 0:
                  this.findWork();
                  break;
               case 1:
                  this.pathfindToRoad();
                  break;
               case 2:
                  this.repairRoad();
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
            ArrayList<OrderedPair> roadRepairList = this.road.getRoadRepairList();
            ArrayList<MobBase> mobArray = this.mob.getMobsByAssignment(Assignment.AssignmentType.REPAIRING_ROAD, false);
            ArrayList<OrderedPair> damagedRoads = new ArrayList<>();

            label78:
            for (OrderedPair o : roadRepairList) {
               if ((!this.thisMob.getMobFlags().canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isTileThreatened(o))
                  && this.thisMob.canReach(o, true)
                  && !this.workSelection.getClearRoadSelectionMapArray().contains(o)) {
                  for (MobBase m : mobArray) {
                     if (m.getAssignment() != null && m.getAssignment().getPrimaryCoordinates() == o) {
                        continue label78;
                     }
                  }

                  damagedRoads.add(o);
               }
            }

            if (damagedRoads.size() > 0) {
               OrderedPair.sortPairsDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), damagedRoads);
               OrderedPair nearestRoad = damagedRoads.get(0);
               int randomX = nearestRoad.getX();
               int randomY = nearestRoad.getY();
               ArrayList<OrderedPair> validLocations = new ArrayList<>();
               if (this.thisMob.canReach(randomX, randomY - 1, true)) {
                  validLocations.add(OrderedPair.getOrderedPair(randomX, randomY - 1));
               }

               if (this.thisMob.canReach(randomX, randomY + 1, true)) {
                  validLocations.add(OrderedPair.getOrderedPair(randomX, randomY + 1));
               }

               if (this.thisMob.canReach(randomX - 1, randomY, true)) {
                  validLocations.add(OrderedPair.getOrderedPair(randomX - 1, randomY));
               }

               if (this.thisMob.canReach(randomX + 1, randomY, true)) {
                  validLocations.add(OrderedPair.getOrderedPair(randomX + 1, randomY));
               }

               if (this.thisMob.canReach(randomX, randomY, true)) {
                  validLocations.add(OrderedPair.getOrderedPair(randomX, randomY));
               }

               Assignment assignment = this.thisMob.getAssignment();
               assignment.reset();
               assignment.setPrimaryCoordinates(nearestRoad);
               assignment.setSecondaryCoordinates(validLocations.get(Utilities.randomInt(validLocations.size())));
               assignment.setAssignmentType(Assignment.AssignmentType.REPAIRING_ROAD);
               assignment.finalizeAssignment();
               this.requestPath(this.thisMob.getAssignment().getSecondaryCoordinates());
               this.aiStep = 1;
            } else {
               this.switchNode(failBranch);
            }
         }
      }
   }

   private void pathfindToRoad() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.repairRoadWork.pathfindToRoad"));
      this.runPath();
      if (this.thisMob.getMobFlags().canIgnoreThreatenedTiles()
         && this.map.getMapThreatMap().isTileThreatened(this.thisMob.getAssignment().getPrimaryCoordinates())) {
         this.switchNode(resetBranch);
      } else if (this.pathCompleted) {
         this.aiStep = 2;
      } else if (this.pathFailed) {
         this.switchNode(resetBranch);
      }
   }

   private void repairRoad() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.repairRoadWork.repairRoad"));
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(resetBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         int workX = assignment.getSecondaryX();
         int workY = assignment.getSecondaryY();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         if (workX == tileX && workY == tileY) {
            boolean usingTools = this.hasPreferredTools();
            this.thisMob.setPositionInteract(usingTools, true);
            int targetX = this.thisMob.getAssignment().getPrimaryCoordinates().getX();
            int targetY = this.thisMob.getAssignment().getPrimaryCoordinates().getY();
            if (tileX < targetX && tileY == targetY) {
               this.thisMob.setPosition(MobBase.MobPosition.EAST);
            } else if (tileX > targetX && tileY == targetY) {
               this.thisMob.setPosition(MobBase.MobPosition.WEST);
            } else if (tileX == targetX && tileY < targetY) {
               this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
            } else if (tileX == targetX && tileY > targetY) {
               this.thisMob.setPosition(MobBase.MobPosition.NORTH);
            }

            this.thisMob.runWorkDelayTick(1);
            this.thisMob.doWork(1, 1, 1, 1, 0);
            if (this.thisMob.getWorkDelayTick() % 75 == 0) {
               this.sound.playSound(SoundModule.SoundType.BUILDING, this.thisMob.getMobX(), this.thisMob.getMobY());
            }

            if (this.thisMob.getWorkDelayTick() % 35 == 0) {
               float attackX = assignment.getPrimaryX() * 16 + 8;
               float attackY = assignment.getPrimaryY() * 16 + 8;
               double xDiff = this.thisMob.getCenterPixelX(true) - attackX;
               double yDiff = this.thisMob.getCenterPixelY(true) - attackY;
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
               if (usingTools) {
                  this.thisMob.getInventoryToolSlot().decreaseUses(1);
               }

               this.thisMob.awardXP(Utilities.randomInt(5) + 5);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.REPAIRING_ROADS);
               }

               this.road.repairRoad(assignment.getPrimaryX(), assignment.getPrimaryY());
               this.switchNode(resetBranch);
            }
         } else {
            this.switchNode(resetBranch);
         }
      }
   }
}
