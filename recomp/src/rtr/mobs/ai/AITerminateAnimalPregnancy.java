package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AITerminateAnimalPregnancy extends AIBase {
   private static final int FIND_PREGNANT_ANIMAL = 0;
   private static final int PATHFIND_TO_ANIMAL = 1;
   private static final int TERMINATE_PREGNANCY = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AITerminateAnimalPregnancy(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_TERMINATE_ANIMAL_PREGNANCY;
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
         failBranch = this.thisMob.getAICaptureSoul();
         if (!this.thisMob.getMobJobType().getJob().canTendAnimals()) {
            this.switchNode(failBranch);
         } else {
            switch (this.aiStep) {
               case 0:
                  this.findPregnantAnimal();
                  break;
               case 1:
                  this.pathfindAnimal();
                  break;
               case 2:
                  this.terminatePregnancy();
            }
         }
      }
   }

   private void findPregnantAnimal() throws SlickException {
      ArrayList<MobBase> animals = this.mob.getMobsByGroup(MobBase.MobGroup.DOMESTICATED_ANIMAL, false);
      if (animals.size() == 0) {
         this.switchNode(failBranch);
      } else {
         ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
         HashMap<MobBase.MobType, Integer> totalAvailableOccupancyByType = new HashMap<>();

         for (ObjectBase o : objectList) {
            if (o.isInRange()) {
               ArrayList<MobBase.MobType> occupantsTypes = o.getObjectFlags().getOccupantsTypes();
               if (occupantsTypes.size() != 0) {
                  for (MobBase.MobType t : occupantsTypes) {
                     if (!totalAvailableOccupancyByType.containsKey(t)) {
                        totalAvailableOccupancyByType.put(t, 0);
                     }

                     int totalAvailable = o.getObjectFlags().getOccupantsMax() - o.getOccupants().size();
                     totalAvailableOccupancyByType.put(t, totalAvailableOccupancyByType.get(t) + totalAvailable);
                  }
               }
            }
         }

         ArrayList<MobBase.MobType> animalsToCull = new ArrayList<>();

         for (MobBase.MobType t : totalAvailableOccupancyByType.keySet()) {
            if (totalAvailableOccupancyByType.get(t) <= 0) {
               animalsToCull.add(t);
            }
         }

         if (animalsToCull.size() == 0) {
            this.switchNode(failBranch);
         } else {
            ArrayList<MobBase> workingFarmers = this.mob.getMobsByAssignment(Assignment.AssignmentType.TERMINATE_ANIMAL_PREGNANCY, false);
            ArrayList<MobBase> pregnantAnimals = new ArrayList<>();

            label86:
            for (MobBase m : animals) {
               if (m.isPregnant() && animalsToCull.contains(m.getMobType()) && this.thisMob != m && this.thisMob.canReach(m) && this.thisMob.isAvailable()) {
                  for (MobBase w : workingFarmers) {
                     if (w.getAssignment().getPrimaryMob() == m) {
                        continue label86;
                     }
                  }

                  pregnantAnimals.add(m);
               }
            }

            if (pregnantAnimals.size() <= 0) {
               this.switchNode(failBranch);
            } else {
               OrderedPair ox = this.pathFinder
                  .searchForMob(this.thisMob.getTileX(), this.thisMob.getTileY(), pregnantAnimals, false, true, this.thisMobFlags.getBlockMapGroup());
               MobBase target = this.pathFinder.mobFetch(pregnantAnimals, ox.getX(), ox.getY());
               if (target == null) {
                  this.switchNode(failBranch);
               } else {
                  Assignment assignment = this.thisMob.getAssignment();
                  assignment.reset();
                  assignment.setPrimaryMob(target);
                  assignment.setAssignmentType(Assignment.AssignmentType.TERMINATE_ANIMAL_PREGNANCY);
                  assignment.finalizeAssignment();
                  this.requestPath(ox.getX(), ox.getY());
                  this.aiStep = 1;
               }
            }
         }
      }
   }

   private void pathfindAnimal() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      Text.setVariableText("mobName", assignment.getPrimaryMobName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.terminateAnimalPregnancy.pathfindToAnimal"));
      this.followMob(assignment.getPrimaryMob());
      if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void terminatePregnancy() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      Text.setVariableText("mobName", assignment.getPrimaryMobName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.terminateAnimalPregnancy.terminatingPregnancy"));
      MobBase targetMob = assignment.getPrimaryMob();
      if (targetMob != null && targetMob.isAvailable() && this.thisMob.canReach(targetMob) && targetMob.isPregnant()) {
         if (!Utilities.coordinatesWithin(this.thisMob.getTileX(), this.thisMob.getTileY(), targetMob.getTileX(), targetMob.getTileY(), 1)) {
            this.thisMob.resetWorkDelayTick();
            this.requestPath(assignment.getPrimaryMob().getTileX(), assignment.getPrimaryMob().getTileY());
            this.aiStep = 1;
         } else {
            boolean usingTools = this.hasPreferredTools();
            this.thisMob.setPositionInteract(usingTools, false);
            int tileX = this.thisMob.getTileX();
            int tileY = this.thisMob.getTileY();
            int targetTileX = targetMob.getTileX();
            int targetTileY = targetMob.getTileY();
            if (targetTileX == tileX + 1 && targetTileY == tileY) {
               this.thisMob.setPosition(MobBase.MobPosition.EAST);
            } else if (targetTileX == tileX - 1 && targetTileY == tileY) {
               this.thisMob.setPosition(MobBase.MobPosition.WEST);
            } else if (targetTileX == tileX && targetTileY == tileY + 1) {
               this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
            } else if (targetTileX == tileX && targetTileY == tileY - 1) {
               this.thisMob.setPosition(MobBase.MobPosition.NORTH);
            }

            this.thisMob.runWorkDelayTick(1);
            this.thisMob.doWork(1, 1, 1, 1, 0);
            if (this.thisMob.getWorkDelayTick() % 50 == 0) {
               targetMob.takeDamage(
                  1, this.thisMob.getMobX(), this.thisMob.getMobY(), this.thisMob, MobBase.DamageType.REGULAR, MobBase.DamageCause.TERMINATING_PREGNANCY, true
               );
            }

            if (this.thisMob.getWorkDelayTick() > this.mobJob.getCalculatedWorkTime(this.thisMob)) {
               this.thisMob.resetWorkDelayTick();
               ResourceBase tool = this.thisMob.getInventoryToolSlot();
               if (tool != null) {
                  tool.decreaseUses(1);
               }

               targetMob.takeDamage(
                  (int)(targetMob.getHitPointsMax() * 0.25),
                  this.thisMob.getMobX(),
                  this.thisMob.getMobY(),
                  this.thisMob,
                  MobBase.DamageType.REGULAR,
                  MobBase.DamageCause.TERMINATING_PREGNANCY,
                  true
               );
               targetMob.setPregnant(false);
               targetMob.setSocialChildFather(null);
               targetMob.setSocialChildBlessed(false);
               this.stats.decreaseVillagerNewPregnanciesThisPeriod(this.thisMob.getMobType());
               this.thisMob.awardXP(Utilities.randomInt(3) + 3);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.03, GodModule.GodXPType.TERMINATING_ANIMAL_PREGNANCIES);
               }

               this.switchNode(resetBranch);
            }
         }
      } else {
         this.switchNode(resetBranch);
      }
   }
}
