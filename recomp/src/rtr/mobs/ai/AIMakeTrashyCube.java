package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIMakeTrashyCube extends AIBase {
   private static final int FIND_TRASHY_TRASH = 0;
   private static final int PATHFIND_TO_RESOURCE_ON_GROUND = 1;
   private static final int PATHFIND_TO_BUILDING = 2;
   private static final int MAKE_TRASHY_CUBE = 3;
   private static final int LIFT_CUBE = 4;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIMakeTrashyCube(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_MAKE_TRASHY_CUBE;
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
         failBranch = this.thisMob.getAIHarvestWork();
         if (this.thisMob.getMobJobType() != null && this.thisMob.getMobJobType().getJob().canMakeTrashyCubes()) {
            switch (this.aiStep) {
               case 0:
                  this.findTrashyTrash();
                  break;
               case 1:
                  this.pathfindToResourceOnGround();
                  break;
               case 2:
                  this.pathfindToBuilding();
                  break;
               case 3:
                  this.makeTrashyCube();
                  break;
               case 4:
                  this.liftCube();
            }
         } else {
            this.switchNode(failBranch);
         }
      }
   }

   private void findTrashyTrash() throws SlickException {
      ArrayList<ResourceModule.ResourceType> trashyTrashTypeArray = new ArrayList<>();
      trashyTrashTypeArray.add(ResourceModule.ResourceType.TRASHY_TRASH);
      ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
      HashMap<ResourceModule.ResourceType, ArrayList<ObjectBase>> validStoresByTypeLowestPriority = AIUtilities.createLowestPriorityStorageListHashMap(
         this.thisMob, trashyTrashTypeArray, objectList
      );
      HashMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> validResourcesOnGroundByType = AIUtilities.createAvailabeResourceList(
         this.thisMob, trashyTrashTypeArray, true, 32
      );
      ArrayList<ObjectBase> validStoresByTypeLowestPriorityArray = validStoresByTypeLowestPriority.get(ResourceModule.ResourceType.TRASHY_TRASH);
      ArrayList<ResourceBase> resourcesOnGroundByTypeArray = validResourcesOnGroundByType.get(ResourceModule.ResourceType.TRASHY_TRASH);
      if (validStoresByTypeLowestPriorityArray.size() == 0 && resourcesOnGroundByTypeArray.size() == 0) {
         this.switchNode(failBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         assignment.reset();
         ArrayList<OrderedPair> coordinatesList = AIUtilities.createAvailabeResourceCoordinatesList(
            this.thisMob, validStoresByTypeLowestPriorityArray, resourcesOnGroundByTypeArray
         );
         OrderedPair closestResourceAnywhere = this.pathFinder
            .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), coordinatesList, false, true, this.thisMobFlags.getBlockMapGroup());
         ResourceBase pickupResource = this.resource.getResourceOnGround(closestResourceAnywhere);
         if (pickupResource != null
            && pickupResource.getType() == ResourceModule.ResourceType.TRASHY_TRASH
            && resourcesOnGroundByTypeArray.contains(pickupResource)) {
            assignment.setPrimaryCoordinates(closestResourceAnywhere);
            assignment.addPickupResources(pickupResource);
            assignment.setAssignmentType(Assignment.AssignmentType.MAKE_TRASHY_CUBE);
            assignment.finalizeAssignment();
            this.requestPath(closestResourceAnywhere);
            this.aiStep = 1;
         } else {
            ObjectBase pickupBuilding = this.object.getBuildingAtLocation(closestResourceAnywhere);
            if (pickupBuilding != null && validStoresByTypeLowestPriorityArray.contains(pickupBuilding)) {
               OrderedPair pickupOutCoordinates = pickupBuilding.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
               assignment.setPrimaryObject(pickupBuilding);
               assignment.setPrimaryCoordinates(pickupOutCoordinates);
               assignment.addPickupResources(pickupBuilding.getAvailableResource(ResourceModule.ResourceType.TRASHY_TRASH));
               assignment.setAssignmentType(Assignment.AssignmentType.MAKE_TRASHY_CUBE);
               assignment.finalizeAssignment();
               this.requestPath(pickupOutCoordinates);
               this.aiStep = 2;
            } else {
               this.switchNode(failBranch);
            }
         }
      }
   }

   private void pathfindToResourceOnGround() throws SlickException {
      this.runPath();
      OrderedPair o = this.thisMob.getAssignment().getPrimaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (!this.pathFailed
         && r != null
         && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
         && !this.road.isResourceNeededForRoad(r)
         && this.thisMob.getAssignment().containsPickupResources(r)) {
         Text.setVariableText("resourceName", this.thisMob.getAssignment().getFirstPickupResources().getType().getTemplate().getName());
         this.thisMob.setCurrentTask(Text.getText("aiTask.makeTrashyCube.pathfindToTrashOnGround"));
         if (this.pathCompleted) {
            this.aiStep = 3;
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void pathfindToBuilding() throws SlickException {
      this.runPath();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (!this.object.objectExists(currentObject)) {
         this.switchNode(resetBranch);
      } else if (this.pathFailed) {
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            ResourceBase r = this.thisMob.getAssignment().getFirstPickupResources();
            if (r != null && currentObject.getResourceCount(this.thisMob.getAssignment().getFirstPickupResources().getType()) > 0) {
               this.aiStep = 3;
            } else {
               this.aiStep = 0;
            }
         }

         Text.setVariableText("resourceName", this.thisMob.getAssignment().getFirstPickupResources().getType().getTemplate().getName());
         Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
         this.thisMob.setCurrentTask(Text.getText("aiTask.makeTrashyCube.pathfindToBuilding"));
      }
   }

   protected void makeTrashyCube() throws SlickException {
      if (this.thisMob.getAssignment().getFirstPickupResources() != null
         && this.thisMob.getAssignment().getFirstPickupResources().getAssigned() == this.thisMob) {
         ResourceBase r = this.thisMob.getAssignment().getFirstPickupResources();
         Text.setVariableText("resourceName", r.getType().getTemplate().getName());
         this.thisMob.setCurrentTask(Text.getText("aiTask.makeTrashyCube.processTrash"));
         this.thisMob.setPositionInteract(false, true);
         if (!this.thisMob.isInsideBuilding()) {
            if (Utilities.randomInt(25) == 0) {
               this.particle
                  .newParticle(
                     this.thisMob.getCenterPixelX(true) + Utilities.randomInt(2),
                     this.thisMob.getCenterPixelY(true) - 3,
                     90.0F,
                     ParticleModule.ParticleType.SMOKE_SPECK,
                     ParticleModule.ParticleSet.GRAY
                  );
            }

            if (Utilities.randomInt(10) == 0) {
               this.particle
                  .newParticle(
                     this.thisMob.getCenterPixelX(true) + Utilities.randomInt(2),
                     this.thisMob.getCenterPixelY(true) - 3,
                     90.0F,
                     ParticleModule.ParticleType.CRUMB,
                     ParticleModule.ParticleSet.BROWN
                  );
            }
         }

         this.thisMob.runWorkDelayTick(1);
         if (this.thisMob.getWorkDelayTick() > this.mobJob.getCalculatedWorkTime(this.thisMob)) {
            this.thisMob.consumeResource(r, 25);
            this.thisMob.resetWorkDelayTick();
         }

         if (this.thisMob.getTrash() >= this.thisMob.getTrashMax()) {
            ResourceBase trashyCube = this.resource
               .createResourceUnassigned(ResourceModule.ResourceType.TRASHY_CUBE, this.thisMob.getAssignment().getFirstPickupResources().getColor());
            this.thisMob.addCarriedResource(trashyCube, true);
            this.thisMob.setTrash(0);
            this.thisMob.awardXP(Utilities.randomInt(5) + 5);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.01, GodModule.GodXPType.PROCESSING_RESOURCES);
            }

            this.aiStep = 4;
         } else if (r.getUses() <= 0) {
            this.switchNode(resetBranch);
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void liftCube() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         this.switchNode(resetBranch);
      }
   }
}
