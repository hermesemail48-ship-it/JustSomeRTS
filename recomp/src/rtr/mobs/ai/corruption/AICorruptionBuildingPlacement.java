package rtr.mobs.ai.corruption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.ai.AIBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.system.gamemodetemplates.GameModeTemplateBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AICorruptionBuildingPlacement extends AIBase {
   private static final int FIND_AREA_TO_BUILD = 0;
   private static final int PATHFIND_TO_TARGET = 1;
   private static final int PLACE_BUILDING = 2;
   private static AIBase resetBranch;

   public AICorruptionBuildingPlacement(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CORRUPTION_BUILDING_PLACEMENT;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowCorruptionWork() && this.corruption.isTileCorrupted(this.thisMob.getTileX(), this.thisMob.getTileY());
   }

   @Override
   public boolean tryRun() {
      return this.allowRun()
         && (
            Utilities.randomInt(this.save.getActiveRegionalSave().getGameMode().getTemplate().getCorruptionBuildingRate()) == 0
               || this.object.getObjectsByGroup(MobBase.MobGroup.MONSTER).size() == 0
         );
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findAreaToBuild();
                  break;
               case 1:
                  this.pathfindToTarget();
                  break;
               case 2:
                  this.placeBuilding();
            }
         }
      }
   }

   private void findAreaToBuild() throws SlickException {
      ArrayList<OrderedPair> corruptionTiles = this.corruption.getCorruptionTilesArray();
      ArrayList<OrderedPair> reachableCorruptionTiles = new ArrayList<>();

      for (OrderedPair o : corruptionTiles) {
         if (this.thisMob.canReach(o) && this.object.getBuildingAtLocation(o) == null) {
            reachableCorruptionTiles.add(o);
         }
      }

      if (reachableCorruptionTiles.size() == 0) {
         this.switchNode(resetBranch);
      } else {
         this.coordinates = reachableCorruptionTiles.get(Utilities.randomInt(reachableCorruptionTiles.size()));
         this.requestPath(this.coordinates);
         this.aiStep = 1;
      }
   }

   private void pathfindToTarget() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.corruptionBuildingPlacement.pathfindToTarget"));
      this.runPath();
      if (this.pathFailed) {
         this.switchNode(resetBranch);
      }

      if (this.pathCompleted) {
         this.aiStep = 2;
      }
   }

   private void placeBuilding() throws SlickException {
      int tileX = this.thisMob.getTileX();
      int tileY = this.thisMob.getTileY();
      if (!this.canCreateNewSpawn()) {
         this.switchNode(resetBranch);
      } else {
         ArrayList<OrderedPair> nearbyCoordinates = this.pathFinder
            .getCoordinatesInRange(tileX, tileY, this.thisMobFlags.getCorruptionWorkNewBuildingDistance(), false, this.thisMobFlags.getBlockMapGroup());
         if (nearbyCoordinates.size() <= 0) {
            this.switchNode(resetBranch);
         } else {
            this.thisMob.setCurrentTask(Text.getText("aiTask.corruptionBuildingPlacement.create"));
            Collections.shuffle(nearbyCoordinates);
            HashMap<MapTilesLoader.TileSet, Integer> shuffledSpawnCreationTypes = this.corruption.getCorruptionBuildingTypes();
            ArrayList<OrderedPair> validCoordinates = new ArrayList<>();
            MapTilesLoader.TileSet spawnCreation = null;

            for (MapTilesLoader.TileSet t : shuffledSpawnCreationTypes.keySet()) {
               if (Utilities.randomInt(shuffledSpawnCreationTypes.get(t)) == 0) {
                  for (OrderedPair o : nearbyCoordinates) {
                     if (this.map.canPlaceObject(o.getX(), o.getY(), t, true) && this.corruption.isTileCorrupted(o.getX(), o.getY())) {
                        validCoordinates.add(o);
                     }
                  }

                  if (validCoordinates.size() > 0) {
                     spawnCreation = t;
                     break;
                  }
               }
            }

            if (spawnCreation != null && validCoordinates.size() > 0) {
               OrderedPair randomCoordinates = validCoordinates.get(Utilities.randomInt(validCoordinates.size()));
               ObjectBase newSpawn = this.map
                  .placeObject(randomCoordinates.getX(), randomCoordinates.getY(), spawnCreation, ObjectBase.ObjectSubType.CONSTRUCTION, true);
               ObjectFlags spawnFlag = newSpawn.getObjectFlags();
               EnumMap<ResourceModule.ResourceType, Integer> baseResources = spawnFlag.getResourceBaseValues();

               for (ResourceModule.ResourceType tx : baseResources.keySet()) {
                  if (baseResources.get(tx) > 0) {
                     ResourceBase r = this.resource.createResourceUnassigned(tx);
                     r.placeInBuilding(newSpawn);
                     newSpawn.increaseStores(r);
                     newSpawn.build(r, false, 0);
                  }
               }

               newSpawn.getObjectFlags().getCombobulatorSpawnTypes().add(this.thisMob.getMobType());
               this.switchNode(resetBranch);
            } else {
               this.switchNode(resetBranch);
            }
         }
      }
   }

   private boolean canCreateNewSpawn() {
      GameModeTemplateBase template = this.save.getActiveRegionalSave().getGameMode().getTemplate();
      ArrayList<ObjectBase> localObjects = this.object
         .getObjectsNearby(this.thisMob.getTileX(), this.thisMob.getTileY(), template.getCorruptionBuildingDensityRange(), MobBase.MobGroup.MONSTER);
      int totalLocalBuildings = localObjects.size();
      return totalLocalBuildings < template.getCorruptionBuildingDensity();
   }
}
