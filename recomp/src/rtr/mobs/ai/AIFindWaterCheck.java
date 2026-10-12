package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIFindWaterCheck extends AIBase {
   private static AIBase resetBranch;
   private static AIBase exitBranchFountain;
   private static AIBase exitBranchHarvest;

   public AIFindWaterCheck(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_FIND_WATER_CHECK;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowThirst();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun()
         && Utilities.randomInt(this.thisMobFlags.getThirstFindWaterChance()) == 0
         && (
            this.thisMob.getThirst() <= this.thisMobFlags.getThirstThirstyWaterSearchLevel()
               || this.thisMob.getThirst() <= this.thisMobFlags.getThirstSnackSearchLevel() && this.thisMob.hasWaterResource()
               || this.thisMob.getThirst() <= this.thisMobFlags.getThirstOverheatSearchLevel()
                  && this.thisMobFlags.getAllowThermal()
                  && this.thisMob.getBodyTemp() >= this.thisMobFlags.getThermalOverheat()
         );
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         exitBranchFountain = this.thisMob.getAIFindWaterFountain();
         exitBranchHarvest = this.thisMob.getAIFindWaterHarvest();
         if (this.thisMob.getThirst() < this.thisMob.getThirstMax()
            && (
               this.thisMob.getThirst() <= this.thisMobFlags.getThirstDrinkBreakawayLevel()
                  || Utilities.randomInt(this.thisMobFlags.getThirstDrinkBreakawayChance()) != 0
            )) {
            if (this.thisMob.hasWaterResource() && !this.thisMob.hasCarriedResources()) {
               this.drinkWaterInInventory();
            } else {
               if (this.thisMob.getThirst() <= this.thisMobFlags.getThirstThirstyWaterSearchLevel()
                  || this.thisMobFlags.getAllowThermal() && this.thisMob.getBodyTemp() >= this.thisMobFlags.getThermalOverheat()) {
                  ArrayList<ObjectBase> objectList = this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.WATER_SOURCES);
                  ArrayList<MobBase> drinkingVillagers = this.mob.getMobsByAssignment(Assignment.AssignmentType.DRINKING_FROM_FROUNTAIN, false);

                  for (ObjectBase objectCheck : objectList) {
                     if (objectCheck.getTotalWater() > 0
                        && (
                           this.thisMob.getMobGroup() == objectCheck.getObjectFlags().getMobGroup()
                              || this.thisMob.getFriendlyMobGroups().contains(objectCheck.getObjectFlags().getMobGroup())
                        )
                        && this.thisMob.canReach(objectCheck, true)
                        && objectCheck.isInRange()) {
                        int enroute = 0;

                        for (MobBase m : drinkingVillagers) {
                           if (m.getAssignment().getPrimaryObject() == objectCheck) {
                              enroute += m.getThirstMax() - m.getThirst();
                           }
                        }

                        if (enroute <= objectCheck.getTotalWater()) {
                           this.switchNode(exitBranchFountain);
                           return;
                        }
                     }
                  }
               }

               if (this.thisMob.getThirst() <= this.thisMobFlags.getThirstDehydrationWaterSearchLevel()
                  || this.thisMobFlags.getAllowThermal() && this.thisMob.getBodyTemp() >= this.thisMobFlags.getThermalOverheatCritical()) {
                  if (this.thisMob.hasCarriedResources()) {
                     if (this.thisMob.getInventoryWaterSlot() == null) {
                        for (ResourceBase r : this.thisMob.getCarriedResourcesList()) {
                           if (r.getType().getTemplate().getWaterPerUse() > 0
                              && r.getType().getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.WATER) {
                              this.thisMob.equipResource(r, r.getType().getTemplate().getResourceSlot());
                              return;
                           }
                        }
                     }

                     this.runDropResource();
                     return;
                  }

                  for (OrderedPair o : this.pathFinder
                     .getCoordinatesInRange(
                        this.thisMob.getTileX(),
                        this.thisMob.getTileY(),
                        this.thisMobFlags.getThirstThirstyWaterSearchDistance(),
                        false,
                        this.thisMobFlags.getBlockMapGroup()
                     )) {
                     if (this.resource.getResourceValues()[o.getX()][o.getY()] > 0
                        && (
                           this.resource.getResourceType()[o.getX()][o.getY()] == ResourceModule.ResourceType.DIRTY_WATER_BUCKET
                              || this.resource.getResourceType()[o.getX()][o.getY()] == ResourceModule.ResourceType.WATER_BUCKET
                        )) {
                        this.switchNode(exitBranchHarvest);
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
}
