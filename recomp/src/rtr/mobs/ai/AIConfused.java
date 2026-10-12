package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIConfused extends AIBase {
   private static final int BE_CONFUSED = 0;
   private static final int WANDER_AROUND = 1;
   private static AIBase resetBranch;

   public AIConfused(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CONFUSED;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowConfused();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getConfusedChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         if (Utilities.randomInt(100) == 0) {
            this.font.newFloater("iconConfused", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY() - 16.0F, true);
         }

         resetBranch = this.thisMob.getAIStart();
         switch (this.aiStep) {
            case 0:
               this.beConfused();
               break;
            case 1:
               this.wanderAround();
         }
      }
   }

   private void beConfused() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.confused.beConfused"));
      OrderedPair wanderTo = null;
      this.thisMob.setPositionFace();
      OrderedPair target = this.thisMob.getConfusedTarget();
      if (target != null) {
         float confusedX = target.getX() * 16 + 8;
         float confusedY = target.getY() * 16 + 8;
         float mobX = this.thisMob.getMobX();
         float mobY = this.thisMob.getMobY();
         float directionX = Math.abs(confusedX - mobX);
         float directionY = Math.abs(confusedY - mobY);
         if (directionX > directionY) {
            if (mobX > confusedX) {
               this.thisMob.setPosition(MobBase.MobPosition.WEST);
            } else {
               this.thisMob.setPosition(MobBase.MobPosition.EAST);
            }
         } else if (mobY > confusedY) {
            this.thisMob.setPosition(MobBase.MobPosition.NORTH);
         } else {
            this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
         }
      }

      if (Utilities.randomInt(this.thisMobFlags.getConfusedWanderChance()) == 0) {
         if (target != null) {
            int confusedX = target.getX() + Utilities.randomInt(-4, 4);
            int confusedY = target.getY() + Utilities.randomInt(-4, 4);
            this.requestPath(confusedX, confusedY);
            this.aiStep = 1;
            wanderTo = null;
         } else {
            wanderTo = this.getConfusedWanderCoordinates();
            if (wanderTo == null) {
               this.switchNode(resetBranch);
            } else {
               this.requestPath(wanderTo);
               this.aiStep = 1;
               wanderTo = null;
            }
         }
      } else if (Utilities.randomInt(this.thisMobFlags.getConfusedBreakAwayChance()) == 0) {
         this.switchNode(resetBranch);
      }
   }

   private void wanderAround() throws SlickException {
      if (this.thisMob.hasCarriedResources()) {
         this.runDropResource();
         if (!this.thisMob.hasCarriedResources()) {
            this.aiStep = 0;
         }
      } else {
         this.thisMob.setCurrentTask(Text.getText("aiTask.confused.wanderAround"));
         this.runPath();
         if (this.pathFailed || this.pathCompleted) {
            this.aiStep = 0;
         }
      }
   }

   public OrderedPair getConfusedWanderCoordinates() {
      int distance = this.thisMobFlags.getConfusedWanderDistance();
      ArrayList<OrderedPair> validCoordinates = this.pathFinder
         .getCoordinatesInRange(this.thisMob.getTileX(), this.thisMob.getTileY(), distance, false, this.thisMobFlags.getBlockMapGroup());
      return validCoordinates.size() == 0 ? null : validCoordinates.get(Utilities.randomInt(validCoordinates.size()));
   }
}
