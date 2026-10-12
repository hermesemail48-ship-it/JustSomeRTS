package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;

public class AISlaughterAnimal extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_ANIMALS = 1;
   private static final int SEARCH_NEARBY = 2;
   private static final int CHASE_ANIMAL = 3;
   private static final int GRAB_ANIMAL = 4;
   private static final int LIFT_ANIMAL = 5;
   private static AIBase resetBranch;
   private static AIBase failBranch;
   private static AIBase nextBranch;

   public AISlaughterAnimal(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_SLAUGHTER_ANIMAL;
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
         failBranch = this.thisMob.getAIBuyLaborer();
         nextBranch = this.thisMob.getAISlaughterAnimalInHand();
         if (!this.thisMob.getMobJobType().getJob().canWorkerSlaughter()) {
            this.switchNode(failBranch);
         } else {
            switch (this.aiStep) {
               case 0:
                  this.findWork();
                  return;
               case 1:
               case 2:
               default:
                  return;
               case 3:
                  this.chaseAnimal();
                  return;
               case 4:
                  this.grabAnimal();
                  return;
               case 5:
                  this.liftAnimal();
            }
         }
      }
   }

   private void findWork() throws SlickException {
      MobJobBase mobJob = this.thisMob.getMobJobType().getJob();
      ArrayList<ResourceModule.ResourceType> slaughterTypes = mobJob.getCanWorkerSlaughter();
      ArrayList<ResourceModule.ResourceType> validSlaughterTypes = new ArrayList<>();

      for (ResourceModule.ResourceType t : slaughterTypes) {
         if (!this.enoughWorkersSlaughtering(t)) {
            validSlaughterTypes.add(t);
         }
      }

      if (validSlaughterTypes.size() == 0) {
         this.switchNode(failBranch);
      } else {
         ArrayList<MobBase> validAnimals = new ArrayList<>();
         ArrayList<ObjectBase> validWorkBuildings = new ArrayList<>();
         ArrayList<MobBase> allButchers = this.mob.getMobsByAssignment(Assignment.AssignmentType.SLAUGHTERING_ANIMAL, false);

         for (ObjectBase o : this.object.getObjectMasterBuildingPriorityList()) {
            if (this.thisMob.canReach(o, true)) {
               ObjectFlags objectFlags = o.getObjectFlags();
               if (objectFlags.getWorkerJobType() == this.thisMob.getMobJobType()) {
                  validWorkBuildings.add(o);
               }

               if (objectFlags.getOccupantsMax() != 0) {
                  ArrayList<MobBase> occupants = o.getOccupants();
                  if (!(occupants.size() <= objectFlags.getOccupantsMax() * 0.75)) {
                     ArrayList<MobBase> assignedAnimals = new ArrayList<>();
                     int enroute = 0;

                     for (MobBase m : allButchers) {
                        if (m != this.thisMob) {
                           if (m.getAssignment().getPrimaryObject() == o) {
                              enroute++;
                           }

                           assignedAnimals.add(m.getAssignment().getPrimaryMob());
                        }
                     }

                     if (!(occupants.size() - enroute <= objectFlags.getOccupantsMax() * 0.75)) {
                        for (MobBase mx : occupants) {
                           if (mx.isAvailable()
                              && this.thisMob.canReach(mx, true)
                              && !assignedAnimals.contains(mx)
                              && mx.getMobFlags().canDropsAllowSlaughter()) {
                              for (ResourceModule.ResourceType tx : mx.getMobFlags().getDropsList().keySet()) {
                                 if (validSlaughterTypes.contains(tx) && !mx.isPregnant()) {
                                    validAnimals.add(mx);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         if (validWorkBuildings.size() == 0) {
            this.switchNode(failBranch);
         } else if (validAnimals.size() == 0) {
            this.switchNode(failBranch);
         } else {
            ArrayList<MobBase> maleAnimals = new ArrayList<>();
            ArrayList<MobBase> femaleAnimals = new ArrayList<>();

            for (MobBase mxx : validAnimals) {
               if (mxx.getGender() == MobBase.MobGender.MALE) {
                  maleAnimals.add(mxx);
               } else {
                  femaleAnimals.add(mxx);
               }
            }

            ArrayList<MobBase> finalValidAnimal;
            if (maleAnimals.size() > femaleAnimals.size()) {
               finalValidAnimal = maleAnimals;
            } else {
               finalValidAnimal = femaleAnimals;
            }

            Collections.sort(finalValidAnimal, new Comparator<MobBase>() {
               public int compare(MobBase m1, MobBase m2) {
                  return m1 == m2 ? 0 : m1.getAge() - m2.getAge();
               }
            });
            MobBase targetAnimal = finalValidAnimal.get(finalValidAnimal.size() - 1);
            ResourceModule.ResourceType targetAnimalResourceType = null;
            Collections.shuffle(validSlaughterTypes);

            for (ResourceModule.ResourceType txx : validSlaughterTypes) {
               if (targetAnimal.getMobFlags().getDropsList().containsKey(txx)) {
                  targetAnimalResourceType = txx;
                  break;
               }
            }

            OrderedPair deliveryObjectCoordinates = this.pathFinder
               .searchForObject(
                  this.thisMob.getTileX(), this.thisMob.getTileY(), validWorkBuildings, false, true, this.thisMob.getMobFlags().getBlockMapGroup()
               );
            ObjectBase deliveryObject = this.object.getBuildingAtLocation(deliveryObjectCoordinates);
            ObjectBase animalPen = this.object.getObjects().get(targetAnimal.getAssignedHome());
            Assignment assignment = this.thisMob.getAssignment();
            assignment.reset();
            assignment.setPrimaryMob(targetAnimal);
            assignment.setPickupResourceType(targetAnimalResourceType);
            assignment.setPrimaryObject(animalPen);
            assignment.setDeliveryObject(deliveryObject);
            assignment.setAssignmentType(Assignment.AssignmentType.SLAUGHTERING_ANIMAL);
            assignment.finalizeAssignment();
            this.requestPath(targetAnimal.getTileX(), targetAnimal.getTileY());
            this.aiStep = 3;
         }
      }
   }

   private void chaseAnimal() throws SlickException {
      Text.setVariableText("mobName", this.thisMob.getAssignment().getPrimaryMob().getFormattedName(true));
      this.thisMob.setCurrentTask(Text.getText("aiTask.slaughterAnimal.chaseAnimal"));
      this.followMob(this.thisMob.getAssignment().getPrimaryMob());
      if (this.pathFailed) {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 4;
         }
      }
   }

   private void grabAnimal() throws SlickException {
      MobBase m = this.thisMob.getAssignment().getPrimaryMob();
      ObjectBase o = this.thisMob.getAssignment().getDeliveryObject();
      if (o != null
         && this.thisMob.canReach(o, true)
         && Math.abs(this.thisMob.getTileX() - m.getTileX()) <= 1
         && Math.abs(this.thisMob.getTileY() - m.getTileY()) <= 1) {
         this.thisMob.setPositionPickup();
         if (this.thisMob.getSprite().isStopped()) {
            ResourceBase capturedAnimalResource = this.resource.createResourceUnassigned(ResourceModule.ResourceType.CAPTURED_ANIMAL);
            capturedAnimalResource.capture(m);
            this.thisMob.addCarriedResource(capturedAnimalResource, true);
            this.thisMob.getSprite().restart();
            this.thisMob.setPositionLift();
            this.aiStep = 5;
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void liftAnimal() throws SlickException {
      this.thisMob.setPositionLift();
      if (this.thisMob.getSprite().isStopped()) {
         ObjectBase pen = this.thisMob.getAssignment().getPrimaryObject();
         ResourceModule.ResourceType t = this.thisMob.getAssignment().getPickupResourceType();
         ObjectBase o = this.thisMob.getAssignment().getDeliveryObject();
         MobBase m = this.thisMob.getAssignment().getPrimaryMob();
         if (o != null && this.thisMob.canReach(o, true)) {
            this.switchNode(nextBranch);
            Assignment assignment = this.thisMob.getAssignment();
            assignment.setDeliveryObject(o);
            assignment.setPrimaryObject(pen);
            assignment.setPrimaryMob(m);
            assignment.setPickupResourceType(t);
            assignment.setAssignmentType(Assignment.AssignmentType.SLAUGHTERING_ANIMAL);
            assignment.finalizeAssignment();
         } else {
            this.switchNode(nextBranch);
         }
      }
   }

   private boolean enoughWorkersSlaughtering(ResourceModule.ResourceType resourceType) {
      if (this.resource.getResourceMakeAmount(resourceType) != ResourceModule.RESOURCE_INFINITE_AMOUNT
         && this.resource.getResourceMaintainAmount(resourceType) != ResourceModule.RESOURCE_INFINITE_AMOUNT) {
         int slaughterCount = 0;

         for (MobBase m : this.mob.getMobsByAssignment(Assignment.AssignmentType.SLAUGHTERING_ANIMAL, false)) {
            if (m.getAssignment() != null && m != this.thisMob) {
               Assignment assignment = m.getAssignment();
               ResourceModule.ResourceType assignmentResourceType = assignment.getPickupResourceType();
               if (assignmentResourceType == resourceType) {
                  slaughterCount += assignment.getPrimaryMob().getMobFlags().getDropsAttempts() - 1;
               }
            }
         }

         if (this.resource.getResourceMakeAmount(resourceType) == ResourceModule.RESOURCE_STORAGE_AMOUNT
            || this.resource.getResourceMaintainAmount(resourceType) == ResourceModule.RESOURCE_STORAGE_AMOUNT) {
            int storageSpace = this.resource.getResourceStoresSpace(resourceType);
            int resourcesOnGround = this.resource.getResourceGround(resourceType, true);
            int resourcesCarried = this.resource.getResourceCarried(resourceType);
            return storageSpace - resourcesOnGround - slaughterCount - resourcesCarried <= 0;
         } else {
            return this.resource.getResourceMakeAmount(resourceType) - slaughterCount <= 0
               && this.resource.getResourceMaintainAmount(resourceType) - this.resource.getResourceTotal(resourceType, true) - slaughterCount <= 0;
         }
      } else {
         return false;
      }
   }
}
