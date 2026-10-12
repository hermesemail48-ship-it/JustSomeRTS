package rtr.road;

public class None extends RoadBase {
   @Override
   public void load() {
      this.nextRoadType = RoadModule.RoadType.PATH;
      this.roadLevel = -1;
      this.debris = false;
      this.loaded = true;
   }
}
