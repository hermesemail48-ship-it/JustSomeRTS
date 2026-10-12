package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.map.WorkSelectionModule;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIHarvestWork extends AIBase {
   private static final int GET_WORK = 0;
   private static final int PATHFIND_TO_RESOURCE = 1;
   private static final int HARVEST_RESOURCE = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;
   private static AIBase exitBranch;

   public AIHarvestWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_HARVEST_WORK;
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
         failBranch = this.thisMob.getAIHarvestWaterWork();
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
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(failBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canWorkerHarvest()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<OrderedPair> resourceList = new ArrayList<>();

            for (ResourceModule.ResourceType t : job.getCanWorkerHarvest()) {
               if (t != ResourceModule.ResourceType.DIRTY_WATER_BUCKET
                  && (this.resource.getResourceMaintainAmount(t) > 0 || this.resource.getResourceMakeAmount(t) > 0)
                  && !this.enoughWorkersHarvesting(t)) {
                  resourceList.addAll(this.workSelection.getHarvestSelectionMapArray(t));
               }
            }

            resourceList = OrderedPair.sortPairsDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), resourceList);
            if (resourceList.size() == 0) {
               this.switchNode(failBranch);
            } else {
               byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());

               for (int i = 0; i < resourceList.size(); i++) {
                  int checkX = resourceList.get(i).getX();
                  int checkY = resourceList.get(i).getY();
                  if ((!this.thisMob.getMobFlags().canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isTileThreatened(checkX, checkY))
                     && (
                        blockMap[checkX][checkY - 1] == 0
                           || blockMap[checkX - 1][checkY] == 0
                           || blockMap[checkX + 1][checkY] == 0
                           || blockMap[checkX][checkY + 1] == 0
                     )
                     && !this.mob.isWorkAssigned(checkX, checkY, Assignment.AssignmentType.HARVESTING)) {
                     ArrayList<OrderedPair> validCoordinates = new ArrayList<>();
                     if (this.thisMob.canReach(checkX, checkY - 1)) {
                        validCoordinates.add(OrderedPair.getOrderedPair(checkX, checkY - 1));
                     }

                     if (this.thisMob.canReach(checkX, checkY + 1)) {
                        validCoordinates.add(OrderedPair.getOrderedPair(checkX, checkY + 1));
                     }

                     if (this.thisMob.canReach(checkX - 1, checkY)) {
                        validCoordinates.add(OrderedPair.getOrderedPair(checkX - 1, checkY));
                     }

                     if (this.thisMob.canReach(checkX + 1, checkY)) {
                        validCoordinates.add(OrderedPair.getOrderedPair(checkX + 1, checkY));
                     }

                     if (validCoordinates.size() > 0) {
                        Assignment assignment = this.thisMob.getAssignment();
                        assignment.reset();
                        assignment.setPrimaryCoordinates(validCoordinates.get(Utilities.randomInt(validCoordinates.size())));
                        assignment.setSecondaryCoordinates(resourceList.get(i));
                        assignment.setPickupResourceType(this.resource.getResourceType()[resourceList.get(i).getX()][resourceList.get(i).getY()]);
                        assignment.setAssignmentType(Assignment.AssignmentType.HARVESTING);
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

   private void pathfindToResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.harvestWork.pathfindToResource"));
      this.runPath();
      Assignment assignment = this.thisMob.getAssignment();
      if (!this.pathFailed
         && (
            !this.thisMob.getMobFlags().canIgnoreThreatenedTiles()
               || !this.map.getMapThreatMap().isTileThreatened(assignment.getSecondaryX(), assignment.getSecondaryY())
         )
         && this.workSelection.getSelectionMap()[assignment.getSecondaryX()][assignment.getSecondaryY()]
            == WorkSelectionModule.SelectionType.HARVEST_TERRAIN.ordinal()
         && !this.enoughWorkersHarvesting(this.thisMob.getAssignment().getPickupResourceType())) {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void harvestResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.harvestWork.harvestResource"));
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(resetBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         float mobX = this.thisMob.getMobX();
         float mobY = this.thisMob.getMobY();
         if (this.thisMob.getMobFlags().canIgnoreThreatenedTiles()
            && this.map.getMapThreatMap().isTileThreatened(assignment.getSecondaryX(), assignment.getSecondaryY())) {
            this.switchNode(resetBranch);
         } else if (this.workSelection.getSelectionMap()[assignment.getSecondaryX()][assignment.getSecondaryY()]
               == WorkSelectionModule.SelectionType.HARVEST_TERRAIN.ordinal()
            && this.resource.getResourceValues()[assignment.getSecondaryX()][assignment.getSecondaryY()] > 0
            && assignment.getPrimaryX() == tileX
            && assignment.getPrimaryY() == tileY) {
            if (this.enoughWorkersHarvesting(this.thisMob.getAssignment().getPickupResourceType())) {
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
                  for (int l = 0; l < 12; l++) {
                     int tileID = this.map.getTileId(assignment.getSecondaryX(), assignment.getSecondaryY(), l);
                     ResourceModule.ResourceType resourceType = this.map.getMapTileLoader().getTileResourceType(tileID);
                     if (resourceType != null) {
                        if (resourceType == ResourceModule.ResourceType.WOOD) {
                           this.sound.playSound(SoundModule.SoundType.WOOD_CHOP, mobX, mobY);
                        } else if (resourceType == ResourceModule.ResourceType.ROCK) {
                           this.sound.playSound(SoundModule.SoundType.MINING, mobX, mobY);
                        }
                        break;
                     }
                  }

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
                     this.god.increaseGodXP(0.01, GodModule.GodXPType.HARVESTING_RESOURCES);
                  }

                  this.thisMob.addCarriedResource(this.resource.harvestResource(assignment.getSecondaryX(), assignment.getSecondaryY(), 40), true);
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

                  if (this.resource.getResourceMakeAmount(this.thisMob.getAssignment().getPickupResourceType()) != ResourceModule.RESOURCE_STORAGE_AMOUNT
                     || this.resource.getResourceMakeAmount(this.thisMob.getAssignment().getPickupResourceType()) != ResourceModule.RESOURCE_INFINITE_AMOUNT) {
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
         } else {
            this.switchNode(resetBranch);
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
