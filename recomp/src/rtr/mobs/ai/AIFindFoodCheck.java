package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIFindFoodCheck extends AIBase {
   private static AIBase resetBranch;
   private static AIBase exitBranchGoHome;
   private static AIBase exitBranchFood;
   private static AIBase exitBranchHarvestFood;

   public AIFindFoodCheck(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_FIND_FOOD_CHECK;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowHunger();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun()
         && Utilities.randomInt(this.thisMobFlags.getHungerFindFoodChance()) == 0
         && (
            this.thisMob.getHunger() <= this.thisMobFlags.getHungerHungryFoodSearchLevel()
               || this.thisMob.getHunger() <= this.thisMobFlags.getHungerSnackSearchLevel() && this.thisMob.hasFoodResource()
         );
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         exitBranchGoHome = this.thisMob.getAIGoHome();
         exitBranchFood = this.thisMob.getAIFindFood();
         exitBranchHarvestFood = this.thisMob.getAIFindFoodHarvest();
         this.findFood();
      }
   }

   private void findFood() throws SlickException {
      if (this.thisMob.getHunger() < this.thisMob.getHungerMax()
         && (
            this.thisMob.getHunger() <= this.thisMobFlags.getHungerEatBreakawayLevel()
               || Utilities.randomInt(this.thisMobFlags.getHungerEatBreakawayChance()) != 0
         )) {
         if (this.thisMob.hasFoodResource() && !this.thisMob.hasCarriedResources()) {
            this.eatFoodInInventory();
         } else {
            if (this.thisMob.getHunger() <= this.thisMobFlags.getHungerHungryFoodSearchLevel()
               && this.thisMobFlags.getAllowHome()
               && this.thisMob.getAssignedHome() >= 0) {
               ResourceModule.ResourceType[] var4;
               for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
                  if (t.getTemplate().getFoodPerUse() != 0 && this.object.getObjects().get(this.thisMob.getAssignedHome()).getResourceCount(t) > 0) {
                     this.switchNode(exitBranchGoHome);
                     return;
                  }
               }
            }

            if (this.thisMob.getHunger() <= this.thisMobFlags.getHungerStarvationFoodSearchLevel()) {
               if (this.thisMob.hasCarriedResources()) {
                  if (this.thisMob.getInventoryFoodSlot() == null) {
                     for (ResourceBase r : this.thisMob.getCarriedResourcesList()) {
                        if (r.getType().getTemplate().getFoodPerUse() > 0 && r.getType().getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.FOOD) {
                           this.thisMob.equipResource(r, r.getType().getTemplate().getResourceSlot());
                           return;
                        }
                     }
                  }

                  this.runDropResource();
                  return;
               }

               ResourceModule.ResourceType[] var19;
               for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
                  if (tx.getTemplate().getFoodPerUse() != 0 && this.resource.getResourceGround(tx, false) > 0) {
                     boolean foundFoodNearby = false;
                     ArrayList<ResourceBase> resourcesOnGround = this.resource
                        .getResourcesNearby(this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getHungerHungryFoodSearchDistance());

                     for (ResourceBase rx : resourcesOnGround) {
                        if (rx.getType() == tx) {
                           foundFoodNearby = true;
                           break;
                        }
                     }

                     if (foundFoodNearby) {
                        if (resourcesOnGround.size() > 0) {
                           this.resource
                              .cullResourcesByDistance(
                                 this.thisMob.getTileX(),
                                 this.thisMob.getTileY(),
                                 this.thisMobFlags.getHungerHungryFoodSearchDistance() * 2,
                                 resourcesOnGround,
                                 this.thisMobFlags.getBlockMapGroup()
                              );
                        }

                        if (resourcesOnGround.size() == 0) {
                           resourcesOnGround = this.resource.getResourcesOnGroundInVillage();
                        }

                        for (ResourceBase rxx : resourcesOnGround) {
                           if (rxx.getType() == tx) {
                              this.switchNode(exitBranchFood);
                              return;
                           }
                        }
                     }
                  }
               }

               ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();

               ResourceModule.ResourceType[] var22;
               for (ResourceModule.ResourceType txx : ResourceModule.ResourceType.values()) {
                  if (txx.getTemplate().getFoodPerUse() != 0) {
                     for (ObjectBase objectCheck : objectList) {
                        if ((
                              this.thisMob.getMobGroup() == objectCheck.getObjectFlags().getMobGroup()
                                 || this.thisMob.getFriendlyMobGroups().contains(objectCheck.getObjectFlags().getMobGroup())
                           )
                           && objectCheck.getObjectFlags().canBuildingStore(txx)
                           && this.thisMob.canReach(objectCheck, true)
                           && objectCheck.isInRange()
                           && objectCheck.isResourceAvailable(txx)) {
                           this.switchNode(exitBranchFood);
                           return;
                        }
                     }
                  }
               }

               for (OrderedPair o : this.pathFinder
                  .getCoordinatesInRange(
                     this.thisMob.getTileX(),
                     this.thisMob.getTileY(),
                     this.thisMobFlags.getHungerHungryFoodSearchDistance(),
                     false,
                     this.thisMobFlags.getBlockMapGroup()
                  )) {
                  if (this.resource.getResourceValues()[o.getX()][o.getY()] > 0
                     && this.resource.getResourceType()[o.getX()][o.getY()] == ResourceModule.ResourceType.RAW_VEGETABLE) {
                     this.switchNode(exitBranchHarvestFood);
                     return;
                  }
               }
            }

            this.switchNode(resetBranch);
         }
      } else {
         this.switchNode(resetBranch);
      }
   }
}
