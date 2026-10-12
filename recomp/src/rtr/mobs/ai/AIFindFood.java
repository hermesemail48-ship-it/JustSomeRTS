package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIFindFood extends AIBase {
   private static final int FIND_FOOD = 0;
   private static final int PATHFIND_TO_RESOURCE_ON_GROUND = 1;
   private static final int PATHFIND_TO_BUILDING = 2;
   private static final int PICK_UP_RESOURCE_ON_GROUND = 3;
   private static final int PICK_UP_RESOURCE_IN_BUILDING = 4;
   private static final int LIFT_RESOURCE = 5;
   private static final int EAT_FOOD = 6;
   private static AIBase resetBranch;

   public AIFindFood(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_FIND_FOOD;
      this.interruptDeath = true;
      this.interruptOnFire = true;
      this.interruptFrozen = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowHunger();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && this.thisMob.getHunger() < this.thisMobFlags.getHungerStarvationFoodSearchLevel();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         if (!this.thisMob.hasCarriedResources()
            || this.thisMob.getCarriedResourcesListType().getTemplate().getFoodPerUse() > 0
               && this.thisMob.getCarriedResourcesListType().getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.FOOD) {
            switch (this.aiStep) {
               case 0:
                  this.findFood();
                  break;
               case 1:
                  this.pathfindToResourceOnGround();
                  break;
               case 2:
                  this.pathfindToBuilding();
                  break;
               case 3:
                  this.pickUpResourceOnGround();
                  break;
               case 4:
                  this.pickUpResourcesInBuilding();
                  break;
               case 5:
                  this.liftResource();
                  break;
               case 6:
                  this.eatFood();
            }
         } else {
            this.runDropResource();
         }
      }
   }

   private void findFood() throws SlickException {
      ArrayList<ResourceModule.ResourceType> canPickupResources = new ArrayList<>();

      ResourceModule.ResourceType[] validStoresByTypeLowestPriorityArray;
      for (ResourceModule.ResourceType t : validStoresByTypeLowestPriorityArray = ResourceModule.ResourceType.values()) {
         if (t.getTemplate().getFoodPerUse() > 0) {
            canPickupResources.add(t);
         }
      }

      ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
      HashMap<ResourceModule.ResourceType, ArrayList<ObjectBase>> validStoresByTypeLowestPriority = AIUtilities.createLowestPriorityStorageListHashMap(
         this.thisMob, canPickupResources, objectList
      );
      HashMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> validResourcesOnGroundByType = AIUtilities.createAvailabeResourceList(
         this.thisMob, canPickupResources, true, this.thisMobFlags.getHungerHungryFoodSearchDistance()
      );
      ArrayList<ObjectBase> validStoresByTypeLowestPriorityArrayx = null;
      ArrayList<ResourceBase> resourcesOnGroundByTypeArray = null;
      Collections.shuffle(canPickupResources);

      for (ResourceModule.ResourceType tx : canPickupResources) {
         validStoresByTypeLowestPriorityArrayx = validStoresByTypeLowestPriority.get(tx);
         resourcesOnGroundByTypeArray = validResourcesOnGroundByType.get(tx);
         if (validStoresByTypeLowestPriorityArrayx.size() > 0 || resourcesOnGroundByTypeArray.size() > 0) {
            break;
         }
      }

      if (validStoresByTypeLowestPriorityArrayx.size() == 0 && resourcesOnGroundByTypeArray.size() == 0) {
         this.switchNode(resetBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         assignment.reset();
         ArrayList<OrderedPair> coordinatesList = AIUtilities.createAvailabeResourceCoordinatesList(
            this.thisMob, validStoresByTypeLowestPriorityArrayx, resourcesOnGroundByTypeArray
         );
         OrderedPair closestResourceAnywhere = this.pathFinder
            .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), coordinatesList, false, true, this.thisMobFlags.getBlockMapGroup());
         ResourceBase pickupResource = this.resource.getResourceOnGround(closestResourceAnywhere);
         if (pickupResource != null && pickupResource.getType().getTemplate().getFoodPerUse() > 0 && resourcesOnGroundByTypeArray.contains(pickupResource)) {
            assignment.setPrimaryCoordinates(closestResourceAnywhere);
            assignment.addPickupResources(pickupResource);
            assignment.setAssignmentType(Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_GROUND);
            assignment.finalizeAssignment();
            this.requestPath(closestResourceAnywhere);
            this.aiStep = 1;
         } else {
            ObjectBase pickupBuilding = this.object.getBuildingAtLocation(closestResourceAnywhere);
            if (pickupBuilding != null && validStoresByTypeLowestPriorityArrayx.contains(pickupBuilding)) {
               OrderedPair pickupOutCoordinates = pickupBuilding.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
               assignment.setPrimaryObject(pickupBuilding);
               assignment.setPrimaryCoordinates(pickupOutCoordinates);

               for (ResourceModule.ResourceType txx : canPickupResources) {
                  if (pickupBuilding.isResourceAvailable(txx)) {
                     assignment.addPickupResources(pickupBuilding.getAvailableResource(txx));
                     break;
                  }
               }

               assignment.setAssignmentType(Assignment.AssignmentType.PICK_UP_RESOURCE_FROM_BUILDING);
               assignment.finalizeAssignment();
               this.requestPath(pickupOutCoordinates);
               this.aiStep = 2;
            } else {
               this.switchNode(resetBranch);
            }
         }
      }
   }

   private void pathfindToResourceOnGround() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.findFood.pathfindToResourceOnGround"));
      this.runPath();
      OrderedPair o = this.thisMob.getAssignment().getPrimaryCoordinates();
      ResourceBase r = this.resource.getResourceOnGround(o.getX(), o.getY());
      if (!this.pathFailed
         && r != null
         && (!this.thisMobFlags.canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isResourceThreatened(r))
         && !this.road.isResourceNeededForRoad(r)
         && this.thisMob.getAssignment().containsPickupResources(r)) {
         if (this.pathCompleted) {
            this.aiStep = 3;
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void pickUpResourceOnGround() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ResourceBase r = this.resource.getResourceOnGround(tileX, tileY);
      if (!this.thisMob.getAssignment().containsPickupResources(r)) {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.aiStep = 0;
      } else {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.equipResource(r, r.getType().getTemplate().getResourceSlot());
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 5;
         }
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.findFood.pathfindToBuilding"));
      this.runPath();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject != null && !currentObject.isDestroyed()) {
         if (this.thisMob.getMobFlags().canIgnoreThreatenedTiles() && this.map.getMapThreatMap().isObjectThreatened(currentObject)) {
            this.aiStep = 0;
         } else if (this.pathFailed) {
            this.aiStep = 0;
         } else {
            if (this.pathCompleted) {
               this.aiStep = 4;
            }
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void pickUpResourcesInBuilding() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase building = this.thisMob.getAssignment().getPrimaryObject();
      if (building != null
         && !building.isDestroyed()
         && building == this.object.getBuildingAtLocation(tileX, tileY)
         && building.getResourceCount(this.thisMob.getAssignment().getFirstPickupResources().getType()) > 0) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            ResourceBase deliveryResource = this.thisMob.getAssignment().removeAndGetFirstPickupResources();
            ResourceBase r = building.takeStores(deliveryResource);
            if (r == null) {
               r = building.takeStores(deliveryResource.getType());
            }

            this.thisMob.getSprite().restart();
            if (r == null) {
               this.thisMob.setPositionFace();
               this.aiStep = 0;
            } else {
               this.thisMob.equipResource(r, r.getType().getTemplate().getResourceSlot());
               this.thisMob.setPositionLift();
               this.aiStep = 5;
            }
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void liftResource() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.aiStep = 6;
      }
   }

   private void eatFood() throws SlickException {
      if (this.thisMob.hasFoodResource()
         && this.thisMob.getHunger() < this.thisMob.getHungerMax()
         && (
            this.thisMob.getHunger() <= this.thisMobFlags.getHungerEatBreakawayLevel()
               || Utilities.randomInt(this.thisMobFlags.getHungerEatBreakawayChance()) != 0
         )) {
         this.eatFoodInInventory();
      } else {
         this.switchNode(resetBranch);
      }
   }
}
