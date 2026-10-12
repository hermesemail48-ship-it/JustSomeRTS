package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.utilities.Utilities;

public class AIWildDoggoWanderInOther extends AIBase {
   private static final int FIND_VILLAGE = 0;
   private static final int PATHFIND_TO_VILLAGE = 1;
   private static AIBase resetBranch;

   public AIWildDoggoWanderInOther(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_WILD_DOGGO_WANDER_IN_OTHER;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowWildDoggoWanderIn();
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
         switch (this.aiStep) {
            case 0:
               this.findVillage();
               break;
            case 1:
               this.pathfindToVillage();
         }
      }
   }

   private void findVillage() throws SlickException {
      ArrayList<ObjectBase> objectList = this.object.getObjectsArrayList();
      if (objectList.size() <= 0) {
         this.switchNode(resetBranch);
      } else {
         ArrayList<ObjectBase> randomObjects = new ArrayList<>();

         for (ObjectBase o : objectList) {
            if (this.thisMob.canReach(o)
               && o.isInRange()
               && o.getObjectFlags().getBaseType().getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL
               && o.getObjectFlags().getBaseDesirability() > 0) {
               randomObjects.add(o);

               for (int x = 0; x < o.getObjectFlags().getBaseDesirability(); x++) {
                  randomObjects.add(o);
               }
            }
         }

         if (randomObjects.size() <= 0) {
            this.switchNode(resetBranch);
         } else {
            ObjectBase building = randomObjects.get(Utilities.randomInt(randomObjects.size()));
            this.coordinates = building.getRandomCoordinates(this.thisMob, true, true);
            this.requestPath(this.coordinates);
            this.aiStep = 1;
         }
      }
   }

   private void pathfindToVillage() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.wildDoggoWanderIn.pathfindToVillage"));
      this.runPath();
      if (this.pathCompleted || this.pathFailed) {
         this.switchNode(resetBranch);
      }
   }
}
