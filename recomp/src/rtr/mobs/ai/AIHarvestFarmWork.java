package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.FarmlandBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIHarvestFarmWork extends AIBase {
   private static final int GET_WORK = 0;
   private static final int PATHFIND_TO_RESOURCE = 1;
   private static final int HARVEST_RESOURCE = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;
   private static AIBase exitBranch;

   public AIHarvestFarmWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_HARVEST_FARM_WORK;
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
         failBranch = this.thisMob.getAITendFarmsWork();
         exitBranch = this.thisMob.getAICaptureAnimalsInHand();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.getWork();
                  break;
               case 1:
                  this.pathfindToResource();
                  break;
               case 2:
                  this.harvestResource();
            }
         }
      }
   }

   private void getWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType != MobJobBase.MobJobType.FARMER) {
         this.switchNode(failBranch);
      } else {
         boolean canHarvestCrops = false;
         ArrayList<FarmlandBase> farmland = this.resource.getFarmlandArray();
         Collections.shuffle(farmland);
         if ((
               this.resource.getResourceMaintainAmount(ResourceModule.ResourceType.RAW_VEGETABLE) > 0
                  || this.resource.getResourceMakeAmount(ResourceModule.ResourceType.RAW_VEGETABLE) > 0
            )
            && !this.enoughWorkersHarvesting(ResourceModule.ResourceType.RAW_VEGETABLE)) {
            for (FarmlandBase f : farmland) {
               ObjectBase farm = this.object.getBuildingAtLocation(f.getTileX(), f.getTileY());
               if ((!this.thisMob.getMobFlags().canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isObjectThreatened(farm))
                  && f.isGrown()
                  && !farm.isPaused()) {
                  canHarvestCrops = true;
                  break;
               }
            }
         }

         if (!canHarvestCrops) {
            this.switchNode(failBranch);
         } else {
            if (this.thisMob.getMobJobType() == MobJobBase.MobJobType.FARMER
               && (
                  this.resource.getResourceMaintainAmount(ResourceModule.ResourceType.RAW_VEGETABLE) > 0
                     || this.resource.getResourceMakeAmount(ResourceModule.ResourceType.RAW_VEGETABLE) > 0
               )
               && !this.enoughWorkersHarvesting(ResourceModule.ResourceType.RAW_VEGETABLE)) {
               for (FarmlandBase fx : farmland) {
                  ObjectBase farm = this.object.getBuildingAtLocation(fx.getTileX(), fx.getTileY());
                  if ((!this.thisMob.getMobFlags().canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isObjectThreatened(farm))
                     && fx.isGrown()
                     && !farm.isPaused()) {
                     int checkX = fx.getX();
                     int checkY = fx.getY();
                     if (!this.mob.isWorkAssigned(checkX, checkY, Assignment.AssignmentType.HARVESTING) && this.thisMob.canReach(checkX, checkY)) {
                        Assignment assignment = this.thisMob.getAssignment();
                        assignment.reset();
                        assignment.setPrimaryCoordinates(OrderedPair.getOrderedPair(checkX, checkY));
                        assignment.setSecondaryCoordinates(OrderedPair.getOrderedPair(checkX, checkY));
                        assignment.setPickupResourceType(ResourceModule.ResourceType.RAW_VEGETABLE);
                        assignment.setAssignmentType(Assignment.AssignmentType.HARVESTING);
                        assignment.finalizeAssignment();
                        this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
                        this.aiStep = 1;
                        return;
                     }
                  }
               }
            }

            this.switchNode(failBranch);
         }
      }
   }

   private void pathfindToResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.harvestFarmWork.pathfindToResource"));
      this.runPath();
      Assignment assignment = this.thisMob.getAssignment();
      if (!this.pathFailed
         && (
            !this.thisMob.getMobFlags().canIgnoreThreatenedTiles()
               || !this.map.getMapThreatMap().isTileThreatened(assignment.getPrimaryX(), assignment.getPrimaryY())
         )
         && this.resource.isCropGrown(assignment)
         && !this.enoughWorkersHarvesting(this.thisMob.getAssignment().getPickupResourceType())) {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void harvestResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.harvestFarmWork.harvestResource"));
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType != MobJobBase.MobJobType.FARMER) {
         this.switchNode(failBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         if (this.thisMob.getMobFlags().canIgnoreThreatenedTiles()
            && this.map.getMapThreatMap().isTileThreatened(assignment.getPrimaryX(), assignment.getPrimaryY())) {
            this.switchNode(resetBranch);
         } else if (!this.resource.isCropGrown(assignment) || assignment.getPrimaryX() != tileX || assignment.getPrimaryY() != tileY) {
            this.switchNode(resetBranch);
         } else if (this.enoughWorkersHarvesting(this.thisMob.getAssignment().getPickupResourceType())) {
            this.switchNode(failBranch);
         } else {
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
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.FARMING);
               }

               this.thisMob.addCarriedResource(this.resource.harvestCrop(assignment.getSecondaryX(), assignment.getSecondaryY()), true);
               if (this.resource.getResourceMakeAmount(this.thisMob.getAssignment().getPickupResourceType()) != ResourceModule.RESOURCE_INFINITE_AMOUNT) {
                  this.resource.decreaseResourceMakeAmount(this.thisMob.getAssignment().getPickupResourceType(), 1);
               }

               this.thisMob.getAssignment().reset();
               this.thisMob.resetWorkDelayTick();
               ArrayList<ResourceBase> resourcesNearby = this.resource
                  .getResourcesNearby(tileX, tileY, this.thisMobFlags.getWorkNearbyResourcesOnGroundDistance());
               resourcesNearby = this.resource
                  .cullResourcesByDistance(
                     tileX, tileY, this.thisMobFlags.getWorkNearbyResourcesOnGroundDistance(), resourcesNearby, this.thisMobFlags.getBlockMapGroup()
                  );
               int countSameType = 0;

               for (ResourceBase r : resourcesNearby) {
                  if (r.getType() == this.thisMob.getCarriedResourcesListType()) {
                     countSameType++;
                  }
               }

               countSameType /= 6;
               if (countSameType < 1) {
                  countSameType = 1;
               }

               if (Utilities.randomInt(countSameType) == 0) {
                  this.aiStep = 0;
               } else {
                  this.switchNode(exitBranch);
               }
            }
         }
      }
   }

   private boolean enoughWorkersHarvesting(ResourceModule.ResourceType resourceType) {
      if (this.resource.getResourceMakeAmount(resourceType) != ResourceModule.RESOURCE_INFINITE_AMOUNT
         && this.resource.getResourceMaintainAmount(resourceType) != ResourceModule.RESOURCE_INFINITE_AMOUNT) {
         int harvestCount = 0;

         for (MobBase m : this.mob.getMobsByAssignment(Assignment.AssignmentType.HARVESTING, false)) {
            if (m.getAssignment() != null && m != this.thisMob) {
               Assignment assignment = m.getAssignment();
               ResourceModule.ResourceType assignmentResourceType = assignment.getPickupResourceType();
               if (assignmentResourceType == resourceType) {
                  harvestCount++;
               }
            }
         }

         if (this.resource.getResourceMakeAmount(resourceType) == ResourceModule.RESOURCE_STORAGE_AMOUNT
            || this.resource.getResourceMaintainAmount(resourceType) == ResourceModule.RESOURCE_STORAGE_AMOUNT) {
            int storageSpace = this.resource.getResourceStoresSpace(resourceType);
            int resourcesOnGround = this.resource.getResourceGround(resourceType, true);
            int resourcesCarried = this.resource.getResourceCarried(resourceType);
            return storageSpace - resourcesOnGround - harvestCount - resourcesCarried <= 0;
         } else {
            return this.resource.getResourceMakeAmount(resourceType) - harvestCount <= 0
               && this.resource.getResourceMaintainAmount(resourceType) - this.resource.getResourceTotal(resourceType, true) - harvestCount <= 0;
         }
      } else {
         return false;
      }
   }
}
