package rtr.road;

import rtr.map.MapTilesLoader;

public class CutStoneAndBoardRoadDebris extends RoadBase {
   @Override
   public void load() {
      this.roadTileSet = MapTilesLoader.TileSet.CUT_STONE_AND_BOARD_ROAD_DEBRIS;
      this.roadLevel = 4;
      this.nextRoadType = RoadModule.RoadType.CUT_STONE_AND_BOARD_ROAD;
      this.debrisType = RoadModule.RoadType.NONE;
      this.debris = true;
      this.loaded = true;
   }
}
