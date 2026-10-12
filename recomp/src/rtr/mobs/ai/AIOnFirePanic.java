package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIOnFirePanic extends AIBase {
   private static final int FIND_FLEE_COORDINATES = 0;
   private static final int PATHFIND_TO_FLEE_COORDINATES = 1;
   private static AIBase resetBranch;

   public AIOnFirePanic(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_ON_FIRE_PANIC;
      this.interruptDeath = true;
      this.interruptStuck = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowOnFire() && this.thisMobFlags.getOnFireCanPanic() && this.thisMob.isOnFire();
   }

   @Override
   public boolean tryRun() throws SlickException {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getOnFirePanicChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         if (Utilities.randomInt(100) == 0) {
            this.font.newFloater("iconExcite", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY() - 16.0F, true);
         }

         if (Utilities.randomInt(100) == 0) {
            this.font.newFloater("iconConfused", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY() - 16.0F, true);
         }

         this.thisMob.setCurrentTask(Text.getText("aiTask.onFirePanic.onFire"));

         while (this.thisMob.hasCarriedResources()) {
            OrderedPair openSpace = this.resource.findOpenSpace(this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(openSpace.getX(), openSpace.getY(), this.thisMob.removeFirstCarriedResource());
         }

         switch (this.aiStep) {
            case 0:
               this.findFleeCoordinates();
               break;
            case 1:
               this.pathfindToFleeCoordinates();
         }
      }
   }

   protected void findFleeCoordinates() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      ArrayList<OrderedPair> allCoordinates = Utilities.getAllCoordinatesWithin(tileX, tileY, this.thisMobFlags.getOnFirePanicDistance());
      ArrayList<OrderedPair> waterCoordinates = new ArrayList<>();
      ArrayList<OrderedPair> validCoordinates = new ArrayList<>();

      for (OrderedPair o : allCoordinates) {
         if (this.thisMob.canReach(o)) {
            int tileID = this.map.getTileId(o.getX(), o.getY(), 6);
            if (this.map.getMapTileLoader().isTileWater(tileID)
               && !this.map.isTileFrozen(o.getX(), o.getY())
               && this.resource.getResourceValues()[o.getX()][o.getY()] > 0) {
               waterCoordinates.add(o);
            } else {
               validCoordinates.add(o);
            }
         }
      }

      OrderedPair ox = null;
      if (waterCoordinates.size() > 0) {
         ox = waterCoordinates.get(Utilities.randomInt(waterCoordinates.size()));
      } else {
         ox = validCoordinates.get(Utilities.randomInt(validCoordinates.size()));
      }

      this.requestPath(ox);
      this.aiStep = 1;
   }

   private void pathfindToFleeCoordinates() throws SlickException {
      this.runFleePath();
      if (this.pathFailed || this.pathCompleted || !this.thisMob.isOnFire()) {
         this.switchNode(resetBranch);
      }
   }
}
