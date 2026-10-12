package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.utilities.Utilities;

public class AIGolemGuardBehavior extends AIBase {
   private static AIBase exitBranchPatrol;
   private static AIBase exitBranchAttack;

   public AIGolemGuardBehavior(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_GOLEM_GUARD_BEHAVIOR;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowGolemBehavior() && this.object.isInRange(this.thisMob.getTileX(), this.thisMob.getTileY());
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getGolemBehaviorChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         exitBranchPatrol = this.thisMob.getAIGolemPatrolBehavior();
         exitBranchAttack = this.thisMob.getAICombatMobs();
         ArrayList<MobBase> enemyList = this.mob.getMobsByGroups(this.thisMob.getHostileMobGroups(), false);
         ArrayList<MobBase> friendlyList = this.mob.getMobsByGroup(this.thisMob.getMobGroup(), false);
         ArrayList<MobBase> mobListHighPriority = new ArrayList<>();
         ArrayList<MobBase> mobListLowPriority = new ArrayList<>();

         label81:
         for (MobBase m : enemyList) {
            if (this.thisMob.canReach(m.getTileX(), m.getTileY())
               && m.isAvailable()
               && m.getID() != this.thisMob.getID()
               && this.object.isInRange(m.getTileX(), m.getTileY())) {
               int attackers = 0;

               for (MobBase f : friendlyList) {
                  if (f.getAssignment().getPrimaryMob() == m) {
                     attackers++;
                  }

                  if (attackers > 5) {
                     continue label81;
                  }
               }

               mobListLowPriority.add(m);
               if (m.getAssignment().getPrimaryMob() != null && this.thisMob.getFriendlyMobGroups().contains(m.getAssignment().getPrimaryMob().getMobGroup())
                  || m.getAssignment().getPrimaryObject() != null
                     && this.thisMob.getFriendlyMobGroups().contains(m.getAssignment().getPrimaryObject().getObjectFlags().getMobGroup())) {
                  mobListHighPriority.add(m);
               }
            }
         }

         if (mobListHighPriority.size() == 0 && mobListLowPriority.size() == 0) {
            this.switchNode(exitBranchPatrol);
         } else {
            ArrayList<MobBase> idealTargets = new ArrayList<>();
            MobBase targetOut = null;
            if (mobListHighPriority.size() > 0) {
               idealTargets.addAll(mobListHighPriority);
               if (idealTargets.size() > 0) {
                  this.mob.sortMobsByDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), idealTargets);
                  targetOut = idealTargets.get(0);
               } else {
                  this.mob.sortMobsByDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), mobListHighPriority);
                  targetOut = mobListHighPriority.get(0);
               }
            } else {
               idealTargets.addAll(mobListLowPriority);
               if (idealTargets.size() > 0) {
                  this.mob.sortMobsByDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), idealTargets);
                  targetOut = idealTargets.get(0);
               } else {
                  this.mob.sortMobsByDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), mobListLowPriority);
                  targetOut = mobListLowPriority.get(0);
               }
            }

            this.switchNode(exitBranchAttack);
            this.thisMob.getAssignment().setPrimaryMob(targetOut);
         }
      }
   }
}
