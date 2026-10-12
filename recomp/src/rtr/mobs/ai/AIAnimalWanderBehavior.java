package rtr.mobs.ai;

import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIAnimalWanderBehavior extends AIBase {
   private static final int FIND_BEST_WAYPOINT = 0;
   private static final int PATHFIND_TO_WAYPOINT = 1;
   private static AIBase resetBranch;

   public AIAnimalWanderBehavior(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_ANIMAL_WANDER_BEHAVIOR;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowAnimalWanderBehavior();
   }

   @Override
   public boolean tryRun() {
      if (!this.allowRun()) {
         return false;
      } else if (Utilities.randomInt(this.thisMobFlags.getAnimalWanderBehaviorChance()) != 0) {
         return false;
      } else {
         ArrayList<OrderedPair> allWaypoints = new ArrayList<>();

         for (ResourceModule.ResourceType t : this.thisMobFlags.getAnimalWanderBehaviorPreferredResources()) {
            ArrayList<OrderedPair> thisTypeWaypoints = this.waypoint.getAnimalResourceWaypoints(t);
            if (thisTypeWaypoints != null && thisTypeWaypoints.size() > 0) {
               allWaypoints.addAll(thisTypeWaypoints);
            }
         }

         if (allWaypoints.size() < 0) {
            return false;
         } else {
            for (OrderedPair o : allWaypoints) {
               if (Utilities.coordinatesWithin(o.getX(), o.getY(), this.thisMob.getTileX(), this.thisMob.getTileY(), 8)) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         switch (this.aiStep) {
            case 0:
               this.findBestWaypoint();
               break;
            case 1:
               this.pathfindToWaypoint();
         }
      }
   }

   private void findBestWaypoint() throws SlickException {
      HashMap<ResourceModule.ResourceType, ArrayList<OrderedPair>> allWaypoints = new HashMap<>();

      for (ResourceModule.ResourceType t : this.thisMobFlags.getAnimalWanderBehaviorPreferredResources()) {
         ArrayList<OrderedPair> thisTypeWaypoints = this.waypoint.getAnimalResourceWaypoints(t);
         if (thisTypeWaypoints != null && thisTypeWaypoints.size() > 0) {
            allWaypoints.put(t, thisTypeWaypoints);
         }
      }

      if (allWaypoints.size() <= 0) {
         this.switchNode(resetBranch);
      } else {
         ArrayList<OrderedPair> validWaypoints = new ArrayList<>();

         for (ArrayList<OrderedPair> list : allWaypoints.values()) {
            for (OrderedPair o : list) {
               if (this.thisMob.canReach(o) && !validWaypoints.contains(o)) {
                  validWaypoints.add(o);
               }
            }
         }

         if (validWaypoints.size() <= 0) {
            this.switchNode(resetBranch);
         } else {
            ArrayList<OrderedPair> idealWaypoints = new ArrayList<>();
            if (idealWaypoints.size() <= 0) {
               label69:
               for (OrderedPair ox : validWaypoints) {
                  if (!this.object.isInRange(ox.getX(), ox.getY()) && this.thisMobFlags.isAnimalWanderBehaviorPreferGroups()) {
                     ArrayList<MobBase> nearbyMobs = this.mob.getMobsNearby(ox.getX(), ox.getY(), 24, this.thisMob.getMobType());
                     if (nearbyMobs.size() < this.thisMobFlags.getAnimalWanderBehaviorPreferGroupsMaxiumumSize()) {
                        for (MobBase m : this.mob.getMobsNearby(ox.getX(), ox.getY(), 24, this.thisMob.getMobGroup())) {
                           if (m.getMobType() != this.thisMob.getMobType()) {
                              continue label69;
                           }
                        }

                        idealWaypoints.add(ox);
                     }
                  }
               }
            }

            if (idealWaypoints.size() <= 0) {
               idealWaypoints = validWaypoints;
            }

            OrderedPair closestWaypoint = this.pathFinder
               .searchFor(this.thisMob.getTileX(), this.thisMob.getTileY(), idealWaypoints, false, true, this.thisMobFlags.getBlockMapGroup());
            this.requestPath(closestWaypoint);
            this.aiStep = 1;
         }
      }
   }

   private void pathfindToWaypoint() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.animalWanderBehavior.pathfindToWaypoint"));
      this.runPath();
      if (this.pathCompleted || this.pathFailed) {
         this.switchNode(resetBranch);
      }
   }
}
