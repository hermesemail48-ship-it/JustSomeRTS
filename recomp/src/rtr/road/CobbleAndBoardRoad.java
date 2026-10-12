package rtr.road;

import rtr.map.MapTilesLoader;
import rtr.resources.ResourceModule;

public class CobbleAndBoardRoad extends RoadBase {
   @Override
   public void load() {
      this.roadTileSet = MapTilesLoader.TileSet.COBBLE_AND_BOARD_ROAD;
      this.roadLevel = 3;
      this.resourceRequirements.add(ResourceModule.ResourceType.WOOD);
      this.resourceRequirements.add(ResourceModule.ResourceType.ROCK);
      this.resourceRequirements.add(ResourceModule.ResourceType.BOARD);
      this.nextRoadType = RoadModule.RoadType.CUT_STONE_AND_BOARD_ROAD;
      this.debrisType = RoadModule.RoadType.COBBLE_AND_BOARD_ROAD_DEBRIS;
      this.debris = false;
      this.loaded = true;
   }
}
