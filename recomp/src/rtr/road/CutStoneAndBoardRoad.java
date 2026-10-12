package rtr.road;

import rtr.map.MapTilesLoader;
import rtr.resources.ResourceModule;

public class CutStoneAndBoardRoad extends RoadBase {
   @Override
   public void load() {
      this.roadTileSet = MapTilesLoader.TileSet.CUT_STONE_AND_BOARD_ROAD;
      this.roadLevel = 4;
      this.resourceRequirements.add(ResourceModule.ResourceType.WOOD);
      this.resourceRequirements.add(ResourceModule.ResourceType.ROCK);
      this.resourceRequirements.add(ResourceModule.ResourceType.BOARD);
      this.resourceRequirements.add(ResourceModule.ResourceType.CUT_STONE);
      this.nextRoadType = null;
      this.debrisType = RoadModule.RoadType.CUT_STONE_AND_BOARD_ROAD_DEBRIS;
      this.debris = false;
      this.loaded = true;
   }
}
