package rtr.road;

import rtr.map.MapTilesLoader;
import rtr.resources.ResourceModule;

public class LogPath extends RoadBase {
   @Override
   public void load() {
      this.roadTileSet = MapTilesLoader.TileSet.LOG_PATH;
      this.roadLevel = 1;
      this.resourceRequirements.add(ResourceModule.ResourceType.WOOD);
      this.nextRoadType = RoadModule.RoadType.COBBLE_AND_LOG_PATH;
      this.debrisType = RoadModule.RoadType.LOG_PATH_DEBRIS;
      this.debris = false;
      this.loaded = true;
   }
}
