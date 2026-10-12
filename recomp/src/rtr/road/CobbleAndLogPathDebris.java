package rtr.road;

import rtr.map.MapTilesLoader;

public class CobbleAndLogPathDebris extends RoadBase {
   @Override
   public void load() {
      this.roadTileSet = MapTilesLoader.TileSet.COBBLE_AND_LOG_PATH_DEBRIS;
      this.roadLevel = 2;
      this.nextRoadType = RoadModule.RoadType.COBBLE_AND_LOG_PATH;
      this.debrisType = RoadModule.RoadType.NONE;
      this.debris = true;
      this.loaded = true;
   }
}
