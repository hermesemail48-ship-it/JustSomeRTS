package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.particles.ParticleModule;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIPray extends AIBase {
   private static final int FIND_WORK = 0;
   private static final int PATHFIND_TO_PRAY_SPOT = 1;
   private static final int PRAY = 2;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIPray(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_PRAY;
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
         failBranch = this.thisMob.getAIStart();
         switch (this.aiStep) {
            case 0:
               this.findWork();
               break;
            case 1:
               this.pathfindToPraySpot();
               break;
            case 2:
               this.pray();
         }
      }
   }

   private void findWork() throws SlickException {
      MobJobBase.MobJobType jobType = this.thisMob.getMobJobType();
      if (jobType == MobJobBase.MobJobType.NONE) {
         this.switchNode(resetBranch);
      } else {
         MobJobBase job = jobType.getJob();
         if (!job.canPray()) {
            this.switchNode(failBranch);
         } else {
            ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
            ArrayList<MobBase> mobArray = this.mob.getMobsByAssignment(Assignment.AssignmentType.PRAYING, false);
            ArrayList<OrderedPair> occupiedPrayCoordinates = new ArrayList<>();

            for (MobBase m : mobArray) {
               Assignment a = m.getAssignment();
               if (a.getPrimaryCoordinates() != null) {
                  occupiedPrayCoordinates.add(a.getPrimaryCoordinates());
               }
            }

            ArrayList<OrderedPair> allPrayCoordinates = new ArrayList<>();

            for (ObjectBase objectCheck : objectList) {
               if (!objectCheck.isPaused()
                  && objectCheck.getEnergy() < objectCheck.getObjectFlags().getEnergyMax() + objectCheck.getEnergyInRoute()
                  && (!this.thisMob.getMobFlags().canIgnoreThreatenedTiles() || !this.map.getMapThreatMap().isObjectThreatened(objectCheck))) {
                  ArrayList<OrderedPair> prayParking = objectCheck.getPrayPoints();
                  if (prayParking.size() > 0
                     && this.thisMob.canReach(objectCheck, true)
                     && this.thisMob.getFriendlyMobGroups().contains(objectCheck.getObjectFlags().getMobGroup())
                     && objectCheck.getObjectFlags().getSubType() != ObjectBase.ObjectSubType.ABANDONED) {
                     allPrayCoordinates.addAll(prayParking);
                  }
               }
            }

            allPrayCoordinates.removeAll(occupiedPrayCoordinates);
            if (allPrayCoordinates.size() == 0) {
               this.switchNode(failBranch);
            } else {
               final int energyDivsor = 20;
               Collections.sort(allPrayCoordinates, new Comparator<OrderedPair>() {
                  public int compare(OrderedPair o1, OrderedPair o2) {
                     if (o1 == o2) {
                        return 0;
                     } else {
                        ObjectBase a1 = AIPray.this.object.getBuildingAtLocation(o1);
                        ObjectBase a2 = AIPray.this.object.getBuildingAtLocation(o2);
                        int a1e = (a1.getEnergy() + a1.getEnergyInRoute()) / energyDivsor;
                        int a2e = (a2.getEnergy() + a2.getEnergyInRoute()) / energyDivsor;
                        return a1e - a2e;
                     }
                  }
               });
               ArrayList<OrderedPair> idealPrayCoordinates = new ArrayList<>();
               int baseLineEnergy = this.object.getBuildingAtLocation(allPrayCoordinates.get(0)).getEnergy() / energyDivsor;

               for (OrderedPair o : allPrayCoordinates) {
                  ObjectBase a1 = this.object.getBuildingAtLocation(o);
                  int a1e = (a1.getEnergy() + a1.getEnergyInRoute()) / energyDivsor;
                  if (a1e == baseLineEnergy) {
                     idealPrayCoordinates.add(o);
                  }
               }

               OrderedPair prayingSpot = this.pathFinder
                  .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), idealPrayCoordinates, false, true, this.thisMobFlags.getBlockMapGroup());
               ObjectBase thisBuilding = this.object.getBuildingAtLocation(prayingSpot);
               if (prayingSpot != null && thisBuilding != null) {
                  Assignment assignment = this.thisMob.getAssignment();
                  assignment.reset();
                  assignment.setPrimaryCoordinates(prayingSpot);
                  assignment.setPrimaryObject(thisBuilding);
                  assignment.setAssignmentType(Assignment.AssignmentType.PRAYING);
                  assignment.finalizeAssignment();
                  this.requestPath(prayingSpot);
                  this.aiStep = 1;
               } else {
                  this.switchNode(failBranch);
               }
            }
         }
      }
   }

   private void pathfindToPraySpot() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.pray.pathfindToPraySpot"));
      Assignment assignment = this.thisMob.getAssignment();
      ObjectBase currentObject = assignment.getPrimaryObject();
      if (currentObject != null && !currentObject.isDestroyed()) {
         if (this.thisMob.getMobFlags().canIgnoreThreatenedTiles() && this.map.getMapThreatMap().isObjectThreatened(currentObject)) {
            this.switchNode(resetBranch);
         } else {
            this.runPath();
            if (this.pathCompleted) {
               this.aiStep = 2;
            } else if (this.pathFailed || !this.thisMob.canReach(this.goalCoordinates, true)) {
               if (currentObject != null && this.thisMob.canReach(currentObject, true)) {
                  this.thisMob.getAssignment().setPrimaryCoordinates(currentObject.getBestRandomInteractCoordinates(this.thisMob, true, true, true));
                  this.requestPath(this.thisMob.getAssignment().getPrimaryCoordinates());
               } else {
                  this.switchNode(resetBranch);
               }

               return;
            }
         }
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void pray() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.pray.pray"));
      Assignment assignment = this.thisMob.getAssignment();
      ObjectBase currentObject = assignment.getPrimaryObject();
      if (currentObject != null && !currentObject.isDestroyed()) {
         this.thisMob.runWorkDelayTick(1);
         this.thisMob.doWork(3, 2, 2, 2, 0);
         if (Utilities.randomInt(100) == 0) {
            this.thisMob.setPosition(MobBase.MobPosition.values()[Utilities.randomInt(MobBase.MobPosition.values().length)]);
         }

         if (Utilities.randomInt(250) == 0) {
            switch (Utilities.randomInt(4)) {
               case 0:
                  this.font.newFloater("iconMusicNoteBlue", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
                  break;
               case 1:
                  this.font.newFloater("iconMusicNoteGreen", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
                  break;
               case 2:
                  this.font.newFloater("iconMusicNotePurple", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
                  break;
               case 3:
                  this.font.newFloater("iconMusicNoteWhite", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY());
            }

            if (Utilities.randomInt(10) == 0) {
               this.sound.playSound(SoundModule.SoundType.PRAY_CHANTING, this.thisMob.getMobX(), this.thisMob.getMobY());
            }
         }

         if (this.thisMob.getWorkDelayTick() > 1500) {
            this.thisMob.setPositionPrayFinishing();
         } else {
            this.thisMob.setPositionPray();
         }

         if (this.thisMob.getWorkDelayTick() > this.mobJob.getCalculatedWorkTime(this.thisMob) - 500 && this.thisMob.getWorkDelayTick() % 50 == 0) {
            for (int i = 0; i < 5; i++) {
               this.particle
                  .newParticleGroup(
                     this.thisMob.getCenterPixelX(true),
                     this.thisMob.getCenterPixelY(true),
                     ParticleModule.ParticleGroup.FLOATING_DUST,
                     ParticleModule.ParticleSet.FIRE_PURPLE
                  );
            }
         }

         if (this.thisMob.getWorkDelayTick() > this.mobJob.getCalculatedWorkTime(this.thisMob)) {
            if (currentObject.getEnergy() < currentObject.getObjectFlags().getEnergyMax() + currentObject.getEnergyInRoute()) {
               currentObject.increaseEnergy(Utilities.randomInt(21) + 25);
            } else {
               this.essence.newEssence(currentObject.getCenterPixelX(), currentObject.getCenterPixelY(), 3, 3);
            }

            this.thisMob.awardXP(Utilities.randomInt(5) + 5);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.01, GodModule.GodXPType.PRAYING);
            }

            if (this.thisMobFlags.getAllowFaith()) {
               this.thisMob.increaseFaith(2, true);
            }

            this.goal.incrementGoal(GoalModule.GoalType.PRAISE_BE_UNTO_THEE);
            this.mob.influenceFaithInArea(this.thisMob.getTileX(), this.thisMob.getTileY(), 4, 1, true);
            this.thisMob.resetWorkDelayTick();
            this.projectile
               .newExpandingEnergyRing(currentObject.getCenterPixelX(), currentObject.getCenterPixelY(), 46, 1.0F, ParticleModule.ParticleSet.FIRE_BLUE);
            this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
            this.switchNode(resetBranch);
         }
      } else {
         this.switchNode(resetBranch);
      }
   }
}
