package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.TimeModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.ObjectModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIProvisionerPost extends AIBase {
   private static final int FIND_MARKETPLACE = 0;
   private static final int PATHFIND_TO_MARKETPLACE = 1;
   private static final int SELL_WARES = 2;
   private static final int PATHFIND_TO_NEW_LOCATION_AT_MARKETPLACE = 3;
   private static AIBase resetBranch;
   private static AIBase exitBranch;

   public AIProvisionerPost(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_PROVISIONER_POST;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowProvisionerBehavior();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getProvisionerWanderBehaviorChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         exitBranch = this.thisMob.getAIProvisionerLeave();
         if (this.attemptLeave()) {
            this.switchNode(exitBranch);
         } else {
            switch (this.aiStep) {
               case 0:
                  this.findMarketplace();
                  break;
               case 1:
                  this.pathfindToMarketplace();
                  break;
               case 2:
                  this.sellWares();
                  break;
               case 3:
                  this.pathfindToNewLocationAtMarketplace();
            }
         }
      }
   }

   private void findMarketplace() throws SlickException {
      ArrayList<ObjectBase> objectList = this.object.getObjectsBySortGroup(ObjectModule.ObjectSortGroup.PROVIDES_PROVISIONERS);
      ArrayList<ObjectBase> availableMarketplaces = new ArrayList<>();
      ArrayList<MobBase> catjeet = this.mob.getMobsByType(MobBase.MobType.CATJEET_PROVISIONER, false);

      for (ObjectBase o : objectList) {
         if (this.thisMob.canReach(o, true)) {
            int posted = 0;

            for (MobBase m : catjeet) {
               if (m.getAssignment().getAssignmentType() == Assignment.AssignmentType.CATJEET_SELLING_WARES && m.getAssignment().getPrimaryObject() == o) {
                  posted++;
               }
            }

            if (posted < o.getObjectFlags().getCatjeetProvisionersProvided()) {
               availableMarketplaces.add(o);
            }
         }
      }

      if (availableMarketplaces.size() == 0) {
         this.switchNode(exitBranch);
      } else {
         OrderedPair closestMarketplace = this.pathFinder
            .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), availableMarketplaces, false, true, this.thisMobFlags.getBlockMapGroup());
         if (closestMarketplace == null) {
            this.switchNode(resetBranch);
         } else {
            ObjectBase marketplace = this.object.getBuildingAtLocation(closestMarketplace);
            OrderedPair randomCoordinate = marketplace.getRandomCoordinates(this.thisMob, true, true);
            if (randomCoordinate != null) {
               Assignment assignment = this.thisMob.getAssignment();
               assignment.reset();
               assignment.setPrimaryCoordinates(randomCoordinate);
               assignment.setPrimaryObject(marketplace);
               assignment.setAssignmentType(Assignment.AssignmentType.CATJEET_SELLING_WARES);
               assignment.finalizeAssignment();
               this.requestPath(randomCoordinate);
               this.aiStep = 1;
            }
         }
      }
   }

   private void pathfindToMarketplace() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.provisionerPost.pathfindToMarketplace"));
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

   private void sellWares() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.provisionerPost.sellWares"));
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject != null && !currentObject.isDestroyed()) {
         if (Utilities.randomInt(this.thisMobFlags.getProvisionerWanderBehaviorChance()) == 0) {
            OrderedPair o = this.thisMob.getAssignment().getPrimaryObject().getRandomCoordinates(this.thisMob, true, true);
            if (o != null) {
               this.requestPath(o);
               this.aiStep = 3;
               return;
            }
         }

         ObjectBase marketplace = this.object.getBuildingAtLocation(this.thisMob.getTileX(), this.thisMob.getTileY());
         if (Utilities.randomInt(this.thisMobFlags.getProvisionerLeaveVillageChance()) == 0 || marketplace == null) {
            this.switchNode(resetBranch);
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void pathfindToNewLocationAtMarketplace() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.provisionerPost.sellWares"));
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

   private boolean attemptLeave() {
      if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.DUSK) {
         if (Utilities.randomInt(this.thisMobFlags.getProvisionerLeaveVillageChance()) == 0
            || this.thisMobFlags.getAllowHunger() && this.thisMob.getHunger() < this.thisMobFlags.getHungerHungryFoodSearchLevel()
            || this.thisMobFlags.getAllowThirst() && this.thisMob.getThirst() < this.thisMobFlags.getThirstThirstyWaterSearchLevel()) {
            return true;
         }
      } else if (this.thisMobFlags.getAllowHunger() && this.thisMob.getHunger() < this.thisMobFlags.getHungerStarvationFoodSearchLevel()
         || this.thisMobFlags.getAllowThirst() && this.thisMob.getThirst() < this.thisMobFlags.getThirstDehydrationWaterSearchLevel()
         || this.thisMobFlags.getAllowThermal() && this.thisMob.getBodyTemp() < this.thisMobFlags.getThermalFreeze()
         || this.thisMobFlags.getAllowThermal() && this.thisMob.getBodyTemp() > this.thisMobFlags.getThermalOverheat()) {
         return true;
      }

      return false;
   }
}
