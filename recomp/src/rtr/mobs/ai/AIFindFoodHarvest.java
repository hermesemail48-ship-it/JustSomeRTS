package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIFindFoodHarvest extends AIBase {
   private static final int FIND_FOOD = 0;
   private static final int PATHFIND_TO_FOOD = 1;
   private static final int HARVEST_FOOD = 2;
   private static final int EAT_HARVESTED_FOOD = 3;
   private static AIBase resetBranch;

   public AIFindFoodHarvest(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_FIND_FOOD_HARVEST;
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
                  this.getHarvestWork();
                  break;
               case 1:
                  this.pathfindToFood();
                  break;
               case 2:
                  this.harvestFood();
                  break;
               case 3:
                  this.eatHarvestedFood();
            }
         } else {
            this.runDropResource();
         }
      }
   }

   private void getHarvestWork() throws SlickException {
      ArrayList<OrderedPair> nearbyCoordinates = this.pathFinder
         .getCoordinatesInRange(
            this.thisMob.getTileX(),
            this.thisMob.getTileY(),
            this.thisMobFlags.getHungerHungryFoodSearchDistance(),
            false,
            this.thisMobFlags.getBlockMapGroup()
         );
      ArrayList<OrderedPair> nearbyFoodTiles = new ArrayList<>();

      for (OrderedPair o : nearbyCoordinates) {
         if (this.resource.getResourceValues()[o.getX()][o.getY()] > 0
            && this.resource.getResourceType()[o.getX()][o.getY()] == ResourceModule.ResourceType.RAW_VEGETABLE) {
            nearbyFoodTiles.add(o);
         }
      }

      if (nearbyFoodTiles.size() == 0) {
         this.switchNode(resetBranch);
      } else {
         OrderedPair nearestResource = this.pathFinder
            .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), nearbyFoodTiles, false, true, this.thisMobFlags.getBlockMapGroup());
         if (nearestResource != null) {
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            assignment.setPrimaryCoordinates(nearestResource);
            assignment.setPickupResourceType(this.resource.getResourceType()[nearestResource.getX()][nearestResource.getY()]);
            assignment.setAssignmentType(Assignment.AssignmentType.HARVESTING);
            assignment.finalizeAssignment();
            this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
            this.aiStep = 1;
         } else {
            this.switchNode(resetBranch);
         }
      }
   }

   private void pathfindToFood() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.findFoodHarvest.pathfindToFood"));
      this.runPath();
      if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void harvestFood() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.findFoodHarvest.harvestFood"));
      if (!this.thisMobFlags.getAllowHunger()) {
         this.switchNode(resetBranch);
      } else {
         Assignment assignment = this.thisMob.getAssignment();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         if (this.resource.getResourceValues()[assignment.getPrimaryX()][assignment.getPrimaryY()] > 0
            && (
               assignment.getPrimaryX() == tileX + 1 && assignment.getPrimaryY() == tileY
                  || assignment.getPrimaryX() == tileX - 1 && assignment.getPrimaryY() == tileY
                  || assignment.getPrimaryX() == tileX && assignment.getPrimaryY() == tileY + 1
                  || assignment.getPrimaryX() == tileX && assignment.getPrimaryY() == tileY - 1
                  || assignment.getPrimaryX() == tileX && assignment.getPrimaryY() == tileY
            )) {
            byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
            boolean foundSurface = false;
            if (blockMap[assignment.getPrimaryX()][assignment.getPrimaryY()] > 0) {
               foundSurface = true;
            }

            this.thisMob.setPositionInteract(false, !foundSurface);
            if (assignment.getPrimaryX() == tileX + 1 && assignment.getPrimaryY() == tileY) {
               this.thisMob.setPosition(MobBase.MobPosition.EAST);
            } else if (assignment.getPrimaryX() == tileX - 1 && assignment.getPrimaryY() == tileY) {
               this.thisMob.setPosition(MobBase.MobPosition.WEST);
            } else if (assignment.getPrimaryX() == tileX && assignment.getPrimaryY() == tileY + 1) {
               this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
            } else if (assignment.getPrimaryX() == tileX && assignment.getPrimaryY() == tileY - 1) {
               this.thisMob.setPosition(MobBase.MobPosition.NORTH);
            }

            this.thisMob.runHungerHarvestTimeTick(1);
            this.thisMob.doWork(1, 1, 1, 1, 0);
            if (this.thisMob.getHungerHarvestTimeTick() > this.thisMobFlags.getHungerHarvestTime()) {
               this.thisMob.awardXP(Utilities.randomInt(5) + 5);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.03, GodModule.GodXPType.HARVESTING_RESOURCES);
               }

               ResourceBase food = this.resource.harvestResource(assignment.getPrimaryX(), assignment.getPrimaryY(), 30);
               if (food == null) {
                  this.thisMob.resetHungerHarvestTimeTick();
                  this.aiStep = 0;
               } else {
                  this.goal.incrementGoal(GoalModule.GoalType.THE_GRIND);
                  ResourceModule.ResourceType harvestedType = food.getType();
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

                  this.thisMob.equipResource(food, food.getType().getTemplate().getResourceSlot());
                  this.thisMob.resetHungerHarvestTimeTick();
                  this.aiStep = 3;
               }
            }
         } else {
            this.thisMob.resetHungerHarvestTimeTick();
            this.aiStep = 0;
         }
      }
   }

   private void eatHarvestedFood() throws SlickException {
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
