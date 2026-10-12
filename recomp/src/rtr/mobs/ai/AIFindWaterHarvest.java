package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIFindWaterHarvest extends AIBase {
   private static final int FIND_WATER = 0;
   private static final int PATHFIND_TO_WATER = 1;
   private static final int HARVEST_WATER = 2;
   private static final int DRINK_FROM_WATER = 3;
   private static AIBase resetBranch;

   public AIFindWaterHarvest(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_FIND_WATER_HARVEST;
      this.interruptDeath = true;
      this.interruptOnFire = true;
      this.interruptFrozen = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowThirst();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && this.thisMob.getThirst() < this.thisMobFlags.getThirstDehydrationWaterSearchLevel();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findWater();
                  break;
               case 1:
                  this.pathfindToWater();
                  break;
               case 2:
                  this.harvestWater();
                  break;
               case 3:
                  this.drinkFromWater();
            }
         }
      }
   }

   private void findWater() throws SlickException {
      ArrayList<OrderedPair> nearbyCoordinates = this.pathFinder
         .getCoordinatesInRange(
            this.thisMob.getTileX(),
            this.thisMob.getTileY(),
            this.thisMobFlags.getThirstThirstyWaterSearchDistance(),
            false,
            this.thisMobFlags.getBlockMapGroup()
         );
      ArrayList<OrderedPair> nearbyWaterTiles = new ArrayList<>();

      for (OrderedPair o : nearbyCoordinates) {
         if (this.resource.getResourceValues()[o.getX()][o.getY()] > 0
            && (
               this.resource.getResourceType()[o.getX()][o.getY()] == ResourceModule.ResourceType.DIRTY_WATER_BUCKET
                  || this.resource.getResourceType()[o.getX()][o.getY()] == ResourceModule.ResourceType.WATER_BUCKET
            )) {
            nearbyWaterTiles.add(o);
         }
      }

      if (nearbyWaterTiles.size() == 0) {
         this.switchNode(resetBranch);
      } else {
         OrderedPair nearestResource = this.pathFinder
            .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), nearbyWaterTiles, false, true, this.thisMobFlags.getBlockMapGroup());
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

   private void pathfindToWater() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.findWaterHarvest.pathfindToWater"));
      this.runPath();
      if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void harvestWater() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.findWaterHarvest.harvestWater"));
      if (!this.thisMobFlags.getAllowThirst()) {
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
            this.thisMob.setPositionDrink();
            if (assignment.getPrimaryX() == tileX + 1 && assignment.getPrimaryY() == tileY) {
               this.thisMob.setPosition(MobBase.MobPosition.EAST);
            } else if (assignment.getPrimaryX() == tileX - 1 && assignment.getPrimaryY() == tileY) {
               this.thisMob.setPosition(MobBase.MobPosition.WEST);
            } else if (assignment.getPrimaryX() == tileX && assignment.getPrimaryY() == tileY + 1) {
               this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
            } else if (assignment.getPrimaryX() == tileX && assignment.getPrimaryY() == tileY - 1) {
               this.thisMob.setPosition(MobBase.MobPosition.NORTH);
            }

            this.thisMob.runThirstHarvestTimeTick(1);
            this.thisMob.doWork(1, 1, 1, 1, 0);
            if (this.thisMob.getThirstHarvestTimeTick() > this.thisMobFlags.getThirstHarvestTime()) {
               ResourceBase water = this.resource.harvestResource(assignment.getPrimaryX(), assignment.getPrimaryY(), 30);
               if (water == null) {
                  this.thisMob.resetThirstHarvestTimeTick();
                  this.aiStep = 0;
               } else {
                  this.goal.incrementGoal(GoalModule.GoalType.THE_GRIND);
                  ResourceModule.ResourceType harvestedType = water.getType();
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

                  this.thisMob.increaseDysenteryAmount(water.getUses() * 100);
                  this.thisMob.increaseThirst(water.getUses() / 4);
                  this.resource.deleteResource(water);
                  this.thisMob.resetThirstHarvestTimeTick();
                  this.aiStep = 3;
               }
            }
         } else {
            this.thisMob.resetThirstHarvestTimeTick();
            this.aiStep = 0;
         }
      }
   }

   private void drinkFromWater() throws SlickException {
      if (this.thisMob.hasWaterResource()
         && this.thisMob.getThirst() < this.thisMob.getThirstMax()
         && (
            this.thisMob.getThirst() <= this.thisMobFlags.getThirstDrinkBreakawayLevel()
               || Utilities.randomInt(this.thisMobFlags.getThirstDrinkBreakawayChance()) != 0
         )) {
         this.drinkWaterInInventory();
      } else {
         this.switchNode(resetBranch);
      }
   }
}
