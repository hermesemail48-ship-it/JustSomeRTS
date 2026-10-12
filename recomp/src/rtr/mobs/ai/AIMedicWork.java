package rtr.mobs.ai;

import java.util.ArrayList;
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

public class AIMedicWork extends AIBase {
   private static final int FIND_POST = 0;
   private static final int PATHFIND_TO_BUILDING = 1;
   private static final int POSTING = 2;
   private static final int PATHFIND_TO_NEW_POST_POSITION = 3;
   private static final int PATHFIND_TO_HURT_VILLAGER = 5;
   private static final int HEAL_SELF = 6;
   private static final int HEAL_VILLAGER = 7;
   private static AIBase resetBranch;
   private static AIBase failBranch;

   public AIMedicWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_MEDIC_WORK;
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
         failBranch = this.thisMob.getAIHarvestFarmWork();
         if (!this.thisMob.getMobJobType().getJob().isMedic()) {
            this.switchNode(failBranch);
         } else if (this.aiStep != 0 && !this.object.objectExists(this.thisMob.getAssignment().getPrimaryObject())) {
            this.aiStep = 0;
            this.thisMob.getAssignment().reset();
         } else {
            switch (this.aiStep) {
               case 0:
                  this.findPost();
                  break;
               case 1:
                  this.pathfindToBuilding();
                  break;
               case 2:
                  this.posting();
                  break;
               case 3:
                  this.pathfindToNewPostPosition();
               case 4:
               default:
                  break;
               case 5:
                  this.pathfindToHurtVillager();
                  break;
               case 6:
                  this.healSelf();
                  break;
               case 7:
                  this.healVillager();
            }
         }
      }
   }

   private void findPost() throws SlickException {
      ArrayList<ObjectBase> objectList = this.object.getObjectMasterBuildingPriorityList();
      ArrayList<ObjectBase> validClinics = new ArrayList<>();
      ArrayList<ObjectBase> validClinicsTopPriority = new ArrayList<>();

      for (ObjectBase o : objectList) {
         if ((o.getObjectFlags().getWorkerJobType().getJob().isMedic() || o.getObjectFlags().getWorkerJobType().getJob().isGuard())
            && this.thisMob.canReach(o, true)) {
            validClinics.add(o);
            validClinicsTopPriority.add(o);
         }
      }

      if (validClinics.size() <= 0) {
         this.switchNode(failBranch);
      } else {
         for (MobBase m : this.mob.getMobsByAssignment(Assignment.AssignmentType.MEDIC_WORK, false)) {
            ObjectBase ox = m.getAssignment().getPrimaryObject();
            validClinicsTopPriority.remove(ox);
         }

         OrderedPair closestPost = null;
         if (validClinicsTopPriority.size() > 0) {
            closestPost = this.pathFinder
               .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), validClinicsTopPriority, false, true, this.thisMobFlags.getBlockMapGroup());
         }

         if (closestPost == null) {
            closestPost = this.pathFinder
               .searchForObject(this.thisMob.getTileX(), this.thisMob.getTileY(), validClinics, false, true, this.thisMobFlags.getBlockMapGroup());
         }

         if (closestPost == null) {
            this.switchNode(failBranch);
         } else {
            OrderedPair validCoordinates = this.object.getBuildingAtLocation(closestPost).getRandomCoordinates(this.thisMob, true, true);
            if (validCoordinates != null) {
               Assignment assignment = this.thisMob.getAssignment();
               assignment.reset();
               assignment.setPrimaryCoordinates(closestPost);
               assignment.setPrimaryObject(this.object.getBuildingAtLocation(closestPost));
               assignment.setAssignmentType(Assignment.AssignmentType.MEDIC_WORK);
               assignment.finalizeAssignment();
               this.requestPath(validCoordinates);
               this.aiStep = 1;
            }
         }
      }
   }

   private void pathfindToBuilding() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.medicWork.pathfindToBuilding"));
      this.runPath();
      if (this.pathFailed) {
         this.aiStep = 0;
         this.thisMob.getAssignment().reset();
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void posting() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.medicWork.posting"));
      if (Utilities.randomInt(400) == 0) {
         OrderedPair newRandomCoordinate = this.thisMob.getAssignment().getPrimaryObject().getRandomCoordinates(this.thisMob, true, true);
         if (newRandomCoordinate != null) {
            this.requestPath(newRandomCoordinate);
            this.aiStep = 3;
            return;
         }
      }

      if (Utilities.randomInt(5000) == 0) {
         this.switchNode(failBranch);
      } else {
         if (Utilities.randomInt(50) == 0) {
            boolean healSelf = true;
            boolean isSelfHurt = this.thisMob.getHitPoints() < this.thisMob.getHitPointsMax();
            boolean isSelfSick = this.thisMob.isCompromised();
            if (!isSelfHurt && !isSelfSick) {
               healSelf = false;
            }

            if (healSelf
               && this.object.getBuildingAtLocation(this.thisMob.getTileX(), this.thisMob.getTileY()) != this.thisMob.getAssignment().getPrimaryObject()) {
               healSelf = false;
            }

            if (healSelf) {
               for (MobBase w : this.mob.getMobsByAssignment(Assignment.AssignmentType.MEDIC_WORK, false)) {
                  if (w != this.thisMob && w.getAssignment().getPrimaryMob() == this.thisMob) {
                     healSelf = false;
                     break;
                  }
               }

               if (isSelfSick || isSelfHurt) {
                  this.aiStep = 6;
                  return;
               }
            }

            ArrayList<MobBase> villagers = this.mob.getMobsByAssignment(Assignment.AssignmentType.SEEKING_MEDIC, false);
            if (villagers.size() == 0) {
               return;
            }

            ArrayList<MobBase> sickVillagers = new ArrayList<>();
            ArrayList<MobBase> hurtVillagers = new ArrayList<>();

            label129:
            for (MobBase m : villagers) {
               if (this.thisMob != m && this.thisMob.canReach(m) && this.thisMob.isAvailable()) {
                  boolean isHurt = m.getHitPoints() < m.getHitPointsMax();
                  boolean isSick = m.isCompromised();
                  if ((isHurt || isSick)
                     && this.thisMob.getAssignment().getPrimaryObject() == m.getAssignment().getPrimaryObject()
                     && this.object.getBuildingAtLocation(m.getTileX(), m.getTileY()) == this.thisMob.getAssignment().getPrimaryObject()) {
                     for (MobBase wx : this.mob.getMobsByAssignment(Assignment.AssignmentType.MEDIC_WORK, false)) {
                        if (wx.getAssignment().getPrimaryMob() == m) {
                           continue label129;
                        }
                     }

                     if (isSick) {
                        sickVillagers.add(m);
                     }

                     if (isHurt) {
                        hurtVillagers.add(m);
                     }
                  }
               }
            }

            if (hurtVillagers.size() <= 0 && sickVillagers.size() <= 0) {
               return;
            }

            OrderedPair o = null;
            MobBase target = null;
            if (sickVillagers.size() > 0) {
               o = this.pathFinder
                  .searchForMob(this.thisMob.getTileX(), this.thisMob.getTileY(), sickVillagers, false, true, this.thisMobFlags.getBlockMapGroup());
               target = this.pathFinder.mobFetch(sickVillagers, o.getX(), o.getY());
            }

            if (target == null && hurtVillagers.size() > 0) {
               o = this.pathFinder
                  .searchForMob(this.thisMob.getTileX(), this.thisMob.getTileY(), hurtVillagers, false, true, this.thisMobFlags.getBlockMapGroup());
               target = this.pathFinder.mobFetch(hurtVillagers, o.getX(), o.getY());
            }

            if (target == null) {
               return;
            }

            Assignment assignment = this.thisMob.getAssignment();
            assignment.setPrimaryMob(target);
            this.requestPath(o.getX(), o.getY());
            this.aiStep = 5;
         }
      }
   }

   private void pathfindToNewPostPosition() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.medicWork.posting"));
      this.runPath();
      if (this.pathFailed) {
         this.aiStep = 0;
         this.thisMob.getAssignment().reset();
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void pathfindToHurtVillager() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      Text.setVariableText("mobName", assignment.getPrimaryMobName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.medicWork.pathfindToHurtVillager"));
      this.followMob(assignment.getPrimaryMob());
      if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            this.aiStep = 7;
         }
      }
   }

   private void healSelf() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      this.thisMob.setCurrentTask(Text.getText("aiTask.medicWork.healSelf"));
      this.thisMob.setPositionInteract(this.hasPreferredTools(), false);
      this.thisMob.runWorkDelayTick(1);
      this.thisMob.doWork(1, 1, 1, 1, 0);
      if (this.thisMob.getHitPoints() >= this.thisMob.getHitPointsMax()
         && !this.thisMob.isPoisoned()
         && !this.thisMob.isDysentery()
         && !this.thisMob.isBlighted()) {
         this.aiStep = 2;
         this.thisMob.resetWorkDelayTick();
         this.thisMob.setPositionFace();
         assignment.resetPrimaryMob();
      } else {
         if (this.thisMob.getWorkDelayTick() > this.mobJob.getCalculatedWorkTime(this.thisMob)) {
            this.thisMob.resetWorkDelayTick();
            ResourceBase medkit = this.thisMob.getInventoryToolSlot();
            if (medkit != null) {
               medkit.decreaseUses(1);
               this.thisMob.increaseHitPoints((int)(this.thisMob.getHitPointsMax() * (medkit.getType().getTemplate().getHealthPercentPerUse() / 100.0F)));
            } else {
               this.thisMob.increaseHitPoints((int)(this.thisMob.getHitPointsMax() * 0.1F));
            }

            this.thisMob.resetBlightAmount();
            this.thisMob.resetPoisonedAmount();
            this.thisMob.resetDysenteryAmount();
            this.thisMob.awardXP(Utilities.randomInt(3) + 3);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.01, GodModule.GodXPType.HEALING_VILLAGERS);
            }

            if (this.thisMob.getHitPoints() >= this.thisMob.getHitPointsMax()
               && !this.thisMob.isPoisoned()
               && !this.thisMob.isDysentery()
               && !this.thisMob.isBlighted()) {
               this.aiStep = 2;
               this.thisMob.resetWorkDelayTick();
               this.thisMob.setPositionFace();
               assignment.resetPrimaryMob();
               return;
            }
         }
      }
   }

   private void healVillager() throws SlickException {
      Assignment assignment = this.thisMob.getAssignment();
      Text.setVariableText("mobName", assignment.getPrimaryMobName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.medicWork.healVillager"));
      MobBase targetMob = assignment.getPrimaryMob();
      if (targetMob != null
         && targetMob.isAvailable()
         && this.thisMob.canReach(targetMob)
         && targetMob.getAssignment().getAssignmentType() == Assignment.AssignmentType.SEEKING_MEDIC) {
         if (!Utilities.coordinatesWithin(this.thisMob.getTileX(), this.thisMob.getTileY(), targetMob.getTileX(), targetMob.getTileY(), 1)) {
            this.aiStep = 2;
            this.thisMob.resetWorkDelayTick();
            this.thisMob.setPositionFace();
            assignment.resetPrimaryMob();
         } else {
            this.thisMob.setPositionInteract(this.hasPreferredTools(), false);
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
            if (targetMob.getHitPoints() >= targetMob.getHitPointsMax() && !targetMob.isPoisoned() && !targetMob.isDysentery() && !targetMob.isBlighted()) {
               this.aiStep = 2;
               this.thisMob.resetWorkDelayTick();
               this.thisMob.setPositionFace();
               assignment.resetPrimaryMob();
            } else {
               if (this.thisMob.getWorkDelayTick() > this.mobJob.getCalculatedWorkTime(this.thisMob)) {
                  this.thisMob.resetWorkDelayTick();
                  ResourceBase medkit = this.thisMob.getInventoryToolSlot();
                  if (medkit != null) {
                     medkit.decreaseUses(1);
                     targetMob.increaseHitPoints((int)(targetMob.getHitPointsMax() * (medkit.getType().getTemplate().getHealthPercentPerUse() / 100.0F)));
                  } else {
                     targetMob.increaseHitPoints((int)(targetMob.getHitPointsMax() * 0.2F));
                  }

                  if (targetMob.getMobFlags().getAllowBlighted()) {
                     targetMob.resetBlightAmount();
                  }

                  if (targetMob.getMobFlags().getAllowPoisoned()) {
                     targetMob.resetPoisonedAmount();
                  }

                  if (targetMob.getMobFlags().getAllowDysentery()) {
                     targetMob.resetDysenteryAmount();
                  }

                  if (targetMob.getMobFlags().getAllowThermal()) {
                     if (targetMob.getBodyTemp() > targetMob.getMobFlags().getThermalOverheat()) {
                        targetMob.decreaseBodyTemp((int)(targetMob.getBodyTempMax() * 0.1F));
                     }

                     if (targetMob.getBodyTemp() < targetMob.getMobFlags().getThermalFreeze()) {
                        targetMob.increaseBodyTemp((int)(targetMob.getBodyTempMax() * 0.1F));
                     }
                  }

                  this.thisMob.awardXP(Utilities.randomInt(3) + 3);
                  if (this.thisMobFlags.getAllowGodExperience()) {
                     this.god.increaseGodXP(0.01, GodModule.GodXPType.HEALING_VILLAGERS);
                  }

                  if (targetMob.getHitPoints() >= targetMob.getHitPointsMax() && !targetMob.isPoisoned() && !targetMob.isDysentery() && !targetMob.isBlighted()
                     )
                   {
                     this.aiStep = 2;
                     this.thisMob.resetWorkDelayTick();
                     this.thisMob.setPositionFace();
                     assignment.resetPrimaryMob();
                     return;
                  }
               }
            }
         }
      } else {
         this.aiStep = 2;
         this.thisMob.resetWorkDelayTick();
         this.thisMob.setPositionFace();
         assignment.resetPrimaryMob();
      }
   }
}
