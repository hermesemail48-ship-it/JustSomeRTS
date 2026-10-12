package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AISpawnRepair extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_SPAWNER = 1;
   private static final int REPAIR = 2;
   private static AIBase resetBranch;

   public AISpawnRepair(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_SPAWN_REPAIR;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowSpawnRepair();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getSpawnRepairChance()) == 0 && this.isSpawnDamaged();
   }

   private boolean isSpawnDamaged() {
      ObjectBase mobSpawn = this.object.getObjects().get(this.thisMob.getAssignedSpawn());
      if (mobSpawn == null) {
         return false;
      } else {
         return mobSpawn.getHitPoints() >= mobSpawn.getHitPointsMax() && mobSpawn.getFireDamageArray().size() <= 0
            ? false
            : this.thisMob.canReach(mobSpawn, false);
      }
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findWork();
                  break;
               case 1:
                  this.pathfindToSpawner();
                  break;
               case 2:
                  this.repair();
            }
         }
      }
   }

   private void findWork() throws SlickException {
      if (this.isSpawnDamaged()) {
         ObjectBase mobSpawn = this.object.getObjects().get(this.thisMob.getAssignedSpawn());
         if (mobSpawn == null) {
            this.switchNode(resetBranch);
            return;
         }

         OrderedPair randomCoordinates = mobSpawn.getRandomCoordinates(this.thisMob, true, false);
         Assignment assignment = this.thisMob.getAssignment();
         assignment.reset();
         assignment.setPrimaryObject(mobSpawn);
         assignment.setPrimaryCoordinates(randomCoordinates);
         assignment.setAssignmentType(Assignment.AssignmentType.REPAIRING);
         assignment.finalizeAssignment();
         this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
         this.aiStep = 1;
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void pathfindToSpawner() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.spawnRepair.pathfindToSpawner"));
      this.runPath();
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject != null && !currentObject.isDestroyed()) {
         if (this.pathCompleted) {
            this.aiStep = 2;
         } else if (this.pathFailed || !this.thisMob.canReach(this.goalCoordinates, false)) {
            if (currentObject != null && this.thisMob.canReach(currentObject, false)) {
               this.thisMob.getAssignment().setPrimaryCoordinates(currentObject.getBestRandomInteractCoordinates(this.thisMob, true, true, true));
               this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
            } else {
               this.switchNode(resetBranch);
            }

            return;
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void repair() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.spawnRepair.repair"));
      Assignment assignment = this.thisMob.getAssignment();
      int workX = assignment.getPrimaryX();
      int workY = assignment.getPrimaryY();
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ObjectBase currentObject = assignment.getPrimaryObject();
      if (currentObject == null || currentObject.isDestroyed()) {
         this.switchNode(resetBranch);
      } else if (workX == tileX && workY == tileY) {
         this.thisMob.setPositionInteract(false, true);
         byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
         if (blockMap[tileX + 1][tileY] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(tileX + 1, tileY)) {
            this.thisMob.setPosition(MobBase.MobPosition.EAST);
         } else if (blockMap[tileX - 1][tileY] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(tileX - 1, tileY)) {
            this.thisMob.setPosition(MobBase.MobPosition.WEST);
         } else if (blockMap[tileX][tileY + 1] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(tileX, tileY + 1)) {
            this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
         } else if (blockMap[tileX][tileY - 1] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(tileX, tileY - 1)) {
            this.thisMob.setPosition(MobBase.MobPosition.NORTH);
         }

         this.thisMob.runWorkDelayTick(1);
         this.thisMob.doWork(1, 1, 1, 1, 0);
         if (this.thisMob.getWorkDelayTick() % 75 == 0) {
            this.sound.playSound(SoundModule.SoundType.BUILDING, this.thisMob.getMobX(), this.thisMob.getMobY());
         }

         if (this.thisMob.getWorkDelayTick() % 35 == 0) {
            int attackX = assignment.getPrimaryX();
            int attackY = assignment.getPrimaryY();
            if (blockMap[attackX + 1][attackY] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(attackX + 1, attackY)) {
               attackX++;
            } else if (blockMap[attackX - 1][attackY] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(attackX - 1, attackY)) {
               attackX--;
            } else if (blockMap[attackX][attackY + 1] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(attackX, attackY + 1)) {
               attackY++;
            } else if (blockMap[attackX][attackY - 1] > 0 && assignment.getPrimaryObject().isBuildingAtLocation(attackX, attackY - 1)) {
               attackY--;
            }

            double xDiff = this.thisMob.getCenterPixelX(true) - attackX * 16 + 8;
            double yDiff = this.thisMob.getCenterPixelY(true) - attackY * 16 + 8;
            float angle = (float)Math.toDegrees(Math.atan2(yDiff, xDiff));
            int randomParticles = Utilities.randomInt(2) + 1;

            for (int x = 0; x < randomParticles; x++) {
               this.particle
                  .newParticleGroup(
                     this.thisMob.getCenterPixelX(true),
                     this.thisMob.getCenterPixelY(true),
                     angle,
                     ParticleModule.ParticleGroup.DUST,
                     ParticleModule.ParticleSet.BLACK
                  );
               this.particle
                  .newParticleGroup(
                     this.thisMob.getCenterPixelX(true),
                     this.thisMob.getCenterPixelY(true),
                     ParticleModule.ParticleGroup.SMOKE_BELLOW,
                     ParticleModule.ParticleSet.GRAY
                  );
            }
         }

         if (this.thisMob.getWorkDelayTick() > this.thisMobFlags.getSpawnRepairTime()) {
            if (currentObject.getHitPoints() < currentObject.getHitPointsMax()) {
               this.thisMob.awardXP(Utilities.randomInt(2) + 2);
               if (this.thisMobFlags.getAllowGodExperience()) {
                  this.god.increaseGodXP(0.01, GodModule.GodXPType.REPAIRING_BUILDINGS);
               }

               currentObject.repair(5);
            }

            this.thisMob.resetWorkDelayTick();
            this.thisMob.getAssignment().reset();
            this.aiStep = 0;
         }
      } else {
         this.switchNode(resetBranch);
      }
   }
}
