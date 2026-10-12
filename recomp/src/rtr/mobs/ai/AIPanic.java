package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.rangebuilder.RangeBuilder;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIPanic extends AIBase {
   private static final int FIND_PANIC_COORDINATES = 0;
   private static final int PATHFIND_TO_PANIC_COORDINATES = 1;
   private static AIBase exitBranchConfused;
   private static AIBase exitBranchInHouse;

   public AIPanic(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_PANIC;
      this.interruptDeath = true;
      this.interruptStuck = true;
      this.interruptOnFire = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowPanic();
   }

   @Override
   public boolean tryRun() throws SlickException {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getPanicChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         exitBranchConfused = this.thisMob.getAIConfused();
         exitBranchInHouse = this.thisMob.getAIInHomeHiding();
         if (Utilities.randomInt(100) == 0) {
            this.font.newFloater("iconExcite", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY() - 16.0F, true);
         }

         if (Utilities.randomInt(100) == 0) {
            this.font.newFloater("iconConfused", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY() - 16.0F, true);
         }

         this.thisMob.setCurrentTask(Text.getText("aiTask.panic.panicking"));

         while (this.thisMob.hasCarriedResources()) {
            OrderedPair openSpace = this.resource.findOpenSpace(this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getBlockMapGroup());
            this.resource.setResourcesOnGround(openSpace.getX(), openSpace.getY(), this.thisMob.removeFirstCarriedResource());
         }

         if (Utilities.randomInt(this.thisMobFlags.getPanicBreakAwayChance()) == 0) {
            this.switchNode(exitBranchConfused);
         } else {
            switch (this.aiStep) {
               case 0:
                  this.findPanicCoordinates();
                  break;
               case 1:
                  this.pathfindToPanicCoordinates();
            }
         }
      }
   }

   private void findPanicCoordinates() throws SlickException {
      if (this.thisMobFlags.getAllowPanicHomeSearch() && this.thisMobFlags.getAllowHome() && this.thisMob.getAssignedHome() != -1) {
         ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
         if (home != null && this.thisMob.canReach(home)) {
            ArrayList<ObjectBase> nearbyBuildings = this.object
               .getObjectsNearby(this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getPanicHomeSearchDisance(), this.thisMob.getMobGroup());
            if (nearbyBuildings.contains(home) || Utilities.randomInt(this.thisMobFlags.getPanicRunHomeChance()) == 0) {
               this.coordinates = home.getBestRandomInteractCoordinates(this.thisMob, true, true, true);
               this.requestPath(this.coordinates);
               this.aiStep = 1;
               return;
            }
         }
      }

      this.coordinates = this.onFleeRandom();
      this.requestPath(this.coordinates);
      this.aiStep = 1;
   }

   private void pathfindToPanicCoordinates() throws SlickException {
      this.runFleePath();
      if (this.pathFailed || this.pathCompleted) {
         ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
         if (this.thisMobFlags.getAllowPanicHomeSearch()
            && this.thisMobFlags.getCanEnterHome()
            && home != null
            && this.object.getBuildingAtLocation(this.thisMob.getTileX(), this.thisMob.getTileY()) == home
            && this.thisMob.getTileX() == this.coordinates.getX()
            && this.thisMob.getTileY() == this.coordinates.getY()) {
            this.thisMob.enterBuilding();
            this.switchNode(exitBranchInHouse);
         } else {
            this.aiStep = 0;
         }
      }
   }

   private OrderedPair onFleeRandom() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
      int[][] rangeMap = this.object.getRangeMap(RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY);
      if (blockMap[tileX][tileY] > 0) {
         return OrderedPair.getOrderedPair(tileX, tileY);
      } else {
         int radius = this.thisMobFlags.getPanicDistance();
         int villageCheckCount = 0;
         int randomX = tileX + Utilities.randomInt(radius * -1, radius);
         int randomY = tileY + Utilities.randomInt(radius * -1, radius);
         randomX = randomX > 255 ? 255 : (randomX < 0 ? 0 : randomX);

         for (randomY = randomY > 255 ? 255 : (randomY < 0 ? 0 : randomY);
            blockMap[randomX][randomY] > 0
               || villageCheckCount < 1000 && this.thisMobFlags.getCombatPrefersVillage() && rangeMap[randomX][randomY] > 0
               || !this.thisMob.canReach(randomX, randomY);
            villageCheckCount++
         ) {
            randomX = tileX + Utilities.randomInt(radius * -1, radius);
            randomY = tileY + Utilities.randomInt(radius * -1, radius);
            randomX = randomX > 255 ? 255 : (randomX < 0 ? 0 : randomX);
            randomY = randomY > 255 ? 255 : (randomY < 0 ? 0 : randomY);
         }

         return OrderedPair.getOrderedPair(randomX, randomY);
      }
   }
}
