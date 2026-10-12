package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobWayMaker extends MobJobBase {
   public MobJobWayMaker() {
      this.mobJobName = Text.getText("mobJob.wayMaker.single");
      this.mobJobNamePlural = Text.getText("mobJob.wayMaker.plural");
      this.description = Text.getText("mobJob.wayMaker.description");
      this.baseColor = new Color(-14540254);
      this.accentColor = new Color(-12303292);
      this.canRepairRoad = true;
      this.canClearTopography = true;
      this.canClearRoads = true;
      this.canDigHoles = true;
      this.clearingSpeedBase = 200;
      this.diggingSpeedBase = 800;
      this.buildingSpeedBase = 400;
      this.dismantlingSpeedBase = 200;
      this.repairSpeedBase = 200;
      this.preferredTools.add(ResourceModule.ResourceType.PICK_AXE);
      this.preferredTools.add(ResourceModule.ResourceType.HAMMER);
      this.preferredTools.add(ResourceModule.ResourceType.SHOVEL);
      this.canWorkerDeliverRoadConstruction.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerDeliverRoadConstruction.add(ResourceModule.ResourceType.BOARD);
      this.canWorkerDeliverRoadConstruction.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerDeliverRoadConstruction.add(ResourceModule.ResourceType.CUT_STONE);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerBuildRoad.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerBuildRoad.add(ResourceModule.ResourceType.BOARD);
      this.canWorkerBuildRoad.add(ResourceModule.ResourceType.ROCK);
      this.canWorkerBuildRoad.add(ResourceModule.ResourceType.CUT_STONE);
   }
}
