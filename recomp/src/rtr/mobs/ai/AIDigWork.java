package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.gui.states.BrushLoader;
import rtr.map.MapTilesLoader;
import rtr.map.WorkSelectionModule;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIDigWork extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_TERRAIN = 1;
   private static final int DIG = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIDigWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_DIG_WORK;
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
         failBranch = this.thisMob.getAIRepairRoadWork();
         if (this.thisMob.hasCarriedResources()) {
            this.runDropResource();
         } else {
            switch (this.aiStep) {
               case 0:
                  this.findWork();
                  break;
               case 1:
                  this.pathfindToTerrain();
                  break;
               case 2:
                  this.dig();
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
         if (!job.canDigHoles()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<OrderedPair> digList = new ArrayList<>();
            digList.addAll(this.workSelection.getDigSelectionMapArray());
            if (digList.size() == 0) {
               this.switchNode(failBranch);
            } else {
               digList = OrderedPair.sortPairsDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), digList);
               byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());

               for (int i = 0; i < digList.size(); i++) {
                  int checkX = digList.get(i).getX();
                  int checkY = digList.get(i).getY();
                  if ((!this.thisMob.getMobFlags().canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isTileThreatened(checkX, checkY))
                     && (
                        blockMap[checkX][checkY - 1] == 0
                           || blockMap[checkX - 1][checkY] == 0
                           || blockMap[checkX + 1][checkY] == 0
                           || blockMap[checkX][checkY + 1] == 0
                     )
                     && !this.mob.isWorkAssigned(checkX, checkY, false, true, Assignment.AssignmentType.DIGGING)) {
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
                        assignment.setSecondaryCoordinates(digList.get(i));
                        assignment.setAssignmentType(Assignment.AssignmentType.DIGGING);
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

   private void pathfindToTerrain() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.digWork.pathfindToTerrain"));
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

   private void dig() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.digWork.dig"));
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(resetBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         if (this.workSelection.getSelectionMap()[assignment.getSecondaryX()][assignment.getSecondaryY()] == WorkSelectionModule.SelectionType.DIG.ordinal()
            && assignment.getPrimaryX() == tileX
            && assignment.getPrimaryY() == tileY) {
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
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.DIGGING_HOLES);
               }

               if (this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                  this.goal.incrementGoal(GoalModule.GoalType.DIGGY_DIGGY_HOLE);
               }

               this.map.drawToMap(assignment.getSecondaryX(), assignment.getSecondaryY(), MapTilesLoader.TileSet.WATER_DRY, 1, BrushLoader.BrushType.ROUND);
               this.thisMob.resetWorkDelayTick();
            }
         } else {
            this.switchNode(resetBranch);
         }
      }
   }
}
