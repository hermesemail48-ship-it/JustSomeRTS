package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AISleepOutside extends AIBase {
   private static final int FIND_SAFE_LOCATION = 0;
   private static final int PATHFIND_TO_SAFE_LOCATION = 1;
   private static final int SLEEP_OUTSIDE = 2;
   private static AIBase resetBranch;

   public AISleepOutside(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_SLEEP_OUTSIDE;
      this.interruptAggroMobs = true;
      this.interruptStuck = true;
      this.interruptDeath = true;
      this.interruptOnFire = true;
      this.interruptFrozen = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowSleep();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun()
         && this.thisMob.getEnergy() <= this.thisMobFlags.getSleepOutsideEnergyLevel()
         && this.thisMobFlags.getCanSleepOutside()
         && Utilities.randomInt(this.thisMobFlags.getSleepOutsideChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findSafeLocation();
                  break;
               case 1:
                  this.pathfindToSafeLocation();
                  break;
               case 2:
                  this.sleepOutside();
            }
         }
      }
   }

   private void findSafeLocation() throws SlickException {
      ObjectBase targetObject = null;
      if (this.thisMobFlags.getCanSleepOutsidePreferHome() && this.thisMob.getAssignedHome() == -1) {
         ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
         if (home != null && this.thisMob.canReach(home)) {
            targetObject = home;
         }
      }

      if (targetObject == null) {
         ArrayList<ObjectBase> objectsInRange = this.object.getObjectsNearby(this.thisMob, 16);

         for (int x = objectsInRange.size() - 1; x >= 0; x--) {
            if (objectsInRange.get(x).getObjectFlags().getBaseType().getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL) {
               objectsInRange.remove(x);
            }
         }

         if (objectsInRange.size() > 0) {
            objectsInRange = this.object
               .cullObjectsByDistance(this.thisMob.getTileX(), this.thisMob.getTileY(), 160, objectsInRange, this.thisMobFlags.getBlockMapGroup());
         }

         if (objectsInRange.size() > 0) {
            this.thisMob.setCurrentTask(Text.getText("aiTask.sleepOutside.findSafeLocation"));
            targetObject = objectsInRange.get(Utilities.randomInt(objectsInRange.size()));
         }
      }

      if (targetObject == null) {
         this.aiStep = 2;
      } else {
         OrderedPair coordinates = targetObject.getRandomCoordinates(this.thisMob, true, false);
         this.requestPath(coordinates);
         this.aiStep = 1;
      }
   }

   private void pathfindToSafeLocation() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.sleepOutside.pathfindToSafeLocation"));
      this.runPath();
      if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            this.thisMob.setPositionSleep();
            this.thisMob.getSprite().restart();
            this.aiStep = 2;
         }
      }
   }

   private void sleepOutside() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.sleepOutside.sleepOutside"));
      this.thisMob.setPositionSleep();
      this.thisMob.runSleepDelayTick(1);
      if (this.thisMob.getSleepDelayTick() % 30 == 0 && Utilities.randomInt(5) == 0) {
         if (Utilities.randomInt(2) == 0) {
            this.font.newFloater("iconZ", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY() - 3.0F);
         } else {
            this.font.newFloater("iconZ", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY() - 3.0F, true);
         }
      }

      if (this.thisMob.getSleepDelayTick() > this.thisMobFlags.getSleepDelay()) {
         this.thisMob.increaseEnergy(1);
         this.thisMob.resetSleepDelayTick();
      }

      if (this.thisMob.getEnergy() > this.thisMobFlags.getSleepOutsideBreakawayLevel()
         && Utilities.randomInt(this.thisMobFlags.getSleepOutsideBreakawayChance()) == 0) {
         this.switchNode(resetBranch);
      }
   }
}
