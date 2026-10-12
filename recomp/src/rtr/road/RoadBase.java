package rtr.road;

import java.util.ArrayList;
import rtr.map.MapTilesLoader;
import rtr.resources.ResourceModule;

public class RoadBase {
   protected MapTilesLoader.TileSet roadTileSet;
   protected int roadLevel;
   protected ArrayList<ResourceModule.ResourceType> resourceRequirements = new ArrayList<>();
   protected RoadModule.RoadType nextRoadType;
   protected RoadModule.RoadType debrisType;
   protected boolean debris;
   protected boolean loaded;

   public void load() {
   }

   public MapTilesLoader.TileSet getRoadTileSet() {
      return this.roadTileSet;
   }

   public int getRoadLevel() {
      return this.roadLevel;
   }

   public ArrayList<ResourceModule.ResourceType> getResourceRequirements() {
      return this.resourceRequirements;
   }

   public RoadModule.RoadType getNextRoadType() {
      return this.nextRoadType;
   }

   public RoadModule.RoadType getDebrisType() {
      return this.debrisType;
   }

   public boolean isDebris() {
      return this.debris;
   }

   public boolean loaded() {
      return this.loaded;
   }
}
