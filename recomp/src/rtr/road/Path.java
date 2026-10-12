package rtr.road;

import rtr.map.MapTilesLoader;

public class Path extends RoadBase {
   @Override
   public void load() {
      this.roadTileSet = MapTilesLoader.TileSet.PATH;
      this.roadLevel = 0;
      this.nextRoadType = RoadModule.RoadType.LOG_PATH;
      this.debrisType = RoadModule.RoadType.NONE;
      this.debris = false;
      this.loaded = true;
   }
}
