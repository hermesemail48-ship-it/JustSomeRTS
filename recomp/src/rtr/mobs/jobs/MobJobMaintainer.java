package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobMaintainer extends MobJobBase {
   public MobJobMaintainer() {
      this.mobJobName = Text.getText("mobJob.maintainer.single");
      this.mobJobNamePlural = Text.getText("mobJob.maintainer.plural");
      this.description = Text.getText("mobJob.maintainer.description");
      this.baseColor = new Color(-16736475);
      this.accentColor = new Color(-12648292);
      this.canRepair = true;
      this.canRepairRoad = true;
      this.canDismantle = true;
      this.canClearTopography = true;
      this.canClearRoads = true;
      this.canDigHoles = true;
      this.clearingSpeedBase = 200;
      this.diggingSpeedBase = 800;
      this.dismantlingSpeedBase = 200;
      this.repairSpeedBase = 200;
      this.preferredTools.add(ResourceModule.ResourceType.AXE);
      this.preferredTools.add(ResourceModule.ResourceType.HAMMER);
      this.preferredTools.add(ResourceModule.ResourceType.PICK_AXE);
      this.preferredTools.add(ResourceModule.ResourceType.SHOVEL);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.CRYSTALLY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.ROCKY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
   }
}
