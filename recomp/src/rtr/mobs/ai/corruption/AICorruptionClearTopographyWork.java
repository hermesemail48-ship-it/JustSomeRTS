package rtr.mobs.ai.corruption;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.gui.states.BrushLoader;
import rtr.map.MapModule;
import rtr.mobs.MobBase;
import rtr.mobs.ai.AIBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AICorruptionClearTopographyWork extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_TOPOGRAPHY = 1;
   private static final int CLEAR_TOPOGRAPHY = 2;
   private static AIBase resetBranch;

   public AICorruptionClearTopographyWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CORRUPTION_CLEAR_TOPOGRAPHY_WORK;
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
         if (this.runDropResource()) {
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
      ArrayList<OrderedPair> clearList = new ArrayList<>();
      ArrayList<OrderedPair> rawClearList = new ArrayList<>();
      rawClearList.addAll(this.corruption.getCorruptionTilesArray());
      if (rawClearList.size() == 0) {
         this.switchNode(resetBranch);
      } else {
         for (OrderedPair o : rawClearList) {
            if (this.map.canClearAtCoord(o.getX(), o.getY())
               && (
                  this.resource.getResourceValues()[o.getX()][o.getY()] <= 0
                     || this.resource.getResourceType()[o.getX()][o.getY()] == ResourceModule.ResourceType.DIRTY_WATER_BUCKET
               )) {
               clearList.add(o);
            }
         }

         if (clearList.size() == 0) {
            this.switchNode(resetBranch);
         } else {
            clearList = OrderedPair.sortPairsDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), clearList);
            byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());

            for (int i = 0; i < clearList.size(); i++) {
               int checkX = clearList.get(i).getX();
               int checkY = clearList.get(i).getY();
               if ((
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

            this.switchNode(resetBranch);
         }
      }
   }

   private void pathfindToTopography() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.corruptionClearTopographyWork.pathfindToTopography"));
      this.runPath();
      if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void clearTopography() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.corruptionClearTopographyWork.clearTopography"));
      Assignment assignment = this.thisMob.getAssignment();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      if (assignment.getPrimaryX() == tileX
         && assignment.getPrimaryY() == tileY
         && this.map.canClearAtCoord(assignment.getSecondaryX(), assignment.getSecondaryY())
         && (
            this.resource.getResourceValues()[assignment.getSecondaryX()][assignment.getSecondaryY()] <= 0
               || this.resource.getResourceType()[assignment.getSecondaryX()][assignment.getSecondaryY()] == ResourceModule.ResourceType.DIRTY_WATER_BUCKET
         )) {
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

         if (this.thisMob.getWorkDelayTick() > this.thisMobFlags.getCorruptionWorkTime()) {
            if (usingTools) {
               this.thisMob.getInventoryToolSlot().decreaseUses(1);
            }

            this.thisMob.awardXP(Utilities.randomInt(5) + 5);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.01, GodModule.GodXPType.CLEARING_TOPOGRAPHY);
            }

            int x = assignment.getSecondaryX();
            int y = assignment.getSecondaryY();
            if (this.resource.getResourceType()[x][y] == ResourceModule.ResourceType.DIRTY_WATER_BUCKET) {
               this.resource.getResourceValues()[x][y] = 0;
               this.map.erase(x, y, MapModule.EraseMode.TOPOGRAPHY, 1, BrushLoader.BrushType.ROUND);
            } else if (Utilities.randomInt(15) == 0 && this.resource.getResourceValues()[x][y] > 0) {
               this.thisMob.addCarriedResource(this.resource.harvestResource(x, y, 2), true);
            } else {
               this.resource.damageResource(x, y);
            }

            this.thisMob.resetWorkDelayTick();
         }
      } else {
         this.switchNode(resetBranch);
      }
   }
}
