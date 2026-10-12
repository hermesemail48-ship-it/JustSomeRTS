package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobCarpenter extends MobJobBase {
   public MobJobCarpenter() {
      this.mobJobName = Text.getText("mobJob.carpenter.single");
      this.mobJobNamePlural = Text.getText("mobJob.carpenter.plural");
      this.description = Text.getText("mobJob.carpenter.description");
      this.baseColor = new Color(-10805248);
      this.accentColor = new Color(-16733441);
      this.refineSpeedBase = 1000;
      this.buildingSpeedBase = 300;
      this.preferredTools.add(ResourceModule.ResourceType.AXE);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.BOARD);
      this.canWorkerRefine.add(ResourceModule.ResourceType.BOARD);
      this.canWorkerStore.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerStore.add(ResourceModule.ResourceType.BOARD);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.WOODY_TRASH);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.WOOD);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.BOARD);
   }
}
