package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIFindWaterFountain extends AIBase {
   private static final int FIND_WATER_FOUNTAIN = 0;
   private static final int PATHFIND_TO_WATER_FOUNTAIN = 1;
   private static final int DRINK_FROM_WATER_FOUNTAIN = 2;
   private static AIBase resetBranch;

   public AIFindWaterFountain(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_FIND_WATER_FOUNTAIN;
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
      return this.allowRun()
         && (
            this.thisMob.getThirst() <= this.thisMobFlags.getThirstThirstyWaterSearchLevel()
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
         if (!this.thisMob.hasCarriedResources()
            || this.thisMob.getCarriedResourcesListType().getTemplate().getWaterPerUse() > 0
               && this.thisMob.getCarriedResourcesListType().getTemplate().getResourceSlot() == ResourceModule.ResourceSlot.WATER) {
            switch (this.aiStep) {
               case 0:
                  this.findWaterFountain();
                  break;
               case 1:
                  this.pathfindToWaterFountain();
                  break;
               case 2:
                  this.drinkFromWaterFountain();
            }
         } else {
            this.runDropResource();
         }
      }
   }

   private void findWaterFountain() throws SlickException {
      ArrayList<ObjectBase> objectList = this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.WATER_SOURCES);
      ArrayList<ObjectBase> validWaterFountains = new ArrayList<>();
      ArrayList<ObjectBase> validWaterSources = new ArrayList<>();
      ArrayList<MobBase> drinkingVillagers = this.mob.getMobsByAssignment(Assignment.AssignmentType.DRINKING_FROM_FROUNTAIN, false);

      for (ObjectBase objectCheck : objectList) {
         if (objectCheck.getTotalWater() > 0 && this.thisMob.canReach(objectCheck, true) && objectCheck.isInRange()) {
            int enroute = 0;

            for (MobBase m : drinkingVillagers) {
               if (m.getAssignment().getPrimaryObject() == objectCheck) {
                  enroute += m.getThirstMax() - m.getThirst();
               }
            }

            if (enroute <= objectCheck.getTotalWater()) {
               if (objectCheck.getObjectFlags().isFountain()) {
                  validWaterFountains.add(objectCheck);
               } else {
                  validWaterSources.add(objectCheck);
               }
            }
         }
      }

      if (validWaterSources.size() == 0 && validWaterFountains.size() == 0) {
         this.switchNode(resetBranch);
      } else {
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         OrderedPair pickupCoordinates = null;
         if (validWaterFountains.size() > 0) {
            pickupCoordinates = this.pathFinder.searchForObject(tileX, tileY, validWaterFountains, false, true, this.thisMobFlags.getBlockMapGroup());
         } else {
            pickupCoordinates = this.pathFinder.searchForObject(tileX, tileY, validWaterSources, false, true, this.thisMobFlags.getBlockMapGroup());
         }

         ObjectBase pickupOut = this.object.getBuildingAtLocation(pickupCoordinates.getX(), pickupCoordinates.getY());
         if (pickupOut == null) {
            this.switchNode(resetBranch);
         } else {
            OrderedPair randomCoordinates = pickupOut.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            assignment.setPrimaryObject(pickupOut);
            assignment.setPrimaryCoordinates(randomCoordinates);
            assignment.setAssignmentType(Assignment.AssignmentType.DRINKING_FROM_FROUNTAIN);
            assignment.finalizeAssignment();
            this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
            this.aiStep = 1;
         }
      }
   }

   private void pathfindToWaterFountain() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.findWaterFountain.pathfindToWaterFountain"));
      this.runPath();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject == null || currentObject.isDestroyed()) {
         this.aiStep = 0;
      } else if (this.pathFailed) {
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void drinkFromWaterFountain() throws SlickException {
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject != null && !currentObject.isDestroyed()) {
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         if (currentObject != this.object.getBuildingAtLocation(tileX, tileY)) {
            this.aiStep = 0;
         } else if (currentObject.getTotalWater() <= 0) {
            this.aiStep = 0;
         } else {
            Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
            this.thisMob.setCurrentTask(Text.getText("aiTask.findWaterFountain.drinkFromWaterFountain"));
            if (this.thisMob.getThirst() < this.thisMob.getThirstMax()
               && (
                  this.thisMob.getThirst() <= this.thisMobFlags.getThirstDrinkBreakawayLevel()
                     || Utilities.randomInt(this.thisMobFlags.getThirstDrinkBreakawayChance()) != 0
               )) {
               this.thisMob.setPositionDrink();
               this.thisMob.runThirstDrinkTimeTick(1);
               if (this.thisMob.getThirstDrinkTimeTick() >= 10) {
                  currentObject.decreaseWater(1);
                  this.thisMob.drinkWater(1);
                  this.thisMob.resetThirstDrinkTimeTick();
               }
            } else {
               this.switchNode(resetBranch);
            }
         }
      } else {
         this.aiStep = 0;
      }
   }
}
