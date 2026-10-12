package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIMigrationPreperationBehavior extends AIBase {
   private static final int FIND_LEAVE_COORDINATES = 0;
   private static final int PATHFIND_TO_LEAVE_COORDINATES = 1;
   private static final int BECOME_MIGRANT = 2;

   public AIMigrationPreperationBehavior(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_MIGRATION_PREPERATION_BEHAVIOR;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowMigrationPreperationBehavior();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getMigrationPreperationChance()) == 0 && this.canLeave();
   }

   private boolean canLeave() {
      if (!this.migration.canMobPrepareToMigrate()) {
         return false;
      } else if (this.time.getTimeOfDay() != this.thisMobFlags.getMigrationPreperationTimeOfDay()) {
         return false;
      } else if (this.thisMob.getMate() != null || this.thisMob.getOwner() != null) {
         return false;
      } else {
         return !this.migration.isMobQualifiedToLeave(this.thisMob) ? false : this.canMigrateIncludeEnRoute();
      }
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         switch (this.aiStep) {
            case 0:
               this.findNearestWayStation();
               break;
            case 1:
               this.pathfindToLeaveCoordinates();
               break;
            case 2:
               this.becomeMigrant();
         }
      }
   }

   private void findNearestWayStation() throws SlickException {
      ArrayList<ObjectBase> objectList = this.object.getObjectsArrayList();
      ArrayList<ObjectBase> validBuilding = new ArrayList<>();

      for (ObjectBase o : objectList) {
         if (this.thisMob.canReach(o) && o.isInRange() && o.getObjectFlags().getMigrationsPerDayProvided() != 0) {
            validBuilding.add(o);
         }
      }

      if (validBuilding.size() == 0) {
         this.switchNode(this.thisMob.getAIStart());
      } else {
         OrderedPair targetCoordinates = this.pathFinder
            .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), validBuilding, false, true, this.thisMobFlags.getBlockMapGroup());
         ObjectBase target = this.object.getBuildingAtLocation(targetCoordinates);
         OrderedPair interactionCoordinates = target.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
         Assignment assignment = this.thisMob.getAssignment();
         assignment.reset();
         assignment.setPrimaryCoordinates(interactionCoordinates);
         assignment.setPrimaryObject(target);
         assignment.setAssignmentType(Assignment.AssignmentType.VILLAGER_BECOMING_MIGRANTS);
         assignment.finalizeAssignment();
         this.requestPath(interactionCoordinates);
         this.aiStep = 1;
      }
   }

   private void pathfindToLeaveCoordinates() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.migrationPreperationBehavior.pathfindToLeaveCoordinates"));
      this.runPath();
      if (!this.canMigrateIncludeEnRoute()) {
         this.switchNode(this.thisMob.getAIStart());
      } else if (this.pathFailed) {
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private boolean canMigrateIncludeEnRoute() {
      ArrayList<MobBase> migratingMobs = this.mob.getMobsByType(MobBase.MobType.HUMAN_MIGRANT_DEPARTING, false);
      migratingMobs.addAll(this.mob.getMobsByType(MobBase.MobType.CATJEET_MIGRANT_DEPARTING, false));
      migratingMobs.addAll(this.mob.getMobsByType(MobBase.MobType.HUMAN_MIGRANT_NEPHILIM_DEPARTING, false));
      migratingMobs.addAll(this.mob.getMobsByType(MobBase.MobType.CATJEET_MIGRANT_NEPHILIM_DEPARTING, false));
      migratingMobs.addAll(this.mob.getMobsByAssignment(Assignment.AssignmentType.VILLAGER_BECOMING_MIGRANTS, false));
      migratingMobs.remove(this.thisMob);
      return migratingMobs.size() >= this.migration.getTotalRequestedPopulation()
         ? false
         : this.migration.getTotalMigratedToday() + migratingMobs.size() < this.object.getMaximumMigrationsPerDay();
   }

   private void becomeMigrant() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase building = this.object.getBuildingAtLocation(tileX, tileY);
      if (building != null && this.thisMob.getAssignment().getPrimaryObject() == building) {
         this.mob.spawnMob(this.thisMob, this.thisMobFlags.getMigrationPreperationMobType(), Text.getText("console.banner.mob.migrantPrepared.subtitle"), true);
         if (this.thisMobFlags.getAllowGodExperience()) {
            this.god.increaseGodXP(0.05, GodModule.GodXPType.MIGRANTS_PREPARING);
         }
      } else {
         this.aiStep = 0;
      }
   }
}
