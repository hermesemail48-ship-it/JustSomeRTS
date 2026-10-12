package rtr.mobs.ai.corruption;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.TimeModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.mobs.ai.AIBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIHostileBehaviorLeave extends AIBase {
   private static final int FIND_LEAVE_COORDINATES = 0;
   private static final int PATHFIND_TO_LEAVE_COORDINATES = 1;
   private static final int LEAVE_MAP = 2;

   public AIHostileBehaviorLeave(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_MONSTER_LEAVE;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowHostileBehavior()
         && this.time.getTimeOfDay() != TimeModule.TimeOfDay.NIGHT
         && this.time.getTimeOfDay() != TimeModule.TimeOfDay.DUSK
         && !this.time.isEclipse()
         && this.corruption.isTileCorrupted(this.thisMob.getTileX(), this.thisMob.getTileY())
         && this.spawn.tooManyDaytimeMobs();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getHostileBehaviorLeaveChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         switch (this.aiStep) {
            case 0:
               this.findLeaveCoordinates();
               break;
            case 1:
               this.pathfindToLeaveCoordinates();
               break;
            case 2:
               this.leaveMap();
         }
      }
   }

   private void findLeaveCoordinates() throws SlickException {
      OrderedPair o = this.getLeaveCoordinates();
      this.requestPath(o.getX(), o.getY());
      this.aiStep = 1;
   }

   private void pathfindToLeaveCoordinates() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.hostileBehaviorLeave.pathfindToLeaveCoordinates"));
      this.runPath();
      if (this.pathFailed) {
         this.aiStep = 0;
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
            this.thisMob.fadeOut();
         }
      }
   }

   private void leaveMap() throws SlickException {
      if (this.thisMob.getAlpha() <= 0.0F) {
         this.thisMob.unassignFromAll(false);
         this.resource.deleteMobInventory(this.thisMob);
         this.mob.deleteMob(this.thisMob);
      }
   }

   public OrderedPair getLeaveCoordinates() {
      int distance = 8;
      ArrayList<OrderedPair> validCoordinates = this.pathFinder
         .getCoordinatesInRange(this.thisMob.getTileX(), this.thisMob.getTileY(), distance, false, this.thisMobFlags.getBlockMapGroup());
      validCoordinates.add(OrderedPair.getOrderedPair(this.thisMob.getTileX(), this.thisMob.getTileY()));
      if (validCoordinates.size() == 0) {
         return null;
      } else {
         ArrayList<OrderedPair> validFilteredCoordinates = new ArrayList<>();

         for (OrderedPair o : validCoordinates) {
            if (this.corruption.isTileCorrupted(o.getX(), o.getY())) {
               validFilteredCoordinates.add(o);
            }
         }

         return validFilteredCoordinates.size() == 0
            ? validCoordinates.get(Utilities.randomInt(validCoordinates.size()))
            : validFilteredCoordinates.get(Utilities.randomInt(validFilteredCoordinates.size()));
      }
   }
}
