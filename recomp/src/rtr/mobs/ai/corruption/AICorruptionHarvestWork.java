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
import rtr.road.RoadModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AICorruptionHarvestWork extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_HARVEST_WORK = 1;
   private static final int HARVEST_RESOURCE = 2;
   private static AIBase failBranch;
   private static AIBase exitBranch;

   public AICorruptionHarvestWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CORRUPTION_HARVEST_WORK;
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
         failBranch = this.thisMob.getAICorruptionClearTopographyWork();
         exitBranch = this.thisMob.getAICorruptionDeliverResourceInHand();
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
            }
         }
      }
   }

   private void findWork() throws SlickException {
      ArrayList<ObjectBase> objectList = this.object.getObjectsByGroup(MobBase.MobGroup.MONSTER);
      ArrayList<ObjectBase> validObjects = new ArrayList<>();
      ArrayList<ResourceModule.ResourceType> validResourceTypes = new ArrayList<>();
      ArrayList<ResourceModule.ResourceType> blockedResourceTypes = new ArrayList<>();

      for (ResourceBase r : this.resource.getResourcesOnGroundInCorruption()) {
         if (!blockedResourceTypes.contains(r.getType())) {
            blockedResourceTypes.add(r.getType());
         }
      }

      for (ObjectBase o : objectList) {
         if (o.isOnCorruption() && !o.isPaused() && this.thisMob.canReach(o) && o.getObjectFlags().canBuildingBeBuilt()) {
            for (ResourceModule.ResourceType type : o.getObjectFlags().getCanBuildingBeBuilt()) {
               if (o.getResourceCount(type, true) < o.getObjectFlags().getResourceValueBaseCount(type) - o.getResourceValueCount(type)
                  && !validResourceTypes.contains(type)
                  && !blockedResourceTypes.contains(type)) {
                  validResourceTypes.add(type);
               }
            }

            if (validResourceTypes.size() != 0) {
               validObjects.add(o);
            }
         }
      }

      if (validObjects.size() <= 0) {
         validResourceTypes.clear();
         ArrayList<OrderedPair> roadSelectionList = this.corruption.getCorruptedRoadMapArray();
         ArrayList<OrderedPair> roadSelectionListFiltered = new ArrayList<>();
         roadSelectionListFiltered.addAll(roadSelectionList);

         for (OrderedPair ox : roadSelectionListFiltered) {
            RoadModule.RoadType roadType = this.road.getRoadType(ox.getX(), ox.getY());
            if (roadType.getTemplate().getNextRoadType() != null) {
               ResourceModule.ResourceType typex = this.road.getNextRequiredResource(ox.getX(), ox.getY());
               if (typex != null
                  && this.resource.getResourceTypeOnGround(ox.getX(), ox.getY()) == null
                  && !validResourceTypes.contains(typex)
                  && !blockedResourceTypes.contains(typex)) {
                  validResourceTypes.add(typex);
               }
            }
         }

         if (validResourceTypes.size() <= 0) {
            this.switchNode(failBranch);
            return;
         }
      }

      ArrayList<OrderedPair> resourceList = Utilities.getAllCoordinatesWithin(
         this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getCorruptionWorkHarvestDistance()
      );
      if (resourceList.size() == 0) {
         this.switchNode(failBranch);
      } else {
         for (int i = resourceList.size() - 1; i >= 0; i--) {
            ResourceModule.ResourceType typex = this.resource.getResourceType()[resourceList.get(i).getX()][resourceList.get(i).getY()];
            if (typex == null || !validResourceTypes.contains(typex)) {
               resourceList.remove(i);
            }
         }

         if (resourceList.size() == 0) {
            this.switchNode(failBranch);
         } else {
            resourceList = OrderedPair.sortPairsDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), resourceList);
            byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());

            for (int ix = 0; ix < resourceList.size(); ix++) {
               int checkX = resourceList.get(ix).getX();
               int checkY = resourceList.get(ix).getY();
               if ((
                     blockMap[checkX][checkY - 1] == 0
                        || blockMap[checkX - 1][checkY] == 0
                        || blockMap[checkX + 1][checkY] == 0
                        || blockMap[checkX][checkY + 1] == 0
                  )
                  && !this.mob.isWorkAssigned(checkX, checkY, Assignment.AssignmentType.CORRUPTION_HARVESTING)) {
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
                     assignment.setSecondaryCoordinates(resourceList.get(ix));
                     assignment.setPickupResourceType(this.resource.getResourceType()[resourceList.get(ix).getX()][resourceList.get(ix).getY()]);
                     assignment.setAssignmentType(Assignment.AssignmentType.CORRUPTION_HARVESTING);
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

   private void pathfindToHarvestWork() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.corruptionHarvestWork.pathfindToHarvestWork"));
      this.runPath();
      if (this.pathFailed) {
         this.switchNode(failBranch);
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void harvestResource() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.corruptionHarvestWork.harvestResource"));
      Assignment assignment = this.thisMob.getAssignment();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      float mobX = this.thisMob.getMobX();
      float mobY = this.thisMob.getMobY();
      if (assignment.getPrimaryX() == tileX
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

         if (this.thisMob.getWorkDelayTick() > this.thisMobFlags.getCorruptionWorkTime()) {
            if (usingTools) {
               this.thisMob.getInventoryToolSlot().decreaseUses(1);
            }

            this.thisMob.awardXP(Utilities.randomInt(5) + 5);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.01, GodModule.GodXPType.HARVESTING_RESOURCES);
            }

            this.thisMob.addCarriedResource(this.resource.harvestResource(assignment.getSecondaryX(), assignment.getSecondaryY(), 50), true);
            this.thisMob.getAssignment().reset();
            this.thisMob.resetWorkDelayTick();
            ArrayList<ResourceBase> resourcesNearby = this.resource
               .getResourcesNearby(tileX, tileY, this.thisMobFlags.getCorruptionWorkNearbyResourcesOnGroundDistance());
            resourcesNearby = this.resource
               .cullResourcesByDistance(
                  tileX, tileY, this.thisMobFlags.getCorruptionWorkNearbyResourcesOnGroundDistance(), resourcesNearby, this.thisMobFlags.getBlockMapGroup()
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
               this.aiStep = 0;
            } else {
               this.switchNode(exitBranch);
            }
         }
      } else {
         this.switchNode(failBranch);
      }
   }
}
