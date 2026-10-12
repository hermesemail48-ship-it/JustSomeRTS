package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIEquipResource extends AIBase {
   private static final int FIND_EQUIPMENT = 0;
   private static final int PATHFIND_TO_EQUIPMENT_ON_GROUND = 1;
   private static final int PATHFIND_TO_BUILDING = 2;
   private static final int EQUIP_EQUIPMENT_ON_GROUND = 3;
   private static final int EQUIP_EQUIPMENT_IN_BUILDING = 4;
   private static final int EQUIP_LIFT = 5;
   private static AIBase resetBranch;

   public AIEquipResource(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_EQUIP_RESOURCE;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowInventory();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getInventorySearchChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         switch (this.aiStep) {
            case 0:
               this.findEquipment();
               break;
            case 1:
               this.pathfindToEquipmentOnGround();
               break;
            case 2:
               this.pathfindToBuilding();
               break;
            case 3:
               this.equipEquipmentOnGround();
               break;
            case 4:
               this.equipEquipmentInBuilding();
               break;
            case 5:
               this.equipLift();
         }
      }
   }

   private void findEquipment() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      assignment.reset();
      ArrayList<ResourceModule.ResourceType> canEquipResources = new ArrayList<>();

      ResourceModule.ResourceType[] validResourcesOnGroundByType;
      for (ResourceModule.ResourceType type : validResourcesOnGroundByType = ResourceModule.ResourceType.values()) {
         if (this.thisMob.isDesiredItem(type)) {
            canEquipResources.add(type);
         }
      }

      if (canEquipResources.size() <= 0) {
         this.switchNode(resetBranch);
      } else {
         ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
         ArrayList<ObjectBase> validEquipmentObjects = new ArrayList<>();
         if (this.thisMobFlags.getAllowInventoryEquipBuildings()) {
            for (ObjectBase o : objectList) {
               if (o.getObjectFlags().getOccupantsMax() <= 0 || this.thisMob.getAssignedHome() == o.getID()) {
                  validEquipmentObjects.add(o);
               }
            }
         }

         HashMap<ResourceModule.ResourceType, ArrayList<ObjectBase>> validStoresByTypeLowestPriority = AIUtilities.createLowestPriorityStorageListHashMap(
            this.thisMob, canEquipResources, validEquipmentObjects
         );
         HashMap<ResourceModule.ResourceType, ArrayList<ResourceBase>> validResourcesOnGroundByTypex = AIUtilities.createAvailabeResourceList(
            this.thisMob, canEquipResources, this.thisMobFlags.getCanInventorySearchVillage(), this.thisMobFlags.getInventoryNearbyResourcesOnGroundDistance()
         );
         Collections.shuffle(canEquipResources);
         Collections.sort(canEquipResources, new Comparator<ResourceModule.ResourceType>() {
            public int compare(ResourceModule.ResourceType m1, ResourceModule.ResourceType m2) {
               return m1 == m2 ? 0 : m1.getTemplate().getItemLevel() - m2.getTemplate().getItemLevel();
            }
         });
         Collections.reverse(canEquipResources);

         for (ResourceModule.ResourceType pickupType : canEquipResources) {
            ArrayList<ObjectBase> validStoresByTypeLowestPriorityArray = validStoresByTypeLowestPriority.get(pickupType);
            ArrayList<ResourceBase> resourcesOnGroundByTypeArray = validResourcesOnGroundByTypex.get(pickupType);
            if (validStoresByTypeLowestPriorityArray.size() != 0 || resourcesOnGroundByTypeArray.size() != 0) {
               ArrayList<OrderedPair> coordinatesList = AIUtilities.createAvailabeResourceCoordinatesList(
                  this.thisMob, validStoresByTypeLowestPriorityArray, resourcesOnGroundByTypeArray
               );
               OrderedPair closestResourceAnywhere = this.pathFinder
                  .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), coordinatesList, false, true, this.thisMobFlags.getBlockMapGroup());
               ResourceBase pickupResource = this.resource.getResourceOnGround(closestResourceAnywhere);
               if (pickupResource != null && pickupResource.getType() == pickupType && resourcesOnGroundByTypeArray.contains(pickupResource)) {
                  assignment.setPrimaryCoordinates(closestResourceAnywhere);
                  assignment.addPickupResources(pickupResource);
                  assignment.setAssignmentType(Assignment.AssignmentType.EQUIP_RESOURCE);
                  assignment.finalizeAssignment();
                  this.requestPath(closestResourceAnywhere);
                  this.aiStep = 1;
                  return;
               }

               ObjectBase pickupBuilding = this.object.getBuildingAtLocation(closestResourceAnywhere);
               if (pickupBuilding != null && validStoresByTypeLowestPriorityArray.contains(pickupBuilding)) {
                  OrderedPair pickupOutCoordinates = pickupBuilding.getBestRandomInteractCoordinates(this.thisMob, true, false, true);
                  assignment.setPrimaryObject(pickupBuilding);
                  assignment.setPrimaryCoordinates(pickupOutCoordinates);
                  assignment.addPickupResources(pickupBuilding.getAvailableResource(pickupType));
                  assignment.setAssignmentType(Assignment.AssignmentType.EQUIP_RESOURCE);
                  assignment.finalizeAssignment();
                  this.requestPath(pickupOutCoordinates);
                  this.aiStep = 2;
                  return;
               }
            }
         }

         this.switchNode(resetBranch);
      }
   }

   private void pathfindToEquipmentOnGround() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.equipResource.pathfindToEquipmentOnGround"));
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
         this.aiStep = 0;
      }
   }

   private void equipEquipmentOnGround() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ResourceBase resourceOnGround = this.resource.getResourceOnGround(tileX, tileY);
      if (resourceOnGround != null && this.thisMob.getAssignment().containsPickupResources(resourceOnGround)) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            this.thisMob.equipResource(resourceOnGround, resourceOnGround.getType().getTemplate().getResourceSlot());
            this.thisMob.awardXP(Utilities.randomInt(2) + 1);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.01, GodModule.GodXPType.EQUIPING_RESOURCES);
            }

            if (this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER && resourceOnGround.getType() == ResourceModule.ResourceType.BOW) {
               this.goal.incrementGoal(GoalModule.GoalType.LORD_OF_THE_ARROWS);
            }

            this.getFloater(resourceOnGround.getType().getTemplate().getResourceSlot());
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 5;
         }
      } else {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.aiStep = 0;
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.equipResource.pathfindToBuilding"));
      this.runPath();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject == null || currentObject.isDestroyed()) {
         this.aiStep = 0;
      } else if (this.pathFailed) {
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 4;
         }
      }
   }

   private void equipEquipmentInBuilding() throws SlickException {
      ObjectBase building = this.thisMob.getAssignment().getPrimaryObject();
      if (building != null
         && !building.isDestroyed()
         && building == this.object.getBuildingAtLocation(this.thisMob.getTileX(), this.thisMob.getTileY())
         && building.getResourceCount(this.thisMob.getAssignment().getFirstPickupResources().getType()) > 0) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            ResourceBase equipResource = this.thisMob.getAssignment().removeAndGetFirstPickupResources();
            ResourceBase r = building.takeStores(equipResource);
            if (r == null) {
               r = building.takeStores(equipResource.getType());
            }

            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            if (r == null) {
               this.aiStep = 0;
            } else {
               this.thisMob.equipResource(r, r.getType().getTemplate().getResourceSlot());
               this.thisMob.awardXP(Utilities.randomInt(2) + 1);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.EQUIPING_RESOURCES);
               }

               if (this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER && r.getType() == ResourceModule.ResourceType.BOW) {
                  this.goal.incrementGoal(GoalModule.GoalType.LORD_OF_THE_ARROWS);
               }

               this.getFloater(r.getType().getTemplate().getResourceSlot());
               this.aiStep = 5;
            }
         }
      } else {
         this.thisMob.getSprite().restart();
         this.thisMob.setPositionFace();
         this.aiStep = 0;
      }
   }

   private void equipLift() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         this.switchNode(resetBranch);
      }
   }

   private void getFloater(ResourceModule.ResourceSlot s) throws SlickException {
      switch (s) {
         case HEAD:
            this.font.newFloater("iconHeadSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case CHEST:
            this.font.newFloater("iconChestSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case MELEE_WEAPON:
            this.font.newFloater("iconMeleeWeaponSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case SHIELD:
            this.font.newFloater("iconShieldSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case TOOL:
            this.font.newFloater("iconToolSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case RECOVERY:
            this.font.newFloater("iconRecoverySlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case WATER:
            this.font.newFloater("iconWaterSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case FOOD:
            this.font.newFloater("iconFoodSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case RANGED_WEAPON:
            this.font.newFloater("iconRangedWeaponSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            break;
         case RANGED_WEAPON_AMMO:
            this.font.newFloater("iconRangedWeaponAmmoSlot", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
      }
   }
}
