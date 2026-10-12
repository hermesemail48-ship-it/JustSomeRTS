package rtr.road;

import rtr.map.MapTilesLoader;
import rtr.resources.ResourceModule;

public class CobbleAndLogPath extends RoadBase {
   @Override
   public void load() {
      this.roadTileSet = MapTilesLoader.TileSet.COBBLE_AND_LOG_PATH;
      this.roadLevel = 2;
      this.resourceRequirements.add(ResourceModule.ResourceType.WOOD);
      this.resourceRequirements.add(ResourceModule.ResourceType.ROCK);
      this.nextRoadType = RoadModule.RoadType.COBBLE_AND_BOARD_ROAD;
      this.debrisType = RoadModule.RoadType.COBBLE_AND_LOG_PATH_DEBRIS;
      this.debris = false;
      this.loaded = true;
   }
}
