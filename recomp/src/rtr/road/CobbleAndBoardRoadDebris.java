package rtr.road;

import rtr.map.MapTilesLoader;

public class CobbleAndBoardRoadDebris extends RoadBase {
   @Override
   public void load() {
      this.roadTileSet = MapTilesLoader.TileSet.COBBLE_AND_BOARD_ROAD_DEBRIS;
      this.roadLevel = 3;
      this.nextRoadType = RoadModule.RoadType.COBBLE_AND_BOARD_ROAD;
      this.debrisType = RoadModule.RoadType.NONE;
      this.debris = true;
      this.loaded = true;
   }
}
