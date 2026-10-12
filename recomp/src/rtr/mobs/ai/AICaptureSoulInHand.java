package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AICaptureSoulInHand extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_DEAD = 1;
   private static final int COLLECT_SOUL = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AICaptureSoulInHand(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CAPTURE_SOUL_IN_HAND;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMob.hasCarriedResources();
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
         failBranch = this.thisMob.getAIPlaceVesselInHand();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToDead();
               break;
            case 2:
               this.collectSoul();
         }
      }
   }

   private void findWork() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      if (assignment.getPrimaryMob() != null && assignment.getAssignmentType() == Assignment.AssignmentType.CAPTURING_SOUL) {
         this.requestPath(assignment.getPrimaryMob().getTileX(), assignment.getPrimaryMob().getTileY());
         this.aiStep = 1;
      } else {
         MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
         if (jobType == MobJobBase.MobJobType.NONE) {
            this.switchNode(failBranch);
         } else {
            MobJobBase job = jobType.getJob();
            if (!job.canCaptureSouls()) {
               this.switchNode(failBranch);
            } else {
               assignment.reset();
               ArrayList<MobBase> allDeadVillagers = this.mob.getDeadMobsByGroups(MobBase.MobGroup.VILLAGER);
               ArrayList<MobBase> allOccultistsEnroute = this.mob.getMobsByAssignment(Assignment.AssignmentType.CAPTURING_SOUL, false);
               ArrayList<MobBase> allValidDead = new ArrayList<>();

               label62:
               for (MobBase m : allDeadVillagers) {
                  if (this.thisMob.canReach(m, true)
                     && !this.map.getMapThreatMap().isTileThreatened(m.getTileX(), m.getTileY())
                     && m.getBoundGhostBody() != null) {
                     for (MobBase m2 : allOccultistsEnroute) {
                        if (m2.getAssignment().getPrimaryMob() == m) {
                           continue label62;
                        }
                     }

                     allValidDead.add(m);
                  }
               }

               if (allValidDead.size() == 0) {
                  this.switchNode(failBranch);
               } else {
                  OrderedPair nearestDeadCoordinates = this.pathFinder
                     .searchForMob(this.thisMob.getTileX(), this.thisMob.getTileY(), allValidDead, false, true, this.thisMobFlags.getBlockMapGroup());
                  if (nearestDeadCoordinates == null) {
                     this.switchNode(failBranch);
                  } else {
                     MobBase deadOut = this.pathFinder.mobFetch(allValidDead, nearestDeadCoordinates.getX(), nearestDeadCoordinates.getY());
                     assignment.setPrimaryMob(deadOut);
                     assignment.setPickupResourceType(this.thisMob.getCarriedResourcesListType());
                     assignment.setAssignmentType(Assignment.AssignmentType.CAPTURING_SOUL);
                     assignment.finalizeAssignment();
                     this.requestPath(deadOut.getTileX(), deadOut.getTileY());
                     this.aiStep = 1;
                  }
               }
            }
         }
      }
   }

   private void pathfindToDead() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      MobBase dead = assignment.getPrimaryMob();
      Text.setVariableText("resourceName", this.thisMob.getAssignment().getPickupResourceType().getTemplate().getName());
      Text.setVariableText("mobName", assignment.getPrimaryMobName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.captureSoulInHand.pathfindToDead"));
      this.runPath();
      if (this.pathCompleted) {
         this.aiStep = 2;
      } else if (this.pathFailed
         || dead == null
         || this.map.getMapThreatMap().isTileThreatened(dead.getTileX(), dead.getTileY())
         || !this.thisMob.canReach(dead, true)) {
         this.switchNode(failBranch);
      }
   }

   private void collectSoul() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      MobBase dead = assignment.getPrimaryMob();
      Text.setVariableText("mobName", assignment.getPrimaryMobName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.captureSoulInHand.captureSoul"));
      if (dead != null && dead.getTileX() == this.thisMob.getTileX() && dead.getTileY() == this.thisMob.getTileY()) {
         this.thisMob.setPositionInteract(false, true);
         this.thisMob.runWorkDelayTick(1);
         this.thisMob.doWork(0, 1, 1, 1, 0);
         int deadTileX = dead.getTileX();
         int deadTileY = dead.getTileY();
         int deadCenterX = dead.getCenterPixelX(true);
         int deadCenterY = dead.getCenterPixelY(true);
         int thisCenterX = this.thisMob.getCenterPixelX(true);
         int thisCenterY = this.thisMob.getCenterPixelY(true);
         if (this.thisMob.getWorkDelayTick() % 75 == 0) {
            this.sound.playSound(SoundModule.SoundType.MAGIC_ZAP, this.thisMob.getMobX(), this.thisMob.getMobY());
         }

         if (this.thisMob.getWorkDelayTick() % 35 == 0) {
            double xDiff = thisCenterX - deadTileX * 16 + 8;
            double yDiff = thisCenterY - deadTileY * 16 + 8;
            float angle = (float)Math.toDegrees(Math.atan2(yDiff, xDiff));
            int randomParticles = Utilities.randomInt(2) + 1;

            for (int x = 0; x < randomParticles; x++) {
               this.particle.newParticleGroup(thisCenterX, thisCenterY, angle, ParticleModule.ParticleGroup.DUST, ParticleModule.ParticleSet.BLACK);
               this.particle.newParticleGroup(thisCenterX, thisCenterY, ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.GRAY);
            }

            this.projectile
               .newEnergyBeam(deadCenterX + Utilities.randomInt(-25, 25), deadCenterY - 100 + Utilities.randomInt(-25, 25), deadCenterX, deadCenterY);
            this.projectile.newGroundStatic(deadCenterX, deadCenterY, ParticleModule.ParticleSet.MAGIC_BLUE);
            this.projectile.newGroundStatic(deadCenterX, deadCenterY, ParticleModule.ParticleSet.MAGIC_GREEN);
         }

         if (this.thisMob.getWorkDelayTick() == this.mobJob.getCalculatedWorkTime(this.thisMob) - 256) {
            this.projectile
               .newConstrictingEnergyRing(deadCenterX, deadCenterY, 64, 0.25F, ParticleModule.ParticleSet.MAGIC_RED, ParticleModule.ParticleSet.MAGIC_BLUE);
         }

         if (this.thisMob.getWorkDelayTick() > this.mobJob.getCalculatedWorkTime(this.thisMob)) {
            ResourceBase emptyVessel = this.thisMob.removeFirstCarriedResource();
            this.resource.deleteResource(emptyVessel);
            ResourceBase filledVessel = this.resource.createResourceUnassigned(ResourceModule.ResourceType.EERIE_VESSEL, emptyVessel.getColor());
            this.thisMob.addCarriedResource(filledVessel, true);
            this.goal.incrementGoal(GoalModule.GoalType.GHOST_TOWN);
            MobBase deadGhost = dead.getBoundGhostBody();

            while (dead != null && dead.getDecayTick() < 200000) {
               dead.poke();
            }

            deadGhost.setBoundGhost(filledVessel);
            filledVessel.setBoundGhost(deadGhost);
            this.thisMob.awardXP(Utilities.randomInt(25) + 5);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.1, GodModule.GodXPType.CAPTURING_SOULS);
            }

            this.switchNode(resetBranch);
         }
      } else {
         this.switchNode(failBranch);
      }
   }
}
