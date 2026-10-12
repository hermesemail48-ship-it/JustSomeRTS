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
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIConstructionHarvestWork extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_HARVEST_WORK = 1;
   private static final int HARVEST_RESOURCE = 2;
   private static final int CONTINUE_WORK = 3;
   private static AIBase resetBranch;
   private static AIBase exitBranch;
   private static AIBase failBranch;

   public AIConstructionHarvestWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CONSTRUCTION_HARVEST_WORK;
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
         exitBranch = this.thisMob.getAICaptureAnimalsInHand();
         failBranch = this.thisMob.getAICriticalRepairWork();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findWork();
                  break;
               case 1:
                  this.pathfindToHarvestWork();
                  break;
               case 2:
                  this.harvestResource();
                  break;
               case 3:
                  this.continueWork();
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
         if (!job.canWorkerConstructionHarvest()) {
            this.switchNode(failBranch);
         } else {
            int mobID = this.thisMob.getID();
            ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
            ArrayList<ResourceModule.ResourceType> selectedResourceTypes = new ArrayList<>();

            for (ResourceModule.ResourceType t : job.getCanWorkerConstructionHarvest()) {
               if (this.workSelection.getHarvestSelectionMapArray(t).size() > 0) {
                  selectedResourceTypes.add(t);
               }
            }

            ObjectBase objectTopPriority = null;

            for (ObjectBase o : objectList) {
               if (o.isInRange() && !o.isPaused() && this.thisMob.canReach(o, true) && o.getObjectFlags().canBuildingBeBuilt()) {
                  boolean canHarvest = false;

                  for (ResourceModule.ResourceType type : o.getObjectFlags().getCanBuildingBeBuilt()) {
                     if (selectedResourceTypes.contains(type)
                        && o.getResourceCount(type, true) < o.getObjectFlags().getResourceValueBaseCount(type) - o.getResourceValueCount(type)
                        && job.canWorkerConstructionHarvest(type)) {
                        canHarvest = true;
                        break;
                     }
                  }

                  if (canHarvest) {
                     objectTopPriority = o;
                     break;
                  }
               }
            }

            if (objectTopPriority == null) {
               this.switchNode(failBranch);
            } else {
               ArrayList<ResourceModule.ResourceType> resourceBlocked = new ArrayList<>();

               ResourceModule.ResourceType[] var29;
               for (ResourceModule.ResourceType typex : ResourceModule.ResourceType.values()) {
                  resourceBlocked.add(typex);
               }

               ArrayList<ResourceBase> resourcesOnGroundNearby = this.resource
                  .getResourcesNearby(this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getWorkNearbyResourcesOnGroundDistance());
               ArrayList<ResourceBase> resourcesOnGround = this.resource.getResourcesOnGroundInVillage();

               for (ResourceBase r : resourcesOnGroundNearby) {
                  if (!resourcesOnGround.contains(r)) {
                     resourcesOnGround.add(r);
                  }
               }

               label173:
               for (ResourceModule.ResourceType typex : job.getCanWorkerConstructionHarvest()) {
                  if (resourceBlocked.contains(typex)
                     && objectTopPriority.getObjectFlags().canBuildingBeBuilt(typex)
                     && !objectTopPriority.isPaused()
                     && objectTopPriority.getAcceptBuildResourceCount(typex, mobID) > 0) {
                     for (ObjectBase ox : objectList) {
                        if (ox.getObjectFlags().canBuildingStore(typex) && ox.getResourceCount(typex) > 0 && ox.isResourceAvailable(typex)) {
                           continue label173;
                        }
                     }

                     for (ResourceBase rx : resourcesOnGround) {
                        if (rx.getType() == typex && !rx.isAssigned()) {
                           continue label173;
                        }
                     }

                     resourceBlocked.remove(typex);
                  }
               }

               ArrayList<OrderedPair> resourceList = new ArrayList<>();

               for (ResourceModule.ResourceType tx : job.getCanWorkerConstructionHarvest()) {
                  if (!resourceBlocked.contains(tx)) {
                     resourceList.addAll(this.workSelection.getHarvestSelectionMapArray(tx));
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
   }

   private void pathfindToHarvestWork() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.constructionHarvestWork.pathfindToHarvestWork"));
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

   private void harvestResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.constructionHarvestWork.harvestResource"));
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(resetBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         float mobX = this.thisMob.getMobX();
         float mobY = this.thisMob.getMobY();
         if (this.workSelection.getSelectionMap()[assignment.getSecondaryX()][assignment.getSecondaryY()]
               == WorkSelectionModule.SelectionType.HARVEST_TERRAIN.ordinal()
            && assignment.getPrimaryX() == tileX
            && assignment.getPrimaryY() == tileY
            && this.resource.getResourceValues()[assignment.getSecondaryX()][assignment.getSecondaryY()] > 0) {
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

               this.thisMob.addCarriedResource(this.resource.harvestResource(assignment.getSecondaryX(), assignment.getSecondaryY(), 20), true);
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

               countSameType /= 3;
               if (countSameType < 1) {
                  countSameType = 1;
               }

               if (Utilities.randomInt(countSameType) == 0) {
                  this.aiStep = 3;
               } else {
                  this.switchNode(exitBranch);
               }
            }
         } else {
            this.switchNode(resetBranch);
         }
      }
   }

   private void continueWork() throws SlickException {
      this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
      this.aiStep = 1;
   }
}
