package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.objects.rangebuilder.RangeBuilder;
import rtr.resources.ResourceBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIWander extends AIBase {
   private static final int FIND_WANDER_COORDINATES = 0;
   private static final int PATHFIND_TO_WANDER_COORDINATES = 1;
   private static AIBase resetBranch;
   private static AIBase exitBranchVisitMate;

   public AIWander(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_WANDER;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowWander();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getWanderChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         exitBranchVisitMate = this.thisMob.getAIVisitMate();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findWanderCoordinates();
                  break;
               case 1:
                  this.pathfindToWanderCoordinates();
            }
         }
      }
   }

   private void findWanderCoordinates() throws SlickException {
      boolean selectingBehavior = true;
      OrderedPair wanderTo = null;
      boolean blockAimlessly = false;
      boolean blockObjects = false;
      boolean blockMate = false;
      boolean blockOwner = false;
      boolean blockHome = false;
      boolean blockSpawn = false;
      boolean blockGhost = false;
      if (!this.thisMobFlags.canWanderAimlessly()) {
         blockAimlessly = true;
      }

      if (!this.thisMobFlags.canWanderObjects()) {
         blockObjects = true;
      }

      if (!this.thisMobFlags.canWanderHome()) {
         blockHome = true;
      }

      if (!this.thisMobFlags.canWanderMate()) {
         blockMate = true;
      }

      if (!this.thisMobFlags.canWanderOwner()) {
         blockOwner = true;
      }

      if (!this.thisMobFlags.canWanderSpawn()) {
         blockSpawn = true;
      }

      if (!this.thisMobFlags.canWanderGhost()) {
         blockGhost = true;
      }

      byte[][] blockMap = this.map.getMapData().getBlockMap(this.thisMobFlags.getBlockMapGroup());

      while (
         selectingBehavior
            && blockMap[this.thisMob.getTileX()][this.thisMob.getTileY()] <= 0
            && (!blockAimlessly || !blockObjects || !blockMate || !blockHome || !blockSpawn)
      ) {
         if (!blockAimlessly && this.thisMobFlags.canWanderAimlessly() && Utilities.randomInt(this.thisMobFlags.getWanderAimlesslyWeight()) == 0) {
            wanderTo = this.getWanderCoordinates(this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getWanderAimlesslyDistance());
            if (wanderTo == null) {
               blockAimlessly = true;
            } else {
               selectingBehavior = false;
            }
         } else if (blockObjects
            || !this.thisMobFlags.canWanderObjects()
            || Utilities.randomInt(this.thisMobFlags.getWanderObjectWeight()) != 0
               && (
                  this.thisMob.getMobJobType().getJob().isGuard()
                     || !this.thisMobFlags.getWanderPreferVillage()
                     || this.object.isInRange(this.thisMob.getTileX(), this.thisMob.getTileY())
               )
               && (
                  !this.corruption.isInitialized()
                     || !this.thisMobFlags.getWanderPreferCorruption()
                     || this.corruption.isTileCorrupted(this.thisMob.getTileX(), this.thisMob.getTileY())
               )) {
            if (!blockHome && this.thisMobFlags.canWanderHome() && Utilities.randomInt(this.thisMobFlags.getWanderHomeWeight()) == 0) {
               ObjectBase home = this.object.getObjects().get(this.thisMob.getAssignedHome());
               if (home != null && this.thisMob.canReach(home)) {
                  wanderTo = home.getRandomCoordinates(this.thisMob, true, false);
                  selectingBehavior = false;
               } else {
                  blockHome = true;
               }
            } else if (!blockMate && this.thisMobFlags.canWanderMate() && Utilities.randomInt(this.thisMobFlags.getWanderMateWeight()) == 0) {
               if (this.thisMob.getMate() == null) {
                  blockMate = true;
               } else {
                  if (this.thisMob.getAIVisitMate().allowRun()) {
                     this.switchNode(exitBranchVisitMate);
                     return;
                  }

                  blockMate = true;
               }
            } else if (!blockOwner && this.thisMobFlags.canWanderOwner() && Utilities.randomInt(this.thisMobFlags.getWanderOwnerWeight()) == 0) {
               if (this.thisMob.getOwner() != null && this.thisMob.canReach(this.thisMob.getOwner())) {
                  this.thisMob.getAssignment().setPrimaryMob(this.thisMob.getOwner());
                  wanderTo = OrderedPair.getOrderedPair(this.thisMob.getOwner().getTileX(), this.thisMob.getOwner().getTileY());
                  selectingBehavior = false;
               } else {
                  blockOwner = true;
               }
            } else if (blockSpawn
               || !this.thisMobFlags.canWanderSpawn()
               || Utilities.randomInt(this.thisMobFlags.getWanderSpawnWeight()) != 0
                  && this.object
                     .getObjectsNearby(this.thisMob, this.thisMobFlags.getWanderSpawnDistance())
                     .contains(this.object.getObjects().get(this.thisMob.getAssignedSpawn()))) {
               if (!blockGhost && this.thisMobFlags.canWanderGhost() && Utilities.randomInt(this.thisMobFlags.getWanderGhostWeight()) == 0) {
                  MobBase body = this.thisMob.getBoundGhostBody();
                  ResourceBase vessel = this.thisMob.getBoundGhostResource();
                  if (body == null && vessel == null) {
                     blockGhost = true;
                  } else {
                     if (body != null && this.thisMob.canReach(body)) {
                        wanderTo = this.getWanderCoordinates(body.getTileX(), body.getTileY(), this.thisMobFlags.getWanderGhostDistance());
                     } else if (vessel != null && this.thisMob.canReach(vessel)) {
                        wanderTo = this.getWanderCoordinates(vessel.getTileX(), vessel.getTileY(), this.thisMobFlags.getWanderGhostDistance());
                     }

                     selectingBehavior = false;
                  }
               }
            } else {
               ObjectBase spawn = this.object.getObjects().get(this.thisMob.getAssignedSpawn());
               if (spawn != null && this.thisMob.canReach(spawn)) {
                  wanderTo = spawn.getRandomCoordinates(this.thisMob, true, false);
                  selectingBehavior = false;
               } else {
                  blockSpawn = true;
               }
            }
         } else {
            ArrayList<ObjectBase> objectList = this.object.getObjectsArrayList();
            if (objectList.size() <= 0) {
               blockObjects = true;
            } else {
               ArrayList<ObjectBase> randomObjects = new ArrayList<>();

               for (ObjectBase o : objectList) {
                  if (this.thisMob.canReach(o)
                     && (
                        !this.thisMobFlags.getWanderPreferVillage()
                           || o.isInRange()
                              && o.getObjectFlags().getBaseType().getTileSetTemplate().getType() != MapTilesLoader.TileSetType.WALL
                              && o.getObjectFlags().getBaseDesirability() > 0
                     )
                     && (!this.corruption.isInitialized() || !this.thisMobFlags.getWanderPreferCorruption() || o.isOnCorruption())) {
                     randomObjects.add(o);
                     if (this.thisMobFlags.getWanderPreferVillage()) {
                        for (int x = 0; x < o.getObjectFlags().getBaseDesirability(); x++) {
                           randomObjects.add(o);
                        }
                     }
                  }
               }

               if (randomObjects.size() <= 0) {
                  blockObjects = true;
               } else {
                  ObjectBase objectCheck = randomObjects.get(Utilities.randomInt(randomObjects.size()));
                  wanderTo = objectCheck.getRandomCoordinates(this.thisMob, true, false);
                  selectingBehavior = false;
               }
            }
         }
      }

      if (wanderTo == null) {
         this.switchNode(resetBranch);
      } else {
         this.requestPath(wanderTo);
         wanderTo = null;
         this.aiStep = 1;
      }
   }

   private void pathfindToWanderCoordinates() throws SlickException {
      if (this.thisMob.getAssignment().getPrimaryMob() != null) {
         Text.setVariableText("mobName", this.thisMob.getAssignment().getPrimaryMob().getFormattedName(true));
         this.thisMob.setCurrentTask(Text.getText("aiTask.wander.pathfindToWanderCoordinatesMob"));
         this.followMob(this.thisMob.getAssignment().getPrimaryMob());
      } else {
         this.thisMob.setCurrentTask(Text.getText("aiTask.wander.pathfindToWanderCoordinates"));
         this.runPath();
      }

      if (this.pathFailed || this.pathCompleted) {
         this.switchNode(resetBranch);
      }
   }

   public OrderedPair getWanderCoordinates(int tileX, int tileY, int distance) {
      ArrayList<OrderedPair> validCoordinates = this.pathFinder.getCoordinatesInRange(tileX, tileY, distance, false, this.thisMobFlags.getBlockMapGroup());
      if (validCoordinates.size() == 0) {
         return null;
      } else {
         ArrayList<OrderedPair> validFilteredCoordinates = new ArrayList<>();
         if (!this.thisMob.getMobJobType().getJob().isGuard() && this.thisMobFlags.getWanderPreferVillage()) {
            for (OrderedPair o : validCoordinates) {
               validFilteredCoordinates.add(o);
               int amount = this.object.getRangeMap(RangeBuilder.RangeMapLayer.RANGE_AND_DESIRABILITY)[o.getX()][o.getY()];
               if (amount < 0) {
                  amount = 0;
               }

               for (int x = 0; x < amount; x++) {
                  validFilteredCoordinates.add(o);
               }
            }
         }

         if (this.corruption.isInitialized() && this.thisMobFlags.getWanderPreferCorruption()) {
            for (OrderedPair o : validCoordinates) {
               if (this.corruption.isTileCorrupted(o.getX(), o.getY())) {
                  validFilteredCoordinates.add(o);
               }
            }
         }

         return validFilteredCoordinates.size() == 0
            ? validCoordinates.get(Utilities.randomInt(validCoordinates.size()))
            : validFilteredCoordinates.get(Utilities.randomInt(validFilteredCoordinates.size()));
      }
   }
}
