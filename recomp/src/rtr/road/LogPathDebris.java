package rtr.road;

import rtr.map.MapTilesLoader;

public class LogPathDebris extends RoadBase {
   @Override
   public void load() {
      this.roadTileSet = MapTilesLoader.TileSet.LOG_PATH_DEBRIS;
      this.roadLevel = 1;
      this.nextRoadType = RoadModule.RoadType.LOG_PATH;
      this.debrisType = RoadModule.RoadType.NONE;
      this.debris = true;
      this.loaded = true;
   }
}
