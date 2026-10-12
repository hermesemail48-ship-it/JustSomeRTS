package rtr.mobs.ai.corruption;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.TimeModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.ai.AIBase;
import rtr.objects.ObjectBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIHostileBehaviorAttack extends AIBase {
   private static final int GET_HOSTILE_BEAHVIOR = 0;
   private static final int PATHFIND_TO_TARGET = 1;
   private static AIBase resetBranch;

   public AIHostileBehaviorAttack(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_HOSTILE_BEHAVIOR_ATTACK;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowHostileBehavior();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun()
         && !this.time.isFullMoonDay()
         && (this.time.getTimeOfDay() == TimeModule.TimeOfDay.NIGHT || this.time.getTimeOfDay() == TimeModule.TimeOfDay.DUSK || this.time.isEclipse())
         && Utilities.randomInt(this.thisMobFlags.getHostileBehaviorAttackChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         switch (this.aiStep) {
            case 0:
               this.getHostileBehavior();
               break;
            case 1:
               this.pathfindToTarget();
         }
      }
   }

   private void getHostileBehavior() throws SlickException {
      if (this.thisMobFlags.combatCanAttackMobs() && Utilities.randomInt(2) == 0) {
         ArrayList<MobBase> mobVillagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
         ArrayList<MobBase> mobList = new ArrayList<>();
         ArrayList<MobBase> mobListCantReach = new ArrayList<>();

         for (int e = 0; e < mobVillagers.size(); e++) {
            MobBase villager = mobVillagers.get(e);
            if (villager != this.thisMob && villager.isAvailable()) {
               if (this.thisMob.canReach(villager)) {
                  mobList.add(villager);
               } else {
                  mobListCantReach.add(villager);
               }
            }
         }

         if (mobList.size() > 0) {
            MobBase mob = mobList.get(Utilities.randomInt(mobList.size()));
            this.coordinates = OrderedPair.getOrderedPair(mob.getTileX(), mob.getTileY());
            this.tryHostileBehaviorPack();
            this.requestPath(this.coordinates);
            this.aiStep = 1;
            return;
         }

         if (mobListCantReach.size() > 0) {
            MobBase mob = mobListCantReach.get(Utilities.randomInt(mobListCantReach.size()));
            this.coordinates = OrderedPair.getOrderedPair(mob.getTileX(), mob.getTileY());
            this.tryHostileBehaviorPack();
            this.requestPath(this.coordinates);
            this.aiStep = 1;
            return;
         }
      } else if (this.thisMobFlags.combatCanAttackBuildings()) {
         ArrayList<ObjectBase> buildingsList = this.object.getObjectsArrayList();
         ArrayList<ObjectBase> buildingList = new ArrayList<>();
         ArrayList<ObjectBase> buildingListCantReach = new ArrayList<>();

         for (ObjectBase o : buildingsList) {
            if (o.isInRange() && !o.canNotBeAttacked() && o.getObjectFlags().getSubType() != ObjectBase.ObjectSubType.ABANDONED) {
               if (this.thisMob.canReach(o)) {
                  buildingList.add(o);
               } else {
                  buildingListCantReach.add(o);
               }
            }
         }

         if (buildingList.size() > 0) {
            ObjectBase building = buildingList.get(Utilities.randomInt(buildingList.size()));
            OrderedPair c = building.getRandomCoordinates(this.thisMob, true, false);
            this.coordinates = OrderedPair.getOrderedPair(c.getX(), c.getY());
            this.tryHostileBehaviorPack();
            this.requestPath(this.coordinates);
            this.aiStep = 1;
            return;
         }

         if (buildingListCantReach.size() > 0) {
            ObjectBase building = buildingListCantReach.get(Utilities.randomInt(buildingListCantReach.size()));
            OrderedPair c = building.getRandomCoordinates(this.thisMob, false, false);
            this.coordinates = OrderedPair.getOrderedPair(c.getX(), c.getY());
            this.tryHostileBehaviorPack();
            this.requestPath(this.coordinates);
            this.aiStep = 1;
            return;
         }
      }

      this.switchNode(resetBranch);
   }

   private void pathfindToTarget() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.hostileBehaviorAttack.pathfindToTarget"));
      this.runPath();
      if (this.tryBreakaway() || this.pathFailed || this.pathCompleted) {
         this.switchNode(resetBranch);
      }
   }

   private void tryHostileBehaviorPack() throws SlickException {
      if (this.thisMobFlags.getAllowHostileBehaviorPack() && this.tryPackBehavior()) {
         ArrayList<MobBase> nearbyMobs = this.mob
            .getMobsNearby(this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getHostileBehaviorPackDistance(), this.thisMob.getMobType());
         Collections.shuffle(nearbyMobs);
         int sendCount = 0;

         for (MobBase m : nearbyMobs) {
            if (m != this.thisMob && (m.isIdle() || m.getActiveNode() == m.getAIWander())) {
               sendCount++;
               m.interruptNode(m.getAIHostileBehaviorAttackOther());
            }

            if (sendCount >= this.thisMobFlags.getHostileBehaviorPackMaximum()) {
               break;
            }
         }
      }
   }

   public boolean tryPackBehavior() {
      return Utilities.randomInt(this.thisMobFlags.getHostileBehaviorPackChance()) == 0;
   }

   public boolean tryBreakaway() {
      return this.time.getTimeOfDay() == TimeModule.TimeOfDay.NIGHT || this.time.getTimeOfDay() == TimeModule.TimeOfDay.DUSK || this.time.isEclipse()
         ? false
         : Utilities.randomInt(this.thisMobFlags.getHostileBehaviorBreakawayChance()) == 0;
   }
}
