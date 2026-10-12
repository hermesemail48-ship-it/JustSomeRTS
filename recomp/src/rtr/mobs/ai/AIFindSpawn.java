package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIFindSpawn extends AIBase {
   private static final int FIND_SPAWN = 0;
   private static final int PATHFIND_TO_SPAWN = 1;
   private static final int CLAIM_SPAWN = 2;
   private static AIBase resetBranch;

   public AIFindSpawn(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_FIND_SPAWN;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowSpawnSearch() && this.thisMob.getAssignedSpawn() == -1;
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getSpawnSearchChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findSpawn();
                  break;
               case 1:
                  this.pathfindToSpawn();
                  break;
               case 2:
                  this.claimSpawn();
            }
         }
      }
   }

   private void findSpawn() throws SlickException {
      ArrayList<ObjectBase> possibleSpawns = new ArrayList<>();
      ArrayList<ObjectBase> objectList = null;
      if (this.thisMobFlags.getSpawnSearchDistance() != -1) {
         objectList = this.object.getObjectsNearby(this.thisMob, this.thisMobFlags.getSpawnSearchDistance());
      } else {
         objectList = this.object.getObjectsArrayList();
      }

      for (ObjectBase o : objectList) {
         if (o.getCombobulatorSpawns().size() < o.getObjectFlags().getCombobulatorMax()
            && o.getObjectFlags().getCombobulatorSpawnTypes().contains(this.thisMob.getMobType())
            && this.thisMob.canReach(o)) {
            possibleSpawns.add(o);
         }
      }

      if (this.thisMobFlags.getSpawnSearchDistance() != -1 && possibleSpawns.size() > 0) {
         possibleSpawns = this.object
            .cullObjectsByDistance(
               this.thisMob.getTileX(),
               this.thisMob.getTileY(),
               this.thisMobFlags.getSpawnSearchDistance() * 2,
               possibleSpawns,
               this.thisMobFlags.getBlockMapGroup()
            );
      }

      if (possibleSpawns.size() > 0) {
         this.thisMob.setCurrentTask(Text.getText("aiTask.findSpawn.findSpawn"));
         ObjectBase attemptSpawns = possibleSpawns.get(Utilities.randomInt(possibleSpawns.size()));
         byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());
         int randomX = attemptSpawns.getObjectX() + Utilities.randomInt(attemptSpawns.getObjectFlags().getWidth());

         int randomY;
         for (randomY = attemptSpawns.getObjectY() + Utilities.randomInt(attemptSpawns.getObjectFlags().getHeight());
            blockMap[randomX][randomY] > 0 || !this.thisMob.canReach(randomX, randomY);
            randomY = attemptSpawns.getObjectY() + Utilities.randomInt(attemptSpawns.getObjectFlags().getHeight())
         ) {
            randomX = attemptSpawns.getObjectX() + Utilities.randomInt(attemptSpawns.getObjectFlags().getWidth());
         }

         Assignment assignment = this.thisMob.getAssignment();
         assignment.reset();
         assignment.setPrimaryObject(attemptSpawns);
         assignment.setPrimaryCoordinates(OrderedPair.getOrderedPair(randomX, randomY));
         assignment.setAssignmentType(Assignment.AssignmentType.FINDING_SPAWN);
         assignment.finalizeAssignment();
         this.requestPath(assignment.getPrimaryCoordinates());
         this.aiStep = 1;
      } else if (this.thisMobFlags.getSpawnSearchDieIfFail()) {
         this.thisMob.decreaseHitPoints(this.thisMob.getHitPointsMax(), MobBase.DamageType.REGULAR, MobBase.DamageCause.NONE);
      } else {
         this.switchNode(resetBranch);
      }
   }

   private void pathfindToSpawn() throws SlickException {
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.findSpawn.pathfindToSpawn"));
      ObjectBase currentObject = this.thisMob.getAssignment().getPrimaryObject();
      if (currentObject != null && !currentObject.isDestroyed()) {
         if (currentObject.getCombobulatorSpawns().size() >= currentObject.getObjectFlags().getCombobulatorMax()) {
            this.aiStep = 0;
         } else {
            this.runPath();
            if (this.pathFailed) {
               this.switchNode(resetBranch);
            } else {
               if (this.pathCompleted) {
                  this.aiStep = 2;
               }
            }
         }
      } else {
         this.aiStep = 0;
      }
   }

   private void claimSpawn() throws SlickException {
      ObjectBase attemptSpawns = this.thisMob.getAssignment().getPrimaryObject();
      Text.setVariableText("buildingName", this.thisMob.getAssignment().getPrimaryObjectName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.findSpawn.claimSpawn"));
      attemptSpawns = this.object.getObjects().get(attemptSpawns.getID());
      if (attemptSpawns != null && attemptSpawns.getCombobulatorSpawns().size() < attemptSpawns.getObjectFlags().getCombobulatorMax()) {
         if (this.object.getObjects().get(this.thisMob.getAssignedSpawn()) != null) {
            this.object.getObjects().get(this.thisMob.getAssignedSpawn()).getCombobulatorSpawns().remove(this.thisMob);
         }

         this.thisMob.setAssignedSpawn(attemptSpawns.getID());
         attemptSpawns.getCombobulatorSpawns().add(this.thisMob);
         this.switchNode(resetBranch);
      } else {
         this.aiStep = 0;
      }
   }
}
