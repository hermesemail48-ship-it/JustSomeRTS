package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIBirth extends AIBase {
   private static final int FIND_SAFE_LOCATION = 0;
   private static final int PATHFIND_TO_SAFE_LOCATION = 1;
   private static final int GIVE_BIRTH = 2;
   private static AIBase resetBranch;

   public AIBirth(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_BIRTH;
      this.interruptAggroMobs = true;
      this.interruptAggroBuildings = true;
      this.interruptStuck = true;
      this.interruptDeath = true;
      this.interruptOnFire = true;
      this.interruptFrozen = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowSocial()
         && this.thisMob.isPregnant()
         && this.thisMob.getSocialGestationDelayTick() >= this.thisMobFlags.getSocialGestationDelay();
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
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findSafeLocation();
                  break;
               case 1:
                  this.pathfindToSafeLocation();
                  break;
               case 2:
                  this.giveBirth();
            }
         }
      }
   }

   private void findSafeLocation() throws SlickException {
      ArrayList<ObjectBase> validClinics = new ArrayList<>();

      for (MobBase m : this.mob.getMobsByAssignment(Assignment.AssignmentType.MEDIC_WORK, false)) {
         if (m != this.thisMob) {
            ObjectBase o = m.getAssignment().getPrimaryObject();
            if (this.thisMob.canReach(o, true)
               && (!this.thisMob.getMobFlags().canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isObjectThreatened(o))
               && !validClinics.contains(o)) {
               validClinics.add(o);
            }
         }
      }

      if (validClinics.size() > 0) {
         OrderedPair closestPost = this.pathFinder
            .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), validClinics, false, true, this.thisMobFlags.getBlockMapGroup());
         if (closestPost != null) {
            OrderedPair validCoordinates = this.object.getBuildingAtLocation(closestPost).getRandomCoordinates(this.thisMob, true, true);
            if (validCoordinates != null) {
               Assignment assignment = this.thisMob.getAssignment();
               assignment.reset();
               assignment.setPrimaryCoordinates(closestPost);
               assignment.setPrimaryObject(this.object.getBuildingAtLocation(closestPost.getX(), closestPost.getY()));
               assignment.setAssignmentType(Assignment.AssignmentType.SEEKING_MEDIC);
               assignment.finalizeAssignment();
               this.requestPath(validCoordinates);
               this.aiStep = 1;
               return;
            }
         }
      }

      ArrayList<ObjectBase> objectsInRange = this.object.getObjectsNearby(this.thisMob, 16);

      for (int x = objectsInRange.size() - 1; x >= 0; x--) {
         if (objectsInRange.get(x).getObjectFlags().getBaseType().getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL) {
            objectsInRange.remove(x);
         }
      }

      if (objectsInRange.size() > 0) {
         objectsInRange = this.object
            .cullObjectsByDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), 160, objectsInRange, this.thisMobFlags.getBlockMapGroup());
      }

      if (objectsInRange.size() > 0) {
         this.thisMob.setCurrentTask(Text.getText("aiTask.birth.findSafeLocation"));
         ObjectBase randomObject = objectsInRange.get(Utilities.randomInt(objectsInRange.size()));
         OrderedPair coordinates = randomObject.getRandomCoordinates(this.thisMob, false, false);
         this.requestPath(coordinates);
         this.aiStep = 1;
      } else {
         this.aiStep = 2;
      }
   }

   private void pathfindToSafeLocation() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.birth.pathfindToSafeLocation"));
      this.runPath();
      if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void giveBirth() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      float mobX = this.thisMob.getMobX();
      float mobY = this.thisMob.getMobY();
      boolean atClinic = false;
      ObjectBase clinic = this.object.getBuildingAtLocation(tileX, tileY);
      if (clinic != null && clinic.getObjectFlags().getWorkerJobType().getJob().isMedic()) {
         for (MobBase m : this.mob.getMobsByAssignment(Assignment.AssignmentType.MEDIC_WORK, false)) {
            if (m != this.thisMob && m.getAssignment().getPrimaryObject() != null && m.getAssignment().getPrimaryObject() == clinic) {
               atClinic = true;
               break;
            }
         }
      }

      if (!atClinic && Utilities.randomInt(this.thisMobFlags.getSocialChildBirthChildDeathChance()) == 0) {
         Text.setVariableText("mobName", this.thisMob.getFormattedName(false));
         Console.newBanner(Text.getText("console.banner.mobAI.childBirthDeath.header"), Text.getText("console.banner.mobAI.childBirthDeath.subtitle"));
         if (this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER) {
            this.sound.playSound(SoundModule.SoundType.VILLAGER_DEATH);
         }
      } else {
         if (this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER) {
            this.sound.playSound(SoundModule.SoundType.VILLAGER_BIRTH);
         }

         if (Utilities.randomInt(this.thisMobFlags.getSocialTwinsChance()) == 0) {
            for (int x = 0; x < 2; x++) {
               this.mob.newVillagerBirth(tileX, tileY, this.thisMob, this.thisMob.getSocialChildFather());
               if (this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                  this.goal.incrementGoal(GoalModule.GoalType.DUGGAR);
               }
            }
         } else if (Utilities.randomInt(this.thisMobFlags.getSocialTripletsChance()) == 0) {
            for (int xx = 0; xx < 3; xx++) {
               this.mob.newVillagerBirth(tileX, tileY, this.thisMob, this.thisMob.getSocialChildFather());
               if (this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                  this.goal.incrementGoal(GoalModule.GoalType.DUGGAR);
               }
            }
         } else {
            this.mob.newVillagerBirth(tileX, tileY, this.thisMob, this.thisMob.getSocialChildFather());
            if (this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER) {
               this.goal.incrementGoal(GoalModule.GoalType.DUGGAR);
            }
         }
      }

      this.thisMob.setSocialChildBlessed(false);
      if (this.thisMobFlags.getAllowSleep()) {
         this.thisMob.decreaseEnergy(this.thisMobFlags.getSocialBirthEnergyDecrease());
      }

      if (this.thisMobFlags.getAllowHunger()) {
         this.thisMob.decreaseHunger(this.thisMobFlags.getSocialBirthHungerDecrease());
      }

      if (!atClinic) {
         this.thisMob.takeDamage((int)(this.thisMob.getHitPoints() * 0.25F), mobX, mobY, MobBase.DamageType.REGULAR, MobBase.DamageCause.CHILD_BIRTH);
      }

      if (!atClinic && this.thisMobFlags.getAllowAging() && this.thisMobFlags.getAgingMaximumAge() > 0) {
         if (Utilities.randomInt(2) == 0) {
            this.thisMob.decreaseLifeExpectancy(1);
         }

         if (this.thisMob.getLifeExpectancy() < this.thisMobFlags.getAgingMaximumAge() / 2 && Utilities.randomInt(4) == 0) {
            this.thisMob.takeDamage(this.thisMob.getHitPointsMax() * 2, mobX, mobY, MobBase.DamageType.REGULAR, MobBase.DamageCause.CHILD_BIRTH);
         } else if (this.thisMob.getLifeExpectancy() < (int)(this.thisMobFlags.getAgingMaximumAge() * 0.75) && Utilities.randomInt(8) == 0) {
            this.thisMob.takeDamage(this.thisMob.getHitPointsMax() * 2, mobX, mobY, MobBase.DamageType.REGULAR, MobBase.DamageCause.CHILD_BIRTH);
         }
      }

      this.thisMob.setPregnant(false);
      this.thisMob.awardXP(50 + Utilities.randomInt(100));
      if (this.thisMobFlags.getAllowGodExperience()) {
         this.god.increaseGodXP(1.0, GodModule.GodXPType.CHILD_BIRTH);
      }

      this.font.newFloater("iconPregnant", mobX + 4.0F, mobY);
      this.thisMob.resetSocialGestationDelayTick();
      this.switchNode(resetBranch);
   }
}
